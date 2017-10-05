package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityCable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.util.EnumChatFormatting;

import static gregtech.api.enums.GT_Values.VN;

import gregtech.api.enums.Dyes;

public class GregtechMetaPipeEntity_Cable extends GT_MetaPipeEntity_Cable implements IMetaTileEntityCable {
	
	private short[] vRGB = null;
	
	public GregtechMetaPipeEntity_Cable(final int aID, final String aName, final String aNameRegional, final float aThickNess, final Materials aMaterial, final long aCableLossPerMeter, final long aAmperage, final long aVoltage, final boolean aInsulated, final boolean aCanShock, final short[] aRGB) {
		super(aID, aName, aNameRegional, aThickNess, aMaterial, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock);
		this.vRGB = aRGB;
	}

	public GregtechMetaPipeEntity_Cable(final String aName, final float aThickNess, final Materials aMaterial, final long aCableLossPerMeter, final long aAmperage, final long aVoltage, final boolean aInsulated, final boolean aCanShock, final short[] aRGB) {
		super(aName, aThickNess, aMaterial, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock);
		this.vRGB = aRGB;
	}
	
	public GregtechMetaPipeEntity_Cable(final int aID, final String aName, final String aNameRegional, final float aThickNess, final long aCableLossPerMeter, final long aAmperage, final long aVoltage, final boolean aInsulated, final boolean aCanShock, final short[] aRGB) {
		super(aID, aName, aNameRegional, aThickNess, null, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock);
		this.vRGB = aRGB;
	}

	public GregtechMetaPipeEntity_Cable(final String aName, final float aThickNess, final long aCableLossPerMeter, final long aAmperage, final long aVoltage, final boolean aInsulated, final boolean aCanShock, final short[] aRGB) {
		super(aName, aThickNess, null, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock);
		this.vRGB = aRGB;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		if (this.mMaterial == null){
			return new GregtechMetaPipeEntity_Cable(this.mName, this.mThickNess, this.mCableLossPerMeter, this.mAmperage, this.mVoltage, this.mInsulated, this.mCanShock, this.vRGB);
		}
		else {
			return new GregtechMetaPipeEntity_Cable(this.mName, this.mThickNess, this.mMaterial, this.mCableLossPerMeter, this.mAmperage, this.mVoltage, this.mInsulated, this.mCanShock, this.vRGB);
		}
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Max Voltage: %%%" + EnumChatFormatting.GREEN + mVoltage + " (" + VN[GT_Utility.getTier(mVoltage)] + ")" + EnumChatFormatting.GRAY,
				"Max Amperage: %%%" + EnumChatFormatting.YELLOW + mAmperage + EnumChatFormatting.GRAY,
				"Loss/Meter/Ampere: %%%" + EnumChatFormatting.RED + mCableLossPerMeter + EnumChatFormatting.GRAY + "%%% EU-Volt",
				CORE.GT_Tooltip
		};
	}
	
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections,
			byte aColorIndex, boolean aConnected, boolean aRedstone) {
		
		if (this.vRGB == null || this.vRGB.length < 3 || this.vRGB.length > 4){
			this.vRGB = new short[]{200, 0, 200, 0};
		}
		if (this.vRGB.length != 4){
			short[] tempRGB = this.vRGB;
			this.vRGB = new short[]{tempRGB[0], tempRGB[1], tempRGB[2], 0};
		}
		
		Materials wireMaterial = this.mMaterial;
		
		if (wireMaterial == null){
			wireMaterial = Materials.Iron;
		}
		
		
		if (!(this.mInsulated))
			return new ITexture[] { new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[69],
					Dyes.getModulation(aColorIndex, this.vRGB)) };
		if (aConnected) {
			float tThickNess = getThickNess();
			if (tThickNess < 0.124F)
				return new ITexture[] { new GT_RenderedTexture(Textures.BlockIcons.INSULATION_FULL,
						Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
			if (tThickNess < 0.374F)
				return new ITexture[] {
						new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[69], this.vRGB),
						new GT_RenderedTexture(Textures.BlockIcons.INSULATION_TINY,
								Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
			if (tThickNess < 0.499F)
				return new ITexture[] {
						new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[69], this.vRGB),
						new GT_RenderedTexture(Textures.BlockIcons.INSULATION_SMALL,
								Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
			if (tThickNess < 0.624F)
				return new ITexture[] {
						new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[69], this.vRGB),
						new GT_RenderedTexture(Textures.BlockIcons.INSULATION_MEDIUM,
								Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
			if (tThickNess < 0.749F)
				return new ITexture[] {
						new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[69], this.vRGB),
						new GT_RenderedTexture(Textures.BlockIcons.INSULATION_MEDIUM_PLUS,
								Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
			if (tThickNess < 0.874F)
				return new ITexture[] {
						new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[69], this.vRGB),
						new GT_RenderedTexture(Textures.BlockIcons.INSULATION_LARGE,
								Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
			return new ITexture[] { new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[69], this.vRGB),
					new GT_RenderedTexture(Textures.BlockIcons.INSULATION_HUGE,
							Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
		}
		return new ITexture[] { new GT_RenderedTexture(Textures.BlockIcons.INSULATION_FULL,
				Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
	}
}