package gregtech.common.tileentities.casings.upgrade;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GT_Values;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;
import gregtech.api.util.GT_Utility;

public class Ampere extends UpgradeCasing {

    private long amperage;

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.functional.amperage";
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        super.readMultiTileNBT(aNBT);
        amperage = aNBT.getInteger(GT_Values.NBT.UPGRADE_AMPERAGE);
    }

    @Override
    protected void customWork(IMultiBlockController target) {
        target.setMaxAmperage(amperage);
    }

    @Override
    public boolean breakBlock() {
        final IMultiBlockController controller = getTarget(false);
        if (controller != null) {
            controller.setMaxAmperage(2);
        }
        return super.breakBlock();
    }

    @Override
    public void addToolTips(List<String> list, ItemStack stack, boolean f3_h) {
        super.addToolTips(list, stack, f3_h);
        list.add("Increases allowed amperage to " + GT_Utility.formatNumbers(amperage));
    }
}
