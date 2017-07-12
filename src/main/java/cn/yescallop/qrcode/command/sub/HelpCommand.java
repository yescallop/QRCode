package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.qrcode.command.QRCodeCommand;
import cn.yescallop.qrcode.command.SubCommand;
import cn.yescallop.qrcode.lang.Language;

import java.util.Optional;

public class HelpCommand extends SubCommand {

    public HelpCommand() {
        super("help");
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        if (args.length == 0) {
            QRCodeCommand.sendUsages(player);
            return true;
        }
        Optional<SubCommand> command = QRCodeCommand.getSubCommand(args[0]);
        if (!command.isPresent()) {
            player.sendMessage(TextFormat.RED + Language.translate("commands.generic.notFound"));
            return false;
        }
        command.get().sendUsage(player);
        return true;
    }
}
