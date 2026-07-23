package gregtech.loaders.materials;

import java.util.EnumSet;
import java.util.function.Consumer;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.materials2.Materials2Markers;
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

    // spotless:off
    private record Superconductor(Consumer<Materials> field, Consumer<Material> backingField, String internalName, String localName, int argb) {}

    private static final Superconductor[] SUPERCONDUCTORS = {
        new Superconductor(m -> Materials.SuperconductorMV = m, m -> Materials2Markers.SuperconductorMV = m, "SuperconductorMV", "Superconductor MV", 0x00555555),
        new Superconductor(m -> Materials.SuperconductorHV = m, m -> Materials2Markers.SuperconductorHV = m, "SuperconductorHV", "Superconductor HV", 0x00331900),
        new Superconductor(m -> Materials.SuperconductorEV = m, m -> Materials2Markers.SuperconductorEV = m, "SuperconductorEV", "Superconductor EV", 0x00008700),
        new Superconductor(m -> Materials.SuperconductorIV = m, m -> Materials2Markers.SuperconductorIV = m, "SuperconductorIV", "Superconductor IV", 0x00330033),
        new Superconductor(m -> Materials.SuperconductorLuV = m, m -> Materials2Markers.SuperconductorLuV = m, "SuperconductorLuV", "Superconductor LuV", 0x00994c00),
        new Superconductor(m -> Materials.SuperconductorZPM = m, m -> Materials2Markers.SuperconductorZPM = m, "SuperconductorZPM", "Superconductor ZPM", 0x000a0a0a),
        new Superconductor(m -> Materials.SuperconductorUV = m, m -> Materials2Markers.SuperconductorUV = m, "SuperconductorUV", "Superconductor UV", 0x00e0d207),
        new Superconductor(m -> Materials.SuperconductorUHV = m, m -> Materials2Markers.SuperconductorUHV = m, "Superconductor", "Superconductor UHV", 0x002681bd),
        new Superconductor(m -> Materials.SuperconductorUEV = m, m -> Materials2Markers.SuperconductorUEV = m, "SuperconductorUEV", "Superconductor UEV", 0x00ae0808),
        new Superconductor(m -> Materials.SuperconductorUIV = m, m -> Materials2Markers.SuperconductorUIV = m, "SuperconductorUIV", "Superconductor UIV", 0x00e558b1),
        new Superconductor(m -> Materials.SuperconductorUMV = m, m -> Materials2Markers.SuperconductorUMV = m, "SuperconductorUMV", "Superconductor UMV", 0x00b526cd), };
    // spotless:on

    /// Registers a shapeless MaterialLib [Material] backing each superconductor facade, which `MU#material`
    /// resolves by registry name, and assigns the [Materials2Markers] fields from the same builders --
    /// `SuperconductorUHV`'s backing name is `Superconductor`, so binding by name lookup would be wrong. A
    /// backing whose internal name already names a MaterialLib material is not re-registered; the field binds
    /// that existing material, matching `MU#material`'s registry fallback. Runs during material registration,
    /// after [gregtech.api.enums.materials2.Materials2Materials#init], so the skip check sees every real
    /// material.
    public static void registerBackingMaterials() {
        for (Superconductor sc : SUPERCONDUCTORS) {
            Material existing = MaterialLibAPI.getMaterial("gregtech", sc.internalName());
            if (existing != null) {
                sc.backingField()
                    .accept(existing);
                continue;
            }
            sc.backingField()
                .accept(
                    MaterialLibAPI
                        .newMaterial(
                            "gregtech",
                            sc.internalName(),
                            com.ruling_0.materiallib.api.TextureSet.of("gregtech", TextureSet.SET_SHINY.mSetName))
                        .setProperty(GTMaterialProperties.LEGACY_NAME, sc.internalName())
                        .setProperty(GTMaterialProperties.LOCAL_NAME, sc.localName())
                        .setProperty(GTMaterialProperties.ARGB, sc.argb())
                        .build());
        }
        if (Materials2Markers.SuperconductorUHV == null
            || !"Superconductor".equals(Materials2Markers.SuperconductorUHV.getName())) {
            throw new IllegalStateException("SuperconductorUHV must bind the \"Superconductor\" backing material");
        }
        Materials2Markers.AnyBronze = registerWildcard(
            "AnyBronze",
            "AnyBronze",
            TextureSet.SET_SHINY,
            GTMaterialFlag.METAL,
            null,
            null,
            null);
        Materials2Markers.AnyCopper = registerWildcard(
            "AnyCopper",
            "AnyCopper",
            TextureSet.SET_SHINY,
            GTMaterialFlag.METAL,
            "Copper",
            "Copper",
            "AnnealedCopper");
        Materials2Markers.AnyCarbon = registerWildcard(
            "AnyCarbon",
            "AnyCarbon",
            TextureSet.SET_DULL,
            null,
            null,
            null,
            null);
        Materials2Markers.AnyIron = registerWildcard(
            "AnyIron",
            "AnyIron",
            TextureSet.SET_SHINY,
            GTMaterialFlag.METAL,
            "Iron",
            "Iron",
            null);
        Materials2Markers.AnyRubber = registerWildcard(
            "AnyRubber",
            "AnyRubber",
            TextureSet.SET_SHINY,
            null,
            null,
            "Rubber",
            null);
        Materials2Markers.AnySyntheticRubber = registerWildcard(
            "AnySyntheticRubber",
            "AnySyntheticRubber",
            TextureSet.SET_SHINY,
            null,
            null,
            null,
            null);
    }

    /// Registers a shapeless MaterialLib backing for a wildcard `Materials` (`AnyCopper`, `AnyIron`, ...), which
    /// `MU#material` resolves by [GTMaterialProperties#LEGACY_NAME]. Ports the smelt/macerate/arc targets, the
    /// metal flag, and the `setUnifiable(false)` every wildcard facade carries; a name that already names a
    /// real MaterialLib material is returned as-is instead of re-registered.
    private static Material registerWildcard(String name, String localName, TextureSet texture, GTMaterialFlag flag,
        String smeltInto, String macerateInto, String arcSmeltInto) {
        Material existing = MaterialLibAPI.getMaterial("gregtech", name);
        if (existing != null) return existing;
        com.ruling_0.materiallib.api.MaterialBuilder builder = MaterialLibAPI
            .newMaterial("gregtech", name, com.ruling_0.materiallib.api.TextureSet.of("gregtech", texture.mSetName))
            .setProperty(GTMaterialProperties.LEGACY_NAME, name)
            .setProperty(GTMaterialProperties.LOCAL_NAME, localName)
            .setProperty(GTMaterialProperties.ARGB, DEFAULT_ARGB)
            .setProperty(GTMaterialProperties.UNIFIABLE, false);
        if (flag != null) builder = builder.setProperty(GTMaterialProperties.FLAGS, EnumSet.of(flag));
        if (smeltInto != null)
            builder = builder.setProperty(GTMaterialProperties.SMELT_INTO, new MaterialRef(smeltInto));
        if (macerateInto != null)
            builder = builder.setProperty(GTMaterialProperties.MACERATE_INTO, new MaterialRef(macerateInto));
        if (arcSmeltInto != null)
            builder = builder.setProperty(GTMaterialProperties.ARC_SMELT_INTO, new MaterialRef(arcSmeltInto));
        return builder.build();
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
