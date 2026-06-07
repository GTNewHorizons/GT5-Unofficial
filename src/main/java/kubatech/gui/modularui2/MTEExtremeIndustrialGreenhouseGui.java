package kubatech.gui.modularui2;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Alignment.CrossAxis;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.ItemSlotSH;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.modularui2.widget.SlotLikeButtonWidget;
import kubatech.api.helpers.GTHelper;
import kubatech.api.implementations.KubaTechGTMultiBlockBaseGUI;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;

public class MTEExtremeIndustrialGreenhouseGui extends KubaTechGTMultiBlockBaseGUI<MTEExtremeIndustrialGreenhouse> {

    private static final int SLOTS_PER_ROW = 7;
    private static final int WARNING_ANIM_FRAMES = 63;
    private static final int WARNING_ANIM_TICK_MS = 50;
    private static final UITexture OVERLAY_INVENTORY = UITexture.builder()
        .canApplyTheme()
        .location("kubatech", "gui/overlay_button/bee_list")
        .build();
    private static final UITexture OVERLAY_INVENTORY_FULL_WARNING_SHEET = UITexture.builder()
        .canApplyTheme()
        .location("kubatech", "gui/icons/inventory_full_warning")
        .build();

    private boolean isInInventory = false;
    private List<GTHelper.StackableItemSlot> seedSlots = new ArrayList<>();
    private int maxSeedTypes = 0;
    private int maxSeedCount = 0;
    private int usedSeedTypes = 0;
    private int usedSeedCount = 0;
    private boolean machineActive = false;
    private IntSyncValue seedClickSyncer;
    private DynamicSyncHandler seedInventoryHandler;
    private PanelSyncManager mainSyncManager;

    public MTEExtremeIndustrialGreenhouseGui(MTEExtremeIndustrialGreenhouse multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        registerEIGSyncValues(syncManager);
    }

    private void registerEIGSyncValues(PanelSyncManager syncManager) {
        this.mainSyncManager = syncManager;

        syncManager.syncValue(
            "eigSetupPhase",
            new IntSyncValue(() -> multiblock.getSetupPhase(), val -> multiblock.setSetupPhase(val)).allowC2S());
        syncManager.syncValue(
            "eigMode",
            new IntSyncValue(
                () -> multiblock.getEIGMode()
                    .getUIIndex(),
                val -> multiblock.setModeByUIIndex(val)).allowC2S());
        syncManager.syncValue(
            "eigNoHumidity",
            new BooleanSyncValue(() -> multiblock.isInNoHumidityMode(), val -> multiblock.setNoHumidity(val))
                .allowC2S());

        IntSyncValue maxSeedTypesSyncer = new IntSyncValue(
            () -> multiblock.getMaxSeedTypes(),
            val -> maxSeedTypes = val);
        syncManager.syncValue("eigMaxSeedTypes", maxSeedTypesSyncer);

        IntSyncValue maxSeedCountSyncer = new IntSyncValue(
            () -> multiblock.getMaxSeedCount(),
            val -> maxSeedCount = val);
        syncManager.syncValue("eigMaxSeedCount", maxSeedCountSyncer);

        IntSyncValue usedSeedTypesSyncer = new IntSyncValue(
            () -> multiblock.buckets.size(),
            val -> usedSeedTypes = val);
        syncManager.syncValue("eigUsedSeedTypes", usedSeedTypesSyncer);

        IntSyncValue usedSeedCountSyncer = new IntSyncValue(
            () -> multiblock.getTotalSeedCount(),
            val -> usedSeedCount = val);
        syncManager.syncValue("eigUsedSeedCount", usedSeedCountSyncer);

        syncManager.syncValue(
            "eigActive",
            new BooleanSyncValue(
                () -> multiblock.getBaseMetaTileEntity()
                    .isActive(),
                val -> machineActive = val));

        seedClickSyncer = new IntSyncValue(() -> 0, this::handleSeedClick).allowC2S();
        syncManager.syncValue("eigSeedClick", seedClickSyncer);

        GenericListSyncHandler<GTHelper.StackableItemSlot> seedListSyncer = new GenericListSyncHandler<>(
            this::buildSeedSlotList,
            val -> seedSlots = val,
            GTHelper.StackableItemSlot::read,
            (buffer, slot) -> slot.write(buffer),
            (a, b) -> a.count == b.count && ItemStack.areItemStacksEqual(a.stack, b.stack),
            null);
        syncManager.syncValue("eigSeedList", seedListSyncer);

        seedInventoryHandler = new DynamicSyncHandler().widgetProvider((sm, packet) -> {
            if (packet == null) return new EmptyWidget();
            return createSeedSlotGrid(packet.readInt());
        });

        if (!syncManager.isClient()) {
            seedListSyncer.setChangeListener(
                () -> notifySeedInventoryUpdate(
                    seedListSyncer.getValue()
                        .size()));
            maxSeedTypesSyncer.setChangeListener(() -> notifySeedInventoryUpdate(buildSeedSlotList().size()));
            maxSeedCountSyncer.setChangeListener(() -> notifySeedInventoryUpdate(buildSeedSlotList().size()));
            usedSeedTypesSyncer.setChangeListener(() -> notifySeedInventoryUpdate(buildSeedSlotList().size()));
            usedSeedCountSyncer.setChangeListener(() -> notifySeedInventoryUpdate(buildSeedSlotList().size()));
        }

        registerSeedBufferSlot(syncManager);
    }

