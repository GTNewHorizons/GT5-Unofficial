package gregtech.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.OrePrefixes;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;

public interface IStoneType {

    /** Checks if this stone type contains this specific block. */
    public boolean contains(Block block, int meta);

    public OrePrefixes getPrefix();

    public IStoneCategory getCategory();

    public ItemStack getDust(boolean pure, int amount);

    public ObjectIntPair<Block> getCobblestone();

    public ObjectIntPair<Block> getStone();

    public ITexture getTexture(int side);

    public IIcon getIcon(int side);

    public boolean isRich();

    public boolean isDimensionSpecific();

    public boolean isExtraneous();

    public boolean isEnabled();
}
