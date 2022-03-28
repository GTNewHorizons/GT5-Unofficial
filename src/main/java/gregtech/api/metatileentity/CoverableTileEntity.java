package gregtech.api.metatileentity;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.tileentity.IGregtechWailaProvider;
import gregtech.api.net.GT_Packet_RequestCoverData;
import gregtech.api.net.GT_Packet_SendCoverData;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;
import gregtech.common.GT_Client;
import gregtech.common.covers.GT_Cover_Fluidfilter;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.List;

import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.NW;
import static gregtech.api.util.GT_LanguageManager.FACES;
import static gregtech.api.util.GT_LanguageManager.getTranslation;

public abstract class CoverableTileEntity extends BaseTileEntity implements ICoverable, IGregtechWailaProvider {
    public static final String[] COVER_DATA_NBT_KEYS = Arrays.stream(ForgeDirection.VALID_DIRECTIONS).mapToInt(Enum::ordinal).mapToObj(i -> "mCoverData" + i).toArray(String[]::new);
    protected final GT_CoverBehaviorBase<?>[] mCoverBehaviors = new GT_CoverBehaviorBase<?>[]{GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior};
    protected byte[] mSidedRedstone = new byte[]{15, 15, 15, 15, 15, 15};
    protected boolean mRedstone = false;
    protected byte mStrongRedstone = 0;

    protected int[] mCoverSides = new int[]{0, 0, 0, 0, 0, 0};
    protected ISerializableObject[] mCoverData = new ISerializableObject[6];
    protected final boolean[] mCoverNeedUpdate = new boolean[]{false, false, false, false, false, false};
    protected short mID = 0;
    public long mTickTimer = 0;

    protected void writeCoverNBT(NBTTagCompound aNBT, boolean isDrop) {
        boolean hasCover = false;
        for (int i = 0; i < mCoverData.length; i++) {
            if (mCoverSides[i] != 0 && mCoverData[i] != null) {
                aNBT.setTag(COVER_DATA_NBT_KEYS[i], mCoverData[i].saveDataToNBT());
                hasCover = true;
            }
        }
        if (mStrongRedstone > 0) aNBT.setByte("mStrongRedstone", mStrongRedstone);
        if (hasCover) aNBT.setIntArray("mCoverSides", mCoverSides);

        if(!isDrop) {
            aNBT.setByteArray("mRedstoneSided", mSidedRedstone);
            aNBT.setBoolean("mRedstone", mRedstone);
        }

    }


