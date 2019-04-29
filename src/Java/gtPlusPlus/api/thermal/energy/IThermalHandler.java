package gtPlusPlus.api.thermal.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IThermalHandler extends IThermalProvider, IThermalReceiver {
	
	int receiveThermalEnergy(ForgeDirection arg0, int arg1, boolean arg2);

	int extractThermalEnergy(ForgeDirection arg0, int arg1, boolean arg2);

	int getThermalEnergyStored(ForgeDirection arg0);

	int getMaxThermalEnergyStored(ForgeDirection arg0);
	
}