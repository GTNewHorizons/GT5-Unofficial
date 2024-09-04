package gregtech.common.tileentities.casings.upgrade;

import java.util.List;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GTValues;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;
import gregtech.api.util.GTUtility;

public class Tank extends UpgradeCasing {

    private int tankCount;
    private int tankCapacity;
    public UUID tankID;
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    public static final int BOTH = 2;
    private String tankName = "tank";
    private int type = BOTH;

    @Override
    protected void customWork(IMultiBlockController aTarget) {

    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.tank";
    }

    public String getCustomTankName() {
        return tankName;
    }

    public String getTankID() {
        return tankID.toString();
    }

    public int getType() {
        return type;
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        super.readMultiTileNBT(aNBT);
        tankCount = aNBT.getInteger(GTValues.NBT.UPGRADE_TANK_COUNT);
        tankCapacity = aNBT.getInteger(GTValues.NBT.UPGRADE_TANK_CAPACITY);
    }

    @Override
    public void addToolTips(List<String> list, ItemStack stack, boolean f3_h) {
        super.addToolTips(list, stack, f3_h);
        list.add("Adds another tank inventory");
        list.add("Number of tanks: " + tankCount);
        list.add("Tank capacity: " + GTUtility.formatNumbers(tankCapacity) + " L");
    }
}
