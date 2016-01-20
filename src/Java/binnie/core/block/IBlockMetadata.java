package binnie.core.block;

import java.util.List;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract interface IBlockMetadata
  extends ITileEntityProvider
{
  public abstract int getPlacedMeta(ItemStack paramItemStack, World paramWorld, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection);
  
  public abstract int getDroppedMeta(int paramInt1, int paramInt2);
  
  public abstract String getBlockName(ItemStack paramItemStack);
  
  public abstract void getBlockTooltip(ItemStack paramItemStack, List paramList);
  
  public abstract void dropAsStack(World paramWorld, int paramInt1, int paramInt2, int paramInt3, ItemStack paramItemStack);
}
