package gregtech.api.material;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.util.GTUtility;

/// Resolves a [Material]'s chemical formula display string from [GTMaterialProperties#FORMULA], translating it
/// through the material's `.ChemicalFormula` lang key when [GTMaterialProperties#FORMULA_LOCALIZED] is set.
///
/// [#forSearch] keeps the `"?"` and `"??"` placeholders. [#forTooltip] hides the `"?"` placeholder.
public final class MaterialFormulas {

    private MaterialFormulas() {}

    /// Returns the formula string for NEI search, or null when the material is null or carries none.
    public static @Nullable String forSearch(@Nullable Material material) {
        if (material == null) return null;

        String formula = material.getProperty(GTMaterialProperties.FORMULA);
        if (formula == null) return null;
        return Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.FORMULA_LOCALIZED))
            ? StatCollector.translateToLocal(formulaKey(material))
            : formula;
    }

    /// Returns the formula tooltip line, or null when there is none or it is the `"?"` placeholder.
    public static @Nullable String forTooltip(@Nullable Material material) {
        String formula = forSearch(material);
        return GTUtility.isStringValid(formula) && !"?".equals(formula) ? formula : null;
    }

    /// Returns the material's [MaterialUtils#localizedNameKey] plus `.ChemicalFormula`.
    private static String formulaKey(Material material) {
        return MaterialUtils.localizedNameKey(material) + ".ChemicalFormula";
    }
}
