package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
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
		super(aName, 6, aDescription, aTextures);
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

	@Override
	public String[] getDescription() {
		String[] s2 = new String[]{
				"Fluid Input for Multiblocks",
				"Capacity: " + getCapacity()+"L"
				};		
		return s2;
	}

	public boolean isFluidInputAllowed(final FluidStack aFluid) {
		return aFluid.getFluid() == this.mLockedFluid;
	}

	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return (MetaTileEntity) new GT_MetaTileEntity_Hatch_CustomFluidBase(this.mLockedFluid, this.mFluidCapacity, this.mName, this.mDescriptionArray,	this.mTextures);
	}
}