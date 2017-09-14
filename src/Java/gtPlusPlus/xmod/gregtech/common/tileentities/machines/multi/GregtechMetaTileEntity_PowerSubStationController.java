package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_PowerSubStationController extends GT_MetaTileEntity_MultiBlockBase {

	private static boolean controller;
	
	public GregtechMetaTileEntity_PowerSubStationController(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_PowerSubStationController(final String aName) {
		super(aName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Power Sub-Station",
				"Stores quite a lot of power.",
				"Size(WxHxD): 5x3x5, Controller (One above the Bottom)",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(24)],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER_ACTIVE : Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(24)]};
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MatterFabricator.png");
	}

	@Override
	public void onConfigLoad(final GT_Config aConfig) {
		super.onConfigLoad(aConfig);
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		Utils.LOG_MACHINE_INFO("Checking structure for Industrial Power Sub-Station.");
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
							if ((!this.addMufflerToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(24)))) {
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
								if ((!this.addMufflerToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(24)))) {
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
					}

					//Dealt with inner 5x5, now deal with the exterior.
					else {

						//Deal with all 4 sides (Sifter walls)
						if ((h == 1) || (h == 2)) {
							if (h == 2){
								if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
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
							if ((!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) && (!this.addInputToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(24)))) {
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
		if ((this.mInputBusses.size() != 1) || (this.mOutputBusses.size() != 1)
				|| (this.mMaintenanceHatches.size() != 1) || (this.mEnergyHatches.size() >= 1)
				|| (this.mDynamoHatches.size() >= 1)) {
			Utils.LOG_MACHINE_INFO("Returned False 3");
			Utils.LOG_MACHINE_INFO("Input Buses: "+this.mInputBusses.size()+" | expected: 1");
			Utils.LOG_MACHINE_INFO("Output Buses: "+this.mOutputBusses.size()+" | expected: 1");
			Utils.LOG_MACHINE_INFO("Energy Hatches: "+this.mEnergyHatches.size()+" | expected: >= 1");
			Utils.LOG_MACHINE_INFO("Dynamo Hatches: "+this.mDynamoHatches.size()+" | expected: >= 1");
			Utils.LOG_MACHINE_INFO("Maint. Hatches: "+this.mMaintenanceHatches.size()+" | expected: 1");
			return false;
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
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
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
	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_PowerSubStationController(this.mName);
	}

}