package binnie.craftgui.extratrees.dictionary;

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
import binnie.extratrees.ExtraTrees;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

public class WindowLepidopteristDatabase
  extends WindowAbstractDatabase
{
  public WindowLepidopteristDatabase(EntityPlayer player, Side side, boolean nei)
  {
    super(player, side, nei, Binnie.Genetics.mothBreedingSystem, 160.0F);
  }
  
  public static Window create(EntityPlayer player, Side side, boolean nei)
  {
    return new WindowLepidopteristDatabase(player, side, nei);
  }
  
  protected void addTabs()
  {
    new PageSpeciesOverview(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraTrees.instance, "butterfly.species.overview", 0));
    new PageSpeciesClassification(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraTrees.instance, "butterfly.species.classification", 0));
    new PageSpeciesImage(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraTrees.instance, "butterfly.species.specimen", 0));
    new PageSpeciesResultant(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraTrees.instance, "butterfly.species.resultant", 0));
    new PageSpeciesMutations(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraTrees.instance, "butterfly.species.further", 0));
    
    new PageBranchOverview(getInfoPages(WindowAbstractDatabase.Mode.Branches), new DatabaseTab(ExtraTrees.instance, "butterfly.branches.overview", 0));
    new PageBranchSpecies(getInfoPages(WindowAbstractDatabase.Mode.Branches), new DatabaseTab(ExtraTrees.instance, "butterfly.branches.species", 0));
    
    new PageBreeder(getInfoPages(WindowAbstractDatabase.Mode.Breeder), getUsername(), new DatabaseTab(ExtraTrees.instance, "butterfly.breeder", 0));
  }
  
  protected AbstractMod getMod()
  {
    return ExtraTrees.instance;
  }
  
  protected String getName()
  {
    return "MothDatabase";
  }
}
