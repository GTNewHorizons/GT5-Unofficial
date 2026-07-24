package gregtech.loaders.materials;

import java.util.function.Consumer;

import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;

/// `Materials` fields that MaterialLib carries no data for and that unchanged code still references
/// directly: the superconductor markers (`SuperconductorMV`..`SuperconductorUMV`) and wildcard markers
/// (`AnyBronze`/`AnyCopper`/`AnyCarbon`/`AnyIron`/`AnyRubber`/`AnySyntheticRubber`, used by
/// `Materials#setReRegistration`) are reproduced here through `MaterialBuilder`.
///
/// The MaterialLib-backed twins of these markers -- the shapeless backings the rest of the code references
/// through [gregtech.api.enums.materials2.Materials2Markers] -- are registered by
/// [gregtech.api.enums.materials2.Materials2Markers#registerBackingMaterials]. [RecognitionMaterials]
/// separately builds the markers that exist so `Materials.get(name)` resolves other mods' ore-dictionary
/// entries and legacy names, and that carry ore-unification identity or composition association for the rest
/// of the code.
public class LegacyMarkerMaterials {

    private LegacyMarkerMaterials() {}

    public static void loadMarkers() {
        loadRandomMarkers();
        loadSuperconductorsMarkers();
    }

    // spotless:off
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
        new Superconductor(m -> Materials.SuperconductorUMV = m, "SuperconductorUMV", "Superconductor UMV", 0x00b526cd), };
    // spotless:on

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
    /// [gregtech.loaders.preload.GTPreLoad] registers their display names from
    /// [gregtech.api.enums.materials2.Materials2Markers#getSuperconductorMarkers] instead, and `MU#toMaterial`
    /// resolves them to their [gregtech.api.enums.materials2.Materials2Markers#registerBackingMaterials]
    /// backing by name.
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
