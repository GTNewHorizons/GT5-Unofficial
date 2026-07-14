package gregtech.api.material;

import static gregtech.api.enums.GTValues.M;

import java.util.List;
import java.util.function.ToLongFunction;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Element;

/// Computes a material's atomic quantities from declaration data, reproducing the legacy
/// `Materials#getProtons`/`#getNeutrons`/`#getMass` semantics as the single canonical formula: an
/// [GTMaterialProperties#ELEMENT]-backed material reads the [Element] table, a composition-backed one takes
/// the density-weighted average over [GTMaterialProperties#COMPOSITION]
/// (`density * sum(amount * component) / (totalAmount * M)`), and a material with neither falls back to
/// [Element#Tc] exactly as the legacy formula does.
public final class MaterialAtomics {

    private MaterialAtomics() {}

    public static long protons(Material ml) {
        return compute(ml, Element::getProtons);
    }

    public static long neutrons(Material ml) {
        return compute(ml, Element::getNeutrons);
    }

    public static long mass(Material ml) {
        return compute(ml, Element::getMass);
    }

    private static long compute(Material ml, ToLongFunction<Element> value) {
        String elementName = ml.getProperty(GTMaterialProperties.ELEMENT);
        if (elementName != null) return value.applyAsLong(Element.valueOf(elementName));
        List<MaterialRefStack> composition = ml.getProperty(GTMaterialProperties.COMPOSITION);
        if (composition == null || composition.isEmpty()) return value.applyAsLong(Element.Tc);
        long totalAmount = 0;
        long sum = 0;
        for (MaterialRefStack stack : composition) {
            totalAmount += stack.amount();
            sum += stack.amount() * compute(
                stack.material()
                    .resolve(),
                value);
        }
        return (density(ml) * sum) / (totalAmount * M);
    }

    private static long density(Material ml) {
        Integer multiplier = ml.getProperty(GTMaterialProperties.DENSITY_MULTIPLIER);
        Integer divider = ml.getProperty(GTMaterialProperties.DENSITY_DIVIDER);
        return (M * (multiplier != null ? multiplier : 1)) / (divider != null ? divider : 1);
    }
}
