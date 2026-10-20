package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

/// Registers crafting and assembler recipes for a material's machine casing and its advanced variant. Called once
/// per casing material by [ShapeRecipeTable], not on its prefix.
public class ProcessingCasing implements PartRecipeGenerator {

    public static final ProcessingCasing INSTANCE = new ProcessingCasing();

    private ProcessingCasing() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        OrePrefixes outer = material == Materials.Wood ? OrePrefixes.plank : OrePrefixes.plateDouble;

        if (!material.hasShape(Shapes.plate) || !material.hasShape(Shapes.screw)
            || !material.hasShape(Shapes.gearGtSmall)
            || !material.hasShape(Shapes.gearGt)
            || (material != Materials.Wood && !material.hasShape(Shapes.plateDouble))) {
            return;
        }

        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(1, stack),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PSP", "PGP", "PSP", 'P', OrePrefixes.plate.ingredient(material), 'S',
                OrePrefixes.screw.ingredient(material), 'G', OrePrefixes.gearGtSmall.ingredient(material) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(material, Shapes.plate, 6),
                MaterialLibAPI.getStack(material, Shapes.screw, 2),
                MaterialLibAPI.getStack(material, Shapes.gearGtSmall, 1))
            .itemOutputs(GTUtility.copyAmount(1, stack))
            .duration(10 * SECONDS)
            .eut(calculateRecipeEU(material, 16))
            .addTo(assemblerRecipes);

        ItemStack advanced = MaterialLibAPI.findStack(material, BlockShapes.blockCasingAdvanced, 1);
        if (advanced == null) return;

        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(1, advanced),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PSP", "PGP", "PSP", 'P', outer.ingredient(material), 'S',
                OrePrefixes.screw.ingredient(material), 'G', OrePrefixes.gearGt.ingredient(material) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                outerPart(material, 6L),
                MaterialLibAPI.getStack(material, Shapes.screw, 2),
                MaterialLibAPI.getStack(material, Shapes.gearGt, 1))
            .itemOutputs(GTUtility.copyAmount(1, advanced))
            .duration(10 * SECONDS)
            .eut(calculateRecipeEU(material, 16))
            .addTo(assemblerRecipes);
    }

    /// Returns the advanced casing's outer plating. That is vanilla planks for [Materials#Wood], otherwise the
    /// material's double plate.
    private static ItemStack outerPart(Material material, long amount) {
        if (material == Materials.Wood) return new ItemStack(Blocks.planks, (int) amount, WILDCARD);
        return MaterialLibAPI.getStack(material, Shapes.plateDouble, (int) amount);
    }
}
