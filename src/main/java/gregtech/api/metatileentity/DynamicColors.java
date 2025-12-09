package gregtech.api.metatileentity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class DynamicColors {

    private static final Map<String, Integer> COLORS = new HashMap<>();

    public static void init() {
        try (InputStream in = DynamicColors.class
            .getResourceAsStream("assets/gregtech/textures/TextColors.properties")) {
            Properties p = new Properties();
            p.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            p.forEach((k, v) -> {
                try {
                    COLORS.put(k.toString(), Integer.parseUnsignedInt(v.toString(), 16));
                } catch (NumberFormatException ignore) {}
            });
        } catch (IOException e) {
            throw new RuntimeException("Could not load colours", e);
        }
    }

    public static int get(String key, int fallback) {
        return COLORS.getOrDefault(key, fallback);
    }
}
