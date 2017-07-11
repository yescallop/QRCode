package cn.yescallop.qrcode;

import cn.nukkit.plugin.PluginBase;
import cn.yescallop.qrcode.lang.Language;

public class QRCode extends PluginBase {

    private Language lang;

    @Override
    public void onEnable() {
        lang = new Language(this.getServer().getLanguage().getLang());
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        this.getLogger().info(lang.translateString("qrcode.loaded"));
    }

    public Language getLanguage() {
        return lang;
    }
}
