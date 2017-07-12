package cn.yescallop.qrcode.command;

import cn.yescallop.qrcode.api.MinecraftQRCode.Orientation;

import java.nio.charset.Charset;
import java.util.Optional;
import java.util.stream.Stream;

public enum Parameter {

    BACKGROUND("background", "%params.block"),
    BORDER_SIZE("borderSize", "%params.nonNegativeInteger"),
    CHARSET("charset", Charset.availableCharsets().keySet().toArray(new String[0])),
    ERROR_CORRECTION_LEVEL("ecLevel", "L", "M", "Q", "H"),
    FOREGROUND("foreground", "%params.block"),
    MAGNIFIER("magnifier", "%params.positiveInteger"),
    ORIENTATION("orientation", Stream.of(Orientation.values()).map(Orientation::getName).toArray(String[]::new));

    private String name;
    private String[] availableValues;

    Parameter(String name, String... availableValues) {
        this.name = name;
        this.availableValues = availableValues;
    }

    public static Optional<Parameter> byName(String name) {
        return Stream.of(values()).filter(p -> name.equals(p.name)).findAny();
    }

    public String getName() {
        return name;
    }

    public String[] getAvailableValues() {
        return availableValues;
    }
}
