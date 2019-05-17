package gtPlusPlus.api.thermal.energy;

import net.minecraft.nbt.NBTTagCompound;

public class ThermalStorage implements IThermalStorage {
	
	protected int thermal_energy;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public ThermalStorage(int arg0) {
		this(arg0, arg0, arg0);
	}

	public ThermalStorage(int arg0, int arg1) {
		this(arg0, arg1, arg1);
	}

	public ThermalStorage(int arg0, int arg1, int arg2) {
		this.capacity = arg0;
		this.maxReceive = arg1;
		this.maxExtract = arg2;
	}

	public ThermalStorage readFromNBT(NBTTagCompound arg0) {
		this.thermal_energy = arg0.getInteger("ThermalEnergy");
		if (this.thermal_energy > this.capacity) {
			this.thermal_energy = this.capacity;
		}
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound arg0) {
		if (this.thermal_energy < 0) {
			this.thermal_energy = 0;
		}
		arg0.setInteger("ThermalEnergy", this.thermal_energy);
		return arg0;
	}

	public void setCapacity(int arg0) {
		this.capacity = arg0;
		if (this.thermal_energy > arg0) {
			this.thermal_energy = arg0;
		}

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

	public int getMaxReceive() {
		return this.maxReceive;
	}

	public int getMaxExtract() {
		return this.maxExtract;
	}

	public void setEnergyStored(int arg0) {
		this.thermal_energy = arg0;
		if (this.thermal_energy > this.capacity) {
			this.thermal_energy = this.capacity;
		} else if (this.thermal_energy < 0) {
			this.thermal_energy = 0;
		}

	}

	public void modifyEnergyStored(int arg0) {
		this.thermal_energy += arg0;
		if (this.thermal_energy > this.capacity) {
			this.thermal_energy = this.capacity;
		} else if (this.thermal_energy < 0) {
			this.thermal_energy = 0;
		}

	}

	public int receiveThermalEnergy(int arg0, boolean arg1) {
		int arg2 = Math.min(this.capacity - this.thermal_energy, Math.min(this.maxReceive, arg0));
		if (!arg1) {
			this.thermal_energy += arg2;
		}

		return arg2;
	}

	public int extractThermalEnergy(int arg0, boolean arg1) {
		int arg2 = Math.min(this.thermal_energy, Math.min(this.maxExtract, arg0));
		if (!arg1) {
			this.thermal_energy -= arg2;
		}

		return arg2;
	}

	public int getThermalEnergyStored() {
		return this.thermal_energy;
	}

	public int getMaxThermalEnergyStored() {
		return this.capacity;
	}
}