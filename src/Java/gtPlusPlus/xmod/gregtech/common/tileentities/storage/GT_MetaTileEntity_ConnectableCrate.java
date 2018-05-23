package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.util.data.ArrayUtils;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_SuperChest;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_SuperChest;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT_MetaTileEntity_ConnectableCrate extends GT_MetaTileEntity_TieredMachineBlock {

	/*sides.put(getUp());
	sides.put(getDown());
	sides.put(getXPos());
	sides.put(getXNeg());
	sides.put(getZPos());
	sides.put(getZNeg());*/

	public int mItemCount = 0;
	public ItemStack mItemStack = null;
	private final static double mStorageFactor = 2;

	//Meta Tile ID
	public final static int mCrateID = 955;

	//Sides
	public final static int SIDE_Up = 0;
	public final static int SIDE_Down = 1;
	public final static int SIDE_XPos = 2;
	public final static int SIDE_XNeg = 3;
	public final static int SIDE_ZPos = 4;
	public final static int SIDE_ZNeg = 5;
	public final static int[] SIDES = new int[] {SIDE_Up, SIDE_Down, SIDE_XPos, SIDE_XNeg, SIDE_ZPos, SIDE_ZNeg};

	//Neighbour Cache
	private GT_MetaTileEntity_ConnectableCrate[] mNeighbourCache = new GT_MetaTileEntity_ConnectableCrate[6];
	//Cached Crate Location
	private BlockPos mCurrentPos = null;
	//Master Crate Position
	protected BlockPos mMasterCrateLocation = null;
	//Is Master?
	protected boolean mIsMaster = false;
	//Is Connected?
	protected boolean mIsConnected[] = new boolean[] {false, false, false, false, false, false};
	//How many are connected?
	protected int mConnectedCount = 0;
	//Map of connected locations
	protected AutoMap<String> mConnectedCache = new AutoMap<String>();
	

	public GT_MetaTileEntity_ConnectableCrate(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 3,
				"This Crate stores " + (int) (Math.pow(6.0D, (double) aTier) * mStorageFactor) + " Items", new ITexture[0]);
	}

	public GT_MetaTileEntity_ConnectableCrate(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
	}

	public boolean isSimpleMachine() {
		return true;
	}

	public boolean isFacingValid(byte aFacing) {
		return true;
	}

	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	public boolean isValidSlot(int aIndex) {
		return true;
	}

	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_ConnectableCrate(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	public int getOppositeSide(int side) {
		if (side == SIDE_Up) {
			return SIDE_Down;
		}
		else if (side == SIDE_Down) {
			return SIDE_Up;
		}
		else if (side == SIDE_XNeg) {
			return SIDE_XPos;
		}
		else if (side == SIDE_XPos) {
			return SIDE_XNeg;
		}
		else if (side == SIDE_ZNeg) {
			return SIDE_ZPos;
		}
		else {
			return SIDE_ZNeg;
		}
	}

	public boolean calculateOwnershipIfConnected() {
		if (mCurrentPos == null) {
			mCurrentPos = new BlockPos(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord(), this.getBaseMetaTileEntity().getWorld());
		}
		AutoMap<BlockPos> n = mCurrentPos.getSurroundingBlocks();
		int p = 0;
		for (BlockPos i : n) {
			if (i != null) {
				if (doesSideContainCrate(p)) {
					GT_MetaTileEntity_ConnectableCrate yu = getCrateAtBlockPos(i);
					if (yu != null) {
						mNeighbourCache[p] = yu;
					}
				}
			}			
			p++;
		}

		int e4 = 0;
		if (mNeighbourCache.length > 0) {
			for (GT_MetaTileEntity_ConnectableCrate e : mNeighbourCache) {				
				this.mConnectedCount = this.mConnectedCache.size();				
				if (e != null) {
					//First, we check if this Crate is a Master, if not, continue checking what it is.
					if (this.mIsMaster) {
						//So this other Crate is also a master? Which is more Powerful
						if (e.mIsMaster) {
							//This crate holds more connected, it becomes master now.
							if (this.mConnectedCount > e.mConnectedCount) {
								e.mIsMaster = false;
								e.mMasterCrateLocation = this.mCurrentPos;
								if (!e.mIsConnected[getOppositeSide(e4)]) {
									e.mIsConnected[getOppositeSide(e4)] = true;
								}
								if (!this.mIsConnected[e4]) {
									this.mIsConnected[e4] = true;	
								}
								this.mConnectedCache = ArrayUtils.mergeTwoMaps(this.mConnectedCache, e.mConnectedCache);
								continue;
							}
							//Other crate held more connected, it is now master.
							else {
								this.mIsMaster = false;
								this.mMasterCrateLocation = e.mCurrentPos;
								if (!this.mIsConnected[e4]) {
									this.mIsConnected[e4] = true;
								}
								if (!e.mIsConnected[getOppositeSide(e4)]) {
									e.mIsConnected[getOppositeSide(e4)] = true;
								}
								e.mConnectedCache = ArrayUtils.mergeTwoMaps(e.mConnectedCache, this.mConnectedCache);
								//Best wipe our cache of connected blocks then, since they no longer hold value.
								mConnectedCache.clear();
								continue;
							}
						}
						//Other Crate was not a Master, but we are, time to inherit it into our connection hivemind.
						else {
							//It would appear this controller has another master, time to query it.
							if (e.mMasterCrateLocation != null && !e.mMasterCrateLocation.getUniqueIdentifier().equalsIgnoreCase(this.mMasterCrateLocation.getUniqueIdentifier())) {
								GT_MetaTileEntity_ConnectableCrate gM = getCrateAtBlockPos(e.mMasterCrateLocation);
								if (gM != null) {
									//Lets compare controller strengths
									int gM_Owned = gM.mConnectedCount;
									//We are stronger, let's inherit it.
									if (this.mConnectedCount > gM_Owned) {
										e.mIsMaster = false;
										e.mMasterCrateLocation = this.mCurrentPos;																		
										gM.mIsMaster = false;
										gM.mMasterCrateLocation = this.mCurrentPos;
										if (!e.mIsConnected[getOppositeSide(e4)]) {
											e.mIsConnected[getOppositeSide(e4)] = true;
										}
										if (!this.mIsConnected[e4]) {
											this.mIsConnected[e4] = true;	
										}
										this.mConnectedCache = ArrayUtils.mergeTwoMaps(this.mConnectedCache, gM.mConnectedCache);
										continue;
									}
									//We lost, time to submit to a new master crate
									else {
										this.mIsMaster = false;
										this.mMasterCrateLocation = e.mMasterCrateLocation;
										if (!this.mIsConnected[e4]) {
											this.mIsConnected[e4] = true;
										}
										if (!e.mIsConnected[getOppositeSide(e4)]) {
											e.mIsConnected[getOppositeSide(e4)] = true;
										}
										gM.mConnectedCache = ArrayUtils.mergeTwoMaps(gM.mConnectedCache, this.mConnectedCache);
										//Best wipe our cache of connected blocks then, since they no longer hold value.
										mConnectedCache.clear();
										continue;
									}
								}
								else {
									//Could not get the Tile Entity for the Other Master Crate.. Guess I can just ignore this case for now~ TODO
									continue;
								}
							}
							//Either the other crate has no known Master or it is already this crate.
							else {
								//The other crate has no master, time to inherit.
								if (e.mMasterCrateLocation == null || (!e.mIsConnected[getOppositeSide(e4)])) {
									e.mMasterCrateLocation = this.mCurrentPos;
									if (!e.mIsConnected[getOppositeSide(e4)]) {
										e.mIsConnected[getOppositeSide(e4)] = true;
									}
									if (!this.mIsConnected[e4]) {
										this.mIsConnected[e4] = true;	
									}
									mConnectedCache.put(e.mCurrentPos.getUniqueIdentifier());
									continue;
								}
								else {
									//Do nothing, we own this Crate already :)
									continue;
								}


							}						
						}					
					}

					//We are not a Storage Master Crate, into a brave new world we go
					else {
						//Best wipe our cache of connected blocks then, since they no longer hold value.
						mConnectedCache.clear();
						
						//Dang, the other crate is a master, time to get incorporated.
						if (e.mIsMaster) {
							this.mIsMaster = false;
							this.mMasterCrateLocation = e.mCurrentPos;
							this.mIsConnected[e4] = true;
							if (!e.mIsConnected[e4]) {
								e.mIsConnected[e4] = true;
							}
							if (e.mMasterCrateLocation == null) {
								e.mMasterCrateLocation = e.mCurrentPos;
							}
							e.mConnectedCache.put(this.mCurrentPos.getUniqueIdentifier());
							continue;
						}
						//So the Crate we Checked is not a Master, so let's see if it knows where one is
						else {
							//So, this Crate we have found knows about a master
							if (e.mMasterCrateLocation != null) {							
								GT_MetaTileEntity_ConnectableCrate gM = getCrateAtBlockPos(e.mMasterCrateLocation);
								//Found the master crate
								if (gM != null) {
									this.mIsMaster = false;
									this.mMasterCrateLocation = e.mMasterCrateLocation;
									if (!this.mIsConnected[e4]) {
										this.mIsConnected[e4] = true;
									}
									if (!e.mIsConnected[getOppositeSide(e4)]) {
										e.mIsConnected[getOppositeSide(e4)] = true;
									}
									gM.mConnectedCache.put(this.mCurrentPos.getUniqueIdentifier());
									continue;
								}
								else {
									//Could not get the Tile Entity for the Other Master Crate.. Guess I can just ignore this case for now~ TODO
									continue;
								}							
							}
							//This crate has no master, not going to check if it's connected.
							else {
								this.mIsMaster = true;
								this.mMasterCrateLocation = this.mCurrentPos;
								e.mIsMaster = false;
								e.mMasterCrateLocation = this.mCurrentPos;
								if (!e.mIsConnected[getOppositeSide(e4)]) {
									e.mIsConnected[getOppositeSide(e4)] = true;
								}
								if (!this.mIsConnected[e4]) {
									this.mIsConnected[e4] = true;	
								}
								mConnectedCache.put(e.mCurrentPos.getUniqueIdentifier());
								continue;
							}
						}
					}
				}
				e4++;
			}
			return true;
		}
		else {
			return false;			
		}




	}

	public boolean doesSideContainCrate(int side) {		
		return checkSideForDataType(0, side);
	}

	public boolean isCrateAtSideController(int side) {		
		return checkSideForDataType(1, side);
	}

	private boolean checkSideForDataType(int aType, int aSide) {		
		BlockPos mPosToCheck = 
				aSide == SIDE_Up ? mCurrentPos.getUp() : 
					aSide == SIDE_Down ? mCurrentPos.getDown() : 
						aSide == SIDE_XPos ? mCurrentPos.getXPos() : 
							aSide == SIDE_XNeg ? mCurrentPos.getXNeg() : 
								aSide == SIDE_ZPos ? mCurrentPos.getZPos() : 
									mCurrentPos.getZNeg();
								GT_MetaTileEntity_ConnectableCrate g = getCrateAtBlockPos(mPosToCheck);
								if (g != null) {
									if (aType == 0) {
										return true;
									}
									else {
										if (g.mIsMaster) {
											return true;
										}
									}
								}
								return false;
	}

	public GT_MetaTileEntity_ConnectableCrate getCrateAtBlockPos(BlockPos pos) {		
		if (pos != null) {
			Block b = pos.getBlockAtPos();
			int m = pos.getMetaAtPos();
			TileEntity t = pos.world.getTileEntity(pos.xPos, pos.yPos, pos.zPos);
			if (b != null && t != null) {
				if (b == GregTech_API.sBlockMachines && m == mCrateID) {
					if (t instanceof IGregTechTileEntity) {
						IGregTechTileEntity g = (IGregTechTileEntity) t;
						final IMetaTileEntity aMetaTileEntity = g.getMetaTileEntity();
						if (aMetaTileEntity == null) {
							return null;
						}
						if (aMetaTileEntity instanceof GT_MetaTileEntity_ConnectableCrate) {
							return ((GT_MetaTileEntity_ConnectableCrate) aMetaTileEntity);

						}												
					}						
				}
			}
		}
		return null;		
	}











	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		} else {
			aBaseMetaTileEntity.openGUI(aPlayer);
			return true;
		}
	}

	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_SuperChest(aPlayerInventory, aBaseMetaTileEntity);
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_SuperChest(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName());
	}















	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
		if (this.getBaseMetaTileEntity().isServerSide() && this.getBaseMetaTileEntity().isAllowedToWork()) {
			if (this.getItemCount() <= 0) {
				this.mItemStack = null;
				this.mItemCount = 0;
			}

			if (this.mItemStack == null && this.mInventory[0] != null) {
				this.mItemStack = this.mInventory[0].copy();
			}

			if (this.mInventory[0] != null && this.mItemCount < this.getMaxItemCount()
					&& GT_Utility.areStacksEqual(this.mInventory[0], this.mItemStack)) {
				this.mItemCount += this.mInventory[0].stackSize;
				if (this.mItemCount > this.getMaxItemCount()) {
					this.mInventory[0].stackSize = this.mItemCount - this.getMaxItemCount();
					this.mItemCount = this.getMaxItemCount();
				} else {
					this.mInventory[0] = null;
				}
			}

			if (this.mInventory[1] == null && this.mItemStack != null) {
				this.mInventory[1] = this.mItemStack.copy();
				this.mInventory[1].stackSize = Math.min(this.mItemStack.getMaxStackSize(), this.mItemCount);
				this.mItemCount -= this.mInventory[1].stackSize;
			} else if (this.mItemCount > 0 && GT_Utility.areStacksEqual(this.mInventory[1], this.mItemStack)
					&& this.mInventory[1].getMaxStackSize() > this.mInventory[1].stackSize) {
				int tmp = Math.min(this.mItemCount,
						this.mInventory[1].getMaxStackSize() - this.mInventory[1].stackSize);
				this.mInventory[1].stackSize += tmp;
				this.mItemCount -= tmp;
			}

			if (this.mItemStack != null) {
				this.mInventory[2] = this.mItemStack.copy();
				this.mInventory[2].stackSize = Math.min(this.mItemStack.getMaxStackSize(), this.mItemCount);
			} else {
				this.mInventory[2] = null;
			}
		}

	}

	private int getItemCount() {
		return this.mItemCount;
	}

	public void setItemCount(int aCount) {
		this.mItemCount = aCount;
	}

	public int getProgresstime() {
		return this.mItemCount + (this.mInventory[0] == null ? 0 : this.mInventory[0].stackSize)
				+ (this.mInventory[1] == null ? 0 : this.mInventory[1].stackSize);
	}

	public int maxProgresstime() {
		return this.getMaxItemCount();
	}

	public int getMaxItemCount() {
		return (int) (Math.pow(6.0D, (double) this.mTier) * mStorageFactor - 128.0D);
	}

	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aIndex == 1;
	}

	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aIndex == 0 && (this.mInventory[0] == null || GT_Utility.areStacksEqual(this.mInventory[0], aStack));
	}

	public String[] getInfoData() {
		return this.mItemStack == null
				? new String[]{"Super Storage Chest", "Stored Items:", "No Items", Integer.toString(0),
						Integer.toString(this.getMaxItemCount())}
		: new String[]{"Super Storage Chest", "Stored Items:", this.mItemStack.getDisplayName(),
				Integer.toString(this.mItemCount), Integer.toString(this.getMaxItemCount())};
	}

	public boolean isGivingInformation() {
		return true;
	}

	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setInteger("mItemCount", this.mItemCount);
		if (this.mItemStack != null) {
			aNBT.setTag("mItemStack", this.mItemStack.writeToNBT(new NBTTagCompound()));
		}
		aNBT.setString("mMasterCrateLocation", mMasterCrateLocation.getUniqueIdentifier());
		aNBT.setBoolean("mIsMaster", mIsMaster);
		for (int y=0;y<this.mIsConnected.length;y++) {
			aNBT.setBoolean("mIsConnected"+y, mIsConnected[y]);
		}		
		aNBT.setInteger("mConnectedCount", mConnectedCount);
	}

	public void loadNBTData(NBTTagCompound aNBT) {
		if (aNBT.hasKey("mItemCount")) {
			this.mItemCount = aNBT.getInteger("mItemCount");
		}
		if (aNBT.hasKey("mItemStack")) {
			this.mItemStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) aNBT.getTag("mItemStack"));
		}
		if (aNBT.hasKey("mMasterCrateLocation")) {
			this.mMasterCrateLocation = BlockPos.generateBlockPos(aNBT.getString("mMasterCrateLocation"));
		}
		if (aNBT.hasKey("mIsMaster")) {
			this.mIsMaster = aNBT.getBoolean("mIsMaster");
		}

		for (int y=0;y<this.mIsConnected.length;y++) {
			if (aNBT.hasKey("mIsConnected"+y)) {
				this.mIsConnected[y] = aNBT.getBoolean("mIsConnected"+y);
			}
		}

		if (aNBT.hasKey("mConnectedCount")) {
			this.mConnectedCount = aNBT.getInteger("mConnectedCount");
		}

	}

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
			boolean aActive, boolean aRedstone) {
		return aBaseMetaTileEntity.getFrontFacing() == 0 && aSide == 4
				? new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.TEXTURE_CASING_AMAZON),
						new GT_RenderedTexture(BlockIcons.OVERLAY_QCHEST)}
		: (aSide == aBaseMetaTileEntity.getFrontFacing()
				? new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.TEXTURE_CASING_AMAZON),
						new GT_RenderedTexture(BlockIcons.OVERLAY_QCHEST)}
		: new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.TEXTURE_CASING_AMAZON)});
	}

	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		return new ITexture[0][0][0];
	}

	@Override
	public void onExplosion() {
		// TODO Auto-generated method stub
		super.onExplosion();
	}

	@Override
	public void onRemoval() {
		// TODO Auto-generated method stub
		super.onRemoval();
	}

	@Override
	public void onMachineBlockUpdate() {
		// TODO Auto-generated method stub
		super.onMachineBlockUpdate();
	}
}