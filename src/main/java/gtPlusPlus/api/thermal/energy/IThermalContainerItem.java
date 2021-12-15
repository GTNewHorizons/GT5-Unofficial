package gtPlusPlus.api.thermal.energy;

import net.minecraft.item.ItemStack;

public interface IThermalContainerItem {
	
	int receiveThermalEnergy(ItemStack arg0, int arg1, boolean arg2);

	int extractThermalEnergy(ItemStack arg0, int arg1, boolean arg2);

	int getThermalEnergyStored(ItemStack arg0);

	int getMaxThermalEnergyStored(ItemStack arg0);
	
}