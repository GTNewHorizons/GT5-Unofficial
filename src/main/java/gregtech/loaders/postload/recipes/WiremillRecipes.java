package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.wiremillRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeRegistrator.registerWiremillRecipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.util.GTModHandler;

public class WiremillRecipes implements Runnable {

    @Override
    public void run() {

        registerWiremillRecipes(Materials.Graphene, 20 * SECONDS, 2, Shapes.dust, Shapes.stick, 1);

        registerWiremillRecipes(Materials.SpaceTime, 8 * SECONDS + 8 * TICKS, (int) TierEU.RECIPE_LuV);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Polycaprolactam, Shapes.ingot, 1))
            .itemOutputs(new ItemStack(Items.string, 32))
            .duration(4 * SECONDS)
            .eut(48)
            .addTo(wiremillRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Steel, TEBlockShapes.pipeTiny, 1))
            .itemOutputs(GTModHandler.getIC2Item("miningPipe", 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(wiremillRecipes);
    }
}
