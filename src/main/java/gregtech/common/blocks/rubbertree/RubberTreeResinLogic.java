package gregtech.common.blocks.rubbertree;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class RubberTreeResinLogic {

    private static final int MIN_REFILL_TICKS = 20 * 85; // 85 seconds + ~2.5 seconds of throttle
    private static final int MAX_REFILL_TICKS = 20 * 295; // 295 seconds + ~2.5 seconds of throttle
    private static final int MAX_CONNECTED_LOGS = 64;

    private RubberTreeResinLogic() {}

    public static void tickNaturalLog(World world, int x, int y, int z) {
        List<int[]> treeLogs = collectConnectedNaturalLogs(world, x, y, z);
        if (treeLogs.isEmpty()) {
            return;
        }

        if (treeHasFilledLog(world, treeLogs)) {
            return;
        }

        long now = world.getTotalWorldTime();
        long scheduledAt = getScheduledRefillAt(world, treeLogs);

        // Bootstrap of a freshly generated/loaded tree
        if (scheduledAt == 0L) {
            scheduleTreeRefill(world, treeLogs, now + randomDelay(world.rand));
            return;
        }

        if (now < scheduledAt) {
            return;
        }

        if (fillRandomLog(world, treeLogs, world.rand)) {
            clearTreeSchedule(world, treeLogs);
        } else {
            // No valid logs visible? We'll try again later
            scheduleTreeRefill(world, treeLogs, now + 20 * 30 + world.rand.nextInt(20 * 30 + 1));
        }
    }

    public static void onResinCollected(World world, int x, int y, int z) {
        List<int[]> treeLogs = collectConnectedNaturalLogs(world, x, y, z);
        if (treeLogs.isEmpty()) {
            return;
        }

        long when = world.getTotalWorldTime() + randomDelay(world.rand);
        scheduleTreeRefill(world, treeLogs, when);
    }

    private static int randomDelay(@NotNull Random random) {
        return MIN_REFILL_TICKS + random.nextInt(MAX_REFILL_TICKS - MIN_REFILL_TICKS + 1);
    }

    private static boolean fillRandomLog(World world, @NotNull List<int[]> treeLogs, Random random) {
        List<int[]> candidateLogs = new ArrayList<>();

        for (int[] pos : treeLogs) {
            if (!getAvailableResinSides(world, pos[0], pos[1], pos[2]).isEmpty()) {
                candidateLogs.add(pos);
            }
        }

        if (candidateLogs.isEmpty()) {
            return false;
        }

        int[] chosenLog = candidateLogs.get(random.nextInt(candidateLogs.size()));
        List<Integer> sides = getAvailableResinSides(world, chosenLog[0], chosenLog[1], chosenLog[2]);
        int chosenSide = sides.get(random.nextInt(sides.size()));

        TileEntityRubberLog te = getRubberLogTileEntity(world, chosenLog[0], chosenLog[1], chosenLog[2]);
        if (te == null) {
            return false;
        }

        te.setResinSide(chosenSide);
        return true;
    }

    private static @NotNull List<Integer> getAvailableResinSides(World world, int x, int y, int z) {
        List<Integer> result = new ArrayList<>(4);

        for (int side : new int[] { 2, 3, 4, 5 }) {
            ForgeDirection dir = ForgeDirection.getOrientation(side);
            int nx = x + dir.offsetX;
            int ny = y + dir.offsetY;
            int nz = z + dir.offsetZ;

            Block neighbour = world.getBlock(nx, ny, nz);
            if (neighbour == null
                || neighbour.isAir(world, nx, ny, nz)
                || neighbour.isLeaves(world, nx, ny, nz)
                || neighbour.canBeReplacedByLeaves(world, nx, ny, nz)) {
                result.add(side);
            }
        }

        return result;
    }

    private static boolean treeHasFilledLog(World world, @NotNull List<int[]> treeLogs) {
        for (int[] pos : treeLogs) {
            TileEntityRubberLog te = getRubberLogTileEntity(world, pos[0], pos[1], pos[2]);
            if (te != null && te.hasResin()) {
                return true;
            }
        }
        return false;
    }

    private static long getScheduledRefillAt(World world, @NotNull List<int[]> treeLogs) {
        long earliest = 0L;

        for (int[] pos : treeLogs) {
            TileEntityRubberLog te = getRubberLogTileEntity(world, pos[0], pos[1], pos[2]);
            if (te == null) {
                continue;
            }

            long next = te.getNextRefillAt();
            if (next > 0L && (earliest == 0L || next < earliest)) {
                earliest = next;
            }
        }

        return earliest;
    }

    private static void scheduleTreeRefill(World world, @NotNull List<int[]> treeLogs, long when) {
        for (int[] pos : treeLogs) {
            TileEntityRubberLog te = getRubberLogTileEntity(world, pos[0], pos[1], pos[2]);
            if (te != null) {
                te.setNextRefillAt(when);
            }
        }
    }

    private static void clearTreeSchedule(World world, @NotNull List<int[]> treeLogs) {
        for (int[] pos : treeLogs) {
            TileEntityRubberLog te = getRubberLogTileEntity(world, pos[0], pos[1], pos[2]);
            if (te != null) {
                te.clearNextRefillAt();
            }
        }
    }

    private static @Nullable TileEntityRubberLog getRubberLogTileEntity(@NotNull World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return te instanceof TileEntityRubberLog ? (TileEntityRubberLog) te : null;
    }

    private static @NotNull List<int[]> collectConnectedNaturalLogs(World world, int startX, int startY, int startZ) {
        List<int[]> result = new ArrayList<>();
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();

        queue.add(new int[] { startX, startY, startZ });

        while (!queue.isEmpty() && result.size() < MAX_CONNECTED_LOGS) {
            int[] pos = queue.removeFirst();
            String key = pos[0] + ":" + pos[1] + ":" + pos[2];

            if (!seen.add(key)) {
                continue;
            }

            Block block = world.getBlock(pos[0], pos[1], pos[2]);
            if (!(block instanceof BlockRubberLog)) {
                continue;
            }

            int meta = world.getBlockMetadata(pos[0], pos[1], pos[2]);
            if (!BlockRubberLog.isNatural(meta)) {
                continue;
            }

            result.add(pos);

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                queue.add(new int[] {
                    pos[0] + dir.offsetX,
                    pos[1] + dir.offsetY,
                    pos[2] + dir.offsetZ
                });
            }
        }

        return result;
    }

    public static boolean tryHarvestResin(World world, int x, int y, int z, int side) {
        TileEntityRubberLog te = getRubberLogTileEntity(world, x, y, z);
        if (te == null || !te.hasResin() || te.getResinSide() != side) {
            return false;
        }

        te.clearResin();
        onResinCollected(world, x, y, z);
        return true;
    }

    public static boolean canHarvestResinFromSide(World world, int x, int y, int z, int side) {
        TileEntityRubberLog te = getRubberLogTileEntity(world, x, y, z);
        return te != null && te.hasResin() && te.getResinSide() == side;
    }
}
