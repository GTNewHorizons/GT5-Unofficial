package gtPlusPlus.xmod.gregtech.api.gui.computer;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.*;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.slots.SlotDataStick;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.misc.GT_TileEntity_ComputerCube;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class GT_Container_ComputerCube extends GT_ContainerMetaTile_Machine {

	public long mEUOut;
	public int mEUOut1;
	public int mEUOut2;

	public int mHeat;

	public int mMaxHeat;

	public int mHEM;

	public int mExplosionStrength;

	public long mEU;
	public int mEU1;
	public int mEU2;
	
	public long mStoredEU;
	public int mStoredEU1;
	public int mStoredEU2;
	public long mMaxStoredEU;
	public int mMaxStoredEU1;
	public int mMaxStoredEU2;

	public int mProgress;

	public int mID;

	public GT_Container_ComputerCube(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, int aID) {
		super(aInventoryPlayer, aTileEntity);
		Logger.INFO("1 Container Mode: "+aID);
		Logger.INFO("2 Container Mode: "+getMode());
		mID = getMode();
		Logger.INFO("3 Container Mode: "+getMode());
		// addSlotsComputer(aInventoryPlayer);
		detectAndSendChanges();
	}

	public void addSlots(InventoryPlayer aInventoryPlayer) {
		addSlotsComputer(aInventoryPlayer);
	}

	private int getMode() {
		return ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mMode;
	}

	public void addSlotsComputer(InventoryPlayer aInventoryPlayer) {
		int y;
		mID = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mMode;
		Logger.INFO(""+(Utils.isClient() ? "Client" : "Server")+" Mode: " + mID);
		addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 58, 156 + ((this.mID == 5) ? 50 : 0), 4, false, false, 1));
		switch (this.mID) {
			case 1 :
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 58, 156, 86, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 58, 156, 70, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 58, 156, 54, false, false, 1));
				for (y = 0; y < 6; y++) {
					for (int x = 0; x < 9; x++)
						addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, x + y * 9, 5 + x * 16, 5 + y * 16, false, false, 64));
				}
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 113, 153, 28, false, false, 64));
				break;
			case 2 :
				addSlotToContainer(new SlotDataStick(this.mTileEntity, 54, 8, 28));
				addSlotToContainer(new Slot(this.mTileEntity, 55, 26, 28));
				addSlotToContainer(new GT_Slot_Output(this.mTileEntity, 56, 134, 28));
				addSlotToContainer(new GT_Slot_Output(this.mTileEntity, 57, 152, 28));
				break;
			case 3 :
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 58, 88, 65, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 58, 104, 65, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 59, 122, 35, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 60, 92, 5, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 61, 122, 5, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 62, 152, 35, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 63, 122, 65, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 64, 92, 35, false, false, 64));
				break;
			case 4 :
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 58, 88, 65, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 58, 104, 65, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 59, 122, 5, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 60, 122, 65, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 61, 152, 35, false, false, 64));
				break;
			case 5 :
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 58, 190, 146, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 58, 206, 146, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 59, 206, 38, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 60, 206, 56, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 61, 206, 74, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 62, 206, 92, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 63, 206, 110, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 64, 153, 7, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 65, 169, 7, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 66, 185, 7, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 67, 153, 23, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 68, 169, 23, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 69, 185, 23, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 70, 153, 39, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 71, 169, 39, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 72, 185, 39, false, false, 64));
				break;
			case 6 :
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 58, 88, 65, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 58, 104, 65, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 59, 122, 35, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 60, 92, 5, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 61, 122, 5, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 62, 152, 35, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 63, 122, 65, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 64, 92, 35, false, false, 64));
				break;
		}
	}

	public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
		//Logger.INFO("Clicked slot " + aSlotIndex);
		if (aSlotIndex < 0) {
			//Logger.INFO("");
			return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
		}
		if (this.mID != ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mMode) {
			//Logger.INFO("This ID: " + mID + ", Tile: " + ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mMode);
			return null;
		}
		Slot tSlot = (Slot) this.inventorySlots.get(aSlotIndex);
		if (tSlot == null) {
			//Logger.INFO("Null Slot?");
		}
		else {
			ItemStack tStack = tSlot.getStack();
			//Logger.INFO("Good Slot!");
			if (aSlotIndex == 0) {
				//Logger.INFO("Slot is 0");
				if (aMouseclick == 0) {
					Logger.INFO("Forward");
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchModeForward();
				}
				else {
					Logger.INFO("Backwards");
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchModeBackward();
				}
				if (aPlayer instanceof EntityPlayerMP) {
					EntityPlayerMP aPlayerMP = (EntityPlayerMP) aPlayer;
					changePage(aPlayerMP, ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mMode);			
				}
				//FMLNetworkHandler.openGui(entityPlayer, mod, modGuiId, world, x, y, z);
				//aPlayer.openGui(CORE.MODID, getComputerCubeGUIID(), this.mTileEntity.getWorld(), this.mTileEntity.getXCoord(), this.mTileEntity.getYCoord(), this.mTileEntity.getZCoord());
			}
			else if (aSlotIndex <= 2 && this.mID == 3) {
				if (aSlotIndex == 1) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchCentrifugePageBackward();
					onCraftMatrixChanged(this.mTileEntity);
				}
				else if (aSlotIndex == 2) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchCentrifugePageForward();
					onCraftMatrixChanged(this.mTileEntity);
				}
			}
			else if (aSlotIndex <= 2 && this.mID == 6) {
				if (aSlotIndex == 1) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchElectrolyzerPageBackward();
					onCraftMatrixChanged(this.mTileEntity);
				}
				else if (aSlotIndex == 2) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchElectrolyzerPageForward();
					onCraftMatrixChanged(this.mTileEntity);
				}
			}
			else if (aSlotIndex <= 2 && this.mID == 4) {
				if (aSlotIndex == 1) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchFusionPageBackward();
					onCraftMatrixChanged(this.mTileEntity);
				}
				else if (aSlotIndex == 2) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchFusionPageForward();
					onCraftMatrixChanged(this.mTileEntity);
				}
			}
			else if (aSlotIndex <= 2 && this.mID == 5) {
				if (aSlotIndex == 1) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchDescriptionPageBackward();
					onCraftMatrixChanged(this.mTileEntity);
				}
				else if (aSlotIndex == 2) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchDescriptionPageForward();
					onCraftMatrixChanged(this.mTileEntity);
				}
			}
			else if (aSlotIndex <= 58 && this.mID == 1) {
				if (aSlotIndex == 1) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchNuclearReactor();
					onCraftMatrixChanged(this.mTileEntity);
				}
				else if (aSlotIndex == 2) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).loadNuclearReactor();
					onCraftMatrixChanged(this.mTileEntity);
				}
				else if (aSlotIndex == 3) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).saveNuclearReactor();
				}
				else {
					if (aShifthold == 1) {
						tSlot.putStack(null);
						((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).getSimulator().slotClick(aSlotIndex, null);
						return null;
					}

					if (aMouseclick == 1) {
						tSlot.putStack(null);
					}
					if (aMouseclick == 0) {
						if (tStack == null) {
							if (getSlot(58).getStack() != null && aSlotIndex != 58) {
								tSlot.putStack(getSlot(58).getStack().copy());
								((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).getSimulator().slotClick(aSlotIndex, new GT_ItemStack(getSlot(58).getStack().copy()));
							}
							else {
								tSlot.putStack(new ItemStack(GT_TileEntity_ComputerCube.sReactorList.get(0).mItem, 1));
								((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).getSimulator().slotClick(aSlotIndex, GT_TileEntity_ComputerCube.sReactorList.get(0));
							}
							return null;
						}
						for (int i = 1; i < GT_TileEntity_ComputerCube.sReactorList.size(); i++) {
							if (GT_TileEntity_ComputerCube.sReactorList.get(i - 1).mItem == tStack.getItem()) {
								tSlot.putStack(new ItemStack(GT_TileEntity_ComputerCube.sReactorList.get(i).mItem, 1, 0));
								((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).getSimulator().slotClick(aSlotIndex, GT_TileEntity_ComputerCube.sReactorList.get(i));
								/*if (tSlot.getStack() != null && tSlot.getStack().getItem() == GT_ModHandler.getIC2Item("reactorIsotopeCell", 1).getItem()) {
									tSlot.getStack().setItemDamage(tSlot.getStack().getMaxDamage() - 1);
								}*/
								return null;
							}
						}
						tSlot.putStack(null);
						((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).getSimulator().slotClick(aSlotIndex, null);
						return null;
					}
					if (tStack == null)
						return null;
					if (tStack.stackSize < tStack.getMaxStackSize()) {
						tStack.stackSize++;
						return null;
					}
					tStack.stackSize = 1;
					return null;
				}
			}
			else {
				//Logger.INFO("Super 2");
				return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
			}
		}
		//Logger.INFO("???");
		return null;
	}



	public void changePage(EntityPlayerMP aPlayerMP, int aMode) {			
		GT_TileEntity_ComputerCube aCompTile = (GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity();
		aCompTile.onRightclick(mTileEntity, aPlayerMP);
	}

	public boolean doesBindPlayerInventory() {
		return (this.mID != 1 && this.mID != 5);
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) {
			return;
		}
		mStoredEU = mTileEntity.getStoredEU();
		mMaxStoredEU = mTileEntity.getEUCapacity();
		int[] aStored = MathUtils.splitLongIntoTwoIntegers(mStoredEU);
		int[] aMaxStorage = MathUtils.splitLongIntoTwoIntegers(mMaxStoredEU);
		mStoredEU1 = aStored[0];
		mStoredEU2 = aStored[1];
		mMaxStoredEU1 = aMaxStorage[0];
		mMaxStoredEU2 = aMaxStorage[1];
		
		
		this.mID = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mMode;
		this.mEUOut = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mEUOut;
		int[] aEUSplit1 = MathUtils.splitLongIntoTwoIntegers(mEUOut);
		this.mEUOut1 = aEUSplit1[0];
		this.mEUOut2 = aEUSplit1[1];
		this.mHeat = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mHeat;
		this.mMaxHeat = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mMaxHeat;
		this.mHEM = (int) (((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mHEM * 10000.0F);
		this.mExplosionStrength = (int) (((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mExplosionStrength * 100.0F);
		this.mEU = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mEU;
		this.mProgress = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mProgress;
		this.mMaxProgressTime = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mMaxProgress;
		this.mProgressTime = (int) ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).getEUVar();
		int[] aEUSplit2 = MathUtils.splitLongIntoTwoIntegers(mEU);
		this.mEU1 = aEUSplit2[0];
		this.mEU2 = aEUSplit2[1];
		Iterator<ICrafting> var2 = this.crafters.iterator();
		while (var2.hasNext()) {
			ICrafting var1 = var2.next();
			var1.sendProgressBarUpdate(this, 101, this.mID);
			var1.sendProgressBarUpdate(this, 102, this.mHeat & 0xFFFF);
			var1.sendProgressBarUpdate(this, 103, this.mMaxHeat & 0xFFFF);
			var1.sendProgressBarUpdate(this, 104, this.mHEM);
			var1.sendProgressBarUpdate(this, 105, this.mExplosionStrength);
			var1.sendProgressBarUpdate(this, 106, this.mHeat >>> 16);
			var1.sendProgressBarUpdate(this, 107, this.mMaxHeat >>> 16);
			var1.sendProgressBarUpdate(this, 108, this.mEU1);
			var1.sendProgressBarUpdate(this, 109, this.mEU2);
			var1.sendProgressBarUpdate(this, 110, this.mProgress);
			var1.sendProgressBarUpdate(this, 111, this.mEUOut1);
			var1.sendProgressBarUpdate(this, 112, this.mEUOut2);
			var1.sendProgressBarUpdate(this, 113, mStoredEU1);
			var1.sendProgressBarUpdate(this, 114, mStoredEU2);
			var1.sendProgressBarUpdate(this, 115, mMaxStoredEU1);
			var1.sendProgressBarUpdate(this, 116, mMaxStoredEU2);
		}
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		super.updateProgressBar(par1, par2);
		switch (par1) {
			case 101 :
				this.mID = par2;
				break;
			case 102 :
				this.mHeat = this.mHeat & 0xFFFF0000 | par2;
				break;
			case 103 :
				this.mMaxHeat = this.mMaxHeat & 0xFFFF0000 | par2;
				break;
			case 104 :
				this.mHEM = par2;
				break;
			case 105 :
				this.mExplosionStrength = par2;
				break;
			case 106 :
				this.mHeat = this.mHeat & 0xFFFF | par2 << 16;
				break;
			case 107 :
				this.mMaxHeat = this.mMaxHeat & 0xFFFF | par2 << 16;
				break;
			case 108 :
				this.mEU1 = par2;
			case 109 :
				this.mEU2 = par2;
				this.mEU = MathUtils.combineTwoIntegersToLong(mEU1, mEU2);
				break;
			case 110 :
				this.mProgress = par2;
				break;
			case 111 :
				this.mEUOut1 = par2;
			case 112 :
				this.mEUOut2 = par2;
				this.mEUOut = MathUtils.combineTwoIntegersToLong(mEUOut1, mEUOut2);
				break;
			case 113 :
				mStoredEU1 = par2;
				break;
			case 114 :
				mStoredEU2 = par2;
				mStoredEU = MathUtils.combineTwoIntegersToLong(mStoredEU1, mStoredEU2);
				break;
			case 115 :
				mMaxStoredEU1 = par2;
				break;
			case 116 :
				mMaxStoredEU2 = par2;
				mMaxStoredEU = MathUtils.combineTwoIntegersToLong(mMaxStoredEU1, mMaxStoredEU2);
				break;
		}
	}

	public int getSlotStartIndex() {
		return 1;
	}

	public int getSlotCount() {
		return (this.mID == 2) ? 4 : 0;
	}

	public int getShiftClickSlotCount() {
		return (this.mID == 2) ? 2 : 0;
	}
}
