package cn.yescallop.qrcode.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.qrcode.QRCode;
import cn.yescallop.qrcode.lang.Language;

public abstract class SubCommand {

    private final String name;
    private final QRCode plugin;
    private final Language lang;

    protected SubCommand(QRCode plugin, String name) {
        this.name = name;
        this.plugin = plugin;
        this.lang = plugin.getLanguage();
    }

    abstract boolean execute(CommandSender sender, String[] args);

    protected void sendUsage(CommandSender sender) {
        sender.sendMessage(new TranslationContainer("commands.generic.usage", lang.translateString("commands." + this.name + ".usage")));
    }

    protected boolean testInGame(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.inGame"));
            return false;
        }
        return true;
    }
}
