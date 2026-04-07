package gregtech.common.gui.modularui.hatch;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchObjectHolder;

public class MTEHatchObjectHolderGui extends MTEHatchBaseGui<MTEHatchObjectHolder> {

    public MTEHatchObjectHolderGui(MTEHatchObjectHolder hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue isActiveSyncer = syncManager.findSyncHandler("isActive", BooleanSyncValue.class);

        Flow mainRow = Flow.row()
            .full();

        // active status decoration
        mainRow.child(
            new DynamicDrawable(
                () -> isActiveSyncer.getBoolValue() ? GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_ON
                    : GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_DISABLED).asWidget()
                        .background(GTGuiTextures.BUTTON_STANDARD_LIGHT_16x16)
                        .size(16)
                        .align(Alignment.TopRight)
                        .marginTop(4)
                        .marginRight(4));

        // central decoration
        mainRow.child(
            GTGuiTextures.PICTURE_HEAT_SINK.asWidget()
                .size(84, 60)
                .align(Alignment.CENTER));
        mainRow.child(
            GTGuiTextures.PICTURE_RACK_LARGE.asWidget()
                .size(40, 40)
                .align(Alignment.CENTER));

        // input slot
        mainRow.child(new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, 0) {

            @Override
            public int getItemStackLimit(@NotNull ItemStack stack) {
                return 1;
            }

            @Override
            public boolean canTakeStack(EntityPlayer playerIn) {
                return !isActiveSyncer.getBoolValue();
            }
        }.slotGroup("item_inv"))
            .align(Alignment.CENTER));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    @Override
    protected IDrawable.DrawableWidget createLogo() {
        return new IDrawable.DrawableWidget(GTGuiTextures.TT_PICTURE_TECTECH_LOGO).size(18);
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.registerSlotGroup("item_inv", 1);
        syncManager.syncValue("isActive", new BooleanSyncValue(baseMetaTileEntity::isActive));
    }
}
