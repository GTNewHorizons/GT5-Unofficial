package miscutil.core.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

import java.util.ArrayList;
import java.util.Arrays;

import miscutil.core.block.ModBlocks;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.xmod.gregtech.api.util.GregtechRecipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

public class GregtechMetaTileEntityMassFabricator extends GT_MetaTileEntity_MultiBlockBase {

	public static int sUUAperUUM = 1;
	public static int sUUASpeedBonus = 4;
	public static int sDurationMultiplier = 3215;
	public static boolean sRequiresUUA = false;
	private int recipeCounter = 0;
	private static Block IC2Glass = Block.getBlockFromItem(UtilsItems.getItem("IC2:blockAlloyGlass"));
	//public FluidStack mFluidOut = Materials.UUMatter.getFluid(1L);

	public GregtechMetaTileEntityMassFabricator(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntityMassFabricator(String aName) {
		super(aName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Pyrolyse Oven",
				"Industrial Charcoal producer and Oil from Plants",
				"Size(WxHxD): 5x4x5, Controller (Bottom center)",
				"3x1x3 Kanthal Heating Coils (Inside bottom 5x1x5 layer)",
				"9x Kanthal Heating Coils (Centered 3x1x3 area in Bottom layer)",
				"1x Input Hatch/Bus (Centered 3x1x3 area in Top layer)",
				"1x Output Hatch/Bus (Any bottom layer casing)",
				"1x Maintenance Hatch (Any bottom layer casing)",
				"1x Muffler Hatch (Centered 3x1x3 area in Top layer)",
				"1x Energy Hatch (Any bottom layer casing)",
		"ULV Machine Casings for the rest (60 at least!)"};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[66],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[66]};
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "VacuumFreezer.png");
	}

	@Override
	public void onConfigLoad(GT_Config aConfig) {
		super.onConfigLoad(aConfig);
		sDurationMultiplier = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUM_Duration_Multiplier", sDurationMultiplier);
		sUUAperUUM = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_per_UUM", sUUAperUUM);
		sUUASpeedBonus = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_Speed_Bonus", sUUASpeedBonus);
		sRequiresUUA = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_Requirement", sRequiresUUA);
		Materials.UUAmplifier.mChemicalFormula = ("Mass Fabricator Eff/Speed Bonus: x" + sUUASpeedBonus);
	}

	/*@Override
	public boolean checkRecipe(ItemStack aStack) {

		ArrayList<FluidStack> tFluidList = getStoredFluids();
		Utils.LOG_INFO("Stored Fluid Amount: "+tFluidList.size());
		for (int i = 0; i < tFluidList.size(); i++) {
			for (int j = i + 1; j < tFluidList.size(); j++) {
				if (GT_Utility.areFluidsEqual((FluidStack) tFluidList.get(i), (FluidStack) tFluidList.get(j))) {
					if (((FluidStack) tFluidList.get(i)).amount >= ((FluidStack) tFluidList.get(j)).amount) {
						tFluidList.remove(j--);
					} else {
						tFluidList.remove(i--);
						break;
					}
				}
			}
		}

		for (FluidStack temp : tFluidList) {
			Utils.LOG_INFO("Stored Fluid is: "+temp.getLocalizedName()+", There is "+temp.amount+"L of it.");
		}

		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		FluidStack tFluids = tFluidList.get(0);
        FluidStack[] tFluids1 = (FluidStack[]) Arrays.copyOfRange(tFluidList.toArray(new FluidStack[tFluidList.size()]), 0, tFluidList.size());
		if (tFluids.amount > 0) {
			GT_Recipe tRecipe = GregtechRecipe.Gregtech_Recipe_Map.sMatterFab2Recipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], new FluidStack[]{tFluids}, new ItemStack[]{});
			if (tRecipe != null) {

				for (FluidStack temp : tRecipe.mFluidOutputs) {
					Utils.LOG_INFO("Variable Output Fluid is: "+temp.getLocalizedName()+", There is "+temp.amount+"L of it.");
				}

				this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
				this.mEfficiencyIncrease = 10000;
				if (tRecipe.mEUt <= 16) {
					this.mEUt = (tRecipe.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
					this.mMaxProgresstime = (tRecipe.mDuration / (1 << tTier - 1));
				} else {
					this.mEUt = tRecipe.mEUt;
					this.mMaxProgresstime = tRecipe.mDuration;
					while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
						this.mEUt *= 4;
						this.mMaxProgresstime /= 2;
					}
				}
				if (this.mEUt > 0) {
					this.mEUt = (-this.mEUt);
				}
				this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
				this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0)};
				this.mOutputFluids = tRecipe.mFluidOutputs.clone();
				ArrayUtils.reverse(mOutputFluids);
				updateSlots();
				Utils.LOG_INFO("Good Recipe");
				return true;
			}
			
			if (tFluids1.length > 0) {
	        	for(int i = 0;i<tFluids1.length;i++){
	            GT_Recipe tRecipe1 = GT_Recipe.GT_Recipe_Map.sDistillationRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], new FluidStack[]{tFluids1[i]}, new ItemStack[]{});
				
			if (tRecipe1 != null) {

				for (FluidStack temp : tRecipe1.mFluidOutputs) {
					Utils.LOG_INFO("Variable Output Fluid is: "+temp.getLocalizedName()+", There is "+temp.amount+"L of it.");
				}

				this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
				this.mEfficiencyIncrease = 10000;
				if (tRecipe1.mEUt <= 16) {
					this.mEUt = (tRecipe1.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
					this.mMaxProgresstime = (tRecipe1.mDuration / (1 << tTier - 1));
				} else {
					this.mEUt = tRecipe1.mEUt;
					this.mMaxProgresstime = tRecipe1.mDuration;
					while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
						this.mEUt *= 4;
						this.mMaxProgresstime /= 2;
					}
				}
				if (this.mEUt > 0) {
					this.mEUt = (-this.mEUt);
				}
				this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
				this.mOutputItems = new ItemStack[]{tRecipe1.getOutput(0)};
				this.mOutputFluids = tRecipe1.mFluidOutputs.clone();
				ArrayUtils.reverse(mOutputFluids);
				updateSlots();
				Utils.LOG_INFO("Good Recipe");
				return true;
			}
	        	}
			}
			Utils.LOG_INFO("Bad Recipe");
		}
		if (tFluids.amount <= 0) {
			Utils.LOG_INFO("Bad Fluid amount");
		}
		return false;
	}*/
	
	@Override
	public boolean checkRecipe(ItemStack aStack) {
        ArrayList<FluidStack> tFluidList = getStoredFluids();
        for (int i = 0; i < tFluidList.size() - 1; i++) {
            for (int j = i + 1; j < tFluidList.size(); j++) {
                if (GT_Utility.areFluidsEqual((FluidStack) tFluidList.get(i), (FluidStack) tFluidList.get(j))) {
                    if (((FluidStack) tFluidList.get(i)).amount >= ((FluidStack) tFluidList.get(j)).amount) {
                        tFluidList.remove(j--);
                    } else {
                        tFluidList.remove(i--);
                        break;
                    }
                }
            }
        }

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        FluidStack[] tFluids = (FluidStack[]) Arrays.copyOfRange(tFluidList.toArray(new FluidStack[tFluidList.size()]), 0, tFluidList.size());
        if (tFluids.length > 0) {
        	for(int i = 0;i<tFluids.length;i++){
            GT_Recipe tRecipe = GregtechRecipe.Gregtech_Recipe_Map.sMatterFab2Recipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], new FluidStack[]{tFluids[i]}, new ItemStack[]{});
            if (tRecipe != null) {
                if (tRecipe.isRecipeInputEqual(true, tFluids, new ItemStack[]{})) {
                    this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;
                    if (tRecipe.mEUt <= 16) {
                        this.mEUt = (tRecipe.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
                        this.mMaxProgresstime = (tRecipe.mDuration / (1 << tTier - 1));
                    } else {
                        this.mEUt = tRecipe.mEUt;
                        this.mMaxProgresstime = tRecipe.mDuration;
                        while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
                            this.mEUt *= 4;
                            this.mMaxProgresstime /= 2;
                        }
                    }
                    if (this.mEUt > 0) {
                        this.mEUt = (-this.mEUt);
                    }
                    this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
                    this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0)};
                    this.mOutputFluids = tRecipe.mFluidOutputs.clone();
                    ArrayUtils.reverse(mOutputFluids);
                    recipeCounter++;
                    updateSlots();
                    Utils.LOG_INFO("Recipes Finished: "+recipeCounter);
                    return true;
                	}
                }
            else {
                Utils.LOG_INFO("Invalid Recipe");
            }
            }
        }
        return false;
    }

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;
		for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				for (int h = 0; h < 4; h++) {
					IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
					/*if (tTileEntity == Blocks.air) {
						Utils.LOG_INFO("h:"+h+" i:"+i+" j:"+j);
						double tX = tTileEntity.getXCoord();
						double tY = tTileEntity.getYCoord();
						double tZ = tTileEntity.getZCoord();
						Utils.LOG_INFO("Found Air at X:"+tX+" Y:"+tY+" Z:"+tZ);
						//return false;
					}*/
					if ((i != -2 && i != 2) && (j != -2 && j != 2)) {// innerer 3x3 ohne h�he
						if (h == 0) {// innen boden (kantal coils)
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Utils.LOG_INFO("Multiblock Invalid.");
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
								Utils.LOG_INFO("Multiblock Invalid.");
								return false;
							}
						} else if (h == 3) {// innen decke (ulv casings + input + muffler)
							if ((!addMufflerToMachineList(tTileEntity, 66))) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO("Multiblock Invalid.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
									Utils.LOG_INFO("Multiblock Invalid.");
									return false;
								}
							}
						} else {// innen air
							if (!aBaseMetaTileEntity.getAirOffset(xDir + i, h, zDir + j)) {
								Utils.LOG_INFO("Multiblock Invalid.");
								return false;
							}
						}
					} else {// Outer 5x5
						if (h == 0) {// au�en boden (controller, output, energy, maintainance, rest ulv casings)
							if ((!addMaintenanceToMachineList(tTileEntity, 66)) && (!addInputToMachineList(tTileEntity, 66)) && (!addOutputToMachineList(tTileEntity, 66)) && (!addEnergyInputToMachineList(tTileEntity, 66))) {
								if ((xDir + i != 0) || (zDir + j != 0)) {//no controller
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Utils.LOG_INFO("Multiblock Invalid.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
										Utils.LOG_INFO("Multiblock Invalid.");
										return false;
									}
								}
							}
						} else {// au�en �ber boden (ulv casings)

							/*//Utils.LOG_INFO("h:"+h+" i:"+i+" j:"+j);
							if (h == 1  || h == 2) {
								//Utils.LOG_INFO("h:"+h+" i:"+i+" j:"+j);
								//if ((i < 2 && i > -2) && (j == 0) || (i == 0) && (j < 2 && j > -2)){
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != IC2Glass) {
									double tX = tTileEntity.getXCoord();
									double tY = tTileEntity.getYCoord();
									double tZ = tTileEntity.getZCoord();
									Utils.LOG_INFO("Found Not Glass at X:"+tX+" Y:"+tY+" Z:"+tZ);
									Utils.LOG_INFO("Multiblock Invalid. No Glass.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 0) {
									double tX = tTileEntity.getXCoord();
									double tY = tTileEntity.getYCoord();
									double tZ = tTileEntity.getZCoord();
									Utils.LOG_INFO("Found Not Glass at X:"+tX+" Y:"+tY+" Z:"+tZ);
									Utils.LOG_INFO("Multiblock Invalid.  No Glass.");
									return false;
								}  
								//}
								//Utils.LOG_INFO("h:"+h+" i:"+i+" j:"+j);
								//if ((i == 2 || i == -2) && (j == 0) || (i == 0) && (j == 2 && j == -2)){
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO("Multiblock Invalid. No Corner Blocks.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
									Utils.LOG_INFO("Multiblock Invalid.  No Corner Blocks.");
									return false;
								}    
								//}
							} */                       	

							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Utils.LOG_INFO("Multiblock Invalid. 1");
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
								Utils.LOG_INFO("Multiblock Invalid. 1");
								return false;
							}
						}
					}
				}
			}
		}
		Utils.LOG_INFO("Multiblock Formed.");
		return true;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 20;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityMassFabricator(this.mName);
	}

}