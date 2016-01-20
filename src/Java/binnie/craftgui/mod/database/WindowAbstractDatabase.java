package binnie.craftgui.mod.database;

import binnie.core.BinnieCore;
import binnie.core.genetics.BreedingSystem;
import binnie.core.proxy.BinnieProxy;
import binnie.core.util.IValidator;
import binnie.craftgui.controls.ControlTextEdit;
import binnie.craftgui.controls.listbox.ControlListBox;
import binnie.craftgui.controls.listbox.ControlTextOption;
import binnie.craftgui.controls.page.ControlPage;
import binnie.craftgui.controls.page.ControlPages;
import binnie.craftgui.controls.tab.ControlTab;
import binnie.craftgui.controls.tab.ControlTabBar;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.CraftGUIUtil;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.events.EventHandler.Origin;
import binnie.craftgui.events.EventTextEdit;
import binnie.craftgui.events.EventTextEdit.Handler;
import binnie.craftgui.events.EventValueChanged;
import binnie.craftgui.events.EventValueChanged.Handler;
import binnie.craftgui.minecraft.MinecraftGUI.PanelType;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlHelp;
import binnie.craftgui.window.Panel;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IClassification;
import forestry.api.genetics.ISpeciesRoot;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;

public abstract class WindowAbstractDatabase
  extends Window
{
  private float selectionBoxWidth = 95.0F;
  private final float infoBoxWidth = 144.0F;
  private final float infoBoxHeight = 176.0F;
  private final float infoTabWidth = 16.0F;
  private final float modeTabWidth = 22.0F;
  private final float searchBoxHeight = 16.0F;
  
  public void changeMode(IDatabaseMode mode)
  {
    this.modePages.setValue(mode);
  }
  
  public WindowAbstractDatabase(EntityPlayer player, Side side, boolean nei, BreedingSystem system, float wid)
  {
    super(100.0F, 192.0F, player, null, side);
    this.isNEI = nei;
    this.system = system;
    this.selectionBoxWidth = wid;
  }
  
  public static enum Mode
    implements IDatabaseMode
  {
    Species,  Branches,  Breeder;
    
    private Mode() {}
    
    public String getName()
    {
      return BinnieCore.proxy.localise("gui.database.mode." + name().toLowerCase());
    }
  }
  
  public static abstract class ModeWidgets
  {
    public WindowAbstractDatabase database;
    public ControlPage<IDatabaseMode> modePage;
    private ControlPages<DatabaseTab> infoPages;
    public ControlListBox listBox;
    private ControlTabBar<DatabaseTab> infoTabs;
    
    public ModeWidgets(IDatabaseMode mode, WindowAbstractDatabase database)
    {
      this.database = database;
      this.modePage = new ControlPage(database.modePages, 0.0F, 0.0F, database.getSize().x(), database.getSize().y(), mode);
      
      IArea listBoxArea = database.panelSearch.area().inset(2);
      
      createListBox(listBoxArea);
      
      CraftGUIUtil.alignToWidget(this.listBox, database.panelSearch);
      CraftGUIUtil.moveWidget(this.listBox, new IPoint(2.0F, 2.0F));
      
      this.infoPages = new ControlPages(this.modePage, 0.0F, 0.0F, 144.0F, 176.0F);
      
      CraftGUIUtil.alignToWidget(this.infoPages, database.panelInformation);
    }
    
    public abstract void createListBox(IArea paramIArea);
  }
  
  public ControlPages<DatabaseTab> getInfoPages(IDatabaseMode mode)
  {
    return ((ModeWidgets)this.modes.get(mode)).infoPages;
  }
  
  private Map<IDatabaseMode, ModeWidgets> modes = new HashMap();
  boolean isNEI;
  private BreedingSystem system;
  
  public boolean isNEI()
  {
    return this.isNEI;
  }
  
  public BreedingSystem getBreedingSystem()
  {
    return this.system;
  }
  
  public WindowAbstractDatabase(EntityPlayer player, Side side, boolean nei, BreedingSystem system)
  {
    this(player, side, nei, system, 95.0F);
  }
  
  private Panel panelInformation = null;
  private Panel panelSearch = null;
  private ControlPages<IDatabaseMode> modePages = null;
  
  protected ModeWidgets createMode(IDatabaseMode mode, ModeWidgets widgets)
  {
    this.modes.put(mode, widgets);
    
    return widgets;
  }
  
  public void initialiseClient()
  {
    setSize(new IPoint(176.0F + this.selectionBoxWidth + 22.0F + 8.0F, 208.0F));
    
    addEventHandler(new EventValueChanged.Handler()
    {
      public void onEvent(EventValueChanged event)
      {
        if (((event.getOrigin().getParent() instanceof ControlPage)) && (!(event.getValue() instanceof DatabaseTab)))
        {
          ControlPage parent = (ControlPage)event.getOrigin().getParent();
          if ((parent.getValue() instanceof IDatabaseMode)) {
            for (IWidget widget : parent.getWidgets()) {
              if ((widget instanceof ControlPages)) {
                if (event.getValue() == null)
                {
                  widget.hide();
                }
                else
                {
                  widget.show();
                  for (IWidget widget2 : widget.getWidgets()) {
                    if ((widget2 instanceof PageAbstract)) {
                      ((PageAbstract)widget2).onValueChanged(event.getValue());
                    }
                  }
                }
              }
            }
          }
        }
      }
    });
    addEventHandler(new EventTextEdit.Handler()
    {
      public void onEvent(final EventTextEdit event)
      {
        for (WindowAbstractDatabase.ModeWidgets widgets : WindowAbstractDatabase.this.modes.values()) {
          widgets.listBox.setValidator(new IValidator()
          {
            public boolean isValid(IWidget object)
            {
              return (event.getValue() == "") || (((ControlTextOption)object).getText().toLowerCase().contains(((String)event.getValue()).toLowerCase()));
            }
          });
        }
      }
    }.setOrigin(EventHandler.Origin.DirectChild, this));
    











    new ControlHelp(this, 4.0F, 4.0F);
    
    this.panelInformation = new Panel(this, 24.0F, 24.0F, 144.0F, 176.0F, MinecraftGUI.PanelType.Black);
    this.panelInformation.setColour(860416);
    
    this.panelSearch = new Panel(this, 176.0F, 24.0F, this.selectionBoxWidth, 160.0F, MinecraftGUI.PanelType.Black);
    
    this.panelSearch.setColour(860416);
    
    this.modePages = new ControlPages(this, 0.0F, 0.0F, getSize().x(), getSize().y());
    
    new ControlTextEdit(this, 176.0F, 184.0F, this.selectionBoxWidth, 16.0F);
    

    createMode(Mode.Species, new ModeWidgets(Mode.Species, this)
    {
      public void createListBox(IArea area)
      {
        GameProfile playerName = WindowAbstractDatabase.this.getUsername();
        
        Collection<IAlleleSpecies> speciesList = !this.database.isNEI ? this.database.system.getDiscoveredSpecies(this.database.getWorld(), playerName) : this.database.system.getAllSpecies();
        

        this.listBox = new ControlSpeciesBox(this.modePage, area.x(), area.y(), area.w(), area.h());
        this.listBox.setOptions(speciesList);
      }
    });
    createMode(Mode.Branches, new ModeWidgets(Mode.Branches, this)
    {
      public void createListBox(IArea area)
      {
        EntityPlayer player = this.database.getPlayer();
        GameProfile playerName = WindowAbstractDatabase.this.getUsername();
        
        Collection<IClassification> speciesList = !this.database.isNEI ? this.database.system.getDiscoveredBranches(this.database.getWorld(), playerName) : this.database.system.getAllBranches();
        

        this.listBox = new ControlBranchBox(this.modePage, area.x(), area.y(), area.w(), area.h());
        this.listBox.setOptions(speciesList);
      }
    });
    createMode(Mode.Breeder, new ModeWidgets(Mode.Breeder, this)
    {
      public void createListBox(IArea area)
      {
        this.listBox = new ControlListBox(this.modePage, area.x(), area.y(), area.w(), area.h(), 12.0F);
      }
    });
    addTabs();
    
    ControlTabBar<IDatabaseMode> tab = new ControlTabBar(this, 176.0F + this.selectionBoxWidth, 24.0F, 22.0F, 176.0F, Position.Right)
    {
      public ControlTab<IDatabaseMode> createTab(float x, float y, float w, float h, IDatabaseMode value)
      {
        new ControlTab(this, x, y, w, h, value)
        {
          public String getName()
          {
            return ((IDatabaseMode)this.value).getName();
          }
        };
      }
    };
    tab.setValues(this.modePages.getValues());
    
    CraftGUIUtil.linkWidgets(tab, this.modePages);
    

    changeMode(Mode.Species);
    for (IDatabaseMode mode : this.modes.keySet())
    {
      ((ModeWidgets)this.modes.get(mode)).infoTabs = new ControlTabBar(((ModeWidgets)this.modes.get(mode)).modePage, 8.0F, 24.0F, 16.0F, 176.0F, Position.Left);
      

      ((ModeWidgets)this.modes.get(mode)).infoTabs.setValues(((ModeWidgets)this.modes.get(mode)).infoPages.getValues());
      
      CraftGUIUtil.linkWidgets(((ModeWidgets)this.modes.get(mode)).infoTabs, ((ModeWidgets)this.modes.get(mode)).infoPages);
    }
  }
  
  public void initialiseServer()
  {
    IBreedingTracker tracker = this.system.getSpeciesRoot().getBreedingTracker(getWorld(), getUsername());
    if (tracker != null) {
      tracker.synchToPlayer(getPlayer());
    }
  }
  
  protected void addTabs() {}
  
  public void gotoSpecies(IAlleleSpecies value)
  {
    if (value != null)
    {
      this.modePages.setValue(Mode.Species);
      changeMode(Mode.Species);
      ((ModeWidgets)this.modes.get(this.modePages.getValue())).listBox.setValue(value);
    }
  }
  
  public void gotoSpeciesDelayed(IAlleleSpecies species)
  {
    this.gotoSpecies = species;
  }
  
  private IAlleleSpecies gotoSpecies = null;
  
  public void onUpdateClient()
  {
    super.onUpdateClient();
    if (this.gotoSpecies != null)
    {
      ((WindowAbstractDatabase)getSuperParent()).gotoSpecies(this.gotoSpecies);
      this.gotoSpecies = null;
    }
  }
}
