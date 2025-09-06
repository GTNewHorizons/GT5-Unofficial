package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.ITEM_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;
import static gregtech.api.util.GTUtility.areStacksEqual;
import static gregtech.api.util.GTUtility.isStackInvalid;
import static gregtech.api.util.GTUtility.isStackValid;
import static gregtech.api.util.GTUtility.moveMultipleItemStacks;

import java.util.BitSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.modularui.api.forge.ItemHandlerHelper;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.GTMod;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OutputBusType;
import gregtech.api.gui.widgets.PhantomItemButton;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.IOutputBusTransaction;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IItemLockable;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.extensions.ArrayExt;

public class MTEHatchOutputBus extends MTEHatch implements IAddUIWidgets, IItemLockable, IDataCopyable, IOutputBus {

    private static final String DATA_STICK_DATA_TYPE = "outputBusFilter";
    private static final String LOCKED_ITEM_NBT_KEY = "lockedItem";

    protected ItemStack lockedItem = null;

    public MTEHatchOutputBus(int aID, String aName, String aNameRegional, int aTier) {
        this(aID, aName, aNameRegional, aTier, getSlots(aTier));
    }

    public MTEHatchOutputBus(int id, String name, String nameRegional, int tier, int slots) {
        super(
            id,
            name,
            nameRegional,
            tier,
            slots,
            ArrayExt.of(
                "Item Output for Multiblocks",
                "Capacity: " + getSlots(tier) + " stack" + (getSlots(tier) >= 2 ? "s" : ""),
                "Left click with data stick to save filter config",
                "Right click with data stick to load filter config"));
    }

    public MTEHatchOutputBus(int aID, String aName, String aNameRegional, int aTier, String[] aDescription) {
        super(aID, aName, aNameRegional, aTier, getSlots(aTier), aDescription);
    }

    public MTEHatchOutputBus(int aID, String aName, String aNameRegional, int aTier, String[] aDescription,
        int inventorySize) {
        super(aID, aName, aNameRegional, aTier, inventorySize, aDescription);
    }

