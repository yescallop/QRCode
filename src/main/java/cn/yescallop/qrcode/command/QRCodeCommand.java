package cn.yescallop.qrcode.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.qrcode.QRCode;
import cn.yescallop.qrcode.command.sub.*;
import cn.yescallop.qrcode.lang.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class QRCodeCommand extends Command {

    private static List<SubCommand> subCommands = new ArrayList<>();
    private static QRCode plugin;

    public QRCodeCommand(QRCode plugin) {
        super("qrcode");
        QRCodeCommand.plugin = plugin;
        this.setAliases(new String[]{"qr"});
        this.description = Language.translate("commands.main.description");
        this.usageMessage = Language.translate("commands.main.usage");
        this.setPermission("qrcode.commands");

        subCommands.add(new HelpCommand());
        subCommands.add(new NewCommand());
        subCommands.add(new PlaceCommand());
        subCommands.add(new PreviewCommand());
        subCommands.add(new RemoveCommand());
        subCommands.add(new RotateCommand());
        subCommands.add(new TurnCommand());
    }

    public static Optional<SubCommand> getSubCommand(String name) {
        return subCommands.stream().filter(c -> c.getName().equals(name)).findAny();
    }

    public static void sendUsages(CommandSender sender) {
        List<String> list = new ArrayList<>();
        list.add(Language.translate("commands.help.header"));
        subCommands.forEach(c -> list.add(TextFormat.DARK_GREEN + "/qrcode " + c.getName() + ": " + TextFormat.WHITE + Language.translate("commands." + c.getName() + ".description")));
        list.stream().reduce((a, b) -> a + "\n" + b).ifPresent(sender::sendMessage);
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
        Optional<SubCommand> cmd = getSubCommand(args[0]);
        if (!cmd.isPresent()) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.notFound"));
            return false;
        }
        return cmd.get().execute((Player) sender, args.length == 1 ? new String[0] : Arrays.copyOfRange(args, 1, args.length));
    }

    public static QRCode getPlugin() {
        return plugin;
    }
}
