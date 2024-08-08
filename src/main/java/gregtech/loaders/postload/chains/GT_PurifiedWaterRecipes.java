package gregtech.loaders.postload.chains;

import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.FluorBuergerit;
import static gregtech.api.enums.MaterialsBotania.ManaDiamond;
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
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;
import static gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitParticleExtractor.BARYONIC_MATTER_OUTPUT;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.maps.PurificationUnitParticleExtractorFrontend;
import gregtech.api.recipe.metadata.PurificationPlantBaseChanceKey;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationPlant;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitUVTreatment;
import gtPlusPlus.core.material.Particle;

public class GT_PurifiedWaterRecipes {

    static final PurificationPlantBaseChanceKey BASE_CHANCE = PurificationPlantBaseChanceKey.INSTANCE;

    public static void run() {
        final int duration = GT_MetaTileEntity_PurificationPlant.CYCLE_TIME_TICKS;

        // Grade 1 - Clarifier
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.ActivatedCarbonFilterMesh.get(1))
            .fluidInputs(GT_ModHandler.getWater(1000L))
            .fluidOutputs(Materials.Grade1PurifiedWater.getFluid(900L))
            .itemOutputs(new ItemStack(Items.stick, 1), Materials.Stone.getDust(1), Materials.Gold.getNuggets(1))
            .outputChances(1000, 500, 100)
            .duration(duration)
            .eut(TierEU.RECIPE_LuV)
            .metadata(BASE_CHANCE, 70.0f)
            .addTo(purificationClarifierRecipes);

