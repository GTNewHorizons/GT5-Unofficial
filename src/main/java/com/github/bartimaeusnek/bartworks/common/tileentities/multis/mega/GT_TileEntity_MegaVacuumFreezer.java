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

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import com.github.bartimaeusnek.bartworks.util.MegaUtils;
import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.bartimaeusnek.crossmod.tectech.TecTechUtils;
import cpw.mods.fml.common.Optional;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_VacuumFreezer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.enums.GT_Values.V;

@Optional.Interface(iface = "com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti", modid = "tectech", striprefs = true)
public class GT_TileEntity_MegaVacuumFreezer extends GT_MetaTileEntity_VacuumFreezer implements TecTechEnabledMulti {

    public GT_TileEntity_MegaVacuumFreezer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MegaVacuumFreezer(String aName) {
        super(aName);
    }

    public String[] getDescription() {
        String[] dsc = StatCollector.translateToLocal("tooltip.tile.mvf.0.name").split(";");
        String[] fdsc = new String[dsc.length + 1];
        for (int i = 0; i < dsc.length; i++) {
            fdsc[i] = dsc[i];
            fdsc[dsc.length] = StatCollector.translateToLocal("tooltip.bw.1.name") + ChatColorHelper.DARKGREEN + " BartWorks";
        }
        return fdsc;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaVacuumFreezer(this.mName);
    }

    @SuppressWarnings("rawtypes")
    public ArrayList TTTunnels = new ArrayList<>();
    @SuppressWarnings("rawtypes")
    public ArrayList TTMultiAmp = new ArrayList<>();

    @Override
    public boolean addEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (LoaderReference.tectech)
            return TecTechUtils.addEnergyInputToMachineList(this, aTileEntity, aBaseCasingIndex);
        return super.addEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        if (LoaderReference.tectech)
            return TecTechUtils.drainEnergyMEBFTecTech(this, aEU);
        return MegaUtils.drainEnergyMegaVanilla(this, aEU);
    }

    @Override
    public long getMaxInputVoltage() {
        if (LoaderReference.tectech)
            return TecTechUtils.getMaxInputVoltage(this);
        return super.getMaxInputVoltage();
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        ItemStack[] tInputs = this.getStoredInputs().toArray(new ItemStack[0]);
        ArrayList<ItemStack> outputItems = new ArrayList<>();

        long nominalV = LoaderReference.tectech ? TecTechUtils.getnominalVoltageTT(this) : BW_Util.getnominalVoltage(this);

        byte tTier = (byte) Math.max(1, GT_Utility.getTier(nominalV));

        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sVacuumRecipes.findRecipe(this.getBaseMetaTileEntity(), false, V[tTier], null, tInputs);
        boolean found_Recipe = false;
        int processed = 0;

        while (this.getStoredInputs().size() > 0 && processed < ConfigHandler.megaMachinesMax) {
            if (tRecipe != null && (tRecipe.mEUt * (processed + 1)) < nominalV && tRecipe.isRecipeInputEqual(true, null, tInputs)) {
                found_Recipe = true;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    outputItems.add(tRecipe.getOutput(i));
                }
                ++processed;
            } else
                break;
        }

        if (found_Recipe) {
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
            this.mOutputItems = new ItemStack[outputItems.size()];
            this.mOutputItems = outputItems.toArray(this.mOutputItems);
            this.updateSlots();
            return true;
        }
        return false;
    }

    // -------------- TEC TECH COMPAT ----------------

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (LoaderReference.tectech) {
            this.getTecTechEnergyMultis().clear();
            this.getTecTechEnergyTunnels().clear();
        }
        return (
                BW_Util.check_layer(aBaseMetaTileEntity, 7, -7, -6, GregTech_API.sBlockCasings2, 1, 7, false, false, true, GregTech_API.sBlockCasings2, 1, true, 17)
                        && BW_Util.check_layer(aBaseMetaTileEntity, 7, -6, 0, GregTech_API.sBlockCasings2, 1, 7, false, false, true, Blocks.air, -1, true, 17)
                        && BW_Util.check_layer(aBaseMetaTileEntity, 7, 0, 1, GregTech_API.sBlockCasings2, 1, 7, true, false, true, Blocks.air, -1, true, 17)
                        && BW_Util.check_layer(aBaseMetaTileEntity, 7, 1, 7, GregTech_API.sBlockCasings2, 1, 7, false, false, true, Blocks.air, -1, true, 17)
                        && BW_Util.check_layer(aBaseMetaTileEntity, 7, 7, 8, GregTech_API.sBlockCasings2, 1, 7, false, false, true, GregTech_API.sBlockCasings2, 1, true, 17)
        ) &&
                !this.mInputBusses.isEmpty() &&
                !this.mOutputBusses.isEmpty() &&
                !this.mMaintenanceHatches.isEmpty() &&
                LoaderReference.tectech ?
                (!this.getTecTechEnergyMultis().isEmpty() || !this.getTecTechEnergyTunnels().isEmpty() || !this.mEnergyHatches.isEmpty()) :
                !this.mEnergyHatches.isEmpty();
    }

    @Override
    public String[] getInfoData() {
        return LoaderReference.tectech ? this.getInfoDataArray(this) : super.getInfoData();
    }

    @Override
    @Optional.Method(modid = "tectech")
    public List<GT_MetaTileEntity_Hatch_Energy> getVanillaEnergyHatches() {
        return this.mEnergyHatches;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Optional.Method(modid = "tectech")
    public List getTecTechEnergyTunnels() {
        return TTTunnels;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Optional.Method(modid = "tectech")
    public List getTecTechEnergyMultis() {
        return TTMultiAmp;
    }
}