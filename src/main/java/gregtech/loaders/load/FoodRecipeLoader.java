package gregtech.loaders.load;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IFoodStat;
import gregtech.api.items.GTGenericItem;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

public class FoodRecipeLoader implements Runnable {

    public static final List<GTFoodStatEntry> GT_FOOD_QUEUE = new ArrayList<>();

    public static class GTFoodStatEntry {

        public final ItemStack stack;
        public final IFoodStat stat;

        public GTFoodStatEntry(ItemStack stack, IFoodStat stat) {
            this.stack = stack;
            this.stat = stat;
        }
    }

    @Override
    public void run() {
        GT_FML_LOGGER.debug("GTMod: Adding Food Recipes to the Automatic Canning Machine.");

        final Set<Item> SpoiledFood = new HashSet<>(
            Arrays.asList(
                Items.rotten_flesh,
                Items.spider_eye,
                Items.chicken,
                Items.porkchop,
                Items.beef,
                Items.fermented_spider_eye,
                Items.poisonous_potato));

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cake, 1, WILDCARD), ItemList.FoodCanEmpty.get(12L))
            .itemOutputs(ItemList.FoodCanFilled.get(12L))
            .duration(30 * SECONDS)
            .eut(1)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Concrete.getDust(1), ItemList.FoodCanEmpty.get(4L))
            .itemOutputs(ItemList.FoodCanSpoiled.get(4L))
            .duration(30 * SECONDS)
            .eut(1)
            .addTo(cannerRecipes);

        for (GTFoodStatEntry entry : GT_FOOD_QUEUE) {
            Item metaItemInstance = entry.stack.getItem();

            int tFoodValue = entry.stat.getFoodLevel((MetaBaseItem) metaItemInstance, entry.stack, null);

            if (tFoodValue > 0) {
                GTValues.RA.stdBuilder()
                    .itemInputs(entry.stack, ItemList.FoodCanEmpty.get(tFoodValue))
                    .itemOutputs(
                        entry.stat.isRotten((MetaBaseItem) metaItemInstance, entry.stack, null)
                            ? ItemList.FoodCanSpoiled.get(tFoodValue)
                            : ItemList.FoodCanFilled.get(tFoodValue))
                    .duration(tFoodValue * 5 * SECONDS)
                    .eut(1)
                    .addTo(cannerRecipes);
            }
        }

        GT_FOOD_QUEUE.clear();

        // Adding non gt food
        for (Object o : Item.itemRegistry) {

            if (!(o instanceof Item)) continue;
            Item tItem = (Item) o;

            if (tItem.getUnlocalizedName() == null) continue;

            if (tItem instanceof GTGenericItem) continue;

            if (((tItem instanceof ItemFood) && (tItem != ItemList.FoodCanFilled.getItem())
                && (tItem != ItemList.FoodCanSpoiled.getItem()))) {

                int tFoodValue = ((ItemFood) tItem).func_150905_g(new ItemStack(tItem, 1, 0));

                if (tFoodValue > 0) {
                    GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                    recipeBuilder.itemInputs(new ItemStack(tItem, 1, WILDCARD), ItemList.FoodCanEmpty.get(tFoodValue));

                    if (SpoiledFood.contains(tItem)) {
                        if (GTUtility.getContainerItem(new ItemStack(tItem, 1, 0), true) == null) {
                            recipeBuilder.itemOutputs(ItemList.FoodCanSpoiled.get(tFoodValue));
                        } else {
                            recipeBuilder.itemOutputs(
                                ItemList.FoodCanSpoiled.get(tFoodValue),
                                GTUtility.getContainerItem(new ItemStack(tItem, 1, 0), true));
                        }
                    } else {
                        if (GTUtility.getContainerItem(new ItemStack(tItem, 1, 0), true) == null) {
                            recipeBuilder.itemOutputs(ItemList.FoodCanFilled.get(tFoodValue));
                        } else {
                            recipeBuilder.itemOutputs(
                                ItemList.FoodCanFilled.get(tFoodValue),
                                GTUtility.getContainerItem(new ItemStack(tItem, 1, 0), true));
                        }
                    }
                    recipeBuilder.duration(tFoodValue * 5 * SECONDS)
                        .eut(1)
                        .addTo(cannerRecipes);
                }
            }
        }
    }
}
