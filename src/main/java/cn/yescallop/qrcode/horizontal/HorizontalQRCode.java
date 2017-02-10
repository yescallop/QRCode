package cn.yescallop.qrcode.horizontal;

import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.yescallop.qrcode.QRCode;
import cn.yescallop.qrcode.Rotation;
import com.google.zxing.WriterException;

public class HorizontalQRCode extends QRCode {

    private HorizontalDirection direction;

    private HorizontalQRCode() {
    }

    @Override
    protected void calculateArea() {
        final int n = matrix.size() - 1;
        switch (direction) {
            case EAST_NORTH:
                matrix.forEach((x, y, b) -> area.put(pos.add(x, 0, n - y), b));
            case WEST_NORTH:
                matrix.forEach((x, y, b) -> area.put(pos.add(x - n, 0, n - y), b));
            case WEST_SOUTH:
                matrix.forEach((x, y, b) -> area.put(pos.add(x - n, 0, y - n), b));
            case EAST_SOUTH:
                matrix.forEach((x, y, b) -> area.put(pos.add(x, 0, y - n), b));
        }
    }

    public static class Builder {

        private final HorizontalQRCode qrCode = new HorizontalQRCode();

        public Builder at(Level level, Vector3 pos) {
            qrCode.level = level;
            qrCode.pos = pos;
            return this;
        }

        public Builder direction(HorizontalDirection direction) {
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

        public HorizontalQRCode build() throws WriterException {
            qrCode.calculate();
            return qrCode;
        }
    }
}
