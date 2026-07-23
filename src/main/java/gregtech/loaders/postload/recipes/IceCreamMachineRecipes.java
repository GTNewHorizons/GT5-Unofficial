package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.iceCreamMachineRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;

public class IceCreamMachineRecipes implements Runnable {

    @Override
    public void run() {
        // Fake recipe
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Credit_Greg_Gold.get(2))
            .itemOutputs(ItemList.Ice_Cream_Random.get(1))
            .duration(25 * SECONDS)
            .eut(0)
            .fake()
            .addTo(iceCreamMachineRecipes);

        addRecipe(ItemList.Ice_Cream_Banana);
        addRecipe(ItemList.Ice_Cream_Benzene);
        addRecipe(ItemList.Ice_Cream_Blueberry);
        addRecipe(ItemList.Ice_Cream_Brownie);
        addRecipe(ItemList.Ice_Cream_Chocolate);
        addRecipe(ItemList.Ice_Cream_Coffee);
        addRecipe(ItemList.Ice_Cream_CookieDough);
        addRecipe(ItemList.Ice_Cream_CookiesAndCream);
        addRecipe(ItemList.Ice_Cream_CottonCandy);
        addRecipe(ItemList.Ice_Cream_JustCone);
        addRecipe(ItemList.Ice_Cream_Mango);
        addRecipe(ItemList.Ice_Cream_Meat);
        addRecipe(ItemList.Ice_Cream_MintChip);
        addRecipe(ItemList.Ice_Cream_Neapolitan);
        addRecipe(ItemList.Ice_Cream_NoFlavor);
        addRecipe(ItemList.Ice_Cream_PeanutButter);
        addRecipe(ItemList.Ice_Cream_Pistachio);
        addRecipe(ItemList.Ice_Cream_SaltedCaramel);
        addRecipe(ItemList.Ice_Cream_Strawberry);
        addRecipe(ItemList.Ice_Cream_Vanilla);
    }

    private void addRecipe(ItemList flavor) {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Credit_Greg_Gold.get(2))
            .itemOutputs(flavor.get(1))
            .duration(25 * SECONDS)
            .eut(0)
            .hidden()
            .addTo(iceCreamMachineRecipes);
    }
}
