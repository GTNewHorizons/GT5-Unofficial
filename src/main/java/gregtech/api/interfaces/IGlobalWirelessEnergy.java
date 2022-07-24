package gregtech.api.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// If you are adding very late-game content feel free to tap into this interface.
// The eventual goal is to bypass laser/dynamo stuff and have energy deposited directly from ultra-endgame
// multi-blocks directly into the users network.
public interface IGlobalWirelessEnergy {

    // --------------------- NEVER access these maps! Use the methods provided! ---------------------

    // Global EU map.
    HashMap<String, BigInteger> GlobalEnergy = new HashMap<>(100, 0.9f);

    // Maps user IDs to usernames and vice versa. Seems redundant but this makes accessing this
    // easier in certain locations (like gt commands).
    HashMap<String, String> GlobalEnergyName = new HashMap<>(100, 0.9f);

    // Maps UUIDs to other UUIDs. This allows users to join a team.
    HashMap<String, String> GlobalEnergyTeam = new HashMap<>(100, 0.9f);

    // ----------------------------------------------------------------------------------------------

    // Folder
    String GlobalEnergyFolderName = "GlobalEnergyInformationStorage";

    // 3 txt file names. Do not change.
    String GlobalEnergyMapFileName = "GlobalEnergyMap";
    String GlobalEnergyNameFileName = "GlobalEnergyNameMap";
    String GlobalEnergyTeamFileName = "GlobalEnergyTeamMap";

    // User 0 will join user 1 by calling this function. They will share the same energy network.
    default void JoinUserNetwork(String user_0, String user_1) {
        GlobalEnergyTeam.put(user_0, user_1);
    }

    // --- Save data for global energy network --

    default void SaveGlobalEnergyInfo(String world_name) {
        CreateStorageIfNotExist(world_name);
        SaveGlobalEnergyMap(world_name);
        SaveGlobalEnergyName(world_name);
        SaveGlobalEnergyTeam(world_name);

    }

