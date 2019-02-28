package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import java.lang.reflect.Field;

import com.google.common.collect.BiMap;

import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_Hatch_Plasma extends GT_MetaTileEntity_Hatch_Output {

	public final AutoMap<Fluid> mFluidsToUse = new AutoMap<Fluid>();
	public final int mFluidCapacity;
	private int mTotalPlasmaSupported = -1;

	public GT_MetaTileEntity_Hatch_Plasma(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional, 6);
		mFluidCapacity = 256000;
		initHatch();
	}

	public GT_MetaTileEntity_Hatch_Plasma(final String aName, final String aDescription,
			final ITexture[][][] aTextures) {
		super(aName, 6, aDescription, aTextures);
		mFluidCapacity = 256000;
		initHatch();
	}

	public GT_MetaTileEntity_Hatch_Plasma(final String aName, final String[] aDescription,
			final ITexture[][][] aTextures) {
		super(aName, 6, aDescription[0], aTextures);
		mFluidCapacity = 256000;
		initHatch();
	}
	
	private void initHatch() {
		
		//Get all Plasmas, but the easiest way to do this is to just ask the Fluid Registry what exists and filter through them lazily.
		Field fluidNameCache;
		
		fluidNameCache = ReflectionUtils.getField(FluidRegistry.class, "fluidNames");
		
		AutoMap<String> mValidPlasmaNameCache = new AutoMap<String>();		
		if (fluidNameCache != null) {
			try {
				Object fluidNames = fluidNameCache.get(null);
				if (fluidNames != null) {
					try {
						@SuppressWarnings("unchecked")
						BiMap<Integer, String> fluidNamesMap = (BiMap<Integer, String>) fluidNames;
						if (fluidNamesMap != null) {
							for (String g : fluidNamesMap.values()) {
								if (g.toLowerCase().contains("plasma")) {
									mValidPlasmaNameCache.put(g);
								}
							}
						}
					} catch (ClassCastException e) {
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		}
		
		AutoMap<Fluid> mPlasmaCache = new AutoMap<Fluid>();
		if (!mValidPlasmaNameCache.isEmpty()) {
			for (String y : mValidPlasmaNameCache) {
				Fluid t = FluidRegistry.getFluid(y);
				if (t != null) {
					if (t.getTemperature() > 1000) {
						mPlasmaCache.put(t);
					}
				}
			}
		}
		
		if (!mPlasmaCache.isEmpty()) {			
			for (Fluid w : mPlasmaCache) {
				mFluidsToUse.put(w);
			}			
		}
		
		
		
		
		
	}

	public ITexture[] getTexturesActive(final ITexture aBaseTexture) {
		return new ITexture[] { aBaseTexture };
	}

	public ITexture[] getTexturesInactive(final ITexture aBaseTexture) {
		return new ITexture[] { aBaseTexture };
	}

	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		if (aSide == aBaseMetaTileEntity.getFrontFacing() && aIndex == 0) {
			for (Fluid f : mFluidsToUse) {
				if (f != null) {
					if (GT_Utility.getFluidForFilledItem(aStack, true).getFluid() == f) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean isFluidInputAllowed(final FluidStack aFluid) {
		for (Fluid f : mFluidsToUse) {
			if (f != null) {
				if (aFluid.getFluid() == f) {
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
		return (MetaTileEntity) new GT_MetaTileEntity_Hatch_Plasma(this.mName, this.mDescription, this.mTextures);
	}
	
	@Override
	public String[] getDescription() {
		
		if (mTotalPlasmaSupported < 0) {
			if (mFluidsToUse.isEmpty()) {
				mTotalPlasmaSupported = 0;
			}
			else {
				mTotalPlasmaSupported = mFluidsToUse.size();				
			}
		}

		String aX = EnumChatFormatting.GRAY+"";
		String a1 = EnumChatFormatting.GOLD+"Refined containment"+aX;
		String a2 = EnumChatFormatting.GOLD+"Capacity: "+EnumChatFormatting.DARK_AQUA+getCapacity()+"L"+aX;
		String a3 = EnumChatFormatting.GOLD+"Supports "+EnumChatFormatting.DARK_RED+mTotalPlasmaSupported+EnumChatFormatting.GOLD+" types of plasma"+aX;
		
		
		
		String[] s2 = new String[]{
				a1, a2, a3
				};		
		return s2;
	}

	@Override
	public boolean doesFillContainers() {
		return true;
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
			
			return new ITexture[] {g};
		}
		
		return aSide != aFacing	? 
				(textureIndex > 0 ? new ITexture[] { StaticFields59.getCasingTexturePages(a2, texturePointer) } : new ITexture[] { BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1] })
				: (textureIndex > 0	? (aActive ? this.getTexturesActive(StaticFields59.getCasingTexturePages(a2, texturePointer)) : this.getTexturesInactive(StaticFields59.getCasingTexturePages(a2, texturePointer)))
						: (aActive ? this.getTexturesActive(BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1]) : this.getTexturesInactive(BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1])));
	}

}
