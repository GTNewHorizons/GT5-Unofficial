package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.util.HashSet;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.github.bsideup.jabel.Desugar;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

@SuppressWarnings("ControlFlowStatementWithoutBraces")
public class ProcessingPlank implements gregtech.api.interfaces.IOreRecipeRegistrator {

    /**
     * Sometimes automatically searching for recipes is not enough to ensure that the corresponding slab is correctly
     * assigned. These Special lists exist to provide a direct mapping from plank to slab in such cases.
     * <p>
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
     * <p>
     * Some examples of Mods which need special handling, as well as the reasons why:
     * <p>
     * Witchery: The mod forgot to add the "standard" Plank -> Slab recipes, so we need to manually assign them. In most
     * cases this should instead be fixed in the original mod, but we all know this ain't happening for Witchery.
     * <p>
     * Amun-Ra: Needs it because the Planks metadatas are completely different from the Slab metadatas. In this case we
     * match each block subtype individually.
     * <p>
     * Et Futurum Requiem: ETF's Slab recipes _are_ OreDict recipes, meaning they receive the same priority as Forge's
     * standard ShapedOreRecipe, and thus may be skipped. We specify them manually here to avoid this.
     */
    private static final String[] SPECIAL_PLANKS = new String[] { "witchery:witchwood",
        "GalacticraftAmunRa:tile.wood1:2", "GalacticraftAmunRa:tile.wood1:3", "etfuturum:wood_planks" };
    private static final String[] SPECIAL_SLABS = new String[] { "witchery:witchwoodslab",
        "GalacticraftAmunRa:tile.woodSlab:1", "GalacticraftAmunRa:tile.woodSlab:0", "etfuturum:wood_slab" };

    private static final HashSet<String> sProcessedPlanks = new HashSet<>();
    private static final HashSet<Item> sGroupedOakSlabItems = new HashSet<>();

    public ProcessingPlank() {
        OrePrefixes.plank.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (!oreDictName.startsWith("plankWood")) {
            return;
        }

        int tPlankMeta = stack.getItemDamage();
        UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        String tHashPrefix = null;
        if (uid != null) {
            tHashPrefix = uid.modId + ":" + uid.name;
        }

        if (!sProcessedPlanks.add(tHashPrefix + ":" + tPlankMeta)) {
            return;
        }

        boolean tIsWildcard = tPlankMeta == OreDictionary.WILDCARD_VALUE;

        if (!tIsWildcard && sProcessedPlanks.contains(tHashPrefix + ":" + OreDictionary.WILDCARD_VALUE)) {
            return;
        }

        if (stack.getItem() instanceof MetaGeneratedItem) {
            // https://github.com/GTNewHorizons/GT-New-Horizons-Modpack/issues/19273
            // GT's own plankWood items conflict with existing cutter recipes, skip them.
            return;
        }

        int metaCount = 64;
        if (stack.getItem() instanceof ItemMultiTexture imt) {
            metaCount = imt.field_150942_c.length;
        }

        if (tIsWildcard) {
            processWildcardPlank(stack, tHashPrefix, metaCount);
        } else {
            processSinglePlank(stack);
        }
    }

    private void processWildcardPlank(ItemStack stack, String tHashPrefix, int metaCount) {
        boolean anyOwnSlabRecipe = false;
        boolean anyOakSlabFallback = false;

        for (byte i = 0; i < metaCount; i = (byte) (i + 1)) {
            ItemStack tStack = GTUtility.copyMetaData(i, stack);
            if ((tStack == null) && (i >= 16)) break;
            if (!sProcessedPlanks.add(tHashPrefix + ":" + i)) continue;

            switch (convertSlabRecipe(tStack)) {
                case CREATED -> anyOwnSlabRecipe = true;
                case OAK_SLAB_FALLBACK -> anyOakSlabFallback = true;
                default -> {}
            }
        }

        if (anyOakSlabFallback && !anyOwnSlabRecipe && sGroupedOakSlabItems.add(stack.getItem())) {
            createGroupedOakSlabRecipe(stack);
        }
    }

    private void processSinglePlank(ItemStack stack) {
        var result = convertSlabRecipe(stack);
        if (result == SlabRecipeResult.OAK_SLAB_FALLBACK && sGroupedOakSlabItems.add(stack.getItem())) {
            createGroupedOakSlabRecipe(stack);
        }
    }

