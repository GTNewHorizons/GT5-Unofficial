package kubatech.api.arcfurnace;

public interface ArcFurnaceContext {

    int getDurabilityConsumptionThisRun();

    void setDurabilityConsumptionThisRun(int durability);

    long getLastWorkingTick();

    long getTotalRunTime();

}
