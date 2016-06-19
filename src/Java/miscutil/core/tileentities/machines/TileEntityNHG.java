package miscutil.core.tileentities.machines;

import miscutil.core.item.ModItems;
import miscutil.core.item.general.fuelrods.FuelRod_Base;
import miscutil.core.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityNHG extends TileEntity implements IInventory
{
	private ItemStack[] items = new ItemStack[19]; //18
	private int progress = 1;
	private int maxProgress = 180;
	private int heatCycleProgress = 12;
	public float coreTemp;
	public float maxTemp = 10000;
	private boolean fuelrod_1 = false;
	private boolean fuelrod_2 = false;
	private boolean fuelrod_3 = false;
	private boolean fuelrod_4 = false;
	private boolean fuelrod_5 = false;
	private boolean fuelrod_6 = false;
	private boolean fuelrod_7 = false;
	private boolean fuelrod_8 = false;
	private boolean fuelrod_9 = false;
	private boolean fuelrod_10 = false;
	private boolean fuelrod_11 = false;
	private boolean fuelrod_12 = false;
	private boolean fuelrod_13 = false;
	private boolean fuelrod_14 = false;
	private boolean fuelrod_15 = false;
	private boolean fuelrod_16 = false;
	private boolean fuelrod_17 = false;
	private boolean fuelrod_18 = false;

	public float getCoreTemp(){
		return coreTemp;
	}

	public int getProgress(){
		return progress;
	}

	public boolean isValidFuelRod(ItemStack input){
		if(!this.worldObj.isRemote){
			if (input != null){
				if (input.getItem() instanceof FuelRod_Base){
					int fuelRodFuelLevel = getRodFuelValue(input);
					float fuelRodHeatLevel = getRodHeatValue(input);
					Utils.LOG_WARNING("Fuel Left: "+fuelRodFuelLevel+" Current Temp: "+fuelRodHeatLevel);	
					return true;
					//return input.stackTagCompound.getInteger("code");
				}
			}

		}
		return false;
	}

	public ItemStack doFuelRodHeatDamage(ItemStack input){
		if(!this.worldObj.isRemote){
			if (input != null){
				if (isValidFuelRod(input)){
					int fuelRodFuelLevel = getRodFuelValue(input);
					float fuelRodHeatLevel = getRodHeatValue(input);
					if(fuelRodFuelLevel <= 0 || fuelRodFuelLevel == 0){
						return null;
					}
					if(fuelRodHeatLevel == 0 && fuelRodFuelLevel > 0){	
						if(fuelRodFuelLevel >= 5){
							int tempInt=fuelRodFuelLevel;
							float tempFloat=fuelRodHeatLevel;
							ItemStack output = input.copy();
							if (input.stackTagCompound != null){
								output.stackTagCompound = new NBTTagCompound();
								output.stackTagCompound.setInteger("fuelRemaining", tempInt-40);
								output.stackTagCompound.setFloat("heat", tempFloat+20);
							}
							return output;
						}
						return null;
					}
					else if(fuelRodHeatLevel >= 5 && fuelRodFuelLevel > 0){
						int tempInt=fuelRodFuelLevel;
						float tempFloat=fuelRodHeatLevel;
						ItemStack output = input.copy();
						if (input.stackTagCompound != null){
							output.stackTagCompound = new NBTTagCompound();
							output.stackTagCompound.setInteger("fuelRemaining", tempInt-5);
							output.stackTagCompound.setFloat("heat", tempFloat+5);
						}
						return output;
					}				

					else if(fuelRodHeatLevel >= 5 && fuelRodFuelLevel == 0){
						ItemStack output = input.copy();
						if (input.stackTagCompound != null){
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

	public float getRodHeatValue(ItemStack value){
		if (value != null){
			if (value.stackTagCompound != null){
				return value.stackTagCompound.getFloat("heat");
			}
		}
		return 0f;
	}

	public int getRodFuelValue(ItemStack value){
		if (value != null){
			if (value.stackTagCompound != null){
				int tempInt = value.stackTagCompound.getInteger("fuelRemaining");
				return tempInt;
			}
		}
		return 0;
	}

	public void checkFuelRods(){

		if(!this.worldObj.isRemote){
			for (int i = 0; i < getSizeInventory(); i++){
				if (items[i] != null){
					if (items[i].getItem() instanceof FuelRod_Base){				
						ItemStack fuelRodStack = getStackInSlot(i).copy();					
						//setInventorySlotContents(i, doFuelRodHeatDamage(fuelRodStack));
						if (i == 0){
							fuelrod_1 = true;							
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);							
						}
						else if (i == 1){
							fuelrod_2 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);	
						}
						else if (i == 2){
							fuelrod_3 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);	
						}
						else if (i == 3){
							fuelrod_4 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);	
						}
						else if (i == 4){
							fuelrod_5 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);	
						}
						else if (i == 5){
							fuelrod_6 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);
						}
						else if (i == 6){
							fuelrod_7 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);
						}
						else if (i == 7){
							fuelrod_8 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);
						}
						else if (i == 8){
							fuelrod_9 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);
						}
						else if (i == 9){
							fuelrod_10 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);
						}
						else if (i == 10){
							fuelrod_11 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);
						}
						else if (i == 11){
							fuelrod_12 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);
						}
						else if (i == 12){
							fuelrod_13 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);
						}
						else if (i == 13){
							fuelrod_14 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);
						}
						else if (i == 14){
							fuelrod_15 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);
						}
						else if (i == 15){
							fuelrod_16 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);
						}
						else if (i == 16){
							fuelrod_17 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);
						}
						else if (i == 17){
							fuelrod_18 = true;
							ItemStack r = doFuelRodHeatDamage(fuelRodStack);
							setInventorySlotContents(i, r);
						}


					}
				}
			}
			Utils.LOG_WARNING("|"+fuelrod_1+"|"+fuelrod_2+"|"+fuelrod_3+"|                    "+"|"+fuelrod_10+"|"+fuelrod_11+"|"+fuelrod_12+"|");
			Utils.LOG_WARNING("|"+fuelrod_4+"|"+fuelrod_5+"|"+fuelrod_6+"|                    "+"|"+fuelrod_13+"|"+fuelrod_14+"|"+fuelrod_15+"|");
			Utils.LOG_WARNING("|"+fuelrod_7+"|"+fuelrod_8+"|"+fuelrod_9+"|                    "+"|"+fuelrod_16+"|"+fuelrod_17+"|"+fuelrod_18+"|");
		}

	}

	public boolean calculateHeat(){
		/*if (!fuelrod_1 || !fuelrod_2 ||  !fuelrod_3 ||  !fuelrod_4 ||  !fuelrod_5 ||  !fuelrod_6 ||  !fuelrod_7 || !fuelrod_8 || !fuelrod_9 || !fuelrod_10 || !fuelrod_11 || !fuelrod_12 || !fuelrod_13 || !fuelrod_14 || !fuelrod_15 || !fuelrod_16 || !fuelrod_17 || !fuelrod_18){
			coreTemp = 0;
		}
		else {*/
		if(!this.worldObj.isRemote){
			for (int i = 0; i < getSizeInventory(); i++){
				if (items[i] != null){
					if (items[i].getItem() instanceof FuelRod_Base){				
						ItemStack fuelRodStack = getStackInSlot(i).copy();	
						//if (fuelRodStack.stackTagCompound.getFloat("heat") != 0){
						doFuelRodHeatDamage(fuelRodStack);
						coreTemp = coreTemp+fuelRodStack.stackTagCompound.getFloat("heat");
						return true;
						//}
					}
				}
			}
		}
		//}



		return false;
	}

	@Override
	public int getSizeInventory()
	{
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{

		return items[slot];

	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if (items[slot] != null)
		{
			ItemStack itemstack;

			if (items[slot].stackSize == amount)
			{
				itemstack = items[slot];
				items[slot] = null;
				markDirty();
				return itemstack;
			}
			itemstack = items[slot].splitStack(amount);
			if (items[slot].stackSize == 0) items[slot] = null;
			markDirty();
			return itemstack;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if (items[slot] != null)
		{
			ItemStack itemstack = items[slot];
			items[slot] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		if (stack != null){
			items[slot] = stack;
			if (stack != null && stack.stackSize > getInventoryStackLimit())
			{
				stack.stackSize = getInventoryStackLimit();
			}


			markDirty();
		}
	}

	@Override
	public String getInventoryName()
	{
		return "container.NHG";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		NBTTagList list = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		items = new ItemStack[getSizeInventory()];

		for (int i = 0; i < list.tagCount(); ++i) { NBTTagCompound comp = list.getCompoundTagAt(i); int j = comp.getByte("Slot") & 255; if (j >= 0 && j < items.length)
		{
			items[j] = ItemStack.loadItemStackFromNBT(comp);
		}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();

		for (int i = 0; i < items.length; ++i)
		{
			if (items[i] != null)
			{
				NBTTagCompound comp = new NBTTagCompound();
				comp.setByte("Slot", (byte)i);
				items[i].writeToNBT(comp);
				list.appendTag(comp);
			}
		}

		nbt.setTag("Items", list);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return true;
	}

	//Machine Code - TODO
	private ItemStack neutrons;

	@Override
	public void updateEntity() {

		if(!this.worldObj.isRemote){	

			if(progress >= heatCycleProgress){
				//Utils.LOG_SPECIFIC_WARNING("NFHG", "Updating Entity "+this.getBlockType().getUnlocalizedName(), 376);
				if (Utils.divideXintoY(heatCycleProgress, maxProgress)){
					Utils.LOG_SPECIFIC_WARNING("NFHG", "Updating Entity "+this.getBlockType().getUnlocalizedName(), 378);
					calculateHeat();
					heatCycleProgress=0;
				}
			}	

			if(++progress >= maxProgress){


				Utils.LOG_SPECIFIC_WARNING("NFHG", "Updating Entity "+this.getBlockType().getUnlocalizedName(), 338);
				if (items[18] != null){
					ItemStack checkOutput = getStackInSlot(18);
					if(neutrons == null){
						neutrons = new ItemStack(ModItems.itemHeliumBlob, 1);					
						if (checkOutput == null){
							Utils.LOG_WARNING("ItemStack in Output slot is definitely null, making a new ItemStack.");
							setInventorySlotContents(18, neutrons);
							progress = 0;
							markDirty();
						}
						else {
							checkOutput.stackSize++;
							Utils.LOG_WARNING("Found an ItemStack to increase the size of. Current size is "+neutrons.stackSize);

							progress = 0;
							markDirty();
						}
					}
					else if(checkOutput.getItem() == ModItems.itemHeliumBlob && checkOutput.stackSize < 64){
						checkOutput.stackSize++;
						Utils.LOG_WARNING("Found an ItemStack to increase size of. Current size is "+checkOutput.stackSize);
						progress = 0;
						markDirty();
					}
					else if(checkOutput.getItem() == ModItems.itemHeliumBlob && checkOutput.stackSize == 64){
						Utils.LOG_WARNING("Output stack is full.");
						progress = 0;
						markDirty();
					}
				}
				else if (items[18] == null){
					Utils.LOG_WARNING("ItemStack in Output slot is null");
					neutrons = new ItemStack(ModItems.itemHeliumBlob, 1);
					ItemStack checkOutput = getStackInSlot(18);
					if (checkOutput == null){
						Utils.LOG_WARNING("ItemStack in Output slot is definitely null, making a new ItemStack.");
						setInventorySlotContents(18, neutrons);
						progress = 0;
						markDirty();
					}
					else {
						Utils.LOG_WARNING("Found an ItemStack to increase the size of.");
						checkOutput.stackSize++;						
						progress = 0;
						markDirty();
					}
				}
				checkFuelRods();
			}
			progress++;
		}
	}

	public void readCustomNBT(NBTTagCompound tag)
	{
		this.neutrons = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Neutrons"));
		this.progress = tag.getInteger("Progress");
		this.coreTemp = tag.getFloat("coreTemp");
	}

	public void writeCustomNBT(NBTTagCompound tag)
	{
		tag.setInteger("Progress", this.progress);
		tag.setFloat("coreTemp", this.coreTemp);
		if(neutrons != null) {
			NBTTagCompound produce = new NBTTagCompound();
			neutrons.writeToNBT(produce);
			tag.setTag("Neutrons", produce);
		}
		else
			tag.removeTag("Neutrons");
	}



}