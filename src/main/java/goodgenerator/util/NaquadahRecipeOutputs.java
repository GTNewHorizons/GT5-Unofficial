package goodgenerator.util;

import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import bartworks.system.material.gtenhancement.PlatinumSludgeOutputs;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

/// Rewrites naquadah-family dust outputs into their oxide mixtures at recipe-generation time, layered on top of
/// [PlatinumSludgeOutputs] so a single call covers both substitution families.
///
/// The `convert` entry points double ore-processing outputs. The `convertDecomposition` ones leave
/// decomposition outputs (electrolyzer, centrifuge, washer) at their original amount.
public final class NaquadahRecipeOutputs {

    private static final OrePrefixes[] DUST_PREFIXES = { dust, dustSmall, dustTiny };

    private NaquadahRecipeOutputs() {}

    public static ItemStack[] convert(ItemStack... outputs) {
        return convertNaquadah(PlatinumSludgeOutputs.convert(outputs), 2);
    }

    public static ItemStack[] convert(Material inputMaterial, ItemStack... outputs) {
        return convertNaquadah(PlatinumSludgeOutputs.convert(inputMaterial, outputs), 2);
    }

    public static ItemStack[] convertDecomposition(Material inputMaterial, ItemStack... outputs) {
        return convertNaquadah(PlatinumSludgeOutputs.convert(inputMaterial, outputs), 1);
    }

    public static ItemStack[] convertDecomposition(ItemStack... outputs) {
        return convertNaquadah(PlatinumSludgeOutputs.convert(outputs), 1);
    }

    public static ItemStack convert(ItemStack output) {
        return convert(output, 2);
    }

    private static ItemStack convert(ItemStack output, int multiplier) {
        if (!GTUtility.isStackValid(output)) return output;
        for (OrePrefixes prefix : DUST_PREFIXES) {
            ItemStack replaced = swap(output, prefix, multiplier, Materials.Naquadah, Materials.NaquadahOxideMixture);
            if (replaced != null) return replaced;
            replaced = swap(
                output,
                prefix,
                multiplier,
                Materials.NaquadahEnriched,
                Materials.EnrichedNaquadahOxideMixture);
            if (replaced != null) return replaced;
            replaced = swap(output, prefix, multiplier, Materials.Naquadria, Materials.NaquadriaOxideMixture);
            if (replaced != null) return replaced;
        }
        return output;
    }

    private static ItemStack swap(ItemStack output, OrePrefixes prefix, int multiplier, Material from, Material to) {
        if (!GTUtility.areStacksEqual(output, GTOreDictUnificator.get(prefix, from, 1L), true)) return null;
        return GTOreDictUnificator.get(prefix, to, output.stackSize * (long) multiplier);
    }

    private static ItemStack[] convertNaquadah(ItemStack[] outputs, int multiplier) {
        for (int i = 0; i < outputs.length; i++) outputs[i] = convert(outputs[i], multiplier);
        return outputs;
    }
}
