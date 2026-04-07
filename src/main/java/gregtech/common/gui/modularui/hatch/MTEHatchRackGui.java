package gregtech.common.gui.modularui.hatch;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
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

        Flow mainRow = Flow.row()
            .full();

        Flow statusColumn = Flow.col()
            .coverChildren()
            .childPadding(2)
            .marginTop(4)
            .marginRight(4)
            .align(Alignment.TopRight);

        // active status decoration
        statusColumn.child(
            new DynamicDrawable(
                () -> isActiveSyncer.getBoolValue() ? GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_ON
                    : GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_DISABLED).asWidget()
                        .background(GTGuiTextures.BUTTON_STANDARD_LIGHT_16x16)
                        .size(16));

        // heat decoration
        statusColumn.child(
            new DynamicDrawable(
                () -> heatSyncer.getIntValue() > 0 ? GTGuiTextures.TT_OVERLAY_BUTTON_HEAT_ON
                    : GTGuiTextures.TT_OVERLAY_BUTTON_HEAT_OFF).asWidget()
                        .background(GTGuiTextures.BUTTON_STANDARD_LIGHT_16x16)
                        .size(16)
                        .tooltip(
                            t -> t.addLine(
                                IKey.dynamic(
                                    () -> translateToLocalFormatted(
                                        "gt.blockmachines.hatch.rack.heat",
                                        heatSyncer.getIntValue())))));

        mainRow.child(statusColumn);

        // central decoration
        mainRow.child(
            GTGuiTextures.PICTURE_HEAT_SINK.asWidget()
                .size(84, 60)
                .align(Alignment.CENTER));

        // input slots
        // TODO fix sizing
        mainRow
            .child(new Grid().mapTo(2, 4, index -> new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, index) {

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
                .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.TT_OVERLAY_SLOT_RACK)
                .margin(2))
                .align(Alignment.CENTER)
                .coverChildren());

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    @Override
    protected IDrawable.DrawableWidget createLogo() {
        return new IDrawable.DrawableWidget(GTGuiTextures.PICTURE_TECTECH_LOGO).size(18);
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
