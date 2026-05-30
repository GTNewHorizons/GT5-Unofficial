package kubatech.api.arcfurnace;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface ArcFurnaceContext {

    int getDurabilityConsumptionThisRun();

    void setDurabilityConsumptionThisRun(int durability);

    long getLastWorkingTick();

    long getTotalRunTime();

    NBTTagCompound getEffectState();

    void resetEffectState();

    boolean depleteInputAndUpdate(ItemStack stack);

    int getRandomNumber(int range);

}
