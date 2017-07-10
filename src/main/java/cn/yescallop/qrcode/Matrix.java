package cn.yescallop.qrcode;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Matrix {

    private final boolean[][] matrix;
    private final int size;
    private Rotation rotation = Rotation.NORTH;

    public Matrix(boolean[][] matrix) {
        if (!isValidMatrix(matrix)) {
            throw new IllegalArgumentException("matrix must be square!");
        }
        this.matrix = deepClone(matrix);
        this.size = matrix.length;
    }

    public Matrix(ByteMatrix byteMatrix) {
        if (byteMatrix.getHeight() != byteMatrix.getWidth()) {
            throw new IllegalArgumentException("matrix must be square!");
        }
        size = byteMatrix.getWidth();
        matrix = new boolean[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                matrix[x][y] = byteMatrix.get(x, y) == 0x01;
            }
        }
    }

    private static boolean[][] deepClone(boolean[][] matrix) {
        int size = matrix.length;
        boolean[][] result = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(matrix[i], 0, result[i], 0, size);
        }
        return result;
    }

    private static boolean isValidMatrix(boolean[][] arr) {
        int len = arr.length;
        for (boolean[] i : arr) {
            if (i.length != len) {
                return false;
            }
        }
        return true;
    }

    public boolean[][] array() {
        return matrix;
    }

    public Matrix rotation(Rotation rotation) {
        if (this.rotation == rotation) {
            return this;
        }
        boolean[][] raw = deepClone(this.matrix);
        switch (this.rotation.distance(rotation)) {
            case 90:
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size; y++) {
                        matrix[y][size - 1 - x] = raw[x][y];
                    }
                }
                break;
            case -90:
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size; y++) {
                        matrix[x][y] = raw[y][size - 1 - x];
                    }
                }
                break;
            case 180:
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size; y++) {
                        matrix[size - 1 - x][size - 1 - y] = raw[x][y];
                    }
                }
                break;
        }
        this.rotation = rotation;
        return this;
    }

    public boolean get(int x, int y) {
        return matrix[x][y];
    }

    public int size() {
        return size;
    }

    public void forEach(Consumer action) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                action.accept(x, y, matrix[x][y]);
            }
        }
    }

    @FunctionalInterface
    public interface Consumer {
        void accept(int x, int y, boolean b);
    }

    public static class Builder {

        private String content;
        private ErrorCorrectionLevel ecLevel = ErrorCorrectionLevel.L;
        private String charset = "UTF-8";
        private Rotation rotation = Rotation.NORTH;

        private Builder(String content) {
            this.content = content;
        }

        public static Builder forContent(String content) {
            return new Builder(content);
        }

        public Builder errorCorrectionLevel(ErrorCorrectionLevel ecLevel) {
            this.ecLevel = ecLevel;
            return this;
        }

        public Builder charset(StandardCharsets charset) {
            this.charset = charset.toString();
            return this;
        }

        public Builder charset(String charset) {
            this.charset = charset;
            return this;
        }

        public Matrix build() throws WriterException {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, charset);
            return new Matrix(Encoder.encode(content, ecLevel, hints).getMatrix());
        }
    }
}
