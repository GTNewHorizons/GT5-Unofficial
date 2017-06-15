package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import gregtech.GT_Mod;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.player.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTileEntity;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GregtechMetaPollutionDetector extends GregtechMetaTileEntity {

	int mCurrentPollution;
	int mAveragePollution;
	int mAveragePollutionArray[] = new int[10];
	private int mArrayPos = 0;
	private int mTickTimer = 0;
	private int mSecondTimer = 0;

	public GregtechMetaPollutionDetector(final int aID, final String aName, final String aNameRegional, final int aTier, final String aDescription, final int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription);
	}

	public GregtechMetaPollutionDetector(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures, final int aSlotCount) {
		super(aName, aTier, aSlotCount, aDescription, aTextures);
	}

	@Override
	public String[] getDescription() {
		return new String[] {this.mDescription, CORE.GT_Tooltip};
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = this.getFront(i);
			rTextures[1][i + 1] = this.getBack(i);
			rTextures[2][i + 1] = this.getBottom(i);
			rTextures[3][i + 1] = this.getTop(i);
			rTextures[4][i + 1] = this.getSides(i);
			rTextures[5][i + 1] = this.getFrontActive(i);
			rTextures[6][i + 1] = this.getBackActive(i);
			rTextures[7][i + 1] = this.getBottomActive(i);
			rTextures[8][i + 1] = this.getTopActive(i);
			rTextures[9][i + 1] = this.getSidesActive(i);
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return this.mTextures[(aActive ? 5 : 0) + (aSide == aFacing ? 0 : aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex + 1];
	}


	public ITexture[] getFront(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Screen_2)};
	}


	public ITexture[] getBack(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getFrontActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Screen_2)};
	}


	public ITexture[] getBackActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getBottomActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getTopActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getSidesActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaPollutionDetector(this.mName, this.mTier, this.mDescription, this.mTextures, this.mInventory.length);
	}

	@Override public boolean isSimpleMachine()						{return false;}
	@Override public boolean isElectric()							{return true;}
	@Override public boolean isValidSlot(final int aIndex)				{return true;}
	@Override public boolean isFacingValid(final byte aFacing)			{return true;}
	@Override public boolean isEnetInput() 							{return true;}
	@Override public boolean isEnetOutput() 						{return false;}
	@Override public boolean isInputFacing(final byte aSide)				{return aSide!=this.getBaseMetaTileEntity().getFrontFacing();}
	@Override public boolean isOutputFacing(final byte aSide)				{return aSide==this.getBaseMetaTileEntity().getFrontFacing();}
	@Override public boolean isTeleporterCompatible()				{return false;}
	@Override public long getMinimumStoredEU()						{return 0;}
	@Override public long maxEUStore()								{return 0;}

	@Override
	public int getCapacity() {
		return 0;
	}

	@Override
	public long maxEUInput() {
		return 0;
	}

	@Override
	public long maxEUOutput() {
		return 0;
	}

	@Override
	public long maxAmperesIn() {
		return 0;
	}

	@Override
	public long maxAmperesOut() {
		return 0;
	}
	@Override public int rechargerSlotStartIndex()					{return 0;}
	@Override public int dechargerSlotStartIndex()					{return 0;}
	@Override public int rechargerSlotCount()						{return 0;}
	@Override public int dechargerSlotCount()						{return 0;}
	@Override public int getProgresstime()							{return (int)this.getBaseMetaTileEntity().getUniversalEnergyStored();}
	@Override public int maxProgresstime()							{return (int)this.getBaseMetaTileEntity().getUniversalEnergyCapacity();}
	@Override public boolean isAccessAllowed(final EntityPlayer aPlayer)	{return true;}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide())
		{
			return true;
		}
		this.showPollution(aPlayer.getEntityWorld(), aPlayer);
		return true;
	}

	private void showPollution(final World worldIn, final EntityPlayer playerIn){
		if(!GT_Mod.gregtechproxy.mPollution){
			PlayerUtils.messagePlayer(playerIn, "This block is useless, Pollution is disabled.");		
		}
		else {
			PlayerUtils.messagePlayer(playerIn, "This chunk contains "+getCurrentChunkPollution()+" pollution.");
			PlayerUtils.messagePlayer(playerIn, "Average over last ten minutes: "+getAveragePollutionOverLastTen()+" pollution.");
		}
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	public int getCurrentChunkPollution(){
		return getCurrentChunkPollution(this.getBaseMetaTileEntity());
	}

	public int getCurrentChunkPollution(IGregTechTileEntity aBaseMetaTileEntity){
		return GT_Pollution.getPollution(aBaseMetaTileEntity);
	}


	@Override
	public String[] getInfoData() {
		return new String[] {
				this.getLocalName(),
				"Current Pollution: "+this.mCurrentPollution,
				"Average/10 minutes:"+getAveragePollutionOverLastTen()};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
		return null;
	}

	@Override
	public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_, final int p_102007_3_) {
		return false;
	}

	@Override
	public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(final int p_70301_1_) {
		return null;
	}

	@Override
	public ItemStack decrStackSize(final int p_70298_1_, final int p_70298_2_) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int p_70304_1_) {
		return null;
	}

	@Override
	public void setInventorySlotContents(final int p_70299_1_, final ItemStack p_70299_2_) {
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer p_70300_1_) {
		return false;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(final int p_94041_1_, final ItemStack p_94041_2_) {
		return false;
	}

	@Override
	public boolean isOverclockerUpgradable() {
		return false;
	}

	@Override
	public boolean isTransformerUpgradable() {
		return false;
	}

	//int mCurrentPollution;
	//int mAveragePollution;
	//int mAveragePollutionArray[] = new int[10];

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		aNBT.setInteger("mCurrentPollution", this.mCurrentPollution);
		aNBT.setInteger("mAveragePollution", this.mAveragePollution);
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		this.mCurrentPollution = aNBT.getInteger("mCurrentPollution");
		this.mAveragePollution = aNBT.getInteger("mAveragePollution");
	}

	@Override
	public void onFirstTick(final IGregTechTileEntity aBaseMetaTileEntity) {
		if (this.getBaseMetaTileEntity().isServerSide()) {
			if (this.mCurrentPollution == 0) {
				this.mCurrentPollution = getCurrentChunkPollution();
			}
			if (this.mArrayPos < 0 || this.mArrayPos > 9) {
				this.mArrayPos = 0;
			}
			this.mTickTimer = 0;
		}
	}



	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		if (this.getBaseMetaTileEntity().isServerSide()) {
			//TickTimer - 20 times a second
			if (this.mTickTimer >= 0 || this.mTickTimer <= 19){
				this.mTickTimer++;
			}
			else {
				this.mTickTimer = 0;
				//Perform pollution update once a second
				this.mCurrentPollution = getCurrentChunkPollution();
				this.mSecondTimer++;
			}
			//Update Pollution array once a minute
			if (this.mSecondTimer >= 60){
				Utils.sendServerMessage("Udating Average of pollution array. Using Array slot"+this.mArrayPos);
				this.mSecondTimer = 0;
				if (this.mArrayPos<this.mAveragePollutionArray.length){
					this.mAveragePollutionArray[this.mArrayPos] = this.mCurrentPollution;
					this.mArrayPos++;
				}
				else if (this.mArrayPos==this.mAveragePollutionArray.length){
					this.mAveragePollutionArray[this.mArrayPos] = this.mCurrentPollution;
					this.mArrayPos = 0;
				}
			}
		}		
	}

	public int getAveragePollutionOverLastTen(){
		int counter = 0;
		int total = 0;
		
		for (int i=0;i<this.mAveragePollutionArray.length;i++){
			if (this.mAveragePollutionArray[i] != 0){
				total += this.mAveragePollutionArray[i];
				counter++;
			}
		}
		int returnValue = 0;
		if (total > 0 && counter > 0){
			returnValue = (total/counter);
			this.mAveragePollution = returnValue;
		}
		else {
			returnValue = getCurrentChunkPollution();
		}
		Utils.LOG_INFO("| DEBUG: "+returnValue +" | ArrayPos:"+this.mArrayPos+" | Counter:"+counter+" | Total:"+total+" |");
		return returnValue;
	}

}