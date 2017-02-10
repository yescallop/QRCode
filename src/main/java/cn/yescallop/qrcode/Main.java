package cn.yescallop.qrcode;

import cn.nukkit.level.Level;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.plugin.PluginBase;
import com.google.zxing.WriterException;

public class Main extends PluginBase {

    @Override
    public void onEnable() {

    }

    public boolean example() {
        QRCodeGenerator generator;
        Level level = null;
        try {
            generator = new QRCodeGenerator.Builder()
                    .at(level, new BlockVector3(128, 64, 128))
                    .direction(Direction.NORTH_UP)
                    .rotation(Rotation.NORTH)
                    .content("Fuck")
                    .build();
        } catch (WriterException e) {
            e.printStackTrace();
            return false;
        }
        if (!generator.valid() || !generator.safe()) {
            return false;
        }
        QRCode qrCode = generator.generate();
        return false;
    }
}
