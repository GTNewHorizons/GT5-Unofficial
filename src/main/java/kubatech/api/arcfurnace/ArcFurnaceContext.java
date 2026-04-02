package kubatech.api.arcfurnace;

import net.minecraft.nbt.NBTTagCompound;

public interface ArcFurnaceContext {

    int getDurabilityConsumptionThisRun();

    void setDurabilityConsumptionThisRun(int durability);

    long getLastWorkingTick();

    long getTotalRunTime();

    NBTTagCompound getEffectState();

    void resetEffectState();

}
