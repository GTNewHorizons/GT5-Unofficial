package gtPlusPlus.core.item.init;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.food.BaseItemMetaFood;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;

public class ItemsFoods {

    public static void load() {
        run();
    }

    private static void run() {

        ModItems.itemMetaFood = new BaseItemMetaFood();
        BaseItemMetaFood.registerFoodsToOreDict();
        addCookingRecipes();
        addFoodDropsToMobs();
    }

    private static ItemStack getMetaFoodStack(int aID) {
        return ItemUtils.simpleMetaStack(ModItems.itemMetaFood, aID, 1);
    }

    private static void addCookingRecipes() {

        RecipeUtils.addSmeltingRecipe(getMetaFoodStack(0), getMetaFoodStack(1), 0.4F);
        RecipeUtils.addSmeltingRecipe(getMetaFoodStack(2), getMetaFoodStack(3), 0.35F);
        RecipeUtils.addSmeltingRecipe(getMetaFoodStack(4), getMetaFoodStack(5), 0.35F);
        RecipeUtils.addSmeltingRecipe(getMetaFoodStack(6), getMetaFoodStack(7), 0.35F);
    }

    private static void addFoodDropsToMobs() {

        EntityUtils.registerDropsForMob(EntityVillager.class, getMetaFoodStack(0), 2, 1500);
        EntityUtils.registerDropsForMob(EntityHorse.class, getMetaFoodStack(2), 4, 4000);
        EntityUtils.registerDropsForMob(EntityWolf.class, getMetaFoodStack(4), 2, 4000);
        EntityUtils.registerDropsForMob(EntityOcelot.class, getMetaFoodStack(6), 2, 4000);
        EntityUtils.registerDropsForMob(EntityBlaze.class, getMetaFoodStack(8), 1, 500);
    }
}
