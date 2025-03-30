package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.enums.OrePrefixes.bolt;
import static gregtech.api.enums.OrePrefixes.gearGt;
import static gregtech.api.enums.OrePrefixes.gearGtSmall;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.itemCasing;
import static gregtech.api.enums.OrePrefixes.pipeHuge;
import static gregtech.api.enums.OrePrefixes.pipeLarge;
import static gregtech.api.enums.OrePrefixes.pipeMedium;
import static gregtech.api.enums.OrePrefixes.pipeSmall;
import static gregtech.api.enums.OrePrefixes.pipeTiny;
import static gregtech.api.enums.OrePrefixes.ring;
import static gregtech.api.enums.OrePrefixes.rotor;
import static gregtech.api.enums.OrePrefixes.stick;
import static gregtech.api.enums.OrePrefixes.toolHeadDrill;
import static gregtech.api.enums.OrePrefixes.turbineBlade;
import static gregtech.api.recipe.RecipeMaps.steamConformerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;

public class SteamConformerRecipes implements Runnable {

    @Override
    public void run() {

        Materials[] Tier1Materials = { Materials.Bronze, Materials.Iron, Materials.Copper, Materials.Tin,
            Materials.Brass, Materials.Steel, Materials.WroughtIron, Materials.Breel, Materials.Stronze,
            Materials.CompressedSteam, Materials.CrudeSteel, Materials.Rubber, Materials.Clay };

        for (Materials aMaterial : Tier1Materials) {

            if (GTOreDictUnificator.get(stick, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(ingot, aMaterial, 2), ItemList.Shape_Extruder_Rod.get(0))
                    .itemOutputs(GTOreDictUnificator.get(stick, aMaterial, 3))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }

            if (GTOreDictUnificator.get(pipeTiny, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(ingot, aMaterial, 2), ItemList.Shape_Extruder_Pipe_Tiny.get(0))
                    .itemOutputs(GTOreDictUnificator.get(pipeTiny, aMaterial, 3))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }

            if (GTOreDictUnificator.get(pipeSmall, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(ingot, aMaterial, 3), ItemList.Shape_Extruder_Pipe_Small.get(0))
                    .itemOutputs(GTOreDictUnificator.get(pipeSmall, aMaterial, 2))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }

            if (GTOreDictUnificator.get(pipeMedium, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(ingot, aMaterial, 9),
                        ItemList.Shape_Extruder_Pipe_Medium.get(0))
                    .itemOutputs(GTOreDictUnificator.get(pipeMedium, aMaterial, 2))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }

            if (GTOreDictUnificator.get(pipeLarge, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(ingot, aMaterial, 18),
                        ItemList.Shape_Extruder_Pipe_Large.get(0))
                    .itemOutputs(GTOreDictUnificator.get(pipeLarge, aMaterial, 2))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }

            if (GTOreDictUnificator.get(pipeHuge, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(ingot, aMaterial, 36), ItemList.Shape_Extruder_Pipe_Huge.get(0))
                    .itemOutputs(GTOreDictUnificator.get(pipeHuge, aMaterial, 2))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }

            if (GTOreDictUnificator.get(rotor, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(ingot, aMaterial, 15), ItemList.Shape_Extruder_Rotor.get(0))
                    .itemOutputs(GTOreDictUnificator.get(rotor, aMaterial, 2))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }

            if (GTOreDictUnificator.get(itemCasing, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(ingot, aMaterial, 2), ItemList.Shape_Extruder_Casing.get(0))
                    .itemOutputs(GTOreDictUnificator.get(itemCasing, aMaterial, 3))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }

            if (GTOreDictUnificator.get(ring, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(ingot, aMaterial, 2), ItemList.Shape_Extruder_Ring.get(0))
                    .itemOutputs(GTOreDictUnificator.get(ring, aMaterial, 6))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }

            if (GTOreDictUnificator.get(gearGtSmall, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(ingot, aMaterial, 3), ItemList.Shape_Extruder_Small_Gear.get(0))
                    .itemOutputs(GTOreDictUnificator.get(gearGtSmall, aMaterial, 2))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }

            if (GTOreDictUnificator.get(turbineBlade, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(ingot, aMaterial, 18),
                        ItemList.Shape_Extruder_Turbine_Blade.get(0))
                    .itemOutputs(GTOreDictUnificator.get(turbineBlade, aMaterial, 2))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }

            if (GTOreDictUnificator.get(toolHeadDrill, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(ingot, aMaterial, 12),
                        ItemList.Shape_Extruder_ToolHeadDrill.get(0))
                    .itemOutputs(GTOreDictUnificator.get(toolHeadDrill, aMaterial, 2))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }

            if (GTOreDictUnificator.get(gearGt, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(ingot, aMaterial, 12), ItemList.Shape_Extruder_Gear.get(0))
                    .itemOutputs(GTOreDictUnificator.get(gearGt, aMaterial, 2))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }

            if (GTOreDictUnificator.get(bolt, aMaterial, 1) != null) {
                RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(ingot, aMaterial, 1), ItemList.Shape_Extruder_Bolt.get(0))
                    .itemOutputs(GTOreDictUnificator.get(bolt, aMaterial, 6))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(steamConformerRecipes);
            }
        }

    }
}
