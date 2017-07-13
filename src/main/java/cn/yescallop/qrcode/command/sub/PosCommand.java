package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.yescallop.qrcode.EventListener;
import cn.yescallop.qrcode.command.SubCommand;
import cn.yescallop.qrcode.lang.Language;

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
        EventListener.addPlayerToPosSettingWaitingList(player);
        player.sendMessage(Language.translate("commands.pos.success"));
        return true;
    }
}