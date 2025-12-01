package gregtech.common.gui.modularui.hatch;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.NumberFormat;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyor;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;

public class MTEHatchVacuumConveyorGui extends MTEHatchBaseGui<MTEHatchVacuumConveyor> {

    public MTEHatchVacuumConveyorGui(MTEHatchVacuumConveyor VC) {
        super(VC);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        ParentWidget<?> parent = super.createContentSection(panel, syncManager);
        parent.child(createCCSlotGroup(panel, syncManager));
        parent.child(createButtonHoldingColumn(panel, syncManager));
        return parent;
    }

    // todo: add more functionality from the ticket
    protected Flow createButtonHoldingColumn(ModularPanel panel, PanelSyncManager syncManager) {
        Flow column = Flow.column()
            .width(40)
            .coverChildrenHeight()
            .crossAxisAlignment(Alignment.CrossAxis.END);
        column.child(createVoidButton(syncManager));
        return column.align(Alignment.CenterRight)
            .marginRight(3);
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        GenericSyncValue<CircuitComponentPacket> contentsSyncHandler = new GenericSyncValue<>(
            () -> hatch.contents != null ? hatch.contents : new CircuitComponentPacket(),
            val -> hatch.contents = val,
            buf -> { return new CircuitComponentPacket((NBTTagCompound) NetworkUtils.readNBTBase(buf)); },
            (buf, item) -> { NetworkUtils.writeNBTBase(buf, item.writeToNBT()); },
            (a, b) -> {
                return a.getComponents()
                    .equals(b.getComponents());
            },
            null);
        syncManager.syncValue("contents", contentsSyncHandler);
    }

    @SuppressWarnings("unchecked")
    private IWidget createCCSlotGroup(ModularPanel panel, PanelSyncManager syncManager) {

        final String[] matrix = new String[hatch.getRowCount()];
        Arrays.fill(matrix, StringUtils.getRepetitionOf('c', hatch.getColumnCount()));

        GenericSyncValue<CircuitComponentPacket> componentSyncer = syncManager
            .findSyncHandler("contents", GenericSyncValue.class);

        DynamicSyncHandler componentHandler = new DynamicSyncHandler().widgetProvider((syncManager1, buffer) -> {
            if (buffer == null) return new EmptyWidget();

            try {
                CircuitComponentPacket packet = new CircuitComponentPacket(
                    (NBTTagCompound) NetworkUtils.readNBTBase(buffer));
                return SlotGroupWidget.builder()
                    .matrix(matrix)
                    .key('c', index -> {
                        List<ItemStack> itemRepresentations = packet.getItemRepresentations();
                        if (index >= itemRepresentations.size()) return GTGuiTextures.SLOT_ITEM_STANDARD.asWidget()
                            .size(18);
                        ItemStack item = itemRepresentations.get(index);
                        if (item == null) return GTGuiTextures.SLOT_ITEM_STANDARD.asWidget()
                            .size(18);
                        long amount = packet.getComponents()
                            .get(CircuitComponent.getFromFakeStackUnsafe(item));
                        return createSlotWidget(item, amount, syncManager);
                    })
                    .build();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        componentSyncer.setChangeListener(
            () -> componentHandler.notifyUpdate(
                (packet -> packet.writeNBTTagCompoundToBuffer(
                    componentSyncer.getValue()
                        .writeToNBT()))));

        return new DynamicSyncedWidget<>().coverChildren()
            .align(Alignment.CenterLeft)
            .marginLeft(3)
            .syncHandler(componentHandler);
    }

    private Widget<?> createSlotWidget(ItemStack item, long amount, PanelSyncManager syncManager) {
        return new Widget<>().size(18)
            .background(
                GTGuiTextures.SLOT_ITEM_STANDARD,
                new DynamicDrawable(
                    () -> new ItemDrawable(item).asIcon()
                        .size(16)))
            .overlay(
                IKey.dynamic(() -> NumberFormat.format(amount, NumberFormat.AMOUNT_TEXT))
                    .scale(0.6f)
                    .alignment(Alignment.BottomRight)
                    .color(Color.WHITE.main))
            .tooltip(t -> {
                if (!syncManager.isClient()) return; // needed since this runs on server and crashes
                t.addStringLines(item.getTooltip(syncManager.getPlayer(), false))
                    .addLine(EnumChatFormatting.DARK_GRAY + "Amount: " + GTUtility.formatNumbers(amount));
            });
    }

    @SuppressWarnings("unchecked")
    protected IWidget createVoidButton(PanelSyncManager syncManager) {

        GenericSyncValue<CircuitComponentPacket> syncContents = syncManager
            .findSyncHandler("contents", GenericSyncValue.class);

        return new ButtonWidget<>().overlay(GuiTextures.CODE)
            .size(18, 18)
            .onMousePressed(mouseButton -> {
                syncContents.setValue(new CircuitComponentPacket());
                return true;
            })
            .tooltip(t -> {
                t.addLine("Delete Stored Circuit Components");;
            });
    }

}
