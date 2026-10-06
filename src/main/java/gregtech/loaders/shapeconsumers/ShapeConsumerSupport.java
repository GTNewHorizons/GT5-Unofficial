package gregtech.loaders.shapeconsumers;

import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.common.oredict.OreDictRegistrationHandler;
import gregtech.common.ores.GTOreAdapter;

/// Shared glue for the `Consumer*` classes in this package. Each consumer targets a [Shape] and runs the matching
/// `gregtech.loaders.oreprocessing.Processing*` [IOreRecipeRegistrator] once per material, with the stack from
/// [MaterialLibAPI].
///
/// A `Processing*` class that also handles other mods' items attaches to its prefix through [OrePrefixes#add], and
/// the oredict path dispatches those. A prefix with no MaterialLib shape gets no consumer and is served by that path
/// alone.
///
/// Dispatch runs at MaterialLib's postInit, after every mod's init and before GregTech's own postInit.
///
/// Registrators are passed as [Supplier]s because consumers register before
/// `gregtech.loaders.preload.LoaderOreProcessing` constructs the `Processing*` singletons.
final class ShapeConsumerSupport {

    private ShapeConsumerSupport() {}

    /// Registers `registrator` to run under `prefix` once per material generating `shape`, at MaterialLib's
    /// postInit. Materials flagged [GTMaterialFlag#NO_RECIPES], prefixes the material ignores and ignored oredict
    /// names are skipped.
    static void delegate(Shape shape, OrePrefixes prefix, Supplier<IOreRecipeRegistrator> registrator) {
        delegate(shape, prefix, material -> true, registrator);
    }

    /// [#delegate(Shape, OrePrefixes, Supplier)] restricted to materials passing `filter`, which runs before the
    /// material's stack is resolved.
    static void delegate(Shape shape, OrePrefixes prefix, Predicate<Material> filter,
        Supplier<IOreRecipeRegistrator> registrator) {
        if (shape == null) return;
        MaterialLibAPI.registerPostInitShapeConsumer("gregtech", shape, (s, material) -> {
            if (!filter.test(material)) return;
            dispatch(registrator, prefix, material, MaterialLibAPI.getStack(material, s, 1));
        });
    }

    /// [#delegate(Shape, OrePrefixes, Supplier)] for the ore shape. Each material dispatches once per [StoneType]
    /// it has an ore block on, under that stone's [StoneType#getPrefix].
    ///
    /// All variants of one material share a [GTRecipeBuilder#withVariantGroup] tag named by `groupPrefix`, so NEI
    /// shows one cycling entry per recipe instead of one per stone.
    ///
    /// [StoneType] and [GTOreAdapter] must only be touched inside the callback; see
    /// [gregtech.api.enums.materials.OreShapes].
    static void delegateOreVariants(Shape shape, OrePrefixes groupPrefix, Supplier<IOreRecipeRegistrator> registrator) {
        if (shape == null) return;
        MaterialLibAPI.registerPostInitShapeConsumer("gregtech", shape, (s, material) -> {
            String group = groupPrefix.getName() + ":" + MaterialUtils.internalName(material);

            for (StoneType stoneType : StoneType.VALUES) {
                ItemStack stack = GTOreAdapter.INSTANCE.getVariantStack(material, stoneType, false);
                if (stack == null) continue;
                GTRecipeBuilder
                    .withVariantGroup(group, () -> dispatch(registrator, stoneType.getPrefix(), material, stack));
            }
        });
    }

    private static void dispatch(Supplier<IOreRecipeRegistrator> registrator, OrePrefixes prefix, Material material,
        ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_RECIPES) || prefix.isIgnored(material)) return;
        String oreName = prefix.getName() + MaterialUtils.internalName(material);
        if (OreDictRegistrationHandler.isIgnoredName(oreName)) return;
        registrator.get()
            .registerOre(prefix, material, oreName, "materiallib", stack);
    }
}
