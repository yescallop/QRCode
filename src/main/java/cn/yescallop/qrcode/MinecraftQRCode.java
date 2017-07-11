package cn.yescallop.qrcode;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.utils.PluginException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MinecraftQRCode {

    private Level level;
    private Vector3 pos;
    private Orientation orientation;
    private Block foreground;
    private Block background;
    private boolean turned = false;
    private int magnifier = 1;
    private int borderSize = 1;
    private QRCodeMatrix matrix;

    private Map<Vector3, Boolean> area;
    private boolean previewing = false;
    private boolean valid = true;

    private MinecraftQRCode() {
    }

    public boolean isValid() {
        return valid;
    }

    public MinecraftQRCode place() {
        if (!valid) {
            throw new PluginException("Attempt to place an invalid QR code");
        }
        area.forEach((v, b) -> level.setBlock(v, b ? foreground : background));
        UpdateBlockPacket[] pks = area.keySet().stream().map(v -> {
            Block block = area.get(v) ? foreground : background;
            UpdateBlockPacket pk = new UpdateBlockPacket();
            pk.x = (int) v.x;
            pk.y = (int) v.y;
            pk.z = (int) v.z;
            pk.blockId = block.getId();
            pk.blockData = block.getDamage();
            pk.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
            return pk;
        }).toArray(UpdateBlockPacket[]::new);
        sendBlocks(pks);
        valid = false;
        previewing = false;
        return this;
    }

    public Orientation orientation() {
        return orientation;
    }

    public MinecraftQRCode orientation(Orientation o) {
        if (o != this.orientation) {
            this.orientation = o;
            calculateArea();
        }
        return this;
    }

    public MinecraftQRCode rotate() {
        matrix.rotate();
        calculateArea();
        return this;
    }

    public MinecraftQRCode rotateCCW() {
        matrix.rotateCCW();
        calculateArea();
        return this;
    }

    public Rotation rotation() {
        return matrix.rotation();
    }

    public MinecraftQRCode rotation(Rotation rotation) {
        if (rotation != matrix.rotation()) {
            matrix.rotation(rotation);
            calculateArea();
        }
        return this;
    }

    public int mgnifier() {
        return magnifier;
    }

    public MinecraftQRCode magnifier(int magnifier) {
        if (magnifier != this.magnifier) {
            this.magnifier = magnifier;
            calculateArea();
        }
        return this;
    }

    public int borderSize() {
        return borderSize;
    }

    public MinecraftQRCode borderSize(int size) {
        if (size != this.borderSize) {
            this.borderSize = size;
            calculateArea();
        }
        return this;
    }

    public boolean isTurned() {
        return turned;
    }

    public MinecraftQRCode turn() {
        this.turned = !this.turned;
        calculateArea();
        return this;
    }

    public boolean isPreviewing() {
        return previewing;
    }

    public void preview() {
        if (!valid) {
            throw new PluginException("Attempt to preview an invalid QR code");
        }
        UpdateBlockPacket[] pks = area.keySet().stream().map(v -> {
            Block block = area.get(v) ? foreground : background;
            UpdateBlockPacket pk = new UpdateBlockPacket();
            pk.x = (int) v.x;
            pk.y = (int) v.y;
            pk.z = (int) v.z;
            pk.blockId = block.getId();
            pk.blockData = block.getDamage();
            pk.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
            return pk;
        }).toArray(UpdateBlockPacket[]::new);
        sendBlocks(pks);
        previewing = true;
    }

    public void undoPreview() {
        if (!valid || !previewing) return;
        UpdateBlockPacket[] pks = area.keySet().stream().map(v -> {
            int fullBlock = level.getFullBlock((int) v.x, (int) v.y, (int) v.z);
            UpdateBlockPacket pk = new UpdateBlockPacket();
            pk.x = (int) v.x;
            pk.y = (int) v.y;
            pk.z = (int) v.z;
            pk.blockId = fullBlock >> 4;
            pk.blockData = fullBlock & 0xf;
            pk.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
            return pk;
        }).toArray(UpdateBlockPacket[]::new);
        sendBlocks(pks);
        previewing = false;
    }

    private void sendBlocks(UpdateBlockPacket[] pks) {
        Player[] players = Stream.of(pks)
                .map(pk -> Level.chunkHash(pk.x >> 4, pk.z >> 4))
                .distinct()
                .flatMap(hash -> level.getChunkPlayers((int) (hash >> 32), hash.intValue()).values().stream())
                .distinct()
                .toArray(Player[]::new);
        Server.getInstance().batchPackets(players, pks);
    }

    private void calculateArea() { //Minecraft uses right-handed coordinate system
        boolean previewing = this.previewing;
        undoPreview();
        area = new HashMap<>();
        QRCodeMatrix matrix = this.matrix.magnify(magnifier).border(borderSize);
        if (turned) {
            matrix = matrix.turnVertically();
        }
        int n = matrix.size() - 1;
        switch (orientation) {
            //Horizontal
            case EAST_NORTH:
                matrix.forEach((x, y, b) -> area.put(pos.add(x, 0, y - n), b));
                break;
            case WEST_NORTH:
                matrix.forEach((x, y, b) -> area.put(pos.add(x - n, 0, y - n), b));
                break;
            case WEST_SOUTH:
                matrix.forEach((x, y, b) -> area.put(pos.add(x - n, 0, y), b));
                break;
            case EAST_SOUTH:
                matrix.forEach((x, y, b) -> area.put(pos.add(x, 0, y), b));
                break;
            //Vertical
            case NORTH_UP:
                matrix.forEach((x, y, b) -> area.put(pos.add(0, n - y, -x), b));
                break;
            case NORTH_DOWN:
                matrix.forEach((x, y, b) -> area.put(pos.add(0, -y, -x), b));
                break;
            case SOUTH_UP:
                matrix.forEach((x, y, b) -> area.put(pos.add(0, n - y, x), b));
                break;
            case SOUTH_DOWN:
                matrix.forEach((x, y, b) -> area.put(pos.add(0, -y, x), b));
                break;
            case EAST_UP:
                matrix.forEach((x, y, b) -> area.put(pos.add(x, n - y, 0), b));
                break;
            case EAST_DOWN:
                matrix.forEach((x, y, b) -> area.put(pos.add(x, -y, 0), b));
                break;
            case WEST_UP:
                matrix.forEach((x, y, b) -> area.put(pos.add(-x, n - y, 0), b));
                break;
            case WEST_DOWN:
                matrix.forEach((x, y, b) -> area.put(pos.add(-x, -y, 0), b));
                break;
        }
        valid = area.keySet().stream().map(v -> v.y).max(Double::compareTo).get() < 255;
        if (valid && previewing) preview();
    }

    public Map<Vector3, Boolean> area() {
        return area;
    }

    public enum Orientation {
        EAST_NORTH,
        WEST_NORTH,
        WEST_SOUTH,
        EAST_SOUTH,
        NORTH_UP,
        NORTH_DOWN,
        SOUTH_UP,
        SOUTH_DOWN,
        WEST_UP,
        WEST_DOWN,
        EAST_UP,
        EAST_DOWN
    }

    public static class Builder {

        private final MinecraftQRCode qrCode = new MinecraftQRCode();

        public Builder at(Position pos) {
            qrCode.level = pos.level;
            qrCode.pos = pos;
            return this;
        }

        public Builder at(Level level, Vector3 pos) {
            qrCode.level = level;
            qrCode.pos = pos;
            return this;
        }

        public Builder orientation(Orientation orientation) {
            qrCode.orientation = orientation;
            return this;
        }

        public Builder foregroundBlock(Block block) {
            qrCode.foreground = block;
            return this;
        }

        public Builder backgroundBlock(Block block) {
            qrCode.background = block;
            return this;
        }

        public Builder turned(boolean turned) {
            qrCode.turned = turned;
            return this;
        }

        public Builder magnifier(int magnifier) {
            if (magnifier < 0) {
                throw new IllegalArgumentException("magnifier cannnot be negative!");
            }
            qrCode.magnifier = magnifier;
            return this;
        }

        public Builder borderSize(int size) {
            if (size < 0) {
                throw new IllegalArgumentException("border size cannnot be negative!");
            }
            qrCode.borderSize = size;
            return this;
        }

        public Builder content(QRCodeMatrix matrix) {
            qrCode.matrix = matrix;
            return this;
        }

        public MinecraftQRCode build() {
            qrCode.calculateArea();
            return qrCode;
        }
    }
}
