package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;

public class ProcessingNugget implements PartRecipeGenerator {

    public static final ProcessingNugget INSTANCE = new ProcessingNugget();

    private ProcessingNugget() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, String oreDictName, ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM)
            && MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.gem, 1) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Shape_Mold_Ball.get(0L))
                .itemOutputs(MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.gem, 1))
                .duration(10 * SECONDS)
                .eut(calculateRecipeEU(material, 2))
                .addTo(alloySmelterRecipes);
        }

        if ((!MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM))
            && MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.ingot, 1) != null
            && material != Materials.Aluminium) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Shape_Mold_Ingot.get(0L))
                .itemOutputs(MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.ingot, 1))
                .duration(10 * SECONDS)
                .eut(calculateRecipeEU(material, 2))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(alloySmelterRecipes);
        }

        if (MaterialUtils.hasMolten(material)) {
            if (!(material == Materials.AnnealedCopper || material == Materials.CastIron)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Nugget.get(0L))
                    .itemOutputs(MaterialLibAPI.findStack(material, Shapes.nugget, 1))
                    .fluidInputs(MaterialUtils.molten(material, 1 * NUGGETS))
                    .duration(16 * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(fluidSolidifierRecipes);
            }
        }

        GTRecipeRegistrator.registerReverseFluidSmelting(stack, material, prefix.getMaterialAmount(), null, true);
        GTRecipeRegistrator
            .registerReverseMacerating(stack, material, prefix.getMaterialAmount(), null, null, null, false, true);
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMELTING)
            && MaterialLibAPI.findStack(material, Shapes.ingot, 1) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.findStack(material, Shapes.ingot, 1), ItemList.Shape_Mold_Nugget.get(0L))
                .itemOutputs(GTUtility.copyAmount(9, stack))
                .duration(5 * SECONDS)
                .eut(calculateRecipeEU(material, 1))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(alloySmelterRecipes);
            if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV && material != Materials.Unstable) {
                GTModHandler.addCraftingRecipe(
                    MaterialLibAPI.findStack(material, Shapes.nugget, 8),
                    GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
                    new Object[] { "sI ", 'I', OrePrefixes.ingot.ingredient(material) });
            }
        }
    }
}
