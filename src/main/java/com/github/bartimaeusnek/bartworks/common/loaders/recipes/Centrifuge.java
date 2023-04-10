package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR.HTGRMaterials.MATERIALS_PER_FUEL;
import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR.HTGRMaterials.sHTGR_Fuel;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.common.loaders.BioCultureLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;
import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_THTR;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Util;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class Centrifuge implements Runnable {

    @Override
    public void run() {

        GT_Values.RA.addCentrifugeRecipe(
                Materials.Thorium.getDust(1),
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                Materials.Thorium.getDust(1),
                Materials.Thorium.getDust(1),
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1),
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1),
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1),
                GT_Values.NI,
                new int[] { 800, 375, 22, 22, 5 },
                10000,
                BW_Util.getMachineVoltageFromTier(4));

        ItemStack[] pellets = new ItemStack[6];
        Arrays.fill(pellets, new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 64, 4));

        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(
                false,
                new ItemStack[] { new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 3),
                        GT_Utility.getIntegratedCircuit(17) },
                pellets,
                null,
                null,
                null,
                null,
                48000,
                30,
                0);
        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(
                false,
                new ItemStack[] { new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 5),
                        GT_Utility.getIntegratedCircuit(17) },
                new ItemStack[] { new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 64, 6) },
                null,
                null,
                null,
                null,
                48000,
                30,
                0);
        GT_Values.RA.addCentrifugeRecipe(
                new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 6),
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                Materials.Lead.getDust(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                new int[] { 300 },
                1200,
                30);
        int i = 0;
        for (GT_TileEntity_HTGR.HTGRMaterials.Fuel_ fuel : sHTGR_Fuel) {

            GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(
                    false,
                    new ItemStack[] { new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 3),
                            GT_Utility.getIntegratedCircuit(17) },
                    new ItemStack[] { new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 64, i + 4),
                            new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 64, i + 4),
                            new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 64, i + 4),
                            new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 64, i + 4) },
                    null,
                    null,
                    null,
                    null,
                    12000,
                    30,
                    0);
            GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(
                    false,
                    new ItemStack[] { new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 5),
                            GT_Utility.getIntegratedCircuit(17) },
                    new ItemStack[] { new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 64, i + 6) },
                    null,
                    null,
                    null,
                    null,
                    3000,
                    30,
                    0);

            GT_Values.RA.addCentrifugeRecipe(
                    new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 6),
                    GT_Values.NI,
                    GT_Values.NF,
                    fuel.recycledFluid,
                    fuel.recycledItems[0],
                    fuel.recycledItems[1],
                    fuel.recycledItems[2],
                    fuel.recycledItems[3],
                    fuel.recycledItems[4],
                    fuel.recycledItems[5],
                    fuel.recycleChances,
                    1200,
                    30);
            i += MATERIALS_PER_FUEL;
        }

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(17),
                null,
                new FluidStack(BioCultureLoader.eColi.getFluid(), 1000),
                new FluidStack(FluidLoader.BioLabFluidMaterials[1], 10),
                BioItemList.getOther(4),
                null,
                null,
                null,
                null,
                null,
                new int[] { 1000 },
                60 * 20,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(17),
                null,
                new FluidStack(FluidLoader.BioLabFluidMaterials[1], 1000),
                new FluidStack(FluidLoader.BioLabFluidMaterials[3], 250),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                60 * 20,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(17),
                null,
                new FluidStack(BioCultureLoader.CommonYeast.getFluid(), 1000),
                new FluidStack(FluidLoader.BioLabFluidMaterials[2], 10),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                60 * 20,
                BW_Util.getMachineVoltageFromTier(3));
    }
}
