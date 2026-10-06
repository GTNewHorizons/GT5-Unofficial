package gregtech.loaders.materialrecipes;

/// Runs the property-driven material recipe generators in this package. Called from `GTMod`'s postInit, since the
/// generators need MaterialLib's shapes to be resolved.
public final class LoaderMaterialRecipes {

    private LoaderMaterialRecipes() {}

    public static void run() {
        LoaderMixerRecipes.run();
        LoaderChemicalRecipes.run();
    }
}