    private void registerSeedBufferSlot(PanelSyncManager syncManager) {
        ItemStackHandler seedBufferInv = new ItemStackHandler(1);
        ModularSlot seedBufferSlot = new ModularSlot(seedBufferInv, 0).filter(this::canAcceptSeed)
            .singletonSlotGroup(SlotGroup.STORAGE_SLOT_PRIO)
            .changeListener((newItem, onlyAmountChanged, client, init) -> {
                if (client || init || newItem == null) return;
                ItemStack toAdd = newItem.copy();
                multiblock.addCrop(toAdd);
                seedBufferInv.setStackInSlot(0, null);
                if (toAdd.stackSize > 0) {
                    EntityPlayer player = mainSyncManager.getPlayer();
                    if (!player.inventory.addItemStackToInventory(toAdd)) {
                        player.entityDropItem(toAdd, 0.f);
                    }
                    player.inventoryContainer.detectAndSendChanges();
                }
            });
        syncManager.syncValue("seedBuffer", new ItemSlotSH(seedBufferSlot));
    }

    private boolean canAcceptSeed(ItemStack stack) {
        if (multiblock.mMaxProgresstime > 0) return false;
        return multiblock.getTotalSeedCount() < multiblock.getMaxSeedCount();
    }

    private List<GTHelper.StackableItemSlot> buildSeedSlotList() {
        List<GTHelper.StackableItemSlot> result = new ArrayList<>();
        for (int i = 0; i < multiblock.buckets.size(); i++) {
            var bucket = multiblock.buckets.get(i);
            if (bucket == null) continue;
            ItemStack stack = bucket.getSeedStack();
            if (stack == null) continue;
            result.add(new GTHelper.StackableItemSlot(bucket.getSeedCount(), stack, new ArrayList<>(List.of(i))));
        }
        return result;
    }

    private void notifySeedInventoryUpdate(int listSize) {
        // Read directly from the multiblock, not from local sync fields (which are only set on the client)
        boolean hasEmpty = multiblock.getTotalSeedCount() < multiblock.getMaxSeedCount();
        int activeCount = listSize + (hasEmpty ? 1 : 0);
        seedInventoryHandler.notifyUpdate(buf -> buf.writeInt(activeCount));
    }

