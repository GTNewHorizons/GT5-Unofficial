package gtPlusPlus.api.thermal.sample;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gtPlusPlus.api.thermal.energy.IThermalHandler;
import gtPlusPlus.api.thermal.energy.ThermalStorage;

public class TileThermalHandler extends TileEntity implements IThermalHandler {

    protected ThermalStorage storage = new ThermalStorage(32000);

    @Override
    public void readFromNBT(NBTTagCompound arg0) {
        super.readFromNBT(arg0);
        this.storage.readFromNBT(arg0);
    }

    @Override
    public void writeToNBT(NBTTagCompound arg0) {
        super.writeToNBT(arg0);
        this.storage.writeToNBT(arg0);
    }

    @Override
    public boolean canConnectThermalEnergy(ForgeDirection arg0) {
        return true;
    }

    @Override
    public int receiveThermalEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
        return this.storage.receiveThermalEnergy(arg1, arg2);
    }

    @Override
    public int extractThermalEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
        return this.storage.extractThermalEnergy(arg1, arg2);
    }

    @Override
    public int getThermalEnergyStored(ForgeDirection arg0) {
        return this.storage.getThermalEnergyStored();
    }

    @Override
    public int getMaxThermalEnergyStored(ForgeDirection arg0) {
        return this.storage.getMaxThermalEnergyStored();
    }
}
