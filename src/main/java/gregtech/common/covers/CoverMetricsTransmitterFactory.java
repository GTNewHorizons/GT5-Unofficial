package gregtech.common.covers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.covers.CoverFactoryBase;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;

public class CoverMetricsTransmitterFactory extends CoverFactoryBase<CoverMetricsTransmitter.MetricsTransmitterData> {

    @Override
    public CoverMetricsTransmitter.MetricsTransmitterData createDataObject() {
        return new CoverMetricsTransmitter.MetricsTransmitterData();
    }

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable aTileEntity) {
        return aTileEntity instanceof final IGregTechDeviceInformation device && device.isGivingInformation();
    }

}
