package gregtech.api.material;

/// Boolean flags a bartworks `Werkstoff` carried that have no per-material `GTMaterialProperties` key of their
/// own: the `Werkstoff.Stats` quality booleans and the `GenerationFeatures` extra-recipe markers. Stored
/// inside [GTMaterialProperties#WERKSTOFF_FLAGS].
public enum GTWerkstoffFlag {
    /// `Stats#isSublimation`.
    SUBLIMATION,
    /// `Stats#isElektrolysis` -- an electrolyzer decomposition recipe is generated from the contents.
    ELECTROLYSIS,
    /// `Stats#isCentrifuge` -- a centrifuge decomposition recipe is generated from the contents.
    CENTRIFUGE,
    /// `Stats#isGas` -- the material's base fluid is a gas.
    GAS,
    /// `GenerationFeatures#enforceUnification`.
    ENFORCE_UNIFICATION,
    /// `GenerationFeatures` ChemicalSynthesis extra recipe.
    CHEMICAL_SYNTHESIS,
    /// `GenerationFeatures` MetalCraftingSolidification extra recipe.
    METAL_CRAFTING_SOLIDIFICATION,
    /// `GenerationFeatures` MetalSolidification extra recipe.
    METAL_SOLIDIFICATION,
    /// `GenerationFeatures` Mixing extra recipe.
    MIXING,
    /// `GenerationFeatures` Sifting extra recipe.
    SIFTING,
    /// The chemical-formula tooltip is a localization key rather than literal text
    /// (`Werkstoff#isFormulaNeededLocalized`).
    LOCALIZED_FORMULA
}
