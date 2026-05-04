package kubatech.gui.modularui2;

import static forestry.api.apiculture.BeeManager.beeRoot;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.cleanroommc.modularui.ModularUI;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Alignment.CrossAxis;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.ItemSlotSH;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;

import codechicken.nei.LayoutManager;
import codechicken.nei.SearchField;
import forestry.api.apiculture.EnumBeeType;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.modularui2.widget.SlotLikeButtonWidget;
import kubatech.api.helpers.GTHelper;
import kubatech.tileentity.gregtech.multiblock.MTEMegaIndustrialApiary;
import kubatech.tileentity.gregtech.multiblock.MTEMegaIndustrialApiary.BeeSimulator;

public class MTEMegaIndustrialApiaryGui extends MTEMultiBlockBaseGui<MTEMegaIndustrialApiary> {

    private static final int SLOTS_PER_ROW = 9;

    private boolean isInInventory = true;
    private List<GTHelper.StackableItemSlot> beeSlots = new ArrayList<>();
    private int maxSlots = 0;
    private int usedSlots = 0;
    private IntSyncValue beeClickSyncer;
    private DynamicSyncHandler beeInventoryHandler;
    private PanelSyncManager mainSyncManager;

    public MTEMegaIndustrialApiaryGui(MTEMegaIndustrialApiary multiblock) {
        super(multiblock);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        setMachineModeIcons();
        registerSyncValues(syncManager);
        registerApiarySyncValues(syncManager);
        ModularPanel panel = getBasePanel(guiData, syncManager, uiSettings);
        return panel.child(createMainColumn(panel, syncManager));
    }

    protected void registerApiarySyncValues(PanelSyncManager syncManager) {
        this.mainSyncManager = syncManager;

        syncManager.syncValue(
            "apiaryPrimaryMode",
            new IntSyncValue(() -> multiblock.mPrimaryMode, val -> multiblock.mPrimaryMode = val));
        syncManager.syncValue(
            "apiarySecondaryMode",
            new IntSyncValue(() -> multiblock.mSecondaryMode, val -> multiblock.mSecondaryMode = val));
        syncManager.syncValue("apiaryMaxSlots", new IntSyncValue(() -> multiblock.mMaxSlots, val -> maxSlots = val));
        syncManager
            .syncValue("apiaryUsedSlots", new IntSyncValue(() -> multiblock.mStorage.size(), val -> usedSlots = val));

        beeClickSyncer = new IntSyncValue(() -> 0, this::handleBeeClick);
        syncManager.syncValue("beeClick", beeClickSyncer);

        GenericListSyncHandler<GTHelper.StackableItemSlot> beeListSyncer = createBeeListSyncer();
        syncManager.syncValue("apiaryBeeList", beeListSyncer);

        beeInventoryHandler = new DynamicSyncHandler().widgetProvider((sm, packet) -> {
            if (packet == null) return new EmptyWidget();
            return createBeeSlotGrid(packet.readInt());
        });

        if (!syncManager.isClient()) {
            beeListSyncer.setChangeListener(this::notifyBeeInventoryUpdate);
        }

        registerQueenBufferSlot(syncManager);
    }

