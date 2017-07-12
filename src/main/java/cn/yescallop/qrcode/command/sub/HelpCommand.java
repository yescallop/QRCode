package cn.yescallop.qrcode.command.sub;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.qrcode.command.QRCodeCommand;
import cn.yescallop.qrcode.command.SubCommand;

import java.util.Optional;

public class HelpCommand extends SubCommand {

    public HelpCommand(QRCodeCommand mainCommand) {
        super(mainCommand, "help");
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            mainCommand.sendUsages(sender);
            return true;
        }
        Optional<SubCommand> command = mainCommand.getSubCommand(args[0]);
        if (!command.isPresent()) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.notFound"));
            return false;
        }
        command.get().sendUsage(sender);
        return true;
    }
}
