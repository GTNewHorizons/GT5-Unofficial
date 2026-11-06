package gregtech.common.data;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkWatchEvent;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Table;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.enums.CoilLeaseType;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.net.GTCoilStatus;
import gregtech.api.structure.ActiveBlockLeaseType;
import gregtech.api.structure.ActiveBlockLeaseTypeRegistry;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

/**
 * This class tracks all active structure blocks, such as heating coils and fireboxes. Each instance is responsible for
 * one world. The main reason this exists is so that wallshared blocks deactivate properly. Without the ref counter,
 * wallshared blocks would deactivate incorrectly when one multi deactivates but not the other.
 * <p />
 * Additionally, it gives us more control over the activation syncing. Since activation meta is only stored on the
 * client, there needs to be a way to sync it from the server. Activation meta is only stored on the client to prevent
 * weird interactions with e.g. the structure checker and so that we have more control over the process. We don't want
 * to check structures every time the blocks activate, for instance. The reduction in network bandwidth and CPU usage
 * (since we aren't sending chunk updates) is a nice side effect.
 * <p />
 * Since the block list is produced by structure checks, and structure checks only run on the server, syncing that info
 * is the easiest way to tell the client which blocks are active.
 */
@EventBusSubscriber
public class GTActiveBlockTracker {

    private static final Map<World, GTActiveBlockTracker> TRACKERS = new MapMaker().weakKeys()
        .makeMap();

    @SuppressWarnings("unused")
    private final WeakReference<World> world;
    private final WeakReference<GTActiveBlockTracker> self;

    /**
     * Ref counter to make sure there aren't any race conditions/conflicts when deactivating blocks. {packed x,y,z: ref
     * count}
     */
    public final Long2IntOpenHashMap activeBlocks = new Long2IntOpenHashMap();

    /**
     * Used to send active blocks when the player views a new chunk. {packed chunk x,0,chunk z: set of packed x,y,z
     * active blocks}
     */
    public final Long2ReferenceOpenHashMap<LongSet> activeBlocksByChunk = new Long2ReferenceOpenHashMap<>();

    /**
     * Used to sync activations to clients in one packet. [packed x,y,z]
     */
    private LongSet pendingActivations = new LongOpenHashSet();

    /**
     * Used to sync deactivations to clients in one packet. [packed x,y,z]
     */
    private LongSet pendingDeactivations = new LongOpenHashSet();

    /**
     * Used to prevent duplicate lease registrations by the same multi. {multi reference, lease type: lease reference}
     */
    private final Table<MTEMultiBlockBase, ActiveBlockLeaseType, ActiveBlockLease> leasesByMulti = HashBasedTable
        .create();

    private static final Long2ObjectFunction<LongSet> CHUNK_LIST_CTOR = ignored -> new LongOpenHashSet();

    /**
     * An object that represents an activated set of blocks. These are always owned by a multi, and must be deactivated
     * at some point or the blocks will remain active forever.
     *
     * @see GTActiveBlockTracker#activate(MTEMultiBlockBase, ActiveBlockLeaseType, LongList)
     * @see GTActiveBlockTracker#deactivate(ActiveBlockLease)
     */
    public static class ActiveBlockLease {

        private final WeakReference<GTActiveBlockTracker> tracker;
        private final WeakReference<MTEMultiBlockBase> multi;
        private final ActiveBlockLeaseType type;
        private final LongList blocks;

        private ActiveBlockLease(WeakReference<GTActiveBlockTracker> tracker, MTEMultiBlockBase multi,
            ActiveBlockLeaseType type, LongList blocks) {
            this.tracker = tracker;
            this.multi = new WeakReference<>(multi);
            this.type = type;
            this.blocks = blocks;
        }

        public ActiveBlockLeaseType getType() {
            return type;
        }
    }

    private GTActiveBlockTracker(World world) {
        this.self = new WeakReference<>(this);
        this.world = new WeakReference<>(world);
    }

    private ActiveBlockLease activateImpl(MTEMultiBlockBase multi, ActiveBlockLeaseType type, LongList blocks) {
        ActiveBlockLease lease = new ActiveBlockLease(self, multi, type, new LongArrayList(blocks));

        ActiveBlockLease existing = leasesByMulti.put(multi, type, lease);

        if (existing != null) {
            deactivateImpl(existing);
        }

        for (long block : blocks) {
            activate(block);
        }

        return lease;
    }

    private void deactivateImpl(ActiveBlockLease lease) {
        for (long block : lease.blocks) {
            deactivate(block);
        }

        // remove the blocks so that this lease can't be double-deactivated
        lease.blocks.clear();

        MTEMultiBlockBase multi = lease.multi.get();

        // only drop the mapping if it still points at this lease, a replacing lease may already occupy it
        if (multi != null && leasesByMulti.get(multi, lease.type) == lease) {
            leasesByMulti.remove(multi, lease.type);
        }
    }

    private void activate(long block) {
        int old = activeBlocks.addTo(block, 1);

        // if this block wasn't activated by any multis and we just activated it, then the state changed
        if (old == 0) {
            pendingActivations.add(block);
            pendingDeactivations.remove(block);

            // maybe there's a more efficient way to do this, but I couldn't figure out how to shove a chunk coord into
            // a long
            long chunk = CoordinatePacker
                .pack(CoordinatePacker.unpackX(block) >> 4, 0, CoordinatePacker.unpackZ(block) >> 4);

            activeBlocksByChunk.computeIfAbsent(chunk, CHUNK_LIST_CTOR)
                .add(block);
        }
    }

