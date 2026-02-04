package gregtech.common.gui.modularui.multiblock;

import bwcrossmod.galacticgreg.MTEVoidMinerBase;
import bwcrossmod.galacticgreg.VoidMinerUtility;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TODO: add lang keys for everything because if ranzu gets on me about it im gonna be sad
// TODO: update tooltips as well
// TODO: add data stick compat
// TODO: Make it work
public class MTEVoidMinerBaseGui extends MTEMultiBlockBaseGui<MTEVoidMinerBase> {

    String search = "";
    PanelSyncManager mainPSM;

    public MTEVoidMinerBaseGui(MTEVoidMinerBase base) {
        super(base);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        mainPSM = syncManager;
        syncManager.syncValue("blacklist", new BooleanSyncValue(() -> multiblock.mBlacklist, bool -> multiblock.mBlacklist = bool));
        // TODO: WOW! THIS SUCKS! figure out how to sync to the client in ANY way other than this
        syncManager.registerClientSyncedAction("cupcakeSelected", buf -> {
            try {
                multiblock.selected.deserializeNBT(buf.readNBTTagCompoundFromBuffer());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
        syncManager.registerServerSyncedAction("updateSelected", buf -> {
            try {
                multiblock.selected.deserializeNBT(buf.readNBTTagCompoundFromBuffer());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        mainPSM.callSyncedAction("cupcakeSelected", this::writeSelectedPacket);

        GTUtility.ItemId[] ores = sortOres(multiblock.dropMap);
        return new ModularPanel("gt:vm:filter").child(ButtonWidget.panelCloseButton())
            .child(new Column().child(IKey.str(EnumChatFormatting.UNDERLINE + "Ore Voiding Selection").asWidget())
                .child(new Row().child(createOreToggleButtonGrid(syncManager, ores))
                    .child(new Column()
                        .child(new ToggleButton()
                            .syncHandler("blacklist")
                            .tooltip(false, t -> t.add("Whitelist - Unselected ores will be voided"))
                            .tooltip(true, t -> t.add("Blacklist - Selected ores will be voided"))
                            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_WHITELIST.asIcon().size(16))
                            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST.asIcon().size(16)))
                        .child(new ButtonWidget<>()
                            .onMousePressed(button -> {
                                for (int i = 0; i < ores.length; i++) {
                                    multiblock.selected.insertItem(i, ores[i].getItemStack(), false);
                                }
                                syncManager.callSyncedAction("updateSelected", this::writeSelectedPacket);
                                return true;
                            })
                            .tooltip(t -> t.add("Select all ores"))
                            .overlay(GTGuiTextures.OVERLAY_BUTTON_CHECKMARK.asIcon().size(16)))
                        .child(new ButtonWidget<>()
                            .tooltip(t -> t.add("Deselect all ores"))
                            .onMousePressed(button -> {
                                multiblock.selected = new ItemStackHandler(ores.length);
                                syncManager.callSyncedAction("updateSelected", this::writeSelectedPacket);
                                return true;
                            })
                            .overlay(GTGuiTextures.OVERLAY_BUTTON_CROSS.asIcon().size(16)))
                        .childPadding(3)
                        .coverChildren())
                    .childPadding(3)
                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .coverChildren())
                .child(new TextFieldWidget()
                    .value(new StringSyncValue(() -> search, str -> search = str))
                    .hintText("Search by name")
                    .alignX(0f)
                    .width(100))
                .childPadding(3)
                .margin(8)
                .coverChildren())
            .coverChildren();
    }

    private ListWidget<IWidget, ?> createOreToggleButtonGrid(PanelSyncManager syncManager, GTUtility.ItemId[] ores) {
        int buttonsPerRow = 10;
        int rowCount = (int) Math.ceil((double) ores.length / buttonsPerRow);
        return new ListWidget<>().children(rowCount, row -> {
            return new Row().children(Math.min(buttonsPerRow, ores.length - (row * buttonsPerRow)), rowIndex -> {
                int index = (row * buttonsPerRow) + rowIndex;
                ItemStack stack = ores[index].getItemStack();
                if (!(stack.getItem() instanceof GTItemOre ore)) return new EmptyWidget();
                return new ToggleButton()
                    .value(new BoolValue.Dynamic(() -> ItemStack.areItemStacksEqual(stack, multiblock.selected.getStackInSlot(index)), bool -> {
                        if (bool) {
                            multiblock.selected.insertItem(index, stack, false);
                        } else multiblock.selected.extractItem(index, 1, false);
                        syncManager.callSyncedAction("updateSelected", this::writeSelectedPacket);
                    }))
                    .overlay(new DynamicDrawable(() -> {
                        ItemStack rawOre = GTOreDictUnificator.get(OrePrefixes.rawOre, ore.blockOre.getMaterial(stack.getItemDamage()), 1);
                        if (matchesSearch(stack)) {
                            return new DrawableStack(
                                new Rectangle().setColor(Color.rgb(0, 255, 0)).asIcon().size(16),
                                new ItemDrawable(rawOre).asIcon().size(16));
                        } else {
                            return new ItemDrawable(rawOre).asIcon().size(16);
                        }
                    }))
                    .tooltip(t -> t.add(stack.getDisplayName()))
                    .size(18);
            }).collapseDisabledChild().coverChildren();
        }).crossAxisAlignment(Alignment.CrossAxis.START)
            .coverChildrenWidth()
            .height(18 * Math.min(rowCount, 8));
    }

    // On game launch the order in the multi's drop map randomizes, so we sort it by meta so the UI can be cohesive.
    private GTUtility.ItemId[] sortOres(VoidMinerUtility.DropMap dropMap) {
        return Arrays.stream(dropMap.getOres()).sorted((ore1, ore2) -> {
            return ore1.metaData() > ore2.metaData() ? 1 : -1;
        }).toArray(GTUtility.ItemId[]::new);
    }

    private boolean matchesSearch(ItemStack ore) {
        return !search.isEmpty() && ore.getDisplayName().toLowerCase().contains(search.toLowerCase());
    }

    private void writeSelectedPacket(PacketBuffer packet) {
        try {
            packet.writeNBTTagCompoundToBuffer(multiblock.selected.serializeNBT());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
