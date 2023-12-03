package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR.HTGRMaterials.MATERIALS_PER_FUEL;
import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR.HTGRMaterials.sHTGR_Fuel;
import static gregtech.api.enums.Mods.Gendustry;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_THTR;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class Mixer implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 10),
                        Materials.Uranium235.getDust(1),
                        GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials)).duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LV).addTo(mixerRecipes);

        int i = 0;
        for (GT_TileEntity_HTGR.HTGRMaterials.Fuel_ fuel : sHTGR_Fuel) {
            GT_Values.RA.stdBuilder().itemInputs(fuel.mainItem, fuel.secondaryItem, GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i))
                    .duration(20 * SECONDS).eut(TierEU.RECIPE_LV).addTo(mixerRecipes);

            i += MATERIALS_PER_FUEL;
        }

        if (Gendustry.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_Utility.getIntegratedCircuit(17),
                            GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Radon, 1L))
                    .itemOutputs(Materials.Empty.getCells(1))
                    .fluidInputs(FluidRegistry.getFluidStack("liquiddna", 1000))
                    .fluidOutputs(new FluidStack(FluidLoader.BioLabFluidMaterials[0], 2000)).duration(25 * SECONDS)
                    .eut(TierEU.RECIPE_HV).addTo(mixerRecipes);
        }

    }
}
