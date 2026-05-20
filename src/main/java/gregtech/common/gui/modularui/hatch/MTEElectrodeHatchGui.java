package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import kubatech.loaders.ArcFurnaceElectrode;

public class MTEElectrodeHatchGui extends MTEHatchInputBusGui {

    public MTEElectrodeHatchGui(MTEHatchInputBus hatch) {
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
        return Arrays.stream(ArcFurnaceElectrode.values())
            .anyMatch(
                electrode -> electrode.getElectrodeItem(1)
                    .isItemEqual(itemStack));
    }
}
