package miscutil.gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

import java.util.ArrayList;
import java.util.Arrays;

import miscutil.core.block.ModBlocks;
import miscutil.core.util.Utils;
import miscutil.gregtech.api.gui.GUI_MultiMachine;
import miscutil.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import miscutil.gregtech.api.objects.GregtechRenderedTexture;
import miscutil.gregtech.api.util.GregtechRecipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntityIndustrialCentrifuge
extends GregtechMeta_MultiBlockBase {
	private static boolean controller;
	private int mLevel = 2;

	public GregtechMetaTileEntityIndustrialCentrifuge(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntityIndustrialCentrifuge(String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityIndustrialCentrifuge(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Industrial Centrifuge",
				"Size: 3x3x3 (Hollow)", 
				"Controller (Front Center)",
				"1x Maintenance Hatch (Rear Center)",
				"The rest can be placed anywhere except the Front",
				"1x Input Hatch",
				"1x Output Hatch",
				"1x Input Bus",
				"1x Output Bus",
				"1x [EV] Energy Hatch (Can be higher Tier)",
				"Needs a Turbine Item (inside controller GUI)",
				"Centrifuge Casings for the rest (16 at least)",};
	}

	/*@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{GregtechTextures.BlockIcons.GT_CASING_BLOCKS[0], new GT_RenderedTexture(aActive ? Textures.BlockIcons.LARGETURBINE_ACTIVE5 : Textures.BlockIcons.LARGETURBINE5)};
		}
		return new ITexture[]{GregtechTextures.BlockIcons.GT_CASING_BLOCKS[0]};
	}*/

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][aColorIndex + 1], aFacing == aSide ? aActive ? new GregtechRenderedTexture(Textures.BlockIcons.LARGETURBINE_ACTIVE5) : new GregtechRenderedTexture(Textures.BlockIcons.LARGETURBINE5) : Textures.BlockIcons.CASING_BLOCKS[57]};
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "CokeOven.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes;
	}

	/*@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }*/

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	ArrayList<ItemStack> tInputList = getStoredInputs();
	GT_Recipe mLastRecipe;

	/*@Override
	public boolean checkRecipe(ItemStack aStack) {
		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

		GT_Recipe.GT_Recipe_Map map = getRecipeMap();
		for (int i = 0; i < tInputList.size() - 1; i++) {
			for (int j = i + 1; j < tInputList.size(); j++) {
				if (GT_Utility.areStacksEqual((ItemStack) tInputList.get(i), (ItemStack) tInputList.get(j))) {
					if (((ItemStack) tInputList.get(i)).stackSize >= ((ItemStack) tInputList.get(j)).stackSize) {
						tInputList.remove(j--);
					} else {
						tInputList.remove(i--);
						break;
					}
				}
			}
		}
		ItemStack[] tInputs = (ItemStack[]) Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, 2);

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
		FluidStack[] tFluids = (FluidStack[]) Arrays.copyOfRange(tFluidList.toArray(new FluidStack[tInputList.size()]), 0, 1);
		if (tInputList.size() > 0 || tFluids.length > 0) {
			GT_Recipe tRecipe = map.findRecipe(getBaseMetaTileEntity(), mLastRecipe, false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);
			if (tRecipe != null) {
				if (tRecipe.mFluidInputs != null) {

				}
				mLastRecipe = tRecipe;
				this.mEUt = 0;
				this.mOutputItems = null;
				this.mOutputFluids = null;
				int machines = Math.min(16, mInventory[1].stackSize);
				int i = 0;
				for (; i < machines; i++) {
					if (!tRecipe.isRecipeInputEqual(true, tFluids, tInputs)) {
						if (i == 0) {
							return false;
						}
						break;
					}
				}
				this.mMaxProgresstime = tRecipe.mDuration;
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
						this.mMaxProgresstime /= 4;
					}
				}
				this.mEUt *= i;
				if (this.mEUt > 0) {
					this.mEUt = (-this.mEUt);
				}
				ItemStack[] tOut = new ItemStack[tRecipe.mOutputs.length];
				for (int h = 0; h < tRecipe.mOutputs.length; h++) {
					tOut[h] = tRecipe.getOutput(h).copy();
					tOut[h].stackSize = 0;
				}
				FluidStack tFOut = null;
				if (tRecipe.getFluidOutput(0) != null) tFOut = tRecipe.getFluidOutput(0).copy();
				for (int f = 0; f < tOut.length; f++) {
					if (tRecipe.mOutputs[f] != null && tOut[f] != null) {
						for (int g = 0; g < i; g++) {
							if (getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(f))
								tOut[f].stackSize += tRecipe.mOutputs[f].stackSize;
						}
					}
				}
				if (tFOut != null) {
					int tSize = tFOut.amount;
					tFOut.amount = tSize * i;
				}
				this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
				List<ItemStack> overStacks = new ArrayList<ItemStack>();
				for (int f = 0; f < tOut.length; f++) {
					if (tOut[f].getMaxStackSize() < tOut[f].stackSize) {
						while (tOut[f].getMaxStackSize() < tOut[f].stackSize) {
							ItemStack tmp = tOut[f].copy();
							tmp.stackSize = tmp.getMaxStackSize();
							tOut[f].stackSize = tOut[f].stackSize - tOut[f].getMaxStackSize();
							overStacks.add(tmp);
						}
					}
				}
				if (overStacks.size() > 0) {
					ItemStack[] tmp = new ItemStack[overStacks.size()];
					tmp = overStacks.toArray(tmp);
					tOut = ArrayUtils.addAll(tOut, tmp);
				}
				List<ItemStack> tSList = new ArrayList<ItemStack>();
				for (ItemStack tS : tOut) {
					if (tS.stackSize > 0) tSList.add(tS);
				}
				tOut = tSList.toArray(new ItemStack[tSList.size()]);
				this.mOutputItems = tOut;
				this.mOutputFluids = new FluidStack[]{tFOut};
				updateSlots();
				return true;
			}
		}
		return false;
	}*/
	
	 @Override
	    public boolean checkRecipe(ItemStack aStack) {
	        ArrayList<ItemStack> tInputList = getStoredInputs();
	        for (int i = 0; i < tInputList.size() - 1; i++) {
	            for (int j = i + 1; j < tInputList.size(); j++) {
	                if (GT_Utility.areStacksEqual((ItemStack) tInputList.get(i), (ItemStack) tInputList.get(j))) {
	                    if (((ItemStack) tInputList.get(i)).stackSize >= ((ItemStack) tInputList.get(j)).stackSize) {
	                        tInputList.remove(j--);
	                    } else {
	                        tInputList.remove(i--);
	                        break;
	                    }
	                }
	            }
	        }
	        ItemStack[] tInputs = (ItemStack[]) Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, 2);

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
	        FluidStack[] tFluids = (FluidStack[]) Arrays.copyOfRange(tFluidList.toArray(new FluidStack[tInputList.size()]), 0, 1);
	        if (tInputList.size() > 0) {
	            long tVoltage = getMaxInputVoltage();
	            byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
	            GT_Recipe tRecipe = GregtechRecipe.Gregtech_Recipe_Map.sCokeOvenRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);
	            if ((tRecipe != null) && (this.mLevel >= tRecipe.mSpecialValue) && (tRecipe.isRecipeInputEqual(true, tFluids, tInputs))) {
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
	                this.mOutputFluids = new FluidStack[]{tRecipe.getFluidOutput(0)};
	                updateSlots();
	                return true;
	            }
	        }
	        return false;
	    }

	@SuppressWarnings("static-method")
	public Block getCasingBlock() {
		return ModBlocks.blockCasingsMisc;
	}

	@SuppressWarnings("static-method")
	public byte getCasingMeta() {
		return 0;
	}

	@SuppressWarnings("static-method")
	public byte getCasingTextureIndex() {
		return 0;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int yDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetY;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		//Utils.LOG_WARNING("X:"+xDir+" Y:"+yDir+" Z:"+zDir);
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		}
		int tAmount = 0;
		for (int i = -1; i < 2; i++) { //X-Dir
			for (int j = -1; j < 2; j++) { //Z-Dir
				for (int h = -1; h < 2; h++) { //Y-Dir
					if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {			

						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						//Utils.LOG_INFO("X:"+tTileEntity.getXCoord()+" Y:"+tTileEntity.getYCoord()+" Z:"+tTileEntity.getZCoord());
						if ((!addMaintenanceToMachineList(tTileEntity, 57)) && (!addInputToMachineList(tTileEntity, 57)) && (!addOutputToMachineList(tTileEntity, 57)) && (!addEnergyInputToMachineList(tTileEntity, 57))) {

							//Maintenance Hatch
							if ((tTileEntity != null) && (tTileEntity.getMetaTileEntity() != null)) {
								if (tTileEntity.getXCoord() == aBaseMetaTileEntity.getXCoord() && tTileEntity.getYCoord() == aBaseMetaTileEntity.getYCoord() && tTileEntity.getZCoord() == (aBaseMetaTileEntity.getZCoord()+2)) {
									if ((tTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Maintenance)) {
										Utils.LOG_INFO("MAINT HATCH IN CORRECT PLACE");
										this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) tTileEntity.getMetaTileEntity());
										((GT_MetaTileEntity_Hatch) tTileEntity.getMetaTileEntity()).mMachineBlock = getCasingTextureIndex();
									} else {
										return false;
									}
								}	
								else {
									Utils.LOG_INFO("MAINT HATCH IN WRONG PLACE");
								}
							}

							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 0) {
								return false;
							}
							tAmount++;

						}
					}
				}
			}
		}		
		return tAmount >= 16;
	}

	@SuppressWarnings("static-method")
	public boolean ignoreController(Block tTileEntity) {
		if (!controller && tTileEntity == GregTech_API.sBlockMachines) {
			return true;
		}
		return false;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
	}

	/*@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}*/

	@Override
	public int getAmountOfOutputs() {
		return 2;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	private boolean checkStructure(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack){

		Utils.LOG_INFO("structure checking");
		int x = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int y = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetY;
		int z = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		Utils.LOG_INFO("X:"+x+" Y:"+y+" Z:"+z);

		//North case
		int hatchMaintainence[] = {x, y, z+2};
		int hatchInputBus[] = {x+1, y, z+1};
		int hatchOutputBus[] = {x-1, y, z+1};
		int hatchInput[] = {x+1, y+1, z+1};
		int hatchOutput[] = {x-1, y+1, z+1};
		int hatchEnergy[] = {x, y+1, z+2};

		//South case
		int hatchMaintainence0[] = {x, y, z-2};
		int hatchInputBus0[] = {x-1, y, z-1};
		int hatchOutputBus0[] = {x+1, y, z-1};
		int hatchInput0[] = {x-1, y+1, z-1};
		int hatchOutput0[] = {x+1, y+1, z-1};
		int hatchEnergy0[] = {x, y+1, z-2};


		//East case
		int hatchMaintainence1[] = {x+2, y, z};
		int hatchInputBus1[] = {x+1, y, z+1};
		int hatchOutputBus1[] = {x+1, y, z-1};
		int hatchInput1[] = {x+1, y+1, z+1};
		int hatchOutput1[] = {x+1, y+1, z-1};
		int hatchEnergy1[] = {x+2, y+1, z};

		//West case
		int hatchMaintainence2[] = {x-2, y, z};
		int hatchInputBus2[] = {x-1, y, z-1};
		int hatchOutputBus2[] = {x-1, y, z+1};
		int hatchInput2[] = {x-1, y+1, z-1};
		int hatchOutput2[] = {x-1, y+1, z+1};
		int hatchEnergy2[] = {x-2, y+1, z};



		/*//Maintenance Hatch
		IGregTechTileEntity tTileEntityMaintenanceHatch = getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2);
		if ((tTileEntityMaintenanceHatch != null) && (tTileEntityMaintenanceHatch.getMetaTileEntity() != null)) {
			if ((tTileEntityMaintenanceHatch.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Maintenance)) {
				this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) tTileEntityMaintenanceHatch.getMetaTileEntity());
				((GT_MetaTileEntity_Hatch) tTileEntityMaintenanceHatch.getMetaTileEntity()).mMachineBlock = getCasingTextureIndex();
			} else {
				return false;
			}
		}*/

		/*//Energy Hatch
		IGregTechTileEntity tTileEntityEnergyHatch = getBaseMetaTileEntity().getIGregTechTileEntity(i, h+1, j+2);
		if ((tTileEntityEnergyHatch != null) && (tTileEntityEnergyHatch.getMetaTileEntity() != null)) {
			if ((tTileEntityEnergyHatch.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Energy)) {
				this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) tTileEntityEnergyHatch.getMetaTileEntity());
				((GT_MetaTileEntity_Hatch) tTileEntityEnergyHatch.getMetaTileEntity()).mMachineBlock = getCasingTextureIndex();
			} else {
				return false;
			}
		}*/




		return false;
	}
}