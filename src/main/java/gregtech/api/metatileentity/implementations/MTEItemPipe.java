package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.ALL_VALID_SIDES;
import static gregtech.api.enums.Textures.BlockIcons.PIPE_RESTRICTOR;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityItemPipe;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;

public class MTEItemPipe extends MetaPipeEntity implements IMetaTileEntityItemPipe {

    public final float mThickNess;
    public final Materials mMaterial;
    public final int mStepSize;
    public final int mTickTime;
    public int mTransferredItems = 0;
    public long mCurrentTransferStartTick = 0;
    public ForgeDirection mLastReceivedFrom = ForgeDirection.UNKNOWN, oLastReceivedFrom = ForgeDirection.UNKNOWN;
    public boolean mIsRestrictive = false;
    private int[] cacheSides;

    public MTEItemPipe(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial,
        int aInvSlotCount, int aStepSize, boolean aIsRestrictive, int aTickTime) {
        super(aID, aName, aNameRegional, aInvSlotCount, false);
        mIsRestrictive = aIsRestrictive;
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mStepSize = aStepSize;
        mTickTime = aTickTime;
        addInfo(aID);
    }

    public MTEItemPipe(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial,
        int aInvSlotCount, int aStepSize, boolean aIsRestrictive) {
        this(aID, aName, aNameRegional, aThickNess, aMaterial, aInvSlotCount, aStepSize, aIsRestrictive, 20);
    }

    public MTEItemPipe(String aName, float aThickNess, Materials aMaterial, int aInvSlotCount, int aStepSize,
        boolean aIsRestrictive, int aTickTime) {
        super(aName, aInvSlotCount);
        mIsRestrictive = aIsRestrictive;
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mStepSize = aStepSize;
        mTickTime = aTickTime;
    }

