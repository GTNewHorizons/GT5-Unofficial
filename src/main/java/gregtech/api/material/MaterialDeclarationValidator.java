package gregtech.api.material;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Property;

import gregtech.api.enums.Element;

/// Resolves every cross-material reference GregTech declares, once, so a reference whose target was renamed or
/// deleted is a boot failure naming the material and the property.
///
/// Without this the same declaration bug surfaces once per consumer and never as itself: [MaterialAtomics]
/// throws lazily at the first tooltip render or recipe load, [MaterialUtils#materialList] and
/// [MaterialUtils#oreByProducts] compute formulas and decomposition recipes from a partial composition, and
/// `MaterialUtils`'s smelt/macerate/arc-smelt chase silently falls back to the material itself, producing a
/// plausible "smelts into itself" nobody notices until gameplay. Those read paths therefore stay lenient: with
/// this sweep in place their miss branches are unreachable for declared data.
public final class MaterialDeclarationValidator {

    private static final int MAX_REPORTED = 20;

    private static final List<Property<MaterialRef>> REF_PROPERTIES = List.of(
        GTMaterialProperties.ARC_SMELT_INTO,
        GTMaterialProperties.DIRECT_SMELTING,
        GTMaterialProperties.HANDLE_MATERIAL,
        GTMaterialProperties.MACERATE_INTO,
        GTMaterialProperties.SMELT_INTO);

    private static final List<Property<List<MaterialRefStack>>> REF_STACK_PROPERTIES = List
        .of(GTMaterialProperties.COMPOSITION, GTMaterialProperties.ORE_BYPRODUCTS);

    private MaterialDeclarationValidator() {}

    /// Requires the resolved registry, so it runs no earlier than the start of GT's preInit.
    public static void validate() {
        List<String> errors = new ArrayList<>();
        List<Material> materials = new ArrayList<>();

        for (Material material : MaterialLibAPI.getMaterials()) {
            if (!"gregtech".equals(material.getModId())) continue;
            materials.add(material);
            checkRefs(material, errors);
            checkElement(material, errors);
            checkDensity(material, errors);
        }

        Set<Material> settled = new HashSet<>();
        for (Material material : materials) {
            checkCompositionCycles(material, settled, new LinkedHashSet<>(), errors);
        }

        if (!errors.isEmpty()) {
            StringBuilder detail = new StringBuilder();
            for (String error : errors.subList(0, Math.min(errors.size(), MAX_REPORTED))) {
                detail.append("\n  ")
                    .append(error);
            }
            throw new IllegalStateException("Invalid GregTech material declarations (" + errors.size() + "):" + detail);
        }
    }

    private static void checkRefs(Material material, List<String> errors) {
        for (Property<MaterialRef> property : REF_PROPERTIES) {
            MaterialRef ref = material.getProperty(property);
            if (ref != null && ref.resolve() == null) {
                errors.add(describe(material, property) + " names unregistered material " + ref.name());
            }
        }
        for (Property<List<MaterialRefStack>> property : REF_STACK_PROPERTIES) {
            List<MaterialRefStack> entries = material.getProperty(property);
            if (entries == null) continue;
            long total = 0;
            for (MaterialRefStack entry : entries) {
                total += entry.amount();
                if (entry.material()
                    .resolve() == null) {
                    errors.add(
                        describe(material, property) + " names unregistered material "
                            + entry.material()
                                .name());
                }
            }
            if (property == GTMaterialProperties.COMPOSITION && !entries.isEmpty() && total == 0) {
                errors.add(describe(material, property) + " amounts sum to 0");
            }
        }
    }

    private static void checkElement(Material material, List<String> errors) {
        String name = material.getProperty(GTMaterialProperties.ELEMENT);
        if (name == null) return;
        if (Element.get(name) == Element._NULL && !Element._NULL.name()
            .equals(name)) {
            errors.add(describe(material, GTMaterialProperties.ELEMENT) + " names unknown element " + name);
        }
    }

    private static void checkDensity(Material material, List<String> errors) {
        Integer divider = material.getProperty(GTMaterialProperties.DENSITY_DIVIDER);
        if (divider != null && divider == 0) {
            errors.add(describe(material, GTMaterialProperties.DENSITY_DIVIDER) + " is 0");
        }
    }

    private static void checkCompositionCycles(Material material, Set<Material> settled, LinkedHashSet<Material> path,
        List<String> errors) {
        if (settled.contains(material)) return;
        if (!path.add(material)) {
            StringBuilder cycle = new StringBuilder();
            for (Material step : path) {
                cycle.append(step.getName())
                    .append(" -> ");
            }
            errors.add("composition cycle: " + cycle.append(material.getName()));
            return;
        }
        List<MaterialRefStack> composition = material.getProperty(GTMaterialProperties.COMPOSITION);
        if (composition != null) {
            for (MaterialRefStack entry : composition) {
                Material component = entry.material()
                    .resolve();
                if (component != null) checkCompositionCycles(component, settled, path, errors);
            }
        }
        path.remove(material);
        settled.add(material);
    }

    private static String describe(Material material, Property<?> property) {
        return material.getName() + "." + property.getName();
    }
}
