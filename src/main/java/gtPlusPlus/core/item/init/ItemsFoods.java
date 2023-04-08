package gtPlusPlus.core.item.init;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.foods.BaseItemFood;
import gtPlusPlus.core.item.base.foods.BaseItemHotFood;
import gtPlusPlus.core.item.food.BaseItemMetaFood;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;

public class ItemsFoods {

    public static void load() {
        run();
    }

    private static void run() {

        // Raisin Bread
        ModItems.itemIngotRaisinBread = new BaseItemFood(
                "itemIngotRaisinBread",
                "Raisin Bread",
                3,
                1.5f,
                false,
                new PotionEffect(Potion.weakness.id, 40, 1)).setAlwaysEdible();
        GT_OreDictUnificator.registerOre(
                "foodRaisinBread",
                ItemUtils.getItemStackFromFQRN(GTPlusPlus.ID + ":itemIngotRaisinBread", 1));
        // Hot Raisin Bread
        ModItems.itemHotIngotRaisinBread = new BaseItemHotFood(
                "itemHotIngotRaisinBread",
                1,
                0.5f,
                "Raisin Bread",
                120,
                ModItems.itemIngotRaisinBread);
        GT_OreDictUnificator.registerOre(
                "foodHotRaisinBread",
                ItemUtils.getItemStackFromFQRN(GTPlusPlus.ID + ":itemHotIngotRaisinBread", 1));

        // Raisin Bread
        ModItems.itemFoodRaisinToast = new BaseItemFood("itemFoodRaisinToast", "Raisin Toast", 1, 0.5f, false)
                .setAlwaysEdible();
        GT_OreDictUnificator.registerOre(
                "foodRaisinToast",
                ItemUtils.getItemStackFromFQRN(GTPlusPlus.ID + ":itemFoodRaisinToast", 1));
        // Hot Raisin Bread
        ModItems.itemHotFoodRaisinToast = new BaseItemHotFood(
                "itemHotFoodRaisinToast",
                1,
                0.5f,
                "Raisin Toast",
                20,
                ModItems.itemFoodRaisinToast);
        GT_OreDictUnificator.registerOre(
                "foodHotRaisinToast",
                ItemUtils.getItemStackFromFQRN(GTPlusPlus.ID + ":itemHotFoodRaisinToast", 1));

        // Raisin Bread
        ModItems.itemFoodCurriedSausages = new BaseItemFood(
                "itemFoodCurriedSausages",
                "Curried Sausages",
                5,
                2f,
                false);
        GT_OreDictUnificator.registerOre(
                "foodCurriedSausages",
                ItemUtils.getItemStackFromFQRN(GTPlusPlus.ID + ":itemFoodCurriedSausages", 1));
        // Hot Raisin Bread
        ModItems.itemHotFoodCurriedSausages = new BaseItemHotFood(
                "itemHotFoodCurriedSausages",
                1,
                0.5f,
                "Curried Sausages",
                240,
                ModItems.itemFoodCurriedSausages);
        GT_OreDictUnificator.registerOre(
                "foodHotCurriedSausages",
                ItemUtils.getItemStackFromFQRN(GTPlusPlus.ID + ":itemHotFoodCurriedSausages", 1));

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
