package cn.yescallop.qrcode;

import cn.nukkit.level.Position;

public class QRCodeCreator {

    private Position pos;
    private Orientation orientation;
    private Matrix matrix;

    //TODO: APIs

    private QRCodeCreator() {
    }

    public static class Builder {

        private final QRCodeCreator creator = new QRCodeCreator();

        public Builder at(Position pos) {
            creator.pos = pos;
            return this;
        }

        public Builder orientation(Orientation orientation) {
            creator.orientation = orientation;
            return this;
        }


        public Builder content(Matrix matrix) {
            creator.matrix = matrix;
            return this;
        }
    }
}
