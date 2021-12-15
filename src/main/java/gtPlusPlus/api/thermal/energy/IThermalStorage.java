package gtPlusPlus.api.thermal.energy;

public interface IThermalStorage {
	
	int receiveThermalEnergy(int arg0, boolean arg1);

	int extractThermalEnergy(int arg0, boolean arg1);

	int getThermalEnergyStored();

	int getMaxThermalEnergyStored();
	
}