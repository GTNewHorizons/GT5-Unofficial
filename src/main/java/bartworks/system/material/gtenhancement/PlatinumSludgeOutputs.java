package bartworks.system.material.gtenhancement;

import static bartworks.system.material.WerkstoffLoader.CrudeRhMetall;
import static bartworks.system.material.WerkstoffLoader.IrLeachResidue;
import static bartworks.system.material.WerkstoffLoader.IrOsLeachResidue;
import static bartworks.system.material.WerkstoffLoader.LeachResidue;
import static bartworks.system.material.WerkstoffLoader.PDMetallicPowder;
import static bartworks.system.material.WerkstoffLoader.PTMetallicPowder;
import static bartworks.system.material.WerkstoffLoader.Rhodium;
import static bartworks.system.material.WerkstoffLoader.Ruthenium;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustImpure;
import static gregtech.api.enums.OrePrefixes.dustPure;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;

import net.minecraft.item.ItemStack;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public final class PlatinumSludgeOutputs {

    private PlatinumSludgeOutputs() {}

    public static ItemStack[] convert(ItemStack... outputs) {
        for (int i = 0; i < outputs.length; i++) outputs[i] = convert(outputs[i]);
        return outputs;
    }

    public static ItemStack[] convert(Materials inputMaterial, ItemStack... outputs) {
        return isMaterialBlacklisted(inputMaterial) ? outputs : convert(outputs);
    }

    public static ItemStack convertSmelting(Materials inputMaterial, OrePrefixes inputPrefix, ItemStack output) {
        if (!GTUtility.isStackValid(output) || isMaterialBlacklisted(inputMaterial)) return output;
        if (inputMaterial == Materials.Platinum && (inputPrefix == dust || inputPrefix == dustTiny)) return output;

        ItemData association = GTOreDictUnificator.getAssociation(output);
        if (association == null || association.mPrefix == null || association.mMaterial == null) return output;
        Werkstoff replacement = association.mMaterial.mMaterial == Materials.Platinum ? PTMetallicPowder
            : association.mMaterial.mMaterial == Materials.Palladium ? PDMetallicPowder : null;
        return replacement == null ? output
            : replacement.get(association.mPrefix == OrePrefixes.nugget ? dustTiny : dust, output.stackSize * 2);
    }

    public static ItemStack convertCrafting(Materials material, ItemStack output) {
        if (!GTUtility.isStackValid(output)) return output;
        if (material == Materials.Platinum) return PTMetallicPowder.get(dust, output.stackSize * 2);
        if (material == Materials.Palladium) return PDMetallicPowder.get(dust, output.stackSize * 2);
        if (material == Materials.Iridium) return IrLeachResidue.get(dust, output.stackSize);
        if (material == Materials.Osmium) return IrOsLeachResidue.get(dust, output.stackSize);
        return output;
    }

    public static ItemStack convert(ItemStack output) {
        if (!GTUtility.isStackValid(output)) return output;

        if (matchesDust(Ruthenium, output)) return LeachResidue.get(dust, output.stackSize * 2);
        if (matchesDust(Rhodium, output)) return CrudeRhMetall.get(dust, output.stackSize * 2);

        ItemData association = GTOreDictUnificator.getAssociation(output);
        if (association == null || association.mPrefix == null || association.mMaterial == null) return output;

        Werkstoff replacement = association.mMaterial.mMaterial == Materials.Platinum ? PTMetallicPowder
            : association.mMaterial.mMaterial == Materials.Palladium ? PDMetallicPowder
                : association.mMaterial.mMaterial == Materials.Iridium ? IrLeachResidue
                    : association.mMaterial.mMaterial == Materials.Osmium ? IrOsLeachResidue : null;
        OrePrefixes prefix = association.mPrefix == dust || association.mPrefix == dustImpure
            || association.mPrefix == dustPure ? dust
                : association.mPrefix == dustSmall ? dustSmall : association.mPrefix == dustTiny ? dustTiny : null;
        return replacement == null || prefix == null ? output : replacement.get(prefix, output.stackSize * 2);
    }

    private static boolean matchesDust(Werkstoff material, ItemStack stack) {
        return GTUtility.areStacksEqual(material.get(dust), stack)
            || GTUtility.areStacksEqual(material.get(dustImpure), stack)
            || GTUtility.areStacksEqual(material.get(dustPure), stack);
    }

    private static boolean isMaterialBlacklisted(Materials material) {
        return material == Materials.HSSS || material == Materials.EnderiumBase
            || material == Materials.Osmiridium
            || material == Materials.TPV
            || material == Materials.SuperconductorEVBase
            || material == Materials.SuperconductorZPMBase
            || material == Materials.SuperconductorUVBase;
    }
}
