/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bwcrossmod.tectech;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;

import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsAlloy;
import tectech.recipe.TTRecipeAdder;

public class TecTechResearchLoader {

    public static void runResearches() {
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemRegistry.voidminer[0].copy(),
            1024000,
            256,
            (int) TierEU.RECIPE_ZPM,
            24,
            new Object[] { ItemRegistry.voidminer[0].copy(),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackPlutonium, 9L),
                MaterialLibAPI.getStack(Materials2Materials.BlackPlutonium, Materials2Shapes.shapePlate, (int) (3)),
                ItemList.Electric_Motor_ZPM.get(9L), ItemList.Sensor_ZPM.get(9L), ItemList.Field_Generator_ZPM.get(9L),
                MaterialLibAPI.getStack(Materials2Materials.BlackPlutonium, Materials2Shapes.shapeScrew, (int) (36)) },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS),
                WerkstoffLoader.Krypton.getFluidOrGas(20_000) },
            ItemRegistry.voidminer[1].copy(),
            2 * MINUTES,
            (int) TierEU.RECIPE_ZPM);

        // Void Miner Mk III
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemRegistry.voidminer[1].copy(),
            8192000,
            512,
            (int) TierEU.RECIPE_UV,
            64,
            new Object[] { ItemRegistry.voidminer[1].copy(),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 9L),
                MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.shapePlate, (int) (3)),
                ItemList.Electric_Motor_UV.get(9L), ItemList.Sensor_UV.get(9L), ItemList.Field_Generator_UV.get(9L),
                MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.shapeScrew, (int) (36)) },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS),
                WerkstoffLoader.Oganesson.getFluidOrGas(20_000) },
            ItemRegistry.voidminer[2].copy(),
            2 * MINUTES,
            (int) TierEU.RECIPE_UV);

        // Electric Implosion Compressor
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Machine_Multi_ImplosionCompressor.get(1L),
            64000,
            48,
            (int) TierEU.RECIPE_UV,
            8,
            new Object[] { ItemList.Machine_Multi_ImplosionCompressor.get(1L),
                MaterialLibAPI
                    .getStack(Materials2Materials.Neutronium, Materials2Shapes.shapePlateSuperdense, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.shapeStickLong, (int) (64)),
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.shapeRing, (int) (64)),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64),
                ItemList.Electric_Piston_UV.get(64), },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS), MaterialLibAPI
                .getFluidStack(Materials2Materials.Osmium, Materials2FluidShapes.shapeFluidMolten, (int) (10 * INGOTS)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Neutronium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (10 * INGOTS)) },
            ItemList.ElectricImplosionCompressor.get(1L),
            2 * MINUTES,
            (int) TierEU.RECIPE_UV);

    }
}
