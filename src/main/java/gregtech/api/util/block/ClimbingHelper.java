package gregtech.api.util.block;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import gregtech.GTMod;

/**
 * Lets players climb the sides of a block as a ladder.
 */
public final class ClimbingHelper {

    private static final double CLIMB_INSET = 1D / 16D;

    private ClimbingHelper() {}

    public static double getClimbableMin(int blockCoordinate) {
        return blockCoordinate + CLIMB_INSET;
    }

    public static double getClimbableMax(int blockCoordinate) {
        return blockCoordinate + 1.0D - CLIMB_INSET;
    }

    public static AxisAlignedBB getClimbableBox(int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(
            getClimbableMin(x),
            y,
            getClimbableMin(z),
            getClimbableMax(x),
            getClimbableMax(y),
            getClimbableMax(z));
    }

    /**
     * Applies climbing to an entity in contact with a block whose collision faces lie at
     * {@link #getClimbableMin} and {@link #getClimbableMax}.
     * Call it from {@code Block#onEntityCollidedWithBlock}.
     *
     * @param world    the world of the block
     * @param x        the x coordinate of the block
     * @param y        the y coordinate of the block
     * @param z        the z coordinate of the block
     * @param collider the entity in contact with the block
     */
    public static void onEntityCollided(World world, int x, int y, int z, Entity collider) {
        if (!(collider instanceof EntityPlayer player)) {
            return;
        }
        player.fallDistance = 0.0F;
        if (!world.isRemote) {
            return;
        }
        if (!isStandingOn(player, y)) {
            GTMod.clientProxy().frameBoxClimbHandler.onSideTouched(player);
        } else if (player.isSneaking()) {
            player.setPosition(player.posX, player.posY + (y + 1.0D - player.boundingBox.minY), player.posZ);
        } else {
            stepOffEdge(world, x, z, player);
        }
    }

    private static boolean isStandingOn(EntityPlayer player, int y) {
        return player.boundingBox.minY >= getClimbableMax(y);
    }

    // Only as deep as the inset top, so the block under the climbable block is not footing.
    private static List<AxisAlignedBB> getFooting(World world, EntityPlayer player) {
        return world
            .getCollidingBoundingBoxes(player, player.boundingBox.getOffsetBoundingBox(0.0D, -CLIMB_INSET, 0.0D));
    }

    private static void stepOffEdge(World world, int x, int z, EntityPlayer player) {
        final List<AxisAlignedBB> footing = getFooting(world, player);
        if (footing.isEmpty()) {
            return;
        }
        AxisAlignedBB bounds = footing.get(0);
        for (int index = 1; index < footing.size(); index++) {
            // func_111270_a is AxisAlignedBB.union(AxisAlignedBB)
            bounds = bounds.func_111270_a(footing.get(index));
        }
        final AxisAlignedBB playerBox = player.boundingBox;
        final double shiftX = getEdgeShift(bounds.minX, bounds.maxX, playerBox.minX, playerBox.maxX, x);
        final double shiftZ = getEdgeShift(bounds.minZ, bounds.maxZ, playerBox.minZ, playerBox.maxZ, z);
        if (shiftX != 0.0D && (shiftZ == 0.0D || Math.abs(shiftX) <= Math.abs(shiftZ))) {
            player.setPosition(player.posX + shiftX, player.posY, player.posZ);
        } else if (shiftZ != 0.0D) {
            player.setPosition(player.posX, player.posY, player.posZ + shiftZ);
        }
    }

    private static double getEdgeShift(double footingMin, double footingMax, double playerMin, double playerMax,
        int blockMin) {
        final double shift;
        if (footingMax < blockMin + 1.0D && playerMax > footingMax) {
            shift = footingMax - playerMin;
        } else if (footingMin > blockMin && playerMin < footingMin) {
            shift = footingMin - playerMax;
        } else {
            return 0.0D;
        }
        return Math.abs(shift) <= CLIMB_INSET ? shift : 0.0D;
    }
}
