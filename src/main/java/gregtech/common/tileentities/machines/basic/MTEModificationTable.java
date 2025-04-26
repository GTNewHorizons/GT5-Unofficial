package gregtech.common.tileentities.machines.basic;

import static gregtech.api.items.ItemAugment.*;
import static gregtech.api.items.armor.MechArmorAugmentRegistries.LARGEST_FRAME;
import static gregtech.api.items.armor.MechArmorAugmentRegistries.augmentsMap;
import static gregtech.api.items.armor.MechArmorAugmentRegistries.framesMap;
import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_CHECKMARK;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.google.common.collect.ImmutableMap;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.ItemAugment;
import gregtech.api.items.ItemAugmentAbstract;
import gregtech.api.items.ItemAugmentCore;
import gregtech.api.items.ItemAugmentFrame;
import gregtech.api.items.armor.ArmorHelper;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Frames;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.common.items.armor.MechArmorBase;

public class MTEModificationTable extends MTEBasicMachine {

    private static final int AUGMENT_SLOTS_COUNT = LARGEST_FRAME * 4;
    private static final Map<Integer, IDrawable> CATEGORY_SLOT_TEXTURES = ImmutableMap.of(
        CATEGORY_PROTECTION,
        new ItemDrawable(new ItemStack(Items.iron_chestplate)),
        CATEGORY_MOVEMENT,
        new ItemDrawable(new ItemStack(Items.sugar)),
        CATEGORY_UTILITY,
        new ItemDrawable(new ItemStack(Items.wheat_seeds)),
        CATEGORY_PRISMATIC,
        new ItemDrawable(new ItemStack(Items.nether_star)));
    private static final Map<Integer, Function<Frames, Integer>> CATEGORY_SLOT_COUNTS = ImmutableMap.of(
        CATEGORY_PROTECTION,
        Frames::getProtectionSlots,
        CATEGORY_MOVEMENT,
        Frames::getMovementSlots,
        CATEGORY_UTILITY,
        Frames::getUtilitySlots,
        CATEGORY_PRISMATIC,
        Frames::getPrismaticSlots);

    private @NotNull Frames frame = Frames.None;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        ItemStack armorStack = armorSlotHandler.getStackInSlot(0);
        if (armorStack != null) {
            aNBT.setTag("armor", armorStack.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("armor")) {
            NBTBase armorNbt = aNBT.getTag("armor");
            if (armorNbt instanceof NBTTagCompound armorTag) {
                armorSlotHandler.setStackInSlot(0, ItemStack.loadItemStackFromNBT(armorTag));
            }
        }
    }

