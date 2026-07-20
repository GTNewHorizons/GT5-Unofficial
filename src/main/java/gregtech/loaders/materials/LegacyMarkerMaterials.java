package gregtech.loaders.materials;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TextureSet;
import gregtech.api.material.MarkerMaterial;

/// `Materials` fields that MaterialLib carries no data for and that unchanged code still references
/// directly: the superconductor markers (`SuperconductorMV`..`SuperconductorUMV`) and wildcard markers
/// (`AnyBronze`/`AnyCopper`/`AnyCarbon`/`AnyIron`/`AnyRubber`/`AnySyntheticRubber`, used by
/// `Materials#setReRegistration`) are reproduced here through `MaterialBuilder`. The voltage-tier markers
/// (`ULV`..`MAX`) and circuit-component markers (`Resistor`, `Diode`, `Transistor`, `Capacitor`, `Inductor`)
/// name their ore-dictionary entry and nothing more, so they are plain [MarkerMaterial]s.
///
/// [RecognitionMaterials] separately builds the markers that exist so `Materials.get(name)` resolves other
/// mods' ore-dictionary entries and legacy names, and that carry ore-unification identity or composition
/// association for the rest of the code.
public class LegacyMarkerMaterials {

    private static final int DEFAULT_ARGB = 0x00ffffff;

    private LegacyMarkerMaterials() {}

    public static void loadMarkers() {
        loadRandomMarkers();
        loadTiersMarkers();
        loadCircuitryMarkers();
        loadSuperconductorsMarkers();
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

    private static void loadTiersMarkers() {
        Materials.ULV = new MarkerMaterial("Primitive", "Primitive", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.LV = new MarkerMaterial("Basic", "Basic", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.MV = new MarkerMaterial("Good", "Good", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.HV = new MarkerMaterial("Advanced", "Advanced", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.EV = new MarkerMaterial("Data", "Data", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.IV = new MarkerMaterial("Elite", "Elite", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.LuV = new MarkerMaterial("Master", "Master", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.ZPM = new MarkerMaterial("Ultimate", "Ultimate", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.UV = new MarkerMaterial("Superconductor", "Superconductor", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.UHV = new MarkerMaterial("Infinite", "Infinite", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.UEV = new MarkerMaterial("Bio", "Bio", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.UIV = new MarkerMaterial("Optical", "Optical", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.UMV = new MarkerMaterial("Exotic", "Exotic", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.UXV = new MarkerMaterial("Cosmic", "Cosmic", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.MAX = new MarkerMaterial("Transcendent", "Transcendent", TextureSet.SET_NONE, DEFAULT_ARGB);
    }

    private static void loadCircuitryMarkers() {
        Materials.Resistor = new MarkerMaterial("Resistor", "Resistor", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.Diode = new MarkerMaterial("Diode", "Diode", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.Transistor = new MarkerMaterial("Transistor", "Transistor", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.Capacitor = new MarkerMaterial("Capacitor", "Capacitor", TextureSet.SET_NONE, DEFAULT_ARGB);
        Materials.Inductor = new MarkerMaterial("Inductor", "Inductor", TextureSet.SET_NONE, DEFAULT_ARGB);
    }

    private static void loadSuperconductorsMarkers() {
        Materials.SuperconductorMV = loadSuperconductorMV();
        Materials.SuperconductorHV = loadSuperconductorHV();
        Materials.SuperconductorEV = loadSuperconductorEV();
        Materials.SuperconductorIV = loadSuperconductorIV();
        Materials.SuperconductorLuV = loadSuperconductorLuV();
        Materials.SuperconductorZPM = loadSuperconductorZPM();
        Materials.SuperconductorUV = loadSuperconductorUV();
        Materials.SuperconductorUHV = loadSuperconductorUHV();
        Materials.SuperconductorUEV = loadSuperconductorUEV();
        Materials.SuperconductorUIV = loadSuperconductorUIV();
        Materials.SuperconductorUMV = loadSuperconductorUMV();
    }

    private static Materials loadSuperconductorMV() {
        return new MaterialBuilder().setName("SuperconductorMV")
            .setDefaultLocalName("Superconductor MV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00555555)
            .addAspect(TCAspects.ELECTRUM, 6)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorHV() {
        return new MaterialBuilder().setName("SuperconductorHV")
            .setDefaultLocalName("Superconductor HV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00331900)
            .addAspect(TCAspects.ELECTRUM, 12)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorEV() {
        return new MaterialBuilder().setName("SuperconductorEV")
            .setDefaultLocalName("Superconductor EV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00008700)
            .addAspect(TCAspects.ELECTRUM, 18)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorIV() {
        return new MaterialBuilder().setName("SuperconductorIV")
            .setDefaultLocalName("Superconductor IV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeMagenta)
            .setARGB(0x00330033)
            .addAspect(TCAspects.ELECTRUM, 24)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorLuV() {
        return new MaterialBuilder().setName("SuperconductorLuV")
            .setDefaultLocalName("Superconductor LuV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00994c00)
            .addAspect(TCAspects.ELECTRUM, 30)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorZPM() {
        return new MaterialBuilder().setName("SuperconductorZPM")
            .setDefaultLocalName("Superconductor ZPM")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000a0a0a)
            .addAspect(TCAspects.ELECTRUM, 36)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorUV() {
        return new MaterialBuilder().setName("SuperconductorUV")
            .setDefaultLocalName("Superconductor UV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00e0d207)
            .addAspect(TCAspects.ELECTRUM, 42)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorUHV() {
        return new MaterialBuilder().setName("Superconductor")
            .setDefaultLocalName("Superconductor UHV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x002681bd)
            .addAspect(TCAspects.ELECTRUM, 48)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorUEV() {
        return new MaterialBuilder().setName("SuperconductorUEV")
            .setDefaultLocalName("Superconductor UEV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00ae0808)
            .addAspect(TCAspects.ELECTRUM, 54)
            .constructMaterial();
    }

    private static Materials loadSuperconductorUIV() {
        return new MaterialBuilder().setName("SuperconductorUIV")
            .setDefaultLocalName("Superconductor UIV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00e558b1)
            .addAspect(TCAspects.ELECTRUM, 60)
            .constructMaterial();
    }

    private static Materials loadSuperconductorUMV() {
        return new MaterialBuilder().setName("SuperconductorUMV")
            .setDefaultLocalName("Superconductor UMV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00b526cd)
            .addAspect(TCAspects.ELECTRUM, 66)
            .constructMaterial();
    }
}
