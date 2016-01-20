package binnie.craftgui.extratrees.dictionary;

import binnie.core.genetics.TreeBreedingSystem;
import binnie.craftgui.controls.ControlText;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.TextJustification;
import binnie.craftgui.mod.database.ControlSpeciesBox;
import binnie.craftgui.mod.database.DatabaseTab;
import binnie.craftgui.mod.database.PageAbstract;
import binnie.craftgui.mod.database.WindowAbstractDatabase;
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.proxy.Proxy;
import forestry.api.genetics.IAlleleSpecies;
import java.util.Collection;
import net.minecraft.item.ItemStack;

public class PageFruit
  extends PageAbstract<ItemStack>
{
  boolean treesThatBearFruit;
  
  public PageFruit(IWidget parent, DatabaseTab tab, boolean treesThatBearFruit)
  {
    super(parent, tab);
    this.treesThatBearFruit = treesThatBearFruit;
  }
  
  public void onValueChanged(ItemStack species)
  {
    deleteAllChildren();
    WindowAbstractDatabase database = (WindowAbstractDatabase)WindowAbstractDatabase.get(this);
    new ControlText(this, new IArea(0.0F, 0.0F, size().x(), 24.0F), ExtraTrees.proxy.localise("gui.database.tab.fruit." + (this.treesThatBearFruit ? "natural" : "potential")), TextJustification.MiddleCenter);
    

    Collection<IAlleleSpecies> trees = this.treesThatBearFruit ? ((TreeBreedingSystem)database.getBreedingSystem()).getTreesThatBearFruit(species, database.isNEI(), database.getWorld(), database.getUsername()) : ((TreeBreedingSystem)database.getBreedingSystem()).getTreesThatCanBearFruit(species, database.isNEI(), database.getWorld(), database.getUsername());
    






    new ControlSpeciesBox(this, 4.0F, 24.0F, size().x() - 8.0F, size().y() - 4.0F - 24.0F).setOptions(trees);
  }
}
