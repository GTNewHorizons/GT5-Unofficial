package gregtech.loaders.shapeconsumers;

import static gregtech.GTLoggers.GT_FML_LOGGER;

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

/// Shared dispatch glue for the `Consumer*` classes in this package: each targets one [Shape] and delegates to
/// the same [IOreRecipeRegistrator] its `gregtech.loaders.oreprocessing.Processing*` counterpart already
/// implements, resolving stacks through [MaterialLibAPI] instead of an oredict event. Most shapes map onto a
/// single legacy `OrePrefixes`; the ore shape maps onto one prefix per stone variant, through
/// [#delegateOreVariants].
///
/// A `Processing*` class that also generates recipes for other mods' items attaches to its prefix through
/// [OrePrefixes#add], which the ore-dictionary registration path dispatches. A prefix with no MaterialLib shape
/// gets no consumer here and serves those items through that path alone.
///
/// Dispatch is postInit, not init: several `Processing*` bodies transitively class-load
/// [gregtech.api.util.GTRecipeConstants], whose static initializer reads material data that
/// [gregtech.api.enums.materials.Materials] populates from `MaterialRegistrationEvent`. Dispatching at init
/// can reach that initializer before the registry resolves -- the class-init trap
/// [gregtech.api.enums.materials.OreShapes]'s javadoc describes. postInit still precedes GregTech's own
/// postInit, since `required-after:materiallib` orders MaterialLib's lifecycle first.
///
/// Registrators are passed as [Supplier]s, not resolved instances: `Consumer*#register` runs during
/// `MaterialRegistrationEvent`, before `gregtech.loaders.preload.LoaderOreProcessing` has constructed the
/// `Processing*` singletons.
final class ShapeConsumerSupport {

    private ShapeConsumerSupport() {}

    /// Registers `registrator` to run once per material generating `shape`, at MaterialLib's postInit, passing
    /// the MaterialLib material straight through to `registrator`'s
    /// [IOreRecipeRegistrator#registerOre(OrePrefixes, com.ruling_0.materiallib.api.Material, String, String,
    /// ItemStack)] entry. A shape/material pair with no resolvable stack is skipped.
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

    /// [#delegate(Shape, OrePrefixes, Supplier)] for the variant-carrying ore shape: a GregTech ore material
    /// ([GTOreAdapter#isGtOreMaterial]) dispatches once per [StoneType] it generates a block on, under that
    /// stone's own [StoneType#getPrefix] and that stone's block. gtPlusPlus' and
    /// bartworks' materials, which own no stone variants, dispatch once under `fallbackPrefix` with the
    /// shape's canonical stack.
    ///
    /// Every variant of one material shares a [GTRecipeBuilder#withVariantGroup] tag, so NEI shows the fan-out
    /// as a single cycling entry per action instead of one entry per stone.
    ///
    /// [StoneType] and [GTOreAdapter] are touched only from inside the postInit callback:
    /// see the class-init trap [gregtech.api.enums.materials.OreShapes]'s javadoc describes.
    static void delegateOreVariants(Shape shape, OrePrefixes fallbackPrefix,
        Supplier<IOreRecipeRegistrator> registrator) {
        if (shape == null) return;
        MaterialLibAPI.registerPostInitShapeConsumer("gregtech", shape, (s, material) -> {
            if (!GTOreAdapter.isGtOreMaterial(material)) {
                dispatch(registrator, fallbackPrefix, material, MaterialLibAPI.getStack(material, s, 1));
                return;
            }

            if (MaterialUtils.oldSubId(material) < 0) {
                GT_FML_LOGGER
                    .warn("GregTech ore material {} carries no legacy sub id", MaterialUtils.internalName(material));
            }

            String group = fallbackPrefix.getName() + ":" + MaterialUtils.internalName(material);

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
