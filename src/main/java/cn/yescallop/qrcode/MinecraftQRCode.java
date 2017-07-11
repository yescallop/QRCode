package cn.yescallop.qrcode;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.UpdateBlockPacket;

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
    private boolean placed = false;

    private MinecraftQRCode() {
    }

    public boolean isPlaced() {
        return placed;
    }

    public void checkPlaced() {
        if (placed) {
            throw new RuntimeException("QR code has been placed");
        }
    }

    public MinecraftQRCode place() {
        checkPlaced();
        area.forEach((v, b) -> level.setBlock(v, b ? foreground : background));
        placed = true;
        previewing = false;
        return this;
    }

    public Orientation orientation() {
        return orientation;
    }

    public MinecraftQRCode orientation(Orientation o) {
        checkPlaced();
        if (o != this.orientation) {
            this.orientation = o;
            calculateArea();
            refreshPreview();
        }
        return this;
    }

    public MinecraftQRCode rotate() {
        checkPlaced();
        matrix.rotate();
        calculateArea();
        refreshPreview(false);
        return this;
    }

    public MinecraftQRCode rotateCCW() {
        checkPlaced();
        matrix.rotateCCW();
        calculateArea();
        refreshPreview(false);
        return this;
    }

    public Rotation rotation() {
        return matrix.rotation();
    }

    public MinecraftQRCode rotation(Rotation rotation) {
        checkPlaced();
        if (rotation != matrix.rotation()) {
            matrix.rotation(rotation);
            calculateArea();
            refreshPreview(false);
        }
        return this;
    }

    public int mgnifier() {
        return magnifier;
    }

    public MinecraftQRCode magnifier(int magnifier) {
        checkPlaced();
        if (magnifier != this.magnifier) {
            this.magnifier = magnifier;
            calculateArea();
            refreshPreview();
        }
        return this;
    }

    public int borderSize() {
        return borderSize;
    }

    public MinecraftQRCode borderSize(int size) {
        checkPlaced();
        if (size != this.borderSize) {
            this.borderSize = size;
            calculateArea();
            refreshPreview();
        }
        return this;
    }

    public boolean isTurned() {
        return turned;
    }

    public MinecraftQRCode turn() {
        checkPlaced();
        this.turned = !this.turned;
        calculateArea();
        refreshPreview(false);
        return this;
    }

    public boolean isPreviewing() {
        return previewing;
    }

    public void preview() {
        checkPlaced();
        Stream<UpdateBlockPacket> stream = area.keySet().stream().map(v -> {
            Block block = area.get(v) ? foreground : background;
            UpdateBlockPacket pk = new UpdateBlockPacket();
            pk.x = (int) v.x;
            pk.y = (int) v.y;
            pk.z = (int) v.z;
            pk.blockId = block.getId();
            pk.blockData = block.getDamage();
            pk.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
            return pk;
        });
        Player[] players = stream.flatMap(pk -> level.getChunkPlayers(pk.x >> 4, pk.y >> 4).values().stream())
                .toArray(Player[]::new);
        Server.getInstance().batchPackets(players, stream.toArray(UpdateBlockPacket[]::new));
        previewing = true;
    }

    public void undoPreview() {
        checkPlaced();
        Stream<UpdateBlockPacket> stream = area.keySet().stream().map(v -> {
            UpdateBlockPacket pk = new UpdateBlockPacket();
            pk.x = (int) v.x;
            pk.y = (int) v.y;
            pk.z = (int) v.z;
            pk.blockId = level.getBlockIdAt((int) v.x, (int) v.y, (int) v.z);
            pk.blockData = level.getBlockDataAt((int) v.x, (int) v.y, (int) v.z);
            pk.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
            return pk;
        });
        Player[] players = stream.flatMap(pk -> level.getChunkPlayers(pk.x >> 4, pk.y >> 4).values().stream())
                .toArray(Player[]::new);
        Server.getInstance().batchPackets(players, stream.toArray(UpdateBlockPacket[]::new));
        previewing = false;
    }

    public void refreshPreview() {
        refreshPreview(true);
    }

    public void refreshPreview(boolean forced) {
        if (previewing) {
            if (forced) undoPreview();
            preview();
        }
    }

    private void calculateArea() {
        area = new HashMap<>();
        QRCodeMatrix matrix = this.matrix.magnify(magnifier).border(borderSize);
        if (turned) {
            matrix = matrix.turnVertically();
        }
        switch (orientation) {
            //TODO: calculate
        }
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