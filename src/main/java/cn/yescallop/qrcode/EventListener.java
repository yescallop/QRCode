package cn.yescallop.qrcode;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Position;
import cn.yescallop.qrcode.lang.Language;

import java.util.ArrayList;
import java.util.List;

public class EventListener implements Listener {

    private static List<Player> posSettingWaitingList = new ArrayList<>();
    private final QRCode plugin;

    public EventListener(QRCode plugin) {
        this.plugin = plugin;
    }

    public static void addPlayerToPosSettingWaitingList(Player player) {
        posSettingWaitingList.add(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && posSettingWaitingList.contains(player)) {
            event.setCancelled();
            Position pos = event.getBlock().getSide(event.getFace());
            QRCodeManager.get(player).pos(pos);
            posSettingWaitingList.remove(player);
            player.sendMessage(Language.translate("pos.set.success", pos.x, pos.y, pos.z));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        posSettingWaitingList.remove(event.getPlayer());
        QRCodeManager.remove(event.getPlayer());
    }
}
