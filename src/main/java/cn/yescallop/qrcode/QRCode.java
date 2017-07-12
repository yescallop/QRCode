package cn.yescallop.qrcode;

import cn.nukkit.plugin.PluginBase;
import cn.yescallop.qrcode.command.QRCodeCommand;
import cn.yescallop.qrcode.lang.Language;

public class QRCode extends PluginBase {

    @Override
    public void onEnable() {
        Language.load(this.getServer().getLanguage().getLang());
        this.getServer().getCommandMap().register("QRCode", new QRCodeCommand(this));
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        this.getLogger().info(Language.translate("qrcode.loaded"));
    }
}
