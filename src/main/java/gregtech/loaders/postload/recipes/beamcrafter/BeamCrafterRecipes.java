package gregtech.loaders.postload.recipes.beamcrafter;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.tileentity.recipe.beamline.TargetChamberMetadata;
import net.minecraft.item.ItemStack;

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
            .fluidInputs(Materials.Hydrogen.getGas(2000L))
            .itemOutputs(new ItemStack(ModItems.itemStandarParticleBase, 1, 24)) // typo >:(
            .metadata(
                BEAMCRAFTER_METADATA,
                BeamCrafterMetadata
                    .builder()
                    .particleID_A(PROTON.getId())
                    .particleID_B(ELECTRON.getId())
                    .amount_A(1)
                    .amount_B(1)
                    //.amount_A(120*10)
                    //.amount_B(120*60)
                    .energy_A(1)
                    .energy_B(1)
                    .build()
            )
            .outputChances(2000)
            .duration(1 * SECONDS) // todo: this needs to depend purely on beam rates, like target chamber
            .eut(30720)
            .addTo(beamcrafterRecipes);

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
            .eut(491520)
            .addTo(beamcrafterRecipes);


    }

}
