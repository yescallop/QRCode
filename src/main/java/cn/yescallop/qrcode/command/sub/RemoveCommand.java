package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.yescallop.qrcode.QRCodeManager;
import cn.yescallop.qrcode.api.MinecraftQRCode;
import cn.yescallop.qrcode.command.SubCommand;
import cn.yescallop.qrcode.lang.Language;

public class RemoveCommand extends SubCommand {

    public RemoveCommand() {
        super("remove");
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
        qrCode.stopPreview();
        QRCodeManager.remove(player);
        player.sendMessage(Language.translate("commands.remove.success"));
        return true;
    }
}