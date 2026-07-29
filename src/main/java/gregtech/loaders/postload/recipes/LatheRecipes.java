package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.MaterialParts;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTOreDictUnificator;

public class LatheRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("slabWood", 1))
            .itemOutputs(
                new ItemStack(Items.bowl, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.dustSmall, (int) (1)))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(latheRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.stack(Materials2Shapes.gemFlawless, Materials2Materials.LanthanumHexaboride, 1))
            .itemOutputs(
                MaterialParts.stack(Materials2Shapes.stickLong, Materials2Materials.LanthanumHexaboride, 1),
                MaterialParts.stack(Materials2Shapes.dust, Materials2Materials.LanthanumHexaboride, 1))
            .duration((100 * SECONDS))
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(latheRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("plankWood", 1))
            .itemOutputs(GTOreDictUnificator.get("stickWood", 2L))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(latheRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("logWood", 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.stickLong, (int) (4L)),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.dust, (int) (2L)))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(latheRecipes);

        // From ProcessingSaplings
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("treeSapling", 1))
            .itemOutputs(
                GTOreDictUnificator.get("stickWood", 1L),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.dustTiny, (int) (1L)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(latheRecipes);
    }
}
