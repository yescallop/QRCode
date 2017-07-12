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

    private QRCode plugin;
    private Language lang;
    private List<SubCommand> subCommands = new ArrayList<>();

    public QRCodeCommand(QRCode plugin) {
        super("qrcode");
        this.plugin = plugin;
        this.lang = plugin.getLanguage();
        this.setAliases(new String[]{"qr"});
        this.description = lang.translateString("commands.main.description");
        this.usageMessage = lang.translateString("commands.main.usage");
        this.setPermission("qrcode.commands");

        subCommands.add(new HelpCommand(this));
        subCommands.add(new NewCommand(this));
        subCommands.add(new PlaceCommand(this));
        subCommands.add(new PreviewCommand(this));
        subCommands.add(new RemoveCommand(this));
        subCommands.add(new RotateCommand(this));
        subCommands.add(new TurnCommand(this));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.inGame"));
            return false;
        }
        if (args.length == 0) {
            sendUsages(sender);
            return false;
        }
        Optional<SubCommand> cmd = this.getSubCommand(args[0]);
        if (!cmd.isPresent()) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.notFound"));
            return false;
        }
        return cmd.get().execute((Player) sender, args.length == 1 ? new String[0] : Arrays.copyOfRange(args, 1, args.length));
    }

    public QRCode getPlugin() {
        return plugin;
    }

    Language getLanguage() {
        return lang;
    }

    public Optional<SubCommand> getSubCommand(String name) {
        return subCommands.stream().filter(c -> c.getName().equals(name) || Arrays.asList(c.getAliases()).contains(name)).findAny();
    }

    public void sendUsages(CommandSender sender) {
        List<String> list = new ArrayList<>();
        list.add(lang.translateString("commands.help.header"));
        subCommands.forEach(c -> list.add(TextFormat.DARK_GREEN + "/qrcode " + c.getName() + ": " + TextFormat.WHITE + lang.translateString("commands." + c.getName() + ".description")));
        list.stream().reduce((a, b) -> a + "\n" + b).ifPresent(sender::sendMessage);
    }
}
