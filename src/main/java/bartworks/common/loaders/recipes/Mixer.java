package bartworks.common.loaders.recipes;

import static gregtech.api.enums.Mods.Gendustry;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.FluidLoader;
import bartworks.common.tileentities.multis.MTEThoriumHighTempReactor;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTModHandler;

public class Mixer implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 10),
                MaterialLibAPI.getStack(Materials2Materials.Uranium235, Materials2Shapes.dust, (int) (1)))
            .circuit(2)
            .itemOutputs(new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        if (Gendustry.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Radon, Materials2CellShapes.cell, (int) (1L)))
                .circuit(17)
                .itemOutputs(Materials.Empty.getCells(1))
                .fluidInputs(GTModHandler.getLiquidDNA(1_000))
                .fluidOutputs(new FluidStack(FluidLoader.BioLabFluidMaterials[0], 2_000))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);
        }

    }
}
