package binnie.craftgui.extratrees.dictionary;

import binnie.Binnie;
import binnie.core.AbstractMod;
import binnie.core.genetics.ManagerGenetics;
import binnie.core.genetics.TreeBreedingSystem;
import binnie.craftgui.controls.listbox.ControlList;
import binnie.craftgui.controls.listbox.ControlListBox;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.mod.database.ControlItemStackOption;
import binnie.craftgui.mod.database.DatabaseTab;
import binnie.craftgui.mod.database.IDatabaseMode;
import binnie.craftgui.mod.database.PageBranchOverview;
import binnie.craftgui.mod.database.PageBranchSpecies;
import binnie.craftgui.mod.database.PageBreeder;
import binnie.craftgui.mod.database.PageSpeciesClassification;
import binnie.craftgui.mod.database.PageSpeciesMutations;
import binnie.craftgui.mod.database.PageSpeciesOverview;
import binnie.craftgui.mod.database.PageSpeciesResultant;
import binnie.craftgui.mod.database.WindowAbstractDatabase;
import binnie.craftgui.mod.database.WindowAbstractDatabase.Mode;
import binnie.craftgui.mod.database.WindowAbstractDatabase.ModeWidgets;
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.proxy.Proxy;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class WindowArboristDatabase
  extends WindowAbstractDatabase
{
  ControlListBox<ItemStack> selectionBoxFruit;
  ControlListBox<ItemStack> selectionBoxWood;
  ControlListBox<ItemStack> selectionBoxPlanks;
  
  static enum TreeMode
    implements IDatabaseMode
  {
    Fruit,  Wood,  Planks;
    
    private TreeMode() {}
    
    public String getName()
    {
      return ExtraTrees.proxy.localise("gui.database.mode." + name().toLowerCase());
    }
  }
  
  public WindowArboristDatabase(EntityPlayer player, Side side, boolean nei)
  {
    super(player, side, nei, Binnie.Genetics.treeBreedingSystem, 120.0F);
  }
  
  public static Window create(EntityPlayer player, Side side, boolean nei)
  {
    return new WindowArboristDatabase(player, side, nei);
  }
  
  protected void addTabs()
  {
    new PageSpeciesOverview(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraTrees.instance, "species.overview", 0));
    new PageSpeciesTreeGenome(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraTrees.instance, "species.genome", 0));
    new PageSpeciesClassification(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraTrees.instance, "species.classification", 0));
    new PageSpeciesResultant(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraTrees.instance, "species.resultant", 0));
    new PageSpeciesMutations(getInfoPages(WindowAbstractDatabase.Mode.Species), new DatabaseTab(ExtraTrees.instance, "species.further", 0));
    
    new PageBranchOverview(getInfoPages(WindowAbstractDatabase.Mode.Branches), new DatabaseTab(ExtraTrees.instance, "branches.overview", 0));
    new PageBranchSpecies(getInfoPages(WindowAbstractDatabase.Mode.Branches), new DatabaseTab(ExtraTrees.instance, "branches.species", 0));
    
    new PageBreeder(getInfoPages(WindowAbstractDatabase.Mode.Breeder), getUsername(), new DatabaseTab(ExtraTrees.instance, "breeder", 0));
    
    createMode(TreeMode.Fruit, new WindowAbstractDatabase.ModeWidgets(TreeMode.Fruit, this)
    {
      public void createListBox(IArea area)
      {
        this.listBox = new ControlListBox(this.modePage, area.x(), area.y(), area.w(), area.h(), 12.0F)
        {
          public IWidget createOption(ItemStack value, int y)
          {
            return new ControlItemStackOption((ControlList)getContent(), value, y);
          }
        };
        this.listBox.setOptions(((TreeBreedingSystem)WindowArboristDatabase.this.getBreedingSystem()).allFruits);
      }
    });
    createMode(TreeMode.Wood, new WindowAbstractDatabase.ModeWidgets(TreeMode.Wood, this)
    {
      public void createListBox(IArea area)
      {
        this.listBox = new ControlListBox(this.modePage, area.x(), area.y(), area.w(), area.h(), 12.0F)
        {
          public IWidget createOption(ItemStack value, int y)
          {
            return new ControlItemStackOption((ControlList)getContent(), value, y);
          }
        };
        this.listBox.setOptions(((TreeBreedingSystem)WindowArboristDatabase.this.getBreedingSystem()).allWoods);
      }
    });
    createMode(TreeMode.Planks, new WindowAbstractDatabase.ModeWidgets(TreeMode.Planks, this)
    {
      public void createListBox(IArea area)
      {
        this.listBox = new ControlListBox(this.modePage, area.x(), area.y(), area.w(), area.h(), 12.0F)
        {
          public IWidget createOption(ItemStack value, int y)
          {
            return new ControlItemStackOption((ControlList)getContent(), value, y);
          }
        };
      }
    });
    new PageFruit(getInfoPages(TreeMode.Fruit), new DatabaseTab(ExtraTrees.instance, "fruit.natural", 0), true);
    new PageFruit(getInfoPages(TreeMode.Fruit), new DatabaseTab(ExtraTrees.instance, "fruit.potential", 0), false);
    
    new PageWood(getInfoPages(TreeMode.Wood), new DatabaseTab(ExtraTrees.instance, "wood.natural", 0));
    
    new PagePlanksOverview(getInfoPages(TreeMode.Planks), new DatabaseTab(ExtraTrees.instance, "planks.overview", 0));
    new PagePlanksTrees(getInfoPages(TreeMode.Planks), new DatabaseTab(ExtraTrees.instance, "planks.natural", 1));
  }
  
  protected AbstractMod getMod()
  {
    return ExtraTrees.instance;
  }
  
  protected String getName()
  {
    return "TreeDatabase";
  }
}
