package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.yescallop.qrcode.QRCodeManager;
import cn.yescallop.qrcode.api.MinecraftQRCode;
import cn.yescallop.qrcode.command.SubCommand;
import cn.yescallop.qrcode.lang.Language;

public class TurnCommand extends SubCommand {

    public TurnCommand() {
        super("turn");
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        if (!checkHasQRCode(player)) {
            return false;
        }
        MinecraftQRCode qrCode = QRCodeManager.get(player);
        qrCode.turn();
        player.sendMessage(Language.translate("commands.turn.success"));
        return true;
    }
}