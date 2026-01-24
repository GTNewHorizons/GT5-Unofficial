package gregtech.nei.searchprovider;

import java.util.regex.Pattern;

import net.minecraft.item.ItemStack;

import codechicken.nei.ItemStackMap;
import codechicken.nei.api.ItemFilter;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

public class ChemicalFormulaFilter implements ItemFilter {

    private static final ItemStackMap<String> itemSearchNames = new ItemStackMap<>();

    private final Pattern pattern;

    public ChemicalFormulaFilter(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        return pattern.matcher(getSearchFormula(itemStack))
            .find();
    }

    public static void clearCache() {
        itemSearchNames.clear();
    }

    public static void putItem(ItemStack stack) {
        String chemicalFormula = getChemicalFormula(stack.copy());
        synchronized (itemSearchNames) {
            itemSearchNames.put(stack, chemicalFormula);
        }
    }

    public static String getSearchFormula(ItemStack stack) {

        String chemicalFormula = itemSearchNames.get(stack);

        if (chemicalFormula == null) {
            chemicalFormula = getChemicalFormula(stack.copy());

            synchronized (itemSearchNames) {
                itemSearchNames.put(stack, chemicalFormula);
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
