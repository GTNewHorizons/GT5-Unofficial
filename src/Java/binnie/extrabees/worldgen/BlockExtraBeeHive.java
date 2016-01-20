package binnie.extrabees.worldgen;

import binnie.extrabees.ExtraBees;
import binnie.extrabees.proxy.ExtraBeesProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.IHiveDrop;
import forestry.api.core.Tabs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockExtraBeeHive
  extends Block
{
  IIcon[][] icons;
  
  public BlockExtraBeeHive()
  {
    super(ExtraBees.materialBeehive);
    setLightLevel(0.2F);
    setHardness(1.0F);
    setTickRandomly(true);
    setBlockName("hive");
    
    setCreativeTab(Tabs.tabApiculture);
  }
  
  public String getUnlocalizedName(ItemStack par1ItemStack)
  {
    return "extrabees.block.hive." + par1ItemStack.getItemDamage();
  }
  
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List itemList)
  {
    for (int i = 0; i < 4; i++) {
      itemList.add(new ItemStack(this, 1, i));
    }
  }
  
  public IIcon getIcon(int side, int metadata)
  {
    if (metadata >= EnumHiveType.values().length) {
      return null;
    }
    if (side < 2) {
      return this.icons[metadata][1];
    }
    return this.icons[metadata][0];
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister register)
  {
    this.icons = new IIcon[EnumHiveType.values().length][2];
    for (EnumHiveType hive : EnumHiveType.values())
    {
      this.icons[hive.ordinal()][0] = ExtraBees.proxy.getIcon(register, "hive/" + hive.toString().toLowerCase() + ".0");
      this.icons[hive.ordinal()][1] = ExtraBees.proxy.getIcon(register, "hive/" + hive.toString().toLowerCase() + ".1");
    }
  }
  
  public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
  {
    ArrayList<ItemStack> ret = new ArrayList();
    
    List<IHiveDrop> dropList = EnumHiveType.values()[metadata].drops;
    
    Collections.shuffle(dropList);
    
    int tries = 0;
    boolean hasPrincess = false;
    while ((tries <= 10) && (!hasPrincess))
    {
      tries++;
      for (IHiveDrop drop : dropList) {
        if (world.rand.nextInt(100) < drop.getChance(world, x, y, z))
        {
          ret.add(drop.getPrincess(world, x, y, z, fortune));
          hasPrincess = true;
          break;
        }
      }
    }
    for (IHiveDrop drop : dropList) {
      if (world.rand.nextInt(100) < drop.getChance(world, x, y, z))
      {
        ret.addAll(drop.getDrones(world, x, y, z, fortune));
        break;
      }
    }
    for (IHiveDrop drop : dropList) {
      if (world.rand.nextInt(100) < drop.getChance(world, x, y, z))
      {
        ret.addAll(drop.getAdditional(world, x, y, z, fortune));
        break;
      }
    }
    return ret;
  }
}
