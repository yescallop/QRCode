package cn.yescallop.qrcode;

import cn.nukkit.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Language {

    private static Map<String, String> lang;
    private static Map<String, String> fallbackLang;

    private Language() {
        //no instance
    }

    static void load(String langName) {
        if (lang != null) {
            return;
        }
        try {
            lang = loadLang(Language.class.getClassLoader().getResourceAsStream("lang/" + langName + ".ini"));
        } catch (NullPointerException e) {
            lang = new HashMap<>();
        }
        fallbackLang = loadLang(Language.class.getClassLoader().getResourceAsStream("lang/eng.ini"));
    }

    public static String translate(String str, Object... params) {
        String baseText = get(str);
        baseText = parseTranslation(baseText != null ? baseText : str);
        for (int i = 0; i < params.length; i++) {
            baseText = baseText.replace("{%" + i + "}", parseTranslation(String.valueOf(params[i])));
        }

        return baseText;
    }

    public static String translate(String str, String... params) {
        String baseText = get(str);
        baseText = parseTranslation(baseText != null ? baseText : str);
        for (int i = 0; i < params.length; i++) {
            baseText = baseText.replace("{%" + i + "}", parseTranslation(params[i]));
        }

        return baseText;
    }

    private static Map<String, String> loadLang(InputStream stream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            Map<String, String> d = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.charAt(0) == '#') {
                    continue;
                }
                int index = line.indexOf('=');
                if (index <= 0 || index == line.length() - 1)
                    continue;
                d.put(line.substring(0, index), line.substring(index + 1, line.length()));
            }
            return d;
        } catch (IOException e) {
            Server.getInstance().getLogger().logException(e);
            return null;
        }
    }

    private static String internalGet(String id) {
        if (lang.containsKey(id)) {
            return lang.get(id);
        } else if (fallbackLang.containsKey(id)) {
            return fallbackLang.get(id);
        }
        return null;
    }

    private static String get(String id) {
        if (lang.containsKey(id)) {
            return lang.get(id);
        } else if (fallbackLang.containsKey(id)) {
            return fallbackLang.get(id);
        }
        return id;
    }

    private static String parseTranslation(String text) {
        StringBuilder newString = new StringBuilder();
        StringBuilder replaceString = null;

        int len = text.length();

        for (int i = 0; i < len; ++i) {
            char c = text.charAt(i);
            if (replaceString != null) {
                if ((c >= 0x30 && c <= 0x39) // 0-9
                        || (c >= 0x41 && c <= 0x5a) // A-Z
                        || (c >= 0x61 && c <= 0x7a) || // a-z
                        c == '.' || c == '-') {
                    replaceString.append(c);
                } else {
                    String t = internalGet(replaceString.substring(1));
                    if (t != null) {
                        newString.append(t);
                    } else {
                        newString.append(replaceString);
                    }
                    replaceString = null;
                    if (c == '%') {
                        replaceString = new StringBuilder().append(c);
                    } else {
                        newString.append(c);
                    }
                }
            } else if (c == '%') {
                replaceString = new StringBuilder().append(c);
            } else {
                newString.append(c);
            }
        }

        if (replaceString != null) {
            String t = internalGet(replaceString.substring(1));
            if (t != null) {
                newString.append(t);
            } else {
                newString.append(replaceString);
            }
        }
        return newString.toString();
    }
}