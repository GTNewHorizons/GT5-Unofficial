package gtPlusPlus.core.tileentities.machines;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.general.fuelrods.FuelRod_Base;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityNHG extends TileEntity implements IInventory {
	private ItemStack[]	items				= new ItemStack[19];	// 18
	private int			progress			= 1;
	private final int	maxProgress			= 180;
	private int			heatCycleProgress	= 12;
	public float		coreTemp;
	public float		maxTemp				= 10000;
	private boolean		fuelrod_1			= false;
	private boolean		fuelrod_2			= false;
	private boolean		fuelrod_3			= false;
	private boolean		fuelrod_4			= false;
	private boolean		fuelrod_5			= false;
	private boolean		fuelrod_6			= false;
	private boolean		fuelrod_7			= false;
	private boolean		fuelrod_8			= false;
	private boolean		fuelrod_9			= false;
	private boolean		fuelrod_10			= false;
	private boolean		fuelrod_11			= false;
	private boolean		fuelrod_12			= false;
	private boolean		fuelrod_13			= false;
	private boolean		fuelrod_14			= false;
	private boolean		fuelrod_15			= false;
	private boolean		fuelrod_16			= false;
	private boolean		fuelrod_17			= false;
	private boolean		fuelrod_18			= false;

	// Machine Code - TODO
	private ItemStack neutrons;

	public boolean calculateHeat() {
		/*
		 * if (!fuelrod_1 || !fuelrod_2 || !fuelrod_3 || !fuelrod_4 ||
		 * !fuelrod_5 || !fuelrod_6 || !fuelrod_7 || !fuelrod_8 || !fuelrod_9 ||
		 * !fuelrod_10 || !fuelrod_11 || !fuelrod_12 || !fuelrod_13 ||
		 * !fuelrod_14 || !fuelrod_15 || !fuelrod_16 || !fuelrod_17 ||
		 * !fuelrod_18){ coreTemp = 0; } else {
		 */
		if (!this.worldObj.isRemote) {
			for (int i = 0; i < this.getSizeInventory(); i++) {
				if (this.items[i] != null) {
					if (this.items[i].getItem() instanceof FuelRod_Base) {
						final ItemStack fuelRodStack = this.getStackInSlot(i).copy();
						// if (fuelRodStack.stackTagCompound.getFloat("heat") !=
						// 0){
						this.doFuelRodHeatDamage(fuelRodStack);
						this.coreTemp = this.coreTemp + fuelRodStack.stackTagCompound.getFloat("heat");
						return true;
						// }
					}
				}
			}
		}
		// }

		return false;
	}

	public void checkFuelRods() {

		if (!this.worldObj.isRemote) {
			for (int i = 0; i < this.getSizeInventory(); i++) {
				if (this.items[i] != null) {
					if (this.items[i].getItem() instanceof FuelRod_Base) {
						final ItemStack fuelRodStack = this.getStackInSlot(i).copy();
						// setInventorySlotContents(i,
						// doFuelRodHeatDamage(fuelRodStack));
						if (i == 0) {
							this.fuelrod_1 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 1) {
							this.fuelrod_2 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 2) {
							this.fuelrod_3 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 3) {
							this.fuelrod_4 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 4) {
							this.fuelrod_5 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 5) {
							this.fuelrod_6 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 6) {
							this.fuelrod_7 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 7) {
							this.fuelrod_8 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 8) {
							this.fuelrod_9 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 9) {
							this.fuelrod_10 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 10) {
							this.fuelrod_11 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 11) {
							this.fuelrod_12 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 12) {
							this.fuelrod_13 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 13) {
							this.fuelrod_14 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 14) {
							this.fuelrod_15 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 15) {
							this.fuelrod_16 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 16) {
							this.fuelrod_17 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}
						else if (i == 17) {
							this.fuelrod_18 = true;
							final ItemStack r = this.doFuelRodHeatDamage(fuelRodStack);
							this.setInventorySlotContents(i, r);
						}

					}
				}
			}
			Utils.LOG_WARNING(
					"|" + this.fuelrod_1 + "|" + this.fuelrod_2 + "|" + this.fuelrod_3 + "|                    " + "|"
							+ this.fuelrod_10 + "|" + this.fuelrod_11 + "|" + this.fuelrod_12 + "|");
			Utils.LOG_WARNING(
					"|" + this.fuelrod_4 + "|" + this.fuelrod_5 + "|" + this.fuelrod_6 + "|                    " + "|"
							+ this.fuelrod_13 + "|" + this.fuelrod_14 + "|" + this.fuelrod_15 + "|");
			Utils.LOG_WARNING(
					"|" + this.fuelrod_7 + "|" + this.fuelrod_8 + "|" + this.fuelrod_9 + "|                    " + "|"
							+ this.fuelrod_16 + "|" + this.fuelrod_17 + "|" + this.fuelrod_18 + "|");
		}

	}

	@Override
	public void closeInventory() {
	}

	@Override
	public ItemStack decrStackSize(final int slot, final int amount) {
		if (this.items[slot] != null) {
			ItemStack itemstack;

			if (this.items[slot].stackSize == amount) {
				itemstack = this.items[slot];
				this.items[slot] = null;
				this.markDirty();
				return itemstack;
			}
			itemstack = this.items[slot].splitStack(amount);
			if (this.items[slot].stackSize == 0) {
				this.items[slot] = null;
			}
			this.markDirty();
			return itemstack;
		}
		return null;
	}

	public ItemStack doFuelRodHeatDamage(final ItemStack input) {
		if (!this.worldObj.isRemote) {
			if (input != null) {
				if (this.isValidFuelRod(input)) {
					final int fuelRodFuelLevel = this.getRodFuelValue(input);
					final float fuelRodHeatLevel = this.getRodHeatValue(input);
					if (fuelRodFuelLevel <= 0 || fuelRodFuelLevel == 0) {
						return null;
					}
					if (fuelRodHeatLevel == 0 && fuelRodFuelLevel > 0) {
						if (fuelRodFuelLevel >= 5) {
							final int tempInt = fuelRodFuelLevel;
							final float tempFloat = fuelRodHeatLevel;
							final ItemStack output = input.copy();
							if (input.stackTagCompound != null) {
								output.stackTagCompound = new NBTTagCompound();
								output.stackTagCompound.setInteger("fuelRemaining", tempInt - 40);
								output.stackTagCompound.setFloat("heat", tempFloat + 20);
							}
							return output;
						}
						return null;
					}
					else if (fuelRodHeatLevel >= 5 && fuelRodFuelLevel > 0) {
						final int tempInt = fuelRodFuelLevel;
						final float tempFloat = fuelRodHeatLevel;
						final ItemStack output = input.copy();
						if (input.stackTagCompound != null) {
							output.stackTagCompound = new NBTTagCompound();
							output.stackTagCompound.setInteger("fuelRemaining", tempInt - 5);
							output.stackTagCompound.setFloat("heat", tempFloat + 5);
						}
						return output;
					}

					else if (fuelRodHeatLevel >= 5 && fuelRodFuelLevel == 0) {
						final ItemStack output = input.copy();
						if (input.stackTagCompound != null) {
							output.stackTagCompound = new NBTTagCompound();
							output.stackTagCompound.setInteger("heat", -5);
						}
						return output;
					}
					else {
						return null;
					}
				}
			}
		}
		return null;
	}

	public float getCoreTemp() {
		return this.coreTemp;
	}

	@Override
	public String getInventoryName() {
		return "container.NHG";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public int getProgress() {
		return this.progress;
	}

	public int getRodFuelValue(final ItemStack value) {
		if (value != null) {
			if (value.stackTagCompound != null) {
				final int tempInt = value.stackTagCompound.getInteger("fuelRemaining");
				return tempInt;
			}
		}
		return 0;
	}

	public float getRodHeatValue(final ItemStack value) {
		if (value != null) {
			if (value.stackTagCompound != null) {
				return value.stackTagCompound.getFloat("heat");
			}
		}
		return 0f;
	}

	@Override
	public int getSizeInventory() {
		return this.items.length;
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {

		return this.items[slot];

	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int slot) {
		if (this.items[slot] != null) {
			final ItemStack itemstack = this.items[slot];
			this.items[slot] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack stack) {
		return true;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false
				: player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	public boolean isValidFuelRod(final ItemStack input) {
		if (!this.worldObj.isRemote) {
			if (input != null) {
				if (input.getItem() instanceof FuelRod_Base) {
					final int fuelRodFuelLevel = this.getRodFuelValue(input);
					final float fuelRodHeatLevel = this.getRodHeatValue(input);
					Utils.LOG_WARNING("Fuel Left: " + fuelRodFuelLevel + " Current Temp: " + fuelRodHeatLevel);
					return true;
					// return input.stackTagCompound.getInteger("code");
				}
			}

		}
		return false;
	}

	@Override
	public void openInventory() {
	}

	public void readCustomNBT(final NBTTagCompound tag) {
		this.neutrons = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Neutrons"));
		this.progress = tag.getInteger("Progress");
		this.coreTemp = tag.getFloat("coreTemp");
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		final NBTTagList list = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		this.items = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < list.tagCount(); ++i) {
			final NBTTagCompound comp = list.getCompoundTagAt(i);
			final int j = comp.getByte("Slot") & 255;
			if (j >= 0 && j < this.items.length) {
				this.items[j] = ItemStack.loadItemStackFromNBT(comp);
			}
		}
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack stack) {
		if (stack != null) {
			this.items[slot] = stack;
			if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
				stack.stackSize = this.getInventoryStackLimit();
			}

			this.markDirty();
		}
	}

	@Override
	public void updateEntity() {

		if (!this.worldObj.isRemote) {

			if (this.progress >= this.heatCycleProgress) {
				// Utils.LOG_SPECIFIC_WARNING("NFHG", "Updating Entity
				// "+this.getBlockType().getUnlocalizedName(), 376);
				if (MathUtils.divideXintoY(this.heatCycleProgress, this.maxProgress)) {
					Utils.LOG_SPECIFIC_WARNING("NFHG", "Updating Entity " + this.getBlockType().getUnlocalizedName(),
							378);
					this.calculateHeat();
					this.heatCycleProgress = 0;
				}
			}

			if (++this.progress >= this.maxProgress) {

				Utils.LOG_SPECIFIC_WARNING("NFHG", "Updating Entity " + this.getBlockType().getUnlocalizedName(), 338);
				if (this.items[18] != null) {
					final ItemStack checkOutput = this.getStackInSlot(18);
					if (this.neutrons == null) {
						this.neutrons = new ItemStack(ModItems.itemHeliumBlob, 1);
						if (checkOutput == null) {
							Utils.LOG_WARNING("ItemStack in Output slot is definitely null, making a new ItemStack.");
							this.setInventorySlotContents(18, this.neutrons);
							this.progress = 0;
							this.markDirty();
						}
						else {
							checkOutput.stackSize++;
							Utils.LOG_WARNING("Found an ItemStack to increase the size of. Current size is "
									+ this.neutrons.stackSize);

							this.progress = 0;
							this.markDirty();
						}
					}
					else if (checkOutput.getItem() == ModItems.itemHeliumBlob && checkOutput.stackSize < 64) {
						checkOutput.stackSize++;
						Utils.LOG_WARNING(
								"Found an ItemStack to increase size of. Current size is " + checkOutput.stackSize);
						this.progress = 0;
						this.markDirty();
					}
					else if (checkOutput.getItem() == ModItems.itemHeliumBlob && checkOutput.stackSize == 64) {
						Utils.LOG_WARNING("Output stack is full.");
						this.progress = 0;
						this.markDirty();
					}
				}
				else if (this.items[18] == null) {
					Utils.LOG_WARNING("ItemStack in Output slot is null");
					this.neutrons = new ItemStack(ModItems.itemHeliumBlob, 1);
					final ItemStack checkOutput = this.getStackInSlot(18);
					if (checkOutput == null) {
						Utils.LOG_WARNING("ItemStack in Output slot is definitely null, making a new ItemStack.");
						this.setInventorySlotContents(18, this.neutrons);
						this.progress = 0;
						this.markDirty();
					}
					else {
						Utils.LOG_WARNING("Found an ItemStack to increase the size of.");
						checkOutput.stackSize++;
						this.progress = 0;
						this.markDirty();
					}
				}
				this.checkFuelRods();
			}
			this.progress++;
		}
	}

	public void writeCustomNBT(final NBTTagCompound tag) {
		tag.setInteger("Progress", this.progress);
		tag.setFloat("coreTemp", this.coreTemp);
		if (this.neutrons != null) {
			final NBTTagCompound produce = new NBTTagCompound();
			this.neutrons.writeToNBT(produce);
			tag.setTag("Neutrons", produce);
		}
		else {
			tag.removeTag("Neutrons");
		}
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		final NBTTagList list = new NBTTagList();

		for (int i = 0; i < this.items.length; ++i) {
			if (this.items[i] != null) {
				final NBTTagCompound comp = new NBTTagCompound();
				comp.setByte("Slot", (byte) i);
				this.items[i].writeToNBT(comp);
				list.appendTag(comp);
			}
		}

		nbt.setTag("Items", list);
	}

}