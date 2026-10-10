package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

/// Registers the stick-to-frame recipes for a material's `frameGt`. Materials below IV processing tier also get a
/// hand-crafting recipe. Called once per frame material by [ShapeRecipeTable], not on its prefix.
public class ProcessingFrame implements PartRecipeGenerator {

    public static final ProcessingFrame INSTANCE = new ProcessingFrame();

    private ProcessingFrame() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
            GTModHandler.addCraftingRecipe(
                GTUtility.copyAmount(2, stack),
                GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED
                    | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
                new Object[] { "SSS", "SwS", "SSS", 'S', OrePrefixes.stick.ingredient(material) });
        }

        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_RECIPES)
            && MaterialLibAPI.findStack(material, Shapes.stick, 1) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.findStack(material, Shapes.stick, 4))
                .circuit(4)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(calculateRecipeEU(material, 7))
                .addTo(assemblerRecipes);
        }
    }
}
