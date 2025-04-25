package gregtech.common.tileentities.machines.basic;

import static gregtech.api.items.ItemAugment.CATEGORY_MOVEMENT;
import static gregtech.api.items.ItemAugment.CATEGORY_PROTECTION;
import static gregtech.api.items.ItemAugment.CATEGORY_UTILITY;
import static gregtech.api.items.armor.MechArmorAugmentRegistries.LARGEST_FRAME;
import static gregtech.api.items.armor.MechArmorAugmentRegistries.framesMap;
import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_CHECKMARK;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.CategoryList;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.ItemAugment;
import gregtech.api.items.ItemAugmentAbstract;
import gregtech.api.items.ItemAugmentCore;
import gregtech.api.items.ItemAugmentFrame;
import gregtech.api.items.armor.ArmorHelper;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Frames;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.modularui2.GTGuis;
import gregtech.common.items.armor.MechArmorBase;

public class MTEModificationTable extends MTEBasicMachine implements IAddUIWidgets {

    public MTEModificationTable(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "", 1, 1);
    }

    public MTEModificationTable(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEModificationTable(mName, mTier, mDescriptionArray, mTextures);
    }

    private ItemStackHandler inputHandler = new ItemStackHandler(2);

    private void updateAugmentSlot(ItemStack newItem, int category) {
        ItemStack armorItem = armorSlotHandler.getStackInSlot(0);
        if (armorItem == null || newItem == null) return;

    }

    private boolean applyAugment(ItemStack armorItem, ItemStack modItem) {
        if (armorItem == null || modItem == null) return false;

        NBTTagCompound tag = getOrCreateNbtCompound(armorItem);

        // Sanity check, filter on the item slots should already verify this
        if (!(modItem.getItem() instanceof ItemAugmentAbstract baseAugment
            && armorItem.getItem() instanceof MechArmorBase armor)) {
            return false;
        }

        // Verify behaviors meet requirements

        // These checks are only needed for non frame/core augments
        if (baseAugment instanceof ItemAugment augment) {
            // Check augment is available for this armor
            if (!augment.getValidArmors()
                .contains(armor)) return false;
        }

        // Check armor against required and incompatible lists
        for (IArmorBehavior requiredBehavior : baseAugment.getRequiredBehaviors()) {
            if (!tag.hasKey(requiredBehavior.getMainNBTTag())) return false;
        }
        for (IArmorBehavior incompatibleBehavior : baseAugment.getIncompatibleBehaviors()) {
            if (tag.hasKey(incompatibleBehavior.getMainNBTTag())) return false;
        }

        // At this point the modification should be successful, verification has passed
        tag.setInteger(
            ArmorHelper.VIS_DISCOUNT_KEY,
            tag.getInteger(ArmorHelper.VIS_DISCOUNT_KEY) + baseAugment.getVisDiscount());

        if (baseAugment instanceof ItemAugmentFrame frame) {
            armorItem.getTagCompound()
                .setInteger("frame", frame.frameData.id);
        }

        if (baseAugment instanceof ItemAugmentCore core) {
            armorItem.getTagCompound()
                .setInteger("core", core.getCoreid());
        }

        baseAugment.getAttachedBehaviors()
            .forEach(behavior -> behavior.addBehaviorNBT(armorItem, tag));

        return true;
    }

    LimitingItemStackHandler armorSlotHandler = new LimitingItemStackHandler(1, 1);

    // Arbitrary slot number, could be raised
    LimitingItemStackHandler augmentsSlotHandler = new LimitingItemStackHandler(21, 1);

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager) {
        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(this, data, syncManager)
            .build();

        syncManager.registerSlotGroup("armor", 1);
        syncManager.registerSlotGroup("augments", LARGEST_FRAME);

        ListWidget<IWidget, CategoryList.Root> list = new ListWidget<>();

        list.pos(50, 0);
        list.size(70, 70);

        ItemSlot[] augSlots = new ItemSlot[LARGEST_FRAME * 4];

        for (int i = 0; i < LARGEST_FRAME; i++) {

            ItemSlot protectionSlot = new ItemSlot().slot(
                new ModularSlot(augmentsSlotHandler, i).slotGroup("augments")
                    .filter(
                        (x) -> x.getItem() instanceof ItemAugment augment && augment.category == CATEGORY_PROTECTION)
                    .changeListener((stack, i1, i2, i3) -> updateAugmentSlot(stack, CATEGORY_PROTECTION)))
                .background(new ItemDrawable(new ItemStack(Items.iron_chestplate)));

            ItemSlot movementSlot = new ItemSlot().slot(
                new ModularSlot(augmentsSlotHandler, i + LARGEST_FRAME).slotGroup("augments")
                    .filter((x) -> x.getItem() instanceof ItemAugment augment && augment.category == CATEGORY_MOVEMENT))
                .background(new ItemDrawable(new ItemStack(Items.sugar)));

            ItemSlot utilitySlot = new ItemSlot().slot(
                new ModularSlot(augmentsSlotHandler, i + (LARGEST_FRAME * 2)).slotGroup("augments")
                    .filter((x) -> x.getItem() instanceof ItemAugment augment && augment.category == CATEGORY_UTILITY))
                .background(new ItemDrawable(new ItemStack(Items.wheat_seeds)));

            ItemSlot prismaticSlot = new ItemSlot()
                .slot(
                    new ModularSlot(augmentsSlotHandler, i + (LARGEST_FRAME * 3)).slotGroup("augments")
                        .filter((x) -> x.getItem() instanceof ItemAugment))
                .background(new ItemDrawable(new ItemStack(Items.nether_star)));

            protectionSlot.setEnabled(false);
            movementSlot.setEnabled(false);
            utilitySlot.setEnabled(false);
            prismaticSlot.setEnabled(false);

            augSlots[i] = protectionSlot;
            augSlots[i + LARGEST_FRAME] = movementSlot;
            augSlots[i + (LARGEST_FRAME * 2)] = utilitySlot;
            augSlots[i + (LARGEST_FRAME * 3)] = prismaticSlot;

            list.child(protectionSlot);
            list.child(movementSlot);
            list.child(utilitySlot);
            list.child(prismaticSlot);
        }
        panel.child(
            new ItemSlot().slot(
                new ModularSlot(armorSlotHandler, 0).slotGroup("armor")
                    .filter((x) -> x.getItem() instanceof MechArmorBase)
                    .changeListener((itemStack, i2, i3, i4) -> {
                        if (syncManager.isClient()) {
                            if (itemStack == null) {
                                list.setEnabled(false);
                                return;
                            }

                            list.setEnabled(true);

                            int frame = itemStack.getTagCompound()
                                .getInteger("frame");

                            for (ItemSlot augSlot : augSlots) augSlot.setEnabled(false);

                            if (frame != 0) {
                                Frames frameData = framesMap.get(frame);
                                for (int i = 0; i < frameData.protectionSlots; i++) augSlots[i].setEnabled(true);
                                for (int i = 0; i < frameData.movementSlots; i++)
                                    augSlots[i + LARGEST_FRAME].setEnabled(true);
                                for (int i = 0; i < frameData.utilitySlots; i++)
                                    augSlots[i + (LARGEST_FRAME * 2)].setEnabled(true);
                                for (int i = 0; i < frameData.prismaticSlots; i++)
                                    augSlots[i + (LARGEST_FRAME * 3)].setEnabled(true);
                            }

                            WidgetTree.resize(list);
                        }
                    }))
                .pos(4, 21)
                .background(new ItemDrawable(new ItemStack(Items.iron_helmet))));

        panel.child(
            new ButtonWidget<>().pos(-20, 61)
                .syncHandler(new InteractionSyncHandler().setOnMousePressed(ignored -> {
                    for (ItemStack stack : augmentsSlotHandler.getStacks()) {
                        if (stack != null) applyAugment(armorSlotHandler.getStackInSlot(0), stack);
                    }
                }))
                .overlay(OVERLAY_BUTTON_CHECKMARK)
                .size(16, 16)
                .addTooltipLine("Submit"));
        panel.child(list);

        return panel;
    }

    private static class LimitingItemStackHandler extends ItemStackHandler
        implements IItemHandlerModifiable, IItemHandler {

        private final int slotLimit;

        private LimitingItemStackHandler(int slots, int slotLimit) {
            super(slots);
            this.slotLimit = slotLimit;
        }

        @Override
        public int getSlotLimit(int slot) {
            return slotLimit;
        }
    }
}
