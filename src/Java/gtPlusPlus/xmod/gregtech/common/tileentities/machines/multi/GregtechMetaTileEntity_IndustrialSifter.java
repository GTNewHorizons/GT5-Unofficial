package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.*;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_IndustrialSifter
extends GregtechMeta_MultiBlockBase {
	private static boolean controller;

	public GregtechMetaTileEntity_IndustrialSifter(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialSifter(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialSifter(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Industrial Sifter",
				"Size[WxHxL]: 5x3x5",
				"Controller (Center Bottom)",
				"1x Input Bus (Any top or bottom edge casing)",
				"4x Output Bus (Any top or bottom edge casing)",
				"1x Maintenance Hatch (Any top or bottom edge casing)",
				"1x Energy Hatch (Any top or bottom edge casing)",
				"9x Sieve Grate (Top and Middle 3x3)",
				"Sieve Casings for the rest (47)",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{TexturesGtBlock.CASING_BLOCKS_GTPP[21], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Diesel_Horizontal_Active : TexturesGtBlock.Overlay_Machine_Diesel_Horizontal)};
		}
		return new ITexture[]{TexturesGtBlock.CASING_BLOCKS_GTPP[21]};
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MacerationStack.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sSifterRecipes;
	}

	/*@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}*/

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public void onPreTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);
		if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive()) && (aBaseMetaTileEntity.getFrontFacing() != 1) && (aBaseMetaTileEntity.getCoverIDAtSide((byte) 1) == 0) && (!aBaseMetaTileEntity.getOpacityAtSide((byte) 1))) {
			if (MathUtils.randInt(0, 5) == 5){
				final Random tRandom = aBaseMetaTileEntity.getWorld().rand;
				aBaseMetaTileEntity.getWorld().spawnParticle("reddust", (aBaseMetaTileEntity.getXCoord() + 0.8F) - (tRandom.nextFloat() * 0.6F), aBaseMetaTileEntity.getYCoord() + 0.3f + (tRandom.nextFloat() * 0.2F), (aBaseMetaTileEntity.getZCoord() + 1.2F) - (tRandom.nextFloat() * 1.6F), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void startSoundLoop(final byte aIndex, final double aX, final double aY, final double aZ) {
		super.startSoundLoop(aIndex, aX, aY, aZ);
		if (aIndex == 1) {
			GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(Integer.valueOf(201)), 10, 1.0F, aX, aY, aZ);
		}
	}

	@Override
	public void startProcess() {
		this.sendLoopStart((byte) 1);
	}

	ItemStack[] mInputStacks;
	int[] cloneChances;
	GT_Recipe baseRecipe;
	GT_Recipe cloneRecipe;
	
	@Override
	public boolean checkRecipe(final ItemStack aStack) {

		Utils.LOG_WARNING("1");
		
		//Get inputs.
		final ArrayList<ItemStack> tInputList = this.getStoredInputs();
		for (int i = 0; i < (tInputList.size() - 1); i++) {
			for (int j = i + 1; j < tInputList.size(); j++) {
				if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
					if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
						tInputList.remove(j--);
					} else {
						tInputList.remove(i--);
						break;
					}
				}
			}
		}
		
		Utils.LOG_WARNING("2");

		//Temp var
		final ItemStack[] tInputs = Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, 2);

		//Don't check the recipe if someone got around the output bus size check.
		if (this.mOutputBusses.size() != 4){
			return false;
		}
		
		
		Utils.LOG_WARNING("3");

		//Make a recipe instance for the rest of the method.
		final GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sSifterRecipes.findRecipe(this.getBaseMetaTileEntity(), false, 9223372036854775807L, null, tInputs);
		
		baseRecipe = tRecipe.copy();
		
		if (cloneRecipe != tRecipe || cloneRecipe == null){
			cloneRecipe = tRecipe.copy();
			Utils.LOG_WARNING("Setting Recipe");
		}		
		if (mInputStacks != tRecipe.mInputs || mInputStacks == null){
			mInputStacks = tRecipe.mInputs;
			Utils.LOG_WARNING("Setting Recipe Inputs");
		}
		if (cloneChances != tRecipe.mChances || cloneChances == null){
			cloneChances = tRecipe.mChances.clone();
			Utils.LOG_WARNING("Setting Chances");
		}
		
		for (int r=0;r<cloneChances.length;r++){
			Utils.LOG_WARNING("Original map Output["+r+"] chance = "+cloneChances[r]);
		}
		
		Utils.LOG_WARNING("3.1");
		
		//Change bonus chances
		int[] outputChances;
		
		Utils.LOG_WARNING("3.2");
		
		if (cloneRecipe.mChances != null){
			outputChances = cloneRecipe.mChances.clone();
			
			Utils.LOG_WARNING("3.3");
			
			for (int r=0;r<outputChances.length;r++){
				Utils.LOG_WARNING("Output["+r+"] chance = "+outputChances[r]);
				if (outputChances[r]<10000){
					int temp = outputChances[r];
					if (outputChances[r] < 8000 && outputChances[r] >= 1){
						outputChances[r] = temp+1200;
						Utils.LOG_WARNING("Output["+r+"] chance now = "+outputChances[r]);
					}
					else if (outputChances[r] < 9000 && outputChances[r] >= 8000){
						outputChances[r] = temp+400;
						Utils.LOG_WARNING("Output["+r+"] chance now = "+outputChances[r]);
					}
					else if (outputChances[r] <= 9900 && outputChances[r] >= 9000){
						outputChances[r] = temp+100;
						Utils.LOG_WARNING("Output["+r+"] chance now = "+outputChances[r]);
					}
				}
			}

			Utils.LOG_WARNING("3.4");
			
			//Rebuff Drop Rates for % output
			cloneRecipe.mChances = outputChances;
			
		}
		
		
		Utils.LOG_WARNING("4");
	
		
		final int tValidOutputSlots = this.getValidOutputSlots(this.getBaseMetaTileEntity(), cloneRecipe, tInputs);
		Utils.LOG_WARNING("Sifter - Valid Output Hatches: "+tValidOutputSlots);

		//More than or one input
		if ((tInputList.size() > 0) && (tValidOutputSlots >= 1)) {
			if ((cloneRecipe != null) && (cloneRecipe.isRecipeInputEqual(true, null, tInputs))) {
				Utils.LOG_WARNING("Valid Recipe found - size "+cloneRecipe.mOutputs.length);
				this.mEfficiency = (10000 - ((this.getIdealStatus() - this.getRepairStatus()) * 1000));
				this.mEfficiencyIncrease = 10000;


				this.mEUt = (-cloneRecipe.mEUt);
				this.mMaxProgresstime = Math.max(1, (cloneRecipe.mDuration/5));
				final ItemStack[] outputs = new ItemStack[cloneRecipe.mOutputs.length];
				for (int i = 0; i < cloneRecipe.mOutputs.length; i++){
					if (i==0) {
						Utils.LOG_WARNING("Adding the default output");
						outputs[0] =  cloneRecipe.getOutput(i);
					}
					else if (this.getBaseMetaTileEntity().getRandomNumber(7500) < cloneRecipe.getOutputChance(i)){
						Utils.LOG_WARNING("Adding a bonus output");
						outputs[i] = cloneRecipe.getOutput(i);
					}
					else {
						Utils.LOG_WARNING("Adding null output");
						outputs[i] = null;
					}
				}

				this.mOutputItems = outputs;
				this.sendLoopStart((byte) 20);
				this.updateSlots();
				//tRecipe.mChances = baseRecipe.mChances;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		Utils.LOG_MACHINE_INFO("Checking structure for Industrial Sifter.");
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;
		
		/*if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
			Utils.LOG_MACHINE_INFO("Don't know why this exists?");
			return false;
		}*/
		
		int tAmount = 0;
		controller = false;
		for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				for (int h = 0; h < 3; h++) {
					
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);

					// Sifter Floor/Roof inner 5x5
					if (((i != -2) && (i != 2)) && ((j != -2) && (j != 2))) {

						// Sifter Floor & Roof (Inner 5x5) + Mufflers, Dynamos and Fluid outputs.
						if ((h == 0) || (h == 2 || h == 1)) {
							
							if (h == 2 || h == 1){						
							//If not a hatch, continue, else add hatch and continue.
							if ((!this.addMufflerToMachineList(tTileEntity, 78)) && (!this.addOutputToMachineList(tTileEntity, 78)) && (!this.addDynamoToMachineList(tTileEntity, 78))) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
									Utils.LOG_MACHINE_INFO("Sifter Casing(s) Missing from one of the top layers inner 3x3.");
									Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 6) {
									Utils.LOG_MACHINE_INFO("Sifter Casing(s) Missing from one of the top layers inner 3x3. Wrong Meta for Casing. Found:"+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName()+" with meta:"+aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j));
									return false;
								}
							}
							}
							else {
								if ((!this.addMufflerToMachineList(tTileEntity, 78)) && (!this.addOutputToMachineList(tTileEntity, 78)) && (!this.addDynamoToMachineList(tTileEntity, 78))) {
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
										Utils.LOG_MACHINE_INFO("Sifter Casing(s) Missing from one of the bottom layers inner 3x3.");
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 5) {
										Utils.LOG_MACHINE_INFO("Sifter Casing(s) Missing from one of the bottom layers inner 3x3. Wrong Meta for Casing. Found:"+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName()+" with meta:"+aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j));
										return false;
									}
									tAmount++;
								}
							}
						}

						// Inside 2 layers, mostly air
						/*else {
							// Sifter Inner 5x5
							//if ((i != -1 && i != 1) && (j != -1 && j != 1)) {
							if (!aBaseMetaTileEntity.getAirOffset(xDir + i, h, zDir + j)) {
								Utils.LOG_MACHINE_INFO("Make sure the inner 3x3 of the Multiblock is Air.");
								return false;
							}

						}*/
					}

					//Dealt with inner 5x5, now deal with the exterior.
					else {

						//Deal with all 4 sides (Sifter walls)
						if ((h == 1) || (h == 2)) {
							if (h == 2){
								if (!this.addToMachineList(tTileEntity, 78)) {
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
										Utils.LOG_MACHINE_INFO("Sifter Casings Missing from somewhere in the top layer edge.");
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 5) {
										Utils.LOG_MACHINE_INFO("Sifter Casings Missing from somewhere in the top layer edge.");
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									tAmount++;
								}
							}
							else {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
									Utils.LOG_MACHINE_INFO("Sifter Casings Missing from somewhere in the second layer.");
									Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 5) {
									Utils.LOG_MACHINE_INFO("Sifter Casings Missing from somewhere in the second layer.");
									Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									return false;
								}
								tAmount++;
							}
						}

						//Deal with top and Bottom edges (Inner 5x5)
						else if ((h == 0) || (h == 2)) {
							if ((!this.addToMachineList(tTileEntity, 78)) && (!this.addInputToMachineList(tTileEntity, 78)) && (!this.addOutputToMachineList(tTileEntity, 78)) && (!this.addDynamoToMachineList(tTileEntity, 78))) {
								if (((xDir + i) != 0) || ((zDir + j) != 0)) {//no controller

									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
										Utils.LOG_MACHINE_INFO("Sifter Casing(s) Missing from one of the edges on the top layer.");
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 5) {
										Utils.LOG_MACHINE_INFO("Sifter Casing(s) Missing from one of the edges on the top layer. "+h);
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										if (h ==0){
											if (tTileEntity instanceof GregtechMetaTileEntity_IndustrialSifter){

											}
										}
										else {
											return false;
										}
									}
								}
							}
						}
					}
					
				}
			}
		}
		if ((this.mInputBusses.size() != 1) || (this.mOutputBusses.size() != 4)
				|| (this.mMaintenanceHatches.size() != 1) || (this.mEnergyHatches.size() != 1)) {
			Utils.LOG_MACHINE_INFO("Returned False 3");
			Utils.LOG_MACHINE_INFO("Input Buses: "+this.mInputBusses.size()+" | expected: 1");
			Utils.LOG_MACHINE_INFO("Output Buses: "+this.mOutputBusses.size()+" | expected: 4");
			Utils.LOG_MACHINE_INFO("Energy Hatches: "+this.mEnergyHatches.size()+" | expected: 1");
			Utils.LOG_MACHINE_INFO("Maint. hatches: "+this.mMaintenanceHatches.size()+" | expected: 1");
			return false;
		}
		final int height = this.getBaseMetaTileEntity().getYCoord();
		
		final GT_MetaTileEntity_Hatch_OutputBus[] tmpHatches = new GT_MetaTileEntity_Hatch_OutputBus[4];
		for (int i = 0; i < this.mOutputBusses.size(); i++) {
			final int hatchNumber = this.mOutputBusses.get(i).getBaseMetaTileEntity().getYCoord() - 1 - height;
			if (tmpHatches[i] == null) {
				tmpHatches[i] = this.mOutputBusses.get(i);
			} else {
				Utils.LOG_MACHINE_INFO("Returned False 5 - "+this.mOutputBusses.size());
				return false;
			}
		}
		this.mOutputBusses.clear();
		for (int i = 0; i < tmpHatches.length; i++) {
			this.mOutputBusses.add(tmpHatches[i]);
		}
		
		Utils.LOG_INFO("Structure Built? "+(tAmount>35));
		
		return tAmount >= 35;
	}

	public boolean ignoreController(final Block tTileEntity) {
		if (!controller && (tTileEntity == GregTech_API.sBlockMachines)) {
			return true;
		}
		return false;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 16;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean isOverclockerUpgradable() {
		return true;
	}
}