package gregtech.common.data;

import java.util.Map;

import net.minecraft.world.World;

import com.google.common.collect.MapMaker;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.enums.GTValues;
import gregtech.api.net.GTPacketBlockEvent;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;

@EventBusSubscriber
public class GTBlockEventTracker {

    private static final Map<World, GTBlockEventTracker> TRACKERS = new MapMaker().weakKeys()
        .makeMap();

    private final LongList packedCoordinates = new LongArrayList();
    private final ShortList idsAndValues = new ShortArrayList();

    public GTBlockEventTracker() {}

    public static void enqueue(World world, int xCoord, int yCoord, int zCoord, byte aID, byte aValue) {
        GTBlockEventTracker tracker = TRACKERS.computeIfAbsent(world, w -> new GTBlockEventTracker());
        tracker.packedCoordinates.add(CoordinatePacker.pack(xCoord, yCoord, zCoord));
        tracker.idsAndValues.add((short) ((aID << 8) | aValue));
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.side != Side.SERVER) return;

        TRACKERS.forEach((world, tracker) -> {
            final int eventCount = tracker.packedCoordinates.size();
            if (eventCount == 0) return;

            GTValues.NW.sendToWorld(
                world,
                new GTPacketBlockEvent(
                    world.provider.dimensionId,
                    eventCount,
                    tracker.packedCoordinates,
                    tracker.idsAndValues));

            tracker.packedCoordinates.clear();
            tracker.idsAndValues.clear();
        });
    }
}
