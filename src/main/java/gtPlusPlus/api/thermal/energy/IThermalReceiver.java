package gtPlusPlus.api.thermal.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IThermalReceiver extends IThermalConnection {
	
	int receiveThermalEnergy(ForgeDirection arg0, int arg1, boolean arg2);

	int getThermalEnergyStored(ForgeDirection arg0);

	int getMaxThermalEnergyStored(ForgeDirection arg0);
	
}