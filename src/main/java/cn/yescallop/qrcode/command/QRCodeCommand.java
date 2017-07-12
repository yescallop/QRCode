package cn.yescallop.qrcode.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.qrcode.QRCode;
import cn.yescallop.qrcode.lang.Language;

import java.util.*;

public class QRCodeCommand extends Command {

    private QRCode plugin;
    private Language lang;
    private Map<String, SubCommand> subCommands = new LinkedHashMap<>();

    public QRCodeCommand(QRCode plugin) {
        super("qrcode");
        this.plugin = plugin;
        this.lang = plugin.getLanguage();
        this.setAliases(new String[]{"qr"});
        this.description = lang.translateString("commands.main.description");
        this.usageMessage = lang.translateString("commands.main.usage");
        this.setPermission("qrcode.commands");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length == 0) {
            sendUsages(sender);
            return false;
        }
        SubCommand cmd = subCommands.get(args[0]);
        if (cmd == null) {
            sendUsages(sender);
            return false;
        }
        return cmd.execute(sender, args.length == 1 ? new String[0] : Arrays.copyOfRange(args, 1, args.length - 1));
    }

    private void sendUsages(CommandSender sender) {
        List<String> list = new ArrayList<>();
        list.add(lang.translateString("commands.help.header"));
        subCommands.keySet().forEach(c -> list.add(TextFormat.GREEN + "/qrcode " + c + ": " + TextFormat.RESET + lang.translateString("commands." + c + ".description")));
        list.stream().reduce((a, b) -> a + "\n" + b).ifPresent(sender::sendMessage);
    }
}
