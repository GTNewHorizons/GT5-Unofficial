package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GTModHandler.getIC2Item;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTOreDictUnificator;

public class ThermalCentrifugeRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(getIC2Item("RTGPellets", 1))
            .itemOutputs(
                getIC2Item("Plutonium", 3),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (54)))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Lithium, Materials2Shapes.dustTiny, (int) (1)))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Clay, Materials2Shapes.dust, (int) (4)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (1)))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.cobblestone, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);
    }
}
