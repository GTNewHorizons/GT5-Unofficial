package gregtech.common.items;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.GTNHLib;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.ToolboxSlot;
import gregtech.api.interfaces.item.IMiddleClickItem;
import gregtech.api.items.GTGenericItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.modularui2.ToolboxSelectGuiFactory;
import gregtech.api.net.GTPacketToolboxEvent;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.item.ToolboxInventoryGui;
import gregtech.crossmod.backhand.Backhand;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;

public class ItemToolbox extends GTGenericItem
    implements IGuiHolder<PlayerInventoryGuiData>, ISpecialElectricItem, IMiddleClickItem {

    public static final String CONTENTS_NBT_KEY = "gt5u.toolbox:Contents";
    public static final String TOOLBOX_OPEN_NBT_KEY = "gt5u.toolbox:ToolboxOpen";
    public static final String CURRENT_TOOL_NBT_KEY = "gt5u.toolbox:SelectedSlot";
    public static final int NO_TOOL_SELECTED = -1;

    private static final int CHARGE_TICK = 20;

    public ItemToolbox(final String aUnlocalized, final String aEnglish, final String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        setMaxStackSize(1);
    }

    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entity, final int timer, final boolean isInHand) {
        if (world.isRemote) {
            return;
        }

        if (isInHand && entity instanceof final EntityPlayer player) {
            final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(stack);
            boolean shouldUpdate = false;

            if (player.ticksExisted % CHARGE_TICK == 0 && stack.hasTagCompound() && !stack.getTagCompound().getBoolean(TOOLBOX_OPEN_NBT_KEY)) {
                // If the toolbox is open, don't charge items to prevent syncing issues.

                final ItemStack battery = handler.extractItem(ToolboxSlot.BATTERY.getSlotID(), 1, true);
                if (battery != null && battery.getItem() instanceof final IElectricItem batteryItem) {
                    shouldUpdate = getElectricManager(battery).map(batteryManager -> {
                        boolean dirty = false;
                        double remainingCharge = batteryManager.discharge(
                            battery,
                            getMaxVoltage(batteryItem.getTier(battery)) * CHARGE_TICK,
                            Integer.MAX_VALUE,
                            true,
                            true,
                            true
                        );

                        for (final ToolboxSlot slot : ToolboxSlot.values()) {
                            if (slot == ToolboxSlot.BATTERY) {
                                continue;
                            }

                            if (remainingCharge <= 0) {
                                break;
                            }
                            final double availableCharge = remainingCharge;

                            final ItemStack slotStack = handler.extractItem(slot.getSlotID(), 1, true);
                            if (slotStack == null || !(slotStack.getItem() instanceof final IElectricItem slotItem)) {
                                continue;
                            }

                            final double powerUsed = getElectricManager(slotStack).map(slotManager -> slotManager.charge(
                                slotStack,
                                (int) Math.min(availableCharge, getMaxVoltage(slotItem.getTier(slotStack)) * CHARGE_TICK),
                                Integer.MAX_VALUE,
                                true,
                                false)).orElse(0d);

                            if (powerUsed > 0) {
                                batteryManager.discharge(battery, powerUsed, Integer.MAX_VALUE, true, true, false);
                                remainingCharge -= powerUsed;
                                dirty = true;
                                handler.setStackInSlot(slot.getSlotID(), slotStack);
                            }
                        }

                        if (dirty) {
                            handler.setStackInSlot(ToolboxSlot.BATTERY.getSlotID(), battery);
                        }

                        return dirty;
                    }).orElse(false);
                }
            }

            if (shouldUpdate) {
                saveToolbox(stack, handler);
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer player) {
        if (!world.isRemote) {
            // TODO: Decide if the inventory GUI should be openable from offhand
            if (itemStack == Backhand.getOffhandItem(player)) {
                GuiFactories.playerInventory()
                    .openFromPlayerInventory(player, Backhand.getOffhandSlot(player));
            } else {
                GuiFactories.playerInventory()
                    .openFromMainHand(player);
            }
        }

        return super.onItemRightClick(itemStack, world, player);
    }

    @Override
    public String getItemStackDisplayName(final ItemStack toolbox) {
        final String base = super.getItemStackDisplayName(toolbox);

        if (toolbox.hasTagCompound()) {
            final int selectedTool = toolbox.getTagCompound()
                .getInteger(CURRENT_TOOL_NBT_KEY);
            final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolbox);

            return ToolboxSlot.getBySlot(selectedTool)
                .map(slot -> {
                    final String toolName = StatCollector.translateToLocal("GT5U.gui.text.toolbox.slot_title." + selectedTool);
                    final Optional<ItemStack> potentialTool = handler.getCurrentTool();
                    final byte toolMode = potentialTool.map(MetaGeneratedTool::getToolMode).orElse((byte) 0);

                    //noinspection SimplifyOptionalCallChains
                    return toolMode > 0
                            ? StatCollector.translateToLocalFormatted(
                                "GT5U.item.toolbox.name_template.mode",
                                base,
                                toolName,
                                potentialTool.map(currentTool -> currentTool.getItem() instanceof final MetaGeneratedTool mgToolItem
                                    ? mgToolItem.getToolModeName(currentTool)
                                    : "").orElse(""))
                            : StatCollector
                                .translateToLocalFormatted("GT5U.item.toolbox.name_template", base, toolName);
                })
                .orElse(base);
        }

        return base;
    }

    @Override
    public boolean onMiddleClick(final ItemStack toolbox, final EntityPlayer player) {
        // TODO: Switch tool if you pick block a machine?
        final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolbox);

        int toolCount = 0;
        int lastSlot = -1;
        for (ToolboxSlot slot : ToolboxSlot.TOOL_SLOTS) {
            if (handler.getStackInSlot(slot.getSlotID()) != null) {
                toolCount++;
                lastSlot = slot.getSlotID();
            }
        }

        switch (toolCount) {
            case 0:
                GTNHLib.proxy.printMessageAboveHotbar(
                    StatCollector.translateToLocal("GT5U.gui.text.toolbox.error.no_tools"),
                    120,
                    true,
                    true);
            case 1:
                final int currentTool = toolbox.hasTagCompound() && toolbox.getTagCompound().hasKey(CURRENT_TOOL_NBT_KEY)
                    ? toolbox.getTagCompound().getInteger(CURRENT_TOOL_NBT_KEY)
                    : NO_TOOL_SELECTED;

                GTValues.NW.sendToServer(
                    new GTPacketToolboxEvent(
                        GTPacketToolboxEvent.Action.CHANGE_ACTIVE_TOOL,
                        player.inventory.getCurrentItem() == toolbox ? player.inventory.currentItem : Backhand.getOffhandSlot(player),
                        currentTool == NO_TOOL_SELECTED ? lastSlot : NO_TOOL_SELECTED));
                return true;
            default:
                if (player instanceof final EntityClientPlayerMP playerMP) {
                    ToolboxSelectGuiFactory.INSTANCE.open(playerMP);
                    return true;
                }
        }

        return false;
    }

    @Override
    public ModularPanel buildUI(final PlayerInventoryGuiData data, final PanelSyncManager syncManager,
        final UISettings settings) {
        final int slot = data.getSlotIndex();
        final ToolboxItemStackHandler stackHandler = new ToolboxItemStackHandler(data.getPlayer(), slot);

        if (data.getUsedItemStack() != null) {
            syncManager.addOpenListener(player -> {
                // Despite the Javadoc's insistence, this function only runs on the client.
                // Keeping this check in here just in case it gets fixed upstream, so it doesn't break later.
                if (player.worldObj.isRemote) {
                    GTValues.NW.sendToServer(new GTPacketToolboxEvent(GTPacketToolboxEvent.Action.UI_OPEN, slot));
                }
            })
                .addCloseListener(player -> {
                    if (!player.worldObj.isRemote) {
                        // Retrieve stack from player again. Persist the toolbox contents and allow charging again.
                        final ItemStack toolbox = player.inventory.getStackInSlot(slot);

                        saveToolbox(toolbox, stackHandler, tag -> {
                            tag.setBoolean(TOOLBOX_OPEN_NBT_KEY, false);

                            // Unselect the active tool if it was removed from the toolbox.
                            if (tag.hasKey(CURRENT_TOOL_NBT_KEY)) {
                                final int selectedToolSlot = tag.getInteger(CURRENT_TOOL_NBT_KEY);
                                if (selectedToolSlot >= 0 && selectedToolSlot < stackHandler.getSlots()
                                    && stackHandler.getStackInSlot(selectedToolSlot) == null) {
                                    tag.removeTag(CURRENT_TOOL_NBT_KEY);
                                }
                            }
                        });

                        player.inventory.setInventorySlotContents(data.getSlotIndex(), toolbox);

                        GTUtility.sendSoundToPlayers(
                            player.worldObj,
                            SoundResource.GT_TOOLBOX_CLOSE,
                            1.0F,
                            1,
                            player.posX,
                            player.posY,
                            player.posZ);
                    }
                });
        }
        return new ToolboxInventoryGui(syncManager, data, stackHandler).build();
    }

    /**
     * Handler for tool mode switch keybind. The toolbox delegates this action to the currently selected tool, switching
     * its mode while still inside.
     *
     * @param player  The player doing the switching
     * @param keybind The keybind responsible for triggering this action
     * @param keyDown true if the key is depressed
     */
    public static void switchToolMode(EntityPlayerMP player, SyncedKeybind keybind, boolean keyDown) {
        if (!keyDown) {
            return;
        }

        getToolboxIfEquipped(player).ifPresent(toolboxStack -> {
            if (toolboxStack.hasTagCompound()) {
                ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolboxStack);
                handler.mutateCurrentTool(MetaGeneratedTool::switchToolMode);
                toolboxStack.getTagCompound()
                    .setTag(CONTENTS_NBT_KEY, handler.serializeNBT());
            }
        });
    }

    /**
     * Gets the currently equipped toolbox if the player is holding it in their main hand or offhand.
     *
     * @param player The player to interrogate
     * @return An optional with the toolbox's item stack, or empty if the user is not wielding a toolbox
     */
    private static Optional<ItemStack> getToolboxIfEquipped(EntityPlayer player) {
        for (ItemStack stack : new ItemStack[] { player.inventory.getCurrentItem(), Backhand.getOffhandItem(player) }) {
            if (stack != null && stack.getItem() instanceof ItemToolbox) {
                return Optional.of(stack);
            }
        }

        return Optional.empty();
    }

    /**
     * {@code additionalAction} defaults to null.
     *
     * @see #saveToolbox(ItemStack, ItemStackHandler, Consumer)
     */
    private static void saveToolbox(final ItemStack toolbox, final ItemStackHandler handler) {
        saveToolbox(toolbox, handler, null);
    }

    /**
     * Writes the toolbox's data to the {@link ItemStack ItemStack's} NBT data.
     *
     * @param toolbox          The toolbox to persist
     * @param handler          The {@link ItemStackHandler} that contains updated contents data
     * @param additionalAction Any additional changes to make to the NBT data of the toolbox before persisting.
     *                         If null, nothing happens.
     */
    private static void saveToolbox(final ItemStack toolbox, final ItemStackHandler handler,
        @Nullable Consumer<NBTTagCompound> additionalAction) {
        final NBTTagCompound tag = toolbox.hasTagCompound() ? toolbox.getTagCompound() : new NBTTagCompound();
        tag.setTag(CONTENTS_NBT_KEY, handler.serializeNBT());

        if (additionalAction != null) {
            additionalAction.accept(tag);
        }

        toolbox.setTagCompound(tag);
    }

    // region Electric Item Functions

    private static Optional<IElectricItemManager> getElectricManager(final ItemStack itemStack) {
        if (itemStack == null) {
            return Optional.empty();
        }

        final Item item = itemStack.getItem();

        IElectricItemManager manager = null;
        if (item instanceof final ISpecialElectricItem special) {
            manager = special.getManager(itemStack);
        } else if (item instanceof IElectricItem) {
            manager = ic2.api.item.ElectricItem.manager;
        }

        return Optional.ofNullable(manager);
    }

    private static long getMaxVoltage(int tier) {
        if (tier >= GTValues.V.length) {
            tier = GTValues.V.length - 1;
        }

        return GTValues.V[tier];
    }

    /**
     * Convenience method for getting the battery from the toolbox.
     * <p>
     * <b>NOTE:</b> Any changes to the battery MUST be persisted via {@link #saveBattery(ItemStack, ItemStack)}! This is
     * not
     * done automatically.
     *
     * @param toolbox The toolbox you wish to search
     * @return An optional containing a copy of the battery's ItemStack, or empty if there isn't one.
     */
    private static Optional<ItemStack> getBattery(final ItemStack toolbox) {
        return Optional
            .ofNullable(new ToolboxItemStackHandler(toolbox).getStackInSlot(ToolboxSlot.BATTERY.getSlotID()));
    }

    @Override
    public IElectricItemManager getManager(final ItemStack toolbox) {
        return new ToolboxElectricManager();
    }

    @Override
    public boolean canProvideEnergy(final ItemStack itemStack) {
        return false;
    }

    @Override
    public Item getChargedItem(final ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(final ItemStack itemStack) {
        return this;
    }

    @Override
    public double getMaxCharge(final ItemStack toolbox) {
        return getBattery(toolbox).map(battery -> battery.getItem() instanceof final IElectricItem item ? item.getMaxCharge(battery) : 0d).orElse(0d);
    }

    @Override
    public double getTransferLimit(final ItemStack toolbox) {
        return getBattery(toolbox).map(battery -> battery.getItem() instanceof final IElectricItem item ? item.getTransferLimit(battery) : 0d).orElse(0d);
    }

    /**
     * Get the {@link IElectricItemManager} for the battery inside a toolbox.
     * 
     * @param toolbox The toolbox to search
     * @param mapper  A function to run if a battery is found inside the toolbox.
     *                Arguments are the battery's {@link ItemStack} and its manager.
     * @return An optional containing the results of the mapping function, or empty if no battery or manager was found.
     * @param <U> The return type of the mapper
     */
    private static <U> Optional<U> mapBatteryManager(final ItemStack toolbox,
        BiFunction<? super ItemStack, ? super IElectricItemManager, ? extends U> mapper) {
        final Optional<ItemStack> stack = getBattery(toolbox);

        return stack.flatMap(ItemToolbox::getElectricManager)
            .map(manager -> mapper.apply(stack.get(), manager));
    }

    /**
     * Writes the battery to the toolbox. Use after mutating the battery; changes to tools are not automatically
     * propagated to the containing toolbox!
     * 
     * @param toolbox The toolbox to use
     * @param battery A battery to save to the toolbox
     */
    private static void saveBattery(final ItemStack toolbox, final ItemStack battery) {
        ItemStackHandler handler = new ItemToolbox.ToolboxItemStackHandler(toolbox);
        handler.setStackInSlot(ToolboxSlot.BATTERY.getSlotID(), battery);

        saveToolbox(toolbox, handler);
    }

    // endregion

    /**
     * A special {@link ItemStackHandler} for working with toolboxes. Used both to drive the inventory GUI and for
     * general inventory management.
     */
    public static class ToolboxItemStackHandler extends ItemStackHandler {

        private final int currentTool;

        public ToolboxItemStackHandler(final ItemStack toolbox) {
            super(ToolboxSlot.values().length);
            int currentTool = -1;

            NBTTagCompound itemData = toolbox.getTagCompound();
            if (itemData != null) {
                if (itemData.hasKey(CONTENTS_NBT_KEY)) {
                    deserializeNBT(itemData.getCompoundTag(CONTENTS_NBT_KEY));
                }

                if (itemData.hasKey(CURRENT_TOOL_NBT_KEY)) {
                    currentTool = itemData.getInteger(CURRENT_TOOL_NBT_KEY);
                }
            }

            this.currentTool = currentTool;
        }

        public ToolboxItemStackHandler(EntityPlayer player, int slot) {
            this(player.inventory.getStackInSlot(slot));
        }

        @Override
        public boolean isItemValid(final int slot, final ItemStack stack) {
            return ToolboxSlot.getBySlot(slot)
                .map(definition -> definition.test(stack))
                .orElse(false);
        }

        @Override
        public int getSlotLimit(final int slot) {
            return ToolboxSlot.getBySlot(slot)
                .map(ToolboxSlot::isGeneric)
                .map(x -> x ? 64 : 1)
                .orElse(64);
        }

        /**
         * Allows for the currently selected tool to change. Tool is saved to the handler after the action.
         *
         * @param action Receives the current tool. If the user has no tool selected, action does not run.
         */
        public void mutateCurrentTool(Consumer<ItemStack> action) {
            getCurrentTool().ifPresent(tool -> {
                action.accept(tool);
                setStackInSlot(currentTool, tool);
            });
        }

        /**
         * Retrieves the currently selected tool from the toolbox.
         * 
         * @return An Optional containing the tool's {@link ItemStack}, or empty if the user hasn't selected one
         */
        public Optional<ItemStack> getCurrentTool() {
            if (currentTool == -1 || currentTool > this.stacks.size()) {
                return Optional.empty();
            }

            return Optional.of(this.stacks.get(currentTool));
        }
    }

    /**
     * A special {@link IElectricItemManager} for delegating electricity tasks to the toolbox's battery.
     */
    private static class ToolboxElectricManager implements IElectricItemManager {

        @Override
        public double charge(final ItemStack toolbox, final double charge, final int tier,
            final boolean ignoreTransferLimit, final boolean simulate) {

            // To prevent syncing issues, don't allow the toolbox to do any charging while open.
            if (toolbox.hasTagCompound() && toolbox.getTagCompound()
                .getBoolean(TOOLBOX_OPEN_NBT_KEY)) {
                return 0;
            }

            return mapBatteryManager(toolbox, (battery, manager) -> {
                final double amountCharged = manager.charge(battery, charge, tier, ignoreTransferLimit, simulate);
                if (amountCharged > 0) {
                    saveBattery(toolbox, battery);
                }

                return amountCharged;
            }).orElse(0d);
        }

        @Override
        public double discharge(final ItemStack toolbox, final double charge, final int tier,
            final boolean ignoreTransferLimit, final boolean batteryAlike, final boolean simulate) {

            // To prevent syncing issues, don't allow the toolbox to do any discharging while open.
            if (toolbox.hasTagCompound() && toolbox.getTagCompound()
                .getBoolean(TOOLBOX_OPEN_NBT_KEY)) {
                return 0;
            }

            return mapBatteryManager(toolbox, (battery, manager) -> {
                final double amountDischarged = manager
                    .discharge(battery, charge, tier, ignoreTransferLimit, batteryAlike, simulate);
                if (amountDischarged > 0) {
                    saveBattery(toolbox, battery);
                }
                return amountDischarged;
            }).orElse(0d);
        }

        @Override
        public double getCharge(final ItemStack toolbox) {
            return mapBatteryManager(toolbox, (battery, manager) -> manager.getCharge(battery)).orElse(0d);
        }

        @Override
        public boolean canUse(final ItemStack toolbox, final double amount) {
            return mapBatteryManager(toolbox, (battery, manager) -> manager.canUse(battery, amount)).orElse(false);
        }

        @Override
        public boolean use(final ItemStack toolbox, final double amount, final EntityLivingBase entityLivingBase) {
            return mapBatteryManager(toolbox, (battery, manager) -> {
                final boolean used = manager.use(battery, amount, entityLivingBase);
                if (used) {
                    saveBattery(toolbox, battery);
                }

                return used;
            }).orElse(false);
        }

        @Override
        public void chargeFromArmor(final ItemStack toolbox, final EntityLivingBase entityLivingBase) {
            // TODO: add mutate stack
            getBattery(toolbox).ifPresent(battery -> getElectricManager(battery).ifPresent(manager -> {
                manager.chargeFromArmor(battery, entityLivingBase);
                saveBattery(toolbox, battery);
            }));
        }

        @Override
        public String getToolTip(final ItemStack toolbox) {
            return null;
        }

    }
}
