package gregtech.api.metatileentity;

import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.NW;
import static gregtech.api.util.GT_LanguageManager.FACES;
import static gregtech.api.util.GT_LanguageManager.getTranslation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.math.MainAxisAlignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.Column;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregtechWailaProvider;
import gregtech.api.net.GT_Packet_RequestCoverData;
import gregtech.api.net.GT_Packet_SendCoverData;
import gregtech.api.net.GT_Packet_TileEntityCoverGUI;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;
import gregtech.common.GT_Client;
import gregtech.common.covers.CoverInfo;
import gregtech.common.covers.GT_Cover_Fluidfilter;

public abstract class CoverableTileEntity extends BaseTileEntity implements ICoverable, IGregtechWailaProvider {

    public static final String[] COVER_DATA_NBT_KEYS = Arrays.stream(ForgeDirection.VALID_DIRECTIONS)
        .mapToInt(Enum::ordinal)
        .mapToObj(i -> "mCoverData" + i)
        .toArray(String[]::new);

    // New Cover Information
    protected final CoverInfo[] coverInfos = new CoverInfo[] { null, null, null, null, null, null };

    protected byte[] mSidedRedstone = new byte[] { 15, 15, 15, 15, 15, 15 };
    protected boolean mRedstone = false;
    protected byte mStrongRedstone = 0;

    /* Deprecated Cover Variables */
    @Deprecated
    protected final GT_CoverBehaviorBase<?>[] mCoverBehaviors = new GT_CoverBehaviorBase<?>[] {
        GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior,
        GregTech_API.sNoBehavior, GregTech_API.sNoBehavior };

    @Deprecated
    protected int[] mCoverSides = new int[] { 0, 0, 0, 0, 0, 0 };

    @Deprecated
    protected ISerializableObject[] mCoverData = new ISerializableObject[6];

    @Deprecated
    protected final boolean[] mCoverNeedUpdate = new boolean[] { false, false, false, false, false, false };
    /* End Deprecated Cover Variables */

    protected short mID = 0;
    public long mTickTimer = 0;

    protected void writeCoverNBT(NBTTagCompound aNBT, boolean isDrop) {
        final NBTTagList tList = new NBTTagList();
        final int[] coverSides = new int[] { 0, 0, 0, 0, 0, 0 };

        for (byte i = 0; i < coverInfos.length; i++) {
            final CoverInfo coverInfo = getCoverInfoAtSide(i);
            if (!coverInfo.isValid()) continue;

            // Backwards compat, in case of a revert... for now
            tList.appendTag(coverInfo.writeToNBT(new NBTTagCompound()));
            aNBT.setTag(
                COVER_DATA_NBT_KEYS[i],
                coverInfo.getCoverData()
                    .saveDataToNBT());
        }
        if (tList.tagCount() > 0) {
            aNBT.setTag(GT_Values.NBT.COVERS, tList);
            // Backwards compat, in case of a revert... for now
            aNBT.setIntArray("mCoverSides", coverSides);
        }

        if (mStrongRedstone > 0) aNBT.setByte("mStrongRedstone", mStrongRedstone);

        if (!isDrop) {
            aNBT.setByteArray("mRedstoneSided", mSidedRedstone);
            aNBT.setBoolean("mRedstone", mRedstone);
        }
    }

    protected void readCoverNBT(NBTTagCompound aNBT) {
        mRedstone = aNBT.getBoolean("mRedstone");
        mSidedRedstone = aNBT.hasKey("mRedstoneSided") ? aNBT.getByteArray("mRedstoneSided")
            : new byte[] { 15, 15, 15, 15, 15, 15 };
        mStrongRedstone = aNBT.getByte("mStrongRedstone");

        if (aNBT.hasKey(GT_Values.NBT.COVERS)) {
            readCoverInfoNBT(aNBT);
        } else if (aNBT.hasKey("mCoverSides")) {
            readLegacyCoverInfoNBT(aNBT);
        }
    }

    public void readCoverInfoNBT(NBTTagCompound aNBT) {
        final NBTTagList tList = aNBT.getTagList(GT_Values.NBT.COVERS, 10);
        for (byte i = 0; i < tList.tagCount(); i++) {
            final NBTTagCompound tNBT = tList.getCompoundTagAt(i);
            final CoverInfo coverInfo = new CoverInfo(this, tNBT);
            this.setCoverInfoAtSide(coverInfo.getSide(), coverInfo);
            if (coverInfo.isDataNeededOnClient()) issueCoverUpdate(i);
        }
    }

