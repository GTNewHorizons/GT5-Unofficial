package gregtech.common.items.tools;

import static gregtech.api.enums.Mods.Railcraft;

import java.util.function.BooleanSupplier;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import gregtech.api.enums.SoundResource;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

/**
 * The "right click a rail to turn it" half of a crowbar.
 * <p/>
 * This used to live in {@code BehaviourCrowbar}. Note that it does nothing at all when Railcraft is installed:
 * Railcraft has its own, better crowbar handling for rails, and GregTech steps aside rather than fighting it.
 */
public final class CrowbarActions {

    private CrowbarActions() {}

    /**
     * Tries to turn the rail the player clicked.
     *
     * @param pay charges the tool for one use, and reports whether it could be paid for.
     * @return whether the click was consumed.
     */
    public static boolean use(World world, int x, int y, int z, float hitX, float hitY, float hitZ,
        BooleanSupplier pay) {
        // Every branch below edits the world, so the client has nothing useful to do here.
        if (world.isRemote) return false;
        // Railcraft rotates its own rails, and does it better. Leave them alone when it is present.
        if (GTModHandler.getModItem(Railcraft.ID, "fluid.creosote.bucket", 1L) != null) return false;
        final Block block = world.getBlock(x, y, z);
        if (block == null) return false;
        final int meta = world.getBlockMetadata(x, y, z);

        if (block == Blocks.rail) {
            if (pay.getAsBoolean()) {
                // A plain rail has ten shapes, the last four being the curves.
                setShapeWithoutDropping(world, x, y, z, block, (meta + 1) % 10);
                playSound(world, hitX, hitY, hitZ);
            }
            return true;
        }
        if (block == Blocks.detector_rail || block == Blocks.activator_rail || block == Blocks.golden_rail) {
            if (pay.getAsBoolean()) {
                // Powered rails have no curves, so only the low three bits cycle; bit 8 is the powered flag.
                setShapeWithoutDropping(world, x, y, z, block, meta / 8 * 8 + (meta % 8 + 1) % 6);
                playSound(world, hitX, hitY, hitZ);
            }
            return true;
        }
        return false;
    }

    /**
     * Rewrites a rail's shape. Pretending to be the client for the duration is what the old behaviour did, and it is
     * what stops the rail popping off as an item on the way through.
     */
    private static void setShapeWithoutDropping(World world, int x, int y, int z, Block block, int meta) {
        world.isRemote = true;
        world.setBlock(x, y, z, block, meta, 0);
        world.isRemote = false;
    }

    private static void playSound(World world, float hitX, float hitY, float hitZ) {
        GTUtility.sendSoundToPlayers(world, SoundResource.RANDOM_BREAK, 1.0F, -1.0F, hitX, hitY, hitZ);
    }
}
