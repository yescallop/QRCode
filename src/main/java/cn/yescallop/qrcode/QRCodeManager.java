package cn.yescallop.qrcode;

import cn.nukkit.Player;
import cn.yescallop.qrcode.api.MinecraftQRCode;

import java.util.HashMap;
import java.util.Map;

public class QRCodeManager {

    private static Map<Player, MinecraftQRCode> qrCodes = new HashMap<>();

    public static void add(Player player, MinecraftQRCode qrCode) {
        qrCodes.put(player, qrCode);
    }

    public static MinecraftQRCode get(Player player) {
        return qrCodes.get(player);
    }

    public static boolean has(Player player) {
        return qrCodes.containsKey(player);
    }

    public static boolean remove(Player player) {
        return qrCodes.remove(player) != null;
    }
}
