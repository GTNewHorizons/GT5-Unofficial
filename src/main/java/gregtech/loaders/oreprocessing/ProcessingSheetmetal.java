package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;

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
import gregtech.api.util.GTUtility;

/// Registers the bender recipe that presses two plates into a `sheetmetal`. Called once per sheetmetal material by
/// [ShapeRecipeTable].
public class ProcessingSheetmetal implements PartRecipeGenerator {

    public static final ProcessingSheetmetal INSTANCE = new ProcessingSheetmetal();

    private ProcessingSheetmetal() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_RECIPES)) return;

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(material, Shapes.plate, 2), GTUtility.getIntegratedCircuit(11))
            .itemOutputs(GTUtility.copyAmount(1, stack))
            .eut(TierEU.RECIPE_LV)
            .duration(10)
            .addTo(benderRecipes);
    }
}
