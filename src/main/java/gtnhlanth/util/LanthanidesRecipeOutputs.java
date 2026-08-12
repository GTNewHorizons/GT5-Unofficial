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

            Werkstoff replacement = null;
            OrePrefixes prefix = null;
            if (GTUtility.areStacksEqual(output, Materials.Cerium.getDust(1), true)) {
                replacement = WerkstoffMaterialPool.CeriumRichMixture;
                prefix = dust;
            } else if (GTUtility.areStacksEqual(output, Materials.Cerium.getDustSmall(1), true)) {
                replacement = WerkstoffMaterialPool.CeriumRichMixture;
                prefix = dustSmall;
            } else if (GTUtility.areStacksEqual(output, Materials.Cerium.getDustTiny(1), true)) {
                replacement = WerkstoffMaterialPool.CeriumRichMixture;
                prefix = dustTiny;
            } else if (GTUtility.areStacksEqual(output, Materials.Samarium.getDust(1), true)) {
                replacement = WerkstoffMaterialPool.SamariumOreConcentrate;
                prefix = dust;
            } else if (GTUtility.areStacksEqual(output, Materials.Samarium.getDustSmall(1), true)) {
                replacement = WerkstoffMaterialPool.SamariumOreConcentrate;
                prefix = dustSmall;
            } else if (GTUtility.areStacksEqual(output, Materials.Samarium.getDustTiny(1), true)) {
                replacement = WerkstoffMaterialPool.SamariumOreConcentrate;
                prefix = dustTiny;
            } else {
                ItemData association = GTOreDictUnificator.getAssociation(output);
                if (association == null || association.mPrefix == null || association.mMaterial == null) continue;
                Materials material = association.mMaterial.mMaterial;
                if (material == Materials.Cerium) {
                    replacement = WerkstoffMaterialPool.CeriumRichMixture;
                } else if (material == Materials.Samarium) {
                    replacement = WerkstoffMaterialPool.SamariumOreConcentrate;
                }
                prefix = association.mPrefix;
            }

            if (replacement == null) continue;
            if (prefix != dust && prefix != dustSmall && prefix != dustTiny) continue;
            outputs[i] = replacement.get(prefix, output.stackSize * multiplier);
        }
        return outputs;
    }
}
