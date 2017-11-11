package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.qrcode.Language;
import cn.yescallop.qrcode.command.QRCodeCommand;
import cn.yescallop.qrcode.command.SubCommand;

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
        if (args.length > 1) {
            this.sendUsage(player);
            return false;
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
