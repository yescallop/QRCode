package cn.yescallop.qrcode;

import cn.nukkit.event.Listener;
import cn.yescallop.qrcode.lang.Language;

public class EventListener implements Listener {

    private QRCode plugin;
    private Language lang;

    public EventListener(QRCode plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguage();
    }
}
