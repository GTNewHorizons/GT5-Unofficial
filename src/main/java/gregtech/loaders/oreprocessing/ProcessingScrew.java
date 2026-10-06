package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ProcessingScrew implements PartRecipeGenerator {

    public static final ProcessingScrew INSTANCE = new ProcessingScrew();

    private ProcessingScrew() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, String oreDictName, ItemStack stack) {
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            if (MaterialLibAPI.findStack(material, Shapes.bolt, 1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.findStack(material, Shapes.bolt, 1))
                    .itemOutputs(GTUtility.copyAmount(1, stack))
                    .duration(((int) Math.max(MaterialUtils.mass(material) / 8L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(latheRecipes);
            }
            if (MaterialUtils.unifiable(material)) {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        MaterialLibAPI.findStack(material, Shapes.screw, 1),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "fX", "X ", 'X', OrePrefixes.bolt.ingredient(material) });
                }
            }
        }
    }
}
