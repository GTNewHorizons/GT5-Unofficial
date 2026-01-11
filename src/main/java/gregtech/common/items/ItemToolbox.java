package gregtech.common.items;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.item.IMiddleClickItem;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.item.ToolboxInventoryGui;
import gregtech.crossmod.backhand.Backhand;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.rwtema.extrautils.item.ItemHealingAxe;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.GTGenericItem;
import gregtech.api.util.GTUtility;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemToolbox extends GTGenericItem implements IGuiHolder<PlayerInventoryGuiData>, ISpecialElectricItem, IMiddleClickItem {

    private static final String CONTENTS_NBT_KEY = "GT5ToolboxContents";
    private static final String CURRENT_TOOL_NBT_KEY = "GT5ToolboxSelectedSlot";
    private static final int CHARGE_TICK = 20;

    public ItemToolbox(final String aUnlocalized, final String aEnglish, final String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        setMaxStackSize(1);
    }

    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entity, final int timer, final boolean isInHand) {
        if (isInHand && entity instanceof final EntityPlayer player) {
            // Since we need to interact with the stack quite a bit in this function, we'll avoid the convenience
            // methods as they instantiate a new handler every time you call them.
            final IItemHandlerModifiable handler = getHandler(player);

            if (Mods.ExtraUtilities.isModLoaded()) {
                final ItemStack healingAxe = handler.getStackInSlot(SlotDefinition.HEALING_AXE.getSlotID());
                if (healingAxe != null && healingAxe.getItem() instanceof final ItemHealingAxe healingAxeItem) {
                    healingAxeItem.onUpdate(stack, world, entity, timer, true);
                }
            }

            // TODO: Battery
            if (player.ticksExisted % CHARGE_TICK == 0) {
                final ItemStack battery = handler.getStackInSlot(SlotDefinition.BATTERY.getSlotID());
                if (battery != null && battery.getItem() instanceof final MetaBaseItem batteryItem) {
                    double availableCharge = batteryItem.discharge(
                        battery,
                        GTValues.V[batteryItem.getTier(battery)] * CHARGE_TICK,
                        Integer.MAX_VALUE,
                        true,
                        true,
                        true
                    );
                    boolean updateBattery = false;

                    for (final SlotDefinition slot : SlotDefinition.values()) {
                        if (availableCharge <= 0) {
                            break;
                        }

                        final ItemStack slotStack = handler.getStackInSlot(slot.getSlotID());
                        if (slotStack == null) {
                            continue;
                        }

                        if (!(slotStack.getItem() instanceof final IElectricItem electricItem)) {
                            continue;
                        }

                        IElectricItemManager manager;
                        if (electricItem instanceof IElectricItemManager) {
                            manager = (IElectricItemManager) electricItem;
                        } else if (electricItem instanceof ISpecialElectricItem) {
                            manager = ((ISpecialElectricItem) electricItem).getManager(slotStack);
                        } else {
                            manager = ic2.api.item.ElectricItem.manager;
                        }

                        double powerUsed = manager.charge(
                            slotStack,
                            (int) Math.min(availableCharge, GTValues.V[electricItem.getTier(slotStack)] * CHARGE_TICK),
                            Integer.MAX_VALUE,
                            true,
                            false);

                        if (powerUsed > 0) {
                            batteryItem.discharge(battery, powerUsed, Integer.MAX_VALUE, true, true, false);
                            availableCharge -= powerUsed;
                            updateBattery = true;
                        }
                    }

                    if (updateBattery) {
                        handler.setStackInSlot(SlotDefinition.BATTERY.getSlotID(), battery);
                    }
                }
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer player) {
        if (!world.isRemote) {
            // TODO: Decide if the inventory GUI should be openable from offhand
            if (itemStack == Backhand.getOffhandItem(player)) {
                GuiFactories.playerInventory().openFromPlayerInventory(player, Backhand.getOffhandSlot(player));
            } else {
                GuiFactories.playerInventory().openFromMainHand(player);
            }
        }

        return super.onItemRightClick(itemStack, world, player);
    }

    @Override
    public ModularPanel buildUI(final PlayerInventoryGuiData data, final PanelSyncManager syncManager,
        final UISettings settings) {
        return new ToolboxInventoryGui(syncManager, data).build();
    }

    public IItemHandlerModifiable getHandler(EntityPlayer player) {
        final ItemStack offhandItem = Backhand.getOffhandItem(player);
        final int slot;

        if (offhandItem != null && offhandItem.getItem() instanceof ItemToolbox) {
            slot = Backhand.getOffhandSlot(player);
        } else {
            slot = player.inventory.currentItem;
        }

        return new ToolboxItemStackHandler(player, slot);
    }

    @Override
    public boolean onMiddleClick(final ItemStack itemStack, final EntityPlayer player) {
        // TODO: Tool switching
        return false;
    }

    //region Electric Item Functions
    @Override
    public IElectricItemManager getManager(final ItemStack toolbox) {
        return new DelegateElectricManager();
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
        return SlotDefinition.BATTERY.mapMetaBaseItem(toolbox, (battery, item) -> item.getMaxCharge(battery)).orElse(0d);
    }

    @Override
    public double getTransferLimit(final ItemStack toolbox) {
        return SlotDefinition.BATTERY.mapMetaBaseItem(toolbox, (battery, item) -> item.getTransferLimit(battery)).orElse(0d);
    }
    //endregion

    public enum SlotDefinition {

        WRENCH(0, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sWrenchList)),
        WIRE_CUTTER(1, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sWireCutterList)),
        SCREWDRIVER(2, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sScrewdriverList)),
        SOFT_MALLET(3, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sSoftMalletList)),
        HARD_HAMMER(4, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sHardHammerList)),
        CROWBAR(5, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sCrowbarList)),
        SOLDERING_IRON(6, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sSolderingToolList)),

        // Charges any item in the toolbox.
        BATTERY(7, OrePrefixes.battery::contains),
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
        GENERIC_SLOT1(9, x -> true, true),
        GENERIC_SLOT2(10, x -> true, true),
        GENERIC_SLOT3(11, x -> true, true),
        GENERIC_SLOT4(12, x -> true, true),
        GENERIC_SLOT5(13, x -> true, true),;

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

        public boolean test(final ItemStack aStack) {
            // TODO: Do not allow toolboxes in any slot
            // TODO: Also do not allow single-use tools in the tool slots
            return itemStackTest.test(aStack);
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

        public UITexture getBackground() {
            return GTGuiTextures.SLOT_ITEM_STANDARD;
        }

        /**
         * Gets the stack for this slot in the toolbox.
         * @param toolbox The ItemStack containing the toolbox
         * @return The item in the slot, or empty Optional if there is none
         */
        public Optional<ItemStack> getStack(final ItemStack toolbox) {
            IItemHandlerModifiable handler = new ToolboxItemStackHandler(toolbox);
            return Optional.ofNullable(handler.getStackInSlot(slot));
        }

        /**
         * Sets the stack for this slot in the toolbox
         * @param toolbox The ItemStack containing the toolbox
         * @param newContents The new ItemStack to put in the slot. Pass null to delete anything currently in the slot
         */
        public void setStack(final ItemStack toolbox, final ItemStack newContents) {
            IItemHandlerModifiable handler = new ToolboxItemStackHandler(toolbox);
            handler.setStackInSlot(slot, newContents);
        }

        public boolean isGeneric() {
            return isGeneric.get();
        }

        /**
         * Retrieves the item in the given slot of the passed toolbox, if present, and maps it according to the passed
         * function.
         * @param toolbox The {@link ItemStack} for the entire toolbox
         * @param mapper A function with the {@link ItemStack} of the slot inside the toolbox as the argument
         * @return An {@link Optional} with the results of the mapping, or empty if there is no item in the slot
         * @param <U> The return type of the mapper
         */
        public <U> Optional<U> mapStack(final ItemStack toolbox, Function<? super ItemStack, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            return getStack(toolbox).map(mapper);
        }

        /**
         * Retrieves the item in the given slot of the passed toolbox and its associated {@link MetaBaseItem}, if
         * present, and maps it according to the passed function.
         * @param toolbox The ItemStack for the entire toolbox
         * @param mapper A function that is passed both the item stack of the slot, and its associated {@link MetaBaseItem}
         * @return An {@link Optional} with the results of the mapping, or empty if there is no item in the slot
         * @param <U> The return type of the mapper
         */
        public <U> Optional<U> mapMetaBaseItem(final ItemStack toolbox, BiFunction<? super ItemStack, ? super MetaBaseItem, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            final Optional<ItemStack> slotStack = getStack(toolbox);

            return slotStack.map(stack -> stack.getItem() instanceof final MetaBaseItem mbItem ? mbItem : null)
                .map(metaBaseItem -> mapper.apply(slotStack.get(), metaBaseItem));
        }

        public static Optional<SlotDefinition> getBySlot(int slot) {
            return Optional.ofNullable(LOOKUP.get(slot));
        }
    }

    public static class ToolboxItemStackHandler extends ItemStackHandler {
        private final ItemStack toolbox;

        public ToolboxItemStackHandler(final ItemStack toolbox) {
            super(SlotDefinition.values().length);
            this.toolbox = toolbox;

            NBTTagCompound itemData = this.toolbox.getTagCompound();
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
        protected void onContentsChanged(final int containerSlot) {
            NBTTagCompound tag = toolbox.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
            }

            tag.setTag(CONTENTS_NBT_KEY, serializeNBT());
            toolbox.setTagCompound(tag);
        }

        @Override
        public int getSlotLimit(final int slot) {
            return SlotDefinition.getBySlot(slot).map(SlotDefinition::isGeneric).map(x -> x ? 64 : 1).orElse(64);
        }
    }

    private static class DelegateElectricManager implements IElectricItemManager {

        @Override
        public double charge(final ItemStack toolbox, final double charge, final int tier, final boolean ignoreTransferLimit, final boolean simulate) {
            return SlotDefinition.BATTERY.mapMetaBaseItem(toolbox, (battery, item) -> {
                final double amountCharged = item.charge(battery, charge, tier, ignoreTransferLimit, simulate);
                if (amountCharged > 0) {
                    SlotDefinition.BATTERY.setStack(toolbox, battery);
                }

                return amountCharged;
            }).orElse(0d);
        }

        @Override
        public double discharge(final ItemStack toolbox, final double charge, final int tier, final boolean ignoreTransferLimit, final boolean batteryAlike, final boolean simulate) {
            return SlotDefinition.BATTERY.mapMetaBaseItem(toolbox, (battery, item) -> {
                final double amountDischarged = item.discharge(battery, charge, tier, ignoreTransferLimit, batteryAlike, simulate);
                if (amountDischarged > 0) {
                    SlotDefinition.BATTERY.setStack(toolbox, battery);
                }
                return amountDischarged;
            }).orElse(0d);
        }

        @Override
        public double getCharge(final ItemStack toolbox) {
            return SlotDefinition.BATTERY.mapMetaBaseItem(toolbox, (battery, item) -> item.getCharge(battery)).orElse(0d);
        }

        @Override
        public boolean canUse(final ItemStack toolbox, final double amount) {
            return SlotDefinition.BATTERY.mapMetaBaseItem(toolbox,  (battery, item) -> item.canUse(battery, amount)).orElse(false);
        }

        @Override
        public boolean use(final ItemStack toolbox, final double amount, final EntityLivingBase entityLivingBase) {
            return SlotDefinition.BATTERY.mapMetaBaseItem(toolbox, (battery, item) -> item.use(battery, amount, entityLivingBase)).orElse(false);
        }

        @Override
        public void chargeFromArmor(final ItemStack toolbox, final EntityLivingBase entityLivingBase) {
            SlotDefinition.BATTERY.getStack(toolbox).ifPresent(battery -> {
                if (battery.getItem() instanceof final MetaBaseItem item) {
                    item.chargeFromArmor(battery, entityLivingBase);
                }
            });
        }

        @Override
        public String getToolTip(final ItemStack toolbox) {
            return null;
        }
    }
}
