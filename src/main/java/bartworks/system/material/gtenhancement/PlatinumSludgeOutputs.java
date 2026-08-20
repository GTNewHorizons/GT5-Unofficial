package bartworks.system.material.gtenhancement;

import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustImpure;
import static gregtech.api.enums.OrePrefixes.dustPure;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
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
            : GTOreDictUnificator
                .get(association.mPrefix == OrePrefixes.nugget ? dustTiny : dust, replacement, output.stackSize * 2L);
    }

    public static ItemStack convertCrafting(Material material, ItemStack output) {
        if (!GTUtility.isStackValid(output)) return output;
        if (material == Materials.Platinum)
            return GTOreDictUnificator.get(dust, Materials.PlatinumMetallicPowder, output.stackSize * 2L);
        if (material == Materials.Palladium)
            return GTOreDictUnificator.get(dust, Materials.PalladiumMetallicPowder, output.stackSize * 2L);
        if (material == Materials.Iridium)
            return GTOreDictUnificator.get(dust, Materials.IridiumMetalResidue, output.stackSize);
        if (material == Materials.Osmium)
            return GTOreDictUnificator.get(dust, Materials.RarestMetalResidue, output.stackSize);
        return output;
    }

    public static ItemStack convert(ItemStack output) {
        if (!GTUtility.isStackValid(output)) return output;

        if (matchesDust(Materials.Ruthenium, output))
            return GTOreDictUnificator.get(dust, Materials.LeachResidue, output.stackSize * 2L);
        if (matchesDust(Materials.Rhodium, output))
            return GTOreDictUnificator.get(dust, Materials.CrudeRhodiumMetal, output.stackSize * 2L);

        ItemData association = GTOreDictUnificator.getAssociation(output);
        if (association == null || association.mPrefix == null || association.mMaterial == null) return output;

        Material replacement;
        if (association.mMaterial.mMaterial == Materials.Platinum) replacement = Materials.PlatinumMetallicPowder;
        else if (association.mMaterial.mMaterial == Materials.Palladium)
            replacement = Materials.PalladiumMetallicPowder;
        else if (association.mMaterial.mMaterial == Materials.Iridium) replacement = Materials.IridiumMetalResidue;
        else if (association.mMaterial.mMaterial == Materials.Osmium) replacement = Materials.RarestMetalResidue;
        else return output;

        OrePrefixes prefix;
        if (association.mPrefix == dust || association.mPrefix == dustImpure || association.mPrefix == dustPure) {
            prefix = dust;
        } else if (association.mPrefix == dustSmall) {
            prefix = dustSmall;
        } else if (association.mPrefix == dustTiny) {
            prefix = dustTiny;
        } else {
            return output;
        }
        return GTOreDictUnificator.get(prefix, replacement, output.stackSize * 2L);
    }

    private static boolean matchesDust(Material material, ItemStack stack) {
        return GTUtility.areStacksEqual(GTOreDictUnificator.get(dust, material, 1L), stack)
            || GTUtility.areStacksEqual(GTOreDictUnificator.get(dustImpure, material, 1L), stack)
            || GTUtility.areStacksEqual(GTOreDictUnificator.get(dustPure, material, 1L), stack);
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
