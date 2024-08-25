package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GT_ModHandler.getIC2Item;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;

public class ThermalCentrifugeRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.SunnariumCell.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 1),
                new ItemStack(Items.glowstone_dust, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        // Recipes from the old ic2 recipe maps
        GT_Values.RA.stdBuilder()
            .itemInputs(getIC2Item("reactorDepletedMOXSimple", 1))
            .itemOutputs(
                getIC2Item("smallPlutonium", 1),
                getIC2Item("Plutonium", 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getIC2Item("reactorDepletedMOXDual", 1))
            .itemOutputs(
                getIC2Item("smallPlutonium", 2),
                getIC2Item("Plutonium", 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 3))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getIC2Item("reactorDepletedMOXQuad", 1))
            .itemOutputs(
                getIC2Item("smallPlutonium", 4),
                getIC2Item("Plutonium", 12),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 6))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getIC2Item("reactorDepletedUraniumSimple", 1))
            .itemOutputs(
                getIC2Item("smallPlutonium", 1),
                getIC2Item("Uran238", 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getIC2Item("reactorDepletedUraniumDual", 1))
            .itemOutputs(
                getIC2Item("smallPlutonium", 2),
                getIC2Item("Uran238", 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 3))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getIC2Item("reactorDepletedUraniumQuad", 1))
            .itemOutputs(
                getIC2Item("smallPlutonium", 4),
                getIC2Item("Uran238", 16),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 6))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getIC2Item("RTGPellets", 1))
            .itemOutputs(getIC2Item("Plutonium", 3), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 54))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 2))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lithium, 1))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 4))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.cobblestone, 1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);
    }
}
