package gregtech.api.metatileentity;

import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.NW;
import static gregtech.api.util.GT_LanguageManager.FACES;
import static gregtech.api.util.GT_LanguageManager.getTranslation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
            final CoverInfo coverInfo = getCoverInfoAtSide(ForgeDirection.getOrientation(i));
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
            if (coverInfo.isDataNeededOnClient()) issueCoverUpdate(ForgeDirection.getOrientation(i));
        }
    }

    public void readLegacyCoverInfoNBT(NBTTagCompound aNBT) {
        final int[] coverIDs = aNBT.hasKey("mCoverSides") ? aNBT.getIntArray("mCoverSides")
            : new int[] { 0, 0, 0, 0, 0, 0 };
        final boolean hasOldCoverData = (aNBT.hasKey("mCoverData", 11) && aNBT.getIntArray("mCoverData").length == 6);
        final int[] tOldData = hasOldCoverData ? aNBT.getIntArray("mCoverData") : new int[] {};

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final int ordinalSide = side.ordinal();
            if (coverIDs[ordinalSide] == 0) continue;

            final CoverInfo coverInfo = new CoverInfo(side, coverIDs[ordinalSide], this, null);
            final GT_CoverBehaviorBase<?> coverBehavior = coverInfo.getCoverBehavior();
            if (coverBehavior == GregTech_API.sNoBehavior) continue;

            ISerializableObject coverData = null;
            if (hasOldCoverData) {
                if (coverBehavior instanceof GT_Cover_Fluidfilter) {
                    final String filterKey = String.format("fluidFilter%d", ordinalSide);
                    if (aNBT.hasKey(filterKey)) {
                        coverData = coverInfo.getCoverBehavior()
                            .createDataObject(
                                (tOldData[ordinalSide] & 7)
                                    | (FluidRegistry.getFluidID(aNBT.getString(filterKey)) << 3));
                    }
                } else {
                    coverData = coverBehavior.createDataObject(tOldData[ordinalSide]);
                }
            } else {
                if (aNBT.hasKey(COVER_DATA_NBT_KEYS[ordinalSide]))
                    coverData = coverBehavior.createDataObject(aNBT.getTag(COVER_DATA_NBT_KEYS[ordinalSide]));
            }

            if (coverData != null) coverInfo.setCoverData(coverData);
            setCoverInfoAtSide(side, coverInfo);
            if (coverInfo.isDataNeededOnClient()) issueCoverUpdate(side);
        }
    }

    public abstract boolean isStillValid();

    protected boolean doCoverThings() {
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (!tickCoverAtSide(side)) return false;
        }
        return true;
    }

    public boolean tickCoverAtSide(ForgeDirection aSide) {
        return tickCoverAtSide(aSide, mTickTimer);
    }

    /**
     * Returns false if the tile is no longer valid after ticking the cover
     */
    public boolean tickCoverAtSide(ForgeDirection aSide, long aTickTimer) {
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

    public abstract boolean allowCoverOnSide(ForgeDirection aSide, GT_ItemStack aCoverID);

    protected void checkDropCover() {
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final int coverId = getCoverIDAtSide(side);
            if (coverId != 0) if (!allowCoverOnSide(side, new GT_ItemStack(coverId))) dropCover(side, side, true);
        }
    }

    protected void updateCoverBehavior() {
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final CoverInfo coverInfo = getCoverInfoAtSide(side);
            if (coverInfo.isValid()) coverInfo.updateCoverBehavior();
        }
    }

    @Override
    public void issueCoverUpdate(ForgeDirection aSide) {
        // If we've got a null worldObj we're getting called as a part of readingNBT from a non tickable MultiTileEntity
        // on chunk load before the world is set
        // so we'll want to send a cover update.
        final CoverInfo coverInfo = getCoverInfoAtSide(aSide);
        if (worldObj == null || (isServerSide() && coverInfo.isDataNeededOnClient())) coverInfo.setNeedsUpdate(true);
    }

    public final ITexture getCoverTexture(ForgeDirection aSide) {
        final CoverInfo coverInfo = getCoverInfoAtSide(aSide);
        if (!coverInfo.isValid()) return null;
        if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x1) != 0) {
            return Textures.BlockIcons.HIDDEN_TEXTURE[0]; // See through
        }
        final ITexture coverTexture = (!(this instanceof BaseMetaPipeEntity)) ? coverInfo.getSpecialCoverFGTexture()
            : coverInfo.getSpecialCoverTexture();

        return coverTexture != null ? coverTexture
            : GregTech_API.sCovers.get(new GT_ItemStack(getCoverIDAtSide(aSide)));
    }

    protected void requestCoverDataIfNeeded() {
        if (worldObj == null || !worldObj.isRemote) return;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final CoverInfo coverInfo = getCoverInfoAtSide(side);
            if (coverInfo.isDataNeededOnClient()) NW.sendToServer(new GT_Packet_RequestCoverData(coverInfo, this));
        }
    }

    @Override
    public void setCoverIdAndDataAtSide(ForgeDirection aSide, int aId, ISerializableObject aData) {
        if (setCoverIDAtSideNoUpdate(aSide, aId, aData)) {
            issueCoverUpdate(aSide);
            issueBlockUpdate();
        }
    }

    @Override
    public void setCoverIDAtSide(ForgeDirection aSide, int aID) {
        setCoverIdAndDataAtSide(aSide, aID, null);
    }

    @Override
    public boolean setCoverIDAtSideNoUpdate(ForgeDirection aSide, int aID) {
        return setCoverIDAtSideNoUpdate(aSide, aID, null);
    }

    public boolean setCoverIDAtSideNoUpdate(ForgeDirection aSide, int aID, ISerializableObject aData) {
        final CoverInfo oldCoverInfo = getCoverInfoAtSide(aSide);
        if (aSide != ForgeDirection.UNKNOWN && oldCoverInfo.getCoverID() != aID) {
            if (aID == 0 && isClientSide()) oldCoverInfo.onDropped();
            setCoverInfoAtSide(aSide, new CoverInfo(aSide, aID, this, aData));
            return true;
        }
        return false;
    }

    @Override
    @Deprecated
    public void setCoverDataAtSide(ForgeDirection aSide, int aData) {
        final CoverInfo coverInfo = getCoverInfoAtSide(aSide);
        if (coverInfo.isValid() && coverInfo.getCoverData() instanceof ISerializableObject.LegacyCoverData)
            coverInfo.setCoverData(new ISerializableObject.LegacyCoverData(aData));
    }

    @Override
    public void setCoverDataAtSide(ForgeDirection aSide, ISerializableObject aData) {
        final CoverInfo coverInfo = getCoverInfoAtSide(aSide);
        if (coverInfo.isValid() && coverInfo.getCoverBehavior()
            .cast(aData) != null) coverInfo.setCoverData(aData);
    }

    @Override
    @Deprecated
    public GT_CoverBehavior getCoverBehaviorAtSide(ForgeDirection aSide) {
        final GT_CoverBehaviorBase<?> behavior = getCoverInfoAtSide(aSide).getCoverBehavior();
        if (behavior instanceof GT_CoverBehavior) return (GT_CoverBehavior) behavior;
        return GregTech_API.sNoBehavior;
    }

    @Override
    public void setCoverItemAtSide(ForgeDirection aSide, ItemStack aCover) {
        GregTech_API.getCoverBehaviorNew(aCover)
            .placeCover(aSide, aCover, this);
    }

    @Override
    public int getCoverIDAtSide(ForgeDirection aSide) {
        return getCoverInfoAtSide(aSide).getCoverID();
    }

    @Override
    public ItemStack getCoverItemAtSide(ForgeDirection aSide) {
        return getCoverInfoAtSide(aSide).getDisplayStack();
    }

    @Override
    public boolean canPlaceCoverIDAtSide(ForgeDirection aSide, int aID) {
        return getCoverIDAtSide(aSide) == 0;
    }

    @Override
    public boolean canPlaceCoverItemAtSide(ForgeDirection aSide, ItemStack aCover) {
        return getCoverIDAtSide(aSide) == 0;
    }

    @Override
    @Deprecated
    public int getCoverDataAtSide(ForgeDirection aSide) {
        final ISerializableObject coverData = getCoverInfoAtSide(aSide).getCoverData();
        if (coverData instanceof ISerializableObject.LegacyCoverData) {
            return ((ISerializableObject.LegacyCoverData) coverData).get();
        }
        return 0;
    }

    @Override
    public ISerializableObject getComplexCoverDataAtSide(ForgeDirection aSide) {
        return getCoverInfoAtSide(aSide).getCoverData();
    }

    @Override
    public GT_CoverBehaviorBase<?> getCoverBehaviorAtSideNew(ForgeDirection aSide) {
        return getCoverInfoAtSide(aSide).getCoverBehavior();
    }

    public void setCoverInfoAtSide(ForgeDirection aSide, CoverInfo coverInfo) {
        if (aSide != ForgeDirection.UNKNOWN) coverInfos[aSide.ordinal()] = coverInfo;
    }

    @Override
    public CoverInfo getCoverInfoAtSide(ForgeDirection aSide) {
        final int ordinalSide = aSide.ordinal();
        if (aSide != ForgeDirection.UNKNOWN) {
            if (coverInfos[ordinalSide] == null) coverInfos[ordinalSide] = new CoverInfo(aSide, this);
            return coverInfos[aSide.ordinal()];
        }
        return CoverInfo.EMPTY_INFO;
    }

    public void clearCoverInfoAtSide(ForgeDirection aSide) {
        if (aSide != ForgeDirection.UNKNOWN) {
            setCoverIDAtSide(aSide, 0);
        }
    }

    @Override
    public boolean dropCover(ForgeDirection aSide, ForgeDirection aDroppedSide, boolean aForced) {
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
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final CoverInfo coverInfo = getCoverInfoAtSide(side);
            if (coverInfo.isValid()) coverInfo.onBaseTEDestroyed();
        }
    }

    @Override
    public void setOutputRedstoneSignal(ForgeDirection aSide, byte strength) {
        final byte cappedStrength = (byte) Math.min(Math.max(0, strength), 15);
        if (aSide == ForgeDirection.UNKNOWN) return;

        final int ordinalSide = aSide.ordinal();
        if (mSidedRedstone[ordinalSide] != cappedStrength || (mStrongRedstone & (1 << ordinalSide)) > 0) {
            if ((mStrongRedstone & (1 << ordinalSide)) > 0) {
                mStrongRedstone ^= (1 << ordinalSide);
                issueBlockUpdate();
            }
            mSidedRedstone[ordinalSide] = cappedStrength;
            issueBlockUpdate();
        }
    }

    @Override
    public void setStrongOutputRedstoneSignal(ForgeDirection aSide, byte strength) {
        final byte cappedStrength = (byte) Math.min(Math.max(0, strength), 15);
        if (aSide == ForgeDirection.UNKNOWN) return;

        final int ordinalSide = aSide.ordinal();
        if (mSidedRedstone[ordinalSide] != cappedStrength || (mStrongRedstone & (1 << ordinalSide)) == 0) {
            mStrongRedstone |= (1 << ordinalSide);
            mSidedRedstone[ordinalSide] = cappedStrength;
            issueBlockUpdate();
        }
    }

    @Override
    public void setInternalOutputRedstoneSignal(ForgeDirection aSide, byte aStrength) {
        if (!getCoverBehaviorAtSideNew(aSide)
            .manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this))
            setOutputRedstoneSignal(aSide, aStrength);
    }

    @Override
    public boolean getRedstone() {
        return Arrays.stream(ForgeDirection.VALID_DIRECTIONS)
            .anyMatch(this::getRedstone);
    }

    @Override
    public boolean getRedstone(ForgeDirection aSide) {
        return getInternalInputRedstoneSignal(aSide) > 0;
    }

    @Override
    public byte getStrongestRedstone() {
        return Arrays.stream(ForgeDirection.VALID_DIRECTIONS)
            .map(this::getInternalInputRedstoneSignal)
            .max(Comparator.comparing(Byte::valueOf))
            .orElse((byte) 0);
    }

    @Override
    public byte getStrongOutputRedstoneSignal(ForgeDirection aSide) {
        final int ordinalSide = aSide.ordinal();
        return aSide != ForgeDirection.UNKNOWN && (mStrongRedstone & (1 << ordinalSide)) != 0
            ? (byte) (mSidedRedstone[ordinalSide] & 15)
            : 0;
    }

    @Override
    public void setGenericRedstoneOutput(boolean aOnOff) {
        mRedstone = aOnOff;
    }

    @Override
    public byte getInternalInputRedstoneSignal(ForgeDirection aSide) {
        return (byte) (getCoverBehaviorAtSideNew(aSide).getRedstoneInput(
            aSide,
            getInputRedstoneSignal(aSide),
            getCoverIDAtSide(aSide),
            getComplexCoverDataAtSide(aSide),
            this) & 15);
    }

    @Override
    public byte getInputRedstoneSignal(ForgeDirection aSide) {
        return (byte) (worldObj
            .getIndirectPowerLevelTo(getOffsetX(aSide, 1), getOffsetY(aSide, 1), getOffsetZ(aSide, 1), aSide.ordinal())
            & 15);
    }

    @Override
    public byte getOutputRedstoneSignal(ForgeDirection aSide) {
        return getCoverBehaviorAtSideNew(aSide)
            .manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this)
                ? mSidedRedstone[aSide.ordinal()]
                : getGeneralRS(aSide);
    }

    protected void updateOutputRedstoneSignal(ForgeDirection aSide) {
        setOutputRedstoneSignal(aSide, (byte) 0);
    }

    @Override
    public void receiveCoverData(byte ordinalSide, int aCoverID, int aCoverData) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        if (side == ForgeDirection.UNKNOWN) return;
        final CoverInfo oldCoverInfo = getCoverInfoAtSide(side);
        if (!oldCoverInfo.isValid()) return;

        setCoverIDAtSideNoUpdate(side, aCoverID);
        setCoverDataAtSide(side, aCoverData);
    }

    @Override
    public void receiveCoverData(byte ordinalSide, int aCoverID, ISerializableObject aCoverData,
        EntityPlayerMP aPlayer) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        if (side == ForgeDirection.UNKNOWN) return;

        final CoverInfo oldCoverInfo = getCoverInfoAtSide(side);

        if (!oldCoverInfo.isValid()) return;
        oldCoverInfo.preDataChanged(aCoverID, aCoverData);
        setCoverIDAtSideNoUpdate(side, aCoverID, aCoverData);
        setCoverDataAtSide(side, aCoverData);

        if (isClientSide()) {
            getCoverInfoAtSide(side).onDataChanged();
        }
    }

    protected void sendCoverDataIfNeeded() {
        if (worldObj == null || worldObj.isRemote) return;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final CoverInfo coverInfo = getCoverInfoAtSide(side);
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
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();
        final ForgeDirection currentFacing = accessor.getSide();

        final NBTTagList tList = tag.getTagList(GT_Values.NBT.COVERS, 10);
        for (byte i = 0; i < tList.tagCount(); i++) {
            final NBTTagCompound tNBT = tList.getCompoundTagAt(i);
            final CoverInfo coverInfo = new CoverInfo(this, tNBT);
            if (!coverInfo.isValid() || coverInfo.getCoverBehavior() == GregTech_API.sNoBehavior) continue;

            final ItemStack coverStack = coverInfo.getDisplayStack();
            if (coverStack != null) {
                currentTip.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.waila.cover",
                        currentFacing == coverInfo.getSide()
                            ? StatCollector.translateToLocal("GT5U.waila.cover.current_facing")
                            : StatCollector.translateToLocal(
                                "GT5U.interface.coverTabs." + coverInfo.getSide()
                                    .toString()
                                    .toLowerCase()),
                        coverStack.getDisplayName()));
                final String behaviorDesc = coverInfo.getBehaviorDescription();
                if (!Objects.equals(behaviorDesc, E)) currentTip.add(behaviorDesc);
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
                        getTranslation(
                            FACES[coverInfo.getSide()
                                .ordinal()]),
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

    protected ModularWindow createCoverWindow(EntityPlayer player, ForgeDirection side) {
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

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final byte ordinalSide = (byte) side.ordinal();
            buildContext
                .addSyncedWindow(ordinalSide + COVER_WINDOW_ID_START, player -> createCoverWindow(player, side));
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
    protected List<String> getCoverTabTooltip(ForgeDirection side) {
        final String[] SIDE_TOOLTIPS = new String[] { "GT5U.interface.coverTabs.down", "GT5U.interface.coverTabs.up",
            "GT5U.interface.coverTabs.north", "GT5U.interface.coverTabs.south", "GT5U.interface.coverTabs.west",
            "GT5U.interface.coverTabs.east" };
        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        final ItemStack coverItem = coverInfo.getDisplayStack();
        if (coverItem == null) return Collections.emptyList();
        final boolean coverHasGUI = coverInfo.hasCoverGUI();

        final List<String> tooltip = coverItem.getTooltip(Minecraft.getMinecraft().thePlayer, true);
        for (int i = 0; i < tooltip.size(); i++) {
            if (i == 0) {
                tooltip.set(
                    0,
                    (coverHasGUI ? EnumChatFormatting.UNDERLINE : EnumChatFormatting.DARK_GRAY)
                        + StatCollector.translateToLocal(SIDE_TOOLTIPS[side.ordinal()])
                        + (coverHasGUI ? EnumChatFormatting.RESET + ": " : ": " + EnumChatFormatting.RESET)
                        + tooltip.get(0));
            } else {
                tooltip.set(i, EnumChatFormatting.GRAY + tooltip.get(i));
            }
        }
        return tooltip;
    }

    protected void onTabClicked(Widget.ClickData ignoredClickData, Widget widget, ForgeDirection side) {
        if (isClientSide()) return;
        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        if (coverInfo.useModularUI()) {
            widget.getContext()
                .openSyncedWindow(side.ordinal() + COVER_WINDOW_ID_START);
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
