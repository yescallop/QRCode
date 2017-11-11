package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.nukkit.block.BlockWool;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.qrcode.Language;
import cn.yescallop.qrcode.api.MinecraftQRCode;
import cn.yescallop.qrcode.api.QRCodeManager;
import cn.yescallop.qrcode.command.QRCodeCommand;
import cn.yescallop.qrcode.command.SubCommand;
import com.google.zxing.WriterException;

import java.util.stream.Stream;

public class NewCommand extends SubCommand {

    public NewCommand() {
        super("new");
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        if (args.length == 0) {
            this.sendUsage(player);
            return false;
        }
        if (QRCodeManager.has(player)) {
            player.sendMessage(Language.translate("commands.new.fail"));
            return false;
        }
        MinecraftQRCode qrCode;
        try {
            qrCode = new MinecraftQRCode.Builder()
                    .foreground(new BlockWool(DyeColor.BLACK))
                    .background(new BlockWool(DyeColor.WHITE))
                    .content(Stream.of(args).reduce((a, b) -> a + " " + b).get())
                    .orientation(MinecraftQRCode.Orientation.SOUTH_UP)
                    .build();
        } catch (WriterException e) {
            QRCodeCommand.getPlugin().getLogger().error("Exception caught while creating QR code", e);
            player.sendMessage(new TextContainer(TextFormat.RED + "%commands.generic.exception"));
            return false;
        }
        QRCodeManager.add(player, qrCode);
        player.sendMessage(Language.translate("commands.new.success"));
        return true;
    }
}
