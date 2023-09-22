package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR.HTGRMaterials.MATERIALS_PER_FUEL;
import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR.HTGRMaterials.sHTGR_Fuel;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.common.loaders.BioCultureLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;
import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_THTR;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_Utility;

public class Centrifuge implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder().itemInputs(Materials.Thorium.getDust(1))
                .itemOutputs(
                        Materials.Thorium.getDust(1),
                        Materials.Thorium.getDust(1),
                        WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1),
                        WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1),
                        WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1))
                .outputChances(800, 375, 22, 22, 5).duration(8 * MINUTES + 20 * SECONDS).eut(TierEU.RECIPE_EV)
                .addTo(sCentrifugeRecipes);

        ItemStack[] pellets = new ItemStack[6];
        Arrays.fill(pellets, new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 64, 4));

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 3),
                        GT_Utility.getIntegratedCircuit(17))
                .itemOutputs(pellets).duration(40 * MINUTES).eut(TierEU.RECIPE_LV).addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 5),
                        GT_Utility.getIntegratedCircuit(17))
                .itemOutputs(new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 64, 6))
                .duration(40 * MINUTES).eut(TierEU.RECIPE_LV).addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 6))
                .itemOutputs(Materials.Lead.getDust(1)).outputChances(300).duration(60 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(sCentrifugeRecipes);

        int i = 0;
        for (GT_TileEntity_HTGR.HTGRMaterials.Fuel_ fuel : sHTGR_Fuel) {

            GT_Values.RA.stdBuilder()
                    .itemInputs(
                            new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 3),
                            GT_Utility.getIntegratedCircuit(17))
                    .itemOutputs(
                            new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 64, i + 4),
                            new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 64, i + 4),
                            new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 64, i + 4),
                            new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 64, i + 4))
                    .duration(10 * MINUTES).eut(TierEU.RECIPE_LV).addTo(sCentrifugeRecipes);

            GT_Values.RA.stdBuilder()
                    .itemInputs(
                            new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 5),
                            GT_Utility.getIntegratedCircuit(17))
                    .itemOutputs(new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 64, i + 6))
                    .duration(2 * MINUTES + 30 * SECONDS).eut(TierEU.RECIPE_LV).addTo(sCentrifugeRecipes);

            GT_RecipeBuilder recipeBuilder = GT_Values.RA.stdBuilder()
                    .itemInputs(new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 6))
                    .itemOutputs(
                            fuel.recycledItems[0],
                            fuel.recycledItems[1],
                            fuel.recycledItems[2],
                            fuel.recycledItems[3],
                            fuel.recycledItems[4])
                    .outputChances(fuel.recycleChances);
            if (fuel.recycledFluid != null) {
                recipeBuilder.fluidOutputs(fuel.recycledFluid);
            }
            recipeBuilder.duration(1 * MINUTES).eut(TierEU.RECIPE_LV).addTo(sCentrifugeRecipes);

            i += MATERIALS_PER_FUEL;
        }

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(17)).itemOutputs(BioItemList.getOther(4))
                .fluidInputs(new FluidStack(BioCultureLoader.eColi.getFluid(), 1000))
                .fluidOutputs(new FluidStack(FluidLoader.BioLabFluidMaterials[1], 10)).duration(60 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(17))
                .fluidInputs(new FluidStack(FluidLoader.BioLabFluidMaterials[1], 1000))
                .fluidOutputs(new FluidStack(FluidLoader.BioLabFluidMaterials[3], 250)).duration(60 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(17))
                .fluidInputs(new FluidStack(BioCultureLoader.CommonYeast.getFluid(), 1000))
                .fluidOutputs(new FluidStack(FluidLoader.BioLabFluidMaterials[2], 10)).duration(60 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sCentrifugeRecipes);

    }
}
