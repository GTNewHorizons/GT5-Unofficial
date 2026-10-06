package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.MaterialParts;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

/// Registers crafting and assembler recipes for a material's machine casing and its advanced variant. Called once
/// per casing material by [gregtech.loaders.shapeconsumers.ConsumerCasing], not on its prefix.
public class ProcessingCasing implements IOreRecipeRegistrator {

    public static final ProcessingCasing INSTANCE = new ProcessingCasing();

    private ProcessingCasing() {}

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        OrePrefixes outer = material == Materials.Wood ? OrePrefixes.plank : OrePrefixes.plateDouble;

        if (MaterialParts.stack(OrePrefixes.plate, material, 1L) == null
            || MaterialParts.stack(OrePrefixes.screw, material, 1L) == null
            || MaterialParts.stack(OrePrefixes.gearGtSmall, material, 1L) == null
            || MaterialParts.stack(OrePrefixes.gearGt, material, 1L) == null
            || outerPart(material, 1L) == null) {
            return;
        }

        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(1, stack),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PSP", "PGP", "PSP", 'P', MaterialParts.craftIngredient(OrePrefixes.plate, material), 'S',
                MaterialParts.craftIngredient(OrePrefixes.screw, material), 'G',
                MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, material) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.stack(OrePrefixes.plate, material, 6L),
                MaterialParts.stack(OrePrefixes.screw, material, 2L),
                MaterialParts.stack(OrePrefixes.gearGtSmall, material, 1L))
            .itemOutputs(GTUtility.copyAmount(1, stack))
            .duration(10 * SECONDS)
            .eut(calculateRecipeEU(material, 16))
            .addTo(assemblerRecipes);

        ItemStack advanced = MaterialParts.stack(OrePrefixes.blockCasingAdvanced, material, 1L);
        if (advanced == null) return;

        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(1, advanced),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PSP", "PGP", "PSP", 'P', MaterialParts.craftIngredient(outer, material), 'S',
                MaterialParts.craftIngredient(OrePrefixes.screw, material), 'G',
                MaterialParts.craftIngredient(OrePrefixes.gearGt, material) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                outerPart(material, 6L),
                MaterialParts.stack(OrePrefixes.screw, material, 2L),
                MaterialParts.stack(OrePrefixes.gearGt, material, 1L))
            .itemOutputs(GTUtility.copyAmount(1, advanced))
            .duration(10 * SECONDS)
            .eut(calculateRecipeEU(material, 16))
            .addTo(assemblerRecipes);
    }

    /// The advanced casing's outer plating: vanilla planks for [Materials#Wood], otherwise the material's double
    /// plate.
    private static @Nullable ItemStack outerPart(Material material, long amount) {
        if (material == Materials.Wood) return new ItemStack(Blocks.planks, (int) amount, WILDCARD);
        return MaterialParts.stack(OrePrefixes.plateDouble, material, amount);
    }
}
