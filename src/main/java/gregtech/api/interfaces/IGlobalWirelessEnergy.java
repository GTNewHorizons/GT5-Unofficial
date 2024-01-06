package gregtech.api.interfaces;

import static gregtech.common.misc.GlobalVariableStorage.GlobalEnergy;
import static gregtech.common.misc.GlobalVariableStorage.GlobalEnergyName;
import static gregtech.common.misc.GlobalVariableStorage.GlobalEnergyTeam;

import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.misc.GlobalEnergyWorldSavedData;

// If you are adding very late-game content feel free to tap into this interface.
// The eventual goal is to bypass laser/dynamo stuff and have energy deposited directly from ultra-endgame
// multi-blocks directly into the users network.
public interface IGlobalWirelessEnergy {

    // User 0 will join user 1 by calling this function. They will share the same energy network.
    default void joinUserNetwork(String user_uuid_0, String user_uuid_1) {
        GlobalEnergyTeam.put(user_uuid_0, user_uuid_1);
    }

    // Adds a user to the energy map if they do not already exist. Otherwise, do nothing. Will also check if the user
    // has changed their username and adjust the maps accordingly. This should be called infrequently. Ideally on first
    // tick of a machine being placed only.

    default void strongCheckOrAddUser(EntityPlayer user) {
        strongCheckOrAddUser(
            user.getUniqueID()
                .toString(),
            user.getDisplayName());
    }

    default void strongCheckOrAddUser(UUID user_uuid, String user_name) {
        strongCheckOrAddUser(user_uuid.toString(), user_name);
    }

    default void strongCheckOrAddUser(String user_uuid, String user_name) {

        // Check if the user has a team. Add them if not.
        GlobalEnergyTeam.putIfAbsent(user_uuid, user_uuid);

        // Check if the user is in the global energy map.
        GlobalEnergy.putIfAbsent(user_uuid, BigInteger.ZERO);

        // If the username linked to the users fixed uuid is not equal to their current name then remove it.
        // This indicates that their username has changed.
        if (!(GlobalEnergyName.getOrDefault(user_uuid, "")
            .equals(user_name))) {
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
    default boolean addEUToGlobalEnergyMap(String userUUID, BigInteger EU) {

        // Mark the data as dirty and in need of saving.
        try {
            GlobalEnergyWorldSavedData.INSTANCE.markDirty();
        } catch (Exception exception) {
            System.out.println("COULD NOT MARK GLOBAL ENERGY AS DIRTY IN ADD EU");
            exception.printStackTrace();
        }

        // Get the team UUID. Users are by default in a team with a UUID equal to their player UUID.
        String teamUUID = GlobalEnergyTeam.getOrDefault(userUUID, userUUID);

        // Get the teams total energy stored. If they are not in the map, return 0 EU.
        BigInteger totalEU = GlobalEnergy.getOrDefault(teamUUID, BigInteger.ZERO);
        totalEU = totalEU.add(EU);

        // If there is sufficient EU then complete the operation and return true.
        if (totalEU.signum() >= 0) {
            GlobalEnergy.put(teamUUID, totalEU);
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

    default BigInteger getUserEU(String user_uuid) {
        return GlobalEnergy.getOrDefault(GlobalEnergyTeam.getOrDefault(user_uuid, user_uuid), BigInteger.ZERO);
    }

    // This overwrites the EU in the network. Only use this if you are absolutely sure you know what you are doing.
    default void setUserEU(String user_uuid, BigInteger EU) {
        // Mark the data as dirty and in need of saving.
        try {
            GlobalEnergyWorldSavedData.INSTANCE.markDirty();
        } catch (Exception exception) {
            System.out.println("COULD NOT MARK GLOBAL ENERGY AS DIRTY IN SET EU");
            exception.printStackTrace();
        }

        GlobalEnergy.put(GlobalEnergyTeam.get(user_uuid), EU);
    }

    default String GetUsernameFromUUID(String uuid) {
        return GlobalEnergyName.getOrDefault(GlobalEnergyTeam.getOrDefault(uuid, ""), "");
    }

    default String getUUIDFromUsername(String username) {
        return GlobalEnergyTeam.getOrDefault(GlobalEnergyName.getOrDefault(username, ""), "");
    }

    /**
     *
     * @param username
     * @return
     */
    default String getRawUUIDFromUsername(String username) {
        return GlobalEnergyName.getOrDefault(username, "");
    }

    static void clearGlobalEnergyInformationMaps() {
        // Do not use this unless you are 100% certain you know what you are doing.
        GlobalEnergy.clear();
        GlobalEnergyName.clear();
        GlobalEnergyTeam.clear();
    }

    default String processInitialSettings(final IGregTechTileEntity machine) {

        // UUID and username of the owner.
        final String UUID = machine.getOwnerUuid()
            .toString();
        final String name = machine.getOwnerName();

        strongCheckOrAddUser(UUID, name);
        return UUID;
    }
}
