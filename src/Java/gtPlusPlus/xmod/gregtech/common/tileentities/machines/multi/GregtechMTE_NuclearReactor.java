package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

public class GregtechMTE_NuclearReactor extends GT_MetaTileEntity_MultiBlockBase {

	public static int sUUAperUUM = 1;
	public static int sUUASpeedBonus = 4;
	public static int sDurationMultiplier = 3215;
	public static boolean sRequiresUUA = false;
	private int recipeCounter = 0;
	private static Block IC2Glass = Block.getBlockFromItem(UtilsItems.getItem("IC2:blockAlloyGlass"));
	//public FluidStack mFluidOut = Materials.UUMatter.getFluid(1L);

	public GregtechMTE_NuclearReactor(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMTE_NuclearReactor(String aName) {
		super(aName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Nuclear Reactor",
				"Produces heat from Radioactive beta decay.",
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
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[66]};
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "MatterFabricator.png");
	}

	@Override
	public void onConfigLoad(GT_Config aConfig) {
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
						recipeCounter++;
						updateSlots();
						//Utils.LOG_INFO("Recipes Finished: "+recipeCounter);
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
					
					if ((i != -2 && i != 2) && (j != -2 && j != 2)) {// innerer 3x3 ohne h�he
						if (h == 0) {// inner floor 3x3
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Utils.LOG_INFO("Coils missings from the bottom layer, inner 3x3.");
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
								Utils.LOG_INFO("Coils missings from the bottom layer, inner 3x3.");
								return false;
							}
						} else if (h == 3) {//Roofing blocks 3x3 (casings + input + muffler)
							if ((!addMufflerToMachineList(tTileEntity, 66))) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO("Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
									Utils.LOG_INFO("Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
							}
						} else {// Inner Air section, may require blocks here.
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
	public boolean isCorrectMachinePart(ItemStack aStack) {
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
	public int getDamageToComponent(ItemStack aStack) {
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
		return new GregtechMTE_NuclearReactor(this.mName);
	}

}