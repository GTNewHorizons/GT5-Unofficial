package gregtech.api.multitileentity.interfaces;
import gregtech.api.util.GT_StructureUtilityMuTE.UpgradeCasings;

public interface UpgradableModularMuTE extends UpgradableMuTE{
    void increaseMucCount(UpgradeCasings casingType, int tier);

    void resetMucCount();
}
