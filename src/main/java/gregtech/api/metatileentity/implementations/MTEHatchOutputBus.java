package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.ITEM_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;
import static gregtech.api.util.GTUtility.areStacksEqual;
import static gregtech.api.util.GTUtility.isStackInvalid;
import static gregtech.api.util.GTUtility.moveMultipleItemStacks;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
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
import gregtech.api.gui.widgets.PhantomItemButton;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IItemLockable;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.extensions.ArrayExt;

public class MTEHatchOutputBus extends MTEHatch implements IAddUIWidgets, IItemLockable, IDataCopyable, IAddGregtechLogo {

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

    public boolean storePartial(ItemStack aStack) {
        return storePartial(aStack, false);
    }

    /**
     * Attempt to store as many items as possible into the internal inventory of this output bus. If you need atomicity
     * you should use {@link gregtech.api.interfaces.tileentity.IHasInventory#addStackToSlot(int, ItemStack)}
     *
     * @param stack    The stack to insert. Will be modified by this method (will contain whatever items could not be
     *                 inserted; stackSize will be 0 when everything was inserted).
     * @param simulate When true this bus will not be modified.
     * @return true if stack is fully accepted. false is stack is partially accepted or nothing is accepted
     */
    public boolean storePartial(ItemStack stack, boolean simulate) {
        markDirty();

        if (lockedItem != null && !lockedItem.isItemEqual(stack)) return false;

        int invLength = mInventory.length;

        for (int i = 0; i < invLength && stack.stackSize > 0; i++) {
            @Nullable
            ItemStack slot = mInventory[i];

            // the slot has an item and the stacks can't be merged; ignore it
            if (!isStackInvalid(slot) && !areStacksEqual(slot, stack)) continue;

            int inSlot = slot == null ? 0 : slot.stackSize;

            int toInsert = Math
                .min(Math.min(getInventoryStackLimit(), stack.getMaxStackSize() - inSlot), stack.stackSize);

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
        final int BUTTON_SIZE = 18;
        int slotCount = getSizeInventory();
        final int itemColumns = Math.max(1, mTier + 1);
        final int itemRows = Math.max(1, mTier + 1);
        final int centerX = (getGUIWidth() - (itemColumns * BUTTON_SIZE)) / 2;
        final int centerY = 14 - (mTier - 1);

        switch (slotCount) {
            case 1 -> getBaseMetaTileEntity().add1by1Slot(builder);
            case 4 -> getBaseMetaTileEntity().add2by2Slots(builder);
            case 9 -> getBaseMetaTileEntity().add3by3Slots(builder);
            case 16 -> getBaseMetaTileEntity().add4by4Slots(builder);
            default -> {
                for (int row = 0; row < itemRows; row++) {
                    for (int col = 0; col < itemColumns; col++) {
                        int slotIndex = row * itemColumns + col;
                        if (slotIndex < slotCount) {
                            builder.widget(
                                new SlotWidget(inventoryHandler, slotIndex).setBackground(ModularUITextures.ITEM_SLOT)
                                    .setPos(centerX + col * 18, centerY + row * 18));
                        }
                    }
                }
            }
        }

        if (acceptsItemLock()) {
            builder.widget(
                new PhantomItemButton(this).setPos(6, 60 + (mTier < 4 ? 0 : 16 * (mTier - 1)))
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
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                .setSize(18, 18)
                .setPos(152 + (mTier < 4 ? 0 : 2 * (mTier - 1)), 60 + (mTier < 4 ? 0 : 16 * (mTier - 1))));
    }


    @Override
    public int getGUIWidth() {
        return super.getGUIWidth() + (mTier < 4 ? 0 : 2 * (mTier - 1));
    }

    @Override
    public int getGUIHeight() {
        return super.getGUIHeight() + (mTier < 4 ? 0 : 16 * (mTier - 1));
    }
}
