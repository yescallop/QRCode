package cn.yescallop.qrcode;

import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.yescallop.qrcode.enumeration.Direction;
import cn.yescallop.qrcode.enumeration.Orientation;
import cn.yescallop.qrcode.enumeration.Rotation;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRCode {

    private Position pos;
    private Orientation orientation;
    private Direction direction;
    private Rotation rotation = Rotation.Up;
    private String content;

    private List<Vector3> list;

    public QRCode at(@NotNull Position pos) {
        this.pos = pos;
        return this;
    }

    public QRCode orientation(@NotNull Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public QRCode direction(@NotNull Direction direction) {
        this.direction = direction;
        return this;
    }

    public QRCode rotation(@NotNull Rotation rotation) {
        this.rotation = rotation;
        return this;
    }

    public QRCode content(@NotNull String content) {
        this.content = content;
        return this;
    }

    public QRCode generate() throws WriterException {
        if (this.pos == null || this.orientation == null || this.direction == null || this.content == null) {
            throw new IllegalArgumentException("Pos, orientation, direction and content cannot be null!");
        }
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        ByteMatrix byteMatrix = Encoder.encode(content, ErrorCorrectionLevel.L, hints).getMatrix();
        boolean[][] matrix = new boolean[byteMatrix.getWidth()][byteMatrix.getHeight()];
        rotateMatrix(matrix, rotation);
        return this;
    }

    //private static List<Vector3> generateLandscape(Vector3 pos, boolean[][] matrix)

    private static void rotateMatrix(boolean[][] matrix, Rotation rotation) {
        if (rotation == Rotation.Up) {
            return;
        }
        boolean[][] raw = matrix.clone();
        int len = raw.length;
        switch (rotation) {
            case Down:
                for (int x = 0; x < len; x++) {
                    for (int y = 0; y < len; y++) {
                        matrix[len - 1 - x][len - 1 - y] = raw[x][y];
                    }
                }
                break;
            case Left:
                for (int x = 0; x < len; x++) {
                    for (int y = 0; y < len; x++) {
                        matrix[x][y] = raw[y][len - 1 - x];
                    }
                }
                break;
            case Right:
                for (int x = 0; x < len; x++) {
                    for (int y = 0; y < len; x++) {
                        matrix[y][len - 1 - x] = raw[x][y];
                    }
                }
                break;
        }
    }
}
