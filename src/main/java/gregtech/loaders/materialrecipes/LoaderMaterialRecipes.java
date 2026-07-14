package gregtech.loaders.materialrecipes;

/// Registers every canonical marker-driven material recipe generator in this package, dispatched from `GTMod`'s
/// postInit.
///
/// PostInit, not init or preInit, because these generators read composition and marker data off materials
/// through [gregtech.api.material.MU], which resolves items via `MaterialLibAPI.getStack` -- only valid once
/// MaterialLib's shapes have resolved -- and some formulas replicate bartworks facade behavior that is only
/// populated once every mod's own init phase has run (the same postInit-not-init timing
/// [gregtech.loaders.shapeconsumers.ShapeConsumerSupport] documents for the shape-driven generators). Unlike
/// that package, there is no per-shape MaterialLib consumer hook to dispatch through here (`MaterialLibAPI`
/// exposes shape-keyed consumer registration only), so `GTMod` calls this directly from its own postInit --
/// itself a postInit-phase handler, which is all the timing guarantee requires.
public final class LoaderMaterialRecipes {

    private LoaderMaterialRecipes() {}

    public static void run() {
        LoaderSifterRecipes.run();
        LoaderMixerRecipes.run();
    }
}