    private GenericListSyncHandler<GTHelper.StackableItemSlot> createBeeListSyncer() {
        return new GenericListSyncHandler<>(this::buildAggregatedBeeList, val -> beeSlots = val, buffer -> {
            try {
                return GTHelper.StackableItemSlot.read(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, (buffer, slot) -> {
            try {
                slot.write(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        },
            (a, b) -> a.count == b.count && a.stack.isItemEqual(b.stack) && a.stack.stackSize == b.stack.stackSize,
            null);
    }

    private void registerQueenBufferSlot(PanelSyncManager syncManager) {
        ItemStackHandler queenBufferInv = new ItemStackHandler(1);
        ModularSlot queenBufferSlot = new ModularSlot(queenBufferInv, 0).filter(this::canAcceptQueen)
            .singletonSlotGroup(SlotGroup.STORAGE_SLOT_PRIO)
            .changeListener((newItem, onlyAmountChanged, client, init) -> {
                if (client || init || newItem == null) return;
                tryAddBeeToStorage(newItem.copy());
                queenBufferInv.setStackInSlot(0, null);
            });
        syncManager.syncValue("queenBuffer", new ItemSlotSH(queenBufferSlot));
    }

    private boolean canAcceptQueen(ItemStack stack) {
        return beeRoot.getType(stack) == EnumBeeType.QUEEN && multiblock.mMaxProgresstime == 0
            && multiblock.mStorage.size() < multiblock.mMaxSlots;
    }

    private void tryAddBeeToStorage(ItemStack queenStack) {
        World world = multiblock.getBaseMetaTileEntity()
            .getWorld();
        float voltageTier = (float) multiblock.getVoltageTierExact();
        BeeSimulator bee = new BeeSimulator(queenStack, world, voltageTier);
        if (bee.isValid) {
            multiblock.mStorage.add(bee);
            multiblock.onStorageContentChanged(false);
        }
    }

    private void notifyBeeInventoryUpdate() {
        int beeTypeCount = buildAggregatedBeeList().size();
        boolean hasEmptySlot = multiblock.mStorage.size() < multiblock.mMaxSlots;
        int activeCount = beeTypeCount + (hasEmptySlot ? 1 : 0);
        beeInventoryHandler.notifyUpdate(buf -> buf.writeInt(activeCount));
    }

    private List<GTHelper.StackableItemSlot> buildAggregatedBeeList() {
        HashMap<ItemId, Integer> countMap = new HashMap<>();
        HashMap<ItemId, ItemStack> stackMap = new HashMap<>();
        HashMap<ItemId, ArrayList<Integer>> realSlotMap = new HashMap<>();
        for (int i = 0; i < multiblock.mStorage.size(); i++) {
            ItemStack stack = multiblock.mStorage.get(i).queenStack;
            ItemId id = ItemId.createNoCopyWithStackSize(stack);
            countMap.merge(id, 1, Integer::sum);
            stackMap.putIfAbsent(id, stack);
            realSlotMap.computeIfAbsent(id, unused -> new ArrayList<>())
                .add(i);
        }
        List<GTHelper.StackableItemSlot> result = new ArrayList<>();
        for (Map.Entry<ItemId, Integer> entry : countMap.entrySet()) {
            result.add(
                new GTHelper.StackableItemSlot(
                    entry.getValue(),
                    stackMap.get(entry.getKey()),
                    realSlotMap.get(entry.getKey())));
        }
        return result;
    }

    @Override
    protected Flow createTerminalRow(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().size(getTerminalRowWidth(), getTerminalRowHeight())
            .child(
                new ParentWidget<>().size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
                    .paddingTop(4)
                    .paddingBottom(4)
                    .paddingLeft(4)
                    .paddingRight(0)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .child(
                        createTerminalTextWidget(syncManager, panel)
                            .size(getTerminalWidgetWidth() - 4, getTerminalWidgetHeight() - 8)
                            .collapseDisabledChild()
                            .setEnabledIf(w -> !isInInventory))
                    .child(
                        new ParentWidget<>().size(getTerminalWidgetWidth() - 4, getTerminalWidgetHeight() - 8)
                            .setEnabledIf(w -> isInInventory)
                            .child(createBeeInventoryWidget()))
                    .childIf(
                        multiblock.supportsTerminalRightCornerColumn(),
                        () -> createTerminalRightCornerColumn(panel, syncManager)));
    }

    private IWidget createBeeInventoryWidget() {
        return new DynamicSyncedWidget<>().widthRel(1f)
            .heightRel(1f)
            .syncHandler(beeInventoryHandler);
    }

    private IWidget createBeeSlotGrid(int activeCount) {
        if (activeCount <= 0) return new EmptyWidget();

        ListWidget<IWidget, ?> listWidget = new ListWidget<>().crossAxisAlignment(CrossAxis.START)
            .widthRel(1f)
            .heightRel(1f);

        for (int rowStart = 0; rowStart < activeCount; rowStart += SLOTS_PER_ROW) {
            Row row = new Row();
            row.coverChildren();
            int rowEnd = Math.min(rowStart + SLOTS_PER_ROW, activeCount);
            for (int j = rowStart; j < rowEnd; j++) {
                row.child(createBeeSlot(j));
            }
            listWidget.child(row);
        }

        return listWidget;
    }

    private IWidget createBeeSlot(int idx) {
        SlotLikeButtonWidget slb = new SlotLikeButtonWidget(() -> getBeeStack(idx)) {

            @Override
            public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetThemeEntry) {
                super.draw(context, widgetThemeEntry);
                int count = getBeeSlotCount(idx);
                if (count > 1) {
                    String format = isNEIFilteredOut(getBeeStack(idx)) ? EnumChatFormatting.GRAY.toString() : null;
                    GuiDraw.drawStandardSlotAmountText(count, format, getArea());
                }
            }
        };
        slb.size(18, 18);

        slb.onMousePressed(mouseButton -> {
            boolean shift = Interactable.hasShiftDown();
            int encoded = ((idx + 1) << 3) | (mouseButton << 1) | (shift ? 1 : 0);
            beeClickSyncer.setIntValue(encoded, true, true);
            return true;
        });

        slb.tooltipBuilder(t -> {
            t.setAutoUpdate(true);
            buildBeeSlotTooltip(t, idx);
        });

        return slb;
    }

    private ItemStack getBeeStack(int idx) {
        return idx < beeSlots.size() ? beeSlots.get(idx).stack : null;
    }

    private void buildBeeSlotTooltip(com.cleanroommc.modularui.screen.RichTooltip tooltip, int idx) {
        if (idx < beeSlots.size()) {
            GTHelper.StackableItemSlot slot = beeSlots.get(idx);
            tooltip.addLine(slot.stack.getDisplayName());
            if (slot.count > 1) {
                tooltip.addLine(
                    EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocalFormatted(
                        "kubatech.gui.tooltip.dynamic_inventory.identical_slots",
                        slot.count));
            }
        } else if (idx == beeSlots.size() && maxSlots > usedSlots) {
            tooltip.addLine(
                EnumChatFormatting.GRAY
                    + StatCollector.translateToLocal("kubatech.gui.tooltip.dynamic_inventory.empty_slot"));
            int remaining = maxSlots - usedSlots;
            if (remaining > 1) {
                tooltip.addLine(
                    EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocalFormatted(
                        "kubatech.gui.tooltip.dynamic_inventory.identical_slots",
                        remaining));
            }
        }
    }

    private static boolean isNEIFilteredOut(ItemStack item) {
        if (!ModularUI.Mods.NEI.isLoaded()) return false;
        if (!SearchField.searchInventories()) return false;
        if (item == null) return !codechicken.nei.NEIClientConfig.getSearchExpression()
            .isEmpty();
        return !LayoutManager.searchField.getFilter()
            .matches(item);
    }

    private int getBeeSlotCount(int idx) {
        if (idx < beeSlots.size()) return beeSlots.get(idx).count;
        return maxSlots - usedSlots;
    }

    private void handleBeeClick(int encoded) {
        if (encoded == 0) return;
        if (mainSyncManager == null || mainSyncManager.isClient()) return;
        if (multiblock.mMaxProgresstime > 0) return;

        int slotIdx = (encoded >>> 3) - 1;
        int mouseButton = (encoded >>> 1) & 0x3;
        boolean shift = (encoded & 1) != 0;

        EntityPlayer player = mainSyncManager.getPlayer();
        if (!(player instanceof EntityPlayerMP playerMP)) return;

        List<GTHelper.StackableItemSlot> serverSlots = buildAggregatedBeeList();

        if (slotIdx >= 0 && slotIdx < serverSlots.size()) {
            handleOccupiedSlotClick(serverSlots.get(slotIdx), mouseButton, shift, player, playerMP);
        } else {
            handleEmptySlotClick(mouseButton, player, playerMP);
        }
    }

    private void handleOccupiedSlotClick(GTHelper.StackableItemSlot serverSlot, int mouseButton, boolean shift,
        EntityPlayer player, EntityPlayerMP playerMP) {
        if (mouseButton == 2) {
            creativePickBee(serverSlot, player, playerMP);
        } else if (shift) {
            extractBeeToInventory(serverSlot, player, playerMP);
        } else if (player.inventory.getItemStack() != null) {
            replaceBeeWithHeldItem(serverSlot, player, playerMP);
        } else {
            extractBeeToCursor(serverSlot, player, playerMP);
        }
    }

    private void creativePickBee(GTHelper.StackableItemSlot serverSlot, EntityPlayer player, EntityPlayerMP playerMP) {
        if (!player.capabilities.isCreativeMode || player.inventory.getItemStack() != null) return;
        int realID = serverSlot.realSlots.get(0);
        if (realID >= multiblock.mStorage.size()) return;
        ItemStack stack = multiblock.mStorage.get(realID).queenStack.copy();
        stack.stackSize = stack.getMaxStackSize();
        player.inventory.setItemStack(stack);
        playerMP.isChangingQuantityOnly = false;
        playerMP.updateHeldItem();
    }

    private void extractBeeToInventory(GTHelper.StackableItemSlot serverSlot, EntityPlayer player,
        EntityPlayerMP playerMP) {
        int realID = serverSlot.realSlots.get(0);
        if (realID >= multiblock.mStorage.size()) return;
        BeeSimulator removed = multiblock.mStorage.remove(realID);
        multiblock.onStorageContentChanged(false);
        if (removed == null) return;
        if (player.inventory.addItemStackToInventory(removed.queenStack)) {
            player.inventoryContainer.detectAndSendChanges();
        } else {
            player.entityDropItem(removed.queenStack, 0.f);
        }
    }

    private void replaceBeeWithHeldItem(GTHelper.StackableItemSlot serverSlot, EntityPlayer player,
        EntityPlayerMP playerMP) {
        ItemStack input = player.inventory.getItemStack();
        if (input.stackSize != 1) return;
        int realID = serverSlot.realSlots.get(0);
        if (realID >= multiblock.mStorage.size()) return;
        World world = multiblock.getBaseMetaTileEntity()
            .getWorld();
        float voltageTier = (float) multiblock.getVoltageTierExact();
        BeeSimulator bee = new BeeSimulator(input, world, voltageTier);
        if (!bee.isValid) return;
        BeeSimulator removed = multiblock.mStorage.remove(realID);
        multiblock.mStorage.add(realID, bee);
        multiblock.onStorageContentChanged(false);
        player.inventory.setItemStack(removed.queenStack);
        playerMP.isChangingQuantityOnly = false;
        playerMP.updateHeldItem();
    }

    private void extractBeeToCursor(GTHelper.StackableItemSlot serverSlot, EntityPlayer player,
        EntityPlayerMP playerMP) {
        int realID = serverSlot.realSlots.get(0);
        if (realID >= multiblock.mStorage.size()) return;
        BeeSimulator removed = multiblock.mStorage.remove(realID);
        multiblock.onStorageContentChanged(false);
        if (removed == null) return;
        player.inventory.setItemStack(removed.queenStack);
        playerMP.isChangingQuantityOnly = false;
        playerMP.updateHeldItem();
    }

    private void handleEmptySlotClick(int mouseButton, EntityPlayer player, EntityPlayerMP playerMP) {
        ItemStack input = player.inventory.getItemStack();
        if (input == null) return;
        if (multiblock.mStorage.size() >= multiblock.mMaxSlots) return;
        if (beeRoot.getType(input) != EnumBeeType.QUEEN) return;

        if (mouseButton == 1) {
            injectSingleBee(input, player, playerMP);
        } else {
            injectEntireStack(input, player, playerMP);
        }
    }

    private void injectSingleBee(ItemStack input, EntityPlayer player, EntityPlayerMP playerMP) {
        ItemStack singleQueen = input.copy();
        singleQueen.stackSize = 1;
        if (!tryAddBeeToStorage(singleQueen, player)) return;
        input.stackSize--;
        if (input.stackSize <= 0) {
            player.inventory.setItemStack(null);
        }
        playerMP.isChangingQuantityOnly = input.stackSize > 0;
        playerMP.updateHeldItem();
    }

    private void injectEntireStack(ItemStack input, EntityPlayer player, EntityPlayerMP playerMP) {
        if (!tryAddBeeToStorage(input, player)) return;
        if (input.stackSize > 0) {
            playerMP.isChangingQuantityOnly = true;
            playerMP.updateHeldItem();
            return;
        }
        player.inventory.setItemStack(null);
        playerMP.isChangingQuantityOnly = false;
        playerMP.updateHeldItem();
    }

    private boolean tryAddBeeToStorage(ItemStack queenStack, EntityPlayer player) {
        World world = multiblock.getBaseMetaTileEntity()
            .getWorld();
        float voltageTier = (float) multiblock.getVoltageTierExact();
        BeeSimulator bee = new BeeSimulator(queenStack, world, voltageTier);
        if (!bee.isValid) return false;
        multiblock.mStorage.add(bee);
        multiblock.onStorageContentChanged(false);
        return true;
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(parent, syncManager).child(createInventoryToggleButton())
            .child(createConfigurationButton(syncManager, parent));
    }

    private IWidget createInventoryToggleButton() {
        return new ButtonWidget<>().size(18, 18)
            .overlay(
                new DynamicDrawable(
                    () -> isInInventory ? GTGuiTextures.OVERLAY_BUTTON_WHITELIST
                        : GTGuiTextures.OVERLAY_BUTTON_BLACKLIST))
            .onMousePressed(button -> {
                isInInventory = !isInInventory;
                return true;
            })
            .tooltipBuilder(
                t -> t.addLine(
                    IKey.dynamic(
                        () -> isInInventory ? StatCollector.translateToLocal("kubatech.gui.text.inventory")
                            : StatCollector.translateToLocal("kubatech.gui.text.status"))))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private IWidget createConfigurationButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler configPanel = syncManager.syncedPanel(
            "apiaryConfigPanel",
            true,
            (p_syncManager, syncHandler) -> createConfigurationPanel(p_syncManager, parent, syncManager));
        return new ButtonWidget<>().size(18, 18)
            .overlay(GuiTextures.GEAR)
            .onMousePressed(d -> {
                if (!configPanel.isPanelOpen()) {
                    configPanel.openPanel();
                } else {
                    configPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("kubatech.gui.text.configuration")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel createConfigurationPanel(PanelSyncManager p_syncManager, ModularPanel parent,
        PanelSyncManager mainSyncManager) {
        IntSyncValue primaryModeSyncer = mainSyncManager.findSyncHandler("apiaryPrimaryMode", IntSyncValue.class);
        IntSyncValue secondaryModeSyncer = mainSyncManager.findSyncHandler("apiarySecondaryMode", IntSyncValue.class);

        return new ModularPanel("apiaryConfigPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(200, 100)
            .widgetTheme("backgroundPopup")
            .child(
                new Column().sizeRel(1)
                    .padding(5)
                    .child(
                        new TextWidget<>(
                            EnumChatFormatting.UNDERLINE
                                + StatCollector.translateToLocal("kubatech.gui.text.configuration"))
                                    .alignment(Alignment.Center)
                                    .height(18)
                                    .marginBottom(4))
                    .child(
                        createModeRow(
                            primaryModeSyncer,
                            "kubatech.gui.text.mia.primary_mode",
                            3,
                            MTEMegaIndustrialApiaryGui::getPrimaryModeText))
                    .child(
                        createModeRow(
                            secondaryModeSyncer,
                            "kubatech.gui.text.mia.secondary_mode",
                            2,
                            MTEMegaIndustrialApiaryGui::getSecondaryModeText))
                    .child(
                        IKey.str(
                            EnumChatFormatting.RED
                                + StatCollector.translateToLocal("GT5U.gui.text.cannot_change_when_running"))
                            .asWidget()
                            .widthRel(1)
                            .height(18)
                            .setEnabledIf(w -> multiblock.mMaxProgresstime > 0)));
    }

    private IWidget createModeRow(IntSyncValue modeSyncer, String labelKey, int cycleLength,
        java.util.function.IntFunction<String> modeTextProvider) {
        return new Row().widthRel(1)
            .height(18)
            .marginBottom(2)
            .setEnabledIf(w -> multiblock.mMaxProgresstime == 0)
            .child(
                new TextWidget<>(StatCollector.translateToLocal(labelKey)).width(100)
                    .height(18))
            .child(
                new CycleButtonWidget().length(cycleLength)
                    .value(modeSyncer)
                    .overlay(
                        new DynamicDrawable(
                            () -> IKey.str(modeTextProvider.apply(modeSyncer.getIntValue()))
                                .alignment(Alignment.Center)))
                    .tooltipBuilder(t -> t.addLine(modeTextProvider.apply(modeSyncer.getIntValue())))
                    .size(70, 18));
    }

    private static String getPrimaryModeText(int mode) {
        return switch (mode) {
            case 0 -> StatCollector.translateToLocal("kubatech.gui.text.input");
            case 1 -> StatCollector.translateToLocal("kubatech.gui.text.output");
            case 2 -> StatCollector.translateToLocal("kubatech.gui.text.operating");
            default -> "";
        };
    }

    private static String getSecondaryModeText(int mode) {
        return switch (mode) {
            case 0 -> StatCollector.translateToLocal("kubatech.gui.text.mia.normal");
            case 1 -> StatCollector.translateToLocal("kubatech.gui.text.mia.swarmer");
            default -> "";
        };
    }
}
