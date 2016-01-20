package binnie.extrabees.gui.database;

import binnie.Binnie;
import binnie.core.AbstractMod;
import binnie.core.genetics.ManagerGenetics;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.mod.database.DatabaseTab;
import binnie.craftgui.mod.database.PageBranchOverview;
import binnie.craftgui.mod.database.PageBranchSpecies;
import binnie.craftgui.mod.database.PageBreeder;
import binnie.craftgui.mod.database.PageSpeciesClassification;
import binnie.craftgui.mod.database.PageSpeciesMutations;
import binnie.craftgui.mod.database.PageSpeciesOverview;
import binnie.craftgui.mod.database.PageSpeciesResultant;
import binnie.craftgui.mod.database.WindowAbstractDatabase;
import binnie.craftgui.mod.database.WindowAbstractDatabase.Mode;
import binnie.extrabees.ExtraBees;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

public class WindowApiaristDatabase
  extends WindowAbstractDatabase
{
  static enum SpeciesTab
  {
    Overview(255),  Genome(16776960),  Productivity(65535),  Climate(16711680),  ResultantMutations(16711935),  FurtherMutations(65280);
    
    public int colour;
    
    private SpeciesTab(int colour)
    {
      this.colour = colour;
    }
  }
  
  static enum BranchesTab
  {
    Overview(255),  Species(16711680);
    
    public int colour;
    
    private BranchesTab(int colour)
    {
      this.colour = colour;
    }
  }
  
  protected void addTabs()
  {
    new PageSpeciesOverview(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraBees.instance, "species.overview", 0));
    new PageSpeciesClassification(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraBees.instance, "species.classification", 0));
    new PageSpeciesGenome(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraBees.instance, "species.genome", 0));
    new PageSpeciesProducts(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraBees.instance, "species.products", 0));
    new PageSpeciesClimate(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraBees.instance, "species.climate", 0));
    new PageSpeciesResultant(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraBees.instance, "species.resultant", 0));
    new PageSpeciesMutations(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraBees.instance, "species.further", 0));
    
    new PageBranchOverview(getInfoPages(WindowAbstractDatabase.Mode.Branches), new DatabaseTab(ExtraBees.instance, "branches.overview", 0));
    new PageBranchSpecies(getInfoPages(WindowAbstractDatabase.Mode.Branches), new DatabaseTab(ExtraBees.instance, "branches.species", 0));
    
    new PageBreeder(getInfoPages(WindowAbstractDatabase.Mode.Breeder), getUsername(), new DatabaseTab(ExtraBees.instance, "breeder", 0));
  }
  
  public WindowApiaristDatabase(EntityPlayer player, Side side, boolean nei)
  {
    super(player, side, nei, Binnie.Genetics.beeBreedingSystem, 110.0F);
  }
  
  public static Window create(EntityPlayer player, Side side, boolean nei)
  {
    return new WindowApiaristDatabase(player, side, nei);
  }
  
  public AbstractMod getMod()
  {
    return ExtraBees.instance;
  }
  
  public String getName()
  {
    return "Database";
  }
}
