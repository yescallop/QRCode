package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.yescallop.qrcode.MinecraftQRCode;
import cn.yescallop.qrcode.QRCodeManager;
import cn.yescallop.qrcode.command.QRCodeCommand;
import cn.yescallop.qrcode.command.SubCommand;

public class TurnCommand extends SubCommand {

    public TurnCommand(QRCodeCommand mainCommand) {
        super(mainCommand, "turn");
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        if (!checkHasQRCode(player)) {
            return false;
        }
        MinecraftQRCode qrCode = QRCodeManager.get(player);
        qrCode.turn();
        player.sendMessage(lang.translateString("commands.turn.success"));
        return true;
    }
}