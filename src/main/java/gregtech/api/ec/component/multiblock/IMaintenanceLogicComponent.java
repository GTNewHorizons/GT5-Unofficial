package gregtech.api.ec.component.multiblock;

import static gregtech.api.util.GTUtility.validMTEList;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IUniqueComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;

public interface IMaintenanceLogicComponent extends IHatchContainerComponent, IUniqueComponent {

    ComponentKey.UniqueKey<IMaintenanceLogicComponent> KEY = ComponentKey.unique();

    boolean tryAttachMaintenance(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);

    @NotNull
    List<MTEHatchMaintenance> getMaintenanceHatches();

    @NotNull
    default Iterable<MTEHatchMaintenance> getValidMaintenanceHatches() {
        return validMTEList(getMaintenanceHatches());
    }

    boolean supportIssue(int mask);

    boolean hasIssue(int mask);

    void setIssue(int mask, boolean hasIssue);

    int getMaintenanceErrorBits();

    int getMaintenanceErrorCount();

    int getRepairStatus();

    int getIdealStatus();

    void fixAllIssues();

    void causeAllIssues();

    void causeRandomIssue();

    void checkMaintenance();

    int getMaintenanceEfficiencyPenalty();
}
