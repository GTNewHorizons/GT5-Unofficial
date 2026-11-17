package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.WAFER_DOPING_PARTICLE_MODE;
import static gregtech.api.recipe.RecipeMaps.WAFER_DOPING_PARTICLE_MODE_ANOMALY;
import static gregtech.api.recipe.RecipeMaps.WAFER_DOPING_PARTICLE_MODE_PARTICLES;
import static gregtech.api.recipe.RecipeMaps.waferDopingFacilityRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.MAX_COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.STRUCTURE_TIER;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTUtility;
import gtnhlanth.common.register.WerkstoffMaterialPool;

public final class WaferDopingFacilityRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Crude_Si_Wafer.get(36),
                Materials.GalliumArsenide.getDust(1),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Si_AsGa_Wafer.get(36))
            .duration(60 * SECONDS)
            .eut(30)
            .metadata(STRUCTURE_TIER, 1)
            .addTo(waferDopingFacilityRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Crude_Si_Wafer.get(48),
                Materials.GalliumArsenide.getDust(2),
                Materials.Aluminiumoxide.getDust(16),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Si_AsGa_Al2O3_Wafer.get(48))
            .duration(60 * SECONDS)
            .eut(30)
            .metadata(STRUCTURE_TIER, 2)
            .metadata(COIL_HEAT, 1_500)
            .metadata(MAX_COIL_HEAT, 2_500)
            .addTo(waferDopingFacilityRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Crude_Si_Wafer.get(48),
                Materials.GalliumArsenide.getDust(2),
                GGMaterial.naquadahine.get(OrePrefixes.dust, 16),
                GTUtility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Si_AsGa_NqO2_Wafer.get(48))
            .duration(60 * SECONDS)
            .eut(30)
            .metadata(STRUCTURE_TIER, 2)
            .metadata(COIL_HEAT, 1_500)
            .metadata(MAX_COIL_HEAT, 2_500)
            .addTo(waferDopingFacilityRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Microwaved_Si_Wafer.get(48),
                Materials.GalliumArsenide.getDust(4),
                WerkstoffMaterialPool.EuropiumOxide.get(OrePrefixes.dust, 16),
                GTUtility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.MW_Si_AsGa_EuO_Wafer.get(48))
            .duration(60 * SECONDS)
            .eut(30)
            .metadata(STRUCTURE_TIER, 3)
            .metadata(COIL_HEAT, 1_500)
            .metadata(MAX_COIL_HEAT, 2_500)
            .addTo(waferDopingFacilityRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Microwaved_Si_Wafer.get(48),
                Materials.GalliumArsenide.getDust(4),
                ItemList.Tritanium_Oxide_Dust.get(16),
                GTUtility.getIntegratedCircuit(5))
            .itemOutputs(ItemList.MW_Si_AsGa_TnO_Wafer.get(48))
            .duration(60 * SECONDS)
            .eut(30)
            .metadata(STRUCTURE_TIER, 3)
            .metadata(COIL_HEAT, 1_500)
            .metadata(MAX_COIL_HEAT, 2_500)
            .addTo(waferDopingFacilityRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.AlGaAs_Wafer.get(64), GTUtility.getIntegratedCircuit(6))
            .itemOutputs(ItemList.Modified_AlGaAs_Wafer.get(64))
            .duration(60 * SECONDS)
            .eut(30)
            .metadata(STRUCTURE_TIER, 4)
            .metadata(COIL_HEAT, 10_000)
            .metadata(MAX_COIL_HEAT, 11_000)
            .metadata(WAFER_DOPING_PARTICLE_MODE, WAFER_DOPING_PARTICLE_MODE_PARTICLES)
            .addTo(waferDopingFacilityRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.AlGaAs_Wafer.get(64), GTUtility.getIntegratedCircuit(7))
            .itemOutputs(ItemList.Modified_AlGaAs_Wafer.get(64))
            .duration(60 * SECONDS)
            .eut(30)
            .metadata(STRUCTURE_TIER, 4)
            .metadata(COIL_HEAT, 10_000)
            .metadata(MAX_COIL_HEAT, 11_000)
            .metadata(WAFER_DOPING_PARTICLE_MODE, WAFER_DOPING_PARTICLE_MODE_ANOMALY)
            .addTo(waferDopingFacilityRecipes);
    }
}
