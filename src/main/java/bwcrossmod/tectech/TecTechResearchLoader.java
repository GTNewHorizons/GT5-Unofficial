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

import static gregtech.api.util.GTRecipeBuilder.MINUTES;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import tectech.recipe.TTRecipeAdder;

public class TecTechResearchLoader {

    public static void runResearches() {
        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemRegistry.voidminer[0].copy(),
            1024000,
            256,
            (int) TierEU.RECIPE_ZPM,
            24,
            new Object[] { ItemRegistry.voidminer[0].copy(),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackPlutonium, 9L),
                Materials.BlackPlutonium.getPlates(3), ItemList.Electric_Motor_ZPM.get(9L), ItemList.Sensor_ZPM.get(9L),
                ItemList.Field_Generator_ZPM.get(9L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.BlackPlutonium, 36L) },
            new FluidStack[] { new FluidStack(solderIndalloy, 1440), WerkstoffLoader.Krypton.getFluidOrGas(20000) },
            ItemRegistry.voidminer[1].copy(),
            5 * MINUTES,
            (int) TierEU.RECIPE_ZPM);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemRegistry.voidminer[1].copy(),
            8192000,
            512,
            (int) TierEU.RECIPE_UV,
            64,
            new Object[] { ItemRegistry.voidminer[1].copy(),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 9L),
                Materials.Neutronium.getPlates(3), ItemList.Electric_Motor_UV.get(9L), ItemList.Sensor_UV.get(9L),
                ItemList.Field_Generator_UV.get(9L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 36L) },
            new FluidStack[] { new FluidStack(solderIndalloy, 1440), WerkstoffLoader.Oganesson.getFluidOrGas(20000) },
            ItemRegistry.voidminer[2].copy(),
            5 * MINUTES,
            (int) TierEU.RECIPE_UV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Machine_Multi_ImplosionCompressor.get(1L),
            64000,
            48,
            (int) TierEU.RECIPE_UV,
            8,
            new Object[] { ItemList.Machine_Multi_ImplosionCompressor.get(1L), Materials.Neutronium.getBlocks(5),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Osmium, 64),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Osmium, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64),
                ItemList.Electric_Piston_UV.get(64), },
            new FluidStack[] { new FluidStack(solderIndalloy, 1440), Materials.Osmium.getMolten(1440),
                Materials.Neutronium.getMolten(1440) },
            ItemRegistry.eic.copy(),
            5 * MINUTES,
            (int) TierEU.RECIPE_UV);

    }
}
