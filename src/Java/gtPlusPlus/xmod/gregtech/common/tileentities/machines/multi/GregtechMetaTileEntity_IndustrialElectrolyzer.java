package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import static gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks.GTID;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

public class GregtechMetaTileEntity_IndustrialElectrolyzer
extends GT_MetaTileEntity_MultiBlockBase {
	public GregtechMetaTileEntity_IndustrialElectrolyzer(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialElectrolyzer(String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialElectrolyzer(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Controller Block for the Industrial Electrolyzer",
				"Size: 3x3x3 (Hollow)",
				"Controller (front centered)",
				"1x Input Bus (anywhere)",
				"1x Output Bus (anywhere)",
				"1x Input Hatch (anywhere)",
				"1x Output Hatch (anywhere)",
				"1x Energy Hatch (anywhere)",
				"1x Maintenance Hatch (anywhere)",
				"1x Muffler (anywhere)",
				"Electrolyzer Casings for the rest (16 at least!)",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[GTID+5], new GT_RenderedTexture(aActive ? Textures.BlockIcons.STEAM_TURBINE_SIDE_ACTIVE : Textures.BlockIcons.STEAM_TURBINE_SIDE)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[GTID+5]};
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "IndustrialElectrolyzer.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes;
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
	public boolean checkRecipe(ItemStack aStack) { //TODO - Add Check to make sure Fluid output isn't full
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
            GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);
            if ((tRecipe != null) && (7500 >= tRecipe.mSpecialValue) && (tRecipe.isRecipeInputEqual(true, tFluids, tInputs))) {
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
                
                ItemStack[] tOut = new ItemStack[tRecipe.mOutputs.length];
				for (int h = 0; h < tRecipe.mOutputs.length; h++) {
					tOut[h] = tRecipe.getOutput(h).copy();
					tOut[h].stackSize = 0;
				}
                FluidStack tFOut = null;
				if (tRecipe.getFluidOutput(0) != null) tFOut = tRecipe.getFluidOutput(0).copy();
				for (int f = 0; f < tOut.length; f++) {
					if (tRecipe.mOutputs[f] != null && tOut[f] != null) {
						for (int g = 0; g < 1; g++) {
							if (getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(f))
								tOut[f].stackSize += tRecipe.mOutputs[f].stackSize;
						}
					}
				}
				if (tFOut != null) {
					int tSize = tFOut.amount;
					tFOut.amount = tSize * 1;
				}
				
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
				
               /* this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0), tRecipe.getOutput(1)};
                updateSlots();*/
                return true;
            }
        }
        return false;
    }

	@Override
	public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 20) {
            GT_Utility.doSoundAtClient((String) GregTech_API.sSoundList.get(Integer.valueOf(5)), 10, 1.0F, aX, aY, aZ);
        }
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
                        if ((!addMaintenanceToMachineList(tTileEntity, 62)) && (!addMufflerToMachineList(tTileEntity, 62)) && (!addInputToMachineList(tTileEntity, 62)) && (!addOutputToMachineList(tTileEntity, 62)) && (!addEnergyInputToMachineList(tTileEntity, 62))) {
                            Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
                            byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
                            if (((tBlock != ModBlocks.blockCasingsMisc) || (tMeta != 5))) {
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
