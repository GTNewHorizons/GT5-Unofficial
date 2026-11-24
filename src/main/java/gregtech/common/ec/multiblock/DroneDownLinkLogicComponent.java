package gregtech.common.ec.multiblock;

import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.component.IOnMachineRemovalComponent;
import gregtech.api.ec.component.IOnMachineUnloadComponent;
import gregtech.api.ec.component.IOnRecipeFinishComponent;
import gregtech.api.ec.component.IOnWorkStateChangeComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.multiblock.IMaintenanceLogicComponent;
import gregtech.api.ec.component.multiblock.IOnHatchAttachedComponent;
import gregtech.api.ec.component.multiblock.IPreStructureCheckComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.ec.multiblock.internal.BaseMultiblockComponent;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;
import gregtech.common.tileentities.machines.multi.drone.MTEHatchDroneDownLink;
import gregtech.common.tileentities.machines.multi.drone.production.ProductionRecord;

public class DroneDownLinkLogicComponent extends BaseMultiblockComponent
    implements IOnHatchAttachedComponent, IPreStructureCheckComponent, IOnMachineUnloadComponent,
    IOnMachineRemovalComponent, IOnRecipeFinishComponent, IOnWorkStateChangeComponent {

    private final List<MTEHatchDroneDownLink> downLinks = new ArrayList<>();

    @Override
    public void onMachineRemoval() {
        downLinks.clear();
    }

    @Override
    public void onMachineUnload() {
        downLinks.clear();
    }

    @Override
    public void onHatchAttached(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof MTEHatchDroneDownLink ddl) {
            if (!downLinks.contains(ddl)) {
                ddl.registerMachineController(getMachine());
                downLinks.add(ddl);
            }
        }
    }

    @Override
    public void onPreStructureCheck(@NotNull IGregTechTileEntity te) {
        downLinks.clear();
    }

    @Override
    public void onRecipeFinish(@NotNull IRecipeLogicComponent recipeLogicComponent) {
        for (var ddl : validMTEList(downLinks)) {
            MTEDroneCentre centre = ddl.getCentre();
            if (centre == null) continue;
            ProductionRecord pdr = centre.productionDataRecorder;
            if (pdr.isActive()) pdr.addRecord(
                ((long) recipeLogicComponent.getMaxProgressTime()) * recipeLogicComponent.getEUt(),
                recipeLogicComponent.getOutputItems(),
                recipeLogicComponent.getOutputFluids());
        }

    }

    @Override
    public int getRecipeFinishPriority() {
        return RecipeFinishPriority.DRONE_COLLECT_DATA.priority;
    }

    @Override
    public void onEnableWorking() {
        for (var ddl : validMTEList(downLinks)) {
            ddl.findConnection(getMachine())
                .ifPresent(conn -> conn.setActive(true));
        }
    }

    @Override
    public void onDisableWorking() {
        for (var ddl : validMTEList(downLinks)) {
            ddl.findConnection(getMachine())
                .ifPresent(conn -> {
                    conn.setActive(false);
                    conn.setShutdownReason(
                        getMachine().getBaseMetaTileEntity()
                            .getLastShutDownReason());
                });
        }
    }

    @Override
    public void onComponentsReady() {
        getComponent(IMaintenanceLogicComponent.KEY);
    }
}
