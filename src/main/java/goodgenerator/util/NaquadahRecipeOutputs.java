package goodgenerator.util;

import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;

import net.minecraft.item.ItemStack;

import bartworks.system.material.gtenhancement.PlatinumSludgeOutputs;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTUtility;

public final class NaquadahRecipeOutputs {

    private NaquadahRecipeOutputs() {}

    public static ItemStack[] convert(ItemStack... outputs) {
        return convertNaquadah(PlatinumSludgeOutputs.convert(outputs), 2);
    }

    public static ItemStack[] convert(Materials inputMaterial, ItemStack... outputs) {
        return convertNaquadah(PlatinumSludgeOutputs.convert(inputMaterial, outputs), 2);
    }

    public static ItemStack[] convertDecomposition(Materials inputMaterial, ItemStack... outputs) {
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
        if (GTUtility.areStacksEqual(output, Materials.Naquadah.getDust(1), true)) {
            return GGMaterial.naquadahEarth.get(dust, output.stackSize * multiplier);
        }
        if (GTUtility.areStacksEqual(output, Materials.NaquadahEnriched.getDust(1), true)) {
            return GGMaterial.enrichedNaquadahEarth.get(dust, output.stackSize * multiplier);
        }
        if (GTUtility.areStacksEqual(output, Materials.Naquadria.getDust(1), true)) {
            return GGMaterial.naquadriaEarth.get(dust, output.stackSize * multiplier);
        }
        if (GTUtility.areStacksEqual(output, Materials.Naquadah.getDustSmall(1), true)) {
            return GGMaterial.naquadahEarth.get(dustSmall, output.stackSize * multiplier);
        }
        if (GTUtility.areStacksEqual(output, Materials.NaquadahEnriched.getDustSmall(1), true)) {
            return GGMaterial.enrichedNaquadahEarth.get(dustSmall, output.stackSize * multiplier);
        }
        if (GTUtility.areStacksEqual(output, Materials.Naquadria.getDustSmall(1), true)) {
            return GGMaterial.naquadriaEarth.get(dustSmall, output.stackSize * multiplier);
        }
        if (GTUtility.areStacksEqual(output, Materials.Naquadah.getDustTiny(1), true)) {
            return GGMaterial.naquadahEarth.get(dustTiny, output.stackSize * multiplier);
        }
        if (GTUtility.areStacksEqual(output, Materials.NaquadahEnriched.getDustTiny(1), true)) {
            return GGMaterial.enrichedNaquadahEarth.get(dustTiny, output.stackSize * multiplier);
        }
        if (GTUtility.areStacksEqual(output, Materials.Naquadria.getDustTiny(1), true)) {
            return GGMaterial.naquadriaEarth.get(dustTiny, output.stackSize * multiplier);
        }
        return output;
    }

    private static ItemStack[] convertNaquadah(ItemStack[] outputs, int multiplier) {
        for (int i = 0; i < outputs.length; i++) outputs[i] = convert(outputs[i], multiplier);
        return outputs;
    }
}
