package gregtech.loaders.materials;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enchants.EnchantmentEnderDamage;
import gregtech.api.enchants.EnchantmentRadioactivity;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Element;
import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.TierEU;
import gregtech.api.util.CustomGlyphs;

public class MaterialsInit {

    public static void load() {
        loadElements();
        loadIsotopes();
        loadWaterLine();
        loadRandom();
        loadDontCare();
        loadUnknownComponents();
        loadTiers();
        loadCircuitry();
        loadNotExact();
        loadTODOThis();
        loadDegree1Compounds();
        loadUnclassified01();
        loadUnclassified02();
        loadUnclassified03();
        loadUnclassified04();
        loadUnclassified05();
        loadRoastedOres();
        loadSiliconLine();
        loadUnclassified06();
        loadDegree2Compounds();
        loadDegree3Compounds();
        loadDegree4Compounds();
        loadDegree5Compounds();
        loadDegree6Compounds();
        loadPolybenzimidazoleLine();
        loadGasolineLine();
        loadAdded();
        loadGalaxySpace();
        loadUnclassified07();
        loadOverpoweredMaterials();
        loadSuperconductorBases();
        loadSuperconductors();
        loadWaterLineChemicals();
        loadRadoxLine();
        loadNetheriteLine();
        loadPrismaticAcidLine();
        loadFranciumLine();
        loadMagicMaterials();
        loadBotaniaMaterials();
        loadKevlarLine();
        loadAluminiumOres();
        loadUEVPlusMaterials();
        loadGTNHMaterials();
    }

    private static void loadElements() {
        Materials.Aluminium = loadAluminium();
        Materials.Americium = loadAmericium();
        Materials.Antimony = loadAntimony();
        Materials.Argon = loadArgon();
        Materials.Arsenic = loadArsenic();
        Materials.Barium = loadBarium();
        Materials.Beryllium = loadBeryllium();
        Materials.Bismuth = loadBismuth();
        Materials.Boron = loadBoron();
        Materials.Caesium = loadCaesium();
        Materials.Calcium = loadCalcium();
        Materials.Carbon = loadCarbon();
        Materials.Cadmium = loadCadmium();
        Materials.Cerium = loadCerium();
        Materials.Chlorine = loadChlorine();
        Materials.Chrome = loadChrome();
        Materials.Cobalt = loadCobalt();
        Materials.Copper = loadCopper();
        Materials.Desh = loadDesh(); // Not a real element
        Materials.Dysprosium = loadDysprosium();
        Materials.Empty = loadEmpty(); // Not a real element
        Materials.Erbium = loadErbium();
        Materials.Europium = loadEuropium();
        Materials.Flerovium = loadFlerovium();
        Materials.Fluorine = loadFluorine();
        Materials.Francium = loadFrancium();
        Materials.Gadolinium = loadGadolinium();
        Materials.Gallium = loadGallium();
        Materials.Gold = loadGold();
        Materials.Holmium = loadHolmium();
        Materials.Hydrogen = loadHydrogen();
        Materials.Helium = loadHelium();
        Materials.Indium = loadIndium();
        Materials.Iridium = loadIridium();
        Materials.Iron = loadIron();
        Materials.Lanthanum = loadLanthanum();
        Materials.Lead = loadLead();
        Materials.Lithium = loadLithium();
        Materials.Lutetium = loadLutetium();
        Materials.Magic = loadMagic(); // Not a real element
        Materials.Magnesium = loadMagnesium();
        Materials.Manganese = loadManganese();
        Materials.Mercury = loadMercury();
        Materials.MeteoricIron = loadMeteoricIron(); // Not a real element
        Materials.Molybdenum = loadMolybdenum();
        Materials.Naquadah = loadNaquadah(); // Not a real element
        Materials.Neodymium = loadNeodymium();
        Materials.Neutronium = loadNeutronium();
        Materials.Nickel = loadNickel();
        Materials.Niobium = loadNiobium();
        Materials.Nitrogen = loadNitrogen();
        Materials._NULL = loadNULL(); // Not a real element
        Materials.Oriharukon = loadOriharukon();
        Materials.Osmium = loadOsmium();
        Materials.Oxygen = loadOxygen();
        Materials.Palladium = loadPalladium();
        Materials.Phosphorus = loadPhosphorus();
        Materials.Platinum = loadPlatinum();
        Materials.Plutonium = loadPlutonium();
        Materials.Potassium = loadPotassium();
        Materials.Praseodymium = loadPraseodymium();
        Materials.Promethium = loadPromethium();
        Materials.Radon = loadRadon();
        Materials.Rubidium = loadRubidium();
        Materials.Samarium = loadSamarium();
        Materials.Scandium = loadScandium();
        Materials.Silicon = loadSilicon();
        Materials.Silver = loadSilver();
        Materials.Sodium = loadSodium();
        Materials.Strontium = loadStrontium();
        Materials.Sulfur = loadSulfur();
        Materials.Tantalum = loadTantalum();
        Materials.Tellurium = loadTellurium();
        Materials.Terbium = loadTerbium();
        Materials.Thorium = loadThorium();
        Materials.Thulium = loadThulium();
        Materials.Tin = loadTin();
        Materials.Titanium = loadTitanium();
        Materials.Tritanium = loadTritanium(); // Not a real element
        Materials.Tungsten = loadTungsten();
        Materials.Uranium = loadUranium();
        Materials.Vanadium = loadVanadium();
        Materials.Ytterbium = loadYtterbium();
        Materials.Yttrium = loadYttrium();
        Materials.Zinc = loadZinc();
    }

    private static Materials loadAluminium() {
        return new MaterialBuilder().setName("Aluminium")
            .setDefaultLocalName("Aluminium")
            .setElement(Element.Al)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x0080c8f0)
            .setTool(128, 2, 10.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(933)
            .setBlastFurnaceTemp(1_700)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.VOLATUS, 1)
            .addOreByproduct(() -> Materials.Bauxite)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadAmericium() {
        return new MaterialBuilder().setName("Americium")
            .setDefaultLocalName("Americium")
            .setElement(Element.Am)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00c8c8c8)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_449)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadAntimony() {
        return new MaterialBuilder().setName("Antimony")
            .setDefaultLocalName("Antimony")
            .setElement(Element.Sb)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00dcdcf0)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(903)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.AQUA, 1)
            .addOreByproduct(() -> Materials.Zinc)
            .addOreByproduct(() -> Materials.Iron)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .constructMaterial();
    }

    private static Materials loadArgon() {
        return new MaterialBuilder().setName("Argon")
            .setDefaultLocalName("Argon")
            .setElement(Element.Ar)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGreen)
            .setARGB(0xf000ff00)
            .addCell()
            .addPlasma()
            .setMeltingPoint(83)
            .addAspect(TCAspects.AER, 2)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadArsenic() {
        return new MaterialBuilder().setName("Arsenic")
            .setDefaultLocalName("Arsenic")
            .setElement(Element.As)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeOrange)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addCell()
            .addPlasma()
            .setMeltingPoint(1_090)
            .addAspect(TCAspects.VENENUM, 3)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadBarium() {
        return new MaterialBuilder().setName("Barium")
            .setDefaultLocalName("Barium")
            .setElement(Element.Ba)
            .setIconSet(TextureSet.SET_METALLIC)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_000)
            .addAspect(TCAspects.VINCULUM, 3)
            .constructMaterial();
    }

    private static Materials loadBeryllium() {
        return new MaterialBuilder().setName("Beryllium")
            .setDefaultLocalName("Beryllium")
            .setElement(Element.Be)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x0064b464)
            .setTool(64, 2, 14.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(1_560)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.LUCRUM, 1)
            .addOreByproduct(() -> Materials.Emerald)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadBismuth() {
        return new MaterialBuilder().setName("Bismuth")
            .setDefaultLocalName("Bismuth")
            .setElement(Element.Bi)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x0064a0a0)
            .setTool(64, 1, 6.0f)
            .setToolEnchantment(() -> Enchantment.baneOfArthropods, 4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(544)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadBoron() {
        return new MaterialBuilder().setName("Boron")
            .setDefaultLocalName("Boron")
            .setElement(Element.B)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00d2fad2)
            .addDustItems()
            .addPlasma()
            .setMeltingPoint(2_349)
            .addAspect(TCAspects.VITREUS, 3)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .constructMaterial();
    }

    private static Materials loadCaesium() {
        return new MaterialBuilder().setName("Caesium")
            .setDefaultLocalName("Caesium")
            .setElement(Element.Cs)
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00b0c4de)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(301)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadCalcium() {
        return new MaterialBuilder().setName("Calcium")
            .setDefaultLocalName("Calcium")
            .setElement(Element.Ca)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setARGB(0x00fff5f5)
            .addDustItems()
            .addPlasma()
            .setMeltingPoint(1_115)
            .setBlastFurnaceTemp(1_115)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.SANO, 1)
            .addAspect(TCAspects.TUTAMEN, 1)
            .constructMaterial();
    }

    private static Materials loadCarbon() {
        return new MaterialBuilder().setName("Carbon")
            .setDefaultLocalName("Carbon")
            .setElement(Element.C)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00141414)
            .setTool(64, 2, 1.0f)
            .addDustItems()
            .addOreItems()
            .addMetalItems()
            .addCell()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3_800)
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.IGNIS, 1)
            .addSubTag(SubTag.NO_SMELTING)
            .addOrePrefix(OrePrefixes.nanite)
            .removeOrePrefix(OrePrefixes.sheetmetal)
            .constructMaterial();
    }

    private static Materials loadCadmium() {
        return new MaterialBuilder().setName("Cadmium")
            .setDefaultLocalName("Cadmium")
            .setElement(Element.Cd)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGray)
            .setARGB(0x0032323c)
            .addDustItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(594)
            .addAspect(TCAspects.METALLUM, 1)
            .addAspect(TCAspects.POTENTIA, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .constructMaterial();
    }

    private static Materials loadCerium() {
        return new MaterialBuilder().setName("Cerium")
            .setDefaultLocalName("Cerium")
            .setElement(Element.Ce)
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x007bd490)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_068)
            .setBlastFurnaceTemp(1_068)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadChlorine() {
        return new MaterialBuilder().setName("Chlorine")
            .setDefaultLocalName("Chlorine")
            .setElement(Element.Cl)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .addCell()
            .addPlasma()
            .setMeltingPoint(171)
            .addAspect(TCAspects.AQUA, 2)
            .addAspect(TCAspects.PANNUS, 1)
            .constructMaterial();
    }

    private static Materials loadChrome() {
        return new MaterialBuilder().setName("Chrome")
            .setDefaultLocalName("Chrome")
            .setElement(Element.Cr)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyePink)
            .setARGB(0x00ffe6e6)
            .setTool(256, 3, 11.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2_180)
            .setBlastFurnaceTemp(1_700)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MACHINA, 1)
            .addOreByproduct(() -> Materials.Iron)
            .addOreByproduct(() -> Materials.Magnesium)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadCobalt() {
        return new MaterialBuilder().setName("Cobalt")
            .setDefaultLocalName("Cobalt")
            .setElement(Element.Co)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x005050fa)
            .setTool(512, 3, 8.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_768)
            .setBlastFurnaceTemp(1_700)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .addOreByproduct(() -> Materials.Cobaltite)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.WASHING_SODIUMPERSULFATE)
            .constructMaterial();
    }

    private static Materials loadCopper() {
        return new MaterialBuilder().setName("Copper")
            .setDefaultLocalName("Copper")
            .setElement(Element.Cu)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff6400)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addGearItems()
            .setMeltingPoint(1_357)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.PERMUTATIO, 1)
            .addOreByproduct(() -> Materials.Cobalt)
            .addOreByproduct(() -> Materials.Gold)
            .addOreByproduct(() -> Materials.Nickel)
            .setArcSmeltingInto(() -> Materials.AnnealedCopper)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.WASHING_SODIUMPERSULFATE)
            .addOrePrefix(OrePrefixes.nanite)
            .constructMaterial();
    }

    private static Materials loadDesh() {
        return new MaterialBuilder().setName("Desh")
            .setDefaultLocalName("Desh")
            .setElement(Element.De)
            .setChemicalFormula("De")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00282828)
            .setTool(1_280, 4, 20.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2_500)
            .setBlastFurnaceTemp(2_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ALIENIS, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadDysprosium() {
        return new MaterialBuilder().setName("Dysprosium")
            .setDefaultLocalName("Dysprosium")
            .setElement(Element.Dy)
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x0069d150)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_680)
            .setBlastFurnaceTemp(1_680)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 3)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadEmpty() {
        return new MaterialBuilder().setName("Empty")
            .setDefaultLocalName("Empty")
            .setElement(Element._NULL)
            .setARGB(0xffffffff)
            .addEmpty()
            .addAspect(TCAspects.VACUOS, 2)
            .addSubTag(SubTag.TRANSPARENT)
            .removeOrePrefix(OrePrefixes.cell)
            .removeOrePrefix(OrePrefixes.bucket)
            .removeOrePrefix(OrePrefixes.bucketClay)
            .removeOrePrefix(OrePrefixes.bottle)
            .constructMaterial();
    }

    private static Materials loadErbium() {
        return new MaterialBuilder().setName("Erbium")
            .setDefaultLocalName("Erbium")
            .setElement(Element.Er)
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x00b09851)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_802)
            .setBlastFurnaceTemp(1_802)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadEuropium() {
        return new MaterialBuilder().setName("Europium")
            .setDefaultLocalName("Europium")
            .setElement(Element.Eu)
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x00f6b5ff)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_099)
            .setBlastFurnaceTemp(1_099)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadFlerovium() {
        return new MaterialBuilder().setName("Flerovium_GT5U")
            .setDefaultLocalName("Flerovium")
            .setElement(Element.Fl)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 3)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadFluorine() {
        return new MaterialBuilder().setName("Fluorine")
            .setDefaultLocalName("Fluorine")
            .setElement(Element.F)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x7fffffff)
            .addCell()
            .addPlasma()
            .setMeltingPoint(53)
            .addAspect(TCAspects.PERDITIO, 2)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadFrancium() {
        return new MaterialBuilder().setName("Francium_GT5U")
            .setDefaultLocalName("Francium")
            .setElement(Element.Fr)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00ff3900)
            .addDustItems()
            .addPlasma()
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 2)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadGadolinium() {
        return new MaterialBuilder().setName("Gadolinium")
            .setDefaultLocalName("Gadolinium")
            .setElement(Element.Gd)
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x003bba1c)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_585)
            .setBlastFurnaceTemp(1_585)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadGallium() {
        return new MaterialBuilder().setName("Gallium")
            .setDefaultLocalName("Gallium")
            .setElement(Element.Ga)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00dcdcff)
            .setTool(64, 2, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(302)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ELECTRUM, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadGold() {
        return new MaterialBuilder().setName("Gold")
            .setDefaultLocalName("Gold")
            .setElement(Element.Au)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff1e)
            .setTool(64, 2, 12.0f)
            .setToolEnchantment(() -> Enchantment.smite, 3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_337)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.LUCRUM, 2)
            .addOreByproduct(() -> Materials.Copper)
            .addOreByproduct(() -> Materials.Nickel)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.WASHING_MERCURY)
            .addOrePrefix(OrePrefixes.nanite)
            .removeOrePrefix(OrePrefixes.nugget) // minecraft:gold_nugget
            .removeOrePrefix(OrePrefixes.block) // minecraft:gold_block
            .removeOrePrefix(OrePrefixes.ingot) // minecraft:gold_ingot
            .constructMaterial();
    }

    private static Materials loadHolmium() {
        return new MaterialBuilder().setName("Holmium")
            .setDefaultLocalName("Holmium")
            .setElement(Element.Ho)
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x001608a6)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_734)
            .setBlastFurnaceTemp(1_734)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadHydrogen() {
        return new MaterialBuilder().setName("Hydrogen")
            .setDefaultLocalName("Hydrogen")
            .setElement(Element.H)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setARGB(0xf00000ff)
            .setFuel(MaterialBuilder.FuelType.Gas, 20)
            .setOreMultiplier(4)
            .addCell()
            .addPlasma()
            .setMeltingPoint(14)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.ICE_ORE)
            .addSubTag(SubTag.NO_ORE_PROCESSING)
            .addOrePrefix(OrePrefixes.ore)
            .addOrePrefix(OrePrefixes.rawOre)
            .constructMaterial();
    }

    private static Materials loadHelium() {
        return new MaterialBuilder().setName("Helium")
            .setDefaultLocalName("Helium")
            .setElement(Element.He)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0xf0ffff00)
            .addCell()
            .addPlasma()
            .setMeltingPoint(1)
            .addAspect(TCAspects.AER, 2)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadIndium() {
        return new MaterialBuilder().setName("Indium")
            .setDefaultLocalName("Indium")
            .setElement(Element.In)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00400080)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(429)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadIridium() {
        return new MaterialBuilder().setName("Iridium")
            .setDefaultLocalName("Iridium")
            .setElement(Element.Ir)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00f0f0f5)
            .setTool(2_560, 3, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2_719)
            .setBlastFurnaceTemp(4_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MACHINA, 1)
            .addOreByproduct(() -> Materials.Platinum)
            .addOreByproduct(() -> Materials.Osmium)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadIron() {
        return new MaterialBuilder().setName("Iron")
            .setDefaultLocalName("Iron")
            .setElement(Element.Fe)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00c8c8c8)
            .setTool(256, 2, 6.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_811)
            .addAspect(TCAspects.METALLUM, 3)
            .addOreByproduct(() -> Materials.Nickel)
            .addOreByproduct(() -> Materials.Tin)
            .setArcSmeltingInto(() -> Materials.WroughtIron)
            .addSubTag(SubTag.BLASTFURNACE_CALCITE_TRIPLE)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.MULTI_PLATE)
            .addOrePrefix(OrePrefixes.nanite)
            .removeOrePrefix(OrePrefixes.block) // minecraft:iron_block
            .removeOrePrefix(OrePrefixes.ingot) // minecraft:iron_ingot
            .constructMaterial();
    }

    private static Materials loadLanthanum() {
        return new MaterialBuilder().setName("Lanthanum")
            .setDefaultLocalName("Lanthanum")
            .setElement(Element.La)
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x008a8a8a)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_193)
            .setBlastFurnaceTemp(1_193)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadLead() {
        return new MaterialBuilder().setName("Lead")
            .setDefaultLocalName("Lead")
            .setElement(Element.Pb)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setARGB(0x008c648c)
            .setTool(64, 1, 8.0f)
            .setToolEnchantment(() -> Enchantment.baneOfArthropods, 2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(600)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ORDO, 1)
            .addOreByproduct(() -> Materials.Silver)
            .addOreByproduct(() -> Materials.Sulfur)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.SOLDERING_MATERIAL)
            .addSubTag(SubTag.SOLDERING_MATERIAL_BAD)
            .constructMaterial();
    }

    private static Materials loadLithium() {
        return new MaterialBuilder().setName("Lithium")
            .setDefaultLocalName("Lithium")
            .setElement(Element.Li)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x00e1dcff)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(454)
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.POTENTIA, 2)
            .addOreByproduct(() -> Materials.Lithium)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadLutetium() {
        return new MaterialBuilder().setName("Lutetium")
            .setDefaultLocalName("Lutetium")
            .setElement(Element.Lu)
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x00bc3ec7)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_925)
            .setBlastFurnaceTemp(1_925)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadMagic() {
        return new MaterialBuilder().setName("Magic")
            .setDefaultLocalName("Magic")
            .setElement(Element.Ma)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyePurple)
            .setARGB(0x006400c8)
            .setTool(5_120, 5, 8.0f)
            .setFuel(MaterialBuilder.FuelType.Magic, 32)
            .addDustItems()
            .addMetalItems()
            .addGemItems()
            .addOreItems()
            .addCell()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5_000)
            .addAspect(TCAspects.PRAECANTATIO, 4)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadMagnesium() {
        return new MaterialBuilder().setName("Magnesium")
            .setDefaultLocalName("Magnesium")
            .setElement(Element.Mg)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setARGB(0x00ffc8c8)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(923)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.SANO, 1)
            .addOreByproduct(() -> Materials.Olivine)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadManganese() {
        return new MaterialBuilder().setName("Manganese")
            .setDefaultLocalName("Manganese")
            .setElement(Element.Mn)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fafafa)
            .setTool(512, 2, 7.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(1_519)
            .addAspect(TCAspects.METALLUM, 3)
            .addOreByproduct(() -> Materials.Chrome)
            .addOreByproduct(() -> Materials.Iron)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadMercury() {
        return new MaterialBuilder().setName("Mercury")
            .setDefaultLocalName("Mercury")
            .setElement(Element.Hg)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00ffdcdc)
            .setFuel(MaterialBuilder.FuelType.Magic, 32)
            .setToolEnchantment(() -> EnchantmentEnderDamage.INSTANCE, 3)
            .addCell()
            .addPlasma()
            .setMeltingPoint(234)
            .addAspect(TCAspects.METALLUM, 1)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.SMELTING_TO_GEM)
            .constructMaterial();
    }

    private static Materials loadMeteoricIron() {
        return new MaterialBuilder().setName("MeteoricIron")
            .setDefaultLocalName("Meteoric Iron")
            .setElement(Element.SpFe)
            .setChemicalFormula("SpFe")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00643250)
            .setTool(384, 3, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(1_811)
            .setBlastFurnaceTemp(1_000)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .addOreByproduct(() -> Materials.Iron)
            .addOreByproduct(() -> Materials.Nickel)
            .addOreByproduct(() -> Materials.Iridium)
            .addOreByproduct(() -> Materials.Platinum)
            .addSubTag(SubTag.BLASTFURNACE_CALCITE_TRIPLE)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadMolybdenum() {
        return new MaterialBuilder().setName("Molybdenum")
            .setDefaultLocalName("Molybdenum")
            .setElement(Element.Mo)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00b4b4dc)
            .setTool(512, 2, 7.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(2_896)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadNaquadah() {
        return new MaterialBuilder().setName("Naquadah")
            .setDefaultLocalName("Naquadah")
            .setElement(Element.Nq)
            .setChemicalFormula("Nq")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00323232)
            .setMoltenARGB(0x0000ff00)
            .setTool(1_280, 4, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5_400)
            .setBlastFurnaceTemp(5_400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 3)
            .addAspect(TCAspects.RADIO, 1)
            .addAspect(TCAspects.NEBRISUM, 1)
            .addOreByproduct(() -> Materials.NaquadahEnriched)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadNeodymium() {
        return new MaterialBuilder().setName("Neodymium")
            .setDefaultLocalName("Neodymium")
            .setElement(Element.Nd)
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00646464)
            .setTool(512, 2, 7.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_297)
            .setBlastFurnaceTemp(1_297)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 2)
            .addOreByproduct(() -> Materials.Monazite)
            .addOreByproduct(() -> Materials.RareEarth)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadNeutronium() {
        return new MaterialBuilder().setName("Neutronium")
            .setDefaultLocalName("Neutronium")
            .setElement(Element.Nt)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fafafa)
            .setTool(655_360, 6, 24.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(10_000)
            .setBlastFurnaceTemp(10_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 4)
            .addAspect(TCAspects.VITREUS, 3)
            .addAspect(TCAspects.ALIENIS, 2)
            .addOreByproduct(() -> Materials.Neutronium)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .addOrePrefix(OrePrefixes.nanite)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_LuV);
    }

    private static Materials loadNickel() {
        return new MaterialBuilder().setName("Nickel")
            .setDefaultLocalName("Nickel")
            .setElement(Element.Ni)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x00c8c8fa)
            .setTool(64, 2, 6.0f)
            .setToolEnchantment(() -> Enchantment.baneOfArthropods, 2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_728)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.IGNIS, 1)
            .addOreByproduct(() -> Materials.Cobalt)
            .addOreByproduct(() -> Materials.Platinum)
            .addOreByproduct(() -> Materials.Iron)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.WASHING_SODIUMPERSULFATE)
            .constructMaterial();
    }

    private static Materials loadNiobium() {
        return new MaterialBuilder().setName("Niobium")
            .setDefaultLocalName("Niobium")
            .setElement(Element.Nb)
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00beb4c8)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(2_750)
            .setBlastFurnaceTemp(2_750)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ELECTRUM, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadNitrogen() {
        return new MaterialBuilder().setName("Nitrogen")
            .setDefaultLocalName("Nitrogen")
            .setElement(Element.N)
            .setIconSet(TextureSet.SET_GEM_HORIZONTAL)
            .setColor(Dyes.dyeCyan)
            .setARGB(0xf00096c8)
            .setOreMultiplier(4)
            .addCell()
            .addPlasma()
            .setMeltingPoint(63)
            .addAspect(TCAspects.AER, 2)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.ICE_ORE)
            .addSubTag(SubTag.NO_ORE_PROCESSING)
            .addOrePrefix(OrePrefixes.ore)
            .addOrePrefix(OrePrefixes.rawOre)
            .constructMaterial();
    }

    private static Materials loadNULL() {
        return new MaterialBuilder().setName("NULL")
            .setDefaultLocalName("NULL")
            .setElement(Element._NULL)
            .constructMaterial();
    }

    private static Materials loadOriharukon() {
        return new MaterialBuilder().setName("Oriharukon")
            .setDefaultLocalName("Oriharukon")
            .setElement(Element.Oh)
            .setChemicalFormula("Oh")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00677d68)
            .setTool(10_240, 5, 32.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5_400)
            .setBlastFurnaceTemp(5_400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.LUCRUM, 2)
            .addAspect(TCAspects.ALIENIS, 1)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadOsmium() {
        return new MaterialBuilder().setName("Osmium")
            .setDefaultLocalName("Osmium")
            .setElement(Element.Os)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x003232ff)
            .setTool(1_280, 4, 16.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3_306)
            .setBlastFurnaceTemp(4_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MACHINA, 1)
            .addAspect(TCAspects.NEBRISUM, 1)
            .addOreByproduct(() -> Materials.Iridium)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.WASHING_MERCURY)
            .constructMaterial();
    }

    private static Materials loadOxygen() {
        return new MaterialBuilder().setName("Oxygen")
            .setDefaultLocalName("Oxygen")
            .setElement(Element.O)
            .setIconSet(TextureSet.SET_GEM_VERTICAL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0xf00064c8)
            .setOreMultiplier(4)
            .addCell()
            .addPlasma()
            .setMeltingPoint(54)
            .addAspect(TCAspects.AER, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.ICE_ORE)
            .addSubTag(SubTag.NO_ORE_PROCESSING)
            .addOrePrefix(OrePrefixes.ore)
            .addOrePrefix(OrePrefixes.rawOre)
            .constructMaterial();
    }

    private static Materials loadPalladium() {
        return new MaterialBuilder().setName("Palladium")
            .setDefaultLocalName("Palladium")
            .setElement(Element.Pd)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGray)
            .setARGB(0xB1B1B1)
            .setTool(512, 4, 8.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_828)
            .setBlastFurnaceTemp(1_828)
            .setBlastFurnaceRequired(true)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 3)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadPhosphorus() {
        return new MaterialBuilder().setName("Phosphorus")
            .setDefaultLocalName("Phosphorus")
            .setElement(Element.P)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .addDustItems()
            .addPlasma()
            .setMeltingPoint(317)
            .addAspect(TCAspects.IGNIS, 2)
            .addAspect(TCAspects.POTENTIA, 1)
            .addOreByproduct(() -> Materials.Phosphate)
            .constructMaterial();
    }

    private static Materials loadPlatinum() {
        return new MaterialBuilder().setName("Platinum")
            .setDefaultLocalName("Platinum")
            .setElement(Element.Pt)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ffffc8)
            .setTool(64, 4, 12.0f)
            .setToolEnchantment(() -> Enchantment.smite, 5)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2_041)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.NEBRISUM, 1)
            .addOreByproduct(() -> Materials.Nickel)
            .addOreByproduct(() -> Materials.Iridium)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.WASHING_MERCURY)
            .constructMaterial();
    }

    private static Materials loadPlutonium() {
        return new MaterialBuilder().setName("Plutonium")
            .setDefaultLocalName("Plutonium 239")
            .setElement(Element.Pu)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00f03232)
            .setTool(512, 3, 6.0f)
            .setToolEnchantment(() -> EnchantmentRadioactivity.INSTANCE, 1)
            .setArmorEnchantment(() -> EnchantmentRadioactivity.INSTANCE, 1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .setMeltingPoint(912)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 2)
            .addOreByproduct(() -> Materials.Uranium)
            .addOreByproduct(() -> Materials.Lead)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadPotassium() {
        return new MaterialBuilder().setName("Potassium")
            .setDefaultLocalName("Potassium")
            .setElement(Element.K)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x009aacdf)
            .addDustItems()
            .addMetalItems()
            .addPlasma()
            .setMeltingPoint(336)
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.POTENTIA, 1)
            .constructMaterial();
    }

    private static Materials loadPraseodymium() {
        return new MaterialBuilder().setName("Praseodymium")
            .setDefaultLocalName("Praseodymium")
            .setElement(Element.Pr)
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x0075d681)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_208)
            .setBlastFurnaceTemp(1_208)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadPromethium() {
        return new MaterialBuilder().setName("Promethium")
            .setDefaultLocalName("Promethium")
            .setElement(Element.Pm)
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x0024b535)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_315)
            .setBlastFurnaceTemp(1_315)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadRadon() {
        return new MaterialBuilder().setName("Radon")
            .setDefaultLocalName("Radon")
            .setElement(Element.Rn)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePurple)
            .setARGB(0xf0ff00ff)
            .addCell()
            .addPlasma()
            .setMeltingPoint(202)
            .addAspect(TCAspects.AER, 1)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadRubidium() {
        return new MaterialBuilder().setName("Rubidium")
            .setDefaultLocalName("Rubidium")
            .setElement(Element.Rb)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00f01e1e)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(312)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.VITREUS, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadSamarium() {
        return new MaterialBuilder().setName("Samarium")
            .setDefaultLocalName("Samarium")
            .setElement(Element.Sm)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00ffffcc)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_345)
            .setBlastFurnaceTemp(1_345)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addAspect(TCAspects.MAGNETO, 10)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadScandium() {
        return new MaterialBuilder().setName("Scandium")
            .setDefaultLocalName("Scandium")
            .setElement(Element.Sc)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00cccccc)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .setMeltingPoint(1_814)
            .setBlastFurnaceTemp(1_814)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadSilicon() {
        return new MaterialBuilder().setName("Silicon")
            .setDefaultLocalName("Raw Silicon")
            .setElement(Element.Si)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x003c3c50)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(2_273)
            .setBlastFurnaceTemp(2_273)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.TENEBRAE, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadSilver() {
        return new MaterialBuilder().setName("Silver")
            .setDefaultLocalName("Silver")
            .setElement(Element.Ag)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00dcdcff)
            .setTool(64, 2, 10.0f)
            .setToolEnchantment(() -> EnchantmentEnderDamage.INSTANCE, 2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_234)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.LUCRUM, 1)
            .addOreByproduct(() -> Materials.Lead)
            .addOreByproduct(() -> Materials.Sulfur)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.WASHING_MERCURY_99_PERCENT)
            .addOrePrefix(OrePrefixes.nanite)
            .constructMaterial();
    }

    private static Materials loadSodium() {
        return new MaterialBuilder().setName("Sodium")
            .setDefaultLocalName("Sodium")
            .setElement(Element.Na)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00000096)
            .addDustItems()
            .addCell()
            .addPlasma()
            .setMeltingPoint(370)
            .addAspect(TCAspects.VITREUS, 2)
            .addAspect(TCAspects.LUX, 1)
            .constructMaterial();
    }

    private static Materials loadStrontium() {
        return new MaterialBuilder().setName("Strontium")
            .setDefaultLocalName("Strontium")
            .setElement(Element.Sr)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00c8c8c8)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addGearItems()
            .setMeltingPoint(1_050)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.STRONTIO, 1)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadSulfur() {
        return new MaterialBuilder().setName("Sulfur")
            .setDefaultLocalName("Sulfur")
            .setElement(Element.S)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00c8c800)
            .addDustItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(388)
            .addAspect(TCAspects.IGNIS, 1)
            .addOreByproduct(() -> Materials.Sulfur)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadTantalum() {
        return new MaterialBuilder().setName("Tantalum")
            .setDefaultLocalName("Tantalum")
            .setElement(Element.Ta)
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x0069b7ff)
            .setTool(2_560, 3, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(3_290)
            .setBlastFurnaceTemp(3_290)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.VINCULUM, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadTellurium() {
        return new MaterialBuilder().setName("Tellurium")
            .setDefaultLocalName("Tellurium")
            .setElement(Element.Te)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00ceff56)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(722)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadTerbium() {
        return new MaterialBuilder().setName("Terbium")
            .setDefaultLocalName("Terbium")
            .setElement(Element.Tb)
            .setIconSet(TextureSet.SET_METALLIC)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_629)
            .setBlastFurnaceTemp(1_629)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadThorium() {
        return new MaterialBuilder().setName("Thorium")
            .setDefaultLocalName("Thorium")
            .setElement(Element.Th)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00001e00)
            .setTool(512, 2, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(2_115)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addOreByproduct(() -> Materials.Uranium)
            .addOreByproduct(() -> Materials.Lead)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadThulium() {
        return new MaterialBuilder().setName("Thulium")
            .setDefaultLocalName("Thulium")
            .setElement(Element.Tm)
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x00596bc2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_818)
            .setBlastFurnaceTemp(1_818)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadTin() {
        return new MaterialBuilder().setName("Tin")
            .setDefaultLocalName("Tin")
            .setElement(Element.Sn)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00dcdcdc)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addGearItems()
            .setMeltingPoint(505)
            .setBlastFurnaceTemp(505)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.VITREUS, 1)
            .addOreByproduct(() -> Materials.Iron)
            .addOreByproduct(() -> Materials.Zinc)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.SOLDERING_MATERIAL)
            .constructMaterial();
    }

    private static Materials loadTitanium() {
        return new MaterialBuilder().setName("Titanium")
            .setDefaultLocalName("Titanium")
            .setElement(Element.Ti)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00dca0f0)
            .setTool(1_600, 3, 7.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_941)
            .setBlastFurnaceTemp(1_940)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.TUTAMEN, 1)
            .addOreByproduct(() -> Materials.Almandine)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadTritanium() {
        return new MaterialBuilder().setName("Tritanium")
            .setDefaultLocalName("Tritanium")
            .setElement(Element.Tn)
            .setChemicalFormula("Tn")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00600000)
            .setTool(1_435_392, 6, 20.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(9_900)
            .setBlastFurnaceTemp(9_900)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ORDO, 2)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadTungsten() {
        return new MaterialBuilder().setName("Tungsten")
            .setDefaultLocalName("Tungsten")
            .setElement(Element.W)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00323232)
            .setTool(2_560, 3, 7.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3_695)
            .setBlastFurnaceTemp(3_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 3)
            .addAspect(TCAspects.TUTAMEN, 1)
            .addOreByproduct(() -> Materials.Manganese)
            .addOreByproduct(() -> Materials.Molybdenum)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadUranium() {
        return new MaterialBuilder().setName("Uranium")
            .setDefaultLocalName("Uranium 238")
            .setElement(Element.U)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x0032f032)
            .setTool(512, 3, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(1_405)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addOreByproduct(() -> Materials.Lead)
            .addOreByproduct(() -> Materials.Uranium235)
            .addOreByproduct(() -> Materials.Thorium)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadVanadium() {
        return new MaterialBuilder().setName("Vanadium")
            .setDefaultLocalName("Vanadium")
            .setElement(Element.V)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00323232)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(2_183)
            .setBlastFurnaceTemp(2_183)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadYtterbium() {
        return new MaterialBuilder().setName("Ytterbium")
            .setDefaultLocalName("Ytterbium")
            .setElement(Element.Yb)
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x002cc750)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_097)
            .setBlastFurnaceTemp(1_097)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadYttrium() {
        return new MaterialBuilder().setName("Yttrium")
            .setDefaultLocalName("Yttrium")
            .setElement(Element.Y)
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00dcfadc)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1_799)
            .setBlastFurnaceTemp(1_799)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadZinc() {
        return new MaterialBuilder().setName("Zinc")
            .setDefaultLocalName("Zinc")
            .setElement(Element.Zn)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00faf0f0)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(692)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.SANO, 1)
            .addOreByproduct(() -> Materials.Tin)
            .addOreByproduct(() -> Materials.Gallium)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.WASHING_SODIUMPERSULFATE)
            .constructMaterial();
    }

    private static void loadIsotopes() {
        Materials.Deuterium = loadDeuterium();
        Materials.Helium3 = loadHelium3();
        Materials.Plutonium241 = loadPlutonium241();
        Materials.Tritium = loadTritium();
        Materials.Uranium235 = loadUranium235();
    }

    private static Materials loadDeuterium() {
        return new MaterialBuilder().setName("Deuterium")
            .setDefaultLocalName("Deuterium")
            .setElement(Element.D)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0xf0ffff00)
            .addCell()
            .addPlasma()
            .setMeltingPoint(14)
            .addAspect(TCAspects.AQUA, 3)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadHelium3() {
        return new MaterialBuilder().setName("Helium_3")
            .setDefaultLocalName("Helium-3")
            .setElement(Element.He_3)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0xf0ffff00)
            .addCell()
            .setMeltingPoint(1)
            .addAspect(TCAspects.AER, 3)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadPlutonium241() {
        return new MaterialBuilder().setName("Plutonium241")
            .setDefaultLocalName("Plutonium 241")
            .setElement(Element.Pu_241)
            .setChemicalFormula(
                CustomGlyphs.SUPERSCRIPT2 + CustomGlyphs.SUPERSCRIPT4 + CustomGlyphs.SUPERSCRIPT1 + "Pu")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00fa4646)
            .setTool(512, 3, 6.0f)
            .setToolEnchantment(() -> EnchantmentRadioactivity.INSTANCE, 3)
            .setArmorEnchantment(() -> EnchantmentRadioactivity.INSTANCE, 3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(912)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 3)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadTritium() {
        return new MaterialBuilder().setName("Tritium")
            .setDefaultLocalName("Tritium")
            .setElement(Element.T)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setARGB(0xf0ff0000)
            .addCell()
            .addPlasma()
            .setMeltingPoint(14)
            .addAspect(TCAspects.AQUA, 4)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadUranium235() {
        return new MaterialBuilder().setName("Uranium235")
            .setDefaultLocalName("Uranium 235")
            .setElement(Element.U_235)
            .setChemicalFormula(CustomGlyphs.SUPERSCRIPT2 + CustomGlyphs.SUPERSCRIPT3 + CustomGlyphs.SUPERSCRIPT5 + "U")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x0046fa46)
            .setTool(512, 3, 6.0f)
            .setToolEnchantment(() -> EnchantmentRadioactivity.INSTANCE, 2)
            .setArmorEnchantment(() -> EnchantmentRadioactivity.INSTANCE, 2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(1_405)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 2)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static void loadWaterLine() {
        Materials.FlocculationWasteLiquid = loadFlocculationWasteLiquid();
        Materials.Grade1PurifiedWater = loadGrade1PurifiedWater();
        Materials.Grade2PurifiedWater = loadGrade2PurifiedWater();
        Materials.Grade3PurifiedWater = loadGrade3PurifiedWater();
        Materials.Grade4PurifiedWater = loadGrade4PurifiedWater();
        Materials.Grade5PurifiedWater = loadGrade5PurifiedWater();
        Materials.Grade6PurifiedWater = loadGrade6PurifiedWater();
        Materials.Grade7PurifiedWater = loadGrade7PurifiedWater();
        Materials.Grade8PurifiedWater = loadGrade8PurifiedWater();
    }

    private static Materials loadFlocculationWasteLiquid() {
        return new MaterialBuilder().setName("FlocculationWasteLiquid")
            .setDefaultLocalName("Flocculation Waste Liquid")
            .setChemicalFormula("Al₂(OH)₃??Cl₃")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x003d3a52)
            .addCell()
            .addFluid()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadGrade1PurifiedWater() {
        return new MaterialBuilder().setName("Grade1PurifiedWater")
            .setDefaultLocalName("Filtered Water (Grade 1)")
            .setChemicalFormula("H₂O")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x003f4cfd)
            .addCell()
            .addFluid()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadGrade2PurifiedWater() {
        return new MaterialBuilder().setName("Grade2PurifiedWater")
            .setDefaultLocalName("Ozonated Water (Grade 2)")
            .setChemicalFormula("H₂O")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x005d5dfe)
            .addCell()
            .addFluid()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadGrade3PurifiedWater() {
        return new MaterialBuilder().setName("Grade3PurifiedWater")
            .setDefaultLocalName("Flocculated Water (Grade 3)")
            .setChemicalFormula("H₂O")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00736dfe)
            .addCell()
            .addFluid()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadGrade4PurifiedWater() {
        return new MaterialBuilder().setName("Grade4PurifiedWater")
            .setDefaultLocalName("pH Neutralized Water (Grade 4)")
            .setChemicalFormula("H₂O")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00877eff)
            .addCell()
            .addFluid()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadGrade5PurifiedWater() {
        return new MaterialBuilder().setName("Grade5PurifiedWater")
            .setDefaultLocalName("Extreme-Temperature Treated Water (Grade 5)")
            .setChemicalFormula("H₂O")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x009890ff)
            .addCell()
            .addFluid()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadGrade6PurifiedWater() {
        return new MaterialBuilder().setName("Grade6PurifiedWater")
            .setDefaultLocalName("Ultraviolet Treated Electrically Neutral Water (Grade 6)")
            .setChemicalFormula("H₂O")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00a8a1ff)
            .addCell()
            .addFluid()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadGrade7PurifiedWater() {
        return new MaterialBuilder().setName("Grade7PurifiedWater")
            .setDefaultLocalName("Degassed Decontaminant-Free Water (Grade 7)")
            .setChemicalFormula("H₂O")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00b7b3ff)
            .addCell()
            .addFluid()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadGrade8PurifiedWater() {
        return new MaterialBuilder().setName("Grade8PurifiedWater")
            .setDefaultLocalName("Subatomically Perfect Water (Grade 8)")
            .setChemicalFormula("H₂O")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00c5c5ff)
            .addCell()
            .addFluid()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static void loadRandom() {
        Materials.AnyBronze = loadAnyBronze();
        Materials.AnyCopper = loadAnyCopper();
        Materials.AnyIron = loadAnyIron();
        Materials.AnyRubber = loadAnyRubber();
        Materials.AnySyntheticRubber = loadAnySyntheticRubber();
        Materials.BrickNether = loadBrickNether();
        Materials.Cobblestone = loadCobblestone();
        Materials.Crystal = loadCrystal();
        Materials.Metal = loadMetal();
        Materials.Organic = loadOrganic();
        Materials.Quartz = loadQuartz();
        Materials.Unknown = loadUnknown();
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
            .setArcSmeltingInto(() -> Materials.AnnealedCopper)
            .addSubTag(SubTag.METAL)
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
            .setArcSmeltingInto(() -> Materials.WroughtIron)
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

    private static Materials loadBrickNether() {
        return new MaterialBuilder().setName("BrickNether")
            .setDefaultLocalName("BrickNether")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_DULL)
            .removeOrePrefix(OrePrefixes.ingot) // minecraft:netherbrick
            .constructMaterial();
    }

    private static Materials loadCobblestone() {
        return new MaterialBuilder().setName("Cobblestone")
            .setDefaultLocalName("Cobblestone")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_DULL)
            .constructMaterial();
    }

    private static Materials loadCrystal() {
        return new MaterialBuilder().setName("Crystal")
            .setDefaultLocalName("Crystal")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_SHINY)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadMetal() {
        return new MaterialBuilder().setName("Metal")
            .setDefaultLocalName("Metal")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_METALLIC)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadOrganic() {
        return new MaterialBuilder().setName("Organic")
            .setDefaultLocalName("Organic")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_LEAF)
            .constructMaterial();
    }

    private static Materials loadQuartz() {
        return new MaterialBuilder().setName("Quartz")
            .setDefaultLocalName("Quartz")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_QUARTZ)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.QUARTZ)
            .constructMaterial();
    }

    private static Materials loadUnknown() {
        return new MaterialBuilder().setName("Unknown")
            .setDefaultLocalName("Unknown")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_DULL)
            .constructMaterial();
    }

    private static void loadDontCare() {
        Materials.Alfium = loadAlfium();
        Materials.Aquamarine = loadAquamarine();
        Materials.DarkThaumium = loadDarkThaumium();
        Materials.Draconium = loadDraconium();
        Materials.DraconiumAwakened = loadDraconiumAwakened();
        Materials.Ender = loadEnder();
        Materials.Endium = loadEndium();
        Materials.Fluix = loadFluix();
        Materials.Flux = loadFlux();
        Materials.InfusedTeslatite = loadInfusedTeslatite();
        Materials.IridiumSodiumOxide = loadIridiumSodiumOxide();
        Materials.Mutation = loadMutation();
        Materials.OsmiumTetroxide = loadOsmiumTetroxide();
        Materials.PhasedGold = loadPhasedGold();
        Materials.PhasedIron = loadPhasedIron();
        Materials.PlatinumGroupSludge = loadPlatinumGroupSludge();
        Materials.PurpleAlloy = loadPurpleAlloy();
        Materials.RubberTreeSap = loadRubberTreeSap();
        Materials.Serpentine = loadSerpentine();
        Materials.SodiumPeroxide = loadSodiumPeroxide();
        Materials.Teslatite = loadTeslatite();
    }

    private static Materials loadAlfium() {
        return new MaterialBuilder().setName("Alfium")
            .setDefaultLocalName("Alfium")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadAquamarine() {
        return new MaterialBuilder().setName("Aquamarine")
            .setDefaultLocalName("Aquamarine")
            .addDustItems()
            .addGemItems()
            .constructMaterial();
    }

    private static Materials loadDarkThaumium() {
        return new MaterialBuilder().setName("DarkThaumium")
            .setDefaultLocalName("Dark Thaumium")
            .addDustItems()
            .addMetalItems()
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadDraconium() {
        return new MaterialBuilder().setName("Draconium")
            .setDefaultLocalName("Draconium")
            .setChemicalFormula("D")
            .setIconSet(TextureSet.SET_DRACONIUM)
            .setColor(Dyes.dyePink)
            .setARGB(0x007a44b0)
            .setTool(32_768, 7, 20.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5_000)
            .setBlastFurnaceTemp(7_200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.METAL)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial();
    }

    private static Materials loadDraconiumAwakened() {
        return new MaterialBuilder().setName("DraconiumAwakened")
            .setDefaultLocalName("Awakened Draconium")
            .setChemicalFormula("D*")
            .setIconSet(TextureSet.SET_AWOKEN_DRACONIUM)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00f44e00)
            .setTool(65_536, 9, 40.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(9_900)
            .setBlastFurnaceTemp(9_900)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .addSubTag(SubTag.METAL)

            .constructMaterial();
    }

    private static Materials loadEnder() {
        return new MaterialBuilder().setName("Ender")
            .setDefaultLocalName("Ender")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadEndium() {
        return new MaterialBuilder().setName("HeeEndium")
            .setDefaultLocalName("Endium")
            .setChemicalFormula("Em")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x00a5dcfa)
            .setTool(1_024, 4, 16.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadFluix() {
        return new MaterialBuilder().setName("Fluix")
            .setDefaultLocalName("Fluix")
            .addDustItems()
            .addGemItems()
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.QUARTZ)
            .constructMaterial();
    }

    private static Materials loadFlux() {
        return new MaterialBuilder().setName("Flux")
            .setDefaultLocalName("Flux")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadInfusedTeslatite() {
        return new MaterialBuilder().setName("InfusedTeslatite")
            .setDefaultLocalName("Infused Teslatite")
            .setARGB(0x0064b4ff)
            .constructMaterial();
    }

    private static Materials loadIridiumSodiumOxide() {
        return new MaterialBuilder().setName("IridiumSodiumOxide")
            .setDefaultLocalName("Iridium Sodium Oxide")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMutation() {
        return new MaterialBuilder().setName("Mutation")
            .setDefaultLocalName("Mutation")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadOsmiumTetroxide() {
        return new MaterialBuilder().setName("OsmiumTetroxide")
            .setDefaultLocalName("Osmium Tetroxide")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadPhasedGold() {
        return new MaterialBuilder().setName("PhasedGold")
            .setDefaultLocalName("Phased Gold")
            .addDustItems()
            .addMetalItems()
            .setBlastFurnaceTemp(1_800)
            .setBlastFurnaceRequired(true)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadPhasedIron() {
        return new MaterialBuilder().setName("PhasedIron")
            .setDefaultLocalName("Phased Iron")
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(3_300)
            .setBlastFurnaceTemp(3_300)
            .setBlastFurnaceRequired(true)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadPlatinumGroupSludge() {
        return new MaterialBuilder().setName("PlatinumGroupSludge")
            .setDefaultLocalName("Platinum Group Sludge")
            .setChemicalFormula(
                "(SiO₂)" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK
                    + "Au"
                    + CustomGlyphs.SUBSCRIPT_QUESTION_MARK
                    + "Pt"
                    + CustomGlyphs.SUBSCRIPT_QUESTION_MARK
                    + "Pd"
                    + CustomGlyphs.SUBSCRIPT_QUESTION_MARK
                    + "??")
            .setIconSet(TextureSet.SET_POWDER)
            .setARGB(0x00001e00)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadPurpleAlloy() {
        return new MaterialBuilder().setName("PurpleAlloy")
            .setDefaultLocalName("Purple Alloy")
            .setARGB(0x0064b4ff)
            .constructMaterial();
    }

    private static Materials loadRubberTreeSap() {
        return new MaterialBuilder().setName("RubberTreeSap")
            .setDefaultLocalName("Rubber Tree Sap")
            .constructMaterial();
    }

    private static Materials loadSerpentine() {
        return new MaterialBuilder().setName("Serpentine")
            .setDefaultLocalName("Serpentine")
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadSodiumPeroxide() {
        return new MaterialBuilder().setName("SodiumPeroxide")
            .setDefaultLocalName("Sodium Peroxide")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadTeslatite() {
        return new MaterialBuilder().setName("Teslatite")
            .setDefaultLocalName("Teslatite")
            .setARGB(0x003cb4c8)
            .addDustItems()
            .setOreMultiplier(5) // No .addOreItems?
            .addOreByproduct(() -> Materials.Diamond)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .addSubTag(SubTag.STONE)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static void loadUnknownComponents() {
        Materials.Adamantium = loadAdamantium();
        Materials.Adamite = loadAdamite();
        Materials.Adluorite = loadAdluorite();
        Materials.Agate = loadAgate();
        Materials.Alduorite = loadAlduorite();
        Materials.Amber = loadAmber();
        Materials.Ammonium = loadAmmonium();
        Materials.Amordrine = loadAmordrine();
        Materials.AncientDebris = loadAncientDebris();
        Materials.Andesite = loadAndesite();
        Materials.Ardite = loadArdite();
        Materials.Aredrite = loadAredrite();
        Materials.Bitumen = loadBitumen();
        Materials.Black = loadBlack();
        Materials.Blizz = loadBlizz();
        Materials.Bloodstone = loadBloodstone();
        Materials.Bluestone = loadBluestone();
        Materials.Blutonium = loadBlutonium();
        Materials.CertusQuartz = loadCertusQuartz();
        Materials.CertusQuartzCharged = loadCertusQuartzCharged();
        Materials.Ceruclase = loadCeruclase();
        Materials.Chimerite = loadChimerite();
        Materials.Chrysocolla = loadChrysocolla();
        Materials.Citrine = loadCitrine();
        Materials.CobaltHexahydrate = loadCobaltHexahydrate();
        Materials.ConstructionFoam = loadConstructionFoam();
        Materials.Coral = loadCoral();
        Materials.CrudeOil = loadCrudeOil();
        Materials.CrystalFlux = loadCrystalFlux();
        Materials.Cyanite = loadCyanite();
        Materials.DarkIron = loadDarkIron();
        Materials.DarkStone = loadDarkStone();
        Materials.Demonite = loadDemonite();
        Materials.Desichalkos = loadDesichalkos();
        Materials.Dilithium = loadDilithium();
        Materials.Draconic = loadDraconic();
        Materials.Drulloy = loadDrulloy();
        Materials.Duranium = loadDuranium();
        Materials.ElectrumFlux = loadElectrumFlux();
        Materials.Emery = loadEmery();
        Materials.EnderiumBase = loadEnderiumBase();
        Materials.Energized = loadEnergized();
        Materials.FierySteel = loadFierySteel();
        Materials.Firestone = loadFirestone();
        Materials.Fluorite = loadFluorite();
        Materials.Force = loadForce();
        Materials.Forcicium = loadForcicium();
        Materials.Forcillium = loadForcillium();
        Materials.Glowstone = loadGlowstone();
        Materials.Graphene = loadGraphene();
        Materials.Graphite = loadGraphite();
        Materials.Greenstone = loadGreenstone();
        Materials.Hematite = loadHematite();
        Materials.HSLA = loadHSLA();
        Materials.Infernal = loadInfernal();
        Materials.InfusedAir = loadInfusedAir();
        Materials.InfusedDull = loadInfusedDull();
        Materials.InfusedEarth = loadInfusedEarth();
        Materials.InfusedEntropy = loadInfusedEntropy();
        Materials.InfusedFire = loadInfusedFire();
        Materials.InfusedGold = loadInfusedGold();
        Materials.InfusedOrder = loadInfusedOrder();
        Materials.InfusedVis = loadInfusedVis();
        Materials.InfusedWater = loadInfusedWater();
        Materials.Invisium = loadInvisium();
        Materials.Jade = loadJade();
        Materials.Lava = loadLava();
        Materials.Limestone = loadLimestone();
        Materials.Magma = loadMagma();
        Materials.Mawsitsit = loadMawsitsit();
        Materials.Mercassium = loadMercassium();
        Materials.MeteoricSteel = loadMeteoricSteel();
        Materials.Meteorite = loadMeteorite();
        Materials.Migmatite = loadMigmatite();
        Materials.Mimichite = loadMimichite();
        Materials.Moonstone = loadMoonstone();
        Materials.NaquadahAlloy = loadNaquadahAlloy();
        Materials.NaquadahEnriched = loadNaquadahEnriched();
        Materials.Naquadria = loadNaquadria();
        Materials.Nether = loadNether();
        Materials.NetherBrick = loadNetherBrick();
        Materials.NetherQuartz = loadNetherQuartz();
        Materials.NetherStar = loadNetherStar();
        Materials.ObsidianFlux = loadObsidianFlux();
        Materials.Oilsands = loadOilsands();
        Materials.Onyx = loadOnyx();
        Materials.Orichalcum = loadOrichalcum();
        Materials.Osmonium = loadOsmonium();
        Materials.Painite = loadPainite();
        Materials.Peanutwood = loadPeanutwood();
        Materials.Petroleum = loadPetroleum();
        Materials.Pewter = loadPewter();
        Materials.Phoenixite = loadPhoenixite();
        Materials.Quartzite = loadQuartzite();
        Materials.Randomite = loadRandomite();
        Materials.Rubracium = loadRubracium();
        Materials.Sand = loadSand();
        Materials.Siltstone = loadSiltstone();
        Materials.Spinel = loadSpinel();
        Materials.Sunstone = loadSunstone();
        Materials.Tar = loadTar();
        Materials.Tartarite = loadTartarite();
        Materials.UUAmplifier = loadUUAmplifier();
        Materials.UUMatter = loadUUMatter();
        Materials.Void = loadVoid();
        Materials.Voidstone = loadVoidstone();
        Materials.Vulcanite = loadVulcanite();
        Materials.Vyroxeres = loadVyroxeres();
        Materials.Yellorium = loadYellorium();
        Materials.Zectium = loadZectium();
    }

    private static Materials loadAdamantium() {
        return new MaterialBuilder().setName("Adamantium")
            .setDefaultLocalName("Adamantium")
            .setChemicalFormula("Ad")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setTool(8_192, 10, 32.0f)
            .setTurbine(1.0f, 5.0f, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(7_200)
            .setBlastFurnaceTemp(7_200)
            .setBlastFurnaceRequired(true)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadAdamite() {
        return new MaterialBuilder().setName("Adamite")
            .setDefaultLocalName("Adamite")
            .setColor(Dyes.dyeLightGray)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadAdluorite() {
        return new MaterialBuilder().setName("Adluorite")
            .setDefaultLocalName("Adluorite")
            .setColor(Dyes.dyeLightBlue)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadAgate() {
        return new MaterialBuilder().setName("Agate")
            .setDefaultLocalName("Agate")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadAlduorite() {
        return new MaterialBuilder().setName("Alduorite")
            .setDefaultLocalName("Alduorite")
            .setChemicalFormula("SpAl")
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x009fb4b4)
            .setTool(8_192, 1, 32.0f)
            .setTurbine(6.0f, 1.0f, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(6_600)
            .setBlastFurnaceTemp(6_600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadAmber() {
        return new MaterialBuilder().setName("Amber")
            .setDefaultLocalName("Amber")
            .setIconSet(TextureSet.SET_RUBY)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x7fff8000)
            .setTool(128, 2, 4.0f)
            .setToolEnchantment(() -> Enchantment.silkTouch, 1)
            .setFuel(MaterialBuilder.FuelType.Magic, 32)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addToolHeadItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 10)
            .addMaterial(Materials.Hydrogen, 10)
            .addMaterial(Materials.Oxygen, 16)
            .addAspect(TCAspects.VINCULUM, 2)
            .addAspect(TCAspects.VITREUS, 1)
            .addOreByproduct(() -> Materials.Amber)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadAmmonium() {
        return new MaterialBuilder().setName("Ammonium")
            .setDefaultLocalName("Ammonium")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadAmordrine() {
        return new MaterialBuilder().setName("Amordrine")
            .setDefaultLocalName("Amordrine")
            .setTool(64, 2, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadAncientDebris() {
        return new MaterialBuilder().setName("Debris")
            .setDefaultLocalName("Ancient Debris")
            .setARGB(0x351a0b)
            .constructMaterial();
    }

    private static Materials loadAndesite() {
        return new MaterialBuilder().setName("Andesite")
            .setDefaultLocalName("Andesite")
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadArdite() {
        return new MaterialBuilder().setName("Ardite")
            .setDefaultLocalName("Ardite")
            .setChemicalFormula("Ai")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00fa8100)
            .setTool(1_024, 4, 18.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_600)
            .setBlastFurnaceTemp(1_600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadAredrite() {
        return new MaterialBuilder().setName("Aredrite")
            .setDefaultLocalName("Aredrite")
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ff0000)
            .setTool(64, 2, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadBitumen() {
        return new MaterialBuilder().setName("Bitumen")
            .setDefaultLocalName("Bitumen")
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadBlack() {
        return new MaterialBuilder().setName("Black")
            .setDefaultLocalName("Black")
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00000000)
            .constructMaterial();
    }

    private static Materials loadBlizz() {
        return new MaterialBuilder().setName("Blizz")
            .setDefaultLocalName("Blizz")
            .setChemicalFormula("❆Ma")
            .setIconSet(TextureSet.SET_BLIZZ)
            .setARGB(0x00dce9ff)
            .addDustItems()
            .setAutoGeneratedRecycleRecipes(false)
            .constructMaterial();
    }

    private static Materials loadBloodstone() {
        return new MaterialBuilder().setName("Bloodstone")
            .setDefaultLocalName("Bloodstone")
            .setColor(Dyes.dyeRed)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadBluestone() {
        return new MaterialBuilder().setName("Bluestone")
            .setDefaultLocalName("Bluestone")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlue)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadBlutonium() {
        return new MaterialBuilder().setName("Blutonium")
            .setDefaultLocalName("Blutonium")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x000000ff)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadCertusQuartz() {
        return new MaterialBuilder().setName("CertusQuartz")
            .setDefaultLocalName("Certus Quartz")
            .setChemicalFormula("SiO₂")
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00d2d2e6)
            .setTool(32, 1, 5.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addToolHeadItems()
            .addAspect(TCAspects.POTENTIA, 1)
            .addAspect(TCAspects.VITREUS, 1)
            .addOreByproduct(() -> Materials.Quartzite)
            .addOreByproduct(() -> Materials.Barite)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.QUARTZ)
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadCertusQuartzCharged() {
        return new MaterialBuilder().setName("ChargedCertusQuartz")
            .setDefaultLocalName("Charged Certus Quartz")
            .setChemicalFormula("SiO₂" + CustomGlyphs.HIGH_VOLTAGE)
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00ddddec)
            .setTool(32, 1, 5.0f)
            .addOreItems()
            .setOreMultiplier(2)
            .addAspect(TCAspects.POTENTIA, 1)
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.ELECTRUM, 1)
            .addOreByproduct(() -> Materials.CertusQuartz)
            .addOreByproduct(() -> Materials.Quartzite)
            .addOreByproduct(() -> Materials.Barite)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.QUARTZ)
            .removeOrePrefix(OrePrefixes.dust) // dreamcraft:item.ChargedCertusQuartzDust
            .constructMaterial();
    }

    private static Materials loadCeruclase() {
        return new MaterialBuilder().setName("Ceruclase")
            .setDefaultLocalName("Ceruclase")
            .setChemicalFormula("SpAg")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x008cbdd0)
            .setTool(1_280, 2, 32.0f)
            .setTurbine(1.0f, 22.0f, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(6_600)
            .setBlastFurnaceTemp(6_600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadChimerite() {
        return new MaterialBuilder().setName("Chimerite")
            .setDefaultLocalName("Chimerite")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadChrysocolla() {
        return new MaterialBuilder().setName("Chrysocolla")
            .setDefaultLocalName("Chrysocolla")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCitrine() {
        return new MaterialBuilder().setName("Citrine")
            .setDefaultLocalName("Citrine")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCobaltHexahydrate() {
        return new MaterialBuilder().setName("CobaltHexahydrate")
            .setDefaultLocalName("Cobalt Hexahydrate")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x005050fa)
            .addDustItems()
            .addCell()
            .constructMaterial();
    }

    private static Materials loadConstructionFoam() {
        return new MaterialBuilder().setName("ConstructionFoam")
            .setDefaultLocalName("Construction Foam")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00808080)
            .addDustItems()
            .addCell()
            .addToolHeadItems()
            .addGearItems()
            .addSubTag(SubTag.STONE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.EXPLOSIVE)
            .addSubTag(SubTag.NO_SMELTING)
            .removeOrePrefix(OrePrefixes.cell) // IC2:itemCellEmpty:4
            .constructMaterial();
    }

    private static Materials loadCoral() {
        return new MaterialBuilder().setName("Coral")
            .setDefaultLocalName("Coral")
            .setARGB(0x00ff80ff)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCrudeOil() {
        return new MaterialBuilder().setName("CrudeOil")
            .setDefaultLocalName("Crude Oil")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000a0a0a)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCrystalFlux() {
        return new MaterialBuilder().setName("CrystalFlux")
            .setDefaultLocalName("Flux Crystal")
            .setIconSet(TextureSet.SET_QUARTZ)
            .setARGB(0x00643264)
            .addDustItems()
            .addGemItems()
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadCyanite() {
        return new MaterialBuilder().setName("Cyanite")
            .setDefaultLocalName("Cyanite")
            .setColor(Dyes.dyeCyan)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDarkIron() {
        return new MaterialBuilder().setName("DarkIron")
            .setDefaultLocalName("Deep Dark Iron")
            .setChemicalFormula("Sp₆Fe" + CustomGlyphs.PICKAXE)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setARGB(0x0037283c)
            .setTool(384, 3, 7.0f)
            .setToolEnchantment(() -> Enchantment.fireAspect, 2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addOreByproduct(() -> Materials.Iron)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadDarkStone() {
        return new MaterialBuilder().setName("DarkStone")
            .setDefaultLocalName("Dark Stone")
            .setColor(Dyes.dyeBlack)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDemonite() {
        return new MaterialBuilder().setName("Demonite")
            .setDefaultLocalName("Demonite")
            .setColor(Dyes.dyeRed)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDesichalkos() {
        return new MaterialBuilder().setName("Desichalkos")
            .setDefaultLocalName("Desichalkos")
            .setTool(1_280, 3, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadDilithium() {
        return new MaterialBuilder().setName("Dilithium")
            .setDefaultLocalName("Dilithium")
            .setChemicalFormula("∳Li∳Li∳")
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x7ffffafa)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addCell()
            .addOreByproduct(() -> Materials.Dilithium)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.QUARTZ)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadDraconic() {
        return new MaterialBuilder().setName("Draconic")
            .setDefaultLocalName("Draconic")
            .setColor(Dyes.dyeRed)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDrulloy() {
        return new MaterialBuilder().setName("Drulloy")
            .setDefaultLocalName("Drulloy")
            .setColor(Dyes.dyeRed)
            .addDustItems()
            .addCell()
            .constructMaterial();
    }

    private static Materials loadDuranium() {
        return new MaterialBuilder().setName("Duranium")
            .setDefaultLocalName("Duranium")
            .setChemicalFormula("Du")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightGray)
            .setTool(40_960, 11, 32.0f)
            .setTurbine(16.0f, 16.0f, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadElectrumFlux() {
        return new MaterialBuilder().setName("ElectrumFlux")
            .setDefaultLocalName("Fluxed Electrum")
            .setChemicalFormula("The formula is too long...")
            .setIconSet(TextureSet.SET_FLUXED)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffffff)
            .setTool(512, 3, 16.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(9_000)
            .setBlastFurnaceTemp(9_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadEmery() {
        return new MaterialBuilder().setName("Emery")
            .setDefaultLocalName("Emery")
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadEnderiumBase() {
        return new MaterialBuilder().setName("EnderiumBase")
            .setDefaultLocalName("Enderium Base")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x00487799)
            .setTool(768, 4, 16.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3_600)
            .setBlastFurnaceTemp(3_600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Tin, 2)
            .addMaterial(Materials.Silver, 2)
            .addMaterial(Materials.Platinum, 2)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ALIENIS, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadEnergized() {
        return new MaterialBuilder().setName("Energized")
            .setDefaultLocalName("Energized")
            .constructMaterial();
    }

    private static Materials loadFierySteel() {
        return new MaterialBuilder().setName("FierySteel")
            .setDefaultLocalName("Fiery Steel")
            .setChemicalFormula(CustomGlyphs.BRIMSTONE + "Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C")
            .setIconSet(TextureSet.SET_FIERY)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00400000)
            .setTool(256, 3, 8.0f)
            .setToolEnchantment(() -> Enchantment.fireAspect, 3)
            .setFuel(MaterialBuilder.FuelType.Magic, 2_048)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_811)
            .setBlastFurnaceTemp(1_800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addAspect(TCAspects.PRAECANTATIO, 3)
            .addAspect(TCAspects.IGNIS, 3)
            .addAspect(TCAspects.CORPUS, 3)
            .addSubTag(SubTag.BURNING)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadFirestone() {
        return new MaterialBuilder().setName("Firestone")
            .setDefaultLocalName("Firestone")
            .setChemicalFormula("⽕")
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00c81400)
            .setTool(1_280, 3, 6.0f)
            .setToolEnchantment(() -> Enchantment.fireAspect, 3)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .setHeatDamage(5.0f)
            .addSubTag(SubTag.BURNING)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.QUARTZ)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadFluorite() {
        return new MaterialBuilder().setName("Fluorite")
            .setDefaultLocalName("Fluorite")
            .setColor(Dyes.dyeGreen)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadForce() {
        return new MaterialBuilder().setName("Force")
            .setDefaultLocalName("Force")
            .setChemicalFormula("Fc⚙")
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .setTool(128, 3, 10.0f)
            .setToolEnchantment(() -> Enchantment.silkTouch, 1)
            .addDustItems()
            .addMetalItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.POTENTIA, 5)
            .addOreByproduct(() -> Materials.Force)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadForcicium() {
        return new MaterialBuilder().setName("Forcicium")
            .setDefaultLocalName("Forcicium")
            .setChemicalFormula("◃◁◀")
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x00323246)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addCell()
            .addAspect(TCAspects.POTENTIA, 2)
            .addOreByproduct(() -> Materials.Thorium)
            .addOreByproduct(() -> Materials.Neodymium)
            .addOreByproduct(() -> Materials.RareEarth)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadForcillium() {
        return new MaterialBuilder().setName("Forcillium")
            .setDefaultLocalName("Forcillium")
            .setChemicalFormula("▶▷▹")
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x00323246)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addCell()
            .addAspect(TCAspects.POTENTIA, 2)
            .addOreByproduct(() -> Materials.Thorium)
            .addOreByproduct(() -> Materials.Neodymium)
            .addOreByproduct(() -> Materials.RareEarth)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadGlowstone() {
        return new MaterialBuilder().setName("Glowstone")
            .setDefaultLocalName("Glowstone")
            .setIconSet(TextureSet.SET_GLOWSTONE)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .addDustItems()
            .setOreMultiplier(5) // No .addOreItems?
            .addCell()
            .addAspect(TCAspects.LUX, 2)
            .addAspect(TCAspects.SENSUS, 1)
            .addOreByproduct(() -> Materials.Redstone)
            .addOreByproduct(() -> Materials.Gold)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .addSubTag(SubTag.STONE)
            .addSubTag(SubTag.UNBURNABLE)
            .addOrePrefix(OrePrefixes.nanite)
            .addOrePrefix(OrePrefixes.plate)
            .removeOrePrefix(OrePrefixes.dust) // minecraft:glowstone_dust
            .constructMaterial();
    }

    private static Materials loadGraphene() {
        return new MaterialBuilder().setName("Graphene")
            .setDefaultLocalName("Graphene")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00808080)
            .setTool(32, 1, 6.0f)
            .addDustItems()
            .addToolHeadItems()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.VITREUS, 2)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadGraphite() {
        return new MaterialBuilder().setName("Graphite")
            .setDefaultLocalName("Graphite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00808080)
            .setTool(32, 2, 5.0f)
            .addDustItems()
            .addOreItems()
            .addCell()
            .addToolHeadItems()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.VITREUS, 2)
            .addAspect(TCAspects.IGNIS, 1)
            .addOreByproduct(() -> Materials.Carbon)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadGreenstone() {
        return new MaterialBuilder().setName("Greenstone")
            .setDefaultLocalName("Greenstone")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGreen)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadHematite() {
        return new MaterialBuilder().setName("Hematite")
            .setDefaultLocalName("Hematite")
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadHSLA() {
        return new MaterialBuilder().setName("HSLA")
            .setDefaultLocalName("HSLA Steel")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00808080)
            .setTool(500, 3, 6.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_811)
            .setBlastFurnaceTemp(1_000)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 1)
            .addAspect(TCAspects.ORDO, 1)
            .addOreByproduct(() -> Materials.Iron)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .constructMaterial();
    }

    private static Materials loadInfernal() {
        return new MaterialBuilder().setName("Infernal")
            .setDefaultLocalName("Infernal")
            .constructMaterial();
    }

    private static Materials loadInfusedAir() {
        return new MaterialBuilder().setName("InfusedAir")
            .setDefaultLocalName("Aer")
            .setChemicalFormula(CustomGlyphs.AIR)
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .setTool(64, 3, 8.0f)
            .setToolEnchantment(() -> Enchantment.knockback, 2)
            .setArmorEnchantment(() -> Enchantment.respiration, 3)
            .setFuel(MaterialBuilder.FuelType.Magic, 160)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.AER, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadInfusedDull() {
        return new MaterialBuilder().setName("InfusedDull")
            .setDefaultLocalName("Vacuus")
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00646464)
            .setTool(64, 3, 32.0f)
            .setArmorEnchantment(() -> Enchantment.blastProtection, 4)
            .setFuel(MaterialBuilder.FuelType.Magic, 160)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.VACUOS, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadInfusedEarth() {
        return new MaterialBuilder().setName("InfusedEarth")
            .setDefaultLocalName("Terra")
            .setChemicalFormula(CustomGlyphs.EARTH)
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x0000ff00)
            .setTool(256, 3, 8.0f)
            .setToolEnchantment(() -> Enchantment.baneOfArthropods, 5)
            .setArmorEnchantment(() -> Enchantment.protection, 4)
            .setFuel(MaterialBuilder.FuelType.Magic, 160)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.TERRA, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadInfusedEntropy() {
        return new MaterialBuilder().setName("InfusedEntropy")
            .setDefaultLocalName("Perditio")
            .setChemicalFormula(CustomGlyphs.CHAOS)
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x003e3e3e)
            .setTool(64, 4, 32.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 5)
            .setArmorEnchantment(() -> Enchantment.thorns, 3)
            .setFuel(MaterialBuilder.FuelType.Magic, 320)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.PERDITIO, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadInfusedFire() {
        return new MaterialBuilder().setName("InfusedFire")
            .setDefaultLocalName("Ignis")
            .setChemicalFormula(CustomGlyphs.FIRE)
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00ff0000)
            .setTool(64, 3, 8.0f)
            .setToolEnchantment(() -> Enchantment.fireAspect, 3)
            .setArmorEnchantment(() -> Enchantment.featherFalling, 4)
            .setFuel(MaterialBuilder.FuelType.Magic, 320)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.IGNIS, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadInfusedGold() {
        return new MaterialBuilder().setName("InfusedGold")
            .setDefaultLocalName("Infused Gold")
            .setChemicalFormula("AuMa*")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffc83c)
            .setTool(64, 3, 12.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .addOreByproduct(() -> Materials.Gold)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadInfusedOrder() {
        return new MaterialBuilder().setName("InfusedOrder")
            .setDefaultLocalName("Ordo")
            .setChemicalFormula(CustomGlyphs.ORDER)
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fcfcfc)
            .setTool(64, 3, 8.0f)
            .setToolEnchantment(() -> Enchantment.silkTouch, 1)
            .setArmorEnchantment(() -> Enchantment.projectileProtection, 4)
            .setFuel(MaterialBuilder.FuelType.Magic, 240)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.ORDO, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadInfusedVis() {
        return new MaterialBuilder().setName("InfusedVis")
            .setDefaultLocalName("Auram")
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00ff00ff)
            .setTool(64, 3, 8.0f)
            .setToolEnchantment(() -> Enchantment.smite, 5)
            .setArmorEnchantment(() -> Enchantment.protection, 4)
            .setFuel(MaterialBuilder.FuelType.Magic, 240)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.AURAM, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadInfusedWater() {
        return new MaterialBuilder().setName("InfusedWater")
            .setDefaultLocalName("Aqua")
            .setChemicalFormula(CustomGlyphs.WATER)
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x000000ff)
            .setTool(64, 3, 8.0f)
            .setToolEnchantment(() -> Enchantment.fortune, 3)
            .setArmorEnchantment(() -> Enchantment.aquaAffinity, 1)
            .setFuel(MaterialBuilder.FuelType.Magic, 160)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.AQUA, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadInvisium() {
        return new MaterialBuilder().setName("Invisium")
            .setDefaultLocalName("Invisium")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadJade() {
        return new MaterialBuilder().setName("Jade")
            .setDefaultLocalName("Jade")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x00006400)
            .setTool(16, 2, 1.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Oxygen, 6)
            .addAspect(TCAspects.LUCRUM, 6)
            .addAspect(TCAspects.VITREUS, 3)
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadLava() {
        return new MaterialBuilder().setName("Lava")
            .setDefaultLocalName("Lava")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff4000)
            .addCell()
            .setHeatDamage(3.0f)
            .removeOrePrefix(OrePrefixes.cell) // IC2:itemCellEmpty:2
            .removeOrePrefix(OrePrefixes.bucket) // minecraft:lava_bucket
            .removeOrePrefix(OrePrefixes.bucketClay) // IguanaTweaksTConstruct:clayBucketLava
            .constructMaterial();
    }

    private static Materials loadLimestone() {
        return new MaterialBuilder().setName("Limestone")
            .setDefaultLocalName("Limestone")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMagma() {
        return new MaterialBuilder().setName("Magma")
            .setDefaultLocalName("Magma")
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff4000)
            .constructMaterial();
    }

    private static Materials loadMawsitsit() {
        return new MaterialBuilder().setName("Mawsitsit")
            .setDefaultLocalName("Mawsitsit")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMercassium() {
        return new MaterialBuilder().setName("Mercassium")
            .setDefaultLocalName("Mercassium")
            .setTool(64, 1, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadMeteoricSteel() {
        return new MaterialBuilder().setName("MeteoricSteel")
            .setDefaultLocalName("Meteoric Steel")
            .setChemicalFormula("SpFe₅" + CustomGlyphs.SUBSCRIPT0 + "C")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00321928)
            .setTool(768, 4, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .setMeltingPoint(1_811)
            .setBlastFurnaceTemp(1_000)
            .setBlastFurnaceRequired(true)
            .setDensity(51, 50)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.MeteoricIron, 50)
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .addAspect(TCAspects.ORDO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadMeteorite() {
        return new MaterialBuilder().setName("Meteorite")
            .setDefaultLocalName("Meteorite")
            .setColor(Dyes.dyePurple)
            .setARGB(0x0050233c)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadMigmatite() {
        return new MaterialBuilder().setName("Migmatite")
            .setDefaultLocalName("Migmatite")
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMimichite() {
        return new MaterialBuilder().setName("Mimichite")
            .setDefaultLocalName("Mimichite")
            .setIconSet(TextureSet.SET_GEM_VERTICAL)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadMoonstone() {
        return new MaterialBuilder().setName("Moonstone")
            .setDefaultLocalName("Moonstone")
            .setColor(Dyes.dyeWhite)
            .addDustItems()
            .addOreItems()
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.ALIENIS, 1)
            .constructMaterial();
    }

    private static Materials loadNaquadahAlloy() {
        return new MaterialBuilder().setName("NaquadahAlloy")
            .setDefaultLocalName("Naquadah Alloy")
            .setChemicalFormula("Nq₂KeC")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00282828)
            .setTool(5_120, 5, 8.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(7_200)
            .setBlastFurnaceTemp(7_200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 4)
            .addAspect(TCAspects.NEBRISUM, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadNaquadahEnriched() {
        return new MaterialBuilder().setName("NaquadahEnriched")
            .setDefaultLocalName("Enriched Naquadah")
            .setChemicalFormula("Nq+")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00323232)
            .setMoltenARGB(0x0040ff40)
            .setTool(1_280, 4, 6.0f)
            .setToolEnchantment(() -> EnchantmentRadioactivity.INSTANCE, 4)
            .setArmorEnchantment(() -> EnchantmentRadioactivity.INSTANCE, 4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .setMeltingPoint(4_500)
            .setBlastFurnaceTemp(4_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 3)
            .addAspect(TCAspects.RADIO, 2)
            .addAspect(TCAspects.NEBRISUM, 2)
            .addOreByproduct(() -> Materials.Naquadah)
            .addOreByproduct(() -> Materials.Naquadria)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadNaquadria() {
        return new MaterialBuilder().setName("Naquadria")
            .setDefaultLocalName("Naquadria")
            .setChemicalFormula("Nq*")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x001e1e1e)
            .setMoltenARGB(0x0080ff80)
            .setTool(512, 4, 1.0f)
            .setToolEnchantment(() -> EnchantmentRadioactivity.INSTANCE, 5)
            .setArmorEnchantment(() -> EnchantmentRadioactivity.INSTANCE, 5)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(9_000)
            .setBlastFurnaceTemp(9_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 4)
            .addAspect(TCAspects.RADIO, 3)
            .addAspect(TCAspects.NEBRISUM, 3)
            .addOreByproduct(() -> Materials.Naquadria)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadNether() {
        return new MaterialBuilder().setName("Nether")
            .setDefaultLocalName("Nether")
            .constructMaterial();
    }

    private static Materials loadNetherBrick() {
        return new MaterialBuilder().setName("NetherBrick")
            .setDefaultLocalName("Nether Brick")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00640000)
            .addDustItems()
            .setMaceratingInto(() -> Materials.Netherrack)
            .addAspect(TCAspects.IGNIS, 1)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STONE)
            .constructMaterial();
    }

    private static Materials loadNetherQuartz() {
        return new MaterialBuilder().setName("NetherQuartz")
            .setDefaultLocalName("Nether Quartz")
            .setChemicalFormula("SiO₂")
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00e6d2d2)
            .setTool(32, 1, 1.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addToolHeadItems()
            .addAspect(TCAspects.POTENTIA, 1)
            .addAspect(TCAspects.VITREUS, 1)
            .addOreByproduct(() -> Materials.Netherrack)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.QUARTZ)
            .removeOrePrefix(OrePrefixes.gem) // minecraft:quartz
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadNetherStar() {
        return new MaterialBuilder().setName("NetherStar")
            .setDefaultLocalName("Nether Star")
            .setChemicalFormula("(Nh₂Ma)₃" + CustomGlyphs.CIRCLE_CROSS + "C₆")
            .setIconSet(TextureSet.SET_NETHERSTAR)
            .setColor(Dyes.dyeWhite)
            .setTool(5_120, 4, 6.0f)
            .setToolEnchantment(() -> Enchantment.silkTouch, 1)
            .setFuel(MaterialBuilder.FuelType.Magic, 50_000)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.UNBURNABLE)
            .removeOrePrefix(OrePrefixes.gem) // minecraft:nether_star
            .constructMaterial();
    }

    private static Materials loadObsidianFlux() {
        return new MaterialBuilder().setName("ObsidianFlux")
            .setDefaultLocalName("Fluxed Obsidian")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00503264)
            .addDustItems()
            .addMetalItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadOilsands() {
        return new MaterialBuilder().setName("Oilsands")
            .setDefaultLocalName("Oilsands")
            .setARGB(0x000a0a0a)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadOnyx() {
        return new MaterialBuilder().setName("Onyx")
            .setDefaultLocalName("Onyx")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadOrichalcum() {
        return new MaterialBuilder().setName("Orichalcum")
            .setDefaultLocalName("Orichalcum")
            .setChemicalFormula("SpBi")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00547a38)
            .setTool(20_480, 1, 32.0f)
            .setTurbine(1.0f, 6.0f, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(6_000)
            .setBlastFurnaceTemp(6_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadOsmonium() {
        return new MaterialBuilder().setName("Osmonium")
            .setDefaultLocalName("Osmonium")
            .setColor(Dyes.dyeBlue)
            .setTool(64, 1, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadPainite() {
        return new MaterialBuilder().setName("Painite")
            .setDefaultLocalName("Painite")
            .constructMaterial();
    }

    private static Materials loadPeanutwood() {
        return new MaterialBuilder().setName("Peanutwood")
            .setDefaultLocalName("Peanut Wood")
            .setMaceratingInto(() -> Materials.Wood)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.WOOD)
            .constructMaterial();
    }

    private static Materials loadPetroleum() {
        return new MaterialBuilder().setName("Petroleum")
            .setDefaultLocalName("Petroleum")
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadPewter() {
        return new MaterialBuilder().setName("Pewter")
            .setDefaultLocalName("Pewter")
            .constructMaterial();
    }

    private static Materials loadPhoenixite() {
        return new MaterialBuilder().setName("Phoenixite")
            .setDefaultLocalName("Phoenixite")
            .setTool(64, 1, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadQuartzite() {
        return new MaterialBuilder().setName("Quartzite")
            .setDefaultLocalName("Quartzite")
            .setChemicalFormula("SiO₂")
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00d2e6d2)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addOreByproduct(() -> Materials.CertusQuartz)
            .addOreByproduct(() -> Materials.Barite)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.QUARTZ)
            .addOrePrefix(OrePrefixes.dustImpure)
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadRandomite() {
        return new MaterialBuilder().setName("Randomite")
            .setDefaultLocalName("Randomite")
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadRubracium() {
        return new MaterialBuilder().setName("Rubracium")
            .setDefaultLocalName("Rubracium")
            .setChemicalFormula("SpRb")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00972d2d)
            .setTool(128, 1, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .addOreByproduct(() -> Materials.Samarium)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadSand() {
        return new MaterialBuilder().setName("Sand")
            .setDefaultLocalName("Sand")
            .setColor(Dyes.dyeYellow)
            .setSmeltingInto(() -> Materials.Glass)
            .addSubTag(SubTag.NO_RECYCLING)
            .constructMaterial();
    }

    private static Materials loadSiltstone() {
        return new MaterialBuilder().setName("Siltstone")
            .setDefaultLocalName("Siltstone")
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadSpinel() {
        return new MaterialBuilder().setName("FoolsRuby")
            .setDefaultLocalName("Spinel")
            .setIconSet(TextureSet.SET_RUBY)
            .setColor(Dyes.dyeRed)
            .setARGB(0x7fff6464)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Oxygen, 4)
            .addAspect(TCAspects.LUCRUM, 2)
            .addAspect(TCAspects.VITREUS, 2)
            .addOreByproduct(() -> Materials.Jasper)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadSunstone() {
        return new MaterialBuilder().setName("Sunstone")
            .setDefaultLocalName("Sunstone")
            .setColor(Dyes.dyeYellow)
            .addDustItems()
            .addOreItems()
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.ALIENIS, 1)
            .constructMaterial();
    }

    private static Materials loadTar() {
        return new MaterialBuilder().setName("Tar")
            .setDefaultLocalName("Tar")
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000a0a0a)
            .constructMaterial();
    }

    private static Materials loadTartarite() {
        return new MaterialBuilder().setName("Tartarite")
            .setDefaultLocalName("Tartarite")
            .setChemicalFormula("Tt")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00ff763c)
            .setTool(20_480, 3, 32.0f)
            .setTurbine(1_120.0f, 1_120.0f, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .setMeltingPoint(10_400)
            .setBlastFurnaceTemp(10_400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadUUAmplifier() {
        return new MaterialBuilder().setName("UUAmplifier")
            .setDefaultLocalName("UU-Amplifier")
            .setChemicalFormula("Accelerates the Mass Fabricator")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePink)
            .setARGB(0x00600080)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadUUMatter() {
        return new MaterialBuilder().setName("UUMatter")
            .setDefaultLocalName("UU-Matter")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePink)
            .setARGB(0x008000c4)
            .addCell()
            .removeOrePrefix(OrePrefixes.cell) // IC2:itemCellEmpty:3
            .constructMaterial();
    }

    private static Materials loadVoid() {
        return new MaterialBuilder().setName("Void")
            .setDefaultLocalName("Void")
            .setChemicalFormula("ShFeMa₃")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x001c0639)
            .setTool(512, 4, 32.0f)
            .setFuel(MaterialBuilder.FuelType.Magic, 1_500)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setDensity(2, 1)
            .addAspect(TCAspects.VACUOS, 1)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadVoidstone() {
        return new MaterialBuilder().setName("Voidstone")
            .setDefaultLocalName("Voidstone")
            .setARGB(0xc8ffffff)
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.VACUOS, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadVulcanite() {
        return new MaterialBuilder().setName("Vulcanite")
            .setDefaultLocalName("Vulcanite")
            .setChemicalFormula("SpCu")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00ff8448)
            .setTool(20_480, 2, 32.0f)
            .setTurbine(40.0f, 1.0f, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(8_400)
            .setBlastFurnaceTemp(8_400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadVyroxeres() {
        return new MaterialBuilder().setName("Vyroxeres")
            .setDefaultLocalName("Vyroxeres")
            .setChemicalFormula("SpBe")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x0055e001)
            .setTool(7_680, 1, 32.0f)
            .setTurbine(1.0f, 3.0f, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .setMeltingPoint(5_400)
            .setBlastFurnaceTemp(5_400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadYellorium() {
        return new MaterialBuilder().setName("Yellorium")
            .setDefaultLocalName("Yellorium")
            .setColor(Dyes.dyeYellow)
            .addDustItems()
            .addMetalItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadZectium() {
        return new MaterialBuilder().setName("Zectium")
            .setDefaultLocalName("Zectium")
            .setColor(Dyes.dyeBlack)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static void loadTiers() {
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

    private static void loadCircuitry() {
        Materials.Resistor = loadResistor();
        Materials.Diode = loadDiode();
        Materials.Transistor = loadTransistor();
        Materials.Capacitor = loadCapacitor();
        Materials.Inductor = loadInductor();
        Materials.Nano = loadNano();
        Materials.Piko = loadPiko();
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

    private static Materials loadNano() {
        return new MaterialBuilder().setName("Nano")
            .setDefaultLocalName("Bio")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 11)
            .constructMaterial();
    }

    private static Materials loadPiko() {
        return new MaterialBuilder().setName("Piko")
            .setDefaultLocalName("Bio")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 12)
            .constructMaterial();
    }

    private static void loadNotExact() {
        Materials.Biomass = loadBiomass();
        Materials.CharcoalByproducts = loadCharcoalByproducts();
        Materials.Cheese = loadCheese();
        Materials.Chili = loadChili();
        Materials.Chocolate = loadChocolate();
        Materials.Cluster = loadCluster();
        Materials.CoalFuel = loadCoalFuel();
        Materials.Cocoa = loadCocoa();
        Materials.Coffee = loadCoffee();
        Materials.Creosote = loadCreosote();
        Materials.Diesel = loadDiesel();
        Materials.Ethanol = loadEthanol();
        Materials.FermentedBiomass = loadFermentedBiomass();
        Materials.FishOil = loadFishOil();
        Materials.FryingOilHot = loadFryingOilHot();
        Materials.Glue = loadGlue();
        Materials.GlueAdvanced = loadGlueAdvanced();
        Materials.Gunpowder = loadGunpowder();
        Materials.Honey = loadHoney();
        Materials.Leather = loadLeather();
        Materials.Lubricant = loadLubricant();
        Materials.McGuffium239 = loadMcGuffium239();
        Materials.MeatCooked = loadMeatCooked();
        Materials.MeatRaw = loadMeatRaw();
        Materials.Milk = loadMilk();
        Materials.Mud = loadMud();
        Materials.Oil = loadOil();
        Materials.Paper = loadPaper();
        Materials.Peat = loadPeat();
        Materials.RareEarth = loadRareEarth();
        Materials.Red = loadRed();
        Materials.Reinforced = loadReinforced();
        Materials.SeedOil = loadSeedOil();
        Materials.SeedOilHemp = loadSeedOilHemp();
        Materials.SeedOilLin = loadSeedOilLin();
        Materials.Stone = loadStone();
        Materials.TNT = loadTNT();
        Materials.Unstable = loadUnstable();
        Materials.UnstableIngot = loadUnstableIngot();
        Materials.Vinegar = loadVinegar();
        Materials.WeedEX9000 = loadWeedEX9000();
        Materials.Wheat = loadWheat();
        Materials.WoodGas = loadWoodGas();
        Materials.WoodTar = loadWoodTar();
        Materials.WoodVinegar = loadWoodVinegar();
    }

    private static Materials loadBiomass() {
        return new MaterialBuilder().setName("Biomass")
            .setDefaultLocalName("Forestry Biomass")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x0000ff00)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 8)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadCharcoalByproducts() {
        return new MaterialBuilder().setName("CharcoalByproducts")
            .setDefaultLocalName("Charcoal Byproducts")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00784421)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadCheese() {
        return new MaterialBuilder().setName("Cheese")
            .setDefaultLocalName("Cheese")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .addDustItems()
            .addOreItems()
            .setMeltingPoint(320)
            .addSubTag(SubTag.FOOD)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .constructMaterial();
    }

    private static Materials loadChili() {
        return new MaterialBuilder().setName("Chili")
            .setDefaultLocalName("Chili")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00c80000)
            .addDustItems()
            .addSubTag(SubTag.FOOD)
            .constructMaterial();
    }

    private static Materials loadChocolate() {
        return new MaterialBuilder().setName("Chocolate")
            .setDefaultLocalName("Chocolate")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00be5f00)
            .addDustItems()
            .addSubTag(SubTag.FOOD)
            .constructMaterial();
    }

    private static Materials loadCluster() {
        return new MaterialBuilder().setName("Cluster")
            .setDefaultLocalName("Cluster")
            .setColor(Dyes.dyeWhite)
            .setARGB(0x7fffffff)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadCoalFuel() {
        return new MaterialBuilder().setName("CoalFuel")
            .setDefaultLocalName("Coalfuel")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00323246)
            .setFuel(MaterialBuilder.FuelType.Diesel, 16)
            .addCell()
            .removeOrePrefix(OrePrefixes.cell)
            .constructMaterial();
    }

    private static Materials loadCocoa() {
        return new MaterialBuilder().setName("Cocoa")
            .setDefaultLocalName("Cocoa")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00be5f00)
            .addDustItems()
            .addSubTag(SubTag.FOOD)
            .constructMaterial();
    }

    private static Materials loadCoffee() {
        return new MaterialBuilder().setName("Coffee")
            .setDefaultLocalName("Coffee")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00964b00)
            .addDustItems()
            .addSubTag(SubTag.FOOD)
            .constructMaterial();
    }

    private static Materials loadCreosote() {
        return new MaterialBuilder().setName("Creosote")
            .setDefaultLocalName("Creosote")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00804000)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 8)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadDiesel() {
        return new MaterialBuilder().setName("Fuel")
            .setDefaultLocalName("Diesel")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .setFuel(MaterialBuilder.FuelType.Diesel, 480)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadEthanol() {
        return new MaterialBuilder().setName("Ethanol")
            .setDefaultLocalName("Ethanol")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff8000)
            .setFuel(MaterialBuilder.FuelType.Diesel, 192)
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial();
    }

    private static Materials loadFermentedBiomass() {
        return new MaterialBuilder().setName("FermentedBiomass")
            .setDefaultLocalName("Fermented Biomass")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00445500)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadFishOil() {
        return new MaterialBuilder().setName("FishOil")
            .setDefaultLocalName("Fish Oil")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffc400)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 2)
            .addCell()
            .addAspect(TCAspects.CORPUS, 2)
            .addSubTag(SubTag.FOOD)
            .constructMaterial();
    }

    private static Materials loadFryingOilHot() {
        return new MaterialBuilder().setName("FryingOilHot")
            .setDefaultLocalName("Hot Frying Oil")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00c8c400)
            .addCell()
            .setHeatDamage(1.0f)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.IGNIS, 1)
            .addSubTag(SubTag.FOOD)
            .constructMaterial();
    }

    private static Materials loadGlue() {
        return new MaterialBuilder().setName("Glue")
            .setDefaultLocalName("Refined Glue")
            .setChemicalFormula("No Horses were harmed in the the making of this substance")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00c8c400)
            .addCell()
            .addAspect(TCAspects.LIMUS, 2)
            .constructMaterial();
    }

    private static Materials loadGlueAdvanced() {
        return new MaterialBuilder().setName("AdvancedGlue")
            .setDefaultLocalName("Advanced Glue")
            .setChemicalFormula("A chemically approved glue!")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffffb9)
            .addCell()
            .addFluid()
            .addAspect(TCAspects.LIMUS, 5)
            .constructMaterial();
    }

    private static Materials loadGunpowder() {
        return new MaterialBuilder().setName("Gunpowder")
            .setDefaultLocalName("Gunpowder")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00808080)
            .addDustItems()
            .addAspect(TCAspects.PERDITIO, 3)
            .addAspect(TCAspects.IGNIS, 4)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.EXPLOSIVE)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.NO_SMASHING)
            .removeOrePrefix(OrePrefixes.dust) // minecraft:gunpowder
            .constructMaterial();
    }

    private static Materials loadHoney() {
        return new MaterialBuilder().setName("Honey")
            .setDefaultLocalName("Honey")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00d2c800)
            .addCell()
            .addSubTag(SubTag.FOOD)
            .constructMaterial();
    }

    private static Materials loadLeather() {
        return new MaterialBuilder().setName("Leather")
            .setDefaultLocalName("Leather")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x7f969650)
            .addDustItems()
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadLubricant() {
        return new MaterialBuilder().setName("Lubricant")
            .setDefaultLocalName("Lubricant")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ffc400)
            .addCell()
            .addAspect(TCAspects.AQUA, 2)
            .addAspect(TCAspects.MACHINA, 1)
            .constructMaterial();
    }

    private static Materials loadMcGuffium239() {
        return new MaterialBuilder().setName("McGuffium239")
            .setDefaultLocalName("Mc Guffium 239")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePink)
            .setARGB(0x00c83296)
            .addCell()
            .addAspect(TCAspects.ALIENIS, 8)
            .addAspect(TCAspects.PERMUTATIO, 8)
            .addAspect(TCAspects.SPIRITUS, 8)
            .addAspect(TCAspects.AURAM, 8)
            .addAspect(TCAspects.VITIUM, 8)
            .addAspect(TCAspects.RADIO, 8)
            .addAspect(TCAspects.MAGNETO, 8)
            .addAspect(TCAspects.ELECTRUM, 8)
            .addAspect(TCAspects.NEBRISUM, 8)
            .addAspect(TCAspects.STRONTIO, 8)
            .constructMaterial();
    }

    private static Materials loadMeatCooked() {
        return new MaterialBuilder().setName("MeatCooked")
            .setDefaultLocalName("Cooked Meat")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyePink)
            .setARGB(0x00963c14)
            .addDustItems()
            .addSubTag(SubTag.FOOD)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadMeatRaw() {
        return new MaterialBuilder().setName("MeatRaw")
            .setDefaultLocalName("Raw Meat")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyePink)
            .setARGB(0x00ff6464)
            .addDustItems()
            .setSmeltingInto(() -> Materials.MeatCooked)
            .addSubTag(SubTag.FOOD)
            .addSubTag(SubTag.NO_SMASHING)
            .constructMaterial();
    }

    private static Materials loadMilk() {
        return new MaterialBuilder().setName("Milk")
            .setDefaultLocalName("Milk")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fefefe)
            .addDustItems()
            .addCell()
            .addAspect(TCAspects.SANO, 2)
            .addSubTag(SubTag.FOOD)
            .removeOrePrefix(OrePrefixes.bucket) // minecraft:milk_bucket
            .removeOrePrefix(OrePrefixes.bucketClay) // IguanaTweaksTConstruct:clayBucketMilk
            .removeOrePrefix(OrePrefixes.bottle)
            .constructMaterial();
    }

    private static Materials loadMud() {
        return new MaterialBuilder().setName("Mud")
            .setDefaultLocalName("Mud")
            .setColor(Dyes.dyeBrown)
            .constructMaterial();
    }

    private static Materials loadOil() {
        return new MaterialBuilder().setName("Oil")
            .setDefaultLocalName("Oil")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000a0a0a)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 20)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadPaper() {
        return new MaterialBuilder().setName("Paper")
            .setDefaultLocalName("Paper")
            .setIconSet(TextureSet.SET_PAPER)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fafafa)
            .addDustItems()
            .addAspect(TCAspects.COGNITIO, 1)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.PAPER)
            .addOrePrefix(OrePrefixes.plateDouble)
            .addOrePrefix(OrePrefixes.plateTriple)
            .addOrePrefix(OrePrefixes.plateQuadruple)
            .addOrePrefix(OrePrefixes.plateQuintuple)
            .addOrePrefix(OrePrefixes.ring)
            .constructMaterial();
    }

    private static Materials loadPeat() {
        return new MaterialBuilder().setName("Peat")
            .setDefaultLocalName("Peat")
            .setColor(Dyes.dyeBrown)
            .addAspect(TCAspects.POTENTIA, 2)
            .addAspect(TCAspects.IGNIS, 2)
            .constructMaterial();
    }

    private static Materials loadRareEarth() {
        return new MaterialBuilder().setName("RareEarth")
            .setDefaultLocalName("Rare Earth")
            .setChemicalFormula("??????")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00808064)
            .addDustItems()
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.LUCRUM, 1)
            .constructMaterial();
    }

    private static Materials loadRed() {
        return new MaterialBuilder().setName("Red")
            .setDefaultLocalName("Red")
            .setColor(Dyes.dyeRed)
            .setARGB(0x00ff0000)
            .constructMaterial();
    }

    private static Materials loadReinforced() {
        return new MaterialBuilder().setName("Reinforced")
            .setDefaultLocalName("Reinforced")
            .setChemicalFormula("Fe₂(C(MgFeSi₂O₈)₈)")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00698da5)
            .setTool(480, 4, 7.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1_700)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial();
    }

    private static Materials loadSeedOil() {
        return new MaterialBuilder().setName("SeedOil")
            .setDefaultLocalName("Seed Oil")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00c4ff00)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 2)
            .addCell()
            .addAspect(TCAspects.GRANUM, 2)
            .addSubTag(SubTag.FOOD)
            .constructMaterial();
    }

    private static Materials loadSeedOilHemp() {
        return new MaterialBuilder().setName("SeedOilHemp")
            .setDefaultLocalName("Hemp Seed Oil")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00c4ff00)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 2)
            .addCell()
            .addAspect(TCAspects.GRANUM, 2)
            .addSubTag(SubTag.FOOD)
            .constructMaterial();
    }

    private static Materials loadSeedOilLin() {
        return new MaterialBuilder().setName("SeedOilLin")
            .setDefaultLocalName("Lin Seed Oil")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00c4ff00)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 2)
            .addCell()
            .addAspect(TCAspects.GRANUM, 2)
            .addSubTag(SubTag.FOOD)
            .constructMaterial();
    }

    private static Materials loadStone() {
        return new MaterialBuilder().setName("Stone")
            .setDefaultLocalName("Stone")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00cdcdcd)
            .setTool(32, 1, 4.0f)
            .addDustItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.TERRA, 1)
            .addSubTag(SubTag.NO_RECYCLING)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STONE)
            .addOrePrefix(OrePrefixes.dustImpure)
            .constructMaterial();
    }

    private static Materials loadTNT() {
        return new MaterialBuilder().setName("TNT")
            .setDefaultLocalName("TNT")
            .setColor(Dyes.dyeRed)
            .addAspect(TCAspects.PERDITIO, 7)
            .addAspect(TCAspects.IGNIS, 4)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.EXPLOSIVE)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.NO_SMASHING)
            .constructMaterial();
    }

    private static Materials loadUnstable() {
        return new MaterialBuilder().setName("Unstable")
            .setDefaultLocalName("Unstable")
            .setChemicalFormula(CustomGlyphs.FIXED_JAPANESE_OPENING_QUOTE + "Fe/C⌋")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x7fdcdcdc)
            .addDustItems()
            .addAspect(TCAspects.PERDITIO, 4)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadUnstableIngot() {
        return new MaterialBuilder().setName("Unstableingot")
            .setDefaultLocalName("Unstable")
            .setColor(Dyes.dyeWhite)
            .setARGB(0x7fffffff)
            .addAspect(TCAspects.PERDITIO, 4)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadVinegar() {
        return new MaterialBuilder().setName("Vinegar")
            .setDefaultLocalName("Vinegar")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .constructMaterial();
    }

    private static Materials loadWeedEX9000() {
        return new MaterialBuilder().setName("WeedEX9000")
            .setDefaultLocalName("Weed-EX 9000")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x0040e056)
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadWheat() {
        return new MaterialBuilder().setName("Wheat")
            .setDefaultLocalName("Wheat")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffffc4)
            .addDustItems()
            .addAspect(TCAspects.MESSIS, 2)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.FOOD)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .constructMaterial();
    }

    private static Materials loadWoodGas() {
        return new MaterialBuilder().setName("WoodGas")
            .setDefaultLocalName("Wood Gas")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00decd87)
            .setFuel(MaterialBuilder.FuelType.Gas, 24)
            .addCell()
            .addGas()
            .constructMaterial();
    }

    private static Materials loadWoodTar() {
        return new MaterialBuilder().setName("WoodTar")
            .setDefaultLocalName("Wood Tar")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x0028170b)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadWoodVinegar() {
        return new MaterialBuilder().setName("WoodVinegar")
            .setDefaultLocalName("Wood Vinegar")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00d45500)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static void loadTODOThis() {
        Materials.AluminiumBrass = loadAluminiumBrass();
        Materials.Endstone = loadEndstone();
        Materials.Netherrack = loadNetherrack();
        Materials.Osmiridium = loadOsmiridium();
        Materials.SoulSand = loadSoulSand();
        Materials.Sunnarium = loadSunnarium();
    }

    private static Materials loadAluminiumBrass() {
        return new MaterialBuilder().setName("AluminiumBrass")
            .setDefaultLocalName("Aluminium Brass")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setTool(64, 2, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadEndstone() {
        return new MaterialBuilder().setName("Endstone")
            .setDefaultLocalName("Endstone")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .addDustItems()
            .addOreByproduct(() -> Materials.Helium3)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STONE)
            .addOrePrefix(OrePrefixes.dustImpure)
            .constructMaterial();
    }

    private static Materials loadNetherrack() {
        return new MaterialBuilder().setName("Netherrack")
            .setDefaultLocalName("Netherrack")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00c80000)
            .addDustItems()
            .addOreByproduct(() -> Materials.Sulfur)
            .setSmeltingInto(() -> Materials.NetherBrick)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STONE)
            .addSubTag(SubTag.UNBURNABLE)
            .addOrePrefix(OrePrefixes.dustImpure)
            .constructMaterial();
    }

    private static Materials loadOsmiridium() {
        return new MaterialBuilder().setName("Osmiridium")
            .setDefaultLocalName("Osmiridium")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x006464ff)
            .setTool(1_600, 3, 7.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3_500)
            .setBlastFurnaceTemp(4_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iridium, 3)
            .addMaterial(Materials.Osmium, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadSoulSand() {
        return new MaterialBuilder().setName("SoulSand")
            .setDefaultLocalName("Soulsand")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadSunnarium() {
        return new MaterialBuilder().setName("Sunnarium")
            .setDefaultLocalName("Sunnarium")
            .setChemicalFormula("Su")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(4_200)
            .setBlastFurnaceTemp(4_200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static void loadDegree1Compounds() {
        Materials.AceticAcid = loadAceticAcid();
        Materials.Acetone = loadAcetone();
        Materials.Air = loadAir();
        Materials.AllylChloride = loadAllylChloride();
        Materials.Almandine = loadAlmandine();
        Materials.Ammonia = loadAmmonia();
        Materials.Andradite = loadAndradite();
        Materials.AnnealedCopper = loadAnnealedCopper();
        Materials.AntimonyTrioxide = loadAntimonyTrioxide();
        Materials.ArsenicTrioxide = loadArsenicTrioxide();
        Materials.Asbestos = loadAsbestos();
        Materials.Ash = loadAsh();
        Materials.AshDark = loadAshDark();
        Materials.BandedIron = loadBandedIron();
        Materials.BatteryAlloy = loadBatteryAlloy();
        Materials.Benzene = loadBenzene();
        Materials.BlueTopaz = loadBlueTopaz();
        Materials.Bone = loadBone();
        Materials.Brass = loadBrass();
        Materials.Brick = loadBrick();
        Materials.Bronze = loadBronze();
        Materials.BrownLimonite = loadBrownLimonite();
        Materials.Calcite = loadCalcite();
        Materials.CarbonDioxide = loadCarbonDioxide();
        Materials.Cassiterite = loadCassiterite();
        Materials.CassiteriteSand = loadCassiteriteSand();
        Materials.Chalcopyrite = loadChalcopyrite();
        Materials.Charcoal = loadCharcoal();
        Materials.Chlorobenzene = loadChlorobenzene();
        Materials.Chromite = loadChromite();
        Materials.ChromiumDioxide = loadChromiumDioxide();
        Materials.Cinnabar = loadCinnabar();
        Materials.Coal = loadCoal();
        Materials.CobaltOxide = loadCobaltOxide();
        Materials.Cobaltite = loadCobaltite();
        Materials.Cooperite = loadCooperite();
        Materials.CupricOxide = loadCupricOxide();
        Materials.Cupronickel = loadCupronickel();
        Materials.DeepIron = loadDeepIron();
        Materials.Diamond = loadDiamond();
        Materials.DilutedHydrochloricAcid = loadDilutedHydrochloricAcid();
        Materials.Electrum = loadElectrum();
        Materials.Emerald = loadEmerald();
        Materials.Epoxid = loadEpoxid();
        Materials.FerriteMixture = loadFerriteMixture();
        Materials.Ferrosilite = loadFerrosilite();
        Materials.FreshWater = loadFreshWater();
        Materials.Galena = loadGalena();
        Materials.Garnierite = loadGarnierite();
        Materials.Glyceryl = loadGlyceryl();
        Materials.GreenSapphire = loadGreenSapphire();
        Materials.Grossular = loadGrossular();
        Materials.HolyWater = loadHolyWater();
        Materials.HydricSulfide = loadHydricSulfide();
        Materials.Ice = loadIce();
        Materials.Ilmenite = loadIlmenite();
        Materials.Invar = loadInvar();
        Materials.Kanthal = loadKanthal();
        Materials.Lazurite = loadLazurite();
        Materials.LiquidNitrogen = loadLiquidNitrogen();
        Materials.LiquidOxygen = loadLiquidOxygen();
        Materials.Magnalium = loadMagnalium();
        Materials.Magnesia = loadMagnesia();
        Materials.Magnesite = loadMagnesite();
        Materials.Magnesiumchloride = loadMagnesiumchloride();
        Materials.Magnetite = loadMagnetite();
        Materials.Massicot = loadMassicot();
        Materials.Methane = loadMethane();
        Materials.Molybdenite = loadMolybdenite();
        Materials.Nichrome = loadNichrome();
        Materials.NickelZincFerrite = loadNickelZincFerrite();
        Materials.NiobiumNitride = loadNiobiumNitride();
        Materials.NiobiumTitanium = loadNiobiumTitanium();
        Materials.NitroCarbon = loadNitroCarbon();
        Materials.NitrogenDioxide = loadNitrogenDioxide();
        Materials.NobleGases = loadNobleGases();
        Materials.Obsidian = loadObsidian();
        Materials.Phosphate = loadPhosphate();
        Materials.PigIron = loadPigIron();
        Materials.Polycaprolactam = loadPolycaprolactam();
        Materials.Polydimethylsiloxane = loadPolydimethylsiloxane();
        Materials.Polyethylene = loadPolyethylene();
        Materials.Polytetrafluoroethylene = loadPolytetrafluoroethylene();
        Materials.Potash = loadPotash();
        Materials.Powellite = loadPowellite();
        Materials.Pumice = loadPumice();
        Materials.Pyrite = loadPyrite();
        Materials.Pyrochlore = loadPyrochlore();
        Materials.Pyrolusite = loadPyrolusite();
        Materials.Pyrope = loadPyrope();
        Materials.Quicklime = loadQuicklime();
        Materials.RockSalt = loadRockSalt();
        Materials.Rubber = loadRubber();
        Materials.RubberRaw = loadRubberRaw();
        Materials.RubberSilicone = loadRubberSilicone();
        Materials.Ruby = loadRuby();
        Materials.Rutile = loadRutile();
        Materials.Salt = loadSalt();
        Materials.Saltpeter = loadSaltpeter();
        Materials.Sapphire = loadSapphire();
        Materials.Scheelite = loadScheelite();
        Materials.SiliconDioxide = loadSiliconDioxide();
        Materials.Snow = loadSnow();
        Materials.SodaAsh = loadSodaAsh();
        Materials.Sodalite = loadSodalite();
        Materials.SodiumPersulfate = loadSodiumPersulfate();
        Materials.SodiumSulfide = loadSodiumSulfide();
        Materials.Titaniumtetrachloride = loadTitaniumtetrachloride();
        Materials.Water = Materials.Steam = loadWater();
        Materials.Zincite = loadZincite();
    }

    private static Materials loadAceticAcid() {
        return new MaterialBuilder().setName("AceticAcid")
            .setDefaultLocalName("Acetic Acid")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00c8b4a0)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 4)
            .addMaterial(Materials.Oxygen, 2)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadAcetone() {
        return new MaterialBuilder().setName("Acetone")
            .setDefaultLocalName("Acetone")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00afafaf)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadAir() {
        return new MaterialBuilder().setName("Air")
            .setDefaultLocalName("Air")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0xf0a9d0f5)
            .addCell()
            .addMaterial(Materials.Nitrogen, 40)
            .addMaterial(Materials.Oxygen, 11)
            .addMaterial(Materials.Argon, 1)
            .addMaterial(Materials.NobleGases, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadAllylChloride() {
        return new MaterialBuilder().setName("AllylChloride")
            .setDefaultLocalName("Allyl Chloride")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x0087deaa)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Hydrogen, 5)
            .addMaterial(Materials.Chlorine, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadAlmandine() {
        return new MaterialBuilder().setName("Almandine")
            .setDefaultLocalName("Almandine")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00ff0000)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Iron, 3)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 12)
            .addOreByproduct(() -> Materials.GarnetRed)
            .addOreByproduct(() -> Materials.Aluminiumoxide)
            .constructMaterial();
    }

    private static Materials loadAmmonia() {
        return new MaterialBuilder().setName("Ammonia")
            .setDefaultLocalName("Ammonia")
            .setIconSet(TextureSet.SET_EMERALD)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x003f3480)
            .setOreMultiplier(4)
            .addCell()
            .addGas()
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Hydrogen, 3)
            .addElectrolyzerRecipe()
            .addSubTag(SubTag.ICE_ORE)
            .addSubTag(SubTag.NO_ORE_PROCESSING)
            .addOrePrefix(OrePrefixes.ore)
            .addOrePrefix(OrePrefixes.rawOre)
            .constructMaterial();
    }

    private static Materials loadAndradite() {
        return new MaterialBuilder().setName("Andradite")
            .setDefaultLocalName("Andradite")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00967800)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Calcium, 3)
            .addMaterial(Materials.Iron, 2)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 12)
            .addOreByproduct(() -> Materials.GarnetYellow)
            .addOreByproduct(() -> Materials.Iron)
            .constructMaterial();
    }

    private static Materials loadAnnealedCopper() {
        return new MaterialBuilder().setName("AnnealedCopper")
            .setDefaultLocalName("Annealed Copper")
            .setChemicalFormula("Cu*")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff7814)
            .addDustItems()
            .addMetalItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Copper, 1)
            .setArcSmeltingInto(() -> Materials.AnnealedCopper)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .constructMaterial();
    }

    private static Materials loadAntimonyTrioxide() {
        return new MaterialBuilder().setName("AntimonyTrioxide")
            .setDefaultLocalName("Antimony Trioxide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00e6e6f0)
            .addDustItems()
            .addMaterial(Materials.Antimony, 2)
            .addMaterial(Materials.Oxygen, 3)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadArsenicTrioxide() {
        return new MaterialBuilder().setName("ArsenicTrioxide")
            .setDefaultLocalName("Arsenic Trioxide")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGreen)
            .addDustItems()
            .addMaterial(Materials.Arsenic, 2)
            .addMaterial(Materials.Oxygen, 3)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadAsbestos() {
        return new MaterialBuilder().setName("Asbestos")
            .setDefaultLocalName("Asbestos")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00e6e6e6)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // Mg3Si2O5(OH)4
            .addMaterial(Materials.Magnesium, 3)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Hydrogen, 4)
            .addMaterial(Materials.Oxygen, 9)
            .addOreByproduct(() -> Materials.Asbestos)
            .addOreByproduct(() -> Materials.SiliconDioxide)
            .addOreByproduct(() -> Materials.Magnesium)
            .constructMaterial();
    }

    private static Materials loadAsh() {
        return new MaterialBuilder().setName("Ash")
            .setDefaultLocalName("Ashes")
            .setChemicalFormula("??")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00969696)
            .addDustItems()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.PERDITIO, 1)
            .addOreByproduct(() -> Materials.Carbon)
            .constructMaterial();
    }

    private static Materials loadAshDark() {
        return new MaterialBuilder().setName("DarkAsh")
            .setDefaultLocalName("Dark Ashes")
            .setChemicalFormula("C" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00323232)
            .addDustItems()
            .setDensity(2, 1)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.IGNIS, 1)
            .addAspect(TCAspects.PERDITIO, 1)
            .addOreByproduct(() -> Materials.Carbon)
            .constructMaterial();
    }

    private static Materials loadBandedIron() {
        return new MaterialBuilder().setName("BandedIron")
            .setDefaultLocalName("Banded Iron")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00915a5a)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 2)
            .addMaterial(Materials.Oxygen, 3)
            .setDirectSmelting(() -> Materials.Iron)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .constructMaterial();
    }

    private static Materials loadBatteryAlloy() {
        return new MaterialBuilder().setName("BatteryAlloy")
            .setDefaultLocalName("Battery Alloy")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setARGB(0x009c7ca0)
            .addDustItems()
            .addMetalItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Lead, 4)
            .addMaterial(Materials.Antimony, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadBenzene() {
        return new MaterialBuilder().setName("Benzene")
            .setDefaultLocalName("Benzene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGray)
            .setARGB(0x001a1a1a)
            .setFuel(MaterialBuilder.FuelType.Gas, 360)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 6)
            .addMaterial(Materials.Hydrogen, 6)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadBlueTopaz() {
        return new MaterialBuilder().setName("BlueTopaz")
            .setDefaultLocalName("Blue Topaz")
            .setIconSet(TextureSet.SET_GEM_HORIZONTAL)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x7f0000ff)
            .setTool(256, 3, 7.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Fluorine, 2)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 6)
            .addAspect(TCAspects.LUCRUM, 6)
            .addAspect(TCAspects.VITREUS, 4)
            .addOreByproduct(() -> Materials.Topaz)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadBone() {
        return new MaterialBuilder().setName("Bone")
            .setDefaultLocalName("Bone")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fafafa)
            .addDustItems()
            .addMaterial(Materials.Calcium, 1)
            .addAspect(TCAspects.MORTUUS, 2)
            .addAspect(TCAspects.CORPUS, 1)
            .removeOrePrefix(OrePrefixes.dust) // minecraft:dye:15
            .removeOrePrefix(OrePrefixes.stick)
            .constructMaterial();
    }

    private static Materials loadBrass() {
        return new MaterialBuilder().setName("Brass")
            .setDefaultLocalName("Brass")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffb400)
            .setTool(96, 1, 7.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Zinc, 1)
            .addMaterial(Materials.Copper, 3)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .addOreByproduct(() -> Materials.Copper)
            .addOreByproduct(() -> Materials.Zinc)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .constructMaterial();
    }

    private static Materials loadBrick() {
        return new MaterialBuilder().setName("Brick")
            .setDefaultLocalName("Brick")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x009b5643)
            .addDustItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 4)
            .addMaterial(Materials.Oxygen, 11)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.STONE)
            .removeOrePrefix(OrePrefixes.ingot) // minecraft:brick
            .constructMaterial();
    }

    private static Materials loadBronze() {
        return new MaterialBuilder().setName("Bronze")
            .setDefaultLocalName("Bronze")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff8000)
            .setTool(192, 2, 6.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Tin, 1)
            .addMaterial(Materials.Copper, 3)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .addOreByproduct(() -> Materials.Copper)
            .addOreByproduct(() -> Materials.Tin)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadBrownLimonite() {
        return new MaterialBuilder().setName("BrownLimonite")
            .setDefaultLocalName("Brown Limonite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00c86400)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            // FeO(OH)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Oxygen, 2)
            .addOreByproduct(() -> Materials.Malachite)
            .addOreByproduct(() -> Materials.YellowLimonite)
            .setDirectSmelting(() -> Materials.Iron)
            .addSubTag(SubTag.BLASTFURNACE_CALCITE_DOUBLE)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .constructMaterial();
    }

    private static Materials loadCalcite() {
        return new MaterialBuilder().setName("Calcite")
            .setDefaultLocalName("Calcite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00fae6dc)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addOreByproduct(() -> Materials.Andradite)
            .addOreByproduct(() -> Materials.Malachite)
            .constructMaterial();
    }

    private static Materials loadCarbonDioxide() {
        return new MaterialBuilder().setName("CarbonDioxide")
            .setDefaultLocalName("Carbon Dioxide")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0xf0a9d0f5)
            .setOreMultiplier(4)
            .addCell()
            .addGas()
            .setMeltingPoint(25)
            .setBlastFurnaceTemp(1)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Oxygen, 2)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.ICE_ORE)
            .addSubTag(SubTag.NO_ORE_PROCESSING)
            .addOrePrefix(OrePrefixes.ore)
            .addOrePrefix(OrePrefixes.rawOre)
            .constructMaterial();
    }

    private static Materials loadCassiterite() {
        return new MaterialBuilder().setName("Cassiterite")
            .setDefaultLocalName("Cassiterite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00dcdcdc)
            .addDustItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Tin, 1)
            .addMaterial(Materials.Oxygen, 2)
            .addOreByproduct(() -> Materials.Tin)
            .setDirectSmelting(() -> Materials.Tin)
            .constructMaterial();
    }

    private static Materials loadCassiteriteSand() {
        return new MaterialBuilder().setName("CassiteriteSand")
            .setDefaultLocalName("Cassiterite Sand")
            .setIconSet(TextureSet.SET_SAND)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00dcdcdc)
            .addDustItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Tin, 1)
            .addMaterial(Materials.Oxygen, 2)
            .addOreByproduct(() -> Materials.Tin)
            .setDirectSmelting(() -> Materials.Tin)
            .constructMaterial();
    }

    private static Materials loadChalcopyrite() {
        return new MaterialBuilder().setName("Chalcopyrite")
            .setDefaultLocalName("Chalcopyrite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00a07828)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Copper, 1)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Sulfur, 2)
            .addOreByproduct(() -> Materials.Pyrite)
            .addOreByproduct(() -> Materials.Cobalt)
            .addOreByproduct(() -> Materials.Cadmium)
            .addOreByproduct(() -> Materials.Gold)
            .setDirectSmelting(() -> Materials.Copper)
            .addSubTag(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE)
            .addSubTag(SubTag.INDUCTIONSMELTING_LOW_OUTPUT)
            .constructMaterial();
    }

    private static Materials loadCharcoal() {
        return new MaterialBuilder().setName("Charcoal")
            .setDefaultLocalName("Charcoal")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00644646)
            .addDustItems()
            .addGemItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.POTENTIA, 2)
            .addAspect(TCAspects.IGNIS, 2)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .removeOrePrefix(OrePrefixes.gem) // minecraft:coal:1
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadChlorobenzene() {
        return new MaterialBuilder().setName("Chlorobenzene")
            .setDefaultLocalName("Chlorobenzene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00003241)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 6)
            .addMaterial(Materials.Hydrogen, 5)
            .addMaterial(Materials.Chlorine, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadChromite() {
        return new MaterialBuilder().setName("Chromite")
            .setDefaultLocalName("Chromite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setARGB(0x0023140f)
            .addDustItems()
            .addOreItems()
            .setMeltingPoint(1_700)
            .setBlastFurnaceTemp(1_700)
            .setBlastFurnaceRequired(true)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Chrome, 2)
            .addMaterial(Materials.Oxygen, 4)
            .addOreByproduct(() -> Materials.Iron)
            .addOreByproduct(() -> Materials.Magnesium)
            .setDirectSmelting(() -> Materials.Chrome)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .constructMaterial();
    }

    private static Materials loadChromiumDioxide() {
        return new MaterialBuilder().setName("ChromiumDioxide")
            .setDefaultLocalName("Chromium Dioxide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePink)
            .setARGB(0x00e6c8c8)
            .setTool(256, 3, 11.0f)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(650)
            .setBlastFurnaceTemp(650)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Chrome, 1)
            .addMaterial(Materials.Oxygen, 2)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MACHINA, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadCinnabar() {
        return new MaterialBuilder().setName("Cinnabar")
            .setDefaultLocalName("Cinnabar")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00960000)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Mercury, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addOreByproduct(() -> Materials.Redstone)
            .addOreByproduct(() -> Materials.Sulfur)
            .addOreByproduct(() -> Materials.Glowstone)
            .setDirectSmelting(() -> Materials.Mercury)
            .addSubTag(SubTag.INDUCTIONSMELTING_LOW_OUTPUT)
            .addSubTag(SubTag.SMELTING_TO_GEM)
            .constructMaterial();
    }

    private static Materials loadCoal() {
        return new MaterialBuilder().setName("Coal")
            .setDefaultLocalName("Coal")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00464646)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .setDensity(2, 1)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.POTENTIA, 2)
            .addAspect(TCAspects.IGNIS, 2)
            .addOreByproduct(() -> Materials.Lignite)
            .addOreByproduct(() -> Materials.Thorium)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .removeOrePrefix(OrePrefixes.block) // minecraft:coal_block
            .removeOrePrefix(OrePrefixes.gem) // minecraft:coal:0
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadCobaltOxide() {
        return new MaterialBuilder().setName("CobaltOxide")
            .setDefaultLocalName("Cobalt Oxide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x00668000)
            .addDustItems()
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadCobaltite() {
        return new MaterialBuilder().setName("Cobaltite")
            .setDefaultLocalName("Cobaltite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x005050fa)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.Arsenic, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addOreByproduct(() -> Materials.Cobalt)
            .setDirectSmelting(() -> Materials.Cobalt)
            .addSubTag(SubTag.WASHING_SODIUMPERSULFATE)
            .constructMaterial();
    }

    private static Materials loadCooperite() {
        return new MaterialBuilder().setName("Cooperite")
            .setDefaultLocalName("Sheldonite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffffc8)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Platinum, 3)
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Palladium, 1)
            .addOreByproduct(() -> Materials.Palladium)
            .addOreByproduct(() -> Materials.Nickel)
            .addOreByproduct(() -> Materials.Iridium)
            .setDirectSmelting(() -> Materials.Platinum)
            .addSubTag(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE)
            .addSubTag(SubTag.WASHING_MERCURY)
            .constructMaterial();
    }

    private static Materials loadCupricOxide() {
        return new MaterialBuilder().setName("CupricOxide")
            .setDefaultLocalName("Cupric Oxide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000f0f0f)
            .addDustItems()
            .setMeltingPoint(1_599)
            .addMaterial(Materials.Copper, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadCupronickel() {
        return new MaterialBuilder().setName("Cupronickel")
            .setDefaultLocalName("Cupronickel")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00e39680)
            .setTool(64, 1, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Copper, 1)
            .addMaterial(Materials.Nickel, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadDeepIron() {
        return new MaterialBuilder().setName("DeepIron")
            .setDefaultLocalName("Deep Iron")
            .setChemicalFormula("Sp₂Fe")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setARGB(0x00968c8c)
            .setTool(384, 2, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .setMeltingPoint(7_500)
            .setBlastFurnaceTemp(7_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addMaterial(Materials.Iron, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .addOreByproduct(() -> Materials.Trinium)
            .addOreByproduct(() -> Materials.Iron)
            .addSubTag(SubTag.BLASTFURNACE_CALCITE_TRIPLE)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadDiamond() {
        return new MaterialBuilder().setName("Diamond")
            .setDefaultLocalName("Diamond")
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x7fc8ffff)
            .setTool(1_280, 4, 8.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setDensity(64, 1)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.VITREUS, 3)
            .addAspect(TCAspects.LUCRUM, 4)
            .addOreByproduct(() -> Materials.Graphite)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .removeOrePrefix(OrePrefixes.block) // minecraft:diamond_block
            .removeOrePrefix(OrePrefixes.gem) // minecraft:diamond
            .constructMaterial();
    }

    private static Materials loadDilutedHydrochloricAcid() {
        return new MaterialBuilder().setName("DilutedHydrochloricAcid_GT5U")
            .setDefaultLocalName("Diluted Hydrochloric Acid")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x0099a7a3)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Chlorine, 1)
            .constructMaterial();
    }

    private static Materials loadElectrum() {
        return new MaterialBuilder().setName("Electrum")
            .setDefaultLocalName("Electrum")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff64)
            .setTool(64, 2, 12.0f)
            .setToolEnchantment(() -> EnchantmentEnderDamage.INSTANCE, 3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Silver, 1)
            .addMaterial(Materials.Gold, 1)
            .addOreByproduct(() -> Materials.Gold)
            .addOreByproduct(() -> Materials.Silver)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .constructMaterial();
    }

    private static Materials loadEmerald() {
        return new MaterialBuilder().setName("Emerald")
            .setDefaultLocalName("Emerald")
            .setIconSet(TextureSet.SET_EMERALD)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x7f50ff50)
            .setTool(256, 4, 7.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addMaterial(Materials.Beryllium, 3)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 6)
            .addMaterial(Materials.Oxygen, 18)
            .addAspect(TCAspects.VITREUS, 3)
            .addAspect(TCAspects.LUCRUM, 5)
            .addOreByproduct(() -> Materials.Beryllium)
            .addOreByproduct(() -> Materials.Aluminiumoxide)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .removeOrePrefix(OrePrefixes.block) // minecraft:emerald_block
            .removeOrePrefix(OrePrefixes.gem) // minecraft:emerald
            .constructMaterial();
    }

    private static Materials loadEpoxid() {
        return new MaterialBuilder().setName("Epoxid")
            .setDefaultLocalName("Epoxid")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00c88c14)
            .setTool(32, 1, 3.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .removeOrePrefix(OrePrefixes.sheetmetal)
            .setMeltingPoint(400)
            .addMaterial(Materials.Carbon, 21)
            .addMaterial(Materials.Hydrogen, 24)
            .addMaterial(Materials.Oxygen, 4)
            .addAspect(TCAspects.MOTUS, 2)
            .constructMaterial();
    }

    private static Materials loadFerriteMixture() {
        return new MaterialBuilder().setName("FerriteMixture")
            .setDefaultLocalName("Ferrite Mixture")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00b4b4b4)
            .addDustItems()
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.Zinc, 1)
            .addMaterial(Materials.Iron, 4)
            .constructMaterial();
    }

    private static Materials loadFerrosilite() {
        return new MaterialBuilder().setName("Ferrosilite")
            .setDefaultLocalName("Ferrosilite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x0097632a)
            .addDustItems()
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadFreshWater() {
        return new MaterialBuilder().setName("FreshWater")
            .setDefaultLocalName("Fresh Water")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x000000ff)
            .addCell()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.AQUA, 2)
            .addSubTag(SubTag.FOOD)
            .constructMaterial();
    }

    private static Materials loadGalena() {
        return new MaterialBuilder().setName("Galena")
            .setDefaultLocalName("Galena")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00643c64)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Lead, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addOreByproduct(() -> Materials.Sulfur)
            .addOreByproduct(() -> Materials.Silver)
            .addOreByproduct(() -> Materials.Lead)
            .setDirectSmelting(() -> Materials.Lead)
            .constructMaterial();
    }

    private static Materials loadGarnierite() {
        return new MaterialBuilder().setName("Garnierite")
            .setDefaultLocalName("Garnierite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x0032c846)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addOreByproduct(() -> Materials.Nickel)
            .addSubTag(SubTag.BLASTFURNACE_CALCITE_DOUBLE)
            .setDirectSmelting(() -> Materials.Nickel)
            .constructMaterial();
    }

    private static Materials loadGlyceryl() {
        return new MaterialBuilder().setName("Glyceryl")
            .setDefaultLocalName("Glyceryl Trinitrate")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x00009696)
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Hydrogen, 5)
            .addMaterial(Materials.Nitrogen, 3)
            .addMaterial(Materials.Oxygen, 9)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.EXPLOSIVE)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.NO_SMASHING)
            .constructMaterial();
    }

    private static Materials loadGreenSapphire() {
        return new MaterialBuilder().setName("GreenSapphire")
            .setDefaultLocalName("Green Sapphire")
            .setIconSet(TextureSet.SET_GEM_HORIZONTAL)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x7f64c882)
            .setTool(256, 2, 7.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Oxygen, 3)
            .addAspect(TCAspects.LUCRUM, 5)
            .addAspect(TCAspects.VITREUS, 3)
            .addOreByproduct(() -> Materials.Aluminiumoxide)
            .addOreByproduct(() -> Materials.Sapphire)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadGrossular() {
        return new MaterialBuilder().setName("Grossular")
            .setDefaultLocalName("Grossular")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00c86400)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.Calcium, 3)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 12)
            .addOreByproduct(() -> Materials.GarnetYellow)
            .addOreByproduct(() -> Materials.Calcium)
            .constructMaterial();
    }

    private static Materials loadHolyWater() {
        return new MaterialBuilder().setName("HolyWater")
            .setDefaultLocalName("Holy Water")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x000000ff)
            .addCell()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.AQUA, 2)
            .addAspect(TCAspects.AURAM, 1)
            .constructMaterial();
    }

    private static Materials loadHydricSulfide() {
        return new MaterialBuilder().setName("HydricSulfide")
            .setDefaultLocalName("Hydrogen Sulfide")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .addCell()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Sulfur, 1)
            .constructMaterial();
    }

    private static Materials loadIce() {
        return new MaterialBuilder().setName("Ice")
            .setDefaultLocalName("Ice")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00c8c8ff)
            .addDustItems()
            .addCell()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 1)
            .setSmeltingInto(() -> Materials.Water)
            .addAspect(TCAspects.GELUM, 2)
            .addSubTag(SubTag.FOOD)
            .addSubTag(SubTag.NO_RECYCLING)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadIlmenite() {
        return new MaterialBuilder().setName("Ilmenite")
            .setDefaultLocalName("Ilmenite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00463732)
            .addDustItems()
            .addOreItems()
            .setDensity(2, 1)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Titanium, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addOreByproduct(() -> Materials.Iron)
            .addOreByproduct(() -> Materials.Rutile)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .constructMaterial();
    }

    private static Materials loadInvar() {
        return new MaterialBuilder().setName("Invar")
            .setDefaultLocalName("Invar")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00b4b478)
            .setTool(256, 2, 6.0f)
            .setToolEnchantment(() -> Enchantment.baneOfArthropods, 3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Iron, 2)
            .addMaterial(Materials.Nickel, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.GELUM, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .constructMaterial();
    }

    private static Materials loadKanthal() {
        return new MaterialBuilder().setName("Kanthal")
            .setDefaultLocalName("Kanthal")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00c2d2df)
            .setTool(64, 2, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .setMeltingPoint(1_800)
            .setBlastFurnaceTemp(1_800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Chrome, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadLazurite() {
        return new MaterialBuilder().setName("Lazurite")
            .setDefaultLocalName("Lazurite")
            .setIconSet(TextureSet.SET_LAPIS)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x006478ff)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(6)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Aluminium, 6)
            .addMaterial(Materials.Silicon, 6)
            .addMaterial(Materials.Calcium, 8)
            .addMaterial(Materials.Sodium, 8)
            .addOreByproduct(() -> Materials.Sodalite)
            .addOreByproduct(() -> Materials.Lapis)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadLiquidNitrogen() {
        return new MaterialBuilder().setName("LiquidNitrogen")
            .setDefaultLocalName("Liquid Nitrogen")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0xf0a9d0f5)
            .addCell()
            .setMeltingPoint(4)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Nitrogen, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadLiquidOxygen() {
        return new MaterialBuilder().setName("LiquidOxygen")
            .setDefaultLocalName("Liquid Oxygen")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0xf0a9d0f5)
            .addCell()
            .setMeltingPoint(4)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Oxygen, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadMagnalium() {
        return new MaterialBuilder().setName("Magnalium")
            .setDefaultLocalName("Magnalium")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x00c8beff)
            .setTool(256, 2, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Aluminium, 2)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadMagnesia() {
        return new MaterialBuilder().setName("Magnesia")
            .setDefaultLocalName("Magnesia")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00ffe1e1)
            .addDustItems()
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Oxygen, 1)
            .constructMaterial();
    }

    private static Materials loadMagnesite() {
        return new MaterialBuilder().setName("Magnesite")
            .setDefaultLocalName("Magnesite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setARGB(0x00fafab4)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addOreByproduct(() -> Materials.Magnesium)
            .constructMaterial();
    }

    private static Materials loadMagnesiumchloride() {
        return new MaterialBuilder().setName("Magnesiumchloride")
            .setDefaultLocalName("Magnesiumchloride")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00d40d5c)
            .addDustItems()
            .addCell()
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Chlorine, 2)
            .constructMaterial();
    }

    private static Materials loadMagnetite() {
        return new MaterialBuilder().setName("Magnetite")
            .setDefaultLocalName("Magnetite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x001e1e1e)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 3)
            .addMaterial(Materials.Oxygen, 4)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .addOreByproduct(() -> Materials.Iron)
            .addOreByproduct(() -> Materials.Gold)
            .setDirectSmelting(() -> Materials.Iron)
            .addSubTag(SubTag.BLASTFURNACE_CALCITE_DOUBLE)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD)
            .constructMaterial();
    }

    private static Materials loadMassicot() {
        return new MaterialBuilder().setName("Massicot")
            .setDefaultLocalName("Massicot")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffdd55)
            .addDustItems()
            .addMaterial(Materials.Lead, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadMethane() {
        return new MaterialBuilder().setName("Methane")
            .setDefaultLocalName("Methane")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeMagenta)
            .setFuel(MaterialBuilder.FuelType.Gas, 104)
            .setOreMultiplier(4)
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Hydrogen, 4)
            .addSubTag(SubTag.ICE_ORE)
            .addSubTag(SubTag.NO_ORE_PROCESSING)
            .addOrePrefix(OrePrefixes.ore)
            .addOrePrefix(OrePrefixes.rawOre)
            .constructMaterial();
    }

    private static Materials loadMolybdenite() {
        return new MaterialBuilder().setName("Molybdenite")
            .setDefaultLocalName("Molybdenite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00191919)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // MoS2 (also source of Re)
            .addMaterial(Materials.Molybdenum, 1)
            .addMaterial(Materials.Sulfur, 2)
            .addOreByproduct(() -> Materials.Molybdenum)
            .setDirectSmelting(() -> Materials.Molybdenum)
            .addSubTag(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE)
            .constructMaterial();
    }

    private static Materials loadNichrome() {
        return new MaterialBuilder().setName("Nichrome")
            .setDefaultLocalName("Nichrome")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00cdcef6)
            .setTool(64, 2, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .setMeltingPoint(2_700)
            .setBlastFurnaceTemp(2_700)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Nickel, 4)
            .addMaterial(Materials.Chrome, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadNickelZincFerrite() {
        return new MaterialBuilder().setName("NickelZincFerrite")
            .setDefaultLocalName("Nickel-Zinc Ferrite")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x003c3c3c)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setTool(32, 0, 3.0f)
            .setBlastFurnaceRequired(true)
            .setBlastFurnaceTemp(1_500)
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.Zinc, 1)
            .addMaterial(Materials.Iron, 4)
            .addMaterial(Materials.Oxygen, 8)
            .constructMaterial();
    }

    private static Materials loadNiobiumNitride() {
        // Anti-Reflective Material
        return new MaterialBuilder().setName("NiobiumNitride")
            .setDefaultLocalName("Niobium Nitride")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x001d291d)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(2_573)
            .setBlastFurnaceTemp(2_573)
            .setBlastFurnaceRequired(true)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Niobium, 1)
            .addMaterial(Materials.Nitrogen, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadNiobiumTitanium() {
        return new MaterialBuilder().setName("NiobiumTitanium")
            .setDefaultLocalName("Niobium-Titanium")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x001d1d29)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(4_500)
            .setBlastFurnaceTemp(4_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Niobium, 1)
            .addMaterial(Materials.Titanium, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadNitroCarbon() {
        return new MaterialBuilder().setName("NitroCarbon")
            .setDefaultLocalName("Nitro-Carbon")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x00004b64)
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Carbon, 1)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.EXPLOSIVE)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.NO_SMASHING)
            .constructMaterial();
    }

    private static Materials loadNitrogenDioxide() {
        return new MaterialBuilder().setName("NitrogenDioxide")
            .setDefaultLocalName("Nitrogen Dioxide")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x0064afff)
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadNobleGases() {
        return new MaterialBuilder().setName("NobleGases")
            .setDefaultLocalName("Noble Gases")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0xf0a9d0f5)
            .addCell()
            .addGas()
            .setMeltingPoint(4)
            .addCentrifugeRecipe()
            .addMaterial(Materials.CarbonDioxide, 21)
            .addMaterial(Materials.Helium, 9)
            .addMaterial(Materials.Methane, 3)
            .addMaterial(Materials.Deuterium, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadObsidian() {
        return new MaterialBuilder().setName("Obsidian")
            .setDefaultLocalName("Obsidian")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00503264)
            .addDustItems()
            .addMetalItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Oxygen, 8)
            .addOreByproduct(() -> Materials.Olivine)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STONE)
            .addOrePrefix(OrePrefixes.plate)
            .removeOrePrefix(OrePrefixes.stickLong) // dreamcraft:item.LongObsidianRod
            .constructMaterial();
    }

    private static Materials loadPhosphate() {
        return new MaterialBuilder().setName("Phosphate")
            .setDefaultLocalName("Phosphate")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .addDustItems()
            .addOreItems()
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Phosphorus, 1)
            .addMaterial(Materials.Oxygen, 4)
            .addOreByproduct(() -> Materials.Phosphorus)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.EXPLOSIVE)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadPigIron() {
        return new MaterialBuilder().setName("PigIron")
            .setDefaultLocalName("Pig Iron")
            .setChemicalFormula("¿Fe?")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setARGB(0x00c8b4b4)
            .setTool(384, 2, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Iron, 1)
            .addOreByproduct(() -> Materials.Iron)
            .setSmeltingInto(() -> Materials.Iron)
            .setMaceratingInto(() -> Materials.Iron)
            .setArcSmeltingInto(() -> Materials.WroughtIron)
            .addSubTag(SubTag.BLASTFURNACE_CALCITE_TRIPLE)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadPolycaprolactam() {
        return new MaterialBuilder().setName("Polycaprolactam")
            .setDefaultLocalName("Polycaprolactam")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00323232)
            .setTool(32, 1, 3.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .removeOrePrefix(OrePrefixes.sheetmetal)
            .setMeltingPoint(500)
            .addMaterial(Materials.Carbon, 6)
            .addMaterial(Materials.Hydrogen, 11)
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.MOTUS, 2)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadPolydimethylsiloxane() {
        return new MaterialBuilder().setName("Polydimethylsiloxane")
            .setDefaultLocalName("Polydimethylsiloxane")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00f5f5f5)
            .addDustItems()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 1)
            .addMaterial(Materials.Silicon, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadPolyethylene() {
        return new MaterialBuilder().setName("Plastic")
            .setDefaultLocalName("Polyethylene")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00c8c8c8)
            .setTool(32, 1, 3.0f)
            .setToolEnchantment(() -> Enchantment.knockback, 1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(400)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Hydrogen, 2)
            .addAspect(TCAspects.MOTUS, 2)
            .addSubTag(SubTag.BOUNCY)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STRETCHY)
            .constructMaterial();
    }

    private static Materials loadPolytetrafluoroethylene() {
        return new MaterialBuilder().setName("Polytetrafluoroethylene")
            .setDefaultLocalName("Polytetrafluoroethylene")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00646464)
            .setTool(32, 1, 3.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(600)
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Fluorine, 4)
            .addAspect(TCAspects.MOTUS, 2)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STRETCHY)
            .constructMaterial();
    }

    private static Materials loadPotash() {
        return new MaterialBuilder().setName("Potash")
            .setDefaultLocalName("Potash")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00784237)
            .addDustItems()
            .addMaterial(Materials.Potassium, 2)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadPowellite() {
        return new MaterialBuilder().setName("Powellite")
            .setDefaultLocalName("Powellite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Molybdenum, 1)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadPumice() {
        return new MaterialBuilder().setName("Pumice")
            .setDefaultLocalName("Pumice")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00e6b9b9)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Stone, 1)
            .constructMaterial();
    }

    private static Materials loadPyrite() {
        return new MaterialBuilder().setName("Pyrite")
            .setDefaultLocalName("Pyrite")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00967828)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Sulfur, 2)
            .addOreByproduct(() -> Materials.Sulfur)
            .addOreByproduct(() -> Materials.TricalciumPhosphate)
            .addOreByproduct(() -> Materials.Iron)
            .setDirectSmelting(() -> Materials.Iron)
            .addSubTag(SubTag.BLASTFURNACE_CALCITE_DOUBLE)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .addSubTag(SubTag.INDUCTIONSMELTING_LOW_OUTPUT)
            .constructMaterial();
    }

    private static Materials loadPyrochlore() {
        return new MaterialBuilder().setName("Pyrochlore")
            .setDefaultLocalName("Pyrochlore")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x002b1100)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Calcium, 2)
            .addMaterial(Materials.Niobium, 2)
            .addMaterial(Materials.Oxygen, 7)
            .addOreByproduct(() -> Materials.Apatite)
            .addOreByproduct(() -> Materials.Calcite)
            .addOreByproduct(() -> Materials.Niobium)
            .constructMaterial();
    }

    private static Materials loadPyrolusite() {
        return new MaterialBuilder().setName("Pyrolusite")
            .setDefaultLocalName("Pyrolusite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x009696aa)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Manganese, 1)
            .addMaterial(Materials.Oxygen, 2)
            .addOreByproduct(() -> Materials.Manganese)
            .addOreByproduct(() -> Materials.Tantalite)
            .addOreByproduct(() -> Materials.Niobium)
            .addSubTag(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE)
            .constructMaterial();
    }

    private static Materials loadPyrope() {
        return new MaterialBuilder().setName("Pyrope")
            .setDefaultLocalName("Pyrope")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00783264)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Magnesium, 3)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 12)
            .addOreByproduct(() -> Materials.GarnetRed)
            .addOreByproduct(() -> Materials.Magnesium)
            .constructMaterial();
    }

    private static Materials loadQuicklime() {
        return new MaterialBuilder().setName("Quicklime")
            .setDefaultLocalName("Quicklime")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00f0f0f0)
            .addDustItems()
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadRockSalt() {
        return new MaterialBuilder().setName("RockSalt")
            .setDefaultLocalName("Rock Salt")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00f0c8c8)
            .addDustItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Chlorine, 1)
            .addOreByproduct(() -> Materials.Salt)
            .addOreByproduct(() -> Materials.Borax)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STONE)
            .constructMaterial();
    }

    private static Materials loadRubber() {
        return new MaterialBuilder().setName("Rubber")
            .setDefaultLocalName("Rubber")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00000000)
            .setTool(32, 0, 1.5f)
            .setToolEnchantment(() -> Enchantment.knockback, 2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(400)
            .addMaterial(Materials.Carbon, 5)
            .addMaterial(Materials.Hydrogen, 8)
            .addAspect(TCAspects.MOTUS, 2)
            .addSubTag(SubTag.BOUNCY)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STRETCHY)
            .constructMaterial();
    }

    private static Materials loadRubberRaw() {
        return new MaterialBuilder().setName("RawRubber")
            .setDefaultLocalName("Raw Rubber")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00ccc789)
            .addDustItems()
            .setMeltingPoint(400)
            .addMaterial(Materials.Carbon, 5)
            .addMaterial(Materials.Hydrogen, 8)
            .addAspect(TCAspects.MOTUS, 2)
            .constructMaterial();
    }

    private static Materials loadRubberSilicone() {
        return new MaterialBuilder().setName("Silicone")
            .setDefaultLocalName("Silicone Rubber")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00dcdcdc)
            .setTool(128, 1, 3.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(900)
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 1)
            .addMaterial(Materials.Silicon, 1)
            .addAspect(TCAspects.MOTUS, 2)
            .addSubTag(SubTag.BOUNCY)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STRETCHY)
            .constructMaterial();
    }

    private static Materials loadRuby() {
        return new MaterialBuilder().setName("Ruby")
            .setDefaultLocalName("Ruby")
            .setIconSet(TextureSet.SET_RUBY)
            .setColor(Dyes.dyeRed)
            .setARGB(0x7fff6464)
            .setTool(256, 2, 7.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addMaterial(Materials.Chrome, 1)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Oxygen, 3)
            .addAspect(TCAspects.LUCRUM, 6)
            .addAspect(TCAspects.VITREUS, 4)
            .addOreByproduct(() -> Materials.Chrome)
            .addOreByproduct(() -> Materials.GarnetRed)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadRutile() {
        return new MaterialBuilder().setName("Rutile")
            .setDefaultLocalName("Rutile")
            .setIconSet(TextureSet.SET_GEM_HORIZONTAL)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00d40d5c)
            .addDustItems()
            .addOreItems()
            .setDensity(2, 1)
            .addMaterial(Materials.Titanium, 1)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadSalt() {
        return new MaterialBuilder().setName("Salt")
            .setDefaultLocalName("Salt")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fafafa)
            .addDustItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Chlorine, 1)
            .addOreByproduct(() -> Materials.RockSalt)
            .addOreByproduct(() -> Materials.Borax)
            .addSubTag(SubTag.FOOD)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STONE)
            .constructMaterial();
    }

    private static Materials loadSaltpeter() {
        return new MaterialBuilder().setName("Saltpeter")
            .setDefaultLocalName("Saltpeter")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00e6e6e6)
            .addDustItems()
            .addOreItems()
            .setOreMultiplier(4)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addOreByproduct(() -> Materials.Saltpeter)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSapphire() {
        return new MaterialBuilder().setName("Sapphire")
            .setDefaultLocalName("Sapphire")
            .setIconSet(TextureSet.SET_GEM_VERTICAL)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x7f6464c8)
            .setTool(256, 2, 7.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Oxygen, 3)
            .addAspect(TCAspects.LUCRUM, 5)
            .addAspect(TCAspects.VITREUS, 3)
            .addOreByproduct(() -> Materials.Aluminiumoxide)
            .addOreByproduct(() -> Materials.GreenSapphire)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadScheelite() {
        return new MaterialBuilder().setName("Scheelite")
            .setDefaultLocalName("Scheelite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00c88c14)
            .addDustItems()
            .addOreItems()
            .setOreMultiplier(2)
            .setMeltingPoint(2_500)
            .setBlastFurnaceTemp(2_500)
            .addMaterial(Materials.Tungsten, 1)
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Oxygen, 4)
            .addOreByproduct(() -> Materials.Manganese)
            .addOreByproduct(() -> Materials.Molybdenum)
            .addOreByproduct(() -> Materials.Calcium)
            .constructMaterial();
    }

    private static Materials loadSiliconDioxide() {
        return new MaterialBuilder().setName("SiliconDioxide")
            .setDefaultLocalName("Silicon Dioxide")
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeLightGray)
            .addDustItems()
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Oxygen, 2)
            .addOreByproduct(() -> Materials.SiliconDioxide)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.QUARTZ)
            .constructMaterial();
    }

    private static Materials loadSnow() {
        return new MaterialBuilder().setName("Snow")
            .setDefaultLocalName("Snow")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fafafa)
            .addDustItems()
            .addCell()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 1)
            .setSmeltingInto(() -> Materials.Water)
            .addAspect(TCAspects.GELUM, 1)
            .addSubTag(SubTag.NO_RECYCLING)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSodaAsh() {
        return new MaterialBuilder().setName("SodaAsh")
            .setDefaultLocalName("Soda Ash")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00dcdcff)
            .addDustItems()
            .addMaterial(Materials.Sodium, 2)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadSodalite() {
        return new MaterialBuilder().setName("Sodalite")
            .setDefaultLocalName("Sodalite")
            .setIconSet(TextureSet.SET_LAPIS)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x001414ff)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(6)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Aluminium, 3)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Sodium, 4)
            .addMaterial(Materials.Chlorine, 1)
            .addOreByproduct(() -> Materials.Lazurite)
            .addOreByproduct(() -> Materials.Lapis)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadSodiumPersulfate() {
        return new MaterialBuilder().setName("SodiumPersulfate")
            .setDefaultLocalName("Sodium Persulfate")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Sodium, 2)
            .addMaterial(Materials.Sulfur, 2)
            .addMaterial(Materials.Oxygen, 8)
            .constructMaterial();
    }

    private static Materials loadSodiumSulfide() {
        return new MaterialBuilder().setName("SodiumSulfide")
            .setDefaultLocalName("Sodium Sulfide")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ffe680)
            .addDustItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Sodium, 2)
            .addMaterial(Materials.Sulfur, 1)
            .constructMaterial();
    }

    private static Materials loadTitaniumtetrachloride() {
        return new MaterialBuilder().setName("Titaniumtetrachloride")
            .setDefaultLocalName("Titaniumtetrachloride")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00d40d5c)
            .addCell()
            .addMaterial(Materials.Titanium, 1)
            .addMaterial(Materials.Chlorine, 4)
            .constructMaterial();
    }

    private static Materials loadWater() {
        return new MaterialBuilder().setName("Water")
            .setDefaultLocalName("Water")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x000000ff)
            .addCell()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.AQUA, 2)
            .addSubTag(SubTag.FOOD)
            .addSubTag(SubTag.NO_RECYCLING)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .removeOrePrefix(OrePrefixes.cell) // IC2:itemCellEmpty:1
            .removeOrePrefix(OrePrefixes.bucket) // minecraft:water_bucket
            .removeOrePrefix(OrePrefixes.bucketClay) // IguanaTweaksTConstruct:clayBucketWater
            .removeOrePrefix(OrePrefixes.bottle) // minecraft:potion:0
            .constructMaterial();
    }

    private static Materials loadZincite() {
        return new MaterialBuilder().setName("Zincite")
            .setDefaultLocalName("Zincite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fffff5)
            .addDustItems()
            .addMaterial(Materials.Zinc, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static void loadUnclassified01() {
        Materials.DenseSteam = loadDenseSteam();
        Materials.DenseSuperheatedSteam = loadDenseSuperheatedSteam();
        Materials.DenseSupercriticalSteam = loadDenseSupercriticalSteam();
        Materials.OilExtraHeavy = loadOilExtraHeavy();
        Materials.OilHeavy = loadOilHeavy();
        Materials.OilLight = loadOilLight();
        Materials.OilMedium = loadOilMedium();
        Materials.SuperCoolant = loadSuperCoolant();
        Materials.EnrichedHolmium = loadEnrichedHolmium();
        Materials.TengamPurified = loadTengamPurified();
        Materials.TengamAttuned = loadTengamAttuned();
        Materials.TengamRaw = loadTengamRaw();
    }

    private static Materials loadDenseSteam() {
        return new MaterialBuilder().setName("DenseSteam")
            .setDefaultLocalName("Dense Steam")
            .addCell()
            .addGas()
            .setColor(Dyes.dyeWhite)
            .constructMaterial();
    }

    private static Materials loadDenseSuperheatedSteam() {
        return new MaterialBuilder().setName("DenseSuperheatedSteam")
            .setDefaultLocalName("Dense Superheated Steam")
            .addCell()
            .addGas()
            .setColor(Dyes.dyeWhite)
            .constructMaterial();
    }

    private static Materials loadDenseSupercriticalSteam() {
        return new MaterialBuilder().setName("DenseSupercriticalSteam")
            .setDefaultLocalName("Dense Supercritical Steam")
            .addCell()
            .addGas()
            .setColor(Dyes.dyeWhite)
            .constructMaterial();
    }

    private static Materials loadOilExtraHeavy() {
        return new MaterialBuilder().setName("OilExtraHeavy")
            .setDefaultLocalName("Very Heavy Oil")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000a0a0a)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 45)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadOilHeavy() {
        return new MaterialBuilder().setName("OilHeavy")
            .setDefaultLocalName("Heavy Oil")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000a0a0a)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 40)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadOilLight() {
        return new MaterialBuilder().setName("OilLight")
            .setDefaultLocalName("Light Oil")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000a0a0a)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 20)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadOilMedium() {
        return new MaterialBuilder().setName("OilMedium")
            .setDefaultLocalName("Raw Oil")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000a0a0a)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 30)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadSuperCoolant() {
        return new MaterialBuilder().setName("SuperCoolant")
            .setDefaultLocalName("Super Coolant")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00025b6f)
            .addCell()
            .addFluid()
            .constructMaterial()
            .setLiquidTemperature(1);
    }

    private static Materials loadEnrichedHolmium() {
        return new MaterialBuilder().setName("EnrichedHolmium")
            .setDefaultLocalName("Enriched Holmium")
            .setChemicalFormula("Nq+₄Ho₁")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePurple)
            .setARGB(0x001264ff)
            .addMetalItems()
            .setBlastFurnaceTemp(3_000)
            .setBlastFurnaceRequired(true)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadTengamPurified() {
        return new MaterialBuilder().setName("TengamPurified")
            .setDefaultLocalName("Purified Tengam")
            .setChemicalFormula("M")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00badf70)
            .addDustItems()
            .addGearItems()
            .addMetalItems()
            .addToolHeadItems()
            .addAspect(TCAspects.MAGNETO, 2)
            .addAspect(TCAspects.ELECTRUM, 2)
            .addSubTag(SubTag.METAL)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UV);
    }

    private static Materials loadTengamAttuned() {
        return new MaterialBuilder().setName("TengamAttuned")
            .setDefaultLocalName("Attuned Tengam")
            .setChemicalFormula(CustomGlyphs.HIGH_VOLTAGE + "M" + CustomGlyphs.MAGNET)
            .setIconSet(TextureSet.SET_MAGNETIC)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00d5ff80)
            .addDustItems()
            .addGearItems()
            .addMetalItems()
            .addToolHeadItems()
            .setSmeltingInto(() -> Materials.TengamPurified)
            .setMaceratingInto(() -> Materials.TengamPurified)
            .setArcSmeltingInto(() -> Materials.TengamPurified)
            .addAspect(TCAspects.MAGNETO, 4)
            .addAspect(TCAspects.ELECTRUM, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UV);
    }

    private static Materials loadTengamRaw() {
        return new MaterialBuilder().setName("TengamRaw")
            .setDefaultLocalName("Raw Tengam")
            .setChemicalFormula("M" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00a0bf60)
            .addOreItems()
            .addAspect(TCAspects.MAGNETO, 1)
            .addAspect(TCAspects.ELECTRUM, 4)
            .addOreByproduct(() -> Materials.NeodymiumMagnetic)
            .addOreByproduct(() -> Materials.SamariumMagnetic)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UV);
    }

    private static void loadUnclassified02() {
        Materials.Gas = loadGas();
        Materials.HeavyFuel = loadHeavyFuel();
        Materials.LightFuel = loadLightFuel();
        Materials.LPG = loadLPG();
        Materials.Naphtha = loadNaphtha();
        Materials.NaturalGas = loadNaturalGas();
        Materials.SulfuricGas = loadSulfuricGas();
        Materials.SulfuricHeavyFuel = loadSulfuricHeavyFuel();
        Materials.SulfuricLightFuel = loadSulfuricLightFuel();
        Materials.SulfuricNaphtha = loadSulfuricNaphtha();
    }

    private static Materials loadGas() {
        return new MaterialBuilder().setName("Gas")
            .setDefaultLocalName("Refinery Gas")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setFuel(MaterialBuilder.FuelType.Gas, 160)
            .addCell()
            .addCrackingRecipes()
            .constructMaterial();
    }

    private static Materials loadHeavyFuel() {
        return new MaterialBuilder().setName("HeavyFuel")
            .setDefaultLocalName("Heavy Fuel")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00ffff00)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 240)
            .addCell()
            .addCrackingRecipes()
            .constructMaterial();
    }

    private static Materials loadLightFuel() {
        return new MaterialBuilder().setName("LightFuel")
            .setDefaultLocalName("Light Fuel")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .setFuel(MaterialBuilder.FuelType.Diesel, 305)
            .addCell()
            .addCrackingRecipes()
            .constructMaterial();
    }

    private static Materials loadLPG() {
        return new MaterialBuilder().setName("LPG")
            .setDefaultLocalName("LPG")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .setFuel(MaterialBuilder.FuelType.Gas, 320)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadNaphtha() {
        return new MaterialBuilder().setName("Naphtha")
            .setDefaultLocalName("Naphtha")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .setFuel(MaterialBuilder.FuelType.Gas, 220)
            .addCell()
            .addCrackingRecipes()
            .constructMaterial();
    }

    private static Materials loadNaturalGas() {
        return new MaterialBuilder().setName("NatruralGas")
            .setDefaultLocalName("Natural Gas")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setFuel(MaterialBuilder.FuelType.Gas, 20)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadSulfuricGas() {
        return new MaterialBuilder().setName("SulfuricGas")
            .setDefaultLocalName("Sulfuric Gas")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setFuel(MaterialBuilder.FuelType.Gas, 25)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadSulfuricHeavyFuel() {
        return new MaterialBuilder().setName("SulfuricHeavyFuel")
            .setDefaultLocalName("Sulfuric Heavy Fuel")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00ffff00)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 40)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadSulfuricLightFuel() {
        return new MaterialBuilder().setName("SulfuricLightFuel")
            .setDefaultLocalName("Sulfuric Light Fuel")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .setFuel(MaterialBuilder.FuelType.Diesel, 40)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadSulfuricNaphtha() {
        return new MaterialBuilder().setName("SulfuricNaphtha")
            .setDefaultLocalName("Sulfuric Naphtha")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .setFuel(MaterialBuilder.FuelType.Gas, 40)
            .addCell()
            .constructMaterial();
    }

    private static void loadUnclassified03() {
        Materials.BioMediumRaw = loadBioMediumRaw();
        Materials.BioMediumSterilized = loadBioMediumSterilized();
        Materials.ReinforcedGlass = loadReinforcedGlass();
    }

    private static Materials loadBioMediumRaw() {
        return new MaterialBuilder().setName("BioMediumRaw")
            .setDefaultLocalName("Raw Bio Catalyst Medium")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x0061932e)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadBioMediumSterilized() {
        return new MaterialBuilder().setName("BiohMediumSterilized")
            .setDefaultLocalName("Sterilized Bio Catalyst Medium")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00a2fd35)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadReinforcedGlass() {
        return new MaterialBuilder().setName("ReinforcedGlass")
            .setDefaultLocalName("Reinforced Glass")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00c0f5fe)
            .setMeltingPoint(2_000)
            .setAutoGeneratedRecycleRecipes(false)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .constructMaterial();
    }

    private static void loadUnclassified04() {
        Materials.GrowthMediumRaw = loadGrowthMediumRaw();
        Materials.GrowthMediumSterilized = loadGrowthMediumSterilized();
    }

    private static Materials loadGrowthMediumRaw() {
        return new MaterialBuilder().setName("GrowthMediumRaw")
            .setDefaultLocalName("Raw Growth Catalyst Medium")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00d38d5f)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadGrowthMediumSterilized() {
        return new MaterialBuilder().setName("GrowthMediumSterilized")
            .setDefaultLocalName("Growth Catalyst Medium")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00deaa87)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static void loadUnclassified05() {
        Materials.BioDiesel = loadBioDiesel();
        Materials.BisphenolA = loadBisphenolA();
        Materials.Butadiene = loadButadiene();
        Materials.Butane = loadButane();
        Materials.Butene = loadButene();
        Materials.CalciumAcetateSolution = loadCalciumAcetateSolution();
        Materials.CarbonMonoxide = loadCarbonMonoxide();
        Materials.CaesiumHydroxide = loadCaesiumHydroxide();
        Materials.Chloramine = loadChloramine();
        Materials.Chloroform = loadChloroform();
        Materials.Chloromethane = loadChloromethane();
        Materials.Cumene = loadCumene();
        Materials.Dichlorobenzene = loadDichlorobenzene();
        Materials.Dimethylamine = loadDimethylamine();
        Materials.Dimethyldichlorosilane = loadDimethyldichlorosilane();
        Materials.Dimethylhydrazine = loadDimethylhydrazine();
        Materials.DinitrogenTetroxide = loadDinitrogenTetroxide();
        Materials.Epichlorohydrin = loadEpichlorohydrin();
        Materials.Ethane = loadEthane();
        Materials.Ethenone = loadEthenone();
        Materials.Ethylene = loadEthylene();
        Materials.Glycerol = loadGlycerol();
        Materials.HydrochloricAcid = loadHydrochloricAcid();
        Materials.HydrofluoricAcid = loadHydrofluoricAcid();
        Materials.HypochlorousAcid = loadHypochlorousAcid();
        Materials.IronIIIChloride = loadIronIIIChloride();
        Materials.Isoprene = loadIsoprene();
        Materials.LifeEssence = loadLifeEssence();
        Materials.MetalMixture = loadMetalMixture();
        Materials.Methanol = loadMethanol();
        Materials.MethylAcetate = loadMethylAcetate();
        Materials.NitrationMixture = loadNitrationMixture();
        Materials.NitricAcid = loadNitricAcid();
        Materials.NitricOxide = loadNitricOxide();
        Materials.Phenol = loadPhenol();
        Materials.PhosphoricAcid = loadPhosphoricAcid();
        Materials.PhosphorousPentoxide = loadPhosphorousPentoxide();
        Materials.PolyphenyleneSulfide = loadPolyphenyleneSulfide();
        Materials.Polystyrene = loadPolystyrene();
        Materials.PolyvinylAcetate = loadPolyvinylAcetate();
        Materials.PolyvinylChloride = loadPolyvinylChloride();
        Materials.PotassiumHydroxide = loadPotassiumHydroxide();
        Materials.Propane = loadPropane();
        Materials.Propene = loadPropene();
        Materials.SaltWater = loadSaltWater();
        Materials.SodiumBisulfate = loadSodiumBisulfate();
        Materials.SodiumHydroxide = loadSodiumHydroxide();
        Materials.SodiumOxide = loadSodiumOxide();
        Materials.Styrene = loadStyrene();
        Materials.StyreneButadieneRubber = loadStyreneButadieneRubber();
        Materials.SulfurDioxide = loadSulfurDioxide();
        Materials.SulfurTrioxide = loadSulfurTrioxide();
        Materials.Tetrafluoroethylene = loadTetrafluoroethylene();
        Materials.Tetranitromethane = loadTetranitromethane();
        Materials.Toluene = loadToluene();
        Materials.VinylAcetate = loadVinylAcetate();
        Materials.VinylChloride = loadVinylChloride();
    }

    private static Materials loadBioDiesel() {
        return new MaterialBuilder().setName("BioDiesel")
            .setDefaultLocalName("Bio Diesel")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff8000)
            .setFuel(MaterialBuilder.FuelType.Diesel, 320)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadBisphenolA() {
        return new MaterialBuilder().setName("BisphenolA")
            .setDefaultLocalName("Bisphenol A")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00d4aa00)
            .addCell()
            .addMaterial(Materials.Carbon, 15)
            .addMaterial(Materials.Hydrogen, 16)
            .addMaterial(Materials.Oxygen, 2)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadButadiene() {
        return new MaterialBuilder().setName("Butadiene")
            .setDefaultLocalName("Butadiene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00e86900)
            .setFuel(MaterialBuilder.FuelType.Gas, 206)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Hydrogen, 6)
            .addElectrolyzerRecipe()
            .addCrackingRecipes()
            .constructMaterial();
    }

    private static Materials loadButane() {
        return new MaterialBuilder().setName("Butane")
            .setDefaultLocalName("Butane")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00b6371e)
            .setFuel(MaterialBuilder.FuelType.Gas, 296)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Hydrogen, 10)
            .addElectrolyzerRecipe()
            .addCrackingRecipes()
            .constructMaterial();
    }

    private static Materials loadButene() {
        return new MaterialBuilder().setName("Butene")
            .setDefaultLocalName("Butene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00cf5005)
            .setFuel(MaterialBuilder.FuelType.Gas, 256)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Hydrogen, 8)
            .addElectrolyzerRecipe()
            .addCrackingRecipes()
            .constructMaterial();
    }

    private static Materials loadCaesiumHydroxide() {
        return new MaterialBuilder().setName("CaesiumHydroxide_GT5U")
            .setDefaultLocalName("Caesium Hydroxide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x00becaee)
            .addDustItems()
            .addMaterial(Materials.Caesium, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .constructMaterial();
    }

    private static Materials loadCalciumAcetateSolution() {
        return new MaterialBuilder().setName("CalciumAcetateSolution")
            .setDefaultLocalName("Calcium Acetate Solution")
            .setIconSet(TextureSet.SET_RUBY)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x00dcc8b4)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Oxygen, 4)
            .addMaterial(Materials.Hydrogen, 6)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadCarbonMonoxide() {
        return new MaterialBuilder().setName("CarbonMonoxide")
            .setDefaultLocalName("Carbon Monoxide")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x000e4880)
            .setFuel(MaterialBuilder.FuelType.Gas, 24)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Oxygen, 1)
            .constructMaterial();
    }

    private static Materials loadChloramine() {
        return new MaterialBuilder().setName("Chloramine")
            .setDefaultLocalName("Chloramine")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x003f9f80)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Chlorine, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadChloroform() {
        return new MaterialBuilder().setName("Chloroform")
            .setDefaultLocalName("Chloroform")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00892ca0)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Chlorine, 3)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadChloromethane() {
        return new MaterialBuilder().setName("Chloromethane")
            .setDefaultLocalName("Chloromethane")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeMagenta)
            .setARGB(0x00c82ca0)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Hydrogen, 3)
            .addMaterial(Materials.Chlorine, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadCumene() {
        return new MaterialBuilder().setName("Isopropylbenzene")
            .setDefaultLocalName("Isopropylbenzene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00552200)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 9)
            .addMaterial(Materials.Hydrogen, 12)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDichlorobenzene() {
        return new MaterialBuilder().setName("Dichlorobenzene")
            .setDefaultLocalName("Dichlorobenzene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00004455)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 6)
            .addMaterial(Materials.Hydrogen, 4)
            .addMaterial(Materials.Chlorine, 2)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDimethylamine() {
        return new MaterialBuilder().setName("Dimethylamine")
            .setDefaultLocalName("Dimethylamine")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00554469)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 7)
            .addMaterial(Materials.Nitrogen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDimethyldichlorosilane() {
        return new MaterialBuilder().setName("Dimethyldichlorosilane")
            .setDefaultLocalName("Dimethyldichlorosilane")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00441650)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Chlorine, 2)
            .addMaterial(Materials.Silicon, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDimethylhydrazine() {
        return new MaterialBuilder().setName("1,1Dimethylhydrazine")
            .setDefaultLocalName("1,1-Dimethylhydrazine")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00000055)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 8)
            .addMaterial(Materials.Nitrogen, 2)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDinitrogenTetroxide() {
        return new MaterialBuilder().setName("DinitrogenTetroxide")
            .setDefaultLocalName("Dinitrogen Tetroxide")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00004184)
            .addCell()
            .addGas()
            .addMaterial(Materials.Nitrogen, 2)
            .addMaterial(Materials.Oxygen, 4)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadEpichlorohydrin() {
        return new MaterialBuilder().setName("Epichlorohydrin")
            .setDefaultLocalName("Epichlorohydrin")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00501d05)
            .addCell()
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Hydrogen, 5)
            .addMaterial(Materials.Chlorine, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadEthane() {
        return new MaterialBuilder().setName("Ethane")
            .setDefaultLocalName("Ethane")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x00c8c8ff)
            .setFuel(MaterialBuilder.FuelType.Gas, 168)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 6)
            .addElectrolyzerRecipe()
            .addCrackingRecipes()
            .constructMaterial();
    }

    private static Materials loadEthenone() {
        return new MaterialBuilder().setName("Ethenone")
            .setDefaultLocalName("Ethenone")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00141446)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadEthylene() {
        return new MaterialBuilder().setName("Ethylene")
            .setDefaultLocalName("Ethylene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00e1e1e1)
            .setFuel(MaterialBuilder.FuelType.Gas, 128)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 4)
            .addElectrolyzerRecipe()
            .addCrackingRecipes()
            .constructMaterial();
    }

    private static Materials loadGlycerol() {
        return new MaterialBuilder().setName("Glycerol")
            .setDefaultLocalName("Glycerol")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x0087de87)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 164)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Hydrogen, 8)
            .addMaterial(Materials.Oxygen, 3)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadHydrochloricAcid() {
        return new MaterialBuilder().setName("HydrochloricAcid_GT5U")
            .setDefaultLocalName("Hydrochloric Acid")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00b7c8c4)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Chlorine, 1)
            .constructMaterial();
    }

    private static Materials loadHydrofluoricAcid() {
        return new MaterialBuilder().setName("HydrofluoricAcid_GT5U")
            .setDefaultLocalName("Hydrofluoric Acid")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x000088aa)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Fluorine, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadHypochlorousAcid() {
        return new MaterialBuilder().setName("HypochlorousAcid")
            .setDefaultLocalName("Hypochlorous Acid")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGray)
            .setARGB(0x006f8a91)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Chlorine, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadIronIIIChloride() {
        return new MaterialBuilder().setName("IronIIIChloride")
            .setDefaultLocalName("Iron III Chloride")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x0016150e)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Chlorine, 3)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadIsoprene() {
        return new MaterialBuilder().setName("Isoprene")
            .setDefaultLocalName("Isoprene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00141414)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 5)
            .addMaterial(Materials.Hydrogen, 8)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadLifeEssence() {
        return new MaterialBuilder().setName("lifeessence")
            .setDefaultLocalName("Life")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeRed)
            .setARGB(0x006e0303)
            .setFuel(MaterialBuilder.FuelType.Magic, 100)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadMetalMixture() {
        return new MaterialBuilder().setName("MetalMixture")
            .setDefaultLocalName("Metal Mixture")
            .setChemicalFormula(
                "Fe" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "O" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00502d16)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMethanol() {
        return new MaterialBuilder().setName("Methanol")
            .setDefaultLocalName("Methanol")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00aa8800)
            .setFuel(MaterialBuilder.FuelType.Diesel, 84)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Hydrogen, 4)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadMethylAcetate() {
        return new MaterialBuilder().setName("MethylAcetate")
            .setDefaultLocalName("Methyl Acetate")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00eec6af)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 2)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadNitrationMixture() {
        return new MaterialBuilder().setName("NitrationMixture")
            .setDefaultLocalName("Nitration Mixture")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00e6e2ab)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadNitricAcid() {
        return new MaterialBuilder().setName("NitricAcid")
            .setDefaultLocalName("Nitric Acid")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00e6e2ab)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadNitricOxide() {
        return new MaterialBuilder().setName("NitricOxide")
            .setDefaultLocalName("Nitric Oxide")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x007dc8f0)
            .addCell()
            .addGas()
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadPhenol() {
        return new MaterialBuilder().setName("Phenol")
            .setDefaultLocalName("Phenol")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00784421)
            .setFuel(MaterialBuilder.FuelType.Gas, 288)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 6)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadPhosphoricAcid() {
        return new MaterialBuilder().setName("PhosphoricAcid_GT5U")
            .setDefaultLocalName("Phosphoric Acid")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00dcdc00)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Hydrogen, 3)
            .addMaterial(Materials.Phosphorus, 1)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadPhosphorousPentoxide() {
        return new MaterialBuilder().setName("PhosphorousPentoxide")
            .setDefaultLocalName("Phosphorous Pentoxide")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00dcdc00)
            .addCell()
            .addDustItems()
            .addMaterial(Materials.Phosphorus, 4)
            .addMaterial(Materials.Oxygen, 10)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadPolyphenyleneSulfide() {
        return new MaterialBuilder().setName("PolyphenyleneSulfide")
            .setDefaultLocalName("Polyphenylene Sulfide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00aa8800)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setTool(32, 1, 3.0f)
            .addMaterial(Materials.Carbon, 6)
            .addMaterial(Materials.Hydrogen, 4)
            .addMaterial(Materials.Sulfur, 1)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STRETCHY)
            .constructMaterial();
    }

    private static Materials loadPolystyrene() {
        return new MaterialBuilder().setName("Polystyrene")
            .setDefaultLocalName("Polystyrene")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00beb4aa)
            .setTool(32, 1, 3.0f)
            .setToolEnchantment(() -> Enchantment.knockback, 1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addMaterial(Materials.Carbon, 8)
            .addMaterial(Materials.Hydrogen, 8)
            .addSubTag(SubTag.BOUNCY)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STRETCHY)
            .constructMaterial();
    }

    private static Materials loadPolyvinylAcetate() {
        return new MaterialBuilder().setName("PolyvinylAcetate")
            .setDefaultLocalName("Polyvinyl Acetate")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff9955)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadPolyvinylChloride() {
        return new MaterialBuilder().setName("PolyvinylChloride")
            .setDefaultLocalName("Polyvinyl Chloride")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00d7e6e6)
            .setTool(32, 1, 3.0f)
            .setToolEnchantment(() -> Enchantment.knockback, 1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 3)
            .addMaterial(Materials.Chlorine, 1)
            .addSubTag(SubTag.BOUNCY)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STRETCHY)
            .constructMaterial();
    }

    private static Materials loadPotassiumHydroxide() {
        return new MaterialBuilder().setName("PotassiumHydroxide_GT5U")
            .setDefaultLocalName("Potassium Hydroxide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x005e4a9e)
            .addDustItems()
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .constructMaterial();
    }

    private static Materials loadPropane() {
        return new MaterialBuilder().setName("Propane")
            .setDefaultLocalName("Propane")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00fae250)
            .setFuel(MaterialBuilder.FuelType.Gas, 232)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Hydrogen, 8)
            .addElectrolyzerRecipe()
            .addCrackingRecipes()
            .constructMaterial();
    }

    private static Materials loadPropene() {
        return new MaterialBuilder().setName("Propene")
            .setDefaultLocalName("Propene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffdd55)
            .setFuel(MaterialBuilder.FuelType.Gas, 192)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Hydrogen, 6)
            .addElectrolyzerRecipe()
            .addCrackingRecipes()
            .constructMaterial();
    }

    private static Materials loadSaltWater() {
        return new MaterialBuilder().setName("SaltWater")
            .setDefaultLocalName("Salt Water")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x000000c8)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadSodiumBisulfate() {
        return new MaterialBuilder().setName("SodiumBisulfate")
            .setDefaultLocalName("Sodium Bisulfate")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00004455)
            .addDustItems()
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadSodiumHydroxide() {
        return new MaterialBuilder().setName("SodiumHydroxide_GT5U")
            .setDefaultLocalName("Sodium Hydroxide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00003380)
            .addDustItems()
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .constructMaterial();
    }

    private static Materials loadSodiumOxide() {
        return new MaterialBuilder().setName("SodiumOxide")
            .setDefaultLocalName("Sodium Oxide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00ffffeb)
            .addDustItems()
            .addMaterial(Materials.Sodium, 2)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadStyrene() {
        return new MaterialBuilder().setName("Styrene")
            .setDefaultLocalName("Styrene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00d2c8be)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 8)
            .addMaterial(Materials.Hydrogen, 8)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadStyreneButadieneRubber() {
        return new MaterialBuilder().setName("StyreneButadieneRubber")
            .setDefaultLocalName("Styrene-Butadiene Rubber")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00211a18)
            .setTool(128, 1, 3.0f)
            .setToolEnchantment(() -> Enchantment.knockback, 2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addMaterial(Materials.Styrene, 1)
            .addMaterial(Materials.Butadiene, 3)
            .addSubTag(SubTag.BOUNCY)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STRETCHY)
            .constructMaterial();
    }

    private static Materials loadSulfurDioxide() {
        return new MaterialBuilder().setName("SulfurDioxide")
            .setDefaultLocalName("Sulfur Dioxide")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00c8c819)
            .setOreMultiplier(4)
            .addCell()
            .addGas()
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Oxygen, 2)
            .addSubTag(SubTag.ICE_ORE)
            .addSubTag(SubTag.NO_ORE_PROCESSING)
            .addOrePrefix(OrePrefixes.ore)
            .addOrePrefix(OrePrefixes.rawOre)
            .constructMaterial();
    }

    private static Materials loadSulfurTrioxide() {
        return new MaterialBuilder().setName("SulfurTrioxide")
            .setDefaultLocalName("Sulfur Trioxide")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00a0a014)
            .addCell()
            .addGas()
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadTetrafluoroethylene() {
        return new MaterialBuilder().setName("Tetrafluoroethylene")
            .setDefaultLocalName("Tetrafluoroethylene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGray)
            .setARGB(0x007d7d7d)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Fluorine, 4)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadTetranitromethane() {
        return new MaterialBuilder().setName("Tetranitromethane")
            .setDefaultLocalName("Tetranitromethane")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000f2828)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Nitrogen, 4)
            .addMaterial(Materials.Oxygen, 8)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadToluene() {
        return new MaterialBuilder().setName("Toluene")
            .setDefaultLocalName("Toluene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00501d05)
            .setFuel(MaterialBuilder.FuelType.Gas, 328)
            .addCell()
            .addMaterial(Materials.Carbon, 7)
            .addMaterial(Materials.Hydrogen, 8)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadVinylAcetate() {
        return new MaterialBuilder().setName("VinylAcetate")
            .setDefaultLocalName("Vinyl Acetate")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ffb380)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 2)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadVinylChloride() {
        return new MaterialBuilder().setName("VinylChloride")
            .setDefaultLocalName("Vinyl Chloride")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00e1f0f0)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 3)
            .addMaterial(Materials.Chlorine, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static void loadRoastedOres() {
        Materials.RoastedAntimony = loadRoastedAntimony();
        Materials.RoastedArsenic = loadRoastedArsenic();
        Materials.RoastedCobalt = loadRoastedCobalt();
        Materials.RoastedCopper = loadRoastedCopper();
        Materials.RoastedIron = loadRoastedIron();
        Materials.RoastedLead = loadRoastedLead();
        Materials.RoastedNickel = loadRoastedNickel();
        Materials.RoastedZinc = loadRoastedZinc();
    }

    private static Materials loadRoastedAntimony() {
        return new MaterialBuilder().setName("RoastedAntimony")
            .setDefaultLocalName("Roasted Antimony")
            .setChemicalFormula("Sb" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK)
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00c4b2c2)
            .addDustItems()
            .setDirectSmelting(() -> Materials.Antimony)
            .constructMaterial();
    }

    private static Materials loadRoastedArsenic() {
        return new MaterialBuilder().setName("RoastedArsenic")
            .setDefaultLocalName("Roasted Arsenic")
            .setChemicalFormula("As" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK)
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x00f0f0f0)
            .addDustItems()
            .setDirectSmelting(() -> Materials.Arsenic)
            .constructMaterial();
    }

    private static Materials loadRoastedCobalt() {
        return new MaterialBuilder().setName("RoastedCobalt")
            .setDefaultLocalName("Roasted Cobalt")
            .setChemicalFormula("Co" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK)
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00084009)
            .addDustItems()
            .setDirectSmelting(() -> Materials.Cobalt)
            .constructMaterial();
    }

    private static Materials loadRoastedCopper() {
        return new MaterialBuilder().setName("RoastedCopper")
            .setDefaultLocalName("Roasted Copper")
            .setChemicalFormula("Cu" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK)
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x004d1212)
            .addDustItems()
            .setDirectSmelting(() -> Materials.Copper)
            .constructMaterial();
    }

    private static Materials loadRoastedIron() {
        return new MaterialBuilder().setName("RoastedIron")
            .setDefaultLocalName("Roasted Iron")
            .setChemicalFormula("Fe" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK)
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00946262)
            .addDustItems()
            .addOreItems()
            .setDirectSmelting(() -> Materials.Iron)
            .constructMaterial();
    }

    private static Materials loadRoastedLead() {
        return new MaterialBuilder().setName("RoastedLead")
            .setDefaultLocalName("Roasted Lead")
            .setChemicalFormula("Pb" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK)
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x00a8952b)
            .addDustItems()
            .setDirectSmelting(() -> Materials.Lead)
            .constructMaterial();
    }

    private static Materials loadRoastedNickel() {
        return new MaterialBuilder().setName("RoastedNickel")
            .setDefaultLocalName("Roasted Nickel")
            .setChemicalFormula("Ni" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK)
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00468c2d)
            .addDustItems()
            .addOreItems()
            .addOreByproduct(() -> Materials.Nickel)
            .setDirectSmelting(() -> Materials.Nickel)
            .constructMaterial();
    }

    private static Materials loadRoastedZinc() {
        return new MaterialBuilder().setName("RoastedZinc")
            .setDefaultLocalName("Roasted Zinc")
            .setChemicalFormula("Zn" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK)
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00d1d1d1)
            .addDustItems()
            .setDirectSmelting(() -> Materials.Zinc)
            .constructMaterial();
    }

    private static void loadSiliconLine() {
        Materials.AluminiumFluoride = loadAluminiumFluoride();
        Materials.CalciumDisilicide = loadCalciumDisilicide();
        Materials.Calciumhydride = loadCalciumhydride();
        Materials.Dichlorosilane = loadDichlorosilane();
        Materials.Hexachlorodisilane = loadHexachlorodisilane();
        Materials.Silane = loadSilane();
        Materials.SiliconSG = loadSiliconSG();
        Materials.SiliconTetrachloride = loadSiliconTetrachloride();
        Materials.SiliconTetrafluoride = loadSiliconTetrafluoride();
        Materials.Trichlorosilane = loadTrichlorosilane();
    }

    private static Materials loadAluminiumFluoride() {
        return new MaterialBuilder().setName("Aluminiumfluoride")
            .setDefaultLocalName("Aluminium Fluoride")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .addDustItems()
            .addOreItems()
            .setMeltingPoint(1_533)
            .addElectrolyzerRecipe()
            // ALF3
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Fluorine, 3)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.ORDO, 1)
            .constructMaterial();
    }

    private static Materials loadCalciumDisilicide() {
        return new MaterialBuilder().setName("CalciumDisilicide")
            .setDefaultLocalName("Calcium Disilicide")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00b4b4b4)
            .addDustItems()
            .addOreItems()
            .setMeltingPoint(1_313)
            .addElectrolyzerRecipe()
            // CaSi2
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Silicon, 2)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.ORDO, 1)
            .constructMaterial();
    }

    private static Materials loadCalciumhydride() {
        return new MaterialBuilder().setName("CalciumHydride")
            .setDefaultLocalName("Calcium Hydride")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00dcdcdc)
            .addDustItems()
            .addOreItems()
            .setMeltingPoint(1_089)
            .addElectrolyzerRecipe()
            // CaH2
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Hydrogen, 2)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.ORDO, 1)
            .constructMaterial();
    }

    private static Materials loadDichlorosilane() {
        return new MaterialBuilder().setName("Dichlorosilane")
            .setDefaultLocalName("Dichlorosilane")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00ffffff)
            .addCell()
            .addGas()
            .setMeltingPoint(151)
            .addElectrolyzerRecipe()
            // SIH2CL2
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Chlorine, 2)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadHexachlorodisilane() {
        return new MaterialBuilder().setName("Hexachlorodisilane")
            .setDefaultLocalName("Hexachlorodisilane")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .setMeltingPoint(272)
            .addElectrolyzerRecipe()
            // SI2CL6
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Chlorine, 6)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial();
    }

    private static Materials loadSilane() {
        return new MaterialBuilder().setName("Silane")
            .setDefaultLocalName("Silane")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addGas()
            .setMeltingPoint(88)
            // SIH4
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Hydrogen, 4)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial();
    }

    private static Materials loadSiliconSG() {
        return new MaterialBuilder().setName("SiliconSolarGrade")
            .setDefaultLocalName("Silicon Solar Grade (Poly SI)")
            .setChemicalFormula("Si*")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00505064)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .setMeltingPoint(2_273)
            .setBlastFurnaceTemp(2_273)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addAspect(TCAspects.METALLUM, 4)
            .addAspect(TCAspects.TENEBRAE, 2)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadSiliconTetrachloride() {
        return new MaterialBuilder().setName("SiliconTetrachloride")
            .setDefaultLocalName("Silicon Tetrachloride")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00dcdcdc)
            .addCell()
            .addFluid()
            .setMeltingPoint(204)
            // SICL4
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Chlorine, 4)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .constructMaterial();
    }

    private static Materials loadSiliconTetrafluoride() {
        return new MaterialBuilder().setName("SiliconTetrafluoride")
            .setDefaultLocalName("Silicon Tetrafluoride")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00c8c8c8)
            .addCell()
            .addGas()
            .setMeltingPoint(178)
            // SIF4
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Fluorine, 4)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadTrichlorosilane() {
        return new MaterialBuilder().setName("Trichlorosilane")
            .setDefaultLocalName("Trichlorosilane")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .setMeltingPoint(139)
            // HSICL3
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Chlorine, 3)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .constructMaterial();
    }

    private static void loadUnclassified06() {
        Materials.GalliumArsenide = loadGalliumArsenide();
        Materials.IndiumGalliumPhosphide = loadIndiumGalliumPhosphide();
        Materials.SolderingAlloy = loadSolderingAlloy();
        Materials.Spessartine = loadSpessartine();
        Materials.Sphalerite = loadSphalerite();
        Materials.StainlessSteel = loadStainlessSteel();
        Materials.Steel = loadSteel();
        Materials.Stibnite = loadStibnite();
        Materials.SulfuricAcid = loadSulfuricAcid();
        Materials.Tanzanite = loadTanzanite();
        Materials.Tetrahedrite = loadTetrahedrite();
        Materials.TinAlloy = loadTinAlloy();
        Materials.Topaz = loadTopaz();
        Materials.Tungstate = loadTungstate();
        Materials.Ultimet = loadUltimet();
        Materials.Uraninite = loadUraninite();
        Materials.Uvarovite = loadUvarovite();
        Materials.VanadiumGallium = loadVanadiumGallium();
        Materials.Wood = loadWood();
        Materials.WroughtIron = loadWroughtIron();
        Materials.Wulfenite = loadWulfenite();
        Materials.YellowLimonite = loadYellowLimonite();
        Materials.YttriumBariumCuprate = loadYttriumBariumCuprate();
    }

    private static Materials loadGalliumArsenide() {
        return new MaterialBuilder().setName("GalliumArsenide")
            .setDefaultLocalName("Gallium Arsenide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00a0a0a0)
            .addDustItems()
            .addMetalItems()
            .setBlastFurnaceTemp(1_200)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Arsenic, 1)
            .addMaterial(Materials.Gallium, 1)
            .constructMaterial();
    }

    private static Materials loadIndiumGalliumPhosphide() {
        return new MaterialBuilder().setName("IndiumGalliumPhosphide")
            .setDefaultLocalName("Indium Gallium Phosphide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00a08cbe)
            .addDustItems()
            .addMetalItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Indium, 1)
            .addMaterial(Materials.Gallium, 1)
            .addMaterial(Materials.Phosphorus, 1)
            .constructMaterial();
    }

    private static Materials loadSolderingAlloy() {
        return new MaterialBuilder().setName("SolderingAlloy")
            .setDefaultLocalName("Soldering Alloy")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00dcdce6)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(400)
            .setBlastFurnaceTemp(400)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Tin, 9)
            .addMaterial(Materials.Antimony, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.SOLDERING_MATERIAL)
            .addSubTag(SubTag.SOLDERING_MATERIAL_GOOD)
            .constructMaterial();
    }

    private static Materials loadSpessartine() {
        return new MaterialBuilder().setName("Spessartine")
            .setDefaultLocalName("Spessartine")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00ff6464)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Manganese, 3)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 12)
            .addOreByproduct(() -> Materials.GarnetRed)
            .addOreByproduct(() -> Materials.Manganese)
            .constructMaterial();
    }

    private static Materials loadSphalerite() {
        return new MaterialBuilder().setName("Sphalerite")
            .setDefaultLocalName("Sphalerite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.Zinc, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addOreByproduct(() -> Materials.GarnetYellow)
            .addOreByproduct(() -> Materials.Cadmium)
            .addOreByproduct(() -> Materials.Gallium)
            .addOreByproduct(() -> Materials.Zinc)
            .setDirectSmelting(() -> Materials.Zinc)
            .addSubTag(SubTag.INDUCTIONSMELTING_LOW_OUTPUT)
            .constructMaterial();
    }

    private static Materials loadStainlessSteel() {
        return new MaterialBuilder().setName("StainlessSteel")
            .setDefaultLocalName("Stainless Steel")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00c8c8dc)
            .setTool(480, 4, 7.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1_700)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 6)
            .addMaterial(Materials.Chrome, 1)
            .addMaterial(Materials.Manganese, 1)
            .addMaterial(Materials.Nickel, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadSteel() {
        return new MaterialBuilder().setName("Steel")
            .setDefaultLocalName("Steel")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00808080)
            .setTool(512, 3, 6.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_811)
            .setBlastFurnaceTemp(1_000)
            .setBlastFurnaceRequired(true)
            .setDensity(51, 50)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 50)
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ORDO, 1)
            .addOreByproduct(() -> Materials.Iron)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadStibnite() {
        return new MaterialBuilder().setName("Stibnite")
            .setDefaultLocalName("Stibnite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00464646)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Antimony, 2)
            .addMaterial(Materials.Sulfur, 3)
            .addOreByproduct(() -> Materials.Antimony)
            .setDirectSmelting(() -> Materials.Antimony)
            .constructMaterial();
    }

    private static Materials loadSulfuricAcid() {
        return new MaterialBuilder().setName("SulfuricAcid")
            .setDefaultLocalName("Sulfuric Acid")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff8000)
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadTanzanite() {
        return new MaterialBuilder().setName("Tanzanite")
            .setDefaultLocalName("Tanzanite")
            .setIconSet(TextureSet.SET_GEM_VERTICAL)
            .setColor(Dyes.dyePurple)
            .setARGB(0x7f4000c8)
            .setTool(256, 2, 7.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addMaterial(Materials.Calcium, 2)
            .addMaterial(Materials.Aluminium, 3)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Oxygen, 13)
            .addAspect(TCAspects.LUCRUM, 5)
            .addAspect(TCAspects.VITREUS, 3)
            .addOreByproduct(() -> Materials.Opal)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadTetrahedrite() {
        return new MaterialBuilder().setName("Tetrahedrite")
            .setDefaultLocalName("Tetrahedrite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00c82000)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            // Cu3SbS3 + x(Fe,Zn)6Sb2S9
            .addMaterial(Materials.Copper, 3)
            .addMaterial(Materials.Antimony, 1)
            .addMaterial(Materials.Sulfur, 3)
            .addMaterial(Materials.Iron, 1)
            .addOreByproduct(() -> Materials.Antimony)
            .addOreByproduct(() -> Materials.Zinc)
            .setDirectSmelting(() -> Materials.Copper)
            .addSubTag(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE)
            .addSubTag(SubTag.INDUCTIONSMELTING_LOW_OUTPUT)
            .addSubTag(SubTag.WASHING_SODIUMPERSULFATE)
            .constructMaterial();
    }

    private static Materials loadTinAlloy() {
        return new MaterialBuilder().setName("TinAlloy")
            .setDefaultLocalName("Tin Alloy")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00c8c8c8)
            .setTool(96, 2, 6.5f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Tin, 1)
            .addMaterial(Materials.Iron, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadTopaz() {
        return new MaterialBuilder().setName("Topaz")
            .setDefaultLocalName("Topaz")
            .setIconSet(TextureSet.SET_GEM_HORIZONTAL)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x7fff8000)
            .setTool(256, 3, 7.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Fluorine, 2)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 6)
            .addAspect(TCAspects.LUCRUM, 6)
            .addAspect(TCAspects.VITREUS, 4)
            .addOreByproduct(() -> Materials.BlueTopaz)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadTungstate() {
        return new MaterialBuilder().setName("Tungstate")
            .setDefaultLocalName("Tungstate")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00373223)
            .addDustItems()
            .addOreItems()
            .setOreMultiplier(2)
            .setMeltingPoint(2_500)
            .setBlastFurnaceTemp(2_500)
            .setBlastFurnaceRequired(true)
            .addMaterial(Materials.Tungsten, 1)
            .addMaterial(Materials.Lithium, 2)
            .addMaterial(Materials.Oxygen, 4)
            .addOreByproduct(() -> Materials.Manganese)
            .addOreByproduct(() -> Materials.Silver)
            .addOreByproduct(() -> Materials.Lithium)
            .constructMaterial();
    }

    private static Materials loadUltimet() {
        return new MaterialBuilder().setName("Ultimet")
            .setDefaultLocalName("Ultimet")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x00b4b4e6)
            .setTool(2_048, 4, 9.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2_700)
            .setBlastFurnaceTemp(2_700)
            .setBlastFurnaceRequired(true)
            .addElectrolyzerRecipe()
            // 54% Cobalt, 26% Chromium, 9% Nickel, 5% Molybdenum, 3% Iron, 2% Tungsten, 0.8% Manganese, 0.3% Silicon,
            // 0.08% Nitrogen and 0.06% Carbon
            .addMaterial(Materials.Cobalt, 5)
            .addMaterial(Materials.Chrome, 2)
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.Molybdenum, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadUraninite() {
        return new MaterialBuilder().setName("Uraninite")
            .setDefaultLocalName("Uraninite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00232323)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Uranium, 1)
            .addMaterial(Materials.Oxygen, 2)
            .addOreByproduct(() -> Materials.Uranium)
            .addOreByproduct(() -> Materials.Thorium)
            .addOreByproduct(() -> Materials.Uranium235)
            .constructMaterial();
    }

    private static Materials loadUvarovite() {
        return new MaterialBuilder().setName("Uvarovite")
            .setDefaultLocalName("Uvarovite")
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x00b4ffb4)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Calcium, 3)
            .addMaterial(Materials.Chrome, 2)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 12)
            .addOreByproduct(() -> Materials.GarnetYellow)
            .addOreByproduct(() -> Materials.Chrome)
            .constructMaterial();
    }

    private static Materials loadVanadiumGallium() {
        return new MaterialBuilder().setName("VanadiumGallium")
            .setDefaultLocalName("Vanadium-Gallium")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGray)
            .setARGB(0x0080808c)
            .addDustItems()
            .addMetalItems()
            .addGearItems()
            .setMeltingPoint(4_500)
            .setBlastFurnaceTemp(4_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Vanadium, 3)
            .addMaterial(Materials.Gallium, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadWood() {
        return new MaterialBuilder().setName("Wood")
            .setDefaultLocalName("Wood")
            .setChemicalFormula("")
            .setIconSet(TextureSet.SET_WOOD)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00643200)
            .setTool(16, 0, 2.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .addAspect(TCAspects.ARBOR, 2)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.WOOD)
            .removeOrePrefix(OrePrefixes.stick) // minecraft:stick
            .removeOrePrefix(OrePrefixes.ingot)
            .constructMaterial();
    }

    private static Materials loadWroughtIron() {
        return new MaterialBuilder().setName("WroughtIron")
            .setDefaultLocalName("Wrought Iron")
            .setChemicalFormula("Fe*")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00c8b4b4)
            .setTool(384, 2, 6.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_811)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Iron, 1)
            .addSubTag(SubTag.BLASTFURNACE_CALCITE_TRIPLE)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .constructMaterial();
    }

    private static Materials loadWulfenite() {
        return new MaterialBuilder().setName("Wulfenite")
            .setDefaultLocalName("Wulfenite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff8000)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Lead, 1)
            .addMaterial(Materials.Molybdenum, 1)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadYellowLimonite() {
        return new MaterialBuilder().setName("YellowLimonite")
            .setDefaultLocalName("Yellow Limonite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00c8c800)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            // FeO(OH) + a bit of Ni and Co
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Oxygen, 2)
            .addOreByproduct(() -> Materials.Nickel)
            .addOreByproduct(() -> Materials.BrownLimonite)
            .addOreByproduct(() -> Materials.Cobalt)
            .setDirectSmelting(() -> Materials.Iron)
            .addSubTag(SubTag.BLASTFURNACE_CALCITE_DOUBLE)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .addSubTag(SubTag.INDUCTIONSMELTING_LOW_OUTPUT)
            .constructMaterial();
    }

    private static Materials loadYttriumBariumCuprate() {
        return new MaterialBuilder().setName("YttriumBariumCuprate")
            .setDefaultLocalName("Yttrium Barium Cuprate")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00504046)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(4_500)
            .setBlastFurnaceTemp(4_500)
            .setBlastFurnaceRequired(true)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addMaterial(Materials.Yttrium, 1)
            .addMaterial(Materials.Barium, 2)
            .addMaterial(Materials.Copper, 3)
            .addMaterial(Materials.Oxygen, 7)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static void loadDegree2Compounds() {
        Materials.Aluminiumhydroxide = loadAluminiumHydroxide();
        Materials.Aluminiumoxide = loadAluminiumoxide();
        Materials.Alumite = loadAlumite();
        Materials.Alunite = loadAlunite();
        Materials.Amethyst = loadAmethyst();
        Materials.Apatite = loadApatite();
        Materials.Barite = loadBarite();
        Materials.Bastnasite = loadBastnasite();
        Materials.Bauxite = loadBauxite();
        Materials.Bentonite = loadBentonite();
        Materials.Biotite = loadBiotite();
        Materials.BismuthBronze = loadBismuthBronze();
        Materials.BlackBronze = loadBlackBronze();
        Materials.BlackSteel = loadBlackSteel();
        Materials.Blaze = loadBlaze();
        Materials.Borax = loadBorax();
        Materials.Chrysotile = loadChrysotile();
        Materials.Clay = loadClay();
        Materials.CobaltBrass = loadCobaltBrass();
        Materials.Concrete = loadConcrete();
        Materials.Cryolite = loadCryolite();
        Materials.DamascusSteel = loadDamascusSteel();
        Materials.DilutedSulfuricAcid = loadDilutedSulfuricAcid();
        Materials.Dolomite = loadDolomite();
        Materials.ElectricalSteel = loadElectricalSteel();
        Materials.EnderPearl = loadEnderPearl();
        Materials.EpoxidFiberReinforced = loadEpoxidFiberReinforced();
        Materials.Flint = loadFlint();
        Materials.FullersEarth = loadFullersEarth();
        Materials.GarnetRed = loadGarnetRed();
        Materials.GarnetYellow = loadGarnetYellow();
        Materials.Glass = loadGlass();
        Materials.Glauconite = loadGlauconite();
        Materials.GlauconiteSand = loadGlauconiteSand();
        Materials.GraniteBlack = loadGraniteBlack();
        Materials.GraniticMineralSand = loadGraniticMineralSand();
        Materials.Gypsum = loadGypsum();
        Materials.HydratedCoal = loadHydratedCoal();
        Materials.IronMagnetic = loadIronMagnetic();
        Materials.Jasper = loadJasper();
        Materials.Kaolinite = loadKaolinite();
        Materials.Knightmetal = loadKnightmetal();
        Materials.Kyanite = loadKyanite();
        Materials.Lapis = loadLapis();
        Materials.Lepidolite = loadLepidolite();
        Materials.Lignite = loadLignite();
        Materials.LiquidAir = loadLiquidAir();
        Materials.LiveRoot = loadLiveRoot();
        Materials.Malachite = loadMalachite();
        Materials.Manyullyn = loadManyullyn();
        Materials.Marble = loadMarble();
        Materials.Mica = loadMica();
        Materials.Mirabilite = loadMirabilite();
        Materials.Monazite = loadMonazite();
        Materials.NeodymiumMagnetic = loadNeodymiumMagnetic();
        Materials.Niter = loadNiter();
        Materials.NitroCoalFuel = loadNitroCoalFuel();
        Materials.NitroFuel = loadNitroFuel();
        Materials.Olivine = loadOlivine();
        Materials.Opal = loadOpal();
        Materials.Pentlandite = loadPentlandite();
        Materials.Perlite = loadPerlite();
        Materials.Pitchblende = loadPitchblende();
        Materials.Pollucite = loadPollucite();
        Materials.PotassiumFeldspar = loadPotassiumFeldspar();
        Materials.QuartzSand = loadQuartzSand();
        Materials.RawStyreneButadieneRubber = loadRawStyreneButadieneRubber();
        Materials.Realgar = loadRealgar();
        Materials.RedMud = loadRedMud();
        Materials.Redrock = loadRedrock();
        Materials.Redstone = loadRedstone();
        Materials.RoseGold = loadRoseGold();
        Materials.SamariumMagnetic = loadSamariumMagnetic();
        Materials.Soapstone = loadSoapstone();
        Materials.SodiumAluminate = loadSodiumAluminate();
        Materials.SodiumCarbonate = loadSodiumCarbonate();
        Materials.Spodumene = loadSpodumene();
        Materials.SteelMagnetic = loadSteelMagnetic();
        Materials.Steeleaf = loadSteeleaf();
        Materials.SterlingSilver = loadSterlingSilver();
        Materials.Sugar = loadSugar();
        Materials.Talc = loadTalc();
        Materials.Tantalite = loadTantalite();
        Materials.Thaumium = loadThaumium();
        Materials.TPV = loadTPV();
        Materials.TricalciumPhosphate = loadTricalciumPhosphate();
        Materials.Trona = loadTrona();
        Materials.TungstenCarbide = loadTungstenCarbide();
        Materials.TungstenSteel = loadTungstenSteel();
        Materials.VanadiumMagnetite = loadVanadiumMagnetite();
        Materials.VanadiumSteel = loadVanadiumSteel();
        Materials.Vermiculite = loadVermiculite();
        Materials.Vinteum = loadVinteum();
        Materials.Vis = loadVis();
        Materials.VolcanicAsh = loadVolcanicAsh();
        Materials.Wollastonite = loadWollastonite();
        Materials.WoodSealed = loadWoodSealed();
        Materials.Zeolite = loadZeolite();
    }

    private static Materials loadAluminiumHydroxide() {
        return new MaterialBuilder().setName("AluminiumHydroxide")
            .setDefaultLocalName("Aluminium Hydroxide")
            .setChemicalFormula("Al(OH)₃")
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00ebebff)
            .setTool(64, 1, 1.0f)
            .addDustItems()
            .setBlastFurnaceTemp(1_200)
            .setMeltingPoint(1_200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addMaterial(Materials.Hydrogen, 3)
            .addAspect(TCAspects.GELUM, 2)
            .constructMaterial();
    }

    private static Materials loadAluminiumoxide() {
        return new MaterialBuilder().setName("Alumina")
            .setDefaultLocalName("Alumina")
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00ebffff)
            .setTool(64, 1, 1.0f)
            .addDustItems()
            .setBlastFurnaceTemp(2_054)
            .setMeltingPoint(2_054)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Oxygen, 3)
            .addAspect(TCAspects.GELUM, 2)
            .constructMaterial();
    }

    private static Materials loadAlumite() {
        return new MaterialBuilder().setName("Alumite")
            .setDefaultLocalName("Obzinite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setARGB(0x00ff69b4)
            .setTool(768, 2, 5.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Zinc, 5)
            .addMaterial(Materials.Steel, 2)
            .addMaterial(Materials.Obsidian, 2)
            .addAspect(TCAspects.STRONTIO, 2)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadAlunite() {
        return new MaterialBuilder().setName("Alunite")
            .setDefaultLocalName("Alunite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00e1b441)
            .addDustItems()
            .addOreItems()
            // KAl3(SO4)2(OH)6
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Aluminium, 3)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 14)
            .constructMaterial();
    }

    private static Materials loadAmethyst() {
        return new MaterialBuilder().setName("Amethyst")
            .setDefaultLocalName("Amethyst")
            .setIconSet(TextureSet.SET_FLINT)
            .setColor(Dyes.dyePink)
            .setARGB(0x7fd232d2)
            .setTool(256, 3, 7.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.SiliconDioxide, 4)
            .addMaterial(Materials.Iron, 1)
            .addAspect(TCAspects.LUCRUM, 6)
            .addAspect(TCAspects.VITREUS, 4)
            .addOreByproduct(() -> Materials.Amethyst)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadApatite() {
        return new MaterialBuilder().setName("Apatite")
            .setDefaultLocalName("Apatite")
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x00c8c8ff)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(4)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Calcium, 5)
            .addMaterial(Materials.Phosphate, 3)
            .addMaterial(Materials.Chlorine, 1)
            .addAspect(TCAspects.MESSIS, 2)
            .addOreByproduct(() -> Materials.TricalciumPhosphate)
            .addOreByproduct(() -> Materials.Phosphate)
            .addOreByproduct(() -> Materials.Pyrochlore)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadBarite() {
        return new MaterialBuilder().setName("Barite")
            .setDefaultLocalName("Barite")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00e6ebff)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Barium, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadBastnasite() {
        return new MaterialBuilder().setName("Bastnasite")
            .setDefaultLocalName("Bastnasite")
            .setIconSet(TextureSet.SET_FINE)
            .setARGB(0x00c86e2d)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // (Ce, La, Y)CO3F
            .addMaterial(Materials.Cerium, 1)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Fluorine, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addOreByproduct(() -> Materials.Neodymium)
            .addOreByproduct(() -> Materials.RareEarth)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM)
            .constructMaterial();
    }

    private static Materials loadBauxite() {
        return new MaterialBuilder().setName("Bauxite")
            .setDefaultLocalName("Bauxite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00c86400)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Rutile, 2)
            .addMaterial(Materials.Aluminium, 16)
            .addMaterial(Materials.Hydrogen, 10)
            .addMaterial(Materials.Oxygen, 11)
            .addOreByproduct(() -> Materials.Grossular)
            .addOreByproduct(() -> Materials.Rutile)
            .addOreByproduct(() -> Materials.Gallium)
            .constructMaterial();
    }

    private static Materials loadBentonite() {
        return new MaterialBuilder().setName("Bentonite")
            .setDefaultLocalName("Bentonite")
            .setIconSet(TextureSet.SET_ROUGH)
            .setARGB(0x00f5d7d2)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // (Na,Ca)0.33(Al,Mg)2(Si4O10)(OH)2 nH2O
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Magnesium, 6)
            .addMaterial(Materials.Silicon, 12)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Water, 5)
            .addMaterial(Materials.Oxygen, 36)
            .addOreByproduct(() -> Materials.Aluminiumoxide)
            .addOreByproduct(() -> Materials.Calcium)
            .addOreByproduct(() -> Materials.Magnesium)
            .constructMaterial();
    }

    private static Materials loadBiotite() {
        return new MaterialBuilder().setName("Biotite")
            .setDefaultLocalName("Biotite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00141e14)
            .addDustItems()
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Magnesium, 3)
            .addMaterial(Materials.Aluminium, 3)
            .addMaterial(Materials.Fluorine, 2)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 10)
            .constructMaterial();
    }

    private static Materials loadBismuthBronze() {
        return new MaterialBuilder().setName("BismuthBronze")
            .setDefaultLocalName("Bismuth Bronze")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x00647d7d)
            .setTool(256, 2, 8.0f)
            .setToolEnchantment(() -> Enchantment.baneOfArthropods, 5)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1_100)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Bismuth, 1)
            .addMaterial(Materials.Zinc, 1)
            .addMaterial(Materials.Copper, 3)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadBlackBronze() {
        return new MaterialBuilder().setName("BlackBronze")
            .setDefaultLocalName("Black Bronze")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setARGB(0x0064327d)
            .setTool(256, 2, 12.0f)
            .setToolEnchantment(() -> Enchantment.smite, 2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(2_000)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Gold, 1)
            .addMaterial(Materials.Silver, 1)
            .addMaterial(Materials.Copper, 3)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadBlackSteel() {
        return new MaterialBuilder().setName("BlackSteel")
            .setDefaultLocalName("Black Steel")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00646464)
            .setTool(768, 3, 6.5f)
            .setToolEnchantment(() -> Enchantment.sharpness, 4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1_200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.BlackBronze, 1)
            .addMaterial(Materials.Steel, 3)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadBlaze() {
        return new MaterialBuilder().setName("Blaze")
            .setDefaultLocalName("Blaze")
            .setIconSet(TextureSet.SET_BLAZE)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffc800)
            .addDustItems()
            .setMeltingPoint(6_400)
            .setDensity(3, 2)
            .addCentrifugeRecipe()
            .addMaterial(Materials.AshDark, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Magic, 1)
            .addAspect(TCAspects.PRAECANTATIO, 2)
            .addAspect(TCAspects.IGNIS, 4)
            .addSubTag(SubTag.BURNING)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .addSubTag(SubTag.UNBURNABLE)
            .addOrePrefix(OrePrefixes.stickLong)
            .removeOrePrefix(OrePrefixes.dust) // minecraft:blaze_powder
            .removeOrePrefix(OrePrefixes.stick) // minecraft:blaze_rod
            .constructMaterial();
    }

    private static Materials loadBorax() {
        return new MaterialBuilder().setName("Borax")
            .setDefaultLocalName("Borax")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fafafa)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Sodium, 2)
            .addMaterial(Materials.Boron, 4)
            .addMaterial(Materials.Oxygen, 7)
            .addMaterial(Materials.Water, 10)
            .constructMaterial();
    }

    private static Materials loadChrysotile() {
        return new MaterialBuilder().setName("Chrysotile")
            .setDefaultLocalName("Chrysotile")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x006e8c6e)
            .setTool(10_240, 3, 32.0f)
            .setTurbine(280.0f, 280.0f, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(9_400)
            .setBlastFurnaceTemp(9_400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Asbestos, 1)
            .addOreByproduct(() -> Materials.Asbestos)
            .addOreByproduct(() -> Materials.SiliconDioxide)
            .addOreByproduct(() -> Materials.Magnesium)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadClay() {
        return new MaterialBuilder().setName("Clay")
            .setDefaultLocalName("Clay")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x00c8c8dc)
            .addDustItems()
            .addMaterial(Materials.Sodium, 2)
            .addMaterial(Materials.Lithium, 1)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Oxygen, 7)
            .addMaterial(Materials.Water, 2)
            .addOreByproduct(() -> Materials.Clay)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .constructMaterial();
    }

    private static Materials loadCobaltBrass() {
        return new MaterialBuilder().setName("CobaltBrass")
            .setDefaultLocalName("Cobalt Brass")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00b4b4a0)
            .setTool(256, 2, 8.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Brass, 7)
            .addMaterial(Materials.Tin, 1)
            .addMaterial(Materials.Cobalt, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadConcrete() {
        return new MaterialBuilder().setName("Concrete")
            .setDefaultLocalName("Concrete")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00646464)
            .addDustItems()
            .setMeltingPoint(300)
            .addMaterial(Materials.Stone, 1)
            .addAspect(TCAspects.TERRA, 1)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .addSubTag(SubTag.STONE)
            .addOrePrefix(OrePrefixes.plate)
            .constructMaterial();
    }

    private static Materials loadCryolite() {
        return new MaterialBuilder().setName("Cryolite")
            .setDefaultLocalName("Cryolite")
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x00bfefff)
            .setTool(64, 1, 1.0f)
            .addOreItems()
            .setOreMultiplier(4)
            .setMeltingPoint(1_012)
            .setBlastFurnaceTemp(1_012)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addMaterial(Materials.Sodium, 3)
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Fluorine, 6)
            .addOreByproduct(() -> Materials.Aluminiumoxide)
            .addOreByproduct(() -> Materials.Sodium)
            .constructMaterial();
    }

    private static Materials loadDamascusSteel() {
        return new MaterialBuilder().setName("DamascusSteel")
            .setDefaultLocalName("Damascus Steel")
            .setChemicalFormula("(Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C)₉Mn₄Cr₄CSiV")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x006e6e6e)
            .setTool(1_280, 3, 8.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 5)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2_000)
            .setBlastFurnaceTemp(1_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Steel, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadDilutedSulfuricAcid() {
        return new MaterialBuilder().setName("DilutedSulfuricAcid")
            .setDefaultLocalName("Diluted Sulfuric Acid")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00c07820)
            .addCell()
            .addFluid()
            .addMaterial(Materials.SulfuricAcid, 1)
            .constructMaterial();
    }

    private static Materials loadDolomite() {
        return new MaterialBuilder().setName("Dolomite")
            .setDefaultLocalName("Dolomite")
            .setIconSet(TextureSet.SET_FLINT)
            .setARGB(0x00e1cdcd)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // CaMg(CO3)2
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Oxygen, 6)
            .constructMaterial();
    }

    private static Materials loadElectricalSteel() {
        return new MaterialBuilder().setName("ElectricalSteel")
            .setDefaultLocalName("Electrical Steel")
            .setIconSet(TextureSet.SET_DARKSTEEL)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00d8d8d8)
            .setTool(512, 2, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_811)
            .setBlastFurnaceTemp(1_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Steel, 1)
            .addMaterial(Materials.Coal, 1)
            .addMaterial(Materials.Silicon, 1)
            .addSubTag(SubTag.METAL)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial();
    }

    private static Materials loadEnderPearl() {
        return new MaterialBuilder().setName("EnderPearl")
            .setDefaultLocalName("Enderpearl")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x006cdcc8)
            .setTool(16, 1, 1.0f)
            .setToolEnchantment(() -> Enchantment.silkTouch, 1)
            .addDustItems()
            .addGemItems()
            .setDensity(16, 10)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Beryllium, 1)
            .addMaterial(Materials.Potassium, 4)
            .addMaterial(Materials.Nitrogen, 5)
            .addMaterial(Materials.Magic, 6)
            .addAspect(TCAspects.ALIENIS, 4)
            .addAspect(TCAspects.ITER, 4)
            .addAspect(TCAspects.PRAECANTATIO, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.PEARL)
            .removeOrePrefix(OrePrefixes.gem) // minecraft:ender_pearl
            .constructMaterial();
    }

    private static Materials loadEpoxidFiberReinforced() {
        return new MaterialBuilder().setName("EpoxidFiberReinforced")
            .setDefaultLocalName("Fiber-Reinforced Epoxy Resin")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00a07010)
            .setTool(64, 1, 3.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .removeOrePrefix(OrePrefixes.sheetmetal)
            .setMeltingPoint(400)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Epoxid, 1)
            .addAspect(TCAspects.MOTUS, 2)
            .constructMaterial();
    }

    private static Materials loadFlint() {
        return new MaterialBuilder().setName("Flint")
            .setDefaultLocalName("Flint")
            .setIconSet(TextureSet.SET_FLINT)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00002040)
            .setTool(128, 1, 2.5f)
            .setToolEnchantment(() -> Enchantment.fireAspect, 1)
            .addDustItems()
            .addToolHeadItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.SiliconDioxide, 1)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .addOreByproduct(() -> Materials.Obsidian)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STONE)
            .addOrePrefix(OrePrefixes.dustImpure)
            .removeOrePrefix(OrePrefixes.gem) // minecraft:flint
            .constructMaterial();
    }

    private static Materials loadFullersEarth() {
        return new MaterialBuilder().setName("FullersEarth")
            .setDefaultLocalName("Fullers Earth")
            .setIconSet(TextureSet.SET_FINE)
            .setARGB(0x00a0a078)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // (Mg,Al)2Si4O10(OH) 4(H2O)
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Silicon, 4)
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Water, 4)
            .addMaterial(Materials.Oxygen, 11)
            .addOreByproduct(() -> Materials.Aluminiumoxide)
            .addOreByproduct(() -> Materials.SiliconDioxide)
            .addOreByproduct(() -> Materials.Magnesium)
            .constructMaterial();
    }

    private static Materials loadGarnetRed() {
        return new MaterialBuilder().setName("GarnetRed")
            .setDefaultLocalName("Red Garnet")
            .setIconSet(TextureSet.SET_RUBY)
            .setColor(Dyes.dyeRed)
            .setARGB(0x7fc85050)
            .setTool(128, 2, 7.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Pyrope, 3)
            .addMaterial(Materials.Almandine, 5)
            .addMaterial(Materials.Spessartine, 8)
            .addAspect(TCAspects.VITREUS, 3)
            .addOreByproduct(() -> Materials.Spessartine)
            .addOreByproduct(() -> Materials.Pyrope)
            .addOreByproduct(() -> Materials.Almandine)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadGarnetYellow() {
        return new MaterialBuilder().setName("GarnetYellow")
            .setDefaultLocalName("Yellow Garnet")
            .setIconSet(TextureSet.SET_RUBY)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x7fc8c850)
            .setTool(128, 2, 7.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Andradite, 5)
            .addMaterial(Materials.Grossular, 8)
            .addMaterial(Materials.Uvarovite, 3)
            .addAspect(TCAspects.VITREUS, 3)
            .addOreByproduct(() -> Materials.Andradite)
            .addOreByproduct(() -> Materials.Grossular)
            .addOreByproduct(() -> Materials.Uvarovite)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadGlass() {
        return new MaterialBuilder().setName("Glass")
            .setDefaultLocalName("Glass")
            .setIconSet(TextureSet.SET_GLASS)
            .setColor(Dyes.dyeWhite)
            .setARGB(0xdcfafafa)
            .setTool(4, 0, 1.0f)
            .addDustItems()
            .addGemItems()
            .setMeltingPoint(1_500)
            .addCentrifugeRecipe()
            .addMaterial(Materials.SiliconDioxide, 1)
            .addAspect(TCAspects.VITREUS, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_RECYCLING)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadGlauconite() {
        return new MaterialBuilder().setName("Glauconite")
            .setDefaultLocalName("Glauconite")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x0082b43c)
            .addDustItems()
            .addOreItems()
            // (K,Na)(Fe3+,Al,Mg)2(Si,Al)4O10(OH)2
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Magnesium, 2)
            .addMaterial(Materials.Aluminium, 4)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 12)
            .addOreByproduct(() -> Materials.Sodium)
            .addOreByproduct(() -> Materials.Aluminiumoxide)
            .addOreByproduct(() -> Materials.Iron)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .constructMaterial();
    }

    private static Materials loadGlauconiteSand() {
        return new MaterialBuilder().setName("GlauconiteSand")
            .setDefaultLocalName("Glauconite Sand")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x0082b43c)
            .addDustItems()
            .addOreItems()
            // (K,Na)(Fe3+,Al,Mg)2(Si,Al)4O10(OH)2
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Magnesium, 2)
            .addMaterial(Materials.Aluminium, 4)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 12)
            .addOreByproduct(() -> Materials.Sodium)
            .addOreByproduct(() -> Materials.Aluminiumoxide)
            .addOreByproduct(() -> Materials.Iron)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .constructMaterial();
    }

    private static Materials loadGraniteBlack() {
        return new MaterialBuilder().setName("GraniteBlack")
            .setDefaultLocalName("Black Granite")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000a0a0a)
            .setTool(64, 3, 4.0f)
            .addDustItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.SiliconDioxide, 4)
            .addMaterial(Materials.Biotite, 1)
            .addAspect(TCAspects.TUTAMEN, 1)
            .addOreByproduct(() -> Materials.Biotite)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STONE)
            .addOrePrefix(OrePrefixes.dustImpure)
            .addOrePrefix(OrePrefixes.plate)
            .constructMaterial();
    }

    private static Materials loadGraniticMineralSand() {
        return new MaterialBuilder().setName("GraniticMineralSand")
            .setDefaultLocalName("Granitic Mineral Sand")
            .setIconSet(TextureSet.SET_SAND)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00283c3c)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Magnetite, 1)
            .addMaterial(Materials.GraniteBlack, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .addOreByproduct(() -> Materials.GraniteBlack)
            .addOreByproduct(() -> Materials.Magnetite)
            .setDirectSmelting(() -> Materials.Iron)
            .addSubTag(SubTag.BLASTFURNACE_CALCITE_DOUBLE)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD)
            .addSubTag(SubTag.INDUCTIONSMELTING_LOW_OUTPUT)
            .constructMaterial();
    }

    private static Materials loadGypsum() {
        return new MaterialBuilder().setName("Gypsum")
            .setDefaultLocalName("Gypsum")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00e6e6fa)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // CaSO4 2H2O
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Oxygen, 4)
            .addMaterial(Materials.Water, 2)
            .constructMaterial();
    }

    private static Materials loadHydratedCoal() {
        return new MaterialBuilder().setName("HydratedCoal")
            .setDefaultLocalName("Hydrated Coal")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00464664)
            .addDustItems()
            .setDensity(9, 8)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Coal, 8)
            .addMaterial(Materials.Water, 1)
            .constructMaterial();
    }

    private static Materials loadIronMagnetic() {
        return new MaterialBuilder().setName("IronMagnetic")
            .setDefaultLocalName("Magnetic Iron")
            .setChemicalFormula("Fe" + CustomGlyphs.MAGNET)
            .setIconSet(TextureSet.SET_MAGNETIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00c8c8c8)
            .setTool(256, 2, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 1)
            .setSmeltingInto(() -> Materials.Iron)
            .setMaceratingInto(() -> Materials.Iron)
            .setArcSmeltingInto(() -> Materials.WroughtIron)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .constructMaterial();
    }

    private static Materials loadJasper() {
        return new MaterialBuilder().setName("Jasper")
            .setDefaultLocalName("Jasper")
            .setIconSet(TextureSet.SET_EMERALD)
            .setColor(Dyes.dyeRed)
            .setARGB(0x64c85050)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.SiliconDioxide, 1)
            .addAspect(TCAspects.LUCRUM, 4)
            .addAspect(TCAspects.VITREUS, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadKaolinite() {
        return new MaterialBuilder().setName("Kaolinite")
            .setDefaultLocalName("Kaolinite")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00f5ebeb)
            .addDustItems()
            .addOreItems()
            // Al2Si2O5(OH)4
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Hydrogen, 4)
            .addMaterial(Materials.Oxygen, 9)
            .constructMaterial();
    }

    private static Materials loadKnightmetal() {
        return new MaterialBuilder().setName("Knightmetal")
            .setDefaultLocalName("Knightmetal")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00d2f0c8)
            .setTool(1_024, 3, 8.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 3)
            .setArmorEnchantment(() -> Enchantment.protection, 1)
            .setFuel(MaterialBuilder.FuelType.Magic, 24)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Steel, 2)
            .addMaterial(Materials.Magic, 1)
            .addAspect(TCAspects.LUCRUM, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadKyanite() {
        return new MaterialBuilder().setName("Kyanite")
            .setDefaultLocalName("Kyanite")
            .setIconSet(TextureSet.SET_FLINT)
            .setARGB(0x006e6efa)
            .addDustItems()
            .addOreItems()
            // Al2SiO5
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Oxygen, 5)
            .constructMaterial();
    }

    private static Materials loadLapis() {
        return new MaterialBuilder().setName("Lapis")
            .setDefaultLocalName("Lapis")
            .setIconSet(TextureSet.SET_LAPIS)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x004646dc)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(6)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Lazurite, 12)
            .addMaterial(Materials.Sodalite, 2)
            .addMaterial(Materials.Pyrite, 1)
            .addMaterial(Materials.Calcite, 1)
            .addAspect(TCAspects.SENSUS, 1)
            .addOreByproduct(() -> Materials.Lazurite)
            .addOreByproduct(() -> Materials.Sodalite)
            .addOreByproduct(() -> Materials.Pyrite)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .removeOrePrefix(OrePrefixes.block) // minecraft:lapis_block
            .removeOrePrefix(OrePrefixes.gem) // minecraft:dye:4
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadLepidolite() {
        return new MaterialBuilder().setName("Lepidolite")
            .setDefaultLocalName("Lepidolite")
            .setIconSet(TextureSet.SET_FINE)
            .setARGB(0x00f0328c)
            .addDustItems()
            .addOreItems()
            // K(Li,Al,Rb)3(Al,Si)4O10(F,OH)2
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Lithium, 3)
            .addMaterial(Materials.Aluminium, 4)
            .addMaterial(Materials.Fluorine, 2)
            .addMaterial(Materials.Oxygen, 10)
            .addOreByproduct(() -> Materials.Lithium)
            .addOreByproduct(() -> Materials.Caesium)
            .constructMaterial();
    }

    private static Materials loadLignite() {
        return new MaterialBuilder().setName("Lignite")
            .setDefaultLocalName("Lignite Coal")
            .setIconSet(TextureSet.SET_LIGNITE)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00644646)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Water, 1)
            .addOreByproduct(() -> Materials.Coal)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadLiquidAir() {
        return new MaterialBuilder().setName("LiquidAir")
            .setDefaultLocalName("Liquid Air")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0xf0a9d0f5)
            .addCell()
            .setMeltingPoint(4)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Nitrogen, 40)
            .addMaterial(Materials.Oxygen, 11)
            .addMaterial(Materials.Argon, 1)
            .addMaterial(Materials.NobleGases, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadLiveRoot() {
        return new MaterialBuilder().setName("LiveRoot")
            .setDefaultLocalName("Liveroot")
            .setChemicalFormula("(COH₃)Ma")
            .setIconSet(TextureSet.SET_WOOD)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00dcc800)
            .setFuel(MaterialBuilder.FuelType.Magic, 16)
            .addDustItems()
            .setDensity(4, 3)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Wood, 3)
            .addMaterial(Materials.Magic, 1)
            .addAspect(TCAspects.ARBOR, 2)
            .addAspect(TCAspects.VICTUS, 2)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.WOOD)
            .constructMaterial();
    }

    private static Materials loadMalachite() {
        return new MaterialBuilder().setName("Malachite")
            .setDefaultLocalName("Malachite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x00055f05)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // Cu2CO3(OH)2
            .addMaterial(Materials.Copper, 2)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 5)
            .addOreByproduct(() -> Materials.Copper)
            .addOreByproduct(() -> Materials.BrownLimonite)
            .addOreByproduct(() -> Materials.Calcite)
            .setDirectSmelting(() -> Materials.Copper)
            .addSubTag(SubTag.INDUCTIONSMELTING_LOW_OUTPUT)
            .constructMaterial();
    }

    private static Materials loadManyullyn() {
        return new MaterialBuilder().setName("Manyullyn")
            .setDefaultLocalName("Manyullyn")
            .setChemicalFormula("AiCo")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyePurple)
            .setARGB(0x009a4cb9)
            .setTool(2_048, 5, 25.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3_600)
            .setBlastFurnaceTemp(3_600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.Ardite, 1)
            .addAspect(TCAspects.STRONTIO, 2)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadMarble() {
        return new MaterialBuilder().setName("Marble")
            .setDefaultLocalName("Marble")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00c8c8c8)
            .setTool(16, 1, 1.0f)
            .addDustItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Calcite, 7)
            .addAspect(TCAspects.PERFODIO, 1)
            .addOreByproduct(() -> Materials.Calcite)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STONE)
            .addOrePrefix(OrePrefixes.dustImpure)
            .addOrePrefix(OrePrefixes.plate)
            .constructMaterial();
    }

    private static Materials loadMica() {
        return new MaterialBuilder().setName("Mica")
            .setDefaultLocalName("Mica")
            .setIconSet(TextureSet.SET_FINE)
            .setARGB(0x00c3c3cd)
            .addDustItems()
            .addOreItems()
            // KAl2(AlSi3O10)(F,OH)2
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Aluminium, 3)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Fluorine, 2)
            .addMaterial(Materials.Oxygen, 10)
            .constructMaterial();
    }

    private static Materials loadMirabilite() {
        return new MaterialBuilder().setName("Mirabilite")
            .setDefaultLocalName("Mirabilite")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00f0fad2)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // Na2SO4 10H2O
            .addMaterial(Materials.Sodium, 2)
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Water, 10)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadMonazite() {
        return new MaterialBuilder().setName("Monazite")
            .setDefaultLocalName("Monazite")
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x00324632)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(8)
            .addElectrolyzerRecipe()
            // Wikipedia: (Ce, La, Nd, Th, Sm, Gd)PO4 Monazite also smelt-extract to Helium, it is brown like the rare
            // earth Item Monazite sand deposits are inevitably of the monazite-(Ce) composition. Typically, the
            // lanthanides in such monazites contain about 45.8% cerium, about 24% lanthanum, about 17% neodymium, about
            // 5% praseodymium, and minor quantities of samarium, gadolinium, and yttrium. Europium concentrations tend
            // to be low, about 0.05% Thorium content of monazite is variable.
            .addMaterial(Materials.RareEarth, 1)
            .addMaterial(Materials.Phosphate, 1)
            .addOreByproduct(() -> Materials.Thorium)
            .addOreByproduct(() -> Materials.Neodymium)
            .addOreByproduct(() -> Materials.RareEarth)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadNeodymiumMagnetic() {
        return new MaterialBuilder().setName("NeodymiumMagnetic")
            .setDefaultLocalName("Magnetic Neodymium")
            .setChemicalFormula("Nd" + CustomGlyphs.MAGNET)
            .setIconSet(TextureSet.SET_MAGNETIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00646464)
            .setTool(512, 2, 7.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_297)
            .setBlastFurnaceTemp(1_297)
            .setBlastFurnaceRequired(true)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Neodymium, 1)
            .setSmeltingInto(() -> Materials.Neodymium)
            .setMaceratingInto(() -> Materials.Neodymium)
            .setArcSmeltingInto(() -> Materials.Neodymium)
            .addAspect(TCAspects.METALLUM, 1)
            .addAspect(TCAspects.MAGNETO, 3)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadNiter() {
        return new MaterialBuilder().setName("Niter")
            .setDefaultLocalName("Niter")
            .setIconSet(TextureSet.SET_FLINT)
            .setColor(Dyes.dyePink)
            .setARGB(0x00ffc8c8)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Saltpeter, 1)
            .addOreByproduct(() -> Materials.Saltpeter)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadNitroCoalFuel() {
        return new MaterialBuilder().setName("NitroCoalFuel")
            .setDefaultLocalName("Nitro-Coalfuel")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00324632)
            .setFuel(MaterialBuilder.FuelType.Diesel, 48)
            .addCell()
            .addMaterial(Materials.Glyceryl, 1)
            .addMaterial(Materials.CoalFuel, 4)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.EXPLOSIVE)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.NO_SMASHING)
            .constructMaterial();
    }

    private static Materials loadNitroFuel() {
        return new MaterialBuilder().setName("NitroFuel")
            .setDefaultLocalName("Cetane-Boosted Diesel")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00c8ff00)
            .setFuel(MaterialBuilder.FuelType.Diesel, 1_000)
            .addCell()
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.EXPLOSIVE)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.NO_SMASHING)
            .constructMaterial();
    }

    private static Materials loadOlivine() {
        return new MaterialBuilder().setName("Olivine")
            .setDefaultLocalName("Olivine")
            .setIconSet(TextureSet.SET_RUBY)
            .setColor(Dyes.dyeLime)
            .setARGB(0x7f96ff96)
            .setTool(256, 2, 7.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Magnesium, 2)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.SiliconDioxide, 2)
            .addAspect(TCAspects.LUCRUM, 4)
            .addAspect(TCAspects.VITREUS, 2)
            .addOreByproduct(() -> Materials.Pyrope)
            .addOreByproduct(() -> Materials.Magnesium)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadOpal() {
        return new MaterialBuilder().setName("Opal")
            .setDefaultLocalName("Opal")
            .setIconSet(TextureSet.SET_OPAL)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x000000ff)
            .setTool(256, 2, 7.0f)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.SiliconDioxide, 1)
            .addAspect(TCAspects.LUCRUM, 5)
            .addAspect(TCAspects.VITREUS, 3)
            .addOreByproduct(() -> Materials.Tanzanite)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadPentlandite() {
        return new MaterialBuilder().setName("Pentlandite")
            .setDefaultLocalName("Pentlandite")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00a59605)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // (Fe,Ni)9S8
            .addMaterial(Materials.Nickel, 9)
            .addMaterial(Materials.Sulfur, 8)
            .addOreByproduct(() -> Materials.Iron)
            .addOreByproduct(() -> Materials.Sulfur)
            .addOreByproduct(() -> Materials.Cobalt)
            .setDirectSmelting(() -> Materials.Nickel)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .addSubTag(SubTag.INDUCTIONSMELTING_LOW_OUTPUT)
            .constructMaterial();
    }

    private static Materials loadPerlite() {
        return new MaterialBuilder().setName("Perlite")
            .setDefaultLocalName("Perlite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x001e141e)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Obsidian, 2)
            .addMaterial(Materials.Water, 1)
            .constructMaterial();
    }

    private static Materials loadPitchblende() {
        return new MaterialBuilder().setName("Pitchblende")
            .setDefaultLocalName("Pitchblende")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00c8d200)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Uraninite, 3)
            .addMaterial(Materials.Thorium, 1)
            .addMaterial(Materials.Lead, 1)
            .addOreByproduct(() -> Materials.Thorium)
            .addOreByproduct(() -> Materials.Uranium)
            .addOreByproduct(() -> Materials.Lead)
            .constructMaterial();
    }

    private static Materials loadPollucite() {
        return new MaterialBuilder().setName("Pollucite")
            .setDefaultLocalName("Pollucite")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00f0d2d2)
            .addDustItems()
            .addOreItems()
            // (Cs,Na)2Al2Si4O12 2H2O (also a source of Rb)
            .addMaterial(Materials.Caesium, 2)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 4)
            .addMaterial(Materials.Water, 2)
            .addMaterial(Materials.Oxygen, 12)
            .addOreByproduct(() -> Materials.Caesium)
            .addOreByproduct(() -> Materials.Aluminiumoxide)
            .addOreByproduct(() -> Materials.Rubidium)
            .constructMaterial();
    }

    private static Materials loadPotassiumFeldspar() {
        return new MaterialBuilder().setName("PotassiumFeldspar")
            .setDefaultLocalName("Potassium Feldspar")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyePink)
            .setARGB(0x00782828)
            .addDustItems()
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 8)
            .constructMaterial();
    }

    private static Materials loadQuartzSand() {
        return new MaterialBuilder().setName("QuartzSand")
            .setDefaultLocalName("Quartz Sand")
            .setChemicalFormula("(SiO₂)" + CustomGlyphs.SUBSCRIPT_QUESTION_MARK + "??")
            .setIconSet(TextureSet.SET_SAND)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00c2b280)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.CertusQuartz, 1)
            .addMaterial(Materials.Quartzite, 1)
            .addOreByproduct(() -> Materials.CertusQuartz)
            .addOreByproduct(() -> Materials.Quartzite)
            .addOreByproduct(() -> Materials.Barite)
            .constructMaterial();
    }

    private static Materials loadRawStyreneButadieneRubber() {
        return new MaterialBuilder().setName("RawStyreneButadieneRubber")
            .setDefaultLocalName("Raw Styrene-Butadiene Rubber")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGray)
            .setARGB(0x0054403d)
            .addDustItems()
            .addMaterial(Materials.Styrene, 1)
            .addMaterial(Materials.Butadiene, 3)
            .constructMaterial();
    }

    private static Materials loadRealgar() {
        return new MaterialBuilder().setName("Realgar")
            .setDefaultLocalName("Realgar")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x008c6464)
            .setTool(32, 1, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .addMaterial(Materials.Arsenic, 4)
            .addMaterial(Materials.Sulfur, 4)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadRedMud() {
        return new MaterialBuilder().setName("RedMud")
            .setDefaultLocalName("Red Mud")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeRed)
            .setARGB(0x008c1616)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadRedrock() {
        return new MaterialBuilder().setName("Redrock")
            .setDefaultLocalName("Redrock")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00ff5032)
            .addDustItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Calcite, 2)
            .addMaterial(Materials.Flint, 1)
            .addMaterial(Materials.Clay, 1)
            .addOreByproduct(() -> Materials.Clay)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STONE)
            .addOrePrefix(OrePrefixes.dustImpure)
            .constructMaterial();
    }

    private static Materials loadRedstone() {
        return new MaterialBuilder().setName("Redstone")
            .setDefaultLocalName("Redstone")
            .setIconSet(TextureSet.SET_REDSTONE)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00c80000)
            .addDustItems()
            .addOreItems()
            .setOreMultiplier(5)
            .setMeltingPoint(500)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Pyrite, 5)
            .addMaterial(Materials.Ruby, 1)
            .addMaterial(Materials.Mercury, 3)
            .addAspect(TCAspects.MACHINA, 1)
            .addAspect(TCAspects.POTENTIA, 2)
            .addOreByproduct(() -> Materials.Cinnabar)
            .addOreByproduct(() -> Materials.RareEarth)
            .addOreByproduct(() -> Materials.Glowstone)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.PULVERIZING_CINNABAR)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .addSubTag(SubTag.STONE)
            .addSubTag(SubTag.UNBURNABLE)
            .addOrePrefix(OrePrefixes.plate)
            .removeOrePrefix(OrePrefixes.block) // minecraft:redstone_block
            .removeOrePrefix(OrePrefixes.dust) // minecraft:redstone
            .constructMaterial();
    }

    private static Materials loadRoseGold() {
        return new MaterialBuilder().setName("RoseGold")
            .setDefaultLocalName("Rose Gold")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyePink)
            .setARGB(0x00dea193)
            .setTool(128, 2, 14.0f)
            .setToolEnchantment(() -> Enchantment.smite, 4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1_600)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Copper, 1)
            .addMaterial(Materials.Gold, 4)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadSamariumMagnetic() {
        return new MaterialBuilder().setName("SamariumMagnetic")
            .setDefaultLocalName("Magnetic Samarium")
            .setChemicalFormula("Sm" + CustomGlyphs.MAGNET)
            .setIconSet(TextureSet.SET_MAGNETIC)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00ffffcc)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_345)
            .setBlastFurnaceTemp(1_345)
            .setBlastFurnaceRequired(true)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Samarium, 1)
            .setSmeltingInto(() -> Materials.Samarium)
            .setMaceratingInto(() -> Materials.Samarium)
            .setArcSmeltingInto(() -> Materials.Samarium)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addAspect(TCAspects.MAGNETO, 10)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadSoapstone() {
        return new MaterialBuilder().setName("Soapstone")
            .setDefaultLocalName("Soapstone")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x005f915f)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // H2Mg3(SiO3)4
            .addMaterial(Materials.Magnesium, 3)
            .addMaterial(Materials.Silicon, 4)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadSodiumAluminate() {
        return new MaterialBuilder().setName("SodiumAluminate")
            .setDefaultLocalName("Sodium Aluminate")
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00ffebff)
            .setTool(64, 1, 1.0f)
            .addDustItems()
            .setBlastFurnaceTemp(1_800)
            .setMeltingPoint(1_800)
            .setBlastFurnaceRequired(false)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadSodiumCarbonate() {
        return new MaterialBuilder().setName("SodiumCarbonate")
            .setDefaultLocalName("Sodium Carbonate")
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00ffffeb)
            .setTool(64, 1, 1.0f)
            .addDustItems()
            .setBlastFurnaceTemp(851)
            .setMeltingPoint(851)
            .setBlastFurnaceRequired(false)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Sodium, 2)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadSpodumene() {
        return new MaterialBuilder().setName("Spodumene")
            .setDefaultLocalName("Spodumene")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00beaaaa)
            .addDustItems()
            .addOreItems()
            // LiAl(SiO3)2
            .addMaterial(Materials.Lithium, 1)
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Oxygen, 6)
            .addOreByproduct(() -> Materials.Aluminiumoxide)
            .addOreByproduct(() -> Materials.Lithium)
            .constructMaterial();
    }

    private static Materials loadSteelMagnetic() {
        return new MaterialBuilder().setName("SteelMagnetic")
            .setDefaultLocalName("Magnetic Steel")
            .setChemicalFormula("Fe₅" + CustomGlyphs.SUBSCRIPT0 + "C" + CustomGlyphs.MAGNET)
            .setIconSet(TextureSet.SET_MAGNETIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00808080)
            .setTool(512, 3, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_000)
            .setBlastFurnaceTemp(1_000)
            .setBlastFurnaceRequired(true)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Steel, 1)
            .setSmeltingInto(() -> Materials.Steel)
            .setMaceratingInto(() -> Materials.Steel)
            .setArcSmeltingInto(() -> Materials.Steel)
            .addAspect(TCAspects.METALLUM, 1)
            .addAspect(TCAspects.ORDO, 1)
            .addAspect(TCAspects.MAGNETO, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .constructMaterial();
    }

    private static Materials loadSteeleaf() {
        return new MaterialBuilder().setName("Steeleaf")
            .setDefaultLocalName("Steeleaf")
            .setIconSet(TextureSet.SET_LEAF)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x00327f32)
            .setTool(768, 3, 8.0f)
            .setToolEnchantment(() -> Enchantment.fortune, 2)
            .setArmorEnchantment(() -> Enchantment.protection, 2)
            .setFuel(MaterialBuilder.FuelType.Magic, 24)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Steel, 1)
            .addMaterial(Materials.Magic, 1)
            .addAspect(TCAspects.HERBA, 2)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.WOOD)
            .constructMaterial();
    }

    private static Materials loadSterlingSilver() {
        return new MaterialBuilder().setName("SterlingSilver")
            .setDefaultLocalName("Sterling Silver")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fadce1)
            .setTool(128, 2, 13.0f)
            .setToolEnchantment(() -> EnchantmentEnderDamage.INSTANCE, 4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1_700)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Copper, 1)
            .addMaterial(Materials.Silver, 4)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadSugar() {
        return new MaterialBuilder().setName("Sugar")
            .setDefaultLocalName("Sugar")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fafafa)
            .addDustItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Water, 5)
            .addMaterial(Materials.Oxygen, 25)
            .addAspect(TCAspects.HERBA, 1)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.AER, 1)
            .addSubTag(SubTag.FOOD)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .removeOrePrefix(OrePrefixes.dust) // minecraft:sugar
            .constructMaterial();
    }

    private static Materials loadTalc() {
        return new MaterialBuilder().setName("Talc")
            .setDefaultLocalName("Talc")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x005ab45a)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // H2Mg3(SiO3)4
            .addMaterial(Materials.Magnesium, 3)
            .addMaterial(Materials.Silicon, 4)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadTantalite() {
        return new MaterialBuilder().setName("Tantalite")
            .setDefaultLocalName("Tantalite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00915028)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // (Fe, Mn)Ta2O6 (also source of Nb)
            .addMaterial(Materials.Manganese, 1)
            .addMaterial(Materials.Tantalum, 2)
            .addMaterial(Materials.Oxygen, 6)
            .addOreByproduct(() -> Materials.Manganese)
            .addOreByproduct(() -> Materials.Niobium)
            .addOreByproduct(() -> Materials.Tantalum)
            .constructMaterial();
    }

    private static Materials loadThaumium() {
        return new MaterialBuilder().setName("Thaumium")
            .setDefaultLocalName("Thaumium")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePurple)
            .setARGB(0x009664c8)
            .setTool(256, 3, 12.0f)
            .setToolEnchantment(() -> Enchantment.fortune, 2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setDensity(2, 1)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Magic, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadTPV() {
        return new MaterialBuilder().setName("TPVAlloy")
            .setDefaultLocalName("TPV-Alloy")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00faaafa)
            .setTool(4_000, 5, 16.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3_000)
            .setBlastFurnaceTemp(3_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Titanium, 3)
            .addMaterial(Materials.Platinum, 3)
            .addMaterial(Materials.Vanadium, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadTricalciumPhosphate() {
        return new MaterialBuilder().setName("TricalciumPhosphate")
            .setDefaultLocalName("Tricalcium Phosphate")
            .setIconSet(TextureSet.SET_FLINT)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(3)
            .addCell()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Calcium, 3)
            .addMaterial(Materials.Phosphate, 2)
            .addOreByproduct(() -> Materials.Apatite)
            .addOreByproduct(() -> Materials.Phosphate)
            .addOreByproduct(() -> Materials.Pyrochlore)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.EXPLOSIVE)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadTrona() {
        return new MaterialBuilder().setName("Trona")
            .setDefaultLocalName("Trona")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x0087875f)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // Na3(CO3)(HCO3) 2H2O
            .addMaterial(Materials.Sodium, 3)
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Water, 2)
            .addMaterial(Materials.Oxygen, 6)
            .constructMaterial();
    }

    private static Materials loadTungstenCarbide() {
        return new MaterialBuilder().setName("TungstenCarbide")
            .setDefaultLocalName("Tungstencarbide")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00330066)
            .setTool(1_280, 4, 14.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 5)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2_460)
            .setBlastFurnaceTemp(2_460)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Tungsten, 1)
            .addMaterial(Materials.Carbon, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadTungstenSteel() {
        return new MaterialBuilder().setName("TungstenSteel")
            .setDefaultLocalName("Tungstensteel")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x006464a0)
            .setTool(2_560, 4, 8.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(4_000)
            .setBlastFurnaceTemp(4_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Steel, 1)
            .addMaterial(Materials.Tungsten, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadVanadiumMagnetite() {
        return new MaterialBuilder().setName("VanadiumMagnetite")
            .setDefaultLocalName("Vanadium Magnetite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x0023233c)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            // Mixture of Fe3O4 and V2O5
            .addMaterial(Materials.Magnetite, 1)
            .addMaterial(Materials.Vanadium, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .addOreByproduct(() -> Materials.Magnetite)
            .addOreByproduct(() -> Materials.Vanadium)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD)
            .constructMaterial();
    }

    private static Materials loadVanadiumSteel() {
        return new MaterialBuilder().setName("VanadiumSteel")
            .setDefaultLocalName("Vanadiumsteel")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00c0c0c0)
            .setTool(1_920, 3, 3.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_453)
            .setBlastFurnaceTemp(1_453)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Vanadium, 1)
            .addMaterial(Materials.Chrome, 1)
            .addMaterial(Materials.Steel, 7)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadVermiculite() {
        return new MaterialBuilder().setName("Vermiculite")
            .setDefaultLocalName("Vermiculite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00c8b40f)
            .addDustItems()
            .addOreItems()
            // (Mg+2, Fe+2, Fe+3)3 [(AlSi)4O10] (OH)2 4H2O)
            .addMaterial(Materials.Iron, 3)
            .addMaterial(Materials.Aluminium, 4)
            .addMaterial(Materials.Silicon, 4)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Water, 4)
            .addMaterial(Materials.Oxygen, 12)
            .addOreByproduct(() -> Materials.Iron)
            .addOreByproduct(() -> Materials.Aluminiumoxide)
            .addOreByproduct(() -> Materials.Magnesium)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)
            .constructMaterial();
    }

    private static Materials loadVinteum() {
        return new MaterialBuilder().setName("Vinteum")
            .setDefaultLocalName("Vinteum")
            .setChemicalFormula("FeMa*")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x0064c8ff)
            .setTool(128, 3, 10.0f)
            .setToolEnchantment(() -> Enchantment.fortune, 1)
            .setFuel(MaterialBuilder.FuelType.Magic, 32)
            .addDustItems()
            .addMetalItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Thaumium, 1)
            .addAspect(TCAspects.VITREUS, 2)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addOreByproduct(() -> Materials.Vinteum)
            .addSubTag(SubTag.METAL)
            .removeOrePrefix(OrePrefixes.lens)
            .constructMaterial();
    }

    private static Materials loadVis() {
        return new MaterialBuilder().setName("Vis")
            .setDefaultLocalName("Vis")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyePurple)
            .setARGB(0x008000ff)
            .setFuel(MaterialBuilder.FuelType.Magic, 32)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Magic, 1)
            .addAspect(TCAspects.AURAM, 2)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .constructMaterial();
    }

    private static Materials loadVolcanicAsh() {
        return new MaterialBuilder().setName("VolcanicAsh")
            .setDefaultLocalName("Volcanic Ashes")
            .setIconSet(TextureSet.SET_FLINT)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x003c3232)
            .addDustItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Flint, 6)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Magnesium, 1)
            .constructMaterial();
    }

    private static Materials loadWollastonite() {
        return new MaterialBuilder().setName("Wollastonite")
            .setDefaultLocalName("Wollastonite")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00f0f0f0)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // CaSiO3
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadWoodSealed() {
        return new MaterialBuilder().setName("WoodSealed")
            .setDefaultLocalName("Sealed Wood")
            .setChemicalFormula("")
            .setIconSet(TextureSet.SET_WOOD)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00502800)
            .setTool(24, 0, 3.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addMaterial(Materials.Wood, 1)
            .setMaceratingInto(() -> Materials.Wood)
            .addAspect(TCAspects.ARBOR, 2)
            .addAspect(TCAspects.FABRICO, 1)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.NO_WORKING)
            .addSubTag(SubTag.WOOD)
            .removeOrePrefix(OrePrefixes.ingot)
            .constructMaterial();
    }

    private static Materials loadZeolite() {
        return new MaterialBuilder().setName("Zeolite")
            .setDefaultLocalName("Zeolite")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00f0e6e6)
            .addDustItems()
            .addOreItems()
            // NaCa4(Si27Al9)O72
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Calcium, 4)
            .addMaterial(Materials.Silicon, 27)
            .addMaterial(Materials.Aluminium, 9)
            .addMaterial(Materials.Oxygen, 72)
            .constructMaterial();
    }

    private static void loadDegree3Compounds() {
        Materials.Basalt = loadBasalt();
        Materials.BlueSteel = loadBlueSteel();
        Materials.BorosilicateGlass = loadBorosilicateGlass();
        Materials.Cryotheum = loadCryotheum();
        Materials.DarkSteel = loadDarkSteel();
        Materials.Diatomite = loadDiatomite();
        Materials.EnderEye = loadEnderEye();
        Materials.Fireclay = loadFireclay();
        Materials.GarnetSand = loadGarnetSand();
        Materials.HSSG = loadHSSG();
        Materials.IronWood = loadIronWood();
        Materials.Pyrotheum = loadPyrotheum();
        Materials.GraniteRed = loadGraniteRed();
        Materials.RedAlloy = loadRedAlloy();
        Materials.RedSteel = loadRedSteel();
        Materials.RedstoneAlloy = loadRedstoneAlloy();
    }

    private static Materials loadBasalt() {
        return new MaterialBuilder().setName("Basalt")
            .setDefaultLocalName("Basalt")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x001e1414)
            .setTool(64, 1, 1.0f)
            .addDustItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .setAutoGeneratedRecycleRecipes(false)
            .addMaterial(Materials.Olivine, 1)
            .addMaterial(Materials.Calcite, 3)
            .addMaterial(Materials.Flint, 8)
            .addMaterial(Materials.AshDark, 4)
            .addAspect(TCAspects.TENEBRAE, 1)
            .addOreByproduct(() -> Materials.Olivine)
            .addOreByproduct(() -> Materials.AshDark)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STONE)
            .addOrePrefix(OrePrefixes.dustImpure)
            .addOrePrefix(OrePrefixes.plate)
            .constructMaterial();
    }

    private static Materials loadBlueSteel() {
        return new MaterialBuilder().setName("BlueSteel")
            .setDefaultLocalName("Blue Steel")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x0064648c)
            .setTool(1_024, 4, 7.5f)
            .setToolEnchantment(() -> Enchantment.sharpness, 5)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1_400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.RoseGold, 1)
            .addMaterial(Materials.Brass, 1)
            .addMaterial(Materials.Steel, 2)
            .addMaterial(Materials.BlackSteel, 4)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadBorosilicateGlass() {
        return new MaterialBuilder().setName("BorosilicateGlass")
            .setDefaultLocalName("Borosilicate Glass")
            .setIconSet(TextureSet.SET_GLASS)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00e6f3e6)
            .addDustItems()
            .addMetalItems()
            .addMaterial(Materials.Boron, 1)
            .addMaterial(Materials.Glass, 7)
            .addCentrifugeRecipe()
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_RECYCLING)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .constructMaterial();
    }

    private static Materials loadCryotheum() {
        return new MaterialBuilder().setName("Cryotheum")
            .setDefaultLocalName("Cryotheum")
            .setChemicalFormula(
                "(KNO₃)(" + Materials.Redstone.mChemicalFormula + ")(H₂O)(" + Materials.Blizz.mChemicalFormula + ")")
            .setIconSet(TextureSet.SET_CRYOTHEUM)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x000094cb)
            .setFuel(MaterialBuilder.FuelType.Thermal, 62)
            .addDustItems()
            .setDensity(4, 1)
            .setAutoGeneratedRecycleRecipes(false)
            .addMaterial(Materials.Saltpeter, 1)
            .addMaterial(Materials.Redstone, 1)
            .addMaterial(Materials.Snow, 1)
            .addMaterial(Materials.Blizz, 1)
            .addAspect(TCAspects.PRAECANTATIO, 2)
            .addAspect(TCAspects.ELECTRUM, 1)
            .addAspect(TCAspects.GELUM, 1)
            .constructMaterial();
    }

    private static Materials loadDarkSteel() {
        return new MaterialBuilder().setName("DarkSteel")
            .setDefaultLocalName("Dark Steel")
            .setIconSet(TextureSet.SET_DARKSTEEL)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00504650)
            .setTool(512, 3, 8.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1_800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.ElectricalSteel, 1)
            .addMaterial(Materials.Coal, 1)
            .addMaterial(Materials.Obsidian, 1)
            .addSubTag(SubTag.METAL)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial();
    }

    private static Materials loadDiatomite() {
        return new MaterialBuilder().setName("Diatomite")
            .setDefaultLocalName("Diatomite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00e1e1e1)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Flint, 8)
            .addMaterial(Materials.BandedIron, 1)
            .addMaterial(Materials.Sapphire, 1)
            .addOreByproduct(() -> Materials.BandedIron)
            .addOreByproduct(() -> Materials.Sapphire)
            .constructMaterial();
    }

    private static Materials loadEnderEye() {
        return new MaterialBuilder().setName("EnderEye")
            .setDefaultLocalName("Endereye")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x00a0fae6)
            .setTool(16, 1, 1.0f)
            .setFuel(MaterialBuilder.FuelType.Magic, 10)
            .addDustItems()
            .addGemItems()
            .setDensity(2, 1)
            .addMaterial(Materials.EnderPearl, 1)
            .addMaterial(Materials.Blaze, 1)
            .addAspect(TCAspects.SENSUS, 4)
            .addAspect(TCAspects.ALIENIS, 4)
            .addAspect(TCAspects.ITER, 4)
            .addAspect(TCAspects.PRAECANTATIO, 3)
            .addAspect(TCAspects.IGNIS, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.PEARL)
            .removeOrePrefix(OrePrefixes.gem) // minecraft:ender_eye
            .constructMaterial();
    }

    private static Materials loadFireclay() {
        return new MaterialBuilder().setName("Fireclay")
            .setDefaultLocalName("Fireclay")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00ada09b)
            .addDustItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Brick, 1)
            .addMaterial(Materials.Clay, 1)
            .constructMaterial();
    }

    private static Materials loadGarnetSand() {
        return new MaterialBuilder().setName("GarnetSand")
            .setDefaultLocalName("Garnet Sand")
            .setIconSet(TextureSet.SET_SAND)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00c86400)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.GarnetRed, 1)
            .addMaterial(Materials.GarnetYellow, 1)
            .addOreByproduct(() -> Materials.GarnetRed)
            .addOreByproduct(() -> Materials.GarnetYellow)
            .constructMaterial();
    }

    private static Materials loadHSSG() {
        return new MaterialBuilder().setName("HSSG")
            .setDefaultLocalName("HSS-G")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00999900)
            .setTool(4_000, 3, 10.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(4_500)
            .setBlastFurnaceTemp(4_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.TungstenSteel, 5)
            .addMaterial(Materials.Chrome, 1)
            .addMaterial(Materials.Molybdenum, 2)
            .addMaterial(Materials.Vanadium, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadIronWood() {
        return new MaterialBuilder().setName("IronWood")
            .setDefaultLocalName("Ironwood")
            .setIconSet(TextureSet.SET_WOOD)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00968c6e)
            .setTool(384, 2, 6.0f)
            .setToolEnchantment(() -> Enchantment.fortune, 1)
            .setArmorEnchantment(() -> Enchantment.aquaAffinity, 1)
            .setFuel(MaterialBuilder.FuelType.Magic, 8)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setDensity(19, 18)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Iron, 9)
            .addMaterial(Materials.LiveRoot, 9)
            .addMaterial(Materials.Gold, 1)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MORTAR_GRINDABLE)
            .addSubTag(SubTag.WOOD)
            .constructMaterial();
    }

    private static Materials loadPyrotheum() {
        return new MaterialBuilder().setName("Pyrotheum")
            .setDefaultLocalName("Pyrotheum")
            .setIconSet(TextureSet.SET_PYROTHEUM)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ff8000)
            .setFuel(MaterialBuilder.FuelType.Thermal, 62)
            .addDustItems()
            .setDensity(4, 1)
            .setHeatDamage(5.0f)
            .setAutoGeneratedRecycleRecipes(false)
            .addMaterial(Materials.Coal, 1)
            .addMaterial(Materials.Redstone, 1)
            .addMaterial(Materials.Blaze, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addAspect(TCAspects.PRAECANTATIO, 2)
            .addAspect(TCAspects.IGNIS, 1)
            .constructMaterial();
    }

    private static Materials loadGraniteRed() {
        return new MaterialBuilder().setName("GraniteRed")
            .setDefaultLocalName("Red Granite")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeMagenta)
            .setARGB(0x00ff0080)
            .setTool(64, 3, 4.0f)
            .addDustItems()
            .addToolHeadItems()
            .addGearItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.PotassiumFeldspar, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addAspect(TCAspects.TUTAMEN, 1)
            .addOreByproduct(() -> Materials.PotassiumFeldspar)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STONE)
            .addOrePrefix(OrePrefixes.dustImpure)
            .addOrePrefix(OrePrefixes.plate)
            .constructMaterial();
    }

    private static Materials loadRedAlloy() {
        return new MaterialBuilder().setName("RedAlloy")
            .setDefaultLocalName("Red Alloy")
            .setChemicalFormula("Cu(" + Materials.Redstone.mChemicalFormula + ")₄")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00c80000)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(500)
            .setDensity(5, 1)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Copper, 1)
            .addMaterial(Materials.Redstone, 4)
            .addAspect(TCAspects.MACHINA, 3)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadRedSteel() {
        return new MaterialBuilder().setName("RedSteel")
            .setDefaultLocalName("Red Steel")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setARGB(0x008c6464)
            .setTool(896, 4, 7.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1_300)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.SterlingSilver, 1)
            .addMaterial(Materials.BismuthBronze, 1)
            .addMaterial(Materials.Steel, 2)
            .addMaterial(Materials.BlackSteel, 4)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadRedstoneAlloy() {
        return new MaterialBuilder().setName("RedstoneAlloy")
            .setDefaultLocalName("Redstone Alloy")
            .setIconSet(TextureSet.SET_DARKSTEEL)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00ff4332)
            .setTool(128, 2, 3.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(671)
            .setBlastFurnaceTemp(1_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Redstone, 1)
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Coal, 1)
            .addSubTag(SubTag.METAL)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial();
    }

    private static void loadDegree4Compounds() {
        Materials.BasalticMineralSand = loadBasalticMineralSand();
        Materials.ConductiveIron = loadConductiveIron();
        Materials.EndSteel = loadEndSteel();
        Materials.HSSE = loadHSSE();
        Materials.HSSS = loadHSSS();
        Materials.PulsatingIron = loadPulsatingIron();
    }

    private static Materials loadBasalticMineralSand() {
        return new MaterialBuilder().setName("BasalticMineralSand")
            .setDefaultLocalName("Basaltic Mineral Sand")
            .setIconSet(TextureSet.SET_SAND)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00283228)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Magnetite, 1)
            .addMaterial(Materials.Basalt, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .addOreByproduct(() -> Materials.Basalt)
            .addOreByproduct(() -> Materials.Magnetite)
            .setDirectSmelting(() -> Materials.Iron)
            .addSubTag(SubTag.BLASTFURNACE_CALCITE_DOUBLE)
            .addSubTag(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD)
            .addSubTag(SubTag.INDUCTIONSMELTING_LOW_OUTPUT)
            .constructMaterial();
    }

    private static Materials loadConductiveIron() {
        return new MaterialBuilder().setName("ConductiveIron")
            .setDefaultLocalName("Conductive Iron")
            .setIconSet(TextureSet.SET_DARKSTEEL)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00ffbfc3)
            .setTool(256, 3, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1_200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.RedstoneAlloy, 1)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Silver, 1)
            .addSubTag(SubTag.METAL)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial();
    }

    private static Materials loadEndSteel() {
        return new MaterialBuilder().setName("EndSteel")
            .setDefaultLocalName("End Steel")
            .setIconSet(TextureSet.SET_END_STEEL)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00dbce7d)
            .setTool(2_000, 4, 12.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(940)
            .setBlastFurnaceTemp(3_600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.DarkSteel, 1)
            .addMaterial(Materials.Tungsten, 1)
            .addMaterial(Materials.Endstone, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadHSSE() {
        return new MaterialBuilder().setName("HSSE")
            .setDefaultLocalName("HSS-E")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x00336600)
            .setTool(10_240, 7, 32.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 5)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5_400)
            .setBlastFurnaceTemp(5_400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.HSSG, 6)
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.Manganese, 1)
            .addMaterial(Materials.Silicon, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadHSSS() {
        return new MaterialBuilder().setName("HSSS")
            .setDefaultLocalName("HSS-S")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00660033)
            .setTool(10_240, 8, 32.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 5)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5_400)
            .setBlastFurnaceTemp(5_400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.HSSG, 6)
            .addMaterial(Materials.Iridium, 2)
            .addMaterial(Materials.Osmium, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadPulsatingIron() {
        return new MaterialBuilder().setName("PulsatingIron")
            .setDefaultLocalName("Pulsating Iron")
            .setIconSet(TextureSet.SET_DARKSTEEL)
            .setColor(Dyes.dyeLime)
            .setARGB(0x0080f69b)
            .setTool(256, 3, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1_800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.EnderPearl, 1)
            .addMaterial(Materials.RedstoneAlloy, 1)
            .addSubTag(SubTag.METAL)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial();
    }

    private static void loadDegree5Compounds() {
        Materials.CrystallineAlloy = loadCrystallineAlloy();
        Materials.EnergeticAlloy = loadEnergeticAlloy();
        Materials.EnergeticSilver = loadEnergeticSilver();
        Materials.MelodicAlloy = loadMelodicAlloy();
    }

    private static Materials loadCrystallineAlloy() {
        return new MaterialBuilder().setName("CrystallineAlloy")
            .setDefaultLocalName("Crystalline Alloy")
            .setIconSet(TextureSet.SET_CRYSTALLINE)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x004adbdb)
            .setTool(768, 4, 18.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(4_500)
            .setBlastFurnaceTemp(4_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Gold, 1)
            .addMaterial(Materials.Diamond, 1)
            .addMaterial(Materials.PulsatingIron, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadEnergeticAlloy() {
        return new MaterialBuilder().setName("EnergeticAlloy")
            .setDefaultLocalName("Energetic Alloy")
            .setIconSet(TextureSet.SET_ENERGETIC)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff8c19)
            .setTool(1_024, 3, 12.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(2_200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.ConductiveIron, 1)
            .addMaterial(Materials.Gold, 1)
            .addMaterial(Materials.BlackSteel, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadEnergeticSilver() {
        return new MaterialBuilder().setName("EnergeticSilver")
            .setDefaultLocalName("Energetic Silver")
            .setIconSet(TextureSet.SET_ENERGETIC_SILVER)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x0063a2c7)
            .setTool(512, 3, 8.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(2_200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Silver, 1)
            .addMaterial(Materials.ConductiveIron, 1)
            .addMaterial(Materials.BlackSteel, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadMelodicAlloy() {
        return new MaterialBuilder().setName("MelodicAlloy")
            .setDefaultLocalName("Melodic Alloy")
            .setIconSet(TextureSet.SET_MELODIC)
            .setColor(Dyes.dyeMagenta)
            .setARGB(0x00c155c1)
            .setTool(1_024, 5, 24.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5_400)
            .setBlastFurnaceTemp(5_400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.EndSteel, 1)
            .addMaterial(Materials.EnderEye, 1)
            .addMaterial(Materials.Oriharukon, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static void loadDegree6Compounds() {
        Materials.CrystallinePinkSlime = loadCrystallinePinkSlime();
        Materials.StellarAlloy = loadStellarAlloy();
        Materials.VibrantAlloy = loadVibrantAlloy();
        Materials.VividAlloy = loadVividAlloy();
    }

    private static Materials loadCrystallinePinkSlime() {
        return new MaterialBuilder().setName("CrystallinePinkSlime")
            .setDefaultLocalName("Crystalline Pink Slime")
            .setIconSet(TextureSet.SET_CRYSTALLINE_PINK_SLIME)
            .setColor(Dyes.dyePink)
            .setARGB(0x00E56BDB)
            .setTool(128, 3, 6.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5_000)
            .setBlastFurnaceTemp(5_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.CrystallineAlloy, 1)
            .addMaterial(Materials.Diamond, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial();
    }

    private static Materials loadStellarAlloy() {
        return new MaterialBuilder().setName("StellarAlloy")
            .setDefaultLocalName("Stellar Alloy")
            .setIconSet(TextureSet.SET_STELLAR)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00d3ffff)
            .setTool(10_240, 7, 96.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(7_200)
            .setBlastFurnaceTemp(7_200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.NetherStar, 1)
            .addMaterial(Materials.MelodicAlloy, 1)
            .addMaterial(Materials.Naquadah, 1)
            .addSubTag(SubTag.METAL)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial();
    }

    private static Materials loadVibrantAlloy() {
        return new MaterialBuilder().setName("VibrantAlloy")
            .setDefaultLocalName("Vibrant Alloy")
            .setIconSet(TextureSet.SET_VIBRANT)
            .setColor(Dyes.dyeLime)
            .setARGB(0x0095e011)
            .setTool(4_048, 4, 18.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3_300)
            .setBlastFurnaceTemp(3_300)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.EnergeticAlloy, 1)
            .addMaterial(Materials.EnderEye, 1)
            .addMaterial(Materials.Chrome, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadVividAlloy() {
        return new MaterialBuilder().setName("VividAlloy")
            .setDefaultLocalName("Vivid Alloy")
            .setIconSet(TextureSet.SET_VIVID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x0046bcdb)
            .setTool(768, 4, 12.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3_300)
            .setBlastFurnaceTemp(3_300)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.EnergeticSilver, 1)
            .addMaterial(Materials.EnderEye, 1)
            .addMaterial(Materials.Chrome, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static void loadPolybenzimidazoleLine() {
        Materials.ChromiumTrioxide = loadChromiumTrioxide();
        Materials.Diaminobenzidin = loadDiaminobenzidin();
        Materials.Dichlorobenzidine = loadDichlorobenzidine();
        Materials.Dimethylbenzene = loadDimethylbenzene();
        Materials.Diphenylisophthalate = loadDiphenylisophthalate();
        Materials.Nitrochlorobenzene = loadNitrochlorobenzene();
        Materials.PhthalicAcid = loadPhthalicAcid();
        Materials.Polybenzimidazole = loadPolybenzimidazole();
        Materials.PotassiumNitrade = loadPotassiumNitrade();
        Materials.Potassiumdichromate = loadPotassiumdichromate();
    }

    private static Materials loadChromiumTrioxide() {
        return new MaterialBuilder().setName("Chromiumtrioxide")
            .setDefaultLocalName("Chromium Trioxide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePink)
            .setARGB(0x00ffe4e1)
            .addDustItems()
            .addMaterial(Materials.Chrome, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDiaminobenzidin() {
        return new MaterialBuilder().setName("3,3Diaminobenzidine")
            .setDefaultLocalName("3,3-Diaminobenzidine")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00337d59)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 12)
            .addMaterial(Materials.Hydrogen, 14)
            .addMaterial(Materials.Nitrogen, 4)
            .constructMaterial();
    }

    private static Materials loadDichlorobenzidine() {
        return new MaterialBuilder().setName("3,3Dichlorobenzidine")
            .setDefaultLocalName("3,3-Dichlorobenzidine")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00a1dea6)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 12)
            .addMaterial(Materials.Hydrogen, 10)
            .addMaterial(Materials.Nitrogen, 2)
            .addMaterial(Materials.Chlorine, 2)
            .constructMaterial();
    }

    private static Materials loadDimethylbenzene() {
        return new MaterialBuilder().setName("Dimethylbenzene")
            .setDefaultLocalName("1,2-Dimethylbenzene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00669c40)
            .addCell()
            .addFluid()
            .setMeltingPoint(248)
            .addMaterial(Materials.Carbon, 8)
            .addMaterial(Materials.Hydrogen, 10)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDiphenylisophthalate() {
        return new MaterialBuilder().setName("DiphenylIsophtalate")
            .setDefaultLocalName("Diphenyl Isophthalate")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00246e57)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 20)
            .addMaterial(Materials.Hydrogen, 14)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadNitrochlorobenzene() {
        return new MaterialBuilder().setName("2Nitrochlorobenzene")
            .setDefaultLocalName("2-Nitrochlorobenzene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x008fb51a)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 6)
            .addMaterial(Materials.Hydrogen, 4)
            .addMaterial(Materials.Chlorine, 1)
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadPhthalicAcid() {
        return new MaterialBuilder().setName("phtalicacid")
            .setDefaultLocalName("Phthalic Acid")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00368547)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 8)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadPolybenzimidazole() {
        return new MaterialBuilder().setName("Polybenzimidazole")
            .setDefaultLocalName("Polybenzimidazole")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x002d2d2d)
            .setTool(64, 1, 3.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_450)
            .addMaterial(Materials.Carbon, 20)
            .addMaterial(Materials.Nitrogen, 4)
            .addMaterial(Materials.Hydrogen, 12)
            .addAspect(TCAspects.ORDO, 2)
            .addAspect(TCAspects.VOLATUS, 1)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STRETCHY)
            .constructMaterial();
    }

    private static Materials loadPotassiumNitrade() {
        return new MaterialBuilder().setName("PotassiumNitrate")
            .setDefaultLocalName("Potassium Nitrate")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setARGB(0x0081228d)
            .addDustItems()
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadPotassiumdichromate() {
        return new MaterialBuilder().setName("PotassiumDichromate")
            .setDefaultLocalName("Potassium Dichromate")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePink)
            .setARGB(0x00ff087f)
            .addDustItems()
            .addMaterial(Materials.Potassium, 2)
            .addMaterial(Materials.Chrome, 2)
            .addMaterial(Materials.Oxygen, 7)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static void loadGasolineLine() {
        Materials.AntiKnock = loadAntiKnock();
        Materials.GasolinePremium = loadGasolinePremium();
        Materials.GasolineRaw = loadGasolineRaw();
        Materials.GasolineRegular = loadGasolineRegular();
        Materials.MTBEMixture = loadMTBEMixture();
        Materials.MTBEMixtureAlt = loadMTBEMixtureAlt();
        Materials.NitrousOxide = loadNitrousOxide();
        Materials.Octane = loadOctane();
    }

    private static Materials loadAntiKnock() {
        return new MaterialBuilder().setName("EthylTertButylEther")
            .setDefaultLocalName("Anti-Knock Agent")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 6)
            .addMaterial(Materials.Hydrogen, 14)
            .addMaterial(Materials.Oxygen, 1)
            .constructMaterial();
    }

    private static Materials loadGasolinePremium() {
        return new MaterialBuilder().setName("HighOctaneGasoline")
            .setDefaultLocalName("High Octane Gasoline")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ffa500)
            .setFuel(MaterialBuilder.FuelType.Diesel, 2_500)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadGasolineRaw() {
        return new MaterialBuilder().setName("RawGasoline")
            .setDefaultLocalName("Raw Gasoline")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff6400)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadGasolineRegular() {
        return new MaterialBuilder().setName("Gasoline")
            .setDefaultLocalName("Gasoline")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ffa500)
            .setFuel(MaterialBuilder.FuelType.Diesel, 576)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadMTBEMixture() {
        return new MaterialBuilder().setName("MTBEReactionMixture(Butene)")
            .setDefaultLocalName("MTBE Reaction Mixture (Butene)")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 5)
            .addMaterial(Materials.Hydrogen, 12)
            .addMaterial(Materials.Oxygen, 1)
            .constructMaterial();
    }

    private static Materials loadMTBEMixtureAlt() {
        return new MaterialBuilder().setName("MTBEReactionMixture(Butane)")
            .setDefaultLocalName("MTBE Reaction Mixture (Butane)")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addGas()
            .addMaterial(Materials.Carbon, 5)
            .addMaterial(Materials.Hydrogen, 14)
            .addMaterial(Materials.Oxygen, 1)
            .constructMaterial();
    }

    private static Materials loadNitrousOxide() {
        return new MaterialBuilder().setName("NitrousOxide")
            .setDefaultLocalName("Nitrous Oxide")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x007dc8ff)
            .addCell()
            .addGas()
            .addMaterial(Materials.Nitrogen, 2)
            .addMaterial(Materials.Oxygen, 1)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadOctane() {
        return new MaterialBuilder().setName("Octane")
            .setDefaultLocalName("Octane")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setFuel(MaterialBuilder.FuelType.Diesel, 80)
            .addCell()
            .addFluid()
            .addMaterial(Materials.Carbon, 8)
            .addMaterial(Materials.Hydrogen, 18)
            .constructMaterial();
    }

    private static void loadAdded() {
        Materials.BloodInfusedIron = loadBloodInfusedIron();
        Materials.Electrotine = loadElectrotine();
        Materials.Galgadorian = loadGalgadorian();
        Materials.GalgadorianEnhanced = loadGalgadorianEnhanced();
        Materials.Shadow = loadShadow();
    }

    private static Materials loadBloodInfusedIron() {
        return new MaterialBuilder().setName("BloodInfusedIron")
            .setDefaultLocalName("Blood Infused Iron")
            .setChemicalFormula(CustomGlyphs.BRIMSTONE + "Fe")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setARGB(0x0045090a)
            .setTool(384, 2, 10.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2_400)
            .addAspect(TCAspects.METALLUM, 3)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadElectrotine() {
        return new MaterialBuilder().setName("Electrotine")
            .setDefaultLocalName("Electrotine")
            .setChemicalFormula("Rp")
            .setIconSet(TextureSet.SET_ELECTROTINE)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x003cb4c8)
            .addDustItems()
            .addOreItems()
            .setOreMultiplier(5)
            .addMaterial(Materials.Redstone, 1)
            .addMaterial(Materials.Electrum, 1)
            .addAspect(TCAspects.ELECTRUM, 2)
            .addOreByproduct(() -> Materials.Redstone)
            .addOreByproduct(() -> Materials.Electrum)
            .addOreByproduct(() -> Materials.Diamond)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .addSubTag(SubTag.STONE)
            .addSubTag(SubTag.UNBURNABLE)
            .addOrePrefix(OrePrefixes.plate)
            .constructMaterial();
    }

    private static Materials loadGalgadorian() {
        return new MaterialBuilder().setName("Galgadorian")
            .setDefaultLocalName("Galgadorian")
            .setChemicalFormula("???C₉Nh₃Fe₂(C(MgFeSi₂O₈)₈)")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setARGB(0x009a6977)
            .setTool(3_600, 3, 16.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3_000)
            .setBlastFurnaceTemp(3_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial();
    }

    private static Materials loadGalgadorianEnhanced() {
        return new MaterialBuilder().setName("EnhancedGalgadorian")
            .setDefaultLocalName("Enhanced Galgadorian")
            .setChemicalFormula("???C₉Nh₃Fe₂(C(MgFeSi₂O₈)₈)")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setARGB(0x00985d85)
            .setTool(7_200, 5, 32.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(4_500)
            .setBlastFurnaceTemp(4_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial();
    }

    private static Materials loadShadow() {
        return new MaterialBuilder().setName("Shadow")
            .setDefaultLocalName("Shadow Metal")
            .setChemicalFormula("Sh₆(FeMa₃)₂")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00100342)
            .setTool(8_192, 4, 32.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_800)
            .setBlastFurnaceTemp(1_800)
            .setBlastFurnaceRequired(true)
            .setDensity(4, 3)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static void loadGalaxySpace() {
        Materials.BlackPlutonium = loadBlackPlutonium();
        Materials.CallistoIce = loadCallistoIce();
        Materials.Duralumin = loadDuralumin();
        Materials.Ledox = loadLedox();
        Materials.MysteriousCrystal = loadMysteriousCrystal();
        Materials.Mytryl = loadMytryl();
        Materials.Quantium = loadQuantium();
    }

    private static Materials loadBlackPlutonium() {
        return new MaterialBuilder().setName("BlackPlutonium")
            .setDefaultLocalName("Black Plutonium")
            .setChemicalFormula("SpPu")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00323232)
            .setTool(8_192, 8, 36.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(9_000)
            .setBlastFurnaceTemp(9_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadCallistoIce() {
        return new MaterialBuilder().setName("CallistoIce")
            .setDefaultLocalName("Callisto Ice")
            .setChemicalFormula("SpH₂O")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x001eb1ff)
            .setTool(1_024, 4, 9.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadDuralumin() {
        return new MaterialBuilder().setName("Duralumin")
            .setDefaultLocalName("Duralumin")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ebd1a0)
            .setTool(512, 3, 16.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_600)
            .setBlastFurnaceTemp(1_600)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Aluminium, 6)
            .addMaterial(Materials.Copper, 1)
            .addMaterial(Materials.Manganese, 1)
            .addMaterial(Materials.Magnesium, 1)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadLedox() {
        return new MaterialBuilder().setName("Ledox")
            .setDefaultLocalName("Ledox")
            .setChemicalFormula("SpPb")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x000074ff)
            .setTool(1_024, 4, 15.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadMysteriousCrystal() {
        return new MaterialBuilder().setName("MysteriousCrystal")
            .setDefaultLocalName("Mysterious Crystal")
            .setChemicalFormula(CustomGlyphs.CIRCLE_STAR)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x0016856c)
            .setTool(256, 6, 8.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(7_200)
            .setBlastFurnaceTemp(7_200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static Materials loadMytryl() {
        return new MaterialBuilder().setName("Mytryl")
            .setDefaultLocalName("Mytryl")
            .setChemicalFormula("SpPt₂FeMa")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00f26404)
            .setTool(512, 4, 8.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3_600)
            .setBlastFurnaceTemp(3_600)
            .setBlastFurnaceRequired(true)
            .addOreByproduct(() -> Materials.Samarium)
            .addOreByproduct(() -> Materials.Zinc)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadQuantium() {
        return new MaterialBuilder().setName("Quantium")
            .setDefaultLocalName("Quantium")
            .setChemicalFormula("Qt")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLime)
            .setARGB(0x0000d10b)
            .setTool(2_048, 4, 18.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(9_900)
            .setBlastFurnaceTemp(9_900)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static void loadUnclassified07() {
        Materials.AstralSilver = loadAstralSilver();
        Materials.BlueAlloy = loadBlueAlloy();
        Materials.ClayCompound = loadClayCompound();
        Materials.Enderium = loadEnderium();
        Materials.Mithril = loadMithril();
        Materials.ShadowIron = loadShadowIron();
        Materials.ShadowSteel = loadShadowSteel();
        Materials.Soularium = loadSoularium();
    }

    private static Materials loadAstralSilver() {
        return new MaterialBuilder().setName("AstralSilver")
            .setDefaultLocalName("Astral Silver")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00e6e6ff)
            .setTool(64, 2, 10.0f)
            .setToolEnchantment(() -> EnchantmentEnderDamage.INSTANCE, 5)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Silver, 2)
            .addMaterial(Materials.Thaumium, 1)
            .addOreByproduct(() -> Materials.Silver)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.WASHING_MERCURY)
            .constructMaterial();
    }

    private static Materials loadBlueAlloy() {
        return new MaterialBuilder().setName("BlueAlloy")
            .setDefaultLocalName("Blue Alloy")
            .setChemicalFormula("AgRp₄")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x0064b4ff)
            .addDustItems()
            .addMetalItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Silver, 1)
            .addMaterial(Materials.Electrotine, 4)
            .addAspect(TCAspects.ELECTRUM, 3)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadClayCompound() {
        return new MaterialBuilder().setName("CrudeSteel")
            .setDefaultLocalName("Clay Compound")
            .setIconSet(TextureSet.SET_CRUDE_STEEL)
            .setColor(Dyes.dyeGray)
            .setARGB(0x009e9087)
            .setTool(64, 2, 2.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1_000)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Stone, 1)
            .addMaterial(Materials.Clay, 1)
            .addMaterial(Materials.Flint, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadEnderium() {
        return new MaterialBuilder().setName("Enderium")
            .setDefaultLocalName("Enderium")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x00599187)
            .setTool(1_500, 3, 8.0f)
            .setToolEnchantment(() -> Enchantment.silkTouch, 1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(4_500)
            .setBlastFurnaceTemp(4_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.EnderiumBase, 2)
            .addMaterial(Materials.Thaumium, 1)
            .addMaterial(Materials.EnderPearl, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ALIENIS, 1)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadMithril() {
        return new MaterialBuilder().setName("Mithril")
            .setDefaultLocalName("Mithril")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0x00ffffd2)
            .setTool(64, 2, 32.0f)
            .setToolEnchantment(() -> Enchantment.fortune, 3)
            .setTurbine(22.0f, 1.0f, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .setMeltingPoint(6_600)
            .setBlastFurnaceTemp(6_600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Platinum, 2)
            .addMaterial(Materials.Thaumium, 1)
            .addOreByproduct(() -> Materials.Platinum)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.WASHING_MERCURY)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadShadowIron() {
        return new MaterialBuilder().setName("ShadowIron")
            .setDefaultLocalName("Shadow Iron")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00787878)
            .setTool(10_240, 2, 32.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 3)
            .setTurbine(1.0f, 76.0f, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .setMeltingPoint(8_400)
            .setBlastFurnaceTemp(8_400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Thaumium, 3)
            .addOreByproduct(() -> Materials.Iron)
            .addSubTag(SubTag.BLASTFURNACE_CALCITE_TRIPLE)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .constructMaterial();
    }

    private static Materials loadShadowSteel() {
        return new MaterialBuilder().setName("ShadowSteel")
            .setDefaultLocalName("Shadow Steel")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x005a5a5a)
            .setTool(768, 4, 6.0f)
            .setToolEnchantment(() -> Enchantment.sharpness, 4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .setBlastFurnaceTemp(1_700)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Steel, 1)
            .addMaterial(Materials.Thaumium, 3)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadSoularium() {
        return new MaterialBuilder().setName("Soularium")
            .setDefaultLocalName("Soularium")
            .setIconSet(TextureSet.SET_DARKSTEEL)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00916d3e)
            .setTool(256, 2, 8.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(800)
            .setBlastFurnaceTemp(1_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.SoulSand, 1)
            .addMaterial(Materials.Gold, 1)
            .addMaterial(Materials.Ash, 1)
            .constructMaterial();
    }

    private static void loadOverpoweredMaterials() {
        Materials.Bedrockium = loadBedrockium();
        Materials.CosmicNeutronium = loadCosmicNeutronium();
        Materials.Ichorium = loadIchorium();
        Materials.Infinity = loadInfinity();
        Materials.InfinityCatalyst = loadInfinityCatalyst();
        Materials.Trinium = loadTrinium();
    }

    private static Materials loadBedrockium() {
        return new MaterialBuilder().setName("Bedrockium")
            .setDefaultLocalName("Bedrockium")
            .setIconSet(TextureSet.SET_BEDROCKIUM)
            .setColor(Dyes.dyeBlack)
            .addOreItems()
            .addDustItems()
            .addPlasma()
            .addMetalItems()
            .setTool(327_680, 9, 8f)
            .setBlastFurnaceRequired(true)
            .setBlastFurnaceTemp(9_900)
            .setMeltingPoint(9_900)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addMaterial(Materials.SiliconDioxide, 26_244)
            .addMaterial(Materials.Diamond, 9)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSMUTABLE_NUGGETS)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_EV);
    }

    private static Materials loadCosmicNeutronium() {
        return new MaterialBuilder().setName("CosmicNeutronium")
            .setDefaultLocalName("Cosmic Neutronium")
            .setChemicalFormula("SpNt")
            .setIconSet(TextureSet.SET_COSMIC_NEUTRONIUM)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00323237)
            .setTool(163_840, 12, 96.0f)
            .setTurbine(6.0f, 6.0f, 3.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(9_900)
            .setBlastFurnaceTemp(9_900)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_ZPM);
    }

    private static Materials loadIchorium() {
        return new MaterialBuilder().setName("Ichorium")
            .setDefaultLocalName("Ichorium")
            .setChemicalFormula("IcMa")
            .setFlavorText("Fabric of planar coalescence")
            .setIconSet(TextureSet.SET_ICHORIUM)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00d37806)
            .setTool(850_000, 12, 32.0f)
            .setToolEnchantment(() -> Enchantment.smite, 8)
            .setTurbine(6.0f, 6.0f, 3.0f)
            .setFuel(MaterialBuilder.FuelType.Magic, 250_000)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(9_000)
            .setBlastFurnaceTemp(9_000)
            .setBlastFurnaceRequired(true)
            .addSubTag(SubTag.METAL)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial();
    }

    private static Materials loadInfinity() {
        return new MaterialBuilder().setName("Infinity")
            .setDefaultLocalName("Infinity")
            .setChemicalFormula("If*")
            .setFlavorText("The fury of the universe in the palm of your hand")
            .setIconSet(TextureSet.SET_INFINITY)
            .setColor(Dyes.dyeLightGray)
            .setTool(2_621_440, 17, 256.0f)
            .setFuel(MaterialBuilder.FuelType.Magic, 5_000_000)
            .addDustItems()
            .addMetalItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(10_800)
            .setBlastFurnaceTemp(10_800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UV);
    }

    private static Materials loadInfinityCatalyst() {
        return new MaterialBuilder().setName("InfinityCatalyst")
            .setDefaultLocalName("Infinity Catalyst")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setTool(1_310_720, 10, 64.0f)
            .setFuel(MaterialBuilder.FuelType.Magic, 500_000)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(10_800)
            .setBlastFurnaceTemp(10_800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UV);
    }

    private static Materials loadTrinium() {
        return new MaterialBuilder().setName("Trinium")
            .setDefaultLocalName("Trinium")
            .setChemicalFormula("Ke")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00c8c8d2)
            .setTool(51_200, 8, 128.0f)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(7_200)
            .setBlastFurnaceTemp(7_200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.MULTI_PLATE)
            .constructMaterial();
    }

    private static void loadSuperconductorBases() {
        Materials.SuperconductorMVBase = loadSuperconductorMVBase();
        Materials.SuperconductorHVBase = loadSuperconductorHVBase();
        Materials.SuperconductorEVBase = loadSuperconductorEVBase();
        Materials.SuperconductorIVBase = loadSuperconductorIVBase();
        Materials.SuperconductorLuVBase = loadSuperconductorLuVBase();
        Materials.SuperconductorZPMBase = loadSuperconductorZPMBase();
        Materials.SuperconductorUVBase = loadSuperconductorUVBase();
        Materials.SuperconductorUHVBase = loadSuperconductorUHVBase();
        Materials.SuperconductorUEVBase = loadSuperconductorUEVBase();
        Materials.SuperconductorUIVBase = loadSuperconductorUIVBase();
        Materials.SuperconductorUMVBase = loadSuperconductorUMVBase();
    }

    private static Materials loadSuperconductorMVBase() {
        return new MaterialBuilder().setName("Pentacadmiummagnesiumhexaoxid")
            .setDefaultLocalName("Superconductor Base MV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00555555)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(2_500)
            .setBlastFurnaceTemp(2_500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Cadmium, 5)
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Oxygen, 6)
            .addAspect(TCAspects.ELECTRUM, 3)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadSuperconductorHVBase() {
        return new MaterialBuilder().setName("Titaniumonabariumdecacoppereikosaoxid")
            .setDefaultLocalName("Superconductor Base HV")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00331900)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(3_300)
            .setBlastFurnaceTemp(3_300)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Titanium, 1)
            .addMaterial(Materials.Barium, 9)
            .addMaterial(Materials.Copper, 10)
            .addMaterial(Materials.Oxygen, 20)
            .addAspect(TCAspects.ELECTRUM, 6)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadSuperconductorEVBase() {
        return new MaterialBuilder().setName("Uraniumtriplatinid")
            .setDefaultLocalName("Superconductor Base EV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00008700)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(4_400)
            .setBlastFurnaceTemp(4_400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Uranium, 1)
            .addMaterial(Materials.Platinum, 3)
            .addAspect(TCAspects.ELECTRUM, 9)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadSuperconductorIVBase() {
        return new MaterialBuilder().setName("Vanadiumtriindinid")
            .setDefaultLocalName("Superconductor Base IV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeMagenta)
            .setARGB(0x00330033)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(5_200)
            .setBlastFurnaceTemp(5_200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Vanadium, 1)
            .addMaterial(Materials.Indium, 3)
            .addAspect(TCAspects.ELECTRUM, 12)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadSuperconductorLuVBase() {
        return new MaterialBuilder().setName("Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid")
            .setDefaultLocalName("Superconductor Base LuV")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00994c00)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(6_000)
            .setBlastFurnaceTemp(6_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Indium, 4)
            .addMaterial(Materials.Tin, 2)
            .addMaterial(Materials.Barium, 2)
            .addMaterial(Materials.Titanium, 1)
            .addMaterial(Materials.Copper, 7)
            .addMaterial(Materials.Oxygen, 14)
            .addAspect(TCAspects.ELECTRUM, 15)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadSuperconductorZPMBase() {
        return new MaterialBuilder().setName("Tetranaquadahdiindiumhexaplatiumosminid")
            .setDefaultLocalName("Superconductor Base ZPM")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000a0a0a)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(8_100)
            .setBlastFurnaceTemp(8_100)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Naquadah, 4)
            .addMaterial(Materials.Indium, 2)
            .addMaterial(Materials.Palladium, 6)
            .addMaterial(Materials.Osmium, 1)
            .addAspect(TCAspects.ELECTRUM, 18)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadSuperconductorUVBase() {
        return new MaterialBuilder().setName("Longasssuperconductornameforuvwire")
            .setDefaultLocalName("Superconductor Base UV")
            .setChemicalFormula("Nq*₄(Ir₃Os)₃EuSm")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00e0d207)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(9_900)
            .setBlastFurnaceTemp(9_900)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Naquadria, 4)
            .addMaterial(Materials.Osmiridium, 3)
            .addMaterial(Materials.Europium, 1)
            .addMaterial(Materials.Samarium, 1)
            .addAspect(TCAspects.ELECTRUM, 21)
            .addSubTag(SubTag.METAL)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_LuV);
    }

    private static Materials loadSuperconductorUHVBase() {
        return new MaterialBuilder().setName("Longasssuperconductornameforuhvwire")
            .setDefaultLocalName("Superconductor Base UHV")
            .setChemicalFormula("D₆(SpNt)₇Tn₅Am₆")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x002681bd)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(10_800)
            .setBlastFurnaceTemp(10_800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Draconium, 6)
            .addMaterial(Materials.CosmicNeutronium, 7)
            .addMaterial(Materials.Tritanium, 5)
            .addMaterial(Materials.Americium, 6)
            .addAspect(TCAspects.ELECTRUM, 24)
            .addSubTag(SubTag.METAL)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_ZPM);
    }

    private static Materials loadSuperconductorUEVBase() {
        return new MaterialBuilder().setName("SuperconductorUEVBase")
            .setDefaultLocalName("Superconductor Base UEV")
            .setChemicalFormula("D*₅If*₅(✦◆✦)(⚷⚙⚷Ni4Ti6)")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00ae0808)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(11_700)
            .setBlastFurnaceTemp(11_800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.ELECTRUM, 27)
            .addSubTag(SubTag.METAL)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UV);
    }

    private static Materials loadSuperconductorUIVBase() {
        return new MaterialBuilder().setName("SuperconductorUIVBase")
            .setDefaultLocalName("Superconductor Base UIV")
            .setChemicalFormula("(C₁₄Os₁₁O₇Ag₃SpH₂O)₄?₁" + CustomGlyphs.SUBSCRIPT0 + "(Fs⚶)₆(⌘☯☯⌘)₅")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00e558b1)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(12_700)
            .setBlastFurnaceTemp(12_700)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.ELECTRUM, 34)
            .addSubTag(SubTag.METAL)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UHV);
    }

    private static Materials loadSuperconductorUMVBase() {
        return new MaterialBuilder().setName("SuperconductorUMVBase")
            .setDefaultLocalName("Superconductor Base UMV")
            .setChemicalFormula(
                "?₆Or₃(Hy⚶)₁₁(((CW)₇Ti₃)₃" + CustomGlyphs.FIRE + CustomGlyphs.EARTH + CustomGlyphs.CHAOS + ")₅۞₂")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00b526cd)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(13_600)
            .setBlastFurnaceTemp(13_600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.ELECTRUM, 40)
            .addSubTag(SubTag.METAL)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static void loadSuperconductors() {
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

    private static void loadWaterLineChemicals() {
        Materials.ActivatedCarbon = loadActivatedCarbon();
        Materials.PreActivatedCarbon = loadPreActivatedCarbon();
        Materials.DirtyActivatedCarbon = loadDirtyActivatedCarbon();
        Materials.PolyAluminiumChloride = loadPolyAluminiumChloride();
        Materials.Ozone = loadOzone();
        Materials.StableBaryonicMatter = loadStableBaryonicMatter();
    }

    private static Materials loadActivatedCarbon() {
        return new MaterialBuilder().setName("ActivatedCarbon")
            .setDefaultLocalName("Activated Carbon")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00141414)
            .addDustItems()
            .setAutoGeneratedRecycleRecipes(false)
            .addMaterial(Materials.Carbon, 1)
            .constructMaterial();
    }

    private static Materials loadPreActivatedCarbon() {
        return new MaterialBuilder().setName("PreActivatedCarbon")
            .setDefaultLocalName("Pre-Activated Carbon")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x000f3341)
            .addDustItems()
            .setAutoGeneratedRecycleRecipes(false)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.PhosphoricAcid, 1)
            .constructMaterial();
    }

    private static Materials loadDirtyActivatedCarbon() {
        return new MaterialBuilder()
            // don't change this to the more sensible name or a centrifuge recipe appears
            .setName("carbonactivateddirty")
            .setDefaultLocalName("Dirty Activated Carbon")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x006e6e6e)
            .addDustItems()
            .setAutoGeneratedRecycleRecipes(false)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.PhosphoricAcid, 1)
            .constructMaterial();
    }

    private static Materials loadPolyAluminiumChloride() {
        return new MaterialBuilder().setName("PolyaluminiumChloride")
            .setDefaultLocalName("Polyaluminium Chloride")
            .setChemicalFormula("Al₂(OH)₃Cl₃")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00fcec05)
            .addFluid()
            .addCell()
            .constructMaterial();
    }

    private static Materials loadOzone() {
        return new MaterialBuilder().setName("Ozone")
            .setDefaultLocalName("Ozone")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00bef4fa)
            .addGas()
            .addCell()
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadStableBaryonicMatter() {
        return new MaterialBuilder().setName("stablebaryonicmatter")
            .setDefaultLocalName("Stabilised Baryonic Matter")
            .setIconSet(TextureSet.SET_SBM)
            .setARGB(0x00ffffff)
            .addCell()
            .addFluid()
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static void loadRadoxLine() {
        Materials.RadoxCracked = loadRadoxCracked();
        Materials.RadoxGas = loadRadoxGas();
        Materials.RadoxHeavy = loadRadoxHeavy();
        Materials.RadoxLight = loadRadoxLight();
        Materials.RadoxPolymer = loadRadoxPolymer();
        Materials.RadoxRaw = loadRadoxRaw();
        Materials.RadoxSuperHeavy = loadRadoxSuperHeavy();
        Materials.RadoxSuperLight = loadRadoxSuperLight();
        Materials.Xenoxene = loadXenoxene();
        Materials.XenoxeneDiluted = loadXenoxeneDiluted();
    }

    private static Materials loadRadoxCracked() {
        return new MaterialBuilder().setName("CrackedRadox")
            .setDefaultLocalName("Cracked Radox")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00b482b4)
            .addGas()
            .constructMaterial();
    }

    private static Materials loadRadoxGas() {
        return new MaterialBuilder().setName("RadoxGas")
            .setDefaultLocalName("Radox Gas")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00ff82ff)
            .addGas()
            .constructMaterial();
    }

    private static Materials loadRadoxHeavy() {
        return new MaterialBuilder().setName("HeavyRadox")
            .setDefaultLocalName("Heavy Radox")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00730073)
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadRadoxLight() {
        return new MaterialBuilder().setName("LightRadox")
            .setDefaultLocalName("Light Radox")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x008c008c)
            .addGas()
            .constructMaterial();
    }

    private static Materials loadRadoxPolymer() {
        return new MaterialBuilder().setName("RadoxPoly")
            .setDefaultLocalName("Radox Polymer")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00850080)
            .setTool(600000, 3, 8.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addCell()
            .addGas()
            .setMeltingPoint(6_203)
            .addMaterial(Materials.Carbon, 14)
            .addMaterial(Materials.Osmium, 11)
            .addMaterial(Materials.Oxygen, 7)
            .addMaterial(Materials.Silver, 3)
            .addMaterial(Materials.CallistoIce, 1)
            .addAspect(TCAspects.HUMANUS, 2)
            .addSubTag(SubTag.SOFT)
            .removeOrePrefix(OrePrefixes.cell) // non molten cell is useless
            .constructMaterial()
            .setGasTemperature(12_406);
    }

    private static Materials loadRadoxRaw() {
        return new MaterialBuilder().setName("RawRadox")
            .setDefaultLocalName("Raw Radox")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00501e50)
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadRadoxSuperHeavy() {
        return new MaterialBuilder().setName("SuperHeavyRadox")
            .setDefaultLocalName("Super Heavy Radox")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00640064)
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadRadoxSuperLight() {
        return new MaterialBuilder().setName("SuperLightRadox")
            .setDefaultLocalName("Super Light Radox")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x009b009b)
            .addGas()
            .constructMaterial();
    }

    private static Materials loadXenoxene() {
        return new MaterialBuilder().setName("Xenoxene")
            .setDefaultLocalName("Xenoxene")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00858280)
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadXenoxeneDiluted() {
        return new MaterialBuilder().setName("DilutedXenoxene")
            .setDefaultLocalName("Diluted Xenoxene")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00cec8c4)
            .addFluid()
            .constructMaterial();
    }

    private static void loadNetheriteLine() {
        Materials.NetherAir = loadNetherAir();
        Materials.NetherSemiFluid = loadNethersemifluid();
        Materials.NefariousGas = loadNefariousGas();
        Materials.NefariousOil = loadNefariousOil();
        Materials.PoorNetherWaste = loadPoorNetherWaste();
        Materials.RichNetherWaste = loadRichNetherWaste();
        Materials.HellishMetal = loadHellishMetal();
        Materials.Netherite = loadNetherite();
        Materials.Netherite.remove(SubTag.SMELTING_TO_FLUID);
        Materials.ActivatedNetherite = loadActivatedNetherite();
    }

    private static Materials loadNetherAir() {
        return new MaterialBuilder().setName("netherair")
            .setDefaultLocalName("Nether Air")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00eea39a)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadNethersemifluid() {
        return new MaterialBuilder().setName("nethersemifluid")
            .setDefaultLocalName("Nether Semifluid")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00dac172)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadNefariousGas() {
        return new MaterialBuilder().setName("nefariousgas")
            .setDefaultLocalName("Nefarious Gas")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00300a05)
            .setFuel(MaterialBuilder.FuelType.Gas, 1200)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadNefariousOil() {
        return new MaterialBuilder().setName("nefariousoil")
            .setDefaultLocalName("Nefarious Oil")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00391616)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 256)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadPoorNetherWaste() {
        return new MaterialBuilder().setName("poornetherwaste")
            .setDefaultLocalName("Poor Nether Waste")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00a0827e)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadRichNetherWaste() {
        return new MaterialBuilder().setName("richnetherwaste")
            .setDefaultLocalName("Rich Nether Waste")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00f9827e)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadHellishMetal() {
        return new MaterialBuilder().setName("HellishMetal")
            .setDefaultLocalName("Hellish Metal")
            .setChemicalFormula("RhMa")
            .setIconSet(TextureSet.SET_FIERY)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0xffaaaaaa)
            .addMetalItems()
            .setMeltingPoint(1_200)
            .setBlastFurnaceTemp(1_900)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.TRANSPARENT)
            .removeOrePrefix(OrePrefixes.itemCasing)
            .removeOrePrefix(OrePrefixes.nugget)
            .constructMaterial();
    }

    private static Materials loadNetherite() {
        return new MaterialBuilder().setName("Netherite")
            .setDefaultLocalName("Netherite")
            .setChemicalFormula("NrAuMa*")
            .setIconSet(TextureSet.SET_NETHERITE)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0xffffffff)
            .addMetalItems()
            .addGearItems()
            .setMeltingPoint(1_200)
            .setBlastFurnaceTemp(1_900)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .setAutoGeneratedRecycleRecipes(false)
            .addSubTag(SubTag.NO_RECIPES)
            .addSubTag(SubTag.TRANSPARENT)
            .removeOrePrefix(OrePrefixes.nugget)
            .removeOrePrefix(OrePrefixes.spring)
            .removeOrePrefix(OrePrefixes.springSmall)
            .removeOrePrefix(OrePrefixes.sheetmetal)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_ZPM);
    }

    private static Materials loadActivatedNetherite() {
        return new MaterialBuilder().setName("activatednetherite")
            .setDefaultLocalName("Activated Netherite")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x009c575a)
            .addFluid()
            .addCell()
            .constructMaterial();
    }

    private static void loadPrismaticAcidLine() {
        Materials.PrismarineSolution = loadPrismarineSolution();
        Materials.PrismarineContaminatedHydrogenPeroxide = loadPrismarinecontaminatedhydrogenperoxide();
        Materials.PrismarineRichNitrobenzeneSolution = loadPrismarinerichnitrobenzenesolution();
        Materials.PrismarineContaminatedNitrobenzeSolution = loadPrismarinecontaminatednitrobenzenesolution();
        Materials.PrismaticGas = loadPrismaticGas();
        Materials.PrismaticAcid = loadPrismaticAcid();
        Materials.PrismaticNaquadah = loadPrismaticNaquadah();
        Materials.PrismaticNaquadahCompositeSlurry = loadPrismaticNaquadahCompositeSlurry();
    }

    private static Materials loadPrismarineSolution() {
        return new MaterialBuilder().setName("prismarinesolution")
            .setDefaultLocalName("Prismarine Solution")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00559a8a)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadPrismarinecontaminatedhydrogenperoxide() {
        return new MaterialBuilder().setName("prismarinecontaminatedhydrogenperoxide")
            .setDefaultLocalName("Prismarine-Contaminated Hydrogen Peroxide")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00445f59)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadPrismarinerichnitrobenzenesolution() {
        return new MaterialBuilder().setName("prismarinerichnitrobenzenesolution")
            .setDefaultLocalName("Prismarine-Rich Nitrobenzene Solution")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x005d763f)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadPrismarinecontaminatednitrobenzenesolution() {
        return new MaterialBuilder().setName("prismarinecontaminatednitrobenzenesolution")
            .setDefaultLocalName("Prismarine-Contaminated Nitrobenzene Solution")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x002f331e)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadPrismaticGas() {
        return new MaterialBuilder().setName("prismaticgas")
            .setDefaultLocalName("Prismatic Gas")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x0076babd)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadPrismaticAcid() {
        return new MaterialBuilder().setName("prismaticacid")
            .setDefaultLocalName("Prismatic Acid")
            .setIconSet(TextureSet.SET_PRISMATIC_ACID)
            .setARGB(0x00ffffff)
            .addCell()
            .addFluid()
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadPrismaticNaquadah() {
        return new MaterialBuilder().setName("prismaticnaquadah")
            .setDefaultLocalName("Prismatic Naquadah")
            .setChemicalFormula(Materials.Naquadah.mChemicalFormula + "\u0394")
            .setFlavorText("Absorbs all radiation")
            .setIconSet(TextureSet.SET_METALLIC)
            .setARGB(0x00373737)
            .addDustItems()
            .addMetalItems()
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_ZPM);
    }

    private static Materials loadPrismaticNaquadahCompositeSlurry() {
        return new MaterialBuilder().setName("prismaticnaquadahcompositeslurry")
            .setDefaultLocalName("Prismatic Naquadah Composite Slurry")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x004b4b4b)
            .addCell()
            .addFluid()
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static void loadFranciumLine() {
        Materials.CrudeFrancium = loadCrudeFrancium();
        Materials.DepletedUraniumResidue = loadDepletedUraniumResidue();
        Materials.FranciumHydroxide = loadFranciumHydroxide();
        Materials.UraniumInfusedAcidicSolution = loadUraniumInfusedAcidicSolution();
    }

    private static Materials loadCrudeFrancium() {
        return new MaterialBuilder().setName("CrudeFrancium")
            .setDefaultLocalName("Crude Francium")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeRed)
            .setARGB(0x004a180a)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDepletedUraniumResidue() {
        return new MaterialBuilder().setName("DepletedUraniumResidue")
            .setDefaultLocalName("Depleted Uranium Residue")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x001d4733)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadFranciumHydroxide() {
        return new MaterialBuilder().setName("FranciumHydroxide")
            .setDefaultLocalName("Francium Hydroxide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePink)
            .setARGB(0x00f2653d)
            .addDustItems()
            .addMaterial(Materials.Francium, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .constructMaterial();
    }

    private static Materials loadUraniumInfusedAcidicSolution() {
        return new MaterialBuilder().setName("UraniumInfusedAcidicSolution")
            .setDefaultLocalName("Uranium Infused Acidic Solution")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x0014f78a)
            .addCell()
            .addFluid()
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static void loadMagicMaterials() {
        Materials.ComplexityCatalyst = loadComplexityCatalyst();
        Materials.EntropicCatalyst = loadEntropicCatalyst();
        Materials.SoulInfusedMedium = loadSoulInfusedMedium();
    }

    private static Materials loadComplexityCatalyst() {
        return new MaterialBuilder().setName("ComplexityCatalyst")
            .setDefaultLocalName("Complexity Catalyst")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x008b93a9)
            .addFluid()
            .addCell()
            .constructMaterial();
    }

    private static Materials loadEntropicCatalyst() {
        return new MaterialBuilder().setName("EntropicCatalyst")
            .setDefaultLocalName("Entropic Catalyst")
            .setIconSet(TextureSet.SET_DULL)
            .setARGB(0x00a99da5)
            .addFluid()
            .addCell()
            .constructMaterial();
    }

    private static Materials loadSoulInfusedMedium() {
        return new MaterialBuilder().setName("SoulInfusedMedium")
            .setDefaultLocalName("Soul Infused Medium")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0xff32cd32)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static void loadBotaniaMaterials() {
        Materials.Manasteel = loadManasteel();
        Materials.Terrasteel = loadTerrasteel();
        Materials.ElvenElementium = loadElvenElementium();
        Materials.Livingrock = loadLivingrock();
        Materials.GaiaSpirit = loadGaiaSpirit();
        Materials.Livingwood = loadLivingwood();
        Materials.Dreamwood = loadDreamwood();
        Materials.ManaDiamond = loadManaDiamond();
        Materials.Dragonstone = loadDragonstone();
    }

    private static Materials loadManasteel() {
        return new MaterialBuilder().setName("Manasteel")
            .setDefaultLocalName("Manasteel")
            .setChemicalFormula("Ms")
            .setIconSet(TextureSet.SET_MANASTEEL)
            .setARGB(0xff46aae6)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setTool(5_120, 4, 8.0f)
            .setMeltingPoint(1_500)
            .setBlastFurnaceTemp(1_500)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 3)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .removeOrePrefix(OrePrefixes.ingot) // Botania:manaResource:0
            .removeOrePrefix(OrePrefixes.nugget) // Botania:manaResource:17
            .constructMaterial();
    }

    private static Materials loadTerrasteel() {
        return new MaterialBuilder().setName("Terrasteel")
            .setDefaultLocalName("Terrasteel")
            .setChemicalFormula("Tr")
            .setIconSet(TextureSet.SET_MANASTEEL)
            .setARGB(0xff46c800)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setTool(10_240, 5, 32.0f)
            .setMeltingPoint(5_400)
            .setBlastFurnaceTemp(5_400)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .removeOrePrefix(OrePrefixes.ingot) // Botania:manaResource:4
            .removeOrePrefix(OrePrefixes.nugget) // Botania:manaResource:18
            .constructMaterial();
    }

    private static Materials loadElvenElementium() {
        return new MaterialBuilder().setName("ElvenElementium")
            .setDefaultLocalName("Elven Elementium")
            .setChemicalFormula("Ef")
            .setIconSet(TextureSet.SET_MANASTEEL)
            .setARGB(0xffff2df0)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setTool(32_768, 7, 20.0f)
            .setMeltingPoint(7_200)
            .setBlastFurnaceTemp(7_200)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 3)
            .addAspect(TCAspects.PRAECANTATIO, 2)
            .addAspect(TCAspects.AURAM, 1)
            .removeOrePrefix(OrePrefixes.ingot) // Botania:manaResource:7
            .removeOrePrefix(OrePrefixes.nugget) // Botania:manaResource:19
            .constructMaterial();
    }

    private static Materials loadLivingrock() {
        return new MaterialBuilder().setName("Livingrock")
            .setDefaultLocalName("Livingrock")
            .setChemicalFormula("Lv")
            .setIconSet(TextureSet.SET_LIVINGROCK)
            .addDustItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.TERRA, 2)
            .addAspect(TCAspects.VICTUS, 2)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addOrePrefix(OrePrefixes.plate)
            .addOrePrefix(OrePrefixes.rod) // this is not working
            .removeOrePrefix(OrePrefixes.rotor)
            .constructMaterial();
    }

    private static Materials loadGaiaSpirit() {
        return new MaterialBuilder().setName("GaiaSpirit")
            .setDefaultLocalName("Gaia Spirit")
            .setChemicalFormula("Gs")
            .setIconSet(TextureSet.SET_GAIA_SPIRIT)
            .setTool(850_000, 12, 32.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addSubTag(SubTag.SOFT)
            .addSubTag(SubTag.TRANSPARENT)
            .removeOrePrefix(OrePrefixes.ingot) // Botania::manaResource:14
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial();
    }

    private static Materials loadLivingwood() {
        return new MaterialBuilder().setName("Livingwood")
            .setDefaultLocalName("Livingwood")
            .setChemicalFormula("Lw")
            .setIconSet(TextureSet.SET_LIVINGWOOD)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.ARBOR, 4)
            .addAspect(TCAspects.VICTUS, 2)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.WOOD)
            .removeOrePrefix(OrePrefixes.ingot)
            .removeOrePrefix(OrePrefixes.nugget)
            .constructMaterial();
    }

    private static Materials loadDreamwood() {
        return new MaterialBuilder().setName("Dreamwood")
            .setDefaultLocalName("Dreamwood")
            .setChemicalFormula("Dw")
            .setIconSet(TextureSet.SET_DREAMWOOD)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.ARBOR, 4)
            .addAspect(TCAspects.AURAM, 2)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.WOOD)
            .removeOrePrefix(OrePrefixes.ingot)
            .removeOrePrefix(OrePrefixes.nugget)
            .constructMaterial();
    }

    private static Materials loadManaDiamond() {
        return new MaterialBuilder().setName("ManaDiamond")
            .setDefaultLocalName("Mana Diamond")
            .setChemicalFormula("Ma₄C")
            .setIconSet(TextureSet.SET_MANA_DIAMOND)
            .setTool(2_560, 8, 16.0f)
            .addDustItems()
            .addGemItems()
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .removeOrePrefix(OrePrefixes.gem) // Botania:manaResource:2
            .constructMaterial();
    }

    private static Materials loadDragonstone() {
        return new MaterialBuilder().setName("BotaniaDragonstone")
            .setDefaultLocalName("Dragonstone")
            .setChemicalFormula("Dg")
            .setIconSet(TextureSet.SET_DRAGONSTONE)
            .setTool(3_840, 12, 24.0f)
            .addDustItems()
            .addGemItems()
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .removeOrePrefix(OrePrefixes.gem) // Botania:manaResource:9
            .constructMaterial();
    }

    private static void loadKevlarLine() {
        Materials.DiphenylmethaneDiisocyanate = loadDiphenylmethaneDiisocyanate();
        Materials.DiaminodiphenylmethanMixture = loadDiaminodiphenylmethanMixture();
        Materials.DiphenylmethaneDiisocyanateMixture = loadDiphenylmethaneDiisocyanateMixture();
        Materials.Butyraldehyde = loadButyraldehyde();
        Materials.Isobutyraldehyde = loadIsobutyraldehyde();
        Materials.NickelTetracarbonyl = loadNickelTetracarbonyl();
        Materials.KevlarCatalyst = loadKevlarCatalyst();
        Materials.EthyleneOxide = loadEthyleneOxide();
        Materials.SiliconOil = loadSiliconOil();
        Materials.Ethyleneglycol = loadEthyleneglycol();
        Materials.Acetaldehyde = loadAcetaldehyde();
        Materials.Pentaerythritol = loadPentaerythritol();
        Materials.PolyurethaneResin = loadPolyurethaneResin();
        Materials.NMethylIIPyrrolidone = loadNMethylIIPyrrolidone();
        Materials.TerephthaloylChloride = loadTerephthaloylChloride();
        Materials.Acetylene = loadAcetylene();
        Materials.IVNitroaniline = loadIVNitroaniline();
        Materials.ParaPhenylenediamine = loadParaPhenylenediamine();
        Materials.Methylamine = loadMethylamine();
        Materials.Trimethylamine = loadTrimethylamine();
        Materials.GammaButyrolactone = loadGammaButyrolactone();
        Materials.CalciumCarbide = loadCalciumCarbide();
        Materials.LiquidCrystalKevlar = loadLiquidCrystalKevlar();
        Materials.IIButinIIVdiol = loadIIButinIIVdiol();
        Materials.NickelAluminide = loadNickelAluminide();
        Materials.RaneyNickelActivated = loadRaneyNickelActivated();
        Materials.BismuthIIIOxide = loadBismuthIIIOxide();
        Materials.ThionylChloride = loadThionylChloride();
        Materials.SulfurDichloride = loadSulfurDichloride();
        Materials.DimethylTerephthalate = loadDimethylTerephthalate();
        Materials.Kevlar = loadKevlar();
        Materials.TerephthalicAcid = loadTerephthalicAcid();
        Materials.IIIDimethylbenzene = loadIIIDimethylbenzene();
        Materials.IVDimethylbenzene = loadIVDimethylbenzene();
        Materials.CobaltIINaphthenate = loadCobaltIINaphthenate();
        Materials.NaphthenicAcid = loadNaphthenicAcid();
        Materials.CobaltIIHydroxide = loadCobaltIIHydroxide();
        Materials.CobaltIIAcetate = loadCobaltIIAcetate();
        Materials.CobaltIINitrate = loadCobaltIINitrate();
        Materials.OrganorhodiumCatalyst = loadOrganorhodiumCatalyst();
        Materials.SodiumBorohydride = loadSodiumBorohydride();
        Materials.RhodiumChloride = loadRhodiumChloride();
        Materials.Triphenylphosphene = loadTriphenylphosphene();
        Materials.PhosphorusTrichloride = loadPhosphorusTrichloride();
        Materials.SodiumHydride = loadSodiumHydride();
        Materials.TrimethylBorate = loadTrimethylBorate();
        Materials.SodiumMethoxide = loadSodiumMethoxide();
    }

    private static Materials loadDiphenylmethaneDiisocyanate() {
        return new MaterialBuilder().setName("DiphenylmethaneDiisocyanate")
            .setDefaultLocalName("4,4'-Diphenylmethane Diisocyanate")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffe632)
            .addDustItems()
            .setMeltingPoint(310)
            .addMaterial(Materials.Carbon, 15)
            .addMaterial(Materials.Hydrogen, 10)
            .addMaterial(Materials.Nitrogen, 2)
            .addMaterial(Materials.Oxygen, 2)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .constructMaterial();
    }

    private static Materials loadDiaminodiphenylmethanMixture() {
        return new MaterialBuilder().setName("DiaminodiphenylmethanMixture")
            .setDefaultLocalName("Diaminodiphenylmethane Mixture")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00fff37a)
            .addCell()
            .addFluid()
            .setMeltingPoint(365)
            .addMaterial(Materials.Carbon, 13)
            .addMaterial(Materials.Hydrogen, 14)
            .addMaterial(Materials.Nitrogen, 2)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .constructMaterial();
    }

    private static Materials loadDiphenylmethaneDiisocyanateMixture() {
        return new MaterialBuilder().setName("DiphenylmethaneDiisocyanateMixture")
            .setDefaultLocalName("Diphenylmethane Diisocyanate Mixture")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffe632)
            .addCell()
            .addFluid()
            .setMeltingPoint(310)
            .addMaterial(Materials.Carbon, 15)
            .addMaterial(Materials.Hydrogen, 10)
            .addMaterial(Materials.Nitrogen, 2)
            .addMaterial(Materials.Oxygen, 2)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .constructMaterial();
    }

    private static Materials loadButyraldehyde() {
        return new MaterialBuilder().setName("Butyraldehyde")
            .setDefaultLocalName("Butyraldehyde")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .setMeltingPoint(176)
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Hydrogen, 8)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.IGNIS, 1)
            .constructMaterial();
    }

    private static Materials loadIsobutyraldehyde() {
        return new MaterialBuilder().setName("Isobutyraldehyde")
            .setDefaultLocalName("Isobutyraldehyde")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .setMeltingPoint(208)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Hydrogen, 8)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.IGNIS, 1)
            .constructMaterial();
    }

    private static Materials loadNickelTetracarbonyl() {
        return new MaterialBuilder().setName("NickelTetracarbonyl")
            .setDefaultLocalName("Nickel Tetracarbonyl")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .setMeltingPoint(256)
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.Oxygen, 4)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.METALLUM, 1)
            .constructMaterial();
    }

    private static Materials loadKevlarCatalyst() {
        return new MaterialBuilder().setName("PolyurethaneCatalystADust")
            .setDefaultLocalName("Polyurethane Catalyst A")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00323232)
            .addDustItems()
            .setMeltingPoint(300)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.LUCRUM, 1)
            .constructMaterial();
    }

    private static Materials loadEthyleneOxide() {
        return new MaterialBuilder().setName("EthyleneOxide")
            .setDefaultLocalName("Ethylene Oxide")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addGas()
            .setMeltingPoint(160)
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 4)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.IGNIS, 1)
            .constructMaterial();
    }

    private static Materials loadSiliconOil() {
        return new MaterialBuilder().setName("SiliconOil")
            .setDefaultLocalName("Silicon Oil")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .setMeltingPoint(473)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.MACHINA, 1)
            .constructMaterial();
    }

    private static Materials loadEthyleneglycol() {
        return new MaterialBuilder().setName("EthyleneGlycol")
            .setDefaultLocalName("Ethylene Glycol")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .setMeltingPoint(260)
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 2)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.IGNIS, 1)
            .constructMaterial();
    }

    private static Materials loadAcetaldehyde() {
        return new MaterialBuilder().setName("Acetaldehyde")
            .setDefaultLocalName("Acetaldehyde")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addGas()
            .setMeltingPoint(150)
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 4)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.IGNIS, 1)
            .constructMaterial();
    }

    private static Materials loadPentaerythritol() {
        return new MaterialBuilder().setName("Pentaerythritol")
            .setDefaultLocalName("Pentaerythritol")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .addDustItems()
            .setMeltingPoint(533)
            .addMaterial(Materials.Carbon, 5)
            .addMaterial(Materials.Hydrogen, 12)
            .addMaterial(Materials.Oxygen, 4)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.LUCRUM, 1)
            .constructMaterial();
    }

    private static Materials loadPolyurethaneResin() {
        return new MaterialBuilder().setName("PolyurethaneResin")
            .setDefaultLocalName("Polyurethane Resin")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00e6e678)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadNMethylIIPyrrolidone() {
        return new MaterialBuilder().setName("NMethylpyrolidone")
            .setDefaultLocalName("N-Methyl-2-pyrrolidone")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .setMeltingPoint(249)
            .addMaterial(Materials.Carbon, 5)
            .addMaterial(Materials.Hydrogen, 9)
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .constructMaterial();
    }

    private static Materials loadTerephthaloylChloride() {
        return new MaterialBuilder().setName("TerephthaloylChloride")
            .setDefaultLocalName("Terephthaloyl Chloride")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x0000ff0c)
            .addDustItems()
            .setMeltingPoint(355)
            .addMaterial(Materials.Carbon, 8)
            .addMaterial(Materials.Hydrogen, 4)
            .addMaterial(Materials.Chlorine, 2)
            .addMaterial(Materials.Oxygen, 2)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.VITREUS, 1)
            .constructMaterial();
    }

    private static Materials loadAcetylene() {
        // TODO: Add to JUPITER Athmosphere and Enceladus and to moon of Saturn
        return new MaterialBuilder().setName("Acetylene")
            .setDefaultLocalName("Acetylene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addGas()
            .setMeltingPoint(192)
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 2)
            .constructMaterial();
    }

    private static Materials loadIVNitroaniline() {
        return new MaterialBuilder().setName("4Nitroaniline")
            .setDefaultLocalName("4-Nitroaniline")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff8733)
            .addCell()
            .addFluid()
            .setMeltingPoint(420)
            .addMaterial(Materials.Carbon, 6)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Nitrogen, 2)
            .addMaterial(Materials.Oxygen, 2)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.VITREUS, 1)
            .constructMaterial();
    }

    private static Materials loadParaPhenylenediamine() {
        return new MaterialBuilder().setName("pPhenylenediamine")
            .setDefaultLocalName("para-Phenylenediamine")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00fbec5d)
            .addDustItems()
            .setMeltingPoint(293)
            .addMaterial(Materials.Carbon, 6)
            .addMaterial(Materials.Hydrogen, 8)
            .addMaterial(Materials.Nitrogen, 2)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.VITREUS, 1)
            .constructMaterial();
    }

    private static Materials loadMethylamine() {
        return new MaterialBuilder().setName("Methylamine")
            .setDefaultLocalName("Methylamine")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00414469)
            .addCell()
            .addGas()
            .setMeltingPoint(180)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Hydrogen, 5)
            .addMaterial(Materials.Nitrogen, 1)
            .constructMaterial();
    }

    private static Materials loadTrimethylamine() {
        return new MaterialBuilder().setName("Trimethylamine")
            .setDefaultLocalName("Trimethylamine")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00694469)
            .addCell()
            .addGas()
            .setMeltingPoint(156)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Hydrogen, 9)
            .addMaterial(Materials.Nitrogen, 1)
            .constructMaterial();
    }

    private static Materials loadGammaButyrolactone() {
        return new MaterialBuilder().setName("GammaButyrolactone")
            .setDefaultLocalName("gamma-Butyrolactone")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff97)
            .addCell()
            .addFluid()
            .setMeltingPoint(229)
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadCalciumCarbide() {
        return new MaterialBuilder().setName("CacliumCarbide")
            .setDefaultLocalName("Calcium Carbide")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00ebebeb)
            .addDustItems()
            .setMeltingPoint(2430)
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Carbon, 2)
            .constructMaterial();
    }

    private static Materials loadLiquidCrystalKevlar() {
        return new MaterialBuilder().setName("LiquidCrystalKevlar")
            .setDefaultLocalName("Liquid Crystal Kevlar")
            .setChemicalFormula("[-CO-C₆H₄-CO-NH-C₆H₄-NH-]n")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00f0f078)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadIIButinIIVdiol() {
        return new MaterialBuilder().setName("2Butin14diol")
            .setDefaultLocalName("2-Butin-1,4-diol")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00f7f7b4)
            .addDustItems()
            .setMeltingPoint(331)
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadNickelAluminide() {
        return new MaterialBuilder().setName("NickelAluminide")
            .setDefaultLocalName("Nickel Aluminide")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00e6e6e6)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(1_668)
            .setBlastFurnaceTemp(1_668)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.Aluminium, 3)
            .constructMaterial();
    }

    private static Materials loadRaneyNickelActivated() {
        return new MaterialBuilder().setName("RaneyNickelActivated")
            .setDefaultLocalName("Raney Nickel")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00e6e6e6)
            .addDustItems()
            .setMeltingPoint(1_955)
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.Aluminium, 1)
            .constructMaterial();
    }

    private static Materials loadBismuthIIIOxide() {
        return new MaterialBuilder().setName("BismuthIIIOxide")
            .setDefaultLocalName("Bismuth Oxide")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00323232)
            .addDustItems()
            .setMeltingPoint(1090)
            .addMaterial(Materials.Bismuth, 2)
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadThionylChloride() {
        // SOCl2
        return new MaterialBuilder().setName("ThionylChloride")
            .setDefaultLocalName("Thionyl Chloride")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadSulfurDichloride() {
        // SCl2
        return new MaterialBuilder().setName("SulfurDichloride")
            .setDefaultLocalName("Sulfur Dichloride")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00c80000)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadDimethylTerephthalate() {
        return new MaterialBuilder().setName("DimethylTerephthalate")
            .setDefaultLocalName("Dimethyl Terephthalate")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .setMeltingPoint(415)
            .addMaterial(Materials.Carbon, 10)
            .addMaterial(Materials.Hydrogen, 10)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadKevlar() {
        return new MaterialBuilder().setName("Kevlar")
            .setDefaultLocalName("Kevlar")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00f0f078)
            .addDustItems()
            .addMetalItems()
            .addGearItems()
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.MULTI_PLATE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.STRETCHY)
            .constructMaterial();
    }

    private static Materials loadTerephthalicAcid() {
        return new MaterialBuilder().setName("TerephthalicAcid")
            .setDefaultLocalName("Terephthalic Acid")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .setMeltingPoint(480)
            .addMaterial(Materials.Carbon, 8)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadIIIDimethylbenzene() {
        return new MaterialBuilder().setName("1,3Dimethylbenzene")
            .setDefaultLocalName("1,3-Dimethylbenzene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x0070924a)
            .addCell()
            .addFluid()
            .setMeltingPoint(225)
            .addMaterial(Materials.Carbon, 8)
            .addMaterial(Materials.Hydrogen, 10)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadIVDimethylbenzene() {
        return new MaterialBuilder().setName("1,4Dimethylbenzene")
            .setDefaultLocalName("1,4-Dimethylbenzene")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x007a8854)
            .addCell()
            .addFluid()
            .setMeltingPoint(286)
            .addMaterial(Materials.Carbon, 8)
            .addMaterial(Materials.Hydrogen, 10)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadCobaltIINaphthenate() {
        return new MaterialBuilder().setName("Cobalt(II)Naphthenate")
            .setDefaultLocalName("Cobalt II Naphthenate")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x008f5f27)
            .addDustItems()
            .setMeltingPoint(413)
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.Carbon, 22)
            .addMaterial(Materials.Hydrogen, 14)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadNaphthenicAcid() {
        return new MaterialBuilder().setName("NaphthenicAcid")
            .setDefaultLocalName("Naphthenic Acid")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 80)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadCobaltIIHydroxide() {
        return new MaterialBuilder().setName("CobaltIIHydroxide")
            .setDefaultLocalName("Cobalt II Hydroxide")
            .setChemicalFormula("Co(OH)₂")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00e58cef)
            .addDustItems()
            .setMeltingPoint(441)
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadCobaltIIAcetate() {
        return new MaterialBuilder().setName("Cobalt(II)Acetate")
            .setDefaultLocalName("Cobalt II Acetate")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00dba2e5)
            .addDustItems()
            .setMeltingPoint(413)
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadCobaltIINitrate() {
        return new MaterialBuilder().setName("Cobalt(II)Nitrate")
            .setDefaultLocalName("Cobalt II Nitrate")
            .setChemicalFormula("Co(NO₃)₂")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00aa0000)
            .addDustItems()
            .setMeltingPoint(373)
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.Nitrogen, 2)
            .addMaterial(Materials.Oxygen, 6)
            .constructMaterial();
    }

    private static Materials loadOrganorhodiumCatalyst() {
        return new MaterialBuilder().setName("OrganorhodiumCatalyst")
            .setDefaultLocalName("Organorhodium Catalyst")
            .setChemicalFormula("RhHCO(P(C₆H₅)₃)₃")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00aa0000)
            .addDustItems()
            .setMeltingPoint(373)
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.NitricAcid, 2)
            .constructMaterial();
    }

    private static Materials loadSodiumBorohydride() {
        return new MaterialBuilder().setName("SodiumBorohydride")
            .setDefaultLocalName("Sodium Borohydride")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeWhite)
            .addDustItems()
            .setMeltingPoint(673)
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Boron, 1)
            .addMaterial(Materials.Hydrogen, 4)
            .constructMaterial();
    }

    private static Materials loadRhodiumChloride() {
        return new MaterialBuilder().setName("RhodiumChloride")
            .setDefaultLocalName("Rhodium Chloride")
            .setChemicalFormula("RhCl₃")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00800000)
            .addDustItems()
            .setMeltingPoint(723)
            .constructMaterial();
    }

    private static Materials loadTriphenylphosphene() {
        return new MaterialBuilder().setName("Triphenylphosphene")
            .setDefaultLocalName("Triphenylphosphine")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeWhite)
            .addDustItems()
            .setMeltingPoint(353)
            .addMaterial(Materials.Carbon, 18)
            .addMaterial(Materials.Hydrogen, 15)
            .addMaterial(Materials.Phosphorus, 1)
            .constructMaterial();
    }

    private static Materials loadPhosphorusTrichloride() {
        return new MaterialBuilder().setName("PhosphorusTrichloride")
            .setDefaultLocalName("Phosphorus Trichloride")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .setMeltingPoint(179)
            .addMaterial(Materials.Phosphorus, 1)
            .addMaterial(Materials.Chlorine, 3)
            .constructMaterial();
    }

    private static Materials loadSodiumHydride() {
        return new MaterialBuilder().setName("SodiumHydride")
            .setDefaultLocalName("Sodium Hydride")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00c0c0c0)
            .addDustItems()
            .setMeltingPoint(911)
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .constructMaterial();
    }

    private static Materials loadTrimethylBorate() {
        return new MaterialBuilder().setName("TrimethylBorate")
            .setDefaultLocalName("Trimethyl Borate")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .addFluid()
            .setMeltingPoint(239)
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Hydrogen, 9)
            .addMaterial(Materials.Boron, 1)
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadSodiumMethoxide() {
        return new MaterialBuilder().setName("SodiumMethoxide")
            .setDefaultLocalName("Sodium Methoxide")
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeWhite)
            .addDustItems()
            .setMeltingPoint(400)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Hydrogen, 3)
            .addMaterial(Materials.Oxygen, 1)
            .addMaterial(Materials.Sodium, 1)
            .constructMaterial();
    }

    private static void loadAluminiumOres() {
        Materials.BauxiteSlurry = loadBauxiteSlurry();
        Materials.HeatedBauxiteSlurry = loadHeatedBauxiteSlurry();
        Materials.SluiceJuice = loadSluiceJuice();
        Materials.SluiceSand = loadSluiceSand();
        Materials.BauxiteSlag = loadBauxiteSlag();
        Materials.IlmeniteSlag = loadIlmeniteSlag();
        Materials.GreenSapphireJuice = loadGreenSapphireJuice();
        Materials.SapphireJuice = loadSapphireJuice();
        Materials.RubyJuice = loadRubyJuice();
    }

    private static Materials loadBauxiteSlurry() {
        return new MaterialBuilder().setName("BauxiteSlurry")
            .setDefaultLocalName("Bauxite Slurry")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x002543a8)
            .addCell()
            .addFluid()
            .setMeltingPoint(295)
            .constructMaterial();
    }

    private static Materials loadHeatedBauxiteSlurry() {
        return new MaterialBuilder().setName("HeadedBauxiteSlurry")
            .setDefaultLocalName("Heated Bauxite Slurry")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00375cd4)
            .addCell()
            .addFluid()
            .setMeltingPoint(295)
            .constructMaterial();
    }

    private static Materials loadSluiceJuice() {
        return new MaterialBuilder().setName("SluiceJuice")
            .setDefaultLocalName("Sluice Juice")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGray)
            .setARGB(0x005c3c24)
            .addCell()
            .addFluid()
            .setMeltingPoint(295)
            .constructMaterial();
    }

    private static Materials loadSluiceSand() {
        return new MaterialBuilder().setName("SluiceSand")
            .setDefaultLocalName("Sluice Sand")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00a5a578)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadBauxiteSlag() {
        return new MaterialBuilder().setName("BauxiteSlag")
            .setDefaultLocalName("Bauxite Slag")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeRed)
            .setARGB(0x006e1f1f)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadIlmeniteSlag() {
        return new MaterialBuilder().setName("IlmeniteSlag")
            .setDefaultLocalName("Ilmenite Slag")
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00a32626)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadGreenSapphireJuice() {
        return new MaterialBuilder().setName("GreenSapphireJuice")
            .setDefaultLocalName("Green Sapphire Juice")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x0064c882)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadSapphireJuice() {
        return new MaterialBuilder().setName("SapphireJuice")
            .setDefaultLocalName("Sapphire Juice")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x006464c8)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadRubyJuice() {
        return new MaterialBuilder().setName("RubyJuice")
            .setDefaultLocalName("Ruby Juice")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00ff6464)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static void loadUEVPlusMaterials() {
        Materials.DTCC = loadDTCC();
        Materials.DTPC = loadDTPC();
        Materials.DTRC = loadDTRC();
        Materials.DTEC = loadDTEC();
        Materials.DTSC = loadDTSC();
        Materials.ExcitedDTCC = loadExcitedDTCC();
        Materials.ExcitedDTPC = loadExcitedDTPC();
        Materials.ExcitedDTRC = loadExcitedDTRC();
        Materials.ExcitedDTEC = loadExcitedDTEC();
        Materials.ExcitedDTSC = loadExcitedDTSC();
        Materials.DTR = loadDTR();
        Materials.SpaceTime = loadSpaceTime();
        Materials.TranscendentMetal = loadTranscendentMetal();
        Materials.MHDCSM = loadMHDCSM();
        Materials.RawStarMatter = loadRawStarMatter();
        Materials.WhiteDwarfMatter = loadWhiteDwarfMatter();
        Materials.BlackDwarfMatter = loadBlackDwarfMatter();
        Materials.Time = loadTime();
        Materials.Space = loadSpace();
        Materials.Universium = loadUniversium();
        Materials.Eternity = loadEternity();
        Materials.PrimordialMatter = loadPrimordialMatter();
        Materials.MagMatter = loadMagMatter();
        Materials.QuarkGluonPlasma = loadQuarkGluonPlasma();
        Materials.PhononMedium = loadPhononMedium();
        Materials.PhononCrystalSolution = loadPhononCrystalSolution();
        Materials.SixPhasedCopper = loadSixPhasedCopper();
        Materials.Mellion = loadMellion();
        Materials.Creon = loadCreon();
        Materials.GravitonShard = loadGravitonShard();
        Materials.DimensionallyShiftedSuperfluid = loadDimensionallyShiftedSuperfluid();
        Materials.MoltenProtoHalkoniteBase = loadMoltenProtoHalkoniteBase();
        Materials.HotProtoHalkonite = loadHotProtoHalkonite();
        Materials.ProtoHalkonite = loadProtoHalkonite();
        Materials.MoltenExoHalkoniteBase = loadMoltenExoHalkoniteBase();
        Materials.HotExoHalkonite = loadHotExoHalkonite();
        Materials.ExoHalkonite = loadExoHalkonite();
        Materials.Antimatter = loadAntimatter();
        Materials.Protomatter = loadProtomatter();
        Materials.StargateCrystalSlurry = loadStargateCrystalSlurry();
        Materials.LumipodExtract = loadLumipodExtract();
        Materials.BiocatalyzedPropulsionFluid = loadBiocatalyzedPropulsionFluid();
    }

    private static Materials loadDTCC() {
        return new MaterialBuilder().setName("DimensionallyTranscendentCrudeCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Crude Catalyst")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x010a1414)
            .addCell()
            .addFluid()
            .setMeltingPoint(25_000_000)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadDTPC() {
        return new MaterialBuilder().setName("DimensionallyTranscendentProsaicCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Prosaic Catalyst")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x010a1414)
            .addCell()
            .addFluid()
            .setMeltingPoint(50_000_000)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadDTRC() {
        return new MaterialBuilder().setName("DimensionallyTranscendentResplendentCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Resplendent Catalyst")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x010a1414)
            .addCell()
            .addFluid()
            .setMeltingPoint(75_000_000)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadDTEC() {
        return new MaterialBuilder().setName("DimensionallyTranscendentExoticCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Exotic Catalyst")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeMagenta)
            .setARGB(0x010a1414)
            .addCell()
            .addFluid()
            .setMeltingPoint(100_000_000)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadDTSC() {
        return new MaterialBuilder().setName("DimensionallyTranscendentStellarCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Stellar Catalyst")
            .setChemicalFormula("Stellar")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x010a1414)
            .addCell()
            .addFluid()
            .setMeltingPoint(100_000_000)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadExcitedDTCC() {
        return new MaterialBuilder().setName("ExcitedDTCC")
            .setDefaultLocalName("Excited Dimensionally Transcendent Crude Catalyst")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x010a1414)
            .addCell()
            .setMeltingPoint(500_000_000)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadExcitedDTPC() {
        return new MaterialBuilder().setName("ExcitedDTPC")
            .setDefaultLocalName("Excited Dimensionally Transcendent Prosaic Catalyst")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x01233b29)
            .addCell()
            .setMeltingPoint(500_000_000)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadExcitedDTRC() {
        return new MaterialBuilder().setName("ExcitedDTRC")
            .setDefaultLocalName("Excited Dimensionally Transcendent Resplendent Catalyst")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x01261438)
            .addCell()
            .setMeltingPoint(500_000_000)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadExcitedDTEC() {
        return new MaterialBuilder().setName("ExcitedDTEC")
            .setDefaultLocalName("Excited Dimensionally Transcendent Exotic Catalyst")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeMagenta)
            .setARGB(0x01f0f029)
            .addCell()
            .setMeltingPoint(500_000_000)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadExcitedDTSC() {
        return new MaterialBuilder().setName("ExcitedDTSC")
            .setDefaultLocalName("Excited Dimensionally Transcendent Stellar Catalyst")
            .setChemicalFormula("[-Stellar-Stellar-]")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x017e4b0b)
            .addCell()
            .setMeltingPoint(500_000_000)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadDTR() {
        return new MaterialBuilder().setName("DimensionallyTranscendentResidue")
            .setDefaultLocalName("Dimensionally Transcendent Residue")
            .setChemicalFormula(CustomGlyphs.SPARKLES + "-" + CustomGlyphs.EMPTY_SET)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x01000000)
            .addCell()
            .setMeltingPoint(25)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadSpaceTime() {
        return new MaterialBuilder().setName("SpaceTime")
            .setDefaultLocalName("SpaceTime")
            .setChemicalFormula("\u03A6")
            .setFlavorText("Reality itself distilled into physical form")
            .setIconSet(TextureSet.SET_SPACETIME)
            .setARGB(0x00ffffff)
            .setTool(10_485_760, 25, 320.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadTranscendentMetal() {
        return new MaterialBuilder().setName("TranscendentMetal")
            .setDefaultLocalName("Transcendent Metal")
            .setChemicalFormula("TsЖ")
            .setFlavorText("Spatially incomprehensible")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x323232)
            .setTool(7864320, 22, 290.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(3_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .setDensity(1_000, 1_000)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.BLACK_HOLE)
            .addSubTag(SubTag.TRANSPARENT)
            .addOrePrefix(OrePrefixes.ingotHot)
            .addOrePrefix(OrePrefixes.nanite)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UHV);
    }

    private static Materials loadMHDCSM() {
        return new MaterialBuilder().setName("MagnetohydrodynamicallyConstrainedStarMatter")
            .setDefaultLocalName("Magnetohydrodynamically Constrained Star Matter")
            .setChemicalFormula(
                "⇲" + CustomGlyphs.ARROW_CORNER_SOUTH_EAST
                    + CustomGlyphs.GALAXY
                    + CustomGlyphs.ARROW_CORNER_NORTH_WEST
                    + "⇱")
            .setFlavorText("Stabilised core of a dead star")
            .setIconSet(TextureSet.SET_MHDCSM)
            .setARGB(0x00ffffff)
            .setTool(10485760, 25, 320.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.NO_RECIPES)
            .addSubTag(SubTag.NO_RECYCLING_RECIPES)
            .addSubTag(SubTag.TRANSPARENT)
            .addOrePrefix(OrePrefixes.gear)
            .addOrePrefix(OrePrefixes.ingot)
            .addOrePrefix(OrePrefixes.toolHeadHammer)
            .addOrePrefix(OrePrefixes.frame)
            .addOrePrefix(OrePrefixes.frameGt)
            .removeOrePrefix(OrePrefixes.cell)
            .removeOrePrefix(OrePrefixes.turbineBlade)
            .removeOrePrefix(OrePrefixes.dust)
            .removeOrePrefix(OrePrefixes.dustSmall)
            .removeOrePrefix(OrePrefixes.dustTiny)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UIV);
    }

    private static Materials loadRawStarMatter() {
        return new MaterialBuilder().setName("RawStarMatter")
            .setDefaultLocalName("Condensed Raw Stellar Plasma Mixture")
            .setChemicalFormula(CustomGlyphs.GALAXY)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePurple)
            .setARGB(0xff6401ff)
            .addCell()
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadWhiteDwarfMatter() {
        return new MaterialBuilder().setName("WhiteDwarfMatter")
            .setDefaultLocalName("White Dwarf Matter")
            .setChemicalFormula("∅")
            .setIconSet(TextureSet.SET_WHITE_DWARF_MATTER)
            .setColor(Dyes.dyePurple)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addFluid()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addOrePrefix(OrePrefixes.nanite)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadBlackDwarfMatter() {
        return new MaterialBuilder().setName("BlackDwarfMatter")
            .setDefaultLocalName("Black Dwarf Matter")
            .setChemicalFormula(">>∅<<")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePurple)
            .setARGB(0xff000000)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addFluid()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.TRANSPARENT)
            .addOrePrefix(OrePrefixes.nanite)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadTime() {
        return new MaterialBuilder().setName("temporalFluid")
            .setDefaultLocalName("Tachyon Rich Temporal Fluid")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePurple)
            .setARGB(0xff6401ff)
            .addCell()
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadSpace() {
        return new MaterialBuilder().setName("spatialFluid")
            .setDefaultLocalName("Spatially Enlarged Fluid")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePurple)
            .setARGB(0xff6401ff)
            .addCell()
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadUniversium() {
        return new MaterialBuilder().setName("Universium")
            .setDefaultLocalName("Universium")
            .setChemicalFormula("\u03A3" + EnumChatFormatting.OBFUSCATED + "X")
            .setFlavorText("A tear into the space beyond space")
            .setIconSet(TextureSet.SET_UNIVERSIUM)
            .setARGB(0xff263145)
            .setTool(10_485_760, 30, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSPARENT)
            .addOrePrefix(OrePrefixes.nanite)
            .removeOrePrefix(OrePrefixes.frame)
            .removeOrePrefix(OrePrefixes.frameGt) // disabled but shows up and is used in game
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UMV);
    }

    private static Materials loadEternity() {
        return new MaterialBuilder().setName("Eternity")
            .setDefaultLocalName("Eternity")
            .setChemicalFormula("En⦼")
            .setIconSet(TextureSet.SET_ETERNITY)
            .setTool(20_971_520, 26, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(14_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.METAL)
            .addOrePrefix(OrePrefixes.nanite)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UMV);
    }

    private static Materials loadPrimordialMatter() {
        return new MaterialBuilder().setName("PrimordialMatter")
            .setDefaultLocalName("Liquid Primordial Matter")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00ffffff)
            .addCell()
            .setMeltingPoint(2_000_000_000)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadMagMatter() {
        return new MaterialBuilder().setName("Magmatter")
            .setDefaultLocalName("Magmatter")
            .setChemicalFormula("M⎋")
            .setIconSet(TextureSet.SET_MAGMATTER)
            .setTool(167_772_160, 26, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(25_000)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.METAL)
            .addOrePrefix(OrePrefixes.nanite)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UMV);
    }

    private static Materials loadQuarkGluonPlasma() {
        return new MaterialBuilder().setName("QuarkGluonPlasma")
            .setDefaultLocalName("Degenerate Quark Gluon Plasma")
            .setChemicalFormula(
                EnumChatFormatting.OBFUSCATED + "X"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + "g"
                    + EnumChatFormatting.OBFUSCATED
                    + "X")
            .setFlavorText("Matter beyond structure")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00ffffff)
            .addCell()
            .setMeltingPoint(2_000_000_000)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadPhononMedium() {
        return new MaterialBuilder().setName("PhononMedium")
            .setDefaultLocalName("Lossless Phonon Transfer Medium")
            .setChemicalFormula(
                "((Si₅O₁" + CustomGlyphs.SUBSCRIPT0
                    + "Fe)₃(Bi₂Te₃)₄ZrO₂Fe₅"
                    + CustomGlyphs.SUBSCRIPT0
                    + "C)₅Og*Pr₁₅((C₁₄Os₁₁O₇Ag₃SpH₂O)₄?₁"
                    + CustomGlyphs.SUBSCRIPT0
                    + "(Fs⚶)₆(⌘☯☯⌘)₅)₆〄₄")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00ffffff)
            .addCell()
            .setMeltingPoint(500)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadPhononCrystalSolution() {
        return new MaterialBuilder().setName("PhononCrystalSolution")
            .setDefaultLocalName("Saturated Phononic Crystal Solution")
            .setChemicalFormula("〄")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00ffffff)
            .addCell()
            .setMeltingPoint(500)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadSixPhasedCopper() {
        return new MaterialBuilder().setName("SixPhasedCopper")
            .setDefaultLocalName("Six-Phased Copper")
            .setChemicalFormula("✢")
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x00ff7814)
            .setTool(20_971_520, 26, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_000)
            .setBlastFurnaceTemp(14_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.ITER, 1)
            .addOrePrefix(OrePrefixes.nanite)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadMellion() {
        return new MaterialBuilder().setName("Mellion")
            .setDefaultLocalName("Mellion")
            .setChemicalFormula(
                "Tn₁₁Or₈Rb₁₁(" + Materials.FierySteel.mChemicalFormula
                    + ")₇"
                    + Materials.Firestone.mChemicalFormula
                    + "₁₃?₁₃")
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x003c0505)
            .setTool(20_971_520, 26, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_000)
            .setBlastFurnaceTemp(14_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.SENSUS, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadCreon() {
        return new MaterialBuilder().setName("Creon")
            .setDefaultLocalName("Creon")
            .setChemicalFormula("⸎")
            .setIconSet(TextureSet.SET_SHINY)
            .setARGB(0x00460046)
            .setTool(20_971_520, 26, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1_000)
            .setBlastFurnaceTemp(14_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.SENSUS, 1)
            .addSubTag(SubTag.METAL)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadGravitonShard() {
        return new MaterialBuilder().setName("GravitonShard")
            .setDefaultLocalName("Graviton Shard")
            .setIconSet(TextureSet.SET_GRAVITON_SHARD)
            .setTool(20_971_520, 26, 1.0f)
            .setMeltingPoint(100_000)
            .setBlastFurnaceTemp(100_000)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.VACUOS, 150)
            .addOrePrefix(OrePrefixes.gem)
            .constructMaterial();
    }

    private static Materials loadDimensionallyShiftedSuperfluid() {
        return new MaterialBuilder().setName("dimensionallyshiftedsuperfluid")
            .setDefaultLocalName("Dimensionally Shifted Superfluid")
            .setIconSet(TextureSet.SET_DIMENSIONALLY_SHIFTED_SUPER_FLUID)
            .setARGB(0x00ffffff)
            .addCell()
            .addFluid()
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadMoltenProtoHalkoniteBase() {
        return new MaterialBuilder().setName("protohalkonitebase")
            .setDefaultLocalName("Molten Proto-Halkonite Steel Base")
            .setChemicalFormula(
                "(" + Materials.TranscendentMetal.mChemicalFormula
                    + ")₂"
                    + "(W₈Nq*₇("
                    + Materials.Bedrockium.mChemicalFormula
                    + ")₄C₄V₃SpPu)₂"
                    + Materials.Tartarite.mChemicalFormula
                    + "₂"
                    + "((CW)₇Ti₃)₃"
                    + CustomGlyphs.FIRE
                    + CustomGlyphs.EARTH
                    + CustomGlyphs.CHAOS
                    + "If*")
            .setIconSet(TextureSet.SET_PROTOHALKONITE_BASE)
            .setARGB(0x00ffffff)
            .addFluid()
            .addCell()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .setAutoGeneratedRecycleRecipes(false)
            .addSubTag(SubTag.TRANSPARENT)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial();
    }

    private static Materials loadHotProtoHalkonite() {
        return new MaterialBuilder().setName("hotprotohalkonite")
            .setDefaultLocalName("Hot Proto-Halkonite Steel")
            .setChemicalFormula(Materials.MoltenProtoHalkoniteBase.mChemicalFormula)
            .setIconSet(TextureSet.SET_HOT_PROTOHALKONITE)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .setAutoGeneratedRecycleRecipes(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.NO_RECIPES)
            .addSubTag(SubTag.NO_RECYCLING_RECIPES)
            .addOrePrefix(OrePrefixes.gear)
            .addOrePrefix(OrePrefixes.ingot)
            .addOrePrefix(OrePrefixes.toolHeadHammer)
            .addOrePrefix(OrePrefixes.frame)
            .addOrePrefix(OrePrefixes.frameGt)
            .removeOrePrefix(OrePrefixes.cell)
            .removeOrePrefix(OrePrefixes.cellMolten)
            .removeOrePrefix(OrePrefixes.turbineBlade)
            .removeOrePrefix(OrePrefixes.nugget)
            .removeOrePrefix(OrePrefixes.dust)
            .removeOrePrefix(OrePrefixes.dustSmall)
            .removeOrePrefix(OrePrefixes.dustTiny)
            .removeOrePrefix(OrePrefixes.spring)
            .removeOrePrefix(OrePrefixes.springSmall)
            .removeOrePrefix(OrePrefixes.itemCasing)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadProtoHalkonite() {
        return new MaterialBuilder().setName("protohalkonite")
            .setDefaultLocalName("Proto-Halkonite Steel")
            .setChemicalFormula(Materials.MoltenProtoHalkoniteBase.mChemicalFormula)
            .setFlavorText("Forged to be indestructible, probably")
            .setIconSet(TextureSet.SET_PROTOHALKONITE)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .setAutoGeneratedRecycleRecipes(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.NO_RECIPES)
            .addSubTag(SubTag.NO_RECYCLING_RECIPES)
            .addOrePrefix(OrePrefixes.gear)
            .addOrePrefix(OrePrefixes.ingot)
            .addOrePrefix(OrePrefixes.toolHeadHammer)
            .addOrePrefix(OrePrefixes.frame)
            .addOrePrefix(OrePrefixes.frameGt)
            .removeOrePrefix(OrePrefixes.cell)
            .removeOrePrefix(OrePrefixes.cellMolten)
            .removeOrePrefix(OrePrefixes.turbineBlade)
            .removeOrePrefix(OrePrefixes.nugget)
            .removeOrePrefix(OrePrefixes.dust)
            .removeOrePrefix(OrePrefixes.dustSmall)
            .removeOrePrefix(OrePrefixes.dustTiny)
            .removeOrePrefix(OrePrefixes.spring)
            .removeOrePrefix(OrePrefixes.springSmall)
            .removeOrePrefix(OrePrefixes.itemCasing)
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadMoltenExoHalkoniteBase() {
        return new MaterialBuilder().setName("moltenexohalkonitebase")
            .setDefaultLocalName("Molten Exo-Halkonite Steel Preparation Base")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x001e1e1e)
            .addFluid()
            .addCell()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .setAutoGeneratedRecycleRecipes(false)
            .constructMaterial();
    }

    private static Materials loadHotExoHalkonite() {
        return new MaterialBuilder().setName("hotexohalkonite")
            .setDefaultLocalName("Hot Exo-Halkonite Steel")
            .setIconSet(TextureSet.SET_HOT_EXOHALKONITE)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .setAutoGeneratedRecycleRecipes(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.NO_RECIPES)
            .addSubTag(SubTag.NO_RECYCLING_RECIPES)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .addOrePrefix(OrePrefixes.gear)
            .addOrePrefix(OrePrefixes.ingot)
            .addOrePrefix(OrePrefixes.toolHeadHammer)
            .addOrePrefix(OrePrefixes.frame)
            .addOrePrefix(OrePrefixes.frameGt)
            .removeOrePrefix(OrePrefixes.cell)
            .removeOrePrefix(OrePrefixes.cellMolten)
            .removeOrePrefix(OrePrefixes.turbineBlade)
            .removeOrePrefix(OrePrefixes.nugget)
            .removeOrePrefix(OrePrefixes.dust)
            .removeOrePrefix(OrePrefixes.dustSmall)
            .removeOrePrefix(OrePrefixes.dustTiny)
            .removeOrePrefix(OrePrefixes.spring)
            .removeOrePrefix(OrePrefixes.springSmall)
            .removeOrePrefix(OrePrefixes.itemCasing)
            .removeOrePrefix(OrePrefixes.plateSuperdense) // when Exo Halkonite is added, remove this.
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadExoHalkonite() {
        return new MaterialBuilder().setName("exohalkonite")
            .setDefaultLocalName("Exo-Halkonite Steel")
            .setIconSet(TextureSet.SET_EXOHALKONITE)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .setAutoGeneratedRecycleRecipes(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.NO_RECIPES)
            .addSubTag(SubTag.NO_RECYCLING_RECIPES)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
            .addOrePrefix(OrePrefixes.gear)
            .addOrePrefix(OrePrefixes.ingot)
            .addOrePrefix(OrePrefixes.toolHeadHammer)
            .addOrePrefix(OrePrefixes.frame)
            .addOrePrefix(OrePrefixes.frameGt)
            .removeOrePrefix(OrePrefixes.cell)
            .removeOrePrefix(OrePrefixes.cellMolten)
            .removeOrePrefix(OrePrefixes.turbineBlade)
            .removeOrePrefix(OrePrefixes.nugget)
            .removeOrePrefix(OrePrefixes.dust)
            .removeOrePrefix(OrePrefixes.dustSmall)
            .removeOrePrefix(OrePrefixes.dustTiny)
            .removeOrePrefix(OrePrefixes.spring)
            .removeOrePrefix(OrePrefixes.springSmall)
            .removeOrePrefix(OrePrefixes.itemCasing)
            .removeOrePrefix(OrePrefixes.plateSuperdense) // when Exo Halkonite is added, remove this.
            .removeOrePrefix(OrePrefixes.sheetmetal) // no custom texture set for this. remove when implemented.
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadAntimatter() {
        return new MaterialBuilder().setName("Antimatter")
            .setDefaultLocalName("Semi-Stable Antimatter")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00ffffff)
            .addCell()
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadProtomatter() {
        return new MaterialBuilder().setName("Protomatter")
            .setDefaultLocalName("Protomatter")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00ffffff)
            .addCell()
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadStargateCrystalSlurry() {
        return new MaterialBuilder().setName("sgcrystalslurry")
            .setDefaultLocalName("Stargate Crystal Slurry")
            .setIconSet(TextureSet.SET_SG_CRYSTAL_SLURRY)
            .addFluid()
            .addCell()
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_MAX);
    }

    private static Materials loadLumipodExtract() {
        return new MaterialBuilder().setName("BrightLumipodExtract")
            .setDefaultLocalName("Bright Lumipod Extract")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x00d7e6bb)
            .addFluid()
            .addCell()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .setAutoGeneratedRecycleRecipes(false)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial()
            .setLiquidTemperature(298);
    }

    private static Materials loadBiocatalyzedPropulsionFluid() {
        return new MaterialBuilder().setName("BiocatalyzedPropulsionFluid")
            .setDefaultLocalName("Biocatalyzed Propulsion Fluid")
            .setChemicalFormula("ඞ")
            .setIconSet(TextureSet.SET_FLUID)
            .setARGB(0x002d1f4d)
            .addFluid()
            .addCell()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .setAutoGeneratedRecycleRecipes(false)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial()
            .setLiquidTemperature(303);
    }

    private static void loadGTNHMaterials() {
        Materials.Signalum = loadSignalum();
        Materials.Lumium = loadLumium();
        Materials.Prismarine = loadPrismarine();
        Materials.AquaRegia = loadAquaRegia();
        Materials.SolutionBlueVitriol = loadSolutionBlueVitriol();
        Materials.SolutionNickelSulfate = loadSolutionNickelSulfate();
        Materials.Lodestone = loadLodestone();
        Materials.Luminite = loadLuminite();
        Materials.Chlorite = loadChlorite();
        Materials.Staurolite = loadStaurolite();
        Materials.Cordierite = loadCordierite();
        Materials.Datolite = loadDatolite();
        Materials.MetamorphicMineralMixture = loadMetamorphicMineralMixture();
        Materials.Plagioclase = loadPlagioclase();
        Materials.Epidote = loadEpidote();
    }

    private static Materials loadSignalum() {
        return new MaterialBuilder().setName("Signalum")
            .setDefaultLocalName("Signalum")
            .addDustItems()
            .addMetalItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadLumium() {
        return new MaterialBuilder().setName("Lumium")
            .setDefaultLocalName("Lumium")
            .addDustItems()
            .addMetalItems()
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadPrismarine() {
        return new MaterialBuilder().setName("Prismarine")
            .setDefaultLocalName("Prismarine")
            .addSubTag(SubTag.NO_ORE_PROCESSING)
            .constructMaterial();
    }

    private static Materials loadAquaRegia() {
        return new MaterialBuilder().setName("AquaRegia")
            .setDefaultLocalName("Aqua Regia")
            .constructMaterial();
    }

    private static Materials loadSolutionBlueVitriol() {
        return new MaterialBuilder().setName("SolutionBlueVitriol")
            .setDefaultLocalName("Blue Vitriol Solution")
            .constructMaterial();
    }

    private static Materials loadSolutionNickelSulfate() {
        return new MaterialBuilder().setName("SolutionNickelSulfate")
            .setDefaultLocalName("Nickel Sulfate Solution")
            .constructMaterial();
    }

    private static Materials loadLodestone() {
        return new MaterialBuilder().setName("Lodestone")
            .setDefaultLocalName("Lodestone")
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadLuminite() {
        return new MaterialBuilder().setName("Luminite")
            .setDefaultLocalName("Luminite")
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fafafa)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadChlorite() {
        return new MaterialBuilder().setName("Chlorite")
            .setDefaultLocalName("Chlorite")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLime)
            .addDustItems()
            .setARGB(0x00607d6c)
            .addMaterial(Materials.Iron, 3)
            .addMaterial(Materials.Magnesium, 2)
            .addMaterial(Materials.Aluminiumoxide, 1)
            .addMaterial(Materials.SiliconDioxide, 3)
            .addMaterial(Materials.Water, 4)
            .addMaterial(Materials.Oxygen, 5)
            .constructMaterial();
    }

    private static Materials loadStaurolite() {
        return new MaterialBuilder().setName("Staurolite")
            .setDefaultLocalName("Staurolite")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .addDustItems()
            .setARGB(0x003f2816)
            .addMaterial(Materials.Iron, 2)
            .addMaterial(Materials.Aluminium, 9)
            .addMaterial(Materials.SiliconDioxide, 4)
            .addMaterial(Materials.Oxygen, 16)
            .addMaterial(Materials.Hydrogen, 1)
            .constructMaterial();
    }

    private static Materials loadCordierite() {
        return new MaterialBuilder().setName("Cordierite")
            .setDefaultLocalName("Cordierite")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00434b82)
            .addDustItems()
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.SiliconDioxide, 5)
            .addMaterial(Materials.Aluminiumoxide, 1)
            .addMaterial(Materials.Oxygen, 5)
            .constructMaterial();
    }

    private static Materials loadDatolite() {
        return new MaterialBuilder().setName("Datolite")
            .setDefaultLocalName("Datolite")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00eac4ce)
            .addDustItems()
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Boron, 1)
            .addMaterial(Materials.SiliconDioxide, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addMaterial(Materials.Hydrogen, 1)
            .constructMaterial();
    }

    private static Materials loadMetamorphicMineralMixture() {
        return new MaterialBuilder().setName("MetamorphicMineralMixture")
            .setDefaultLocalName("Metamorphic Mineral Mixture")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x006c8294)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadPlagioclase() {
        return new MaterialBuilder().setName("Plagioclase")
            .setDefaultLocalName("Plagioclase")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00c1bab2)
            .addDustItems()
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 8)
            .constructMaterial();
    }

    private static Materials loadEpidote() {
        return new MaterialBuilder().setName("Epidote")
            .setDefaultLocalName("Epidote")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLime)
            .setARGB(0x008cb121)
            .addDustItems()
            .addMaterial(Materials.Calcium, 2)
            .addMaterial(Materials.Aluminium, 3)
            .addMaterial(Materials.SiliconDioxide, 3)
            .addMaterial(Materials.Oxygen, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .constructMaterial();
    }
}
