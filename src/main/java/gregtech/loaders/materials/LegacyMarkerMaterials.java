package gregtech.loaders.materials;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TextureSet;

/// `Materials` fields that MaterialLib carries no data for and that unchanged code still references
/// directly: the voltage-tier markers (`ULV`..`MAX`), circuit-component markers (`Resistor`, `Diode`,
/// `Transistor`, `Capacitor`, `Inductor`), superconductor markers (`SuperconductorMV`..`SuperconductorUMV`),
/// and wildcard markers (`AnyBronze`/`AnyCopper`/`AnyCarbon`/`AnyIron`/`AnyRubber`/`AnySyntheticRubber`, used
/// by `Materials#setReRegistration`). Each field's `MaterialBuilder` declaration is reproduced here.
///
/// [RecognitionMaterials] separately builds the markers that exist so `Materials.get(name)` resolves other
/// mods' ore-dictionary entries and legacy names, and that carry ore-unification identity or composition
/// association for the rest of the code.
public class LegacyMarkerMaterials {

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
        Materials.ULV = loadULV();
        Materials.LV = loadLV();
        Materials.MV = loadMV();
        Materials.HV = loadHV();
        Materials.EV = loadEV();
        Materials.IV = loadIV();
        Materials.LuV = loadLuV();
        Materials.ZPM = loadZPM();
        Materials.UV = loadUV();
        Materials.UHV = loadUHV();
        Materials.UEV = loadUEV();
        Materials.UIV = loadUIV();
        Materials.UMV = loadUMV();
        Materials.UXV = loadUXV();
        Materials.MAX = loadMAX();
    }

    private static Materials loadULV() {
        return new MaterialBuilder().setName("Primitive")
            .setDefaultLocalName("Primitive")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 1)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadLV() {
        return new MaterialBuilder().setName("Basic")
            .setDefaultLocalName("Basic")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 2)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadMV() {
        return new MaterialBuilder().setName("Good")
            .setDefaultLocalName("Good")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 3)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadHV() {
        return new MaterialBuilder().setName("Advanced")
            .setDefaultLocalName("Advanced")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 4)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadEV() {
        return new MaterialBuilder().setName("Data")
            .setDefaultLocalName("Data")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 5)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadIV() {
        return new MaterialBuilder().setName("Elite")
            .setDefaultLocalName("Elite")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 6)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadLuV() {
        return new MaterialBuilder().setName("Master")
            .setDefaultLocalName("Master")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 7)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadZPM() {
        return new MaterialBuilder().setName("Ultimate")
            .setDefaultLocalName("Ultimate")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 8)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadUV() {
        return new MaterialBuilder().setName("Superconductor")
            .setDefaultLocalName("Superconductor")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 9)
            .constructMaterial();
    }

    private static Materials loadUHV() {
        return new MaterialBuilder().setName("Infinite")
            .setDefaultLocalName("Infinite")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 10)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadUEV() {
        return new MaterialBuilder().setName("Bio")
            .setDefaultLocalName("Bio")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 11)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadUIV() {
        return new MaterialBuilder().setName("Optical")
            .setDefaultLocalName("Optical")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 12)
            .constructMaterial();
    }

    private static Materials loadUMV() {
        return new MaterialBuilder().setName("Exotic")
            .setDefaultLocalName("Exotic")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 13)
            .constructMaterial();
    }

    private static Materials loadUXV() {
        return new MaterialBuilder().setName("Cosmic")
            .setDefaultLocalName("Cosmic")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 14)
            .constructMaterial();
    }

    private static Materials loadMAX() {
        return new MaterialBuilder().setName("Transcendent")
            .setDefaultLocalName("Transcendent")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 15)
            .constructMaterial();
    }

    private static void loadCircuitryMarkers() {
        Materials.Resistor = loadResistor();
        Materials.Diode = loadDiode();
        Materials.Transistor = loadTransistor();
        Materials.Capacitor = loadCapacitor();
        Materials.Inductor = loadInductor();
    }

    private static Materials loadResistor() {
        return new MaterialBuilder().setName("Resistor")
            .setDefaultLocalName("Resistor")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadDiode() {
        return new MaterialBuilder().setName("Diode")
            .setDefaultLocalName("Diode")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadTransistor() {
        return new MaterialBuilder().setName("Transistor")
            .setDefaultLocalName("Transistor")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadCapacitor() {
        return new MaterialBuilder().setName("Capacitor")
            .setDefaultLocalName("Capacitor")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadInductor() {
        return new MaterialBuilder().setName("Inductor")
            .setDefaultLocalName("Inductor")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
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
