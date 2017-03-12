package gtPlusPlus.core.tileentities.general;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.inventories.InventoryHeliumGenerator;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityHeliumGenerator extends TileEntity{

	private int tickCount = 0;
	private final InventoryHeliumGenerator inventoryContents; //TODO
	private int locationX;
	private int locationY;
	private int locationZ;
	private int baseTickRate = 1200;

	public TileEntityHeliumGenerator(){
		this.inventoryContents = new InventoryHeliumGenerator();//number of slots - without product slot
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

	public InventoryHeliumGenerator getInventory(){
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
		final int lootWeight = MathUtils.randInt(0,  1000);
		ItemStack loot = GT_Values.NI;
		if (lootWeight > 990){
			loot = ItemUtils.getSimpleStack(Items.slime_ball);
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
				
			}
			else {

			}

			if (true){
				this.calculateTickrate();
			}

			//Try add some loot once every 30 seconds.
			if ((this.tickCount%this.baseTickRate)==0){
				if (true){
					//Add loot
					//Utils.LOG_INFO("Adding Loot to the fishtrap at x["+this.locationX+"] y["+this.locationY+"] z["+this.locationZ+"] (Ticking for loot every "+this.baseTickRate+" ticks)");
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
