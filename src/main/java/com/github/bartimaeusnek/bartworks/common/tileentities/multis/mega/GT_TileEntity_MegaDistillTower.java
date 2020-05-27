/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.tileentities.multis.mega;

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import com.github.bartimaeusnek.bartworks.util.MathUtils;
import com.github.bartimaeusnek.bartworks.util.MegaUtils;
import com.google.common.collect.ArrayListMultimap;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DistillationTower;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class GT_TileEntity_MegaDistillTower extends GT_MetaTileEntity_DistillationTower {

    private static final int CASING_INDEX = 49;

    public GT_TileEntity_MegaDistillTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private GT_TileEntity_MegaDistillTower(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaDistillTower(this.mName);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Mega Distillation Tower",
                "Size(WxHxD): 15xhx15 (Hollow), with h ranging from 16 to 61",
                "Controller (Front bottom)",
                "1+ Input Hatch (Any bottom layer casing)",
                "1+ Output Bus (Any bottom layer casing)",
                "An \"Output Layer\" consists of 5 layers!",
                "2-11+ Output Hatch (One or more per Output Layer)",
                "1x Maintenance Hatch (Any casing)",
                "1+ Energy Hatch (Any casing)",
                "Fluids are only put out at the correct height",
                "The correct height equals the slot number in the NEI recipe",
                "Clean Stainless Steel Machine Casings for the rest (15 x h - 5 at least!)",
                StatCollector.translateToLocal("tooltip.bw.1.name") + ChatColorHelper.DARKGREEN + " BartWorks"
        };
    }

    private short controllerY = 0;

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        LAYERMAP.clear();
        controllerY = aBaseMetaTileEntity.getYCoord();
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;
        int x, z, y = 0, casingAmount = 0;
        boolean reachedTop = false;

        IGregTechTileEntity tileEntity;
        Block block;
        for (x = xDir - 7; x <= xDir + 7; ++x) {
            for (z = zDir - 7; z <= zDir + 7; ++z) {
                if (x != 0 || z != 0) {
                    tileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(x, y, z);
                    block = aBaseMetaTileEntity.getBlockOffset(x, y, z);
                    if (!this.addInputToMachineList(tileEntity, CASING_INDEX) && !this.addOutputToMachineList(tileEntity, CASING_INDEX) && !this.addMaintenanceToMachineList(tileEntity, CASING_INDEX) && !this.addEnergyInputToMachineList(tileEntity, CASING_INDEX)) {
                        if (block != GregTech_API.sBlockCasings4 || aBaseMetaTileEntity.getMetaIDOffset(x, y, z) != 1) {
                            return false;
                        }

                        ++casingAmount;
                    }
                }
            }
        }
        for (y = y + 1; y <= 60 && !reachedTop; ++y) {
            for (x = -7; x <= 7; ++x) {
                for (z = -7; z <= 7; ++z) {
                    tileEntity = aBaseMetaTileEntity.getIGregTechTileEntity(aBaseMetaTileEntity.getXCoord() + xDir + x, aBaseMetaTileEntity.getYCoord() + y, aBaseMetaTileEntity.getZCoord() + zDir + z);
                    block = aBaseMetaTileEntity.getBlock(aBaseMetaTileEntity.getXCoord() + xDir + x, aBaseMetaTileEntity.getYCoord() + y, aBaseMetaTileEntity.getZCoord() + zDir + z);
                    final boolean middle = Math.abs(x) < 7 && Math.abs(z) != 7;
                    if (aBaseMetaTileEntity.getAir(aBaseMetaTileEntity.getXCoord() + xDir + x, aBaseMetaTileEntity.getYCoord() + y, aBaseMetaTileEntity.getZCoord() + zDir + z)) {
                        if (!middle) {
                            //aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getXCoord() + xDir + x, aBaseMetaTileEntity.getYCoord() + y, aBaseMetaTileEntity.getZCoord() + zDir + z,GregTech_API.sBlockCasings4,1,2);
                            return false;
                        }
                    } else {
                        if (middle) {
                            reachedTop = true;
                        }
                        if (!this.addOutputToMachineList(tileEntity, CASING_INDEX) && !this.addMaintenanceToMachineList(tileEntity, CASING_INDEX) && !this.addEnergyInputToMachineList(tileEntity, CASING_INDEX)) {
                            if (block != GregTech_API.sBlockCasings4 || aBaseMetaTileEntity.getMetaID(aBaseMetaTileEntity.getXCoord() + xDir + x, aBaseMetaTileEntity.getYCoord() + y, aBaseMetaTileEntity.getZCoord() + zDir + z) != 1) {
                                return false;
                            }

                            ++casingAmount;
                        }
                    }
                }
            }
        }

        return casingAmount >= 15 * y - 5 && y >= 16 && y <= 61 && reachedTop;
    }

    @Override
    public boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (super.addOutputToMachineList(aTileEntity, aBaseCasingIndex)) {
            if (aTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Output) {
                int layer = aTileEntity.getYCoord() - controllerY;
                layer = MathUtils.ceilInt(((double)layer) /5D)-1;
                LAYERMAP.put(layer,(GT_MetaTileEntity_Hatch_Output) aTileEntity.getMetaTileEntity());
            }
            return true;
        }
        return false;
    }

    @Override
    protected void addFluidOutputs(FluidStack[] mOutputFluids2) {
        for (int i = 0; i < mOutputFluids2.length; i++) {
            for (int j = 0; j < LAYERMAP.get(i).size(); j++) {
                LAYERMAP.get(i).get(j).fill(new FluidStack(mOutputFluids2[i],mOutputFluids2[i].amount/LAYERMAP.get(i).size()), true);
            }
        }
    }

    private final ArrayListMultimap<Integer,GT_MetaTileEntity_Hatch_Output> LAYERMAP = ArrayListMultimap.create();

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        ArrayList<FluidStack> tFluidList = this.getStoredFluids();

        for (int i = 0; i < tFluidList.size() - 1; ++i) {
            for (int j = i + 1; j < tFluidList.size(); ++j) {
                if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
                    if (tFluidList.get(i).amount < tFluidList.get(j).amount) {
                        tFluidList.remove(i--);
                        break;
                    }
                    tFluidList.remove(j--);
                }
            }
        }

        long tVoltage = this.getMaxInputVoltage();
        byte tTier = (byte) Math.max(0, GT_Utility.getTier(tVoltage));

        long nominalV = BW_Util.getnominalVoltage(this);
        FluidStack[] tFluids = tFluidList.toArray(new FluidStack[0]);
        if (tFluids.length > 0) {
            for (FluidStack tFluid : tFluids) {
                ArrayList<FluidStack[]> outputFluids = new ArrayList<>();
                int processed = 0;
                boolean found_Recipe = false;
                FluidStack[] output;
                GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sDistillationRecipes.findRecipe(this.getBaseMetaTileEntity(), false, GT_Values.V[tTier], new FluidStack[]{tFluid});
                while (this.getStoredFluids().size() > 0 && processed < ConfigHandler.megaMachinesMax) {
                    if (tRecipe != null && (tRecipe.mEUt * (processed + 1)) < nominalV && tRecipe.isRecipeInputEqual(true, tFluids)) {
                        found_Recipe = true;
                        if (tRecipe.mFluidOutputs.length == 1 && tRecipe.mFluidOutputs[0].amount == 0)
                            tRecipe.mFluidOutputs[0].amount = tRecipe.mFluidInputs[0].amount;
                        output = new FluidStack[tRecipe.mFluidOutputs.length];
                        for (int i = 0; i < output.length; i++) {
                            output[i] = new FluidStack(tRecipe.mFluidOutputs[i],tRecipe.mFluidOutputs[i].amount);
                        }
                        outputFluids.add(output);
                        ++processed;
                    } else
                        break;
                }
                if (!found_Recipe)
                    continue;
                for (int j = 1; j < outputFluids.size(); j++) {
                    for (int k = 0; k < outputFluids.get(j).length; k++) {
                        outputFluids.get(0)[k].amount += outputFluids.get(j)[k].amount;
                    }
                }
                this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
                this.mEfficiencyIncrease = 10000;
                long actualEUT = (long) (tRecipe.mEUt) * processed;
                if (actualEUT > Integer.MAX_VALUE) {
                    byte divider = 0;
                    while (actualEUT > Integer.MAX_VALUE) {
                        actualEUT = actualEUT / 2;
                        divider++;
                    }
                    BW_Util.calculateOverclockedNessMulti((int) (actualEUT / (divider * 2)), tRecipe.mDuration * (divider * 2), 1, nominalV, this);
                } else
                    BW_Util.calculateOverclockedNessMulti((int) actualEUT, tRecipe.mDuration, 1, nominalV, this);
                //In case recipe is too OP for that machine
                if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.mEUt == Integer.MAX_VALUE - 1)
                    return false;
                if (this.mEUt > 0) {
                    this.mEUt = (-this.mEUt);
                }
                this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
                this.mOutputFluids = outputFluids.get(0).clone();
                if (tRecipe.getOutput(0) != null) {
                    int stacks = processed / 64;
                    ItemStack[] outputs = new ItemStack[stacks];
                    if (stacks > 0) {
                        for (int i = 0; i < stacks; i++)
                            if (i != stacks - 1)
                                outputs[i] = BW_Util.setStackSize(tRecipe.getOutput(0),64);
                            else
                                outputs[i] = BW_Util.setStackSize(tRecipe.getOutput(0),processed - (64 * i));
                        this.mOutputItems = outputs;
                    } else
                        this.mOutputItems = null;
                } else
                    this.mOutputItems = null;
                this.updateSlots();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        return MegaUtils.drainEnergyMegaVanilla(this, aEU);
    }
}