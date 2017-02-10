package cn.yescallop.qrcode;

public enum Rotation {

    NORTH(0), WEST(90), SOUTH(180), EAST(270);

    private int angle;

    Rotation(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public int distance(Rotation rotation) {
        int distance = rotation.angle - this.angle;
        return distance == -180 ? 180 : (distance % 180);
    }

    public Rotation getNextRotation() {
        switch (this) {
            case NORTH:
                return WEST;
            case WEST:
                return SOUTH;
            case SOUTH:
                return EAST;
            case EAST:
                return NORTH;
        }
        return null;
    }
}
