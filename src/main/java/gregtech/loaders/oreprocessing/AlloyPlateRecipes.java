package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

/// Reinforced glass and windmill recipes for the advanced and carbon alloy plates, GregTech's own and IC2's.
final class AlloyPlateRecipes {

    private AlloyPlateRecipes() {}

    static void register() {
        reinforcedGlass(ItemList.AdvancedAlloyPlate.get(1));
        carbon(ItemList.CarbonAlloyPlate.get(1));
        reinforcedGlass(GTModHandler.getModItem("IC2", "itemPartAlloy", 1, 0));
        carbon(GTModHandler.getModItem("IC2", "itemPartCarbonPlate", 1, 0));
    }

    private static void carbon(ItemStack plate) {
        if (!GTUtility.isStackValid(plate)) return;
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("generator", 1L), GTUtility.copyAmount(4, plate))
            .itemOutputs(GTModHandler.getIC2Item("windMill", 1L))
            .duration(5 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(assemblerRecipes);
        reinforcedGlass(plate);
    }

    private static void reinforcedGlass(ItemStack plate) {
        if (!GTUtility.isStackValid(plate)) return;
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, plate), new ItemStack(Blocks.glass, 3, WILDCARD))
            .itemOutputs(ItemList.ReinforcedGlass.get(4L))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(alloySmelterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, plate), MaterialLibAPI.getStack(Materials.Glass, Shapes.dust, 3))
            .itemOutputs(ItemList.ReinforcedGlass.get(4L))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(alloySmelterRecipes);
    }
}
