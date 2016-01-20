package binnie.extrabees.core;

import binnie.Binnie;
import binnie.core.Mods;
import binnie.core.Mods.Mod;
import binnie.core.item.IItemMisc;
import binnie.core.liquid.ManagerLiquid;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.proxy.ExtraBeesProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.recipes.ICarpenterManager;
import forestry.api.recipes.RecipeManagers;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

public enum ExtraBeeItems
  implements IItemMisc
{
  ScentedGear("Scented Gear", "scentedGear"),  DiamondShard("Diamond Fragment", "diamondShard"),  EmeraldShard("Emerald Fragment", "emeraldShard"),  RubyShard("Ruby Fragment", "rubyShard"),  SapphireShard("Sapphire Fragment", "sapphireShard"),  LapisShard("Lapis Fragment", "lapisShard"),  IronDust("Iron Grains", "ironDust"),  GoldDust("Gold Grains", "goldDust"),  SilverDust("Silver Grains", "silverDust"),  PlatinumDust("Platinum Grains", "platinumDust"),  CopperDust("Copper Grains", "copperDust"),  TinDust("Tin Grains", "tinDust"),  NickelDust("Nickel Grains", "nickelDust"),  LeadDust("Lead Grains", "leadDust"),  ZincDust("Zinc Grains", "zincDust"),  TitaniumDust("Titanium Grains", "titaniumDust"),  TungstenDust("Tungsten Grains", "tungstenDust"),  UraniumDust("Radioactive Fragments", "radioactiveDust"),  CoalDust("Coal Grains", "coalDust"),  RedDye("Red Dye", "dyeRed"),  YellowDye("Yellow Dye", "dyeYellow"),  BlueDye("Blue Dye", "dyeBlue"),  GreenDye("Green Dye", "dyeGreen"),  WhiteDye("White Dye", "dyeWhite"),  BlackDye("Black Dye", "dyeBlack"),  BrownDye("Brown Dye", "dyeBrown"),  ClayDust("Clay Dust", "clayDust"),  YelloriumDust("Yellorium Grains", "yelloriumDust"),  BlutoniumDust("Blutonium Grains", "blutoniumDust"),  CyaniteDust("Cyanite Grains", "cyaniteDust");
  
  String name;
  String iconPath;
  IIcon icon;
  
  static
  {
    TinDust.setMetal("Tin");
    ZincDust.setMetal("Zinc");
    CopperDust.setMetal("Copper");
    IronDust.setMetal("Iron");
    NickelDust.setMetal("Nickel");
    LeadDust.setMetal("Lead");
    SilverDust.setMetal("Silver");
    GoldDust.setMetal("Gold");
    PlatinumDust.setMetal("Platinum");
    TungstenDust.setMetal("Tungsten");
    TitaniumDust.setMetal("Titanium");
    CoalDust.setMetal("Coal");
    
    YelloriumDust.setMetal("Yellorium");
    BlutoniumDust.setMetal("Blutonium");
    CyaniteDust.setMetal("Cyanite");
    
    DiamondShard.setGem("Diamond");
    EmeraldShard.setGem("Emerald");
    RubyShard.setGem("Ruby");
    SapphireShard.setGem("Sapphire");
  }
  
  public static void init()
  {
    OreDictionary.registerOre("dyeRed", RedDye.get(1));
    OreDictionary.registerOre("dyeYellow", YellowDye.get(1));
    OreDictionary.registerOre("dyeBlue", BlueDye.get(1));
    OreDictionary.registerOre("dyeGreen", GreenDye.get(1));
    OreDictionary.registerOre("dyeBlack", BlackDye.get(1));
    OreDictionary.registerOre("dyeWhite", WhiteDye.get(1));
    OreDictionary.registerOre("dyeBrown", BrownDye.get(1));
  }
  
  public static void postInit()
  {
    ItemStack lapisShard = LapisShard.get(1);
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 1, 4), new Object[] { lapisShard, lapisShard, lapisShard, lapisShard });
    for (ExtraBeeItems item : values()) {
      if (item.metalString != null)
      {
        ItemStack dust = null;
        ItemStack ingot = null;
        if (!OreDictionary.getOres("ingot" + item.metalString).isEmpty()) {
          ingot = ((ItemStack)OreDictionary.getOres("ingot" + item.metalString).get(0)).copy();
        }
        if (!OreDictionary.getOres("dust" + item.metalString).isEmpty()) {
          dust = ((ItemStack)OreDictionary.getOres("dust" + item.metalString).get(0)).copy();
        }
        ItemStack input = item.get(1);
        if (dust != null) {
          GameRegistry.addShapelessRecipe(dust, new Object[] { input, input, input, input });
        } else if (ingot != null) {
          GameRegistry.addShapelessRecipe(ingot, new Object[] { input, input, input, input, input, input, input, input, input });
        } else if (item == CoalDust) {
          GameRegistry.addShapelessRecipe(new ItemStack(Items.coal), new Object[] { input, input, input, input });
        }
      }
      else if (item.gemString != null)
      {
        ItemStack gem = null;
        if (!OreDictionary.getOres("gem" + item.gemString).isEmpty()) {
          gem = (ItemStack)OreDictionary.getOres("gem" + item.gemString).get(0);
        }
        ItemStack input = item.get(1);
        if (gem != null) {
          GameRegistry.addShapelessRecipe(gem.copy(), new Object[] { input, input, input, input, input, input, input, input, input });
        }
      }
    }
    Item woodGear = null;
    try
    {
      woodGear = (Item)Class.forName("buildcraft.BuildCraftCore").getField("woodenGearItem").get(null);
    }
    catch (Exception e) {}
    ItemStack gear = new ItemStack(Blocks.planks, 1);
    if (woodGear != null) {
      gear = new ItemStack(woodGear, 1);
    }
    RecipeManagers.carpenterManager.addRecipe(100, Binnie.Liquid.getLiquidStack("for.honey", 500), null, ScentedGear.get(1), new Object[] { " j ", "bgb", " p ", Character.valueOf('j'), Mods.Forestry.item("royalJelly"), Character.valueOf('b'), Mods.Forestry.item("beeswax"), Character.valueOf('p'), Mods.Forestry.item("pollen"), Character.valueOf('g'), gear });
  }
  
  String metalString = null;
  String gemString = null;
  
  private ExtraBeeItems(String name, String iconPath)
  {
    this.name = name;
    this.iconPath = iconPath;
  }
  
  private void setGem(String string)
  {
    this.gemString = string;
  }
  
  private void setMetal(String string)
  {
    this.metalString = string;
  }
  
  public boolean isActive()
  {
    if (this.metalString != null) {
      return (!OreDictionary.getOres("ingot" + this.metalString).isEmpty()) || (!OreDictionary.getOres("dust" + this.metalString).isEmpty()) || (this == CoalDust);
    }
    if (this.gemString != null) {
      return !OreDictionary.getOres("gem" + this.gemString).isEmpty();
    }
    return true;
  }
  
  public IIcon getIcon(ItemStack stack)
  {
    return this.icon;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    this.icon = ExtraBees.proxy.getIcon(register, "misc/" + this.iconPath);
  }
  
  public String getName(ItemStack stack)
  {
    return this.name;
  }
  
  public ItemStack get(int i)
  {
    return new ItemStack(ExtraBees.itemMisc, i, ordinal());
  }
  
  public void addInformation(List par3List) {}
}
