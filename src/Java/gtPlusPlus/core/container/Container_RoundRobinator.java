package gtPlusPlus.core.container;

import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.inventories.Inventory_RoundRobinator;
import gtPlusPlus.core.slots.SlotNoInput;
import gtPlusPlus.core.tileentities.machines.TileEntityRoundRobinator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Container_RoundRobinator extends Container {

	public TileEntityRoundRobinator tile_entity;
	public final Inventory_RoundRobinator inventoryChest;

	private final World worldObj;
	private final int posX;
	private final int posY;
	private final int posZ;
	
	private final boolean[] mActiveData = new boolean[] {false, false, false, false};

	public static int mStorageSlotNumber = 4; // Number of slots in storage area
	public static int mInventorySlotNumber = 36; // Inventory Slots (Inventory
												// and Hotbar)
	public static int mFullSlotNumber = mInventorySlotNumber + mStorageSlotNumber; // All
																				// slots

	public Container_RoundRobinator(final InventoryPlayer inventory, final TileEntityRoundRobinator te) {
		this.tile_entity = te;
		this.inventoryChest = te.getInventory();
		boolean [] aTemp = te.getActiveSides();
		if (aTemp != null && aTemp.length == 4) {
			for (int i=0;i<4;i++) {
				mActiveData[i] = aTemp[i];
			}
		}

		int var6;
		int var7;
		this.worldObj = te.getWorldObj();
		this.posX = te.xCoord;
		this.posY = te.yCoord;
		this.posZ = te.zCoord;

		int o = 0;
		int xStart = 134;
		int yStart = 32;

		try {
		//0
		this.addSlotToContainer(new SlotNoInput(this.inventoryChest, o++, xStart, yStart));
		this.addSlotToContainer(new SlotNoInput(this.inventoryChest, o++, xStart+18, yStart));
		this.addSlotToContainer(new SlotNoInput(this.inventoryChest, o++, xStart, yStart+17));
		this.addSlotToContainer(new SlotNoInput(this.inventoryChest, o++, xStart+18, yStart+17));		
		
		// Player Inventory
		for (var6 = 0; var6 < 3; ++var6) {
			for (var7 = 0; var7 < 9; ++var7) {
				this.addSlotToContainer(new Slot(inventory, var7 + (var6 * 9) + 9, 8 + (var7 * 18), 84 + (var6 * 18)));
			}
		}
		// Player Hotbar
		for (var6 = 0; var6 < 9; ++var6) {
			this.addSlotToContainer(new Slot(inventory, var6, 8 + (var6 * 18), 142));
		}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		this.detectAndSendChanges();

	}

	@Override
	public ItemStack slotClick(final int aSlotIndex, final int aMouseclick, final int aShifthold,
			final EntityPlayer aPlayer) {

		if (!aPlayer.worldObj.isRemote) {
			if (aSlotIndex < 4) {
				this.tile_entity.toggleSide(aSlotIndex+2);				
				//Logger.INFO("Toggling side: "+(aSlotIndex+2)+" | Active: "+this.tile_entity.getSideActive(aSlotIndex+2)+" | Data:"+this.tile_entity.getDataString());
			}
		}
		return GT_Values.NI;
		//return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	}

	@Override
	public void onContainerClosed(final EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);
	}

	@Override
	public boolean canInteractWith(final EntityPlayer par1EntityPlayer) {
		if (this.worldObj.getBlock(this.posX, this.posY, this.posZ) != ModBlocks.blockRoundRobinator) {
			return false;
		}

		return par1EntityPlayer.getDistanceSq(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) <= 64D;
	}
	


	public final void addCraftingToCrafters(ICrafting par1ICrafting) {
		try {
			super.addCraftingToCrafters(par1ICrafting);
		} catch (Throwable var3) {
			var3.printStackTrace();			
		}
	}

	public final void removeCraftingFromCrafters(ICrafting par1ICrafting) {
		try {
			super.removeCraftingFromCrafters(par1ICrafting);
		} catch (Throwable var3) {
			var3.printStackTrace();		
		}
	}

	public final void detectAndSendChanges() {
		try {
			super.detectAndSendChanges();
			detectAndSendChangesEx();
		} catch (Throwable var2) {
			var2.printStackTrace();		
		}
	}

	public final void updateProgressBar(int par1, int par2) {
		try {
			super.updateProgressBar(par1, par2);
			updateProgressBarEx(par1, par2);
		} catch (Throwable var4) {
			var4.printStackTrace();		
		}
	}
	

	public int mSide_1 = 0;
	public int mSide_2 = 0;
	public int mSide_3 = 0;
	public int mSide_4 = 0;	
	public int mTier = 1;
	public int mTickRate = 50;
	
	private int oSide_1 = 0;
	private int oSide_2 = 0;
	private int oSide_3 = 0;
	private int oSide_4 = 0;	
	private int oTier = 1;
	private int oTickRate = 50;
	
	private int mTimer = 0;
	
	

	public void detectAndSendChangesEx() {
		super.detectAndSendChanges();
		if (!this.tile_entity.getWorldObj().isRemote) {
			boolean [] aTemp = tile_entity.getActiveSides();
			for (int i=0;i<4;i++) {
				mActiveData[i] = aTemp[i];
			}			
			this.mSide_1 = aTemp[0] ? 1 : 0;
			this.mSide_2 = aTemp[1] ? 1 : 0;
			this.mSide_3 = aTemp[2] ? 1 : 0;
			this.mSide_4 = aTemp[3] ? 1 : 0;
			this.mTier = this.tile_entity.getTier();
			this.mTickRate = this.tile_entity.getTickRate();
			++this.mTimer;
			Iterator var2 = this.crafters.iterator();

			while (true) {
				ICrafting var1;
				do {
					if (!var2.hasNext()) {
						this.oSide_1 = this.mSide_1;
						this.oSide_2 = this.mSide_2;
						this.oSide_3 = this.mSide_3;
						this.oSide_4 = this.mSide_4;
						this.oTier = this.mTier;
						this.oTickRate = this.mTickRate;
						return;
					}
					var1 = (ICrafting) var2.next();
					if (this.mTimer % 500 == 10 || this.oSide_1 != this.mSide_1) {
						var1.sendProgressBarUpdate(this, 2, this.mSide_1);
					}
					if (this.mTimer % 500 == 10 || this.oSide_2 != this.mSide_2) {
						var1.sendProgressBarUpdate(this, 4, this.mSide_2);
					}
					if (this.mTimer % 500 == 10 || this.oSide_3 != this.mSide_3) {
						var1.sendProgressBarUpdate(this, 6, this.mSide_3);
					}
					if (this.mTimer % 500 == 10 || this.oSide_4 != this.mSide_4) {
						var1.sendProgressBarUpdate(this, 8, this.mSide_4);
					}
					if (this.mTimer % 500 == 10 || this.oTier != this.mTier) {
						var1.sendProgressBarUpdate(this, 10, this.mTier);
					}
					if (this.mTimer % 500 == 10 || this.oTickRate != this.mTickRate) {
						var1.sendProgressBarUpdate(this, 12, this.mTickRate);
					}
				} while (this.mTimer % 500 != 10);

			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBarEx(int par1, int par2) {
		super.updateProgressBar(par1, par2);
		switch (par1) {
			case 2 :
				this.mSide_1 = par2;
				break;
			case 4 :
				this.mSide_2 = par2;
				break;
			case 6 :
				this.mSide_3 = par2;			
			case 8 :
				this.mSide_4 = par2;		
			case 10 :
				this.mTier = par2;		
			case 12 :
				this.mTickRate = par2;
				break;
		}

	}

}