    @Override
    protected ParentWidget<?> createTerminalParentWidget(ModularPanel panel, PanelSyncManager syncManager) {
        return new ParentWidget<>().size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
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
                Flow.column()
                    .size(getTerminalWidgetWidth() - 4, getTerminalWidgetHeight() - 8)
                    .setEnabledIf(w -> isInInventory)
                    .child(createSeedInventoryWidget()))
            .childIf(
                multiblock.supportsTerminalRightCornerColumn(),
                () -> createTerminalRightCornerColumn(panel, syncManager));
    }

    @Override
    protected Flow createTerminalRightCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.column()
            .coverChildren()
            .rightRel(0, 6, 0)
            .bottomRel(0, 6, 0)
            .child(createInventoryFullWarning())
            .childIf(
                multiblock.supportsShutdownReasonHoverable(),
                () -> createShutdownReasonHoverableTerminal(syncManager))
            .childIf(
                multiblock.supportsMaintenanceIssueHoverable(),
                () -> createMaintIssueHoverableTerminal(syncManager))
            .childIf(multiblock.supportsLogo(), () -> makeLogoWidget(syncManager, panel));
    }

    private IWidget createInventoryFullWarning() {
        return new DynamicDrawable(() -> {
            if (maxSeedCount <= 0 || usedSeedCount < maxSeedCount) return IDrawable.EMPTY;
            int frame = (int) ((System.currentTimeMillis() / WARNING_ANIM_TICK_MS) % WARNING_ANIM_FRAMES);
            float v0 = (float) frame / WARNING_ANIM_FRAMES;
            float v1 = (float) (frame + 1) / WARNING_ANIM_FRAMES;
            return OVERLAY_INVENTORY_FULL_WARNING_SHEET.getSubArea(0f, v0, 1f, v1);
        }).asWidget()
            .size(18, 18)
            .marginBottom(4)
            .tooltipBuilder(t -> t.addLine(IKey.lang("kubatech.gui.text.eig.inventory_full_warning")))
            .tooltipAutoUpdate(true)
            .setEnabledIf(w -> maxSeedCount > 0 && usedSeedCount >= maxSeedCount);
    }

    @Override
    protected IWidget createMaintIssueHoverableTerminal(PanelSyncManager syncManager) {
        return ((Widget<?>) super.createMaintIssueHoverableTerminal(syncManager)).marginTop(1);
    }

    private IWidget createSeedInventoryWidget() {
        return new DynamicSyncedWidget<>().widthRel(1f)
            .expanded()
            .syncHandler(seedInventoryHandler);
    }

    private IWidget createSeedSlotGrid(int activeCount) {
        if (activeCount <= 0) return new EmptyWidget();

        ListWidget<IWidget, ?> listWidget = new ListWidget<>().crossAxisAlignment(CrossAxis.START)
            .widthRel(1f)
            .heightRel(1f);

        for (int rowStart = 0; rowStart < activeCount; rowStart += SLOTS_PER_ROW) {
            Flow row = Flow.row()
                .coverChildren();
            int rowEnd = Math.min(rowStart + SLOTS_PER_ROW, activeCount);
            for (int j = rowStart; j < rowEnd; j++) {
                row.child(createSeedSlot(j));
            }
            listWidget.child(row);
        }
        return listWidget;
    }

    private IWidget createSeedSlot(int idx) {
        SlotLikeButtonWidget slb = new SlotLikeButtonWidget(() -> getSeedStack(idx)) {

            @Override
            public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetThemeEntry) {
                super.draw(context, widgetThemeEntry);
                int count = getSeedSlotCount(idx);
                boolean isEmptySlot = idx >= seedSlots.size();
                if (count > 1 || isEmptySlot) {
                    GuiDraw.drawStandardSlotAmountText(count, null, getArea());
                }
                if (machineActive) {
                    GuiDraw.drawRect(0, 0, 18, 18, 0x80000000);
                }
            }
        };

        slb.onMousePressed(mouseButton -> {
            if (machineActive) return true;
            boolean shift = Interactable.hasShiftDown();
            int encoded = ((idx + 1) << 3) | (mouseButton << 1) | (shift ? 1 : 0);
            seedClickSyncer.setIntValue(encoded, true, true);
            return true;
        });

        slb.tooltipBuilder(t -> {
            t.setAutoUpdate(true);
            buildSeedSlotTooltip(t, idx);
        });

        return slb;
    }

    private ItemStack getSeedStack(int idx) {
        return idx < seedSlots.size() ? seedSlots.get(idx).stack : null;
    }

    private int getSeedSlotCount(int idx) {
        if (idx < seedSlots.size()) return seedSlots.get(idx).count;
        return maxSeedCount - usedSeedCount;
    }

    private void buildSeedSlotTooltip(com.cleanroommc.modularui.screen.RichTooltip tooltip, int idx) {
        if (machineActive) {
            tooltip.addLine(
                EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.gui.text.cannot_change_when_running"));
            return;
        }
        if (idx < seedSlots.size()) {
            GTHelper.StackableItemSlot slot = seedSlots.get(idx);
            tooltip.addLine(slot.stack.getDisplayName());
            tooltip.addLine(
                EnumChatFormatting.GREEN + StatCollector
                    .translateToLocalFormatted("kubatech.gui.tooltip.dynamic_inventory.eig.seed_count", slot.count));
        } else if (idx == seedSlots.size() && maxSeedCount > usedSeedCount) {
            tooltip.addLine(
                EnumChatFormatting.GRAY
                    + StatCollector.translateToLocal("kubatech.gui.tooltip.dynamic_inventory.empty_slot"));
            tooltip.addLine(
                EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocalFormatted(
                    "kubatech.gui.tooltip.dynamic_inventory.eig.remaining_seed_types",
                    (maxSeedTypes - usedSeedTypes)));
            tooltip.addLine(
                EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocalFormatted(
                    "kubatech.gui.tooltip.dynamic_inventory.eig.remaining_seed_capacity",
                    (maxSeedCount - usedSeedCount)));
        }
    }

    private void handleSeedClick(int encoded) {
        if (encoded == 0) return;
        if (mainSyncManager == null || mainSyncManager.isClient()) return;
        if (multiblock.mMaxProgresstime > 0) return;

        int slotIdx = (encoded >>> 3) - 1;
        int mouseButton = (encoded >>> 1) & 0x3;
        boolean shift = (encoded & 1) != 0;

        EntityPlayer player = mainSyncManager.getPlayer();
        if (!(player instanceof EntityPlayerMP playerMP)) return;

        // Use multiblock.buckets directly since seedSlots is only populated on the client
        if (slotIdx >= 0 && slotIdx < multiblock.buckets.size()) {
            handleOccupiedSeedClick(slotIdx, mouseButton, shift, player, playerMP);
        } else {
            handleEmptySeedClick(mouseButton, player, playerMP);
        }
    }

    private void handleOccupiedSeedClick(int slotIdx, int mouseButton, boolean shift, EntityPlayer player,
        EntityPlayerMP playerMP) {
        if (slotIdx >= multiblock.buckets.size()) return;

        if (mouseButton == 2) {
            // Creative pick
            if (!player.capabilities.isCreativeMode || player.inventory.getItemStack() != null) return;
            var bucket = multiblock.buckets.get(slotIdx);
            ItemStack stack = bucket.getSeedStack()
                .copy();
            stack.stackSize = stack.getMaxStackSize();
            player.inventory.setItemStack(stack);
            playerMP.isChangingQuantityOnly = false;
            playerMP.updateHeldItem();
        } else if (shift) {
            // Extract to inventory
            var bucket = multiblock.buckets.get(slotIdx);
            int maxRemove = bucket.getSeedStack()
                .getMaxStackSize();
            ItemStack[] outputs = bucket.tryRemoveSeed(maxRemove, false);
            if (outputs == null || outputs.length == 0) return;
            if (bucket.getSeedCount() <= 0) multiblock.buckets.remove(slotIdx);
            for (ItemStack output : outputs) {
                if (!player.inventory.addItemStackToInventory(output)) {
                    player.entityDropItem(output, 0.f);
                }
            }
            player.inventoryContainer.detectAndSendChanges();
        } else {
            ItemStack input = player.inventory.getItemStack();
            if (input != null) {
                // Try to inject held item
                multiblock.addCrop(input);
                if (input.stackSize <= 0) {
                    player.inventory.setItemStack(null);
                }
                playerMP.isChangingQuantityOnly = false;
                playerMP.updateHeldItem();
            } else {
                // Extract to cursor
                var bucket = multiblock.buckets.get(slotIdx);
                int maxRemove = bucket.getSeedStack()
                    .getMaxStackSize();
                ItemStack[] outputs = bucket.tryRemoveSeed(maxRemove, false);
                if (outputs == null || outputs.length == 0) return;
                if (bucket.getSeedCount() <= 0) multiblock.buckets.remove(slotIdx);
                player.inventory.setItemStack(outputs[0]);
                for (int i = 1; i < outputs.length; i++) {
                    if (!player.inventory.addItemStackToInventory(outputs[i])) {
                        player.entityDropItem(outputs[i], 0.f);
                    }
                }
                playerMP.isChangingQuantityOnly = false;
                playerMP.updateHeldItem();
            }
        }
    }

    private void handleEmptySeedClick(int mouseButton, EntityPlayer player, EntityPlayerMP playerMP) {
        ItemStack input = player.inventory.getItemStack();
        if (input == null) return;

        if (mouseButton == 1) {
            // Inject single
            ItemStack single = input.copy();
            single.stackSize = 1;
            multiblock.addCrop(single);
            if (single.stackSize <= 0) {
                input.stackSize--;
                if (input.stackSize <= 0) {
                    player.inventory.setItemStack(null);
                }
            }
        } else {
            // Inject entire stack
            multiblock.addCrop(input);
            if (input.stackSize <= 0) {
                player.inventory.setItemStack(null);
            }
        }
        playerMP.isChangingQuantityOnly = false;
        playerMP.updateHeldItem();
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(parent, syncManager).child(createInventoryToggleButton())
            .child(createConfigurationButton(syncManager, parent));
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.column()
            .width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(Alignment.MainAxis.END)
            .reverseLayout(true)
            .child(createPowerSwitchButton())
            .child(createStructureUpdateButton(syncManager));
    }

    @Override
    protected boolean shouldDisplayRecipeLock() {
        return false;
    }

    private IWidget createInventoryToggleButton() {
        return new ButtonWidget<>()
            .overlay(
                new DynamicDrawable(() -> isInInventory ? GTGuiTextures.OVERLAY_BUTTON_WHITELIST : OVERLAY_INVENTORY))
            .onMousePressed(button -> {
                isInInventory = !isInInventory;
                return true;
            })
            .tooltipBuilder(
                t -> t.addLine(
                    IKey.dynamic(
                        () -> isInInventory ? StatCollector.translateToLocal("kubatech.gui.text.status")
                            : StatCollector.translateToLocal("kubatech.gui.text.inventory"))))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private IWidget createConfigurationButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler configPanel = syncManager.syncedPanel(
            "eigConfigPanel",
            true,
            (p_syncManager, syncHandler) -> createConfigurationPanel(p_syncManager, parent, syncManager));
        return new ButtonWidget<>().overlay(GuiTextures.GEAR)
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
        IntSyncValue setupPhaseSyncer = mainSyncManager.findSyncHandler("eigSetupPhase", IntSyncValue.class);
        IntSyncValue modeSyncer = mainSyncManager.findSyncHandler("eigMode", IntSyncValue.class);
        BooleanSyncValue humiditySyncer = mainSyncManager.findSyncHandler("eigNoHumidity", BooleanSyncValue.class);

        return new ModularPanel("eigConfigPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(110, 100)
            .widgetTheme("backgroundPopup")
            .child(
                Flow.column()
                    .sizeRel(1)
                    .padding(4)
                    .child(
                        new TextWidget<>(
                            EnumChatFormatting.UNDERLINE
                                + StatCollector.translateToLocal("kubatech.gui.text.configuration"))
                                    .alignment(Alignment.Center)
                                    .height(10)
                                    .marginBottom(2))
                    .child(
                        createConfigEntry(
                            setupPhaseSyncer,
                            "kubatech.gui.text.eig.setup_mode",
                            3,
                            MTEExtremeIndustrialGreenhouseGui::getSetupPhaseText))
                    .child(
                        createConfigEntry(
                            modeSyncer,
                            "kubatech.gui.text.eig.ic2_mode",
                            2,
                            MTEExtremeIndustrialGreenhouseGui::getModeText))
                    .child(createHumidityEntry(humiditySyncer)));
    }

    private IWidget createConfigEntry(IntSyncValue syncer, String labelKey, int cycleLength,
        java.util.function.IntFunction<String> textProvider) {
        return Flow.column()
            .widthRel(1)
            .coverChildrenHeight()
            .crossAxisAlignment(CrossAxis.START)
            .marginBottom(1)
            .child(
                new TextWidget<>(StatCollector.translateToLocal(labelKey)).widthRel(1)
                    .height(9)
                    .marginBottom(1))
            .child(new ButtonWidget<>().overlay(new DynamicDrawable(() -> {
                IKey key = IKey.str(textProvider.apply(syncer.getIntValue()))
                    .alignment(Alignment.Center);
                return multiblock.mMaxProgresstime > 0 ? key.color(0xFFA0A0A0) : key;
            }))
                .onMousePressed(mouseButton -> {
                    if (multiblock.mMaxProgresstime > 0) return true;
                    int current = syncer.getIntValue();
                    int next = mouseButton == 1 ? (current - 1 + cycleLength) % cycleLength
                        : (current + 1) % cycleLength;
                    syncer.setIntValue(next, true, true);
                    return true;
                })
                .tooltipBuilder(t -> {
                    t.setAutoUpdate(true);
                    t.addLine(textProvider.apply(syncer.getIntValue()));
                    if (multiblock.mMaxProgresstime > 0) {
                        t.addLine(
                            EnumChatFormatting.RED
                                + StatCollector.translateToLocal("GT5U.gui.text.cannot_change_when_running"));
                    }
                })
                .width(75)
                .height(12)
                .marginBottom(1));
    }

    private IWidget createHumidityEntry(BooleanSyncValue syncer) {
        return Flow.column()
            .widthRel(1)
            .coverChildrenHeight()
            .crossAxisAlignment(CrossAxis.START)
            .marginBottom(1)
            .child(
                new TextWidget<>(StatCollector.translateToLocal("kubatech.gui.text.eig.no_humidity_mode")).widthRel(1)
                    .height(9)
                    .marginBottom(1))
            .child(new ButtonWidget<>().overlay(new DynamicDrawable(() -> {
                String text = syncer.getBoolValue() ? StatCollector.translateToLocal("kubatech.gui.text.eig.enabled")
                    : StatCollector.translateToLocal("kubatech.gui.text.eig.disabled");
                IKey key = IKey.str(text)
                    .alignment(Alignment.Center);
                return multiblock.mMaxProgresstime > 0 ? key.color(0xFFA0A0A0) : key;
            }))
                .onMousePressed(mouseButton -> {
                    if (multiblock.mMaxProgresstime > 0) return true;
                    syncer.setBoolValue(!syncer.getBoolValue(), true, true);
                    return true;
                })
                .tooltipBuilder(t -> {
                    t.setAutoUpdate(true);
                    t.addLine(
                        syncer.getBoolValue() ? StatCollector.translateToLocal("kubatech.gui.text.eig.enabled")
                            : StatCollector.translateToLocal("kubatech.gui.text.eig.disabled"));
                    if (multiblock.mMaxProgresstime > 0) {
                        t.addLine(
                            EnumChatFormatting.RED
                                + StatCollector.translateToLocal("GT5U.gui.text.cannot_change_when_running"));
                    }
                })
                .width(75)
                .height(12)
                .marginBottom(1));
    }

    private static String getSetupPhaseText(int phase) {
        return switch (phase) {
            case 0 -> StatCollector.translateToLocal("kubatech.gui.text.operating");
            case 1 -> StatCollector.translateToLocal("kubatech.gui.text.input");
            case 2 -> StatCollector.translateToLocal("kubatech.gui.text.output");
            default -> "";
        };
    }

    private static String getModeText(int mode) {
        return switch (mode) {
            case 0 -> StatCollector.translateToLocal("kubatech.gui.text.eig.disabled");
            case 1 -> StatCollector.translateToLocal("kubatech.gui.text.eig.enabled");
            default -> "";
        };
    }
}
