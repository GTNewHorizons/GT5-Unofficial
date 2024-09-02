package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.BuildCraftSilicon;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;

public class FormingPressRecipes implements Runnable {

    @Override
    public void run() {
        if (BuildCraftSilicon.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 1))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 1))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 2))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1L),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 3))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 2L, 4))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.NetherQuartz, 1L),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 5))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Items.comparator, 1, 32767),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 6))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);
        }

        if (AppliedEnergistics2.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 0L, 13))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 16))
                .duration(10 * SECONDS)
                .eut(16)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.CertusQuartz, 1L),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 0L, 13))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 16))
                .duration(10 * SECONDS)
                .eut(16)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1L),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 0L, 14))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 17))
                .duration(10 * SECONDS)
                .eut(16)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 0L, 15))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 18))
                .duration(10 * SECONDS)
                .eut(16)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.SiliconSG, 1L),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 0L, 19))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 20))
                .duration(10 * SECONDS)
                .eut(16)
                .addTo(formingPressRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Food_Dough_Sugar.get(4L), ItemList.Shape_Mold_Cylinder.get(0L))
            .itemOutputs(ItemList.Food_Raw_Cake.get(1L))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(4)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Cupronickel, 1L),
                ItemList.Shape_Mold_Credit.get(0L))
            .itemOutputs(ItemList.Credit_Greg_Cupronickel.get(4L))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Brass, 1L),
                ItemList.Shape_Mold_Credit.get(0L))
            .itemOutputs(ItemList.Coin_Doge.get(4L))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                ItemList.Shape_Mold_Credit.get(0L))
            .itemOutputs(ItemList.Credit_Iron.get(4L))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                ItemList.Shape_Mold_Credit.get(0L))
            .itemOutputs(ItemList.Credit_Iron.get(4L))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Brick, 1L),
                ItemList.Shape_Mold_Ingot.get(0L))
            .itemOutputs(new ItemStack(Items.brick, 1, 0))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(formingPressRecipes);
    }
}
