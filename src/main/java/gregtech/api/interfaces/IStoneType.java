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
    public boolean contains(Block block, int meta);

    /** Gets the oredict prefix for this stone type (usually just {@code ore}) */
    public OrePrefixes getPrefix();

    /** Gets the stone category for this stone. */
    public IStoneCategory getCategory();

    /** Gets the dust version of this stone */
    public ItemStack getDust(boolean pure, int amount);

    /** Gets the cobblestone block for this stone. */
    public ImmutableBlockMeta getCobblestone();

    /** Gets the stone block for this stone. */
    public ImmutableBlockMeta getStone();

    /** Gets the texture for this stone. */
    public ITexture getTexture(int side);

    /** Gets the icon for this stone. */
    public IIcon getIcon(int side);

    /** If this stone drops double the ore. */
    public boolean isRich();

    /** If this stone should be kept when the {@link GTProxy#oreDropSystem} is {@link OreDropSystem#PerDimBlock}. */
    public boolean isDimensionSpecific();

    /** If this stone should be hidden in NEI. */
    public boolean isExtraneous();

    /** If this stone's parent mod is loaded. */
    public boolean isEnabled();

    /** If this stone can generate in the given world. */
    public boolean canGenerateInWorld(World world);
}
