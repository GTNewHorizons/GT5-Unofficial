package gregtech.common.ec.multiblock;

import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.component.IOnActiveStateChangeComponent;
import gregtech.api.ec.component.IOnMachineRemovalComponent;
import gregtech.api.ec.component.IOnMachineUnloadComponent;
import gregtech.api.ec.component.multiblock.ICoilLogicComponent;
import gregtech.api.ec.component.multiblock.IPostStructureCheckComponent;
import gregtech.api.ec.component.multiblock.IPreStructureCheckComponent;
import gregtech.api.ec.component.multiblock.IStructureCheckHandlerComponent;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.common.data.GTCoilTracker;
import gregtech.common.ec.multiblock.internal.BaseMultiblockComponent;
import it.unimi.dsi.fastutil.longs.LongArrayList;

public class CoilLogicComponent extends BaseMultiblockComponent
    implements ICoilLogicComponent, IPreStructureCheckComponent, IOnMachineUnloadComponent, IOnMachineRemovalComponent,
    IOnActiveStateChangeComponent, IPostStructureCheckComponent, IStructureCheckHandlerComponent {

    private final LongArrayList mCoils = new LongArrayList();
    private GTCoilTracker.MultiCoilLease coilLease = null;
    private @NotNull HeatingCoilLevel coilLevel = HeatingCoilLevel.None;
    private @Nullable Consumer<HeatingCoilLevel> onCoilValidated = null;

    public CoilLogicComponent setOnCoilValidated(@Nullable Consumer<HeatingCoilLevel> onCoilValidated) {
        this.onCoilValidated = onCoilValidated;
        return this;
    }

    @Override
    public void addCoilCoordinate(long packedCorrdinate) {
        mCoils.add(packedCorrdinate);
    }

    @Override
    public @NotNull LongArrayList getCoilCoordinates() {
        return mCoils;
    }

    @Override
    public @NotNull HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    @Override
    public void setCoilLevel(@NotNull HeatingCoilLevel level) {
        coilLevel = level;
    }

    protected void activateCoilLease() {
        if (!mCoils.isEmpty() && coilLease == null) {
            coilLease = GTCoilTracker.activate(getMachine(), mCoils);
        }
    }

    protected void deactivateCoilLease() {
        if (coilLease != null) {
            GTCoilTracker.deactivate(coilLease);
            coilLease = null;
        }
    }

    private void clearAndDeactivate() {
        mCoils.clear();
        coilLevel = HeatingCoilLevel.None;
        deactivateCoilLease();
    }

    @Override
    public void onMachineUnload() {
        clearAndDeactivate();
    }

    @Override
    public void onMachineRemoval() {
        clearAndDeactivate();
    }

    @Override
    public void onPreStructureCheck(@NotNull IGregTechTileEntity te) {
        clearAndDeactivate();
    }

    @Override
    public void onPostStructureCheck(@NotNull IGregTechTileEntity te, @NotNull List<StructureError> errors) {
        if (errors.isEmpty()) {
            if (te.isActive()) {
                activateCoilLease();
            }
            if (onCoilValidated != null) {
                onCoilValidated.accept(coilLevel);
            }
        }
    }

    @Override
    public int getPostStructureCheckPriority() {
        return PostStructureCheckPriority.COIL.priority;
    }

    @Override
    public void onMachineActivated(@NotNull IGregTechTileEntity igte) {
        if (igte.isServerSide() && isMachineFormed()) activateCoilLease();
    }

    @Override
    public void onMachineDeactivated(@NotNull IGregTechTileEntity igte) {
        if (igte.isServerSide()) deactivateCoilLease();
    }

    @Override
    public void checkMachine(@NotNull IGregTechTileEntity aBaseMetaTileEntity, @NotNull List<StructureError> errors) {
        if (coilLevel == HeatingCoilLevel.None) {
            errors.add(StructureErrorRegistry.COIL_LEVEL_NOT_ENOUGH);
        }
    }

    @Override
    public int getStructureCheckPriority() {
        return StructureCheckPriority.COIL.priority;
    }
}