    default void SaveGlobalEnergyMap(String world_name) {
        try {
            List<String> lines = GlobalEnergy.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.toList());

            Path path = Paths.get("./saves/" + world_name + "/" + GlobalEnergyFolderName + "/" + GlobalEnergyMapFileName + ".txt").toAbsolutePath();
            Files.write(path, lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void SaveGlobalEnergyName(String world_name) {
        try {
            List<String> lines = GlobalEnergyName.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.toList());

            Path path = Paths.get("./saves/" + world_name + "/" + GlobalEnergyFolderName + "/" + GlobalEnergyNameFileName + ".txt").toAbsolutePath();
            Files.write(path, lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void SaveGlobalEnergyTeam(String world_name) {
        try {
            List<String> lines = GlobalEnergyTeam.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.toList());

            Path path = Paths.get("./saves/" + world_name + "/" + GlobalEnergyFolderName + "/" + GlobalEnergyTeamFileName + ".txt").toAbsolutePath();
            Files.write(path, lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Load data for global energy network ---

    default void LoadGlobalEnergyInfo(World world) {
        CreateStorageIfNotExist(world.getWorldInfo().getWorldName());
        LoadGlobalEnergyMap(world);
        LoadGlobalEnergyName(world);
        LoadGlobalEnergyTeam(world);
    }

    default void LoadGlobalEnergyMap(World world) {
        try {
            Path path = Paths.get("./saves/" + world.getWorldInfo().getWorldName() + "/" + GlobalEnergyFolderName + "/" + GlobalEnergyMapFileName + ".txt").toAbsolutePath();

            String[] data;
            for(String line : Files.readAllLines(path)) {
                data = line.split(":");

                String UUID = data[0];
                BigInteger num = new BigInteger(data[1]);

                GlobalEnergy.put(UUID, num);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void LoadGlobalEnergyName(World world) {
        try {
            Path path = Paths.get("./saves/" + world.getWorldInfo().getWorldName() + "/"
                + GlobalEnergyFolderName + "/" + GlobalEnergyNameFileName + ".txt").toAbsolutePath();

            String[] data;
            for(String line : Files.readAllLines(path)) {
                data = line.split(":");

                GlobalEnergyName.put(data[0], data[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void LoadGlobalEnergyTeam(World world) {
        try {
            Path path = Paths.get("./saves/" + world.getWorldInfo().getWorldName() + "/"
                + GlobalEnergyFolderName + "/" + GlobalEnergyTeamFileName + ".txt").toAbsolutePath();

            String[] data;
            for(String line : Files.readAllLines(path)) {
                data = line.split(":");

                GlobalEnergyName.put(data[0], data[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------

    default void CreateStorageIfNotExist(String world_name) {
        Path folder_path = Paths.get("./saves/" + world_name + "/" + GlobalEnergyFolderName).toAbsolutePath();

        // Create folder for storing global energy network info.
        try {
            Files.createDirectories(folder_path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create txt files.
        try {
            File file_0 = new File(folder_path + "/" + GlobalEnergyMapFileName + ".txt");
            file_0.createNewFile();
            File file_1 = new File(folder_path + "/" + GlobalEnergyNameFileName + ".txt");
            file_1.createNewFile();
            File file_2 = new File(folder_path + "/" + GlobalEnergyTeamFileName + ".txt");
            file_2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Adds a user to the energy map if they do not already exist. Otherwise, do nothing. Will also check if the user
    // has changed their username and adjust the maps accordingly. This should be called infrequently. Ideally on first
    // tick of a machine being placed only.

    default void StrongCheckOrAddUser(EntityPlayer user) {
        StrongCheckOrAddUser(user.getUniqueID().toString(), user.getDisplayName());
    }

    default void StrongCheckOrAddUser(UUID user_uuid, String user_name) {
        StrongCheckOrAddUser(user_uuid.toString(), user_name);
    }

    default void StrongCheckOrAddUser(String user_uuid, String user_name) {

        // Check if the user has a team. Add them if not.
        GlobalEnergyTeam.putIfAbsent(user_uuid, user_uuid);

        // Check if the user is in the global energy map.
        GlobalEnergy.putIfAbsent(user_uuid, BigInteger.ZERO);

        // If the username linked to the users fixed uuid is not equal to their current name then remove it.
        // This indicates that their username has changed.
        if (!(GlobalEnergyName.getOrDefault(user_uuid, "").equals(user_name))) {
            String old_name = GlobalEnergyName.get(user_uuid);
            GlobalEnergyName.remove(old_name);
        }

        // Add UUID -> Name, Name -> UUID.
        GlobalEnergyName.put(user_name, user_uuid);
        GlobalEnergyName.put(user_uuid, user_name);
    }

    // ------------------------------------------------------------------------------------
    // Add EU to the users global energy. You can enter a negative number to subtract it.
    // If the value goes below 0 it will return false and not perform the operation.
    // BigIntegers have much slower operations than longs/ints. You should call these methods
    // as infrequently as possible and bulk store values to add to the global map.
    default boolean addEUToGlobalEnergyMap(String user_uuid, BigInteger EU) {

        // Get the team UUID. Users are by default in a team with a UUID equal to their player UUID.
        String team_uuid = GlobalEnergyTeam.getOrDefault(user_uuid, user_uuid);

        // Get the teams total energy stored. If they are not in the map, return 0 EU.
        BigInteger total_eu = GlobalEnergy.getOrDefault(team_uuid, BigInteger.ZERO);
        total_eu = total_eu.add(EU);

        // If there is sufficient EU then complete the operation and return true.
        if (total_eu.signum() >= 0) {
            GlobalEnergy.put(team_uuid, total_eu);
            return true;
        }

        // There is insufficient EU so cancel the operation and return false.
        return false;
    }

    default boolean addEUToGlobalEnergyMap(UUID user_uuid, BigInteger EU) {
        return addEUToGlobalEnergyMap(user_uuid.toString(), EU);
    }

    default boolean addEUToGlobalEnergyMap(UUID user_uuid, long EU) {
        return addEUToGlobalEnergyMap(user_uuid.toString(), BigInteger.valueOf(EU));
    }

    default boolean addEUToGlobalEnergyMap(UUID user_uuid, int EU) {
        return addEUToGlobalEnergyMap(user_uuid.toString(), BigInteger.valueOf(EU));
    }

    default boolean addEUToGlobalEnergyMap(String user_uuid, long EU) {
        return addEUToGlobalEnergyMap(user_uuid, BigInteger.valueOf(EU));
    }

    default boolean addEUToGlobalEnergyMap(String user_uuid, int EU) {
        return addEUToGlobalEnergyMap(user_uuid, BigInteger.valueOf(EU));
    }

    // ------------------------------------------------------------------------------------

    default BigInteger GetUserEU(String user_uuid) {
        return GlobalEnergy.getOrDefault(GlobalEnergyTeam.getOrDefault(user_uuid, user_uuid), BigInteger.ZERO);
    }

    // This overwrites the EU in the network. Only use this if you are absolutely sure you know what you are doing.
    default void SetUserEU(String user_uuid, BigInteger EU) {
        GlobalEnergy.put(GlobalEnergyTeam.get(user_uuid), EU);
    }

    default String GetUsernameFromUUID(String uuid) {
        return GlobalEnergyTeam.getOrDefault(uuid, "");
    }

    default String GetUUIDFromUsername(String username) {
        return GlobalEnergyTeam.getOrDefault(GlobalEnergyName.getOrDefault(username, ""), "");
    }

    default void ClearMaps() {
        GlobalEnergy.clear();
        GlobalEnergyName.clear();
        GlobalEnergyTeam.clear();
    }

}


