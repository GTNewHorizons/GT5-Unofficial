package gregtech.common.tileentities.machines.basic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Transformer;

public class GT_MetaTileEntity_TransformerHiAmp extends GT_MetaTileEntity_Transformer {

	public GT_MetaTileEntity_TransformerHiAmp(int aID, String aName, String aNameRegional, int aTier, String aDescription) {
		super(aID, aName, aNameRegional, aTier, aDescription);
	}

	public GT_MetaTileEntity_TransformerHiAmp(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}

	@Override
	public long maxEUStore() {
		return ((512L + gregtech.api.enums.GT_Values.V[(this.mTier + 1)] * 2L) * 8);
	}

	@Override
	public long maxAmperesOut() {
		
		return ((getBaseMetaTileEntity().isAllowedToWork()) ? 16L : 4L);
	}

	@Override
	public long maxAmperesIn() {
		
		return ((getBaseMetaTileEntity().isAllowedToWork()) ? 4L : 16L);
	}

	@Override
	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		ITexture[][][] rTextures = new ITexture[12][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
					Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier] };
			rTextures[1][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
					Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier] };
			rTextures[2][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
					Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier] };
			rTextures[3][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
					Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier + 1] };
			rTextures[4][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
					Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier + 1] };
			rTextures[5][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
					Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier + 1] };
			rTextures[6][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
					Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier] };
			rTextures[7][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
					Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier] };
			rTextures[8][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
					Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier] };
			rTextures[9][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
					Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier + 1] };
			rTextures[10][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
					Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier + 1] };
			rTextures[11][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
					Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier + 1] };
		}
		return rTextures;
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_TransformerHiAmp(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	@Override
	public String[] getDescription() {
		return new String[] { this.mDescription, "Accepts 4A and outputs 16A" };
	}
}