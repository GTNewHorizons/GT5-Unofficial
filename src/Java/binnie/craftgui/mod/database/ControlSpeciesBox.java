package binnie.craftgui.mod.database;

import binnie.core.genetics.BreedingSystem;
import binnie.craftgui.controls.listbox.ControlList;
import binnie.craftgui.controls.listbox.ControlListBox;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.minecraft.Window;
import com.mojang.authlib.GameProfile;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IClassification;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;

public class ControlSpeciesBox
  extends ControlListBox<IAlleleSpecies>
{
  public IWidget createOption(IAlleleSpecies value, int y)
  {
    return new ControlSpeciexBoxOption((ControlList)getContent(), value, y);
  }
  
  public ControlSpeciesBox(IWidget parent, float x, float y, float width, float height)
  {
    super(parent, x, y, width, height, 12.0F);
  }
  
  private IClassification branch = null;
  
  public void setBranch(IClassification branch)
  {
    if (branch != this.branch)
    {
      this.branch = branch;
      List<IAlleleSpecies> speciesList2 = new ArrayList();
      movePercentage(-100.0F);
      setOptions(speciesList2);
      
      EntityPlayer player = Window.get(this).getPlayer();
      GameProfile playerName = Window.get(this).getUsername();
      WindowAbstractDatabase db = (WindowAbstractDatabase)Window.get(this);
      
      Collection<IAlleleSpecies> speciesList = !db.isNEI ? db.getBreedingSystem().getDiscoveredSpecies(db.getWorld(), playerName) : db.getBreedingSystem().getAllSpecies();
      if (branch != null) {
        for (IAlleleSpecies species : branch.getMemberSpecies()) {
          if (speciesList.contains(species)) {
            speciesList2.add(species);
          }
        }
      }
      setOptions(speciesList2);
    }
  }
}
