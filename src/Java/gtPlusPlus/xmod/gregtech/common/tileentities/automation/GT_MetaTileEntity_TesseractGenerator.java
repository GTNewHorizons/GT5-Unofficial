package gtPlusPlus.xmod.gregtech.common.tileentities.automation;

import static gtPlusPlus.core.lib.CORE.sTesseractGenerators;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IDigitalChest;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.player.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class GT_MetaTileEntity_TesseractGenerator
extends GT_MetaTileEntity_BasicTank
{
	public static int TESSERACT_ENERGY_COST_DIMENSIONAL = 2048;
	public static int TESSERACT_ENERGY_COST = 1024;
	public byte isWorking = 0;
	public int oFrequency = 0;
	public int mNeededEnergy = 0;
	public int mFrequency = 0;

	public GT_MetaTileEntity_TesseractGenerator(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 3, "");
	}

	public GT_MetaTileEntity_TesseractGenerator(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
	}  

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_TesseractGenerator(mName, mTier, mDescription, mTextures);
	}

	@Override
	public boolean isTransformerUpgradable()
	{
		return true;
	}

	@Override
	public boolean isOverclockerUpgradable()
	{
		return false;
	}

	@Override
	public boolean isSimpleMachine()
	{
		return false;
	}

	@Override
	public boolean isFacingValid(byte aFacing)
	{
		return true;
	}

	@Override
	public boolean isEnetInput()
	{
		return true;
	}

	@Override
	public boolean isEnetOutput()
	{
		return false;
	}

	@Override
	public boolean isInputFacing(byte aSide)
	{
		return true;
	}

	@Override
	public boolean isOutputFacing(byte aSide) {
		return aSide == getBaseMetaTileEntity().getBackFacing();
	}

	@Override
	public boolean isValidSlot(int aIndex)
	{
		return false;
	}

	@Override
	public long getMinimumStoredEU()
	{
		return getBaseMetaTileEntity().getEUCapacity() / 2;
	}

	@Override
	public long maxEUInput()
	{
		return 2048;
	}

	@Override
	public long maxEUOutput()
	{
		return 0;
	}

	@Override
	public long maxEUStore()
	{
		return 100000;
	}

	@Override
	public long maxSteamStore()
	{
		return maxEUStore();
	}

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer)
	{
		return true;
	}

	@Override
	public boolean ownerControl()
	{
		return true;
	}

	@Override
	public int getProgresstime()
	{
		return (sTesseractGenerators.get(Integer.valueOf(this.mFrequency)) == this) && (this.isWorking >= 20) ? 999 : 0;
	}

	@Override
	public int maxProgresstime()
	{
		return 1000;
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT)
	{
		aNBT.setInteger("mFrequency", this.mFrequency);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT)
	{
		this.mFrequency = aNBT.getInteger("mFrequency");
	}

	@Override
	public void onConfigLoad(GT_Config aConfig)
	{
		TESSERACT_ENERGY_COST = 1024;
		TESSERACT_ENERGY_COST_DIMENSIONAL = 2048;
	}

	@Override
	public void onServerStart()
	{
		sTesseractGenerators.clear();
	}

	public void onServerStop()
	{
		sTesseractGenerators.clear();
	}
	
	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ){
		if (aSide == getBaseMetaTileEntity().getFrontFacing()){
			float[] tCoords = GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ);
			switch ((byte)((byte)(int)(tCoords[0] * 2.0F) + 2 * (byte)(int)(tCoords[1] * 2.0F))){
			case 0: 
				Utils.LOG_INFO("Freq. -1 | " + this.mFrequency);
				this.mFrequency -= 1;
				break;
			case 1: 
				Utils.LOG_INFO("Freq. +1 | " + this.mFrequency);
				this.mFrequency += 1;
			default:
				//Utils.LOG_INFO("Did not click the correct place.");
				break;
			}
			PlayerUtils.messagePlayer(aPlayer, "Frequency: " + this.mFrequency);
			PlayerUtils.messagePlayer(aPlayer, ((sTesseractGenerators.get(Integer.valueOf(this.mFrequency)) != null) && (sTesseractGenerators.get(Integer.valueOf(this.mFrequency)) != this) ? EnumChatFormatting.RED + " (Occupied)" : ""));
			}
		return true;
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ)
	{
		if (aSide == getBaseMetaTileEntity().getFrontFacing())
		{
			float[] tCoords = GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ);
			switch ((byte)((byte)(int)(tCoords[0] * 2.0F) + 2 * (byte)(int)(tCoords[1] * 2.0F)))
			{
			case 0: 
				this.mFrequency -= 64;
				break;
			case 1: 
				this.mFrequency += 64;
				break;
			case 2: 
				this.mFrequency -= 512;
				break;
			case 3: 
				this.mFrequency += 512;
			}
			GT_Utility.sendChatToPlayer(aPlayer, "Frequency: " + this.mFrequency + ((sTesseractGenerators.get(Integer.valueOf(this.mFrequency)) != null) && (sTesseractGenerators.get(Integer.valueOf(this.mFrequency)) != this) ? EnumChatFormatting.RED + " (Occupied)" : ""));
		}
	}

	public boolean allowCoverOnSide(byte aSide, int aCoverID)
	{
		return aSide != getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public String[] getInfoData()
	{
		TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity != null) && (getBaseMetaTileEntity().isAllowedToWork()) && ((tTileEntity instanceof IGregTechDeviceInformation)) && (((IGregTechDeviceInformation)tTileEntity).isGivingInformation())) {
			return ((IGregTechDeviceInformation)tTileEntity).getInfoData();
		}
		return new String[] { "Tesseract Generator", "Freqency:", "" + this.mFrequency, (sTesseractGenerators.get(Integer.valueOf(this.mFrequency)) == this) && (this.isWorking >= 20) ? "Active" : "Inactive" };
	}

	@Override
	public boolean isGivingInformation()
	{
		return true;
	}

	public boolean isSendingInformation()
	{
		TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity != null) && (getBaseMetaTileEntity().isAllowedToWork()) && ((tTileEntity instanceof IGregTechDeviceInformation))) {
			return ((IGregTechDeviceInformation)tTileEntity).isGivingInformation();
		}
		return false;
	}

	@Override
	public boolean isDigitalChest()
	{
		TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity != null) && (getBaseMetaTileEntity().isAllowedToWork()) && ((tTileEntity instanceof IDigitalChest))) {
			return ((IDigitalChest)tTileEntity).isDigitalChest();
		}
		return false;
	}

	@Override
	public ItemStack[] getStoredItemData()
	{
		TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity != null) && (getBaseMetaTileEntity().isAllowedToWork()) && ((tTileEntity instanceof IDigitalChest))) {
			return ((IDigitalChest)tTileEntity).getStoredItemData();
		}
		return null;
	}

	@Override
	public void setItemCount(int aCount)
	{
		TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity != null) && (getBaseMetaTileEntity().isAllowedToWork()) && ((tTileEntity instanceof IDigitalChest))) {
			((IDigitalChest)tTileEntity).setItemCount(aCount);
		}
	}

	@Override
	public int getMaxItemCount()
	{
		TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity != null) && (getBaseMetaTileEntity().isAllowedToWork()) && ((tTileEntity instanceof IDigitalChest))) {
			return ((IDigitalChest)tTileEntity).getMaxItemCount();
		}
		return 0;
	}

	@Override
	public boolean isItemValidForSlot(int aIndex, ItemStack aStack)
	{
		IInventory tTileEntity = getBaseMetaTileEntity().getIInventoryAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.isItemValidForSlot(aIndex, aStack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int aSide)
	{
		IInventory tTileEntity = getBaseMetaTileEntity().getIInventoryAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return new int[0];
		}
		if ((tTileEntity instanceof ISidedInventory)) {
			return ((ISidedInventory)tTileEntity).getAccessibleSlotsFromSide(aSide);
		}
		int[] rArray = new int[getSizeInventory()];
		for (int i = 0; i < getSizeInventory(); i++) {
			rArray[i] = i;
		}
		return rArray;
	}

	@Override
	public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide)
	{
		IInventory tTileEntity = getBaseMetaTileEntity().getIInventoryAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		if ((tTileEntity instanceof ISidedInventory)) {
			return ((ISidedInventory)tTileEntity).canInsertItem(aIndex, aStack, aSide);
		}
		return true;
	}

	@Override
	public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide)
	{
		IInventory tTileEntity = getBaseMetaTileEntity().getIInventoryAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		if ((tTileEntity instanceof ISidedInventory)) {
			return ((ISidedInventory)tTileEntity).canExtractItem(aIndex, aStack, aSide);
		}
		return true;
	}

	@Override
	public int getSizeInventory()
	{
		IInventory tTileEntity = getBaseMetaTileEntity().getIInventoryAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int aIndex)
	{
		IInventory tTileEntity = getBaseMetaTileEntity().getIInventoryAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.getStackInSlot(aIndex);
	}

	@Override
	public void setInventorySlotContents(int aIndex, ItemStack aStack)
	{
		IInventory tTileEntity = getBaseMetaTileEntity().getIInventoryAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return;
		}
		tTileEntity.setInventorySlotContents(aIndex, aStack);
	}

	@Override
	public ItemStack decrStackSize(int aIndex, int aAmount)
	{
		IInventory tTileEntity = getBaseMetaTileEntity().getIInventoryAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.decrStackSize(aIndex, aAmount);
	}

	@Override
	public String getInventoryName()
	{
		IInventory tTileEntity = getBaseMetaTileEntity().getIInventoryAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return "";
		}
		return tTileEntity.getInventoryName();
	}

	@Override
	public int getInventoryStackLimit()
	{
		IInventory tTileEntity = getBaseMetaTileEntity().getIInventoryAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.getInventoryStackLimit();
	}

	@Override
	public boolean canFill(ForgeDirection aSide, Fluid aFluid)
	{
		IFluidHandler tTileEntity = getBaseMetaTileEntity().getITankContainerAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.canFill(aSide, aFluid);
	}

	@Override
	public boolean canDrain(ForgeDirection aSide, Fluid aFluid)
	{
		IFluidHandler tTileEntity = getBaseMetaTileEntity().getITankContainerAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.canDrain(aSide, aFluid);
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection aSide)
	{
		IFluidHandler tTileEntity = getBaseMetaTileEntity().getITankContainerAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return new FluidTankInfo[0];
		}
		return tTileEntity.getTankInfo(aSide);
	}

	@Override
	public int fill_default(ForgeDirection aDirection, FluidStack aFluid, boolean doFill)
	{
		IFluidHandler tTileEntity = getBaseMetaTileEntity().getITankContainerAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.fill(aDirection, aFluid, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection aDirection, int maxDrain, boolean doDrain)
	{
		IFluidHandler tTileEntity = getBaseMetaTileEntity().getITankContainerAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.drain(aDirection, maxDrain, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection aSide, FluidStack aFluid, boolean doDrain)
	{
		IFluidHandler tTileEntity = getBaseMetaTileEntity().getITankContainerAtSide(getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.drain(aSide, aFluid, doDrain);
	}

	public boolean addEnergyConsumption(GT_MetaTileEntity_TesseractTerminal aTerminal)
	{
		if (!getBaseMetaTileEntity().isAllowedToWork()) {
			return false;
		}
		this.mNeededEnergy += (aTerminal.getBaseMetaTileEntity().getWorld() == getBaseMetaTileEntity().getWorld() ? TESSERACT_ENERGY_COST : TESSERACT_ENERGY_COST_DIMENSIONAL);
		return true;
	}

	public boolean isValidTesseractGenerator(String aOwnerName, boolean aWorkIrrelevant)
	{
		return (getBaseMetaTileEntity() != null) && (!getBaseMetaTileEntity().isInvalidTileEntity()) && (getBaseMetaTileEntity().isAllowedToWork()) && ((aOwnerName == null) || (getBaseMetaTileEntity().getOwnerName().equals(aOwnerName))) && ((aWorkIrrelevant) || (this.isWorking >= 20));
	}

	public void onPostTick()
	{
		if (getBaseMetaTileEntity().isServerSide()){
			if (this.mFrequency != this.oFrequency){
				
				Utils.LOG_INFO("mFreq != oFreq");
				
				if (sTesseractGenerators.get(Integer.valueOf(this.oFrequency)) == this)
				{
					sTesseractGenerators.remove(Integer.valueOf(this.oFrequency));
					getBaseMetaTileEntity().issueBlockUpdate();
					Utils.LOG_INFO("this Gen == oFreq on map - do block update");
				}
				Utils.LOG_INFO("mFreq will be set to oFreq");
				this.oFrequency = this.mFrequency;
			}
			if ((getBaseMetaTileEntity().isAllowedToWork()) && (getBaseMetaTileEntity().decreaseStoredEnergyUnits(this.mNeededEnergy, false)))
			{
				Utils.LOG_INFO("Can Work & Has Energy");
				if ((sTesseractGenerators.get(Integer.valueOf(this.mFrequency)) == null) || (!((GT_MetaTileEntity_TesseractGenerator)sTesseractGenerators.get(Integer.valueOf(this.mFrequency))).isValidTesseractGenerator(null, true))) {
					Utils.LOG_INFO("storing TE I think to mFreq map?");
					sTesseractGenerators.put(Integer.valueOf(this.mFrequency), this);
				}
			}
			else
			{
				if (sTesseractGenerators.get(Integer.valueOf(this.mFrequency)) == this)
				{
					Utils.LOG_INFO("this gen == mFreq on map - do block update");
					sTesseractGenerators.remove(Integer.valueOf(this.mFrequency));
					getBaseMetaTileEntity().issueBlockUpdate();
				}
				this.isWorking = 0;
			}
			if (sTesseractGenerators.get(Integer.valueOf(this.mFrequency)) == this)
			{
				Utils.LOG_INFO("mFreq == this - do work related things");
				if (this.isWorking < 20) {
					this.isWorking = ((byte)(this.isWorking + 1));
				}
				if (this.isWorking == 20)
				{
					getBaseMetaTileEntity().issueBlockUpdate();
					this.isWorking = ((byte)(this.isWorking + 1));
				}
			}
			else
			{
				this.isWorking = 0;
			}
			this.mNeededEnergy = 0;
		}
	}

	@Override
	public String[] getDescription()
	{
		return new String[] {"Generates a Tesseract for the attached Inventory"};
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		return new ITexture[0][0][0];
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		return aSide == aFacing ? new ITexture[]{ new GT_RenderedTexture(TexturesGtBlocks.Casing_Machine_Dimensional), new GT_RenderedTexture(TexturesGtBlocks.Casing_Machine_Screen_Frequency)} : new ITexture[]{new GT_RenderedTexture(TexturesGtBlocks.Casing_Machine_Dimensional), new GT_RenderedTexture(Textures.BlockIcons.VOID)};
	}

	
	//To-Do?
	@Override
	public boolean doesFillContainers() {
		return false;
	}

	@Override
	public boolean doesEmptyContainers() {
		return false;
	}

	@Override
	public boolean canTankBeFilled() {
		return false;
	}

	@Override
	public boolean canTankBeEmptied() {
		return false;
	}

	@Override
	public boolean displaysItemStack() {
		return false;
	}

	@Override
	public boolean displaysStackSize() {
		return false;
	}
}
