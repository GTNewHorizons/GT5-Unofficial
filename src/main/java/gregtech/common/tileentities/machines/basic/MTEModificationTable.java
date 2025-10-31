package gregtech.common.tileentities.machines.basic;

import static gregtech.api.items.ItemAugment.*;
import static gregtech.api.items.armor.MechArmorAugmentRegistries.LARGEST_FRAME;
import static gregtech.api.items.armor.MechArmorAugmentRegistries.augmentsMap;
import static gregtech.api.items.armor.MechArmorAugmentRegistries.coresMap;
import static gregtech.api.items.armor.MechArmorAugmentRegistries.framesMap;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;
import static gregtech.common.items.armor.MechArmorBase.MECH_CORE_KEY;
import static gregtech.common.items.armor.MechArmorBase.MECH_FRAME_KEY;

import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.google.common.collect.ImmutableMap;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.ItemAugment;
import gregtech.api.items.ItemAugmentAbstract;
import gregtech.api.items.ItemAugmentCore;
import gregtech.api.items.ItemAugmentFrame;
import gregtech.api.items.armor.ArmorHelper;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Cores;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Frames;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.render.TextureFactory;
import gregtech.common.items.armor.MechArmorBase;

public class MTEModificationTable extends MetaTileEntity {

    private static final int AUGMENT_SLOTS_COUNT = LARGEST_FRAME * 4;
    private static final Map<Integer, IDrawable> CATEGORY_SLOT_TEXTURES = ImmutableMap.of(
        CATEGORY_PROTECTION,
        GTGuiTextures.SLOT_ITEM_GOLD,
        CATEGORY_MOVEMENT,
        GTGuiTextures.SLOT_ITEM_GREEN,
        CATEGORY_UTILITY,
        GTGuiTextures.SLOT_ITEM_PURPLE,
        CATEGORY_PRISMATIC,
        GTGuiTextures.SLOT_ITEM_PRISMATIC);
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
    private int installedAugments = 0;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        ItemStack armorStack = armorSlotHandler.getStackInSlot(0);
        if (armorStack != null) {
            aNBT.setTag("armor", armorStack.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("armor")) {
            NBTBase armorNbt = aNBT.getTag("armor");
            if (armorNbt instanceof NBTTagCompound armorTag) {
                armorSlotHandler.setStackInSlot(0, ItemStack.loadItemStackFromNBT(armorTag));
            }
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getDescription() {
        return new String[] { EnumChatFormatting.AQUA + "Modifies Modular Mechanisms",
            "Can be used to alter Mechanical Armor", "Insert armor, then frame, then energy core, then augments",
            "Remove all augments before removing frame or core" };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == ForgeDirection.UP)
            return new ITexture[] { TextureFactory.of(Textures.BlockIcons.MODIFICATIONTABLE_TOP) };
        return new ITexture[] { TextureFactory.of(Textures.BlockIcons.MODIFICATIONTABLE_SIDE) };
    }

    public MTEModificationTable(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 3 + (LARGEST_FRAME * 4));
    }

    public MTEModificationTable(String aName) {
        super(aName, 3 + (LARGEST_FRAME * 4));
    }

    public @NotNull Frames getFrame() {
        return frame;
    }

    public void setFrame(@NotNull Frames frame) {
        this.frame = frame;
    }

    @Override
    public byte getTileEntityBaseType() {
        return 2;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEModificationTable(mName);
    }

    private void updateFrameSlot(ItemStack newItem) {
        ItemStack armorItem = armorSlotHandler.getStackInSlot(0);
        if (armorItem == null) return;
        ItemStack updatedArmorItem = armorItem.copy();
        NBTTagCompound armorTag = getOrCreateNbtCompound(armorItem);
        if (!armorTag.hasKey(MECH_FRAME_KEY)) armorTag.setString(MECH_FRAME_KEY, "None");

        if (newItem != null) {
            if (newItem.getItem() instanceof ItemAugmentFrame itemAugment) {
                armorTag.setString(MECH_FRAME_KEY, itemAugment.frameData.id);
                applyAugmentToTag(armorTag, newItem);
            }
        } else {
            String oldFrameID = armorTag.getString(MECH_FRAME_KEY);
            if (!oldFrameID.equals("None")) {
                Item oldItem = framesMap.get(oldFrameID).item.getItem();
                if (oldItem instanceof ItemAugmentFrame frameItem) {
                    armorTag.setString(MECH_FRAME_KEY, "None");
                    frameItem.getAttachedBehaviors()
                        .forEach(behavior -> armorTag.removeTag(behavior.getMainNBTTag()));
                }
            }
        }
        updatedArmorItem.setTagCompound(armorTag);
        armorSlotHandler.setStackInSlot(0, updatedArmorItem);
    }

