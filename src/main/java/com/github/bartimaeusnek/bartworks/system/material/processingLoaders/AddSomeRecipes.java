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

package com.github.bartimaeusnek.bartworks.system.material.processingLoaders;

import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import com.github.bartimaeusnek.bartworks.system.material.BW_NonMeta_MaterialItems;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;

public class AddSomeRecipes implements Runnable {

    public void run() {

        GT_Values.RA.stdBuilder().itemInputs(BW_NonMeta_MaterialItems.Depleted_Tiberium_1.get(1))
                .itemOutputs(
                        WerkstoffLoader.Zirconium.get(dust),
                        WerkstoffLoader.Zirconium.get(dust),
                        WerkstoffLoader.Tiberium.get(dustSmall, 2),
                        WerkstoffLoader.Zirconium.get(dust, 2),
                        GT_OreDictUnificator.get(dust, Materials.TungstenSteel, 8L),
                        GT_OreDictUnificator.get(dust, Materials.Platinum, 1L))
                .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
                .fluidOutputs(WerkstoffLoader.Xenon.getFluidOrGas(1)).duration(12 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_EV).addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(BW_NonMeta_MaterialItems.Depleted_Tiberium_2.get(1))
                .itemOutputs(
                        WerkstoffLoader.Zirconium.get(dust, 2),
                        WerkstoffLoader.Zirconium.get(dust, 2),
                        WerkstoffLoader.Tiberium.get(dust),
                        WerkstoffLoader.Zirconium.get(dust, 4),
                        GT_OreDictUnificator.get(dust, Materials.TungstenSteel, 18L),
                        GT_OreDictUnificator.get(dust, Materials.Platinum, 2L))
                .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
                .fluidOutputs(WerkstoffLoader.Xenon.getFluidOrGas(2)).duration(12 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_EV).addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(BW_NonMeta_MaterialItems.Depleted_Tiberium_4.get(1))
                .itemOutputs(
                        WerkstoffLoader.Zirconium.get(dust, 4),
                        WerkstoffLoader.Zirconium.get(dust, 4),
                        WerkstoffLoader.Tiberium.get(dust, 2),
                        WerkstoffLoader.Zirconium.get(dust, 8),
                        GT_OreDictUnificator.get(dust, Materials.TungstenSteel, 38L),
                        GT_OreDictUnificator.get(dust, Materials.Platinum, 4L))
                .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
                .fluidOutputs(WerkstoffLoader.Xenon.getFluidOrGas(4)).duration(50 * SECONDS).eut(TierEU.RECIPE_EV)
                .addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(BW_NonMeta_MaterialItems.Depleted_TheCoreCell.get(1))
                .itemOutputs(
                        ItemList.Depleted_Naquadah_4.get(8),
                        WerkstoffLoader.Zirconium.get(dust, 64),
                        WerkstoffLoader.Zirconium.get(dust, 64),
                        GT_OreDictUnificator.get(dust, Materials.TungstenSteel, 64L),
                        GT_OreDictUnificator.get(dust, Materials.TungstenSteel, 64L),
                        GT_OreDictUnificator.get(dust, Materials.TungstenSteel, 48L))
                .outputChances(100_00, 50_00, 50_00, 100_00, 100_00, 100_00).duration(1 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_IV).addTo(sCentrifugeRecipes);

    }
}
