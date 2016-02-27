package crazypants.enderio.conduit.gas;

import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import crazypants.enderio.conduit.ConduitNetworkTickHandler;
import crazypants.enderio.conduit.ConduitNetworkTickHandler.TickListener;
import crazypants.enderio.conduit.ConnectionMode;
import crazypants.enderio.conduit.IConduit;
import crazypants.enderio.conduit.IConduitBundle;
import crazypants.util.BlockCoord;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class GasConduitNetwork
  extends AbstractGasTankConduitNetwork<GasConduit>
{
  private final ConduitGasTank tank = new ConduitGasTank(0);
  private final Set<GasOutput> outputs = new HashSet();
  private Iterator<GasOutput> outputIterator;
  private int ticksActiveUnsynced;
  private boolean lastSyncedActive = false;
  private int lastSyncedVolume = -1;
  private long timeAtLastApply;
  private final InnerTickHandler tickHandler = new InnerTickHandler(null);
  
  public GasConduitNetwork()
  {
    super(GasConduit.class);
  }
  
  public Class<IGasConduit> getBaseConduitType()
  {
    return IGasConduit.class;
  }
  
  public void addConduit(GasConduit con)
  {
    this.tank.setCapacity(this.tank.getMaxGas() + 1000);
    if (con.getTank().containsValidGas()) {
      this.tank.addAmount(con.getTank().getStored());
    }
    for (ForgeDirection dir : con.getExternalConnections()) {
      if (con.getConnectionMode(dir).acceptsOutput()) {
        this.outputs.add(new GasOutput(con.getLocation().getLocation(dir), dir.getOpposite()));
      }
    }
    this.outputIterator = null;
    super.addConduit(con);
  }
  
  public boolean setGasType(GasStack newType)
  {
    if (super.setGasType(newType))
    {
      GasStack ft = getGasType();
      this.tank.setGas(ft == null ? null : ft.copy());
      return true;
    }
    return false;
  }
  
  public void destroyNetwork()
  {
    setConduitVolumes();
    this.outputs.clear();
    super.destroyNetwork();
  }
  
  private void setConduitVolumes()
  {
    GasStack gasPerConduit;
    int leftOvers;
    if ((this.tank.containsValidGas()) && (!this.conduits.isEmpty()))
    {
      gasPerConduit = this.tank.getGas().copy();
      int numCons = this.conduits.size();
      leftOvers = gasPerConduit.amount % numCons;
      gasPerConduit.amount /= numCons;
      for (GasConduit con : this.conduits)
      {
        GasStack f = gasPerConduit.copy();
        if (leftOvers > 0)
        {
          f.amount += 1;
          leftOvers--;
        }
        con.getTank().setGas(f);
        BlockCoord bc = con.getLocation();
        con.getBundle().getEntity().getWorldObj().markTileEntityChunkModified(bc.x, bc.y, bc.z, con.getBundle().getEntity());
      }
    }
  }
  
  public void onUpdateEntity(IConduit conduit)
  {
    World world = conduit.getBundle().getEntity().getWorldObj();
    if (world == null) {
      return;
    }
    if (world.isRemote) {
      return;
    }
    long curTime = world.getTotalWorldTime();
    if ((curTime > 0L) && (curTime != this.timeAtLastApply))
    {
      this.timeAtLastApply = curTime;
      ConduitNetworkTickHandler.instance.addListener(this.tickHandler);
    }
  }
  
  private void doTick()
  {
    if ((this.gasType == null) || (this.outputs.isEmpty()) || (!this.tank.containsValidGas()) || (this.tank.isEmpty()))
    {
      updateActiveState();
      return;
    }
    if ((this.outputIterator == null) || (!this.outputIterator.hasNext())) {
      this.outputIterator = this.outputs.iterator();
    }
    updateActiveState();
    
    int numVisited = 0;
    while ((!this.tank.isEmpty()) && (numVisited < this.outputs.size()))
    {
      if (!this.outputIterator.hasNext()) {
        this.outputIterator = this.outputs.iterator();
      }
      GasOutput output = (GasOutput)this.outputIterator.next();
      if (output != null)
      {
        IGasHandler cont = getTankContainer(output.location);
        if (cont != null)
        {
          GasStack offer = this.tank.getGas().copy();
          int filled = cont.receiveGas(output.dir, offer);
          if (filled > 0) {
            this.tank.addAmount(-filled);
          }
        }
      }
      numVisited++;
    }
  }
  
  private void updateActiveState()
  {
    boolean isActive = (this.tank.containsValidGas()) && (!this.tank.isEmpty());
    if (this.lastSyncedActive != isActive) {
      this.ticksActiveUnsynced += 1;
    } else {
      this.ticksActiveUnsynced = 0;
    }
    if ((this.ticksActiveUnsynced >= 10) || ((this.ticksActiveUnsynced > 0) && (isActive)))
    {
      if (!isActive) {
        setGasType(null);
      }
      for (IConduit con : this.conduits) {
        con.setActive(isActive);
      }
      this.lastSyncedActive = isActive;
      this.ticksActiveUnsynced = 0;
    }
  }
  
  public int fill(ForgeDirection from, GasStack resource, boolean doFill)
  {
    if (resource == null) {
      return 0;
    }
    resource.amount = Math.min(resource.amount, GasConduit.MAX_IO_PER_TICK);
    boolean gasWasValid = this.tank.containsValidGas();
    int res = this.tank.receive(resource, doFill);
    if ((doFill) && (res > 0) && (gasWasValid))
    {
      int vol = this.tank.getStored();
      setGasType(resource);
      this.tank.setAmount(vol);
    }
    return res;
  }
  
  public GasStack drain(ForgeDirection from, GasStack resource, boolean doDrain)
  {
    if ((resource == null) || (this.tank.isEmpty()) || (!this.tank.containsValidGas()) || (!areGassCompatable(getGasType(), resource))) {
      return null;
    }
    int amount = Math.min(resource.amount, this.tank.getStored());
    amount = Math.min(amount, GasConduit.MAX_IO_PER_TICK);
    GasStack result = resource.copy();
    result.amount = amount;
    if (doDrain) {
      this.tank.addAmount(-amount);
    }
    return result;
  }
  
  public GasStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    if ((this.tank.isEmpty()) || (!this.tank.containsValidGas())) {
      return null;
    }
    int amount = Math.min(maxDrain, this.tank.getStored());
    GasStack result = this.tank.getGas().copy();
    result.amount = amount;
    if (doDrain) {
      this.tank.addAmount(-amount);
    }
    return result;
  }
  
  public boolean extractFrom(GasConduit advancedGasConduit, ForgeDirection dir, int maxExtractPerTick)
  {
    if (this.tank.isFull()) {
      return false;
    }
    IGasHandler extTank = getTankContainer(advancedGasConduit, dir);
    if (extTank != null)
    {
      int maxExtract = Math.min(maxExtractPerTick, this.tank.getAvailableSpace());
      if ((this.gasType == null) || (!this.tank.containsValidGas()))
      {
        GasStack drained = extTank.drawGas(dir.getOpposite(), maxExtract);
        if ((drained == null) || (drained.amount <= 0)) {
          return false;
        }
        setGasType(drained);
        this.tank.setGas(drained.copy());
        return true;
      }
      GasStack couldDrain = this.gasType.copy();
      couldDrain.amount = maxExtract;
      







      GasStack drained = extTank.drawGas(dir.getOpposite(), maxExtract);
      if ((drained == null) || (drained.amount == 0)) {
        return false;
      }
      if (drained.isGasEqual(getGasType())) {
        this.tank.addAmount(drained.amount);
      }
      return true;
    }
    return false;
  }
  
  public IGasHandler getTankContainer(BlockCoord bc)
  {
    World w = getWorld();
    if (w == null) {
      return null;
    }
    TileEntity te = w.getTileEntity(bc.x, bc.y, bc.z);
    if ((te instanceof IGasHandler)) {
      return (IGasHandler)te;
    }
    return null;
  }
  
  public IGasHandler getTankContainer(GasConduit con, ForgeDirection dir)
  {
    BlockCoord bc = con.getLocation().getLocation(dir);
    return getTankContainer(bc);
  }
  
  World getWorld()
  {
    if (this.conduits.isEmpty()) {
      return null;
    }
    return ((GasConduit)this.conduits.get(0)).getBundle().getWorld();
  }
  
  public void removeInput(GasOutput lo)
  {
    this.outputs.remove(lo);
    this.outputIterator = null;
  }
  
  public void addInput(GasOutput lo)
  {
    this.outputs.add(lo);
    this.outputIterator = null;
  }
  
  public void updateConduitVolumes()
  {
    if (this.tank.getStored() == this.lastSyncedVolume) {
      return;
    }
    setConduitVolumes();
    this.lastSyncedVolume = this.tank.getStored();
  }
  
  private class InnerTickHandler
    implements ConduitNetworkTickHandler.TickListener
  {
    private InnerTickHandler() {}
    
    public void tickStart(TickEvent.ServerTickEvent evt) {}
    
    public void tickEnd(TickEvent.ServerTickEvent evt)
    {
      GasConduitNetwork.this.doTick();
    }
  }
}
