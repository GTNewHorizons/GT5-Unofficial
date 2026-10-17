package kubatech.gui.modularui2;

import static forestry.api.apiculture.BeeManager.beeRoot;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.ModularUI;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
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

import codechicken.nei.LayoutManager;
import codechicken.nei.SearchField;
import forestry.api.apiculture.EnumBeeType;
import gregtech.api.enums.Mods;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.modularui2.widget.SlotLikeButtonWidget;
import kubatech.api.helpers.GTHelper;
import kubatech.tileentity.gregtech.multiblock.MTEMegaIndustrialApiary;
import kubatech.tileentity.gregtech.multiblock.MTEMegaIndustrialApiary.BeeGroup;

public class MTEMegaIndustrialApiaryGui extends MTEMultiBlockBaseGui<MTEMegaIndustrialApiary> {

    private static final int SLOTS_PER_ROW = 9;
    private static final int BEE_ENTRIES_PER_PAGE = 36;
    private static final int WARNING_ANIM_FRAMES = 63;
    private static final int WARNING_ANIM_TICK_MS = 50;
    private static final UITexture OVERLAY_INVENTORY_FULL_WARNING_SHEET = UITexture
        .fullImage(Mods.ModIDs.KUBA_TECH, "gui/icons/inventory_full_warning");
    private static final UITexture OVERLAY_BEE_LIST = UITexture
        .fullImage(Mods.ModIDs.KUBA_TECH, "gui/overlay_button/bee_list");

    private boolean isInInventory = true;
    private List<GTHelper.StackableItemSlot> beeSlots = new ArrayList<>();
    private int maxSlots = 0;
    private int usedSlots = 0;
    private boolean machineRunning = false;
    private IntSyncValue beeClickSyncer;
    private DynamicSyncHandler beeInventoryHandler;
    private PanelSyncManager mainSyncManager;
    private int beePage = 0;
    private int beePageCount = 1;
    private IntSyncValue beePageSyncer;
    private List<GTHelper.StackableItemSlot> cachedFullBeeList;
    private List<BeeGroup> cachedSlotGroups = new ArrayList<>();
    private int cachedStorageVersion = -1;

    public MTEMegaIndustrialApiaryGui(MTEMegaIndustrialApiary multiblock) {
        super(multiblock);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        isInInventory = multiblock.getBaseMetaTileEntity()
            .isActive();
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
            new IntSyncValue(() -> multiblock.mPrimaryMode, val -> multiblock.mPrimaryMode = val).allowC2S());
        syncManager.syncValue(
            "apiarySecondaryMode",
            new IntSyncValue(() -> multiblock.mSecondaryMode, val -> multiblock.mSecondaryMode = val).allowC2S());

        IntSyncValue maxSlotsSyncer = new IntSyncValue(() -> multiblock.mMaxSlots, val -> maxSlots = val);
        syncManager.syncValue("apiaryMaxSlots", maxSlotsSyncer);

        IntSyncValue usedSlotsSyncer = new IntSyncValue(multiblock::getBeeCount, val -> usedSlots = val);
        syncManager.syncValue("apiaryUsedSlots", usedSlotsSyncer);

        beeClickSyncer = new IntSyncValue(() -> 0, this::handleBeeClick).allowC2S();
        syncManager.syncValue("beeClick", beeClickSyncer);

        beePageSyncer = new IntSyncValue(() -> beePage, val -> beePage = val).allowC2S();
        syncManager.syncValue("apiaryBeePage", beePageSyncer);

        syncManager.syncValue("apiaryBeePageCount", new IntSyncValue(() -> beePageCount, val -> beePageCount = val));

        GenericListSyncHandler<GTHelper.StackableItemSlot> beeListSyncer = createBeeListSyncer();
        syncManager.syncValue("apiaryBeeList", beeListSyncer);

        beeInventoryHandler = new DynamicSyncHandler().widgetProvider((sm, packet) -> {
            if (packet == null) return new EmptyWidget();
            return createBeeSlotGrid(packet.readInt());
        });

        if (!syncManager.isClient()) {
            beeListSyncer.setChangeListener(
                () -> notifyBeeInventoryUpdate(
                    beeListSyncer.getValue()
                        .size()));
            maxSlotsSyncer.setChangeListener(() -> notifyBeeInventoryUpdate(buildPagedAggregatedBeeList().size()));
            usedSlotsSyncer.setChangeListener(() -> notifyBeeInventoryUpdate(buildPagedAggregatedBeeList().size()));
        }

