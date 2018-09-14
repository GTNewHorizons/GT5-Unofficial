package gtPlusPlus.core.tileentities.general;

import gtPlusPlus.core.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityPlayerDoorBase extends TileEntity {

	private boolean mIsOpen = false;
	private short mMeta = 0;
	private long mTickCounter = 0;
	private final Block mBlockType;
	
	public TileEntityPlayerDoorBase(Block aBlock, int meta){
		mMeta = (short) meta;
		mBlockType = aBlock;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound aNBT) {
		super.readFromNBT(aNBT);
		this.mIsOpen = aNBT.getBoolean("mIsOpen");
	}

	@Override
	public void writeToNBT(NBTTagCompound aNBT) {
		super.writeToNBT(aNBT);
		aNBT.setBoolean("mIsOpen", mIsOpen);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		mTickCounter++;		
		if (mTickCounter % 10 == 0) {
			if (checkForPlayers(this.getWorldObj())) {
				if (this.mIsOpen) {
					this.getWorldObj().setBlock(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), this.getClosedMeta(), 3);
					this.mIsOpen = false;
				}
				else {
					this.getWorldObj().setBlock(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), this.getOpenMeta(), 3);
					this.mIsOpen = true;
				}
			}
		}		
	}

	@Override
	public int getBlockMetadata() {
		return this.mMeta;
	}

	@Override
	public Block getBlockType() {
		return mBlockType;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}
	
	private boolean checkForPlayers(World aWorld) {
		int x = 0, y = 0, z = 0;
		x = this.xCoord;
		y = this.yCoord;
		z = this.zCoord;
		EntityPlayer aPlayer = aWorld.getClosestPlayer(x, y, z, 8D);		
		if (aPlayer != null) {
			return true;
		}		
		return false;
	}
	
	private short getClosedMeta() {
		return 0;
	}
	
	private short getOpenMeta() {
		return 1;
	}

}
