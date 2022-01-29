package gtPlusPlus.xmod.gregtech.common.tileentities.redstone;

import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_RedstoneStrengthDisplay extends GT_MetaTileEntity_RedstoneBase {

	public byte mRedstoneStrength = 0, mType = 0;
	public static TexturesGtBlock.CustomIcon[] sIconList = new TexturesGtBlock.CustomIcon[144];
	
	static {
		for (int i=0;i<144;i++) {
			sIconList[i] = new CustomIcon("TileEntities/gt4/redstone/Display/"+i);
		}
	}
	
	public GT_MetaTileEntity_RedstoneStrengthDisplay(int aID, String aUnlocal, String aLocal, String aDescription) {
		super(aID, aUnlocal, aLocal, 5, 0, aDescription);
	}

	public GT_MetaTileEntity_RedstoneStrengthDisplay(final String aName, String aDescription, final ITexture[][][] aTextures) {
		super(aName, 5, 0, aDescription, aTextures);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_RedstoneStrengthDisplay(this.mName, mDescription, this.mTextures);
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setByte("mRedstoneStrength", mRedstoneStrength);
		aNBT.setByte("mType", mType);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		mRedstoneStrength = aNBT.getByte("mRedstoneStrength");
		mType = aNBT.getByte("mType");
	}

	@Override
	public void onValueUpdate(byte aValue) {
		mRedstoneStrength = (byte) (aValue & 15);
		mType = (byte) (aValue >>> 4);
	}

	@Override
	public byte getUpdateData() {
		return (byte) ((mRedstoneStrength & 15) | (mType << 4));
	}

	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);
		if (getBaseMetaTileEntity().isAllowedToWork() && getBaseMetaTileEntity().isServerSide()) {
			mRedstoneStrength = getBaseMetaTileEntity().getStrongestRedstone();
		}
	}
	
	/*@Override
	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
		if (aSide == 0)
			return aRedstone ? 60 : 59;
		if (aSide == 1)
			return aRedstone ? 58 : 57;
		return aRedstone ? 62 : 61;
	}*/

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (aSide == getBaseMetaTileEntity().getFrontFacing())
			mType = (byte) ((mType + 1) % 6);
	}

	/*@Override
	public IIcon getTextureIcon(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
		return aSide == aFacing ? sIconList[mType * 16 + mRedstoneStrength] : null;
	}*/
	
	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = this.getFront(i);
			rTextures[1][i + 1] = this.getSides(i);
			rTextures[2][i + 1] = this.getBottom(i);
			rTextures[3][i + 1] = this.getTop(i);
			rTextures[4][i + 1] = this.getSides(i);
			rTextures[5][i + 1] = this.getFront(i);
			rTextures[6][i + 1] = this.getSidesActive(i);
			rTextures[7][i + 1] = this.getBottomActive(i);
			rTextures[8][i + 1] = this.getTopActive(i);
			rTextures[9][i + 1] = this.getSidesActive(i);
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] {Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], new GT_RenderedTexture(sIconList[mType * 16 + mRedstoneStrength])};  
		}
		return this.mTextures[(aActive || hasRedstoneSignal() ? 5 : 0) + (aSide == aFacing ? 0 : aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex + 1];
	}

	public ITexture[] getFront(final byte aColor) {
		return new ITexture[] {Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1]};
	}

	public ITexture[] getTop(final byte aColor) {
		return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Top_Off)};
	}
	
	public ITexture[] getTopActive(final byte aColor) {
		return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Top_On)};
	}
	
	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Bottom_Off)};
	}
	
	public ITexture[] getBottomActive(final byte aColor) {
		return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Bottom_On)};
	}

	public ITexture[] getSides(final byte aColor) {
		return new ITexture[] {Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Side_Off)};
	}
	
	public ITexture[] getSidesActive(final byte aColor) {
		return new ITexture[] {Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Side_On)};
	}
	
}
