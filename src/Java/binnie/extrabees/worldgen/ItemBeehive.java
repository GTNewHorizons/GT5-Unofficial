package binnie.extrabees.worldgen;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBeehive
  extends ItemBlock
{
  public ItemBeehive(Block block)
  {
    super(block);
    setMaxDamage(0);
    setHasSubtypes(true);
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public int getMetadata(int i)
  {
    return i;
  }
  
  public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List itemList)
  {
    for (int i = 0; i < 4; i++) {
      itemList.add(new ItemStack(this, 1, i));
    }
  }
  
  public String getItemStackDisplayName(ItemStack itemStack)
  {
    return EnumHiveType.values()[itemStack.getItemDamage()].toString() + " Hive";
  }
}
