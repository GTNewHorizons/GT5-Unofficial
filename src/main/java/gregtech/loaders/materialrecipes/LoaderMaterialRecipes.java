package gregtech.loaders.materialrecipes;

/// Registers every canonical marker-driven material recipe generator in this package, dispatched from `GTMod`'s
/// postInit.
///
/// PostInit, not init or preInit: these generators resolve their items through `MaterialLibAPI.getStack`,
/// which is only valid once MaterialLib's shapes have resolved -- the same timing
/// [gregtech.loaders.shapeconsumers.ShapeConsumerSupport] documents for the shape-driven generators. There is
/// no per-shape MaterialLib consumer hook to dispatch through here, so `GTMod` calls this directly.
public final class LoaderMaterialRecipes {

    private LoaderMaterialRecipes() {}

    public static void run() {
        LoaderSifterRecipes.run();
        LoaderMixerRecipes.run();
        LoaderChemicalRecipes.run();
    }
}
