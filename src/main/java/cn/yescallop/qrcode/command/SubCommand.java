package cn.yescallop.qrcode.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.yescallop.qrcode.QRCodeManager;
import cn.yescallop.qrcode.lang.Language;

public abstract class SubCommand {

    private final String name;

    protected SubCommand(String name) {
        this.name = name;
    }

    protected static boolean checkHasQRCode(Player player) {
        if (!QRCodeManager.has(player)) {
            player.sendMessage(Language.translate("commands.generic.noQRCode"));
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    protected abstract boolean execute(Player player, String[] args);

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(new TranslationContainer("commands.generic.usage", Language.translate("commands." + this.name + ".usage")));
    }
}
