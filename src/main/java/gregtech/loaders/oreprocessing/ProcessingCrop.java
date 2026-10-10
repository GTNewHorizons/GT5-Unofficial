package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTUtility;

public class ProcessingCrop implements ForeignPartRule {

    public static final ProcessingCrop INSTANCE = new ProcessingCrop();

    private ProcessingCrop() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, String oreDictName, ItemStack stack) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(8, stack))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);
    }
}
