package binnie.craftgui.mod.database;

import binnie.Binnie;
import binnie.core.genetics.BreedingSystem;
import binnie.core.genetics.ManagerGenetics;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlItemDisplay;
import com.mojang.authlib.GameProfile;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpeciesRoot;
import net.minecraft.util.IIcon;

public class ControlDatabaseIndividualDisplay
  extends ControlItemDisplay
  implements ITooltip
{
  public void setSpecies(IAlleleSpecies species)
  {
    setSpecies(species, EnumDiscoveryState.Show);
  }
  
  public void setSpecies(IAlleleSpecies species, EnumDiscoveryState state)
  {
    ISpeciesRoot speciesRoot = Binnie.Genetics.getSpeciesRoot(species);
    
    BreedingSystem system = Binnie.Genetics.getSystem(speciesRoot.getUID());
    
    IIndividual ind = system.getSpeciesRoot().templateAsIndividual(system.getSpeciesRoot().getTemplate(species.getUID()));
    
    super.setItemStack(system.getSpeciesRoot().getMemberStack(ind, system.getDefaultType()));
    this.species = species;
    
    GameProfile username = Window.get(this).getUsername();
    if (state == EnumDiscoveryState.Undetermined) {
      state = system.isSpeciesDiscovered(species, Window.get(this).getWorld(), username) ? EnumDiscoveryState.Discovered : EnumDiscoveryState.Undiscovered;
    }
    if (((Window.get(this) instanceof WindowAbstractDatabase)) && 
      (((WindowAbstractDatabase)Window.get(this)).isNEI)) {
      state = EnumDiscoveryState.Show;
    }
    this.discovered = state;
    
    addAttribute(Attribute.MouseOver);
  }
  
  private IAlleleSpecies species = null;
  EnumDiscoveryState discovered = EnumDiscoveryState.Show;
  
  public ControlDatabaseIndividualDisplay(IWidget parent, float x, float y)
  {
    this(parent, x, y, 16.0F);
  }
  
  public ControlDatabaseIndividualDisplay(IWidget parent, float x, float y, float size)
  {
    super(parent, x, y, size);
    

    addSelfEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        if ((event.getButton() == 0) && (ControlDatabaseIndividualDisplay.this.species != null) && (EnumDiscoveryState.Show == ControlDatabaseIndividualDisplay.this.discovered)) {
          ((WindowAbstractDatabase)ControlDatabaseIndividualDisplay.this.getSuperParent()).gotoSpeciesDelayed(ControlDatabaseIndividualDisplay.this.species);
        }
      }
    });
  }
  
  public void onRenderForeground()
  {
    IIcon icon = null;
    if (this.species == null) {
      return;
    }
    BreedingSystem system = Binnie.Genetics.getSystem(this.species.getRoot());
    switch (2.$SwitchMap$binnie$craftgui$mod$database$EnumDiscoveryState[this.discovered.ordinal()])
    {
    case 1: 
      super.onRenderForeground();
      return;
    case 2: 
      icon = system.getDiscoveredIcon();
      break;
    case 3: 
      icon = system.getUndiscoveredIcon();
      break;
    }
    if (icon != null) {
      CraftGUI.Render.iconItem(IPoint.ZERO, icon);
    }
  }
  
  public void getTooltip(Tooltip tooltip)
  {
    if (this.species != null) {
      switch (2.$SwitchMap$binnie$craftgui$mod$database$EnumDiscoveryState[this.discovered.ordinal()])
      {
      case 1: 
        tooltip.add(this.species.getName());
        break;
      case 2: 
        tooltip.add("Discovered Species");
        break;
      case 3: 
        tooltip.add("Undiscovered Species");
      }
    }
  }
}
