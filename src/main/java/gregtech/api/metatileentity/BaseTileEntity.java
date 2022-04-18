package gregtech.api.metatileentity;

import gregtech.api.interfaces.tileentity.IGTEnet;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.interfaces.tileentity.IIC2Enet;
import gregtech.api.net.GT_Packet_Block_Event;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;
import static gregtech.api.enums.GT_Values.COMPASS_DIRECTIONS;
import static gregtech.api.enums.GT_Values.GT;
import static gregtech.api.enums.GT_Values.NW;
import static gregtech.api.enums.GT_Values.SIDE_DOWN;
import static gregtech.api.enums.GT_Values.SIDE_UP;

/**
 * The Functions my old TileEntities and my BaseMetaTileEntities have in common.
 * <p/>
 * Basically everything a TileEntity should have.
 */
public abstract class BaseTileEntity extends TileEntity implements IHasWorldObjectAndCoords, IIC2Enet, IGTEnet {
    protected boolean mInventoryChanged = false;

    /**
     * Buffers adjacent TileEntities for faster access
     * <p/>
     * "this" means that there is no TileEntity, while "null" means that it doesn't know if there is even a TileEntity and still needs to check that if needed.
     */
    private final TileEntity[] mBufferedTileEntities = new TileEntity[6];
    /**
     * If this TileEntity checks for the Chunk to be loaded before returning World based values.
     * The AdvPump hacks this to false to ensure everything runs properly even when far Chunks are not actively loaded.
     * But anything else should not cause worfin' Chunks, uhh I mean orphan Chunks.
     */
    public boolean ignoreUnloadedChunks = true;
    /**
     * This Variable checks if this TileEntity is dead, because Minecraft is too stupid to have proper TileEntity unloading.
     */
    public boolean isDead = false;

    private final ChunkCoordinates mReturnedCoordinates = new ChunkCoordinates();

    public static byte getSideForPlayerPlacing(Entity aPlayer, byte aDefaultFacing, boolean[] aAllowedFacings) {
        if(aPlayer != null) {
            if (aPlayer.rotationPitch >= 65 && aAllowedFacings[SIDE_UP]) return SIDE_UP;
            if (aPlayer.rotationPitch <= -65 && aAllowedFacings[SIDE_DOWN]) return SIDE_DOWN;
            final byte rFacing = COMPASS_DIRECTIONS[MathHelper.floor_double(0.5D + 4.0F * aPlayer.rotationYaw / 360.0F) & 0x3];
            if (aAllowedFacings[rFacing]) return rFacing;
        }
        for (final byte tSide : ALL_VALID_SIDES) if (aAllowedFacings[tSide]) return tSide;
        return aDefaultFacing;
    }


    private void clearNullMarkersFromTileEntityBuffer() {
        for (int i = 0; i < mBufferedTileEntities.length; i++)
            if (mBufferedTileEntities[i] == this) mBufferedTileEntities[i] = null;
    }

    /**
     * Called automatically when the Coordinates of this TileEntity have been changed
     */
    protected final void clearTileEntityBuffer() {
        Arrays.fill(mBufferedTileEntities, null);
    }

    @Override
    public final World getWorld() {
        return worldObj;
    }

    @Override
    public final int getXCoord() {
        return xCoord;
    }

    @Override
    public final short getYCoord() {
        return (short) yCoord;
    }

    @Override
    public final int getZCoord() {
        return zCoord;
    }

    @Override
    public ChunkCoordinates getCoords() {
        mReturnedCoordinates.posX = xCoord;
        mReturnedCoordinates.posY = yCoord;
        mReturnedCoordinates.posZ = zCoord;
        return mReturnedCoordinates;
    }

    @Override
    public final int getOffsetX(byte aSide, int aMultiplier) {
        return xCoord + ForgeDirection.getOrientation(aSide).offsetX * aMultiplier;
    }

    @Override
    public final short getOffsetY(byte aSide, int aMultiplier) {
        return (short) (yCoord + ForgeDirection.getOrientation(aSide).offsetY * aMultiplier);
    }

    @Override
    public final int getOffsetZ(byte aSide, int aMultiplier) {
        return zCoord + ForgeDirection.getOrientation(aSide).offsetZ * aMultiplier;
    }

    @Override
    public final boolean isServerSide() {
        return !worldObj.isRemote;
    }

    @Override
    public final boolean isClientSide() {
        return worldObj.isRemote;
    }

    @Override
    public final boolean openGUI(EntityPlayer aPlayer) {
        return openGUI(aPlayer, 0);
    }

