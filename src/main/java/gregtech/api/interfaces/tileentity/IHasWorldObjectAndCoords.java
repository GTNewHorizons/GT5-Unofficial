package gregtech.api.interfaces.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * This is a bunch of Functions my TileEntities provide, to make life much easier, and to get rid of internal TileEntity
 * stuff.
 * <p/>
 * This also makes access to adjacent TileEntities more Efficient.
 * <p/>
 * Note: It doesn't have to be a TileEntity in certain cases! And only certain cases, such as the Recipe checking of the
 * findRecipe Function.
 */
public interface IHasWorldObjectAndCoords {

    World getWorld();

    int getXCoord();

    short getYCoord();

    int getZCoord();

    default ChunkCoordinates getCoords() {
        return new ChunkCoordinates(getXCoord(), getYCoord(), getZCoord());
    }

    boolean isServerSide();

    boolean isClientSide();

    int getRandomNumber(int aRange);

    TileEntity getTileEntity(int aX, int aY, int aZ);

    TileEntity getTileEntityOffset(int aX, int aY, int aZ);

    TileEntity getTileEntityAtSide(ForgeDirection aSide);

    TileEntity getTileEntityAtSideAndDistance(ForgeDirection aSide, int aDistance);

    IInventory getIInventory(int aX, int aY, int aZ);

    IInventory getIInventoryOffset(int aX, int aY, int aZ);

    IInventory getIInventoryAtSide(ForgeDirection aSide);

    IInventory getIInventoryAtSideAndDistance(ForgeDirection aSide, int aDistance);

    IFluidHandler getITankContainer(int aX, int aY, int aZ);

    IFluidHandler getITankContainerOffset(int aX, int aY, int aZ);

    IFluidHandler getITankContainerAtSide(ForgeDirection aSide);

    IFluidHandler getITankContainerAtSideAndDistance(ForgeDirection aSide, int aDistance);

    IGregTechTileEntity getIGregTechTileEntity(int aX, int aY, int aZ);

    IGregTechTileEntity getIGregTechTileEntityOffset(int aX, int aY, int aZ);

    IGregTechTileEntity getIGregTechTileEntityAtSide(ForgeDirection aSide);

    IGregTechTileEntity getIGregTechTileEntityAtSideAndDistance(ForgeDirection aSide, int aDistance);

    Block getBlock(int aX, int aY, int aZ);

    Block getBlockOffset(int aX, int aY, int aZ);

    Block getBlockAtSide(ForgeDirection aSide);

    Block getBlockAtSideAndDistance(ForgeDirection aSide, int aDistance);

    byte getMetaID(int aX, int aY, int aZ);

    byte getMetaIDOffset(int aX, int aY, int aZ);

    byte getMetaIDAtSide(ForgeDirection aSide);

    byte getMetaIDAtSideAndDistance(ForgeDirection aSide, int aDistance);

    byte getLightLevel(int aX, int aY, int aZ);

    byte getLightLevelOffset(int aX, int aY, int aZ);

    byte getLightLevelAtSide(ForgeDirection aSide);

    byte getLightLevelAtSideAndDistance(ForgeDirection aSide, int aDistance);

    boolean getOpacity(int aX, int aY, int aZ);

    boolean getOpacityOffset(int aX, int aY, int aZ);

    boolean getOpacityAtSide(ForgeDirection aSide);

    boolean getOpacityAtSideAndDistance(ForgeDirection aSide, int aDistance);

    boolean getSky(int aX, int aY, int aZ);

    boolean getSkyOffset(int aX, int aY, int aZ);

    boolean getSkyAtSide(ForgeDirection aSide);

    boolean getSkyAtSideAndDistance(ForgeDirection aSide, int aDistance);

    boolean getAir(int aX, int aY, int aZ);

    boolean getAirOffset(int aX, int aY, int aZ);

    boolean getAirAtSide(ForgeDirection aSide);

    boolean getAirAtSideAndDistance(ForgeDirection aSide, int aDistance);

    BiomeGenBase getBiome();

    BiomeGenBase getBiome(int aX, int aZ);

    int getOffsetX(ForgeDirection aSide, int aMultiplier);

    short getOffsetY(ForgeDirection aSide, int aMultiplier);

    int getOffsetZ(ForgeDirection aSide, int aMultiplier);

    /**
     * Checks if the TileEntity is Invalid or Unloaded. Stupid Minecraft cannot do that btw.
     */
    boolean isDead();

    /**
     * Sends a Block Event to the Client TileEntity, the byte Parameters are only for validation as Minecraft doesn't
     * properly write Packet Data.
     */
    void sendBlockEvent(byte aID, byte aValue);

    /**
     * @return the Time this TileEntity has been loaded.
     */
    long getTimer();

    /**
     * Sets the Light Level of this Block on a Scale of 0 - 15 It could be that it doesn't work. This is just for
     * convenience.
     */
    void setLightValue(byte aLightValue);

    /**
     * Function of the regular TileEntity
     */
    void writeToNBT(NBTTagCompound aNBT);

    /**
     * Function of the regular TileEntity
     */
    void readFromNBT(NBTTagCompound aNBT);

    /**
     * Function of the regular TileEntity
     */
    boolean isInvalidTileEntity();

    /**
     * Opens the GUI with this ID of this MetaTileEntity
     * 
     * @deprecated Use ModularUI
     */
    @Deprecated
    default boolean openGUI(EntityPlayer aPlayer, int aID) {
        return false;
    }

    /**
     * Opens the GUI with the ID = 0 of this TileEntity
     * 
     * @deprecated Use ModularUI
     */
    @Deprecated
    default boolean openGUI(EntityPlayer aPlayer) {
        return false;
    }
}
