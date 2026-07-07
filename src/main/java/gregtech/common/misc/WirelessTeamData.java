package gregtech.common.misc;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizon.gtnhlib.teams.ITeamData;
import com.gtnewhorizon.gtnhlib.teams.Team;
import com.gtnewhorizon.gtnhlib.teams.TeamDataTransferReason;

import gregtech.api.util.GTRecipe;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;

public class WirelessTeamData implements ITeamData {

    public static final String DATA_KEY = "wirelessNetwork";
    public static final String ENERGY_TAG = "energy";
    public static final long DOWNLOAD_TICK_OFFSET = 25;
    public static final long IO_TICK_RATE = 200;

    BigInteger wirelessEnergy = BigInteger.ZERO;

    /**
     * This data is volatile on purpose and should not be saved to disk,
     * as it's technically already stored within each providing databank & wireless databank connector hatch.
     * It's supposed to be loaded {@value DOWNLOAD_TICK_OFFSET} ticks after the first tick of the loaded databanks after
     * the world was loaded.
     * Structure: {@literal <long packedHatchCoord, Set<AsslineRecipe>>}
     */
    private final Long2ObjectOpenHashMap<OwnedSet> wirelessDataStricks = new Long2ObjectOpenHashMap<>(8, 0.9f);
    /**
     * Indicator for each wireless assembly line connector hatch that it may update at the next checking interval of
     * ticks % {@value IO_TICK_RATE} = {@value DOWNLOAD_TICK_OFFSET}.
     * Accessed via {@link #hasDirtySticks()} by the assembly lines.
     */
    private boolean dirtySticks = false;

    private int registeredDataOutputs = 0;
    private int downloadCounter = 0;
    private ObjectOpenHashSet<GTRecipe.RecipeAssemblyLine> cached = new ObjectOpenHashSet<>(100, 0.9f);

    /**
     * If as many registered outputs "download" the cache, it's marked as clean.
     */
    public ObjectOpenHashSet<GTRecipe.RecipeAssemblyLine> downloadDatasticks() {
        if (++downloadCounter >= registeredDataOutputs) {
            dirtySticks = false;
            downloadCounter = 0;
        } else {
            // cache the flattening after first "download" to avoid re-flattening per downloading assline.
            cached = wirelessDataStricks.long2ObjectEntrySet()
                .stream()
                .flatMap((entry) -> entry.getValue().set.stream())
                .collect(ObjectOpenHashSet::new, ObjectOpenHashSet::add, ObjectOpenHashSet::addAll);
        }
        return cached;
    }

    /**
     * Marks the datastick cache as dirty and adds the dataPacket to the cached set.
     *
     * @param dataPacket null only resets the cache, non-null also adds the dataPacket to the cache
     */
    public void uploadDatastick(long coord, ALRecipeDataPacket dataPacket, UUID ownerUUID) {
        OwnedSet atHatch = this.wirelessDataStricks
            .computeIfAbsent(coord, _ -> new OwnedSet(new ObjectOpenHashSet<>(100, 0.9f), ownerUUID));
        if (!dirtySticks) {
            atHatch.set.clear();
            dirtySticks = true;
        }
        if (dataPacket != null) {
            atHatch.set.addAll(Arrays.asList(dataPacket.getContent()));
        }
    }

    public boolean hasDirtySticks() {
        return dirtySticks;
    }

    public void registerDataOutput() {
        registeredDataOutputs = registeredDataOutputs + 1;
    }

    public void unregisterDataOutput() {
        registeredDataOutputs = Math.max(0, registeredDataOutputs - 1);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setString(ENERGY_TAG, wirelessEnergy.toString());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        wirelessEnergy = new BigInteger(tag.getString(ENERGY_TAG));
    }

    @Override
    public void mergeData(Team consumed, Team surviving, ITeamData oldTeamData) {
        // do nothing for now
    }

    @Override
    public void transferData(Team prevTeam, Team newTeam, UUID playerId, ITeamData prevTeamData,
        TeamDataTransferReason reason) {
        if (!(prevTeamData instanceof WirelessTeamData prevWirelessTeamData)) return;

        LongList toRemove = new LongArrayList(8);
        for (Long2ObjectMap.Entry<OwnedSet> entry : prevWirelessTeamData.wirelessDataStricks.long2ObjectEntrySet()) {
            if (entry.getValue().owner.equals(playerId)) {
                toRemove.add(entry.getLongKey());
                wirelessDataStricks.put(entry.getLongKey(), entry.getValue());
            }
        }
        prevWirelessTeamData.registeredDataOutputs -= toRemove.size();
        prevWirelessTeamData.downloadCounter = 0;
        prevWirelessTeamData.dirtySticks = true;
        this.registeredDataOutputs += toRemove.size();
        this.downloadCounter = 0;
        this.dirtySticks = true;
        toRemove.forEach(wirelessDataStricks::remove);
    }

    private record OwnedSet(ObjectOpenHashSet<GTRecipe.RecipeAssemblyLine> set, UUID owner) {}
}
