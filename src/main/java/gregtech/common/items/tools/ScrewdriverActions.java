package gregtech.common.items.tools;

import java.util.function.BooleanSupplier;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import gregtech.api.enums.SoundResource;
import gregtech.api.util.GTUtility;

/**
 * The "right click a block to adjust it" half of a screwdriver: stepping a redstone repeater's delay or a
 * comparator's mode.
 * <p/>
 * This used to live in {@code BehaviourScrewdriver}. The screwdriver is no longer a {@code MetaGeneratedTool}
 * metadata, so the logic moved here -- but the soldering iron still is one and still registers that behaviour, so
 * unlike the wrench and soft mallet helpers this one is called from both sides. That is why it takes the payment as a
 * {@link BooleanSupplier} rather than an item: the standalone tool spends its own durability or energy, while the
 * behaviour goes through {@code GTModHandler.damageOrDechargeItem} as it always did.
 * <p/>
 * GregTech machines and covers are not handled here -- they pick the screwdriver out of
 * {@link gregtech.api.GregTechAPI#sScrewdriverList} themselves, in {@code BaseMetaTileEntity}.
 */
public final class ScrewdriverActions {

    private ScrewdriverActions() {}

    /**
     * Tries to adjust the block the player clicked.
     *
     * @param pay charges the tool for one use, and reports whether it could be paid for.
     * @return whether the click was consumed.
     */
    public static boolean use(World world, int x, int y, int z, float hitX, float hitY, float hitZ,
        BooleanSupplier pay) {
        // Every branch below edits the world, so the client has nothing useful to do here.
        if (world.isRemote) return false;
        final Block block = world.getBlock(x, y, z);
        if (block == null) return false;

        if (block == Blocks.unpowered_repeater || block == Blocks.powered_repeater
            || block == Blocks.unpowered_comparator
            || block == Blocks.powered_comparator) {
            final int meta = world.getBlockMetadata(x, y, z);
            // The low two bits are the setting (repeater delay, comparator mode); the high bits are the facing.
            final int adjusted = meta / 4 * 4 + (meta % 4 + 1) % 4;
            if (pay.getAsBoolean()) {
                world.setBlockMetadataWithNotify(x, y, z, adjusted, 3);
                GTUtility.sendSoundToPlayers(world, SoundResource.GTCEU_OP_SCREWDRIVER, 1.0F, 1.0F, hitX, hitY, hitZ);
            }
            return true;
        }
        return false;
    }
}
