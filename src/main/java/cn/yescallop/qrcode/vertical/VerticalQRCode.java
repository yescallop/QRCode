package cn.yescallop.qrcode.vertical;

import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.yescallop.qrcode.QRCode;
import cn.yescallop.qrcode.Rotation;
import com.google.zxing.WriterException;

public class VerticalQRCode extends QRCode {

    private VerticalDirection direction;

    private VerticalQRCode() {
    }

    @Override
    protected void calculateArea() {
        final int n = matrix.size() - 1;
        switch (direction) {
            case NORTH_UP:
                matrix.forEach((x, y, b) -> area.put(pos.add(0, x, n - y), b));
            case SOUTH_UP:
                matrix.forEach((x, y, b) -> area.put(pos.add(0, x - n, n - y), b));
            case SOUTH_DOWN:
                matrix.forEach((x, y, b) -> area.put(pos.add(0, x - n, y - n), b));
            case NORTH_DOWN:
                matrix.forEach((x, y, b) -> area.put(pos.add(0, x, y - n), b));
            case WEST_UP:
                matrix.forEach((x, y, b) -> area.put(pos.add(x, n - y, 0), b));
            case EAST_UP:
                matrix.forEach((x, y, b) -> area.put(pos.add(x - n, n - y, 0), b));
            case EAST_DOWN:
                matrix.forEach((x, y, b) -> area.put(pos.add(x - n, y - n, 0), b));
            case WEST_DOWN:
                matrix.forEach((x, y, b) -> area.put(pos.add(x, y - n, 0), b));
        }
    }

    public static class Builder {

        private final VerticalQRCode qrCode = new VerticalQRCode();

        public Builder at(Level level, Vector3 pos) {
            qrCode.level = level;
            qrCode.pos = pos;
            return this;
        }

        public Builder direction(VerticalDirection direction) {
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

        public VerticalQRCode build() throws WriterException {
            qrCode.calculate();
            return qrCode;
        }
    }
}
