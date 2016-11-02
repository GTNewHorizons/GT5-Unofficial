package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MatterFab;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

public class GregtechMetaTileEntity_MassFabricator extends GregtechMeta_MultiBlockBase {

	public static int sUUAperUUM = 1;
	public static int sUUASpeedBonus = 4;
	public static int sDurationMultiplier = 3215;
	public static boolean sRequiresUUA = false;
	private int mAmplifierUsed = 0;
	private int mMatterProduced = 0;
	private static Block IC2Glass = Block.getBlockFromItem(ItemUtils.getItem("IC2:blockAlloyGlass"));
	FluidStack tempFake = FluidUtils.getFluidStack("uuamplifier", 1);
	GT_Recipe fakeRecipe;
	
	public int getAmplifierUsed(){
		return mAmplifierUsed;
	}
	
	public int getMatterProduced(){
		return mMatterProduced;
	}

	public GregtechMetaTileEntity_MassFabricator(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_MassFabricator(String aName) {
		super(aName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Matter Fabricator",
				"Produces UU-Matter from UU-Amplifier",
				"Size(WxHxD): 5x4x5, Controller (Bottom center)",
				"3x1x3 Matter Generation Coils (Inside bottom 5x1x5 layer)",
				"9x Matter Generation Coils (Centered 3x1x3 area in Bottom layer)",
				"1x Input Hatch (Any bottom layer casing)",
				"1x Output Hatch (Any bottom layer casing)",
				"1x Maintenance Hatch (Any bottom layer casing)",
				"1x Muffler Hatch (Centered 3x1x3 area in Top layer)",
				"1x Energy Hatch (Any bottom layer casing)",				
				"24x IC2 Reinforced Glass for the walls",
				"Matter Fabricator Casings for the edges & top (40 at least!)",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[66],
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Casing_Machine_Screen_3 : TexturesGtBlock.Casing_Machine_Screen_1)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[66]};
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MatterFab(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "MatterFabricator.png");
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
			//Utils.LOG_INFO("Input fluid found");
			for(int i = 0;i<tFluids.length;i++){
				GT_Recipe tRecipe = Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], new FluidStack[]{tFluids[i]}, new ItemStack[]{});
				if (tRecipe != null) {
					if (tRecipe.isRecipeInputEqual(true, tFluids, new ItemStack[]{})) {
						this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
						this.mEfficiencyIncrease = 10000;
						if (tRecipe.mEUt <= 16) {
							this.mEUt = (tRecipe.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
							this.mMaxProgresstime = ((tRecipe.mDuration/**sDurationMultiplier*/) / (1 << tTier - 1));
						} else {
							this.mEUt = tRecipe.mEUt;
							this.mMaxProgresstime = (tRecipe.mDuration/**sDurationMultiplier*/);
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
						mMatterProduced++;
						mAmplifierUsed++;
						updateSlots();
						//Utils.LOG_INFO("Recipes Finished: "+mMatterProduced);
						return true;
					}
				}
				else {
					//Utils.LOG_INFO("Invalid Recipe");
					return false;
				}
			}
		}
		else if (tFluids.length == 0) {
			//Utils.LOG_INFO("Input fluid not found");
			fakeRecipe = Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], new FluidStack[]{tempFake}, new ItemStack[]{});
			
			this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
			this.mEfficiencyIncrease = 10000;

			this.mEUt = 32;
			this.mMaxProgresstime = (160*20);
			while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
				this.mEUt *= 4;
				this.mMaxProgresstime /= 2;
			}

			if (this.mEUt > 0) {
				this.mEUt = (-this.mEUt);
			}
			
			if (fakeRecipe != null) {
				this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
				this.mOutputItems = new ItemStack[]{fakeRecipe.getOutput(0)};
				this.mOutputFluids = fakeRecipe.mFluidOutputs.clone();
				ArrayUtils.reverse(mOutputFluids);
				mMatterProduced++;
				updateSlots();
				//Utils.LOG_INFO("Recipes Finished: "+mMatterProduced);
				return true;
			}
		}
		else {
			//Utils.LOG_INFO("Invalid no input Recipe");
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

					//Utils.LOG_INFO("Logging Variables - xDir:"+xDir+" zDir:"+zDir+" h:"+h+" i:"+i+" j:"+j);

					IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
					/*if (tTileEntity != Block.getBlockFromItem(UtilsItems.getItem("IC2:blockAlloyGlass"))) {
						Utils.LOG_INFO("h:"+h+" i:"+i+" j:"+j);
						double tX = tTileEntity.getXCoord();
						double tY = tTileEntity.getYCoord();
						double tZ = tTileEntity.getZCoord();
						Utils.LOG_INFO("Found Glass at X:"+tX+" Y:"+tY+" Z:"+tZ);
						//return false;
					}*/
					if ((i != -2 && i != 2) && (j != -2 && j != 2)) {// innerer 3x3 ohne h�he
						if (h == 0) {// innen boden (kantal coils)
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Utils.LOG_INFO("Matter Generation Coils missings from the bottom layer, inner 3x3.");
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
								Utils.LOG_INFO("Matter Generation Coils missings from the bottom layer, inner 3x3.");
								return false;
							}
						} else if (h == 3) {// innen decke (ulv casings + input + muffler)
							if ((!addMufflerToMachineList(tTileEntity, 66))) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
							}
						} else {// innen air
							if (!aBaseMetaTileEntity.getAirOffset(xDir + i, h, zDir + j)) {
								Utils.LOG_INFO("Make sure the inner 3x3 of the Multiblock is Air.");
								return false;
							}
						}
					} else {// Outer 5x5
						if (h == 0) {// au�en boden (controller, output, energy, maintainance, rest ulv casings)
							if ((!addMaintenanceToMachineList(tTileEntity, 66)) && (!addInputToMachineList(tTileEntity, 66)) && (!addOutputToMachineList(tTileEntity, 66)) && (!addEnergyInputToMachineList(tTileEntity, 66))) {
								if ((xDir + i != 0) || (zDir + j != 0)) {//no controller
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the edges of the bottom layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
										Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the edges of the bottom layer.");
										return false;
									}
								}
							}
						} else {// au�en �ber boden (ulv casings)
							if (h == 1) {

								if ((i == -2 || i == 2) && (j == -2 || j == 2)){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the corners in the second layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
										Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the corners in the second layer.");
										return false;
									}
								}

								else if ((i != -2 || i != 2) && (j != -2 || j != 2)){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != IC2Glass) {
										Utils.LOG_INFO("Glass Casings Missing from somewhere in the second layer.");
										return false;
									}
								}
							}
							if (h == 2) {
								if ((i == -2 || i == 2) && (j == -2 || j == 2)){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the corners in the third layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
										Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the corners in the third layer.");
										return false;
									}
								}

								else if ((i != -2 || i != 2) && (j != -2 || j != 2)){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != IC2Glass) {
										Utils.LOG_INFO("Glass Casings Missing from somewhere in the third layer.");
										return false;
									}
								}
							}
							if (h == 3) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the edges on the top layer.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the edges on the top layer.");
									return false;
								}
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
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_MassFabricator(this.mName);
	}

}