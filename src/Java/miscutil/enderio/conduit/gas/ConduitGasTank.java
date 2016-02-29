package miscutil.enderio.conduit.gas;

import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTank;
import net.minecraft.nbt.NBTTagCompound;

public class ConduitGasTank
  extends GasTank
{
  private int capacity;
  
  ConduitGasTank(int capacity)
  {
    super(capacity);
    this.capacity = capacity;
  }
  
  public float getFilledRatio()
  {
    if (getStored() <= 0) {
      return 0.0F;
    }
    if (getMaxGas() <= 0) {
      return -1.0F;
    }
    float res = getStored() / getMaxGas();
    return res;
  }
  
  public boolean isFull()
  {
    return getStored() >= getMaxGas();
  }
  
  public void setAmount(int amount)
  {
    if (this.stored != null) {
      this.stored.amount = amount;
    }
  }
  
  public int getAvailableSpace()
  {
    return getMaxGas() - getStored();
  }
  
  public void addAmount(int amount)
  {
    setAmount(getStored() + amount);
  }
  
  public int getMaxGas()
  {
    return this.capacity;
  }
  
  public void setCapacity(int capacity)
  {
    this.capacity = capacity;
    if (getStored() > capacity) {
      setAmount(capacity);
    }
  }
  
  public int receive(GasStack resource, boolean doReceive)
  {
    if ((resource == null) || (resource.getGas().getID() < 0)) {
      return 0;
    }
    if ((this.stored == null) || (this.stored.getGas().getID() < 0))
    {
      if (resource.amount <= this.capacity)
      {
        if (doReceive) {
          setGas(resource.copy());
        }
        return resource.amount;
      }
      if (doReceive)
      {
        this.stored = resource.copy();
        this.stored.amount = this.capacity;
      }
      return this.capacity;
    }
    if (!this.stored.isGasEqual(resource)) {
      return 0;
    }
    int space = this.capacity - this.stored.amount;
    if (resource.amount <= space)
    {
      if (doReceive) {
        addAmount(resource.amount);
      }
      return resource.amount;
    }
    if (doReceive) {
      this.stored.amount = this.capacity;
    }
    return space;
  }
  
  public GasStack draw(int maxDrain, boolean doDraw)
  {
    if ((this.stored == null) || (this.stored.getGas().getID() < 0)) {
      return null;
    }
    if (this.stored.amount <= 0) {
      return null;
    }
    int used = maxDrain;
    if (this.stored.amount < used) {
      used = this.stored.amount;
    }
    if (doDraw) {
      addAmount(-used);
    }
    GasStack drained = new GasStack(this.stored.getGas().getID(), used);
    if (this.stored.amount < 0) {
      this.stored.amount = 0;
    }
    return drained;
  }
  
  public String getGasName()
  {
    return this.stored != null ? this.stored.getGas().getLocalizedName() : null;
  }
  
  public boolean containsValidGas()
  {
    return GasUtil.isGasValid(this.stored);
  }
  
  public NBTTagCompound write(NBTTagCompound nbt)
  {
    if (containsValidGas()) {
      this.stored.write(nbt);
    } else {
      nbt.setBoolean("emptyGasTank", true);
    }
    return nbt;
  }
  
  public void read(NBTTagCompound nbt)
  {
    if (!nbt.hasKey("emptyGasTank"))
    {
      GasStack gas = GasStack.readFromNBT(nbt);
      if (gas != null) {
        setGas(gas);
      }
    }
  }
  
  public boolean isEmpty()
  {
    return (this.stored == null) || (this.stored.amount == 0);
  }
}
