package gregtech.common.ores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;

import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IOreMaterial;

public interface IOreAdapter<TMat extends IOreMaterial> {

    /**
     * A fast check to see if this adapter can support this block/meta combination.
     * This function can return false positives, but never false negatives.
     */
    public boolean supports(Block block, int meta);

    /**
     * Checks if this adapter can support the requested ore combination.
     * If this returns true, methods that take an OreInfo in this adapter must return a valid value.
     * If this returns false, methods that take an OreInfo must return an 'invalid' value (see method comments for what
     * this entails).
     */
    public boolean supports(OreInfo<?> info);

    /**
     * Analyzes the given block + meta and returns an OreInfo with its information, or null if the combination is
     * invalid.
     * Calling {@link #getBlock(OreInfo)} with the returned info must give back the same block/meta combination.
     */
    public OreInfo<TMat> getOreInfo(Block block, int meta);

    /**
     * Analyzes the block at the given coordinates and returns an OreInfo with its information, or null if the block is
     * invalid.
     * Calling {@link #getBlock(OreInfo)} with the returned info must give back the same block/meta combination.
     */
    public default OreInfo<TMat> getOreInfo(World world, int x, int y, int z) {
        return getOreInfo(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
    }

    /**
     * Analyzes the block stored in the stack and returns an OreInfo with its information, or null if the stack is
     * invalid.
     * Calling {@link #getBlock(OreInfo)} with the returned info must give back the same block/meta combination.
     */
    public default OreInfo<TMat> getOreInfo(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemBlock itemBlock)) return null;

        return getOreInfo(itemBlock.field_150939_a, Items.feather.getDamage(stack));
    }

    /**
     * Gets a block that represents the given OreInfo, or null if the OreInfo is invalid.
     */
    public ImmutableBlockMeta getBlock(OreInfo<?> info);

    /**
     * Gets a stack of the block for the given OreInfo, or null if the OreInfo is invalid.
     * If the first operation fails, this tries again with the stoneType set to {@link StoneType#Stone}.
     */
    public default ItemStack getStack(OreInfo<?> info, int amount) {
        ImmutableBlockMeta bm = getBlock(info);

        if (bm != null) {
            return new ItemStack(bm.getBlock(), amount, bm.getBlockMeta());
        }

        if (info.stoneType != StoneType.Stone) {
            try (OreInfo<?> info2 = info.clone()) {
                info2.stoneType = StoneType.Stone;

                bm = getBlock(info2);

                if (bm != null) {
                    return new ItemStack(bm.getBlock(), amount, bm.getBlockMeta());
                }
            }
        }

        return null;
    }

    /**
     * Gets the drops for a block. This returns an ArrayList due to the return type of
     * {@link Block#getDrops(World, int, int, int, int, int)}.
     */
    @NotNull
    public ArrayList<ItemStack> getOreDrops(Random rng, OreInfo<?> info, boolean silktouch, int fortune);

    /**
     * Gets the potential drops for a block. Used for the NEI addon.
     */
    public List<ItemStack> getPotentialDrops(OreInfo<?> info);
}
