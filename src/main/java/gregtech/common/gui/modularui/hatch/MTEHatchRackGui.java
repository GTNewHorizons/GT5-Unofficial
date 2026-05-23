package gregtech.common.gui.modularui.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.util.RackSlot;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import tectech.thing.metaTileEntity.hatch.MTEHatchRack;

public class MTEHatchRackGui extends MTEHatchBaseGui<MTEHatchRack> {

    public MTEHatchRackGui(MTEHatchRack hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue isActiveSyncer = syncManager.findSyncHandler("isActive", BooleanSyncValue.class);
        IntSyncValue heatSyncer = syncManager.findSyncHandler("heat", IntSyncValue.class);

        ParentWidget<?> parent = super.createContentSection(panel, syncManager);

        Flow statusColumn = Flow.col()
            .coverChildren()
            .childPadding(2)
            .topRel(0)
            .rightRel(0);

        // active status decoration
        statusColumn.child(
            new DynamicDrawable(
                () -> isActiveSyncer.getBoolValue() ? GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_ON
                    : GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_DISABLED).asWidget()
                        .background(GTGuiTextures.BUTTON_STANDARD_LIGHT_16x16)
                        .size(16)
                        .tooltipAutoUpdate(true)
                        .tooltipDynamic(
                            t -> t.addLine(
                                translateToLocalFormatted(
                                    "tt.gui.text.hatch.status",
                                    translateToLocal(
                                        isActiveSyncer.getBoolValue() ? "tt.gui.text.hatch.status.active"
                                            : "tt.gui.text.hatch.status.inactive")))));

        // heat decoration
        statusColumn.child(
            new DynamicDrawable(
                () -> heatSyncer.getIntValue() > 0 ? GTGuiTextures.TT_OVERLAY_BUTTON_HEAT_ON
                    : GTGuiTextures.TT_OVERLAY_BUTTON_HEAT_OFF).asWidget()
                        .background(GTGuiTextures.BUTTON_STANDARD_LIGHT_16x16)
                        .size(16)
                        .tooltipAutoUpdate(true)
                        .tooltipDynamic(
                            t -> t.addLine(
                                translateToLocalFormatted(
                                    "gt.blockmachines.hatch.rack.heat",
                                    heatSyncer.getIntValue()))));

        parent.child(statusColumn);

        // central decoration
        parent.child(
            GTGuiTextures.TT_PICTURE_HEAT_SINK.asWidget()
                .size(84, 60)
                .center());

        // input slots
        parent.child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(2)
                .itemSlotSupplier(() -> new ItemSlot().backgroundOverlay(GTGuiTextures.TT_OVERLAY_SLOT_RACK))
                .filter(
                    itemStack -> machine.isValidItem(itemStack) && !isActiveSyncer.getBoolValue()
                        && heatSyncer.getIntValue() <= 0)
                .modularSlotSupplier(RackSlot.supplier(isActiveSyncer, heatSyncer))
                .build()
                .center()
                .minElementMargin(2));

        return parent;
    }

    @Override
    protected IDrawable.DrawableWidget createLogo() {
        return new IDrawable.DrawableWidget(GTGuiTextures.TT_PICTURE_TECTECH_LOGO).size(18);
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("isActive", new BooleanSyncValue(baseMetaTileEntity::isActive));
        syncManager.syncValue("heat", new IntSyncValue(machine::getHeat, machine::setHeat));
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
