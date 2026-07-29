package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;

/// The `sheetmetal` recipe registrator: a single bender recipe from two plates of the material, ported from
/// `gregtech.common.blocks.BlockSheetMetal#registerRecipes`. Dispatched once per sheetmetal-generating material
/// by [gregtech.loaders.shapeconsumers.ConsumerSheetmetal].
public class ProcessingSheetmetal implements IOreRecipeRegistrator {

    public static final ProcessingSheetmetal INSTANCE = new ProcessingSheetmetal();

    private ProcessingSheetmetal() {}

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_RECIPES)) return;

        GTValues.RA.stdBuilder()
            .itemInputs(MU.partOf(material, OrePrefixes.plate, 2), GTUtility.getIntegratedCircuit(11))
            .itemOutputs(GTUtility.copyAmount(1, stack))
            .eut(TierEU.RECIPE_LV)
            .duration(10)
            .addTo(benderRecipes);
    }
}
