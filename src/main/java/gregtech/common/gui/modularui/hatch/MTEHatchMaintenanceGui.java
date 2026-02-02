package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.adapter.MaintainceAdapter;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchMaintenanceGui extends MTEHatchBaseGui<MTEHatchMaintenance> {

    public MTEHatchMaintenanceGui(MTEHatchMaintenance hatch) {
        super(hatch);
    }


    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager);
    }

    @Override
    protected Flow createRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        GenericListSyncHandler<MTEMultiBlockBase> attachedMultis = syncManager.findSyncHandler("multis", GenericListSyncHandler.class);
        return super.createRightCornerFlow(panel, syncManager)
            .child(new DynamicDrawable( () -> GTGuiTextures.OVERLAY_NEEDS_WRENCH).asWidget()
                .tooltipDynamic((t) -> {
                    for (MTEMultiBlockBase multiblock: attachedMultis.getValue()){
                            t.addLine(multiblock.getLocalName());
                        if (!multiblock.mCrowbar) t.add(
                                GTGuiTextures.OVERLAY_NEEDS_CROWBAR.asIcon()
                                    .size(16, 16))
                            .add(" ");
                        if (!multiblock.mHardHammer) t.add(
                                GTGuiTextures.OVERLAY_NEEDS_HARDHAMMER.asIcon()
                                    .size(16, 16))
                            .add(" ");
                        if (!multiblock.mScrewdriver) t.add(
                                GTGuiTextures.OVERLAY_NEEDS_SCREWDRIVER.asIcon()
                                    .size(16, 16))
                            .add(" ");
                        if (!multiblock.mSoftMallet) t.add(
                                GTGuiTextures.OVERLAY_NEEDS_SOFTHAMMER.asIcon()
                                    .size(16, 16))
                            .add(" ");
                        if (!multiblock.mSolderingTool) t.add(
                                GTGuiTextures.OVERLAY_NEEDS_SOLDERING.asIcon()
                                    .size(16, 16))
                            .add(" ");
                        if (!multiblock.mWrench) t.add(
                                GTGuiTextures.OVERLAY_NEEDS_WRENCH.asIcon()
                                    .size(16, 16))
                            .addLine(" ");
                    }
                })
        );
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        GenericListSyncHandler<MTEMultiBlockBase> attachedMultis = new GenericListSyncHandler.Builder<MTEMultiBlockBase>()
            .getter(hatch::getMultiBlocks)
            .adapter(new MaintainceAdapter())
            .setter(hatch::setMultiblocks)
            .build();
        syncManager.syncValue("multis", attachedMultis);
    }
}

