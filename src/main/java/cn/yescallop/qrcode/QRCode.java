package cn.yescallop.qrcode;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;

import java.util.HashMap;
import java.util.Map;

public abstract class QRCode {

    protected Level level;
    protected Vector3 pos;
    protected Rotation rotation = Rotation.NORTH;
    protected String content;

    protected QRCodeMatrix matrix;
    protected Map<Vector3, Boolean> area = new HashMap<>();
    protected boolean valid;

    protected void calculate() throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        ByteMatrix byteMatrix = Encoder.encode(content, ErrorCorrectionLevel.L, hints).getMatrix();
        matrix = new QRCodeMatrix(byteMatrix).rotate(this.rotation);
        calculateArea();
    }

    protected abstract void calculateArea();

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
        area.keySet().forEach(v -> level.setBlock(v, new BlockAir()));
    }

    public boolean valid() {
        return valid;
    }

    public boolean safe() {
        return area.keySet().stream().allMatch(v -> level.getBlockIdAt((int) v.x, (int) v.y, (int) v.z) == 0);
    }
}