    private SlabRecipeResult convertSlabRecipe(ItemStack stack) {
        SpecialSlabConversionResult tSpecialResult = trySpecialSlabConversion(stack);

        boolean tSkipRecipeCreation = tSpecialResult.isSpecialConversion && tSpecialResult.resultingSlab == null;
        ItemStack tOutput = tSpecialResult.resultingSlab;

        if (tOutput == null) {
            // https://github.com/GTNewHorizons/GT-New-Horizons-Modpack/issues/19535
            // Prefer the mod's own slab recipe over the oredict "plankWood -> Oak Slab" fallback.
            tOutput = GTModHandler.getRecipeOutputPreferNonOreDict(stack, stack, stack);
        }

        if (tOutput == null || tOutput.stackSize < 3) {
            return SlabRecipeResult.SKIPPED;
        }

        if (isGenericOakSlabFallback(stack, tOutput)) {
            return SlabRecipeResult.OAK_SLAB_FALLBACK;
        }

        GTModHandler.removeRecipeDelayed(stack, stack, stack);
        if (tSkipRecipeCreation) {
            return SlabRecipeResult.SKIPPED;
        }

        addSlabRecipes(stack, GTUtility.copyAmount(tOutput.stackSize / 3, tOutput));
        return SlabRecipeResult.CREATED;
    }

    private static boolean isGenericOakSlabFallback(ItemStack plank, ItemStack slab) {
        if (slab == null) {
            return false;
        }

        boolean isOakSlab = slab.getItem() == Item.getItemFromBlock(Blocks.wooden_slab) && slab.getItemDamage() == 0;
        boolean isOakPlank = plank.getItem() == Item.getItemFromBlock(Blocks.planks) && plank.getItemDamage() == 0;
        return isOakSlab && !isOakPlank;
    }

    private void createGroupedOakSlabRecipe(ItemStack stack) {
        ensureOakPlankRecipesFirst();
        ItemStack wildcardInput = new ItemStack(stack.getItem(), 1, OreDictionary.WILDCARD_VALUE);
        ItemStack oakSlab = new ItemStack(Blocks.wooden_slab, 2, 0);
        addSlabRecipes(wildcardInput, oakSlab);
    }

    private void ensureOakPlankRecipesFirst() {
        if (!sProcessedPlanks.add("minecraft:planks:0")) {
            return;
        }
        addSlabRecipes(new ItemStack(Blocks.planks, 1, 0), new ItemStack(Blocks.wooden_slab, 2, 0));
    }

    private void addSlabRecipes(ItemStack plankInput, ItemStack slabOutput) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, plankInput))
            .itemOutputs(slabOutput)
            .fluidInputs(MU.fluid(Materials2Materials.Water, 4))
            .duration(2 * 25 * TICKS)
            .eut(4)
            .addTo(cutterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, plankInput))
            .itemOutputs(slabOutput)
            .fluidInputs(GTModHandler.getDistilledWater(3))
            .duration(2 * 25 * TICKS)
            .eut(4)
            .addTo(cutterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, plankInput))
            .itemOutputs(slabOutput)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (1)))
            .duration(25 * TICKS)
            .eut(4)
            .addTo(cutterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, plankInput))
            .itemOutputs(slabOutput)
            .fluidInputs(MU.fluid(Materials2Materials.dimensionallyshiftedsuperfluid, 1))
            .duration(10 * TICKS)
            .eut(4)
            .addTo(cutterRecipes);

        GTModHandler.addCraftingRecipe(
            GTUtility.copyOrNull(slabOutput),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "sP", 'P', plankInput });
    }

    private SpecialSlabConversionResult trySpecialSlabConversion(ItemStack plankStack) {
        UniqueIdentifier tIdentifier = GameRegistry.findUniqueIdentifierFor(plankStack.getItem());
        int tPlankMeta = plankStack.getItemDamage();

        for (int i = 0; i < SPECIAL_PLANKS.length; i++) {
            String tPlankUniqueId = SPECIAL_PLANKS[i];

            String[] tPlanksParts = tPlankUniqueId.split(":");

            if (tPlanksParts.length < 2) continue;

            if (tIdentifier != null && !tPlanksParts[0].equals(tIdentifier.modId)) {
                continue;
            }
            if (tIdentifier != null && !tPlanksParts[1].equals(tIdentifier.name)) {
                continue;
            }

            if (tPlanksParts.length > 2 && !tPlanksParts[2].equals("" + tPlankMeta)) {
                continue;
            }

            String tSlabUniqueId = SPECIAL_SLABS[i];

            String[] tSlabParts = tSlabUniqueId.split(":");

            if (tSlabParts.length < 2) {
                continue;
            }

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

    private enum SlabRecipeResult {
        CREATED,
        OAK_SLAB_FALLBACK,
        SKIPPED
    }

    @Desugar
    private record SpecialSlabConversionResult(boolean isSpecialConversion, ItemStack resultingSlab) {}
}
