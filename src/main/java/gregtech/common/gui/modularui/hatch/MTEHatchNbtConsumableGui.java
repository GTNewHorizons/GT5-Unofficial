package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.MTEHatchNbtConsumable;

public class MTEHatchNbtConsumableGui extends MTEHatchBaseGui<MTEHatchNbtConsumable> {

    public MTEHatchNbtConsumableGui(MTEHatchNbtConsumable hatch) {
        super(hatch);
        dimension = switch (hatch.getInputSlotCount()) {
            case 4 -> {
                yield 2;
            }
            case 9 -> {
                yield 3;
            }
            case 16 -> {
                yield 4;
            }
            default -> {
                yield 1;
            }
        };
    }

    private int dimension = 1;

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.registerSlotGroup("input", dimension);
        syncManager.registerSlotGroup("output", dimension);
    }

    @Override
    protected boolean doesAddGregTechLogo() {
        return dimension < 4;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow slotRow = Flow.row()
            .coverChildren()
            .alignY(0.4f)
            .alignX(Alignment.Center);
        final int amountPerSlotGroup = hatch.getInputSlotCount();

        // < 4x4 slot groups have logo in the corner, otherwise its in the center
        boolean logoOverride = !this.doesAddGregTechLogo();

        slotRow.child(createInputColumn(amountPerSlotGroup).marginRight(logoOverride ? 0 : 54));
        slotRow.childIf(logoOverride, createLogo());
        slotRow.child(createOutputColumn(amountPerSlotGroup));

        return super.createContentSection(panel, syncManager).child(slotRow);
    }

    protected Flow createInputColumn(int amountPerSlotGroup) {
        Flow inputColumn = Flow.column()
            .coverChildren();
        if (amountPerSlotGroup < 16) {
            inputColumn.child(
                IKey.lang("gtpp.gui.text.stock")
                    .asWidget());
        }
        String[] matrix = new String[dimension];
        String row = "";
        for (int i = 0; i < dimension; i++) {
            row += "c";
        }
        Arrays.fill(matrix, row);
        inputColumn.child(
            SlotGroupWidget.builder()
                .matrix(matrix)
                .key(
                    'c',
                    index -> new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, index).slotGroup("input")))
                .build());
        return inputColumn;
    }

    @Override
    protected IDrawable.DrawableWidget createLogo() {
        return super.createLogo().margin(0);
    }

    protected Flow createOutputColumn(int amountPerSlotGroup) {
        Flow outputColumn = Flow.column()
            .coverChildren();
        if (amountPerSlotGroup < 16) {
            outputColumn.child(
                IKey.lang("gtpp.gui.text.active")
                    .asWidget());
        }

        String[] matrix = new String[dimension];
        String row = "";
        for (int i = 0; i < dimension; i++) {
            row += "c";
        }
        Arrays.fill(matrix, row);
        outputColumn.child(
            SlotGroupWidget.builder()
                .matrix(matrix)
                .key(
                    'c',
                    index -> new ItemSlot().slot(
                        new ModularSlot(hatch.inventoryHandler, index + amountPerSlotGroup).accessibility(false, true)
                            .slotGroup("output")))
                .build());
        return outputColumn;
    }
}
