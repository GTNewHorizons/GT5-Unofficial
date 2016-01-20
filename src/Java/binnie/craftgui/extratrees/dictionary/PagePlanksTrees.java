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
import forestry.api.genetics.IAlleleSpecies;
import java.util.Collection;
import net.minecraft.item.ItemStack;

public class PagePlanksTrees
  extends PageAbstract<ItemStack>
{
  public PagePlanksTrees(IWidget parent, DatabaseTab tab)
  {
    super(parent, tab);
  }
  
  public void onValueChanged(ItemStack species)
  {
    deleteAllChildren();
    WindowAbstractDatabase database = (WindowAbstractDatabase)WindowAbstractDatabase.get(this);
    new ControlText(this, new IArea(0.0F, 0.0F, size().x(), 24.0F), species.getDisplayName(), TextJustification.MiddleCenter);
    


    Collection<IAlleleSpecies> trees = ((TreeBreedingSystem)database.getBreedingSystem()).getTreesThatMakePlanks(species, database.isNEI(), database.getWorld(), database.getUsername());
    



    new ControlSpeciesBox(this, 4.0F, 24.0F, size().x() - 8.0F, size().y() - 4.0F - 24.0F).setOptions(trees);
  }
}
