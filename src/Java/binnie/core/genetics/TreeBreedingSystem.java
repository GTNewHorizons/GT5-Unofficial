package binnie.core.genetics;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.language.ManagerLanguage;
import binnie.core.resource.ManagerResource;
import binnie.core.util.UniqueItemStackSet;
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.machines.Lumbermill;
import com.mojang.authlib.GameProfile;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.EnumTreeChromosome;
import forestry.api.arboriculture.IAlleleFruit;
import forestry.api.arboriculture.IAlleleTreeSpecies;
import forestry.api.arboriculture.IArboristTracker;
import forestry.api.arboriculture.IFruitProvider;
import forestry.api.arboriculture.ITreeGenome;
import forestry.api.arboriculture.ITreeMutation;
import forestry.api.arboriculture.ITreeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleInteger;
import forestry.api.genetics.IAllelePlantType;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IChromosomeType;
import forestry.api.genetics.IFruitFamily;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.ISpeciesRoot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class TreeBreedingSystem
  extends BreedingSystem
{
  public UniqueItemStackSet allFruits = new UniqueItemStackSet();
  public UniqueItemStackSet allWoods = new UniqueItemStackSet();
  private UniqueItemStackSet discoveredFruits = new UniqueItemStackSet();
  private UniqueItemStackSet discoveredWoods = new UniqueItemStackSet();
  public UniqueItemStackSet discoveredPlanks = new UniqueItemStackSet();
  
  public TreeBreedingSystem()
  {
    this.iconUndiscovered = Binnie.Resource.getItemIcon(ExtraTrees.instance, "icon/undiscoveredTree");
    this.iconDiscovered = Binnie.Resource.getItemIcon(ExtraTrees.instance, "icon/discoveredTree");
  }
  
  public float getChance(IMutation mutation, EntityPlayer player, IAllele species1, IAllele species2)
  {
    IGenome genome0 = getSpeciesRoot().templateAsGenome(getSpeciesRoot().getTemplate(species1.getUID()));
    IGenome genome1 = getSpeciesRoot().templateAsGenome(getSpeciesRoot().getTemplate(species2.getUID()));
    return ((ITreeMutation)mutation).getChance(player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ, species1, species2, genome0, genome1);
  }
  
  public ISpeciesRoot getSpeciesRoot()
  {
    return Binnie.Genetics.getTreeRoot();
  }
  
  public int getColour()
  {
    return 53006;
  }
  
  public Class<? extends IBreedingTracker> getTrackerClass()
  {
    return IArboristTracker.class;
  }
  
  public String getAlleleName(IChromosomeType chromosome, IAllele allele)
  {
    if (chromosome == EnumTreeChromosome.GIRTH) {
      return ((IAlleleInteger)allele).getValue() + "x" + ((IAlleleInteger)allele).getValue();
    }
    if (chromosome == EnumTreeChromosome.PLANT)
    {
      EnumSet<EnumPlantType> types = ((IAllelePlantType)allele).getPlantTypes();
      return types.isEmpty() ? Binnie.Language.localise(BinnieCore.instance, "allele.none") : ((EnumPlantType)types.iterator().next()).toString();
    }
    if ((chromosome == EnumTreeChromosome.FRUITS) && (allele.getUID().contains(".")))
    {
      IFruitProvider provider = ((IAlleleFruit)allele).getProvider();
      return provider.getProducts().length == 0 ? Binnie.Language.localise(BinnieCore.instance, "allele.none") : provider.getProducts()[0].getDisplayName();
    }
    if (chromosome == EnumTreeChromosome.GROWTH)
    {
      if (allele.getUID().contains("Tropical")) {
        return Binnie.Language.localise(BinnieCore.instance, "allele.growth.tropical");
      }
      if (allele.getUID().contains("Lightlevel")) {
        return Binnie.Language.localise(BinnieCore.instance, "allele.growth.lightlevel");
      }
    }
    return super.getAlleleName(chromosome, allele);
  }
  
  public void onSyncBreedingTracker(IBreedingTracker tracker)
  {
    this.discoveredFruits.clear();
    this.discoveredWoods.clear();
    for (IAlleleSpecies species : getDiscoveredSpecies(tracker))
    {
      IAlleleTreeSpecies tSpecies = (IAlleleTreeSpecies)species;
      
      ITreeGenome genome = (ITreeGenome)getSpeciesRoot().templateAsGenome(getSpeciesRoot().getTemplate(tSpecies.getUID()));
      for (ItemStack wood : tSpecies.getLogStacks()) {
        this.discoveredWoods.add(wood);
      }
      for (ItemStack fruit : genome.getFruitProvider().getProducts()) {
        this.discoveredFruits.add(fruit);
      }
      for (Iterator i$ = this.discoveredWoods.iterator(); i$.hasNext(); wood = (ItemStack)i$.next()) {}
    }
    ItemStack wood;
  }
  
  public final void calculateArrays()
  {
    super.calculateArrays();
    for (IAlleleSpecies species : this.allActiveSpecies)
    {
      IAlleleTreeSpecies tSpecies = (IAlleleTreeSpecies)species;
      

      ITreeGenome genome = (ITreeGenome)getSpeciesRoot().templateAsGenome(getSpeciesRoot().getTemplate(tSpecies.getUID()));
      for (ItemStack wood : tSpecies.getLogStacks()) {
        this.allWoods.add(wood);
      }
      for (ItemStack fruit : genome.getFruitProvider().getProducts()) {
        this.allFruits.add(fruit);
      }
    }
  }
  
  public Collection<IAlleleSpecies> getTreesThatBearFruit(ItemStack fruit, boolean nei, World world, GameProfile player)
  {
    Collection<IAlleleSpecies> set = nei ? getAllSpecies() : getDiscoveredSpecies(world, player);
    List<IAlleleSpecies> found = new ArrayList();
    for (IAlleleSpecies species : set)
    {
      IAlleleTreeSpecies tSpecies = (IAlleleTreeSpecies)species;
      ITreeGenome genome = (ITreeGenome)getSpeciesRoot().templateAsGenome(getSpeciesRoot().getTemplate(tSpecies.getUID()));
      for (ItemStack fruit2 : genome.getFruitProvider().getProducts()) {
        if (fruit2.isItemEqual(fruit)) {
          found.add(species);
        }
      }
    }
    return found;
  }
  
  public Collection<IAlleleSpecies> getTreesThatCanBearFruit(ItemStack fruit, boolean nei, World world, GameProfile player)
  {
    Collection<IAlleleSpecies> set = nei ? getAllSpecies() : getDiscoveredSpecies(world, player);
    List<IAlleleSpecies> found = new ArrayList();
    
    Set<IFruitFamily> providers = new HashSet();
    for (IAlleleSpecies species : set)
    {
      IAlleleTreeSpecies tSpecies = (IAlleleTreeSpecies)species;
      ITreeGenome genome = (ITreeGenome)getSpeciesRoot().templateAsGenome(getSpeciesRoot().getTemplate(tSpecies.getUID()));
      for (ItemStack fruit2 : genome.getFruitProvider().getProducts()) {
        if (fruit2.isItemEqual(fruit)) {
          providers.add(genome.getFruitProvider().getFamily());
        }
      }
    }
    for (Iterator i$ = set.iterator(); i$.hasNext();)
    {
      species = (IAlleleSpecies)i$.next();
      tSpecies = (IAlleleTreeSpecies)species;
      for (IFruitFamily family : providers) {
        if (tSpecies.getSuitableFruit().contains(family))
        {
          found.add(species);
          break;
        }
      }
    }
    IAlleleSpecies species;
    IAlleleTreeSpecies tSpecies;
    return found;
  }
  
  public Collection<IAlleleSpecies> getTreesThatHaveWood(ItemStack fruit, boolean nei, World world, GameProfile player)
  {
    Collection<IAlleleSpecies> set = nei ? getAllSpecies() : getDiscoveredSpecies(world, player);
    List<IAlleleSpecies> found = new ArrayList();
    for (IAlleleSpecies species : set)
    {
      IAlleleTreeSpecies tSpecies = (IAlleleTreeSpecies)species;
      for (ItemStack fruit2 : tSpecies.getLogStacks()) {
        if (fruit2.isItemEqual(fruit)) {
          found.add(species);
        }
      }
    }
    return found;
  }
  
  public Collection<IAlleleSpecies> getTreesThatMakePlanks(ItemStack fruit, boolean nei, World world, GameProfile player)
  {
    if (fruit == null) {
      return new ArrayList();
    }
    Collection<IAlleleSpecies> set = nei ? getAllSpecies() : getDiscoveredSpecies(world, player);
    List<IAlleleSpecies> found = new ArrayList();
    for (IAlleleSpecies species : set)
    {
      IAlleleTreeSpecies tSpecies = (IAlleleTreeSpecies)species;
      for (ItemStack fruit2 : tSpecies.getLogStacks()) {
        if ((Lumbermill.getPlankProduct(fruit2) != null) && (fruit.isItemEqual(Lumbermill.getPlankProduct(fruit2)))) {
          found.add(species);
        }
      }
    }
    return found;
  }
  
  public boolean isDNAManipulable(ItemStack member)
  {
    return ((ITreeRoot)getSpeciesRoot()).getType(member) == EnumGermlingType.POLLEN;
  }
  
  public IIndividual getConversion(ItemStack stack)
  {
    if (stack == null) {
      return null;
    }
    for (Map.Entry<ItemStack, IIndividual> entry : AlleleManager.ersatzSaplings.entrySet()) {
      if (ItemStack.areItemStacksEqual(stack, (ItemStack)entry.getKey())) {
        return (IIndividual)entry.getValue();
      }
    }
    return null;
  }
  
  public int[] getActiveTypes()
  {
    return new int[] { EnumGermlingType.SAPLING.ordinal(), EnumGermlingType.POLLEN.ordinal() };
  }
  
  public void addExtraAlleles(IChromosomeType chromosome, TreeSet<IAllele> alleles)
  {
    switch (1.$SwitchMap$forestry$api$arboriculture$EnumTreeChromosome[((EnumTreeChromosome)chromosome).ordinal()])
    {
    case 1: 
      for (ForestryAllele.Saplings a : ForestryAllele.Saplings.values()) {
        alleles.add(a.getAllele());
      }
      break;
    case 2: 
      for (ForestryAllele.Int a : ForestryAllele.Int.values()) {
        alleles.add(a.getAllele());
      }
      break;
    case 3: 
      for (ForestryAllele.TreeHeight a : ForestryAllele.TreeHeight.values()) {
        alleles.add(a.getAllele());
      }
      break;
    case 4: 
      for (ForestryAllele.Maturation a : ForestryAllele.Maturation.values()) {
        alleles.add(a.getAllele());
      }
      break;
    case 5: 
      for (ForestryAllele.Sappiness a : ForestryAllele.Sappiness.values()) {
        alleles.add(a.getAllele());
      }
      break;
    case 6: 
      for (ForestryAllele.Territory a : ForestryAllele.Territory.values()) {
        alleles.add(a.getAllele());
      }
      break;
    case 7: 
      for (ForestryAllele.Yield a : ForestryAllele.Yield.values()) {
        alleles.add(a.getAllele());
      }
      break;
    case 8: 
      for (ForestryAllele.Bool a : ForestryAllele.Bool.values()) {
        alleles.add(a.getAllele());
      }
      break;
    }
  }
}
