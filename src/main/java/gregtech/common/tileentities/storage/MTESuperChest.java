package gregtech.common.tileentities.storage;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTItemTransfer;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTESuperChestGui;

@IMetaTileEntity.SkipGenerateDescription
@IMetaTileEntity.SkipGenerateName
public class MTESuperChest extends MTEQuantumChest {

    private boolean mOutputItems;
    private boolean mVoidAll;
    private boolean mLockItems;
    private boolean mAllowInputFromOutputSide;
    private boolean mOutputToSlot = true;
    private ItemStack lockedItem;

    public MTESuperChest(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTESuperChest(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public String getLocalName() {
        if (!hasOwnLocalName()) return super.getLocalName();
        return StatCollector
            .translateToLocalFormatted("gt.blockmachines.super.chest.name", GTUtility.getRomanNumeral(mTier));
    }

    @Override
    protected String localizedChestName() {
        return StatCollector.translateToLocal("GT5U.infodata.super_chest.name");
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESuperChest(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void setItemNBT(NBTTagCompound nbt) {
        super.setItemNBT(nbt);
        if (mOutputItems) nbt.setBoolean("mOutputItems", true);
        if (mVoidAll) nbt.setBoolean("mVoidAll", true);
        if (mLockItems) nbt.setBoolean("mLockItems", true);
        if (mAllowInputFromOutputSide) nbt.setBoolean("mAllowInputFromOutputSide", true);
        nbt.setBoolean("mOutputToSlot", mOutputToSlot);
        if (mLockItems && lockedItem != null) {
            nbt.setTag("lockedItem", lockedItem.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        nbt.setBoolean("mOutputItems", mOutputItems);
        nbt.setBoolean("mVoidAll", mVoidAll);
        nbt.setBoolean("mLockItems", mLockItems);
        nbt.setBoolean("mAllowInputFromOutputSide", mAllowInputFromOutputSide);
        nbt.setBoolean("mOutputToSlot", mOutputToSlot);
        if (mLockItems && lockedItem != null) {
            nbt.setTag("lockedItem", lockedItem.writeToNBT(new NBTTagCompound()));
        } else {
            nbt.removeTag("lockedItem");
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        mOutputItems = nbt.getBoolean("mOutputItems");
        mVoidAll = nbt.getBoolean("mVoidAll");
        if (mVoidAll) super.setVoidOverflow(false);
        mLockItems = nbt.getBoolean("mLockItems") && !mVoidAll;
        mAllowInputFromOutputSide = nbt.getBoolean("mAllowInputFromOutputSide");
        mOutputToSlot = !nbt.hasKey("mOutputToSlot") || nbt.getBoolean("mOutputToSlot");
        lockedItem = mLockItems && nbt.hasKey("lockedItem")
            ? ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("lockedItem"))
            : null;
    }

    @Override
    public void setItemStack(ItemStack stack) {
        super.setItemStack(stack);
        if (mLockItems && lockedItem == null && GTUtility.isStackValid(stack)) setLockedItem(stack);
    }

    @Override
    public boolean isItemInputAllowed(ItemStack stack) {
        return (!mLockItems || lockedItem == null || GTUtility.areStacksEqual(lockedItem, stack))
            && super.isItemInputAllowed(stack);
    }

    @Override
    protected boolean isItemInputSide(ForgeDirection side) {
        return mAllowInputFromOutputSide || side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    protected ItemStack getMatterClusterTargetItem() {
        ItemStack storedItem = super.getMatterClusterTargetItem();
        return storedItem == null ? lockedItem : storedItem;
    }

    @Override
    protected boolean shouldFillOutputSlot() {
        return mOutputToSlot;
    }

    @Override
    protected boolean isVoidAllItems() {
        return mVoidAll;
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long timer) {
        super.onPostTick(baseMetaTileEntity, timer);
        if (baseMetaTileEntity.isServerSide() && mOutputItems && timer % 20 == 0) {
            GTItemTransfer transfer = new GTItemTransfer();
            transfer.push(baseMetaTileEntity, baseMetaTileEntity.getFrontFacing());
            transfer.transfer();
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (side != ForgeDirection.UP) {
            if (side == baseMetaTileEntity.getFrontFacing())
                return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_PIPE) };
            return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1] };
        }
        return super.getTexture(baseMetaTileEntity, side, ForgeDirection.UP, colorIndex, active, redstoneLevel);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer player, float x, float y, float z,
        ItemStack tool) {
        if (player.isSneaking()) {
            mDisableFilter = !mDisableFilter;
            GTUtility.sendChatTrans(player, "GT5U.hatch.disableFilter." + mDisableFilter);
        } else {
            setAllowInputFromOutputSide(!mAllowInputFromOutputSide);
            GTUtility.sendChatTrans(
                player,
                mAllowInputFromOutputSide ? "gt.interact.desc.input_from_output_on"
                    : "gt.interact.desc.input_from_output_off");
        }
    }

    public boolean isOutputItems() {
        return mOutputItems;
    }

    public void setOutputItems(boolean outputItems) {
        mOutputItems = outputItems;
    }

    public boolean isVoidAll() {
        return mVoidAll;
    }

    public void setVoidAll(boolean voidAll) {
        mVoidAll = voidAll;
        if (voidAll) {
            super.setVoidOverflow(false);
            lockItems(false);
        }
    }

    @Override
    public void setVoidOverflow(boolean voidOverflow) {
        if (voidOverflow) mVoidAll = false;
        super.setVoidOverflow(voidOverflow);
    }

    public boolean isLockItems() {
        return mLockItems;
    }

    public void lockItems(boolean lockItems) {
        if (mVoidAll && lockItems) return;
        mLockItems = lockItems;
        if (!lockItems) {
            lockedItem = null;
        } else if (lockedItem == null && GTUtility.isStackValid(getItemStack())) {
            setLockedItem(getItemStack());
        }
    }

    public boolean isAllowInputFromOutputSide() {
        return mAllowInputFromOutputSide;
    }

    public void setAllowInputFromOutputSide(boolean allowInputFromOutputSide) {
        mAllowInputFromOutputSide = allowInputFromOutputSide;
    }

    public boolean isOutputToSlot() {
        return mOutputToSlot;
    }

    public void setOutputToSlot(boolean outputToSlot) {
        mOutputToSlot = outputToSlot;
    }

    public ItemStack getLockedItem() {
        return lockedItem;
    }

    public void setLockedItem(ItemStack item) {
        if (mVoidAll && GTUtility.isStackValid(item)) return;
        if (GTUtility.isStackInvalid(item)) {
            lockedItem = null;
            mLockItems = false;
        } else if (GTUtility.isStackInvalid(getItemStack()) || GTUtility.areStacksEqual(getItemStack(), item)) {
            lockedItem = GTUtility.copyAmountUnsafe(1, item);
            mLockItems = true;
        }
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTESuperChestGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public String[] getDescription() {
        String[] description = super.getDescription().clone();
        description[1] = "Use a screwdriver to toggle input from the output side";
        description[2] = "Sneak with a screwdriver to toggle the item filter";
        return description;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }
}
