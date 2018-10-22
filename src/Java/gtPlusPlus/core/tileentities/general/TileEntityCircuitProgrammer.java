package gtPlusPlus.core.tileentities.general;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import gregtech.api.util.GT_Utility;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.inventories.InventoryCircuitProgrammer;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraftforge.common.FishingHooks;

public class TileEntityCircuitProgrammer extends TileEntity implements ISidedInventory {

	private int tickCount = 0;
	private boolean isInWater = false;
	private final InventoryCircuitProgrammer inventoryContents;
	private String customName;
	private int locationX;
	private int locationY;
	private int locationZ;
	private int waterSides = 0;
	private int baseTickRate = 600 * 5;

	public TileEntityCircuitProgrammer() {
		this.inventoryContents = new InventoryCircuitProgrammer();// number of slots -
		// without product
		// slot
		this.setTileLocation();
	}

	public boolean setTileLocation() {
		if (this.hasWorldObj()) {
			if (!this.getWorldObj().isRemote) {
				this.locationX = this.xCoord;
				this.locationY = this.yCoord;
				this.locationZ = this.zCoord;
				return true;
			}
		}
		return false;
	}

	public final boolean isSurroundedByWater() {
		this.setTileLocation();
		final Block[] surroundingBlocks = new Block[6];
		if (this.hasWorldObj()) {
			if (!this.getWorldObj().isRemote) {
				surroundingBlocks[0] = this.worldObj.getBlock(this.locationX, this.locationY + 1, this.locationZ); // Above
				surroundingBlocks[1] = this.worldObj.getBlock(this.locationX, this.locationY - 1, this.locationZ); // Below
				surroundingBlocks[2] = this.worldObj.getBlock(this.locationX + 1, this.locationY, this.locationZ);
				surroundingBlocks[3] = this.worldObj.getBlock(this.locationX - 1, this.locationY, this.locationZ);
				surroundingBlocks[4] = this.worldObj.getBlock(this.locationX, this.locationY, this.locationZ + 1);
				surroundingBlocks[5] = this.worldObj.getBlock(this.locationX, this.locationY, this.locationZ - 1);
				int waterCount = 0;
				int trapCount = 0;
				for (final Block checkBlock : surroundingBlocks) {
					if ((checkBlock == Blocks.water) || (checkBlock == Blocks.flowing_water)
							|| checkBlock.getUnlocalizedName().toLowerCase().contains("water")
							|| (checkBlock == ModBlocks.blockCircuitProgrammer)) {
						if (checkBlock != ModBlocks.blockCircuitProgrammer) {
							waterCount++;
						}
						else {
							waterCount++;
							trapCount++;
						}
					}
				}
				if ((waterCount >= 2) && (trapCount <= 4)) {
					this.waterSides = waterCount;
					return true;
				}
				else if ((waterCount >= 2) && (trapCount > 4)) {
					Logger.WARNING("Too many fish traps surrounding this one.");
					Logger.WARNING("Not adding Loot to the fishtrap at x[" + this.locationX + "] y[" + this.locationY
							+ "] z[" + this.locationZ + "] (Ticking for loot every " + this.baseTickRate + " ticks)");
				}
			}
		}
		// Utils.LOG_WARNING("Error finding water");
		return false;
	}

	public InventoryCircuitProgrammer getInventory() {
		return this.inventoryContents;
	}

