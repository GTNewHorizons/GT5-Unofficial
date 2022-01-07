package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTileEntity;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntityThaumcraftResearcher extends GregtechMetaTileEntity {

	public GregtechMetaTileEntityThaumcraftResearcher(final int aID, final String aName, final String aNameRegional, final int aTier, final String aDescription, final int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription);
	}

	public GregtechMetaTileEntityThaumcraftResearcher(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures, final int aSlotCount) {
		super(aName, aTier, aSlotCount, aDescription, aTextures);
	}

	@Override
	public String[] getDescription() {
		return new String[] {this.mDescription, "Generates Thaumcraft research notes, because it's magic."};
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
		return new ITexture[]{getSides(aColor)[0], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Metal_Grate_A)};
	}


	public ITexture[] getBack(final byte aColor) {
		return new ITexture[]{getSides(aColor)[0], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Metal_Grate_B)};
	}


	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{getSides(aColor)[0]};
	}


	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{getSides(aColor)[0], new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Blue)};
	}


	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Material_RedSteel)};
	}


	public ITexture[] getFrontActive(final byte aColor) {
		return getFront(aColor);
		}


	public ITexture[] getBackActive(final byte aColor) {
		return getBack(aColor);
		}


	public ITexture[] getBottomActive(final byte aColor) {
		return getBottom(aColor);
	}


	public ITexture[] getTopActive(final byte aColor) {
		return new ITexture[]{getSides(aColor)[0], new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange)};
	}


	public ITexture[] getSidesActive(final byte aColor) {
		return getSides(aColor);
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityThaumcraftResearcher(this.mName, this.mTier, this.mDescription, this.mTextures, this.mInventory.length);
	}

	@Override public boolean isSimpleMachine()						{return false;}
	@Override public boolean isElectric()							{return true;}
	@Override public boolean isValidSlot(final int aIndex)				{return true;}
	@Override public boolean isFacingValid(final byte aFacing)			{return true;}
	@Override public boolean isEnetInput() 							{return true;}
	@Override public boolean isEnetOutput() 						{return false;}
	@Override public boolean isInputFacing(final byte aSide)				{return aSide!=this.getBaseMetaTileEntity().getFrontFacing();}
	@Override public boolean isOutputFacing(final byte aSide)				{return aSide==this.getBaseMetaTileEntity().getBackFacing();}
	@Override public boolean isTeleporterCompatible()				{return false;}
	@Override public long getMinimumStoredEU()						{return 0;}
	@Override public long maxEUStore()								{return 512000;}
	@Override public int rechargerSlotStartIndex()					{return 0;}
	@Override public int dechargerSlotStartIndex()					{return 0;}
	@Override public int rechargerSlotCount()						{return 0;}
	@Override public int dechargerSlotCount()						{return 0;}
	@Override public boolean isAccessAllowed(final EntityPlayer aPlayer)	{return true;}

	@Override
	public int getCapacity() {
		return 128000;
	}	
	
	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide())
		{
			return true;
		}
		return true;
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return aSide==this.getBaseMetaTileEntity().getBackFacing();
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return true;
	}

	@Override
	public String[] getInfoData() {
		return new String[] {
				this.getLocalName(),
				};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public int getSizeInventory() {
		return 2;
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
		//aNBT.setInteger("mCurrentPollution", this.mCurrentPollution);
		//aNBT.setInteger("mAveragePollution", this.mAveragePollution);
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		//this.mCurrentPollution = aNBT.getInteger("mCurrentPollution");
		//this.mAveragePollution = aNBT.getInteger("mAveragePollution");
	}

	@Override
	public void onFirstTick(final IGregTechTileEntity aBaseMetaTileEntity) {
		super.onFirstTick(aBaseMetaTileEntity);
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);				
	}

}