package gregtech.loaders.postload.recipes.beamcrafter;

import static gregtech.api.recipe.RecipeMaps.BEAMCRAFTER_METADATA;
import static gregtech.api.recipe.RecipeMaps.beamcrafterRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.core.material.MaterialsAlloy.ABYSSAL;
import static gtPlusPlus.core.material.MaterialsAlloy.QUANTUM;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.ADVANCED_NITINOL;
import static gtnhlanth.common.beamline.Particle.ELECTRON;
import static gtnhlanth.common.beamline.Particle.ELECTRONNEUTRINO;
import static gtnhlanth.common.beamline.Particle.ETA;
import static gtnhlanth.common.beamline.Particle.GRAVITON;
import static gtnhlanth.common.beamline.Particle.HIGGS;
import static gtnhlanth.common.beamline.Particle.JPSI;
import static gtnhlanth.common.beamline.Particle.LAMBDA;
import static gtnhlanth.common.beamline.Particle.MUON;
import static gtnhlanth.common.beamline.Particle.MUONNEUTRINO;
import static gtnhlanth.common.beamline.Particle.NEUTRON;
import static gtnhlanth.common.beamline.Particle.OMEGA;
import static gtnhlanth.common.beamline.Particle.PROTON;
import static gtnhlanth.common.beamline.Particle.TAU;
import static gtnhlanth.common.beamline.Particle.TAUNEUTRINO;
import static gtnhlanth.common.beamline.Particle.UPSILON;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gtPlusPlus.core.fluids.GTPPFluids;
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, (int) (2000)))
            .itemOutputs(new ItemStack(ModItems.itemStandarParticleBase, 1, 24))
            // unknown particle
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(PROTON.getId())
                    .particleID_B(ELECTRON.getId())
                    .amount_A(120)
                    .amount_B(720)
                    .build())
            .outputChances(2085)
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.LiquidHydrogen, 1000))
            .itemOutputs(new ItemStack(ModItems.itemStandarParticleBase, 64, 24))
            // unknown particle better recipe
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(PROTON.getId())
                    .particleID_B(MUON.getId())
                    .amount_A(5)
                    .amount_B(3)
                    .build())
            .outputChances(9900)
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Duranium, Materials2FluidShapes.shapeFluidMolten, (int) (40)))
            .itemInputs(new ItemStack(ModItems.itemStandarParticleBase, 1, 24))
            .itemOutputs(GregtechItemList.Laser_Lens_Special.get(1)) // q anomaly
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(NEUTRON.getId())
                    .particleID_B(NEUTRON.getId())
                    .amount_A(25 * 10)
                    .amount_B(25 * 10)
                    .build())
            .outputChances(100)
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Americium, Materials2FluidShapes.shapeFluidPlasma, (int) (9216)))
            .itemInputs(ItemList.Superconducting_Magnet_Solenoid_UV.get(4), ItemList.Field_Generator_ZPM.get(4))
            .itemOutputs(ItemList.CMSCasing.get(4))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(ELECTRON.getId())
                    .particleID_B(ELECTRON.getId())
                    .amount_A(3000)
                    .amount_B(3000)
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
                    .amount_A(900)
                    .amount_B(900)
                    .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(ABYSSAL.getFluidStack(9216))
            .itemInputs(ItemList.Superconducting_Magnet_Solenoid_UEV.get(4), ItemList.Field_Generator_UHV.get(4))
            .itemOutputs(ItemList.ALICECasing.get(4))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(MUONNEUTRINO.getId())
                    .particleID_B(MUONNEUTRINO.getId())
                    .amount_A(450)
                    .amount_B(450)
                    .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(QUANTUM.getFluidStack(9216))
            .itemInputs(ItemList.Superconducting_Magnet_Solenoid_UIV.get(4), ItemList.Field_Generator_UEV.get(4))
            .itemOutputs(ItemList.LHCbCasing.get(4))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(UPSILON.getId())
                    .particleID_B(OMEGA.getId())
                    .amount_A(750)
                    .amount_B(750)
                    .build())
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().URANIUM238.getDust(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Deuterium, Materials2FluidShapes.shapeFluidGas, (int) (400)))
            .itemOutputs(GregtechItemList.Neptunium238Dust.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(NEUTRON.getId())
                    .particleID_B(NEUTRON.getId())
                    .amount_A(25 * 10)
                    .amount_B(25 * 10)
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
                    .build())
            .outputChances(2500)
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Quark_Catalyst_Housing.get(1))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Up.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.shapeFluidGas, (int) (1000)))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(PROTON.getId())
                    .particleID_B(LAMBDA.getId())
                    .amount_A(1000)
                    .amount_B(1000)
                    .build())
            .eut(TierEU.RECIPE_UMV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Quark_Catalyst_Housing.get(1))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Down.get(1))
            .fluidInputs(WerkstoffLoader.Neon.getFluidOrGas(1000))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(NEUTRON.getId())
                    .particleID_B(LAMBDA.getId())
                    .amount_A(1000)
                    .amount_B(1000)
                    .build())
            .eut(TierEU.RECIPE_UMV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Quark_Catalyst_Housing.get(1))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Charm.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Argon, Materials2FluidShapes.shapeFluidGas, (int) (1000)))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(JPSI.getId())
                    .particleID_B(JPSI.getId())
                    .amount_A(1000)
                    .amount_B(1000)
                    .build())
            .eut(TierEU.RECIPE_UMV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Quark_Catalyst_Housing.get(1))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Strange.get(1))
            .fluidInputs(WerkstoffLoader.Krypton.getFluidOrGas(1000))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(ETA.getId())
                    .particleID_B(ETA.getId())
                    .amount_A(1000)
                    .amount_B(1000)
                    .build())
            .eut(TierEU.RECIPE_UMV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Quark_Catalyst_Housing.get(1))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Bottom.get(1))
            .fluidInputs(WerkstoffLoader.Xenon.getFluidOrGas(1000))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(UPSILON.getId())
                    .particleID_B(UPSILON.getId())
                    .amount_A(700)
                    .amount_B(700)
                    .build())
            .eut(TierEU.RECIPE_UMV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Quark_Catalyst_Housing.get(1))
            .itemOutputs(ItemList.Quark_Creation_Catalyst_Top.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Radon, Materials2FluidShapes.shapeFluidGas, (int) (1000)))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(HIGGS.getId())
                    .particleID_B(HIGGS.getId())
                    .amount_A(85)
                    .amount_B(85)
                    .build())
            .eut(TierEU.RECIPE_UMV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.StableEmptyContainmentUnit.get(1))
            .itemOutputs(ItemList.StableBaryonContainmentUnit.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.shapeFluidGas, (int) (100)))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(PROTON.getId())
                    .particleID_B(NEUTRON.getId())
                    .amount_A(20)
                    .amount_B(20)
                    .build())
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.StableEmptyContainmentUnit.get(1))
            .itemOutputs(ItemList.StableLeptonContainmentUnit.get(1))
            .fluidInputs(WerkstoffLoader.Neon.getFluidOrGas(100))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(ELECTRON.getId())
                    .particleID_B(ELECTRON.getId())
                    .amount_A(20)
                    .amount_B(20)
                    .build())
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.StableEmptyContainmentUnit.get(1))
            .itemOutputs(ItemList.StableMesonContainmentUnit.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Argon, Materials2FluidShapes.shapeFluidGas, (int) (100)))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(ETA.getId())
                    .particleID_B(JPSI.getId())
                    .amount_A(20)
                    .amount_B(20)
                    .build())
            .eut(TierEU.RECIPE_UV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.StableEmptyContainmentUnit.get(1))
            .itemOutputs(ItemList.StableBosonContainmentUnit.get(1))
            .fluidInputs(WerkstoffLoader.Krypton.getFluidOrGas(100))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(GRAVITON.getId())
                    .particleID_B(GRAVITON.getId())
                    .amount_A(20)
                    .amount_B(20)
                    .build())
            .eut(TierEU.RECIPE_UHV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Tesseract.get(1))
            .itemOutputs(ItemList.EnergisedTesseract.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(ELECTRONNEUTRINO.getId())
                    .particleID_B(TAUNEUTRINO.getId())
                    .amount_A(50)
                    .amount_B(30)
                    .build())
            .eut(TierEU.RECIPE_UIV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(GGMaterial.plutoniumBasedLiquidFuel.getFluidOrGas(1000))
            .fluidOutputs(GGMaterial.plutoniumBasedLiquidFuelExcited.getFluidOrGas(1000))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(ETA.getId())
                    .particleID_B(UPSILON.getId())
                    .amount_A(2)
                    .amount_B(2)
                    .build())
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.PseudoStar.get(64L), Materials.TranscendentMetal.getNanite(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.InactiveCosmicSolder,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (160_000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SpaceTime,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (16 * INGOTS)))
            .itemOutputs(Materials.TranscendentMetal.getNanite(2))
            .outputChances(8000)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.BoundlessCosmicSolder,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (160_000)))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata.builder()
                    .particleID_A(GRAVITON.getId())
                    .particleID_B(HIGGS.getId())
                    .amount_A(50)
                    .amount_B(5)
                    .build())
            .eut(TierEU.RECIPE_UMV)
            .duration(2 * SECONDS)
            .addTo(beamcrafterRecipes);

    }

}
