package gregtech.loaders.postload.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.common.ores.GTOreAdapter;

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
            GTOreAdapter.INSTANCE.getVariantStack(Materials.Graphite, StoneType.Stone, false),
            MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, 1));

        GTModHandler.addSmeltingRecipe(
            GTOreAdapter.INSTANCE.getVariantStack(Materials.Graphite, StoneType.BlackGranite, false),
            MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, 1));

        GTModHandler.addSmeltingRecipe(
            GTOreAdapter.INSTANCE.getVariantStack(Materials.Graphite, StoneType.Endstone, false),
            MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, 1));

        GTModHandler.addSmeltingRecipe(
            GTOreAdapter.INSTANCE.getVariantStack(Materials.Graphite, StoneType.Netherrack, false),
            MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, 1));

        GTModHandler.addSmeltingRecipe(
            MaterialLibAPI.getStack(Materials.Iron, Shapes.nugget, 1),
            MaterialLibAPI.getStack(Materials.CastIron, Shapes.nugget, 1));

        GTModHandler.addSmeltingRecipe(
            GTOreAdapter.INSTANCE.getVariantStack(Materials.Graphite, StoneType.RedGranite, false),
            MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, 1));

        GameRegistry.addSmelting(ItemList.CompressedFireclay.get(1), ItemList.Firebrick.get(1), 0);
    }
}
