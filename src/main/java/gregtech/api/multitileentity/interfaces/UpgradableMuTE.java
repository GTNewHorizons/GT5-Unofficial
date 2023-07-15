package gregtech.api.multitileentity.interfaces;

public interface UpgradableMuTE {

    void setCleanroom(boolean isCleanroom);

    void setWirelessSupport(boolean canUse);

    void setLaserSupport(boolean canUse);

    void setMaxAmperage(long amperage);

    void increaseHeaterCount();

    void decreaseHeaterCount();
}
