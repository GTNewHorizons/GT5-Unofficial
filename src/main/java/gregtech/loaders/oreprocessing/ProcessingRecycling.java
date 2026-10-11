package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

public class ProcessingRecycling implements PartRecipeGenerator {

    public static final ProcessingRecycling INSTANCE = new ProcessingRecycling();

    private ProcessingRecycling() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        if ((material != Materials.Empty) && (GTUtility.getFluidForFilledItem(stack, true) == null)
            && !MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_FLUID)
            && material.hasShape(Shapes.dust)) {
            GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
            recipeBuilder.itemInputs(stack);
            if (GTUtility.getContainerItem(stack, true) == null) {
                recipeBuilder.itemOutputs(
                    GTUtility.copyAmount(
                        prefix.getMaterialAmount() / 3628800L,
                        MaterialLibAPI.getStack(material, Shapes.dust, 1)));
            } else {
                recipeBuilder.itemOutputs(
                    GTUtility.getContainerItem(stack, true),
                    GTUtility.copyAmount(
                        prefix.getMaterialAmount() / 3628800L,
                        MaterialLibAPI.getStack(material, Shapes.dust, 1)));
            }
            recipeBuilder.duration(((int) Math.max(MaterialUtils.mass(material) / 2L, 1L)) * TICKS)
                .eut(2)
                .addTo(cannerRecipes);
        }
    }
}
