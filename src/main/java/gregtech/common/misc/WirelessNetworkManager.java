package gregtech.common.misc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.gtnewhorizon.gtnhlib.teams.Team;
import com.gtnewhorizon.gtnhlib.teams.TeamManager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.GTLoggers;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEWirelessEnergy;

public class WirelessNetworkManager {

    private final List<MTEWirelessEnergy> hatches = new ArrayList<>();
    private long tick = 0;

    public WirelessNetworkManager() {}

    public static void strongCheckOrAddUser(UUID user_uuid) {
        // a player always is in a team, and a teams' data always is initialized if registered.
        Team team = TeamManager.getTeamByPlayer(user_uuid);
        if (team == null) {
            GTLoggers.GT_FML_LOGGER
                .error("Player ({}) is not in a team. How? Did you forget to create a team for your test?", user_uuid);
        }
    }

    // ------------------------------------------------------------------------------------
    // region Wireless Energy
    // ------------------------------------------------------------------------------------

    // Ticks between energy additions to the hatch. For a dynamo this is how many ticks between energy being consumed
    // and added to the global energy map.
    public static final long TICKS_BETWEEN_ENERGY_ADDITION = 100L * 20L;

    // Total number of energy additions this multi can store before it is full.
    public static final long NUMBER_OF_ENERGY_ADDITIONS = 4L;

    /**
     * Add EU to a user's team global energy. Enter a negative number to subtract from it.
     * If the value goes below {@code 0} it will return {@code false} and not perform the operation.
     * {@link BigInteger BigIntegers} have much slower operations than longs/ints.
     * You should call these methods as infrequently as possible and bulk store values to add to the global map.
     */
    public static boolean addEUToGlobalEnergyMap(UUID user_uuid, BigInteger EU) {
        // LSC is a "bitch" and tries to access global energy with a null UUID.
        if (user_uuid == null) return false;
        Team team = TeamManager.getTeamByPlayer(user_uuid);
        var data = (WirelessTeamData) team.getData(WirelessTeamData.DATA_KEY);
        // this should never happen unless the data is unregistered!
        if (data == null) return false;

        // Get the teams' total energy stored. If they have none, the default team data's value 0 is returned.
        BigInteger totalEU = data.wirelessEnergy;
        totalEU = totalEU.add(EU);

        // If there is sufficient EU then complete the operation and return true.
        if (totalEU.signum() >= 0) {
            data.wirelessEnergy = totalEU;
            team.markDirty();
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

    public static long totalStorage(long tier_eu_per_tick) {
        return tier_eu_per_tick * TICKS_BETWEEN_ENERGY_ADDITION * NUMBER_OF_ENERGY_ADDITIONS;
    }

    /**
     * Get the EU of a team, any member UUID can be used. Will fetch the team for you.
     */
    public static BigInteger getUserEU(UUID user_uuid) {
        // LSC is a "bitch" and tries to access global energy with a null UUID.
        if (user_uuid == null) return BigInteger.ZERO;
        Team team = TeamManager.getTeamByPlayer(user_uuid);
        var data = (WirelessTeamData) team.getData(WirelessTeamData.DATA_KEY);
        // this should never happen unless the data is unregistered!
        if (data == null) return BigInteger.ZERO;
        return data.wirelessEnergy;
    }

    /**
     * This overwrites the EU in the network. Only use this if you are absolutely sure you know what you are doing.
     * Any member UUID can be used, will fetch the team for you.
     */
    public static void setUserEU(UUID user_uuid, BigInteger EU) {
        // LSC is a "bitch" and tries to access global energy with a null UUID.
        if (user_uuid == null) return;
        Team team = TeamManager.getTeamByPlayer(user_uuid);
        var data = (WirelessTeamData) team.getData(WirelessTeamData.DATA_KEY);
        // this should never happen unless the data is unregistered!
        if (data == null) return;
        data.wirelessEnergy = EU;
        team.markDirty();
    }

    /**
     * Do not use this unless you are 100% certain you know what you are doing.
     */
    public static void clearGlobalEnergyInformationMaps() {
        TeamManager.getTeamMap()
            .forEach((_teamUuid, team) -> {
                var data = (WirelessTeamData) team.getData(WirelessTeamData.DATA_KEY);
                data.wirelessEnergy = BigInteger.ZERO;
                team.markDirty();
            });
    }

    public void addHatch(MTEWirelessEnergy e) {
        hatches.add(e);
    }

    // endregion

    public static UUID processInitialSettings(final IGregTechTileEntity machine) {
        // UUID and username of the owner.
        final UUID UUID = machine.getOwnerUuid();

        // a player always is in a team, assert has no effect unless assertions are enabled.
        assert null != TeamManager.getTeamByPlayer(UUID);
        return UUID;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        tick++;
        // This is set up in a way to be as optimized as possible.
        // If a user has a relatively plentiful energy network, it should make no difference to them.
        // Minimizing the number of operations on BigInteger is essential.
        // Every ticks_between_energy_addition adds eu_transferred_per_operation to internal EU storage from the
        // network.
        if (tick % TICKS_BETWEEN_ENERGY_ADDITION != 0L) return;
        Iterator<MTEWirelessEnergy> it = hatches.iterator();
        while (it.hasNext()) {
            MTEWirelessEnergy hatch = it.next();
            if (!hatch.isValid()) {
                it.remove();
                continue;
            }
            hatch.tryFetchingEnergy();
        }
    }

}
