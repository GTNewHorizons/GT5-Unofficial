package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTileEntity;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GregtechMetaGarbageCollector extends GregtechMetaTileEntity {

	long mLastCleanup = 0;
	long mLocalTickVar = 0;
	int mFrequency = 5;

	public GregtechMetaGarbageCollector(final String aName, final String aNameRegional, final String aDescription) {
		super(28750, aName, aNameRegional, 5, 0, aDescription);
	}

	public GregtechMetaGarbageCollector(final String aName, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, 5, 0, aDescription, aTextures);
	}

	@Override
	public String[] getDescription() {
		return new String[] {this.mDescription, "Can request the JVM to perform garbage collection", "Configurable to run once every 5 minute interval (5-180)", "This Machine has no recipe", "Admin Tool, Limit one per world if possible"};
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
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier+3][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_RedSteel)};
	}


	public ITexture[] getBack(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier+3][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_RedSteel)};
	}


	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier+3][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Grisium)};
	}


	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier+3][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Grisium)};
	}


	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier+3][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Redox_3)};
	}


	public ITexture[] getFrontActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier+3][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_RedSteel)};
	}


	public ITexture[] getBackActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier+3][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_RedSteel)};
	}


	public ITexture[] getBottomActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier+3][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Grisium)};
	}


	public ITexture[] getTopActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier+3][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Grisium)};
	}


	public ITexture[] getSidesActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier+3][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Redox_3)};
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (mFrequency < 180){
			mFrequency += 5;
		}
		else {
			mFrequency = 5;
		}
		PlayerUtils.messagePlayer(aPlayer, "Running every "+mFrequency+" minutes.");	
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaGarbageCollector(this.mName, this.mDescription, this.mTextures);
	}

	@Override public boolean isSimpleMachine()						{return true;}
	@Override public boolean isElectric()							{return false;}
	@Override public boolean isValidSlot(final int aIndex)				{return false;}
	@Override public boolean isFacingValid(final byte aFacing)			{return true;}
	@Override public boolean isEnetInput() 							{return false;}
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
		PlayerUtils.messagePlayer(playerIn, "Running every "+mFrequency+" minutes. Owner: "+this.getBaseMetaTileEntity().getOwnerName());
		long aDiff = mLocalTickVar - this.mLastCleanup;	
		PlayerUtils.messagePlayer(playerIn, "Last run: "+Utils.getSecondsFromMillis(aDiff)+" seconds ago.");		
		
		
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
	public String[] getInfoData() {
		return new String[] {
				this.getLocalName()
				};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
		return new int[] {};
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
		return true;
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

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		if (this.getBaseMetaTileEntity().isServerSide()) {
			mLocalTickVar = System.currentTimeMillis();
			long aDiff = mLocalTickVar - this.mLastCleanup;			
			if (Utils.getSecondsFromMillis(aDiff) >= (this.mFrequency * 60)) {
				CORE.gc();
				this.mLastCleanup = mLocalTickVar;
			}
		}		
	}

}