    @Override
    public byte getTileEntityBaseType() {
        return (byte) (mMaterial == null ? 4 : (byte) (4) + Math.max(0, Math.min(3, mMaterial.mToolQuality)));
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEItemPipe(mName, mThickNess, mMaterial, mInventory.length, mStepSize, mIsRestrictive, mTickTime);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int aColorIndex, boolean aConnected, boolean redstoneLevel) {
        if (mIsRestrictive) {
            if (aConnected) {
                float tThickNess = getThickness();
                if (tThickNess < 0.124F) return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                if (tThickNess < 0.374F) // 0.375
                    return new ITexture[] { TextureFactory.of(
                        mMaterial.mIconSet.mTextures[OrePrefixes.pipeTiny.mTextureIndex],
                        Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                if (tThickNess < 0.499F) // 0.500
                    return new ITexture[] { TextureFactory.of(
                        mMaterial.mIconSet.mTextures[OrePrefixes.pipeSmall.mTextureIndex],
                        Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                if (tThickNess < 0.749F) // 0.750
                    return new ITexture[] { TextureFactory.of(
                        mMaterial.mIconSet.mTextures[OrePrefixes.pipeMedium.mTextureIndex],
                        Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                if (tThickNess < 0.874F) // 0.825
                    return new ITexture[] { TextureFactory.of(
                        mMaterial.mIconSet.mTextures[OrePrefixes.pipeLarge.mTextureIndex],
                        Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeHuge.mTextureIndex],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
            }
            return new ITexture[] { TextureFactory.of(
                mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
        }
        if (aConnected) {
            float tThickNess = getThickness();
            if (tThickNess < 0.124F) return new ITexture[] { TextureFactory.of(
                mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            if (tThickNess < 0.374F) // 0.375
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeTiny.mTextureIndex],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            if (tThickNess < 0.499F) // 0.500
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeSmall.mTextureIndex],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            if (tThickNess < 0.749F) // 0.750
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeMedium.mTextureIndex],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            if (tThickNess < 0.874F) // 0.825
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeLarge.mTextureIndex],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            return new ITexture[] { TextureFactory.of(
                mMaterial.mIconSet.mTextures[OrePrefixes.pipeHuge.mTextureIndex],
                Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
        }
        return new ITexture[] { TextureFactory.of(
            mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
            Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return false;
    }

    @Override
    public boolean isValidSlot(int ignoredSlotIndex) {
        return true;
    }

    @Override
    public final boolean renderInside(ForgeDirection side) {
        return false;
    }

    @Override
    public int getProgresstime() {
        return getPipeContent() * 64;
    }

    @Override
    public int maxProgresstime() {
        return getMaxPipeCapacity() * 64;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mLastReceivedFrom", (byte) mLastReceivedFrom.ordinal());
        if (GTMod.proxy.gt6Pipe) aNBT.setByte("mConnections", mConnections);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mLastReceivedFrom = ForgeDirection.getOrientation(aNBT.getByte("mLastReceivedFrom"));
        if (GTMod.proxy.gt6Pipe) {
            mConnections = aNBT.getByte("mConnections");
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && (aTick - mCurrentTransferStartTick) % 10 == 0) {
            if ((aTick - mCurrentTransferStartTick) % mTickTime == 0) {
                mTransferredItems = 0;
                mCurrentTransferStartTick = 0;
            }

            if (!GTMod.proxy.gt6Pipe || mCheckConnections) checkConnections();

            doTickProfilingInThisTick = true;
            if (oLastReceivedFrom == mLastReceivedFrom) {
                doTickProfilingInThisTick = false;

                final ArrayList<IMetaTileEntityItemPipe> tPipeList = new ArrayList<>();

                for (boolean temp = true; temp && !isInventoryEmpty() && pipeCapacityCheck();) {
                    temp = false;
                    tPipeList.clear();
                    for (IMetaTileEntityItemPipe tTileEntity : GTUtility
                        .sortMapByValuesAcending(
                            IMetaTileEntityItemPipe.Util.scanPipes(this, new HashMap<>(), 0, false, false))
                        .keySet()) {
                        if (temp) break;
                        tPipeList.add(tTileEntity);
                        while (!temp && !isInventoryEmpty() && tTileEntity.sendItemStack(aBaseMetaTileEntity))
                            for (IMetaTileEntityItemPipe tPipe : tPipeList)
                                if (!tPipe.incrementTransferCounter(1)) temp = true;
                    }
                }
            }

            if (isInventoryEmpty()) mLastReceivedFrom = ForgeDirection.UNKNOWN;
            oLastReceivedFrom = mLastReceivedFrom;
        }
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (GTMod.proxy.gt6Pipe) {
            final ForgeDirection tSide = GTUtility.determineWrenchingSide(side, aX, aY, aZ);
            if (isConnectedAtSide(tSide)) {
                disconnect(tSide);
                GTUtility.sendChatToPlayer(entityPlayer, GTUtility.trans("215", "Disconnected"));
            } else {
                if (connect(tSide) > 0) GTUtility.sendChatToPlayer(entityPlayer, GTUtility.trans("214", "Connected"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean letsIn(Cover cover) {
        return cover.letsItemsOut(-1);
    }

    @Override
    public boolean letsOut(Cover cover) {
        return cover.letsItemsOut(-1);
    }

    @Override
    public boolean canConnect(ForgeDirection side, TileEntity tileEntity) {
        if (tileEntity == null) return false;

        final ForgeDirection oppositeSide = side.getOpposite();
        boolean connectable = GTUtility.isConnectableNonInventoryPipe(tileEntity, oppositeSide);

        final IGregTechTileEntity gTileEntity = (tileEntity instanceof IGregTechTileEntity)
            ? (IGregTechTileEntity) tileEntity
            : null;
        if (gTileEntity != null) {
            if (gTileEntity.getMetaTileEntity() == null) return false;
            if (gTileEntity.getMetaTileEntity()
                .connectsToItemPipe(oppositeSide)) return true;
            connectable = true;
        }

        if (tileEntity instanceof IInventory) {
            if (((IInventory) tileEntity).getSizeInventory() <= 0) return false;
            connectable = true;
        }
        if (tileEntity instanceof ISidedInventory) {
            final int[] tSlots = ((ISidedInventory) tileEntity).getAccessibleSlotsFromSide(oppositeSide.ordinal());
            if (tSlots == null || tSlots.length == 0) return false;
            connectable = true;
        }

        return connectable;
    }

    @Override
    public boolean getGT6StyleConnection() {
        // Yes if GT6 pipes are enabled
        return GTMod.proxy.gt6Pipe;
    }

    @Override
    public boolean incrementTransferCounter(int aIncrement) {
        if (mTransferredItems == 0) mCurrentTransferStartTick = getBaseMetaTileEntity().getTimer();
        mTransferredItems += aIncrement;
        return pipeCapacityCheck();
    }

    @Override
    public boolean sendItemStack(Object aSender) {
        if (pipeCapacityCheck()) {
            final byte tOffset = (byte) getBaseMetaTileEntity().getRandomNumber(6);
            for (final byte i : ALL_VALID_SIDES) {
                final ForgeDirection tSide = ForgeDirection.getOrientation((i + tOffset) % 6);
                if (isConnectedAtSide(tSide)
                    && (isInventoryEmpty() || (tSide != mLastReceivedFrom || aSender != getBaseMetaTileEntity()))) {
                    if (insertItemStackIntoTileEntity(aSender, tSide)) return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean insertItemStackIntoTileEntity(Object aSender, ForgeDirection side) {
        if (getBaseMetaTileEntity().getCoverAtSide(side)
            .letsItemsOut(-1)) {
            final TileEntity tInventory = getBaseMetaTileEntity().getTileEntityAtSide(side);
            if (tInventory != null && !(tInventory instanceof BaseMetaPipeEntity)) {
                if ((!(tInventory instanceof TileEntityHopper) && !(tInventory instanceof TileEntityDispenser))
                    || getBaseMetaTileEntity().getMetaIDAtSide(side) != side.getOpposite()
                        .ordinal()) {
                    return GTUtility.moveMultipleItemStacks(
                        aSender,
                        tInventory,
                        ForgeDirection.UNKNOWN,
                        side.getOpposite(),
                        null,
                        false,
                        (byte) 64,
                        (byte) 1,
                        (byte) 64,
                        (byte) 1,
                        1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public boolean pipeCapacityCheck() {
        return mTransferredItems <= 0 || getPipeContent() < getMaxPipeCapacity();
    }

    private int getPipeContent() {
        return mTransferredItems;
    }

    private int getMaxPipeCapacity() {
        return Math.max(1, getPipeCapacity());
    }

    /**
     * Amount of ItemStacks this Pipe can conduct per Second.
     */
    public int getPipeCapacity() {
        return mInventory.length;
    }

    @Override
    public int getStepSize() {
        return mStepSize;
    }

    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {
        return isConnectedAtSide(ForgeDirection.getOrientation(ordinalSide))
            && super.canInsertItem(aIndex, aStack, ordinalSide);
    }

    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int ordinalSide) {
        return isConnectedAtSide(ForgeDirection.getOrientation(ordinalSide));
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        final IGregTechTileEntity tTileEntity = getBaseMetaTileEntity();
        final Cover cover = tTileEntity.getCoverAtSide(ForgeDirection.getOrientation(ordinalSide));
        final boolean tAllow = cover.letsItemsIn(-2) || cover.letsItemsOut(-2);
        if (tAllow) {
            if (cacheSides == null) cacheSides = super.getAccessibleSlotsFromSide(ordinalSide);
            return cacheSides;
        } else {
            return GTValues.emptyIntArray;
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return isConnectedAtSide(side);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (!isConnectedAtSide(side)) return false;
        if (isInventoryEmpty()) mLastReceivedFrom = side;
        return mLastReceivedFrom == side && mInventory[aIndex] == null;
    }

    @Override
    public String[] getDescription() {
        if (mTickTime == 20) return new String[] { "Item Capacity: %%%" + getMaxPipeCapacity() + "%%% Stacks/sec",
            "Routing Value: %%%" + GTUtility.formatNumbers(mStepSize) };
        else if (mTickTime % 20 == 0) return new String[] {
            "Item Capacity: %%%" + getMaxPipeCapacity() + "%%% Stacks/%%%" + (mTickTime / 20) + "%%% sec",
            "Routing Value: %%%" + GTUtility.formatNumbers(mStepSize) };
        else return new String[] {
            "Item Capacity: %%%" + getMaxPipeCapacity() + "%%% Stacks/%%%" + mTickTime + "%%% ticks",
            "Routing Value: %%%" + GTUtility.formatNumbers(mStepSize) };
    }

    private boolean isInventoryEmpty() {
        for (ItemStack tStack : mInventory) if (tStack != null) return false;
        return true;
    }

    @Override
    public float getCollisionThickness() {
        return mThickNess;
    }
}