    private void updateCoreSlot(ItemStack newItem) {
        ItemStack armorItem = armorSlotHandler.getStackInSlot(0);
        if (armorItem == null) return;
        ItemStack updatedArmorItem = armorItem.copy();
        NBTTagCompound armorTag = getOrCreateNbtCompound(armorItem);
        if (!armorTag.hasKey(MECH_CORE_KEY)) armorTag.setString(MECH_CORE_KEY, "None");

        if (newItem != null) {
            if (newItem.getItem() instanceof ItemAugmentCore itemAugment) {
                armorTag.setString(MECH_CORE_KEY, itemAugment.coreData.id);
                applyAugmentToTag(armorTag, newItem);
            }
        } else {
            String oldCoreID = armorTag.getString(MECH_CORE_KEY);
            if (!oldCoreID.equals("None")) {
                Item oldItem = coresMap.get(oldCoreID).item.getItem();
                if (oldItem instanceof ItemAugmentCore coreItem) {
                    armorTag.setString(MECH_CORE_KEY, "None");
                    coreItem.getAttachedBehaviors()
                        .forEach(behavior -> armorTag.removeTag(behavior.getMainNBTTag()));
                }
            }
        }
        updatedArmorItem.setTagCompound(armorTag);
        armorSlotHandler.setStackInSlot(0, updatedArmorItem);
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
                installedAugments++;
            }
        } else {
            String oldAugmentID = categoryTag.getString(Integer.toString(column));
            if (!oldAugmentID.isEmpty()) {
                Item oldItem = augmentsMap.get(oldAugmentID).item.getItem();
                if (oldItem instanceof ItemAugmentAbstract augmentItem) {
                    categoryTag.removeTag(Integer.toString(column));
                    augmentItem.getAttachedBehaviors()
                        .forEach(behavior -> behavior.removeBehaviorNBT(armorTag));
                    installedAugments--;
                }
            }
        }
        updatedArmorItem.setTagCompound(armorTag);
        armorSlotHandler.setStackInSlot(0, updatedArmorItem);
    }

    private void applyAugmentToTag(NBTTagCompound armorTag, ItemStack modItem) {
        if (armorTag == null || modItem == null) return;

        // Sanity check, filter on the item slots should already verify this
        if (!(modItem.getItem() instanceof ItemAugmentAbstract baseAugment)) {
            return;
        }

        // At this point the modification should be successful, verification has passed
        armorTag.setInteger(
            ArmorHelper.VIS_DISCOUNT_KEY,
            armorTag.getInteger(ArmorHelper.VIS_DISCOUNT_KEY) + baseAugment.getVisDiscount());

        if (baseAugment instanceof ItemAugmentFrame f) {
            armorTag.setString("frame", f.frameData.id);
        }

        if (baseAugment instanceof ItemAugmentCore core) {
            armorTag.setString("core", core.coreData.id);
        }

        baseAugment.getAttachedBehaviors()
            .forEach(behavior -> behavior.addBehaviorNBT(armorTag));

    }

    LimitingItemStackHandler armorSlotHandler = new LimitingItemStackHandler(3, 1);

    LimitingItemStackHandler augmentsSlotHandler = new LimitingItemStackHandler(AUGMENT_SLOTS_COUNT, 1);

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        EnumSyncValue<Frames> frameSyncHandler = new EnumSyncValue<>(Frames.class, this::getFrame, this::setFrame);
        syncManager.syncValue("frame", frameSyncHandler);

        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
            .build();

        syncManager.registerSlotGroup("armor", 1);
        syncManager.registerSlotGroup("augments", LARGEST_FRAME);

        ParentWidget<?> slots = new ParentWidget<>().alignX(0.3f)
            .top(4)
            .size(18, 18);

        Flow armorConfigurationColumn = Flow.column()
            .coverChildrenHeight()
            .width(18)
            .alignX(0.1f)
            .top(4);

        for (int i = 0; i < LARGEST_FRAME; i++) {
            slots.child(buildAugmentSlot(i, CATEGORY_PROTECTION));
            slots.child(buildAugmentSlot(i, CATEGORY_MOVEMENT));
            slots.child(buildAugmentSlot(i, CATEGORY_UTILITY));
            slots.child(buildAugmentSlot(i, CATEGORY_PRISMATIC));
        }
        ItemSlot armorSlot = new ItemSlot().slot(
            new ModularSlot(armorSlotHandler, 0).slotGroup("armor")
                .filter((x) -> x.getItem() instanceof MechArmorBase)
                .changeListener((newItem, onlyAmountChanged, client, init) -> {
                    Frames newFrame = getFrameFromItemStack(newItem);
                    if (newFrame != getFrame()) {
                        displayInstalledAugments();
                    }
                    setFrame(newFrame);
                }))
            .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_ARMOR);
        ItemSlot frameSlot = new ItemSlot()
            .slot(
                new AccessModifiableModularSlot(armorSlotHandler, 1)
                    .setCanTake(() -> installedAugments == 0 && armorSlotHandler.getStackInSlot(2) == null)
                    .slotGroup("armor")
                    .filter((x) -> x.getItem() instanceof ItemAugmentFrame)
                    .changeListener((newItem, onlyAmountChanged, client, init) -> { updateFrameSlot(newItem); }))
            .setEnabledIf((slot) -> armorSlotHandler.getStackInSlot(0) != null)
            .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_ARMOR_FRAME);
        ItemSlot coreSlot = new ItemSlot()
            .slot(
                new AccessModifiableModularSlot(armorSlotHandler, 2).setCanTake(() -> installedAugments == 0)
                    .slotGroup("armor")
                    .filter((x) -> x.getItem() instanceof ItemAugmentCore)
                    .changeListener((newItem, onlyAmountChanged, client, init) -> { updateCoreSlot(newItem); }))
            .setEnabledIf(
                (slot) -> armorSlotHandler.getStackInSlot(0) != null && armorSlotHandler.getStackInSlot(1) != null)
            .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_ARMOR_CORE);
        armorConfigurationColumn.child(armorSlot)
            .child(frameSlot)
            .child(coreSlot);
        panel.child(armorConfigurationColumn);
        panel.child(slots);

        return panel;
    }

    private ItemSlot buildAugmentSlot(int column, int category) {
        int row = category - 1;
        Function<Frames, Integer> categorySlotCount = CATEGORY_SLOT_COUNTS.get(category);
        return new ItemSlot().slot(
            new ModularSlot(augmentsSlotHandler, column + LARGEST_FRAME * row).slotGroup("augments")
                .filter(isAugmentOfCategory(category).and(isValidForArmor()))
                .changeListener(
                    (newItem, onlyAmountChanged, client, init) -> { updateAugmentSlot(newItem, category, column); }))
            .setEnabledIf(slot -> categorySlotCount.apply(frame) >= column + 1)
            .background(CATEGORY_SLOT_TEXTURES.get(category))
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

    private @NotNull Predicate<ItemStack> isValidForArmor() {
        return (x) -> {
            ItemStack armorStack = armorSlotHandler.getStackInSlot(0);
            if (armorStack == null || !(armorStack.getItem() instanceof MechArmorBase armorItem)
                || !(x.getItem() instanceof ItemAugment augmentItem)) return false;

            NBTTagCompound armorTag = getOrCreateNbtCompound(armorStack);

            String core = armorTag.getString(MECH_CORE_KEY);

            if (core.equals("None")) return false;
            Cores coredata = coresMap.get(core);

            // Check armor against required and incompatible lists
            for (IArmorBehavior requiredBehavior : augmentItem.getRequiredBehaviors()) {
                if (!armorTag.hasKey(requiredBehavior.getMainNBTTag())) return false;
            }
            for (IArmorBehavior incompatibleBehavior : augmentItem.getIncompatibleBehaviors()) {
                if (armorTag.hasKey(incompatibleBehavior.getMainNBTTag())) return false;
            }

            // Check armor slot is valid, check installed core is high enough tier
            return augmentItem.getValidArmors()
                .contains(armorItem) && coredata.tier >= augmentItem.minimumCore;
        };
    }

    private void displayInstalledAugments() {
        installedAugments = 0;
        ItemStack armorStack = armorSlotHandler.getStackInSlot(0);
        for (int i = 0; i < AUGMENT_SLOTS_COUNT; i++) {
            ItemStack augmentStack = getAugmentStackInCategoryAndColumn(
                armorStack,
                (i / LARGEST_FRAME) + 1,
                i % LARGEST_FRAME);
            // if (augmentStack != null) installedAugments++;
            augmentsSlotHandler.setStackInSlot(i, augmentStack);
        }
        armorSlotHandler.setStackInSlot(1, getFrameStack(armorStack));
        armorSlotHandler.setStackInSlot(2, getCoreStack(armorStack));

    }

    private ItemStack getFrameStack(ItemStack armorStack) {
        if (armorStack == null) return null;
        NBTTagCompound armorTag = armorStack.getTagCompound();
        if (armorTag == null) return null;
        String frame = armorTag.getString(MECH_FRAME_KEY);
        if (frame.equals("None")) return null;
        return framesMap.get(frame).item.get(1);
    }

    private ItemStack getCoreStack(ItemStack armorStack) {
        if (armorStack == null) return null;
        NBTTagCompound armorTag = armorStack.getTagCompound();
        if (armorTag == null) return null;
        String core = armorTag.getString(MECH_CORE_KEY);
        if (core.equals("None")) return null;
        return coresMap.get(core).item.get(1);
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

    private static class AccessModifiableModularSlot extends ModularSlot {

        public AccessModifiableModularSlot(IItemHandler itemHandler, int index) {
            super(itemHandler, index);
        }

        private BooleanSupplier canTake;

        public AccessModifiableModularSlot setCanTake(BooleanSupplier canTake) {
            this.canTake = canTake;
            return this;
        }

        @Override
        public boolean canTakeStack(EntityPlayer playerIn) {
            return canTake.getAsBoolean();
        }
    }
}
