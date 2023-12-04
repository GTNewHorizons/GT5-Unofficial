package gtPlusPlus.core.util.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;

public class PlayerCache {

    private static final File cache = new File("PlayerCache.dat");

    public static void initCache() {
        if (CORE.PlayerCache == null) {
            if (cache.exists()) {
                CORE.PlayerCache = PlayerCache.readPropertiesFileAsMap();
                Logger.INFO("Loaded PlayerCache.dat");
            }
            if (CORE.PlayerCache == null) {
                Logger.INFO("Failed to load PlayerCache.dat");
                PlayerCache.createPropertiesFile("PLAYER_", "DATA");
            }
        }
    }

    public static void createPropertiesFile(final String playerName, final String playerUUIDasString) {
        try {
            final Properties props = new Properties();
            props.setProperty(playerName + " ", playerUUIDasString);
            final OutputStream out = new FileOutputStream(cache);
            props.store(out, "Player Cache.");
            Logger.INFO("PlayerCache.dat created for future use.");
            out.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, UUID> readPropertiesFileAsMap() {
        HashMap<String, UUID> map = null;
        try {
            final FileInputStream fis = new FileInputStream(cache);
            final ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap<String, UUID>) ois.readObject();
            ois.close();
            fis.close();
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (final ClassNotFoundException c) {
            Logger.INFO("Class not found");
            c.printStackTrace();
            return null;
        }
        Logger.WARNING("Deserialized PlayerCache..");
        return map;
    }

}
