package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.yescallop.qrcode.Language;
import cn.yescallop.qrcode.api.MinecraftQRCode;
import cn.yescallop.qrcode.api.QRCodeManager;
import cn.yescallop.qrcode.command.SubCommand;

public class TurnCommand extends SubCommand {

    public TurnCommand() {
        super("turn");
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        if (args.length != 0) {
            this.sendUsage(player);
            return false;
        }
        if (!checkHasQRCode(player)) {
            return false;
        }
        MinecraftQRCode qrCode = QRCodeManager.get(player);
        qrCode.turn();
        player.sendMessage(Language.translate("commands.turn.success"));
        return true;
    }
}