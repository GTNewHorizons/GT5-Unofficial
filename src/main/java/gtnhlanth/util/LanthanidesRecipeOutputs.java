package gtnhlanth.util;

import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import bartworks.system.material.gtenhancement.PlatinumSludgeOutputs;
import goodgenerator.util.NaquadahRecipeOutputs;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

/// Rewrites cerium and samarium dust outputs into the lanthanide-line concentrates at recipe-generation time.
///
/// This is the outermost of the three output-substitution layers: every entry point runs
/// [NaquadahRecipeOutputs] (which itself runs [PlatinumSludgeOutputs]) first, so ore-processing loaders only ever
/// need to call this one.
public final class LanthanidesRecipeOutputs {

    private LanthanidesRecipeOutputs() {}

    public static ItemStack[] convertOre(Material inputMaterial, ItemStack... outputs) {
        return convert(2, NaquadahRecipeOutputs.convert(inputMaterial, outputs));
    }

    public static ItemStack[] convertOre(ItemStack... outputs) {
        return convert(2, NaquadahRecipeOutputs.convert(outputs));
    }

    public static ItemStack[] convertDecomposition(Material inputMaterial, ItemStack... outputs) {
        return convert(1, NaquadahRecipeOutputs.convertDecomposition(inputMaterial, outputs));
    }

    public static ItemStack[] convertDecomposition(ItemStack... outputs) {
        return convert(1, NaquadahRecipeOutputs.convertDecomposition(outputs));
    }

    public static ItemStack convertCrafting(Material material, ItemStack output) {
        output = PlatinumSludgeOutputs.convertCrafting(material, output);
        output = NaquadahRecipeOutputs.convert(output);
        if (!GTUtility.isStackValid(output)) return output;
        if (material == Materials.Cerium) {
            return GTOreDictUnificator.get(dust, Materials.CeriumRichMixture, output.stackSize * 2L);
        }
        if (material == Materials.Samarium) {
            return GTOreDictUnificator.get(dust, Materials.SamariumOreConcentrate, output.stackSize * 2L);
        }
        return output;
    }

    private static ItemStack[] convert(int multiplier, ItemStack... outputs) {
        for (int i = 0; i < outputs.length; i++) {
            ItemStack output = outputs[i];
            if (!GTUtility.isStackValid(output)) continue;

            Material replacement = null;
            OrePrefixes prefix = null;
            if (matches(output, dust, Materials.Cerium)) {
                replacement = Materials.CeriumRichMixture;
                prefix = dust;
            } else if (matches(output, dustSmall, Materials.Cerium)) {
                replacement = Materials.CeriumRichMixture;
                prefix = dustSmall;
            } else if (matches(output, dustTiny, Materials.Cerium)) {
                replacement = Materials.CeriumRichMixture;
                prefix = dustTiny;
            } else if (matches(output, dust, Materials.Samarium)) {
                replacement = Materials.SamariumOreConcentrate;
                prefix = dust;
            } else if (matches(output, dustSmall, Materials.Samarium)) {
                replacement = Materials.SamariumOreConcentrate;
                prefix = dustSmall;
            } else if (matches(output, dustTiny, Materials.Samarium)) {
                replacement = Materials.SamariumOreConcentrate;
                prefix = dustTiny;
            } else {
                ItemData association = GTOreDictUnificator.getAssociation(output);
                if (association == null || association.mPrefix == null || association.mMaterial == null) continue;
                Material material = association.mMaterial.mMaterial;
                if (material == Materials.Cerium) {
                    replacement = Materials.CeriumRichMixture;
                } else if (material == Materials.Samarium) {
                    replacement = Materials.SamariumOreConcentrate;
                }
                prefix = association.mPrefix;
            }

            if (replacement == null) continue;
            if (prefix != dust && prefix != dustSmall && prefix != dustTiny) continue;
            outputs[i] = GTOreDictUnificator.get(prefix, replacement, output.stackSize * (long) multiplier);
        }
        return outputs;
    }

    private static boolean matches(ItemStack output, OrePrefixes prefix, Material material) {
        return GTUtility.areStacksEqual(output, GTOreDictUnificator.get(prefix, material, 1L), true);
    }
}
