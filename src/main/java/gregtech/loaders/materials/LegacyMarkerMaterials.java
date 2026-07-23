package gregtech.loaders.materials;

import java.util.EnumSet;
import java.util.function.Consumer;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialRef;

/// `Materials` fields that MaterialLib carries no data for and that unchanged code still references
/// directly: the superconductor markers (`SuperconductorMV`..`SuperconductorUMV`) and wildcard markers
/// (`AnyBronze`/`AnyCopper`/`AnyCarbon`/`AnyIron`/`AnyRubber`/`AnySyntheticRubber`, used by
/// `Materials#setReRegistration`) are reproduced here through `MaterialBuilder`.
///
/// [RecognitionMaterials] separately builds the markers that exist so `Materials.get(name)` resolves other
/// mods' ore-dictionary entries and legacy names, and that carry ore-unification identity or composition
/// association for the rest of the code.
public class LegacyMarkerMaterials {

    private static final int DEFAULT_ARGB = 0x00ffffff;

    private LegacyMarkerMaterials() {}

    public static void loadMarkers() {
        loadRandomMarkers();
        loadSuperconductorsMarkers();
    }

    private record Superconductor(Consumer<Materials> field, String internalName, String localName, int argb) {}

    private static final Superconductor[] SUPERCONDUCTORS = {
        new Superconductor(m -> Materials.SuperconductorMV = m, "SuperconductorMV", "Superconductor MV", 0x00555555),
        new Superconductor(m -> Materials.SuperconductorHV = m, "SuperconductorHV", "Superconductor HV", 0x00331900),
        new Superconductor(m -> Materials.SuperconductorEV = m, "SuperconductorEV", "Superconductor EV", 0x00008700),
        new Superconductor(m -> Materials.SuperconductorIV = m, "SuperconductorIV", "Superconductor IV", 0x00330033),
        new Superconductor(m -> Materials.SuperconductorLuV = m, "SuperconductorLuV", "Superconductor LuV", 0x00994c00),
        new Superconductor(m -> Materials.SuperconductorZPM = m, "SuperconductorZPM", "Superconductor ZPM", 0x000a0a0a),
        new Superconductor(m -> Materials.SuperconductorUV = m, "SuperconductorUV", "Superconductor UV", 0x00e0d207),
        new Superconductor(m -> Materials.SuperconductorUHV = m, "Superconductor", "Superconductor UHV", 0x002681bd),
        new Superconductor(m -> Materials.SuperconductorUEV = m, "SuperconductorUEV", "Superconductor UEV", 0x00ae0808),
        new Superconductor(m -> Materials.SuperconductorUIV = m, "SuperconductorUIV", "Superconductor UIV", 0x00e558b1),
        new Superconductor(
            m -> Materials.SuperconductorUMV = m,
            "SuperconductorUMV",
            "Superconductor UMV",
            0x00b526cd), };

    /// Registers a shapeless MaterialLib [Material] backing each superconductor facade, which `MU#material`
    /// resolves by registry name; skips any whose internal name already names a MaterialLib material. Runs
    /// during material registration, after [gregtech.api.enums.materials2.Materials2Materials#init], so the
    /// skip check sees every real material.
    public static void registerBackingMaterials() {
        for (Superconductor sc : SUPERCONDUCTORS) {
            if (MaterialLibAPI.getMaterial("gregtech", sc.internalName()) != null) continue;
            MaterialLibAPI
                .newMaterial(
                    "gregtech",
                    sc.internalName(),
                    com.ruling_0.materiallib.api.TextureSet.of("gregtech", TextureSet.SET_SHINY.mSetName))
                .setProperty(GTMaterialProperties.LEGACY_NAME, sc.internalName())
                .setProperty(GTMaterialProperties.LOCAL_NAME, sc.localName())
                .setProperty(GTMaterialProperties.ARGB, sc.argb())
                .build();
        }
        registerWildcard("AnyBronze", "AnyBronze", TextureSet.SET_SHINY, GTMaterialFlag.METAL, null, null, null);
        registerWildcard(
            "AnyCopper",
            "AnyCopper",
            TextureSet.SET_SHINY,
            GTMaterialFlag.METAL,
            "Copper",
            "Copper",
            "AnnealedCopper");
        registerWildcard("AnyCarbon", "AnyCarbon", TextureSet.SET_DULL, null, null, null, null);
        registerWildcard("AnyIron", "AnyIron", TextureSet.SET_SHINY, GTMaterialFlag.METAL, "Iron", "Iron", null);
        registerWildcard("AnyRubber", "AnyRubber", TextureSet.SET_SHINY, null, null, "Rubber", null);
        registerWildcard("AnySyntheticRubber", "AnySyntheticRubber", TextureSet.SET_SHINY, null, null, null, null);
    }