    public MTEHatchOutputBus(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier), aDescription, aTextures);
    }

    public MTEHatchOutputBus(String name, int tier, int slots, String[] description, ITexture[][][] textures) {
        super(name, tier, slots, description, textures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return GTMod.proxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(ITEM_OUT_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return GTMod.proxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(ITEM_OUT_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchOutputBus(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!acceptsItemLock() || !(aPlayer instanceof EntityPlayerMP)) {
            openGui(aPlayer);
            return true;
        }

        final ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) {
            openGui(aPlayer);
            return true;
        }

        if (!pasteCopiedData(aPlayer, dataStick.stackTagCompound)) {
            aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.output_bus.invalid"));
            return false;
        }

        aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.output_bus.loaded"));
        return true;

    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!acceptsItemLock() || !(aPlayer instanceof EntityPlayerMP)) {
            return;
        }
        final ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) {
            return;
        }

        dataStick.stackTagCompound = getCopiedData(aPlayer);
        setDataStickName(dataStick);
        aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.output_bus.saved"));
    }

    protected void setDataStickName(ItemStack dataStick) {
        dataStick.setStackDisplayName("Output Bus Configuration");
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("type", DATA_STICK_DATA_TYPE);
        if (lockedItem != null) {
            nbt.setTag(LOCKED_ITEM_NBT_KEY, lockedItem.writeToNBT(new NBTTagCompound()));
        }
        return nbt;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (nbt == null || !DATA_STICK_DATA_TYPE.equals(nbt.getString("type"))) return false;
        if (nbt.hasKey(LOCKED_ITEM_NBT_KEY)) {
            lockedItem = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(LOCKED_ITEM_NBT_KEY));
        } else {
            lockedItem = null;
        }
        return true;
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return DATA_STICK_DATA_TYPE;
    }

    @Override
    public boolean storePartial(ItemStack stack, boolean simulate) {
        if (!simulate) markDirty();

        if (lockedItem != null && !lockedItem.isItemEqual(stack)) return false;

        int invLength = mInventory.length;

        for (int i = 0; i < invLength && stack.stackSize > 0; i++) {
            @Nullable
            ItemStack slot = mInventory[i];

            // the slot has an item and the stacks can't be merged; ignore it
            if (!isStackInvalid(slot) && !areStacksEqual(slot, stack)) continue;

            int inSlot = slot == null ? 0 : slot.stackSize;

            int toInsert = Math
                .min(Math.min(getInventoryStackLimit(), getStackSizeLimit(i, slot) - inSlot), stack.stackSize);

            if (toInsert == 0) continue;

            if (!simulate) {
                // if the slot is invalid create an empty stack in it
                if (isStackInvalid(slot)) mInventory[i] = slot = stack.splitStack(0);

                slot.stackSize += toInsert;
            }

            stack.stackSize -= toInsert;
        }

        return stack.stackSize == 0;
    }

    /**
     * Does this Output Bus push its content to the adjacent inventory.
     *
     * @return true if this Output Bus should push its contents, false otherwise.
     */
    public boolean pushOutputInventory() {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork()
            && (aTick & 0x7) == 0
            && pushOutputInventory()) {
            final IInventory tTileEntity = aBaseMetaTileEntity
                .getIInventoryAtSide(aBaseMetaTileEntity.getFrontFacing());
            if (tTileEntity != null) {
                moveMultipleItemStacks(
                    aBaseMetaTileEntity,
                    tTileEntity,
                    aBaseMetaTileEntity.getFrontFacing(),
                    aBaseMetaTileEntity.getBackFacing(),
                    null,
                    false,
                    (byte) 64,
                    (byte) 1,
                    (byte) 64,
                    (byte) 1,
                    mInventory.length);
                for (int i = 0; i < mInventory.length; i++)
                    if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (lockedItem != null) {
            aNBT.setTag(LOCKED_ITEM_NBT_KEY, lockedItem.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey(LOCKED_ITEM_NBT_KEY)) {
            lockedItem = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag(LOCKED_ITEM_NBT_KEY));
        }
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        switch (mTier) {
            case 0 -> getBaseMetaTileEntity().add1by1Slot(builder);
            case 1 -> getBaseMetaTileEntity().add2by2Slots(builder);
            case 2 -> getBaseMetaTileEntity().add3by3Slots(builder);
            default -> getBaseMetaTileEntity().add4by4Slots(builder);
        }

        if (acceptsItemLock()) {
            builder.widget(
                new PhantomItemButton(this).setPos(getGUIWidth() - 25, 40)
                    .setBackground(PhantomItemButton.FILTER_BACKGROUND));
        }
    }

    @Override
    public void setLockedItem(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            clearLock();
        } else {
            lockedItem = ItemHandlerHelper.copyStackWithSize(itemStack, 1);
        }
    }

    @Nullable
    @Override
    public ItemStack getLockedItem() {
        return lockedItem;
    }

    @Override
    public void clearLock() {
        lockedItem = null;
    }

    @Override
    public boolean isLocked() {
        return lockedItem != null;
    }

    @Override
    public boolean acceptsItemLock() {
        return true;
    }

    @Override
    public boolean isFiltered() {
        return isLocked();
    }

    @Override
    public boolean isFilteredToItem(GTUtility.ItemId id) {
        if (lockedItem == null) return false;

        return id.matches(lockedItem);
    }

    @Override
    public IOutputBusTransaction createTransaction() {
        return new StandardOutputBusTransaction();
    }

    @Override
    public OutputBusType getBusType() {
        return lockedItem == null ? OutputBusType.StandardUnfiltered : OutputBusType.StandardFiltered;
    }

    /**
     * Gets the max stack size limit for a slot and a stack.
     *
     * @param slot  The slot, or -1 for a general 'lowest slot' query.
     * @param stack The stack, or null for a general 'any standard stack' query (getMaxStackSize() == 64).
     */
    public int getStackSizeLimit(int slot, @Nullable ItemStack stack) {
        return Math.min(getInventoryStackLimit(), stack == null ? 64 : stack.getMaxStackSize());
    }

    class StandardOutputBusTransaction implements IOutputBusTransaction {

        private final ItemStack[] inventory;

        private final BitSet availableSlots = new BitSet();

        private boolean active = true;

        StandardOutputBusTransaction() {
            inventory = GTDataUtils.mapToArray(mInventory, ItemStack[]::new, GTUtility::copy);

            for (int i = 0, inventoryLength = inventory.length; i < inventoryLength; i++) {
                ItemStack stack = inventory[i];

                int capacity = getStackSizeLimit(i, stack);

                if (stack == null || stack.stackSize < capacity) {
                    availableSlots.set(i);
                }
            }
        }

        @Override
        public IOutputBus getBus() {
            return MTEHatchOutputBus.this;
        }

        @Override
        public boolean storePartial(GTUtility.ItemId id, ItemStack stack) {
            if (!active) throw new IllegalStateException("Cannot add to a transaction after committing it");

            int maxStackSize = getStackSizeLimit(-1, stack);

            for (int i = availableSlots.nextSetBit(0); i >= 0; i = availableSlots.nextSetBit(i + 1)) {
                if (stack.stackSize <= 0) break;

                @Nullable
                ItemStack slot = inventory[i];

                // the slot has an item and the stacks can't be merged; ignore it
                if (isStackValid(slot) && !areStacksEqual(slot, stack)) continue;

                int inSlot = slot == null ? 0 : slot.stackSize;

                int toInsert = Math.min(Math.min(getInventoryStackLimit(), maxStackSize - inSlot), stack.stackSize);

                if (toInsert == 0) continue;

                // if the slot is invalid create an empty stack in it
                if (isStackInvalid(slot)) inventory[i] = slot = stack.splitStack(0);

                slot.stackSize += toInsert;
                stack.stackSize -= toInsert;

                if (slot.stackSize == maxStackSize) {
                    availableSlots.clear(i);
                }

                return true;
            }

            return false;
        }

        @Override
        public void completeItem(GTUtility.ItemId id) {
            if (!active) throw new IllegalStateException("Cannot add to a transaction after committing it");

            for (int i = 0, invLength = inventory.length; i < invLength; i++) {
                if (!availableSlots.get(i)) continue;

                @Nullable
                ItemStack slot = inventory[i];

                if (isStackValid(slot) && id.matches(slot)) {
                    availableSlots.clear(i);
                }
            }
        }

        @Override
        public boolean hasAvailableSpace() {
            return !availableSlots.isEmpty();
        }

        @Override
        public void commit() {
            assert mInventory.length == inventory.length;

            System.arraycopy(inventory, 0, mInventory, 0, inventory.length);

            MTEHatchOutputBus.this.markDirty();

            active = false;
        }
    }
}
