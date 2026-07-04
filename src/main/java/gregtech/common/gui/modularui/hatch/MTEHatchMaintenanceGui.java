package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;
import java.util.stream.Collectors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;

public class MTEHatchMaintenanceGui extends MTEHatchBaseGui<MTEHatchMaintenance> {

    private final boolean mAuto;

    public MTEHatchMaintenanceGui(MTEHatchMaintenance hatch, boolean mAuto) {
        super(hatch);
        this.mAuto = mAuto;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        ParentWidget<?> parent = super.createContentSection(panel, syncManager);

        // tip box
        parent.child(
            GuiTextures.BUBBLE.asWidget()
                .background(GTGuiTextures.BUTTON_STANDARD)
                .topRel(0)
                .rightRel(0)
                .addTooltipStringLines(
                    Arrays
                        .stream(
                            GTUtility.translateMultiline(
                                mAuto ? "GT5U.gui.text.autorepair_info" : "GT5U.gui.text.repair_info"))
                        .collect(Collectors.toList())));

        parent.childIf(
            mAuto,
            () -> new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(2)
                .build()
                .center());

        // maintenance slot background
        parent.childIf(
            !mAuto,
            () -> GTGuiTextures.SLOT_MAINTENANCE.asWidget()
                .size(20)
                .center());

        parent.childIf(!mAuto, () -> createSimpleMaintenanceSlot(syncManager));

        return parent;
    }

    private ItemSlot createSimpleMaintenanceSlot(PanelSyncManager syncManager) {
        return new PhantomItemSlot() {

            @Override
            public boolean handleDragAndDrop(@NotNull ItemStack draggedStack, int button) {
                return false;
            }

            @Override
            public PhantomItemSlot slot(ModularSlot slot) {
                return syncHandler(new PhantomItemSlotSH(slot) {

                    @Override
                    protected void phantomClick(MouseData mouseData, ItemStack cursorStack) {
                        if (cursorStack == null) return;
                        EntityPlayer player = syncManager.getPlayer();

                        if (player == null) return;

                        machine.onToolClick(cursorStack, player);
                        // refresh held stack
                        if (cursorStack.stackSize < 1) syncManager.setCursorItem(null);
                        else syncManager.setCursorItem(cursorStack);
                    }
                });
            }
        }.slot(new ModularSlot(machine.inventoryHandler, 0))
            .size(25)
            .disableThemeBackground(true)
            .disableHoverThemeBackground(true)
            .center();
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
