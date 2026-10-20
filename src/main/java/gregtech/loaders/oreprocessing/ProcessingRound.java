package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import appeng.core.Api;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ProcessingRound implements PartRecipeGenerator {

    public static final ProcessingRound INSTANCE = new ProcessingRound();

    private ProcessingRound() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            if (material.hasShape(Shapes.nugget)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(material, Shapes.nugget, 1))
                    .itemOutputs(GTUtility.copyAmount(1, stack))
                    .duration(((int) Math.max(MaterialUtils.mass(material) / 4L, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_ULV)
                    .addTo(latheRecipes);
            }

            if (MaterialUtils.unifiable(material)) {
                GTModHandler.addCraftingRecipe(
                    MaterialLibAPI.getStack(material, Shapes.round, 1),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "fX", "Xh", 'X', OrePrefixes.nugget.ingredient(material) });
                GTModHandler.addCraftingRecipe(
                    MaterialLibAPI.getStack(material, Shapes.round, 4),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "fXh", 'X', OrePrefixes.ingot.ingredient(material) });
            }
        }
        Api.INSTANCE.registries()
            .matterCannon()
            .registerAmmo(MaterialLibAPI.getStack(material, Shapes.round, 1), MaterialUtils.mass(material));
    }
}
