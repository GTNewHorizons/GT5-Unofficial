package gtPlusPlus.core.util.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;

public class PlayerCache {

    private static final File cache = new File("PlayerCache.dat");

    public static final void initCache() {
        if (CORE.PlayerCache == null) {
            try {

                if (cache != null) {
                    CORE.PlayerCache = PlayerCache.readPropertiesFileAsMap();
                    Logger.INFO("Loaded PlayerCache.dat");
                }

            } catch (final Exception e) {
                Logger.INFO("Failed to initialise PlayerCache.dat");
                PlayerCache.createPropertiesFile("PLAYER_", "DATA");
                // e.printStackTrace();
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

    public static void appendParamChanges(final String playerName, final String playerUUIDasString) {
        final HashMap<String, UUID> playerInfo = new HashMap<>();
        playerInfo.put(playerName, UUID.fromString(playerUUIDasString));

        /*
         * try { Utils.LOG_INFO("Attempting to load "+cache.getName()); properties.load(new FileInputStream(cache)); if
         * (properties == null || properties.equals(null)){ Utils.LOG_INFO("Please wait."); } else {
         * Utils.LOG_INFO("Loaded PlayerCache.dat"); properties.setProperty(playerName+"_", playerUUIDasString);
         * FileOutputStream fr=new FileOutputStream(cache); properties.store(fr, "Player Cache."); fr.close(); } }
         */

        try {
            final FileOutputStream fos = new FileOutputStream("PlayerCache.dat");
            final ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(playerInfo);
            oos.close();
            fos.close();
            Logger.INFO("Serialized Player data saved in PlayerCache.dat");
        } catch (final IOException e) {
            Logger.INFO("No PlayerCache file found, creating one.");
            createPropertiesFile(playerName, playerUUIDasString);
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

    public static String lookupPlayerByUUID(final UUID UUID) {
        if (UUID == null) {
            return null;
        }
        final List<EntityPlayerMP> allPlayers = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (final EntityPlayerMP player : allPlayers) {
            if (player.getUniqueID().equals(UUID)) {
                return player.getDisplayName();
            }
        }
        return "Offline Player.";
    }
}
