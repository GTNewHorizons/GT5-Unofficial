package gregtech.loaders.oreprocessing;

import java.util.IdentityHashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;

/// Recipes for items other mods register in the ore dictionary, and for MaterialLib items registered under a prefix
/// alias. MaterialLib parts get theirs from [ShapeRecipeTable]; this covers what has no MaterialLib pair: crops, logs,
/// planks, slabs, `item*` and `plateAlloy*` names, crystals, foreign blocks, ores, gems, nuggets, plates, sticks and
/// crushed ores, and the stack-size clamp of every foreign block.
///
/// [gregtech.common.oredict.OreDictRegistration] decides which registrations reach [#run].
public final class ForeignPartRecipes {

    private static final Map<OrePrefixes, IOreRecipeRegistrator> REGISTRATORS = new IdentityHashMap<>();

    static {
        register(ProcessingBlock.INSTANCE, OrePrefixes.block);
        register(ProcessingCompressed.INSTANCE, OrePrefixes.compressed);
        register(ProcessingCrop.INSTANCE, OrePrefixes.crop);
        register(ProcessingCrystallized.INSTANCE, OrePrefixes.crystal, OrePrefixes.crystalline);
        register(
            ProcessingDirty.INSTANCE,
            OrePrefixes.clump,
            OrePrefixes.shard,
            OrePrefixes.crushed,
            OrePrefixes.dirtyGravel);
        register(
            ProcessingGem.INSTANCE,
            OrePrefixes.gem,
            OrePrefixes.gemChipped,
            OrePrefixes.gemExquisite,
            OrePrefixes.gemFlawed,
            OrePrefixes.gemFlawless);
        register(ProcessingItem.INSTANCE, OrePrefixes.item);
        register(ProcessingLog.INSTANCE, OrePrefixes.log);
        register(ProcessingNugget.INSTANCE, OrePrefixes.nugget);
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (!prefix.getName()
                .startsWith("ore")) continue;
            if (prefix == OrePrefixes.orePoor || prefix == OrePrefixes.oreSmall
                || prefix == OrePrefixes.oreRich
                || prefix == OrePrefixes.oreNormal) continue;
            register(ProcessingOre.INSTANCE, prefix);
        }
        register(ProcessingPlank.INSTANCE, OrePrefixes.plank);
        register(
            ProcessingPlate.INSTANCE,
            OrePrefixes.plate,
            OrePrefixes.plateDouble,
            OrePrefixes.plateTriple,
            OrePrefixes.plateQuadruple,
            OrePrefixes.plateQuintuple,
            OrePrefixes.plateDense,
            OrePrefixes.plateSuperdense,
            OrePrefixes.plateAlloy,
            OrePrefixes.itemCasing);
        register(ProcessingSlab.INSTANCE, OrePrefixes.slab);
        register(ProcessingStick.INSTANCE, OrePrefixes.stick);
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

    private static void register(IOreRecipeRegistrator registrator, OrePrefixes... prefixes) {
        for (OrePrefixes prefix : prefixes) {
            REGISTRATORS.put(prefix, registrator);
        }
    }

    /// Generates the recipes for one ore-dictionary registration of `stack` under `prefix`. Does nothing for a
    /// [GTMaterialFlag#NO_RECIPES] material or an invalid stack, or under a material-based prefix whose name named
    /// no material.
    public static void run(OrePrefixes prefix, Material material, String oreDictName, @Nullable String modName,
        ItemStack stack) {
        if (material == null || MaterialUtils.hasFlag(material, GTMaterialFlag.NO_RECIPES)) return;
        if (material == Materials.NULL && !prefix.isSelfReferencing() && prefix.isMaterialBased()) return;
        if (!GTUtility.isStackValid(stack)) return;

        clampStackSize(prefix, stack);
        IOreRecipeRegistrator registrator = REGISTRATORS.get(prefix);
        if (registrator != null) {
            registrator.registerOre(prefix, material, oreDictName, modName, GTUtility.copyAmount(1, stack));
        }
    }
}
