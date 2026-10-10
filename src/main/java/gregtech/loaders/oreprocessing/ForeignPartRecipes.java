package gregtech.loaders.oreprocessing;

import java.util.IdentityHashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;
import gregtech.loaders.oreprocessing.ForeignPartRecipeTable.ForeignPart;

/// Recipes for items GregTech has no MaterialLib pair for. MaterialLib parts get theirs from [ShapeRecipeTable].
///
/// Logs, planks, slabs and crops follow a rule: every ore-dictionary registration under those prefixes gets its
/// recipes from [#run], whichever mod registers it. Every other foreign item is listed in [ForeignPartRecipeTable] and
/// processed once by [#registerTable].
///
/// [gregtech.common.oredict.OreDictRegistration] decides which registrations reach [#run].
public final class ForeignPartRecipes {

    private static final Map<OrePrefixes, ForeignPartRule> RULES = new IdentityHashMap<>();
    private static final Map<OrePrefixes, PartRecipeGenerator> TABLE_GENERATORS = new IdentityHashMap<>();

    static {
        RULES.put(OrePrefixes.crop, ProcessingCrop.INSTANCE);
        RULES.put(OrePrefixes.log, ProcessingLog.INSTANCE);
        RULES.put(OrePrefixes.plank, ProcessingPlank.INSTANCE);
        RULES.put(OrePrefixes.slab, ProcessingSlab.INSTANCE);

        register(TABLE_GENERATORS, ProcessingCompressed.INSTANCE, OrePrefixes.compressed);
        register(TABLE_GENERATORS, ProcessingCrystallized.INSTANCE, OrePrefixes.crystal);
        register(TABLE_GENERATORS, ProcessingDirty.INSTANCE, OrePrefixes.shard);
        register(TABLE_GENERATORS, ProcessingItem.INSTANCE, OrePrefixes.item);
        register(TABLE_GENERATORS, ProcessingNugget.INSTANCE, OrePrefixes.nugget);
        register(TABLE_GENERATORS, ProcessingOre.INSTANCE, OrePrefixes.ore, OrePrefixes.oreDeepslate);
        register(TABLE_GENERATORS, ProcessingPlate.INSTANCE, OrePrefixes.plate, OrePrefixes.plateDense);
    }

    private ForeignPartRecipes() {}

    /// Lowers the stack limit of `stack`'s item to `prefix`'s default stack size, when the item is a block. The limit
    /// belongs to the item, so it applies to every stack of it.
    static void clampStackSize(OrePrefixes prefix, ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemBlock && prefix.getDefaultStackSize() < item.getItemStackLimit(stack)) {
            item.setMaxStackSize(prefix.getDefaultStackSize());
        }
    }

    private static void register(Map<OrePrefixes, PartRecipeGenerator> generators, PartRecipeGenerator generator,
        OrePrefixes... prefixes) {
        for (OrePrefixes prefix : prefixes) {
            generators.put(prefix, generator);
        }
    }

    /// Clamps the stack size of one ore-dictionary registration of `stack` under `prefix`, and generates its recipes
    /// when `prefix` is a log, plank, slab or crop. Does nothing for a [GTMaterialFlag#NO_RECIPES] material or an
    /// invalid stack, or under a material-based prefix whose name named no material.
    public static void run(OrePrefixes prefix, Material material, String oreDictName, ItemStack stack) {
        if (material == null || MaterialUtils.hasFlag(material, GTMaterialFlag.NO_RECIPES)) return;
        if (material == Materials.NULL && !prefix.isSelfReferencing() && prefix.isMaterialBased()) return;
        if (!GTUtility.isStackValid(stack)) return;

        clampStackSize(prefix, stack);
        ForeignPartRule rule = RULES.get(prefix);
        if (rule != null) {
            rule.generate(prefix, material, oreDictName, GTUtility.copyAmount(1, stack));
        }
    }

    /// Generates the recipes of every [ForeignPartRecipeTable] row whose item exists and of the alloy plates (see
    /// [AlloyPlateRecipes]), and removes the foreign storage-block recipes [ProcessingBlock#removeForeignBlockRecipes]
    /// replaces.
    public static void registerTable() {
        for (ForeignPart part : ForeignPartRecipeTable.parts()) {
            ItemStack stack = part.stack()
                .get();
            if (!GTUtility.isStackValid(stack)) continue;
            TABLE_GENERATORS.get(part.prefix())
                .generate(part.prefix(), part.material(), null, stack);
        }
        AlloyPlateRecipes.register();
        ProcessingBlock.removeForeignBlockRecipes();
    }
}
