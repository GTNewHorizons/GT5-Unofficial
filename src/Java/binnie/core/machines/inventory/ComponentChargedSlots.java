package binnie.core.machines.inventory;

import binnie.core.machines.Machine;
import binnie.core.machines.MachineComponent;
import binnie.core.machines.MachineUtil;
import binnie.core.machines.network.INetwork.GuiNBT;
import cpw.mods.fml.relauncher.Side;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ComponentChargedSlots
  extends MachineComponent
  implements INetwork.GuiNBT, IChargedSlots
{
  public ComponentChargedSlots(Machine machine)
  {
    super(machine);
  }
  
  public void readFromNBT(NBTTagCompound nbttagcompound)
  {
    super.readFromNBT(nbttagcompound);
    this.charges.clear();
    NBTTagList list = nbttagcompound.getTagList("charges", 10);
    for (int i = 0; i < list.tagCount(); i++)
    {
      NBTTagCompound tag = list.getCompoundTagAt(i);
      this.charges.put(Integer.valueOf(tag.getByte("i")), Float.valueOf(tag.getByte("v") / 100.0F));
    }
  }
  
  public void writeToNBT(NBTTagCompound nbttagcompound)
  {
    super.writeToNBT(nbttagcompound);
    
    NBTTagList chargeList = new NBTTagList();
    for (Map.Entry<Integer, Float> entry : this.charges.entrySet())
    {
      NBTTagCompound chargesNBT = new NBTTagCompound();
      chargesNBT.setByte("i", (byte)(0 + ((Integer)entry.getKey()).intValue()));
      chargesNBT.setByte("v", (byte)(int)(((Float)entry.getValue()).floatValue() * 100.0F));
      chargeList.appendTag(chargesNBT);
    }
    nbttagcompound.setTag("charges", chargeList);
  }
  
  private Map<Integer, Float> charges = new HashMap();
  
  public void addCharge(int slot)
  {
    this.charges.put(Integer.valueOf(slot), Float.valueOf(0.0F));
  }
  
  public void recieveGuiNBT(Side side, EntityPlayer player, String name, NBTTagCompound nbt)
  {
    Iterator i$;
    if (name.equals("slot-charges")) {
      for (i$ = this.charges.keySet().iterator(); i$.hasNext();)
      {
        int i = ((Integer)i$.next()).intValue();
        this.charges.put(Integer.valueOf(i), Float.valueOf(nbt.getShort("" + i) / 100.0F));
      }
    }
  }
  
  public void sendGuiNBT(Map<String, NBTTagCompound> nbt)
  {
    NBTTagCompound tag = new NBTTagCompound();
    for (Iterator i$ = this.charges.keySet().iterator(); i$.hasNext();)
    {
      int i = ((Integer)i$.next()).intValue();
      tag.setShort("" + i, (short)(int)(((Float)this.charges.get(Integer.valueOf(i))).floatValue() * 100.0F));
    }
    nbt.put("slot-charges", tag);
  }
  
  public float getCharge(int slot)
  {
    return this.charges.containsKey(Integer.valueOf(slot)) ? ((Float)this.charges.get(Integer.valueOf(slot))).floatValue() : 0.0F;
  }
  
  public void setCharge(int slot, float charge)
  {
    if (charge > 1.0F) {
      charge = 1.0F;
    }
    if (charge < 0.0F) {
      charge = 0.0F;
    }
    if (this.charges.containsKey(Integer.valueOf(slot))) {
      this.charges.put(Integer.valueOf(slot), Float.valueOf(charge));
    }
  }
  
  public void onUpdate()
  {
    for (Iterator i$ = this.charges.keySet().iterator(); i$.hasNext();)
    {
      int slot = ((Integer)i$.next()).intValue();
      if ((getCharge(slot) <= 0.0F) && 
        (getUtil().decreaseStack(slot, 1) != null)) {
        setCharge(slot, 1.0F);
      }
    }
  }
  
  public void alterCharge(int slot, float charge)
  {
    setCharge(slot, getCharge(slot) + charge);
  }
}
