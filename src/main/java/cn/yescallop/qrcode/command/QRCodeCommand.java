package cn.yescallop.qrcode.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.qrcode.Language;
import cn.yescallop.qrcode.QRCode;
import cn.yescallop.qrcode.command.sub.*;

import java.util.*;

public class QRCodeCommand extends Command {

    private static Map<String, SubCommand> subCommands = new HashMap<>();
    private static QRCode plugin;

    public QRCodeCommand(QRCode plugin) {
        super("qrcode");
        QRCodeCommand.plugin = plugin;
        this.setAliases(new String[]{"qr"});
        this.description = Language.translate("commands.main.description");
        this.usageMessage = Language.translate("commands.main.usage");
        this.setPermission("qrcode.commands");

        subCommands.put("help", new HelpCommand());
        subCommands.put("new", new NewCommand());
        subCommands.put("params", new ParamsCommand());
        subCommands.put("place", new PlaceCommand());
        subCommands.put("pos", new PosCommand());
        subCommands.put("preview", new PreviewCommand());
        subCommands.put("remove", new RemoveCommand());
        subCommands.put("rotate", new RotateCommand());
        subCommands.put("turn", new TurnCommand());
    }

    public static SubCommand getSubCommand(String name) {
        return subCommands.get(name);
    }

    public static void sendUsages(CommandSender sender) {
        List<String> list = new ArrayList<>();
        list.add(Language.translate("commands.help.header"));
        subCommands.forEach((n, c) -> list.add(TextFormat.DARK_GREEN + "/qrcode " + n + ": " + TextFormat.WHITE + Language.translate("commands." + n + ".description")));
        list.stream().reduce((a, b) -> a + "\n" + b).ifPresent(sender::sendMessage);
    }

    public static QRCode getPlugin() {
        return plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.inGame"));
            return false;
        }
        if (args.length == 0) {
            sendUsages(sender);
            return false;
        }
        SubCommand subCommand = getSubCommand(args[0]);
        if (subCommand == null) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.notFound"));
            return false;
        }
        return subCommand.execute((Player) sender, args.length == 1 ? new String[0] : Arrays.copyOfRange(args, 1, args.length));
    }
}