        // Activated Carbon Line
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(1))
            .fluidInputs(Materials.PhosphoricAcid.getFluid(1000L))
            .itemOutputs(Materials.PreActivatedCarbon.getDust(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.PreActivatedCarbon.getDust(1))
            .itemOutputs(Materials.DirtyActivatedCarbon.getDust(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .specialValue((int) HeatingCoilLevel.EV.getHeat())
            .addTo(blastFurnaceRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.DirtyActivatedCarbon.getDust(1))
            .fluidInputs(Materials.Water.getFluid(1000L))
            .itemOutputs(Materials.ActivatedCarbon.getDust(1))
            .fluidOutputs(Materials.PhosphoricAcid.getFluid(1000L))
            .noOptimize()
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.ActivatedCarbon.getDust(64),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Zinc, 16))
            .itemOutputs(ItemList.ActivatedCarbonFilterMesh.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Grade 2 - Ozonation
        for (ItemStack lens : GT_OreDictUnificator.getOres("craftingLensBlue")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(0, lens))
                .noOptimize()
                .fluidInputs(Materials.Air.getGas(10000L))
                .fluidOutputs(Materials.Ozone.getGas(2000L))
                .duration(1 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(laserEngraverRecipes);
        }

        // Recipes for ozonation, uses 128kL, 256kL, 512kL, 1m kL at each tier
        // 20% boost per tier
        // Gets you up to 80%, need to water boost for 95%
        for (int tier = 1; tier <= 4; ++tier) {
            GT_Values.RA.stdBuilder()
                .noOptimize()
                .fluidInputs(
                    Materials.Grade1PurifiedWater.getFluid(1000L),
                    Materials.Ozone.getGas(1000 * (long) Math.pow(2, (tier + 6))))
                .fluidOutputs(Materials.Grade2PurifiedWater.getFluid(900L))
                .itemOutputs(Materials.Manganese.getDust(1), Materials.Iron.getDust(1), Materials.Sulfur.getDust(1))
                .outputChances(500, 500, 500)
                .duration(duration)
                .ignoreCollision()
                .eut(TierEU.RECIPE_LuV)
                .metadata(BASE_CHANCE, tier * 20.0f)
                .addTo(purificationOzonationRecipes);
        }

        // Grade 3 - Flocculation.
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Grade2PurifiedWater.getFluid(1000L))
            .fluidOutputs(Materials.Grade3PurifiedWater.getFluid(900L))
            .itemOutputs(
                new ItemStack(Items.clay_ball, 1),
                Materials.QuartzSand.getDust(1),
                Materials.PolyvinylChloride.getNuggets(1))
            .outputChances(1000, 500, 100)
            .duration(duration)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(BASE_CHANCE, 1 * 10.0f)
            .addTo(purificationFlocculationRecipes);

        // 2 Al(OH)3 + 3 HCl -> Al2(OH)3 Cl3 + 3 H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Aluminiumhydroxide.getDust(8))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(3000L))
            .fluidOutputs(Materials.PolyAluminiumChloride.getFluid(1000L), Materials.Water.getFluid(3000L))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // Diluted is twice what chem balance would suggest, but it is 2:1 with hydrochloric acid which makes it
        // correct I believe.
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.FlocculationWasteLiquid.getFluid(1000L))
            .itemOutputs(Materials.Aluminium.getDust(2))
            .fluidOutputs(Materials.Oxygen.getGas(3000L), Materials.DilutedHydrochloricAcid.getFluid(6000L))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(distillationTowerRecipes);

        // Grade 4 - pH adjustment
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Grade3PurifiedWater.getFluid(1000L))
            .fluidOutputs(Materials.Grade4PurifiedWater.getFluid(900L))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(BASE_CHANCE, 0.0f)
            .addTo(purificationPhAdjustmentRecipes);

        // Grade 5 - Plasma Heating
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Grade4PurifiedWater.getFluid(1000L))
            .fluidOutputs(Materials.Grade5PurifiedWater.getFluid(900L))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UV)
            .metadata(BASE_CHANCE, 0.0f)
            .addTo(purificationPlasmaHeatingRecipes);

        GT_MetaTileEntity_PurificationUnitUVTreatment.LENS_ITEMS.add(MyMaterial.orundum.get(OrePrefixes.lens, 1));
        GT_MetaTileEntity_PurificationUnitUVTreatment.LENS_ITEMS
            .add(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Amber, 1));
        GT_MetaTileEntity_PurificationUnitUVTreatment.LENS_ITEMS
            .add(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.InfusedAir, 1));
        GT_MetaTileEntity_PurificationUnitUVTreatment.LENS_ITEMS
            .add(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Emerald, 1));
        GT_MetaTileEntity_PurificationUnitUVTreatment.LENS_ITEMS
            .add(GT_OreDictUnificator.get(OrePrefixes.lens, ManaDiamond, 1));
        GT_MetaTileEntity_PurificationUnitUVTreatment.LENS_ITEMS
            .add(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.BlueTopaz, 1));
        GT_MetaTileEntity_PurificationUnitUVTreatment.LENS_ITEMS
            .add(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Amethyst, 1));
        GT_MetaTileEntity_PurificationUnitUVTreatment.LENS_ITEMS.add(FluorBuergerit.get(OrePrefixes.lens, 1));
        GT_MetaTileEntity_PurificationUnitUVTreatment.LENS_ITEMS
            .add(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Dilithium, 1));

        // Grade 6 - UV treatment
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Grade5PurifiedWater.getFluid(1000L))
            .fluidOutputs(Materials.Grade6PurifiedWater.getFluid(900L))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UV)
            .metadata(BASE_CHANCE, 0.0f)
            .addTo(purificationUVTreatmentRecipes);

        // Grade 7 - Degasification
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Grade6PurifiedWater.getFluid(1000L))
            .fluidOutputs(Materials.Grade7PurifiedWater.getFluid(900L))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UHV)
            .metadata(BASE_CHANCE, 0.0f)
            .addTo(purificationDegasifierRecipes);

        // Grade 8 - Quark Extraction

        ItemStack[] catalystInputs = new ItemStack[] { ItemList.Quark_Creation_Catalyst_Up.get(1L),
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
            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Quark_Creation_Catalyst_Unaligned.get(1), GT_Utility.getIntegratedCircuit(i + 1))
                .fluidInputs(Materials.StableBaryonicMatter.getFluid(1000L))
                .itemOutputs(catalystInputs[i])
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                // Not sure how I feel about it being in the laser engraver, but I don't see a better multi for it right
                // now,
                // and we can't really add a new one specifically for this (... for now)
                .addTo(laserEngraverRecipes);
        }

        // Initial aligned quark catalysts, these are not meant to be done often, and simply exist to get you started
        ItemStack[] quarks = new ItemStack[] { // make sure these are in the same order as the list above
            Particle.getBaseParticle(Particle.UP), Particle.getBaseParticle(Particle.DOWN),
            Particle.getBaseParticle(Particle.BOTTOM), Particle.getBaseParticle(Particle.TOP),
            Particle.getBaseParticle(Particle.STRANGE), Particle.getBaseParticle(Particle.CHARM) };

        for (int i = 0; i < catalystInputs.length; ++i) {
            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Quark_Catalyst_Housing.get(1), quarks[i])
                .fluidInputs(MaterialsUEVplus.ExcitedDTRC.getFluid(10000L))
                .itemOutputs(catalystInputs[i])
                .metadata(COIL_HEAT, 10800)
                .eut(TierEU.RECIPE_UMV)
                .duration(5 * MINUTES)
                .addTo(plasmaForgeRecipes);
        }

        // Recipe for quark catalyst housing
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Electromagnet_Tengam.get(1))
            .metadata(RESEARCH_TIME, 1 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 16),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 16),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tritanium, 16),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 16),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 16),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 8),
                ItemList.Field_Generator_UEV.get(4))
            .fluidInputs(
                Materials.Neutronium.getMolten(16 * 144),
                Materials.Infinity.getMolten(16 * 144),
                Materials.Tritanium.getMolten(16 * 144),
                Materials.CosmicNeutronium.getMolten(16 * 144))
            .itemOutputs(ItemList.Quark_Catalyst_Housing.get(1))
            .eut(TierEU.RECIPE_UIV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            // Fake item inputs
            .itemInputs(ItemList.Quark_Creation_Catalyst_Charm.get(1), ItemList.Quark_Creation_Catalyst_Strange.get(1))
            .fluidInputs(Materials.Grade7PurifiedWater.getFluid(1000L))
            .fluidOutputs(
                Materials.Grade8PurifiedWater.getFluid(900L),
                Materials.StableBaryonicMatter.getFluid(BARYONIC_MATTER_OUTPUT))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Unaligned.get(2L))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UEV)
            .metadata(BASE_CHANCE, 0.0f)
            .fake()
            .addTo(purificationParticleExtractionRecipes);

        // real recipe
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Grade7PurifiedWater.getFluid(1000L))
            .fluidOutputs(
                Materials.Grade8PurifiedWater.getFluid(900L),
                Materials.StableBaryonicMatter.getFluid(BARYONIC_MATTER_OUTPUT))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Unaligned.get(2L))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_UEV)
            .metadata(BASE_CHANCE, 0.0f)
            .hidden()
            .addTo(purificationParticleExtractionRecipes);
    }
}
