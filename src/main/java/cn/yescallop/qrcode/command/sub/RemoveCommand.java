package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.yescallop.qrcode.MinecraftQRCode;
import cn.yescallop.qrcode.QRCodeManager;
import cn.yescallop.qrcode.command.QRCodeCommand;
import cn.yescallop.qrcode.command.SubCommand;

public class RemoveCommand extends SubCommand {

    public RemoveCommand(QRCodeCommand mainCommand) {
        super(mainCommand, "remove");
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        if (!checkHasQRCode(player)) {
            return false;
        }
        MinecraftQRCode qrCode = QRCodeManager.get(player);
        qrCode.stopPreview();
        QRCodeManager.remove(player);
        player.sendMessage(lang.translateString("commands.remove.success"));
        return true;
    }
}