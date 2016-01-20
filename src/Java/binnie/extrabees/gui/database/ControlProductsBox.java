package binnie.extrabees.gui.database;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.genetics.ManagerGenetics;
import binnie.core.proxy.BinnieProxy;
import binnie.craftgui.controls.listbox.ControlList;
import binnie.craftgui.controls.listbox.ControlListBox;
import binnie.craftgui.core.IWidget;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.apiculture.IBeekeepingMode;
import forestry.api.genetics.IAllele;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.item.ItemStack;

public class ControlProductsBox
  extends ControlListBox<Product>
{
  private int index;
  private Type type;
  
  public IWidget createOption(Product value, int y)
  {
    return new ControlProductsItem((ControlList)getContent(), value, y);
  }
  
  static enum Type
  {
    Products,  Specialties;
    
    private Type() {}
  }
  
  class Product
  {
    ItemStack item;
    float chance;
    
    public Product(ItemStack item, float chance)
    {
      this.item = item;
      this.chance = chance;
    }
  }
  
  public ControlProductsBox(IWidget parent, int x, int y, int width, int height, Type type)
  {
    super(parent, x, y, width, height, 12.0F);
    this.type = type;
  }
  
  IAlleleBeeSpecies species = null;
  
  public void setSpecies(IAlleleBeeSpecies species)
  {
    if (species != this.species)
    {
      this.species = species;
      if (species != null)
      {
        IAllele[] template = Binnie.Genetics.getBeeRoot().getTemplate(species.getUID());
        if (template == null) {
          return;
        }
        IBeeGenome genome = Binnie.Genetics.getBeeRoot().templateAsGenome(template);
        
        float speed = genome.getSpeed();
        
        float modeSpeed = Binnie.Genetics.getBeeRoot().getBeekeepingMode(BinnieCore.proxy.getWorld()).getProductionModifier(genome, 1.0F);
        


        List<Product> strings = new ArrayList();
        if (this.type == Type.Products) {
          for (Map.Entry<ItemStack, Integer> entry : species.getProducts().entrySet()) {
            strings.add(new Product((ItemStack)entry.getKey(), speed * modeSpeed * ((Integer)entry.getValue()).intValue()));
          }
        } else {
          for (Map.Entry<ItemStack, Integer> entry : species.getSpecialty().entrySet()) {
            strings.add(new Product((ItemStack)entry.getKey(), speed * modeSpeed * ((Integer)entry.getValue()).intValue()));
          }
        }
        setOptions(strings);
      }
    }
  }
}
