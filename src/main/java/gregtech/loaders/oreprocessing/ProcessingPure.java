package gregtech.loaders.oreprocessing;

import static goodgenerator.util.NaquadahRecipeOutputs.convert;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtnhlanth.util.LanthanidesRecipeOutputs.convertOre;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;

public class ProcessingPure implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static final ProcessingPure INSTANCE = new ProcessingPure();

    private ProcessingPure() {}

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                convert(material, MaterialLibAPI.findStack(MaterialUtils.macerateInto(material), Shapes.dustPure, 1)))
            .duration(10)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                convertOre(
                    material,
                    GTUtility.copyAmount(
                        1L,
                        MaterialLibAPI.findStack(MaterialUtils.macerateInto(material), Shapes.dustPure, 1),
                        MaterialLibAPI.findStack(MaterialUtils.macerateInto(material), Shapes.dust, 1)),
                    MaterialLibAPI.findStack(
                        GTUtility.selectItemInList(
                            1,
                            MaterialUtils.macerateInto(material),
                            MaterialUtils.oreByProducts(material)),
                        Shapes.dust,
                        1)))
            .outputChances(10000, 1000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);
    }
}
