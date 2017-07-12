package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.yescallop.qrcode.QRCodeManager;
import cn.yescallop.qrcode.api.MinecraftQRCode;
import cn.yescallop.qrcode.command.QRCodeCommand;
import cn.yescallop.qrcode.command.SubCommand;
import cn.yescallop.qrcode.lang.Language;

public class PreviewCommand extends SubCommand {

    public PreviewCommand(QRCodeCommand mainCommand) {
        super(mainCommand, "preview");
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
        player.sendMessage(Language.translate("commands.preview." + (qrCode.switchPreview() ? "enabled" : "disabled")));
        return true;
    }
}
