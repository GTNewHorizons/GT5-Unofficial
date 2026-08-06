package gregtech.crossmod.logisticspipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.PassthroughChainWalker;
import gregtech.api.util.PassthroughChainWalker.StepKind;
import gregtech.common.config.MachineStats;
import gregtech.common.tileentities.machines.basic.MTECleanroomPassthroughHull;
import logisticspipes.interfaces.routing.ISpecialTileConnection;
import logisticspipes.logisticspipes.IRoutedItem;
import logisticspipes.pipes.basic.CoreRoutedPipe;
import logisticspipes.pipes.basic.LogisticsTileGenericPipe;

/**
 * Lets LP routing see through a cleanroom pass-through hull, and through a straight chain of them, the same way it sees
 * through an EnderIO tesseract.
 */
public class CleanroomPassthroughLPConnection implements ISpecialTileConnection {

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public boolean isType(TileEntity tile) {
        return MTECleanroomPassthroughHull.isPassthroughHull(tile);
    }

    @Override
    public Collection<TileEntity> getConnections(TileEntity tile) {
        ForgeDirection front = frontOf(tile);
        if (front == ForgeDirection.UNKNOWN) return new ArrayList<>(0);

        LogisticsTileGenericPipe a = findPipe(tile, front);
        LogisticsTileGenericPipe b = findPipe(tile, front.getOpposite());
        if (a == null || b == null) return new ArrayList<>(0);

        // PathFinder already has the origin pipe in its visited set, so returning both ends is safe.
        List<TileEntity> list = new ArrayList<>(2);
        list.add(a);
        list.add(b);
        return list;
    }

    @Override
    public boolean needsInformationTransition() {
        return true;
    }

    @Override
    public void transmit(TileEntity tile, IRoutedItem arrivingItem) {
        for (TileEntity end : getConnections(tile)) {
            if (end instanceof LogisticsTileGenericPipe pipe && pipe.pipe instanceof CoreRoutedPipe routed) {
                routed.queueUnroutedItemInformation(
                    arrivingItem.getItemIdentifierStack()
                        .clone(),
                    arrivingItem.getInfo());
            }
        }
    }

    private static ForgeDirection frontOf(TileEntity tile) {
        if (tile instanceof IGregTechTileEntity gte) {
            return gte.getFrontFacing();
        }
        return ForgeDirection.UNKNOWN;
    }

    /** Walks the hull chain along {@code side} and returns the routed LP pipe at the end, or null. */
    public static LogisticsTileGenericPipe findPipe(TileEntity origin, ForgeDirection side) {
        World world = origin.getWorldObj();
        if (world == null) return null;
        int step = PassthroughChainWalker
            .walk(n -> kindAt(world, origin, side, n), MachineStats.cleanroom.passthroughChainLimit);
        if (step < 1) return null;
        return (LogisticsTileGenericPipe) tileAt(world, origin, side, step);
    }

    private static StepKind kindAt(World world, TileEntity origin, ForgeDirection side, int step) {
        TileEntity tile = tileAt(world, origin, side, step);
        if (tile instanceof LogisticsTileGenericPipe pipe && pipe.pipe instanceof CoreRoutedPipe) {
            return StepKind.ENDPOINT;
        }
        if (MTECleanroomPassthroughHull.isChainableHull(tile, side)) return StepKind.HULL;
        return StepKind.OTHER;
    }

    private static TileEntity tileAt(World world, TileEntity origin, ForgeDirection side, int step) {
        return world.getTileEntity(
            origin.xCoord + side.offsetX * step,
            origin.yCoord + side.offsetY * step,
            origin.zCoord + side.offsetZ * step);
    }
}
