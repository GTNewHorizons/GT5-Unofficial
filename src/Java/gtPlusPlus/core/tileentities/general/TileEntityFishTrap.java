package gtPlusPlus.core.tileentities.general;

import java.util.UUID;

import gtPlusPlus.core.inventories.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFishTrap extends TileEntity{

	private UUID ownerUUID;
	private int tickCount = 0;
	private boolean isInWater = false;
	private InventoryFishtrap inventoryChest;
	private int locationX = this.xCoord;
	private int locationY = this.yCoord;;
	private int locationZ = this.zCoord;;

	public TileEntityFishTrap(){
		this.inventoryChest = new InventoryFishtrap();//number of slots - without product slot
		this.canUpdate();
	}

	public boolean setTileLocation(){
		if (this.hasWorldObj()){
			locationX = this.xCoord;
			locationY = this.yCoord;
			locationZ = this.zCoord;
			return true;

		}
		return false;	
	}

	public final boolean isSurroundedByWater(){
		Block[] surroundingBlocks = new Block[6];
		if (this.hasWorldObj()){
			surroundingBlocks[0] = worldObj.getBlock(locationX, locationY+1, locationZ); //Above
			surroundingBlocks[1] = worldObj.getBlock(locationX, locationY-1, locationZ); //Below
			surroundingBlocks[2] = worldObj.getBlock(locationX+1, locationY, locationZ);
			surroundingBlocks[3] = worldObj.getBlock(locationX-1, locationY, locationZ);
			surroundingBlocks[4] = worldObj.getBlock(locationX, locationY, locationZ+1);
			surroundingBlocks[5] = worldObj.getBlock(locationX, locationY, locationZ-1);
			int waterCount = 0;
			for (Block checkBlock : surroundingBlocks){
				if (checkBlock == Blocks.water || checkBlock == Blocks.flowing_water){
					waterCount++;
				}
			}
			if (waterCount >= 5){
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public void updateEntity(){
		//if (anyPlayerInRange()){
			this.tickCount += 1;
			if (this.worldObj.isRemote){
				this.isInWater = isSurroundedByWater();

				if (this.isInWater){
					//Add Loot
				}
				//Add some Loot
			}
		//}
	}

	public boolean anyPlayerInRange(){
		return this.worldObj.getClosestPlayer(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, 32) != null;
	}

	public UUID getOwnerUUID() {
		return ownerUUID;
	}

	public void setOwnerUUID(UUID ownerUUID) {
		this.ownerUUID = ownerUUID;
		markDirty();
	}

	public NBTTagCompound getTag(NBTTagCompound nbt, String tag){
		if(!nbt.hasKey(tag)){
			nbt.setTag(tag, new NBTTagCompound());
		}
		return nbt.getCompoundTag(tag);
	}


	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		inventoryChest.writeToNBT(getTag(tagCompound, "ContentsChest"));
		UUID ownerUUID = getOwnerUUID();
		if (ownerUUID != null){
			tagCompound.setLong("OwnerUUIDMost", ownerUUID.getMostSignificantBits());
			tagCompound.setLong("OwnerUUIDLeast", ownerUUID.getLeastSignificantBits());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		inventoryChest.readFromNBT(tagCompound.getCompoundTag("ContentsChest"));
		setOwnerUUID(new UUID(tagCompound.getLong("OwnerUUIDMost"), tagCompound.getLong("OwnerUUIDLeast")));
	}

}
