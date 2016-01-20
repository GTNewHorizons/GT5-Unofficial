package binnie.core.machines;

import binnie.core.BinnieCore;
import binnie.core.machines.component.IInteraction.RightClick;
import binnie.core.machines.component.IRender.DisplayTick;
import binnie.core.machines.network.INetwork.GuiNBT;
import binnie.core.machines.network.INetwork.RecieveGuiNBT;
import binnie.core.machines.network.INetwork.SendGuiNBT;
import binnie.core.machines.network.INetwork.TilePacketSync;
import binnie.core.machines.power.ITankMachine;
import binnie.core.network.BinnieCorePacketID;
import binnie.core.network.INetworkedEntity;
import binnie.core.network.packet.MessageTileNBT;
import binnie.core.network.packet.PacketPayload;
import binnie.core.proxy.BinnieProxy;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import forestry.api.core.INBTTagable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Machine
  implements INetworkedEntity, INBTTagable, INetwork.TilePacketSync, IMachine, INetwork.GuiNBT
{
  private MachinePackage machinePackage;
  private Map<Class, List<MachineComponent>> componentInterfaceMap = new LinkedHashMap();
  private Map<Class<? extends MachineComponent>, MachineComponent> componentMap = new LinkedHashMap();
  private TileEntity tile;
  
  public Machine(MachinePackage pack, TileEntity tile)
  {
    this.tile = tile;
    
    pack.createMachine(this);
    this.machinePackage = pack;
  }
  
  public void addComponent(MachineComponent component)
  {
    if (component == null) {
      throw new NullPointerException("Can't have a null machine component!");
    }
    component.setMachine(this);
    this.componentMap.put(component.getClass(), component);
    for (Class inter : component.getComponentInterfaces())
    {
      if (!this.componentInterfaceMap.containsKey(inter)) {
        this.componentInterfaceMap.put(inter, new ArrayList());
      }
      ((List)this.componentInterfaceMap.get(inter)).add(component);
    }
  }
  
  public Collection<MachineComponent> getComponents()
  {
    return this.componentMap.values();
  }
  
  public <T extends MachineComponent> T getComponent(Class<T> componentClass)
  {
    return hasComponent(componentClass) ? (MachineComponent)componentClass.cast(this.componentMap.get(componentClass)) : null;
  }
  
  public <T> T getInterface(Class<T> interfaceClass)
  {
    if (hasInterface(interfaceClass)) {
      return getInterfaces(interfaceClass).get(0);
    }
    if (interfaceClass.isInstance(getPackage())) {
      return interfaceClass.cast(getPackage());
    }
    for (MachineComponent component : getComponents()) {
      if (interfaceClass.isInstance(component)) {
        return interfaceClass.cast(component);
      }
    }
    return null;
  }
  
  public <T> List<T> getInterfaces(Class<T> interfaceClass)
  {
    ArrayList<T> interfaces = new ArrayList();
    if (!hasInterface(interfaceClass)) {
      return interfaces;
    }
    for (MachineComponent component : (List)this.componentInterfaceMap.get(interfaceClass)) {
      interfaces.add(interfaceClass.cast(component));
    }
    return interfaces;
  }
  
  public boolean hasInterface(Class<?> interfaceClass)
  {
    return this.componentInterfaceMap.containsKey(interfaceClass);
  }
  
  public boolean hasComponent(Class<? extends MachineComponent> componentClass)
  {
    return this.componentMap.containsKey(componentClass);
  }
  
  public TileEntity getTileEntity()
  {
    return this.tile;
  }
  
  public void sendPacket()
  {
    if (!BinnieCore.proxy.isSimulating(getTileEntity().getWorldObj())) {
      return;
    }
    BinnieCore.proxy.sendNetworkEntityPacket((INetworkedEntity)getTileEntity());
  }
  
  public Side getSide()
  {
    return BinnieCore.proxy.isSimulating(getTileEntity().getWorldObj()) ? Side.SERVER : Side.CLIENT;
  }
  
  public void writeToPacket(PacketPayload payload)
  {
    for (MachineComponent component : getComponents()) {
      if ((component instanceof INetworkedEntity)) {
        ((INetworkedEntity)component).writeToPacket(payload);
      }
    }
  }
  
  public void readFromPacket(PacketPayload payload)
  {
    for (MachineComponent component : getComponents()) {
      if ((component instanceof INetworkedEntity)) {
        ((INetworkedEntity)component).readFromPacket(payload);
      }
    }
  }
  
  public void onRightClick(World world, EntityPlayer player, int x, int y, int z)
  {
    for (IInteraction.RightClick component : getInterfaces(IInteraction.RightClick.class)) {
      component.onRightClick(world, player, x, y, z);
    }
  }
  
  public void markDirty()
  {
    this.queuedInventoryUpdate = true;
  }
  
  private boolean queuedInventoryUpdate = false;
  
  public void onUpdate()
  {
    if (BinnieCore.proxy.isSimulating(getWorld())) {
      for (MachineComponent component : getComponents()) {
        component.onUpdate();
      }
    } else {
      for (IRender.DisplayTick renders : getInterfaces(IRender.DisplayTick.class)) {
        renders.onDisplayTick(getWorld(), getTileEntity().xCoord, getTileEntity().yCoord, getTileEntity().zCoord, getWorld().rand);
      }
    }
    if (this.queuedInventoryUpdate)
    {
      for (MachineComponent component : getComponents()) {
        component.onInventoryUpdate();
      }
      this.queuedInventoryUpdate = false;
    }
  }
  
  public IInventory getInventory()
  {
    return (IInventory)getInterface(IInventory.class);
  }
  
  public ITankMachine getTankContainer()
  {
    return (ITankMachine)getInterface(ITankMachine.class);
  }
  
  public void readFromNBT(NBTTagCompound nbttagcompound)
  {
    for (MachineComponent component : getComponents()) {
      component.readFromNBT(nbttagcompound);
    }
    this.owner = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("owner"));
    markDirty();
  }
  
  public void writeToNBT(NBTTagCompound nbttagcompound)
  {
    for (MachineComponent component : getComponents()) {
      component.writeToNBT(nbttagcompound);
    }
    if (this.owner != null)
    {
      NBTTagCompound nbt = new NBTTagCompound();
      NBTUtil.func_152460_a(nbt, this.owner);
      nbttagcompound.setTag("owner", nbt);
    }
  }
  
  public MachinePackage getPackage()
  {
    return this.machinePackage;
  }
  
  public static IMachine getMachine(Object inventory)
  {
    if ((inventory != null) && ((inventory instanceof IMachine))) {
      return (IMachine)inventory;
    }
    if ((inventory != null) && ((inventory instanceof TileEntityMachine))) {
      return ((TileEntityMachine)inventory).getMachine();
    }
    if ((inventory != null) && ((inventory instanceof MachineComponent))) {
      return ((MachineComponent)inventory).getMachine();
    }
    return null;
  }
  
  public static <T> T getInterface(Class<T> interfac, Object inventory)
  {
    IMachine machine = getMachine(inventory);
    if (machine != null) {
      return machine.getInterface(interfac);
    }
    if (interfac.isInstance(inventory)) {
      return interfac.cast(inventory);
    }
    return null;
  }
  
  public MachineUtil getMachineUtil()
  {
    return new MachineUtil(this);
  }
  
  public World getWorld()
  {
    return getTileEntity().getWorldObj();
  }
  
  public void onBlockDestroy()
  {
    for (MachineComponent component : getComponents()) {
      component.onDestruction();
    }
  }
  
  private int nextProgressBarID = 0;
  
  public int getUniqueProgressBarID()
  {
    return this.nextProgressBarID++;
  }
  
  private GameProfile owner = null;
  
  public GameProfile getOwner()
  {
    return this.owner;
  }
  
  public void setOwner(GameProfile owner)
  {
    this.owner = owner;
  }
  
  public Packet getDescriptionPacket()
  {
    NBTTagCompound nbt = new NBTTagCompound();
    
    syncToNBT(nbt);
    if (nbt.hasNoTags()) {
      return null;
    }
    return BinnieCore.instance.getNetworkWrapper().getPacketFrom(new MessageTileNBT(BinnieCorePacketID.TileDescriptionSync.ordinal(), getTileEntity(), nbt).GetMessage());
  }
  
  public void syncToNBT(NBTTagCompound nbt)
  {
    for (INetwork.TilePacketSync comp : getInterfaces(INetwork.TilePacketSync.class)) {
      comp.syncToNBT(nbt);
    }
  }
  
  public void syncFromNBT(NBTTagCompound nbt)
  {
    for (INetwork.TilePacketSync comp : getInterfaces(INetwork.TilePacketSync.class)) {
      comp.syncFromNBT(nbt);
    }
  }
  
  public void recieveGuiNBT(Side side, EntityPlayer player, String name, NBTTagCompound nbt)
  {
    for (INetwork.RecieveGuiNBT recieve : getInterfaces(INetwork.RecieveGuiNBT.class)) {
      recieve.recieveGuiNBT(side, player, name, nbt);
    }
  }
  
  public void sendGuiNBT(Map<String, NBTTagCompound> nbt)
  {
    for (INetwork.SendGuiNBT recieve : getInterfaces(INetwork.SendGuiNBT.class)) {
      recieve.sendGuiNBT(nbt);
    }
  }
}
