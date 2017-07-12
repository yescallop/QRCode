package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.yescallop.qrcode.MinecraftQRCode;
import cn.yescallop.qrcode.QRCodeManager;
import cn.yescallop.qrcode.command.QRCodeCommand;
import cn.yescallop.qrcode.command.SubCommand;

public class PlaceCommand extends SubCommand {

    public PlaceCommand(QRCodeCommand mainCommand) {
        super(mainCommand, "place");
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        if (!checkHasQRCode(player)) {
            return false;
        }
        MinecraftQRCode qrCode = QRCodeManager.get(player);
        if (!qrCode.valid()) {
            player.sendMessage(lang.translateString("commands.generic.heightLimit"));
            return false;
        }
        qrCode.place();
        QRCodeManager.remove(player);
        player.sendMessage(lang.translateString("commands.place.success"));
        return true;
    }
}
