package cn.yescallop.qrcode.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.yescallop.qrcode.QRCodeManager;
import cn.yescallop.qrcode.lang.Language;

public abstract class SubCommand {

    protected final QRCodeCommand mainCommand;
    protected final Language lang;
    private final String name;

    protected SubCommand(QRCodeCommand mainCommand, String name) {
        this.name = name;
        this.mainCommand = mainCommand;
        this.lang = mainCommand.getLanguage();
    }

    public String getName() {
        return name;
    }

    protected abstract boolean execute(Player player, String[] args);

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(new TranslationContainer("commands.generic.usage", lang.translateString("commands." + this.name + ".usage")));
    }

    protected boolean checkHasQRCode(Player player) {
        if (!QRCodeManager.has(player)) {
            player.sendMessage(lang.translateString("commands.generic.noQRCode"));
            return false;
        }
        return true;
    }
}
