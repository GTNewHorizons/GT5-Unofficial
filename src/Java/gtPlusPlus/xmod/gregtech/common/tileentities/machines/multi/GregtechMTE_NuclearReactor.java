package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMTE_NuclearReactor extends GT_MetaTileEntity_MultiBlockBase {

	public static int		sUUAperUUM			= 1;
	public static int		sUUASpeedBonus		= 4;
	public static int		sDurationMultiplier	= 3215;
	public static boolean	sRequiresUUA		= false;
	private static Block	IC2Glass			= Block.getBlockFromItem(ItemUtils.getItem("IC2:blockAlloyGlass"));
	// public FluidStack mFluidOut = Materials.UUMatter.getFluid(1L);
	private int				recipeCounter		= 0;

	public GregtechMTE_NuclearReactor(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMTE_NuclearReactor(final String aName) {
		super(aName);
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;
		for (int i = -3; i < 3; i++) {
			for (int j = -3; j < 4; j++) {
				for (int h = 0; h < 3; h++) {
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i,
							h, zDir + j);

					if (i != -3 && i != 3 && j != -3 && j != 3) {// Reactor
																	// Floor/Roof
																	// inner 7x7
						if (h == 0 || h == 4) {// Reactor Floor & Roof (Inner
												// 7x7) + muffler x4
							if (!this.addMufflerToMachineList(tTileEntity, 66)) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h,
										zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO(
											"Reactor Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
									Utils.LOG_INFO(
											"Reactor Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
							}
						}
						else {// Inside 6 layers, mostly air
							if (i != -1 && i != 1 && j != -1 && j != 1) {// Reactor
																			// Floor/Roof
																			// inner
																			// 5x5
								if (!aBaseMetaTileEntity.getAirOffset(xDir + i, h, zDir + j)) {
									Utils.LOG_INFO("Make sure the inner 3x3 of the Multiblock is Air.");
									return false;
								}
							}
							else { // carbon moderation rods are at 1,1 & -1,-1
									// & 1,-1 & -1,1
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h,
										zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO(
											"Reactor Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
									Utils.LOG_INFO(
											"Reactor Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
							}
						}
					} // End Inner Workings

					else {// Reactor Exterior
						if (h == 1) {
							if ((i == -3 || i == 3) && (j == -3 || j == 3)) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h,
										zDir + j) != ModBlocks.blockCasingsMisc) {
									Block temp = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
									Utils.LOG_INFO(
											"Reactor Casings Missing from one of the corners in the second layer. Block: "+temp.getLocalizedName()
											+" x:"+(aBaseMetaTileEntity.getXCoord()+xDir+i)
											+" y:"+(aBaseMetaTileEntity.getYCoord()+h)
											+" z:"+(aBaseMetaTileEntity.getZCoord()+zDir+j)
											);
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
									Utils.LOG_INFO(
											"Reactor Casings Missing from one of the corners in the second layer. Meta");
									return false;
								}
							}

							else if ((i != -3 || i != 3) && (j != -3 || j != 3)) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h,
										zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO("Glass Casings Missing from somewhere in the second layer.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 13) {
									Utils.LOG_INFO("Glass Casings Missing from somewhere in the second layer.");
									return false;
								}
							}
						}
						if (h == 2) {
							if ((i == -3 || i == 3) && (j == -3 || j == 3)) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h,
										zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO(
											"Reactor Casings Missing from one of the corners in the third layer.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
									Utils.LOG_INFO(
											"Reactor Casings Missing from one of the corners in the third layer.");
									return false;
								}
							}

							else if ((i != -3 || i != 3) && (j != -3 || j != 3)) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h,
										zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO("Glass Casings Missing from somewhere in the second layer.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 13) {
									Utils.LOG_INFO("Glass Casings Missing from somewhere in the second layer.");
									return false;
								}
							}
						}
						if (h == 3) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h,
									zDir + j) != ModBlocks.blockCasingsMisc) {
								Utils.LOG_INFO(
										"Reactor Casings Missing from one of the edges on the top layer.");
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
								Utils.LOG_INFO(
										"Reactor Casings Missing from one of the edges on the top layer.");
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
	public boolean checkRecipe(final ItemStack aStack) {
		final ArrayList<FluidStack> tFluidList = this.getStoredFluids();
		for (int i = 0; i < tFluidList.size() - 1; i++) {
			for (int j = i + 1; j < tFluidList.size(); j++) {
				if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
					if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
						tFluidList.remove(j--);
					}
					else {
						tFluidList.remove(i--);
						break;
					}
				}
			}
		}

		final long tVoltage = this.getMaxInputVoltage();
		final byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		final FluidStack[] tFluids = Arrays.copyOfRange(tFluidList.toArray(new FluidStack[tFluidList.size()]), 0,
				tFluidList.size());
		if (tFluids.length > 0) {
			for (int i = 0; i < tFluids.length; i++) {
				final GT_Recipe tRecipe = Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.findRecipe(
						this.getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], new FluidStack[] {
								tFluids[i]
						}, new ItemStack[] {});
				if (tRecipe != null) {
					if (tRecipe.isRecipeInputEqual(true, tFluids, new ItemStack[] {})) {
						this.mEfficiency = 10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000;
						this.mEfficiencyIncrease = 10000;
						if (tRecipe.mEUt <= 16) {
							this.mEUt = tRecipe.mEUt * (1 << tTier - 1) * (1 << tTier - 1);
							this.mMaxProgresstime = tRecipe.mDuration / (1 << tTier - 1);
						}
						else {
							this.mEUt = tRecipe.mEUt;
							this.mMaxProgresstime = tRecipe.mDuration;
							while (this.mEUt <= gregtech.api.enums.GT_Values.V[tTier - 1]) {
								this.mEUt *= 4;
								this.mMaxProgresstime /= 2;
							}
						}
						if (this.mEUt > 0) {
							this.mEUt = -this.mEUt;
						}
						this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
						this.mOutputItems = new ItemStack[] {
								tRecipe.getOutput(0)
						};
						this.mOutputFluids = tRecipe.mFluidOutputs.clone();
						ArrayUtils.reverse(this.mOutputFluids);
						this.recipeCounter++;
						this.updateSlots();
						// Utils.LOG_INFO("Recipes Finished: "+recipeCounter);
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
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MatterFabricator.png");
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Controller Block for the Nuclear Reactor", "Produces heat from Radioactive beta decay.",
				"Size(WxHxD): 5x4x5, Controller (Bottom center)",
				"3x1x3 Matter Generation Coils (Inside bottom 5x1x5 layer)",
				"9x Matter Generation Coils (Centered 3x1x3 area in Bottom layer)",
				"1x Input Hatch (Any bottom layer casing)", "1x Output Hatch (Any bottom layer casing)",
				"1x Maintenance Hatch (Any bottom layer casing)", "1x Muffler Hatch (Centered 3x1x3 area in Top layer)",
				"1x Energy Hatch (Any bottom layer casing)", "24x IC2 Reinforced Glass for the walls",
				"Reactor Casings for the edges, bottom & top (40 at least!)", CORE.GT_Tooltip
		};
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
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] {
					Textures.BlockIcons.CASING_BLOCKS[66],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_ACTIVE
							: Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER)
			};
		}
		return new ITexture[] {
				Textures.BlockIcons.CASING_BLOCKS[66]
		};
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_NuclearReactor(this.mName);
	}

	@Override
	public void onConfigLoad(final GT_Config aConfig) {
	}

}