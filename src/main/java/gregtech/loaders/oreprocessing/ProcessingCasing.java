package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

/// The blockCasing/blockCasingAdvanced recipe registrator: crafting-table and assembler recipes for a
/// material's bolted machine casing and its rebolted (advanced) variant. Dispatched once per
/// blockCasing-generating material by [gregtech.loaders.shapeconsumers.ConsumerCasing]. The advanced casing's
/// outer plating is `plateDouble`, except Wood which uses `plank`. Like [ProcessingFrame] it is not registered
/// on its prefix, since no foreign mod's oredict entries ever drove casing recipe generation.
public class ProcessingCasing implements IOreRecipeRegistrator {

    public static final ProcessingCasing INSTANCE = new ProcessingCasing();

    private ProcessingCasing() {}

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        OrePrefixes outer = material == Materials2Materials.Wood ? OrePrefixes.plank : OrePrefixes.plateDouble;

        if (GTOreDictUnificator.get(OrePrefixes.plate, material, 1L) == null
            || GTOreDictUnificator.get(OrePrefixes.screw, material, 1L) == null
            || GTOreDictUnificator.get(OrePrefixes.gearGtSmall, material, 1L) == null
            || GTOreDictUnificator.get(OrePrefixes.gearGt, material, 1L) == null
            || GTOreDictUnificator.get(outer, material, 1L) == null) {
            return;
        }

        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(1, stack),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PSP", "PGP", "PSP", 'P', MU.craftIngredient(OrePrefixes.plate, material), 'S',
                MU.craftIngredient(OrePrefixes.screw, material), 'G',
                MU.craftIngredient(OrePrefixes.gearGtSmall, material) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, material, 6L),
                GTOreDictUnificator.get(OrePrefixes.screw, material, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, material, 1L))
            .itemOutputs(GTUtility.copyAmount(1, stack))
            .duration(10 * SECONDS)
            .eut(calculateRecipeEU(material, 16))
            .addTo(assemblerRecipes);

        ItemStack advanced = GTOreDictUnificator.get(OrePrefixes.blockCasingAdvanced, material, 1L);
        if (advanced == null) return;

        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(1, advanced),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PSP", "PGP", "PSP", 'P', MU.craftIngredient(outer, material), 'S',
                MU.craftIngredient(OrePrefixes.screw, material), 'G',
                MU.craftIngredient(OrePrefixes.gearGt, material) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(outer, material, 6L),
                GTOreDictUnificator.get(OrePrefixes.screw, material, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGt, material, 1L))
            .itemOutputs(GTUtility.copyAmount(1, advanced))
            .duration(10 * SECONDS)
            .eut(calculateRecipeEU(material, 16))
            .addTo(assemblerRecipes);
    }
}
