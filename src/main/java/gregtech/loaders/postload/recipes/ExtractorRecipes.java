package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.util.GT_ModHandler.getIC2Item;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.WILDCARD;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class ExtractorRecipes implements Runnable {

    @Override
    public void run() {
        addExtractionRecipe(new ItemStack(Blocks.bookshelf, 1, WILDCARD), new ItemStack(Items.book, 3, 0));
        addExtractionRecipe(
            new ItemStack(Items.slime_ball, 1),
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 2L));
        addExtractionRecipe(
            ItemList.IC2_Resin.get(1L),
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3L));
        addExtractionRecipe(
            getIC2Item("rubberSapling", 1L),
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));
        addExtractionRecipe(
            getIC2Item("rubberLeaves", 16L),
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));

        addExtractionRecipe(ItemList.Cell_Air.get(1L), ItemList.Cell_Empty.get(1L));

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_SU_LV_SulfuricAcid.get(1L))
            .itemOutputs(ItemList.Battery_Hull_LV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_SU_LV_Mercury.get(1L))
            .itemOutputs(ItemList.Battery_Hull_LV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_SU_MV_SulfuricAcid.get(1L))
            .itemOutputs(ItemList.Battery_Hull_MV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_SU_MV_Mercury.get(1L))
            .itemOutputs(ItemList.Battery_Hull_MV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_SU_HV_SulfuricAcid.get(1L))
            .itemOutputs(ItemList.Battery_Hull_HV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_SU_HV_Mercury.get(1L))
            .itemOutputs(ItemList.Battery_Hull_HV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_LV_Cadmium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_LV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_LV_Lithium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_LV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_LV_Sodium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_LV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_MV_Cadmium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_MV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_MV_Lithium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_MV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_MV_Sodium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_MV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_HV_Cadmium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_HV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_HV_Lithium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_HV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_HV_Sodium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_HV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_EV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_EV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_IV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_IV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_LuV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_LuV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_ZPM_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_ZPM.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_UV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_UV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_UHV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_UHV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_UEV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_UEV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_UIV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_UIV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_UMV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_UMV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_UxV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_UxV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
    }

    public void addExtractionRecipe(ItemStack input, ItemStack output) {
        output = GT_OreDictUnificator.get(true, output);
        GT_Values.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(output)
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
    }
}
