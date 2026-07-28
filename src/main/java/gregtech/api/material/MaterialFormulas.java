package gregtech.api.material;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.util.GTUtility;

/// Resolves the chemical-formula display string for a MaterialLib [Material] from
/// [GTMaterialProperties#FORMULA] (see its javadoc for which legacy system sourced each value), translating
/// through the [#formulaKey] localization key when [GTMaterialProperties#FORMULA_LOCALIZED] is set.
///
/// [#forSearch] feeds [gregtech.nei.searchprovider.ChemicalFormulaFilter] and returns the stored string --
/// unsanitized, including `"?"`/`"??"` placeholders; the filter applies its own validity check and
/// sanitization. [#forTooltip] returns the ready-to-display line (or null for none), suppressing the
/// exact-`"?"` placeholder.
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

    /// Builds the `"Material." + name.toLowerCase() + ".ChemicalFormula"` localization key the lang files use
    /// for a material's formula string, where `name` is [GTMaterialProperties#LEGACY_NAME] when present, else
    /// the MaterialLib registration name. A bartworks-origin material's own internal name (lowercased) resolves
    /// to the same key format, so one scheme serves every localized-formula material regardless of origin.
    private static String formulaKey(Material ml) {
        String legacyName = ml.getProperty(GTMaterialProperties.LEGACY_NAME);
        return "Material." + (legacyName != null ? legacyName : ml.getName()).toLowerCase() + ".ChemicalFormula";
    }
}
