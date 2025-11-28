package gregtech.loaders.postload.recipes.beamcrafter;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import gtnhlanth.common.tileentity.recipe.beamline.TargetChamberMetadata;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static gregtech.api.recipe.RecipeMaps.BEAMCRAFTER_METADATA;
import static gregtech.api.recipe.RecipeMaps.beamcrafterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtnhlanth.common.beamline.Particle.ELECTRON;
import static gtnhlanth.common.beamline.Particle.NEUTRON;
import static gtnhlanth.common.beamline.Particle.PROTON;

public class BeamCrafterRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Duranium.getMolten(40L))
            .itemInputs(new ItemStack(ModItems.itemStandarParticleBase, 1, 24)
            )
            .itemOutputs(GregtechItemList.Laser_Lens_Special.get(1))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata
                    .builder()
                    .particleID_A(NEUTRON.getId())
                    .particleID_B(NEUTRON.getId())
                    .amount_A(25*10)
                    .amount_B(25*10)
                    .energy_A(1)
                    .energy_B(1)
                    .build()
            )
            .outputChances(100)
            .duration(25 * SECONDS) // todo: this needs to depend purely on beam rates, like target chamber
            .eut(TierEU.RECIPE_UV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Hydrogen.getGas(1000L))
            .itemOutputs(Particle.getBaseParticle(Particle.UNKNOWN))
            .fluidOutputs(FluidUtils.getFluidStack("plasma.hydrogen", 100))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata
                    .builder()
                    .particleID_A(PROTON.getId())
                    .particleID_B(PROTON.getId())
                    .amount_A(25*10)
                    .amount_B(25*10)
                    .energy_A(1)
                    .energy_B(1)
                    .build()
            )
            .outputChances(2085)
            .duration(2 * SECONDS) // update later
            .eut(TierEU.RECIPE_LuV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().URANIUM238.getDust(1))
            .fluidInputs(Materials.Deuterium.getGas(400))
            .itemOutputs(new ItemStack(ModItems.dustNeptunium238))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata
                    .builder()
                    .particleID_A(NEUTRON.getId())
                    .particleID_B(NEUTRON.getId())
                    .amount_A(25*10)
                    .amount_B(25*10)
                    .energy_A(1)
                    .energy_B(1)
                    .build()
            )
            .outputChances(500)
            .duration(2 * SECONDS) // update later
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
                BeamCrafterMetadata
                    .builder()
                    .particleID_A(NEUTRON.getId())
                    .particleID_B(NEUTRON.getId())
                    .amount_A(25*10)
                    .amount_B(25*10)
                    .energy_A(1)
                    .energy_B(1)
                    .build()
            )
            .outputChances(2500)
            .duration(2 * SECONDS) // update later
            .eut(TierEU.RECIPE_ZPM)
            .addTo(beamcrafterRecipes);
    }


        /*
        GTValues.RA.stdBuilder()
            .fluidInputs(FluidUtils.getFluidStack("plasma.hydrogen", 100))
            .itemOutputs(
                New item to replace graviton
                Recipes that use gravitons to update:
                DTPF quantum anomaly,
                matter manipulator teleporter core mkIII,
                Graviton Anomaly,
                Boundless Gravitationally Severed Structure Casing,
                Spacially Transcendant Gravitational Lens Block,
                Forge of the Gods controller

                Particle.getBaseParticle(Particle.UNKNOWN))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata
                    .builder()
                    .particleID_A(PION.getId())
                    .particleID_B(PION.getId())
                    .amount_A(25*10)
                    .amount_B(25*10)
                    .energy_A(1)
                    .energy_B(1)
                    .build()
            )
            .outputChances(1000, 100)
            .duration(2 * SECONDS) // update later
            .eut(TierEU.RECIPE_LuV)
            .addTo(beamcrafterRecipes);
        */

}
