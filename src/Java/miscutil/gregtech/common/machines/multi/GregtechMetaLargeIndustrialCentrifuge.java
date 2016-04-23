package miscutil.gregtech.common.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import miscutil.core.block.ModBlocks;
import miscutil.gregtech.api.enums.GregtechTextures;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

public class GregtechMetaLargeIndustrialCentrifuge
extends GT_MetaTileEntity_MultiBlockBase {
	private static boolean controller;

	public GregtechMetaLargeIndustrialCentrifuge(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaLargeIndustrialCentrifuge(String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaLargeIndustrialCentrifuge(this.mName);
	}

	/*@Override
	public String[] getDescription() {
		return new String[]{"Controller Block for the Distillation Tower", 
				"Size: 3x3x3 (Hollow)", 
				"Controller (Front Middle)", 
				"1x Input Hatch (Bottom)", 
				"2x Output Hatch (Botton)",
				"1x Energy Hatch (Anywhere)", 
				"1x Maintenance Hatch (Anywhere)",
		"Centrifuge Casings for the rest (20 at least!)"};
	}*/
	
	@Override
	public String[] getDescription() {
        return new String[]{
                "Controller Block for the Industrial Centrifuge",
                "Size: 3x3x3 (Hollow)", "Controller (Front Center)",
                "1x Input Hatch (Anywhere)",
                "1x Output Hatch (Anywhere)",
                "1x Input Bus (Anywhere)",
                "1x Output Bus (Anywhere)",
                "1x [EV] Energy Hatch (Anywhere - Can be higher Tier)",
                "1x Maintenance Hatch (Anywhere)",
                "Needs a Turbine Item (inside controller GUI)",
                "Centrifuge Casings for the rest (16 at least)",};
    }

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{GregtechTextures.BlockIcons.GT_CASING_BLOCKS[0], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
		}
		return new ITexture[]{GregtechTextures.BlockIcons.GT_CASING_BLOCKS[0]};
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "VacuumFreezer.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	ArrayList<ItemStack> tInputList = getStoredInputs();
	GT_Recipe mLastRecipe;
	
	@Override
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
		
		
/*
		if (this.mInputHatches.size() > 0 && this.mInputHatches.get(0) != null && this.mInputHatches.get(0).mFluid != null && this.mInputHatches.get(0).mFluid.amount > 0) {
			GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], new FluidStack[]{this.mInputHatches.get(0).mFluid}, new ItemStack[]{});
			if (tRecipe != null) {
				if (tRecipe.isRecipeInputEqual(true, new FluidStack[]{this.mInputHatches.get(0).mFluid}, new ItemStack[]{})) {
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
					this.mOutputFluids = tRecipe.mFluidOutputs;
					updateSlots();
					return true;
				}
			}
		}

		return false;*/
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
	        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
	        if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
	            return false;
	        }
	        int tAmount = 0;
	        for (int i = -1; i < 2; i++) {
	            for (int j = -1; j < 2; j++) {
	                for (int h = -1; h < 2; h++) {
	                    if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {
	                        IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
	                        if ((!addMaintenanceToMachineList(tTileEntity, 0)) && (!addInputToMachineList(tTileEntity, 0)) && (!addOutputToMachineList(tTileEntity, 0)) && (!addEnergyInputToMachineList(tTileEntity, 0))) {
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
	
	
	/*@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
			return false;
		}
		int tAmount = 0;
		controller = false;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int h = -1; h < 2; h++) { //Height
					if (!(i == 0 && j == 0 && (h > -2 && h < 2)))//((h > 0)&&(h<5)) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))
					{
						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if ((!addMaintenanceToMachineList(tTileEntity, 0)) && (!addInputToMachineList(tTileEntity, 0)) && (!addOutputToMachineList(tTileEntity, 0)) && (!addEnergyInputToMachineList(tTileEntity, 0)) && (!ignoreController(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j)))) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 1) {
								return false;
							}
							tAmount++;
						}
					}
				}
			}
		}
		if (this.mInputHatches.size() >= 1 || this.mOutputBusses.size() != 1 || this.mInputBusses.size() != 0 || this.mOutputHatches.size() >= 2) {
			return false;
		}
		int height = (this.getBaseMetaTileEntity().getYCoord() - 1);
		if (this.mInputHatches.get(0).getBaseMetaTileEntity().getYCoord() != height || this.mOutputHatches.get(0).getBaseMetaTileEntity().getYCoord() != height) {
			return false;
		}
		GT_MetaTileEntity_Hatch_Output[] tmpHatches = new GT_MetaTileEntity_Hatch_Output[2];
		for (int i = 0; i < this.mOutputHatches.size(); i++) {
			int hatchNumber = this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord() - 1 - height;
			if (tmpHatches[hatchNumber] == null) {
				tmpHatches[hatchNumber] = this.mOutputHatches.get(i);
			} else {
				return false;
			}
		}
		this.mOutputHatches.clear();
		for (int i = 0; i < tmpHatches.length; i++) {
			this.mOutputHatches.add(tmpHatches[i]);
		}
		return tAmount >= 20;
	}*/

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
}