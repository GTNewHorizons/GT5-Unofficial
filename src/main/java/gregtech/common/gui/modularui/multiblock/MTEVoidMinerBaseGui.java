package gregtech.common.gui.modularui.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.io.IOException;
import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.BoolValue;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import bwcrossmod.galacticgreg.MTEVoidMinerBase;
import bwcrossmod.galacticgreg.VoidMinerUtility;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class MTEVoidMinerBaseGui extends MTEMultiBlockBaseGui<MTEVoidMinerBase> {

    String search = "";

    public MTEVoidMinerBaseGui(MTEVoidMinerBase base) {
        super(base);
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        IPanelHandler filterPopup = syncManager.syncedPanel("filter", true, this::createFilterPopup);
        return super.createRightPanelGapRow(parent, syncManager)
            .childIf(multiblock.dropMap != null, () -> new ButtonWidget<>().onMousePressed(button -> {
                if (filterPopup.isPanelOpen()) {
                    filterPopup.closePanel();
                } else filterPopup.openPanel();
                return true;
            })
                .overlay(GuiTextures.GEAR));
    }

    public ModularPanel createFilterPopup(PanelSyncManager syncManager, IPanelHandler panelHandler) {
        GenericSyncValue<ItemStackHandler> listSyncer = new GenericSyncValue<>(
            ItemStackHandler.class,
            () -> multiblock.selected,
            handler -> multiblock.selected = handler,
            new ItemStackListAdapter());
        syncManager.syncValue("selected", listSyncer);

        GTUtility.ItemId[] ores = sortOres(multiblock.dropMap);
        return new ModularPanel("gt:vm:filter").child(ButtonWidget.panelCloseButton())
            .child(
                new Column().child(
                    IKey.lang("GT5U.gui.text.vm.title")
                        .asWidget())
                    .child(
                        new Row().child(createOreToggleButtonGrid(syncManager, ores))
                            .child(createRightButtonColumn(listSyncer, ores))
                            .childPadding(3)
                            .crossAxisAlignment(Alignment.CrossAxis.START)
                            .coverChildren())
                    .child(
                        new TextFieldWidget().value(new StringSyncValue(() -> search, str -> search = str))
                            .hintText(translateToLocal("GT5U.gui.text.vm.searchhint"))
                            .autoUpdateOnChange(true)
                            .anchorLeft(0)
                            .width(100))
                    .childPadding(3)
                    .margin(8)
                    .coverChildren())
            .coverChildren();
    }

    private ListWidget<IWidget, ?> createOreToggleButtonGrid(PanelSyncManager syncManager, GTUtility.ItemId[] ores) {
        GenericSyncValue<ItemStackHandler> syncer = (GenericSyncValue<ItemStackHandler>) syncManager
            .findSyncHandler("selected");
        int buttonsPerRow = 10;
        int rowCount = (int) Math.ceil((double) ores.length / buttonsPerRow);
        return new ListWidget<>().child(
            Flow.row()
                .children(ores.length, index -> {
                    ItemStack stack = ores[index].getItemStack();
                    return new ToggleButton()
                        .value(new BoolValue.Dynamic(() -> multiblock.selected.getStackInSlot(index) != null, bool -> {
                            if (bool) {
                                multiblock.selected.insertItem(index, stack, false);
                            } else multiblock.selected.extractItem(index, 1, false);
                            syncer.setValue(multiblock.selected);
                        }))
                        .overlay(
                            new ItemDrawable(stack).asIcon()
                                .size(16))
                        .background(true, GTGuiTextures.BUTTON_STANDARD_PRESSED.withColorOverride(Color.GREY.main))
                        .background(false, GTGuiTextures.BUTTON_STANDARD)
                        .tooltipBuilder(false, t -> getOreButtonTooltip(t, stack, false))
                        .tooltipBuilder(true, t -> getOreButtonTooltip(t, stack, true))
                        .setEnabledIf($ -> matchesSearch(stack))
                        .size(18);
                })
                .wrap()
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .width(18 * buttonsPerRow)
                .coverChildrenHeight()
                .collapseDisabledChild())
            .coverChildrenWidth()
            .height(18 * Math.min(rowCount, 8));
    }

    private Flow createRightButtonColumn(GenericSyncValue<ItemStackHandler> syncer, GTUtility.ItemId[] ores) {
        return new Column()
            .child(
                new ToggleButton()
                    .value(new BooleanSyncValue(() -> multiblock.blacklist, bool -> multiblock.blacklist = bool))
                    .tooltip(false, t -> t.add(translateToLocal("GT5U.gui.button.vm.whitelist")))
                    .tooltip(true, t -> t.add(translateToLocal("GT5U.gui.button.vm.blacklist")))
                    .overlay(
                        false,
                        GTGuiTextures.OVERLAY_BUTTON_WHITELIST.asIcon()
                            .size(16))
                    .overlay(
                        true,
                        GTGuiTextures.OVERLAY_BUTTON_BLACKLIST.asIcon()
                            .size(16)))
            .child(new ButtonWidget<>().onMousePressed(button -> {
                for (int i = 0; i < ores.length; i++) {
                    multiblock.selected.setStackInSlot(i, ores[i].getItemStack());
                }
                syncer.setValue(multiblock.selected);
                return true;
            })
                .tooltip(t -> t.add(translateToLocal("GT5U.gui.button.vm.select")))
                .overlay(
                    GTGuiTextures.OVERLAY_BUTTON_CHECKMARK.asIcon()
                        .size(16)))
            .child(
                new ButtonWidget<>().tooltip(t -> t.add(translateToLocal("GT5U.gui.button.vm.deselect")))
                    .onMousePressed(button -> {
                        multiblock.selected = new ItemStackHandler(ores.length);
                        syncer.setValue(multiblock.selected);
                        return true;
                    })
                    .overlay(
                        GTGuiTextures.OVERLAY_BUTTON_CROSS.asIcon()
                            .size(16)))
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .childPadding(3)
            .coverChildrenWidth()
            .heightRel(1F);
    }

    // On game launch the order in the multis drop map randomizes, so we sort it by meta so everything can stay the same
    private GTUtility.ItemId[] sortOres(VoidMinerUtility.DropMap dropMap) {
        return Arrays.stream(dropMap.getOres())
            .sorted((ore1, ore2) -> {
                if (ore1.metaData() == ore2.metaData()) return 0;
                return ore1.metaData() > ore2.metaData() ? 1 : -1;
            })
            .toArray(GTUtility.ItemId[]::new);
    }

    private void getOreButtonTooltip(RichTooltip tt, ItemStack stack, boolean selected) {
        tt.addFromItem(stack);
        if (multiblock.blacklist) {
            tt.addLine(translateToLocal("GT5U.gui.button.vm.tt." + (selected ? "void" : "novoid")));
        } else tt.addLine(translateToLocal("GT5U.gui.button.vm.tt." + (selected ? "novoid" : "void")));
        tt.setAutoUpdate(true);
    }

    private boolean matchesSearch(ItemStack ore) {
        return search.isEmpty() || ore.getDisplayName()
            .toLowerCase()
            .contains(search.toLowerCase());
    }

    private static class ItemStackListAdapter implements IByteBufAdapter<ItemStackHandler> {

        @Override
        public ItemStackHandler deserialize(PacketBuffer buffer) throws IOException {
            ItemStackHandler handler = new ItemStackHandler();
            handler.deserializeNBT(buffer.readNBTTagCompoundFromBuffer());
            return handler;
        }

        @Override
        public void serialize(PacketBuffer buffer, ItemStackHandler u) throws IOException {
            buffer.writeNBTTagCompoundToBuffer(u.serializeNBT());
        }

        @Override
        public boolean areEqual(@NotNull ItemStackHandler t1, @NotNull ItemStackHandler t2) {
            if (t1.getSlots() != t2.getSlots()) return false;
            for (int i = 0; i < t1.getSlots(); i++) {
                if (!ItemStack.areItemStacksEqual(t1.getStackInSlot(i), t2.getStackInSlot(i))) return false;
            }
            return true;
        }
    }

}
