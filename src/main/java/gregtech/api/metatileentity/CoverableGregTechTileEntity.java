package gregtech.api.metatileentity;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_RequestCoverData;
import gregtech.api.net.GT_Packet_SendCoverData;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.GT_Client;
import gregtech.common.covers.GT_Cover_Fluidfilter;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Arrays;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.NW;

public abstract class CoverableGregTechTileEntity extends BaseTileEntity implements IGregTechTileEntity {
    static final String[] COVER_DATA_NBT_KEYS = Arrays.stream(ForgeDirection.VALID_DIRECTIONS).mapToInt(Enum::ordinal).mapToObj(i -> "mCoverData" + i).toArray(String[]::new);
    protected final GT_CoverBehaviorBase<?>[] mCoverBehaviors = new GT_CoverBehaviorBase<?>[]{GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior};

    protected int[] mCoverSides = new int[]{0, 0, 0, 0, 0, 0};
    protected ISerializableObject[] mCoverData = new ISerializableObject[6];
    protected final boolean[] mCoverNeedUpdate = new boolean[]{false, false, false, false, false, false};
    protected short mID = 0;
    public long mTickTimer = 0;

    protected void saveCoverNBT(NBTTagCompound aNBT) {
        for (int i = 0; i < mCoverData.length; i++) {
            if (mCoverSides[i] != 0 && mCoverData[i] != null)
                aNBT.setTag(COVER_DATA_NBT_KEYS[i], mCoverData[i].saveDataToNBT());
        }
        aNBT.setIntArray("mCoverSides", mCoverSides);

    }

