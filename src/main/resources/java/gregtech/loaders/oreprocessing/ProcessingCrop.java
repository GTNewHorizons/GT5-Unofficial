package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTUtility;

public class ProcessingCrop implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCrop() {
        OrePrefixes.crop.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(8, aStack))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);
    }
}
