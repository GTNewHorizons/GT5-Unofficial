package gregtech.api.interfaces;

import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.misc.WirelessNetworkManager;

// If you are adding very late-game content feel free to tap into this interface.
// The eventual goal is to bypass laser/dynamo stuff and have energy deposited directly from ultra-endgame
// multi-blocks directly into the users network.
/**
 * Use WirelessNetworkManager instead
 */
@Deprecated
public interface IGlobalWirelessEnergy {

    // User 0 will join user 1 by calling this function. They will share the same energy network.
    default void joinUserNetwork(String user_uuid_0, String user_uuid_1) {
        WirelessNetworkManager.joinUserNetwork(user_uuid_0, user_uuid_1);
    }

    // Adds a user to the energy map if they do not already exist. Otherwise, do nothing. Will also check if the user
    // has changed their username and adjust the maps accordingly. This should be called infrequently. Ideally on first
    // tick of a machine being placed only.

    default void strongCheckOrAddUser(EntityPlayer user) {
        WirelessNetworkManager.strongCheckOrAddUser(
            user.getUniqueID()
                .toString(),
            user.getDisplayName());
    }

    default void strongCheckOrAddUser(UUID user_uuid, String user_name) {
        WirelessNetworkManager.strongCheckOrAddUser(user_uuid.toString(), user_name);
    }

    default void strongCheckOrAddUser(String user_uuid, String user_name) {
        WirelessNetworkManager.strongCheckOrAddUser(user_uuid, user_name);
    }

    // ------------------------------------------------------------------------------------
    // Add EU to the users global energy. You can enter a negative number to subtract it.
    // If the value goes below 0 it will return false and not perform the operation.
    // BigIntegers have much slower operations than longs/ints. You should call these methods
    // as infrequently as possible and bulk store values to add to the global map.
    default boolean addEUToGlobalEnergyMap(String userUUID, BigInteger EU) {
        return WirelessNetworkManager.addEUToGlobalEnergyMap(userUUID, EU);
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
        return WirelessNetworkManager.getUserEU(user_uuid);
    }

    // This overwrites the EU in the network. Only use this if you are absolutely sure you know what you are doing.
    default void setUserEU(String user_uuid, BigInteger EU) {
        WirelessNetworkManager.setUserEU(user_uuid, EU);
    }

    default String GetUsernameFromUUID(String uuid) {
        return WirelessNetworkManager.getUsernameFromUUID(uuid);
    }

    default String getUUIDFromUsername(String username) {
        return WirelessNetworkManager.getUUIDFromUsername(username);
    }

    /**
     *
     * @param username
     * @return
     */
    default String getRawUUIDFromUsername(String username) {
        return WirelessNetworkManager.getRawUUIDFromUsername(username);
    }

    static void clearGlobalEnergyInformationMaps() {
        WirelessNetworkManager.clearGlobalEnergyInformationMaps();
    }

    default String processInitialSettings(final IGregTechTileEntity machine) {
        return WirelessNetworkManager.processInitialSettings(machine);
    }
}