    /// Registers a shapeless MaterialLib backing for a wildcard `Materials` (`AnyCopper`, `AnyIron`, ...), which
    /// `MU#material` resolves by [GTMaterialProperties#LEGACY_NAME]. Ports the smelt/macerate/arc targets and
    /// metal flag the facade carried; skips any whose name already names a real MaterialLib material.
    private static void registerWildcard(String name, String localName, TextureSet texture, GTMaterialFlag flag,
        String smeltInto, String macerateInto, String arcSmeltInto) {
        if (MaterialLibAPI.getMaterial("gregtech", name) != null) return;
        com.ruling_0.materiallib.api.MaterialBuilder builder = MaterialLibAPI
            .newMaterial("gregtech", name, com.ruling_0.materiallib.api.TextureSet.of("gregtech", texture.mSetName))
            .setProperty(GTMaterialProperties.LEGACY_NAME, name)
            .setProperty(GTMaterialProperties.LOCAL_NAME, localName)
            .setProperty(GTMaterialProperties.ARGB, DEFAULT_ARGB);
        if (flag != null) builder = builder.setProperty(GTMaterialProperties.FLAGS, EnumSet.of(flag));
        if (smeltInto != null)
            builder = builder.setProperty(GTMaterialProperties.SMELT_INTO, new MaterialRef(smeltInto));
        if (macerateInto != null)
            builder = builder.setProperty(GTMaterialProperties.MACERATE_INTO, new MaterialRef(macerateInto));
        if (arcSmeltInto != null)
            builder = builder.setProperty(GTMaterialProperties.ARC_SMELT_INTO, new MaterialRef(arcSmeltInto));
        builder.build();
    }

    /// The superconductor wire markers, in tier order. They are absent from [Materials#getMaterialsMap], so
    /// the material lang-registration pass in [gregtech.loaders.preload.GTPreLoad] skips them; their
    /// display-name keys are registered from this array instead. Evaluated after
    /// [#loadSuperconductorsMarkers] has populated the fields.
    public static Materials[] getSuperconductorMarkers() {
        return new Materials[] { Materials.SuperconductorMV, Materials.SuperconductorHV, Materials.SuperconductorEV,
            Materials.SuperconductorIV, Materials.SuperconductorLuV, Materials.SuperconductorZPM,
            Materials.SuperconductorUV, Materials.SuperconductorUHV, Materials.SuperconductorUEV,
            Materials.SuperconductorUIV, Materials.SuperconductorUMV };
    }

    private static void loadRandomMarkers() {
        Materials.AnyBronze = loadAnyBronze();
        Materials.AnyCopper = loadAnyCopper();
        Materials.AnyCarbon = loadAnyCarbon();
        Materials.AnyIron = loadAnyIron();
        Materials.AnyRubber = loadAnyRubber();
        Materials.AnySyntheticRubber = loadAnySyntheticRubber();
    }

    private static Materials loadAnyBronze() {
        return new MaterialBuilder().setName("AnyBronze")
            .setDefaultLocalName("AnyBronze")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_SHINY)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadAnyCopper() {
        return new MaterialBuilder().setName("AnyCopper")
            .setDefaultLocalName("AnyCopper")
            .setChemicalFormula("Cu")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_SHINY)
            .setSmeltingInto(() -> Materials.Copper)
            .setMaceratingInto(() -> Materials.Copper)
            .setArcSmeltingIntoWithGas(() -> Materials.Oxygen, () -> Materials.AnnealedCopper)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadAnyCarbon() {
        return new MaterialBuilder().setName("AnyCarbon")
            .setDefaultLocalName("AnyCarbon")
            .setChemicalFormula("C")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_DULL)
            .constructMaterial();
    }

    private static Materials loadAnyIron() {
        return new MaterialBuilder().setName("AnyIron")
            .setDefaultLocalName("AnyIron")
            .setChemicalFormula("Fe")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_SHINY)
            .setSmeltingInto(() -> Materials.Iron)
            .setMaceratingInto(() -> Materials.Iron)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadAnyRubber() {
        return new MaterialBuilder().setName("AnyRubber")
            .setDefaultLocalName("AnyRubber")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_SHINY)
            .setMaceratingInto(() -> Materials.Rubber)
            .constructMaterial();
    }

    private static Materials loadAnySyntheticRubber() {
        return new MaterialBuilder().setName("AnySyntheticRubber")
            .setDefaultLocalName("AnySyntheticRubber")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_SHINY)
            .constructMaterial();
    }

    /// Builds the superconductor facades. They stay out of [Materials#getMaterialsMap] so `Materials.get`
    /// resolution, registry-wide iteration, and the lang pass over the map never see them;
    /// [gregtech.loaders.preload.GTPreLoad] registers their display names from [#getSuperconductorMarkers]
    /// instead, and `MU#toMaterial` resolves them to their [#registerBackingMaterials] backing by name.
    private static void loadSuperconductorsMarkers() {
        for (Superconductor sc : SUPERCONDUCTORS) {
            sc.field()
                .accept(
                    new MaterialBuilder().setName(sc.internalName())
                        .setDefaultLocalName(sc.localName())
                        .setIconSet(TextureSet.SET_SHINY)
                        .setARGB(sc.argb())
                        .setRegisterInMaterialsMap(false)
                        .constructMaterial());
        }
    }
}
