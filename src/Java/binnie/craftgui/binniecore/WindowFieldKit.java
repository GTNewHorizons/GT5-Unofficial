package binnie.craftgui.binniecore;

import binnie.Binnie;
import binnie.core.AbstractMod;
import binnie.core.BinnieCore;
import binnie.core.genetics.BreedingSystem;
import binnie.core.genetics.ManagerGenetics;
import binnie.core.language.ManagerLanguage;
import binnie.core.machines.inventory.SlotValidator;
import binnie.core.machines.inventory.ValidatorIcon;
import binnie.craftgui.controls.ControlText;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.TextJustification;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventHandler.Origin;
import binnie.craftgui.events.EventValueChanged;
import binnie.craftgui.events.EventValueChanged.Handler;
import binnie.craftgui.minecraft.InventoryType;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.WindowInventory;
import binnie.craftgui.minecraft.control.ControlImage;
import binnie.craftgui.minecraft.control.ControlPlayerInventory;
import binnie.craftgui.minecraft.control.ControlSlot;
import binnie.craftgui.resource.StyleSheet;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import binnie.craftgui.resource.minecraft.PaddedTexture;
import binnie.craftgui.resource.minecraft.StandardTexture;
import binnie.extrabees.core.ExtraBeeTexture;
import binnie.extrabees.gui.punnett.ExtraBeeGUITexture;
import binnie.genetics.gui.ControlChromosome;
import binnie.genetics.machine.Analyser;
import cpw.mods.fml.relauncher.Side;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.IChromosomeType;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpeciesRoot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class WindowFieldKit
  extends Window
{
  public WindowFieldKit(EntityPlayer player, IInventory inventory, Side side)
  {
    super(280.0F, 230.0F, player, inventory, side);
  }
  
  protected AbstractMod getMod()
  {
    return BinnieCore.instance;
  }
  
  protected String getName()
  {
    return "Field Kit";
  }
  
  private void setupValidators()
  {
    getWindowInventory().setValidator(0, new SlotValidator(null)
    {
      public boolean isValid(ItemStack object)
      {
        return (AlleleManager.alleleRegistry.isIndividual(object)) || (Binnie.Genetics.getConversion(object) != null);
      }
      
      public String getTooltip()
      {
        return "Individual";
      }
    });
    getWindowInventory().setValidator(1, new SlotValidator(null)
    {
      public boolean isValid(ItemStack object)
      {
        return object.getItem() == Items.paper;
      }
      
      public String getTooltip()
      {
        return "Paper";
      }
    });
    getWindowInventory().disableAutoDispense(1);
  }
  
  public void initialiseClient()
  {
    setTitle("Field Kit");
    
    CraftGUI.Render.stylesheet(new StyleSheetPunnett());
    
    getWindowInventory().createSlot(0);
    getWindowInventory().createSlot(1);
    
    setupValidators();
    
    new ControlPlayerInventory(this);
    
    IPoint handGlass = new IPoint(16.0F, 32.0F);
    this.GlassControl = new ControlImage(this, handGlass.x(), handGlass.y(), new StandardTexture(0, 160, 96, 96, ExtraBeeTexture.GUIPunnett));
    new ControlSlot(this, handGlass.x() + 54.0F, handGlass.y() + 26.0F).assign(InventoryType.Window, 0);
    
    new ControlSlot(this, 208.0F, 8.0F).assign(InventoryType.Window, 1);
    
    this.text = new ControlText(this, new IPoint(232.0F, 13.0F), "Paper");
    this.text.setColour(2236962);
    
    this.text = new ControlText(this, new IArea(0.0F, 120.0F, w(), 24.0F), "", TextJustification.MiddleCenter);
    this.text.setColour(2236962);
    
    this.chromo = new ControlChromosome(this, 150.0F, 24.0F);
    
    addEventHandler(new EventValueChanged.Handler()
    {
      public void onEvent(EventValueChanged event)
      {
        IChromosomeType type = (IChromosomeType)event.getValue();
        if ((type != null) && (WindowFieldKit.this.info.containsKey(type)))
        {
          String t = (String)WindowFieldKit.this.info.get(type);
          WindowFieldKit.this.text.setValue(t);
        }
        else
        {
          WindowFieldKit.this.text.setValue("");
        }
      }
    }.setOrigin(EventHandler.Origin.DirectChild, this.chromo));
  }
  
  public void initialiseServer()
  {
    ItemStack kit = getPlayer().getHeldItem();
    int sheets = 64 - kit.getItemDamage();
    if (sheets != 0) {
      getWindowInventory().setInventorySlotContents(1, new ItemStack(Items.paper, sheets));
    }
    setupValidators();
  }
  
  private float glassOffsetX = 0.0F;
  private float glassOffsetY = 0.0F;
  private float glassVX = 0.0F;
  private float glassVY = 0.0F;
  private Random glassRand = new Random();
  private Control GlassControl;
  private ControlChromosome chromo;
  private ControlText text;
  private float analyseProgress = 1.0F;
  private boolean isAnalysing = false;
  
  public void onUpdateClient()
  {
    super.onUpdateClient();
    if (this.isAnalysing)
    {
      this.analyseProgress += 0.01F;
      if (this.analyseProgress >= 1.0F)
      {
        this.isAnalysing = false;
        this.analyseProgress = 1.0F;
        ItemStack stack = getWindowInventory().getStackInSlot(0);
        if (stack != null) {
          sendClientAction("analyse", new NBTTagCompound());
        }
        refreshSpecies();
      }
    }
    this.glassVX += this.glassRand.nextFloat() - 0.5F - this.glassOffsetX * 0.2F;
    this.glassVY += this.glassRand.nextFloat() - 0.5F - this.glassOffsetY * 0.2F;
    
    this.glassOffsetX += this.glassVX;
    this.glassOffsetX *= (1.0F - this.analyseProgress);
    this.glassOffsetY += this.glassVY;
    this.glassOffsetY *= (1.0F - this.analyseProgress);
    
    this.GlassControl.setOffset(new IPoint(this.glassOffsetX, this.glassOffsetY));
  }
  
  private Map<IChromosomeType, String> info = new HashMap();
  
  private void refreshSpecies()
  {
    ItemStack item = getWindowInventory().getStackInSlot(0);
    if ((item == null) || (!AlleleManager.alleleRegistry.isIndividual(item))) {
      return;
    }
    IIndividual ind = AlleleManager.alleleRegistry.getIndividual(item);
    if (ind == null) {
      return;
    }
    ISpeciesRoot root = AlleleManager.alleleRegistry.getSpeciesRoot(item);
    this.chromo.setRoot(root);
    Random rand = new Random();
    this.info.clear();
    for (IChromosomeType type : root.getKaryotype()) {
      if (!Binnie.Genetics.isInvalidChromosome(type))
      {
        IAllele allele = ind.getGenome().getActiveAllele(type);
        List<String> infos = new ArrayList();
        int i = 0;
        String pref = root.getUID() + ".fieldkit." + type.getName().toLowerCase() + ".";
        while (Binnie.Language.canLocalise(pref + i))
        {
          infos.add(Binnie.Language.localise(pref + i));
          i++;
        }
        String text = Binnie.Genetics.getSystem(root).getAlleleName(type, allele);
        if (!infos.isEmpty()) {
          text = (String)infos.get(rand.nextInt(infos.size()));
        }
        this.info.put(type, text);
        this.chromo.setRoot(root);
      }
    }
  }
  
  private ItemStack prev = null;
  
  public void onWindowInventoryChanged()
  {
    super.onWindowInventoryChanged();
    if (isServer())
    {
      ItemStack kit = getPlayer().getHeldItem();
      int sheets = 64 - kit.getItemDamage();
      int size = getWindowInventory().getStackInSlot(1) == null ? 0 : getWindowInventory().getStackInSlot(1).stackSize;
      if (sheets != size) {
        kit.setItemDamage(64 - size);
      }
      ((EntityPlayerMP)getPlayer()).updateHeldItem();
    }
    if (isClient())
    {
      ItemStack item = getWindowInventory().getStackInSlot(0);
      this.prev = item;
      this.text.setValue("");
      if ((item != null) && (!Analyser.isAnalysed(item)))
      {
        if (getWindowInventory().getStackInSlot(1) == null)
        {
          this.text.setValue("No Paper!");
          this.isAnalysing = false;
          this.analyseProgress = 1.0F;
        }
        else
        {
          startAnalysing();
          this.chromo.setRoot(null);
          if (!damageKit()) {}
        }
      }
      else if (item != null)
      {
        this.isAnalysing = false;
        this.analyseProgress = 1.0F;
        refreshSpecies();
        if (!damageKit()) {}
      }
      else
      {
        this.isAnalysing = false;
        this.analyseProgress = 1.0F;
        
        this.chromo.setRoot(null);
      }
    }
  }
  
  private boolean damageKit()
  {
    return false;
  }
  
  private void startAnalysing()
  {
    this.glassVX = 0.0F;
    this.glassVY = 0.0F;
    this.glassOffsetX = 0.0F;
    this.glassOffsetY = 0.0F;
    this.isAnalysing = true;
    this.analyseProgress = 0.0F;
  }
  
  public boolean showHelpButton()
  {
    return true;
  }
  
  public String showInfoButton()
  {
    return "The Field Kit analyses bees, trees, flowers and butterflies. All that is required is a piece of paper to jot notes";
  }
  
  public void recieveGuiNBT(Side side, EntityPlayer player, String name, NBTTagCompound action)
  {
    super.recieveGuiNBT(side, player, name, action);
    if ((side == Side.SERVER) && (name.equals("analyse")))
    {
      getWindowInventory().setInventorySlotContents(0, Analyser.analyse(getWindowInventory().getStackInSlot(0)));
      getWindowInventory().decrStackSize(1, 1);
    }
  }
  
  static class StyleSheetPunnett
    extends StyleSheet
  {
    public StyleSheetPunnett()
    {
      this.textures.put(CraftGUITexture.Window, new PaddedTexture(0, 0, 160, 160, 0, ExtraBeeTexture.GUIPunnett, 32, 32, 32, 32));
      this.textures.put(CraftGUITexture.Slot, new StandardTexture(160, 0, 18, 18, 0, ExtraBeeTexture.GUIPunnett));
      this.textures.put(ExtraBeeGUITexture.Chromosome, new StandardTexture(160, 36, 16, 16, 0, ExtraBeeTexture.GUIPunnett));
      this.textures.put(ExtraBeeGUITexture.Chromosome2, new StandardTexture(160, 52, 16, 16, 0, ExtraBeeTexture.GUIPunnett));
      this.textures.put(CraftGUITexture.HelpButton, new StandardTexture(178, 0, 16, 16, 0, ExtraBeeTexture.GUIPunnett));
      this.textures.put(CraftGUITexture.InfoButton, new StandardTexture(178, 16, 16, 16, 0, ExtraBeeTexture.GUIPunnett));
    }
  }
}
