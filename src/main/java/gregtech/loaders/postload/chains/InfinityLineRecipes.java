package gregtech.loaders.postload.chains;

import static bartworks.API.recipe.BartWorksRecipeMaps.electricImplosionCompressorRecipes;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.neutroniumCompressorRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.vacuumFurnaceRecipes;

import gtnhlanth.common.item.MaskList;
import gtnhlanth.common.register.LanthItemList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsElements;

public class InfinityLineRecipes {

    public static void run() {

        // Taranium Draining Line

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Vyroxeres, 1),
                getModItem(NewHorizonsCoreMod.ID, "item.LapotronDust", 64),
                getModItem(NewHorizonsCoreMod.ID, "item.LapotronDust", 64),
                getModItem(NewHorizonsCoreMod.ID, "item.LapotronDust", 64),
                getModItem(NewHorizonsCoreMod.ID, "item.LapotronDust", 64),
                getModItem(NewHorizonsCoreMod.ID, "item.LapotronDust", 64))
            .fluidInputs(Materials.ElectrumFlux.getMolten(576))
            .itemOutputs(ItemList.VyroxeresCanvas.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                // TODO add after Taranium - GTOreDictUnificator.get(OrePrefixes.plate, Materials.Taranium, 64),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Naquadria, 64),
                ItemList.VyroxeresCanvas.get(1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Force, 0, false),
                getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.InertTaranium, 64),
                new ItemStack(LanthItemList.maskMap.get(MaskList.HEVP),1)) // TODO make this item compatible with beamline
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.InertTaranium, 16))
            .fluidInputs(Materials.Neutronium.getMolten(576))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.HighlyStableTaranium, 16))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .noOptimize()
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.InertTaranium, 16))
            .fluidInputs(Materials.DraconiumAwakened.getMolten(576))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.HighlyReactiveTaranium, 16))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .noOptimize()
            .addTo(chemicalBathRecipes);

        // UHV Infinity Recipe

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfinityCatalyst, 64))
            .itemOutputs(ItemList.WeakInfinityCatalyst.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.CosmicNeutronium, 24),
            ItemList.FractalAnomaly.get(1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Infinity, 1))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(hammerRecipes);

        // Non-Orientable Matter Line - Möebianite Processing

        // Combined Catalyst
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Vulcanite, 64),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Ceruclase, 64),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Rubracium, 64),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Orichalcum, 64))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(64000))
            .itemOutputs(ItemList.Combined_Catalyst.get(64))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);

        // React the ore with the catalysts
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.Moebianite, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.Moebianite, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.Moebianite, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.Moebianite, 64),
                ItemList.Combined_Catalyst.get(1))
            .fluidInputs(new FluidStack(MaterialsElements.getInstance().BROMINE.getPlasma(), 100))
            .fluidOutputs(MaterialsUEVplus.MoebianiteSlag.getFluid(2500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(COIL_HEAT, 11700)
            .noOptimize()
            .addTo(vacuumFurnaceRecipes);

        // Crystallize the Solution
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.NetherStar, 64))
            .fluidInputs(MaterialsUEVplus.MoebianiteSlag.getFluid(1250))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartzCharged, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartzCharged, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartzCharged, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartzCharged, 64))
            .fluidOutputs(MaterialsUEVplus.MoebianiteCrystalSlurry.getFluid(8000))
            .duration(150 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .noOptimize()
            .addTo(autoclaveRecipes);

        // Compress to insert the impurities inside the gems
        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalGemExquisite", 32))
            .fluidInputs(MaterialsUEVplus.MoebianiteCrystalSlurry.getFluid(64000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gemExquisite, MaterialsUEVplus.Manifold, 16)
            // TODO add chromatic glass gem after coremod pr
            // ,getModItem(NewHorizonsCoreMod.ID, "item.ChromaticGemExquisite", 16)
            )
            .fluidOutputs(MaterialsUEVplus.PurifiedMoebianite.getFluid(8000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .noOptimize()
            .addTo(electricImplosionCompressorRecipes);

        // Align with the Void
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsUEVplus.PurifiedMoebianite.getFluid(125), Materials.Void.getMolten(144))
            .fluidOutputs(MaterialsUEVplus.NonOrientableMatter.getFluid(1000))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(1562500)
            .metadata(FUSION_THRESHOLD, 1_000_000_000)
            .noOptimize()
            .addTo(fusionRecipes);
    }

}
