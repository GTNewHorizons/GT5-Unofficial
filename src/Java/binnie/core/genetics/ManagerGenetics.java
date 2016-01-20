package binnie.core.genetics;

import binnie.Binnie;
import binnie.botany.api.IFlowerRoot;
import binnie.botany.genetics.AlleleColor;
import binnie.core.BinnieCore;
import binnie.core.ManagerBase;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.arboriculture.ITreeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.EnumTolerance;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleFloat;
import forestry.api.genetics.IAlleleInteger;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IChromosomeType;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpeciesRoot;
import forestry.api.lepidopterology.IButterflyRoot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent.Load;

public class ManagerGenetics
  extends ManagerBase
{
  public BreedingSystem beeBreedingSystem;
  public BreedingSystem treeBreedingSystem;
  public BreedingSystem mothBreedingSystem;
  public BreedingSystem flowerBreedingSystem;
  private final Map<ISpeciesRoot, BreedingSystem> BREEDING_SYSTEMS;
  private List<IChromosomeType> invalidChromosomeTypes;
  private Map<ISpeciesRoot, Map<IChromosomeType, List<IAllele>>> chromosomeArray;
  
  public void init()
  {
    if (BinnieCore.isApicultureActive()) {
      this.beeBreedingSystem = new BeeBreedingSystem();
    }
    if (BinnieCore.isArboricultureActive()) {
      this.treeBreedingSystem = new TreeBreedingSystem();
    }
    if (BinnieCore.isLepidopteryActive()) {
      this.mothBreedingSystem = new MothBreedingSystem();
    }
    if (BinnieCore.isBotanyActive()) {
      this.flowerBreedingSystem = new FlowerBreedingSystem();
    }
  }
  
  public void postInit()
  {
    refreshData();
  }
  
  public boolean isSpeciesDiscovered(IAlleleSpecies species, World world, boolean nei)
  {
    return true;
  }
  
  public ITreeRoot getTreeRoot()
  {
    return (ITreeRoot)AlleleManager.alleleRegistry.getSpeciesRoot("rootTrees");
  }
  
  public IBeeRoot getBeeRoot()
  {
    return (IBeeRoot)AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");
  }
  
  public IButterflyRoot getButterflyRoot()
  {
    return (IButterflyRoot)AlleleManager.alleleRegistry.getSpeciesRoot("rootButterflies");
  }
  
  public IFlowerRoot getFlowerRoot()
  {
    return (IFlowerRoot)AlleleManager.alleleRegistry.getSpeciesRoot("rootFlowers");
  }
  
  public BreedingSystem getSystem(String string)
  {
    for (BreedingSystem system : this.BREEDING_SYSTEMS.values()) {
      if (system.getIdent().equals(string)) {
        return system;
      }
    }
    return null;
  }
  
  public BreedingSystem getSystem(ISpeciesRoot root)
  {
    return getSystem(root.getUID());
  }
  
  public ISpeciesRoot getSpeciesRoot(IAlleleSpecies species)
  {
    for (ISpeciesRoot root : AlleleManager.alleleRegistry.getSpeciesRoot().values()) {
      if (root.getKaryotype()[0].getAlleleClass().isInstance(species)) {
        return root;
      }
    }
    return null;
  }
  
  public IAllele getToleranceAllele(EnumTolerance tol)
  {
    return AlleleManager.alleleRegistry.getAllele(Tolerance.values()[tol.ordinal()].getUID());
  }
  
  public int[] getTolerance(EnumTolerance tol)
  {
    return Tolerance.values()[tol.ordinal()].getBounds();
  }
  
  public Collection<BreedingSystem> getActiveSystems()
  {
    return this.BREEDING_SYSTEMS.values();
  }
  
  public void registerBreedingSystem(BreedingSystem system)
  {
    this.BREEDING_SYSTEMS.put(system.getSpeciesRoot(), system);
  }
  
  public BreedingSystem getConversionSystem(ItemStack stack)
  {
    for (BreedingSystem system : getActiveSystems()) {
      if (system.getConversion(stack) != null) {
        return system;
      }
    }
    return null;
  }
  
  public ItemStack getConversionStack(ItemStack stack)
  {
    BreedingSystem system = getConversionSystem(stack);
    return system == null ? null : system.getConversionStack(stack);
  }
  
  public IIndividual getConversion(ItemStack stack)
  {
    BreedingSystem system = getConversionSystem(stack);
    return system == null ? null : system.getConversion(stack);
  }
  
  @SubscribeEvent
  public void onWorldLoad(WorldEvent.Load event)
  {
    refreshData();
  }
  
  private void refreshData()
  {
    loadAlleles();
    for (BreedingSystem system : Binnie.Genetics.getActiveSystems()) {
      system.calculateArrays();
    }
  }
  
  public ManagerGenetics()
  {
    this.BREEDING_SYSTEMS = new LinkedHashMap();
    








































    this.invalidChromosomeTypes = new ArrayList();
    
    this.chromosomeArray = new LinkedHashMap();
  }
  
  private void loadAlleles()
  {
    this.invalidChromosomeTypes.clear();
    for (ISpeciesRoot root : AlleleManager.alleleRegistry.getSpeciesRoot().values())
    {
      BreedingSystem system = getSystem(root);
      Map<IChromosomeType, List<IAllele>> chromosomeMap = new LinkedHashMap();
      for (IChromosomeType chromosome : root.getKaryotype())
      {
        TreeSet<IAllele> alleles = new TreeSet(new ComparatorAllele());
        for (IIndividual individual : root.getIndividualTemplates())
        {
          IGenome genome = individual.getGenome();
          try
          {
            IAllele a1 = genome.getActiveAllele(chromosome);
            IAllele a2 = genome.getInactiveAllele(chromosome);
            if (chromosome.getAlleleClass().isInstance(a1)) {
              alleles.add(a1);
            }
            if (chromosome.getAlleleClass().isInstance(a2)) {
              alleles.add(a2);
            }
          }
          catch (Exception e) {}
        }
        system.addExtraAlleles(chromosome, alleles);
        if (alleles.size() == 0)
        {
          this.invalidChromosomeTypes.add(chromosome);
        }
        else
        {
          List<IAllele> alleleList = new ArrayList();
          alleleList.addAll(alleles);
          chromosomeMap.put(chromosome, alleleList);
        }
      }
      this.chromosomeArray.put(root, chromosomeMap);
    }
  }
  
  static class ComparatorAllele
    implements Comparator<IAllele>
  {
    public int compare(IAllele o1, IAllele o2)
    {
      if ((o1 == null) || (o2 == null)) {
        throw new NullPointerException("Allele is null!");
      }
      if (((o1 instanceof IAlleleFloat)) && ((o2 instanceof IAlleleFloat))) {
        return Float.valueOf(((IAlleleFloat)o1).getValue()).compareTo(Float.valueOf(((IAlleleFloat)o2).getValue()));
      }
      if (((o1 instanceof IAlleleInteger)) && ((o2 instanceof IAlleleInteger)) && (!(o1 instanceof AlleleColor))) {
        return Integer.valueOf(((IAlleleInteger)o1).getValue()).compareTo(Integer.valueOf(((IAlleleInteger)o2).getValue()));
      }
      if ((o1.getName() != null) && (o2.getName() != null)) {
        return o1.getName().compareTo(o2.getName());
      }
      return o1.getUID().compareTo(o2.getUID());
    }
  }
  
  public Map<IChromosomeType, List<IAllele>> getChromosomeMap(ISpeciesRoot root)
  {
    return (Map)this.chromosomeArray.get(root);
  }
  
  public Collection<IChromosomeType> getActiveChromosomes(ISpeciesRoot root)
  {
    return getChromosomeMap(root).keySet();
  }
  
  public boolean isInvalidChromosome(IChromosomeType type)
  {
    return this.invalidChromosomeTypes.contains(type);
  }
}
