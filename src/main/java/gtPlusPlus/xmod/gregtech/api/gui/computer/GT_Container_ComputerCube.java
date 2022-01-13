package gtPlusPlus.xmod.gregtech.api.gui.computer;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.gui.GT_Slot_Output;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.common.tileentities.misc.GT_TileEntity_ComputerCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_ComputerCube extends GT_ContainerMetaTile_Machine {

	public int mEUOut;

	public int mHeat;

	public int mMaxHeat;

	public int mHEM;

	public int mExplosionStrength;

	public int mEU;

	public int mProgress;

	public int mID;

	public GT_Container_ComputerCube(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, int aID) {
		super(aInventoryPlayer, aTileEntity);
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
		Logger.INFO("Mode: " + mID);
		addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 58, 156 + ((this.mID == 5) ? 50 : 0), 4, false, false, 1));
		switch (this.mID) {
			case 1 :
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 58, 156, 86, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 58, 156, 70, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 58, 156, 54, false, false, 1));
				for (y = 0; y < 6; y++) {
					for (int x = 0; x < 9; x++)
						addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, x + y * 9, 5 + x * 16, 5 + y * 16, false, false, 64));
				}
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 113, 153, 28, false, false, 64));
				break;
			case 2 :
				addSlotToContainer(new Slot((IInventory) this.mTileEntity, 54, 8, 28));
				addSlotToContainer(new Slot((IInventory) this.mTileEntity, 55, 26, 28));
				addSlotToContainer(new GT_Slot_Output((IInventory) this.mTileEntity, 56, 134, 28));
				addSlotToContainer(new GT_Slot_Output((IInventory) this.mTileEntity, 57, 152, 28));
				break;
			case 3 :
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 58, 88, 65, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 58, 104, 65, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 59, 122, 35, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 60, 92, 5, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 61, 122, 5, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 62, 152, 35, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 63, 122, 65, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 64, 92, 35, false, false, 64));
				break;
			case 4 :
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 58, 88, 65, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 58, 104, 65, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 59, 122, 5, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 60, 122, 65, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 61, 152, 35, false, false, 64));
				break;
			case 5 :
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 58, 190, 146, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 58, 206, 146, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 59, 206, 38, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 60, 206, 56, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 61, 206, 74, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 62, 206, 92, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 63, 206, 110, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 64, 153, 7, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 65, 169, 7, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 66, 185, 7, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 67, 153, 23, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 68, 169, 23, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 69, 185, 23, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 70, 153, 39, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 71, 169, 39, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 72, 185, 39, false, false, 64));
				break;
			case 6 :
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 58, 88, 65, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 58, 104, 65, false, false, 1));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 59, 122, 35, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 60, 92, 5, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 61, 122, 5, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 62, 152, 35, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 63, 122, 65, false, false, 64));
				addSlotToContainer(new GT_Slot_Holo((IInventory) this.mTileEntity, 64, 92, 35, false, false, 64));
				break;
		}
	}

	public int getComputerCubeGUIID() {
		switch (getMode()) {
			case 1 :
				return 32;
			case 2 :
				return 34;
			case 3 :
				return 35;
			case 4 :
				return 36;
			case 5 :
				return 37;
			case 6 :
				return 38;
		}
		return 4;
	}

	public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
		Logger.INFO("Clicked slot " + aSlotIndex);
		if (aSlotIndex < 0) {
			Logger.INFO("");
			return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
		}
		if (this.mID != ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mMode) {
			Logger.INFO("This ID: " + mID + ", Tile: " + ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mMode);
			return null;
		}
		Slot tSlot = (Slot) this.inventorySlots.get(aSlotIndex);
		if (tSlot == null) {
			Logger.INFO("Null Slot?");
		}
		else {
			ItemStack tStack = tSlot.getStack();
			Logger.INFO("Good Slot!");
			if (aSlotIndex == 0) {
				Logger.INFO("Slot is 0");
				if (aMouseclick == 0) {
					Logger.INFO("Forward");
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchModeForward();
				}
				else {
					Logger.INFO("Backwards");
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchModeBackward();
				}
				aPlayer.openGui(CORE.MODID, getComputerCubeGUIID(), this.mTileEntity.getWorld(), this.mTileEntity.getXCoord(), this.mTileEntity.getYCoord(), this.mTileEntity.getZCoord());
			}
			else if (aSlotIndex <= 2 && this.mID == 3) {
				if (aSlotIndex == 1) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchCentrifugePageBackward();
					onCraftMatrixChanged((IInventory) this.mTileEntity);
				}
				else if (aSlotIndex == 2) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchCentrifugePageForward();
					onCraftMatrixChanged((IInventory) this.mTileEntity);
				}
			}
			else if (aSlotIndex <= 2 && this.mID == 6) {
				if (aSlotIndex == 1) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchElectrolyzerPageBackward();
					onCraftMatrixChanged((IInventory) this.mTileEntity);
				}
				else if (aSlotIndex == 2) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchElectrolyzerPageForward();
					onCraftMatrixChanged((IInventory) this.mTileEntity);
				}
			}
			else if (aSlotIndex <= 2 && this.mID == 4) {
				if (aSlotIndex == 1) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchFusionPageBackward();
					onCraftMatrixChanged((IInventory) this.mTileEntity);
				}
				else if (aSlotIndex == 2) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchFusionPageForward();
					onCraftMatrixChanged((IInventory) this.mTileEntity);
				}
			}
			else if (aSlotIndex <= 2 && this.mID == 5) {
				if (aSlotIndex == 1) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchDescriptionPageBackward();
					onCraftMatrixChanged((IInventory) this.mTileEntity);
				}
				else if (aSlotIndex == 2) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchDescriptionPageForward();
					onCraftMatrixChanged((IInventory) this.mTileEntity);
				}
			}
			else if (aSlotIndex <= 58 && this.mID == 1) {
				if (aSlotIndex == 1) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).switchNuclearReactor();
					onCraftMatrixChanged((IInventory) this.mTileEntity);
				}
				else if (aSlotIndex == 2) {
					((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).loadNuclearReactor();
					onCraftMatrixChanged((IInventory) this.mTileEntity);
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
				Logger.INFO("Super 2");
				return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
			}
		}
		Logger.INFO("???");
		return null;
	}

	public boolean doesBindPlayerInventory() {
		return (this.mID != 1 && this.mID != 5);
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) {
			return;
		}
		this.mID = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mMode;
		this.mEUOut = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mEUOut;
		this.mHeat = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mHeat;
		this.mMaxHeat = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mMaxHeat;
		this.mHEM = (int) (((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mHEM * 10000.0F);
		this.mExplosionStrength = (int) (((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mExplosionStrength * 100.0F);
		this.mEU = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mEU;
		this.mProgress = ((GT_TileEntity_ComputerCube) mTileEntity.getMetaTileEntity()).mProgress;
		Iterator<ICrafting> var2 = this.crafters.iterator();
		while (var2.hasNext()) {
			ICrafting var1 = var2.next();
			var1.sendProgressBarUpdate((Container) this, 101, this.mEUOut);
			var1.sendProgressBarUpdate((Container) this, 102, this.mHeat & 0xFFFF);
			var1.sendProgressBarUpdate((Container) this, 103, this.mMaxHeat & 0xFFFF);
			var1.sendProgressBarUpdate((Container) this, 104, this.mHEM);
			var1.sendProgressBarUpdate((Container) this, 105, this.mExplosionStrength);
			var1.sendProgressBarUpdate((Container) this, 106, this.mHeat >>> 16);
			var1.sendProgressBarUpdate((Container) this, 107, this.mMaxHeat >>> 16);
			var1.sendProgressBarUpdate((Container) this, 108, this.mEU & 0xFFFF);
			var1.sendProgressBarUpdate((Container) this, 109, this.mEU >>> 16);
			var1.sendProgressBarUpdate((Container) this, 110, this.mProgress);
			var1.sendProgressBarUpdate((Container) this, 111, this.mID);
		}
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		super.updateProgressBar(par1, par2);
		switch (par1) {
			case 101 :
				this.mEUOut = par2;
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
				this.mEU = this.mEU & 0xFFFF0000 | par2;
			case 109 :
				this.mEU = this.mEU & 0xFFFF | par2 << 16;
				break;
			case 110 :
				this.mProgress = par2;
				break;
			case 111 :
				this.mID = par2;
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
