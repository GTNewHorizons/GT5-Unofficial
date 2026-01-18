package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.util.HashSet;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingPlank implements gregtech.api.interfaces.IOreRecipeRegistrator {

    /**
     * Sometimes automatically searching for recipes is not enough to ensure that the corresponding slab is correctly
     * assigned. These Special lists exist to provide a direct mapping from plank to slab in such cases.
     *
     * Configuration works as follows: Each nth entry in the SPECIAL_PLANKS lists corresponds to the same nth entry in
     * the SPECIAL_SLABS list. Entries should consist of Strings formatted as: "ModId:BlockId" OR
     * "ModId:BlockId:Metadata" The format for each entry in the SPECIAL_PLANKS defines the behaviour of the entries in
     * the SPECIAL_SLABS list. - If the first entry is null or invalid, the entry is skipped. (AKA it's a useless entry,
     * there's no reason to do this) - If the first entry doesn't have Metadata, ALL subtypes of this block will be
     * handled through the special case. The metadata is sourced from the ItemStack defined in the
     * {@link #convertSlabRecipe(ItemStack)} method call. The ItemStack metadata will also be used for the second entry,
     * UNLESS it defines it's own metadata, in which case that will be used instead. - If the first entry has Metadata,
     * ONLY that specific block subtype will be handled through the special case. This Metadata will be copied into the
     * second entry, UNLESS it defines it's own metadata, in which case that will be used instead. Additionaly, if the
     * second entry is null, then creation of the slab recipes will be skipped entirely, even if an OreDict recipe
     * exists. The standard slab recipes (be they from OreDict or not) will still be removed, however.
     *
     * Some examples of Mods which need special handling, as well as the reasons why:
     *
     * Witchery: The mod forgot to add the "standard" Plank -> Slab recipes, so we need to manually assign them. In most
     * cases this should instead be fixed in the original mod, but we all know this ain't happening for Witchery.
     *
     * Amun-Ra: Needs it because the Planks metadatas are completely different from the Slab metadatas. In this case we
     * match each block subtype individually.
     *
     * Et Futurum Requiem: ETF's Slab recipes _are_ OreDict recipes, meaning they receive the same priority as Forge's
     * standard ShapedOreRecipe, and thus may be skipped. We specify them manually here to avoid this.
     */
    private static final String[] SPECIAL_PLANKS = new String[] { "witchery:witchwood",
        "GalacticraftAmunRa:tile.wood1:2", "GalacticraftAmunRa:tile.wood1:3", "etfuturum:wood_planks" };
    private static final String[] SPECIAL_SLABS = new String[] { "witchery:witchwoodslab",
        "GalacticraftAmunRa:tile.woodSlab:1", "GalacticraftAmunRa:tile.woodSlab:0", "etfuturum:wood_slab" };

    private static final HashSet<String> sProcessedPlanks = new HashSet<>();

    public ProcessingPlank() {
        OrePrefixes.plank.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aOreDictName.startsWith("plankWood")) {
            int tPlankMeta = aStack.getItemDamage();

            String tHashPrefix = aModName + ":" + aStack.getUnlocalizedName();

            // Skip already processed planks (accounting for metadata)
            // Fixes duplicate ETF's and ExtraUtilities's slab recipes
            if (!sProcessedPlanks.add(tHashPrefix + ":" + tPlankMeta)) return;

            boolean tIsWildcard = tPlankMeta == OreDictionary.WILDCARD_VALUE;

            // Also skip this plank if it's wildcard equivalent was already processed
            if (!tIsWildcard && sProcessedPlanks.contains(tHashPrefix + ":" + OreDictionary.WILDCARD_VALUE)) return;

            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aStack))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L))
                .duration(10 * TICKS)
                .eut(8)
                .addTo(latheRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(8, aStack))
                .circuit(8)
                .itemOutputs(new ItemStack(Blocks.chest, 1))
                .duration(40 * SECONDS)
                .eut(4)
                .addTo(assemblerRecipes);

            if (aStack.getItem() instanceof MetaGeneratedItem) {
                // https://github.com/GTNewHorizons/GT-New-Horizons-Modpack/issues/19273
                // "plankWood" from GT are also having other recipes in cutters, which causing recipe conflicts.
                // And I don't think people would use this kind of plankWood to make wooden products, so just skipping
                // these recipes to temporary fix this error.
                return;
            }

            // Gregify slab recipes
            if (tIsWildcard) {
                for (byte i = 0; i < 64; i = (byte) (i + 1)) {
                    ItemStack tStack = GTUtility.copyMetaData(i, aStack);
                    if (tStack == null && i >= 16) break;

                    convertSlabRecipe(tStack);
                }
            } else {
                convertSlabRecipe(aStack);
            }
        }
    }

    private void convertSlabRecipe(ItemStack aStack) {

        SpecialSlabConversionResult tSpecialResult = trySpecialSlabConversion(aStack);

        boolean tSkipRecipeCreation = tSpecialResult.isSpecialConversion && tSpecialResult.resultingSlab == null;
        ItemStack tOutput = tSpecialResult.resultingSlab;

        // Get Recipe and Output, add recipe to delayed removal
        if (tOutput == null) {
            // https://github.com/GTNewHorizons/GT-New-Horizons-Modpack/issues/19535
            // Most modded "Vanilla" plank recipes will have at least two conflicting recipes
            // when converting into slabs (assuming they oredict into "plankWood"):
            // - One for the corresponding slab from the mod itself
            // - Another one through Oredict that converts it into Oak Slab
            //
            // We want to use the first recipe, if it's available, so this method is
            // used to give preference to the specific recipe over the Oredict one.
            tOutput = GTModHandler.getRecipeOutputPreferNonOreDict(aStack, aStack, aStack);
        }

        if (tOutput == null || tOutput.stackSize < 3) return;

        GTModHandler.removeRecipeDelayed(aStack, aStack, aStack);

        if (tSkipRecipeCreation) return;

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(GTUtility.copyAmount(tOutput.stackSize / 3, tOutput))
            .fluidInputs(Materials.Water.getFluid(4))
            .duration(2 * 25 * TICKS)
            .eut(4)
            .addTo(cutterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(GTUtility.copyAmount(tOutput.stackSize / 3, tOutput))
            .fluidInputs(GTModHandler.getDistilledWater(3))
            .duration(2 * 25 * TICKS)
            .eut(4)
            .addTo(cutterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(GTUtility.copyAmount(tOutput.stackSize / 3, tOutput))
            .fluidInputs(Materials.Lubricant.getFluid(1))
            .duration(25 * TICKS)
            .eut(4)
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(GTUtility.copyAmount(tOutput.stackSize / 3, tOutput))
            .fluidInputs(Materials.DimensionallyShiftedSuperfluid.getFluid(1))
            .duration(10 * TICKS)
            .eut(4)
            .addTo(cutterRecipes);

        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(tOutput.stackSize / 3, tOutput),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "sP", 'P', aStack });
    }

    private SpecialSlabConversionResult trySpecialSlabConversion(ItemStack aPlankStack) {
        UniqueIdentifier tIdentifier = GameRegistry.findUniqueIdentifierFor(aPlankStack.getItem());
        int tPlankMeta = aPlankStack.getItemDamage();

        for (int i = 0; i < SPECIAL_PLANKS.length; i++) {
            String tPlankUniqueId = SPECIAL_PLANKS[i];

            if (tPlankUniqueId == null) continue;

            String[] tPlanksParts = tPlankUniqueId.split(":");

            if (tPlanksParts.length < 2) continue;

            if (!tPlanksParts[0].equals(tIdentifier.modId)) continue;
            if (!tPlanksParts[1].equals(tIdentifier.name)) continue;
            if (tPlanksParts.length > 2 && !tPlanksParts[2].equals("" + tPlankMeta)) continue;

            if (i >= SPECIAL_SLABS.length) continue;

            String tSlabUniqueId = SPECIAL_SLABS[i];

            if (tSlabUniqueId == null) {
                // Recipe is a Special case, but is explicitly told not to return anything.
                return new SpecialSlabConversionResult(true, null);
            }

            String[] tSlabParts = tSlabUniqueId.split(":");

            if (tSlabParts.length < 2) continue;

            int tSlabMeta = tPlankMeta;
            try {
                if (tSlabParts.length > 2) tSlabMeta = Integer.parseInt(tSlabParts[2]);
            } catch (NumberFormatException ignored) {
                continue;
            }

            return new SpecialSlabConversionResult(
                true,
                GTModHandler.getModItem(tSlabParts[0], tSlabParts[1], 6, tSlabMeta));
        }

        return new SpecialSlabConversionResult(false, null);
    }

    private class SpecialSlabConversionResult {

        private boolean isSpecialConversion;
        private ItemStack resultingSlab;

        private SpecialSlabConversionResult(boolean isSpecialConversion, ItemStack resultingSlab) {
            this.isSpecialConversion = isSpecialConversion;
            this.resultingSlab = resultingSlab;
        }
    }
}
