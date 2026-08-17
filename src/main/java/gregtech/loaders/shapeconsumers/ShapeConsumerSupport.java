package gregtech.loaders.shapeconsumers;

import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.MaterialUtils;

/// Shared dispatch glue for the `Consumer*` classes in this package: each targets one legacy `OrePrefixes`/
/// [Shape] pair and delegates to the same [IOreRecipeRegistrator] its `gregtech.loaders.oreprocessing.Processing*`
/// counterpart already implements, resolving the canonical stack through [MaterialLibAPI] instead of an
/// oredict event.
///
/// Each `Processing*` class also runs through the oredict-event path (`gregtech.common.OreDictEventContainer`)
/// for foreign mods' items of the same prefix. A prefix with no MaterialLib shape gets no consumer here and
/// keeps serving foreign mods through that path alone.
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

    /// [#delegate(Shape, OrePrefixes, Supplier)] restricted to materials passing `filter` -- for a shape
    /// whose membership is wider than the set of materials the legacy oredict path ever dispatched for
    /// (see `ConsumerWire`'s superconductor markers).
    static void delegate(Shape shape, OrePrefixes prefix, Predicate<Material> filter,
        Supplier<IOreRecipeRegistrator> registrator) {
        if (shape == null) return;
        MaterialLibAPI.registerPostInitShapeConsumer("gregtech", shape, (s, material) -> {
            if (!filter.test(material)) return;
            ItemStack stack = MaterialLibAPI.getStack(material, s, 1);
            registrator.get()
                .registerOre(
                    prefix,
                    material,
                    prefix.getName() + MaterialUtils.internalName(material),
                    "materiallib",
                    stack);
        });
    }
}
