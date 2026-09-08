package gregtech.common.oredict;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTUtility;

final class OreDictRegistration {

    final String oreName;
    final ItemStack stack;
    final OrePrefixes prefix;
    final Materials material;
    final String modId;

    OreDictRegistration(String oreName, ItemStack stack, OrePrefixes prefix, Materials material, String modId) {
        this.oreName = oreName;
        this.stack = stack;
        this.prefix = prefix;
        this.material = material;
        this.modId = modId == null || modId.equals("UNKNOWN") ? null : modId;
    }

    void registerRecipes() {
        if (stack == null || stack.getItem() == null || prefix == null || prefix.isIgnored(material)) {
            return;
        }

        if (stack.stackSize != 1) {
            stack.stackSize = 1;
        }

        prefix
            .processOre(material == null ? Materials._NULL : material, oreName, modId, GTUtility.copyAmount(1, stack));
    }
}
