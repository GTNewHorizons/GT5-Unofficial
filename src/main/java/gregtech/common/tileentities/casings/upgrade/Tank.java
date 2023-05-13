package gregtech.common.tileentities.casings.upgrade;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GT_Values;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;
import gregtech.api.util.GT_Utility;

public class Tank extends UpgradeCasing {

    private int tankCount;
    private int tankCapacity;

    @Override
    protected void customWork(IMultiBlockController aTarget) {

    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.tank";
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        super.readMultiTileNBT(aNBT);
        tankCount = aNBT.getInteger(GT_Values.NBT.UPGRADE_TANK_COUNT);
        tankCapacity = aNBT.getInteger(GT_Values.NBT.UPGRADE_TANK_CAPACITY);
    }

    @Override
    public void addToolTips(List<String> list, ItemStack stack, boolean f3_h) {
        super.addToolTips(list, stack, f3_h);
        list.add("Adds another tank inventory");
        list.add("Number of tanks: " + tankCount);
        list.add("Tank capacity: " + GT_Utility.formatNumbers(tankCapacity) + " L");
    }
}
