package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.yescallop.qrcode.Language;
import cn.yescallop.qrcode.api.QRCodeManager;
import cn.yescallop.qrcode.command.SubCommand;

public class PosCommand extends SubCommand {

    public PosCommand() {
        super("pos");
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
        QRCodeManager.addPlayerToPosSettingWaitingList(player);
        player.sendMessage(Language.translate("commands.pos.success"));
        return true;
    }
}