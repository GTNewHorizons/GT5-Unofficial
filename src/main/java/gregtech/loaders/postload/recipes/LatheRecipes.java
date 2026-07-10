package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.objects.OreDictItemStack;
import gtnhlanth.common.register.WerkstoffMaterialPool;

public class LatheRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("slabWood", 1))
            .itemOutputs(
                new ItemStack(Items.bowl, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeDustSmall, (int) (1)))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(latheRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.LanthanumHexaboride.get(OrePrefixes.gemFlawless, 1))
            .itemOutputs(
                WerkstoffMaterialPool.LanthanumHexaboride.get(OrePrefixes.stickLong, 1),
                WerkstoffMaterialPool.LanthanumHexaboride.get(OrePrefixes.dust, 1))
            .duration((100 * SECONDS))
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(latheRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("plankWood", 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeStick, (int) (2L)))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(latheRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("logWood", 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeStickLong, (int) (4L)),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeDust, (int) (2L)))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(latheRecipes);

        // From ProcessingSaplings
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("treeSapling", 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeStick, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeDustTiny, (int) (1L)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(latheRecipes);
    }
}
