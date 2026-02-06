package gregtech.common.gui.modularui.multiblock;

import bwcrossmod.galacticgreg.MTEVoidMinerBase;
import bwcrossmod.galacticgreg.VoidMinerUtility;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IStringValue;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.BoolValue;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericCollectionSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericSetSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.GTItemOre;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.util.StatCollector.translateToLocal;

// TODO: improve visual clarity on buttons
public class MTEVoidMinerBaseGui extends MTEMultiBlockBaseGui<MTEVoidMinerBase> {

    String search = "";

    public MTEVoidMinerBaseGui(MTEVoidMinerBase base) {
        super(base);
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        IPanelHandler filterPopup = syncManager.panel("filter", this::createFilterPopup, true);
        return super.createRightPanelGapRow(parent, syncManager)
            .child(new ButtonWidget<>().onMousePressed(button -> {
                if (filterPopup.isPanelOpen()) {
                    filterPopup.closePanel();
                } else filterPopup.openPanel();
                return true;
            })
                // TODO: possibly find a better icon for this. but also i love the gear texture
                .overlay(GuiTextures.GEAR));
    }

    public ModularPanel createFilterPopup(PanelSyncManager syncManager, IPanelHandler panelHandler) {
        GenericSyncValue<List<ItemStack>> listSyncer = new GenericSyncValue<>(
            () -> multiblock.selected.getStacks(),
            list -> { multiblock.selected = new ItemStackHandler(list); },
            new ItemStackListAdapter());
        syncManager.syncValue("selected", listSyncer);

        GTUtility.ItemId[] ores = sortOres(multiblock.dropMap);
        return new ModularPanel("gt:vm:filter").child(ButtonWidget.panelCloseButton())
            .child(new Column().child(IKey.lang("GT5U.gui.text.vm.title").asWidget())
                .child(new Row()
                    .child(createOreToggleButtonGrid(syncManager, ores))
                    .child(createRightButtonColumn(listSyncer, ores))
                    .childPadding(3)
                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .coverChildren())
                .child(new TextFieldWidget() {
                    @Override
                    public @NotNull Result onKeyPressed(char character, int keyCode) {
                        Result result = super.onKeyPressed(character, keyCode);
                        if (this.getValue() instanceof IStringValue<?> value) {
                            value.setStringValue(getText());
                        }
                        return result;
                    }
                }
                    .value(new StringSyncValue(() -> search, str -> search = str))
                    .hintText(translateToLocal("GT5U.gui.text.vm.searchhint"))
                    .alignX(0f)
                    .width(100))
                .childPadding(3)
                .margin(8)
                .coverChildren())
            .coverChildren();
    }

    private ListWidget<IWidget, ?> createOreToggleButtonGrid(PanelSyncManager syncManager, GTUtility.ItemId[] ores) {
        GenericSyncValue<List<ItemStack>> syncer = (GenericSyncValue<List<ItemStack>>) syncManager.findSyncHandler("selected");
        int buttonsPerRow = 10;
        int rowCount = (int) Math.ceil((double) ores.length / buttonsPerRow);
        return new ListWidget<>().children(rowCount, row -> {
            return new Row().children(Math.min(buttonsPerRow, ores.length - (row * buttonsPerRow)), rowIndex -> {
                int index = (row * buttonsPerRow) + rowIndex;
                ItemStack stack = ores[index].getItemStack();
                if (!(stack.getItem() instanceof GTItemOre ore)) return new EmptyWidget();
                return new ToggleButton()
                    .value(new BoolValue.Dynamic(() -> multiblock.selected.getStackInSlot(index) != null, bool -> {
                        if (bool) {
                            multiblock.selected.insertItem(index, stack, false);
                        } else multiblock.selected.extractItem(index, 1, false);
                        syncer.setValue(multiblock.selected.getStacks());
                    }))
                    .overlay(getOreButtonOverlay(stack))
                    .tooltip(t -> t.add(stack.getDisplayName()))
                    .size(18);
            }).coverChildren();
        }).crossAxisAlignment(Alignment.CrossAxis.START)
            .coverChildrenWidth()
            .height(18 * Math.min(rowCount, 8));
    }

