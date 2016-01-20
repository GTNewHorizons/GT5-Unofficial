package binnie.craftgui.minecraft;

import binnie.Binnie;
import binnie.core.AbstractMod;
import binnie.core.BinnieCore;
import binnie.core.machines.Machine;
import binnie.core.machines.inventory.IInventoryMachine;
import binnie.core.machines.network.INetwork.RecieveGuiNBT;
import binnie.core.machines.power.PowerSystem;
import binnie.core.network.packet.MessageCraftGUI;
import binnie.core.proxy.BinnieProxy;
import binnie.core.resource.BinnieResource;
import binnie.core.resource.ManagerResource;
import binnie.core.resource.ResourceType;
import binnie.craftgui.controls.ControlText;
import binnie.craftgui.controls.ControlTextCentered;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.ITooltipHelp;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.TopLevelWidget;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventWidget.ChangeSize;
import binnie.craftgui.events.EventWidget.ChangeSize.Handler;
import binnie.craftgui.minecraft.control.ControlHelp;
import binnie.craftgui.minecraft.control.ControlInfo;
import binnie.craftgui.minecraft.control.ControlPowerSystem;
import binnie.craftgui.minecraft.control.ControlSlot;
import binnie.craftgui.minecraft.control.ControlUser;
import binnie.craftgui.minecraft.control.EnumHighlighting;
import binnie.craftgui.resource.StyleSheetManager;
import binnie.craftgui.resource.Texture;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import binnie.craftgui.resource.minecraft.StandardTexture;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class Window
  extends TopLevelWidget
  implements INetwork.RecieveGuiNBT
{
  private GuiCraftGUI gui;
  private ContainerCraftGUI container;
  private WindowInventory windowInventory;
  private ControlText title;
  
  public void getTooltip(Tooltip tooltip)
  {
    Deque<IWidget> queue = calculateMousedOverWidgets();
    while (!queue.isEmpty())
    {
      IWidget widget = (IWidget)queue.removeFirst();
      if ((widget.isEnabled()) && (widget.isVisible()) && (widget.calculateIsMouseOver()))
      {
        if ((widget instanceof ITooltip))
        {
          ((ITooltip)widget).getTooltip(tooltip);
          if (tooltip.exists()) {
            return;
          }
        }
        if (widget.hasAttribute(Attribute.BlockTooltip)) {
          return;
        }
      }
    }
  }
  
  public void getHelpTooltip(MinecraftTooltip tooltip)
  {
    Deque<IWidget> queue = calculateMousedOverWidgets();
    while (!queue.isEmpty())
    {
      IWidget widget = (IWidget)queue.removeFirst();
      if ((widget.isEnabled()) && (widget.isVisible()) && (widget.calculateIsMouseOver()))
      {
        if ((widget instanceof ITooltipHelp))
        {
          ((ITooltipHelp)widget).getHelpTooltip(tooltip);
          if (tooltip.exists()) {
            return;
          }
        }
        if (widget.hasAttribute(Attribute.BlockTooltip)) {
          return;
        }
      }
    }
  }
  
  protected abstract AbstractMod getMod();
  
  protected abstract String getName();
  
  public BinnieResource getBackgroundTextureFile(int i)
  {
    return Binnie.Resource.getPNG(getMod(), ResourceType.GUI, getName() + (i == 1 ? "" : Integer.valueOf(i)));
  }
  
  public boolean showHelpButton()
  {
    return Machine.getInterface(IInventoryMachine.class, getInventory()) != null;
  }
  
  public String showInfoButton()
  {
    if (Machine.getInterface(IMachineInformation.class, getInventory()) != null) {
      return ((IMachineInformation)Machine.getInterface(IMachineInformation.class, getInventory())).getInformation();
    }
    return null;
  }
  
  public Window(float width, float height, EntityPlayer player, IInventory inventory, Side side)
  {
    this.side = side;
    setInventories(player, inventory);
    this.container = new ContainerCraftGUI(this);
    this.windowInventory = new WindowInventory(this);
    if (side == Side.SERVER) {
      return;
    }
    setSize(new IPoint(width, height));
    this.gui = new GuiCraftGUI(this);
    for (EnumHighlighting h : EnumHighlighting.values()) {
      ControlSlot.highlighting.put(h, new ArrayList());
    }
    CraftGUI.Render = new Renderer(this.gui);
    CraftGUI.Render.stylesheet(StyleSheetManager.getDefault());
    
    this.titleButtonLeft = -14.0F;
    if (showHelpButton()) {
      new ControlHelp(this, this.titleButtonLeft += 22.0F, 8.0F);
    }
    if (showInfoButton() != null) {
      new ControlInfo(this, this.titleButtonLeft += 22.0F, 8.0F, showInfoButton());
    }
    Window wind = this;
    
    addSelfEventHandler(new EventWidget.ChangeSize.Handler()
    {
      public void onEvent(EventWidget.ChangeSize event)
      {
        if ((Window.this.isClient()) && (Window.this.getGui() != null))
        {
          Window.this.getGui().resize(Window.this.getSize());
          if (Window.this.title != null) {
            Window.this.title.setSize(new IPoint(Window.this.w(), Window.this.title.h()));
          }
        }
      }
    });
  }
  
  protected float titleButtonLeft = 8.0F;
  protected float titleButtonRight = 8.0F;
  
  public void setTitle(String title)
  {
    this.title = new ControlTextCentered(this, 12.0F, title);
    this.title.setColour(4210752);
  }
  
  @SideOnly(Side.CLIENT)
  public final GuiCraftGUI getGui()
  {
    return this.gui;
  }
  
  public final ContainerCraftGUI getContainer()
  {
    return this.container;
  }
  
  public final WindowInventory getWindowInventory()
  {
    return this.windowInventory;
  }
  
  private StandardTexture bgText1 = null;
  private StandardTexture bgText2 = null;
  private boolean hasBeenInitialised = false;
  private EntityPlayer player;
  private IInventory entityInventory;
  
  public final void initGui()
  {
    if (this.hasBeenInitialised) {
      return;
    }
    this.bgText1 = new StandardTexture(0, 0, 256, 256, getBackgroundTextureFile(1));
    if (getSize().x() > 256.0F) {
      this.bgText2 = new StandardTexture(0, 0, 256, 256, getBackgroundTextureFile(2));
    }
    if (!BinnieCore.proxy.checkTexture(this.bgText1.getTexture()))
    {
      this.bgText1 = null;
      this.bgText2 = null;
    }
    initialiseClient();
    this.hasBeenInitialised = true;
  }
  
  public abstract void initialiseClient();
  
  public void initialiseServer() {}
  
  public void onRenderBackground()
  {
    CraftGUI.Render.colour(16777215);
    if (getBackground1() != null) {
      CraftGUI.Render.texture(getBackground1(), IPoint.ZERO);
    }
    if (getBackground2() != null) {
      CraftGUI.Render.texture(getBackground2(), new IPoint(256.0F, 0.0F));
    }
    CraftGUI.Render.colour(getColour());
    CraftGUI.Render.texture(CraftGUITexture.Window, getArea());
  }
  
  public void onUpdateClient()
  {
    ((List)ControlSlot.highlighting.get(EnumHighlighting.Help)).clear();
    
    ControlSlot.shiftClickActive = false;
  }
  
  public EntityPlayer getPlayer()
  {
    return this.player;
  }
  
  public GameProfile getUsername()
  {
    return getPlayer().getGameProfile();
  }
  
  public ItemStack getHeldItemStack()
  {
    if (this.player != null) {
      return this.player.inventory.getItemStack();
    }
    return null;
  }
  
  public IInventory getInventory()
  {
    return this.entityInventory;
  }
  
  public void setInventories(EntityPlayer player2, IInventory inventory)
  {
    this.player = player2;
    this.entityInventory = inventory;
  }
  
  public void onClose() {}
  
  public void setHeldItemStack(ItemStack stack)
  {
    if (this.player != null) {
      this.player.inventory.setItemStack(stack);
    }
  }
  
  private Side side = Side.CLIENT;
  
  public boolean isServer()
  {
    return !isClient();
  }
  
  public boolean isClient()
  {
    return this.side == Side.CLIENT;
  }
  
  public World getWorld()
  {
    if (getPlayer() != null) {
      return getPlayer().worldObj;
    }
    return BinnieCore.proxy.getWorld();
  }
  
  public void onInventoryUpdate() {}
  
  public void sendClientAction(String name, NBTTagCompound action)
  {
    action.setString("type", name);
    MessageCraftGUI packet = new MessageCraftGUI(action);
    BinnieCore.proxy.sendToServer(packet);
  }
  
  public void recieveGuiNBT(Side side, EntityPlayer player, String name, NBTTagCompound action)
  {
    if ((side == Side.CLIENT) && (name.equals("username")))
    {
      new ControlUser(this, w() - (this.titleButtonRight += 16.0F), 8.0F, action.getString("username"));
      this.titleButtonRight += 6.0F;
    }
    if ((side == Side.CLIENT) && (name.equals("power-system")))
    {
      new ControlPowerSystem(this, w() - (this.titleButtonRight += 16.0F), 8.0F, PowerSystem.get(action.getByte("system")));
      this.titleButtonRight += 6.0F;
    }
  }
  
  public void onWindowInventoryChanged() {}
  
  public Texture getBackground1()
  {
    return this.bgText1;
  }
  
  public Texture getBackground2()
  {
    return this.bgText2;
  }
  
  public static <T extends Window> T get(IWidget widget)
  {
    return (Window)widget.getSuperParent();
  }
}
