package gregtech.common.items;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.rwtema.extrautils.item.ItemHealingAxe;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.item.IMiddleClickItem;
import gregtech.api.items.GTGenericItem;
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
    private static final int CHARGE_TICK = 20;

    public ItemToolbox(final String aUnlocalized, final String aEnglish, final String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack getContainerItem(final ItemStack aStack) {
        return super.getContainerItem(aStack);
    }

    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entity, final int timer, final boolean isInHand) {
        if (world.isRemote) {
            return;
        }

        if (isInHand && entity instanceof final EntityPlayer player) {
            // Since we need to interact with the stack quite a bit in this function, we'll avoid the convenience
            // methods as they instantiate a new handler every time you call them.

            final ItemStack offhandItem = Backhand.getOffhandItem(player);
            final int mainInventorySlot;

            if (offhandItem != null && offhandItem.getItem() instanceof ItemToolbox) {
                mainInventorySlot = Backhand.getOffhandSlot(player);
            } else {
                mainInventorySlot = player.inventory.currentItem;
            }

            final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(player, mainInventorySlot);
            boolean shouldUpdate = false;

            if (Mods.ExtraUtilities.isModLoaded()) {
                final ItemStack healingAxe = handler.getStackInSlot(SlotDefinition.HEALING_AXE.getSlotID());
                if (healingAxe != null && healingAxe.getItem() instanceof final ItemHealingAxe healingAxeItem) {
                    healingAxeItem.onUpdate(stack, world, entity, timer, true);
                }
            }

            if (player.ticksExisted % CHARGE_TICK == 0 && stack.hasTagCompound() && !stack.getTagCompound().getBoolean(TOOLBOX_OPEN_NBT_KEY)) {
                // If the toolbox is open, don't charge items to prevent syncing issues.

                final ItemStack battery = handler.extractItem(SlotDefinition.BATTERY.getSlotID(), 1, true);
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

                        for (final SlotDefinition slot : SlotDefinition.values()) {
                            if (slot == SlotDefinition.BATTERY) {
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
                            handler.setStackInSlot(SlotDefinition.BATTERY.getSlotID(), battery);
                        }

                        return dirty;
                    }).orElse(false);
                }
            }

            if (shouldUpdate) {
                final NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();

                tag.setTag(CONTENTS_NBT_KEY, handler.serializeNBT());
                stack.setTagCompound(tag);
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
    public boolean onMiddleClick(final ItemStack itemStack, final EntityPlayer player) {
        // TODO: Tool switching
        return false;
    }

    @Override
    public ModularPanel buildUI(final PlayerInventoryGuiData data, final PanelSyncManager syncManager,
        final UISettings settings) {
        final int slot = data.getSlotIndex();
        final ToolboxItemStackHandler stackHandler = new ToolboxItemStackHandler(data.getPlayer(), slot);

        if (data.getUsedItemStack() != null) {
            syncManager.addOpenListener(player -> {
                // Despite the javadoc's insistence, this function only runs on the client.
                // Keeping this check in here just in case it gets fixed upstream, so it doesn't break later.
                if (player.worldObj.isRemote) {
                    GTValues.NW.sendToServer(new GTPacketToolboxEvent(GTPacketToolboxEvent.Action.UI_OPEN, slot));
                }
            })
                .addCloseListener(player -> {
                    if (!player.worldObj.isRemote) {
                        // Retrieve stack from player again. Persist the toolbox contents and allow charging again.
                        final ItemStack toolbox = player.inventory.getStackInSlot(slot);
                        final NBTTagCompound tag = toolbox.hasTagCompound() ? toolbox.getTagCompound()
                            : new NBTTagCompound();
                        tag.setTag(CONTENTS_NBT_KEY, stackHandler.serializeNBT());
                        tag.setBoolean(TOOLBOX_OPEN_NBT_KEY, false);

                        // Unselect the active tool if it was removed from the toolbox.
                        if (tag.hasKey(CURRENT_TOOL_NBT_KEY)) {
                            final int selectedToolSlot = tag.getInteger(CURRENT_TOOL_NBT_KEY);
                            if (selectedToolSlot >= 0 && selectedToolSlot < stackHandler.getSlots()
                                && stackHandler.getStackInSlot(selectedToolSlot) == null) {
                                tag.removeTag(CURRENT_TOOL_NBT_KEY);
                            }
                        }

                        toolbox.setTagCompound(tag);
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
        return SlotDefinition.BATTERY.getStack(toolbox).map(battery -> battery.getItem() instanceof final IElectricItem item ? item.getMaxCharge(battery) : 0d).orElse(0d);
    }

    @Override
    public double getTransferLimit(final ItemStack toolbox) {
        return SlotDefinition.BATTERY.getStack(toolbox).map(battery -> battery.getItem() instanceof final IElectricItem item ? item.getTransferLimit(battery) : 0d).orElse(0d);
    }
    // endregion

    public enum SlotDefinition {

        WRENCH(0, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sWrenchList)),
        WIRE_CUTTER(1, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sWireCutterList)),
        SCREWDRIVER(2, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sScrewdriverList)),
        SOFT_MALLET(3, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sSoftMalletList)),
        HARD_HAMMER(4, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sHardHammerList)),
        CROWBAR(5, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sCrowbarList)),
        SOLDERING_IRON(6, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sSolderingToolList)),

        // Charges any item in the toolbox.
        BATTERY(7, OrePrefixes.battery::containsUnCached),
        // If we don't do this, players will never use the toolbox.
        HEALING_AXE(8, itemStack -> {
            if (Mods.ExtraUtilities.isModLoaded()) {
                return itemStack.getItem() instanceof ItemHealingAxe;
            }

            // If XU isn't loaded, just make this a generic slot.
            return true;
        }, () -> !Mods.ExtraUtilities.isModLoaded()),

        // And finally, a few slots that allow anything, for soldering wire and stuff. Also fills out the row in the
        // GUI.
        GENERIC_SLOT1(9),
        GENERIC_SLOT2(10),
        GENERIC_SLOT3(11),
        GENERIC_SLOT4(12),
        GENERIC_SLOT5(13),;

        public static final ImmutableList<SlotDefinition> GENERIC_SLOTS = ImmutableList
            .of(HEALING_AXE, GENERIC_SLOT1, GENERIC_SLOT2, GENERIC_SLOT3, GENERIC_SLOT4, GENERIC_SLOT5);
        public static final ImmutableList<SlotDefinition> TOOL_SLOTS = ImmutableList
            .of(WRENCH, WIRE_CUTTER, SCREWDRIVER, SOFT_MALLET, HARD_HAMMER, CROWBAR, SOLDERING_IRON);
        public static final int ROW_WIDTH = 7;

        private static final ImmutableMap<Integer, SlotDefinition> LOOKUP;
        static {
            final ImmutableMap.Builder<Integer, SlotDefinition> builder = new ImmutableMap.Builder<>();
            for (final SlotDefinition slotDefinition : SlotDefinition.values()) {
                builder.put(slotDefinition.getSlotID(), slotDefinition);
            }

            LOOKUP = builder.build();
        }

        private final Predicate<ItemStack> itemStackTest;
        private final int slot;
        private final Supplier<Boolean> isGeneric;

        SlotDefinition(final int slot) {
            this(slot, x -> true, true);
        }

        SlotDefinition(final int slot, final Predicate<ItemStack> itemStackTest) {
            this(slot, itemStackTest, () -> false);
        }

        SlotDefinition(final int slot, final Predicate<ItemStack> itemStackTest, boolean isGeneric) {
            this(slot, itemStackTest, () -> isGeneric);
        }

        SlotDefinition(final int slot, final Predicate<ItemStack> itemStackTest, Supplier<Boolean> isGeneric) {
            if (slot < 0) {
                throw new RuntimeException("Invalid slot " + slot + ". Must be greater than or equal to zero.");
            }
            this.slot = slot;
            this.itemStackTest = itemStackTest;
            this.isGeneric = isGeneric;
        }

        public boolean test(final ItemStack stack) {
            if (stack == null) {
                return false;
            }

            if (stack.getItem() instanceof ItemToolbox) {
                return false;
            }

            // TODO: Also do not allow single-use tools in the tool slots
            return itemStackTest.test(stack);
        }

        public int getSlotID() {
            return slot;
        }

        public int getRow() {
            // Using integer division on purpose here.
            return slot / ROW_WIDTH;
        }

        public int getColumn() {
            return slot % ROW_WIDTH;
        }

        /**
         * Gets the stack for this slot in the toolbox.
         *
         * @param toolbox The ItemStack containing the toolbox
         * @return The item in the slot, or empty Optional if there is none
         */
        public Optional<ItemStack> getStack(final ItemStack toolbox) {
            IItemHandlerModifiable handler = new ToolboxItemStackHandler(toolbox);
            return Optional.ofNullable(handler.getStackInSlot(slot));
        }

        /**
         * Sets the stack for this slot in the toolbox
         *
         * @param toolbox     The ItemStack containing the toolbox
         * @param newContents The new ItemStack to put in the slot. Pass null to delete anything currently in the slot
         */
        public void setStack(final ItemStack toolbox, final ItemStack newContents) {

            ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolbox);
            handler.setStackInSlot(slot, newContents);

            if (!toolbox.hasTagCompound()) {
                toolbox.setTagCompound(new NBTTagCompound());
            }

            toolbox.getTagCompound()
                .setTag(CONTENTS_NBT_KEY, handler.serializeNBT());
        }

        public boolean isGeneric() {
            return isGeneric.get();
        }

        /**
         * Retrieves the item in the given slot of the passed toolbox, if present, and maps it according to the passed
         * function.
         *
         * @param toolbox The {@link ItemStack} for the entire toolbox
         * @param mapper  A function with the {@link ItemStack} of the slot inside the toolbox as the argument
         * @return An {@link Optional} with the results of the mapping, or empty if there is no item in the slot
         * @param <U> The return type of the mapper
         */
        public <U> Optional<U> mapStack(final ItemStack toolbox, Function<? super ItemStack, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            return getStack(toolbox).map(mapper);
        }

        /**
         * Retrieves the item in the given slot of the passed toolbox and its associated {@link IElectricItem}, if
         * present, and maps it according to the passed function.
         *
         * @param toolbox The ItemStack for the entire toolbox
         * @param mapper  A function that is passed both the item stack of the slot, and its associated
         *                {@link IElectricItem}
         * @return An {@link Optional} with the results of the mapping, or empty if there is no item in the slot
         * @param <U> The return type of the mapper
         */
        public <U> Optional<U> mapElectricItem(final ItemStack toolbox,
            BiFunction<? super ItemStack, ? super IElectricItemManager, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            final Optional<ItemStack> slotStack = getStack(toolbox);

            return slotStack.flatMap(ItemToolbox::getElectricManager)
                .map(electricItem -> mapper.apply(slotStack.get(), electricItem));
        }

        public static Optional<SlotDefinition> getBySlot(int slot) {
            return Optional.ofNullable(LOOKUP.get(slot));
        }
    }

    public static class ToolboxItemStackHandler extends ItemStackHandler {

        public ToolboxItemStackHandler(final ItemStack toolbox) {
            super(SlotDefinition.values().length);

            NBTTagCompound itemData = toolbox.getTagCompound();
            if (itemData != null && itemData.hasKey(CONTENTS_NBT_KEY)) {
                deserializeNBT(itemData.getCompoundTag(CONTENTS_NBT_KEY));
            }
        }

        public ToolboxItemStackHandler(EntityPlayer player, int slot) {
            this(player.inventory.getStackInSlot(slot));
        }

        @Override
        public boolean isItemValid(final int slot, final ItemStack stack) {
            return SlotDefinition.getBySlot(slot)
                .map(definition -> definition.test(stack))
                .orElse(false);
        }

        @Override
        public int getSlotLimit(final int slot) {
            return SlotDefinition.getBySlot(slot)
                .map(SlotDefinition::isGeneric)
                .map(x -> x ? 64 : 1)
                .orElse(64);
        }
    }

    private static class ToolboxElectricManager implements IElectricItemManager {

        @Override
        public double charge(final ItemStack toolbox, final double charge, final int tier,
            final boolean ignoreTransferLimit, final boolean simulate) {

            // To prevent syncing issues, don't allow the toolbox to do any charging while open.
            if (toolbox.hasTagCompound() && toolbox.getTagCompound()
                .getBoolean(TOOLBOX_OPEN_NBT_KEY)) {
                return 0;
            }

            return SlotDefinition.BATTERY.mapElectricItem(toolbox, (battery, manager) -> {
                final double amountCharged = manager.charge(battery, charge, tier, ignoreTransferLimit, simulate);
                if (amountCharged > 0) {
                    SlotDefinition.BATTERY.setStack(toolbox, battery);
                }

                return amountCharged;
            })
                .orElse(0d);
        }

        @Override
        public double discharge(final ItemStack toolbox, final double charge, final int tier,
            final boolean ignoreTransferLimit, final boolean batteryAlike, final boolean simulate) {

            // To prevent syncing issues, don't allow the toolbox to do any discharging while open.
            if (toolbox.hasTagCompound() && toolbox.getTagCompound()
                .getBoolean(TOOLBOX_OPEN_NBT_KEY)) {
                return 0;
            }

            return SlotDefinition.BATTERY.mapElectricItem(toolbox, (battery, manager) -> {
                final double amountDischarged = manager
                    .discharge(battery, charge, tier, ignoreTransferLimit, batteryAlike, simulate);
                if (amountDischarged > 0) {
                    SlotDefinition.BATTERY.setStack(toolbox, battery);
                }
                return amountDischarged;
            })
                .orElse(0d);
        }

        @Override
        public double getCharge(final ItemStack toolbox) {
            return SlotDefinition.BATTERY.mapElectricItem(toolbox, (battery, manager) -> manager.getCharge(battery))
                .orElse(0d);
        }

        @Override
        public boolean canUse(final ItemStack toolbox, final double amount) {
            return SlotDefinition.BATTERY
                .mapElectricItem(toolbox, (battery, manager) -> manager.canUse(battery, amount))
                .orElse(false);
        }

        @Override
        public boolean use(final ItemStack toolbox, final double amount, final EntityLivingBase entityLivingBase) {
            return SlotDefinition.BATTERY
                .mapElectricItem(toolbox, (battery, manager) -> manager.use(battery, amount, entityLivingBase))
                .orElse(false);
        }

        @Override
        public void chargeFromArmor(final ItemStack toolbox, final EntityLivingBase entityLivingBase) {
            // TODO: add mutate stack
            SlotDefinition.BATTERY.getStack(toolbox)
                .ifPresent(
                    battery -> getElectricManager(battery)
                        .ifPresent(manager -> manager.chargeFromArmor(battery, entityLivingBase)));
        }

        @Override
        public String getToolTip(final ItemStack toolbox) {
            return null;
        }
    }
}
