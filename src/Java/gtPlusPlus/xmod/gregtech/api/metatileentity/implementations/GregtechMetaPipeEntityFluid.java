package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import net.minecraft.util.EnumChatFormatting;

public class GregtechMetaPipeEntityFluid extends GT_MetaPipeEntity_Fluid {
	
	
	public static final boolean mGt6Pipe;
	
	static {
		Boolean aGt6 = (Boolean) StaticFields59.getFieldFromGregtechProxy("gt6Pipe");
		if (aGt6 != null) {
			mGt6Pipe = aGt6;
		}
		else {
			mGt6Pipe = false;
		}
	}
	
	public final GT_Materials mMaterial;
	private boolean mCheckConnections;
	
	
	public GregtechMetaPipeEntityFluid(int aID, String aName, String aNameRegional, float aThickNess, GT_Materials aMaterial,
			int aCapacity, int aHeatResistance, boolean aGasProof) {
		this(aID, aName, aNameRegional, aThickNess, aMaterial, aCapacity, aHeatResistance, aGasProof, 1);
	}
	
	public GregtechMetaPipeEntityFluid(final String aName, final float aThickNess, final GT_Materials aMaterial, final int aCapacity, final int aHeatResistance, final boolean aGasProof) {
		this(aName, aThickNess, aMaterial, aCapacity, aHeatResistance, aGasProof, 1);
	}

	public GregtechMetaPipeEntityFluid(int aID, String aName, String aNameRegional, float aThickNess, GT_Materials aMaterial,
			int aCapacity, int aHeatResistance, boolean aGasProof, int aFluidTypes) {
		super(aID, aName, aNameRegional, aThickNess, null, aCapacity, aHeatResistance, aGasProof);
		this.mLastReceivedFrom = 0;
		this.oLastReceivedFrom = 0;
		this.mCheckConnections = !mGt6Pipe;
		this.mMaterial = aMaterial;
	}
	


	public GregtechMetaPipeEntityFluid(String aName, float aThickNess, GT_Materials aMaterial, int aCapacity,
			int aHeatResistance, boolean aGasProof, int aFluidTypes) {
		super(aName, aThickNess, null, aCapacity, aHeatResistance, aGasProof);
		this.mLastReceivedFrom = 0;
		this.oLastReceivedFrom = 0;
		this.mCheckConnections = !mGt6Pipe;
		this.mMaterial = aMaterial;
	}


	@Override
	public byte getTileEntityBaseType() {
		return this.mMaterial == null ? 4 : (byte) ((this.mMaterial.contains(SubTag.WOOD) ? 12 : 4) + Math.max(0, Math.min(3, this.mMaterial.mToolQuality)));
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaPipeEntityFluid(this.mName, this.mThickNess, this.mMaterial, this.mCapacity, this.mHeatResistance, this.mGasProof);
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections, byte aColorIndex, boolean aConnected, boolean aRedstone) {
		if (aConnected) {
			float tThickNess = getThickNess();
			if (tThickNess < 0.124F)
				return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
			if (tThickNess < 0.374F)//0.375
				return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipeTiny.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
			if (tThickNess < 0.499F)//0.500
				return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipeSmall.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
			if (tThickNess < 0.749F)//0.750
				return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipeMedium.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
			if (tThickNess < 0.874F)//0.825
				return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipeLarge.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
			return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipeHuge.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
		}
		return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				EnumChatFormatting.BLUE + "Fluid Capacity: %%%" + (mCapacity * 20) + "%%% L/sec" + EnumChatFormatting.GRAY,
				EnumChatFormatting.RED + "Heat Limit: %%%" + mHeatResistance + "%%% K" + EnumChatFormatting.GRAY,
				EnumChatFormatting.DARK_GREEN + "Gas Proof: " + (this.mGasProof) + EnumChatFormatting.GRAY,
				//CORE.GT_Tooltip
		};
	}
}