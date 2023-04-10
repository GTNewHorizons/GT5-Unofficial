package kubatech.api.helpers;

import java.util.HashMap;
import java.util.UUID;

public class UUIDFinder {

    private static final HashMap<UUID, String> UUIDToUsernameMap = new HashMap<>();
    private static final HashMap<String, UUID> UsernameToUUIDMap = new HashMap<>();

    public static UUID getUUID(String username) {
        return UsernameToUUIDMap.get(username);
    }

    public static String getUsername(UUID player) {
        return UUIDToUsernameMap.get(player);
    }

    public static void updateMapping(String username, UUID player) {
        UUIDToUsernameMap.put(player, username);
        UsernameToUUIDMap.put(username, player);
    }
}
