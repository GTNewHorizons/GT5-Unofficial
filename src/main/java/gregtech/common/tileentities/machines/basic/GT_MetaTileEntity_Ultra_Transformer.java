package gregtech.common.tileentities.machines.basic;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Transformer;
import net.minecraft.util.EnumChatFormatting;

import static gregtech.api.enums.GT_Values.V;

public class GT_MetaTileEntity_Ultra_Transformer extends GT_MetaTileEntity_Transformer{
	public GT_MetaTileEntity_Ultra_Transformer(int aID, String aName, String aNameRegional, int aTier, String aDescription) {
		super(aID, aName, aNameRegional, aTier, aDescription);
	}

	public GT_MetaTileEntity_Ultra_Transformer(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}

	@Override
	public long maxEUStore() {
		return 2048L + V[mTier + 1] * 512L;
	}

	@Override
	public long maxAmperesOut() {
		
		return ((getBaseMetaTileEntity().isAllowedToWork()) ? 256L : 64L);
	}

	@Override
	public long maxAmperesIn() {
		
		return ((getBaseMetaTileEntity().isAllowedToWork()) ? 64L : 256L);
	}

	@Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[12][17][];
        for (byte b = -1; b < 16; b++) {
            rTextures[ 0][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], Textures.BlockIcons.OVERLAYS_ENERGY_OUT_POWER[mTier]};
            rTextures[ 1][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], Textures.BlockIcons.OVERLAYS_ENERGY_OUT_POWER[mTier]};
            rTextures[ 2][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], Textures.BlockIcons.OVERLAYS_ENERGY_OUT_POWER[mTier]};
            rTextures[ 3][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_BUFFER [mTier+1]};
            rTextures[ 4][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_BUFFER [mTier+1]};
            rTextures[ 5][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_BUFFER [mTier+1]};
            rTextures[ 6][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], Textures.BlockIcons.OVERLAYS_ENERGY_IN_POWER [mTier]};
            rTextures[ 7][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], Textures.BlockIcons.OVERLAYS_ENERGY_IN_POWER [mTier]};
            rTextures[ 8][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], Textures.BlockIcons.OVERLAYS_ENERGY_IN_POWER [mTier]};
            rTextures[ 9][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_BUFFER[mTier+1]};
            rTextures[10][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_BUFFER[mTier+1]};
            rTextures[11][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_BUFFER[mTier+1]};
        }
        return rTextures;
    }

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Ultra_Transformer(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	@Override
	public String[] getDescription() {
		return new String[] { this.mDescription, "Accepts 64A and outputs 256A", EnumChatFormatting.WHITE + "GregTech"};
	}
}