    public MTEModificationTable(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "", 1, 1);
    }

    public MTEModificationTable(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 0);
    }

    public @NotNull Frames getFrame() {
        return frame;
    }

    public void setFrame(@NotNull Frames frame) {
        this.frame = frame;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEModificationTable(mName, mTier, mDescriptionArray, mTextures);
    }

    private void updateAugmentSlot(ItemStack newItem, int category, int column) {
        ItemStack armorItem = armorSlotHandler.getStackInSlot(0);
        if (armorItem == null) return;
        ItemStack updatedArmorItem = armorItem.copy();
        NBTTagCompound armorTag = getOrCreateNbtCompound(armorItem);
        if (!armorTag.hasKey("augments")) {
            NBTTagCompound augmentsTag = new NBTTagCompound();
            augmentsTag.setTag("1", new NBTTagCompound());
            augmentsTag.setTag("2", new NBTTagCompound());
            augmentsTag.setTag("3", new NBTTagCompound());
            augmentsTag.setTag("4", new NBTTagCompound());
            armorTag.setTag("augments", augmentsTag);
        }
        NBTTagCompound categoryTag = armorTag.getCompoundTag("augments")
            .getCompoundTag(Integer.toString(category));
        if (newItem != null) {
            if (newItem.getItem() instanceof ItemAugment itemAugment) {
                categoryTag.setString(Integer.toString(column), itemAugment.augmentData.id);
                applyAugmentToTag(armorTag, newItem);
            }
        } else {
            String oldAugmentID = categoryTag.getString(Integer.toString(column));
            if (!oldAugmentID.isEmpty()) {
                Item oldItem = augmentsMap.get(oldAugmentID).item.getItem();
                if (oldItem instanceof ItemAugmentAbstract augmentItem) {
                    categoryTag.removeTag(Integer.toString(column));
                    augmentItem.getAttachedBehaviors().forEach(behavior -> armorTag.removeTag(behavior.getMainNBTTag()));
                }
            }
        }
        updatedArmorItem.setTagCompound(armorTag);
        armorSlotHandler.setStackInSlot(0, updatedArmorItem);
    }

    private boolean applyAugmentToTag(NBTTagCompound armorTag, ItemStack modItem) {
        if (armorTag == null || modItem == null) return false;

        // Sanity check, filter on the item slots should already verify this
        if (!(modItem.getItem() instanceof ItemAugmentAbstract baseAugment)) {
            return false;
        }

        // Verify behaviors meet requirements

        //TODO: This check belongs on the filter
        // These checks are only needed for non frame/core augments
        /*
        if (baseAugment instanceof ItemAugment augment) {
            // Check augment is available for this armor
            if (!augment.getValidArmors()
                .contains(armor)) return false;
        }

         */

        // Check armor against required and incompatible lists
        for (IArmorBehavior requiredBehavior : baseAugment.getRequiredBehaviors()) {
            if (!armorTag.hasKey(requiredBehavior.getMainNBTTag())) return false;
        }
        for (IArmorBehavior incompatibleBehavior : baseAugment.getIncompatibleBehaviors()) {
            if (armorTag.hasKey(incompatibleBehavior.getMainNBTTag())) return false;
        }

        // At this point the modification should be successful, verification has passed
        armorTag.setInteger(
            ArmorHelper.VIS_DISCOUNT_KEY,
            armorTag.getInteger(ArmorHelper.VIS_DISCOUNT_KEY) + baseAugment.getVisDiscount());

        if (baseAugment instanceof ItemAugmentFrame frame) {
            armorTag.setString("frame", frame.frameData.id);
        }

        if (baseAugment instanceof ItemAugmentCore core) {
            armorTag.setInteger("core", core.getCoreid());
        }

        baseAugment.getAttachedBehaviors()
            .forEach(behavior -> behavior.addBehaviorNBT(armorTag));

        return true;
    }

    LimitingItemStackHandler armorSlotHandler = new LimitingItemStackHandler(1, 1);

    // Arbitrary slot number, could be raised
    LimitingItemStackHandler augmentsSlotHandler = new LimitingItemStackHandler(AUGMENT_SLOTS_COUNT, 1);

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager) {
        EnumSyncValue<Frames> frameSyncHandler = new EnumSyncValue<>(Frames.class, this::getFrame, this::setFrame);
        syncManager.syncValue("frame", frameSyncHandler);

        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(this, data, syncManager)
            .build();

        syncManager.registerSlotGroup("armor", 1);
        syncManager.registerSlotGroup("augments", LARGEST_FRAME);

        ParentWidget<?> slots = new ParentWidget<>().pos(50, 4)
            .size(18, 18);

        for (int i = 0; i < LARGEST_FRAME; i++) {
            slots.child(buildAugmentSlot(i, CATEGORY_PROTECTION));
            slots.child(buildAugmentSlot(i, CATEGORY_MOVEMENT));
            slots.child(buildAugmentSlot(i, CATEGORY_UTILITY));
            slots.child(buildAugmentSlot(i, CATEGORY_PRISMATIC));
        }

        panel.child(
            new ItemSlot().slot(
                new ModularSlot(armorSlotHandler, 0).slotGroup("armor")
                    .filter((x) -> x.getItem() instanceof MechArmorBase)
                    .changeListener((newItem, onlyAmountChanged, client, init) -> {
                        Frames newFrame = getFrameFromItemStack(newItem);
                        if ((!client || init) && newFrame != getFrame()) {
                            displayInstalledAugments();
                        }
                        setFrame(newFrame);
                    }))
                .pos(4, 21)
                .background(GTGuiTextures.SLOT_ITEM_STANDARD, new ItemDrawable(new ItemStack(Items.iron_helmet))));

        panel.child(slots);

        return panel;
    }

    private ItemSlot buildAugmentSlot(int column, int category) {
        int row = category - 1;
        Function<Frames, Integer> categorySlotCount = CATEGORY_SLOT_COUNTS.get(category);
        return new ItemSlot().slot(
            new ModularSlot(augmentsSlotHandler, column + LARGEST_FRAME * row).slotGroup("augments")
                .filter(isAugmentOfCategory(category))
                .changeListener((newItem, onlyAmountChanged, client, init) -> {
                    if ((!client || init)) {
                        updateAugmentSlot(newItem, category, column);
                    }
                }))
            .setEnabledIf(slot -> categorySlotCount.apply(frame) >= column + 1)
            .background(GTGuiTextures.SLOT_ITEM_STANDARD, CATEGORY_SLOT_TEXTURES.get(category))
            .posRel(column, row);
    }

    private Frames getFrameFromItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            return Frames.None;
        }
        String frameId = itemStack.getTagCompound()
            .getString("frame");
        Frames frames = framesMap.get(frameId);
        if (frames == null) {
            return Frames.None;
        }
        return frames;
    }

    private static @NotNull Predicate<ItemStack> isAugmentOfCategory(int category) {
        return (x) -> x.getItem() instanceof ItemAugment augment && augment.category == category
            || category == CATEGORY_PRISMATIC;
    }

    private void displayInstalledAugments() {
        ItemStack armorStack = armorSlotHandler.getStackInSlot(0);
        for (int i = 0; i < AUGMENT_SLOTS_COUNT; i++) {
            augmentsSlotHandler.setStackInSlot(
                i,
                getAugmentStackInCategoryAndColumn(armorStack, (i / LARGEST_FRAME) + 1, i % LARGEST_FRAME));
        }
    }

    private ItemStack getAugmentStackInCategoryAndColumn(ItemStack armorStack, int category, int column) {
        if (armorStack == null) {
            return null;
        }
        NBTTagCompound armorTag = armorStack.getTagCompound();
        if (armorTag == null) {
            return null;
        }
        NBTTagCompound augmentTag = armorStack.getTagCompound()
            .getCompoundTag("augments");
        if (augmentTag == null) {
            return null;
        }
        NBTTagCompound categoryTag = augmentTag.getCompoundTag(Integer.toString(category));
        if (categoryTag == null) {
            return null;
        }
        String columnTag = categoryTag.getString(Integer.toString(column));
        if (columnTag.isEmpty()) {
            return null;
        }
        return augmentsMap.get(columnTag).item.get(1);
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
