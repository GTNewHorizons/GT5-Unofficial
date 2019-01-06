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
	 * Cached Matrix of the Multiblock, which makes future structural checks far quicker.
	 */
	private final BlockPos[][][] StructureMatrix;

	/**
	 * Has {@value StructureMatrix} been set yet?
	 */
	@SuppressWarnings("unused")
	private boolean mGeneratedMatrix = false;

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
		StructureMatrix = new BlockPos[width][height][depth];
		//Logger.INFO("Created new Blueprint.");
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

	@SuppressWarnings({ "unused", "rawtypes" })
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity) {
		//Check for Nulls
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

		//Get some Vars
		int xOffSetMulti = ((this.getControllerLayer().width-1)/2);
		int zOffSetMulti = ((this.getControllerLayer().depth-1)/2);		
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * xOffSetMulti;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * zOffSetMulti;		
		ForgeDirection aDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing());
		int tAmount = 0;		

		int contX = aControllerObject.getBaseMetaTileEntity().getXCoord(), contY = aControllerObject.getBaseMetaTileEntity().getYCoord(), contZ = aControllerObject.getBaseMetaTileEntity().getZCoord();

		Logger.INFO("Controller is located at ["+contX+", "+contY+", "+contZ+"]");

		boolean debugCacheDataVisually = true;
		
		
		if (/*!mGeneratedMatrix || StructureMatrix == null*/ true) {
			//Try Fancy Cache Stuff
			BlockPos aPos = getOffsetRelativeToGridPosition(aBaseMetaTileEntity, 0, 0, 0);			
			for (int Y = 0; Y < height; Y++) {
				for (int Z = 0; Z < depth; Z++) {
					for (int X = 0; X < width; X++) {
						int offsetX, offsetZ;
						Pair<Integer, Integer> j = MultiblockLayer.rotateOffsetValues(aDir, X, Z);
						offsetX = j.getKey();
						offsetZ = j.getValue();

						Logger.INFO("Pre-Rotated Offsets ["+X+", "+(aPos.yPos + Y)+", "+Z+"] | "+aDir.name());
						Logger.INFO("Rotated Offsets ["+offsetX+", "+(aPos.yPos + Y)+", "+offsetZ+"]");

						// Resolve Negatives
						int negTestX, negTestZ;
						if (aPos.xPos < 0) {
							int testA = aPos.xPos;
							testA -= -offsetX;
							negTestX = testA;
						} else {
							negTestX = offsetX + aPos.xPos;
						}
						if (aPos.zPos < 0) {
							int testA = aPos.zPos;
							testA -= -offsetZ;
							negTestZ = testA;
						} else {
							negTestZ = offsetZ + aPos.zPos;
						}
						Logger.INFO("Caching With Offset ["+negTestX+", "+(aPos.yPos + Y)+", "+negTestZ+"]");
						StructureMatrix[X][Y][Z] = new BlockPos(negTestX, (aPos.yPos + Y), negTestZ, aPos.world);
						
						if (debugCacheDataVisually) {
							aBaseMetaTileEntity.getWorld().setBlock(negTestX, (aPos.yPos + Y), negTestZ, Blocks.glass);
						}
					}
				}
			}			
			Logger.INFO("Cached blueprint matrix.");
			mGeneratedMatrix = true;			
		}
		else {
			Logger.INFO("Found cached blueprint matrix.");
		}

		if (StructureMatrix == null) {
			Logger.INFO("Error caching blueprint matrix.");
			return false;
		}


		int a1, a2, a3;
		a1 = StructureMatrix.length;
		a2 = StructureMatrix[0].length;
		a3 = StructureMatrix[0][0].length;		

		Logger.INFO("Matrix Size ["+a1+", "+a2+", "+a3+"]");

		for (int H = 0; H < a2; H++) {

			MultiblockLayer currentLayer = this.getLayer(H);
			for (int W = 0; W < a1; W++) {
				for (int D = 0; D < a3; D++) {

					BlockPos aToCheck = StructureMatrix[W][H][D];
					if (aToCheck == null) {
						Logger.INFO("Found bad data stored at X: "+W+", Y: "+H+", Z: "+D);
						continue;
					}
					else {
						//Logger.INFO("Found data stored at X: "+W+", Y: "+H+", Z: "+D);
						Logger.INFO("Checking "+aToCheck.getLocationString());						
					}

					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntity(aToCheck.xPos, aToCheck.yPos, aToCheck.zPos);
					final Block tBlock = aBaseMetaTileEntity.getBlock(aToCheck.xPos, aToCheck.yPos, aToCheck.zPos);
					final int tMeta = aBaseMetaTileEntity.getMetaID(aToCheck.xPos, aToCheck.yPos, aToCheck.zPos);


					LayerBlockData g1 = currentLayer.getDataFromCoordsWithDirection(aDir, W, D);
					if (g1 == null) {
						Logger.INFO("Failed to find LayerBlockData. Using AIR_FALLBACK");
						//return false;*/
						g1 = LayerBlockData.FALLBACK_AIR_CHECK;
					}
					else {
						if (g1.isController) {
							Logger.INFO("Controller is at  X: "+W+", Y: "+H+", Z: "+D);
						}
					}

					boolean isMatch = g1.match(tBlock, tMeta);
					

					if (!isMatch) {
						Logger.INFO("Checking ["+aToCheck.xPos+", "+ aToCheck.yPos +", "+ aToCheck.zPos+"]");
						Logger.INFO("Checking Position relative to Grid. X: "+W+", Y: "+H+", Z: "+D);						
						Logger.INFO("Found "+tBlock.getLocalizedName()+" : "+tMeta + " | Bad ["+W+", "+D+"]");						

						LayerBlockData g = currentLayer.getDataFromCoordsWithDirection(aDir, W, D);

						if (g == null) {
							Logger.INFO("Expected "+" BAD DATA - Possibly Unset Area in Blueprint.");

						}
						else {
							Logger.INFO("Expected "+g.mBlock.getLocalizedName()+" : "+g.mMeta + "");							
						}		
						aBaseMetaTileEntity.getWorld().setBlock(aToCheck.xPos, aToCheck.yPos, aToCheck.zPos, g.mBlock);
						aBaseMetaTileEntity.getWorld().setBlockMetadataWithNotify(aToCheck.xPos, aToCheck.yPos, aToCheck.zPos, g.mMeta, 4);
						//return false;
					}	
					else {

						LayerBlockData g = currentLayer.getDataFromCoordsWithDirection(aDir, W, D);



















						boolean isHatchValidType = false;
						if (g != null) {
							if (g.canBeHatch && !g.isController && tTileEntity != null) {
								IMetaTileEntity aMetaTileEntity2 = tTileEntity.getMetaTileEntity();
								if (aMetaTileEntity2 != null) {
									if (aMetaTileEntity2 instanceof GT_MetaTileEntity_MultiBlockBase) {
										isHatchValidType = true;
										break;										
									}
									else {
										for (Class c : g.mHatchClass) {
											if (c != null) {										
												if (c.isInstance(aMetaTileEntity2)) {
													isHatchValidType = true;
													break;
												}
											}
										}
									}									
								}								
							}						
						}
						
						if (!isHatchValidType && !g.isController && tTileEntity != null) {
							Logger.INFO("Checking ["+aToCheck.xPos+", "+ aToCheck.yPos +", "+ aToCheck.zPos+"]");
							Logger.INFO("Hatch Type did not match allowed types. "+tTileEntity.getClass().getSimpleName());
							return false;
						}						
						if (!aControllerObject.addToMachineList(tTileEntity, mTextureID)) {
							tAmount++;
						}


					}
				}
			}
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
		int X = controllerLocationRelativeToGrid.getKey(), Z = controllerLocationRelativeToGrid.getValue();	
		Logger.INFO("Attempting to translate offsets ["+X+", "+Z+"]");	
		if (aDir == ForgeDirection.NORTH) {			
			offsetX = -X;						
			offsetZ = -Z;			
		}

		else if (aDir == ForgeDirection.EAST) {			
			offsetX = Z;			
			offsetZ = -X;			
		}

		else if (aDir == ForgeDirection.SOUTH) {			
			offsetX = X;			
			offsetZ = Z;			
		}

		else if (aDir == ForgeDirection.WEST) {			
			offsetX = -Z;				
			offsetZ = X;				
		}
		else {
			offsetX = -X;
			offsetZ = -Z;			
		}	

		offsetY = -controllerYRelative;

		Logger.INFO("Attempting to use offsets ["+offsetX+", "+offsetY+", "+offsetZ+"]");

		//Resolve Negatives
		int negTestX, negTestZ;
		if (controllerX < 0) {
			Logger.INFO("Found Negative X Pos.");			
			int testA = controllerX;
			testA -= offsetX;
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
