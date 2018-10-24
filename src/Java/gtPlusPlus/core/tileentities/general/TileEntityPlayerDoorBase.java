package gtPlusPlus.core.tileentities.general;

import java.util.ArrayList;
import java.util.List;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

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

	AutoMap<Entity> mNearbyEntityCache = new AutoMap<Entity>();
	
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

		World aWorld = this.getWorldObj();
		Block aBlock = aWorld.getBlock(xCoord, yCoord, zCoord);
		BlockPos aThisPos = new BlockPos(xCoord, yCoord, zCoord, this.worldObj);
		
		if (mTickCounter % 20 == 0) {
			int x = 0, y = 0, z = 0;
			x = this.xCoord;
			y = this.yCoord;
			z = this.zCoord;
			//List aEntityList = aWorld.loadedEntityList;
			List<Entity> aEntityList = new ArrayList<Entity>();
			Chunk aThisChunk = aWorld.getChunkFromBlockCoords(x, z);
			for (List l : aThisChunk.entityLists) {
				aEntityList.addAll(l);
			}
			for (Object o : aEntityList) {
				if (o != null) {
					if (o instanceof Entity) {
						if (o instanceof EntityPlayer) {
							continue;
						}
						else {
							Entity e = (Entity) o;
							BlockPos p = EntityUtils.findBlockPosUnderEntity(e);
							if (p != null) {
								int newY = p.yPos+1;
								if (e.getDistance(xCoord, yCoord, zCoord) <= 2){
									mNearbyEntityCache.put(e);
								}
								else if (aThisPos.distanceFrom(p.xPos, newY, p.zPos) <= 2) {
									mNearbyEntityCache.put(e);
								}									
							}
						}
					}
				}
			}
		}

		if (mTickCounter % 4 == 0) {			
			for (Entity y : mNearbyEntityCache) {
				if (y.getDistance(xCoord, yCoord, zCoord) > 2){
					mNearbyEntityCache.remove(y);
				}
			}
			
			boolean foundMonster = mNearbyEntityCache.size() > 0;
			int aNeighbourDoorState = 0;
			if (mNeighbourDoor != null) {
				aNeighbourDoorState = getNeighbourState();
			}
			BlockDoor aDoor = (aBlock instanceof BlockDoor ? (BlockDoor) aBlock : null);
			boolean aPlayers = checkForPlayers(this.getWorldObj());

			if (aDoor != null) {
				//If neighbour state != 0 and we are in slave mode
				if (aNeighbourDoorState != 0 && mMeta == -1) {
					if (aNeighbourDoorState == 100) {
						if (!mIsOpen && !foundMonster) {
							//Logger.INFO("Opening Door (Slave)");
							aDoor.func_150014_a(aWorld, this.xCoord, this.yCoord, this.zCoord, true);
							mIsOpen = true;
						}
					} else if (aNeighbourDoorState == -100 || foundMonster) {
						if (mIsOpen) {
							//Logger.INFO("Closing Door (Slave)");
							aDoor.func_150014_a(aWorld, this.xCoord, this.yCoord, this.zCoord, false);
							mIsOpen = false;
						}
					}
					//We are master, proceed
				} else {
					//No redstone found, allow automatic handling
					if (aDoor != null && !hasRedstone()) {
						//Found a nearby player
						if (aPlayers) {
							//If we are closed and there are no monsters nearby, open
							if (!mIsOpen && !foundMonster) {
								//Logger.INFO("Opening Door (Mstr)");
								aDoor.func_150014_a(aWorld, this.xCoord, this.yCoord, this.zCoord, true);
								mIsOpen = true;
							} else {
								// Logger.INFO("Doing Nothing, Door is in correct state.");
							}
							//Did not find nearby player
						} else {
							//If we are open or there is a monster nearby, close.
							if (mIsOpen || foundMonster) {
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
