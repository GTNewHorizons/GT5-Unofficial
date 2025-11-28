package gregtech.common.gui.modularui.hatch;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.text.StringKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.NumberFormat;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyor;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;

public class MTEHatchVacuumConveyorGui extends MTEHatchBaseGui<MTEHatchVacuumConveyor> {

    public MTEHatchVacuumConveyorGui(MTEHatchVacuumConveyor VC) {
        super(VC);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {

        // Sync the contents between server and client
        GenericSyncValue<CircuitComponentPacket> contentsSyncHandler = new GenericSyncValue<>(
            () -> hatch.contents != null ? hatch.contents : new CircuitComponentPacket(),
            val -> hatch.contents = val,
            buf -> {
                CircuitComponentPacket packet = new CircuitComponentPacket(
                    (NBTTagCompound) NetworkUtils.readNBTBase(buf));
                return packet;
            },
            (buf, item) -> { NetworkUtils.writeNBTBase(buf, item.writeToNBT()); });
        syncManager.syncValue("contents", contentsSyncHandler);

        // Create the panel
        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(hatch, guiData, syncManager, uiSettings)
            .doesBindPlayerInventory(false)
            .doesAddGregTechLogo(false)
            .setHeight(176)
            .build();

        // Create row for 2 sections
        Flow panelRow = new Row().widthRel(1)
            .heightRel(1)
            .childPadding(2)
            .margin(7);

        // Create Column for Right side button interactions
        Flow interactablesColumn = new Column().width(70)
            .heightRel(1);

        // Create grid and create customly rendered items that can only show a tooltip and amount
        Grid grid = new Grid().coverChildren()
            .mapTo(5, hatch.getUiSlotCount(), i -> {
                SingleChildWidget<?> slot = new SingleChildWidget<>().background(GTGuiTextures.SLOT_ITEM_STANDARD)
                    .size(18);
                Widget<?> slotChild = new Widget<>().size(16)
                    .pos(1, 1);
                if (hatch.contents != null) {
                    List<ItemStack> representations = hatch.contents.getItemRepresentations();
                    if (i < representations.size()) {
                        ItemStack item = representations.get(i);
                        ItemDrawable itemDraw = new ItemDrawable(item);
                        slotChild.background(new DynamicDrawable(() -> itemDraw))
                            .overlay(
                                new StringKey(
                                    NumberFormat.format(
                                        hatch.contents.getComponents()
                                            .get(CircuitComponent.getFromFakeStackUnsafe(item)),
                                        NumberFormat.AMOUNT_TEXT)).scale(0.6f)
                                            .alignment(Alignment.BottomRight)
                                            .style(EnumChatFormatting.WHITE));
                        slotChild.tooltip(
                            t -> t.clearText()
                                .addStringLines(item.getTooltip(guiData.getPlayer(), false)));
                    }
                }
                slot.child(slotChild);
                return slot;
            });

        // Make sure to update the slots each time the contents gets updated
        contentsSyncHandler.setChangeListener(() -> {
            if (hatch.contents == null) {
                return;
            }

            for (int i = 0; i < hatch.getUiSlotCount(); i++) {
                SingleChildWidget<?> slot = (SingleChildWidget<?>) grid.getChildren()
                    .get(i);
                Widget<?> slotChild = (Widget<?>) slot.getChildren()
                    .get(0);
                if (hatch.contents != null) {
                    List<ItemStack> representations = hatch.contents.getItemRepresentations();
                    if (i < representations.size()) {
                        ItemStack item = representations.get(i);
                        ItemDrawable itemDraw = new ItemDrawable(item);
                        slotChild.background(itemDraw)
                            .overlay(
                                new StringKey(
                                    NumberFormat.format(
                                        hatch.contents.getComponents()
                                            .get(CircuitComponent.getFromFakeStackUnsafe(item)),
                                        NumberFormat.AMOUNT_TEXT)).scale(0.6f)
                                            .alignment(Alignment.BottomRight)
                                            .style(EnumChatFormatting.WHITE));
                        slotChild.tooltip(
                            t -> t.clearText()
                                .addStringLines(item.getTooltip(guiData.getPlayer(), false)));
                        continue;
                    }
                }
                slotChild.overlay();
                slotChild.tooltip(t -> t.clearText());

            }
        });

        return panel.child(
            panelRow.child(grid)
                .child(interactablesColumn.child(createVoidButton(syncManager))));
    }

    protected IWidget createVoidButton(PanelSyncManager syncManager) {

        GenericSyncValue<CircuitComponentPacket> syncContents = (GenericSyncValue<CircuitComponentPacket>) syncManager
            .getSyncHandlerFromMapKey("contents:0");

        return new ButtonWidget<>().size(18, 18)
            .onMousePressed(mouseButton -> {
                syncContents.setValue(new CircuitComponentPacket());
                return true;
            })
            .tooltip(t -> {
                t.addLine("Delete Stored Circuit Components");;
            });
    }

}
