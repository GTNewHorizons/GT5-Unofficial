package gregtech.nei.searchprovider;

import java.util.concurrent.FutureTask;
import java.util.regex.Pattern;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import codechicken.nei.ItemStackMap;
import codechicken.nei.api.ItemFilter;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.Material;

public class ChemicalFormulaFilter implements ItemFilter {

    private final Pattern pattern;
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

    public ChemicalFormulaFilter(Pattern pattern) {
        this.pattern = pattern;
    }

    private static String sanitizeFormula(String formula) {
        return EnumChatFormatting.getTextWithoutFormattingCodes(formula);
    }

    private static boolean isValidFormula(String formula) {
        return !("?".equals(formula) || "??".equals(formula));
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
            return data.mMaterial.mMaterial.mChemicalFormula;
        }

        return "";
    }

}
