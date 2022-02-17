package gregtech.api.metatileentity;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.ALL_SIDES;

public abstract class CommonMetaTileEntity extends CoverableTileEntity implements IGregTechTileEntity {
    protected boolean mNeedsBlockUpdate = true, mNeedsUpdate = true, mSendClientData = false, mInventoryChanged = false;

    protected boolean createNewMetatileEntity(short aID) {
        if (aID <= 0 || aID >= GregTech_API.METATILEENTITIES.length || GregTech_API.METATILEENTITIES[aID] == null) {
            GT_Log.err.println("MetaID " + aID + " not loadable => locking TileEntity!");
        } else {
            if (hasValidMetaTileEntity()) getMetaTileEntity().setBaseMetaTileEntity(null);
            GregTech_API.METATILEENTITIES[aID].newMetaEntity(this).setBaseMetaTileEntity(this);
            mTickTimer = 0;
            mID = aID;
            return true;
        }
        return false;
    }
    protected void saveMetaTileNBT(NBTTagCompound aNBT) {
        try {
            if (hasValidMetaTileEntity()) {
                final NBTTagList tItemList = new NBTTagList();
                for (int i = 0; i < getMetaTileEntity().getRealInventory().length; i++) {
                    final ItemStack tStack = getMetaTileEntity().getRealInventory()[i];
                    if (tStack != null) {
                        final NBTTagCompound tTag = new NBTTagCompound();
                        tTag.setInteger("IntSlot", i);
                        tStack.writeToNBT(tTag);
                        tItemList.appendTag(tTag);
                    }
                }
                aNBT.setTag("Inventory", tItemList);

                try {
                    getMetaTileEntity().saveNBTData(aNBT);
                } catch (Throwable e) {
                    GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity.");
                    GT_Mod.logStackTrace(e);
                }
            }
        } catch (Throwable e) {
            GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity.");
            GT_Mod.logStackTrace(e);
        }
    }

    protected void loadMetaTileNBT(NBTTagCompound aNBT) {
        if (mID != 0 && createNewMetatileEntity(mID)) {
            final NBTTagList tItemList = aNBT.getTagList("Inventory", 10);
            for (int i = 0; i < tItemList.tagCount(); i++) {
                final NBTTagCompound tTag = tItemList.getCompoundTagAt(i);
                final int tSlot = tTag.getInteger("IntSlot");
                if (tSlot >= 0 && tSlot < getMetaTileEntity().getRealInventory().length) {
                    getMetaTileEntity().getRealInventory()[tSlot] = GT_Utility.loadItem(tTag);
                }
            }

            try {
                getMetaTileEntity().loadNBTData(aNBT);
            } catch (Throwable e) {
                GT_FML_LOGGER.error("Encountered Exception while loading MetaTileEntity.");
                GT_Mod.logStackTrace(e);
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        mInventoryChanged = true;
    }

    @Override
    public boolean hasInventoryBeenModified() {
        return mInventoryChanged;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        if (canAccessData()) return getMetaTileEntity().isValidSlot(aIndex);
        return false;
    }

    @Override
    public Packet getDescriptionPacket() {
        issueClientUpdate();
        return null;
    }

    @Override
    public void issueTextureUpdate() {
        mNeedsUpdate = true;
    }

    @Override
    public void issueClientUpdate() {
        mSendClientData = true;
    }

    @Override
    public void issueBlockUpdate() {
        mNeedsBlockUpdate = true;
    }

    @Override
    public boolean isValidFacing(byte aSide) {
        if (canAccessData()) return getMetaTileEntity().isFacingValid(aSide);
        return false;
    }

    protected boolean canAccessData() {
        return !isDead && hasValidMetaTileEntity();
    }

    protected abstract boolean hasValidMetaTileEntity();

    @Override
    public String[] getDescription() {
        if (canAccessData()) return getMetaTileEntity().getDescription();
        return new String[0];
    }

    @Override
    public boolean isStillValid() {
        return hasValidMetaTileEntity();
    }

    @Override
    public boolean allowCoverOnSide(byte aSide, GT_ItemStack aCoverID) {
        return hasValidMetaTileEntity() && getMetaTileEntity().allowCoverOnSide(aSide, aCoverID);
    }

    @Override
    public void issueCoverUpdate(byte aSide) {
        super.issueCoverUpdate(aSide);
        issueClientUpdate();
    }

    /*
     * IC2 Energy Compat
     */
    @Override
    public boolean shouldJoinIc2Enet() {
        final IMetaTileEntity meta = getMetaTileEntity();
        return meta != null && meta.shouldJoinIc2Enet();
    }

}
