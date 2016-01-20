package binnie.core.genetics;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.language.ManagerLanguage;
import binnie.core.resource.ManagerResource;
import binnie.extratrees.ExtraTrees;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleInteger;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IChromosomeType;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.ISpeciesRoot;
import forestry.api.lepidopterology.EnumButterflyChromosome;
import forestry.api.lepidopterology.EnumFlutterType;
import forestry.api.lepidopterology.IButterflyRoot;
import forestry.api.lepidopterology.ILepidopteristTracker;
import java.util.TreeSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

class MothBreedingSystem
  extends BreedingSystem
{
  public MothBreedingSystem()
  {
    this.iconUndiscovered = Binnie.Resource.getItemIcon(ExtraTrees.instance, "icon/undiscoveredMoth");
    this.iconDiscovered = Binnie.Resource.getItemIcon(ExtraTrees.instance, "icon/discoveredMoth");
  }
  
  public float getChance(IMutation mutation, EntityPlayer player, IAllele species1, IAllele species2)
  {
    return 0.0F;
  }
  
  public ISpeciesRoot getSpeciesRoot()
  {
    return Binnie.Genetics.getButterflyRoot();
  }
  
  public int getColour()
  {
    return 62194;
  }
  
  public Class<? extends IBreedingTracker> getTrackerClass()
  {
    return ILepidopteristTracker.class;
  }
  
  public String getAlleleName(IChromosomeType chromosome, IAllele allele)
  {
    if (chromosome == EnumButterflyChromosome.METABOLISM)
    {
      int metabolism = ((IAlleleInteger)allele).getValue();
      if (metabolism >= 19) {
        return Binnie.Language.localise(BinnieCore.instance, "allele.metabolism.highest");
      }
      if (metabolism >= 16) {
        return Binnie.Language.localise(BinnieCore.instance, "allele.metabolism.higher");
      }
      if (metabolism >= 13) {
        return Binnie.Language.localise(BinnieCore.instance, "allele.metabolism.high");
      }
      if (metabolism >= 10) {
        return Binnie.Language.localise(BinnieCore.instance, "allele.metabolism.normal");
      }
      if (metabolism >= 7) {
        return Binnie.Language.localise(BinnieCore.instance, "allele.metabolism.slow");
      }
      if (metabolism >= 4) {
        return Binnie.Language.localise(BinnieCore.instance, "allele.metabolism.slower");
      }
      return Binnie.Language.localise(BinnieCore.instance, "allele.metabolism.slowest");
    }
    if (chromosome == EnumButterflyChromosome.FERTILITY)
    {
      int metabolism = ((IAlleleInteger)allele).getValue();
      return metabolism + "x";
    }
    return super.getAlleleName(chromosome, allele);
  }
  
  public boolean isDNAManipulable(ItemStack member)
  {
    return ((IButterflyRoot)getSpeciesRoot()).getType(member) == EnumFlutterType.SERUM;
  }
  
  public int[] getActiveTypes()
  {
    return new int[] { EnumFlutterType.BUTTERFLY.ordinal(), EnumFlutterType.CATERPILLAR.ordinal(), EnumFlutterType.SERUM.ordinal() };
  }
  
  public void addExtraAlleles(IChromosomeType chromosome, TreeSet<IAllele> alleles)
  {
    switch (1.$SwitchMap$forestry$api$lepidopterology$EnumButterflyChromosome[((EnumButterflyChromosome)chromosome).ordinal()])
    {
    case 1: 
      for (ForestryAllele.Int a : ForestryAllele.Int.values()) {
        alleles.add(a.getAllele());
      }
      break;
    case 2: 
      for (ForestryAllele.Lifespan a : ForestryAllele.Lifespan.values()) {
        alleles.add(a.getAllele());
      }
      break;
    case 3: 
      for (ForestryAllele.Int a : ForestryAllele.Int.values()) {
        alleles.add(a.getAllele());
      }
      break;
    case 4: 
    case 5: 
    case 6: 
      for (ForestryAllele.Bool a : ForestryAllele.Bool.values()) {
        alleles.add(a.getAllele());
      }
      break;
    case 7: 
      for (ForestryAllele.Size a : ForestryAllele.Size.values()) {
        alleles.add(a.getAllele());
      }
      break;
    case 8: 
      for (ForestryAllele.Speed a : ForestryAllele.Speed.values()) {
        alleles.add(a.getAllele());
      }
      break;
    case 9: 
    case 10: 
      for (Tolerance a : Tolerance.values()) {
        alleles.add(a.getAllele());
      }
      break;
    }
  }
}
