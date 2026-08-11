package gtnhlanth.util;

import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;

import net.minecraft.item.ItemStack;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.gtenhancement.PlatinumSludgeOutputs;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtnhlanth.common.register.WerkstoffMaterialPool;

public final class LanthanidesRecipeOutputs {

    private LanthanidesRecipeOutputs() {}

    public static ItemStack[] convertOre(Materials inputMaterial, ItemStack... outputs) {
        return convert(2, PlatinumSludgeOutputs.convert(inputMaterial, outputs));
    }

    public static ItemStack[] convertOre(ItemStack... outputs) {
        return convert(2, PlatinumSludgeOutputs.convert(outputs));
    }

    public static ItemStack[] convertDecomposition(Materials inputMaterial, ItemStack... outputs) {
        return convert(1, PlatinumSludgeOutputs.convert(inputMaterial, outputs));
    }

    public static ItemStack[] convertDecomposition(ItemStack... outputs) {
        return convert(1, PlatinumSludgeOutputs.convert(outputs));
    }

    public static ItemStack convertCrafting(Materials material, ItemStack output) {
        output = PlatinumSludgeOutputs.convertCrafting(material, output);
        if (!GTUtility.isStackValid(output)) return output;
        if (material == Materials.Cerium) {
            return WerkstoffMaterialPool.CeriumRichMixture.get(dust, output.stackSize * 2);
        }
        if (material == Materials.Samarium) {
            return WerkstoffMaterialPool.SamariumOreConcentrate.get(dust, output.stackSize * 2);
        }
        return output;
    }

    private static ItemStack[] convert(int multiplier, ItemStack... outputs) {
        for (int i = 0; i < outputs.length; i++) {
            ItemStack output = outputs[i];
            if (!GTUtility.isStackValid(output)) continue;

            ItemData association = GTOreDictUnificator.getAssociation(output);
            if (association == null || association.mPrefix == null || association.mMaterial == null) continue;

            Werkstoff replacement = association.mMaterial.mMaterial == Materials.Cerium
                ? WerkstoffMaterialPool.CeriumRichMixture
                : association.mMaterial.mMaterial == Materials.Samarium
                    ? WerkstoffMaterialPool.SamariumOreConcentrate
                    : null;
            if (replacement == null) continue;

            OrePrefixes prefix = association.mPrefix;
            if (prefix != dust && prefix != dustSmall && prefix != dustTiny) continue;
            outputs[i] = replacement.get(prefix, output.stackSize * multiplier);
        }
        return outputs;
    }
}