    private void deactivate(long block) {
        int old = activeBlocks.addTo(block, -1);

        // if this block was only activated by 1 multi, and we just deactivated it, then the state changed
        if (old == 1) {
            pendingDeactivations.add(block);
            pendingActivations.remove(block);

            long chunk = CoordinatePacker
                .pack(CoordinatePacker.unpackX(block) >> 4, 0, CoordinatePacker.unpackZ(block) >> 4);

            LongSet list = activeBlocksByChunk.computeIfAbsent(chunk, CHUNK_LIST_CTOR);

            list.remove(block);

            if (list.isEmpty()) {
                activeBlocksByChunk.remove(chunk);
            }
        }
    }

    // subscribe statically so that FML doesn't keep a reference to trackers
    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.side != Side.SERVER) return;

        TRACKERS.forEach((world, tracker) -> {
            if (!tracker.pendingActivations.isEmpty()) {
                GTCoilStatus packet = new GTCoilStatus(world.provider.dimensionId, true, tracker.pendingActivations);

                // I have no idea if packet encoding is immediate
                // Let's just allocate another list instead of finding out
                tracker.pendingActivations = new LongOpenHashSet();

                GTValues.NW.sendToWorld(world, packet);
            }

            if (!tracker.pendingDeactivations.isEmpty()) {
                GTCoilStatus packet = new GTCoilStatus(world.provider.dimensionId, false, tracker.pendingDeactivations);

                tracker.pendingDeactivations = new LongOpenHashSet();

                GTValues.NW.sendToWorld(world, packet);
            }
        });
    }

    // sends the status of blocks in a chunk to a player who just started viewing a chunk
    @SubscribeEvent
    public static void sendChunkStatus(ChunkWatchEvent.Watch event) {
        GTActiveBlockTracker tracker = TRACKERS.get(event.player.worldObj);

        if (tracker == null) return;

        long chunk = CoordinatePacker.pack(event.chunk.chunkXPos, 0, event.chunk.chunkZPos);

        LongSet active = tracker.activeBlocksByChunk.get(chunk);

        if (active != null && !active.isEmpty()) {
            GTCoilStatus packet = new GTCoilStatus(event.player.worldObj.provider.dimensionId, true, active);

            GTValues.NW.sendToPlayer(packet, event.player);
        }
    }

    private static final Function<World, GTActiveBlockTracker> TRACKER_CTOR = GTActiveBlockTracker::new;

    /**
     * Activates the given blocks under the {@link ActiveBlockLeaseTypeRegistry#DEFAULT default} lease type.
     *
     * @see #activate(MTEMultiBlockBase, ActiveBlockLeaseType, LongList)
     */
    public static ActiveBlockLease activate(MTEMultiBlockBase multi, LongList blocks) {
        return activate(multi, ActiveBlockLeaseTypeRegistry.DEFAULT, blocks);
    }

    /**
     * Activates the given list of blocks. A multi cannot have more than one lease per lease type at a time, and if two
     * sets of blocks are activated under the same type, the oldest lease is deactivated automatically.
     *
     * @param multi  The owning multi
     * @param type   The lease type the blocks are activated under
     * @param blocks The list of blocks that should be activated
     * @return A lease owned by the given multi that should be deactivated when needed (see
     *         {@link #deactivate(ActiveBlockLease)}), or null if the multi is not in a loaded world
     */
    public static ActiveBlockLease activate(MTEMultiBlockBase multi, ActiveBlockLeaseType type, LongList blocks) {
        IGregTechTileEntity base = multi.getBaseMetaTileEntity();

        if (base == null || base.isDead()) return null;

        return TRACKERS.computeIfAbsent(base.getWorld(), TRACKER_CTOR)
            .activateImpl(multi, type, blocks);
    }

    /** @deprecated Use {@link #activate(MTEMultiBlockBase, ActiveBlockLeaseType, LongList)}. */
    @Deprecated
    public static ActiveBlockLease activate(MTEMultiBlockBase multi, CoilLeaseType coilType, LongList coils) {
        return activate(multi, coilType.toLeaseType(), coils);
    }

    /**
     * Deactivates a set of blocks. If the multi is destroyed and this is never called, the blocks will remain active
     * until the server restarts or the world is garbage collected.
     *
     * @param lease The lease.
     */
    public static void deactivate(ActiveBlockLease lease) {
        GTActiveBlockTracker tracker = lease.tracker.get();

        if (tracker != null) {
            tracker.deactivateImpl(lease);
        }
    }

    /** @deprecated Use {@link #deactivate(ActiveBlockLease)}, the lease already knows its type. */
    @Deprecated
    public static void deactivate(ActiveBlockLease lease, CoilLeaseType coilType) {
        deactivate(lease);
    }

    /**
     * Checks whether a specific block in a specific world is active or not.
     *
     * @return True when active, false otherwise.
     */
    public static boolean isBlockActive(World world, int x, int y, int z) {
        GTActiveBlockTracker tracker = TRACKERS.get(world);

        if (tracker == null) return false;

        return tracker.activeBlocks.get(CoordinatePacker.pack(x, y, z)) > 0;
    }

    /** @deprecated Use {@link #isBlockActive(World, int, int, int)}. */
    @Deprecated
    public static boolean isCoilActive(World world, int x, int y, int z) {
        return isBlockActive(world, x, y, z);
    }

    public static GTActiveBlockTracker getTracker(World world) {
        return TRACKERS.get(world);
    }
}
