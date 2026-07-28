package gregtech.api.events;

import java.util.UUID;

import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.common.eventhandler.Event;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongLists;

/** Fired after an ore drill scans an area for ore blocks. */
public class OreDrillScanEvent extends Event {

    /** Traversal performed by the drill for this scan. */
    public enum ScanType {
        BLOCK,
        LAYER,
        CHUNK_COLUMN
    }

    public final @NotNull World world;
    public final @NotNull UUID owner;
    public final @NotNull ScanType scanType;

    /** Inclusive lower bounds of the scanned area. */
    public final int minX, minY, minZ;

    /** Exclusive upper bounds of the scanned area. */
    public final int maxX, maxY, maxZ;

    /** Ore block positions packed with {@code CoordinatePacker}. */
    public final @NotNull LongList orePositions;

    public OreDrillScanEvent(@NotNull World world, @NotNull UUID owner, @NotNull ScanType scanType, int minX, int minY,
        int minZ, int maxX, int maxY, int maxZ, @NotNull LongList orePositions) {
        this.world = world;
        this.owner = owner;
        this.scanType = scanType;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.orePositions = orePositions.isEmpty() ? LongLists.EMPTY_LIST
            : LongLists.unmodifiable(new LongArrayList(orePositions));
    }
}