        syncManager.syncValue(
            "apiaryRunning",
            new BooleanSyncValue(
                () -> multiblock.mPrimaryMode == MTEMegaIndustrialApiary.MODE_PRIMARY_OPERATING
                    && multiblock.mMaxProgresstime > 0,
                val -> machineRunning = val));

        registerQueenBufferSlot(syncManager);
    }

    private GenericListSyncHandler<GTHelper.StackableItemSlot> createBeeListSyncer() {
        return new GenericListSyncHandler<>(
            this::buildPagedAggregatedBeeList,
            val -> beeSlots = val,
            GTHelper.StackableItemSlot::read,
            (buffer, slot) -> slot.write(buffer),
            (a, b) -> a.count == b.count && ItemStack.areItemStacksEqual(a.stack, b.stack),
            null);
    }

    private void registerQueenBufferSlot(PanelSyncManager syncManager) {
        // Buffer slot limited to 1 item so shift-clicking transfers queens one at a time
        ItemStackHandler queenBufferInv = new ItemStackHandler(1) {

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
        ModularSlot queenBufferSlot = new ModularSlot(queenBufferInv, 0).filter(this::canAcceptQueen)
            .singletonSlotGroup(SlotGroup.STORAGE_SLOT_PRIO)
            .changeListener((newItem, onlyAmountChanged, client, init) -> {
                if (client || init || newItem == null) return;
                tryAddBeeToStorage(newItem);
                queenBufferInv.setStackInSlot(0, null);
            });
        syncManager.syncValue("queenBuffer", new ItemSlotSH(queenBufferSlot));
    }

    private boolean canAcceptQueen(ItemStack stack) {
        if (multiblock.mPrimaryMode == MTEMegaIndustrialApiary.MODE_PRIMARY_OPERATING
            && multiblock.mMaxProgresstime > 0) return false;
        return beeRoot.getType(stack) == EnumBeeType.QUEEN && multiblock.getBeeCount() < multiblock.mMaxSlots;
    }

    private void notifyBeeInventoryUpdate(int pagedListSize) {
        boolean hasEmptySlot = multiblock.getBeeCount() < multiblock.mMaxSlots;
        boolean isLastPage = (beePage >= beePageCount - 1);
        int activeCount = pagedListSize + (hasEmptySlot && isLastPage ? 1 : 0);
        beeInventoryHandler.notifyUpdate(buf -> buf.writeInt(activeCount));
    }

    private static ItemStack createDisplayStack(ItemStack original) {
        ItemStack display = new ItemStack(original.getItem(), 1, original.getItemDamage());
        if (original.getTagCompound() == null) return display;

        NBTTagCompound originalTag = original.getTagCompound();
        NBTTagCompound newTag = new NBTTagCompound();

        NBTTagCompound genomeTag = originalTag.getCompoundTag("Genome");
        if (!genomeTag.hasNoTags()) {
            NBTTagList oldChromosomes = genomeTag.getTagList("Chromosomes", 10);
            if (oldChromosomes.tagCount() > 1) {
                NBTTagList newChromosomes = new NBTTagList();
                // Chromosome 0 = species, chromosome 1 = speed
                newChromosomes.appendTag(
                    oldChromosomes.getCompoundTagAt(0)
                        .copy());
                newChromosomes.appendTag(
                    oldChromosomes.getCompoundTagAt(1)
                        .copy());
                NBTTagCompound newGenome = new NBTTagCompound();
                newGenome.setTag("Chromosomes", newChromosomes);
                newTag.setTag("Genome", newGenome);
            }
        }

        if (originalTag.hasKey("IsAnalyzed")) {
            newTag.setBoolean("IsAnalyzed", originalTag.getBoolean("IsAnalyzed"));
        }

        display.setTagCompound(newTag);
        return display;
    }

    /**
     * Returns one slot per species. The group each slot acts on is appended to slotGroups in the same order.
     */
    private List<GTHelper.StackableItemSlot> buildAggregatedBeeList(List<BeeGroup> slotGroups) {
        HashMap<String, Integer> countMap = new HashMap<>();
        HashMap<String, ItemStack> stackMap = new HashMap<>();
        HashMap<String, BeeGroup> firstGroupMap = new HashMap<>();
        for (BeeGroup group : multiblock.getBeeGroups()) {
            String key = group.speciesKey;
            countMap.merge(key, group.getCount(), Integer::sum);
            stackMap.computeIfAbsent(key, unused -> createDisplayStack(group.queen));
            firstGroupMap.putIfAbsent(key, group);
        }
        List<GTHelper.StackableItemSlot> result = new ArrayList<>(countMap.size());
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            result
                .add(new GTHelper.StackableItemSlot(entry.getValue(), stackMap.get(entry.getKey()), new ArrayList<>()));
            slotGroups.add(firstGroupMap.get(entry.getKey()));
        }
        return result;
    }

    private List<GTHelper.StackableItemSlot> getFullAggregatedBeeList() {
        if (mainSyncManager == null || mainSyncManager.isClient()) {
            return cachedFullBeeList != null ? cachedFullBeeList : buildAggregatedBeeList(new ArrayList<>());
        }
        refreshBeeListCache();
        return cachedFullBeeList;
    }

    private void refreshBeeListCache() {
        if (cachedFullBeeList == null || cachedStorageVersion != multiblock.getStorageVersion()) {
            cachedSlotGroups = new ArrayList<>();
            cachedFullBeeList = buildAggregatedBeeList(cachedSlotGroups);
            cachedStorageVersion = multiblock.getStorageVersion();
        }
    }

    private List<GTHelper.StackableItemSlot> buildPagedAggregatedBeeList() {
        List<GTHelper.StackableItemSlot> full = getFullAggregatedBeeList();
        int totalPages = Math.max(1, (full.size() + BEE_ENTRIES_PER_PAGE - 1) / BEE_ENTRIES_PER_PAGE);
        beePageCount = totalPages;
        if (beePage >= totalPages) beePage = totalPages - 1;
        if (beePage < 0) beePage = 0;
        int start = beePage * BEE_ENTRIES_PER_PAGE;
        int end = Math.min(start + BEE_ENTRIES_PER_PAGE, full.size());
        if (start >= full.size()) return new ArrayList<>();
        return new ArrayList<>(full.subList(start, end));
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
                    .child(createBeeInventoryWidget())
                    .child(createPageNavigationRow()))
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

    protected IWidget createInventoryFullWarning() {
        return new DynamicDrawable(() -> {
            if (maxSlots <= 0 || usedSlots < maxSlots) return IDrawable.EMPTY;
            int frame = (int) ((System.currentTimeMillis() / WARNING_ANIM_TICK_MS) % WARNING_ANIM_FRAMES);
            float v0 = (float) frame / WARNING_ANIM_FRAMES;
            float v1 = (float) (frame + 1) / WARNING_ANIM_FRAMES;
            return OVERLAY_INVENTORY_FULL_WARNING_SHEET.getSubArea(0f, v0, 1f, v1);
        }).asWidget()
            .size(18, 18)
            .marginBottom(4)
            .tooltipBuilder(t -> t.addLine(IKey.lang("kubatech.gui.text.mia.inventory_full_warning")))
            .tooltipAutoUpdate(true)
            .setEnabledIf(w -> maxSlots > 0 && usedSlots >= maxSlots);
    }

    @Override
    protected IWidget createMaintIssueHoverableTerminal(PanelSyncManager syncManager) {
        return ((Widget<?>) super.createMaintIssueHoverableTerminal(syncManager)).marginTop(1);
    }

    private IWidget createBeeInventoryWidget() {
        return new DynamicSyncedWidget<>().widthRel(1f)
            .expanded()
            .syncHandler(beeInventoryHandler);
    }

    private IWidget createPageNavigationRow() {
        return Flow.row()
            .widthRel(1f)
            .height(14)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .crossAxisAlignment(CrossAxis.CENTER)
            .setEnabledIf(w -> beePageCount > 1)
            .child(
                new ButtonWidget<>().size(14, 14)
                    .overlay(IKey.str("<"))
                    .onMousePressed(btn -> {
                        if (beePage > 0) {
                            beePage--;
                            beePageSyncer.setIntValue(beePage, true, true);
                        }
                        return true;
                    }))
            .child(
                new TextWidget<>(IKey.dynamic(() -> (beePage + 1) + " / " + beePageCount)).alignment(Alignment.Center)
                    .width(50)
                    .height(14))
            .child(
                new ButtonWidget<>().size(14, 14)
                    .overlay(IKey.str(">"))
                    .onMousePressed(btn -> {
                        if (beePage < beePageCount - 1) {
                            beePage++;
                            beePageSyncer.setIntValue(beePage, true, true);
                        }
                        return true;
                    }));
    }

    private IWidget createBeeSlotGrid(int activeCount) {
        if (activeCount <= 0) return new EmptyWidget();

        ListWidget<IWidget, ?> listWidget = new ListWidget<>().crossAxisAlignment(CrossAxis.START)
            .widthRel(1f)
            .heightRel(1f);

        for (int rowStart = 0; rowStart < activeCount; rowStart += SLOTS_PER_ROW) {
            Flow row = Flow.row()
                .coverChildren();
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
                boolean isEmptySlot = idx >= beeSlots.size();
                if (count > 1 || isEmptySlot) {
                    String format = isNEIFilteredOut(getBeeStack(idx)) ? EnumChatFormatting.GRAY.toString() : null;
                    GuiDraw.drawStandardSlotAmountText(count, format, getArea());
                }
                if (machineRunning) {
                    GuiDraw.drawRect(0, 0, 18, 18, 0x80000000);
                }
            }
        };
        slb.size(18, 18);

        slb.onMousePressed(mouseButton -> {
            if (machineRunning) return true;
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
        if (machineRunning) {
            tooltip.addLine(
                EnumChatFormatting.RED + StatCollector.translateToLocal("kubatech.gui.text.mia.locked_while_running"));
            return;
        }
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
        if (multiblock.mPrimaryMode == MTEMegaIndustrialApiary.MODE_PRIMARY_OPERATING
            && multiblock.mMaxProgresstime > 0) return;

        int visibleSlotIdx = (encoded >>> 3) - 1;
        int mouseButton = (encoded >>> 1) & 0x3;
        boolean shift = (encoded & 1) != 0;

        EntityPlayer player = mainSyncManager.getPlayer();
        if (!(player instanceof EntityPlayerMP playerMP)) return;

        refreshBeeListCache();
        List<BeeGroup> serverGroups = cachedSlotGroups;
        int slotIdx = beePage * BEE_ENTRIES_PER_PAGE + visibleSlotIdx;

        if (slotIdx >= 0 && slotIdx < serverGroups.size()) {
            handleOccupiedSlotClick(serverGroups.get(slotIdx), mouseButton, shift, player, playerMP);
        } else {
            handleEmptySlotClick(mouseButton, player, playerMP);
        }
    }

    private void handleOccupiedSlotClick(BeeGroup group, int mouseButton, boolean shift, EntityPlayer player,
        EntityPlayerMP playerMP) {
        if (mouseButton == 2) {
            creativePickBee(group, player, playerMP);
        } else if (shift) {
            extractBeeToInventory(group, player);
        } else if (player.inventory.getItemStack() != null) {
            replaceBeeWithHeldItem(group, player, playerMP);
        } else {
            extractBeeToCursor(group, player, playerMP);
        }
    }

    private void creativePickBee(BeeGroup group, EntityPlayer player, EntityPlayerMP playerMP) {
        if (!player.capabilities.isCreativeMode || player.inventory.getItemStack() != null) return;
        if (group.getCount() <= 0) return;
        ItemStack stack = group.copyQueen();
        stack.stackSize = stack.getMaxStackSize();
        player.inventory.setItemStack(stack);
        playerMP.isChangingQuantityOnly = false;
        playerMP.updateHeldItem();
    }

    private void extractBeeToInventory(BeeGroup group, EntityPlayer player) {
        ItemStack removed = multiblock.removeQueen(group);
        if (removed == null) return;
        multiblock.onStorageContentChanged(false);
        if (player.inventory.addItemStackToInventory(removed)) {
            player.inventoryContainer.detectAndSendChanges();
        } else {
            player.entityDropItem(removed, 0.f);
        }
    }

    private void replaceBeeWithHeldItem(BeeGroup group, EntityPlayer player, EntityPlayerMP playerMP) {
        ItemStack input = player.inventory.getItemStack();
        if (input.stackSize != 1) return;
        if (group.getCount() <= 0) return;
        if (!multiblock.addQueen(input)) return;
        ItemStack removed = multiblock.removeQueen(group);
        multiblock.onStorageContentChanged(false);
        player.inventory.setItemStack(removed);
        playerMP.isChangingQuantityOnly = false;
        playerMP.updateHeldItem();
    }

    private void extractBeeToCursor(BeeGroup group, EntityPlayer player, EntityPlayerMP playerMP) {
        ItemStack removed = multiblock.removeQueen(group);
        if (removed == null) return;
        multiblock.onStorageContentChanged(false);
        player.inventory.setItemStack(removed);
        playerMP.isChangingQuantityOnly = false;
        playerMP.updateHeldItem();
    }

    private void handleEmptySlotClick(int mouseButton, EntityPlayer player, EntityPlayerMP playerMP) {
        ItemStack input = player.inventory.getItemStack();
        if (input == null) return;
        if (multiblock.getBeeCount() >= multiblock.mMaxSlots) return;
        if (beeRoot.getType(input) != EnumBeeType.QUEEN) return;

        if (mouseButton == 1) {
            injectSingleBee(input, player, playerMP);
        } else {
            injectEntireStack(input, player, playerMP);
        }
    }

    private void injectSingleBee(ItemStack input, EntityPlayer player, EntityPlayerMP playerMP) {
        if (!tryAddBeeToStorage(input)) return;
        input.stackSize--;
        if (input.stackSize <= 0) {
            player.inventory.setItemStack(null);
        }
        playerMP.isChangingQuantityOnly = input.stackSize > 0;
        playerMP.updateHeldItem();
    }

    private void injectEntireStack(ItemStack input, EntityPlayer player, EntityPlayerMP playerMP) {
        if (!tryAddBeeToStorage(input)) return;
        input.stackSize--;
        if (input.stackSize > 0) {
            playerMP.isChangingQuantityOnly = true;
            playerMP.updateHeldItem();
            return;
        }
        player.inventory.setItemStack(null);
        playerMP.isChangingQuantityOnly = false;
        playerMP.updateHeldItem();
    }

    private boolean tryAddBeeToStorage(ItemStack queenStack) {
        if (!multiblock.addQueen(queenStack)) return false;
        multiblock.onStorageContentChanged(false);
        return true;
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

    private IWidget createInventoryToggleButton() {
        return new ButtonWidget<>().size(18, 18)
            .overlay(
                new DynamicDrawable(() -> isInInventory ? GTGuiTextures.OVERLAY_BUTTON_WHITELIST : OVERLAY_BEE_LIST))
            .onMousePressed(button -> {
                isInInventory = !isInInventory;
                return true;
            })
            .tooltipBuilder(
                t -> t.addLine(
                    IKey.dynamic(
                        () -> isInInventory
                            ? StatCollector.translateToLocal("kubatech.gui.text.mia.show_machine_status")
                            : StatCollector.translateToLocal("kubatech.gui.text.mia.show_bee_inventory"))))
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
            .size(90, 70)
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
                                    .marginBottom(1))
                    .child(
                        createModeEntry(
                            primaryModeSyncer,
                            "kubatech.gui.text.mia.primary_mode",
                            3,
                            MTEMegaIndustrialApiaryGui::getPrimaryModeText))
                    .child(
                        createModeEntry(
                            secondaryModeSyncer,
                            "kubatech.gui.text.mia.secondary_mode",
                            2,
                            MTEMegaIndustrialApiaryGui::getSecondaryModeText)));
    }

    private IWidget createModeEntry(IntSyncValue modeSyncer, String labelKey, int cycleLength,
        java.util.function.IntFunction<String> modeTextProvider) {
        return Flow.column()
            .widthRel(1)
            .coverChildrenHeight()
            .crossAxisAlignment(CrossAxis.START)
            .marginBottom(1)
            .child(
                new TextWidget<>(StatCollector.translateToLocal(labelKey)).widthRel(1)
                    .height(9)
                    .marginBottom(2))
            .child(new ButtonWidget<>().overlay(new DynamicDrawable(() -> {
                IKey key = IKey.str(modeTextProvider.apply(modeSyncer.getIntValue()))
                    .alignment(Alignment.Center);
                return multiblock.mMaxProgresstime > 0 ? key.color(0xFFA0A0A0) : key;
            }))
                .onMousePressed(mouseButton -> {
                    if (multiblock.mMaxProgresstime > 0) return true;
                    int current = modeSyncer.getIntValue();
                    int next = mouseButton == 1 ? (current - 1 + cycleLength) % cycleLength
                        : (current + 1) % cycleLength;
                    modeSyncer.setIntValue(next, true, true);
                    return true;
                })
                .tooltipBuilder(t -> {
                    t.setAutoUpdate(true);
                    t.addLine(modeTextProvider.apply(modeSyncer.getIntValue()));
                    if (multiblock.mMaxProgresstime > 0) {
                        t.addLine(
                            EnumChatFormatting.RED
                                + StatCollector.translateToLocal("GT5U.gui.text.cannot_change_when_running"));
                    }
                })
                .width(65)
                .height(12)
                .marginBottom(2));
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
