package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers;

import java.util.ArrayList;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSink;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSource;
import com.gtnewhorizon.gtnhlib.item.ItemTransfer;
import com.gtnewhorizon.gtnhlib.item.StandardInventoryIterator;
import com.gtnewhorizon.gtnhlib.util.data.ItemId;

import gregtech.api.implementation.items.GTItemSink;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTEHatchNbtConsumableGui;
import it.unimi.dsi.fastutil.ints.IntIterators;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;

public abstract class MTEHatchNbtConsumable extends MTEHatch {

    protected final int inputSlotCount;
    protected final int totalSlotCount;
    protected final boolean allowDuplicateUsageTypes;

    public MTEHatchNbtConsumable(int aID, String aName, String aNameRegional, int aTier, int aInputSlots,
        String aDescription, boolean aAllowDuplicateTypes) {
        super(aID, aName, aNameRegional, aTier, aInputSlots * 2, aDescription);
        inputSlotCount = getInputSlotCount();
        totalSlotCount = getInputSlotCount() * 2;
        allowDuplicateUsageTypes = aAllowDuplicateTypes;
    }

    public MTEHatchNbtConsumable(String aName, int aTier, int aInputSlots, String[] aDescription,
        boolean aAllowDuplicateTypes, ITexture[][][] aTextures) {
        super(aName, aTier, aInputSlots * 2, aDescription, aTextures);
        inputSlotCount = getInputSlotCount();
        totalSlotCount = getInputSlotCount() * 2;
        allowDuplicateUsageTypes = aAllowDuplicateTypes;
    }

    @Override
    public abstract ITexture[] getTexturesActive(ITexture aBaseTexture);

    @Override
    public abstract ITexture[] getTexturesInactive(ITexture aBaseTexture);

    public abstract int getInputSlotCount();

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public final boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public final boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public abstract MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity);

    @Override
    public final boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        // Make hasInventoryBeenModified return true
        markDirty();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        validateUsageSlots();

        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
            tryFillUsageSlots();
            updateSlots();
        }
    }

    public void updateSlots() {
        // These call markDirty, which makes hasInventoryBeenModified return true

        // Remove any zero-sized stacks
        GTUtility.cleanInventory(this);

        // Compact the input slots
        GTUtility.compactInventory(this, 0, getLastInputSlot());

        // Compact the usage slots
        GTUtility.compactInventory(this, getFirstUsageSlot(), getLastUsageSlot());
    }

    protected void validateUsageSlots() {
        for (int i = getFirstUsageSlot(); i < getLastUsageSlot(); i++) {
            ItemStack stack = mInventory[i];

            if (stack != null && mInventory[i].stackSize < 1) {
                mInventory[i] = null;
                this.markDirty();
            }
        }
    }

    @Override
    public int getStackSizeLimit(int slot, @Nullable ItemStack stack) {
        return slot >= getFirstUsageSlot() ? 1 : super.getStackSizeLimit(slot, stack);
    }

    public void tryFillUsageSlots() {
        ItemSource source = getItemSource(ForgeDirection.UNKNOWN);
        ItemSink sink = new LimitingItemSink(ForgeDirection.UNKNOWN);

        ItemTransfer transfer = new ItemTransfer();

        transfer.source(source);
        transfer.sink(sink);

        transfer.setSourceSlots(IntIterators.unwrap(IntIterators.fromTo(getFirstInputSlot(), getLastInputSlot())));
        transfer.setSinkSlots(IntIterators.unwrap(IntIterators.fromTo(getFirstUsageSlot(), getLastUsageSlot())));

        transfer.setStacksToTransfer(getLastUsageSlot() - getFirstUsageSlot());

        transfer.transfer();
    }

    protected int getFirstInputSlot() {
        return 0;
    }

    protected int getLastInputSlot() {
        return inputSlotCount;
    }

    protected int getFirstUsageSlot() {
        return inputSlotCount;
    }

    protected int getLastUsageSlot() {
        return totalSlotCount;
    }

    public final ArrayList<ItemStack> getContentUsageSlots() {
        ArrayList<ItemStack> aItems = new ArrayList<>();
        for (int i = inputSlotCount; i < totalSlotCount; i++) {
            if (mInventory[i] != null) {
                aItems.add(mInventory[i]);
            }
        }
        return aItems;
    }

    @Override
    public final boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public final boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (side != aBaseMetaTileEntity.getFrontFacing()) return false;
        if (aIndex >= getFirstUsageSlot()) return false;
        if (!isItemValidForInputSlot(aStack)) return false;

        return true;
    }

    /**
     * Checks if the given item is valid for the input slots.
     */
    public abstract boolean isItemValidForInputSlot(ItemStack aStack);

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchNbtConsumableGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    private class LimitingItemSink extends GTItemSink {

        private final ForgeDirection side;
        private int[] allowedSlots;

        public LimitingItemSink(ForgeDirection side) {
            super(MTEHatchNbtConsumable.this, side);
            this.side = side;
        }

        @Override
        public void resetSink() {
            super.resetSink();
            allowedSlots = null;
        }

        @Override
        public void setAllowedSinkSlots(int @Nullable [] slots) {
            this.allowedSlots = slots;
        }

        @Override
        public @NotNull StandardInventoryIterator sinkIterator() {
            Set<ItemStack> stored = new ObjectOpenCustomHashSet<>(ItemId.STACK_ITEM_META_STRATEGY);

            for (int i = getFirstUsageSlot(); i < getLastUsageSlot(); i++) {
                if (mInventory[i] != null) {
                    stored.add(mInventory[i]);
                }
            }

            return new StandardInventoryIterator(inv, side, getSlots(), this.allowedSlots) {

                @Override
                protected boolean canAccess(ItemStack stack, int slot) {
                    return canInsert(stack, slot);
                }

                @Override
                protected boolean canInsert(ItemStack stack, int slot) {
                    return !stored.contains(stack);
                }

                @Override
                protected int getSlotStackLimit(int slot, ItemStack stack) {
                    return LimitingItemSink.this.getSlotStackLimit(slot, stack);
                }

                @Override
                protected void setInventorySlotContents(int slot, ItemStack stack) {
                    super.setInventorySlotContents(slot, stack);

                    stored.add(stack);
                }
            };
        }
    }
}
