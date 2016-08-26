package miscutil.xmod.ic2.item;

import ic2.core.item.block.ItemBlockIC2;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemGenerators
  extends ItemBlockIC2
{
  public ItemGenerators(Block block)
  {
    super(block);
    
    setMaxDamage(0);
    setHasSubtypes(true);
  }
  
  @Override
public int getMetadata(int i)
  {
    return i;
  }
  
  @Override
public String getUnlocalizedName(ItemStack itemstack)
  {
    int meta = itemstack.getItemDamage();
    switch (meta)
    {
    case 0: 
      return "ic2.blockRTGenerator2";
    case 1: 
      return "ic2.blockKineticGenerator2";
    }
    return null;
  }
  
  @Override
public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
  {
    int meta = itemStack.getItemDamage();
    switch (meta)
    {    
    case 0: 
      info.add(StatCollector.translateToLocal("ic2.item.tooltip.PowerOutput") + " 1-32 EU/t " + StatCollector.translateToLocal("ic2.item.tooltip.max")); break;
    case 1: 
      info.add(StatCollector.translateToLocal("ic2.item.tooltip.PowerOutput") + " 1-512 EU/t " + StatCollector.translateToLocal("ic2.item.tooltip.max"));
    }
  }
}
