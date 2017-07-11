package cn.yescallop.qrcode;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;

import java.util.HashMap;
import java.util.Map;

public class QRCodeMatrix implements Cloneable {

    private final boolean[][] matrix;
    private final int size;
    private Rotation rotation;

    public QRCodeMatrix(boolean[][] matrix) {
        this(matrix, Rotation.NORTH);
    }

    public QRCodeMatrix(boolean[][] matrix, Rotation rotation) {
        if (!isValidQRCodeMatrix(matrix)) {
            throw new IllegalArgumentException("matrix is not a valid QR code matrix!");
        }
        this.matrix = deepClone(matrix);
        this.rotation = rotation;
        this.size = matrix.length;
    }

    public QRCodeMatrix(ByteMatrix byteMatrix) {
        this(byteMatrix, Rotation.NORTH);
    }

    public QRCodeMatrix(ByteMatrix byteMatrix, Rotation rotation) {
        if (byteMatrix.getHeight() != byteMatrix.getWidth()) {
            throw new IllegalArgumentException("matrix must be square!");
        }
        this.size = byteMatrix.getWidth();
        this.rotation = rotation;
        this.matrix = new boolean[size][size];
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

    private static boolean isValidQRCodeMatrix(boolean[][] arr) {
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

    public QRCodeMatrix rotate() {
        return rotation(rotation.rotate());
    }

    public QRCodeMatrix rotateCCW() {
        return rotation(rotation.rotateCCW());
    }

    public Rotation rotation() {
        return rotation;
    }

    public QRCodeMatrix rotation(Rotation rotation) {
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

    public QRCodeMatrix turnHorizontally() {
        boolean[][] result = deepClone(this.matrix);
        for (int x = 0; x < size; x++) {
            System.arraycopy(matrix[x], 0, result[size - 1 - x], 0, size);
        }
        return new QRCodeMatrix(result, rotation);
    }

    public QRCodeMatrix turnVertically() {
        boolean[][] result = deepClone(this.matrix);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                result[x][size - 1 - y] = matrix[x][y];
            }
        }
        return new QRCodeMatrix(result, rotation);
    }

    public QRCodeMatrix magnify(int m) {
        if (m == 1) {
            return this;
        }
        boolean[][] result = new boolean[size * m][size * m];
        for (int x = 0; x < size; x++) {
            int j = x * m;
            for (int y = 0; y < size; y++) {
                int k = y * m;
                result[j][k] = matrix[x][y];
                for (int i = 1; i <= m; i++) {
                    result[j][k] = result[j + i][k] = result[j][k + i] = result[j + i][k + i] = matrix[x][y];
                }
            }
        }
        return new QRCodeMatrix(result, rotation);
    }

    public QRCodeMatrix border(int s) {
        if (s == 0) {
            return this;
        }
        boolean[][] result = new boolean[size + s][size + s];
        for (int x = 0; x < size; x++) {
            System.arraycopy(result[x], 0, result[x + s], s, size);
        }
        return new QRCodeMatrix(result, rotation);
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

    @Override
    public QRCodeMatrix clone() {
        return new QRCodeMatrix(matrix, rotation);
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

        public Builder charset(CharacterSetECI charset) {
            this.charset = charset.name();
            return this;
        }

        public Builder charset(String charset) {
            this.charset = charset;
            return this;
        }

        public QRCodeMatrix build() throws WriterException {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, charset);
            return new QRCodeMatrix(Encoder.encode(content, ecLevel, hints).getMatrix());
        }
    }
}
