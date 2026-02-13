package gregtech.common.tileentities.machines.basic;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.ItemAugment;
import gregtech.api.items.ItemAugmentAbstract;
import gregtech.api.items.ItemAugmentCore;
import gregtech.api.items.ItemAugmentFrame;
import gregtech.api.items.armor.ArmorContext.ArmorContextImpl;
import gregtech.api.items.armor.ArmorState;
import gregtech.api.items.armor.AugmentBuilder.AugmentCategory;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Augments;
import gregtech.api.items.armor.behaviors.BehaviorName;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.render.TextureFactory;
import gregtech.common.items.armor.MechArmorBase;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;

public class MTEModificationTable extends MetaTileEntity {

    public static final int AUGMENT_CATEGORY_COUNT = AugmentCategory.values().length;
    // Update this integer if you add a frame with more slots in a single category than the previous highest
    public static final int LARGEST_FRAME = 5;
    private static final int AUGMENT_SLOTS_COUNT = LARGEST_FRAME * AUGMENT_CATEGORY_COUNT;

    private int installedAugments = 0;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        ItemStack armorStack = getArmorStack();
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

    @Override
    public byte getTileEntityBaseType() {
        return 2;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEModificationTable(mName);
    }

    private void updateFrameSlot(ItemStack newItem) {
        ArmorState state = getArmorState();

        if (state == null) return;

        if (newItem != null) {
            if (newItem.getItem() instanceof ItemAugmentFrame itemAugment) {
                state.frame = itemAugment.frame;
            }
        } else {
            state.frame = null;
        }

        setArmorState(state);
    }

    private void updateCoreSlot(ItemStack newItem) {
        ArmorState state = getArmorState();

        if (state == null) return;

        if (newItem != null) {
            if (newItem.getItem() instanceof ItemAugmentCore itemAugment) {
                state.core = itemAugment.core;

                if (newItem.getTagCompound() != null) {
                    state.charge = newItem.getTagCompound()
                        .getDouble("charge");
                }
            }
        } else {
            state.core = null;
        }

        if (state.core != null) {
            double max = state.core.getChargeMax();

            if (state.charge > max) {
                state.charge = max;
            }
        } else {
            state.charge = 0;
        }

        setArmorState(state);
    }

