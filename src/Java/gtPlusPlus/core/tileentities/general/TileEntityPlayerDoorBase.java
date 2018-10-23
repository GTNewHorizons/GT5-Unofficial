package gtPlusPlus.core.tileentities.general;

import gtPlusPlus.api.objects.minecraft.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityPlayerDoorBase extends TileEntity {

	public boolean mIsOpen = false;
	private short mMeta = 0;
	private long mTickCounter = 0;
	private final Block mBlockType;
	private BlockPos mNeighbourDoor;

	public TileEntityPlayerDoorBase(Block aBlock, int meta) {
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

	public int getNeighbourState() {

		if (mNeighbourDoor != null) {
			World aWorld = this.worldObj;
			if (aWorld != null) {
				TileEntity t = aWorld.getTileEntity(mNeighbourDoor.xPos, mNeighbourDoor.yPos, mNeighbourDoor.zPos);
				// Custom Door
				if (t != null) {
					if (t instanceof TileEntityPlayerDoorBase) {
						TileEntityPlayerDoorBase d = (TileEntityPlayerDoorBase) t;
						if (d.mIsOpen) {
							return 100;
						} else {
							return -100;
						}
					} else
						return -100;
				}
				// Vanilla Door
				else {
					Block aBlock = mNeighbourDoor.getBlockAtPos();
					BlockDoor aDoor = (aBlock instanceof BlockDoor ? (BlockDoor) aBlock : null);
					if (aDoor != null) {
						int i1 = aDoor.func_150012_g(mNeighbourDoor.world, mNeighbourDoor.xPos, mNeighbourDoor.yPos,
								mNeighbourDoor.zPos);
						if ((i1 & 4) != 0) {
							return 100;
						} else {
							return -100;
						}
					}
				}
			}
		}
		return 0;
	}

	@Override
	public void updateEntity() {

		if (this.getWorldObj().isRemote) {
			return;
		}

		// Look For Neighbours
		if (mTickCounter % 100 == 0 || mTickCounter == 0) {
			World aWorld = this.getWorldObj();
			BlockPos aThisPos = new BlockPos(xCoord, yCoord, zCoord, aWorld);
			BlockPos[] aNeighbors = new BlockPos[4];
			aNeighbors[0] = aThisPos.getXNeg();
			aNeighbors[1] = aThisPos.getXPos();
			aNeighbors[2] = aThisPos.getZNeg();
			aNeighbors[3] = aThisPos.getZPos();
			boolean aFoundDoor = false;
			for (BlockPos b : aNeighbors) {
				Block aBlock = aWorld.getBlock(b.xPos, b.yPos, b.zPos);
				BlockDoor aDoor = (aBlock instanceof BlockDoor ? (BlockDoor) aBlock : null);
				if (aDoor != null) {
					mNeighbourDoor = b;
					aFoundDoor = true;
					if (mMeta == 0) {					
						TileEntity t = aWorld.getTileEntity(b.xPos, b.yPos, b.zPos);
						if (t != null) {
							if (t instanceof TileEntityPlayerDoorBase) {
								TileEntityPlayerDoorBase d = (TileEntityPlayerDoorBase) t;
								if (d.mMeta != 0) {
									//Logger.INFO("Found Door with Mode set other than 0, assuming slave role.");
									mMeta = -1;
								}
								else {
									//Logger.INFO("Found door with no mode set, assuming we are master.");
									mMeta = 1;
								}
							}
							else {
								//Logger.INFO("Custom door from another mod, assuming slave role.");
								mMeta = -1;
							}
						}
						else {
							//Logger.INFO("No Tile Entity found, Door is probably vanilla, assuming slave role.");
							mMeta = -1;
						}	
					}
					break;
				}
			}
			if (mMeta < 1 && !aFoundDoor) {
				//Logger.INFO("Found No Valid Doors around, setting this one to master mode.");
				mMeta = 1;
			}
		}

		if (mTickCounter % 4 == 0) {
			int aDoorState = 0;
			if (mNeighbourDoor != null) {
				aDoorState = getNeighbourState();
			}
			World aWorld = this.getWorldObj();
			Block aBlock = aWorld.getBlock(xCoord, yCoord, zCoord);
			BlockDoor aDoor = (aBlock instanceof BlockDoor ? (BlockDoor) aBlock : null);
			boolean aPlayers = checkForPlayers(this.getWorldObj());

			if (aDoor != null) {
				if (aDoorState != 0 && mMeta == -1) {
					if (aDoorState == 100) {
						if (!mIsOpen) {
							//Logger.INFO("Opening Door (Slave)");
							aDoor.func_150014_a(aWorld, this.xCoord, this.yCoord, this.zCoord, true);
							mIsOpen = true;
						}
					} else if (aDoorState == -100) {
						if (mIsOpen) {
							//Logger.INFO("Closing Door (Slave)");
							aDoor.func_150014_a(aWorld, this.xCoord, this.yCoord, this.zCoord, false);
							mIsOpen = false;
						}
					}
				} else {
					if (aDoor != null && !hasRedstone()) {
						if (aPlayers) {
							if (!mIsOpen) {
								//Logger.INFO("Opening Door (Mstr)");
								aDoor.func_150014_a(aWorld, this.xCoord, this.yCoord, this.zCoord, true);
								mIsOpen = true;
							} else {
								// Logger.INFO("Doing Nothing, Door is in correct state.");
							}
						} else {
							if (mIsOpen) {
								//Logger.INFO("Closing Door (Mstr)");
								aDoor.func_150014_a(aWorld, this.xCoord, this.yCoord, this.zCoord, false);
								mIsOpen = false;
							} else {
								// Logger.INFO("Doing Nothing, Door is in correct state.");
							}
						}
					}
				}
			}

		}
		super.updateEntity();
		mTickCounter++;
	}

	@Override
	public int getBlockMetadata() {
		return this.mMeta;
	}

	public boolean hasRedstone() {
		World aWorld = this.worldObj;
		if (aWorld != null && !aWorld.isRemote) {
			return aWorld.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)
					|| aWorld.isBlockIndirectlyGettingPowered(xCoord, yCoord + 1, zCoord);
		}
		return false;
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
		EntityPlayer aPlayer = aWorld.getClosestPlayer(x, y, z, 3.5D);
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
