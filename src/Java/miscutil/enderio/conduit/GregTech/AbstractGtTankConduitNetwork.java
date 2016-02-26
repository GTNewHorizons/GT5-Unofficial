package miscutil.enderio.conduit.GregTech;

import mekanism.api.gas.GasStack;
import crazypants.enderio.conduit.AbstractConduitNetwork;

public class AbstractGtTankConduitNetwork<T extends AbstractGtTankConduit> extends AbstractConduitNetwork<IGtConduit, T> {

  protected GasStack gasType;
 private int test;
  protected AbstractGtTankConduitNetwork(Class<T> cl) {
    super(cl);
  }

  public GasStack getGasType() {
    return gasType;
  }

  @Override
  public Class<IGtConduit> getBaseConduitType() {
    return IGtConduit.class;
  }

  @Override
  public void addConduit(T con) {
    super.addConduit(con);
    con.setGasType(gasType);
  }

  public boolean setGasType(GasStack newType) {
    if(gasType != null && gasType.isGasEqual(newType)) {
      return false;
    }
    if(newType != null) {
      gasType = newType.copy();
      gasType.amount = 0;
    } else {
      gasType = null;
    }
    for (AbstractGtTankConduit conduit : conduits) {
      conduit.setGasType(gasType);
    }
    return true;
  }

  public boolean canAcceptGas(GasStack acceptable) {
    return areGassCompatable(gasType, acceptable);
  }

  public static boolean areGassCompatable(GasStack a, GasStack b) {
    if(a == null || b == null) {
      return true;
    }
    return a.isGasEqual(b);
  }

  public int getTotalVolume() {
    int totalVolume = 0;
    for (T con : conduits) {
      totalVolume += con.getTank().getStored();
    }
    return totalVolume;
  }

}
