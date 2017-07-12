package cn.yescallop.qrcode.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.qrcode.lang.Language;

public abstract class SubCommand {

    private final String name;
    private String[] aliases;
    protected final QRCodeCommand mainCommand;
    protected final Language lang;

    protected SubCommand(QRCodeCommand mainCommand, String name) {
        this.name = name;
        this.mainCommand = mainCommand;
        this.lang = mainCommand.getLanguage();
    }

    public void setAliases(String... aliases) {
        this.aliases = aliases;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getName() {
        return name;
    }

    protected abstract boolean execute(CommandSender sender, String[] args);

    public void sendUsage(CommandSender sender) {
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
