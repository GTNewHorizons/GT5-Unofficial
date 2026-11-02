package gregtech.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;

import gregtech.api.enums.OrePrefixes;
import gregtech.common.GTProxy;
import gregtech.common.GTProxy.OreDropSystem;

public interface IStoneType {

    /** Checks if this stone type contains this specific block. */
    boolean contains(Block block, int meta);

    /** Gets the oredict prefix for this stone type (usually just {@code ore}) */
    OrePrefixes getPrefix();

    /** Gets the stone category for this stone. */
    IStoneCategory getCategory();

    /** Gets the dust version of this stone */
    ItemStack getDust(boolean pure, int amount);

    /** Gets the cobblestone block for this stone. */
    ImmutableBlockMeta getCobblestone();

    /** Gets the stone block for this stone. */
    ImmutableBlockMeta getStone();

    /** Gets the texture for this stone. */
    ITexture getTexture(int side);

    /** Gets the icon for this stone. */
    IIcon getIcon(int side);

    /** If this stone drops double the ore. */
    boolean isRich();

    /** If this stone should be kept when the {@link GTProxy#oreDropSystem} is {@link OreDropSystem#PerDimBlock}. */
    boolean isDimensionSpecific();

    /** If this stone should be hidden in NEI. */
    boolean isExtraneous();

    /** If this stone's parent mod is loaded. */
    boolean isEnabled();

    /** If this stone can generate in the given world. */
    boolean canGenerateInWorld(World world);
}