    @Override
    public final boolean openGUI(EntityPlayer aPlayer, int aID) {
        if (aPlayer == null) return false;
        aPlayer.openGui(GT, aID, worldObj, xCoord, yCoord, zCoord);
        return true;
    }

    @Override
    public boolean isInvalidTileEntity() {
        return isInvalid();
    }

    @Override
    public int getRandomNumber(int aRange) {
        return ThreadLocalRandom.current().nextInt(aRange);
    }

    @Override
    public final BiomeGenBase getBiome(int aX, int aZ) {
        return worldObj.getBiomeGenForCoords(aX, aZ);
    }

    @Override
    public final BiomeGenBase getBiome() {
        return getBiome(xCoord, zCoord);
    }

    @Override
    public final Block getBlockOffset(int aX, int aY, int aZ) {
        return getBlock(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final Block getBlockAtSide(byte aSide) {
        return getBlockAtSideAndDistance(aSide, 1);
    }

    @Override
    public final Block getBlockAtSideAndDistance(byte aSide, int aDistance) {
        return getBlock(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final byte getMetaIDOffset(int aX, int aY, int aZ) {
        return getMetaID(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final byte getMetaIDAtSide(byte aSide) {
        return getMetaIDAtSideAndDistance(aSide, 1);
    }

    @Override
    public final byte getMetaIDAtSideAndDistance(byte aSide, int aDistance) {
        return getMetaID(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final byte getLightLevelOffset(int aX, int aY, int aZ) {
        return getLightLevel(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final byte getLightLevelAtSide(byte aSide) {
        return getLightLevelAtSideAndDistance(aSide, 1);
    }

    @Override
    public final byte getLightLevelAtSideAndDistance(byte aSide, int aDistance) {
        return getLightLevel(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final boolean getOpacityOffset(int aX, int aY, int aZ) {
        return getOpacity(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final boolean getOpacityAtSide(byte aSide) {
        return getOpacityAtSideAndDistance(aSide, 1);
    }

    @Override
    public final boolean getOpacityAtSideAndDistance(byte aSide, int aDistance) {
        return getOpacity(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final boolean getSkyOffset(int aX, int aY, int aZ) {
        return getSky(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final boolean getSkyAtSide(byte aSide) {
        return getSkyAtSideAndDistance(aSide, 1);
    }

    @Override
    public final boolean getSkyAtSideAndDistance(byte aSide, int aDistance) {
        return getSky(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final boolean getAirOffset(int aX, int aY, int aZ) {
        return getAir(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final boolean getAirAtSide(byte aSide) {
        return getAirAtSideAndDistance(aSide, 1);
    }

    @Override
    public final boolean getAirAtSideAndDistance(byte aSide, int aDistance) {
        return getAir(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final TileEntity getTileEntityOffset(int aX, int aY, int aZ) {
        return getTileEntity(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final TileEntity getTileEntityAtSideAndDistance(byte aSide, int aDistance) {
        if (aDistance == 1) return getTileEntityAtSide(aSide);
        return getTileEntity(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final IInventory getIInventory(int aX, int aY, int aZ) {
        final TileEntity tTileEntity = getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IInventory) return (IInventory) tTileEntity;
        return null;
    }

    @Override
    public final IInventory getIInventoryOffset(int aX, int aY, int aZ) {
        final TileEntity tTileEntity = getTileEntityOffset(aX, aY, aZ);
        if (tTileEntity instanceof IInventory) return (IInventory) tTileEntity;
        return null;
    }

    @Override
    public final IInventory getIInventoryAtSide(byte aSide) {
        final TileEntity tTileEntity = getTileEntityAtSide(aSide);
        if (tTileEntity instanceof IInventory) return (IInventory) tTileEntity;
        return null;
    }

    @Override
    public final IInventory getIInventoryAtSideAndDistance(byte aSide, int aDistance) {
        final TileEntity tTileEntity = getTileEntityAtSideAndDistance(aSide, aDistance);
        if (tTileEntity instanceof IInventory) return (IInventory) tTileEntity;
        return null;
    }

    @Override
    public final IFluidHandler getITankContainer(int aX, int aY, int aZ) {
        final TileEntity tTileEntity = getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IFluidHandler) return (IFluidHandler) tTileEntity;
        return null;
    }

    @Override
    public final IFluidHandler getITankContainerOffset(int aX, int aY, int aZ) {
        final TileEntity tTileEntity = getTileEntityOffset(aX, aY, aZ);
        if (tTileEntity instanceof IFluidHandler) return (IFluidHandler) tTileEntity;
        return null;
    }

    @Override
    public final IFluidHandler getITankContainerAtSide(byte aSide) {
        final TileEntity tTileEntity = getTileEntityAtSide(aSide);
        if (tTileEntity instanceof IFluidHandler) return (IFluidHandler) tTileEntity;
        return null;
    }

    @Override
    public final IFluidHandler getITankContainerAtSideAndDistance(byte aSide, int aDistance) {
        final TileEntity tTileEntity = getTileEntityAtSideAndDistance(aSide, aDistance);
        if (tTileEntity instanceof IFluidHandler) return (IFluidHandler) tTileEntity;
        return null;
    }

    @Override
    public final IGregTechTileEntity getIGregTechTileEntity(int aX, int aY, int aZ) {
        final TileEntity tTileEntity = getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity) return (IGregTechTileEntity) tTileEntity;
        return null;
    }

    @Override
    public final IGregTechTileEntity getIGregTechTileEntityOffset(int aX, int aY, int aZ) {
        final TileEntity tTileEntity = getTileEntityOffset(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity) return (IGregTechTileEntity) tTileEntity;
        return null;
    }

    @Override
    public final IGregTechTileEntity getIGregTechTileEntityAtSide(byte aSide) {
        final TileEntity tTileEntity = getTileEntityAtSide(aSide);
        if (tTileEntity instanceof IGregTechTileEntity) return (IGregTechTileEntity) tTileEntity;
        return null;
    }

    @Override
    public final IGregTechTileEntity getIGregTechTileEntityAtSideAndDistance(byte aSide, int aDistance) {
        final TileEntity tTileEntity = getTileEntityAtSideAndDistance(aSide, aDistance);
        if (tTileEntity instanceof IGregTechTileEntity) return (IGregTechTileEntity) tTileEntity;
        return null;
    }

    @Override
    public final Block getBlock(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return Blocks.air;
        return worldObj.getBlock(aX, aY, aZ);
    }

    public Block getBlock(ChunkCoordinates aCoords) {
        if (worldObj == null) return Blocks.air;
        if (ignoreUnloadedChunks && crossedChunkBorder(aCoords) && !worldObj.blockExists(aCoords.posX, aCoords.posY, aCoords.posZ)) return Blocks.air;
        return worldObj.getBlock(aCoords.posX, aCoords.posY, aCoords.posZ);
    }

    @Override
    public final byte getMetaID(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return 0;
        return (byte) worldObj.getBlockMetadata(aX, aY, aZ);
    }

    @Override
    public final byte getLightLevel(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return 0;
        return (byte) (worldObj.getLightBrightness(aX, aY, aZ) * 15);
    }

    @Override
    public final boolean getSky(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return true;
        return worldObj.canBlockSeeTheSky(aX, aY, aZ);
    }

    @Override
    public final boolean getOpacity(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return false;
        return GT_Utility.isOpaqueBlock(worldObj, aX, aY, aZ);
    }

    @Override
    public final boolean getAir(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return true;
        return GT_Utility.isBlockAir(worldObj, aX, aY, aZ);
    }

    @Override
    public TileEntity getTileEntity(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return null;
        return worldObj.getTileEntity(aX, aY, aZ);
    }

    @Override
    public final TileEntity getTileEntityAtSide(byte aSide) {
        if (aSide < 0 || aSide >= 6 || mBufferedTileEntities[aSide] == this) return null;
        final int tX = getOffsetX(aSide, 1);
        final int tY = getOffsetY(aSide, 1);
        final int tZ = getOffsetZ(aSide, 1);
        if (crossedChunkBorder(tX, tZ)) {
            mBufferedTileEntities[aSide] = null;
            if (ignoreUnloadedChunks && !worldObj.blockExists(tX, tY, tZ)) return null;
        }
        if (mBufferedTileEntities[aSide] == null) {
            mBufferedTileEntities[aSide] = worldObj.getTileEntity(tX, tY, tZ);
            if (mBufferedTileEntities[aSide] == null) {
                mBufferedTileEntities[aSide] = this;
                return null;
            }
            return mBufferedTileEntities[aSide];
        }
        if (mBufferedTileEntities[aSide].isInvalid()) {
            mBufferedTileEntities[aSide] = null;
            return getTileEntityAtSide(aSide);
        }
        if (mBufferedTileEntities[aSide].xCoord == tX && mBufferedTileEntities[aSide].yCoord == tY && mBufferedTileEntities[aSide].zCoord == tZ) {
            return mBufferedTileEntities[aSide];
        }
        return null;
    }

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        super.writeToNBT(aNBT);
    }

    @Override
    public boolean isDead() {
        return isDead || isInvalidTileEntity();
    }

    @Override
    public void validate() {
        clearNullMarkersFromTileEntityBuffer();
        super.validate();
    }

    @Override
    public void invalidate() {
        leaveEnet();
        clearNullMarkersFromTileEntityBuffer();
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        leaveEnet();
        clearNullMarkersFromTileEntityBuffer();
        super.onChunkUnload();
        isDead = true;
    }

    @Override
    public void updateEntity() {
        // Well if the TileEntity gets ticked it is alive.
        isDead = false;
    }

    public final void onAdjacentBlockChange(int aX, int aY, int aZ) {
        clearNullMarkersFromTileEntityBuffer();
    }

    public void updateNeighbours(int mStrongRedstone, int oStrongRedstone) {
        final Block thisBlock = getBlockOffset(0, 0, 0);
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            final int x1 = xCoord + dir.offsetX, y1 = yCoord + dir.offsetY, z1 = zCoord + dir.offsetZ;

            if (worldObj.blockExists(x1, y1, z1)) {
                worldObj.notifyBlockOfNeighborChange(x1, y1, z1, thisBlock);

                // update if it was / is strong powered.
                if (((((mStrongRedstone | oStrongRedstone) >>> dir.ordinal()) & 1) != 0 ) && getBlock(x1, y1, z1).isNormalCube()) {
                    final int skipUpdateSide = dir.getOpposite().ordinal(); //Don't update this block. Still updates diagonal blocks twice if conditions meet.

                    for (final ForgeDirection dir2 : ForgeDirection.VALID_DIRECTIONS) {
                        final int x2 = x1 + dir2.offsetX, y2 = y1 + dir2.offsetY, z2 = z1 + dir2.offsetZ;
                        if (dir2.ordinal() != skipUpdateSide && worldObj.blockExists(x2, y2, z2))
                            worldObj.notifyBlockOfNeighborChange(x2, y2, z2, thisBlock);
                    }
                }
            }
        }
    }

    @Override
    public final void sendBlockEvent(byte aID, byte aValue) {
        NW.sendPacketToAllPlayersInRange(worldObj, new GT_Packet_Block_Event(xCoord, (short) yCoord, zCoord, aID, aValue), xCoord, zCoord);
    }

    protected boolean crossedChunkBorder(int aX, int aZ) {
        return aX >> 4 != xCoord >> 4 || aZ >> 4 != zCoord >> 4;
    }

    public final boolean crossedChunkBorder(ChunkCoordinates aCoords) {
        return aCoords.posX >> 4 != xCoord >> 4 || aCoords.posZ >> 4 != zCoord >> 4;
    }

    public final void setOnFire() {
        GT_Utility.setCoordsOnFire(worldObj, xCoord, yCoord, zCoord, false);
    }

    public final void setToFire() {
        worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.fire);
    }

    @Override
    public void markDirty() {
        // Avoid sending neighbor updates, just mark the chunk as dirty to make sure it gets saved
        final Chunk chunk = worldObj.getChunkFromBlockCoords(xCoord, zCoord);
        if(chunk != null) {
            chunk.setChunkModified();
        }
    }

    @Deprecated
    public String trans(String aKey, String aEnglish){
    	return GT_Utility.trans(aKey, aEnglish);
    }

    /*
     * IC2 Energy Compat
     */
    protected TileIC2EnergySink ic2EnergySink = null;
    protected boolean joinedIc2Enet = false;

    protected void createIc2Sink() {
        if(ic2EnergySink == null && isServerSide() && shouldJoinIc2Enet()) {
            ic2EnergySink = new TileIC2EnergySink((IGregTechTileEntity)this);
        }
    }

    @Override
    public void doEnetUpdate() {
        leaveEnet();
        joinEnet();
    }

    protected void joinEnet() {
        if (joinedIc2Enet || !shouldJoinIc2Enet()) return;

        if(ic2EnergySink == null) createIc2Sink();

        if (ic2EnergySink != null) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(ic2EnergySink));
            joinedIc2Enet = true;
        }
    }

    protected void leaveEnet() {
        if (joinedIc2Enet && ic2EnergySink != null && isServerSide()) {
            joinedIc2Enet = false;
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(ic2EnergySink));
        }
    }

}
