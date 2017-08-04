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
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.player.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class GT_MetaTileEntity_TesseractTerminal
extends GT_MetaTileEntity_BasicTank
{
	public int mFrequency = 0;
	public boolean mDidWork = false;
	public static boolean sInterDimensionalTesseractAllowed = true;

	public GT_MetaTileEntity_TesseractTerminal(final int aID, final String aName, final String aNameRegional, final int aTier) {
		super(aID, aName, aNameRegional, aTier, 3, "");
	}

	public GT_MetaTileEntity_TesseractTerminal(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
	}

	@Override
	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_TesseractTerminal(this.mName, this.mTier, this.mDescription, this.mTextures);
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
	public boolean isFacingValid(final byte aFacing)
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
		return 2048;
	}

	@Override
	public boolean isAccessAllowed(final EntityPlayer aPlayer)
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
		return this.maxEUStore();
	}

	@Override
	public boolean ownerControl()
	{
		return true;
	}

	@Override
	public int getProgresstime()
	{
		return this.getTesseract(this.mFrequency, false) != null ? 999 : 0;
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
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT)
	{
		this.mFrequency = aNBT.getInteger("mFrequency");
	}

	@Override
	public void onConfigLoad(final GT_Config aConfig)
	{
		sInterDimensionalTesseractAllowed = true;
	}

	/*public boolean onRightclick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ)
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
	}*/

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
			PlayerUtils.messagePlayer(aPlayer, (this.getTesseract(this.mFrequency, false) == null ? "" : new StringBuilder().append(EnumChatFormatting.GREEN).append(" (Connected)").toString()));
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
			GT_Utility.sendChatToPlayer(aPlayer, "Frequency: " + this.mFrequency + (this.getTesseract(this.mFrequency, false) == null ? "" : new StringBuilder().append(EnumChatFormatting.GREEN).append(" (Connected)").toString()));
		}
	}

	public boolean allowCoverOnSide(final byte aSide, final int aCoverID)
	{
		return aSide != this.getBaseMetaTileEntity().getFrontFacing();
	}

	public GT_MetaTileEntity_TesseractGenerator getTesseract(final int aFrequency, final boolean aWorkIrrelevant)
	{
		final GT_MetaTileEntity_TesseractGenerator rTesseract = CORE.sTesseractGenerators.get(Integer.valueOf(aFrequency));
		if (rTesseract == null) {
			return null;
		}
		if (rTesseract.mFrequency != aFrequency)
		{
			CORE.sTesseractGenerators.put(Integer.valueOf(aFrequency), null);return null;
		}
		if (!rTesseract.isValidTesseractGenerator(this.getBaseMetaTileEntity().getOwnerName(), aWorkIrrelevant)) {
			return null;
		}
		if ((!sInterDimensionalTesseractAllowed) && (rTesseract.getBaseMetaTileEntity().getWorld() != this.getBaseMetaTileEntity().getWorld())) {
			return null;
		}
		return rTesseract;
	}

	@Override
	public String[] getInfoData()
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity != null) && (this.getBaseMetaTileEntity().isAllowedToWork()) && (tTileEntity.isSendingInformation())) {
			return tTileEntity.getInfoData();
		}
		return new String[] { "Tesseract Generator", "Freqency:", "" + this.mFrequency, this.getTesseract(this.mFrequency, false) != null ? "Active" : "Inactive" };
	}

	@Override
	public boolean isGivingInformation()
	{
		return true;
	}

	@Override
	public boolean isDigitalChest()
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.isDigitalChest();
	}

	@Override
	public ItemStack[] getStoredItemData()
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.getStoredItemData();
	}

	@Override
	public void setItemCount(final int aCount)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return;
		}
		tTileEntity.setItemCount(aCount);
	}

	@Override
	public int getMaxItemCount()
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.getMaxItemCount();
	}

	@Override
	public boolean isItemValidForSlot(final int aIndex, final ItemStack aStack)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.isItemValidForSlot(aIndex, aStack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int aSide)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return new int[0];
		}
		return tTileEntity.getAccessibleSlotsFromSide(aSide);
	}

	@Override
	public boolean canInsertItem(final int aIndex, final ItemStack aStack, final int aSide)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.canInsertItem(aIndex, aStack, aSide);
	}

	@Override
	public boolean canExtractItem(final int aIndex, final ItemStack aStack, final int aSide)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.canExtractItem(aIndex, aStack, aSide);
	}

	@Override
	public int getSizeInventory()
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(final int aIndex)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.getStackInSlot(aIndex);
	}

	@Override
	public void setInventorySlotContents(final int aIndex, final ItemStack aStack)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return;
		}
		tTileEntity.setInventorySlotContents(aIndex, aStack);
	}

	@Override
	public ItemStack decrStackSize(final int aIndex, final int aAmount)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.decrStackSize(aIndex, aAmount);
	}

	@Override
	public String getInventoryName()
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return "";
		}
		return tTileEntity.getInventoryName();
	}

	@Override
	public int getInventoryStackLimit()
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.getInventoryStackLimit();
	}

	@Override
	public boolean canFill(final ForgeDirection aSide, final Fluid aFluid)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.canFill(aSide, aFluid);
	}

	@Override
	public boolean canDrain(final ForgeDirection aSide, final Fluid aFluid)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return false;
		}
		return tTileEntity.canDrain(aSide, aFluid);
	}

	@Override
	public FluidTankInfo[] getTankInfo(final ForgeDirection aSide)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return new FluidTankInfo[0];
		}
		return tTileEntity.getTankInfo(aSide);
	}

	@Override
	public int fill_default(final ForgeDirection aDirection, final FluidStack aFluid, final boolean doFill)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return 0;
		}
		return tTileEntity.fill(aDirection, aFluid, doFill);
	}

	@Override
	public FluidStack drain(final ForgeDirection aDirection, final int maxDrain, final boolean doDrain)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.drain(aDirection, maxDrain, doDrain);
	}

	@Override
	public FluidStack drain(final ForgeDirection aSide, final FluidStack aFluid, final boolean doDrain)
	{
		final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
		if ((tTileEntity == null) || (!this.getBaseMetaTileEntity().isAllowedToWork())) {
			return null;
		}
		return tTileEntity.drain(aSide, aFluid, doDrain);
	}

	public void onPostTick()
	{
		if ((this.getBaseMetaTileEntity().isServerSide()) && (this.getBaseMetaTileEntity().isAllowedToWork()))
		{
			final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, true);
			if (tTileEntity != null)
			{
				tTileEntity.addEnergyConsumption(this);
				if ((!this.mDidWork) && (this.getTesseract(this.mFrequency, false) != null))
				{
					this.mDidWork = true;
					this.getBaseMetaTileEntity().issueBlockUpdate();
				}
			}
			else if (this.mDidWork == true)
			{
				this.mDidWork = false;
				this.getBaseMetaTileEntity().issueBlockUpdate();
			}
		}
	}
	
	@Override
	public String[] getDescription() {
		return new String[]{this.mDescription, "Accesses Tesseracts remotely", CORE.GT_Tooltip};
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
		return aSide == aFacing ? new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Dimensional), new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Screen_Frequency)} : new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Dimensional), new GT_RenderedTexture(Textures.BlockIcons.VOID)};
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
