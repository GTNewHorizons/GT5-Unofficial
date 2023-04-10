package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR.HTGRMaterials.MATERIALS_PER_FUEL;
import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR.HTGRMaterials.sHTGR_Fuel;
import static gregtech.api.enums.Mods.Gendustry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_THTR;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Util;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class Mixer implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addMixerRecipe(
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 10),
                Materials.Uranium235.getDust(1),
                GT_Utility.getIntegratedCircuit(2),
                null,
                null,
                null,
                new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials),
                400,
                30);

        int i = 0;
        for (GT_TileEntity_HTGR.HTGRMaterials.Fuel_ fuel : sHTGR_Fuel) {
            GT_Values.RA.addMixerRecipe(
                    new ItemStack[] { fuel.mainItem, fuel.secondaryItem, GT_Utility.getIntegratedCircuit(1) },
                    null,
                    new ItemStack[] { new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i) },
                    null,
                    400,
                    30);
            i += MATERIALS_PER_FUEL;
        }

        if (Gendustry.isModLoaded()) {
            GT_Values.RA.addMixerRecipe(
                    GT_Utility.getIntegratedCircuit(17),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Radon, 1L),
                    null,
                    null,
                    FluidRegistry.getFluidStack("liquiddna", 1000),
                    new FluidStack(FluidLoader.BioLabFluidMaterials[0], 2000),
                    Materials.Empty.getCells(1),
                    500,
                    BW_Util.getMachineVoltageFromTier(3));
        }

    }
}
