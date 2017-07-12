package cn.yescallop.qrcode.command.sub;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.qrcode.QRCodeManager;
import cn.yescallop.qrcode.api.MinecraftQRCode;
import cn.yescallop.qrcode.command.Parameter;
import cn.yescallop.qrcode.command.QRCodeCommand;
import cn.yescallop.qrcode.command.SubCommand;
import cn.yescallop.qrcode.lang.Language;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ParamsCommand extends SubCommand {

    public ParamsCommand() {
        super("params");
    }

    private static Block parseBlock(String str) {
        String[] arr = str.split(":");
        if (arr.length == 0 || arr.length > 2) return null;
        int id;
        int meta;
        try {
            id = Integer.parseInt(arr[0]);
            meta = arr.length == 1 ? 0 : Integer.parseInt(arr[1]);
        } catch (NumberFormatException e) {
            return null;
        }
        if (id < 0 || id > 255 || meta < 0 || meta > 15) {
            return null;
        }
        return Block.get(id, meta);
    }

    private static void sendAvailableValues(Player player, Parameter param) {
        Stream.of(param.getAvailableValues())
                .map(v -> v.startsWith("%") ? Language.translate(v) : v)
                .reduce((a, b) -> a + ", " + b)
                .ifPresent(s -> player.sendMessage(Language.translate("commands.params.availableValues", param.getName(), s)));
    }

    private static void sendValues(Player player) {
        List<String> list = new ArrayList<>();
        list.add(Language.translate("commands.params.get.header"));
        MinecraftQRCode qrCode = QRCodeManager.get(player);
        Stream.of(Parameter.values()).
                forEach(p -> list.add(TextFormat.DARK_GREEN + p.getName() + ": " + TextFormat.WHITE + getParamValue(qrCode, p)));
        list.stream().reduce((a, b) -> a + "\n" + b).ifPresent(player::sendMessage);
    }

    private static String getParamValue(MinecraftQRCode qrCode, Parameter param) {
        switch (param) {
            case BACKGROUND:
                return qrCode.background().getId() + ":" + qrCode.background().getDamage();
            case BORDER_SIZE:
                return String.valueOf(qrCode.borderSize());
            case CHARSET:
                return qrCode.charset();
            case CONTENT:
                return qrCode.content();
            case ERROR_CORRECTION_LEVEL:
                return qrCode.errorCorrectionLevel().name();
            case FOREGROUND:
                return qrCode.foreground().getId() + ":" + qrCode.foreground().getDamage();
            case MAGNIFIER:
                return String.valueOf(qrCode.mgnifier());
            case ORIENTATION:
                return qrCode.orientation().getName();
        }
        return null;
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        if (!checkHasQRCode(player)) {
            return false;
        }
        if (args.length == 0) {
            List<String> list = new ArrayList<>();
            list.add(Language.translate("commands.params.header"));
            Stream.of(Parameter.values()).
                    forEach(p -> list.add(TextFormat.DARK_GREEN + p.getName() + ": " + TextFormat.WHITE + Language.translate("params." + p.getName() + ".description")));
            list.stream().reduce((a, b) -> a + "\n" + b).ifPresent(player::sendMessage);
        } else if (args[0].equals("set")) {
            if (args.length == 1) {
                this.sendUsage(player);
                return false;
            }
            Optional<Parameter> optional = Parameter.byName(args[1]);
            if (!optional.isPresent()) {
                player.sendMessage(TextFormat.RED + Language.translate("commands.params.notFound"));
                return false;
            }
            Parameter param = optional.get();
            if (args.length == 2) {
                sendAvailableValues(player, param);
            } else {
                Block block;
                MinecraftQRCode qrCode = QRCodeManager.get(player);
                String value = Stream.of(args).skip(3)
                        .reduce((a, b) -> a + " " + b)
                        .get();
                switch (param) {
                    case BACKGROUND:
                        block = parseBlock(value);
                        if (block == null) {
                            sendAvailableValues(player, param);
                            return false;
                        }
                        qrCode.background(block);
                        break;
                    case BORDER_SIZE:
                        int size;
                        try {
                            size = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            sendAvailableValues(player, param);
                            return false;
                        }
                        if (size < 0) {
                            sendAvailableValues(player, param);
                            return false;
                        }
                        qrCode.borderSize(size);
                        break;
                    case CHARSET:
                        if (!Arrays.asList(param.getAvailableValues()).contains(value)) {
                            sendAvailableValues(player, param);
                            return false;
                        }
                        try {
                            qrCode.charset(args[1]);
                        } catch (WriterException e) {
                            QRCodeCommand.getPlugin().getLogger().error("Exception caught while creating QR code", e);
                            player.sendMessage(new TextContainer(TextFormat.RED + "%commands.generic.exception"));
                            return false;
                        }
                        break;
                    case CONTENT:
                        try {
                            qrCode.content(value);
                        } catch (WriterException e) {
                            QRCodeCommand.getPlugin().getLogger().error("Exception caught while creating QR code", e);
                            player.sendMessage(new TextContainer(TextFormat.RED + "%commands.generic.exception"));
                            return false;
                        }
                        break;
                    case ERROR_CORRECTION_LEVEL:
                        ErrorCorrectionLevel ecLevel;
                        try {
                            ecLevel = ErrorCorrectionLevel.valueOf(value);
                        } catch (IllegalArgumentException e) {
                            sendAvailableValues(player, param);
                            return false;
                        }
                        try {
                            qrCode.errorCorrectionLevel(ecLevel);
                        } catch (WriterException e) {
                            QRCodeCommand.getPlugin().getLogger().error("Exception caught while creating QR code", e);
                            player.sendMessage(new TextContainer(TextFormat.RED + "%commands.generic.exception"));
                            return false;
                        }
                        break;
                    case FOREGROUND:
                        block = parseBlock(value);
                        if (block == null) {
                            sendAvailableValues(player, param);
                            return false;
                        }
                        qrCode.foreground(block);
                        break;
                    case MAGNIFIER:
                        int magnifier;
                        try {
                            magnifier = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            sendAvailableValues(player, param);
                            return false;
                        }
                        if (magnifier < 1) {
                            sendAvailableValues(player, param);
                            return false;
                        }
                        qrCode.magnifier(magnifier);
                        break;
                    case ORIENTATION:
                        MinecraftQRCode.Orientation orientation = MinecraftQRCode.Orientation.byName(value);
                        if (orientation == null) {
                            sendAvailableValues(player, param);
                            return false;
                        }
                        qrCode.orientation(orientation);
                        break;
                }
                player.sendMessage(Language.translate("commands.params.set.success", args[1], value));
            }
        } else if (args[1].equals("get")) {
            if (args.length == 1) {
                sendValues(player);
            } else if (args.length == 2) {
                Optional<Parameter> optional = Parameter.byName(args[1]);
                if (!optional.isPresent()) {
                    player.sendMessage(TextFormat.RED + Language.translate("commands.params.notFound"));
                    return false;
                }
                player.sendMessage(Language.translate("commands.params.get.success", args[1], getParamValue(QRCodeManager.get(player), optional.get())));
            } else {
                this.sendUsage(player);
                return false;
            }
        } else {
            this.sendUsage(player);
            return false;
        }
        return true;
    }
}