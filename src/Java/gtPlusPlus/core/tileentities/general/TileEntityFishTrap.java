package gtPlusPlus.core.tileentities.general;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.inventories.InventoryFishTrap;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFishTrap extends TileEntity{

	private int tickCount = 0;
	private boolean isInWater = false;
	private final InventoryFishTrap inventoryContents;
	private int locationX;
	private int locationY;
	private int locationZ;
	private int waterSides = 0;
	private int baseTickRate = 600*5;

	public TileEntityFishTrap(){
		this.inventoryContents = new InventoryFishTrap();//number of slots - without product slot
		this.setTileLocation();
	}

	public boolean setTileLocation(){
		if (this.hasWorldObj()){
			if (!this.getWorldObj().isRemote){
				this.locationX = this.xCoord;
				this.locationY = this.yCoord;
				this.locationZ = this.zCoord;
				return true;
			}
		}
		return false;
	}

	public final boolean isSurroundedByWater(){
		this.setTileLocation();
		final Block[] surroundingBlocks = new Block[6];
		if (this.hasWorldObj()){
			if (!this.getWorldObj().isRemote){
				surroundingBlocks[0] = this.worldObj.getBlock(this.locationX, this.locationY+1, this.locationZ); //Above
				surroundingBlocks[1] = this.worldObj.getBlock(this.locationX, this.locationY-1, this.locationZ); //Below
				surroundingBlocks[2] = this.worldObj.getBlock(this.locationX+1, this.locationY, this.locationZ);
				surroundingBlocks[3] = this.worldObj.getBlock(this.locationX-1, this.locationY, this.locationZ);
				surroundingBlocks[4] = this.worldObj.getBlock(this.locationX, this.locationY, this.locationZ+1);
				surroundingBlocks[5] = this.worldObj.getBlock(this.locationX, this.locationY, this.locationZ-1);
				int waterCount = 0;
				int trapCount = 0;
				for (final Block checkBlock : surroundingBlocks){
					if ((checkBlock == Blocks.water) || (checkBlock == Blocks.flowing_water) || checkBlock.getUnlocalizedName().toLowerCase().contains("water") || (checkBlock == ModBlocks.blockFishTrap)){
						if (checkBlock != ModBlocks.blockFishTrap){
							waterCount++;
						}
						else {
							waterCount++;
							trapCount++;
						}
					}
				}
				if ((waterCount >= 2) && (trapCount <= 4)){
					this.waterSides = waterCount;
					return true;
				}
				else if ((waterCount >= 2) && (trapCount > 4)){
					Utils.LOG_INFO("Too many fish traps surrounding this one.");
					Utils.LOG_INFO("Not adding Loot to the fishtrap at x["+this.locationX+"] y["+this.locationY+"] z["+this.locationZ+"] (Ticking for loot every "+this.baseTickRate+" ticks)");
				}
			}
		}
		//Utils.LOG_INFO("Error finding water");
		return false;
	}

	public InventoryFishTrap getInventory(){
		return this.inventoryContents;
	}

	public boolean tryAddLoot(){
		if (this.getInventory().getInventory() != null){
			int checkingSlot = 0;
			final ItemStack loot = this.generateLootForFishTrap();
			for (final ItemStack contents : this.getInventory().getInventory()){
				if (contents == null){
					this.getInventory().setInventorySlotContents(checkingSlot, loot);
					this.markDirty();
					return true;
				}
				else if (contents.getItem() == loot.getItem()){
					if (contents.stackSize < contents.getMaxStackSize()){
						contents.stackSize++;
						this.markDirty();
						return true;
					}
					else {
						this.getInventory().setInventorySlotContents(checkingSlot, loot);
						this.markDirty();
						return true;
					}
				}
				else {

				}
				checkingSlot++;
			}
		}
		this.markDirty();
		return false;
	}

	private ItemStack generateLootForFishTrap() {
		final int lootWeight = MathUtils.randInt(0,  100);
		ItemStack loot;
		if (lootWeight <= 10){
			loot = ItemUtils.getSimpleStack(Items.slime_ball);
		}
		else if (lootWeight <= 20){
			loot = ItemUtils.getSimpleStack(Items.bone);
		}
		else if (lootWeight <= 40){
			loot = ItemUtils.getSimpleStack(Blocks.sand);
		}
		else if (lootWeight <= 80){
			loot = ItemUtils.getSimpleStack(Items.fish);
		}
		else if (lootWeight <= 100){
			loot = ItemUtils.getSimpleStack(Items.fish);
		}
		else {
			loot = ItemUtils.getSimpleStack(Blocks.diamond_ore);
		}
		return loot;
	}

	@Override
	public void updateEntity(){
		if (!this.worldObj.isRemote){
			this.tickCount++;
			//Utils.LOG_INFO("Ticking "+this.tickCount);
			//Check if the Tile is within water once per second.
			if ((this.tickCount%20)==0){
				this.isInWater = this.isSurroundedByWater();
			}
			else {

			}

			if (this.isInWater){
				this.calculateTickrate();
			}

			//Try add some loot once every 30 seconds.
			if ((this.tickCount%this.baseTickRate)==0){
				if (this.isInWater){
					//Add loot
					Utils.LOG_INFO("Adding Loot to the fishtrap at x["+this.locationX+"] y["+this.locationY+"] z["+this.locationZ+"] (Ticking for loot every "+this.baseTickRate+" ticks)");
					this.tryAddLoot();
					this.markDirty();
				}
				else {
					Utils.LOG_INFO("This Trap does not have enough water around it.");
					Utils.LOG_INFO("Not adding Loot to the fishtrap at x["+this.locationX+"] y["+this.locationY+"] z["+this.locationZ+"] (Ticking for loot every "+this.baseTickRate+" ticks)");
					this.markDirty();
				}
				this.tickCount = 0;
			}
			if (this.tickCount > (this.baseTickRate+500)){
				this.tickCount = 0;
			}


		}
	}

	public void calculateTickrate(){
		int calculateTickrate = 0;
		if (this.waterSides < 2){
			calculateTickrate = 0;
		}
		else if ((this.waterSides >= 2) && (this.waterSides < 4)){
			calculateTickrate = 3000;
		}
		else if ((this.waterSides >= 4) && (this.waterSides < 6)){
			calculateTickrate = 2000;
		}
		else if (this.waterSides == 6){
			calculateTickrate = 900;
		}
		this.baseTickRate = calculateTickrate;
	}

	public boolean anyPlayerInRange(){
		return this.worldObj.getClosestPlayer(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, 32) != null;
	}

	public NBTTagCompound getTag(final NBTTagCompound nbt, final String tag){
		if(!nbt.hasKey(tag)){
			nbt.setTag(tag, new NBTTagCompound());
		}
		return nbt.getCompoundTag(tag);
	}


	@Override
	public void writeToNBT(final NBTTagCompound nbt){
		super.writeToNBT(nbt);
		//Utils.LOG_INFO("Trying to write NBT data to TE.");
		final NBTTagCompound chestData = new NBTTagCompound();
		this.inventoryContents.writeToNBT(chestData);
		nbt.setTag("ContentsChest", chestData);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt){
		super.readFromNBT(nbt);
		//Utils.LOG_INFO("Trying to read NBT data from TE.");
		this.inventoryContents.readFromNBT(nbt.getCompoundTag("ContentsChest"));
	}

}
