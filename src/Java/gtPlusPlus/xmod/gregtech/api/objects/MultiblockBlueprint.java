package gtPlusPlus.xmod.gregtech.api.objects;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gtPlusPlus.api.objects.Logger;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class MultiblockBlueprint {

	private final MultiblockLayer[] mBlueprintData;
	
	public final int height;
	public final int width;
	public final int depth;	
	public final int mMinimumCasingCount;
	public final int mTextureID;
	
	/**
	 * A detailed class which will contain blueprints for a Multiblock. 
	 * Values are not relative to the controller, but in total.
	 * @param x - Overall width
	 * @param y - Overall height
	 * @param z - Overall depth
	 * @param aMinimumCasings - The lowest amount of casings required
	 * @param aTextureID - The texture ID used by hatches.
	 */
	public MultiblockBlueprint(final int x, final int y, final int z, final int aMinimumCasings, final int aTextureID) {
		mBlueprintData = new MultiblockLayer[y];
		height = y;
		width = x;
		depth = z;
		mMinimumCasingCount = aMinimumCasings;
		mTextureID = aTextureID;
		Logger.INFO("Created new Blueprint.");
	}
	
	/**
	 * 
	 * @param aY - The Y level of the layer to return, where 0 is the bottom and N is the top.
	 * @return - A {@link MultiblockLayer} object.
	 */
	public MultiblockLayer getLayer(int aY) {
		return mBlueprintData[aY];
	}
	
	/**
	 * 
	 * @param aLayer - A {@link MultiblockLayer} object.
	 * @param aY - The Y level of the layer, where 0 is the bottom and N is the top.
	 * 
	 */
	public void setLayer(MultiblockLayer aLayer, int aY) {
		mBlueprintData[aY] = aLayer;
	}
	
	public MultiblockLayer getControllerLayer() {
		for (MultiblockLayer u : mBlueprintData) {
			if (u.hasController()) {
				return u;
			}
		}
		return null;
	}
	
	public int getControllerY() {
		int i = 0;
		for (MultiblockLayer u : mBlueprintData) {
			if (u.hasController()) {
				return i;
			}
			i++;
		}
		return 0;
	}	
	
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity) {
		if (aBaseMetaTileEntity == null) {
			return false;
		}		
		final IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		GT_MetaTileEntity_MultiBlockBase aControllerObject = null;
		if (aMetaTileEntity instanceof GT_MetaTileEntity_MultiBlockBase) {
			aControllerObject = (GT_MetaTileEntity_MultiBlockBase) aMetaTileEntity;
		}		
		if (aControllerObject == null) {
			return false;
		}
			
		int xOffSetMulti = ((this.getControllerLayer().width-1)/2);
		int zOffSetMulti = ((this.getControllerLayer().depth-1)/2);		
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * xOffSetMulti;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * zOffSetMulti;		
		
		int a1, a2;
		a1 = this.getControllerY();
		a2 = this.height;
		int tAmount = 0;		
		
		@SuppressWarnings("unused")
		int xRelativeCounter = 0, yRelativeCounter = 0, zRelativeCounter = 0;		
		for (int Y = -a1; Y < ((a2-1)-a1); Y++) {
			
			MultiblockLayer aCurrentLayerToCheck = this.getLayer(Y);			
			int aWidth = aCurrentLayerToCheck.width;
			int aDepth = aCurrentLayerToCheck.depth;			
			
			int aDepthMin, aDepthMax;
			int aWidthMin, aWidthMax;
			
			aWidthMin = -((aWidth-1)/2);
			aWidthMax = ((aWidth-1)/2);
			aDepthMin = -((aDepth-1)/2);
			aDepthMax = ((aDepth-1)/2);
			
			for (int Z = aDepthMin; Z < aDepthMax; Z++) {				
				for (int X = aWidthMin; X < aWidthMax; X++) {					
					int b1 = xDir + X;
					int b2 = zDir + Z;					
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(b1, Y, b2);
					final Block tBlock = aBaseMetaTileEntity.getBlockOffset(b1, Y, b2);
					final int tMeta = aBaseMetaTileEntity.getMetaIDOffset(b1, Y, b2);					
					if (!aCurrentLayerToCheck.getBlockForPos(tBlock, tMeta, xRelativeCounter, zRelativeCounter, ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()))) {
						return false;
					}	
					else {
						if (!aControllerObject.addToMachineList(tTileEntity, mTextureID)) {
							tAmount++;
						}
					}
					xRelativeCounter++;
				}				
				xRelativeCounter = 0;
				zRelativeCounter++;
			}			
			yRelativeCounter++;
		}
		
		boolean hasCorrectHatches = (aControllerObject.mInputBusses.size() >= this.getMinimumInputBus() && 
				aControllerObject.mOutputBusses.size() >= this.getMinimumOutputBus() && 
				aControllerObject.mInputHatches.size() >= this.getMinimumInputHatch() && 
				aControllerObject.mOutputHatches.size() >= this.getMinimumOutputHatch() && 
				aControllerObject.mDynamoHatches.size() >= this.getMinimumOutputEnergy() && 
				aControllerObject.mEnergyHatches.size() >= this.getMinimumInputEnergy() && 
				aControllerObject.mMaintenanceHatches.size() >= this.getMinimumMaintHatch() && 
				aControllerObject.mMufflerHatches.size() >= this.getMinimumMufflers());
		
		
		return hasCorrectHatches && tAmount >= mMinimumCasingCount;		
	}

	public abstract int getMinimumInputBus();
	public abstract int getMinimumInputHatch();
	public abstract int getMinimumOutputBus();
	public abstract int getMinimumOutputHatch();
	public abstract int getMinimumInputEnergy();
	public abstract int getMinimumOutputEnergy();
	public abstract int getMinimumMaintHatch();
	public abstract int getMinimumMufflers();
	
}
