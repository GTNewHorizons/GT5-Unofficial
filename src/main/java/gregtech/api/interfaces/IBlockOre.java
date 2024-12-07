package gregtech.api.interfaces;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBlockOre {
    
    /** Works like the vanilla getDrops, except it doesn't check to make sure a player is breaking the block. */
    public List<ItemStack> getDropsForMachine(World world, int x, int y, int z, boolean silktouch, int fortune);

    public boolean isSmall(World world, int x, int y, int z);
    public boolean isNatural(World world, int x, int y, int z);
    public String getOreUnlocalizedName(World world, int x, int y, int z);
}
