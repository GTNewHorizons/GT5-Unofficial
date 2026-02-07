package gregtech.loaders.postload.recipes.beamcrafter;

import static gregtech.api.recipe.RecipeMaps.BEAMCRAFTER_METADATA;
import static gregtech.api.recipe.RecipeMaps.beamcrafterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
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

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

public class BeamCrafterRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Hydrogen.getGas(2000L))
            .itemOutputs(new ItemStack(ModItems.itemStandarParticleBase, 1, 24)) // typo >:(
            // unknown particle
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(PROTON.getId())
                    .particleID_B(ELECTRON.getId())
                    .amount_A(1200)
                    .amount_B(7200)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .outputChances(2085)
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Duranium.getMolten(40L))
            .itemInputs(new ItemStack(ModItems.itemStandarParticleBase, 1, 24))
            .itemOutputs(GregtechItemList.Laser_Lens_Special.get(1)) // q anomaly
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(NEUTRON.getId())
                    .particleID_B(NEUTRON.getId())
                    .amount_A(25 * 10)
                    .amount_B(25 * 10)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .outputChances(100)
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Americium.getPlasma(9216L))
            .itemInputs(ItemList.Superconducting_Magnet_Solenoid_UV.get(4), ItemList.Field_Generator_ZPM.get(4))
            .itemOutputs(ItemList.CMSCasing.get(4))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(ELECTRON.getId())
                    .particleID_B(ELECTRON.getId())
                    .amount_A(5000)
                    .amount_B(5000)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(ADVANCED_NITINOL.getFluidStack(9216))
            .itemInputs(ItemList.Superconducting_Magnet_Solenoid_UHV.get(4), ItemList.Field_Generator_UV.get(4))
            .itemOutputs(ItemList.ATLASCasing.get(4))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(TAU.getId())
                    .particleID_B(TAU.getId())
                    .amount_A(5000)
                    .amount_B(5000)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.MoltenProtoHalkoniteBase.getFluid(9216))
            .itemInputs(ItemList.Superconducting_Magnet_Solenoid_UEV.get(4), ItemList.Field_Generator_UHV.get(4))
            .itemOutputs(ItemList.ALICECasing.get(4))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(MUONNEUTRINO.getId())
                    .particleID_B(MUONNEUTRINO.getId())
                    .amount_A(500)
                    .amount_B(500)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.SpaceTime.getMolten(9216L))
            .itemInputs(ItemList.Superconducting_Magnet_Solenoid_UIV.get(4), ItemList.Field_Generator_UEV.get(4))
            .itemOutputs(ItemList.LHCbCasing.get(4))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(UPSILON.getId())
                    .particleID_B(OMEGA.getId())
                    .amount_A(5000)
                    .amount_B(5000)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().URANIUM238.getDust(1))
            .fluidInputs(Materials.Deuterium.getGas(400))
            .itemOutputs(GregtechItemList.Neptunium238Dust.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(NEUTRON.getId())
                    .particleID_B(NEUTRON.getId())
                    .amount_A(25 * 10)
                    .amount_B(25 * 10)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .outputChances(500)
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Particle.getBaseParticle(Particle.UNKNOWN, 8),
                MaterialsElements.getInstance().PLUTONIUM238.getDust(1))
            .fluidInputs(new FluidStack(TFFluids.fluidEnder, 1000))
            .itemOutputs(MaterialsOres.DEEP_EARTH_REACTOR_FUEL_DEPOSIT.getDust(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(NEUTRON.getId())
                    .particleID_B(NEUTRON.getId())
                    .amount_A(25 * 10)
                    .amount_B(25 * 10)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .outputChances(2500)
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Quark_Catalyst_Housing.get(1))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Up.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(PROTON.getId())
                    .particleID_B(LAMBDA.getId())
                    .amount_A(300 * 60 * 20)
                    .amount_B(300 * 60 * 20)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .eut(TierEU.RECIPE_UMV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Quark_Catalyst_Housing.get(1))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Down.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(NEUTRON.getId())
                    .particleID_B(LAMBDA.getId())
                    .amount_A(300 * 60 * 20)
                    .amount_B(300 * 60 * 20)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .eut(TierEU.RECIPE_UMV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Quark_Catalyst_Housing.get(1))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Charm.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(JPSI.getId())
                    .particleID_B(JPSI.getId())
                    .amount_A(300 * 60 * 20)
                    .amount_B(300 * 60 * 20)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .eut(TierEU.RECIPE_UMV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Quark_Catalyst_Housing.get(1))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Strange.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(ETA.getId())
                    .particleID_B(ETA.getId())
                    .amount_A(300 * 60 * 20)
                    .amount_B(300 * 60 * 20)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .eut(TierEU.RECIPE_UMV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Quark_Catalyst_Housing.get(1))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Bottom.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(UPSILON.getId())
                    .particleID_B(UPSILON.getId())
                    .amount_A(300 * 60 * 20)
                    .amount_B(300 * 60 * 20)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .eut(TierEU.RECIPE_UMV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Quark_Catalyst_Housing.get(1))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Top.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(HIGGS.getId())
                    .particleID_B(HIGGS.getId())
                    .amount_A(300 * 60 * 20)
                    .amount_B(300 * 60 * 20)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .eut(TierEU.RECIPE_UMV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        // todo: gravitons
        // todo: new items to scan for grade 8 casings

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.StableEmptyContainmentUnit.get(1))
            .itemOutputs(ItemList.StableBaryonContainmentUnit.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(PROTON.getId())
                    .particleID_B(NEUTRON.getId())
                    .amount_A(60 * 20)
                    .amount_B(60 * 20)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.StableEmptyContainmentUnit.get(1))
            .itemOutputs(ItemList.StableLeptonContainmentUnit.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(ELECTRON.getId())
                    .particleID_B(ELECTRON.getId())
                    .amount_A(60 * 20)
                    .amount_B(60 * 20)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.StableEmptyContainmentUnit.get(1))
            .itemOutputs(ItemList.StableMesonContainmentUnit.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(ETA.getId())
                    .particleID_B(JPSI.getId())
                    .amount_A(60 * 20)
                    .amount_B(60 * 20)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .eut(TierEU.RECIPE_UV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.StableEmptyContainmentUnit.get(1))
            .itemOutputs(ItemList.StableBosonContainmentUnit.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(GRAVITON.getId())
                    .particleID_B(GRAVITON.getId())
                    .amount_A(60 * 20)
                    .amount_B(60 * 20)
                    .energy_A(1)
                    .energy_B(1)
                    .build())
            .eut(TierEU.RECIPE_UHV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

    }

}
