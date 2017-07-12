package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.yescallop.qrcode.QRCodeManager;
import cn.yescallop.qrcode.api.MinecraftQRCode;
import cn.yescallop.qrcode.command.QRCodeCommand;
import cn.yescallop.qrcode.command.SubCommand;

public class RotateCommand extends SubCommand {

    public RotateCommand(QRCodeCommand mainCommand) {
        super(mainCommand, "rotate");
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        if (!checkHasQRCode(player)) {
            return false;
        }
        MinecraftQRCode qrCode = QRCodeManager.get(player);
        qrCode.rotate();
        player.sendMessage(lang.translateString("commands.rotate.success"));
        return true;
    }
}