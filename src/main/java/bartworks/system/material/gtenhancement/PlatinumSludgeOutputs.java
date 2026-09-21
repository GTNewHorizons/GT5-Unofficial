package bartworks.system.material.gtenhancement;

import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustImpure;
import static gregtech.api.enums.OrePrefixes.dustPure;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

/// Rewrites platinum-group outputs at recipe-generation time so that ore processing yields the sludge-line
/// intermediates (metallic powders and leach residues) instead of the pure metals.
///
/// Substitution happens as recipes are built, so a recipe registered against a platinum-group material never
/// exists in its unsubstituted form. Alloys that legitimately contain a platinum-group metal are exempt, see
/// [#isMaterialBlacklisted].
public final class PlatinumSludgeOutputs {

    private PlatinumSludgeOutputs() {}

    public static ItemStack[] convert(ItemStack... outputs) {
        for (int i = 0; i < outputs.length; i++) outputs[i] = convert(outputs[i]);
        return outputs;
    }

    public static ItemStack[] convert(Material inputMaterial, ItemStack... outputs) {
        return isMaterialBlacklisted(inputMaterial) ? outputs : convert(outputs);
    }

    public static ItemStack convertSmelting(Material inputMaterial, OrePrefixes inputPrefix, ItemStack output) {
        if (!GTUtility.isStackValid(output) || isMaterialBlacklisted(inputMaterial)) return output;
        if (inputMaterial == Materials.Platinum && (inputPrefix == dust || inputPrefix == dustTiny)) return output;

        ItemData association = GTOreDictUnificator.getAssociation(output);
        if (association == null || association.mPrefix == null || association.mMaterial == null) return output;
        Material replacement = association.mMaterial.mMaterial == Materials.Platinum ? Materials.PlatinumMetallicPowder
            : association.mMaterial.mMaterial == Materials.Palladium ? Materials.PalladiumMetallicPowder : null;
        return replacement == null ? output
            : MaterialLibAPI.getStack(
                replacement,
                association.mPrefix == OrePrefixes.nugget ? Shapes.dustTiny : Shapes.dust,
                Math.min(64, output.stackSize * 2));
    }

    public static ItemStack convertCrafting(Material material, ItemStack output) {
        if (!GTUtility.isStackValid(output)) return output;
        if (material == Materials.Platinum) return MaterialLibAPI
            .getStack(Materials.PlatinumMetallicPowder, Shapes.dust, Math.min(64, output.stackSize * 2));
        if (material == Materials.Palladium) return MaterialLibAPI
            .getStack(Materials.PalladiumMetallicPowder, Shapes.dust, Math.min(64, output.stackSize * 2));
        if (material == Materials.Iridium)
            return MaterialLibAPI.getStack(Materials.IridiumMetalResidue, Shapes.dust, output.stackSize);
        if (material == Materials.Osmium)
            return MaterialLibAPI.getStack(Materials.RarestMetalResidue, Shapes.dust, output.stackSize);
        return output;
    }

    public static ItemStack convert(ItemStack output) {
        if (!GTUtility.isStackValid(output)) return output;

        if (matchesDust(Materials.Ruthenium, output))
            return MaterialLibAPI.getStack(Materials.LeachResidue, Shapes.dust, Math.min(64, output.stackSize * 2));
        if (matchesDust(Materials.Rhodium, output)) return MaterialLibAPI
            .getStack(Materials.CrudeRhodiumMetal, Shapes.dust, Math.min(64, output.stackSize * 2));

        ItemData association = GTOreDictUnificator.getAssociation(output);
        if (association == null || association.mPrefix == null || association.mMaterial == null) return output;

        Material replacement;
        if (association.mMaterial.mMaterial == Materials.Platinum) replacement = Materials.PlatinumMetallicPowder;
        else if (association.mMaterial.mMaterial == Materials.Palladium)
            replacement = Materials.PalladiumMetallicPowder;
        else if (association.mMaterial.mMaterial == Materials.Iridium) replacement = Materials.IridiumMetalResidue;
        else if (association.mMaterial.mMaterial == Materials.Osmium) replacement = Materials.RarestMetalResidue;
        else return output;

        Shape shape;
        if (association.mPrefix == dust || association.mPrefix == dustImpure || association.mPrefix == dustPure) {
            shape = Shapes.dust;
        } else if (association.mPrefix == dustSmall) {
            shape = Shapes.dustSmall;
        } else if (association.mPrefix == dustTiny) {
            shape = Shapes.dustTiny;
        } else {
            return output;
        }
        return MaterialLibAPI.getStack(replacement, shape, Math.min(64, output.stackSize * 2));
    }

    private static boolean matchesDust(Material material, ItemStack stack) {
        for (Shape shape : new Shape[] { Shapes.dust, Shapes.dustImpure, Shapes.dustPure }) {
            if (material.hasShape(shape)
                && GTUtility.areStacksEqual(MaterialLibAPI.getStack(material, shape, 1), stack)) return true;
        }
        return false;
    }

    private static boolean isMaterialBlacklisted(Material material) {
        return material == Materials.HSSS || material == Materials.EnderiumBase
            || material == Materials.Osmiridium
            || material == Materials.TPVAlloy
            || material == Materials.Uraniumtriplatinid
            || material == Materials.Tetranaquadahdiindiumhexaplatiumosminid
            || material == Materials.Longasssuperconductornameforuvwire;
    }
}
