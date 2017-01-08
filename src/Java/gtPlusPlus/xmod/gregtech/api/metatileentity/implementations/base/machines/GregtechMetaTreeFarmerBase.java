package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.machines;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class GregtechMetaTreeFarmerBase extends GT_MetaTileEntity_TieredMachineBlock {
	public boolean bOutput = false, bRedstoneIfFull = false, bInvert = false, bUnbreakable = false;
	public int mSuccess = 0, mTargetStackSize = 0;

	public GregtechMetaTreeFarmerBase(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription) {
		super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
	}

	public GregtechMetaTreeFarmerBase(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
	}

	@Override
	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = getFront(i);
			rTextures[1][i + 1] = getBack(i);
			rTextures[2][i + 1] = getBottom(i);
			rTextures[3][i + 1] = getTop(i);
			rTextures[4][i + 1] = getSides(i);
			rTextures[5][i + 1] = getFrontActive(i);
			rTextures[6][i + 1] = getBackActive(i);
			rTextures[7][i + 1] = getBottomActive(i);
			rTextures[8][i + 1] = getTopActive(i);
			rTextures[9][i + 1] = getSidesActive(i);
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		return mTextures[(aActive ? 5 : 0) + (aSide == aFacing ? 0 : aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex + 1];
	}

	@Override
	public boolean isSimpleMachine() {
		return false;
	}

	@Override
	public boolean isValidSlot(int aIndex) {
		return false;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return true;
	}

	@Override
	public boolean isEnetInput() {
		return false;
	}

	@Override
	public boolean isEnetOutput() {
		return false;
	}

	@Override
	public boolean isInputFacing(byte aSide) {
		return !isOutputFacing(aSide);
	}

	@Override
	public boolean isOutputFacing(byte aSide) {
		return getBaseMetaTileEntity().getBackFacing() == aSide;
	}

	@Override
	public boolean isTeleporterCompatible() {
		return false;
	}

	@Override
	public long getMinimumStoredEU() {
		return 0;
	}

	@Override
	public long maxEUStore() {
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

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return false;
	}

	public abstract ITexture getOverlayIcon();


	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	public ITexture[] getFront(byte aColor) {
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Farm_Manager)};
	}

	public ITexture[] getBack(byte aColor) {
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Farm_Manager)};
	}

	public ITexture[] getBottom(byte aColor) {
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Acacia_Log)};
	}

	public ITexture[] getTop(byte aColor) {
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Podzol)};
	}

	public ITexture[] getSides(byte aColor) {
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Farm_Manager)};
	}

	public ITexture[] getFrontActive(byte aColor) {
		return getFront(aColor);
	}

	public ITexture[] getBackActive(byte aColor) {
		return getBack(aColor);
	}

	public ITexture[] getBottomActive(byte aColor) {
		return getBottom(aColor);
	}

	public ITexture[] getTopActive(byte aColor) {
		return getTop(aColor);
	}

	public ITexture[] getSidesActive(byte aColor) {
		return getSides(aColor);
	}
}