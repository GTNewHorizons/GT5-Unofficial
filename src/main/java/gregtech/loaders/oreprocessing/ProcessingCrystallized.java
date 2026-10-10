package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

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

public class ProcessingCrystallized implements PartRecipeGenerator {

    public static final ProcessingCrystallized INSTANCE = new ProcessingCrystallized();

    private ProcessingCrystallized() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }
        ProcessingUtils.addHammerRecipe(prefix, material, Shapes.dust, true);

        if (MaterialUtils.macerateInto(material) == null) {
            return;
        }

        if (MaterialLibAPI.findStack(MaterialUtils.macerateInto(material), Shapes.dust, 1) == null) {
            return;
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(MaterialLibAPI.findStack(MaterialUtils.macerateInto(material), Shapes.dust, 1))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(MaterialLibAPI.findStack(MaterialUtils.macerateInto(material), Shapes.dust, 1))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

    }

}
