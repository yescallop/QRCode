package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.qrcode.command.QRCodeCommand;
import cn.yescallop.qrcode.command.SubCommand;

import java.util.Optional;

public class HelpCommand extends SubCommand {

    public HelpCommand(QRCodeCommand mainCommand) {
        super(mainCommand, "help");
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        if (args.length == 0) {
            mainCommand.sendUsages(player);
            return true;
        }
        Optional<SubCommand> command = mainCommand.getSubCommand(args[0]);
        if (!command.isPresent()) {
            player.sendMessage(TextFormat.RED + lang.translateString("commands.generic.notFound"));
            return false;
        }
        command.get().sendUsage(player);
        return true;
    }
}
