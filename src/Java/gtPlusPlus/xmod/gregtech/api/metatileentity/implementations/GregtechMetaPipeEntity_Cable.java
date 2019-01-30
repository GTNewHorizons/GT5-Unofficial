package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.VN;
import static gtPlusPlus.core.lib.CORE.GTNH;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityCable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.core.lib.CORE;

public class GregtechMetaPipeEntity_Cable extends GT_MetaPipeEntity_Cable implements IMetaTileEntityCable {
	private static Textures.BlockIcons INSULATION_MEDIUM_PLUS;
	static{
		if(GTNH) {
			try {
				INSULATION_MEDIUM_PLUS = (Textures.BlockIcons) GT_Utility.getField(Textures.BlockIcons.class, "INSULATION_MEDIUM_PLUS").get(null);
			} catch (IllegalAccessException | NullPointerException e) {
				throw new Error(e);
			}
		}
	}

	private short[] vRGB = null;
	
	public GregtechMetaPipeEntity_Cable(final int aID, final String aName, final String aNameRegional, final float aThickNess, final Materials aMaterial, final long aCableLossPerMeter, final long aAmperage, final long aVoltage, final boolean aInsulated, final boolean aCanShock, final short[] aRGB) {
		super(aID, aName, aNameRegional, aThickNess, aMaterial, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock);
		this.vRGB = aRGB==null || aRGB.length!=4?Materials.Iron.mRGBa:aRGB;
	}

	public GregtechMetaPipeEntity_Cable(final String aName, final float aThickNess, final Materials aMaterial, final long aCableLossPerMeter, final long aAmperage, final long aVoltage, final boolean aInsulated, final boolean aCanShock, final short[] aRGB) {
		super(aName, aThickNess, aMaterial, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock);
		this.vRGB = aRGB==null || aRGB.length!=4?Materials.Iron.mRGBa:aRGB;
	}
	
	public GregtechMetaPipeEntity_Cable(final int aID, final String aName, final String aNameRegional, final float aThickNess, final long aCableLossPerMeter, final long aAmperage, final long aVoltage, final boolean aInsulated, final boolean aCanShock, final short[] aRGB) {
		this(aID, aName, aNameRegional, aThickNess, null, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock,aRGB);
	}

	public GregtechMetaPipeEntity_Cable(final String aName, final float aThickNess, final long aCableLossPerMeter, final long aAmperage, final long aVoltage, final boolean aInsulated, final boolean aCanShock, final short[] aRGB) {
		this(aName, aThickNess, null, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock,aRGB);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaPipeEntity_Cable(this.mName, this.mThickNess, this.mMaterial, this.mCableLossPerMeter, this.mAmperage, this.mVoltage, this.mInsulated, this.mCanShock, this.vRGB);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Max Voltage: %%%" + EnumChatFormatting.GREEN + mVoltage + " (" + VN[GT_Utility.getTier(mVoltage)] + ")" + EnumChatFormatting.GRAY,
				"Max Amperage: %%%" + EnumChatFormatting.YELLOW + mAmperage + EnumChatFormatting.GRAY,
				"Loss/Meter/Ampere: %%%" + EnumChatFormatting.RED + mCableLossPerMeter + EnumChatFormatting.GRAY + "%%% EU-Volt",
				//CORE.GT_Tooltip
		};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections, byte aColorIndex, boolean aConnected, boolean aRedstone) {
		return GTNH?
				getTextureGTNH(aBaseMetaTileEntity,aSide,aConnections,aColorIndex,aConnected,aRedstone):
				getTexturePure(aBaseMetaTileEntity,aSide,aConnections,aColorIndex,aConnected,aRedstone);
	}

	private ITexture[] getTextureGTNH(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections,
									 byte aColorIndex, boolean aConnected, boolean aRedstone) {

		Materials wireMaterial=mMaterial;
		if (wireMaterial == null){
			wireMaterial = Materials.Iron;
		}

		if (!mInsulated)
			return new ITexture[]{new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], Dyes.getModulation(aColorIndex, vRGB) )};
		if (aConnected) {
			float tThickNess = getThickNess();
			if (tThickNess < 0.124F)
				return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.INSULATION_FULL, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
			if (tThickNess < 0.374F)//0.375 x1
				return new ITexture[]{new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], vRGB), new GT_RenderedTexture(Textures.BlockIcons.INSULATION_TINY, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
			if (tThickNess < 0.499F)//0.500 x2
				return new ITexture[]{new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], vRGB), new GT_RenderedTexture(Textures.BlockIcons.INSULATION_SMALL, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
			if (tThickNess < 0.624F)//0.625 x4
				return new ITexture[]{new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], vRGB), new GT_RenderedTexture(Textures.BlockIcons.INSULATION_MEDIUM, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
			if (tThickNess < 0.749F)//0.750 x8
				return new ITexture[]{new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], vRGB), new GT_RenderedTexture(INSULATION_MEDIUM_PLUS, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
			if (tThickNess < 0.874F)//0.825 x12
				return new ITexture[]{new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], vRGB), new GT_RenderedTexture(Textures.BlockIcons.INSULATION_LARGE, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
			return new ITexture[]{new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], vRGB), new GT_RenderedTexture(Textures.BlockIcons.INSULATION_HUGE, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
		}
		return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.INSULATION_FULL, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
	}

	private ITexture[] getTexturePure(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections,
			byte aColorIndex, boolean aConnected, boolean aRedstone) {

		//if (this.vRGB == null || this.vRGB.length < 3 || this.vRGB.length > 4){
		//	this.vRGB = new short[]{200, 0, 200, 0};
		//}
		//if (this.vRGB.length != 4){
		//	short[] tempRGB = this.vRGB;
		//	this.vRGB = new short[]{tempRGB[0], tempRGB[1], tempRGB[2], 0};
		//}
		//
		//Materials wireMaterial = this.mMaterial;
		//
		//if (wireMaterial == null){
		//	wireMaterial = Materials.Iron;
		//}

		//With the code in constructors it should work
		Materials wireMaterial=mMaterial;
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
						new GT_RenderedTexture(Textures.BlockIcons.INSULATION_LARGE,
								Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
			if (tThickNess < 0.874F)
				return new ITexture[] {
						new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[69], this.vRGB),
						new GT_RenderedTexture(Textures.BlockIcons.INSULATION_HUGE,
								Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
			return new ITexture[] { new GT_RenderedTexture(wireMaterial.mIconSet.mTextures[69], this.vRGB),
					new GT_RenderedTexture(Textures.BlockIcons.INSULATION_FULL,
							Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
		}
		return new ITexture[] { new GT_RenderedTexture(Textures.BlockIcons.INSULATION_FULL,
				Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
	}
}