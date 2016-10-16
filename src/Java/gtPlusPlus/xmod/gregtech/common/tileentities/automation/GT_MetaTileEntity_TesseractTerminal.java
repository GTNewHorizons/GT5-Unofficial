package gtPlusPlus.xmod.gregtech.common.tileentities.automation;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class GT_MetaTileEntity_TesseractTerminal
extends GT_MetaTileEntity_BasicTank
{
	public int mFrequency = 0;
	public boolean mDidWork = false;
	public static boolean sInterDimensionalTesseractAllowed = true;

	public GT_MetaTileEntity_TesseractTerminal(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 3, "");
	}

	public GT_MetaTileEntity_TesseractTerminal(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
	}  

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_TesseractTerminal(mName, mTier, mDescription, mTextures);
	}

	@Override
	public boolean isTransformerUpgradable()
	{
		return false;
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
	public boolean isAccessAllowed(EntityPlayer aPlayer)
	{
		return true;
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
	public boolean ownerControl()
	{
		return true;
	}

	@Override
	public int getProgresstime()
	{
		return getTesseract(this.mFrequency, false) != null ? 999 : 0;
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
		sInterDimensionalTesseractAllowed = true;
	}

	public boolean onRightclick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ)
	{
		if (aSide == getBaseMetaTileEntity().getFrontFacing())
		{
			float[] tCoords = GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ);
			switch ((byte)((byte)(int)(tCoords[0] * 2.0F) + 2 * (byte)(int)(tCoords[1] * 2.0F)))
			{
			case 0: 
				this.mFrequency -= 1;
				break;
			case 1: 
				this.mFrequency += 1;
			}
			GT_Utility.sendChatToPlayer(aPlayer, "Frequency: " + this.mFrequency + (getTesseract(this.mFrequency, false) == null ? "" : new StringBuilder().append(EnumChatFormatting.GREEN).append(" (Connected)").toString()));
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
			GT_Utility.sendChatToPlayer(aPlayer, "Frequency: " + this.mFrequency + (getTesseract(this.mFrequency, false) == null ? "" : new StringBuilder().append(EnumChatFormatting.GREEN).append(" (Connected)").toString()));
		}
	}

	public boolean allowCoverOnSide(byte aSide, int aCoverID)
	{
		return aSide != getBaseMetaTileEntity().getFrontFacing();
	}

	public GT_MetaTileEntity_TesseractGenerator getTesseract(int aFrequency, boolean aWorkIrrelevant)
	{
		GT_MetaTileEntity_TesseractGenerator rTesseract = (GT_MetaTileEntity_TesseractGenerator)CORE.sTesseractGenerators.get(Integer.valueOf(aFrequency));
		if (rTesseract == null) {
			return null;
		}
		if (rTesseract.mFrequency != aFrequency)
		{
			CORE.sTesseractGenerators.put(Integer.valueOf(aFrequency), null);return null;
		}
		if (!rTesseract.isValidTesseractGenerator(getBaseMetaTileEntity().getOwnerName(), aWorkIrrelevant)) {
			return null;
		}
		if ((!sInterDimensionalTesseractAllowed) && (rTesseract.getBaseMetaTileEntity().getWorld() != getBaseMetaTileEntity().getWorld())) {
			return null;
		}
		return rTesseract;
	}

	@Override
	public String[] getInfoData()
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity != null) && (getBaseMetaTileEntity().isAllowedToWork()) && (tTileEntity.isSendingInformation())) {
			return tTileEntity.getInfoData();
		}
		return new String[] { "Tesseract Generator", "Freqency:", "" + this.mFrequency, getTesseract(this.mFrequency, false) != null ? "Active" : "Inactive" };
	}

	@Override
	public boolean isGivingInformation()
	{
		return true;
	}

	@Override
	public boolean isDigitalChest()
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.isDigitalChest();
	}

	@Override
	public ItemStack[] getStoredItemData()
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.getStoredItemData();
	}

	@Override
	public void setItemCount(int aCount)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return;
		}
		tTileEntity.setItemCount(aCount);
	}

	@Override
	public int getMaxItemCount()
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.getMaxItemCount();
	}

	@Override
	public boolean isItemValidForSlot(int aIndex, ItemStack aStack)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.isItemValidForSlot(aIndex, aStack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int aSide)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return new int[0];
		}
		return tTileEntity.getAccessibleSlotsFromSide(aSide);
	}

	@Override
	public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.canInsertItem(aIndex, aStack, aSide);
	}

	@Override
	public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.canExtractItem(aIndex, aStack, aSide);
	}

	@Override
	public int getSizeInventory()
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int aIndex)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.getStackInSlot(aIndex);
	}

	@Override
	public void setInventorySlotContents(int aIndex, ItemStack aStack)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return;
		}
		tTileEntity.setInventorySlotContents(aIndex, aStack);
	}

	@Override
	public ItemStack decrStackSize(int aIndex, int aAmount)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.decrStackSize(aIndex, aAmount);
	}

	@Override
	public String getInventoryName()
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return "";
		}
		return tTileEntity.getInventoryName();
	}

	@Override
	public int getInventoryStackLimit()
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.getInventoryStackLimit();
	}

	@Override
	public boolean canFill(ForgeDirection aSide, Fluid aFluid)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.canFill(aSide, aFluid);
	}

	@Override
	public boolean canDrain(ForgeDirection aSide, Fluid aFluid)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.canDrain(aSide, aFluid);
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection aSide)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return new FluidTankInfo[0];
		}
		return tTileEntity.getTankInfo(aSide);
	}

	@Override
	public int fill_default(ForgeDirection aDirection, FluidStack aFluid, boolean doFill)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.fill(aDirection, aFluid, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection aDirection, int maxDrain, boolean doDrain)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.drain(aDirection, maxDrain, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection aSide, FluidStack aFluid, boolean doDrain)
	{
		GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.drain(aSide, aFluid, doDrain);
	}

	public void onPostTick()
	{
		if ((getBaseMetaTileEntity().isServerSide()) && (getBaseMetaTileEntity().isAllowedToWork()))
		{
			GT_MetaTileEntity_TesseractGenerator tTileEntity = getTesseract(this.mFrequency, true);
			if (tTileEntity != null)
			{
				tTileEntity.addEnergyConsumption(this);
				if ((!this.mDidWork) && (getTesseract(this.mFrequency, false) != null))
				{
					this.mDidWork = true;
					getBaseMetaTileEntity().issueBlockUpdate();
				}
			}
			else if (this.mDidWork == true)
			{
				this.mDidWork = false;
				getBaseMetaTileEntity().issueBlockUpdate();
			}
		}
	}

	@Override
	public String[] getDescription()
	{
		return new String[] {"Accesses Tesseracts remotely"};
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
		return aSide == aFacing ? new ITexture[]{new GT_RenderedTexture(TexturesGtBlocks.Casing_Machine_Dimensional), new GT_RenderedTexture(TexturesGtBlocks.Casing_Machine_Screen_Frequency)} : new ITexture[]{new GT_RenderedTexture(TexturesGtBlocks.Casing_Machine_Dimensional), new GT_RenderedTexture(Textures.BlockIcons.VOID)};
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
