package gregtech.common.items.tools;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.enums.SoundResource;
import gregtech.api.util.GTUtil;
import gregtech.api.util.GTUtility;

/**
 * The "right click a block to nudge it" half of a soft mallet: toggling redstone lamps and powered rails, and turning
 * the handful of vanilla blocks that respond to a tap rather than to a wrench.
 * <p/>
 * This used to live in {@code BehaviourSoftMallet}, hung off {@link gregtech.api.items.MetaBaseItem}'s behaviour
 * registry. The soft mallet is no longer a {@code MetaGeneratedTool} metadata, so the logic moved here, where a plain
 * item can call it directly. GregTech machines are not handled here at all -- they pick the mallet out of
 * {@link gregtech.api.GregTechAPI#sSoftMalletList} themselves, in {@code BaseMetaTileEntity}.
 */
public final class SoftMalletActions {

    private SoftMalletActions() {}

    /**
     * Tries to nudge the block the player clicked.
     *
     * @return whether the click was consumed.
     */
    public static boolean use(ToolSoftMalletItem item, ItemStack stack, EntityPlayer player, World world, int x, int y,
        int z, float hitX, float hitY, float hitZ) {
        // Every branch below edits the world, so the client has nothing useful to do here.
        if (world.isRemote) return false;
        final Block block = world.getBlock(x, y, z);
        if (block == null) return false;
        final int meta = world.getBlockMetadata(x, y, z);

        if (block == Blocks.lit_redstone_lamp) {
            if (pay(item, stack, player)) {
                setBlockWithoutRelight(world, x, y, z, Blocks.redstone_lamp);
                playSound(world, hitX, hitY, hitZ);
            }
            return true;
        }
        if (block == Blocks.redstone_lamp) {
            if (pay(item, stack, player)) {
                setBlockWithoutRelight(world, x, y, z, Blocks.lit_redstone_lamp);
                playSound(world, hitX, hitY, hitZ);
            }
            return true;
        }
        if (block == Blocks.golden_rail || block == Blocks.activator_rail) {
            if (pay(item, stack, player)) {
                // Bit 8 of a powered rail's metadata is its powered flag.
                world.isRemote = true;
                world.setBlock(x, y, z, block, (meta + 8) % 16, 0);
                world.isRemote = false;
                playSound(world, hitX, hitY, hitZ);
            }
            return true;
        }
        if (block == Blocks.log || block == Blocks.log2 || block == Blocks.hay_block) {
            if (pay(item, stack, player)) {
                world.setBlockMetadataWithNotify(x, y, z, (meta + 4) % 12, 3);
            }
            return true;
        }
        if (block == Blocks.piston || block == Blocks.sticky_piston
            || block == Blocks.dispenser
            || block == Blocks.dropper) {
            if (pay(item, stack, player)) {
                world.setBlockMetadataWithNotify(x, y, z, (meta + 1) % 6, 3);
                playSound(world, hitX, hitY, hitZ);
            }
            return true;
        }
        if (block == Blocks.pumpkin || block == Blocks.lit_pumpkin
            || block == Blocks.furnace
            || block == Blocks.lit_furnace) {
            if (pay(item, stack, player)) {
                world.setBlockMetadataWithNotify(x, y, z, rotateUpright(meta), 3);
                playSound(world, hitX, hitY, hitZ);
            }
            return true;
        }
        if (block == Blocks.chest || block == Blocks.trapped_chest) {
            // Step past any facing that would put this chest back-to-back with a neighbour.
            int newMeta = rotateUpright(meta);
            while (newMeta != meta && !GTUtil.setVanillaChestDirection(world, x, y, z, newMeta, block, true)) {
                newMeta = rotateUpright(newMeta);
            }
            if (pay(item, stack, player)) {
                GTUtil.setVanillaChestDirection(world, x, y, z, newMeta, block, false);
                playSound(world, hitX, hitY, hitZ);
            }
            return true;
        }
        if (block == Blocks.hopper) {
            if (pay(item, stack, player)) {
                // Metadata 1 is not a valid hopper facing, so skip over it.
                world.setBlockMetadataWithNotify(x, y, z, (meta + 1) % 6 != 1 ? (meta + 1) % 6 : 2, 3);
                playSound(world, hitX, hitY, hitZ);
            }
            return true;
        }
        return false;
    }

    private static boolean pay(ToolSoftMalletItem item, ItemStack stack, EntityPlayer player) {
        return player.capabilities.isCreativeMode || item.spendOneUse(stack);
    }

    /**
     * Swaps a block for its lit or unlit twin. Pretending to be the client for the duration is what the old behaviour
     * did, and it is what stops the lamp dropping itself and relighting the area on the way through.
     */
    private static void setBlockWithoutRelight(World world, int x, int y, int z, Block block) {
        world.isRemote = true;
        world.setBlock(x, y, z, block, 0, 0);
        world.isRemote = false;
    }

    private static void playSound(World world, float hitX, float hitY, float hitZ) {
        GTUtility.sendSoundToPlayers(world, SoundResource.GTCEU_OP_SOFT_HAMMER, 1.0F, 1.0F, hitX, hitY, hitZ);
    }

    /** Cycles a block through the four horizontal facings vanilla stores as metadata 2 through 5. */
    private static int rotateUpright(int meta) {
        return (meta - 1) % 4 + 2;
    }
}
