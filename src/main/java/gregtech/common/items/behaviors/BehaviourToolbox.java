package gregtech.common.items.behaviors;

import com.cleanroommc.modularui.factory.GuiManager;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiFactory;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.common.builder.UIInfo;
import com.rwtema.extrautils.item.ItemHealingAxe;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.item.ToolboxGui;
import gregtech.crossmod.backhand.Backhand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class BehaviourToolbox extends BehaviourNone {
    private static final String INVENTORY_TAG = "toolboxInventory";

    @Override
    public ItemStack onItemRightClick(final MetaBaseItem item, final ItemStack stack, final World world, final EntityPlayer player) {
        final ItemStackHandler inventory = loadInventory(stack);


        if (!world.isRemote && Backhand.getOffhandItem(player) != stack) {
            PlayerInventoryGuiFactory.INSTANCE.openFromMainHand(player);
        }

        return stack;
    }

    @Override
    public void onUpdate(final MetaBaseItem item, final ItemStack stack, final World world, final Entity player, final int timer, final boolean isInHand) {
//        boolean isInOffhand = false;
//        if (!isInHand && player instanceof final EntityPlayer playerEntity) {
//            if (Backhand.getOffhandItem(playerEntity) == stack) {
//                isInOffhand = true;
//            }
//        }

//        if ((isInHand || isInOffhand)) {

        if (isInHand) {
            final ItemStackHandler inventory = loadInventory(stack);

//            // Battery Charge
//            final ItemStack battery = inventory.getStackInSlot(SlotDefinition.BATTERY.getSlot());
//            if (battery != null) {
//                SlotDefinition.TOOL_SLOTS.forEach(slotDefinition -> {
//                    final ItemStack tool = inventory.getStackInSlot(slotDefinition.getSlot());
//                });
//            }

            // Healing Axe
            if (Mods.ExtraUtilities.isModLoaded()) {
                final ItemStack healingAxe = inventory.getStackInSlot(SlotDefinition.HEALING_AXE.getSlot());
                if (healingAxe != null && healingAxe.getItem() instanceof final ItemHealingAxe healingAxeItem) {
                    healingAxeItem.onUpdate(stack, world, player, timer, true);
                }
            }
        }
    }

    private ItemStackHandler loadInventory(ItemStack stack) {
        final ItemStackHandler inventory = new ToolboxItemStackHandler();

        if (!stack.hasTagCompound()) {
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag(INVENTORY_TAG, inventory.serializeNBT());
            stack.setTagCompound(nbt);
        } else {
            inventory.deserializeNBT(stack.getTagCompound().getCompoundTag(INVENTORY_TAG));
        }

        return inventory;
    }

    private void saveInventory(ItemStack stack, ItemStackHandler inventory) {
        final NBTTagCompound nbt;
        if (!stack.hasTagCompound()) {
            nbt = new NBTTagCompound();
        } else {
            nbt = stack.getTagCompound();
        }

        nbt.setTag(INVENTORY_TAG, inventory.serializeNBT());
        stack.setTagCompound(nbt);
    }

    private static class ToolboxItemStackHandler extends ItemStackHandler {
        public ToolboxItemStackHandler() {
            super(SlotDefinition.values().length);
        }

        @Override
        public boolean isItemValid(final int slot, final ItemStack stack) {
            if (slot >= SlotDefinition.values().length || slot < 0) {
                throw new RuntimeException("Attempted to access invalid slot " + slot);
            }

            return SlotDefinition.values()[slot].test(stack);
        }
    }

    private enum SlotDefinition {
        WRENCH        (0, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sWrenchList)),
        WIRE_CUTTER   (1, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sWireCutterList)),
        SCREWDRIVER   (2, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sScrewdriverList)),
        SOFT_MALLET   (3, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sSoftMalletList)),
        HARD_HAMMER   (4, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sHardHammerList)),
        CROWBAR       (5, itemStack -> GTUtility.isStackInList(itemStack, GregTechAPI.sCrowbarList)),
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
        }),

        // And finally, a few slots that allow anything, for soldering wire and stuff. Also fills out the row in the GUI.
        GENERIC_SLOT1(9, x -> true),
        GENERIC_SLOT2(10, x -> true),
        GENERIC_SLOT3(11, x -> true),
        GENERIC_SLOT4(12, x -> true),
        GENERIC_SLOT5(13, x -> true),
        ;

        public static final ImmutableList<SlotDefinition> GENERIC_SLOTS = ImmutableList.of(GENERIC_SLOT1, GENERIC_SLOT2, GENERIC_SLOT3, GENERIC_SLOT4, GENERIC_SLOT5);
        public static final ImmutableList<SlotDefinition> TOOL_SLOTS = ImmutableList.of(WRENCH, WIRE_CUTTER, SCREWDRIVER, SOFT_MALLET, HARD_HAMMER, CROWBAR, SOLDERING_IRON);

        private final Predicate<ItemStack> itemStackTest;
        private final int slot;

        SlotDefinition(final int slot, final Predicate<ItemStack> itemStackTest) {
            if (slot < 0) {
                throw new RuntimeException("Invalid slot " + slot + ". Must be greater than or equal to zero.");
            }
            this.slot = slot;
            this.itemStackTest = itemStackTest;
        }

        public boolean test(final ItemStack aStack) {
            return itemStackTest.test(aStack);
        }

        public int getSlot() {
            return slot;
        }
    }
}
