package gregtech.api.multitileentity.interfaces;

public interface UpgradableModularMuTE extends UpgradableMuTE{
    void increaseMucCount(String casingType, int tier);

    void resetMucCount();
}
