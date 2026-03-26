package gregtech.loaders.postload.recipes.beamcrafter;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.common.tileentities.machines.multi.beamcrafting.LHCModule;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import gtnhlanth.common.beamline.Particle;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static gregtech.api.recipe.RecipeMaps.BEAMCRAFTER_METADATA;
import static gregtech.api.recipe.RecipeMaps.LARGE_HADRON_COLLIDER_METADATA;
import static gregtech.api.recipe.RecipeMaps.beamcrafterRecipes;
import static gregtech.api.recipe.RecipeMaps.largeHadronColliderRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.core.material.MaterialsAlloy.ABYSSAL;
import static gtPlusPlus.core.material.MaterialsAlloy.QUANTUM;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.ADVANCED_NITINOL;
import static gtnhlanth.common.beamline.Particle.ELECTRON;
import static gtnhlanth.common.beamline.Particle.ETA;
import static gtnhlanth.common.beamline.Particle.GRAVITON;
import static gtnhlanth.common.beamline.Particle.HIGGS;
import static gtnhlanth.common.beamline.Particle.JPSI;
import static gtnhlanth.common.beamline.Particle.LAMBDA;
import static gtnhlanth.common.beamline.Particle.MUONNEUTRINO;
import static gtnhlanth.common.beamline.Particle.NEUTRON;
import static gtnhlanth.common.beamline.Particle.OMEGA;
import static gtnhlanth.common.beamline.Particle.PROTON;
import static gtnhlanth.common.beamline.Particle.TAU;
import static gtnhlanth.common.beamline.Particle.UPSILON;

public class LargeHadronColliderRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.CMSCasing.get(1))
            .metadata(LARGE_HADRON_COLLIDER_METADATA,
                LargeHadronColliderMetadata.builder()
                    .particleList(LHCModule.EM.acceptedParticles)
            .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(largeHadronColliderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ATLASCasing.get(1))
            .metadata(LARGE_HADRON_COLLIDER_METADATA,
                LargeHadronColliderMetadata.builder()
                    .particleList(LHCModule.Weak.acceptedParticles.subList(0,8))
            .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(largeHadronColliderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ATLASCasing.get(1))
            .metadata(LARGE_HADRON_COLLIDER_METADATA,
                LargeHadronColliderMetadata.builder()
                    .particleList(LHCModule.Weak.acceptedParticles.subList(8,16))
            .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(largeHadronColliderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ALICECasing.get(1))
            .metadata(LARGE_HADRON_COLLIDER_METADATA,
                LargeHadronColliderMetadata.builder()
                    .particleList(LHCModule.Strong.acceptedParticles)
            .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(largeHadronColliderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.LHCbCasing.get(1))
            .metadata(LARGE_HADRON_COLLIDER_METADATA,
                LargeHadronColliderMetadata.builder()
                    .particleList(LHCModule.Grav.acceptedParticles)
            .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(largeHadronColliderRecipes);

    }

}