    protected void saveMetaTileNBT(NBTTagCompound aNBT) {
        try {
            if (hasValidMetaTileEntity()) {
                NBTTagList tItemList = new NBTTagList();
                for (int i = 0; i < getMetaTileEntity().getRealInventory().length; i++) {
                    ItemStack tStack = getMetaTileEntity().getRealInventory()[i];
                    if (tStack != null) {
                        NBTTagCompound tTag = new NBTTagCompound();
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
            NBTTagList tItemList = aNBT.getTagList("Inventory", 10);
            for (int i = 0; i < tItemList.tagCount(); i++) {
                NBTTagCompound tTag = tItemList.getCompoundTagAt(i);
                int tSlot = tTag.getInteger("IntSlot");
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

    protected void loadCoverNBT(NBTTagCompound aNBT) {
        mCoverSides = aNBT.getIntArray("mCoverSides");
        if (mCoverSides.length != 6) mCoverSides = new int[]{0, 0, 0, 0, 0, 0};
        for (byte i = 0; i < 6; i++) mCoverBehaviors[i] = GregTech_API.getCoverBehaviorNew(mCoverSides[i]);

        // check old form of data
        mCoverData = new ISerializableObject[6];
        if (aNBT.hasKey("mCoverData", 11) && aNBT.getIntArray("mCoverData").length == 6) {
            int[] tOldData = aNBT.getIntArray("mCoverData");
            for (int i = 0; i < tOldData.length; i++) {
                if(mCoverBehaviors[i] instanceof GT_Cover_Fluidfilter) {
                    final String filterKey = String.format("fluidFilter%d", i);
                    if (aNBT.hasKey(filterKey)) {
                        mCoverData[i] = mCoverBehaviors[i].createDataObject((tOldData[i] & 7) | (FluidRegistry.getFluidID(aNBT.getString(filterKey)) << 3));
                    }
                } else if (mCoverBehaviors[i] != null && mCoverBehaviors[i] != GregTech_API.sNoBehavior) {
                    mCoverData[i] = mCoverBehaviors[i].createDataObject(tOldData[i]);
                }
            }
        } else {
            // no old data
            for (byte i = 0; i<6; i++) {
                if (mCoverBehaviors[i] == null)
                    continue;
                if (aNBT.hasKey(COVER_DATA_NBT_KEYS[i]))
                    mCoverData[i] = mCoverBehaviors[i].createDataObject(aNBT.getTag(COVER_DATA_NBT_KEYS[i]));
                else
                    mCoverData[i] = mCoverBehaviors[i].createDataObject();
            }
        }
    }

    protected boolean doCoverThings() {
        for (byte i = 0; i < 6; i++)
            if (getCoverIDAtSide(i) != 0) {
                GT_CoverBehaviorBase<?> tCover = getCoverBehaviorAtSideNew(i);
                int tCoverTickRate = tCover.getTickRate(i, getCoverIDAtSide(i), mCoverData[i], this);
                if (tCoverTickRate > 0 && mTickTimer % tCoverTickRate == 0) {
                    byte tRedstone = tCover.isRedstoneSensitive(i, getCoverIDAtSide(i), mCoverData[i], this, mTickTimer) ? getInputRedstoneSignal(i) : 0;
                    mCoverData[i] = tCover.doCoverThings(i, tRedstone, getCoverIDAtSide(i), mCoverData[i], this, mTickTimer);
                    if (!hasValidMetaTileEntity()) return false;
                }
            }
        return true;
    }

    protected void checkDropCover() {
        for (byte i = 0; i < 6; i++)
            if (getCoverIDAtSide(i) != 0)
                if (!getMetaTileEntity().allowCoverOnSide(i, new GT_ItemStack(getCoverIDAtSide(i))))
                    dropCover(i, i, true);
    }

    protected void updateCoverBehavior() {
        for (byte i = 0; i < 6; i++)
            mCoverBehaviors[i] = GregTech_API.getCoverBehaviorNew(mCoverSides[i]);
    }

    @Override
    public void issueCoverUpdate(byte aSide) {
        if (isServerSide() && getCoverBehaviorAtSideNew(aSide).isDataNeededOnClient(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this))
            mCoverNeedUpdate[aSide] = true;
    }

	public ITexture getCoverTexture(byte aSide) {
		if (getCoverIDAtSide(aSide) == 0) return null;
		if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x1) != 0) {
			return Textures.BlockIcons.HIDDEN_TEXTURE[0]; // See through
		}
        ITexture coverTexture = getCoverBehaviorAtSideNew(aSide).getSpecialCoverTexture(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this);
       return coverTexture != null ? coverTexture : GregTech_API.sCovers.get(new GT_ItemStack(getCoverIDAtSide(aSide)));
    }

    protected void requestCoverDataIfNeeded() {
        for (byte i = 0; i < 6; i++) {
            if (getCoverBehaviorAtSideNew(i).isDataNeededOnClient(i, getCoverIDAtSide(i), getComplexCoverDataAtSide(i), this))
                NW.sendToServer(new GT_Packet_RequestCoverData(i, getCoverIDAtSide(i), this));
        }
    }

	protected abstract boolean hasValidMetaTileEntity();

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

    @Override
    public void setCoverIDAtSide(byte aSide, int aID) {
        if (aSide >= 0 && aSide < 6 && mCoverSides[aSide] != aID) {
            mCoverSides[aSide] = aID;
            mCoverBehaviors[aSide] = GregTech_API.getCoverBehaviorNew(aID);
            mCoverData[aSide] = mCoverBehaviors[aSide].createDataObject();
            issueCoverUpdate(aSide);
            issueBlockUpdate();
        }
    }

    @Override
    @Deprecated
    public GT_CoverBehavior getCoverBehaviorAtSide(byte aSide) {
        if (aSide >= 0 && aSide < mCoverBehaviors.length && mCoverBehaviors[aSide] instanceof GT_CoverBehavior)
            return (GT_CoverBehavior) mCoverBehaviors[aSide];
        return GregTech_API.sNoBehavior;
    }

    @Override
    public void setCoverItemAtSide(byte aSide, ItemStack aCover) {
        GregTech_API.getCoverBehaviorNew(aCover).placeCover(aSide, aCover, this);
    }

    @Override
    public int getCoverIDAtSide(byte aSide) {
        if (aSide >= 0 && aSide < 6) return mCoverSides[aSide];
        return 0;
    }

    @Override
    public ItemStack getCoverItemAtSide(byte aSide) {
        return GT_Utility.intToStack(getCoverIDAtSide(aSide));
    }

    @Override
    public boolean canPlaceCoverIDAtSide(byte aSide, int aID) {
        return getCoverIDAtSide(aSide) == 0;
    }

    @Override
    public boolean canPlaceCoverItemAtSide(byte aSide, ItemStack aCover) {
        return getCoverIDAtSide(aSide) == 0;
    }

    @Override
    @Deprecated
    public int getCoverDataAtSide(byte aSide) {
        if (aSide >= 0 && aSide < 6 && mCoverData[aSide] instanceof ISerializableObject.LegacyCoverData)
            return ((ISerializableObject.LegacyCoverData) mCoverData[aSide]).get();
        return 0;
    }

    @Override
    @Deprecated
    public void setCoverDataAtSide(byte aSide, int aData) {
        if (aSide >= 0 && aSide < 6 && mCoverData[aSide] instanceof ISerializableObject.LegacyCoverData)
            mCoverData[aSide] = new ISerializableObject.LegacyCoverData(aData);
    }

    @Override
    public void setCoverDataAtSide(byte aSide, ISerializableObject aData) {
        if (aSide >= 0 && aSide < 6 && getCoverBehaviorAtSideNew(aSide) != null && getCoverBehaviorAtSideNew(aSide).cast(aData) != null)
            mCoverData[aSide] = aData;
    }

    @Override
    public ISerializableObject getComplexCoverDataAtSide(byte aSide) {
        if (aSide >= 0 && aSide < 6 && getCoverBehaviorAtSideNew(aSide) != null)
            return mCoverData[aSide];
        return GregTech_API.sNoBehavior.createDataObject();
    }

    @Override
    public GT_CoverBehaviorBase<?> getCoverBehaviorAtSideNew(byte aSide) {
        if (aSide >= 0 && aSide < 6)
            return mCoverBehaviors[aSide];
        return GregTech_API.sNoBehavior;
    }

    @Override
    public boolean dropCover(byte aSide, byte aDroppedSide, boolean aForced) {
        if (getCoverBehaviorAtSideNew(aSide).onCoverRemoval(aSide, getCoverIDAtSide(aSide), mCoverData[aSide], this, aForced) || aForced) {
            ItemStack tStack = getCoverBehaviorAtSideNew(aSide).getDrop(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this);
            if (tStack != null) {
                getCoverBehaviorAtSideNew(aSide).onDropped(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this);
                EntityItem tEntity = new EntityItem(worldObj, getOffsetX(aDroppedSide, 1) + 0.5, getOffsetY(aDroppedSide, 1) + 0.5, getOffsetZ(aDroppedSide, 1) + 0.5, tStack);
                tEntity.motionX = 0;
                tEntity.motionY = 0;
                tEntity.motionZ = 0;
                worldObj.spawnEntityInWorld(tEntity);
            }
            setCoverIDAtSide(aSide, 0);
            updateOutputRedstoneSignal(aSide);

            return true;
        }
        return false;
    }
    
    protected void updateOutputRedstoneSignal(byte aSide) {
        setOutputRedstoneSignal(aSide, (byte) 0);
    }

    @Override
    public void receiveCoverData(byte coverSide, int coverID, int coverData) {
        if ((coverSide >= 0 && coverSide < 6) && (mCoverSides[coverSide] == coverID))
            setCoverDataAtSide(coverSide, coverData);
    }

    @Override
    public void receiveCoverData(byte aCoverSide, int aCoverID, ISerializableObject aCoverData, EntityPlayerMP aPlayer) {
        if ((aCoverSide >= 0 && aCoverSide < 6) && (mCoverSides[aCoverSide] == aCoverID)) {
            setCoverDataAtSide(aCoverSide, aCoverData);
            if (isClientSide())
                getCoverBehaviorAtSideNew(aCoverSide).onDataChanged(aCoverSide, aCoverID, aCoverData, this);
        }
    }

    protected void sendCoverDataIfNeeded() {
        int mCoverNeedUpdateLength = mCoverNeedUpdate.length;
        for (byte i = 0; i < mCoverNeedUpdateLength; i++) {
            if (mCoverNeedUpdate[i]) {
                NW.sendPacketToAllPlayersInRange(
                        worldObj,
                        new GT_Packet_SendCoverData(
                                i, getCoverIDAtSide(i), getComplexCoverDataAtSide(i), this),
                        xCoord, zCoord
                );
                mCoverNeedUpdate[i] = false;
            }
        }
    }
}
