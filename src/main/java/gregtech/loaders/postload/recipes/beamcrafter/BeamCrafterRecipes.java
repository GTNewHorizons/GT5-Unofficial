package gregtech.loaders.postload.recipes.beamcrafter;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.tileentity.recipe.beamline.TargetChamberMetadata;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import static gregtech.api.recipe.RecipeMaps.BEAMCRAFTER_METADATA;
import static gregtech.api.recipe.RecipeMaps.beamcrafterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.ADVANCED_NITINOL;
import static gtnhlanth.common.beamline.Particle.ELECTRON;
import static gtnhlanth.common.beamline.Particle.MUONNEUTRINO;
import static gtnhlanth.common.beamline.Particle.NEUTRON;
import static gtnhlanth.common.beamline.Particle.PROTON;
import static gtnhlanth.common.beamline.Particle.TAU;
import static gtnhlanth.common.beamline.Particle.UPSILON;

public class BeamCrafterRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Hydrogen.getGas(2000L))
            .itemOutputs(new ItemStack(ModItems.itemStandarParticleBase, 1, 24)) // typo >:(
                                                                                                    // unknown particle
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata
                    .builder()
                    .particleID_A(PROTON.getId())
                    .particleID_B(ELECTRON.getId())
                    .amount_A(120*10)
                    .amount_B(120*60)
                    .energy_A(1)
                    .energy_B(1)
                    .build()
            )
            .outputChances(2000)
            .duration(1 * SECONDS)
            .eut(30720)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Duranium.getMolten(40L))
            .itemInputs(new ItemStack(ModItems.itemStandarParticleBase, 1, 24)
            )
            .itemOutputs(GregtechItemList.Laser_Lens_Special.get(1)) // q anomaly
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
            .duration(1 * SECONDS)
            .eut(491520)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Americium.getPlasma(9216L))
            .itemInputs(ItemList.Superconducting_Magnet_Solenoid_UV.get(4),
                ItemList.Field_Generator_ZPM.get(4))
            .itemOutputs(ItemList.CMSCasing.get(4))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata
                    .builder()
                    .particleID_A(ELECTRON.getId())
                    .particleID_B(ELECTRON.getId())
                    .amount_A(5000)
                    .amount_B(5000)
                    .energy_A(1)
                    .energy_B(1)
                    .build()
            )
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(ADVANCED_NITINOL.getFluidStack(9216))
            .itemInputs(ItemList.Superconducting_Magnet_Solenoid_UHV.get(4),
                ItemList.Field_Generator_UV.get(4))
            .itemOutputs(ItemList.ATLASCasing.get(4))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata
                    .builder()
                    .particleID_A(TAU.getId())
                    .particleID_B(TAU.getId())
                    .amount_A(5000)
                    .amount_B(5000)
                    .energy_A(1)
                    .energy_B(1)
                    .build()
            )
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.ProtoHalkonite.getFluid(9216))
            .itemInputs(ItemList.Superconducting_Magnet_Solenoid_UEV.get(4),
                ItemList.Field_Generator_UHV.get(4))
            .itemOutputs(ItemList.ALICECasing.get(4))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata
                    .builder()
                    .particleID_A(MUONNEUTRINO.getId())
                    .particleID_B(MUONNEUTRINO.getId())
                    .amount_A(500)
                    .amount_B(500)
                    .energy_A(1)
                    .energy_B(1)
                    .build()
            )
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(beamcrafterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.SpaceTime.getMolten(9216L))
            .itemInputs(ItemList.Superconducting_Magnet_Solenoid_UIV.get(4),
                ItemList.Field_Generator_UEV.get(4))
            .itemOutputs(ItemList.LHCbCasing.get(4))
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata
                    .builder()
                    .particleID_A(UPSILON.getId())
                    .particleID_B(UPSILON.getId())
                    .amount_A(5000)
                    .amount_B(5000)
                    .energy_A(1)
                    .energy_B(1)
                    .build()
            )
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(beamcrafterRecipes);

    }

}
