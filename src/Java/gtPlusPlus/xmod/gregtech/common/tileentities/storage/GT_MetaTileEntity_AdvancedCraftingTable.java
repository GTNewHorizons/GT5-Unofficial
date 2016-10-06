package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_AdvancedWorkbench;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_AdvancedWorkbench;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_AdvancedCraftingTable
extends GT_MetaTileEntity_BasicTank
{
	public boolean mFlushMode = false;

	public GT_MetaTileEntity_AdvancedCraftingTable(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 3, "WorkBench pro noSc0pe");
	}

	public GT_MetaTileEntity_AdvancedCraftingTable(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
	}    

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		Utils.LOG_INFO("Right Click on Block");
		if (aBaseMetaTileEntity.isClientSide()){
			Utils.LOG_INFO("MTE is ClientSide");
			return true;
		}
		Utils.LOG_INFO("MTE is not ClientSide");
		aBaseMetaTileEntity.openGUI(aPlayer);
		Utils.LOG_INFO("MTE is now has an open GUI");
		return true;
	}     

	@Override
	public String[] getInfoData() {

		if (mFluid == null) {
			return new String[]{
					GT_Values.VOLTAGE_NAMES[mTier]+" Workbench",
					"Stored Fluid:",
					"No Fluid",
					Integer.toString(0) + "L",
					Integer.toString(getCapacity()) + "L"};
		}
		return new String[]{
				GT_Values.VOLTAGE_NAMES[mTier]+" Workbench",
				"Stored Fluid:",
				mFluid.getLocalizedName(),
				Integer.toString(mFluid.amount) + "L",
				Integer.toString(getCapacity()) + "L"};
	}    

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		Utils.LOG_INFO("Dumping Fluid data. Name: "+mFluid.getFluid().getName()+" Amount: "+mFluid.amount+"L");
		if (mFluid != null) aNBT.setTag("mFluid", mFluid.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		mFluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid"));       
	}

	@Override
	public boolean isTransformerUpgradable()
	{
		return true;
	}

	public boolean isBatteryUpgradable()
	{
		return true;
	}

	@Override
	public boolean isSimpleMachine()
	{
		return true;
	}

	@Override
	public boolean isValidSlot(int aIndex)
	{
		return (aIndex < 31) || (aIndex > 32);
	}

	@Override
	public boolean isFacingValid(byte aFacing)
	{
		return true;
	}

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer)
	{
		return true;
	}

	@Override
	public boolean isEnetInput()
	{
		return true;
	}

	@Override
	public boolean isInputFacing(byte aSide)
	{
		return true;
	}

	@Override
	public long maxEUInput()
	{
		return 128;
	}

	@Override
	public long maxEUStore()
	{
		return 128000;
	}

	public int getInvSize()
	{
		return 35;
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity)
	{
		return new GT_MetaTileEntity_AdvancedCraftingTable(mName, mTier, mDescription, mTextures);
	}

	public void onRightclick(EntityPlayer aPlayer)
	{
		getBaseMetaTileEntity().openGUI(aPlayer, 160);
	}

	public void onFirstTick()
	{
		getCraftingOutput();
	}

	@Override
	public boolean doesFillContainers()
	{
		return false;
	}

	@Override
	public boolean doesEmptyContainers()
	{
		return false;
	}

	@Override
	public boolean canTankBeFilled()
	{
		return true;
	}

	@Override
	public boolean canTankBeEmptied()
	{
		return true;
	}

	@Override
	public boolean displaysItemStack()
	{
		return false;
	}

	@Override
	public boolean displaysStackSize()
	{
		return false;
	}

	public void onPostTick()
	{
		if (getBaseMetaTileEntity().isServerSide())
		{
			if (getBaseMetaTileEntity().hasInventoryBeenModified()) {
				getCraftingOutput();
			}
			fillLiquidContainers();
			if (this.mFlushMode)
			{
				this.mFlushMode = false;
				for (byte i = 21; i < 30; i = (byte)(i + 1)) {
					if (this.mInventory[i] != null) {
						if (this.mInventory[i].stackSize == 0)
						{
							this.mInventory[i] = null;
						}
						else
						{
							this.mFlushMode = true;
							break;
						}
					}
				}
			}
		}
	}

	public void sortIntoTheInputSlots()
	{
		for (byte i = 21; i < 30; i = (byte)(i + 1)) {
			if (this.mInventory[i] != null)
			{
				if (this.mInventory[i].stackSize == 0) {
					this.mInventory[i] = null;
				}
				if (this.mInventory[i] != null) {
					for (byte j = 0; j < 16; j = (byte)(j + 1)) {
						if (GT_Utility.areStacksEqual(this.mInventory[i], this.mInventory[j])) {
							GT_Utility.moveStackFromSlotAToSlotB(getBaseMetaTileEntity(), getBaseMetaTileEntity(), i, j, (byte)64, (byte)1, (byte)64, (byte)1);
						}
					}
				}
				if (this.mInventory[i] != null) {
					for (byte j = 0; j < 16; j = (byte)(j + 1)) {
						if (this.mInventory[j] == null) {
							GT_Utility.moveStackFromSlotAToSlotB(getBaseMetaTileEntity(), getBaseMetaTileEntity(), i, j, (byte)64, (byte)1, (byte)64, (byte)1);
						}
					}
				}
			}
		}
	}

	private void fillLiquidContainers()
	{
		for (byte i = 16; (i < 21) && (this.mFluid != null); i = (byte)(i + 1))
		{
			ItemStack tOutput = GT_Utility.fillFluidContainer(this.mFluid, this.mInventory[i], true, true);
			if (tOutput != null)
			{
				if (this.mInventory[i].stackSize == 1)
				{
					this.mFluid.amount -= GT_Utility.getFluidForFilledItem(tOutput, true).amount * tOutput.stackSize;
					this.mInventory[i] = tOutput;
				}
				else
				{
					for (byte j = 16; j < 21; j = (byte)(j + 1)) {
						if ((this.mInventory[j] == null) || ((GT_Utility.areStacksEqual(tOutput, this.mInventory[j])) && (this.mInventory[j].stackSize + tOutput.stackSize <= tOutput.getMaxStackSize())))
						{
							this.mFluid.amount -= GT_Utility.getFluidForFilledItem(tOutput, true).amount * tOutput.stackSize;
							getBaseMetaTileEntity().decrStackSize(i, 1);
							if (this.mInventory[j] == null)
							{
								this.mInventory[j] = tOutput; break;
							}
							this.mInventory[j].stackSize += 1;

							break;
						}
					}
				}
				if ((this.mFluid != null) && (this.mFluid.amount <= 0)) {
					this.mFluid = null;
				}
			}
		}
		if ((this.mFluid != null) && (this.mFluid.amount <= 0)) {
			this.mFluid = null;
		}
	}

	public void setBluePrint(ItemStack aStack)
	{
		if (aStack == null) {
			aStack = this.mInventory[30];
		}
		if ((this.mInventory[31] == null) || (aStack == null) /*|| (aStack.itemID != -2)*/ || (aStack.getItemDamage() != 0) || (aStack.stackSize != 1) || (aStack.hasTagCompound())) {
			return;
		}
		NBTTagCompound tNBT = new NBTTagCompound();
		NBTTagList tNBT_ItemList = new NBTTagList();
		for (int i = 0; i < 9; i++)
		{
			ItemStack tStack = this.mInventory[(i + 21)];
			if (tStack != null)
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte)i);
				tStack.writeToNBT(tag);
				tNBT_ItemList.appendTag(tag);
			}
		}
		tNBT.setTag("Inventory", tNBT_ItemList);
		aStack.setTagCompound(tNBT);
	}

	public ItemStack getCraftingOutput()
	{
		if ((this.mInventory[30] != null) /*&& (this.mInventory[30].itemID == -2)*/ && (this.mInventory[30].getItemDamage() == 0) && (this.mInventory[30].hasTagCompound()))
		{
			NBTTagCompound tNBT = this.mInventory[30].getTagCompound();
			NBTTagList tNBT_ItemList = tNBT.getTagList("Blueprint", 10); //TODO
			for (int i = 0; (i < tNBT_ItemList.tagCount()) && (i < 9); i++)
			{
				NBTTagCompound tag = (NBTTagCompound)tNBT_ItemList.getCompoundTagAt(i);
				byte slot = tag.getByte("Slot");
				if ((slot >= 0) && (slot < 9) && (this.mInventory[(slot + 21)] == null))
				{
					this.mInventory[(slot + 21)] = GT_Utility.loadItem(tag);
					if (this.mInventory[(slot + 21)] != null) {
						this.mInventory[(slot + 21)].stackSize = 0;
					}
				}
			}
		}
		this.mInventory[31] = GT_ModHandler.getAllRecipeOutput(getBaseMetaTileEntity().getWorld(), new ItemStack[] { this.mInventory[21], this.mInventory[22], this.mInventory[23], this.mInventory[24], this.mInventory[25], this.mInventory[26], this.mInventory[27], this.mInventory[28], this.mInventory[29] });
		return this.mInventory[31];
	}

	public boolean canDoCraftingOutput()
	{
		if (this.mInventory[31] == null) {
			return false;
		}
		for (ItemStack tStack : recipeContent()) {
			if (tStack.stackSize > getAmountOf(tStack)) {
				return false;
			}
		}
		return true;
	}

	private int getAmountOf(ItemStack aStack)
	{
		int tAmount = 0;
		for (byte i = 0; (i < 30) && (tAmount < 9); i = (byte)(i + 1)) {
			if (GT_Utility.areStacksOrToolsEqual(aStack, this.mInventory[i])) {
				tAmount += this.mInventory[i].stackSize;
			}
		}
		return tAmount;
	}

	private ArrayList<ItemStack> recipeContent()
	{
		ArrayList<ItemStack> tList = new ArrayList();
		for (byte i = 21; i < 30; i = (byte)(i + 1)) {
			if (this.mInventory[i] != null)
			{
				boolean temp = false;
				for (byte j = 0; j < tList.size(); j = (byte)(j + 1)) {
					if (GT_Utility.areStacksOrToolsEqual(this.mInventory[i], (ItemStack)tList.get(j)))
					{
						((ItemStack)tList.get(j)).stackSize += 1;
						temp = true;
						break;
					}
				}
				if (!temp) {
					tList.add(GT_Utility.copyAmount(1L, new Object[] { this.mInventory[i] }));
				}
			}
		}
		return tList;
	}

	public ItemStack consumeMaterials(EntityPlayer aPlayer, ItemStack aHoldStack)
	{
		if (this.mInventory[31] == null) {
			return aHoldStack;
		}
		if (aHoldStack != null)
		{
			if (!GT_Utility.areStacksEqual(aHoldStack, this.mInventory[31])) {
				return aHoldStack;
			}
			if (aHoldStack.stackSize + this.mInventory[31].stackSize > aHoldStack.getMaxStackSize()) {
				return aHoldStack;
			}
		}
		for (byte i = 21; i < 30; i = (byte)(i + 1)) {
			if (this.mInventory[i] != null) {
				for (byte j = 0; j <= i; j = (byte)(j + 1)) {
					if (((j < 21) || (j == i)) && 
							(GT_Utility.areStacksOrToolsEqual(this.mInventory[i], this.mInventory[j])) && (this.mInventory[j].stackSize > 0))
					{
						ItemStack tStack = GT_Utility.getContainerItem(this.mInventory[j], true);
						if ((tStack == null) || ((tStack.isItemStackDamageable()) && (tStack.getItemDamage() >= tStack.getMaxDamage())))
						{
							getBaseMetaTileEntity().decrStackSize(j, 1); break;
						}
						if (this.mInventory[j].stackSize == 1)
						{
							this.mInventory[j] = tStack; break;
						}
						getBaseMetaTileEntity().decrStackSize(j, 1);
						for (byte k = 0; k < 21; k = (byte)(k + 1))
						{
							if (this.mInventory[k] == null)
							{
								this.mInventory[k] = tStack;
								break;
							}
							if ((GT_Utility.areStacksEqual(tStack, this.mInventory[k])) && 
									(tStack.stackSize + this.mInventory[k].stackSize <= this.mInventory[k].getMaxStackSize()))
							{
								this.mInventory[k].stackSize += tStack.stackSize;
								break;
							}
						}
						break;
					}
				}
			}
		}
		if (aHoldStack == null)
		{
			aHoldStack = GT_Utility.copy(new Object[] { this.mInventory[31] });
			aHoldStack.onCrafting(getBaseMetaTileEntity().getWorld(), aPlayer, this.mInventory[31].stackSize);
		}
		else
		{
			aHoldStack.stackSize += this.mInventory[31].stackSize;
			aHoldStack.onCrafting(getBaseMetaTileEntity().getWorld(), aPlayer, this.mInventory[31].stackSize);
		}
		fillLiquidContainers();

		return aHoldStack;
	}

	@Override
	public long getInputTier()
	{
		return GT_Utility.getTier(getBaseMetaTileEntity().getInputVoltage());
	}

	@Override
	public long getOutputTier()
	{
		return GT_Utility.getTier(getBaseMetaTileEntity().getInputVoltage());
	}

	@Override
	public int rechargerSlotStartIndex()
	{
		return 16;
	}

	@Override
	public int rechargerSlotCount()
	{
		return 5;
	}

	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone)
	{
		if (aSide == 0) {
			return 32;
		}
		if (aSide == 1) {
			return 290;
		}
		if ((aFacing == 0) || (aFacing == 1)) {
			return 222;
		}
		if ((aFacing == 2) || (aFacing == 3)) {
			return 223;
		}
		return 215;
	}

	@Override
	public String[] getDescription()
	{
		return new String[]{"For the very large Projects"};
	}

	public boolean allowPutStack(int aIndex, byte aSide, ItemStack aStack)
	{
		if (aIndex < 16)
		{
			for (byte i = 0; i < 16; i = (byte)(i + 1)) {
				if (GT_Utility.areStacksOrToolsEqual(aStack, this.mInventory[i])) {
					return aIndex == i;
				}
			}
			return true;
		}
		return false;
	}

	public boolean allowPullStack(int aIndex, byte aSide, ItemStack aStack)
	{
		return (aIndex == 33) || ((this.mFlushMode) && (aIndex >= 21) && (aIndex < 30));
	}

	@Override
	public int getCapacity()
	{
		return 64000;
	}

	@Override
	public int getTankPressure()
	{
		return -100;
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity)
	{
		return new CONTAINER_AdvancedWorkbench(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity)
	{
		return new GUI_AdvancedWorkbench(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		return new ITexture[0][0][0];
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		return aSide == 1 ? new ITexture[]{ new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CRAFTING)} : new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_STEEL_SIDE), new GT_RenderedTexture(Textures.BlockIcons.VOID)};
	}
}
