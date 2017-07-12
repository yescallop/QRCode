package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.yescallop.qrcode.QRCodeManager;
import cn.yescallop.qrcode.api.MinecraftQRCode;
import cn.yescallop.qrcode.command.SubCommand;
import cn.yescallop.qrcode.lang.Language;

public class PlaceCommand extends SubCommand {

    public PlaceCommand() {
        super("place");
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        if (!checkHasQRCode(player)) {
            return false;
        }
        MinecraftQRCode qrCode = QRCodeManager.get(player);
        if (!qrCode.valid()) {
            player.sendMessage(Language.translate("commands.generic.heightLimit"));
            return false;
        }
        qrCode.place();
        QRCodeManager.remove(player);
        player.sendMessage(Language.translate("commands.place.success"));
        return true;
    }
}
