package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class CannerRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Cadmium, Materials2Shapes.shapeDust, (int) (2L)),
                ItemList.Battery_Hull_LV.get(1L))
            .itemOutputs(ItemList.Battery_RE_LV_Cadmium.get(1L))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Lithium, Materials2Shapes.shapeDust, (int) (2L)),
                ItemList.Battery_Hull_LV.get(1L))
            .itemOutputs(ItemList.Battery_RE_LV_Lithium.get(1L))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, (int) (2L)),
                ItemList.Battery_Hull_LV.get(1L))
            .itemOutputs(ItemList.Battery_RE_LV_Sodium.get(1L))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Cadmium, Materials2Shapes.shapeDust, (int) (8L)),
                ItemList.Battery_Hull_MV.get(1L))
            .itemOutputs(ItemList.Battery_RE_MV_Cadmium.get(1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Lithium, Materials2Shapes.shapeDust, (int) (8L)),
                ItemList.Battery_Hull_MV.get(1L))
            .itemOutputs(ItemList.Battery_RE_MV_Lithium.get(1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, (int) (8L)),
                ItemList.Battery_Hull_MV.get(1L))
            .itemOutputs(ItemList.Battery_RE_MV_Sodium.get(1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Cadmium, Materials2Shapes.shapeDust, (int) (32L)),
                ItemList.Battery_Hull_HV.get(1L))
            .itemOutputs(ItemList.Battery_RE_HV_Cadmium.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Lithium, Materials2Shapes.shapeDust, (int) (32L)),
                ItemList.Battery_Hull_HV.get(1L))
            .itemOutputs(ItemList.Battery_RE_HV_Lithium.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, (int) (32L)),
                ItemList.Battery_Hull_HV.get(1L))
            .itemOutputs(ItemList.Battery_RE_HV_Sodium.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        // Recipes to actually fill the empty hulls with content
        // IV 2048

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sunnarium, Materials2Shapes.shapeDust, (int) (4L)),
                ItemList.BatteryHull_EV.get(1L))
            .itemOutputs(ItemList.BatteryHull_EV_Full.get(1L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(cannerRecipes);
        // EV 8192

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sunnarium, Materials2Shapes.shapeDust, (int) (16L)),
                ItemList.BatteryHull_IV.get(1L))
            .itemOutputs(ItemList.BatteryHull_IV_Full.get(1L))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(cannerRecipes);
        // LuV 32768

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sunnarium, Materials2Shapes.shapeDust, (int) (32L)),
                ItemList.BatteryHull_LuV.get(1L))
            .itemOutputs(ItemList.BatteryHull_LuV_Full.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(cannerRecipes);
        // ZPM 131072

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.shapeDust, (int) (16L)),
                ItemList.BatteryHull_ZPM.get(1L))
            .itemOutputs(ItemList.BatteryHull_ZPM_Full.get(1L))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_IV / 2)
            .addTo(cannerRecipes);
        // UV 524288

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.shapeDust, (int) (32L)),
                ItemList.BatteryHull_UV.get(1L))
            .itemOutputs(ItemList.BatteryHull_UV_Full.get(1L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(cannerRecipes);
        // UHV 2097152

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.shapeDust, (int) (16L)),
                ItemList.BatteryHull_UHV.get(1L))
            .itemOutputs(ItemList.BatteryHull_UHV_Full.get(1L))
            .duration(17 * SECONDS + 10 * TICKS)
            .eut(15720)
            .addTo(cannerRecipes);
        // UEV 8388608

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.shapeDust, (int) (32L)),
                ItemList.BatteryHull_UEV.get(1L))
            .itemOutputs(ItemList.BatteryHull_UEV_Full.get(1L))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(cannerRecipes);
        // UIV 33554432

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.shapeDust, (int) (64L)),
                ItemList.BatteryHull_UIV.get(1L))
            .itemOutputs(ItemList.BatteryHull_UIV_Full.get(1L))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(62880)
            .addTo(cannerRecipes);
        // UMV 134217728

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.shapeDust, (int) (4L)),
                ItemList.BatteryHull_UMV.get(1L))
            .itemOutputs(ItemList.BatteryHull_UMV_Full.get(1L))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(cannerRecipes);
        // UxV 536870912

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.shapeDust, (int) (8L)),
                ItemList.BatteryHull_UxV.get(1L))
            .itemOutputs(ItemList.BatteryHull_UxV_Full.get(1L))
            .duration(30 * SECONDS)
            .eut(251520)
            .addTo(cannerRecipes);
    }
}
