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

package bartworks.system.material.werkstoff_loaders.recipe;

import static gregtech.api.enums.OrePrefixes.crushed;
import static gregtech.api.enums.OrePrefixes.crushedCentrifuged;
import static gregtech.api.enums.OrePrefixes.crushedPurified;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustImpure;
import static gregtech.api.enums.OrePrefixes.dustPure;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.nugget;
import static gregtech.api.enums.OrePrefixes.ore;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.electroMagneticSeparatorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import bartworks.util.BWUtil;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

/// Furnace smelting, hand-crafted downgrade recipes, and the [SubTag]-gated ore-washing and electromagnetic-
/// separation bonus recipes for a werkstoff's ore/crushed/dust chain. No werkstoff currently carries
/// [SubTag#WASHING_MERCURY] or [SubTag#WASHING_SODIUMPERSULFATE], so the washing branches are presently inert.
///
/// The unconditional hammer, macerator, ore-washer, thermal-centrifuge and centrifuge steps of the chain are
/// generated canonically instead: `gregtech.loaders.oreprocessing.ProcessingDirty`/`ProcessingPure`/
/// `ProcessingCrushedOre` cover any material with a MaterialLib ore/dust shape and a legacy bridge material,
/// reached through `gregtech.loaders.shapeconsumers.ConsumerDirty`/`ConsumerPure`/`ConsumerCrushedOre`. The
/// crystallisable-gem autoclave recipes are likewise canonical, driven by `GTMaterialFlag.CRYSTALLISABLE` in
/// `gregtech.loaders.oreprocessing.ProcessingDust`, so this loader no longer registers them.
///
/// The electromagnetic-separator byproduct branch stays: [Werkstoff#contains] resolves a [SubTag] transitively
/// through a compound's composition, so some werkstoffe (e.g. Chromo-Alumino-Povondraite) carry
/// `ELECTROMAGNETIC_SEPERATION_IRON` this way without their own bridge material carrying the matching
/// `GTMaterialFlag`, and `ProcessingDust`'s flag-driven dispatch never fires for them; those materials have no
/// canonical byproduct recipe at all. Materials that carry the `SubTag` directly keep a canonical duplicate
/// coexisting alongside this branch's recipe.
public class CrushedLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        if (!werkstoff.hasItemType(ore) || !werkstoff.hasItemType(dust)) return;

        if (werkstoff.hasItemType(ingot) && !werkstoff.getStats()
            .isBlastFurnace()) {
            if (Werkstoff.Types.ELEMENT.equals(werkstoff.getType())) {
                GTModHandler.addSmeltingRecipe(werkstoff.get(crushed), werkstoff.get(nugget, 10));
                GTModHandler.addSmeltingRecipe(werkstoff.get(crushedPurified), werkstoff.get(nugget, 10));
                GTModHandler.addSmeltingRecipe(werkstoff.get(crushedCentrifuged), werkstoff.get(nugget, 10));
            } else {
                GTModHandler.addSmeltingRecipe(werkstoff.get(crushed), werkstoff.get(ingot));
                GTModHandler.addSmeltingRecipe(werkstoff.get(crushedPurified), werkstoff.get(ingot));
                GTModHandler.addSmeltingRecipe(werkstoff.get(crushedCentrifuged), werkstoff.get(ingot));
            }
            GTModHandler.addSmeltingRecipe(werkstoff.get(dustImpure), werkstoff.get(ingot));
            GTModHandler.addSmeltingRecipe(werkstoff.get(dustPure), werkstoff.get(ingot));
            GTModHandler.addSmeltingRecipe(werkstoff.get(dust), werkstoff.get(ingot));
        }

        GTModHandler.addCraftingRecipe(
            werkstoff.get(dustImpure),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "h  ", "W  ", 'W', werkstoff.get(crushed) });
        GTModHandler.addCraftingRecipe(
            werkstoff.get(dustPure),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "h  ", "W  ", 'W', werkstoff.get(crushedPurified) });
        GTModHandler.addCraftingRecipe(
            werkstoff.get(dust),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "h  ", "W  ", 'W', werkstoff.get(crushedCentrifuged) });

        if (werkstoff.contains(SubTag.WASHING_MERCURY)) {

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(crushed))
                .itemOutputs(
                    werkstoff.get(crushedPurified),
                    werkstoff.getOreByProduct(1, dust),
                    GTOreDictUnificator.get(dust, Materials.Stone, 1L))
                .outputChances(10000, 7000, 4000)
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
                .duration(40 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, (int) TierEU.RECIPE_ULV))
                .addTo(chemicalBathRecipes);

        }
        if (werkstoff.contains(SubTag.WASHING_SODIUMPERSULFATE)) {

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(crushed))
                .itemOutputs(
                    werkstoff.get(crushedPurified),
                    werkstoff.getOreByProduct(1, dust),
                    GTOreDictUnificator.get(dust, Materials.Stone, 1L))
                .outputChances(10000, 7000, 4000)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SodiumPersulfate,
                        Materials2FluidShapes.fluidLiquid,
                        (int) (100)))
                .duration(40 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, (int) TierEU.RECIPE_ULV))
                .addTo(chemicalBathRecipes);

        }
        if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD)) {

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dustPure))
                .itemOutputs(
                    werkstoff.get(dust),
                    GTOreDictUnificator.get(dustSmall, Materials.Gold, 1L),
                    GTOreDictUnificator.get(nugget, Materials.Gold, 1L))
                .outputChances(10000, 4000, 2000)
                .duration(20 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 24))
                .addTo(electroMagneticSeparatorRecipes);

        } else if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)) {

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dustPure))
                .itemOutputs(
                    werkstoff.get(dust),
                    GTOreDictUnificator.get(dustSmall, Materials.Iron, 1L),
                    GTOreDictUnificator.get(nugget, Materials.Iron, 1L))
                .outputChances(10000, 4000, 2000)
                .duration(20 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 24))
                .addTo(electroMagneticSeparatorRecipes);

        } else if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM)) {

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(dustPure))
                .itemOutputs(
                    werkstoff.get(dust),
                    GTOreDictUnificator.get(dustSmall, Materials.Neodymium, 1L),
                    GTOreDictUnificator.get(nugget, Materials.Neodymium, 1L))
                .outputChances(10000, 4000, 2000)
                .duration(20 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 24))
                .addTo(electroMagneticSeparatorRecipes);

        }
    }
}
