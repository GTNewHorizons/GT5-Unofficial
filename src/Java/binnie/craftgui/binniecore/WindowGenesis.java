package binnie.craftgui.binniecore;

import binnie.Binnie;
import binnie.core.AbstractMod;
import binnie.core.BinnieCore;
import binnie.core.genetics.BreedingSystem;
import binnie.core.genetics.Gene;
import binnie.core.genetics.ManagerGenetics;
import binnie.craftgui.controls.listbox.ControlList;
import binnie.craftgui.controls.listbox.ControlListBox;
import binnie.craftgui.controls.listbox.ControlTextOption;
import binnie.craftgui.controls.tab.ControlTab;
import binnie.craftgui.controls.tab.ControlTabBar;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.events.EventHandler.Origin;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.events.EventValueChanged;
import binnie.craftgui.events.EventValueChanged.Handler;
import binnie.craftgui.minecraft.MinecraftGUI.PanelType;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlItemDisplay;
import binnie.craftgui.minecraft.control.ControlPlayerInventory;
import binnie.craftgui.minecraft.control.ControlTabIcon;
import binnie.craftgui.window.Panel;
import binnie.genetics.gui.ControlGenesisOption;
import cpw.mods.fml.relauncher.Side;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IChromosomeType;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpeciesRoot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class WindowGenesis
  extends Window
{
  private ISpeciesRoot root;
  private IAllele[] template;
  private ControlListBox<Gene> geneList;
  private ControlListBox<Gene> geneOptions;
  private Panel panelPickup;
  
  public WindowGenesis(EntityPlayer player, IInventory inventory, Side side)
  {
    super(342.0F, 228.0F, player, inventory, side);
  }
  
  protected AbstractMod getMod()
  {
    return BinnieCore.instance;
  }
  
  protected String getName()
  {
    return "Genesis";
  }
  
  public void initialiseClient()
  {
    new ControlPlayerInventory(this);
    setTitle("Genesis");
    
    ControlTabBar<BreedingSystem> tabSystems = new ControlTabBar(this, 8.0F, 28.0F, 23.0F, 100.0F, Position.Left)
    {
      public ControlTab<BreedingSystem> createTab(float x, float y, float w, float h, BreedingSystem value)
      {
        new ControlTabIcon(this, x, y, w, h, value)
        {
          public ItemStack getItemStack()
          {
            int type = ((BreedingSystem)this.value).getDefaultType();
            IIndividual ind = ((BreedingSystem)this.value).getDefaultIndividual();
            return ((BreedingSystem)this.value).getSpeciesRoot().getMemberStack(ind, type);
          }
          
          public String getName()
          {
            return ((BreedingSystem)this.value).getName();
          }
          
          public int getOutlineColour()
          {
            return ((BreedingSystem)this.value).getColour();
          }
          
          public boolean hasOutline()
          {
            return true;
          }
        };
      }
    };
    tabSystems.setValues(Binnie.Genetics.getActiveSystems());
    
    this.root = ((BreedingSystem)Binnie.Genetics.getActiveSystems().iterator().next()).getSpeciesRoot();
    this.template = this.root.getDefaultTemplate();
    
    IArea one = new IArea(32.0F, 28.0F, 170.0F, 100.0F);
    IArea two = new IArea(214.0F, 28.0F, 100.0F, 100.0F);
    
    new Panel(this, one.outset(1), MinecraftGUI.PanelType.Black);
    new Panel(this, two.outset(1), MinecraftGUI.PanelType.Black);
    
    this.geneList = new ControlListBox(this, one.x(), one.y(), one.w(), one.h(), 10.0F)
    {
      public IWidget createOption(Gene value, int y)
      {
        return new ControlGenesisOption((ControlList)getContent(), value, y);
      }
    };
    this.geneOptions = new ControlListBox(this, two.x(), two.y(), two.w(), two.h(), 10.0F)
    {
      public IWidget createOption(Gene value, int y)
      {
        return new ControlTextOption((ControlList)getContent(), value, y);
      }
    };
    tabSystems.addEventHandler(new EventValueChanged.Handler()
    {
      public void onEvent(EventValueChanged event)
      {
        WindowGenesis.this.root = ((BreedingSystem)event.getValue()).getSpeciesRoot();
        WindowGenesis.this.template = WindowGenesis.this.root.getDefaultTemplate();
        WindowGenesis.this.refreshTemplate(null);
      }
    }.setOrigin(EventHandler.Origin.Self, tabSystems));
    


    this.geneList.addEventHandler(new EventValueChanged.Handler()
    {
      public void onEvent(EventValueChanged event)
      {
        Map<IChromosomeType, List<IAllele>> map = Binnie.Genetics.getChromosomeMap(WindowGenesis.this.root);
        List<Gene> opts = new ArrayList();
        IChromosomeType chromo = ((Gene)event.value).getChromosome();
        for (IAllele allele : (List)map.get(chromo)) {
          opts.add(new Gene(allele, chromo, WindowGenesis.this.root));
        }
        WindowGenesis.this.geneOptions.setOptions(opts);
      }
    }.setOrigin(EventHandler.Origin.Self, this.geneList));
    


    this.geneOptions.addEventHandler(new EventValueChanged.Handler()
    {
      public void onEvent(EventValueChanged event)
      {
        if (event.value == null) {
          return;
        }
        IChromosomeType chromo = ((Gene)event.value).getChromosome();
        WindowGenesis.this.template[chromo.ordinal()] = ((Gene)event.value).getAllele();
        if (chromo == ((Gene)event.value).getSpeciesRoot().getKaryotypeKey()) {
          WindowGenesis.this.template = ((Gene)event.value).getSpeciesRoot().getTemplate(((Gene)event.value).getAllele().getUID());
        }
        WindowGenesis.this.refreshTemplate(chromo);
      }
    }.setOrigin(EventHandler.Origin.Self, this.geneOptions));
    



    this.panelPickup = new Panel(this, 16.0F, 140.0F, 60.0F, 42.0F, MinecraftGUI.PanelType.Black);
    
    refreshTemplate(null);
  }
  
  private void refreshTemplate(IChromosomeType selection)
  {
    List<Gene> genes = new ArrayList();
    IChromosomeType[] chromos = (IChromosomeType[])Binnie.Genetics.getChromosomeMap(this.root).keySet().toArray(new IChromosomeType[0]);
    for (IChromosomeType type : chromos)
    {
      IAllele allele = this.template[type.ordinal()];
      if (allele == null) {
        throw new NullPointerException("Allele missing for Chromosome " + type.getName());
      }
      genes.add(new Gene(allele, type, this.root));
    }
    Map<IChromosomeType, List<IAllele>> map = Binnie.Genetics.getChromosomeMap(this.root);
    this.geneList.setOptions(genes);
    if (selection != null) {
      this.geneList.setValue(new Gene(this.template[selection.ordinal()], selection, this.root));
    } else {
      this.geneOptions.setOptions(new ArrayList());
    }
    refreshPickup();
  }
  
  private void refreshPickup()
  {
    this.panelPickup.deleteAllChildren();
    int i = 0;
    for (int type : Binnie.Genetics.getSystem(this.root).getActiveTypes())
    {
      IIndividual ind = this.root.templateAsIndividual(this.template);
      ind.analyze();
      final ItemStack stack = this.root.getMemberStack(ind, type);
      ControlItemDisplay display = new ControlItemDisplay(this.panelPickup, 4 + i % 3 * 18, 4 + i / 3 * 18);
      display.setItemStack(stack);
      display.setTooltip();
      display.addEventHandler(new EventMouse.Down.Handler()
      {
        public void onEvent(EventMouse.Down event)
        {
          NBTTagCompound nbt = new NBTTagCompound();
          stack.writeToNBT(nbt);
          Window.get(event.getOrigin()).sendClientAction("genesis", nbt);
        }
      }.setOrigin(EventHandler.Origin.Self, display));
      


      i++;
    }
  }
  
  public void recieveGuiNBT(Side side, EntityPlayer player, String name, NBTTagCompound action)
  {
    super.recieveGuiNBT(side, player, name, action);
    if ((side == Side.SERVER) && (name.equals("genesis")))
    {
      ItemStack stack = ItemStack.loadItemStackFromNBT(action);
      InventoryPlayer playerInv = player.inventory;
      if (stack == null) {
        return;
      }
      if (playerInv.getItemStack() == null)
      {
        playerInv.setItemStack(stack);
      }
      else if ((playerInv.getItemStack().isItemEqual(stack)) && (ItemStack.areItemStackTagsEqual(playerInv.getItemStack(), stack)))
      {
        int fit = stack.getMaxStackSize() - (stack.stackSize + playerInv.getItemStack().stackSize);
        if (fit >= 0)
        {
          ItemStack rec = stack;
          rec.stackSize += playerInv.getItemStack().stackSize;
          playerInv.setItemStack(rec);
        }
      }
      player.openContainer.detectAndSendChanges();
      if ((player instanceof EntityPlayerMP)) {
        ((EntityPlayerMP)player).updateHeldItem();
      }
    }
  }
}