	public boolean tryAddLoot() {
		if (this.getInventory().getInventory() != null) {
			int checkingSlot = 0;
			final ItemStack loot = this.generateLootForCircuitProgrammer().copy();
			try {
				//Utils.LOG_WARNING("Trying to add "+loot.getDisplayName()+" | "+loot.getItemDamage());
				for (final ItemStack contents : this.getInventory().getInventory()) {


					if (GT_Utility.areStacksEqual(loot, contents)){
						if (contents.stackSize < contents.getMaxStackSize()) {
							//Utils.LOG_WARNING("3-Trying to add one more "+loot.getDisplayName()+"meta: "+loot.getItemDamage()+" to an existing stack of "+contents.getDisplayName()+" with a size of "+contents.stackSize);
							contents.stackSize++;
							this.markDirty();
							return true;
						}
					}
					checkingSlot++;
				}
				checkingSlot = 0;
				for (final ItemStack contents : this.getInventory().getInventory()) {
					if (contents == null) {
						//Utils.LOG_WARNING("Adding Item To Empty Slot. "+(checkingSlot+1));
						this.getInventory().setInventorySlotContents(checkingSlot, loot);
						this.markDirty();
						return true;
					}
					checkingSlot++;
				}
			}
			catch (final NullPointerException n) {
			}
		}
		this.markDirty();
		return false;
	}

	private ItemStack generateLootForCircuitProgrammer() {
		final int lootWeight = MathUtils.randInt(0, 100);
		ItemStack loot;
		if (lootWeight <= 5) {
			loot = ItemUtils.getSimpleStack(Items.slime_ball);
		}
		else if (lootWeight <= 10) {
			loot = ItemUtils.getSimpleStack(Items.bone);
		}
		else if (lootWeight <= 15) {
			loot = ItemUtils.getSimpleStack(Blocks.sand);
		}
		else if (lootWeight <= 20) {
			loot = ItemUtils.simpleMetaStack(Items.dye, 0, 1);
		}
		// Junk Loot
		else if (lootWeight <= 23) {
			if (LoadedMods.PamsHarvestcraft) {
				loot = ItemUtils.getItemStackOfAmountFromOreDictNoBroken(seaweed, 1);
			}
			else {
				loot = ItemUtils.getSimpleStack(Blocks.dirt);
			}
		}
		// Pam Fish
		else if (lootWeight <= 99) {
			final Random xstr = new Random();
			loot = FishingHooks.getRandomFishable(xstr, 100);
		}

		else if (lootWeight == 100){
			final int rareLoot = MathUtils.randInt(1, 10);
			if (rareLoot <= 4) {
				loot = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("nuggetIron", 1);
				if (loot == null){
					loot = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingotIron", 1);
				}
			}
			else if (rareLoot <= 7) {
				loot = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("nuggetGold", 1);
				if (loot == null){
					loot = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingotGold", 1);
				}
			}
			else if (rareLoot <= 9){
				loot = ItemUtils.getSimpleStack(Items.emerald);
			}
			else {
				loot = ItemUtils.getSimpleStack(Items.diamond);
			}
		}
		else {
			loot = ItemUtils.getSimpleStack(Blocks.diamond_ore);
		}
		loot.stackSize=1;
		Logger.WARNING("Adding x"+loot.stackSize+" "+loot.getDisplayName()+".");
		return loot;
	}

	@Override
	public void updateEntity() {
		try{
			if (!this.worldObj.isRemote) {
				this.tickCount++;
				// Utils.LOG_WARNING("Ticking "+this.tickCount);
				// Check if the Tile is within water once per second.
				if ((this.tickCount % 20) == 0) {
					this.isInWater = this.isSurroundedByWater();
				}
				else {

				}

				if (this.isInWater) {
					this.calculateTickrate();
				}

				// Try add some loot once every 30 seconds.
				if ((this.tickCount % this.baseTickRate) == 0) {
					if (this.isInWater) {
						// Add loot
						// Utils.LOG_WARNING("Adding Loot to the fishtrap at
						// x["+this.locationX+"] y["+this.locationY+"]
						// z["+this.locationZ+"] (Ticking for loot every
						// "+this.baseTickRate+" ticks)");
						this.tryAddLoot();
						this.markDirty();
					}
					else {
						Logger.WARNING("This Trap does not have enough water around it.");
						Logger.WARNING("Not adding Loot to the fishtrap at x[" + this.locationX + "] y[" + this.locationY
								+ "] z[" + this.locationZ + "] (Ticking for loot every " + this.baseTickRate + " ticks)");
						this.markDirty();
					}
					this.tickCount = 0;
				}
				if (this.tickCount > (this.baseTickRate + 500)) {
					this.tickCount = 0;
				}

			}
		}
		catch (final Throwable t){}
	}

