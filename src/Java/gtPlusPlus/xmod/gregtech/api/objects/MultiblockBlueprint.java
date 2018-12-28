package gtPlusPlus.xmod.gregtech.api.objects;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.api.objects.data.Triplet;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.xmod.gregtech.api.objects.MultiblockLayer.LayerBlockData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
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
		int mLogID = 0;
		//Logger.INFO("Trying to Build Blueprint ["+(mLogID++)+"]");
		
		if (aBaseMetaTileEntity == null) {
			return false;
		}		
		//Logger.INFO("Trying to Build Blueprint ["+(mLogID++)+"]");
		final IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		//Logger.INFO("Trying to Build Blueprint ["+(mLogID++)+"]");
		GT_MetaTileEntity_MultiBlockBase aControllerObject = null;
		if (aMetaTileEntity instanceof GT_MetaTileEntity_MultiBlockBase) {
			aControllerObject = (GT_MetaTileEntity_MultiBlockBase) aMetaTileEntity;
		}		
		if (aControllerObject == null) {
			return false;
		}
		//Logger.INFO("Trying to Build Blueprint ["+(mLogID++)+"]");
			
		int xOffSetMulti = ((this.getControllerLayer().width-1)/2);
		int zOffSetMulti = ((this.getControllerLayer().depth-1)/2);		
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * xOffSetMulti;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * zOffSetMulti;		

		//Logger.INFO("Trying to Build Blueprint ["+(mLogID++)+"]");
		
		int tAmount = 0;		

		//Logger.INFO("Trying to Build Blueprint ["+(mLogID++)+"] (pre-Iteration)");
		
		//Try Fancy Cache Stuff

		BlockPos aPos = getOffsetRelativeToGridPosition(aBaseMetaTileEntity, 0, 0, 0);
		BlockPos[][][] StructureMatrix = new BlockPos[width][height][depth];

		ForgeDirection aDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing());
		Pair<Integer, Integer> controllerLocationRelativeToGrid = this.getControllerLayer().getControllerLocation();
		
		for (int Y = 0; Y < height; Y++) {			
			for (int Z = 0; Z < depth; Z++) {				
				for (int X = 0; X < width; X++) {
					
					int offsetX, offsetY, offsetZ;
					
					if (aDir == ForgeDirection.NORTH) {			
						offsetX = X;			
						offsetY = -this.getControllerY();			
						offsetZ = Z;			
					}

					else if (aDir == ForgeDirection.EAST) {			
						offsetX = -X;			
						offsetY = -this.getControllerY();
						offsetZ = Z;			
					}

					else if (aDir == ForgeDirection.SOUTH) {			
						offsetX = -X;			
						offsetY = -this.getControllerY();
						offsetZ = -Z;			
					}

					else if (aDir == ForgeDirection.WEST) {			
						offsetX = X;				
						offsetY = -this.getControllerY();
						offsetZ = -Z;				
					}
					else {
						offsetX = X;			
						offsetY = -this.getControllerY();
						offsetZ = Z;			
					}					
					
					//Resolve Negatives
					int negTestX, negTestZ;
					if (aPos.xPos < 0) {
						//Logger.INFO("Found Negative X Pos.");			
						int testA = aPos.xPos;
						testA -= -offsetX;
						//Logger.INFO("Adding Inverted Offset of "+offsetX+", making "+testA);
						negTestX = testA;		
					}
					else {
						negTestX = offsetX + aPos.xPos;
					}
					
					if (aPos.zPos < 0) {
						//Logger.INFO("Found Negative Z Pos.");			
						int testA = aPos.zPos;
						testA -= -offsetZ;
						//Logger.INFO("Adding Inverted Offset of "+offsetZ+", making "+testA);
						negTestZ = testA;		
					}
					else {
						negTestZ = offsetZ + aPos.zPos;
					}
					
					
					StructureMatrix[X][Y][Z] = new BlockPos(negTestX, aPos.yPos + Y, negTestZ, aPos.world);
				}
			}
		}
		
		int a1, a2, a3;
		a1 = StructureMatrix.length;
		a2 = StructureMatrix[0].length;
		a3 = StructureMatrix[0][0].length;
		
		
		for (int H = 0; H < a2; H++) {
			
			MultiblockLayer currentLayer = this.getLayer(H);
			for (int W = 0; W < a1; W++) {
				for (int D = 0; D < a3; D++) {
					
					BlockPos aToCheck = StructureMatrix[W][H][D];
					if (aToCheck == null) {
						Logger.INFO("Found bad data stored at X: "+W+", Y: "+H+", Z: "+D);
						continue;
					}
					
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntity(aToCheck.xPos, aToCheck.yPos, aToCheck.zPos);
					final Block tBlock = aBaseMetaTileEntity.getBlock(aToCheck.xPos, aToCheck.yPos, aToCheck.zPos);
					final int tMeta = aBaseMetaTileEntity.getMetaID(aToCheck.xPos, aToCheck.yPos, aToCheck.zPos);

					
					
					if (!currentLayer.getBlockForPos(tBlock, tMeta, W, D, aDir)) {

						Logger.INFO("Checking ["+aToCheck.xPos+", "+ aToCheck.yPos +", "+ aToCheck.zPos+"]");
						Logger.INFO("Checking Position relative to Grid. X: "+W+", Y: "+H+", Z: "+D);
						
						
						Logger.INFO("Found "+tBlock.getLocalizedName()+" : "+tMeta + " | Bad ["+W+", "+D+"]");
						
						LayerBlockData g;
						if (aDir == ForgeDirection.SOUTH) {
							g = currentLayer.mVariantOrientations.get(2)[W][D];
						}
						else if (aDir == ForgeDirection.WEST) {
							g = currentLayer.mVariantOrientations.get(3)[W][D];
						}
						else if (aDir == ForgeDirection.NORTH) {
							g = currentLayer.mVariantOrientations.get(0)[W][D];
						}
						else if (aDir == ForgeDirection.EAST) {
							g = currentLayer.mVariantOrientations.get(1)[W][D];
						}
						else {
							g = currentLayer.mLayerData[W][D];			
						}
						
						if (g == null) {
							Logger.INFO("Expected "+" BAD DATA - Possibly Unset Area in Blueprint.");
							
						}
						else {
							Logger.INFO("Expected "+g.mBlock.getLocalizedName()+" : "+g.mMeta + "");
							
						}
						
						
						
						/*
						BlockPos aPos2 = getOffsetRelativeToGridPosition(aBaseMetaTileEntity, X, Y, Z);						
						aBaseMetaTileEntity.getWorld().setBlock(aPos2.xPos, aPos2.yPos, aPos2.zPos, Blocks.bedrock);	*/					
						
						return false;
					}	
					else {
						//Logger.INFO("Found "+tBlock.getLocalizedName()+" : "+tMeta + " | Okay");
						if (!aControllerObject.addToMachineList(tTileEntity, mTextureID)) {
							tAmount++;
						}
					}
				}
			}
		}
		
		
		
		
		try {/*
		for (int Y = 0; Y < height; Y++) {
			
			MultiblockLayer aCurrentLayerToCheck = this.getLayer(Y);			
			int aWidth = aCurrentLayerToCheck.width;
			int aDepth = aCurrentLayerToCheck.depth;			
						
			
			for (int Z = 0; Z < aDepth; Z++) {				
				for (int X = 0; X < aWidth; X++) {
					
					final IGregTechTileEntity tTileEntity = getTileAtOffset(aBaseMetaTileEntity, X, Y, Z);
					final Pair<Block, Integer> tPair = getBlockAtOffset(aBaseMetaTileEntity, X, Y, Z);
					final Block tBlock = tPair.getKey();
					final int tMeta = tPair.getValue();					

					Logger.INFO("Checking Position relative to Controller. X: "+X+", Y: "+Y+", Z: "+Z);
					Logger.INFO("Checking Position relative to Grid. X: "+X+", Y: "+Y+", Z: "+Z);
					
					if (!aCurrentLayerToCheck.getBlockForPos(tBlock, tMeta, X, Z, ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()))) {

						Logger.INFO("Found "+tBlock.getLocalizedName()+" : "+tMeta + " | Bad");
						BlockPos aPos2 = getOffsetRelativeToGridPosition(aBaseMetaTileEntity, X, Y, Z);						
						aBaseMetaTileEntity.getWorld().setBlock(aPos2.xPos, aPos2.yPos, aPos2.zPos, Blocks.bedrock);						
						
						return false;
					}	
					else {
						Logger.INFO("Found "+tBlock.getLocalizedName()+" : "+tMeta + " | Okay");
						if (!aControllerObject.addToMachineList(tTileEntity, mTextureID)) {
							tAmount++;
						}
					}
				}				
			}		
		}
		*/}
		catch (Throwable r) {
			r.printStackTrace();
		}
		
		boolean hasCorrectHatches = (
				aControllerObject.mInputBusses.size() >= this.getMinimumInputBus() && 
				aControllerObject.mOutputBusses.size() >= this.getMinimumOutputBus() && 
				aControllerObject.mInputHatches.size() >= this.getMinimumInputHatch() && 
				aControllerObject.mOutputHatches.size() >= this.getMinimumOutputHatch() && 
				aControllerObject.mDynamoHatches.size() >= this.getMinimumOutputEnergy() && 
				aControllerObject.mEnergyHatches.size() >= this.getMinimumInputEnergy() && 
				aControllerObject.mMaintenanceHatches.size() >= this.getMinimumMaintHatch() && 
				aControllerObject.mMufflerHatches.size() >= this.getMinimumMufflers());
		

		Logger.INFO("mInputBusses: "+aControllerObject.mInputBusses.size());
		Logger.INFO("mOutputBusses: "+aControllerObject.mOutputBusses.size());
		Logger.INFO("mInputHatches: "+aControllerObject.mInputHatches.size());
		Logger.INFO("mOutputHatches: "+aControllerObject.mOutputHatches.size());
		Logger.INFO("mEnergyHatches: "+aControllerObject.mEnergyHatches.size());
		Logger.INFO("mDynamoHatches: "+aControllerObject.mDynamoHatches.size());
		Logger.INFO("mMaintenanceHatches: "+aControllerObject.mMaintenanceHatches.size());
		Logger.INFO("mMufflerHatches: "+aControllerObject.mMufflerHatches.size());
		
		boolean built = hasCorrectHatches && tAmount >= mMinimumCasingCount;
		Logger.INFO("Built? "+built);
		Logger.INFO("hasCorrectHatches? "+hasCorrectHatches);
		Logger.INFO("tAmount? "+tAmount);
		return built;		
	}
	
	public BlockPos getOffsetRelativeToGridPosition(final IGregTechTileEntity aBaseMetaTileEntity, final int x, final int y, final int z) {
		
		if (aBaseMetaTileEntity == null) {
			return null;
		}
		
		int controllerX, controllerY, controllerZ;		
		MultiblockLayer layerController = this.getControllerLayer();
		
		if (layerController == null) {
			return null;
		}	
		
		int controllerYRelative = this.getControllerY();		
		Pair<Integer, Integer> controllerLocationRelativeToGrid = layerController.getControllerLocation();
		
		if (controllerLocationRelativeToGrid == null) {
			return null;
		}

		controllerX = aBaseMetaTileEntity.getXCoord();
		controllerY = aBaseMetaTileEntity.getYCoord();
		controllerZ = aBaseMetaTileEntity.getZCoord();
		
		Logger.INFO("Controller is at ["+controllerX+", "+controllerY+", "+controllerZ+"]");

		ForgeDirection aDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing());
		Logger.INFO("Controller is facing "+aDir.name());
		
		//Find Bottom Left corner of Structure
		// 0, 0, 0
		
		int offsetX, offsetY, offsetZ;
		
		if (aDir == ForgeDirection.NORTH) {			
			offsetX = controllerLocationRelativeToGrid.getKey();			
			offsetY = -controllerYRelative;			
			offsetZ = controllerLocationRelativeToGrid.getValue();			
		}

		else if (aDir == ForgeDirection.EAST) {			
			offsetX = controllerLocationRelativeToGrid.getValue();			
			offsetY = -controllerYRelative;			
			offsetZ = -controllerLocationRelativeToGrid.getKey();			
		}

		else if (aDir == ForgeDirection.SOUTH) {			
			offsetX = controllerLocationRelativeToGrid.getKey();			
			offsetY = -controllerYRelative;			
			offsetZ = controllerLocationRelativeToGrid.getValue();			
		}

		else if (aDir == ForgeDirection.WEST) {			
			offsetX = -controllerLocationRelativeToGrid.getValue();			
			offsetY = -controllerYRelative;			
			offsetZ = controllerLocationRelativeToGrid.getKey();				
		}
		else {
			offsetX = -controllerLocationRelativeToGrid.getKey();			
			offsetY = -controllerYRelative;			
			offsetZ = -controllerLocationRelativeToGrid.getValue();			
		}

		Logger.INFO("Attempting to use offsets ["+offsetX+", "+offsetY+", "+offsetZ+"]");
		
		//Resolve Negatives
		int negTestX, negTestY, negTestZ;
		if (controllerX < 0) {
			Logger.INFO("Found Negative X Pos.");			
			int testA = controllerX;
			testA -= -offsetX;
			Logger.INFO("Adding Inverted Offset of "+offsetX+", making "+testA);
			negTestX = testA;		
		}
		else {
			negTestX = offsetX + controllerX;
		}
		if (controllerZ < 0) {
			Logger.INFO("Found Negative Z Pos.");			
			int testA = controllerZ;
			testA -= -offsetZ;
			Logger.INFO("Adding Inverted Offset of "+offsetZ+", making "+testA);
			negTestZ = testA;		
		}
		else {
			negTestZ = offsetZ + controllerZ;
		}
		
		
		//}
		//Bottom left Corner position
		BlockPos p = new BlockPos(negTestX, offsetY+controllerY, negTestZ, aBaseMetaTileEntity.getWorld());		

		Logger.INFO("World XYZ for Bottom left Corner Block of structure ["+p.xPos+", "+p.yPos+", "+p.zPos+"]");
		
		//Add the xyz relative to the grid.
		BlockPos offsetPos = new BlockPos(p.xPos+x, p.yPos+y, p.zPos+z, aBaseMetaTileEntity.getWorld());	
		Logger.INFO("World XYZ for Target Check Block in structure ["+offsetPos.xPos+", "+offsetPos.yPos+", "+offsetPos.zPos+"]");		
		
		return p;
	}
	
	
	public IGregTechTileEntity getTileAtOffset(final IGregTechTileEntity aBaseMetaTileEntity, int x, int y, int z){		
		BlockPos aPos = getOffsetRelativeToGridPosition(aBaseMetaTileEntity, x, y, z);
		final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(aPos.xPos, aPos.yPos, aPos.zPos);	
		//aBaseMetaTileEntity.getWorld().setBlock(xh, yh, zh, Blocks.gold_ore);	
		return tTileEntity;			
	}
	
	public Pair<Block, Integer> getBlockAtOffset(final IGregTechTileEntity aBaseMetaTileEntity, int x, int y, int z){		
		BlockPos aPos = getOffsetRelativeToGridPosition(aBaseMetaTileEntity, x, y, z);		
		final Block tBlock = aBaseMetaTileEntity.getBlockOffset(aPos.xPos, aPos.yPos, aPos.zPos);
		final int tMeta = aBaseMetaTileEntity.getMetaIDOffset(aPos.xPos, aPos.yPos, aPos.zPos);	
		return new Pair<Block, Integer>(tBlock, tMeta);
	}
	
	public Triplet<Integer, Integer, Integer> getOffsetFromControllerTo00(){		
		MultiblockLayer l = this.getControllerLayer();		
		if (l == null) {
			return null;
		}		
		int yOffset = this.getControllerY();		
		Pair<Integer, Integer> cl = l.getControllerLocation();
		
		if (cl == null) {
			return null;
		}		

		return new Triplet<Integer, Integer, Integer> (cl.getKey(), yOffset, cl.getValue());
		//return new Triplet<Integer, Integer, Integer> (cl.getKey(), yOffset, cl.getValue());
		
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
