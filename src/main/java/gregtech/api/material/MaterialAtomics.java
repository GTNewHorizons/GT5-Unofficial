package gregtech.api.material;

import static gregtech.api.enums.GTValues.M;

import java.util.List;
import java.util.function.ToLongFunction;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Element;

/// Computes a material's atomic quantities -- protons, neutrons, and mass -- from declaration data using a
/// single canonical formula: an [GTMaterialProperties#ELEMENT]-backed material reads the [Element] table, a
/// composition-backed one takes the density-weighted average over [GTMaterialProperties#COMPOSITION]
/// (`density * sum(amount * component) / (totalAmount * M)`), and a material with neither falls back to
/// [Element#Tc].
public final class MaterialAtomics {

    private MaterialAtomics() {}

    public static long protons(Material material) {
        return compute(material, Element::getProtons);
    }

    public static long neutrons(Material material) {
        return compute(material, Element::getNeutrons);
    }

    public static long mass(Material material) {
        return compute(material, Element::getMass);
    }

    private static long compute(Material material, ToLongFunction<Element> value) {
        String elementName = material.getProperty(GTMaterialProperties.ELEMENT);
        if (elementName != null) return value.applyAsLong(Element.get(elementName));
        List<MaterialRefStack> composition = material.getProperty(GTMaterialProperties.COMPOSITION);
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
        return (density(material) * sum) / (totalAmount * M);
    }

    /// The density value `(M * densityMultiplier) / densityDivider` from
    /// [GTMaterialProperties#DENSITY_MULTIPLIER]/[GTMaterialProperties#DENSITY_DIVIDER] (each `1` when
    /// absent), using integer division with no rounding.
    public static long density(Material material) {
        Integer multiplier = material.getProperty(GTMaterialProperties.DENSITY_MULTIPLIER);
        Integer divider = material.getProperty(GTMaterialProperties.DENSITY_DIVIDER);
        return (M * (multiplier != null ? multiplier : 1)) / (divider != null ? divider : 1);
    }
}
