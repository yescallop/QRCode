package cn.yescallop.qrcode;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Position;
import cn.yescallop.qrcode.api.QRCodeManager;

class EventListener implements Listener {


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && QRCodeManager.isPlayerSettingPos(player)) {
            event.setCancelled();
            Position pos = event.getBlock().getSide(event.getFace());
            QRCodeManager.get(player).pos(pos);
            QRCodeManager.removePlayerFromPosSettingWaitingList(player);
            player.sendMessage(Language.translate("pos.set.success", pos.x, pos.y, pos.z));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        QRCodeManager.remove(event.getPlayer());
    }
}
