package gtPlusPlus.core.item.init;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;

import gregtech.api.util.GTModHandler;
import gtPlusPlus.core.item.food.BaseItemMetaFood;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class ItemsFoods {

    public static void load() {
        BaseItemMetaFood food = new BaseItemMetaFood();
        food.registerMetaFoods();
        food.registerFoodsToOreDict();
        addCookingRecipes();
        addFoodDropsToMobs();
    }

    private static void addCookingRecipes() {
        GTModHandler
            .addSmeltingRecipe(GregtechItemList.RawHumanMeat.get(1), GregtechItemList.CookedHumanMeat.get(1), 0.4F);
        GTModHandler
            .addSmeltingRecipe(GregtechItemList.RawHorseMeat.get(1), GregtechItemList.CookedHorseMeat.get(1), 0.35F);
        GTModHandler
            .addSmeltingRecipe(GregtechItemList.RawWolfMeat.get(1), GregtechItemList.CookedWolfMeat.get(1), 0.35F);
        GTModHandler
            .addSmeltingRecipe(GregtechItemList.RawOcelotMeat.get(1), GregtechItemList.CookedOcelotMeat.get(1), 0.35F);
    }

    private static void addFoodDropsToMobs() {
        EntityUtils.registerDropsForMob(EntityVillager.class, GregtechItemList.RawHumanMeat.get(1), 2, 1500);
        EntityUtils.registerDropsForMob(EntityHorse.class, GregtechItemList.RawHorseMeat.get(1), 4, 4000);
        EntityUtils.registerDropsForMob(EntityWolf.class, GregtechItemList.RawWolfMeat.get(1), 2, 4000);
        EntityUtils.registerDropsForMob(EntityOcelot.class, GregtechItemList.RawOcelotMeat.get(1), 2, 4000);
        EntityUtils.registerDropsForMob(EntityBlaze.class, GregtechItemList.BlazeFlesh.get(1), 1, 500);
    }
}
