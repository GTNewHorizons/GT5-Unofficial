package gregtech.loaders.postload.chains;

import static bartworks.system.material.WerkstoffLoader.FluorBuergerit;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.plasmaForgeRecipes;
import static gregtech.api.recipe.RecipeMaps.purificationClarifierRecipes;
import static gregtech.api.recipe.RecipeMaps.purificationDegasifierRecipes;
import static gregtech.api.recipe.RecipeMaps.purificationFlocculationRecipes;
import static gregtech.api.recipe.RecipeMaps.purificationOzonationRecipes;
import static gregtech.api.recipe.RecipeMaps.purificationParticleExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.purificationPhAdjustmentRecipes;
import static gregtech.api.recipe.RecipeMaps.purificationPlasmaHeatingRecipes;
import static gregtech.api.recipe.RecipeMaps.purificationUVTreatmentRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.maps.PurificationUnitParticleExtractorFrontend;
import gregtech.api.recipe.metadata.PurificationPlantBaseChanceKey;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationPlant;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitUVTreatment;
import gtPlusPlus.core.material.Particle;

public class PurifiedWaterRecipes {

    static final PurificationPlantBaseChanceKey BASE_CHANCE = PurificationPlantBaseChanceKey.INSTANCE;
    public static final int extraBaryonicOutput = 500;

    public static void run() {
        final int duration = MTEPurificationPlant.CYCLE_TIME_TICKS;

        // Grade 1 - Clarifier
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ActivatedCarbonFilterMesh.get(1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(Materials.Grade1PurifiedWater.getFluid(900))
            .itemOutputs(new ItemStack(Items.stick, 1), Materials.Stone.getDust(1), Materials.Gold.getNuggets(1))
            .outputChances(1000, 500, 100)
            .duration(duration)
            .eut(TierEU.RECIPE_LuV)
            .metadata(BASE_CHANCE, 70.0f)
            .addTo(purificationClarifierRecipes);

        // Activated Carbon Line
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(1))
            .fluidInputs(Materials.PhosphoricAcid.getFluid(1_000))
            .itemOutputs(Materials.PreActivatedCarbon.getDust(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.PreActivatedCarbon.getDust(1))
            .itemOutputs(Materials.DirtyActivatedCarbon.getDust(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .specialValue((int) HeatingCoilLevel.EV.getHeat())
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.DirtyActivatedCarbon.getDust(1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .itemOutputs(Materials.ActivatedCarbon.getDust(1))
            .fluidOutputs(Materials.PhosphoricAcid.getFluid(1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.ActivatedCarbon.getDust(64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Zinc, 16))
            .itemOutputs(ItemList.ActivatedCarbonFilterMesh.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Grade 2 - Ozonation
        for (ItemStack lens : GTOreDictUnificator.getOres("craftingLensBlue")) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(0, lens))
                .fluidInputs(Materials.Air.getGas(10_000))
                .fluidOutputs(Materials.Ozone.getGas(2_000))
                .duration(1 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(laserEngraverRecipes);
        }

        // Recipes for ozonation, uses 128kL, 256kL, 512kL, 1m kL at each tier
        // 20% boost per tier
        // Gets you up to 80%, need to water boost for 95%
        for (int tier = 1; tier <= 4; ++tier) {
            GTValues.RA.stdBuilder()
                .fluidInputs(
                    Materials.Grade1PurifiedWater.getFluid(1_000),
                    Materials.Ozone.getGas(1000 * (long) GTUtility.powInt(2, (tier + 6))))
                .fluidOutputs(Materials.Grade2PurifiedWater.getFluid(900))
                .itemOutputs(Materials.Manganese.getDust(1), Materials.Iron.getDust(1), Materials.Sulfur.getDust(1))
                .outputChances(500, 500, 500)
                .duration(duration)
                .ignoreCollision()
                .eut(TierEU.RECIPE_LuV)
                .metadata(BASE_CHANCE, tier * 20.0f)
                .addTo(purificationOzonationRecipes);
        }

        // Grade 3 - Flocculation.
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.Grade2PurifiedWater.getFluid(1_000),
                Materials.PolyAluminiumChloride.getFluid(100_000))
            .fluidOutputs(
                Materials.Grade3PurifiedWater.getFluid(900),
                Materials.FlocculationWasteLiquid.getFluid(100_000))
            .itemOutputs(
                new ItemStack(Items.clay_ball, 1),
                Materials.QuartzSand.getDust(1),
                Materials.PolyvinylChloride.getNuggets(1))
            .outputChances(1000, 500, 100)
            .duration(duration)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(BASE_CHANCE, 1 * 10.0f)
            .fake()
            .addTo(purificationFlocculationRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Grade2PurifiedWater.getFluid(1_000))
            .fluidOutputs(Materials.Grade3PurifiedWater.getFluid(900))
            .itemOutputs(
                new ItemStack(Items.clay_ball, 1),
                Materials.QuartzSand.getDust(1),
                Materials.PolyvinylChloride.getNuggets(1))
            .outputChances(1000, 500, 100)
            .duration(duration)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(BASE_CHANCE, 1 * 10.0f)
            .hidden()
            .addTo(purificationFlocculationRecipes);

        // 2 Al(OH)3 + 3 HCl -> Al2(OH)3 Cl3 + 3 H2O
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Aluminiumhydroxide.getDust(8))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(3_000))
            .fluidOutputs(Materials.PolyAluminiumChloride.getFluid(1_000), Materials.Water.getFluid(3_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.FlocculationWasteLiquid.getFluid(10_000))
            .itemOutputs(Materials.Aluminium.getDust(20))
            .fluidOutputs(Materials.Oxygen.getGas(30_000), Materials.HydrochloricAcid.getFluid(30_000))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(distillationTowerRecipes);

        // Grade 4 - pH adjustment
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(64))
            .fluidInputs(Materials.Grade3PurifiedWater.getFluid(1_000), Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Grade4PurifiedWater.getFluid(900))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(BASE_CHANCE, 0.0f)
            .fake()
            .addTo(purificationPhAdjustmentRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Grade3PurifiedWater.getFluid(1_000))
            .fluidOutputs(Materials.Grade4PurifiedWater.getFluid(900))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(BASE_CHANCE, 0.0f)
            .hidden()
            .addTo(purificationPhAdjustmentRecipes);

        // Grade 5 - Plasma Heating
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.Grade4PurifiedWater.getFluid(1_000),
                Materials.Helium.getPlasma(10L),
                Materials.SuperCoolant.getFluid(100L))
            .fluidOutputs(Materials.Grade5PurifiedWater.getFluid(900))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UV)
            .metadata(BASE_CHANCE, 0.0f)
            .fake()
            .addTo(purificationPlasmaHeatingRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Grade4PurifiedWater.getFluid(1_000))
            .fluidOutputs(Materials.Grade5PurifiedWater.getFluid(900))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UV)
            .metadata(BASE_CHANCE, 0.0f)
            .hidden()
            .addTo(purificationPlasmaHeatingRecipes);