    public void readLegacyCoverInfoNBT(NBTTagCompound aNBT) {
        final int[] coverIDs = aNBT.hasKey("mCoverSides") ? aNBT.getIntArray("mCoverSides")
            : new int[] { 0, 0, 0, 0, 0, 0 };
        final boolean hasOldCoverData = (aNBT.hasKey("mCoverData", 11) && aNBT.getIntArray("mCoverData").length == 6);
        final int[] tOldData = hasOldCoverData ? aNBT.getIntArray("mCoverData") : new int[] {};

        for (byte i : ALL_VALID_SIDES) {
            if (coverIDs[i] == 0) continue;

            final CoverInfo coverInfo = new CoverInfo(i, coverIDs[i], this, null);
            final GT_CoverBehaviorBase<?> coverBehavior = coverInfo.getCoverBehavior();
            if (coverBehavior == GregTech_API.sNoBehavior) continue;

            ISerializableObject coverData = null;
            if (hasOldCoverData) {
                if (coverBehavior instanceof GT_Cover_Fluidfilter) {
                    final String filterKey = String.format("fluidFilter%d", i);
                    if (aNBT.hasKey(filterKey)) {
                        coverData = coverInfo.getCoverBehavior()
                            .createDataObject(
                                (tOldData[i] & 7) | (FluidRegistry.getFluidID(aNBT.getString(filterKey)) << 3));
                    }
                } else {
                    coverData = coverBehavior.createDataObject(tOldData[i]);
                }
            } else {
                if (aNBT.hasKey(COVER_DATA_NBT_KEYS[i]))
                    coverData = coverBehavior.createDataObject(aNBT.getTag(COVER_DATA_NBT_KEYS[i]));
            }

            if (coverData != null) coverInfo.setCoverData(coverData);
            setCoverInfoAtSide(i, coverInfo);
            if (coverInfo.isDataNeededOnClient()) issueCoverUpdate(i);
        }
    }

    public abstract boolean isStillValid();

    protected boolean doCoverThings() {
        for (byte i : ALL_VALID_SIDES) {
            if (!tickCoverAtSide(i)) return false;
        }
        return true;
    }

    public boolean tickCoverAtSide(byte aSide) {
        return tickCoverAtSide(aSide, mTickTimer);
    }

    /**
     * Returns false if the tile is no longer valid after ticking the cover
     */
    public boolean tickCoverAtSide(byte aSide, long aTickTimer) {
        final CoverInfo coverInfo = getCoverInfoAtSide(aSide);
        if (!coverInfo.isValid()) return true;
        final int tCoverTickRate = coverInfo.getTickRate();
        if (tCoverTickRate > 0 && aTickTimer % tCoverTickRate == 0) {
            final byte tRedstone = coverInfo.isRedstoneSensitive(aTickTimer) ? getInputRedstoneSignal(aSide) : 0;
            coverInfo.setCoverData(coverInfo.doCoverThings(aTickTimer, tRedstone));
            return isStillValid();
        }

        return true;
    }

    public abstract boolean allowCoverOnSide(byte aSide, GT_ItemStack aCoverID);

    protected void checkDropCover() {
        for (byte i : ALL_VALID_SIDES) {
            final int coverId = getCoverIDAtSide(i);
            if (coverId != 0) if (!allowCoverOnSide(i, new GT_ItemStack(coverId))) dropCover(i, i, true);
        }
    }

    protected void updateCoverBehavior() {
        for (byte i : ALL_VALID_SIDES) {
            final CoverInfo coverInfo = getCoverInfoAtSide(i);
            if (coverInfo.isValid()) coverInfo.updateCoverBehavior();
        }
    }

    @Override
    public void issueCoverUpdate(byte aSide) {
        // If we've got a null worldObj we're getting called as a part of readingNBT from a non tickable MultiTileEntity
        // on chunk load before the world is set
        // so we'll want to send a cover update.
        final CoverInfo coverInfo = getCoverInfoAtSide(aSide);
        if (worldObj == null || (isServerSide() && coverInfo.isDataNeededOnClient())) coverInfo.setNeedsUpdate(true);
    }

