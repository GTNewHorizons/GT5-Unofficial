package tileentities;

import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import kekztech.MultiFluidHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class TFFTMultiHatch extends GT_MetaTileEntity_Hatch {

	private MultiFluidHandler multiFluidHandler;
	
	public TFFTMultiHatch(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 3,
				new String[]{"Exclusive fluid I/O for the T.F.F.T", "Capacity: " + 8000 * (aTier + 1) + "L"}, new ITexture[0]);
	}

	public TFFTMultiHatch(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
	}

	public TFFTMultiHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
	}

	public ITexture[] getTexturesActive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, new GT_RenderedTexture(BlockIcons.OVERLAY_PIPE_IN)};
	}

	public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, new GT_RenderedTexture(BlockIcons.OVERLAY_PIPE_IN)};
	}
	
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new TFFTMultiHatch(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
	}
	
	public boolean isSimpleMachine() {
		return true;
	}

	public boolean isFacingValid(byte aFacing) {
		return true;
	}

	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}
	
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		} else {
			return true;
		}
	}

	public boolean doesFillContainers() {
		return false;
	}

	public boolean doesEmptyContainers() {
		return false;
	}

	public boolean canTankBeFilled() {
		return true;
	}

	public boolean canTankBeEmptied() {
		return true;
	}

	public boolean displaysItemStack() {
		return false;
	}

	public boolean displaysStackSize() {
		return false;
	}
	
	public boolean isFluidInputAllowed(FluidStack aFluid) {
		return (multiFluidHandler != null) ? multiFluidHandler.couldPush(aFluid) : false;
	}

	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}
	
	public int getCapacity() {
		return (multiFluidHandler != null) ? multiFluidHandler.getCapacity() : 0; 
	}

	public int getTankPressure() {
		return -100;
	}
	
	public void setMultiFluidHandler(MultiFluidHandler multiFluidHandler) {
		this.multiFluidHandler = multiFluidHandler;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
