package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;

public class ProcessingNugget implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingNugget INSTANCE;

    public ProcessingNugget() {
        INSTANCE = this;
        OrePrefixes.nugget.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        // Blacklist materials which are handled by Werkstoff loader
        if (material == Materials.Calcium || material == Materials.Magnesia) return;

        if (MU.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM)
            && GTOreDictUnificator.get(OrePrefixes.gem, MU.smeltInto(material), 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Shape_Mold_Ball.get(0L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, MU.smeltInto(material), 1L))
                .duration(10 * SECONDS)
                .eut(calculateRecipeEU(material, 2))
                .addTo(alloySmelterRecipes);
        }

        if ((!MU.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM))
            && GTOreDictUnificator.get(OrePrefixes.ingot, MU.smeltInto(material), 1L) != null
            && material != Materials.Aluminium) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Shape_Mold_Ingot.get(0L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, MU.smeltInto(material), 1L))
                .duration(10 * SECONDS)
                .eut(calculateRecipeEU(material, 2))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(alloySmelterRecipes);
        }

        if (material.mStandardMoltenFluid != null) {
            if (!(material == Materials.AnnealedCopper || material == Materials.CastIron)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Nugget.get(0L))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nugget, material, 1L))
                    .fluidInputs(material.getMolten(1 * NUGGETS))
                    .duration(16 * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(fluidSolidifierRecipes);
            }
        }

        GTRecipeRegistrator.registerReverseFluidSmelting(stack, material, prefix.getMaterialAmount(), null, true);
        GTRecipeRegistrator
            .registerReverseMacerating(stack, material, prefix.getMaterialAmount(), null, null, null, false, true);
        if (!MU.hasFlag(material, GTMaterialFlag.NO_SMELTING)
            && GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L),
                    ItemList.Shape_Mold_Nugget.get(0L))
                .itemOutputs(GTUtility.copyAmount(9, stack))
                .duration(5 * SECONDS)
                .eut(calculateRecipeEU(material, 1))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(alloySmelterRecipes);
            if (material.getProcessingMaterialTierEU() < TierEU.IV) {
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.nugget, material, 8L),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "sI ", 'I', OrePrefixes.ingot.ingredient(material) });
            }
        }
    }
}
