package gregtech.api.material;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;

/// Resolves the legacy chemical-formula display string for a MaterialLib [Material] from declaration data
/// alone, reading whichever of [GTMaterialProperties#WERKSTOFF], [GTMaterialProperties#FORMULA], or
/// [GTMaterialProperties#GTPP_CHEMICAL_FORMULA] carries it -- in that priority order, which reproduces what the legacy
/// item
/// each ML stack replaced actually rendered: a werkstoff-backed material's legacy items (both the bartworks
/// ones and the gregtech bridge ones, whose `Materials` formula `BridgeMaterialsLoader` overwrote from the
/// werkstoff side unconditionally -- hence no fall-through past a present WERKSTOFF property, even when its
/// formula is empty) showed the werkstoff formula; a gregtech-dumped material's items showed
/// `Materials#mChemicalFormula`
/// (baked per material into [gregtech.api.enums.materials2.Materials2Formulas] as
/// [GTMaterialProperties#FORMULA], covering both explicit legacy overrides and constructor-derived strings, so
/// gtpp-merged materials keep the gregtech-side formula their dominant legacy items showed); only a gtpp-only
/// material's items showed `gtPlusPlus.core.material.Material#vChemicalFormula`.
///
/// [#forSearch] feeds [gregtech.nei.searchprovider.ChemicalFormulaFilter] and returns the raw string its
/// legacy resolution produced for the same material -- unsanitized, including `"?"`/`"??"` placeholders; the
/// filter applies its own validity check and sanitization exactly as before. [#forTooltip] returns the
/// ready-to-display line (or null for none), reproducing each legacy renderer's own quirks: `Materials
/// #addTooltips` suppressed the exact-`"?"` placeholder, `Werkstoff#addTooltips` displayed any non-empty
/// string, and `gtPlusPlus.core.material.Material#addTooltips` always displayed the
/// [StringUtils#sanitizeStringKeepBrackets]-cleaned formula (keeping `?` characters when present).
public final class MaterialFormulas {

    private MaterialFormulas() {}

    /// The raw formula string for NEI search, or null when the material carries none.
    public static @Nullable String forSearch(@Nullable Material ml) {
        if (ml == null) return null;

        WerkstoffData werkstoff = ml.getProperty(GTMaterialProperties.WERKSTOFF);
        if (werkstoff != null) {
            return GTUtility.isStringValid(werkstoff.formula()) ? localizedWerkstoffFormula(ml, werkstoff) : null;
        }

        String formula = ml.getProperty(GTMaterialProperties.FORMULA);
        if (formula != null) {
            return Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.FORMULA_LOCALIZED))
                ? StatCollector.translateToLocal(materialsFormulaKey(ml))
                : formula;
        }

        String gtppFormula = ml.getProperty(GTMaterialProperties.GTPP_CHEMICAL_FORMULA);
        if (GTUtility.isStringValid(gtppFormula)) {
            return gtppFormula;
        }

        return null;
    }

    /// The formula tooltip line to display, or null when the legacy renderer showed none.
    public static @Nullable String forTooltip(@Nullable Material ml) {
        if (ml == null) return null;

        WerkstoffData werkstoff = ml.getProperty(GTMaterialProperties.WERKSTOFF);
        if (werkstoff != null) {
            if (!GTUtility.isStringValid(werkstoff.formula())) return null;
            String formula = localizedWerkstoffFormula(ml, werkstoff);
            return GTUtility.isStringValid(formula) ? formula : null;
        }

        String formula = ml.getProperty(GTMaterialProperties.FORMULA);
        if (formula != null) {
            if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.FORMULA_LOCALIZED))) {
                formula = StatCollector.translateToLocal(materialsFormulaKey(ml));
            }
            return GTUtility.isStringValid(formula) && !"?".equals(formula) ? formula : null;
        }

        String gtppFormula = ml.getProperty(GTMaterialProperties.GTPP_CHEMICAL_FORMULA);
        if (GTUtility.isStringValid(gtppFormula)) {
            return gtppFormula.contains("?") ? StringUtils.sanitizeStringKeepBracketsQuestion(gtppFormula)
                : StringUtils.sanitizeStringKeepBrackets(gtppFormula);
        }

        return null;
    }

    private static String localizedWerkstoffFormula(Material ml, WerkstoffData werkstoff) {
        return werkstoff.flags()
            .contains(GTWerkstoffFlag.LOCALIZED_FORMULA) ? StatCollector.translateToLocal(werkstoffFormulaKey(ml))
                : werkstoff.formula();
    }

    /// Mirrors `IOreMaterial#getLocalizedNameKey` (`"Material." + getInternalName().toLowerCase()`) for the
    /// reconstructed legacy `Materials` instance -- `getInternalName()` there is `mName`, i.e.
    /// [GTMaterialProperties#LEGACY_NAME] when present, else the MaterialLib registration name.
    private static String materialsFormulaKey(Material ml) {
        String legacyName = ml.getProperty(GTMaterialProperties.LEGACY_NAME);
        return "Material." + (legacyName != null ? legacyName : ml.getName()).toLowerCase() + ".ChemicalFormula";
    }

    /// As [#materialsFormulaKey], for the reconstructed legacy `Werkstoff` -- `Werkstoff#getInternalName` is
    /// `getVarName()`, `defaultName` (== [GTMaterialProperties#LOCAL_NAME]) with spaces stripped.
    private static String werkstoffFormulaKey(Material ml) {
        String localName = ml.getProperty(GTMaterialProperties.LOCAL_NAME);
        String varName = (localName != null ? localName : ml.getName()).replace(" ", "");
        return "Material." + varName.toLowerCase() + ".ChemicalFormula";
    }
}
