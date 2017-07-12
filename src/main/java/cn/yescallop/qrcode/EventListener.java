package cn.yescallop.qrcode;

import cn.nukkit.event.Listener;
import cn.yescallop.qrcode.lang.Language;

public class EventListener implements Listener {

    private final QRCode plugin;
    private final Language lang;

    public EventListener(QRCode plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguage();
    }
}
