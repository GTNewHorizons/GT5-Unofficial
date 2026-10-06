package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

/// Registers crafting and assembler recipes for a material's machine casing and its advanced variant. Called once
/// per casing material by [ShapeRecipeTable], not on its prefix.
public class ProcessingCasing implements IOreRecipeRegistrator {

    public static final ProcessingCasing INSTANCE = new ProcessingCasing();

    private ProcessingCasing() {}

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        OrePrefixes outer = material == Materials.Wood ? OrePrefixes.plank : OrePrefixes.plateDouble;

        if (MaterialLibAPI.findStack(material, Shapes.plate, 1) == null
            || MaterialLibAPI.findStack(material, Shapes.screw, 1) == null
            || MaterialLibAPI.findStack(material, Shapes.gearGtSmall, 1) == null
            || MaterialLibAPI.findStack(material, Shapes.gearGt, 1) == null
            || outerPart(material, 1L) == null) {
            return;
        }

        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(1, stack),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PSP", "PGP", "PSP", 'P', OrePrefixes.plate.ingredient(material), 'S',
                OrePrefixes.screw.ingredient(material), 'G', OrePrefixes.gearGtSmall.ingredient(material) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.findStack(material, Shapes.plate, 6),
                MaterialLibAPI.findStack(material, Shapes.screw, 2),
                MaterialLibAPI.findStack(material, Shapes.gearGtSmall, 1))
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
                MaterialLibAPI.findStack(material, Shapes.screw, 2),
                MaterialLibAPI.findStack(material, Shapes.gearGt, 1))
            .itemOutputs(GTUtility.copyAmount(1, advanced))
            .duration(10 * SECONDS)
            .eut(calculateRecipeEU(material, 16))
            .addTo(assemblerRecipes);
    }

    /// Returns the advanced casing's outer plating. That is vanilla planks for [Materials#Wood], otherwise the
    /// material's double plate, or null when it has none.
    private static @Nullable ItemStack outerPart(Material material, long amount) {
        if (material == Materials.Wood) return new ItemStack(Blocks.planks, (int) amount, WILDCARD);
        return MaterialLibAPI.findStack(material, Shapes.plateDouble, (int) amount);
    }
}
