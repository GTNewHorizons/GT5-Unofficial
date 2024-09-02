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

package bartworks.system.material.processingLoaders;

import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import bartworks.system.material.BWNonMetaMaterialItems;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;

public class AddSomeRecipes implements Runnable {

    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(BWNonMetaMaterialItems.Depleted_Tiberium_1.get(1))
            .itemOutputs(
                WerkstoffLoader.Zirconium.get(dust),
                WerkstoffLoader.Zirconium.get(dust),
                WerkstoffLoader.Tiberium.get(dustSmall, 2),
                WerkstoffLoader.Zirconium.get(dust, 2),
                GTOreDictUnificator.get(dust, Materials.TungstenSteel, 8L),
                GTOreDictUnificator.get(dust, Materials.Platinum, 1L))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .fluidOutputs(WerkstoffLoader.Xenon.getFluidOrGas(1))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(BWNonMetaMaterialItems.Depleted_Tiberium_2.get(1))
            .itemOutputs(
                WerkstoffLoader.Zirconium.get(dust, 2),
                WerkstoffLoader.Zirconium.get(dust, 2),
                WerkstoffLoader.Tiberium.get(dust),
                WerkstoffLoader.Zirconium.get(dust, 4),
                GTOreDictUnificator.get(dust, Materials.TungstenSteel, 18L),
                GTOreDictUnificator.get(dust, Materials.Platinum, 2L))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .fluidOutputs(WerkstoffLoader.Xenon.getFluidOrGas(2))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(BWNonMetaMaterialItems.Depleted_Tiberium_4.get(1))
            .itemOutputs(
                WerkstoffLoader.Zirconium.get(dust, 4),
                WerkstoffLoader.Zirconium.get(dust, 4),
                WerkstoffLoader.Tiberium.get(dust, 2),
                WerkstoffLoader.Zirconium.get(dust, 8),
                GTOreDictUnificator.get(dust, Materials.TungstenSteel, 38L),
                GTOreDictUnificator.get(dust, Materials.Platinum, 4L))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .fluidOutputs(WerkstoffLoader.Xenon.getFluidOrGas(4))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(BWNonMetaMaterialItems.Depleted_TheCoreCell.get(1))
            .itemOutputs(
                ItemList.Depleted_Naquadah_4.get(8),
                WerkstoffLoader.Zirconium.get(dust, 64),
                WerkstoffLoader.Zirconium.get(dust, 64),
                GTOreDictUnificator.get(dust, Materials.TungstenSteel, 64L),
                GTOreDictUnificator.get(dust, Materials.TungstenSteel, 64L),
                GTOreDictUnificator.get(dust, Materials.TungstenSteel, 48L))
            .outputChances(100_00, 50_00, 50_00, 100_00, 100_00, 100_00)
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

    }
}