	public void calculateTickrate() {
		int calculateTickrate = 0;
		if (this.waterSides <= 2) {
			calculateTickrate = 0;
		}
		else if ((this.waterSides > 2) && (this.waterSides < 4)) {
			calculateTickrate = 4800;
		}
		else if ((this.waterSides >= 4) && (this.waterSides < 6)) {
			calculateTickrate = 3600;
		}
		else if (this.waterSides == 6) {
			calculateTickrate = 2400;
		}
		this.baseTickRate = calculateTickrate;
	}

	public boolean anyPlayerInRange() {
		return this.worldObj.getClosestPlayer(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, 32) != null;
	}

	public NBTTagCompound getTag(final NBTTagCompound nbt, final String tag) {
		if (!nbt.hasKey(tag)) {
			nbt.setTag(tag, new NBTTagCompound());
		}
		return nbt.getCompoundTag(tag);
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		// Utils.LOG_WARNING("Trying to write NBT data to TE.");
		final NBTTagCompound chestData = new NBTTagCompound();
		this.inventoryContents.writeToNBT(chestData);
		nbt.setTag("ContentsChest", chestData);
		if (this.hasCustomInventoryName()) {
			nbt.setString("CustomName", this.getCustomName());
		}
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		// Utils.LOG_WARNING("Trying to read NBT data from TE.");
		this.inventoryContents.readFromNBT(nbt.getCompoundTag("ContentsChest"));
		if (nbt.hasKey("CustomName", 8)) {
			this.setCustomName(nbt.getString("CustomName"));
		}
	}

	final static String prefix = "food";
	final static String suffix = "raw";
	final static String seaweed = "cropSeaweed";
	final static String greenheartFish = "Greenheartfish";
	private static final String[] harvestcraftFish = { "Anchovy", "Bass", "Carp", "Catfish", "Charr", "Clam", "Crab",
			"Crayfish", "Eel", "Frog", "Grouper", "Herring", "Jellyfish", "Mudfish", "Octopus", "Perch", "Scallop",
			"Shrimp", "Snail", "Snapper", "Tilapia", "Trout", "Tuna", "Turtle", "Walleye" };

	public static void pamsHarvestCraftCompat() {
		for (int i = 0; i < harvestcraftFish.length; i++) {

		}
	}

	@Override
	public int getSizeInventory() {
		return this.getInventory().getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		return this.getInventory().getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(final int slot, final int count) {
		return this.getInventory().decrStackSize(slot, count);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int slot) {
		return this.getInventory().getStackInSlotOnClosing(slot);
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack stack) {
		this.getInventory().setInventorySlotContents(slot, stack);
	}

	@Override
	public int getInventoryStackLimit() {
		return this.getInventory().getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer entityplayer) {
		return this.getInventory().isUseableByPlayer(entityplayer);
	}

	@Override
	public void openInventory() {
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 1);
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
		this.getInventory().openInventory();
	}

	@Override
	public void closeInventory() {
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 1);
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
		this.getInventory().closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack itemstack) {
		return this.getInventory().isItemValidForSlot(slot, itemstack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
		final int[] accessibleSides = new int[this.getSizeInventory()];
		for (int r=0; r<this.getInventory().getSizeInventory(); r++){
			accessibleSides[r]=r;
		}
		return accessibleSides;

	}

	@Override
	public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_, final int p_102007_3_) {
		return false;
	}

	@Override
	public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
		return true;
	}

	public String getCustomName() {
		return this.customName;
	}

	public void setCustomName(final String customName) {
		this.customName = customName;
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "container.fishtrap";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return (this.customName != null) && !this.customName.equals("");
	}

}
