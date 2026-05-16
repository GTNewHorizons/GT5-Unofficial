package gregtech.common.gui.modularui.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
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
            .marginTop(4)
            .marginRight(4)
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
            new Grid().gridOfWidthHeight(
                2,
                2,
                ($x, $y, index) -> new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, index) {

                    @Override
                    public int getItemStackLimit(@NotNull ItemStack stack) {
                        return 1;
                    }

                    @Override
                    public boolean canTakeStack(EntityPlayer playerIn) {
                        return !isActiveSyncer.getBoolValue() && heatSyncer.getIntValue() <= 0;
                    }
                }.slotGroup("item_inv")
                    .filter(t -> isValidItem(t) && !isActiveSyncer.getBoolValue() && heatSyncer.getIntValue() <= 0))
                    .backgroundOverlay(GTGuiTextures.TT_OVERLAY_SLOT_RACK))
                .minElementMargin(2)
                .center()
                .coverChildren());

        return parent;
    }

    @Override
    protected IDrawable.DrawableWidget createLogo() {
        return new IDrawable.DrawableWidget(GTGuiTextures.TT_PICTURE_TECTECH_LOGO).size(18);
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.registerSlotGroup("item_inv", 2);
        syncManager.syncValue("isActive", new BooleanSyncValue(baseMetaTileEntity::isActive));
        syncManager.syncValue("heat", new IntSyncValue(hatch::getHeat));
    }

    private boolean isValidItem(ItemStack itemStack) {
        return MTEHatchRack.validRackItems.stream()
            .anyMatch(itemStack::isItemEqual);
    }
}