    /**
     * Gets the cover texture for the given side
     * 
     * @deprecated use {@link #getCoverTexture(ForgeDirection)}
     *
     * @param aSide the {@code byte} ordinal side direction
     * @return the {@link ITexture} of the cover at this side
     */
    @Deprecated
    public final ITexture getCoverTexture(byte aSide) {
        return getCoverTexture(ForgeDirection.getOrientation(aSide));
    }

    /**
     * Gets the cover texture for the given side
     *
     * @param dir the {@link ForgeDirection} of the side
     * @return the {@link ITexture} of the cover at this side
     */
    public final ITexture getCoverTexture(ForgeDirection dir) {
        final CoverInfo coverInfo = getCoverInfoAtSide((byte) dir.ordinal());
        if (!coverInfo.isValid()) return null;
        if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x1) != 0) {
            return Textures.BlockIcons.HIDDEN_TEXTURE[0]; // See through
        }
        final ITexture coverTexture = (!(this instanceof BaseMetaPipeEntity)) ? coverInfo.getSpecialCoverFGTexture()
            : coverInfo.getSpecialCoverTexture();

        return coverTexture != null ? coverTexture
            : GregTech_API.sCovers.get(new GT_ItemStack(getCoverIDAtSide((byte) dir.ordinal())));
    }

    protected void requestCoverDataIfNeeded() {
        if (worldObj == null || !worldObj.isRemote) return;
        for (byte i : ALL_VALID_SIDES) {
            final CoverInfo coverInfo = getCoverInfoAtSide(i);
            if (coverInfo.isDataNeededOnClient()) NW.sendToServer(new GT_Packet_RequestCoverData(coverInfo, this));
        }
    }

    @Override
    public void setCoverIdAndDataAtSide(byte aSide, int aId, ISerializableObject aData) {
        if (setCoverIDAtSideNoUpdate(aSide, aId, aData)) {
            issueCoverUpdate(aSide);
            issueBlockUpdate();
        }
    }

    @Override
    public void setCoverIDAtSide(byte aSide, int aID) {
        setCoverIdAndDataAtSide(aSide, aID, null);
    }

    @Override
    public boolean setCoverIDAtSideNoUpdate(byte aSide, int aID) {
        return setCoverIDAtSideNoUpdate(aSide, aID, null);
    }

    public boolean setCoverIDAtSideNoUpdate(byte aSide, int aID, ISerializableObject aData) {
        final CoverInfo oldCoverInfo = getCoverInfoAtSide(aSide);
        if (aSide >= 0 && aSide < 6 && oldCoverInfo.getCoverID() != aID) {
            if (aID == 0 && isClientSide()) oldCoverInfo.onDropped();
            setCoverInfoAtSide(aSide, new CoverInfo(aSide, aID, this, aData));
            return true;
        }
        return false;
    }

    @Override
    @Deprecated
    public void setCoverDataAtSide(byte aSide, int aData) {
        final CoverInfo coverInfo = getCoverInfoAtSide(aSide);
        if (coverInfo.isValid() && coverInfo.getCoverData() instanceof ISerializableObject.LegacyCoverData)
            coverInfo.setCoverData(new ISerializableObject.LegacyCoverData(aData));
    }

    @Override
    public void setCoverDataAtSide(byte aSide, ISerializableObject aData) {
        final CoverInfo coverInfo = getCoverInfoAtSide(aSide);
        if (coverInfo.isValid() && coverInfo.getCoverBehavior()
            .cast(aData) != null) coverInfo.setCoverData(aData);
    }

    @Override
    @Deprecated
    public GT_CoverBehavior getCoverBehaviorAtSide(byte aSide) {
        final GT_CoverBehaviorBase<?> behavior = getCoverInfoAtSide(aSide).getCoverBehavior();
        if (behavior instanceof GT_CoverBehavior) return (GT_CoverBehavior) behavior;
        return GregTech_API.sNoBehavior;
    }

    @Override
    public void setCoverItemAtSide(byte aSide, ItemStack aCover) {
        GregTech_API.getCoverBehaviorNew(aCover)
            .placeCover(aSide, aCover, this);
    }

    @Override
    public int getCoverIDAtSide(byte aSide) {
        return getCoverInfoAtSide(aSide).getCoverID();
    }

    @Override
    public ItemStack getCoverItemAtSide(byte aSide) {
        return getCoverInfoAtSide(aSide).getDisplayStack();
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
        final ISerializableObject coverData = getCoverInfoAtSide(aSide).getCoverData();
        if (coverData instanceof ISerializableObject.LegacyCoverData) {
            return ((ISerializableObject.LegacyCoverData) coverData).get();
        }
        return 0;
    }

    @Override
    public ISerializableObject getComplexCoverDataAtSide(byte aSide) {
        return getCoverInfoAtSide(aSide).getCoverData();
    }

    @Override
    public GT_CoverBehaviorBase<?> getCoverBehaviorAtSideNew(byte aSide) {
        return getCoverInfoAtSide(aSide).getCoverBehavior();
    }

    public void setCoverInfoAtSide(byte aSide, CoverInfo coverInfo) {
        if (aSide >= 0 && aSide < 6) coverInfos[aSide] = coverInfo;
    }

    @Override
    public CoverInfo getCoverInfoAtSide(byte aSide) {
        if (aSide >= 0 && aSide < 6) {
            if (coverInfos[aSide] == null) coverInfos[aSide] = new CoverInfo(aSide, this);
            return coverInfos[aSide];
        }
        return CoverInfo.EMPTY_INFO;
    }

    public void clearCoverInfoAtSide(byte aSide) {
        if (aSide >= 0 && aSide < 6) {
            setCoverIDAtSide(aSide, 0);
        }
    }

    @Override
    public boolean dropCover(byte aSide, byte aDroppedSide, boolean aForced) {
        final CoverInfo coverInfo = getCoverInfoAtSide(aSide);
        if (!coverInfo.isValid()) return false;
        if (!coverInfo.onCoverRemoval(aForced) && !aForced) return false;
        final ItemStack tStack = coverInfo.getDrop();
        if (tStack != null) {
            coverInfo.onDropped();
            final EntityItem tEntity = new EntityItem(
                worldObj,
                getOffsetX(aDroppedSide, 1) + 0.5,
                getOffsetY(aDroppedSide, 1) + 0.5,
                getOffsetZ(aDroppedSide, 1) + 0.5,
                tStack);
            tEntity.motionX = 0;
            tEntity.motionY = 0;
            tEntity.motionZ = 0;
            worldObj.spawnEntityInWorld(tEntity);
        }
        clearCoverInfoAtSide(aSide);
        updateOutputRedstoneSignal(aSide);

        return true;
    }

    protected void onBaseTEDestroyed() {
        for (byte side : ALL_VALID_SIDES) {
            final CoverInfo coverInfo = getCoverInfoAtSide(side);
            if (coverInfo.isValid()) coverInfo.onBaseTEDestroyed();
        }
    }

    @Override
    public void setOutputRedstoneSignal(byte aSide, byte aStrength) {
        aStrength = (byte) Math.min(Math.max(0, aStrength), 15);
        if (aSide < 0 || aSide >= 6) return;

        if (mSidedRedstone[aSide] != aStrength || (mStrongRedstone & (1 << aSide)) > 0) {
            if ((mStrongRedstone & (1 << aSide)) > 0) {
                mStrongRedstone ^= (1 << aSide);
                issueBlockUpdate();
            }
            mSidedRedstone[aSide] = aStrength;
            issueBlockUpdate();
        }
    }

    @Override
    public void setStrongOutputRedstoneSignal(byte aSide, byte aStrength) {
        aStrength = (byte) Math.min(Math.max(0, aStrength), 15);
        if (aSide < 0 || aSide >= 6) return;

        if (mSidedRedstone[aSide] != aStrength || (mStrongRedstone & (1 << aSide)) == 0) {
            mStrongRedstone |= (1 << aSide);
            mSidedRedstone[aSide] = aStrength;
            issueBlockUpdate();
        }
    }

    @Override
    public void setInternalOutputRedstoneSignal(byte aSide, byte aStrength) {
        if (!getCoverBehaviorAtSideNew(aSide)
            .manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this))
            setOutputRedstoneSignal(aSide, aStrength);
    }

    @Override
    public boolean getRedstone() {
        return IntStream.range(1, 6)
            .anyMatch(i -> getRedstone((byte) i));
    }

    @Override
    public boolean getRedstone(byte aSide) {
        return getInternalInputRedstoneSignal(aSide) > 0;
    }

    @Override
    public byte getStrongestRedstone() {
        return (byte) IntStream.range(1, 6)
            .map(i -> getInternalInputRedstoneSignal((byte) i))
            .max()
            .orElse(0);
    }

    @Override
    public byte getStrongOutputRedstoneSignal(byte aSide) {
        return aSide >= 0 && aSide < 6 && (mStrongRedstone & (1 << aSide)) != 0 ? (byte) (mSidedRedstone[aSide] & 15)
            : 0;
    }

    @Override
    public void setGenericRedstoneOutput(boolean aOnOff) {
        mRedstone = aOnOff;
    }

    @Override
    public byte getInternalInputRedstoneSignal(byte aSide) {
        return (byte) (getCoverBehaviorAtSideNew(aSide).getRedstoneInput(
            aSide,
            getInputRedstoneSignal(aSide),
            getCoverIDAtSide(aSide),
            getComplexCoverDataAtSide(aSide),
            this) & 15);
    }

    @Override
    public byte getInputRedstoneSignal(byte aSide) {
        return (byte) (worldObj
            .getIndirectPowerLevelTo(getOffsetX(aSide, 1), getOffsetY(aSide, 1), getOffsetZ(aSide, 1), aSide) & 15);
    }

    @Override
    public byte getOutputRedstoneSignal(byte aSide) {
        return getCoverBehaviorAtSideNew(aSide)
            .manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this)
                ? mSidedRedstone[aSide]
                : getGeneralRS(aSide);
    }

    protected void updateOutputRedstoneSignal(byte aSide) {
        setOutputRedstoneSignal(aSide, (byte) 0);
    }

    @Override
    public void receiveCoverData(byte aCoverSide, int aCoverID, int aCoverData) {
        if (aCoverSide < 0 || aCoverSide >= 6) return;
        final CoverInfo oldCoverInfo = getCoverInfoAtSide(aCoverSide);
        if (!oldCoverInfo.isValid()) return;

        setCoverIDAtSideNoUpdate(aCoverSide, aCoverID);
        setCoverDataAtSide(aCoverSide, aCoverData);
    }

    @Override
    public void receiveCoverData(byte aCoverSide, int aCoverID, ISerializableObject aCoverData,
        EntityPlayerMP aPlayer) {
        if (aCoverSide < 0 || aCoverSide >= 6) return;

        final CoverInfo oldCoverInfo = getCoverInfoAtSide(aCoverSide);

        if (!oldCoverInfo.isValid()) return;
        oldCoverInfo.preDataChanged(aCoverID, aCoverData);
        setCoverIDAtSideNoUpdate(aCoverSide, aCoverID, aCoverData);
        setCoverDataAtSide(aCoverSide, aCoverData);

        if (isClientSide()) {
            getCoverInfoAtSide(aCoverSide).onDataChanged();
        }
    }

    protected void sendCoverDataIfNeeded() {
        if (worldObj == null || worldObj.isRemote) return;
        for (byte i : ALL_VALID_SIDES) {
            final CoverInfo coverInfo = getCoverInfoAtSide(i);
            if (coverInfo.needsUpdate()) {
                NW.sendPacketToAllPlayersInRange(
                    worldObj,
                    new GT_Packet_SendCoverData(coverInfo, this),
                    xCoord,
                    zCoord);
                coverInfo.setNeedsUpdate(false);
            }
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();
        final byte currentFacing = (byte) accessor.getSide()
            .ordinal();

        final NBTTagList tList = tag.getTagList(GT_Values.NBT.COVERS, 10);
        for (byte i = 0; i < tList.tagCount(); i++) {
            final NBTTagCompound tNBT = tList.getCompoundTagAt(i);
            final CoverInfo coverInfo = new CoverInfo(this, tNBT);
            if (!coverInfo.isValid() || coverInfo.getCoverBehavior() == GregTech_API.sNoBehavior) continue;

            final ItemStack coverStack = coverInfo.getDisplayStack();
            if (coverStack != null) {
                currenttip.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.waila.cover",
                        currentFacing == coverInfo.getSide()
                            ? StatCollector.translateToLocal("GT5U.waila.cover.current_facing")
                            : StatCollector.translateToLocal(
                                "GT5U.interface.coverTabs." + ForgeDirection.getOrientation(coverInfo.getSide())
                                    .toString()
                                    .toLowerCase()),
                        coverStack.getDisplayName()));
                final String behaviorDesc = coverInfo.getBehaviorDescription();
                if (!Objects.equals(behaviorDesc, E)) currenttip.add(behaviorDesc);
            }
        }

        // No super implementation
        // super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        // No super implementation
        // super.getWailaNBTData(player, tile, tag, world, x, y, z);

        // While we have some cover data on the client (enough to render it); we don't have all the information we want,
        // such as details on the fluid filter, so send it all here.
        writeCoverNBT(tag, false);
    }

    /**
     * Add installed cover information, generally called from ItemBlock
     *
     * @param aNBT  - NBTTagCompound from the stack
     * @param aList - List to add the information to
     */
    public static void addInstalledCoversInformation(NBTTagCompound aNBT, List<String> aList) {
        final NBTTagList tList = aNBT.getTagList(GT_Values.NBT.COVERS, 10);
        for (byte i = 0; i < tList.tagCount(); i++) {
            final NBTTagCompound tNBT = tList.getCompoundTagAt(i);
            final CoverInfo coverInfo = new CoverInfo(null, tNBT);
            if (!coverInfo.isValid() || coverInfo.getCoverBehavior() == GregTech_API.sNoBehavior) continue;

            final ItemStack coverStack = coverInfo.getDisplayStack();
            if (coverStack != null) {
                aList.add(
                    String.format(
                        "Cover on %s side: %s",
                        getTranslation(FACES[coverInfo.getSide()]),
                        coverStack.getDisplayName()));
            }
        }

        if (aNBT.hasKey("mCoverSides")) {
            final int[] mCoverSides = aNBT.getIntArray("mCoverSides");
            if (mCoverSides != null && mCoverSides.length == 6) {
                for (byte tSide : ALL_VALID_SIDES) {
                    final int coverId = mCoverSides[tSide];
                    if (coverId == 0) continue;
                    final GT_CoverBehaviorBase<?> behavior = GregTech_API.getCoverBehaviorNew(coverId);
                    if (behavior == null || behavior == GregTech_API.sNoBehavior) continue;
                    if (!aNBT.hasKey(CoverableTileEntity.COVER_DATA_NBT_KEYS[tSide])) continue;
                    final ISerializableObject dataObject = behavior
                        .createDataObject(aNBT.getTag(CoverableTileEntity.COVER_DATA_NBT_KEYS[tSide]));
                    final ItemStack coverStack = behavior.getDisplayStack(coverId, dataObject);
                    if (coverStack != null) {
                        aList.add(
                            String.format(
                                "Cover on %s side: %s",
                                getTranslation(FACES[tSide]),
                                coverStack.getDisplayName()));
                    }
                }
            }
        }
    }

    protected ModularWindow createCoverWindow(EntityPlayer player, byte side) {
        return getCoverInfoAtSide(side).createWindow(player);
    }

    protected static final int COVER_WINDOW_ID_START = 1;

    @Override
    public void addCoverTabs(ModularWindow.Builder builder, UIBuildContext buildContext) {
        final int COVER_TAB_LEFT = -16, COVER_TAB_TOP = 1, COVER_TAB_HEIGHT = 20, COVER_TAB_WIDTH = 18,
            COVER_TAB_SPACING = 2, ICON_SIZE = 16;
        final boolean flipHorizontally = GT_Mod.gregtechproxy.mCoverTabsFlipped;

        final Column columnWidget = new Column();
        builder.widget(columnWidget);
        final int xPos = flipHorizontally ? (getGUIWidth() - COVER_TAB_LEFT - COVER_TAB_WIDTH) : COVER_TAB_LEFT;
        if (GT_Mod.gregtechproxy.mCoverTabsVisible) {
            columnWidget.setPos(xPos, COVER_TAB_TOP)
                .setEnabled(
                    widget -> ((Column) widget).getChildren()
                        .stream()
                        .anyMatch(Widget::isEnabled));
        } else {
            columnWidget.setEnabled(false);
        }
        columnWidget.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
            .setSpace(COVER_TAB_SPACING);

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            final byte side = (byte) direction.ordinal();
            buildContext.addSyncedWindow(side + COVER_WINDOW_ID_START, player -> createCoverWindow(player, side));
            columnWidget.addChild(new MultiChildWidget().addChild(new ButtonWidget() {

                @Override
                public IDrawable[] getBackground() {
                    final List<IDrawable> backgrounds = new ArrayList<>();
                    final GUITextureSet tabIconSet = getGUITextureSet();

                    if (getCoverBehaviorAtSideNew(side).hasCoverGUI()) {
                        if (isHovering()) {
                            backgrounds.add(
                                flipHorizontally ? tabIconSet.getCoverTabHighlightFlipped()
                                    : tabIconSet.getCoverTabHighlight());
                        } else {
                            backgrounds.add(
                                flipHorizontally ? tabIconSet.getCoverTabNormalFlipped()
                                    : tabIconSet.getCoverTabNormal());
                        }
                    } else {
                        backgrounds.add(
                            flipHorizontally ? tabIconSet.getCoverTabDisabledFlipped()
                                : tabIconSet.getCoverTabDisabled());
                    }
                    return backgrounds.toArray(new IDrawable[] {});
                }
            }.setOnClick((clickData, widget) -> onTabClicked(clickData, widget, side))
                .dynamicTooltip(() -> getCoverTabTooltip(side))
                .setSize(COVER_TAB_WIDTH, COVER_TAB_HEIGHT))
                .addChild(
                    new ItemDrawable(() -> getCoverItemAtSide(side)).asWidget()
                        .setPos(
                            (COVER_TAB_WIDTH - ICON_SIZE) / 2 + (flipHorizontally ? -1 : 1),
                            (COVER_TAB_HEIGHT - ICON_SIZE) / 2))
                .setEnabled(widget -> getCoverItemAtSide(side) != null));
        }
    }

    @SideOnly(Side.CLIENT)
    protected List<String> getCoverTabTooltip(byte side) {
        final String[] SIDE_TOOLTIPS = new String[] { "GT5U.interface.coverTabs.down", "GT5U.interface.coverTabs.up",
            "GT5U.interface.coverTabs.north", "GT5U.interface.coverTabs.south", "GT5U.interface.coverTabs.west",
            "GT5U.interface.coverTabs.east" };
        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        final ItemStack coverItem = coverInfo.getDisplayStack();
        if (coverItem == null) return Collections.emptyList();
        final boolean coverHasGUI = coverInfo.hasCoverGUI();

        // noinspection unchecked
        final List<String> tooltip = coverItem.getTooltip(Minecraft.getMinecraft().thePlayer, true);
        for (int i = 0; i < tooltip.size(); i++) {
            if (i == 0) {
                tooltip.set(
                    0,
                    (coverHasGUI ? EnumChatFormatting.UNDERLINE : EnumChatFormatting.DARK_GRAY)
                        + StatCollector.translateToLocal(SIDE_TOOLTIPS[side])
                        + (coverHasGUI ? EnumChatFormatting.RESET + ": " : ": " + EnumChatFormatting.RESET)
                        + tooltip.get(0));
            } else {
                tooltip.set(i, EnumChatFormatting.GRAY + tooltip.get(i));
            }
        }
        return tooltip;
    }

    protected void onTabClicked(Widget.ClickData clickData, Widget widget, byte side) {
        if (isClientSide()) return;
        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        if (coverInfo.useModularUI()) {
            widget.getContext()
                .openSyncedWindow(side + COVER_WINDOW_ID_START);
        } else {
            final GT_Packet_TileEntityCoverGUI packet = new GT_Packet_TileEntityCoverGUI(
                coverInfo,
                getWorld().provider.dimensionId,
                widget.getContext()
                    .getPlayer()
                    .getEntityId(),
                0);
            GT_Values.NW.sendToPlayer(
                packet,
                (EntityPlayerMP) widget.getContext()
                    .getPlayer());
        }
    }
}