        MTEPurificationUnitUVTreatment.LENS_ITEMS.add(GGMaterial.orundum.get(OrePrefixes.lens, 1));
        MTEPurificationUnitUVTreatment.LENS_ITEMS.add(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Amber, 1));
        MTEPurificationUnitUVTreatment.LENS_ITEMS
            .add(GTOreDictUnificator.get(OrePrefixes.lens, Materials.InfusedAir, 1));
        MTEPurificationUnitUVTreatment.LENS_ITEMS.add(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Emerald, 1));
        MTEPurificationUnitUVTreatment.LENS_ITEMS
            .add(GTOreDictUnificator.get(OrePrefixes.lens, Materials.ManaDiamond, 1));
        MTEPurificationUnitUVTreatment.LENS_ITEMS
            .add(GTOreDictUnificator.get(OrePrefixes.lens, Materials.BlueTopaz, 1));
        MTEPurificationUnitUVTreatment.LENS_ITEMS.add(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Amethyst, 1));
        MTEPurificationUnitUVTreatment.LENS_ITEMS.add(FluorBuergerit.get(OrePrefixes.lens, 1));
        MTEPurificationUnitUVTreatment.LENS_ITEMS
            .add(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Dilithium, 1));

        // Grade 6 - UV treatment
        GTValues.RA.stdBuilder()
            .itemInputs(MTEPurificationUnitUVTreatment.LENS_ITEMS.toArray(new ItemStack[9]))
            .fluidInputs(Materials.Grade5PurifiedWater.getFluid(1_000))
            .fluidOutputs(Materials.Grade6PurifiedWater.getFluid(900))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UV)
            .metadata(BASE_CHANCE, 0.0f)
            .fake()
            .addTo(purificationUVTreatmentRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Grade5PurifiedWater.getFluid(1_000))
            .fluidOutputs(Materials.Grade6PurifiedWater.getFluid(900))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UV)
            .metadata(BASE_CHANCE, 0.0f)
            .hidden()
            .addTo(purificationUVTreatmentRecipes);

        // Grade 7 - Degasification
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Grade6PurifiedWater.getFluid(1_000))
            .fluidOutputs(Materials.Grade7PurifiedWater.getFluid(900))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UHV)
            .metadata(BASE_CHANCE, 0.0f)
            .addTo(purificationDegasifierRecipes);

        // Grade 8 - Quark Extraction

        final ItemStack[] catalystInputs = new ItemStack[] { ItemList.Quark_Creation_Catalyst_Up.get(1L),
            ItemList.Quark_Creation_Catalyst_Down.get(1L), ItemList.Quark_Creation_Catalyst_Bottom.get(1L),
            ItemList.Quark_Creation_Catalyst_Top.get(1L), ItemList.Quark_Creation_Catalyst_Strange.get(1L),
            ItemList.Quark_Creation_Catalyst_Charm.get(1L) };

        // Add all combinations of input items to the frontend map
        for (int i = 0; i < catalystInputs.length; ++i) {
            for (int j = 1; j < catalystInputs.length; ++j) {
                PurificationUnitParticleExtractorFrontend.inputItems.add(catalystInputs[i]);
                PurificationUnitParticleExtractorFrontend.inputItemsShuffled
                    .add(catalystInputs[(i + j) % catalystInputs.length]);
            }
        }

        // Add re-alignment recipes
        for (int i = 0; i < catalystInputs.length; ++i) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Quark_Creation_Catalyst_Unaligned.get(1))
                .circuit(i + 1)
                .fluidInputs(Materials.StableBaryonicMatter.getFluid(1_000))
                .itemOutputs(catalystInputs[i])
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                // Not sure how I feel about it being in the laser engraver, but I don't see a better multi for it
                // right
                // now,
                // and we can't really add a new one specifically for this (... for now)
                .addTo(laserEngraverRecipes);
        }

        // Initial aligned quark catalysts, these are not meant to be done often, and simply exist to get you started
        final ItemStack[] quarks = new ItemStack[] { // make sure these are in the same order as the list above
            Particle.getBaseParticle(Particle.UP), Particle.getBaseParticle(Particle.DOWN),
            Particle.getBaseParticle(Particle.BOTTOM), Particle.getBaseParticle(Particle.TOP),
            Particle.getBaseParticle(Particle.STRANGE), Particle.getBaseParticle(Particle.CHARM) };

        for (int i = 0; i < catalystInputs.length; ++i) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Quark_Catalyst_Housing.get(1), quarks[i])
                .fluidInputs(Materials.ExcitedDTRC.getFluid(10_000))
                .itemOutputs(catalystInputs[i])
                .fluidOutputs(Materials.DTR.getFluid(5_000))
                .metadata(COIL_HEAT, 10800)
                .eut(TierEU.RECIPE_UMV)
                .duration(5 * MINUTES)
                .addTo(plasmaForgeRecipes);
        }

        // Recipe for quark catalyst housing
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Electromagnet_Tengam.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES, TierEU.RECIPE_UHV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Tritanium, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 16 },
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 8 },
                ItemList.Field_Generator_UEV.get(1))
            .fluidInputs(
                Materials.Neutronium.getMolten(16 * INGOTS),
                Materials.Infinity.getMolten(16 * INGOTS),
                Materials.Tritanium.getMolten(16 * INGOTS),
                Materials.CosmicNeutronium.getMolten(16 * INGOTS))
            .itemOutputs(ItemList.Quark_Catalyst_Housing.get(1))
            .eut(TierEU.RECIPE_UIV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            // Fake item inputs
            .itemInputs(catalystInputs)
            .fluidInputs(Materials.Grade7PurifiedWater.getFluid(1_000))
            .fluidOutputs(
                Materials.Grade8PurifiedWater.getFluid(900),
                Materials.StableBaryonicMatter.getFluid(extraBaryonicOutput))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Unaligned.get(2L), Particle.getBaseParticle(Particle.UNKNOWN))
            .outputChances(10000, 100)
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UEV)
            .metadata(BASE_CHANCE, 0.0f)
            .fake()
            .addTo(purificationParticleExtractionRecipes);

        // real recipe
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Grade7PurifiedWater.getFluid(1_000))
            .fluidOutputs(
                Materials.Grade8PurifiedWater.getFluid(900),
                Materials.StableBaryonicMatter.getFluid(extraBaryonicOutput))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Unaligned.get(2L), Particle.getBaseParticle(Particle.UNKNOWN))
            .outputChances(10000, 100)
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UEV)
            .metadata(BASE_CHANCE, 0.0f)
            .hidden()
            .addTo(purificationParticleExtractionRecipes);
    }
}
