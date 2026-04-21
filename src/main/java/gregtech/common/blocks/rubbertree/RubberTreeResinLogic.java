package gregtech.common.blocks.rubbertree;

import gregtech.api.GregTechAPI;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class RubberTreeResinLogic {

    public static final int MIN_REFILL_TICKS = 20 * 90;
    public static final int MAX_REFILL_TICKS = 20 * 300;
    private static final int MAX_CONNECTED_LOGS = 64;

    private RubberTreeResinLogic() {}

    public static int nextResinDelay(@NotNull Random random) {
        return MIN_REFILL_TICKS + random.nextInt(MAX_REFILL_TICKS - MIN_REFILL_TICKS + 1);
    }

    public static void scheduleTreeRefill(World world, int x, int y, int z, Random random) {
        List<int[]> treeLogs = collectConnectedNaturalLogs(world, x, y, z);
        if (treeLogs.isEmpty()) {
            return;
        }

        int[] anchor = treeLogs.get(random.nextInt(treeLogs.size()));
        world.scheduleBlockUpdate(
            anchor[0],
            anchor[1],
            anchor[2],
            GregTechAPI.sBlockRubberLogNatural,
            nextResinDelay(random)
        );
    }

    public static void tryRefillTree(@NotNull World world, int x, int y, int z, Random random) {
        if (!(world.getBlock(x, y, z) instanceof BlockRubberLogNatural)) {
            return;
        }

        List<int[]> treeLogs = collectConnectedNaturalLogs(world, x, y, z);
        if (treeLogs.isEmpty()) {
            return;
        }

        if (treeHasFilledLog(world, treeLogs)) {
            return;
        }

        if (!fillRandomLog(world, treeLogs, random)) {
            // retry in 10..15 seconds if fail
            world.scheduleBlockUpdate(x, y, z, GregTechAPI.sBlockRubberLogNatural, 20 * 10 + random.nextInt(20 * 5 + 1));
        }
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

        world.setBlockMetadataWithNotify(chosenLog[0], chosenLog[1], chosenLog[2], chosenSide, 3);
        RubberTreeEffects.spawnResinRefillParticles(world, chosenLog[0], chosenLog[1], chosenLog[2], chosenSide);

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
                || neighbour.canBeReplacedByLeaves(world, nx, ny, nz)
                || neighbour.getMaterial().isReplaceable()) {
                result.add(side);
            }
        }

        return result;
    }

    private static boolean treeHasFilledLog(World world, @NotNull List<int[]> treeLogs) {
        for (int[] pos : treeLogs) {
            int meta = world.getBlockMetadata(pos[0], pos[1], pos[2]);
            if (BlockRubberLogNatural.hasResin(meta)) {
                return true;
            }
        }
        return false;
    }

    private static @NotNull List<int[]> collectConnectedNaturalLogs(World world, int startX, int startY, int startZ) {
        List<int[]> result = new ArrayList<>();
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        Set<Long> seen = new HashSet<>();

        queue.add(new int[] { startX, startY, startZ });

        while (!queue.isEmpty() && result.size() < MAX_CONNECTED_LOGS) {
            int[] pos = queue.removeFirst();
            long key = packPos(pos[0], pos[1], pos[2]);

            if (!seen.add(key)) {
                continue;
            }

            Block block = world.getBlock(pos[0], pos[1], pos[2]);
            if (!(block instanceof BlockRubberLogNatural)) {
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

    private static long packPos(int x, int y, int z) {
        long a = (((long) x) & 0x3ffffffL) << 38;
        long b = (((long) z) & 0x3ffffffL) << 12;
        long c = ((long) y) & 0xfffL;
        return a ^ b ^ c;
    }
}