    private void updateAugmentSlot(ItemStack newItem, AugmentCategory category, int column) {
        ArmorState state = getArmorState();

        if (state == null) return;

        if (newItem != null) {
            if (newItem.getItem() instanceof ItemAugment itemAugment) {
                state.augments.put(ObjectIntPair.of(category, column), itemAugment.augment);
            }
        } else {
            state.augments.remove(ObjectIntPair.of(category, column));
        }

        installedAugments = state.augments.size();

        setArmorState(state);
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
        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
            .build();

        syncManager.registerSlotGroup("armor", 1, 130);
        syncManager.registerSlotGroup("augment" + AugmentCategory.Protection.name(), LARGEST_FRAME, 140);
        syncManager.registerSlotGroup("augment" + AugmentCategory.Movement.name(), LARGEST_FRAME, 141);
        syncManager.registerSlotGroup("augment" + AugmentCategory.Utility.name(), LARGEST_FRAME, 142);
        syncManager.registerSlotGroup("augment" + AugmentCategory.Prismatic.name(), LARGEST_FRAME, 143);

        ParentWidget<?> slots = new ParentWidget<>().alignX(0.3f)
            .top(4)
            .size(18, 18);

        Flow armorConfigurationColumn = Flow.column()
            .coverChildrenHeight()
            .width(18)
            .alignX(0.1f)
            .top(4);

        for (int i = 0; i < LARGEST_FRAME; i++) {
            slots.child(buildAugmentSlot(AugmentCategory.Protection, i));
            slots.child(buildAugmentSlot(AugmentCategory.Movement, i));
            slots.child(buildAugmentSlot(AugmentCategory.Utility, i));
            slots.child(buildAugmentSlot(AugmentCategory.Prismatic, i));
        }

        ItemSlot armorSlot = new ItemSlot().slot(
            new ModularSlot(armorSlotHandler, 0).slotGroup("armor")
                .filter((x) -> x.getItem() instanceof MechArmorBase)
                .changeListener((newItem, onlyAmountChanged, client, init) -> displayInstalledAugments()))
            .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_ARMOR);
        ItemSlot frameSlot = new ItemSlot()
            .slot(
                new AccessModifiableModularSlot(armorSlotHandler, 1)
                    .setCanTake(() -> installedAugments == 0 && getCoreStack() == null)
                    .slotGroup("armor")
                    .filter((x) -> x.getItem() instanceof ItemAugmentFrame)
                    .changeListener((newItem, onlyAmountChanged, client, init) -> updateFrameSlot(newItem)))
            .setEnabledIf((slot) -> getArmorStack() != null)
            .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_ARMOR_FRAME);
        ItemSlot coreSlot = new ItemSlot()
            .slot(
                new AccessModifiableModularSlot(armorSlotHandler, 2).setCanTake(() -> installedAugments == 0)
                    .slotGroup("armor")
                    .filter((x) -> x.getItem() instanceof ItemAugmentCore)
                    .changeListener((newItem, onlyAmountChanged, client, init) -> updateCoreSlot(newItem)))
            .setEnabledIf((slot) -> getArmorStack() != null && getFrameStack() != null)
            .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_ARMOR_CORE);
        armorConfigurationColumn.child(armorSlot)
            .child(frameSlot)
            .child(coreSlot);
        panel.child(armorConfigurationColumn);
        panel.child(slots);

        return panel;
    }

    private @Nullable ItemStack getCoreStack() {
        return armorSlotHandler.getStackInSlot(2);
    }

    private @Nullable ItemStack getFrameStack() {
        return armorSlotHandler.getStackInSlot(1);
    }

    private @Nullable ItemStack getArmorStack() {
        return armorSlotHandler.getStackInSlot(0);
    }

    private ArmorState getArmorState() {
        return getArmorStack() == null ? null : ArmorState.load(getArmorStack());
    }

    private void setArmorState(ArmorState state) {
        ItemStack armorItem = getArmorStack();

        if (armorItem == null) return;

        ArmorContextImpl context = new ArmorContextImpl(armorItem.copy(), state);

        ArmorState.save(context);

        armorSlotHandler.setStackInSlot(0, context.armorStack);
    }

    private ItemSlot buildAugmentSlot(AugmentCategory category, int column) {
        int row = category.ordinal();

        IDrawable background = switch (category) {
            case Protection -> GTGuiTextures.SLOT_ITEM_GOLD;
            case Movement -> GTGuiTextures.SLOT_ITEM_GREEN;
            case Utility -> GTGuiTextures.SLOT_ITEM_PURPLE;
            case Prismatic -> GTGuiTextures.SLOT_ITEM_PRISMATIC;
        };

        return new ItemSlot()
            .slot(
                new AugmentSlot(augmentsSlotHandler, column + LARGEST_FRAME * row)
                    .slotGroup("augment" + category.name())
                    // TODO: fix shift + clicking augments inserting more than should be allowed
                    .filter(isAugmentOfCategory(category).and(isValidForArmor()))
                    .changeListener(
                        (newItem, onlyAmountChanged, client, init) -> updateAugmentSlot(newItem, category, column)))
            .setEnabledIf(slot -> {
                ArmorState state = getArmorState();

                if (state == null || state.frame == null) return false;

                int categorySlotCount = switch (category) {
                    case Protection -> state.frame.getProtectionSlots();
                    case Movement -> state.frame.getMovementSlots();
                    case Utility -> state.frame.getUtilitySlots();
                    case Prismatic -> state.frame.getPrismaticSlots();
                };

                return categorySlotCount >= column + 1;
            })
            .background(background)
            .posRel(column, row);
    }

    private static @NotNull Predicate<ItemStack> isAugmentOfCategory(AugmentCategory category) {
        if (category == AugmentCategory.Prismatic) {
            return x -> x.getItem() instanceof ItemAugment;
        } else {
            return (x) -> x.getItem() instanceof ItemAugment augment && augment.augment.getCategory() == category;
        }
    }

    private @NotNull Predicate<ItemStack> isValidForArmor() {
        return (x) -> {
            ItemStack armorStack = getArmorStack();
            if (armorStack == null || !(armorStack.getItem() instanceof MechArmorBase armorItem)
                || !(x.getItem() instanceof ItemAugment augmentItem)) return false;

            ArmorState state = ArmorState.load(armorStack);

            Augments augment = augmentItem.augment;

            int installed = (int) state.augments.values()
                .stream()
                .filter(x2 -> x2 == augment)
                .count();

            if (installed + 1 > augment.getMaxStack()) {
                return false;
            }

            for (BehaviorName required : augment.getRequiredBehaviors()) {
                if (!state.hasBehavior(required)) return false;
            }

            for (BehaviorName incompatible : augment.getIncompatibleBehaviors()) {
                if (state.hasBehavior(incompatible)) return false;
            }

            // Check armor slot is valid
            if (!augment.getAllowedArmorTypes()
                .contains(armorItem.getArmorType())) return false;

            // Check installed core is high enough tier
            return state.core != null && state.core.getTier() >= augment.getMinimumCore()
                .getTier();
        };
    }

    private void displayInstalledAugments() {
        installedAugments = 0;
        ArmorState state = getArmorState();

        for (int i = 0; i < AUGMENT_SLOTS_COUNT; i++) {
            augmentsSlotHandler.setStackInSlot(i, null);
        }

        armorSlotHandler.setStackInSlot(1, null);
        armorSlotHandler.setStackInSlot(2, null);

        if (state != null) {
            for (var e : state.augments.entrySet()) {
                int row = e.getKey()
                    .left()
                    .ordinal();
                int col = e.getKey()
                    .rightInt();
                Augments augment = e.getValue();

                augmentsSlotHandler.setStackInSlot(row * LARGEST_FRAME + col, augment.getItem(1));
            }

            if (state.frame != null) {
                armorSlotHandler.setStackInSlot(1, state.frame.getItem(1));
            }

            if (state.core != null) {
                ItemStack core = state.core.getItem(1);

                NBTTagCompound tag = new NBTTagCompound();
                core.setTagCompound(tag);

                tag.setDouble("charge", state.charge);

                armorSlotHandler.setStackInSlot(2, core);
            }
        }
    }

    private class AugmentSlot extends ModularSlot {

        public AugmentSlot(IItemHandler itemHandler, int index) {
            super(itemHandler, index);
        }

        @Override
        public boolean canTakeStack(EntityPlayer playerIn) {
            ItemStack myStack = getStack();
            if (myStack == null) return true;
            if (!(myStack.getItem() instanceof ItemAugmentAbstract myAugment)) return true;

            ArmorState state = MTEModificationTable.this.getArmorState();

            if (state != null) {
                for (Augments augment : state.augments.values()) {
                    for (BehaviorName required : augment.getRequiredBehaviors()) {
                        for (IArmorBehavior provided : myAugment.getPart()
                            .getProvidedBehaviors()) {
                            if (provided.getName() == required) return false;
                        }
                    }
                }
            }

            return true;
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
