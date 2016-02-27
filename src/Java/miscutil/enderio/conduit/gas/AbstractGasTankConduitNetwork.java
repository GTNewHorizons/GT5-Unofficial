package crazypants.enderio.conduit.gas;

import crazypants.enderio.conduit.AbstractConduitNetwork;
import mekanism.api.gas.GasStack;

public class AbstractGasTankConduitNetwork<T extends AbstractGasTankConduit>
  extends AbstractConduitNetwork<IGasConduit, T>
{
  protected GasStack gasType;
  
  protected AbstractGasTankConduitNetwork(Class<T> cl)
  {
    super(cl);
  }
  
  public GasStack getGasType()
  {
    return this.gasType;
  }
  
  public Class<IGasConduit> getBaseConduitType()
  {
    return IGasConduit.class;
  }
  
  public void addConduit(T con)
  {
    super.addConduit(con);
    con.setGasType(this.gasType);
  }
  
  public boolean setGasType(GasStack newType)
  {
    if ((this.gasType != null) && (this.gasType.isGasEqual(newType))) {
      return false;
    }
    if (newType != null)
    {
      this.gasType = newType.copy();
      this.gasType.amount = 0;
    }
    else
    {
      this.gasType = null;
    }
    for (AbstractGasTankConduit conduit : this.conduits) {
      conduit.setGasType(this.gasType);
    }
    return true;
  }
  
  public boolean canAcceptGas(GasStack acceptable)
  {
    return areGassCompatable(this.gasType, acceptable);
  }
  
  public static boolean areGassCompatable(GasStack a, GasStack b)
  {
    if ((a == null) || (b == null)) {
      return true;
    }
    return a.isGasEqual(b);
  }
  
  public int getTotalVolume()
  {
    int totalVolume = 0;
    for (T con : this.conduits) {
      totalVolume += con.getTank().getStored();
    }
    return totalVolume;
  }
}
