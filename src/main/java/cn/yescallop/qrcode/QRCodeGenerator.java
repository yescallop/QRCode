package cn.yescallop.qrcode;

import cn.nukkit.level.Level;
import cn.nukkit.math.BlockVector3;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRCodeGenerator {

    protected Level level;
    protected BlockVector3 pos;
    protected Direction direction;
    private Rotation rotation;
    private String content;

    protected QRCodeMatrix matrix;
    private List<BlockVector3> list;
    private boolean valid;

    private QRCodeGenerator(Builder builder) throws WriterException {
        this.level = builder.level;
        this.pos = builder.pos;
        this.direction = builder.direction;
        this.rotation = builder.rotation;
        this.content = builder.content;
        calculate();
    }

    private QRCodeGenerator calculate() throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        ByteMatrix byteMatrix = Encoder.encode(content, ErrorCorrectionLevel.L, hints).getMatrix();
        matrix = new QRCodeMatrix(byteMatrix).rotate(this.rotation);
        return this;
    }

    public boolean valid() {
        return valid;
    }

    public boolean safe() {
        return list.stream().allMatch(v -> level.getBlockIdAt(v.x, v.y, v.z) == 0);
    }

    public QRCode generate() {
        if (!this.valid) {
            return null;
        }
        return new QRCode(this);
    }

    public static class Builder {

        private Level level;
        private BlockVector3 pos;
        private Direction direction;
        private Rotation rotation;
        private String content;

        public Builder at(Level level, BlockVector3 pos) {
            this.level = level;
            this.pos = pos;
            return this;
        }

        public Builder direction(Direction direction) {
            this.direction = direction;
            return this;
        }

        public Builder rotation(Rotation rotation) {
            this.rotation = rotation;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public QRCodeGenerator build() throws WriterException {
            return new QRCodeGenerator(this);
        }
    }
}
