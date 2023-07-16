package gregtech.api.multitileentity.interfaces;

public interface UpgradableMuTE {

    void setCleanroom(boolean isCleanroom);

    void setWirelessSupport(boolean canUse);

    void setLaserSupport(boolean canUse);

    void setMaxAmperage(long amperage);

    void increaseMucCount(String casingType, int tier);

    void resetMucCount();
}
