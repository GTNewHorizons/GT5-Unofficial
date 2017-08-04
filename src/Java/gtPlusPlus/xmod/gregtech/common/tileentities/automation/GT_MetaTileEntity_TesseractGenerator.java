package gtPlusPlus.xmod.gregtech.common.tileentities.automation;

import static gtPlusPlus.core.lib.CORE.sTesseractGeneratorOwnershipMap;
import static gtPlusPlus.core.lib.CORE.sTesseractTerminalOwnershipMap;

import java.util.UUID;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.*;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.player.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.tesseract.TesseractHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class GT_MetaTileEntity_TesseractGenerator
extends GT_MetaTileEntity_BasicTank
{
	public static int TESSERACT_ENERGY_COST_DIMENSIONAL = 2048;
	public static int TESSERACT_ENERGY_COST = 1024;
	public byte isWorking = 0;
	public int oFrequency = 0;
	public int mNeededEnergy = 0;
	public int mFrequency = 0;
	public UUID mOwner;

	public GT_MetaTileEntity_TesseractGenerator(final int aID, final String aName, final String aNameRegional, final int aTier) {
		super(aID, aName, aNameRegional, aTier, 3, "");
	}

	public GT_MetaTileEntity_TesseractGenerator(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
	}

	@Override
	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_TesseractGenerator(this.mName, this.mTier, this.mDescription, this.mTextures);
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
	public boolean isFacingValid(final byte aFacing)
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
	public boolean isInputFacing(final byte aSide)
	{
		return true;
	}

	@Override
	public boolean isOutputFacing(final byte aSide) {
		return aSide == this.getBaseMetaTileEntity().getBackFacing();
	}

	@Override
	public boolean isValidSlot(final int aIndex)
	{
		return false;
	}

	@Override
	public long getMinimumStoredEU()
	{
		return this.getBaseMetaTileEntity().getEUCapacity() / 2;
	}

	@Override
	public long maxEUInput()
	{
		return 512;
	}

	@Override
	public long maxEUOutput()
	{
		return 0;
	}

	@Override
	public long maxEUStore()
	{
		return 512*32;
	}

	@Override
	public long maxSteamStore()
	{
		return this.maxEUStore();
	}

	@Override
	public boolean isAccessAllowed(final EntityPlayer aPlayer)
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
		return (TesseractHelper.getGeneratorByFrequency(PlayerUtils.getPlayerOnServerFromUUID(mOwner), this.mFrequency) == this) && (this.isWorking >= 20) ? 999 : 0;
	}

	@Override
	public int maxProgresstime()
	{
		return 1000;
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT)
	{
		aNBT.setInteger("mFrequency", this.mFrequency);
		aNBT.setString("mOwner", mOwner.toString());
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT)
	{
		this.mFrequency = aNBT.getInteger("mFrequency");
		this.mOwner = UUID.fromString(aNBT.getString("mOnwer"));
	}

	@Override
	public void onConfigLoad(final GT_Config aConfig)
	{
		TESSERACT_ENERGY_COST = 1024;
		TESSERACT_ENERGY_COST_DIMENSIONAL = 2048;
	}

	@Override
	public void onServerStart()
	{
		sTesseractGeneratorOwnershipMap.clear();
		sTesseractTerminalOwnershipMap.clear();
	}

	public void onServerStop()
	{
		sTesseractGeneratorOwnershipMap.clear();
		sTesseractTerminalOwnershipMap.clear();
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer, final byte aSide, final float aX, final float aY, final float aZ){
		if (aSide == this.getBaseMetaTileEntity().getFrontFacing()){
			final float[] tCoords = GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ);
			switch ((byte)((byte)(int)(tCoords[0] * 2.0F) + (2 * (byte)(int)(tCoords[1] * 2.0F)))){
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
			PlayerUtils.messagePlayer(aPlayer, ((getGeneratorEntity(this.mFrequency) != null) && (getGeneratorEntity(this.mFrequency) != this)) ? EnumChatFormatting.RED + " (Occupied)" : "");
		}
		return true;
	}

	@Override
	public void onScrewdriverRightClick(final byte aSide, final EntityPlayer aPlayer, final float aX, final float aY, final float aZ)
	{
		if (aSide == this.getBaseMetaTileEntity().getFrontFacing())
		{
			final float[] tCoords = GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ);
			switch ((byte)((byte)(int)(tCoords[0] * 2.0F) + (2 * (byte)(int)(tCoords[1] * 2.0F))))
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
			GT_Utility.sendChatToPlayer(aPlayer, "Frequency: " + this.mFrequency + ((getGeneratorEntity(this.mFrequency) != null && getGeneratorEntity(this.mFrequency) != this) ? EnumChatFormatting.RED + " (Occupied)" : ""));
		}
	}

	public boolean allowCoverOnSide(final byte aSide, final int aCoverID)
	{
		return aSide != this.getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public String[] getInfoData()
	{
		final TileEntity tTileEntity = this.getBaseMetaTileEntity().getTileEntityAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity != null) && (this.getBaseMetaTileEntity().isAllowedToWork()) && ((tTileEntity instanceof IGregTechDeviceInformation)) && (((IGregTechDeviceInformation)tTileEntity).isGivingInformation())) {
			return ((IGregTechDeviceInformation)tTileEntity).getInfoData();
		}
		return new String[] { "Tesseract Generator", "Freqency:", "" + this.mFrequency, (getGeneratorEntity() == this) && (this.isWorking >= 20) ? "Active" : "Inactive" };
	}

	@Override
	public boolean isGivingInformation()
	{
		return true;
	}

	public boolean isSendingInformation()
	{
		final TileEntity tTileEntity = this.getBaseMetaTileEntity().getTileEntityAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity != null) && (this.getBaseMetaTileEntity().isAllowedToWork()) && ((tTileEntity instanceof IGregTechDeviceInformation))) {
			return ((IGregTechDeviceInformation)tTileEntity).isGivingInformation();
		}
		return false;
	}

	@Override
	public boolean isDigitalChest()
	{
		final TileEntity tTileEntity = this.getBaseMetaTileEntity().getTileEntityAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity != null) && (this.getBaseMetaTileEntity().isAllowedToWork()) && ((tTileEntity instanceof IDigitalChest))) {
			return ((IDigitalChest)tTileEntity).isDigitalChest();
		}
		return false;
	}

	@Override
	public ItemStack[] getStoredItemData()
	{
		final TileEntity tTileEntity = this.getBaseMetaTileEntity().getTileEntityAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity != null) && (this.getBaseMetaTileEntity().isAllowedToWork()) && ((tTileEntity instanceof IDigitalChest))) {
			return ((IDigitalChest)tTileEntity).getStoredItemData();
		}
		return null;
	}

	@Override
	public void setItemCount(final int aCount)
	{
		final TileEntity tTileEntity = this.getBaseMetaTileEntity().getTileEntityAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity != null) && (this.getBaseMetaTileEntity().isAllowedToWork()) && ((tTileEntity instanceof IDigitalChest))) {
			((IDigitalChest)tTileEntity).setItemCount(aCount);
		}
	}

	@Override
	public int getMaxItemCount()
	{
		final TileEntity tTileEntity = this.getBaseMetaTileEntity().getTileEntityAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity != null) && (this.getBaseMetaTileEntity().isAllowedToWork()) && ((tTileEntity instanceof IDigitalChest))) {
			return ((IDigitalChest)tTileEntity).getMaxItemCount();
		}
		return 0;
	}

	@Override
	public boolean isItemValidForSlot(final int aIndex, final ItemStack aStack)
	{
		final IInventory tTileEntity = this.getBaseMetaTileEntity().getIInventoryAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.isItemValidForSlot(aIndex, aStack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int aSide)
	{
		final IInventory tTileEntity = this.getBaseMetaTileEntity().getIInventoryAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return new int[0];
		}
		if ((tTileEntity instanceof ISidedInventory)) {
			return ((ISidedInventory)tTileEntity).getAccessibleSlotsFromSide(aSide);
		}
		final int[] rArray = new int[this.getSizeInventory()];
		for (int i = 0; i < this.getSizeInventory(); i++) {
			rArray[i] = i;
		}
		return rArray;
	}

	@Override
	public boolean canInsertItem(final int aIndex, final ItemStack aStack, final int aSide)
	{
		final IInventory tTileEntity = this.getBaseMetaTileEntity().getIInventoryAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		if ((tTileEntity instanceof ISidedInventory)) {
			return ((ISidedInventory)tTileEntity).canInsertItem(aIndex, aStack, aSide);
		}
		return true;
	}

	@Override
	public boolean canExtractItem(final int aIndex, final ItemStack aStack, final int aSide)
	{
		final IInventory tTileEntity = this.getBaseMetaTileEntity().getIInventoryAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
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
		final IInventory tTileEntity = this.getBaseMetaTileEntity().getIInventoryAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(final int aIndex)
	{
		final IInventory tTileEntity = this.getBaseMetaTileEntity().getIInventoryAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.getStackInSlot(aIndex);
	}

	@Override
	public void setInventorySlotContents(final int aIndex, final ItemStack aStack)
	{
		final IInventory tTileEntity = this.getBaseMetaTileEntity().getIInventoryAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return;
		}
		tTileEntity.setInventorySlotContents(aIndex, aStack);
	}

	@Override
	public ItemStack decrStackSize(final int aIndex, final int aAmount)
	{
		final IInventory tTileEntity = this.getBaseMetaTileEntity().getIInventoryAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.decrStackSize(aIndex, aAmount);
	}

	@Override
	public String getInventoryName()
	{
		final IInventory tTileEntity = this.getBaseMetaTileEntity().getIInventoryAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return "";
		}
		return tTileEntity.getInventoryName();
	}

	@Override
	public int getInventoryStackLimit()
	{
		final IInventory tTileEntity = this.getBaseMetaTileEntity().getIInventoryAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.getInventoryStackLimit();
	}

	@Override
	public boolean canFill(final ForgeDirection aSide, final Fluid aFluid)
	{
		final IFluidHandler tTileEntity = this.getBaseMetaTileEntity().getITankContainerAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.canFill(aSide, aFluid);
	}

	@Override
	public boolean canDrain(final ForgeDirection aSide, final Fluid aFluid)
	{
		final IFluidHandler tTileEntity = this.getBaseMetaTileEntity().getITankContainerAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.canDrain(aSide, aFluid);
	}

	@Override
	public FluidTankInfo[] getTankInfo(final ForgeDirection aSide)
	{
		final IFluidHandler tTileEntity = this.getBaseMetaTileEntity().getITankContainerAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return new FluidTankInfo[0];
		}
		return tTileEntity.getTankInfo(aSide);
	}

	@Override
	public int fill_default(final ForgeDirection aDirection, final FluidStack aFluid, final boolean doFill)
	{
		final IFluidHandler tTileEntity = this.getBaseMetaTileEntity().getITankContainerAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.fill(aDirection, aFluid, doFill);
	}

	@Override
	public FluidStack drain(final ForgeDirection aDirection, final int maxDrain, final boolean doDrain)
	{
		final IFluidHandler tTileEntity = this.getBaseMetaTileEntity().getITankContainerAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.drain(aDirection, maxDrain, doDrain);
	}

	@Override
	public FluidStack drain(final ForgeDirection aSide, final FluidStack aFluid, final boolean doDrain)
	{
		final IFluidHandler tTileEntity = this.getBaseMetaTileEntity().getITankContainerAtSide(this.getBaseMetaTileEntity().getBackFacing());
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.drain(aSide, aFluid, doDrain);
	}

	public boolean addEnergyConsumption(final GT_MetaTileEntity_TesseractTerminal aTerminal)
	{
		if (!this.getBaseMetaTileEntity().isAllowedToWork()) {
			return false;
		}
		this.mNeededEnergy += (aTerminal.getBaseMetaTileEntity().getWorld() == this.getBaseMetaTileEntity().getWorld() ? TESSERACT_ENERGY_COST : TESSERACT_ENERGY_COST_DIMENSIONAL);
		return true;
	}

	public boolean isValidTesseractGenerator(final String aOwnerName, final boolean aWorkIrrelevant)
	{
		return (this.getBaseMetaTileEntity() != null) && (!this.getBaseMetaTileEntity().isInvalidTileEntity()) && (this.getBaseMetaTileEntity().isAllowedToWork()) && ((aOwnerName == null) || (this.getBaseMetaTileEntity().getOwnerName().equals(aOwnerName))) && ((aWorkIrrelevant) || (this.isWorking >= 20));
	}

	public void onPostTick()
	{
		if (this.getBaseMetaTileEntity().isServerSide()){
			if (this.mFrequency != this.oFrequency){

				Utils.LOG_INFO("mFreq != oFreq");

				if (getGeneratorEntity() == this)
				{
					getGeneratorEntity(this.oFrequency);
					this.getBaseMetaTileEntity().issueBlockUpdate();
					Utils.LOG_INFO("this Gen == oFreq on map - do block update");
				}
				Utils.LOG_INFO("mFreq will be set to oFreq");
				this.oFrequency = this.mFrequency;
			}
			if ((this.getBaseMetaTileEntity().isAllowedToWork()) && (this.getBaseMetaTileEntity().decreaseStoredEnergyUnits(this.mNeededEnergy, false)))
			{
				Utils.LOG_INFO("Can Work & Has Energy");
				if ((getGeneratorEntity(Integer.valueOf(this.mFrequency)) == null) || (!getGeneratorEntity(Integer.valueOf(this.mFrequency)).isValidTesseractGenerator(null, true))) {
					Utils.LOG_INFO("storing TE I think to mFreq map?");
					TesseractHelper.setGeneratorOwnershipByPlayer(PlayerUtils.getPlayerOnServerFromUUID(mOwner), this.mFrequency, this);
				}
			}
			else
			{
				if (getGeneratorEntity(Integer.valueOf(this.mFrequency)) == this)
				{
					Utils.LOG_INFO("this gen == mFreq on map - do block update");
					TesseractHelper.removeGenerator(PlayerUtils.getPlayerOnServerFromUUID(mOwner), this.mFrequency);
					this.getBaseMetaTileEntity().issueBlockUpdate();
				}
				this.isWorking = 0;
			}
			if (getGeneratorEntity(Integer.valueOf(this.mFrequency)) == this)
			{
				Utils.LOG_INFO("mFreq == this - do work related things");
				if (this.isWorking < 20) {
					this.isWorking = ((byte)(this.isWorking + 1));
				}
				if (this.isWorking == 20)
				{
					this.getBaseMetaTileEntity().issueBlockUpdate();
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
	public String[] getDescription() {
		return new String[]{this.mDescription, "Generates a Tesseract for the attached Inventory", CORE.GT_Tooltip};
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		return new ITexture[0][0][0];
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return aSide == aFacing ? new ITexture[]{ new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Dimensional), new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Screen_Frequency)} : new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Dimensional), new GT_RenderedTexture(Textures.BlockIcons.VOID)};
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
	
	private GT_MetaTileEntity_TesseractGenerator getGeneratorEntity(){
		GT_MetaTileEntity_TesseractGenerator thisGenerator = TesseractHelper.getGeneratorByFrequency(PlayerUtils.getPlayerOnServerFromUUID(mOwner), this.mFrequency);
		if (thisGenerator != null){
			return thisGenerator;
		}
		return null;
	}
	
	private GT_MetaTileEntity_TesseractGenerator getGeneratorEntity(int frequency){
		GT_MetaTileEntity_TesseractGenerator thisGenerator = TesseractHelper.getGeneratorByFrequency(PlayerUtils.getPlayerOnServerFromUUID(mOwner), frequency);
		if (thisGenerator != null){
			return thisGenerator;
		}
		return null;
	}
	
	@Override
	public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		mOwner = aPlayer.getUniqueID();
		super.onCreated(aStack, aWorld, aPlayer);
	}

	@Override
	public void onRemoval() {
		try {
		CORE.sTesseractGeneratorOwnershipMap.get(mOwner).remove(this.mFrequency);
		} catch (Throwable t){}
		super.onRemoval();
	}
}
