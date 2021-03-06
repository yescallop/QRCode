package cn.yescallop.qrcode.api;

import cn.nukkit.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class QRCodeManager {

    private static Map<Player, MinecraftQRCode> qrCodes = new HashMap<>();

    private static Set<Player> posSettingWaitingList = new HashSet<>();

    public static boolean isPlayerSettingPos(Player player) {
        return posSettingWaitingList.contains(player);
    }

    public static void addPlayerToPosSettingWaitingList(Player player) {
        posSettingWaitingList.add(player);
    }

    public static boolean removePlayerFromPosSettingWaitingList(Player player) {
        return posSettingWaitingList.remove(player);
    }

    public static void add(Player player, MinecraftQRCode qrCode) {
        qrCodes.put(player, qrCode);
        posSettingWaitingList.add(player);
    }

    public static MinecraftQRCode get(Player player) {
        return qrCodes.get(player);
    }

    public static boolean has(Player player) {
        return qrCodes.containsKey(player);
    }

    public static boolean remove(Player player) {
        posSettingWaitingList.remove(player);
        return qrCodes.remove(player) != null;
    }
}
