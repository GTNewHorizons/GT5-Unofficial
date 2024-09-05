package gregtech.loaders.postload.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class SmelterRecipes implements Runnable {

    @Override
    public void run() {
        GTModHandler.addSmeltingRecipe(ItemList.Food_Raw_PotatoChips.get(1L), ItemList.Food_PotatoChips.get(1L));

        GTModHandler
            .addSmeltingRecipe(ItemList.Food_Potato_On_Stick.get(1L), ItemList.Food_Potato_On_Stick_Roasted.get(1L));

        GTModHandler.addSmeltingRecipe(ItemList.Food_Raw_Bun.get(1L), ItemList.Food_Baked_Bun.get(1L));

        GTModHandler.addSmeltingRecipe(ItemList.Food_Raw_Bread.get(1L), ItemList.Food_Baked_Bread.get(1L));

        GTModHandler.addSmeltingRecipe(ItemList.Food_Raw_Baguette.get(1L), ItemList.Food_Baked_Baguette.get(1L));

        GTModHandler
            .addSmeltingRecipe(ItemList.Food_Raw_Pizza_Veggie.get(1L), ItemList.Food_Baked_Pizza_Veggie.get(1L));

        GTModHandler
            .addSmeltingRecipe(ItemList.Food_Raw_Pizza_Cheese.get(1L), ItemList.Food_Baked_Pizza_Cheese.get(1L));

        GTModHandler.addSmeltingRecipe(ItemList.Food_Raw_Pizza_Meat.get(1L), ItemList.Food_Baked_Pizza_Meat.get(1L));

        GTModHandler.addSmeltingRecipe(ItemList.Food_Raw_Baguette.get(1L), ItemList.Food_Baked_Baguette.get(1L));

        GTModHandler.addSmeltingRecipe(ItemList.Food_Raw_Cake.get(1L), ItemList.Food_Baked_Cake.get(1L));

        GTModHandler.addSmeltingRecipe(ItemList.Food_Raw_Cookie.get(1L), new ItemStack(Items.cookie, 1));

        GTModHandler.addSmeltingRecipe(new ItemStack(Items.slime_ball, 1), ItemList.IC2_Resin.get(1L));

        GTModHandler.addSmeltingRecipe(
            GTOreDictUnificator.get(OrePrefixes.ore, Materials.Graphite, 1L),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));

        GTModHandler.addSmeltingRecipe(
            GTOreDictUnificator.get(OrePrefixes.oreBlackgranite, Materials.Graphite, 1L),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));

        GTModHandler.addSmeltingRecipe(
            GTOreDictUnificator.get(OrePrefixes.oreEndstone, Materials.Graphite, 1L),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));

        GTModHandler.addSmeltingRecipe(
            GTOreDictUnificator.get(OrePrefixes.oreNetherrack, Materials.Graphite, 1L),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));

        GTModHandler.addSmeltingRecipe(
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 1L),
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.WroughtIron, 1L));

        GTModHandler.addSmeltingRecipe(
            GTOreDictUnificator.get(OrePrefixes.oreRedgranite, Materials.Graphite, 1L),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));

        GameRegistry.addSmelting(ItemList.CompressedFireclay.get(1), ItemList.Firebrick.get(1), 0);
    }
}
