package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import java.lang.reflect.Field;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_Hatch_Naquadah extends GT_MetaTileEntity_Hatch_Input {

	public final FluidStack[] mFluidsToUse = new FluidStack[3];
	public final int mFluidCapacity;

	public GT_MetaTileEntity_Hatch_Naquadah(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional, 6);
		mFluidCapacity = 32000;
		initHatch();
	}

	public GT_MetaTileEntity_Hatch_Naquadah(final String aName, final String aDescription,
			final ITexture[][][] aTextures) {
		super(aName, 6, aDescription, aTextures);
		mFluidCapacity = 32000;
		initHatch();
	}

	public GT_MetaTileEntity_Hatch_Naquadah(final String aName, final String[] aDescription,
			final ITexture[][][] aTextures) {
		super(aName, 6, aDescription[0], aTextures);
		mFluidCapacity = 32000;
		initHatch();
	}
	
	private void initHatch() {
		if (mFluidsToUse[0] == null) {
			mFluidsToUse[0] = Materials.Naquadah.getMolten(1);
		}
		if (mFluidsToUse[1] == null) {
			mFluidsToUse[1] = Materials.NaquadahEnriched.getMolten(1);
		}
		if (mFluidsToUse[2] == null) {
			mFluidsToUse[2] = Materials.Naquadria.getMolten(1);
		}
	}

	public ITexture[] getTexturesActive(final ITexture aBaseTexture) {
		return new ITexture[] { aBaseTexture, new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.NAQUADAH_REACTOR_FLUID_SIDE_ACTIVE) };
	}

	public ITexture[] getTexturesInactive(final ITexture aBaseTexture) {
		return new ITexture[] { aBaseTexture, new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.NAQUADAH_REACTOR_FLUID_SIDE) };
	}

	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		if (aSide == aBaseMetaTileEntity.getFrontFacing() && aIndex == 0) {
			for (FluidStack f : mFluidsToUse) {
				if (f != null) {
					if (GT_Utility.getFluidForFilledItem(aStack, true).getFluid() == f.getFluid()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean isFluidInputAllowed(final FluidStack aFluid) {
		for (FluidStack f : mFluidsToUse) {
			if (f != null) {
				if (aFluid.getFluid() == f.getFluid()) {
					return true;
				}
			}
		}
		return false;
	}

	public int getCapacity() {
		return this.mFluidCapacity;
	}

	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return (MetaTileEntity) new GT_MetaTileEntity_Hatch_Naquadah(this.mName, this.mDescription, this.mTextures);
	}
	
	@Override
	public String[] getDescription() {
		if (aDescCache[0] == null || aDescCache[0].contains(".name") || aDescCache[0].contains("fluid.")) {
			aDescCache[0] = formatFluidString(this.mFluidsToUse[0]);
		}
		if (aDescCache[1] == null || aDescCache[1].contains(".name") || aDescCache[1].contains("fluid.")) {
			aDescCache[1] = formatFluidString(this.mFluidsToUse[1]);
		}
		if (aDescCache[2] == null || aDescCache[2].contains(".name") || aDescCache[2].contains("fluid.")) {
			aDescCache[2] = formatFluidString(this.mFluidsToUse[2]);
		}		
		String aNaq = aDescCache[0];
		String aEnrNaq = aDescCache[1];
		String aNaquad = aDescCache[2];	
		String[] s2 = new String[]{
				"Fluid Input for Multiblocks",
				"Capacity: " + getCapacity()+"L",
				"Accepted Fluid: " + aNaq,
				"Accepted Fluid: " + aEnrNaq,
				"Accepted Fluid: " + aNaquad
				};		
		return s2;
	}
	
	private static String[] aDescCache = new String[3];	
	private String formatFluidString(FluidStack f) {		
		FluidStack mLockedStack = f;
		Integer mLockedTemp = 0;;
		String mTempMod = ""+EnumChatFormatting.RESET;		
		mLockedTemp = mLockedStack.getFluid().getTemperature();		
		if (mLockedTemp != null) {
			if (mLockedTemp <= -3000) {
				mTempMod = ""+EnumChatFormatting.DARK_PURPLE;
			}
			else if (mLockedTemp >= -2999 && mLockedTemp <= -500) {
				mTempMod = ""+EnumChatFormatting.DARK_BLUE;
			}
			else if (mLockedTemp >= -499 && mLockedTemp <= -50) {
				mTempMod = ""+EnumChatFormatting.BLUE;
			}
			else if (mLockedTemp >= 30 && mLockedTemp <= 300) {
				mTempMod = ""+EnumChatFormatting.AQUA;
			}
			else if (mLockedTemp >= 301 && mLockedTemp <= 800) {
				mTempMod = ""+EnumChatFormatting.YELLOW;
			}
			else if (mLockedTemp >= 801 && mLockedTemp <= 1500) {
				mTempMod = ""+EnumChatFormatting.GOLD;
			}
			else if (mLockedTemp >= 1501) {
				mTempMod = ""+EnumChatFormatting.RED;
			}
		}		
		return mTempMod + mLockedStack.getLocalizedName();
	}

	@Override
	public boolean doesFillContainers() {
		return false;
	}

	@Override
	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		// TODO Auto-generated method stub
		return super.getTextureSet(aTextures);
	}

	private Field F1, F2;
	
	
	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		byte a1 = 0, a2 = 0;
		try {
		if (F1 == null) {
			F1 = ReflectionUtils.getField(getClass(), "actualTexture");
		}
		if (F2 == null) {
			F2 = ReflectionUtils.getField(getClass(), "mTexturePage");
		}
		
		if (F1 != null) {
			a1 = F1.getByte(this);
		}
		if (F2 != null) {
			a2 = F2.getByte(this);
		}
		}
		catch (IllegalArgumentException | IllegalAccessException n) {}
		
		int textureIndex = a1 | a2 << 7;
		byte texturePointer = (byte) (a1 & 127);
		
		
		
		if (aSide == 1 || aSide == 0) {
			ITexture g = textureIndex > 0 ?  StaticFields59.getCasingTexturePages(a2, texturePointer) : BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1];
			
			return new ITexture[] {g, new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.NAQUADAH_REACTOR_FLUID_TOP_ACTIVE) };
		}
		
		return aSide != aFacing	? 
				(textureIndex > 0 ? new ITexture[] { StaticFields59.getCasingTexturePages(a2, texturePointer) } : new ITexture[] { BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1] })
				: (textureIndex > 0	? (aActive ? this.getTexturesActive(StaticFields59.getCasingTexturePages(a2, texturePointer)) : this.getTexturesInactive(StaticFields59.getCasingTexturePages(a2, texturePointer)))
						: (aActive ? this.getTexturesActive(BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1]) : this.getTexturesInactive(BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1])));
	}

}
