package binnie.craftgui.mod.database;

import binnie.core.genetics.BreedingSystem;
import binnie.craftgui.controls.ControlTextCentered;
import binnie.craftgui.controls.page.ControlPage;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.minecraft.Window;
import com.mojang.authlib.GameProfile;
import java.util.List;

public class PageBreeder
  extends ControlPage<DatabaseTab>
{
  private GameProfile player;
  
  public PageBreeder(IWidget parent, GameProfile player, DatabaseTab tab)
  {
    super(parent, 0.0F, 0.0F, parent.getSize().x(), parent.getSize().y(), tab);
    
    this.player = player;
    onPageRefresh();
  }
  
  public void onPageRefresh()
  {
    while (getWidgets().size() > 0) {
      deleteChild((IWidget)getWidgets().get(0));
    }
    BreedingSystem system = ((WindowAbstractDatabase)Window.get(this)).getBreedingSystem();
    
    String descriptor = system.getDescriptor();
    
    new ControlTextCentered(this, 8.0F, "§n" + system.getDescriptor() + " Profile§r");
    
    new ControlTextCentered(this, 75.0F, "" + system.discoveredSpeciesCount + "/" + system.totalSpeciesCount + " Species");
    

    new ControlBreedingProgress(this, 20, 87, 102, 14, system, system.discoveredSpeciesPercentage);
    
    new ControlTextCentered(this, 115.0F, "" + system.discoveredBranchCount + "/" + system.totalBranchCount + " Branches");
    

    new ControlBreedingProgress(this, 20, 127, 102, 14, system, system.discoveredBranchPercentage);
    if (system.discoveredSecretCount > 0) {
      new ControlTextCentered(this, 155.0F, "" + system.discoveredSecretCount + "/" + system.totalSecretCount + " Secret Species");
    }
    new ControlTextCentered(this, 32.0F, this.player.getName());
    new ControlTextCentered(this, 44.0F, "§o" + system.getEpitome() + "§r");
  }
}
