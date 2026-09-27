package gtnhlanth.util;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import bartworks.system.material.gtenhancement.PlatinumSludgeOutputs;
import goodgenerator.util.NaquadahRecipeOutputs;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialData;
import gregtech.api.material.MaterialParts;
import gregtech.api.objects.ItemData;
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
            return MaterialLibAPI
                .getStack(Materials.CeriumRichMixture, Shapes.dust, Math.min(64, output.stackSize * 2));
        }
        if (material == Materials.Samarium) {
            return MaterialLibAPI
                .getStack(Materials.SamariumOreConcentrate, Shapes.dust, Math.min(64, output.stackSize * 2));
        }
        return output;
    }

    private static ItemStack[] convert(int multiplier, ItemStack... outputs) {
        for (int i = 0; i < outputs.length; i++) {
            ItemStack output = outputs[i];
            if (!GTUtility.isStackValid(output)) continue;

            Material replacement = null;
            Shape shape = null;
            if (matches(output, Shapes.dust, Materials.Cerium)) {
                replacement = Materials.CeriumRichMixture;
                shape = Shapes.dust;
            } else if (matches(output, Shapes.dustSmall, Materials.Cerium)) {
                replacement = Materials.CeriumRichMixture;
                shape = Shapes.dustSmall;
            } else if (matches(output, Shapes.dustTiny, Materials.Cerium)) {
                replacement = Materials.CeriumRichMixture;
                shape = Shapes.dustTiny;
            } else if (matches(output, Shapes.dust, Materials.Samarium)) {
                replacement = Materials.SamariumOreConcentrate;
                shape = Shapes.dust;
            } else if (matches(output, Shapes.dustSmall, Materials.Samarium)) {
                replacement = Materials.SamariumOreConcentrate;
                shape = Shapes.dustSmall;
            } else if (matches(output, Shapes.dustTiny, Materials.Samarium)) {
                replacement = Materials.SamariumOreConcentrate;
                shape = Shapes.dustTiny;
            } else {
                ItemData association = MaterialData.getAssociation(output);
                if (association == null || association.mPrefix == null || association.mMaterial == null) continue;
                Material material = association.mMaterial.mMaterial;
                if (material == Materials.Cerium) {
                    replacement = Materials.CeriumRichMixture;
                } else if (material == Materials.Samarium) {
                    replacement = Materials.SamariumOreConcentrate;
                }
                shape = MaterialParts.shape(association.mPrefix);
            }

            if (replacement == null) continue;
            if (shape != Shapes.dust && shape != Shapes.dustSmall && shape != Shapes.dustTiny) continue;
            outputs[i] = MaterialLibAPI.getStack(replacement, shape, Math.min(64, output.stackSize * multiplier));
        }
        return outputs;
    }

    private static boolean matches(ItemStack output, Shape shape, Material material) {
        return GTUtility.areStacksEqual(output, MaterialLibAPI.getStack(material, shape, 1), true);
    }
}
