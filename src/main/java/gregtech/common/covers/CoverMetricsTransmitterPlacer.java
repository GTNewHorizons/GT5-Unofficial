package gregtech.common.covers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.covers.CoverPlacerBase;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;

public class CoverMetricsTransmitterPlacer extends CoverPlacerBase {

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack coverItem, ICoverable coverable) {
        return coverable instanceof final IGregTechDeviceInformation device && device.isGivingInformation();
    }

}
