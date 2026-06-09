package gregtech.loaders.postload.recipes.beamcrafter;

import static gregtech.api.recipe.RecipeMaps.LARGE_HADRON_COLLIDER_METADATA;
import static gregtech.api.recipe.RecipeMaps.largeHadronColliderRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.common.tileentities.machines.multi.beamcrafting.LHCModule;

public class LargeHadronColliderRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.CMSCasing.get(1))
            .metadata(
                LARGE_HADRON_COLLIDER_METADATA,
                LargeHadronColliderMetadata.builder()
                    .particleList(LHCModule.EM.acceptedParticles)
                    .progressBarTextureIndex(0)
                    .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(largeHadronColliderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ATLASCasing.get(1))
            .metadata(
                LARGE_HADRON_COLLIDER_METADATA,
                LargeHadronColliderMetadata.builder()
                    .particleList(LHCModule.Weak.acceptedParticles.subList(0, 8))
                    .progressBarTextureIndex(1)
                    .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(largeHadronColliderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ATLASCasing.get(1))
            .metadata(
                LARGE_HADRON_COLLIDER_METADATA,
                LargeHadronColliderMetadata.builder()
                    .particleList(LHCModule.Weak.acceptedParticles.subList(8, 16))
                    .progressBarTextureIndex(1)
                    .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(largeHadronColliderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ALICECasing.get(1))
            .metadata(
                LARGE_HADRON_COLLIDER_METADATA,
                LargeHadronColliderMetadata.builder()
                    .particleList(LHCModule.Strong.acceptedParticles)
                    .progressBarTextureIndex(2)
                    .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(largeHadronColliderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.LHCbCasing.get(1))
            .metadata(
                LARGE_HADRON_COLLIDER_METADATA,
                LargeHadronColliderMetadata.builder()
                    .particleList(LHCModule.Grav.acceptedParticles)
                    .progressBarTextureIndex(3)
                    .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(largeHadronColliderRecipes);

    }

}
