package gregtech.common.misc;

import static gregtech.common.misc.GlobalVariableStorage.GlobalEnergy;

import java.math.BigInteger;
import java.util.UUID;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

public class WirelessNetworkManager {

    private WirelessNetworkManager() {}

    public static void strongCheckOrAddUser(UUID user_uuid) {
        SpaceProjectManager.checkOrCreateTeam(user_uuid);
        user_uuid = SpaceProjectManager.getLeader(user_uuid);
        if (!GlobalEnergy.containsKey(user_uuid)) {
            GlobalEnergy.put(user_uuid, BigInteger.ZERO);
        }
    }

    // ------------------------------------------------------------------------------------
    // Add EU to the users global energy. You can enter a negative number to subtract it.
    // If the value goes below 0 it will return false and not perform the operation.
    // BigIntegers have much slower operations than longs/ints. You should call these methods
    // as infrequently as possible and bulk store values to add to the global map.
    public static boolean addEUToGlobalEnergyMap(UUID user_uuid, BigInteger EU) {
        // Mark the data as dirty and in need of saving.
        try {
            GlobalEnergyWorldSavedData.INSTANCE.markDirty();
        } catch (Exception exception) {
            System.out.println("COULD NOT MARK GLOBAL ENERGY AS DIRTY IN ADD EU");
            exception.printStackTrace();
        }

        // Get the team UUID. Users are by default in a team with a UUID equal to their player UUID.
        UUID teamUUID = SpaceProjectManager.getLeader(user_uuid);

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

    public static boolean addEUToGlobalEnergyMap(UUID user_uuid, long EU) {
        return addEUToGlobalEnergyMap(user_uuid, BigInteger.valueOf(EU));
    }

    public static boolean addEUToGlobalEnergyMap(UUID user_uuid, int EU) {
        return addEUToGlobalEnergyMap(user_uuid, BigInteger.valueOf(EU));
    }

    // Ticks between energy additions to the hatch. For a dynamo this is how many ticks between energy being consumed
    // and added to the global energy map.
    public static long ticks_between_energy_addition = 100L * 20L;

    // Total number of energy additions this multi can store before it is full.
    public static long number_of_energy_additions = 4L;

    public static long totalStorage(long tier_eu_per_tick) {
        return tier_eu_per_tick * ticks_between_energy_addition * number_of_energy_additions;
    }

    // ------------------------------------------------------------------------------------

    public static BigInteger getUserEU(UUID user_uuid) {
        return GlobalEnergy.getOrDefault(SpaceProjectManager.getLeader(user_uuid), BigInteger.ZERO);
    }

    // This overwrites the EU in the network. Only use this if you are absolutely sure you know what you are doing.
    public static void setUserEU(UUID user_uuid, BigInteger EU) {
        // Mark the data as dirty and in need of saving.
        try {
            GlobalEnergyWorldSavedData.INSTANCE.markDirty();
        } catch (Exception exception) {
            System.out.println("COULD NOT MARK GLOBAL ENERGY AS DIRTY IN SET EU");
            exception.printStackTrace();
        }

        GlobalEnergy.put(SpaceProjectManager.getLeader(user_uuid), EU);
    }

    public static void clearGlobalEnergyInformationMaps() {
        // Do not use this unless you are 100% certain you know what you are doing.
        GlobalEnergy.clear();
    }

    public static UUID processInitialSettings(final IGregTechTileEntity machine) {

        // UUID and username of the owner.
        final UUID UUID = machine.getOwnerUuid();

        SpaceProjectManager.checkOrCreateTeam(UUID);
        return UUID;
    }
}
