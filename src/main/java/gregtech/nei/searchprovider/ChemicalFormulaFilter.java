package gregtech.nei.searchprovider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.regex.Pattern;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.gtnhlib.util.font.GlyphReplacements;

import codechicken.nei.ItemStackMap;
import codechicken.nei.api.ItemFilter;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.Material;

public class ChemicalFormulaFilter implements ItemFilter {

    private final Pattern pattern;
    private static final Map<Character, Character> SCRIPT_DIGIT_MAP = new HashMap<>(20);
    private static final ItemStackMap<String> formulaCache = new ItemStackMap<>();
    private static final FutureTask<Void> loadGTPlusPlusMaterial = new FutureTask<>(() -> {
        Material.mComponentMap.forEach((name, components) -> {
            Material material = Material.mMaterialsByName.get(name);
            if (material != null) {
                String chemicalFormula = material.vChemicalFormula;
                String sanitizedFormula = isValidFormula(chemicalFormula) ? sanitizeFormula(chemicalFormula) : "";
                components.forEach((orePrefix, stack) -> {
                    synchronized (formulaCache) {
                        formulaCache.put(stack, sanitizedFormula);
                    }
                });
            }
        });
        return null;
    });

    static {
        // subscript
        SCRIPT_DIGIT_MAP.put('₀', '0');
        SCRIPT_DIGIT_MAP.put('₁', '1');
        SCRIPT_DIGIT_MAP.put('₂', '2');
        SCRIPT_DIGIT_MAP.put('₃', '3');
        SCRIPT_DIGIT_MAP.put('₄', '4');
        SCRIPT_DIGIT_MAP.put('₅', '5');
        SCRIPT_DIGIT_MAP.put('₆', '6');
        SCRIPT_DIGIT_MAP.put('₇', '7');
        SCRIPT_DIGIT_MAP.put('₈', '8');
        SCRIPT_DIGIT_MAP.put('₉', '9');

        // superscript
        SCRIPT_DIGIT_MAP.put('⁰', '0');
        SCRIPT_DIGIT_MAP.put('¹', '1');
        SCRIPT_DIGIT_MAP.put('²', '2');
        SCRIPT_DIGIT_MAP.put('³', '3');
        SCRIPT_DIGIT_MAP.put('⁴', '4');
        SCRIPT_DIGIT_MAP.put('⁵', '5');
        SCRIPT_DIGIT_MAP.put('⁶', '6');
        SCRIPT_DIGIT_MAP.put('⁷', '7');
        SCRIPT_DIGIT_MAP.put('⁸', '8');
        SCRIPT_DIGIT_MAP.put('⁹', '9');
    }

    public ChemicalFormulaFilter(Pattern pattern) {
        this.pattern = pattern;
    }

    private static String sanitizeFormula(String formula) {
        formula = EnumChatFormatting.getTextWithoutFormattingCodes(formula);
        formula = normalizeCustomGlyphs(formula);
        formula = normalizeScriptDigits(formula);
        return formula;
    }

    private static boolean isValidFormula(String formula) {
        return !(formula == null || formula.isEmpty() || "?".equals(formula) || "??".equals(formula));
    }

    private static void ensureLoadGTPlusPlusMaterials() {
        loadGTPlusPlusMaterial.run();
        try {
            loadGTPlusPlusMaterial.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        return pattern.matcher(getSearchFormula(itemStack))
            .find();
    }

    public static String getSearchFormula(ItemStack stack) {
        ensureLoadGTPlusPlusMaterials();

        String chemicalFormula = formulaCache.get(stack);

        if (chemicalFormula == null) {
            chemicalFormula = getChemicalFormula(stack);
            if (isValidFormula(chemicalFormula)) {
                chemicalFormula = sanitizeFormula(chemicalFormula);
            } else {
                chemicalFormula = "";
            }

            synchronized (formulaCache) {
                formulaCache.put(stack, chemicalFormula);
            }
        }

        return chemicalFormula;
    }

    private static String getChemicalFormula(ItemStack itemstack) {

        ItemData data = GTOreDictUnificator.getAssociation(itemstack);
        if (data != null) {
            return data.mMaterial.mMaterial.getChemicalFormula();
        }

        return "";
    }

    public static String normalizeCustomGlyphs(String s) {

        for (Map.Entry<String, String> e : GlyphReplacements.customGlyphs.entrySet()) {
            if (e.getValue() != null) {
                s = s.replace(e.getKey(), e.getValue());
            }
        }

        return s;
    }

    private static String normalizeScriptDigits(String s) {

        for (Map.Entry<Character, Character> e : SCRIPT_DIGIT_MAP.entrySet()) {
            s = s.replace(e.getKey(), e.getValue());
        }

        return s;
    }

}
