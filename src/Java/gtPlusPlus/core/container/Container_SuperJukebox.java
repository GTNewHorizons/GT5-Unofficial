package gtPlusPlus.core.container;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.block.machine.Machine_SuperJukebox.TileEntitySuperJukebox;
import gtPlusPlus.core.inventories.Inventory_SuperJukebox;
import gtPlusPlus.core.slots.SlotIntegratedCircuit;
import gtPlusPlus.core.slots.SlotJukebox;
import gtPlusPlus.core.slots.SlotNoInput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Container_SuperJukebox extends Container {

	protected TileEntitySuperJukebox tile_entity;
	public final Inventory_SuperJukebox inventoryChest;

	private final World worldObj;
	private final int posX;
	private final int posY;
	private final int posZ;


	public static final int SLOT_HOLO_PLAY = 18;
	public static final int SLOT_HOLO_LOOP = 19;
	public static final int SLOT_OUTPUT = 20;

	public static int StorageSlotNumber = 26; // Number of slots in storage area
	public static int InventorySlotNumber = 36; // Inventory Slots (Inventory
	// and Hotbar)
	public static int FullSlotNumber = InventorySlotNumber + StorageSlotNumber; // All
	// slots

	public Container_SuperJukebox(final InventoryPlayer inventory, final TileEntitySuperJukebox te) {
		this.tile_entity = te;
		this.inventoryChest = te.getInventory();

		int var6;
		int var7;
		this.worldObj = te.getWorldObj();
		this.posX = te.xCoord;
		this.posY = te.yCoord;
		this.posZ = te.zCoord;
		Logger.INFO("1");

		int o = 0;

		// Storage Side
		/*for (var6 = 0; var6 < 3; var6++) {
			for (var7 = 0; var7 < 5; var7++) {
				this.addSlotToContainer(new SlotIntegratedCircuit(o, this.inventoryChest, o, 44 + (var7 * 18), 15 + (var6 * 18)));
				o++;
			}
		}*/


		int xStart = 9;
		int yStart = 20;

		try {

			//Row One
			for (int c = 0; c < 9; c++) {
				if (c >= 3 && c < 6) {
					continue;
				}
				this.addSlotToContainer(new SlotJukebox(this.inventoryChest, o++, xStart+(18*c), yStart));
			}

			//Row Two
			for (int c = 0; c < 9; c++) {
				if (c >= 3 && c < 6) {
					continue;
				}
				this.addSlotToContainer(new SlotJukebox(this.inventoryChest, o++, xStart+(18*c), yStart+18));
			}

			//Row Two
			for (int c = 0; c < 9; c++) {
				if (c >= 3 && c < 6) {
					continue;
				}
				this.addSlotToContainer(new SlotJukebox(this.inventoryChest, o++, xStart+(18*c), yStart+36));
			}


			//Controls
			int c = 4;		

			//Two Control Buttons
			this.addSlotToContainer(new SlotNoInput(this.inventoryChest, SLOT_HOLO_PLAY, xStart+(18*c), 12));
			this.addSlotToContainer(new SlotNoInput(this.inventoryChest, SLOT_HOLO_LOOP, xStart+(18*c), 12+(1*18)));

			//Active playing slot for visual
			this.addSlotToContainer(new SlotJukebox(this.inventoryChest, SLOT_OUTPUT, xStart+(18*c), 18+(2*18), true));




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


			Logger.INFO("3");

		}
		catch (Throwable t) {
			t.printStackTrace();
		}

	}

	@Override
	public void onContainerClosed(final EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);
	}

	@Override
	public boolean canInteractWith(final EntityPlayer par1EntityPlayer) {
		if (this.worldObj.getBlock(this.posX, this.posY, this.posZ) != ModBlocks.blockCustomJukebox) {
			return false;
		}

		return par1EntityPlayer.getDistanceSq(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) <= 64D;
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2) {
		ItemStack var3 = null;
		final Slot var4 = (Slot) this.inventorySlots.get(par2);

		if ((var4 != null) && var4.getHasStack()) {
			final ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			/*
			 * if (par2 == 0) { if (!this.mergeItemStack(var5,
			 * InOutputSlotNumber, FullSlotNumber, true)) { return null; }
			 * 
			 * var4.onSlotChange(var5, var3); } else if (par2 >=
			 * InOutputSlotNumber && par2 < InventoryOutSlotNumber) { if
			 * (!this.mergeItemStack(var5, InventoryOutSlotNumber,
			 * FullSlotNumber, false)) { return null; } } else if (par2 >=
			 * InventoryOutSlotNumber && par2 < FullSlotNumber) { if
			 * (!this.mergeItemStack(var5, InOutputSlotNumber,
			 * InventoryOutSlotNumber, false)) { return null; } } else if
			 * (!this.mergeItemStack(var5, InOutputSlotNumber, FullSlotNumber,
			 * false)) { return null; }
			 */

			if (var5.stackSize == 0) {
				var4.putStack((ItemStack) null);
			} else {
				var4.onSlotChanged();
			}

			if (var5.stackSize == var3.stackSize) {
				return null;
			}

			var4.onPickupFromSlot(par1EntityPlayer, var5);
		}

		return var3;
	}

	// Can merge Slot
	@Override
	public boolean func_94530_a(final ItemStack p_94530_1_, final Slot p_94530_2_) {
		return super.func_94530_a(p_94530_1_, p_94530_2_);
	}



	@Override
	public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
		if (tile_entity == null || tile_entity.getWorldObj().isRemote) return null;
		switch (aSlotIndex) {
		case SLOT_HOLO_PLAY:
			if (tile_entity == null) return null;
			tile_entity.mIsPlaying = !tile_entity.mIsPlaying;
			Logger.INFO("Jukebox | Playing: "+tile_entity.mIsPlaying);
			tile_entity.jukeboxLogicUpdate();
			return null;
		case SLOT_HOLO_LOOP:
			if (tile_entity == null) return null;
			tile_entity.mIsLooping = !tile_entity.mIsLooping;
			Logger.INFO("Jukebox | Looping: "+tile_entity.mIsLooping);
			return null;
		case 20:
			return null;
		default:
			return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
		}
	}

	public boolean isPlaying;
	public boolean isLooping;

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (tile_entity == null || tile_entity.getWorldObj().isRemote) return;

		isPlaying = tile_entity.mIsPlaying;
		isLooping = tile_entity.mIsLooping;

		Iterator var2 = this.crafters.iterator();
		while (var2.hasNext()) {
			ICrafting var1 = (ICrafting) var2.next();
			var1.sendProgressBarUpdate(this, 102, isPlaying ? 1 : 0);
			var1.sendProgressBarUpdate(this, 103, isLooping ? 1 : 0);
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting) {
		super.addCraftingToCrafters(par1ICrafting);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		super.updateProgressBar(par1, par2);
		switch (par1) {
		case 102:
			isPlaying = (par2 != 0);
			break;
		case 103:
			isLooping = (par2 != 0);
			break;
		}
	}
}