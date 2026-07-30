package gregtech.loaders.postload.chains;

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

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.recipe.maps.PurificationUnitParticleExtractorFrontend;
import gregtech.api.recipe.metadata.PurificationPlantBaseChanceKey;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationPlant;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitUVTreatment;
import gtPlusPlus.core.item.chemistry.Particle;

public class PurifiedWaterRecipes {

    static final PurificationPlantBaseChanceKey BASE_CHANCE = PurificationPlantBaseChanceKey.INSTANCE;
    public static final int extraBaryonicOutput = 500;

    public static void run() {
        final int duration = MTEPurificationPlant.CYCLE_TIME_TICKS;

        // Grade 1 - Clarifier
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ActivatedCarbonFilterMesh.get(1))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Grade1PurifiedWater, FluidShapes.fluidLiquid, (int) (900)))
            .itemOutputs(
                new ItemStack(Items.stick, 1),
                MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, (int) (1)),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 1))
            .outputChances(1000, 500, 100)
            .duration(duration)
            .eut(TierEU.RECIPE_LuV)
            .metadata(BASE_CHANCE, 70.0f)
            .addTo(purificationClarifierRecipes);

        // Activated Carbon Line
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.PreActivatedCarbon, Shapes.dust, (int) (1)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PreActivatedCarbon, Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.carbonactivateddirty, Shapes.dust, 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .specialValue((int) HeatingCoilLevel.EV.getHeat())
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.carbonactivateddirty, Shapes.dust, 1))
            .fluidInputs(GTUtility.getWater(1_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.ActivatedCarbon, Shapes.dust, (int) (1)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ActivatedCarbon, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.foil, (int) (16)))
            .itemOutputs(ItemList.ActivatedCarbonFilterMesh.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Grade 2 - Ozonation
        for (ItemStack lens : GTOreDictUnificator.getOres("craftingLensBlue")) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(0, lens))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Air, FluidShapes.fluidGas, (int) (10_000)))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ozone, FluidShapes.fluidGas, (int) (2_000)))
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
                    MaterialLibAPI.getFluidStack(Materials.Grade1PurifiedWater, FluidShapes.fluidLiquid, (int) (1_000)),
                    MaterialLibAPI.getFluidStack(
                        Materials.Ozone,
                        FluidShapes.fluidGas,
                        (int) (1000 * (long) GTUtility.powInt(2, (tier + 6)))))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(Materials.Grade2PurifiedWater, FluidShapes.fluidLiquid, (int) (900)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, (int) (1)))
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
                MaterialLibAPI.getFluidStack(Materials.Grade2PurifiedWater, FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.PolyaluminiumChloride, FluidShapes.fluidLiquid, 100_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Grade3PurifiedWater, FluidShapes.fluidLiquid, (int) (900)),
                MaterialLibAPI
                    .getFluidStack(Materials.FlocculationWasteLiquid, FluidShapes.fluidLiquid, (int) (100_000)))
            .itemOutputs(
                new ItemStack(Items.clay_ball, 1),
                MaterialLibAPI.getStack(Materials.QuartzSand, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.PolyvinylChloride, Shapes.nugget, (int) (1)))
            .outputChances(1000, 500, 100)
            .duration(duration)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(BASE_CHANCE, 1 * 10.0f)
            .fake()
            .addTo(purificationFlocculationRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Grade2PurifiedWater, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Grade3PurifiedWater, FluidShapes.fluidLiquid, (int) (900)))
            .itemOutputs(
                new ItemStack(Items.clay_ball, 1),
                MaterialLibAPI.getStack(Materials.QuartzSand, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.PolyvinylChloride, Shapes.nugget, (int) (1)))
            .outputChances(1000, 500, 100)
            .duration(duration)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(BASE_CHANCE, 1 * 10.0f)
            .hidden()
            .addTo(purificationFlocculationRecipes);

        // 2 Al(OH)3 + 3 HCl -> Al2(OH)3 Cl3 + 3 H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.AluminiumHydroxide, Shapes.dust, 8))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.PolyaluminiumChloride, FluidShapes.fluidLiquid, 1_000),
                GTUtility.getWater(3_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.FlocculationWasteLiquid, FluidShapes.fluidLiquid, (int) (10_000)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, (int) (20)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (30_000)),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 30_000))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(distillationTowerRecipes);

        // Grade 4 - pH adjustment
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Grade3PurifiedWater, FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Grade4PurifiedWater, FluidShapes.fluidLiquid, (int) (900)))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(BASE_CHANCE, 0.0f)
            .fake()
            .addTo(purificationPhAdjustmentRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Grade3PurifiedWater, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Grade4PurifiedWater, FluidShapes.fluidLiquid, (int) (900)))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(BASE_CHANCE, 0.0f)
            .hidden()
            .addTo(purificationPhAdjustmentRecipes);

        // Grade 5 - Plasma Heating
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Grade4PurifiedWater, FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, (int) (100)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Grade5PurifiedWater, FluidShapes.fluidLiquid, (int) (900)))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UV)
            .metadata(BASE_CHANCE, 0.0f)
            .fake()
            .addTo(purificationPlasmaHeatingRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Grade4PurifiedWater, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Grade5PurifiedWater, FluidShapes.fluidLiquid, (int) (900)))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UV)
            .metadata(BASE_CHANCE, 0.0f)
            .hidden()
            .addTo(purificationPlasmaHeatingRecipes);

        MTEPurificationUnitUVTreatment.LENS_ITEMS.add(MaterialLibAPI.getStack(Materials.Orundum, Shapes.lens, 1));
        MTEPurificationUnitUVTreatment.LENS_ITEMS.add(MaterialLibAPI.getStack(Materials.Amber, Shapes.lens, (int) (1)));
        MTEPurificationUnitUVTreatment.LENS_ITEMS
            .add(MaterialLibAPI.getStack(Materials.InfusedAir, Shapes.lens, (int) (1)));
        MTEPurificationUnitUVTreatment.LENS_ITEMS
            .add(MaterialLibAPI.getStack(Materials.Emerald, Shapes.lens, (int) (1)));
        MTEPurificationUnitUVTreatment.LENS_ITEMS
            .add(MaterialLibAPI.getStack(Materials.ManaDiamond, Shapes.lens, (int) (1)));
        MTEPurificationUnitUVTreatment.LENS_ITEMS
            .add(MaterialLibAPI.getStack(Materials.BlueTopaz, Shapes.lens, (int) (1)));
        MTEPurificationUnitUVTreatment.LENS_ITEMS
            .add(MaterialLibAPI.getStack(Materials.Amethyst, Shapes.lens, (int) (1)));
        MTEPurificationUnitUVTreatment.LENS_ITEMS
            .add(MaterialLibAPI.getStack(Materials.FluorBuergerite, Shapes.lens, 1));
        MTEPurificationUnitUVTreatment.LENS_ITEMS
            .add(MaterialLibAPI.getStack(Materials.Dilithium, Shapes.lens, (int) (1)));

        // Grade 6 - UV treatment
        GTValues.RA.stdBuilder()
            .itemInputs(MTEPurificationUnitUVTreatment.LENS_ITEMS.toArray(new ItemStack[9]))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Grade5PurifiedWater, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Grade6PurifiedWater, FluidShapes.fluidLiquid, (int) (900)))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UV)
            .metadata(BASE_CHANCE, 0.0f)
            .fake()
            .addTo(purificationUVTreatmentRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Grade5PurifiedWater, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Grade6PurifiedWater, FluidShapes.fluidLiquid, (int) (900)))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UV)
            .metadata(BASE_CHANCE, 0.0f)
            .hidden()
            .addTo(purificationUVTreatmentRecipes);

        // Grade 7 - Degasification
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Grade6PurifiedWater, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Grade7PurifiedWater, FluidShapes.fluidLiquid, (int) (900)))
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
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.stablebaryonicmatter, FluidShapes.fluidLiquid, 1_000))
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
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.ExcitedDTRC, FluidShapes.fluidLiquid, (int) (10_000)))
                .itemOutputs(catalystInputs[i])
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.DimensionallyTranscendentResidue, FluidShapes.fluidLiquid, 5_000))
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
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.plate, (int) (16)),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plate, (int) (16)),
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.plate, (int) (16)),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.plate, (int) (16)),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, (int) (64)),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.wireFine, (int) (64)),
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.wireFine, (int) (64)),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.wireFine, (int) (64)),
                new Object[] { Circuits.UHV.getIngredient(), 16 },
                new Object[] { Circuits.UEV.getIngredient(), 8 },
                ItemList.Field_Generator_UEV.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, (int) (16 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, (int) (16 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Tritanium, FluidShapes.fluidMolten, (int) (16 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.CosmicNeutronium, FluidShapes.fluidMolten, (int) (16 * INGOTS)))
            .itemOutputs(ItemList.Quark_Catalyst_Housing.get(1))
            .eut(TierEU.RECIPE_UIV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            // Fake item inputs
            .itemInputs(catalystInputs)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Grade7PurifiedWater, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Grade8PurifiedWater, FluidShapes.fluidLiquid, (int) (900)),
                MaterialLibAPI
                    .getFluidStack(Materials.stablebaryonicmatter, FluidShapes.fluidLiquid, extraBaryonicOutput))
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Grade7PurifiedWater, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Grade8PurifiedWater, FluidShapes.fluidLiquid, (int) (900)),
                MaterialLibAPI
                    .getFluidStack(Materials.stablebaryonicmatter, FluidShapes.fluidLiquid, extraBaryonicOutput))
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
