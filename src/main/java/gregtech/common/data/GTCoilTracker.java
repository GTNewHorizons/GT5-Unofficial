package gregtech.common.data;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkWatchEvent;

import com.google.common.collect.MapMaker;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.net.GTCoilStatus;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

/**
 * This class tracks all active heating coils. Each instance is responsible for one world. The main reason this exists
 * is so that wallshare coils deactivate properly. Without the ref counter, wallshared coils would deactivate incorrect
 * when one multi deactivates but not the other.
 * <p />
 * Additionally, it gives us more control over the activation syncing. Since coil activation meta is only stored on the
 * client, there needs to be a way to sync it from the server. Coil meta is only stored on the client to prevent weird
 * interactions with e.g. the structure checker and so that we have more control over the process. We don't want to
 * check structures every time the coils activate, for instance. The reduction in network bandwidth and CPU usage (since
 * we aren't sending chunk updates) is a nice side effect.
 * <p />
 * Since the coil list is produced by structure checks, and structure checks only run on the server, syncing that info
 * is the easiest way to tell the client which coils are active.
 */
@EventBusSubscriber
public class GTCoilTracker {

    private static final Map<World, GTCoilTracker> TRACKERS = new MapMaker().weakKeys()
        .makeMap();

    @SuppressWarnings("unused")
    private final WeakReference<World> world;
    private final WeakReference<GTCoilTracker> self;

    /**
     * Ref counter to make sure there aren't any race conditions/conflicts when deactivating coils. {packed x,y,z: ref
     * count}
     */
    public final Long2IntOpenHashMap activeBlocks = new Long2IntOpenHashMap();

    /**
     * Used to send active coils when the player views a new chunk. {packed chunk x,0,chunk z: set of packed x,y,z
     * active coils}
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
     * Used to prevent duplicate lease registrations by the same multi. {multi reference: lease reference}
     */
    private final Reference2ReferenceMap<MTEMultiBlockBase, MultiCoilLease> leasesByMulti = new Reference2ReferenceOpenHashMap<>();

    private static final Long2ObjectFunction<LongSet> CHUNK_LIST_CTOR = ignored -> new LongOpenHashSet();

    /**
     * An object that represents an activated set of coils. These are always owned by a multi, and must be deactivated
     * at some point or the coils will remain active forever.
     *
     * @see GTCoilTracker#activate(MTEMultiBlockBase, LongList)
     * @see GTCoilTracker#deactivate(MultiCoilLease)
     */
    public static class MultiCoilLease {

        // intentionally package private
        final WeakReference<GTCoilTracker> tracker;
        final WeakReference<MTEMultiBlockBase> multi;
        final LongList coils;

        MultiCoilLease(WeakReference<GTCoilTracker> tracker, MTEMultiBlockBase multi, LongList coils) {
            this.tracker = tracker;
            this.multi = new WeakReference<>(multi);
            this.coils = coils;
        }
    }

    private GTCoilTracker(World world) {
        this.self = new WeakReference<>(this);
        this.world = new WeakReference<>(world);
    }

    private MultiCoilLease activateImpl(MTEMultiBlockBase multi, LongList coils) {
        MultiCoilLease lease = new MultiCoilLease(self, multi, new LongArrayList(coils));

        MultiCoilLease existing = leasesByMulti.put(multi, lease);

        if (existing != null) {
            deactivateImpl(existing);
        }

        for (long coil : coils) {
            activate(coil);
        }

        return lease;
    }

    private void deactivateImpl(MultiCoilLease lease) {
        for (long coil : lease.coils) {
            deactivate(coil);
        }

        // remove the coils so that this lease can't be double-deactivated
        lease.coils.clear();

        MTEMultiBlockBase multi = lease.multi.get();

        if (multi != null) {
            leasesByMulti.remove(multi);
        }
    }

    private void activate(long coil) {
        int old = activeBlocks.addTo(coil, 1);

        // if this coil wasn't activated by any multis and we just activated it, then the state changed
        if (old == 0) {
            pendingActivations.add(coil);
            pendingDeactivations.remove(coil);

            // maybe there's a more efficient way to do this, but I couldn't figure out how to shove a chunk coord into
            // a long
            long chunk = CoordinatePacker
                .pack(CoordinatePacker.unpackX(coil) >> 4, 0, CoordinatePacker.unpackZ(coil) >> 4);

            activeBlocksByChunk.computeIfAbsent(chunk, CHUNK_LIST_CTOR)
                .add(coil);
        }
    }

    private void deactivate(long coil) {
        int old = activeBlocks.addTo(coil, -1);

        // if this coil was only activated by 1 multi, and we just deactivated it, then the state changed
        if (old == 1) {
            pendingDeactivations.add(coil);
            pendingActivations.remove(coil);

            long chunk = CoordinatePacker
                .pack(CoordinatePacker.unpackX(coil) >> 4, 0, CoordinatePacker.unpackZ(coil) >> 4);

            LongSet list = activeBlocksByChunk.computeIfAbsent(chunk, CHUNK_LIST_CTOR);

            list.remove(coil);

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

    // sends the status of coils in a chunk to a player who just started viewing a chunk
    @SubscribeEvent
    public static void sendChunkStatus(ChunkWatchEvent.Watch event) {
        GTCoilTracker tracker = TRACKERS.get(event.player.worldObj);

        if (tracker == null) return;

        long chunk = CoordinatePacker.pack(event.chunk.chunkXPos, 0, event.chunk.chunkZPos);

        LongSet active = tracker.activeBlocksByChunk.get(chunk);

        if (active != null && !active.isEmpty()) {
            GTCoilStatus packet = new GTCoilStatus(event.player.worldObj.provider.dimensionId, true, active);

            GTValues.NW.sendToPlayer(packet, event.player);
        }
    }

    private static final Function<World, GTCoilTracker> TRACKER_CTOR = GTCoilTracker::new;

    /**
     * Activates the given list of coils. A multi cannot have more than one lease at a time, and if two sets of coils
     * are activated, the oldest lease is deactivated automatically.
     *
     * @param multi The owning multi
     * @param coils The list of coils that should be activated
     * @return A lease owned by the given multi that should be deactivated when needed (see
     *         {@link #deactivate(MultiCoilLease)}
     */
    public static MultiCoilLease activate(MTEMultiBlockBase multi, LongList coils) {
        IGregTechTileEntity base = multi.getBaseMetaTileEntity();

        if (base == null || base.isDead()) return null;

        return TRACKERS.computeIfAbsent(base.getWorld(), TRACKER_CTOR)
            .activateImpl(multi, coils);
    }

    /**
     * Deactivates a set of coils. If the multi is destroyed and this is never called, the coils will remain active
     * until the server restarts or the world is garbage collected.
     *
     * @param lease The lease.
     */
    public static void deactivate(MultiCoilLease lease) {
        GTCoilTracker tracker = lease.tracker.get();

        if (tracker != null) {
            tracker.deactivateImpl(lease);
        }
    }

    /**
     * Checks whether a specific coil in a specific world is active or not.
     *
     * @return True when active, false otherwise.
     */
    public static boolean isCoilActive(World world, int x, int y, int z) {
        GTCoilTracker tracker = TRACKERS.get(world);

        if (tracker == null) return false;

        long coord = CoordinatePacker.pack(x, y, z);

        int refs = tracker.activeBlocks.get(coord);

        return refs > 0;
    }

    public static GTCoilTracker getTracker(World world) {
        return TRACKERS.get(world);
    }
}
