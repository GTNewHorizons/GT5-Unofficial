package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;

public class SifterRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.gravel, 1, 0))
            .itemOutputs(
                new ItemStack(Items.flint, 1, 0),
                new ItemStack(Items.flint, 1, 0),
                new ItemStack(Items.flint, 1, 0),
                new ItemStack(Items.flint, 1, 0),
                new ItemStack(Items.flint, 1, 0),
                new ItemStack(Items.flint, 1, 0))
            .outputChances(10000, 9000, 8000, 6000, 3300, 2500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.crushedPurified, (int) (1L)))
            .itemOutputs(
                new ItemStack(Items.coal, 1, 0),
                new ItemStack(Items.coal, 1, 0),
                new ItemStack(Items.coal, 1, 0),
                new ItemStack(Items.coal, 1, 0),
                new ItemStack(Items.coal, 1, 0),
                MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.dust, (int) (1L)))
            .outputChances(10000, 9000, 8000, 7000, 6000, 5000)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ElectronicsLump.get(1))
            .itemOutputs(
                ItemList.Electric_Motor_LV.get(1),
                getModItem(Forestry.ID, "thermionicTubes", 1L, 5),
                getModItem(EnderIO.ID, "itemPowerConduit", 1L, 0))
            .outputChances(25 * 100, 25 * 100, 25 * 100)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(sifterRecipes);
    }
}
