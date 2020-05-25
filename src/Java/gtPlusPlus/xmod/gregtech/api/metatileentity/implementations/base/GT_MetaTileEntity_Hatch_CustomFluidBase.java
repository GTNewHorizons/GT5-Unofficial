package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;

public class GT_MetaTileEntity_Hatch_CustomFluidBase extends GT_MetaTileEntity_Hatch_Input {

	public final Fluid mLockedFluid;
	public final int mFluidCapacity;

	public GT_MetaTileEntity_Hatch_CustomFluidBase(Fluid aFluid, int aAmount, final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional, 6);
		this.mRecipeMap = null;
		this.mLockedFluid = aFluid;
		this.mFluidCapacity = aAmount;
	}

	public GT_MetaTileEntity_Hatch_CustomFluidBase(Fluid aFluid, int aAmount,  final String aName, final String aDescription,
			final ITexture[][][] aTextures) {
		super(aName, 6, aDescription, aTextures);
		this.mRecipeMap = null;
		this.mLockedFluid = aFluid;
		this.mFluidCapacity = aAmount;
	}

	public GT_MetaTileEntity_Hatch_CustomFluidBase(Fluid aFluid, int aAmount,  final String aName, final String[] aDescription, final ITexture[][][] aTextures) {
		super(aName, 6, aDescription[0], aTextures);
		this.mRecipeMap = null;
		this.mLockedFluid = aFluid;
		this.mFluidCapacity = aAmount;
	}

	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		return aSide == aBaseMetaTileEntity.getFrontFacing() && aIndex == 0
				&& (this.mRecipeMap == null || GT_Utility.getFluidForFilledItem(aStack, true).getFluid() == this.mLockedFluid);
	}

	public ITexture[] getTexturesActive(final ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture,
				new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.OVERLAY_PUMP)};
	}

	public ITexture[] getTexturesInactive(final ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture,
				new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.OVERLAY_PUMP)};
	}

	public int getCapacity() {
		return this.mFluidCapacity;
	}

	protected FluidStack mLockedStack = null;
	protected Integer mLockedTemp = null;
	protected String mTempMod = null;

	@Override
	public String[] getDescription() {		
		if (mLockedStack == null) {
			mLockedStack = FluidUtils.getFluidStack(mLockedFluid, 1);
		}
		int aFluidTemp = 0;
		boolean isSteam = false;
		if (mLockedFluid != null) {
			aFluidTemp = mLockedFluid.getTemperature();
			mTempMod = mLockedFluid.getName();
		}
		if (mTempMod.toLowerCase().equals("steam")) {
			isSteam = true;
		}
		
		
		EnumChatFormatting aColour = EnumChatFormatting.BLUE;
		if (aFluidTemp <= -3000) {
			aColour = EnumChatFormatting.DARK_PURPLE;
		}
		else if (aFluidTemp >= -2999 && aFluidTemp <= -500) {
			aColour = EnumChatFormatting.DARK_BLUE;
		}
		else if (aFluidTemp >= -499 && aFluidTemp <= -50) {
			aColour = EnumChatFormatting.BLUE;
		}
		else if (aFluidTemp >= 30 && aFluidTemp <= 300) {
			aColour = EnumChatFormatting.AQUA;
		}
		else if (aFluidTemp >= 301 && aFluidTemp <= 800) {
			aColour = EnumChatFormatting.YELLOW;
		}
		else if (aFluidTemp >= 801 && aFluidTemp <= 1500) {
			aColour = EnumChatFormatting.GOLD;
		}
		else if (aFluidTemp >= 1501) {
			aColour = EnumChatFormatting.RED;
		}
		String aFluidName = "Accepted Fluid: " + aColour + (mLockedStack != null ? mLockedStack.getLocalizedName() : "Empty") + EnumChatFormatting.RESET;
		String[] s2 = new String[]{
				"Fluid Input for "+(isSteam ? "Steam " : "")+"Multiblocks",
				"Capacity: " + getCapacity()+"L",
				aFluidName
		};		
		return s2;
	}

	public boolean isFluidInputAllowed(final FluidStack aFluid) {
		return aFluid.getFluid() == this.mLockedFluid;
	}

	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return (MetaTileEntity) new GT_MetaTileEntity_Hatch_CustomFluidBase(this.mLockedFluid, this.mFluidCapacity, this.mName, this.mDescription,	this.mTextures);
	}
}