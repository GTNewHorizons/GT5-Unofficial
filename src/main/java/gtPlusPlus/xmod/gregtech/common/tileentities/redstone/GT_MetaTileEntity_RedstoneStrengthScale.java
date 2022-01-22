package gtPlusPlus.xmod.gregtech.common.tileentities.redstone;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import net.minecraft.entity.player.EntityPlayer;

public class GT_MetaTileEntity_RedstoneStrengthScale extends GT_MetaTileEntity_RedstoneStrengthDisplay {

	public static TexturesGtBlock.CustomIcon[] sIconList = new TexturesGtBlock.CustomIcon[32];
	
	static {
		for (int i=0;i<32;i++) {
			sIconList[i] = new CustomIcon("TileEntities/gt4/redstone/Scale/"+i);
		}
	}
	
	public GT_MetaTileEntity_RedstoneStrengthScale(int aID) {
		super(aID, "redstone.display.scale", "Redstone Scale", "Redstone Strength on a Scale");
	}

	public GT_MetaTileEntity_RedstoneStrengthScale(final String aName, String aDescription, final ITexture[][][] aTextures) {
		super(aName, aDescription, aTextures);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_RedstoneStrengthScale(this.mName, mDescription, this.mTextures);
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (aSide == getBaseMetaTileEntity().getFrontFacing())
			mType = (byte) ((mType + 1) % 2);
	}
	
	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] {Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], new GT_RenderedTexture(sIconList[mType * 16 + mRedstoneStrength])};  
		}
		return this.mTextures[(aActive || hasRedstoneSignal() ? 5 : 0) + (aSide == aFacing ? 0 : aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex + 1];
	}

}
