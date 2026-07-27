package gregtech.loaders.shapeconsumers;

import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.MU;

/// Shared dispatch glue for the `Consumer*` classes in this package: each targets one legacy `OrePrefixes`/
/// [Shape] pair and delegates to the same [IOreRecipeRegistrator] its `gregtech.loaders.oreprocessing.Processing*`
/// counterpart already implements, resolving the canonical stack through [MaterialLibAPI] instead of an
/// oredict event.
///
/// The `Processing*` class itself is untouched: it keeps running through the legacy oredict-event path
/// (`gregtech.common.OreDictEventContainer`) for foreign mods' items of the same prefix, and only stops seeing
/// MaterialLib's own items once that path is taught to skip them.
final class ShapeConsumerSupport {

    private ShapeConsumerSupport() {}

    /// Registers `registrator` to run once per material generating `shape`, at MaterialLib's postInit, passing
    /// the MaterialLib material straight through to `registrator`'s
    /// [IOreRecipeRegistrator#registerOre(OrePrefixes, com.ruling_0.materiallib.api.Material, String, String,
    /// ItemStack)] entry, which resolves any legacy [Materials] counterpart itself. A shape/material pair with
    /// no resolvable stack is skipped rather than failing the whole dispatch -- see [ShapeConsumerSupport] for
    /// why this should not happen for a `"gregtech"`-owned shape in practice.
    ///
    /// Dispatch is postInit, not init: several `Processing*` bodies transitively class-load
    /// `gregtech.api.util.GTRecipeConstants`, whose static initializer reads bartworks `WerkstoffLoader` fluid
    /// data (`BlastFurnaceGasStat`) that BartWorks' own `BridgeMaterialsLoader` only populates during
    /// BartWorks' init -- since FML runs every mod's init before any mod's postInit, dispatching from
    /// MaterialLib's init could race BartWorks' init depending on mod processing order (observed as an
    /// `ExceptionInInitializerError` from the bartworks material's fluid lookup during a real boot). Postponing to
    /// postInit
    /// (still before GregTech's own postInit, which `required-after:materiallib` orders after MaterialLib's)
    /// avoids the race unconditionally.
    ///
    /// `registrator` is a [Supplier], not a resolved instance: `Consumer*#register` runs during
    /// `MaterialRegistrationEvent`, which MaterialLib fires from its own preInit -- before
    /// `gregtech.loaders.preload.LoaderOreProcessing` (called from GregTech's preInit, which starts only after
    /// MaterialLib's finishes) has constructed the `Processing*` singletons this resolves. Reading the
    /// `INSTANCE` field eagerly at registration time would always see `null`; the supplier defers that read to
    /// dispatch time, well after `LoaderOreProcessing` has run.
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
            if (stack == null) return;
            registrator.get()
                .registerOre(prefix, material, prefix.getName() + MU.internalName(material), "materiallib", stack);
        });
    }
}
