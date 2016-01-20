package binnie.extrabees.genetics;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.IInitializable;
import binnie.core.genetics.ManagerGenetics;
import binnie.core.liquid.ManagerLiquid;
import binnie.core.proxy.BinnieProxy;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.genetics.effect.BlockEctoplasm;
import binnie.extrabees.genetics.effect.ExtraBeesEffect;
import binnie.extrabees.genetics.items.ItemDictionary;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.IGenome;
import forestry.api.recipes.ICarpenterManager;
import forestry.api.recipes.RecipeManagers;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModuleGenetics
  implements IInitializable
{
  public void preInit()
  {
    for (ExtraBeesSpecies species : ) {
      AlleleManager.alleleRegistry.registerAllele(species);
    }
    ExtraBees.dictionary = new ItemDictionary();
    ExtraBees.ectoplasm = new BlockEctoplasm();
    
    GameRegistry.registerBlock(ExtraBees.ectoplasm, "ectoplasm");
  }
  
  public void init()
  {
    ExtraBeesEffect.doInit();
    ExtraBeesFlowers.doInit();
    ExtraBeesSpecies.doInit();
    ExtraBeeMutation.doInit();
    ExtraBeesBranch.doInit();
  }
  
  public void postInit()
  {
    int ebSpeciesCount = 0;
    int ebTotalSpeciesCount = 0;
    for (ExtraBeesSpecies species : ExtraBeesSpecies.values())
    {
      ebTotalSpeciesCount++;
      if (!AlleleManager.alleleRegistry.isBlacklisted(species.getUID())) {
        ebSpeciesCount++;
      }
    }
    RecipeManagers.carpenterManager.addRecipe(100, Binnie.Liquid.getLiquidStack("water", 2000), null, new ItemStack(ExtraBees.dictionary), new Object[] { "X#X", "YEY", "RDR", Character.valueOf('#'), Blocks.glass_pane, Character.valueOf('X'), Items.gold_ingot, Character.valueOf('Y'), "ingotTin", Character.valueOf('R'), Items.redstone, Character.valueOf('D'), Items.diamond, Character.valueOf('E'), Items.emerald });
  }
  
  public static IGenome getGenome(IAlleleBeeSpecies allele0)
  {
    return Binnie.Genetics.getBeeRoot().templateAsGenome(Binnie.Genetics.getBeeRoot().getTemplate(allele0.getUID()));
  }
  
  public static ItemStack getBeeIcon(IAlleleBeeSpecies species)
  {
    if (species == null) {
      return null;
    }
    IAllele[] template = Binnie.Genetics.getBeeRoot().getTemplate(species.getUID());
    if (template == null) {
      return null;
    }
    IBeeGenome genome = Binnie.Genetics.getBeeRoot().templateAsGenome(template);
    
    IBee bee = Binnie.Genetics.getBeeRoot().getBee(BinnieCore.proxy.getWorld(), genome);
    
    ItemStack item = Binnie.Genetics.getBeeRoot().getMemberStack(bee, EnumBeeType.PRINCESS.ordinal());
    
    return item;
  }
}
