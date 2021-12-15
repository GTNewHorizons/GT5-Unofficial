package gtPlusPlus.api.thermal.sample;

import gtPlusPlus.api.thermal.energy.IThermalHandler;
import gtPlusPlus.api.thermal.energy.ThermalStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileThermalHandler extends TileEntity implements IThermalHandler {
	
	protected ThermalStorage storage = new ThermalStorage(32000);

	public void readFromNBT(NBTTagCompound arg0) {
		super.readFromNBT(arg0);
		this.storage.readFromNBT(arg0);
	}

	public void writeToNBT(NBTTagCompound arg0) {
		super.writeToNBT(arg0);
		this.storage.writeToNBT(arg0);
	}

	public boolean canConnectThermalEnergy(ForgeDirection arg0) {
		return true;
	}

	public int receiveThermalEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		return this.storage.receiveThermalEnergy(arg1, arg2);
	}

	public int extractThermalEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		return this.storage.extractThermalEnergy(arg1, arg2);
	}

	public int getThermalEnergyStored(ForgeDirection arg0) {
		return this.storage.getThermalEnergyStored();
	}

	public int getMaxThermalEnergyStored(ForgeDirection arg0) {
		return this.storage.getMaxThermalEnergyStored();
	}
}