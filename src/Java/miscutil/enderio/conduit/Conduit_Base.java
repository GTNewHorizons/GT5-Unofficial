package miscutil.enderio.conduit;

import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fluids.IFluidHandler;

public class Conduit_Base implements IEnergyConnected{

	@Override
	public byte getColorization() {
		 
		return 0;
	}

	@Override
	public byte setColorization(byte aColor) {
		 
		return 0;
	}

	@Override
	public World getWorld() {
		 
		return null;
	}

	@Override
	public int getXCoord() {
		 
		return 0;
	}

	@Override
	public short getYCoord() {
		 
		return 0;
	}

	@Override
	public int getZCoord() {
		 
		return 0;
	}

	@Override
	public boolean isServerSide() {
		 
		return false;
	}

	@Override
	public boolean isClientSide() {
		 
		return false;
	}

	@Override
	public int getRandomNumber(int aRange) {
		 
		return 0;
	}

	@Override
	public TileEntity getTileEntity(int aX, int aY, int aZ) {
		 
		return null;
	}

	@Override
	public TileEntity getTileEntityOffset(int aX, int aY, int aZ) {
		 
		return null;
	}

	@Override
	public TileEntity getTileEntityAtSide(byte aSide) {
		 
		return null;
	}

	@Override
	public TileEntity getTileEntityAtSideAndDistance(byte aSide, int aDistance) {
		 
		return null;
	}

	@Override
	public IInventory getIInventory(int aX, int aY, int aZ) {
		 
		return null;
	}

	@Override
	public IInventory getIInventoryOffset(int aX, int aY, int aZ) {
		 
		return null;
	}

	@Override
	public IInventory getIInventoryAtSide(byte aSide) {
		 
		return null;
	}

	@Override
	public IInventory getIInventoryAtSideAndDistance(byte aSide, int aDistance) {
		 
		return null;
	}

	@Override
	public IFluidHandler getITankContainer(int aX, int aY, int aZ) {
		 
		return null;
	}

	@Override
	public IFluidHandler getITankContainerOffset(int aX, int aY, int aZ) {
		 
		return null;
	}

	@Override
	public IFluidHandler getITankContainerAtSide(byte aSide) {
		 
		return null;
	}

	@Override
	public IFluidHandler getITankContainerAtSideAndDistance(byte aSide,
			int aDistance) {
		 
		return null;
	}

	@Override
	public IGregTechTileEntity getIGregTechTileEntity(int aX, int aY, int aZ) {
		 
		return null;
	}

	@Override
	public IGregTechTileEntity getIGregTechTileEntityOffset(int aX, int aY,
			int aZ) {
		 
		return null;
	}

	@Override
	public IGregTechTileEntity getIGregTechTileEntityAtSide(byte aSide) {
		 
		return null;
	}

	@Override
	public IGregTechTileEntity getIGregTechTileEntityAtSideAndDistance(
			byte aSide, int aDistance) {
		 
		return null;
	}

	@Override
	public Block getBlock(int aX, int aY, int aZ) {
		 
		return null;
	}

	@Override
	public Block getBlockOffset(int aX, int aY, int aZ) {
		 
		return null;
	}

	@Override
	public Block getBlockAtSide(byte aSide) {
		 
		return null;
	}

	@Override
	public Block getBlockAtSideAndDistance(byte aSide, int aDistance) {
		 
		return null;
	}

	@Override
	public byte getMetaID(int aX, int aY, int aZ) {
		 
		return 0;
	}

	@Override
	public byte getMetaIDOffset(int aX, int aY, int aZ) {
		 
		return 0;
	}

	@Override
	public byte getMetaIDAtSide(byte aSide) {
		 
		return 0;
	}

	@Override
	public byte getMetaIDAtSideAndDistance(byte aSide, int aDistance) {
		 
		return 0;
	}

	@Override
	public byte getLightLevel(int aX, int aY, int aZ) {
		 
		return 0;
	}

	@Override
	public byte getLightLevelOffset(int aX, int aY, int aZ) {
		 
		return 0;
	}

	@Override
	public byte getLightLevelAtSide(byte aSide) {
		 
		return 0;
	}

	@Override
	public byte getLightLevelAtSideAndDistance(byte aSide, int aDistance) {
		 
		return 0;
	}

	@Override
	public boolean getOpacity(int aX, int aY, int aZ) {
		 
		return false;
	}

	@Override
	public boolean getOpacityOffset(int aX, int aY, int aZ) {
		 
		return false;
	}

	@Override
	public boolean getOpacityAtSide(byte aSide) {
		 
		return false;
	}

	@Override
	public boolean getOpacityAtSideAndDistance(byte aSide, int aDistance) {
		 
		return false;
	}

	@Override
	public boolean getSky(int aX, int aY, int aZ) {
		 
		return false;
	}

	@Override
	public boolean getSkyOffset(int aX, int aY, int aZ) {
		 
		return false;
	}

	@Override
	public boolean getSkyAtSide(byte aSide) {
		 
		return false;
	}

	@Override
	public boolean getSkyAtSideAndDistance(byte aSide, int aDistance) {
		 
		return false;
	}

	@Override
	public boolean getAir(int aX, int aY, int aZ) {
		 
		return false;
	}

	@Override
	public boolean getAirOffset(int aX, int aY, int aZ) {
		 
		return false;
	}

	@Override
	public boolean getAirAtSide(byte aSide) {
		 
		return false;
	}

	@Override
	public boolean getAirAtSideAndDistance(byte aSide, int aDistance) {
		 
		return false;
	}

	@Override
	public BiomeGenBase getBiome() {
		 
		return null;
	}

	@Override
	public BiomeGenBase getBiome(int aX, int aZ) {
		 
		return null;
	}

	@Override
	public int getOffsetX(byte aSide, int aMultiplier) {
		 
		return 0;
	}

	@Override
	public short getOffsetY(byte aSide, int aMultiplier) {
		 
		return 0;
	}

	@Override
	public int getOffsetZ(byte aSide, int aMultiplier) {
		 
		return 0;
	}

	@Override
	public boolean isDead() {
		 
		return false;
	}

	@Override
	public void sendBlockEvent(byte aID, byte aValue) {
		 
		
	}

	@Override
	public long getTimer() {
		 
		return 0;
	}

	@Override
	public void setLightValue(byte aLightValue) {
		 
		
	}

	@Override
	public void writeToNBT(NBTTagCompound aNBT) {
		 
		
	}

	@Override
	public void readFromNBT(NBTTagCompound aNBT) {
		 
		
	}

	@Override
	public boolean isInvalidTileEntity() {
		 
		return false;
	}

	@Override
	public boolean openGUI(EntityPlayer aPlayer, int aID) {
		 
		return false;
	}

	@Override
	public boolean openGUI(EntityPlayer aPlayer) {
		 
		return false;
	}

	@Override
	public long injectEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
		 
		return 0;
	}

	@Override
	public boolean inputEnergyFrom(byte aSide) {
		 
		return false;
	}

	@Override
	public boolean outputsEnergyTo(byte aSide) {
		 
		return false;
	}

}
