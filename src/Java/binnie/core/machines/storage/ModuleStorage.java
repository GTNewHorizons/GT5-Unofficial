package binnie.core.machines.storage;

import binnie.core.BinnieCore;
import binnie.core.IInitializable;
import binnie.core.machines.MachineGroup;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.ArrayList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModuleStorage
  implements IInitializable
{
  public void preInit()
  {
    BinnieCore.packageCompartment = new MachineGroup(BinnieCore.instance, "storage", "storage", Compartment.values());
    BinnieCore.packageCompartment.setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public void init() {}
  
  public void postInit()
  {
    String ironGear = OreDictionary.getOres("gearIron").isEmpty() ? "ingotIron" : "gearIron";
    String goldGear = OreDictionary.getOres("gearGold").isEmpty() ? "ingotGold" : "gearGold";
    String diamondGear = "gemDiamond";
    
    GameRegistry.addRecipe(new ShapedOreRecipe(Compartment.Compartment.get(1), new Object[] { "pcp", "cbc", "pcp", Character.valueOf('b'), Items.book, Character.valueOf('c'), Blocks.chest, Character.valueOf('p'), Blocks.stone_button }));
    GameRegistry.addRecipe(new ShapedOreRecipe(Compartment.CompartmentCopper.get(1), new Object[] { "pcp", "cbc", "pcp", Character.valueOf('b'), Compartment.Compartment.get(1), Character.valueOf('c'), "gearCopper", Character.valueOf('p'), Blocks.stone_button }));
    GameRegistry.addRecipe(new ShapedOreRecipe(Compartment.CompartmentBronze.get(1), new Object[] { "pcp", "cbc", "pcp", Character.valueOf('b'), Compartment.CompartmentCopper.get(1), Character.valueOf('c'), "gearBronze", Character.valueOf('p'), Items.gold_nugget }));
    
    GameRegistry.addRecipe(new ShapedOreRecipe(Compartment.CompartmentIron.get(1), new Object[] { "pcp", "cbc", "pcp", Character.valueOf('b'), Compartment.CompartmentCopper.get(1), Character.valueOf('c'), ironGear, Character.valueOf('p'), Items.gold_nugget }));
    GameRegistry.addRecipe(new ShapedOreRecipe(Compartment.CompartmentGold.get(1), new Object[] { "pcp", "cbc", "pcp", Character.valueOf('b'), Compartment.CompartmentIron.get(1), Character.valueOf('c'), goldGear, Character.valueOf('p'), Items.emerald }));
    GameRegistry.addRecipe(new ShapedOreRecipe(Compartment.CompartmentDiamond.get(1), new Object[] { "pcp", "cbc", "pcp", Character.valueOf('b'), Compartment.CompartmentGold.get(1), Character.valueOf('c'), diamondGear, Character.valueOf('p'), Items.emerald }));
  }
}
