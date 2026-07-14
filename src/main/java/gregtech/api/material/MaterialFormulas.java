package gregtech.api.material;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.util.GTUtility;

/// Resolves the chemical-formula display string for a MaterialLib [Material] from
/// [GTMaterialProperties#FORMULA] (see its javadoc for which legacy system sourced each value), translating
/// through the legacy `Materials` localization key when [GTMaterialProperties#FORMULA_LOCALIZED] is set.
///
/// [#forSearch] feeds [gregtech.nei.searchprovider.ChemicalFormulaFilter] and returns the stored string --
/// unsanitized, including `"?"`/`"??"` placeholders; the filter applies its own validity check and
/// sanitization. [#forTooltip] returns the ready-to-display line (or null for none), suppressing the
/// exact-`"?"` placeholder the way the legacy `Materials#addTooltips` did.
public final class MaterialFormulas {

    private MaterialFormulas() {}

    /// The raw formula string for NEI search, or null when the material carries none.
    public static @Nullable String forSearch(@Nullable Material ml) {
        if (ml == null) return null;

        String formula = ml.getProperty(GTMaterialProperties.FORMULA);
        if (formula == null) return null;
        return Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.FORMULA_LOCALIZED))
            ? StatCollector.translateToLocal(formulaKey(ml))
            : formula;
    }

    /// The formula tooltip line to display, or null when the legacy renderer showed none.
    public static @Nullable String forTooltip(@Nullable Material ml) {
        String formula = forSearch(ml);
        return GTUtility.isStringValid(formula) && !"?".equals(formula) ? formula : null;
    }

    /// Mirrors `IOreMaterial#getLocalizedNameKey` (`"Material." + getInternalName().toLowerCase()`) for the
    /// reconstructed legacy `Materials` instance -- `getInternalName()` there is `mName`, i.e.
    /// [GTMaterialProperties#LEGACY_NAME] when present, else the MaterialLib registration name. The
    /// reconstructed `Werkstoff`'s own key (`getVarName().toLowerCase()`) resolves identically for every
    /// localized-formula werkstoff, so one scheme serves both facades.
    private static String formulaKey(Material ml) {
        String legacyName = ml.getProperty(GTMaterialProperties.LEGACY_NAME);
        return "Material." + (legacyName != null ? legacyName : ml.getName()).toLowerCase() + ".ChemicalFormula";
    }
}
