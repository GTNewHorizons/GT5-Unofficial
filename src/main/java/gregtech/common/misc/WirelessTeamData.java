package gregtech.common.misc;

import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizon.gtnhlib.teams.ITeamData;
import com.gtnewhorizon.gtnhlib.teams.Team;
import com.gtnewhorizon.gtnhlib.teams.TeamDataTransferReason;

import gregtech.api.util.GTRecipe;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public class WirelessTeamData implements ITeamData {

    public static final String DATA_KEY = "wirelessNetwork";
    public static final String ENERGY_TAG = "energy";
    public static final long DOWNLOAD_TICK_OFFSET = 25;
    public static final long IO_TICK_RATE = 200;

    BigInteger wirelessEnergy = BigInteger.ZERO;

    /**
     *  This data is volatile on purpose and should not be saved to disk,
     *  as it's technically already stored within each providing databank & wireless databank connector hatch.
     *  It's supposed to be loaded {@value DOWNLOAD_TICK_OFFSET} ticks after the first tick of the loaded databanks after the world was loaded.
     *  Structure: {@literal <long packedHatchCoord, Set<AsslineRecipe>>}
     */
    private final Long2ObjectOpenHashMap<ObjectOpenHashSet<GTRecipe.RecipeAssemblyLine>> wirelessDataStricks = new Long2ObjectOpenHashMap<>(
        8,
        0.9f);
    /**
     * Indicator for each wireless assembly line connector hatch that it may update at the next checking interval of ticks % {@value IO_TICK_RATE} = {@value DOWNLOAD_TICK_OFFSET}.
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
                .flatMap(
                    (entry) -> entry.getValue()
                        .stream())
                .collect(ObjectOpenHashSet::new, ObjectOpenHashSet::add, ObjectOpenHashSet::addAll);
        }
        return cached;
    }

    /**
     * Marks the datastick cache as dirty and adds the stick to the cached set.
     *
     * @param stick null only resets the cache, non-null also adds the stick to the cache
     */
    public void uploadDatastick(long coord, GTRecipe.RecipeAssemblyLine stick) {
        ObjectOpenHashSet<GTRecipe.RecipeAssemblyLine> atHatch = this.wirelessDataStricks
            .computeIfAbsent(coord, _ -> new ObjectOpenHashSet<>(100, 0.9f));
        if (!dirtySticks) {
            atHatch.clear();
            dirtySticks = true;
        }
        if (stick != null) {
            atHatch.add(stick);
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
        // do nothing for now
    }
}
