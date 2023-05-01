package gtPlusPlus.api.thermal.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IThermalHandler extends IThermalProvider, IThermalReceiver {

    @Override
    int receiveThermalEnergy(ForgeDirection arg0, int arg1, boolean arg2);

    @Override
    int extractThermalEnergy(ForgeDirection arg0, int arg1, boolean arg2);

    @Override
    int getThermalEnergyStored(ForgeDirection arg0);

    @Override
    int getMaxThermalEnergyStored(ForgeDirection arg0);
}
