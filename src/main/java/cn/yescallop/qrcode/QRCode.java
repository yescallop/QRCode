package cn.yescallop.qrcode;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public final class QRCode {

    private Level level;
    private Vector3 pos;
    private Direction direction;
    private Rotation rotation = Rotation.NORTH;
    private String content;
    private String encoding = "UTF-8";
    private ErrorCorrectionLevel ecLevel = ErrorCorrectionLevel.L;

    private Matrix matrix;
    private Map<Vector3, Boolean> area = new HashMap<>();
    private boolean valid;

    private QRCode() {
    }

    private void calculate() throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, encoding);
        ByteMatrix byteMatrix = Encoder.encode(content, ecLevel, hints).getMatrix();
        matrix = new Matrix(byteMatrix).rotate(this.rotation);
        calculateArea();
    }

    private void calculateArea() {
        area.clear();
        final int n = matrix.size() - 1;
        switch (direction) {
            // Horizontal
            case EAST_NORTH:
                matrix.forEach((x, y, b) -> area.put(pos.add(x, 0, n - y), b));
                break;
            case WEST_NORTH:
                matrix.forEach((x, y, b) -> area.put(pos.add(x - n, 0, n - y), b));
                break;
            case WEST_SOUTH:
                matrix.forEach((x, y, b) -> area.put(pos.add(x - n, 0, y - n), b));
                break;
            case EAST_SOUTH:
                matrix.forEach((x, y, b) -> area.put(pos.add(x, 0, y - n), b));
                break;
            // Vertical
            case NORTH_UP:
                matrix.forEach((x, y, b) -> area.put(pos.add(0, x, n - y), b));
                break;
            case SOUTH_UP:
                matrix.forEach((x, y, b) -> area.put(pos.add(0, x - n, n - y), b));
                break;
            case SOUTH_DOWN:
                matrix.forEach((x, y, b) -> area.put(pos.add(0, x - n, y - n), b));
                break;
            case NORTH_DOWN:
                matrix.forEach((x, y, b) -> area.put(pos.add(0, x, y - n), b));
                break;
            case WEST_UP:
                matrix.forEach((x, y, b) -> area.put(pos.add(x, n - y, 0), b));
                break;
            case EAST_UP:
                matrix.forEach((x, y, b) -> area.put(pos.add(x - n, n - y, 0), b));
                break;
            case EAST_DOWN:
                matrix.forEach((x, y, b) -> area.put(pos.add(x - n, y - n, 0), b));
                break;
            case WEST_DOWN:
                matrix.forEach((x, y, b) -> area.put(pos.add(x, y - n, 0), b));
                break;
        }
    }

    public Direction direction() {
        return direction;
    }

    public void rotate(Rotation rotation) {
        this.rotation = rotation;
        matrix.rotate(rotation);
    }

    public Rotation rotation() {
        return rotation;
    }

    public void generate(Block paint, Block background) {
        area.forEach((v, b) -> level.setBlock(v, b ? paint : background));
    }

    public void revert() {
        Block air = new BlockAir();
        area.keySet().forEach(v -> level.setBlock(v, air));
    }

    public boolean valid() {
        return valid;
    }

    public boolean safe() {
        return area.keySet().stream().allMatch(v -> level.getBlockIdAt((int) v.x, (int) v.y, (int) v.z) == 0);
    }

    public Map<Vector3, Boolean> getArea() {
        return area;
    }

    public static class Builder {

        private final QRCode qrCode = new QRCode();

        public Builder at(Level level, Vector3 pos) {
            qrCode.level = level;
            qrCode.pos = pos;
            return this;
        }

        public Builder direction(Direction direction) {
            qrCode.direction = direction;
            return this;
        }

        public Builder rotation(Rotation rotation) {
            qrCode.rotation = rotation;
            return this;
        }

        public Builder content(String content) {
            qrCode.content = content;
            return this;
        }

        public Builder encoding(CharacterSetECI encoding) {
            qrCode.encoding = encoding.name();
            return this;
        }

        public Builder encoding(Charset encoding) {
            qrCode.encoding = encoding.name();
            return this;
        }

        public Builder encoding(String encoding) {
            qrCode.encoding = encoding;
            return this;
        }

        public Builder errorCorrectionLevel(ErrorCorrectionLevel ecLevel) {
            qrCode.ecLevel = ecLevel;
            return this;
        }

        public QRCode build() throws WriterException {
            qrCode.calculate();
            return qrCode;
        }
    }
}
