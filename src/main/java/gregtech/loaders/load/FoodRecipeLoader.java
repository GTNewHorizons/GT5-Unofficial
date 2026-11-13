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
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTUtility;

public class FoodRecipeLoader implements Runnable {

    private static final List<GTFoodStatEntry> GT_FOOD_QUEUE = new ArrayList<>();
    private static boolean hasRun = false;

    public static class GTFoodStatEntry {

        public final ItemStack stack;
        public final IFoodStat stat;

        public GTFoodStatEntry(ItemStack stack, IFoodStat stat) {
            this.stack = stack;
            this.stat = stat;
        }
    }

    public static void queueFood(ItemStack foodStack, IFoodStat foodStat) {
        if (hasRun) {
            GT_FML_LOGGER.warn(
                "Food item {} was registered after FoodRecipeLoader ran, no canning recipe will be added",
                foodStack.getDisplayName());
            return;
        }
        GT_FOOD_QUEUE.add(new GTFoodStatEntry(foodStack.copy(), foodStat));
    }

    @Override
    public void run() {
        GT_FML_LOGGER.debug("GTMod: Adding Food Recipes to the Automatic Canning Machine.");

        addSpecialRecipes();
        addQueuedGTFoodRecipes();
        addRegistryFoodRecipes();
    }

    private void addSpecialRecipes() {
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
    }

    private void addQueuedGTFoodRecipes() {
        for (GTFoodStatEntry entry : GT_FOOD_QUEUE) {
            MetaBaseItem metaItem = (MetaBaseItem) entry.stack.getItem();
            int foodValue = entry.stat.getFoodLevel(metaItem, entry.stack, null);
            if (foodValue <= 0) continue;

            ItemStack filledCans = entry.stat.isRotten(metaItem, entry.stack, null)
                ? ItemList.FoodCanSpoiled.get(foodValue)
                : ItemList.FoodCanFilled.get(foodValue);

            GTValues.RA.stdBuilder()
                .itemInputs(entry.stack, ItemList.FoodCanEmpty.get(foodValue))
                .itemOutputs(filledCans)
                .duration(foodValue * 5 * SECONDS)
                .eut(1)
                .addTo(cannerRecipes);
        }

        GT_FOOD_QUEUE.clear();
        hasRun = true;
    }

    private void addRegistryFoodRecipes() {
        final Set<Item> spoiledFoods = new HashSet<>(
            Arrays.asList(
                Items.rotten_flesh,
                Items.spider_eye,
                Items.chicken,
                Items.porkchop,
                Items.beef,
                Items.fermented_spider_eye,
                Items.poisonous_potato));

        for (Object registryEntry : Item.itemRegistry) {
            if (!(registryEntry instanceof ItemFood foodItem)) continue;
            if (foodItem.getUnlocalizedName() == null) continue;

            ItemStack sampleStack = new ItemStack(foodItem, 1, 0);
            int foodValue = foodItem.func_150905_g(sampleStack);
            if (foodValue <= 0) continue;

            ItemStack filledCans = spoiledFoods.contains(foodItem) ? ItemList.FoodCanSpoiled.get(foodValue)
                : ItemList.FoodCanFilled.get(foodValue);
            ItemStack containerItem = GTUtility.getContainerItem(sampleStack, true);
            ItemStack[] outputs = containerItem == null ? new ItemStack[] { filledCans }
                : new ItemStack[] { filledCans, containerItem };

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(foodItem, 1, WILDCARD), ItemList.FoodCanEmpty.get(foodValue))
                .itemOutputs(outputs)
                .duration(foodValue * 5 * SECONDS)
                .eut(1)
                .addTo(cannerRecipes);
        }
    }
}
