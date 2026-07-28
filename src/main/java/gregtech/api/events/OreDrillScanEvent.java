package gregtech.api.events;

import java.util.UUID;

import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.common.eventhandler.Event;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongLists;

/** Fired after an ore drill finds ore blocks during a scan. */
public class OreDrillScanEvent extends Event {

    public final @NotNull World world;
    public final @NotNull UUID owner;

    /** Ore block positions packed with {@code CoordinatePacker}. */
    public final @NotNull LongList orePositions;

    public OreDrillScanEvent(@NotNull World world, @NotNull UUID owner, @NotNull LongList orePositions) {
        this.world = world;
        this.owner = owner;
        this.orePositions = LongLists.unmodifiable(new LongArrayList(orePositions));
    }
}
