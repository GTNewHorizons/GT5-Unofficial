package binnie.extrabees.gui.database;

import binnie.craftgui.controls.ControlText;
import binnie.craftgui.controls.ControlTextCentered;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.mod.database.DatabaseTab;
import binnie.craftgui.mod.database.PageSpecies;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.IAlleleSpecies;

public class PageSpeciesProducts
  extends PageSpecies
{
  ControlText pageSpeciesProducts_Title;
  ControlProductsBox pageSpeciesProducts_Products;
  ControlText pageSpeciesProducts_Title2;
  ControlProductsBox pageSpeciesProducts_Specialties;
  
  public PageSpeciesProducts(IWidget parent, DatabaseTab tab)
  {
    super(parent, tab);
    
    this.pageSpeciesProducts_Title = new ControlTextCentered(this, 8.0F, "Products");
    
    this.pageSpeciesProducts_Products = new ControlProductsBox(this, 4, 20, 136, 48, ControlProductsBox.Type.Products);
    

    this.pageSpeciesProducts_Title2 = new ControlTextCentered(this, 68.0F, "Specialties");
    
    this.pageSpeciesProducts_Specialties = new ControlProductsBox(this, 4, 80, 136, 48, ControlProductsBox.Type.Specialties);
  }
  
  public void onValueChanged(IAlleleSpecies species)
  {
    this.pageSpeciesProducts_Products.setSpecies((IAlleleBeeSpecies)species);
    this.pageSpeciesProducts_Specialties.setSpecies((IAlleleBeeSpecies)species);
  }
}
