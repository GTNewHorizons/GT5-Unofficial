/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

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
