package crazypants.enderio.conduit.gas;

import com.enderio.core.client.render.IconUtil;
import com.enderio.core.client.render.IconUtil.IIconProvider;
import com.enderio.core.common.util.BlockCoord;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.EnderIO;
import crazypants.enderio.conduit.AbstractConduitNetwork;
import crazypants.enderio.conduit.ConnectionMode;
import crazypants.enderio.conduit.IConduit;
import crazypants.enderio.conduit.IConduitBundle;
import crazypants.enderio.conduit.geom.CollidableComponent;
import crazypants.enderio.config.Config;
import java.util.HashMap;
import java.util.Map;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class GasConduit
  extends AbstractGasTankConduit
{
  public static final int CONDUIT_VOLUME = 1000;
  public static final String ICON_KEY = "enderio:gasConduit";
  public static final String ICON_CORE_KEY = "enderio:gasConduitCore";
  public static final String ICON_EXTRACT_KEY = "enderio:gasConduitInput";
  public static final String ICON_INSERT_KEY = "enderio:gasConduitOutput";
  public static final String ICON_EMPTY_EDGE = "enderio:gasConduitEdge";
  static final Map<String, IIcon> ICONS = new HashMap();
  private GasConduitNetwork network;
  
  @SideOnly(Side.CLIENT)
  public static void initIcons()
  {
    IconUtil.addIconProvider(new IconUtil.IIconProvider()
    {
      public void registerIcons(IIconRegister register)
      {
        GasConduit.ICONS.put("enderio:gasConduit", register.registerIcon("enderio:gasConduit"));
        GasConduit.ICONS.put("enderio:gasConduitCore", register.registerIcon("enderio:gasConduitCore"));
        GasConduit.ICONS.put("enderio:gasConduitInput", register.registerIcon("enderio:gasConduitInput"));
        GasConduit.ICONS.put("enderio:gasConduitOutput", register.registerIcon("enderio:gasConduitOutput"));
        GasConduit.ICONS.put("enderio:gasConduitEdge", register.registerIcon("enderio:gasConduitEdge"));
      }
      
      public int getTextureType()
      {
        return 0;
      }
    });
  }
  
  private long ticksSinceFailedExtract = 0L;
  public static final int MAX_EXTRACT_PER_TICK = Config.gasConduitExtractRate;
  public static final int MAX_IO_PER_TICK = Config.gasConduitMaxIoRate;
  
  public GasConduit()
  {
    updateTank();
  }
  
  public void updateEntity(World world)
  {
    super.updateEntity(world);
    if (world.isRemote) {
      return;
    }
    doExtract();
    if (this.stateDirty)
    {
      getBundle().dirty();
      this.stateDirty = false;
    }
  }
  
  private void doExtract()
  {
    BlockCoord loc = getLocation();
    if (!hasConnectionMode(ConnectionMode.INPUT)) {
      return;
    }
    if (this.network == null) {
      return;
    }
    this.ticksSinceFailedExtract += 1L;
    if ((this.ticksSinceFailedExtract > 25L) && (this.ticksSinceFailedExtract % 10L != 0L)) {
      return;
    }
    Gas f = this.tank.getGas() == null ? null : this.tank.getGas().getGas();
    for (ForgeDirection dir : this.externalConnections) {
      if ((autoExtractForDir(dir)) && 
        (this.network.extractFrom(this, dir, MAX_EXTRACT_PER_TICK))) {
        this.ticksSinceFailedExtract = 0L;
      }
    }
  }
  
  protected void updateTank()
  {
    this.tank.setCapacity(1000);
    if (this.network != null) {
      this.network.updateConduitVolumes();
    }
  }
  
  public ItemStack createItem()
  {
    return new ItemStack(EnderIO.itemGasConduit);
  }
  
  public AbstractConduitNetwork<?, ?> getNetwork()
  {
    return this.network;
  }
  
  public boolean setNetwork(AbstractConduitNetwork<?, ?> network)
  {
    if (network == null)
    {
      this.network = null;
      return true;
    }
    if (!(network instanceof GasConduitNetwork)) {
      return false;
    }
    GasConduitNetwork n = (GasConduitNetwork)network;
    if (this.tank.getGas() == null) {
      this.tank.setGas(n.getGasType() == null ? null : n.getGasType().copy());
    } else if (n.getGasType() == null) {
      n.setGasType(this.tank.getGas());
    } else if (!this.tank.getGas().isGasEqual(n.getGasType())) {
      return false;
    }
    this.network = n;
    return true;
  }
  
  public boolean canConnectToConduit(ForgeDirection direction, IConduit con)
  {
    if (!super.canConnectToConduit(direction, con)) {
      return false;
    }
    if (!(con instanceof GasConduit)) {
      return false;
    }
    if ((getGasType() != null) && (((GasConduit)con).getGasType() == null)) {
      return false;
    }
    return GasConduitNetwork.areGassCompatable(getGasType(), ((GasConduit)con).getGasType());
  }
  
  public void setConnectionMode(ForgeDirection dir, ConnectionMode mode)
  {
    super.setConnectionMode(dir, mode);
    refreshInputs(dir);
  }
  
  private void refreshInputs(ForgeDirection dir)
  {
    if (this.network == null) {
      return;
    }
    GasOutput lo = new GasOutput(getLocation().getLocation(dir), dir.getOpposite());
    this.network.removeInput(lo);
    if ((getConnectionMode(dir).acceptsOutput()) && (containsExternalConnection(dir))) {
      this.network.addInput(lo);
    }
  }
  
  public void externalConnectionAdded(ForgeDirection fromDirection)
  {
    super.externalConnectionAdded(fromDirection);
    refreshInputs(fromDirection);
  }
  
  public void externalConnectionRemoved(ForgeDirection fromDirection)
  {
    super.externalConnectionRemoved(fromDirection);
    refreshInputs(fromDirection);
  }
  
  public IIcon getTextureForState(CollidableComponent component)
  {
    if (component.dir == ForgeDirection.UNKNOWN) {
      return (IIcon)ICONS.get("enderio:gasConduitCore");
    }
    return (IIcon)ICONS.get("enderio:gasConduit");
  }
  
  public IIcon getTextureForInputMode()
  {
    return (IIcon)ICONS.get("enderio:gasConduitInput");
  }
  
  public IIcon getTextureForOutputMode()
  {
    return (IIcon)ICONS.get("enderio:gasConduitOutput");
  }
  
  public IIcon getNotSetEdgeTexture()
  {
    return (IIcon)ICONS.get("enderio:gasConduitEdge");
  }
  
  public IIcon getTransmitionTextureForState(CollidableComponent component)
  {
    if ((isActive()) && (this.tank.containsValidGas())) {
      return this.tank.getGas().getGas().getIcon();
    }
    return null;
  }
  
  @Deprecated
  @Optional.Method(modid="MekanismAPI|gas")
  public int receiveGas(ForgeDirection side, GasStack stack)
  {
    return receiveGas(side, stack, true);
  }
  
  @Optional.Method(modid="MekanismAPI|gas")
  public int receiveGas(ForgeDirection side, GasStack stack, boolean doTransfer)
  {
    if ((this.network == null) || (!getConnectionMode(side).acceptsInput())) {
      return 0;
    }
    return this.network.fill(side, stack, doTransfer);
  }
  
  @Deprecated
  @Optional.Method(modid="MekanismAPI|gas")
  public GasStack drawGas(ForgeDirection side, int amount)
  {
    return drawGas(side, amount, true);
  }
  
  @Optional.Method(modid="MekanismAPI|gas")
  public GasStack drawGas(ForgeDirection side, int amount, boolean doTransfer)
  {
    if ((this.network == null) || (!getConnectionMode(side).acceptsOutput())) {
      return null;
    }
    return this.network.drain(side, amount, doTransfer);
  }
  
  @Optional.Method(modid="MekanismAPI|gas")
  public boolean canReceiveGas(ForgeDirection from, Gas gas)
  {
    if (this.network == null) {
      return false;
    }
    return (getConnectionMode(from).acceptsInput()) && (GasConduitNetwork.areGassCompatable(getGasType(), new GasStack(gas, 0)));
  }
  
  @Optional.Method(modid="MekanismAPI|gas")
  public boolean canDrawGas(ForgeDirection from, Gas gas)
  {
    if (this.network == null) {
      return false;
    }
    return (getConnectionMode(from).acceptsOutput()) && (GasConduitNetwork.areGassCompatable(getGasType(), new GasStack(gas, 0)));
  }
  
  protected boolean canJoinNeighbour(IGasConduit n)
  {
    return n instanceof GasConduit;
  }
  
  public AbstractGasTankConduitNetwork<? extends AbstractGasTankConduit> getTankNetwork()
  {
    return this.network;
  }
}
