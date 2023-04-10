package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;
import static gregtech.api.enums.Textures.BlockIcons.PIPE_RESTRICTOR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityItemPipe;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.GT_Client;
import gregtech.common.covers.CoverInfo;

public class GT_MetaPipeEntity_Item extends MetaPipeEntity implements IMetaTileEntityItemPipe {

    public final float mThickNess;
    public final Materials mMaterial;
    public final int mStepSize;
    public final int mTickTime;
    public int mTransferredItems = 0;
    public byte mLastReceivedFrom = 0, oLastReceivedFrom = 0;
    public boolean mIsRestrictive = false;
    private int[] cacheSides;

    public GT_MetaPipeEntity_Item(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial,
        int aInvSlotCount, int aStepSize, boolean aIsRestrictive, int aTickTime) {
        super(aID, aName, aNameRegional, aInvSlotCount, false);
        mIsRestrictive = aIsRestrictive;
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mStepSize = aStepSize;
        mTickTime = aTickTime;
        addInfo(aID);
    }

    public GT_MetaPipeEntity_Item(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial,
        int aInvSlotCount, int aStepSize, boolean aIsRestrictive) {
        this(aID, aName, aNameRegional, aThickNess, aMaterial, aInvSlotCount, aStepSize, aIsRestrictive, 20);
    }

