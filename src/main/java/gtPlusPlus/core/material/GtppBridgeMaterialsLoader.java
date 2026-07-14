package gtPlusPlus.core.material;

import gregtech.api.enchants.EnchantmentRadioactivity;
import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;

/// Bridges a pure gtPlusPlus [MaterialReconstruction]-owned material (one with no live [Materials] equivalent --
/// see [Material#tryFindGregtechMaterialEquivalent]) into the legacy [Materials] map, the gtpp counterpart of
/// bartworks' `BridgeMaterialsLoader`: constructing a [MaterialBuilder] material with none of its
/// `addXItems()`/oredict-generating calls registers it into [Materials#getMaterialsMap()] (every `Materials`
/// constructor call does, see that map's `MATERIALS_MAP.put`) without generating a single legacy item or
/// oredict entry, so [gregtech.api.material.MU#materialOf] resolves it while
/// `gregtech.loaders.shapeconsumers.ShapeConsumerSupport` still dispatches every recipe through MaterialLib's own
/// items exclusively.
///
/// Called from [MaterialReconstruction#build] once per reconstructed material, immediately after the gregtech-
/// equivalent check finds nothing -- the same call site that seeds the ~21 name-merge materials onto their live
/// [Materials] constant instead, so a name-merge material never reaches here and never gets a second bridge.
/// That call site runs during gtPlusPlus's own class-loading of the `Materials*` pool declarations (`init`
/// phase), after {@link gregtech.api.enums.Materials#init()} (`gregtech`'s `preInit`) has already frozen
/// `Materials.values()` -- exactly the timing bartworks' bridge runs under (its own `preInit`, also after
/// gregtech's) -- so every field a once-only `Materials.init()` pass would otherwise have populated
/// (handle material, tool enchantments) is set here explicitly instead of relying on that pass to see this
/// material.
final class GtppBridgeMaterialsLoader {

    private GtppBridgeMaterialsLoader() {}

    static Materials register(com.ruling_0.materiallib.api.Material ml, String legacyName, String localName,
        TextureSet textureSet, short[] rgba, Material.GtppScalars scalars, Material material) {
        int argb = (rgba[3] & 0xff) << 24 | (rgba[0] & 0xff) << 16 | (rgba[1] & 0xff) << 8 | rgba[2] & 0xff;

        Materials bridge = new MaterialBuilder().setName(legacyName)
            .setDefaultLocalName(localName)
            .setIconSet(textureSet)
            .setARGB(argb)
            .setTool(scalars.durability(), 0, 1.0f)
            .setMeltingPoint(scalars.meltingPointK())
            .setBlastFurnaceTemp(scalars.meltingPointK())
            .setBlastFurnaceRequired(scalars.usesBlastFurnace())
            .constructMaterial();

        if (MU.isCutOver(OrePrefixes.cell, ml)) {
            bridge.setHasCorrespondingFluid(true);
            bridge.setHasCorrespondingGas(true);
            bridge.mFluid = material.getFluid();
            bridge.mGas = material.getFluid();
        }
        if (MU.isCutOver(OrePrefixes.cellMolten, ml)) {
            bridge.mStandardMoltenFluid = material.getFluid();
        }
        if (MU.isCutOver(OrePrefixes.cellPlasma, ml)) {
            bridge.mPlasma = material.getPlasma();
        }

        bridge.setChemicalFormula(scalars.chemicalFormula(), false);

        Integer tierEU = ml.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
        if (tierEU != null) bridge.setProcessingMaterialTierEU(tierEU);

        bridge.mHandleMaterial = handleMaterial(ml, scalars.durability());

        if (scalars.isRadioactive()) {
            bridge.setEnchantmentForArmors(EnchantmentRadioactivity.INSTANCE, scalars.radiationLevel());
            bridge.setEnchantmentForTools(EnchantmentRadioactivity.INSTANCE, scalars.radiationLevel());
        }

        return bridge;
    }

    private static Materials handleMaterial(com.ruling_0.materiallib.api.Material ml, int durability) {
        if (MU.hasFlag(ml, GTMaterialFlag.BURNING)) return Materials.Blaze;
        if (MU.hasFlag(ml, GTMaterialFlag.MAGICAL)) return Materials.Thaumium;
        if (durability > 5120) return Materials.TungstenSteel;
        if (durability > 1280) return Materials.Steel;
        return Materials.Wood;
    }
}
