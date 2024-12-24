package gregtech.common.tileentities.machines.multiblock.coke_oven;

import com.cleanroommc.modularui.utils.item.IItemStackLong;

import gregtech.api.multitileentity.data.ProcessingData;

public class CokeOvenData extends ProcessingData {

    protected IItemStackLong outputItem;

    public IItemStackLong getOutputItem() {
        return outputItem;
    }

    public void setOutputItem(IItemStackLong outputItem) {
        this.outputItem = outputItem;
    }
}
