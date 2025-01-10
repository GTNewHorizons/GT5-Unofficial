package gregtech.loaders.postload.chains;

import static bartworks.API.recipe.BartWorksRecipeMaps.electricImplosionCompressorRecipes;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.vacuumFurnaceRecipes;

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
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartzCharged, 256))
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