    public GT_MetaPipeEntity_Item(String aName, float aThickNess, Materials aMaterial, int aInvSlotCount, int aStepSize,
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
        return new GT_MetaPipeEntity_Item(
            mName,
            mThickNess,
            mMaterial,
            mInventory.length,
            mStepSize,
            mIsRestrictive,
            mTickTime);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections,
        byte aColorIndex, boolean aConnected, boolean aRedstone) {
        if (mIsRestrictive) {
            if (aConnected) {
                float tThickNess = getThickNess();
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
            float tThickNess = getThickNess();
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
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public final boolean renderInside(byte aSide) {
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
        aNBT.setByte("mLastReceivedFrom", mLastReceivedFrom);
        if (GT_Mod.gregtechproxy.gt6Pipe) aNBT.setByte("mConnections", mConnections);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mLastReceivedFrom = aNBT.getByte("mLastReceivedFrom");
        if (GT_Mod.gregtechproxy.gt6Pipe) {
            mConnections = aNBT.getByte("mConnections");
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && aTick % 10 == 0) {
            if (aTick % mTickTime == 0) mTransferredItems = 0;

            if (!GT_Mod.gregtechproxy.gt6Pipe || mCheckConnections) checkConnections();

            if (oLastReceivedFrom == mLastReceivedFrom) {
                doTickProfilingInThisTick = false;

                final ArrayList<IMetaTileEntityItemPipe> tPipeList = new ArrayList<>();

                for (boolean temp = true; temp && !isInventoryEmpty() && pipeCapacityCheck();) {
                    temp = false;
                    tPipeList.clear();
                    for (IMetaTileEntityItemPipe tTileEntity : GT_Utility
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

            if (isInventoryEmpty()) mLastReceivedFrom = 6;
            oLastReceivedFrom = mLastReceivedFrom;
        }
    }

    @Override
    public boolean onWrenchRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY,
        float aZ) {
        if (GT_Mod.gregtechproxy.gt6Pipe) {
            final byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
            if (isConnectedAtSide(tSide)) {
                disconnect(tSide);
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("215", "Disconnected"));
            } else {
                if (connect(tSide) > 0) GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("214", "Connected"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean letsIn(GT_CoverBehavior coverBehavior, byte aSide, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return coverBehavior.letsItemsIn(aSide, aCoverID, aCoverVariable, -1, aTileEntity);
    }

    @Override
    public boolean letsOut(GT_CoverBehavior coverBehavior, byte aSide, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return coverBehavior.letsItemsOut(aSide, aCoverID, aCoverVariable, -1, aTileEntity);
    }

    @Override
    public boolean letsIn(GT_CoverBehaviorBase<?> coverBehavior, byte aSide, int aCoverID,
        ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return coverBehavior.letsItemsIn(aSide, aCoverID, aCoverVariable, -1, aTileEntity);
    }

    @Override
    public boolean letsOut(GT_CoverBehaviorBase<?> coverBehavior, byte aSide, int aCoverID,
        ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return coverBehavior.letsItemsOut(aSide, aCoverID, aCoverVariable, -1, aTileEntity);
    }

    @Override
    public boolean letsIn(CoverInfo coverInfo) {
        return coverInfo.letsItemsOut(-1);
    }

    @Override
    public boolean letsOut(CoverInfo coverInfo) {
        return coverInfo.letsItemsOut(-1);
    }

    @Override
    public boolean canConnect(byte aSide, TileEntity tTileEntity) {
        if (tTileEntity == null) return false;

        final byte tSide = GT_Utility.getOppositeSide(aSide);
        boolean connectable = GT_Utility.isConnectableNonInventoryPipe(tTileEntity, tSide);

        final IGregTechTileEntity gTileEntity = (tTileEntity instanceof IGregTechTileEntity)
            ? (IGregTechTileEntity) tTileEntity
            : null;
        if (gTileEntity != null) {
            if (gTileEntity.getMetaTileEntity() == null) return false;
            if (gTileEntity.getMetaTileEntity()
                .connectsToItemPipe(tSide)) return true;
            connectable = true;
        }

        if (tTileEntity instanceof IInventory) {
            if (((IInventory) tTileEntity).getSizeInventory() <= 0) return false;
            connectable = true;
        }
        if (tTileEntity instanceof ISidedInventory) {
            final int[] tSlots = ((ISidedInventory) tTileEntity).getAccessibleSlotsFromSide(tSide);
            if (tSlots == null || tSlots.length <= 0) return false;
            connectable = true;
        }

        return connectable;
    }

    @Override
    public boolean getGT6StyleConnection() {
        // Yes if GT6 pipes are enabled
        return GT_Mod.gregtechproxy.gt6Pipe;
    }

    @Override
    public boolean incrementTransferCounter(int aIncrement) {
        mTransferredItems += aIncrement;
        return pipeCapacityCheck();
    }

    @Override
    public boolean sendItemStack(Object aSender) {
        if (pipeCapacityCheck()) {
            final byte tOffset = (byte) getBaseMetaTileEntity().getRandomNumber(6);
            byte tSide = 0;
            for (byte i : ALL_VALID_SIDES) {
                tSide = (byte) ((i + tOffset) % 6);
                if (isConnectedAtSide(tSide)
                    && (isInventoryEmpty() || (tSide != mLastReceivedFrom || aSender != getBaseMetaTileEntity()))) {
                    if (insertItemStackIntoTileEntity(aSender, tSide)) return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean insertItemStackIntoTileEntity(Object aSender, byte aSide) {
        if (getBaseMetaTileEntity().getCoverInfoAtSide(aSide)
            .letsItemsOut(-1)) {
            final TileEntity tInventory = getBaseMetaTileEntity().getTileEntityAtSide(aSide);
            if (tInventory != null && !(tInventory instanceof BaseMetaPipeEntity)) {
                if ((!(tInventory instanceof TileEntityHopper) && !(tInventory instanceof TileEntityDispenser))
                    || getBaseMetaTileEntity().getMetaIDAtSide(aSide) != GT_Utility.getOppositeSide(aSide)) {
                    return GT_Utility.moveMultipleItemStacks(
                        aSender,
                        tInventory,
                        (byte) 6,
                        GT_Utility.getOppositeSide(aSide),
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
    public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
        return isConnectedAtSide(aSide) && super.canInsertItem(aIndex, aStack, aSide);
    }

    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
        return isConnectedAtSide(aSide);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int aSide) {
        final IGregTechTileEntity tTileEntity = getBaseMetaTileEntity();
        final CoverInfo coverInfo = tTileEntity.getCoverInfoAtSide((byte) aSide);
        final boolean tAllow = coverInfo.letsItemsIn(-2) || coverInfo.letsItemsOut(-2);
        if (tAllow) {
            if (cacheSides == null) cacheSides = super.getAccessibleSlotsFromSide(aSide);
            return cacheSides;
        } else {
            return GT_Values.emptyIntArray;
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return isConnectedAtSide(aSide);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        if (!isConnectedAtSide(aSide)) return false;
        if (isInventoryEmpty()) mLastReceivedFrom = aSide;
        return mLastReceivedFrom == aSide && mInventory[aIndex] == null;
    }

    @Override
    public String[] getDescription() {
        if (mTickTime == 20) return new String[] { "Item Capacity: %%%" + getMaxPipeCapacity() + "%%% Stacks/sec",
            "Routing Value: %%%" + GT_Utility.formatNumbers(mStepSize) };
        else if (mTickTime % 20 == 0) return new String[] {
            "Item Capacity: %%%" + getMaxPipeCapacity() + "%%% Stacks/%%%" + (mTickTime / 20) + "%%% sec",
            "Routing Value: %%%" + GT_Utility.formatNumbers(mStepSize) };
        else return new String[] {
            "Item Capacity: %%%" + getMaxPipeCapacity() + "%%% Stacks/%%%" + mTickTime + "%%% ticks",
            "Routing Value: %%%" + GT_Utility.formatNumbers(mStepSize) };
    }

    private boolean isInventoryEmpty() {
        for (ItemStack tStack : mInventory) if (tStack != null) return false;
        return true;
    }

    @Override
    public float getThickNess() {
        if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x1) != 0) return 0.0625F;
        return mThickNess;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x2) != 0)
            return AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX + 1, aY + 1, aZ + 1);
        else return getActualCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    private AxisAlignedBB getActualCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        final float tSpace = (1f - mThickNess) / 2;
        float tSide0 = tSpace;
        float tSide1 = 1f - tSpace;
        float tSide2 = tSpace;
        float tSide3 = 1f - tSpace;
        float tSide4 = tSpace;
        float tSide5 = 1f - tSpace;

        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 0) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 1) != 0) {
            tSide2 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 2) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 3) != 0) {
            tSide0 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 4) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide3 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 5) != 0) {
            tSide0 = tSide2 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }

        final byte tConn = ((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections;
        if ((tConn & (1 << ForgeDirection.DOWN.ordinal())) != 0) tSide0 = 0f;
        if ((tConn & (1 << ForgeDirection.UP.ordinal())) != 0) tSide1 = 1f;
        if ((tConn & (1 << ForgeDirection.NORTH.ordinal())) != 0) tSide2 = 0f;
        if ((tConn & (1 << ForgeDirection.SOUTH.ordinal())) != 0) tSide3 = 1f;
        if ((tConn & (1 << ForgeDirection.WEST.ordinal())) != 0) tSide4 = 0f;
        if ((tConn & (1 << ForgeDirection.EAST.ordinal())) != 0) tSide5 = 1f;

        return AxisAlignedBB
            .getBoundingBox(aX + tSide4, aY + tSide0, aZ + tSide2, aX + tSide5, aY + tSide1, aZ + tSide3);
    }

    @Override
    public void addCollisionBoxesToList(World aWorld, int aX, int aY, int aZ, AxisAlignedBB inputAABB,
        List<AxisAlignedBB> outputAABB, Entity collider) {
        super.addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB, collider);
        if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x2) != 0) {
            final AxisAlignedBB aabb = getActualCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
            if (inputAABB.intersectsWith(aabb)) outputAABB.add(aabb);
        }
    }
}