    protected void readCoverNBT(NBTTagCompound aNBT) {
        mCoverSides = aNBT.hasKey("mCoverSides") ? aNBT.getIntArray("mCoverSides") : new int[]{0, 0, 0, 0, 0, 0};
        mRedstone = aNBT.getBoolean("mRedstone");
        mSidedRedstone = aNBT.hasKey("mRedstoneSided") ? aNBT.getByteArray("mRedstoneSided") : new byte[]{15, 15, 15, 15, 15, 15};
        mStrongRedstone = aNBT.getByte("mStrongRedstone");

        for (byte i = 0; i < 6; i++) mCoverBehaviors[i] = GregTech_API.getCoverBehaviorNew(mCoverSides[i]);

        // check old form of data
        mCoverData = new ISerializableObject[6];
        if (aNBT.hasKey("mCoverData", 11) && aNBT.getIntArray("mCoverData").length == 6) {
            final int[] tOldData = aNBT.getIntArray("mCoverData");
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
                if (mCoverBehaviors[i] == null) continue;
                if (aNBT.hasKey(COVER_DATA_NBT_KEYS[i]))
                    mCoverData[i] = mCoverBehaviors[i].createDataObject(aNBT.getTag(COVER_DATA_NBT_KEYS[i]));
                else
                    mCoverData[i] = mCoverBehaviors[i].createDataObject();
                if (mCoverBehaviors[i].isDataNeededOnClient(i, mCoverSides[i], mCoverData[i], this))
                    issueCoverUpdate(i);
            }
        }

    }
    public abstract boolean isStillValid();

    protected boolean doCoverThings() {
        for (byte i : ALL_VALID_SIDES) {
            if (getCoverIDAtSide(i) != 0) {
                final GT_CoverBehaviorBase<?> tCover = getCoverBehaviorAtSideNew(i);
                final int tCoverTickRate = tCover.getTickRate(i, getCoverIDAtSide(i), mCoverData[i], this);
                if (tCoverTickRate > 0 && mTickTimer % tCoverTickRate == 0) {
                    final byte tRedstone = tCover.isRedstoneSensitive(i, getCoverIDAtSide(i), mCoverData[i], this, mTickTimer) ? getInputRedstoneSignal(i) : 0;
                    mCoverData[i] = tCover.doCoverThings(i, tRedstone, getCoverIDAtSide(i), mCoverData[i], this, mTickTimer);
                    if (!isStillValid()) return false;
                }
            }
        }
        return true;
    }

    public abstract boolean allowCoverOnSide(byte aSide, GT_ItemStack aCoverID);

    protected void checkDropCover() {
        for (byte i : ALL_VALID_SIDES)
            if (getCoverIDAtSide(i) != 0)
                if (!allowCoverOnSide(i, new GT_ItemStack(getCoverIDAtSide(i))))
                    dropCover(i, i, true);
    }

    protected void updateCoverBehavior() {
        for (byte i : ALL_VALID_SIDES)
            mCoverBehaviors[i] = GregTech_API.getCoverBehaviorNew(mCoverSides[i]);
    }

    @Override
    public void issueCoverUpdate(byte aSide) {
        // If we've got a null worldObj we're getting called as a part of readingNBT from a non tickable MultiTileEntity on chunk load before the world is set
        // so we'll want to send a cover update.
        if (worldObj == null || (isServerSide() && getCoverBehaviorAtSideNew(aSide).isDataNeededOnClient(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this)))
            mCoverNeedUpdate[aSide] = true;
    }

	public final ITexture getCoverTexture(byte aSide) {
		if (getCoverIDAtSide(aSide) == 0) return null;
		if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x1) != 0) {
			return Textures.BlockIcons.HIDDEN_TEXTURE[0]; // See through
		}
        final ITexture coverTexture = getCoverBehaviorAtSideNew(aSide).getSpecialCoverTexture(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this);
        return coverTexture != null ? coverTexture : GregTech_API.sCovers.get(new GT_ItemStack(getCoverIDAtSide(aSide)));
    }

    protected void requestCoverDataIfNeeded() {
        if(worldObj == null || !worldObj.isRemote) return;
        for (byte i : ALL_VALID_SIDES) {
            if (getCoverBehaviorAtSideNew(i).isDataNeededOnClient(i, getCoverIDAtSide(i), getComplexCoverDataAtSide(i), this))
                NW.sendToServer(new GT_Packet_RequestCoverData(i, getCoverIDAtSide(i), this));
        }
    }

    @Override
    public void setCoverIdAndDataAtSide(byte aSide, int aId, ISerializableObject aData) {
        if(setCoverIDAtSideNoUpdate(aSide, aId)) {
            setCoverDataAtSide(aSide, aData);
            issueCoverUpdate(aSide);
            issueBlockUpdate();
        }
    }

    @Override
    public void setCoverIDAtSide(byte aSide, int aID) {
        if (setCoverIDAtSideNoUpdate(aSide, aID)) {
            issueCoverUpdate(aSide);
            issueBlockUpdate();
        }
    }

    @Override
    public boolean setCoverIDAtSideNoUpdate(byte aSide, int aID) {
        if (aSide >= 0 && aSide < 6 && mCoverSides[aSide] != aID) {
            if (aID == 0 && isClientSide())
                mCoverBehaviors[aSide].onDropped(aSide, mCoverSides[aSide], mCoverData[aSide], this);
            mCoverSides[aSide] = aID;
            mCoverBehaviors[aSide] = GregTech_API.getCoverBehaviorNew(aID);
            mCoverData[aSide] = mCoverBehaviors[aSide].createDataObject();
            return true;
        }
        return false;
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
        return getCoverBehaviorAtSideNew(aSide).getDisplayStack(getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide));
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
            final ItemStack tStack = getCoverBehaviorAtSideNew(aSide).getDrop(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this);
            if (tStack != null) {
                getCoverBehaviorAtSideNew(aSide).onDropped(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this);
                final EntityItem tEntity = new EntityItem(worldObj, getOffsetX(aDroppedSide, 1) + 0.5, getOffsetY(aDroppedSide, 1) + 0.5, getOffsetZ(aDroppedSide, 1) + 0.5, tStack);
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

    @Override
    public void setOutputRedstoneSignal(byte aSide, byte aStrength) {
        aStrength = (byte) Math.min(Math.max(0, aStrength), 15);
        if (aSide >= 0 && aSide < 6 && mSidedRedstone[aSide] != aStrength) {
            mSidedRedstone[aSide] = aStrength;
            issueBlockUpdate();
        }
    }

    @Override
    public void setStrongOutputRedstoneSignal(byte aSide, byte aStrength) {
        mStrongRedstone |= (1 << aSide);
        setOutputRedstoneSignal(aSide, aStrength);
    }


    @Override
    public void setInternalOutputRedstoneSignal(byte aSide, byte aStrength) {
        if (!getCoverBehaviorAtSideNew(aSide).manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this))
            setOutputRedstoneSignal(aSide, aStrength);
    }

    @Override
    public boolean getRedstone() {
        return IntStream.range(1, 6).anyMatch(i -> getRedstone((byte) i));
    }

    @Override
    public boolean getRedstone(byte aSide) {
        return getInternalInputRedstoneSignal(aSide) > 0;
    }

    @Override
    public byte getStrongestRedstone() {
        return (byte) IntStream.range(1, 6).map(i -> getInternalInputRedstoneSignal((byte) i)).max().orElse(0);
    }

    @Override
    public byte getStrongOutputRedstoneSignal(byte aSide) {
        return aSide >= 0 && aSide < 6 && (mStrongRedstone & (1 << aSide)) != 0 ? (byte) (mSidedRedstone[aSide] & 15) : 0;
    }

    @Override
    public void setGenericRedstoneOutput(boolean aOnOff) {
        mRedstone = aOnOff;
    }

    @Override
    public byte getInternalInputRedstoneSignal(byte aSide) {
        return (byte) (getCoverBehaviorAtSideNew(aSide).getRedstoneInput(aSide, getInputRedstoneSignal(aSide), getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this) & 15);
    }

    @Override
    public byte getInputRedstoneSignal(byte aSide) {
        return (byte) (worldObj.getIndirectPowerLevelTo(getOffsetX(aSide, 1), getOffsetY(aSide, 1), getOffsetZ(aSide, 1), aSide) & 15);
    }

    @Override
    public byte getOutputRedstoneSignal(byte aSide) {
        return getCoverBehaviorAtSideNew(aSide).manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this) ? mSidedRedstone[aSide] : getGeneralRS(aSide);
    }

    protected void updateOutputRedstoneSignal(byte aSide) {
        setOutputRedstoneSignal(aSide, (byte) 0);
    }

    @Override
    public void receiveCoverData(byte aCoverSide, int aCoverID, int aCoverData) {
        if ((aCoverSide >= 0 && aCoverSide < 6))
            setCoverIDAtSideNoUpdate(aCoverSide, aCoverID);
            setCoverDataAtSide(aCoverSide, aCoverData);
    }

    @Override
    public void receiveCoverData(byte aCoverSide, int aCoverID, ISerializableObject aCoverData, EntityPlayerMP aPlayer) {
        if ((aCoverSide >= 0 && aCoverSide < 6)) {
            setCoverIDAtSideNoUpdate(aCoverSide, aCoverID);
            setCoverDataAtSide(aCoverSide, aCoverData);
            if (isClientSide()) {
                getCoverBehaviorAtSideNew(aCoverSide).onDataChanged(aCoverSide, aCoverID, aCoverData, this);
            }
        }
    }

    protected void sendCoverDataIfNeeded() {
        if(worldObj == null || worldObj.isRemote) return;
        final int mCoverNeedUpdateLength = mCoverNeedUpdate.length;
        for (byte i = 0; i < mCoverNeedUpdateLength; i++) {
            if (mCoverNeedUpdate[i]) {
                NW.sendPacketToAllPlayersInRange(
                        worldObj,
                        new GT_Packet_SendCoverData(i, getCoverIDAtSide(i), getComplexCoverDataAtSide(i), this),
                        xCoord, zCoord
                );
                mCoverNeedUpdate[i] = false;
            }
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();
        final byte side = (byte)accessor.getSide().ordinal();

        final int[] coverSides = tag.getIntArray("mCoverSides");
        // Not all data is available on the client, so get it from the NBT packet
        if (coverSides != null && coverSides.length == 6 && coverSides[side] != 0) {
            final int coverId = coverSides[side];
            final GT_CoverBehaviorBase<?> behavior = GregTech_API.getCoverBehaviorNew(coverId);
            if (behavior != null && behavior != GregTech_API.sNoBehavior) {
                if (tag.hasKey(CoverableTileEntity.COVER_DATA_NBT_KEYS[side])) {
                    final ISerializableObject dataObject = behavior.createDataObject(tag.getTag(CoverableTileEntity.COVER_DATA_NBT_KEYS[side]));
                    final ItemStack coverStack = behavior.getDisplayStack(coverId, dataObject);
                    if (coverStack != null) currenttip.add(String.format("Cover: %s", coverStack.getDisplayName()));
                    final String behaviorDesc = behavior.getDescription(side, coverId, dataObject, null);
                    if (!Objects.equals(behaviorDesc, E)) currenttip.add(behaviorDesc);
                }
            }
        }

        // No super implementation
        // super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
        // No super implementation
        // super.getWailaNBTData(player, tile, tag, world, x, y, z);

        // While we have some cover data on the client (enough to render it); we don't have all the information we want, such as
        // details on the fluid filter, so send it all here.
        writeCoverNBT(tag, false);
    }

    /**
     * Add installed cover information, generally called from ItemBlock
     * @param aNBT - NBTTagCompound from the stack
     * @param aList - List to add the information to
     */
    public static void addInstalledCoversInformation(NBTTagCompound aNBT, List<String> aList) {
        if (aNBT.hasKey("mCoverSides")){
            final int[] mCoverSides = aNBT.getIntArray("mCoverSides");
            if (mCoverSides != null && mCoverSides.length == 6) {
                for (byte tSide : ALL_VALID_SIDES) {
                    final int coverId = mCoverSides[tSide];
                    if (coverId == 0) continue;
                    final GT_CoverBehaviorBase<?> behavior = GregTech_API.getCoverBehaviorNew(coverId);
                    if (behavior == null || behavior == GregTech_API.sNoBehavior) continue;
                    if (!aNBT.hasKey(CoverableTileEntity.COVER_DATA_NBT_KEYS[tSide])) continue;
                    final ISerializableObject dataObject = behavior.createDataObject(aNBT.getTag(CoverableTileEntity.COVER_DATA_NBT_KEYS[tSide]));
                    final ItemStack coverStack = behavior.getDisplayStack(coverId, dataObject);
                    if (coverStack != null) {
                        aList.add(String.format("Cover on %s side: %s", getTranslation(FACES[tSide]), coverStack.getDisplayName()));
                    }
                }
            }
        }
    }
}
