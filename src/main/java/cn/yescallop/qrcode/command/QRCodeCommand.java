package cn.yescallop.qrcode.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.qrcode.QRCode;
import cn.yescallop.qrcode.lang.Language;

public class QRCodeCommand extends cn.nukkit.command.Command {

    private QRCode plugin;
    private Language lang;

    public QRCodeCommand(QRCode plugin) {
        super("qrcode");
        this.plugin = plugin;
        this.lang = plugin.getLanguage();
        this.setAliases(new String[]{"qr"});
        this.description = lang.translateString("command.description");
        this.usageMessage = lang.translateString("command.usage");
        this.setPermission("qrcode.commands");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + plugin.getLanguage().translateString("commands.generic.inGame"));
            return false;
        }
        switch (args[0]) {
            //TODO
        }
        return false;
    }
}
