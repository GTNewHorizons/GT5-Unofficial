package miscutil.enderio.conduit.GregTech;

import java.util.List;

import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import crazypants.enderio.conduit.AbstractConduitNetwork;
import crazypants.enderio.conduit.ConduitUtil;
import crazypants.enderio.conduit.ConnectionMode;
import crazypants.enderio.conduit.RaytraceResult;
import crazypants.enderio.tool.ToolUtil;
import crazypants.util.BlockCoord;

public abstract class AbstractGtTankConduit extends AbstractGtConduit {

  protected ConduitGtTank tank = new ConduitGtTank(0);
  protected boolean stateDirty = false;
  protected long lastEmptyTick = 0;
  protected int numEmptyEvents = 0;

  @Override
  public boolean onBlockActivated(EntityPlayer player, RaytraceResult res, List<RaytraceResult> all) {
    if(player.getCurrentEquippedItem() == null) {
      return false;
    }
    if(ToolUtil.isToolEquipped(player)) {

      if(!getBundle().getEntity().getWorldObj().isRemote) {

        if(res != null && res.component != null) {

          ForgeDirection connDir = res.component.dir;
          ForgeDirection faceHit = ForgeDirection.getOrientation(res.movingObjectPosition.sideHit);

          if(connDir == ForgeDirection.UNKNOWN || connDir == faceHit) {

            if(getConnectionMode(faceHit) == ConnectionMode.DISABLED) {
              setConnectionMode(faceHit, getNextConnectionMode(faceHit));
              return true;
            }

            BlockCoord loc = getLocation().getLocation(faceHit);
            IGtConduit n = ConduitUtil.getConduit(getBundle().getEntity().getWorldObj(), loc.x, loc.y, loc.z, IGtConduit.class);
            if(n == null) {
              return false;
            }
            if(!canJoinNeighbour(n)) {
              return false;
            }
            if(!(n instanceof AbstractGtTankConduit)) {
              return false;
            }
            AbstractGtTankConduit neighbour = (AbstractGtTankConduit) n;
            if(neighbour.getGasType() == null || getGasType() == null) {
              GasStack type = getGasType();
              type = type != null ? type : neighbour.getGasType();
              neighbour.setGasTypeOnNetwork(neighbour, type);
              setGasTypeOnNetwork(this, type);
            }
            return ConduitUtil.joinConduits(this, faceHit);
          } else if(containsExternalConnection(connDir)) {
            // Toggle extraction mode
            setConnectionMode(connDir, getNextConnectionMode(connDir));
          } else if(containsConduitConnection(connDir)) {
            GasStack curGasType = null;
            if(getTankNetwork() != null) {
              curGasType = getTankNetwork().getGasType();
            }
            ConduitUtil.disconectConduits(this, connDir);
            setGasType(curGasType);

          }
        }
      }
      return true;

    }

    return false;
  }

  private void setGasTypeOnNetwork(AbstractGtTankConduit con, GasStack type) {
    AbstractConduitNetwork<?, ?> n = con.getNetwork();
    if(n != null) {
      AbstractGtTankConduitNetwork<?> network = (AbstractGtTankConduitNetwork<?>) n;
      network.setGasType(type);
    }

  }

  protected abstract boolean canJoinNeighbour(IGtConduit n);

  public abstract AbstractGtTankConduitNetwork<? extends AbstractGtTankConduit> getTankNetwork();

  public void setGasType(GasStack gasType) {
    if(tank.getGas() != null && tank.getGas().isGasEqual(gasType)) {
      return;
    }
    if(gasType != null) {
      gasType = gasType.copy();
    } else if(tank.getGas() == null) {
      return;
    }
    tank.setGas(gasType);
    stateDirty = true;
  }

  public ConduitGtTank getTank() {
    return tank;
  }

  public GasStack getGasType() {
    GasStack result = null;
    if(getTankNetwork() != null) {
      result = getTankNetwork().getGasType();
    }
    if(result == null) {
      result = tank.getGas();
    }
    return result;
  }
  
  @Override
  public boolean canOutputToDir(ForgeDirection dir) {
    if (super.canOutputToDir(dir)) {
      IGasHandler ext = getExternalHandler(dir);
      return ext != null && ext.canReceiveGas(dir.getOpposite(), tank.getGasType());
    }
    return false;
  }

  protected abstract void updateTank();

  @Override
  public void readFromNBT(NBTTagCompound nbtRoot, short nbtVersion) {
    super.readFromNBT(nbtRoot, nbtVersion);
    updateTank();
    if(nbtRoot.hasKey("tank")) {
      GasStack gas = GasStack.readFromNBT(nbtRoot.getCompoundTag("tank"));
      tank.setGas(gas);
    } else {
      tank.setGas(null);
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound nbtRoot) {
    super.writeToNBT(nbtRoot);
    GasStack gt = getGasType();
    if(GtUtil.isGasValid(gt)) {
      updateTank();
      gt = gt.copy();
      gt.amount = tank.getStored();
      nbtRoot.setTag("tank", gt.write(new NBTTagCompound()));
    }
  }

}
