package binnie.core.genetics;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.language.ManagerLanguage;
import binnie.core.proxy.BinnieProxy;
import binnie.core.resource.BinnieIcon;
import binnie.extrabees.genetics.ExtraBeeMutation;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import forestry.api.core.ForestryEvent.SyncedBreedingTracker;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleBoolean;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IChromosomeType;
import forestry.api.genetics.IClassification;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.ISpeciesRoot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public abstract class BreedingSystem
  implements IItemStackRepresentitive
{
  protected BinnieIcon iconUndiscovered;
  protected BinnieIcon iconDiscovered;
  
  public BreedingSystem()
  {
    Binnie.Genetics.registerBreedingSystem(this);
    MinecraftForge.EVENT_BUS.register(this);
  }
  
  public String getChromosomeName(IChromosomeType chromo)
  {
    return BinnieCore.proxy.localise(getSpeciesRoot().getUID() + ".chromosome." + chromo.getName());
  }
  
  public String getChromosomeShortName(IChromosomeType chromo)
  {
    return BinnieCore.proxy.localise(getSpeciesRoot().getUID() + ".chromosome." + chromo.getName() + ".short");
  }
  
  public final String getEpitome(float discoveredPercentage)
  {
    int i = 0;
    if (discoveredPercentage == 1.0F) {
      i = 6;
    } else if (discoveredPercentage < 0.1F) {
      i = 0;
    } else if (discoveredPercentage < 0.3F) {
      i = 1;
    } else if (discoveredPercentage < 0.5F) {
      i = 2;
    } else if (discoveredPercentage < 0.7F) {
      i = 3;
    } else if (discoveredPercentage < 0.9F) {
      i = 4;
    } else if (discoveredPercentage < 1.0F) {
      i = 5;
    }
    return BinnieCore.proxy.localise(getSpeciesRoot().getUID() + ".epitome." + i);
  }
  
  private List<IClassification> allBranches = new ArrayList();
  List<IAlleleSpecies> allActiveSpecies = new ArrayList();
  private List<IAlleleSpecies> allSpecies = new ArrayList();
  private List<IMutation> allMutations = new ArrayList();
  private Map<IAlleleSpecies, List<IMutation>> resultantMutations = new HashMap();
  private Map<IAlleleSpecies, List<IMutation>> furtherMutations = new HashMap();
  private Map<IAlleleSpecies, List<IMutation>> allResultantMutations = new HashMap();
  private Map<IAlleleSpecies, List<IMutation>> allFurtherMutations = new HashMap();
  public float discoveredSpeciesPercentage;
  public int totalSpeciesCount;
  public int discoveredSpeciesCount;
  public int totalSecretCount;
  public int discoveredSecretCount;
  public float discoveredBranchPercentage;
  public int totalBranchCount;
  public int discoveredBranchCount;
  private int totalSecretBranchCount;
  private int discoveredSecretBranchCount;
  String currentEpithet;
  
  public abstract ISpeciesRoot getSpeciesRoot();
  
  public final List<IClassification> getAllBranches()
  {
    return this.allBranches;
  }
  
  public final Collection<IAlleleSpecies> getAllSpecies()
  {
    return this.allActiveSpecies;
  }
  
  public final Collection<IMutation> getAllMutations()
  {
    return this.allMutations;
  }
  
  public void calculateArrays()
  {
    Collection<IAllele> allAlleles = AlleleManager.alleleRegistry.getRegisteredAlleles().values();
    

    this.resultantMutations = new HashMap();
    this.furtherMutations = new HashMap();
    this.allResultantMutations = new HashMap();
    this.allFurtherMutations = new HashMap();
    
    this.allActiveSpecies = new ArrayList();
    this.allSpecies = new ArrayList();
    for (IAllele species : allAlleles) {
      if (getSpeciesRoot().getTemplate(species.getUID()) != null)
      {
        this.resultantMutations.put((IAlleleSpecies)species, new ArrayList());
        
        this.furtherMutations.put((IAlleleSpecies)species, new ArrayList());
        
        this.allResultantMutations.put((IAlleleSpecies)species, new ArrayList());
        
        this.allFurtherMutations.put((IAlleleSpecies)species, new ArrayList());
        
        this.allSpecies.add((IAlleleSpecies)species);
        if ((!isBlacklisted(species)) && (!species.getUID().contains("speciesBotAlfheim"))) {
          this.allActiveSpecies.add((IAlleleSpecies)species);
        }
      }
    }
    this.allMutations = new ArrayList();
    
    Collection<IClassification> allRegBranches = AlleleManager.alleleRegistry.getRegisteredClassifications().values();
    

    this.allBranches = new ArrayList();
    for (IClassification branch : allRegBranches) {
      if ((branch.getMemberSpecies().length > 0) && 
        (getSpeciesRoot().getTemplate(branch.getMemberSpecies()[0].getUID()) != null))
      {
        boolean possible = false;
        for (IAlleleSpecies species : branch.getMemberSpecies()) {
          if (this.allActiveSpecies.contains(species)) {
            possible = true;
          }
        }
        if (possible) {
          this.allBranches.add(branch);
        }
      }
    }
    if (getSpeciesRoot().getMutations(false) != null)
    {
      Set<IMutation> mutations = new LinkedHashSet();
      mutations.addAll(getSpeciesRoot().getMutations(false));
      if (this == Binnie.Genetics.beeBreedingSystem) {
        mutations.addAll(ExtraBeeMutation.mutations);
      }
      for (IMutation mutation : mutations)
      {
        this.allMutations.add(mutation);
        
        Set<IAlleleSpecies> participatingSpecies = new LinkedHashSet();
        if ((mutation.getAllele0() instanceof IAlleleSpecies)) {
          participatingSpecies.add((IAlleleSpecies)mutation.getAllele0());
        }
        if ((mutation.getAllele1() instanceof IAlleleSpecies)) {
          participatingSpecies.add((IAlleleSpecies)mutation.getAllele1());
        }
        for (IAlleleSpecies species : participatingSpecies)
        {
          ((List)this.allFurtherMutations.get(species)).add(mutation);
          if (this.allActiveSpecies.contains(species)) {
            ((List)this.furtherMutations.get(species)).add(mutation);
          }
        }
        if (this.resultantMutations.containsKey(mutation.getTemplate()[0]))
        {
          ((List)this.allResultantMutations.get(mutation.getTemplate()[0])).add(mutation);
          ((List)this.resultantMutations.get(mutation.getTemplate()[0])).add(mutation);
        }
      }
    }
  }
  
  public final boolean isBlacklisted(IAllele allele)
  {
    return AlleleManager.alleleRegistry.isBlacklisted(allele.getUID());
  }
  
  public final List<IMutation> getResultantMutations(IAlleleSpecies species, boolean includeInactive)
  {
    if (this.resultantMutations.isEmpty()) {
      calculateArrays();
    }
    return includeInactive ? (List)this.allResultantMutations.get(species) : (List)this.resultantMutations.get(species);
  }
  
  public final List<IMutation> getResultantMutations(IAlleleSpecies species)
  {
    if (this.resultantMutations.isEmpty()) {
      calculateArrays();
    }
    return (List)this.resultantMutations.get(species);
  }
  
  public final List<IMutation> getFurtherMutations(IAlleleSpecies species, boolean includeInactive)
  {
    if (this.furtherMutations.isEmpty()) {
      calculateArrays();
    }
    return includeInactive ? (List)this.allFurtherMutations.get(species) : (List)this.furtherMutations.get(species);
  }
  
  public final List<IMutation> getFurtherMutations(IAlleleSpecies species)
  {
    if (this.furtherMutations.isEmpty()) {
      calculateArrays();
    }
    return (List)this.furtherMutations.get(species);
  }
  
  public final boolean isMutationDiscovered(IMutation mutation, World world, GameProfile name)
  {
    return isMutationDiscovered(mutation, getSpeciesRoot().getBreedingTracker(world, name));
  }
  
  public final boolean isMutationDiscovered(IMutation mutation, IBreedingTracker tracker)
  {
    if (tracker == null) {
      return true;
    }
    return tracker.isDiscovered(mutation);
  }
  
  public final boolean isSpeciesDiscovered(IAlleleSpecies species, World world, GameProfile name)
  {
    return isSpeciesDiscovered(species, getSpeciesRoot().getBreedingTracker(world, name));
  }
  
  public final boolean isSpeciesDiscovered(IAlleleSpecies species, IBreedingTracker tracker)
  {
    if (tracker == null) {
      return true;
    }
    return tracker.isDiscovered(species);
  }
  
  public final boolean isSecret(IAlleleSpecies species)
  {
    return !species.isCounted();
  }
  
  public final boolean isSecret(IClassification branch)
  {
    for (IAlleleSpecies species : branch.getMemberSpecies()) {
      if (!isSecret(species)) {
        return false;
      }
    }
    return true;
  }
  
  public final Collection<IClassification> getDiscoveredBranches(World world, GameProfile player)
  {
    List<IClassification> branches = new ArrayList();
    for (IClassification branch : getAllBranches())
    {
      boolean discovered = false;
      for (IAlleleSpecies species : branch.getMemberSpecies()) {
        if (isSpeciesDiscovered(species, world, player)) {
          discovered = true;
        }
      }
      if (discovered) {
        branches.add(branch);
      }
    }
    return branches;
  }
  
  public final Collection<IClassification> getDiscoveredBranches(IBreedingTracker tracker)
  {
    List<IClassification> branches = new ArrayList();
    for (IClassification branch : getAllBranches())
    {
      boolean discovered = false;
      for (IAlleleSpecies species : branch.getMemberSpecies()) {
        if (isSpeciesDiscovered(species, tracker)) {
          discovered = true;
        }
      }
      if (discovered) {
        branches.add(branch);
      }
    }
    return branches;
  }
  
  public final Collection<IAlleleSpecies> getDiscoveredSpecies(World world, GameProfile player)
  {
    List<IAlleleSpecies> speciesList = new ArrayList();
    for (IAlleleSpecies species : getAllSpecies()) {
      if (isSpeciesDiscovered(species, world, player)) {
        speciesList.add(species);
      }
    }
    return speciesList;
  }
  
  public final Collection<IAlleleSpecies> getDiscoveredSpecies(IBreedingTracker tracker)
  {
    List<IAlleleSpecies> speciesList = new ArrayList();
    for (IAlleleSpecies species : getAllSpecies()) {
      if (isSpeciesDiscovered(species, tracker)) {
        speciesList.add(species);
      }
    }
    return speciesList;
  }
  
  public final List<IMutation> getDiscoveredMutations(World world, GameProfile player)
  {
    List<IMutation> speciesList = new ArrayList();
    for (IMutation species : getAllMutations()) {
      if (isMutationDiscovered(species, world, player)) {
        speciesList.add(species);
      }
    }
    return speciesList;
  }
  
  public final List<IMutation> getDiscoveredMutations(IBreedingTracker tracker)
  {
    List<IMutation> speciesList = new ArrayList();
    for (IMutation species : getAllMutations()) {
      if (isMutationDiscovered(species, tracker)) {
        speciesList.add(species);
      }
    }
    return speciesList;
  }
  
  public final int getDiscoveredBranchMembers(IClassification branch, IBreedingTracker tracker)
  {
    int discoveredSpecies = 0;
    for (IAlleleSpecies species : branch.getMemberSpecies()) {
      if (isSpeciesDiscovered(species, tracker)) {
        discoveredSpecies++;
      }
    }
    return discoveredSpecies;
  }
  
  public IIcon getUndiscoveredIcon()
  {
    return this.iconUndiscovered.getIcon();
  }
  
  public IIcon getDiscoveredIcon()
  {
    return this.iconDiscovered.getIcon();
  }
  
  public abstract float getChance(IMutation paramIMutation, EntityPlayer paramEntityPlayer, IAllele paramIAllele1, IAllele paramIAllele2);
  
  public abstract Class<? extends IBreedingTracker> getTrackerClass();
  
  @SubscribeEvent
  public final void onSyncBreedingTracker(ForestryEvent.SyncedBreedingTracker event)
  {
    IBreedingTracker tracker = event.tracker;
    if (!getTrackerClass().isInstance(tracker)) {
      return;
    }
    syncTracker(tracker);
  }
  
  public final void syncTracker(IBreedingTracker tracker)
  {
    this.discoveredSpeciesPercentage = 0.0F;
    
    this.totalSpeciesCount = 0;
    this.discoveredSpeciesCount = 0;
    
    this.totalSecretCount = 0;
    this.discoveredSecretCount = 0;
    
    Collection<IAlleleSpecies> discoveredSpecies = getDiscoveredSpecies(tracker);
    Collection<IAlleleSpecies> allSpecies = getAllSpecies();
    for (IAlleleSpecies species : allSpecies) {
      if (!isSecret(species))
      {
        this.totalSpeciesCount += 1;
        if (isSpeciesDiscovered(species, tracker)) {
          this.discoveredSpeciesCount += 1;
        }
      }
      else
      {
        this.totalSecretCount += 1;
        if (isSpeciesDiscovered(species, tracker)) {
          this.discoveredSecretCount += 1;
        }
      }
    }
    this.discoveredBranchPercentage = 0.0F;
    
    this.totalBranchCount = 0;
    this.discoveredBranchCount = 0;
    
    Collection<IClassification> discoveredBranches = getDiscoveredBranches(tracker);
    Collection<IClassification> allBranches = getAllBranches();
    for (IClassification branch : allBranches) {
      if (!isSecret(branch))
      {
        this.totalBranchCount += 1;
        if (discoveredBranches.contains(branch)) {
          this.discoveredBranchCount += 1;
        }
      }
      else
      {
        this.totalSecretBranchCount += 1;
        if (discoveredBranches.contains(branch)) {
          this.discoveredSecretBranchCount += 1;
        }
      }
    }
    this.discoveredSpeciesPercentage = (this.discoveredSpeciesCount / this.totalSpeciesCount);
    
    this.discoveredBranchPercentage = (this.discoveredBranchCount / this.totalBranchCount);
    

    String epithet = getEpitome();
    
    onSyncBreedingTracker(tracker);
  }
  
  public void onSyncBreedingTracker(IBreedingTracker tracker) {}
  
  public String getEpitome()
  {
    return getEpitome(this.discoveredSpeciesPercentage);
  }
  
  public final String getDescriptor()
  {
    return BinnieCore.proxy.localise(getSpeciesRoot().getUID() + ".descriptor");
  }
  
  public final String getIdent()
  {
    return getSpeciesRoot().getUID();
  }
  
  public final IChromosomeType getChromosome(int i)
  {
    for (IChromosomeType chromosome : getSpeciesRoot().getKaryotype()) {
      if (i == chromosome.ordinal()) {
        return chromosome;
      }
    }
    return null;
  }
  
  public abstract int getColour();
  
  public String getAlleleName(IChromosomeType chromosome, IAllele allele)
  {
    if ((allele instanceof IAlleleBoolean)) {
      return ((IAlleleBoolean)allele).getValue() ? Binnie.Language.localise(BinnieCore.instance, "allele.true") : Binnie.Language.localise(BinnieCore.instance, "allele.false");
    }
    if (allele.getName() == "for.gui.maximum") {
      return Binnie.Language.localise(BinnieCore.instance, "allele.fertility.maximum");
    }
    return allele.getName();
  }
  
  public String getName()
  {
    return BinnieCore.proxy.localise(getSpeciesRoot().getUID() + ".shortName");
  }
  
  public ItemStack getItemStackRepresentitive()
  {
    IIndividual first = (IIndividual)getSpeciesRoot().getIndividualTemplates().get(0);
    return getSpeciesRoot().getMemberStack(first, getDefaultType());
  }
  
  public String toString()
  {
    return getName();
  }
  
  public abstract boolean isDNAManipulable(ItemStack paramItemStack);
  
  public IIndividual getConversion(ItemStack stack)
  {
    return null;
  }
  
  public final IIndividual getDefaultIndividual()
  {
    return getSpeciesRoot().templateAsIndividual(getSpeciesRoot().getDefaultTemplate());
  }
  
  public final int getDefaultType()
  {
    return getActiveTypes()[0];
  }
  
  public abstract int[] getActiveTypes();
  
  public abstract void addExtraAlleles(IChromosomeType paramIChromosomeType, TreeSet<IAllele> paramTreeSet);
  
  public ItemStack getConversionStack(ItemStack stack)
  {
    return getSpeciesRoot().getMemberStack(getConversion(stack), getDefaultType());
  }
  
  public final Collection<IChromosomeType> getActiveKaryotype()
  {
    return Binnie.Genetics.getActiveChromosomes(getSpeciesRoot());
  }
  
  public ItemStack getDefaultMember(String uid)
  {
    return getSpeciesRoot().getMemberStack(getIndividual(uid), getDefaultType());
  }
  
  public IIndividual getIndividual(String uid)
  {
    return getSpeciesRoot().templateAsIndividual(getSpeciesRoot().getTemplate(uid));
  }
  
  public IGenome getGenome(String uid)
  {
    return getSpeciesRoot().templateAsGenome(getSpeciesRoot().getTemplate(uid));
  }
}
