package binnie.core.genetics;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.language.ManagerLanguage;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IChromosomeType;
import forestry.api.genetics.IClassification;
import forestry.api.genetics.ISpeciesRoot;
import java.util.Map;
import net.minecraft.item.ItemStack;

abstract interface IBreedingMessage
{
  public abstract String getTitle();
  
  public abstract String getBody();
  
  public abstract ItemStack getIcon();
  
  public static class MessageSpeciesDiscovered
    implements IBreedingMessage
  {
    IAlleleSpecies species;
    ItemStack stack;
    
    public MessageSpeciesDiscovered(IAlleleSpecies species)
    {
      this.species = species;
      ISpeciesRoot root = null;
      for (ISpeciesRoot sRoot : AlleleManager.alleleRegistry.getSpeciesRoot().values()) {
        if (sRoot.getKaryotype()[0].getAlleleClass().isInstance(species)) {
          root = sRoot;
        }
      }
      if (root != null) {
        this.stack = root.getMemberStack(root.templateAsIndividual(root.getTemplate(species.getUID())), 0);
      }
    }
    
    public String getTitle()
    {
      return Binnie.Language.localise(BinnieCore.instance, "gui.breedingmessage.species");
    }
    
    public String getBody()
    {
      return this.species.getName();
    }
    
    public ItemStack getIcon()
    {
      return this.stack;
    }
  }
  
  public static class BranchDiscovered
    implements IBreedingMessage
  {
    IAlleleSpecies species;
    IClassification classification;
    ItemStack stack;
    
    public BranchDiscovered(IAlleleSpecies species, IClassification classification)
    {
      this.species = species;
      this.classification = classification;
      ISpeciesRoot root = null;
      for (ISpeciesRoot sRoot : AlleleManager.alleleRegistry.getSpeciesRoot().values()) {
        if (sRoot.getKaryotype()[0].getAlleleClass().isInstance(species)) {
          root = sRoot;
        }
      }
      if (root != null) {
        this.stack = root.getMemberStack(root.templateAsIndividual(root.getTemplate(species.getUID())), 0);
      }
    }
    
    public String getTitle()
    {
      return Binnie.Language.localise(BinnieCore.instance, "gui.breedingmessage.branch");
    }
    
    public String getBody()
    {
      return this.classification.getScientific();
    }
    
    public ItemStack getIcon()
    {
      return this.stack;
    }
  }
  
  public static class EpithetGained
    implements IBreedingMessage
  {
    String epithet;
    ItemStack stack;
    
    public EpithetGained(String epithet, ISpeciesRoot root)
    {
      this.epithet = epithet;
      this.stack = root.getMemberStack(root.templateAsIndividual(root.getDefaultTemplate()), 0);
    }
    
    public String getTitle()
    {
      return Binnie.Language.localise(BinnieCore.instance, "gui.breedingmessage.epithet");
    }
    
    public String getBody()
    {
      return this.epithet;
    }
    
    public ItemStack getIcon()
    {
      return this.stack;
    }
  }
}
