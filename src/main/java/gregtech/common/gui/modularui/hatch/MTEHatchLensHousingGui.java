package gregtech.common.gui.modularui.hatch;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;

public class MTEHatchLensHousingGui extends MTEHatchInputBusGui {

    public MTEHatchLensHousingGui(MTEHatchInputBus hatch) {
        super(hatch);
    }

    @Override
    protected int getDimension() {
        return 1;
    }

    @Override
    protected boolean supportsBottomLeftCornerFlow() {
        return false;
    }

    @Override
    protected boolean isValidStack(ItemStack itemStack) {
        return OrePrefixes.lens.contains(itemStack);
    }
}
