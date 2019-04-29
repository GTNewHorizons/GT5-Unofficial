package gtPlusPlus.api.thermal.sample;

import gtPlusPlus.api.thermal.energy.IThermalContainerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemThermalContainer extends Item implements IThermalContainerItem {
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public ItemThermalContainer() {
	}

	public ItemThermalContainer(int arg0) {
		this(arg0, arg0, arg0);
	}

	public ItemThermalContainer(int arg0, int arg1) {
		this(arg0, arg1, arg1);
	}

	public ItemThermalContainer(int arg0, int arg1, int arg2) {
		this.capacity = arg0;
		this.maxReceive = arg1;
		this.maxExtract = arg2;
	}

	public ItemThermalContainer setCapacity(int arg0) {
		this.capacity = arg0;
		return this;
	}

	public void setMaxTransfer(int arg0) {
		this.setMaxReceive(arg0);
		this.setMaxExtract(arg0);
	}

	public void setMaxReceive(int arg0) {
		this.maxReceive = arg0;
	}

	public void setMaxExtract(int arg0) {
		this.maxExtract = arg0;
	}

	public int receiveThermalEnergy(ItemStack arg0, int arg1, boolean arg2) {
		if (arg0.getTagCompound() == null) {
			arg0.stackTagCompound = new NBTTagCompound();
		}
		int arg3 = arg0.stackTagCompound.getInteger("Energy");
		int arg4 = Math.min(this.capacity - arg3, Math.min(this.maxReceive, arg1));
		if (!arg2) {
			arg3 += arg4;
			arg0.stackTagCompound.setInteger("Energy", arg3);
		}
		return arg4;
	}

	public int extractThermalEnergy(ItemStack arg0, int arg1, boolean arg2) {
		if (arg0.stackTagCompound != null && arg0.stackTagCompound.hasKey("Energy")) {
			int arg3 = arg0.stackTagCompound.getInteger("Energy");
			int arg4 = Math.min(arg3, Math.min(this.maxExtract, arg1));
			if (!arg2) {
				arg3 -= arg4;
				arg0.stackTagCompound.setInteger("Energy", arg3);
			}
			return arg4;
		} else {
			return 0;
		}
	}

	public int getEnergyStored(ItemStack arg0) {
		return arg0.stackTagCompound != null && arg0.stackTagCompound.hasKey("Energy")
				? arg0.stackTagCompound.getInteger("Energy")
				: 0;
	}

	public int getMaxThermalEnergyStored(ItemStack arg0) {
		return this.capacity;
	}
}