    private Flow createRightButtonColumn(GenericSyncValue<List<ItemStack>> syncer, GTUtility.ItemId[] ores) {
        return new Column()
            .child(new ToggleButton()
                .value(new BooleanSyncValue(() -> multiblock.mBlacklist, bool -> multiblock.mBlacklist = bool))
                .tooltip(false, t -> t.add(translateToLocal("GT5U.gui.button.vm.whitelist")))
                .tooltip(true, t -> t.add(translateToLocal("GT5U.gui.button.vm.blacklist")))
                .overlay(false, GTGuiTextures.OVERLAY_BUTTON_WHITELIST.asIcon().size(16))
                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST.asIcon().size(16)))
            .child(new ButtonWidget<>()
                .onMousePressed(button -> {
                    for (int i = 0; i < ores.length; i++) {
                        multiblock.selected.setStackInSlot(i, ores[i].getItemStack());
                    }
                    syncer.setValue(multiblock.selected.getStacks());
                    return true;
                })
                .tooltip(t -> t.add(translateToLocal("GT5U.gui.button.vm.select")))
                .overlay(GTGuiTextures.OVERLAY_BUTTON_CHECKMARK.asIcon().size(16)))
            .child(new ButtonWidget<>()
                .tooltip(t -> t.add(translateToLocal("GT5U.gui.button.vm.deselect")))
                .onMousePressed(button -> {
                    multiblock.selected = new ItemStackHandler(ores.length);
                    syncer.setValue(multiblock.selected.getStacks());
                    return true;
                })
                .overlay(GTGuiTextures.OVERLAY_BUTTON_CROSS.asIcon().size(16)))
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .childPadding(3)
            .coverChildrenWidth()
            .heightRel(1F);
    }

    private IDrawable getOreButtonOverlay(ItemStack stack) {
        // if (!(stack.getItem() instanceof GTItemOre ore)) return IDrawable.EMPTY;
        return new DynamicDrawable(() -> {
            // ItemStack rawOre = GTOreDictUnificator.get(OrePrefixes.rawOre, ore.blockOre.getMaterial(stack.getItemDamage()), 1);
            IDrawable oreDrawable = new ItemDrawable(stack).asIcon().size(16);
            if (matchesSearch(stack)) {
                return new DrawableStack(
                    new Rectangle().setColor(Color.rgb(0, 255, 0)).asIcon().size(16),
                    oreDrawable);
            } else {
                return new DrawableStack(new Rectangle().setColor(Color.argb(128, 128, 128, 0)), oreDrawable);
            }
        });
    }

    // On game launch the order in the multis drop map randomizes, so we sort it by meta so everything can stay the same
    private GTUtility.ItemId[] sortOres(VoidMinerUtility.DropMap dropMap) {
        return Arrays.stream(dropMap.getOres()).sorted((ore1, ore2) -> {
            return ore1.metaData() > ore2.metaData() ? 1 : -1;
        }).toArray(GTUtility.ItemId[]::new);
    }

    private boolean matchesSearch(ItemStack ore) {
        return !search.isEmpty() && ore.getDisplayName().toLowerCase().contains(search.toLowerCase());
    }

    private static class ItemStackListAdapter implements IByteBufAdapter<List<ItemStack>> {

        @Override
        public List<ItemStack> deserialize(PacketBuffer buffer) throws IOException {
            List<ItemStack> list = new ArrayList<>();
            int size = buffer.readInt();
            for (int i = 0; i < size; i++) {
                list.add(buffer.readItemStackFromBuffer());
            }
            return list;
        }

        @Override
        public void serialize(PacketBuffer buffer, List<ItemStack> u) throws IOException {
            buffer.writeInt(u.size());
            for (ItemStack stack : u) {
                buffer.writeItemStackToBuffer(stack);
            }
        }

        @Override
        public boolean areEqual(@NotNull List<ItemStack> t1, @NotNull List<ItemStack> t2) {
            if (t1.size() != t2.size()) return false;
            for (int i = 0; i < t1.size(); i++) {
                if (!ItemStack.areItemStacksEqual(t1.get(i), t2.get(i))) return false;
            }
            return true;
        }
    }

}
