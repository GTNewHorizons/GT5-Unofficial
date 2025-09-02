package gregtech.api.enums;

public class MaterialsKevlar {

    public static Materials DiphenylmethaneDiisocyanate;
    public static Materials DiaminodiphenylmethanMixture;
    public static Materials DiphenylmethaneDiisocyanateMixture;
    public static Materials Butyraldehyde;
    public static Materials Isobutyraldehyde;
    public static Materials NickelTetracarbonyl;
    public static Materials KevlarCatalyst;
    public static Materials EthyleneOxide;
    public static Materials SiliconOil;
    public static Materials Ethyleneglycol;
    public static Materials Acetaldehyde;
    public static Materials Pentaerythritol;
    public static Materials PolyurethaneResin;
    public static Materials NMethylIIPyrrolidone;
    public static Materials TerephthaloylChloride;
    public static Materials Acetylene;
    public static Materials IVNitroaniline;
    public static Materials ParaPhenylenediamine;
    public static Materials Methylamine;
    public static Materials Trimethylamine;
    public static Materials GammaButyrolactone;
    public static Materials CalciumCarbide;
    public static Materials LiquidCrystalKevlar;
    public static Materials IIButinIIVdiol;
    public static Materials NickelAluminide;
    public static Materials RaneyNickelActivated;
    public static Materials BismuthIIIOxide;
    public static Materials ThionylChloride;
    public static Materials SulfurDichloride;
    public static Materials DimethylTerephthalate;
    public static Materials Kevlar;
    public static Materials TerephthalicAcid;
    public static Materials IIIDimethylbenzene;
    public static Materials IVDimethylbenzene;
    public static Materials CobaltIINaphthenate;
    public static Materials NaphthenicAcid;
    public static Materials CobaltIIHydroxide;
    public static Materials CobaltIIAcetate;
    public static Materials CobaltIINitrate;
    public static Materials OrganorhodiumCatalyst;
    public static Materials SodiumBorohydride;
    public static Materials RhodiumChloride;
    public static Materials Triphenylphosphene;
    public static Materials PhosphorusTrichloride;
    public static Materials SodiumHydride;
    public static Materials TrimethylBorate;
    public static Materials SodiumMethoxide;

    public static void load() {
        MaterialsKevlar.DiphenylmethaneDiisocyanate = loadDiphenylmethaneDiisocyanate();
        MaterialsKevlar.DiaminodiphenylmethanMixture = loadDiaminodiphenylmethanMixture();
        MaterialsKevlar.DiphenylmethaneDiisocyanateMixture = loadDiphenylmethaneDiisocyanateMixture();
        MaterialsKevlar.Butyraldehyde = loadButyraldehyde();
        MaterialsKevlar.Isobutyraldehyde = loadIsobutyraldehyde();
        MaterialsKevlar.NickelTetracarbonyl = loadNickelTetracarbonyl();
        MaterialsKevlar.KevlarCatalyst = loadKevlarCatalyst();
        MaterialsKevlar.EthyleneOxide = loadEthyleneOxide();
        MaterialsKevlar.SiliconOil = loadSiliconOil();
        MaterialsKevlar.Ethyleneglycol = loadEthyleneglycol();
        MaterialsKevlar.Acetaldehyde = loadAcetaldehyde();
        MaterialsKevlar.Pentaerythritol = loadPentaerythritol();
        MaterialsKevlar.PolyurethaneResin = loadPolyurethaneResin();
        MaterialsKevlar.NMethylIIPyrrolidone = loadNMethylIIPyrrolidone();
        MaterialsKevlar.TerephthaloylChloride = loadTerephthaloylChloride();
        MaterialsKevlar.Acetylene = loadAcetylene();
        MaterialsKevlar.IVNitroaniline = loadIVNitroaniline();
        MaterialsKevlar.ParaPhenylenediamine = loadParaPhenylenediamine();
        MaterialsKevlar.Methylamine = loadMethylamine();
        MaterialsKevlar.Trimethylamine = loadTrimethylamine();
        MaterialsKevlar.GammaButyrolactone = loadGammaButyrolactone();
        MaterialsKevlar.CalciumCarbide = loadCalciumCarbide();
        MaterialsKevlar.LiquidCrystalKevlar = loadLiquidCrystalKevlar();
        MaterialsKevlar.IIButinIIVdiol = loadIIButinIIVdiol();
        MaterialsKevlar.NickelAluminide = loadNickelAluminide();
        MaterialsKevlar.RaneyNickelActivated = loadRaneyNickelActivated();
        MaterialsKevlar.BismuthIIIOxide = loadBismuthIIIOxide();
        MaterialsKevlar.ThionylChloride = loadThionylChloride();
        MaterialsKevlar.SulfurDichloride = loadSulfurDichloride();
        MaterialsKevlar.DimethylTerephthalate = loadDimethylTerephthalate();
        MaterialsKevlar.Kevlar = loadKevlar();
        MaterialsKevlar.TerephthalicAcid = loadTerephthalicAcid();
        MaterialsKevlar.IIIDimethylbenzene = loadIIIDimethylbenzene();
        MaterialsKevlar.IVDimethylbenzene = loadIVDimethylbenzene();
        MaterialsKevlar.CobaltIINaphthenate = loadCobaltIINaphthenate();
        MaterialsKevlar.NaphthenicAcid = loadNaphthenicAcid();
        MaterialsKevlar.CobaltIIHydroxide = loadCobaltIIHydroxide();
        MaterialsKevlar.CobaltIIAcetate = loadCobaltIIAcetate();
        MaterialsKevlar.CobaltIINitrate = loadCobaltIINitrate();
        MaterialsKevlar.OrganorhodiumCatalyst = loadOrganorhodiumCatalyst();
        MaterialsKevlar.SodiumBorohydride = loadSodiumBorohydride();
        MaterialsKevlar.RhodiumChloride = loadRhodiumChloride();
        MaterialsKevlar.Triphenylphosphene = loadTriphenylphosphene();
        MaterialsKevlar.PhosphorusTrichloride = loadPhosphorusTrichloride();
        MaterialsKevlar.SodiumHydride = loadSodiumHydride();
        MaterialsKevlar.TrimethylBorate = loadTrimethylBorate();
        MaterialsKevlar.SodiumMethoxide = loadSodiumMethoxide();
    }

    private static Materials loadDiphenylmethaneDiisocyanate() {
        return new MaterialBuilder().setName("DiphenylmethaneDiisocyanate")
            .setDefaultLocalName("4,4'-Diphenylmethane Diisocyanate")
            .setMetaItemSubID(796)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .setRGB(0xffe632)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(310)
            // C15H10N2O2
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
            .setMetaItemSubID(795)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setRGB(0xfff37a)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(365)
            // C13H14N2
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
            .setMetaItemSubID(794)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setRGB(0xffe632)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(310)
            // C15H10N2O2
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
            .setMetaItemSubID(793)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(176)
            // C4H8O
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
            .setMetaItemSubID(792)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(208)
            .addElectrolyzerRecipe()
            // C4H8O
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
            .setMetaItemSubID(791)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(256)
            // C4NiO4
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
            .setMetaItemSubID(790)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .setRGB(0x323232)
            .setColor(Dyes.dyeBlack)
            .setMeltingPoint(300)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.LUCRUM, 1)
            .constructMaterial();
    }

    private static Materials loadEthyleneOxide() {
        return new MaterialBuilder().setName("EthyleneOxide")
            .setDefaultLocalName("Ethylene Oxide")
            .setMetaItemSubID(789)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addGas()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(160)
            // C2H4O
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
            .setMetaItemSubID(788)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(473)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.MACHINA, 1)
            .constructMaterial();
    }

    private static Materials loadEthyleneglycol() {
        return new MaterialBuilder().setName("EthyleneGlycol")
            .setDefaultLocalName("Ethylene Glycol")
            .setMetaItemSubID(787)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(260)
            // C2H6O2
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
            .setMetaItemSubID(786)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addGas()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(150)
            // C2H4O
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
            .setMetaItemSubID(785)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(533)
            // C5H12O4
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
            .setMetaItemSubID(784)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setRGB(0xe6e678)
            .setColor(Dyes.dyeYellow)
            .constructMaterial();
    }

    private static Materials loadNMethylIIPyrrolidone() {
        return new MaterialBuilder().setName("NMethylpyrolidone")
            .setDefaultLocalName("N-Methyl-2-pyrrolidone")
            .setMetaItemSubID(783)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(249)
            // C5H9NO
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
            .setMetaItemSubID(782)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setRGB(0x00ff0c)
            .setColor(Dyes.dyeGreen)
            .setMeltingPoint(355)
            // C8H4Cl2O2
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
            .setMetaItemSubID(781)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addGas()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(192)
            // C2H2
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 2)
            .constructMaterial();
    }

    private static Materials loadIVNitroaniline() {
        return new MaterialBuilder().setName("4Nitroaniline")
            .setDefaultLocalName("4-Nitroaniline")
            .setMetaItemSubID(780)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setRGB(0xff8733)
            .setColor(Dyes.dyeOrange)
            .setMeltingPoint(420)
            // C6H6N2O2
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
            .setMetaItemSubID(779)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setRGB(0xfbec5d)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(293)
            // C6H6N2
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
            .setMetaItemSubID(778)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addGas()
            .setRGB(0x414469)
            .setColor(Dyes.dyeGray)
            .setMeltingPoint(180)
            .addElectrolyzerRecipe()
            // CH5N
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Hydrogen, 5)
            .addMaterial(Materials.Nitrogen, 1)
            .constructMaterial();
    }

    private static Materials loadTrimethylamine() {
        return new MaterialBuilder().setName("Trimethylamine")
            .setDefaultLocalName("Trimethylamine")
            .setMetaItemSubID(777)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addGas()
            .setRGB(0x694469)
            .setColor(Dyes.dyeGray)
            .setMeltingPoint(156)
            .addElectrolyzerRecipe()
            // C3H9N
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Hydrogen, 9)
            .addMaterial(Materials.Nitrogen, 1)
            .constructMaterial();
    }

    private static Materials loadGammaButyrolactone() {
        return new MaterialBuilder().setName("GammaButyrolactone")
            .setDefaultLocalName("gamma-Butyrolactone")
            .setMetaItemSubID(776)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setRGB(0xffff97)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(229)
            // C4H6O2
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadCalciumCarbide() {
        return new MaterialBuilder().setName("CacliumCarbide")
            .setDefaultLocalName("Calcium Carbide")
            .setMetaItemSubID(775)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .setRGB(0xebebeb)
            .setColor(Dyes.dyeGray)
            .setMeltingPoint(2430)
            // CaC2
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Carbon, 2)
            .constructMaterial();
    }

    private static Materials loadLiquidCrystalKevlar() {
        return new MaterialBuilder().setName("LiquidCrystalKevlar")
            .setDefaultLocalName("Liquid Crystal Kevlar")
            .setChemicalFormula("[-CO-C₆H₄-CO-NH-C₆H₄-NH-]n")
            .setMetaItemSubID(774)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setRGB(0xf0f078)
            .setColor(Dyes.dyeYellow)
            .constructMaterial();
    }

    private static Materials loadIIButinIIVdiol() {
        return new MaterialBuilder().setName("2Butin14diol")
            .setDefaultLocalName("2-Butin-1,4-diol")
            .setMetaItemSubID(773)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setRGB(0xf7f7b4)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(331)
            // C4H6O2
            .addMaterial(Materials.Carbon, 4)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadNickelAluminide() {
        return new MaterialBuilder().setName("NickelAluminide")
            .setDefaultLocalName("Nickel Aluminide")
            .setMetaItemSubID(772)
            .setIconSet(TextureSet.SET_METALLIC)
            .addDustItems()
            .addMetalItems()
            .setRGB(0xe6e6e6)
            .setColor(Dyes.dyeGray)
            .setMeltingPoint(1_668)
            .setBlastFurnaceTemp(1_668)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            // NiAl3
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.Aluminium, 3)
            .constructMaterial();
    }

    private static Materials loadRaneyNickelActivated() {
        return new MaterialBuilder().setName("RaneyNickelActivated")
            .setDefaultLocalName("Raney Nickel")
            .setMetaItemSubID(771)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setRGB(0xe6e6e6)
            .setColor(Dyes.dyeGray)
            .setMeltingPoint(1_955)
            // NiAl
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.Aluminium, 1)
            .constructMaterial();
    }

    private static Materials loadBismuthIIIOxide() {
        return new MaterialBuilder().setName("BismuthIIIOxide")
            .setDefaultLocalName("Bismuth Oxide")
            .setMetaItemSubID(769)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setRGB(0x323232)
            .setColor(Dyes.dyeBlack)
            .setMeltingPoint(1090)
            // Bi2O3
            .addMaterial(Materials.Bismuth, 2)
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadThionylChloride() {
        // SOCl2
        return new MaterialBuilder().setName("ThionylChloride")
            .setDefaultLocalName("Thionyl Chloride")
            .setMetaItemSubID(768)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setColor(Dyes.dyeWhite)
            .constructMaterial();
    }

    private static Materials loadSulfurDichloride() {
        // SCl2
        return new MaterialBuilder().setName("SulfurDichloride")
            .setDefaultLocalName("Sulfur Dichloride")
            .setMetaItemSubID(767)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setRGB(0xc80000)
            .setColor(Dyes.dyeRed)
            .constructMaterial();
    }

    private static Materials loadDimethylTerephthalate() {
        return new MaterialBuilder().setName("DimethylTerephthalate")
            .setDefaultLocalName("Dimethyl Terephthalate")
            .setMetaItemSubID(766)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(415)
            // C10H10O4
            .addMaterial(Materials.Carbon, 10)
            .addMaterial(Materials.Hydrogen, 10)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadKevlar() {
        return new MaterialBuilder().setName("Kevlar")
            .setDefaultLocalName("Kevlar")
            .setMetaItemSubID(765)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .addMetalItems()
            .addGearItems()
            .setRGB(0xf0f078)
            .setColor(Dyes.dyeYellow)
            .constructMaterial();
    }

    private static Materials loadTerephthalicAcid() {
        return new MaterialBuilder().setName("TerephthalicAcid")
            .setDefaultLocalName("Terephthalic Acid")
            .setMetaItemSubID(764)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(480)
            // C9H6O6
            .addMaterial(Materials.Carbon, 8)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadIIIDimethylbenzene() {
        return new MaterialBuilder().setName("1,3Dimethylbenzene")
            .setDefaultLocalName("1,3-Dimethylbenzene")
            .setMetaItemSubID(763)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setRGB(0x70924a)
            .setColor(Dyes.dyeLime)
            .setMeltingPoint(225)
            // C8H10
            .addMaterial(Materials.Carbon, 8)
            .addMaterial(Materials.Hydrogen, 10)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadIVDimethylbenzene() {
        return new MaterialBuilder().setName("1,4Dimethylbenzene")
            .setDefaultLocalName("1,4-Dimethylbenzene")
            .setMetaItemSubID(762)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setRGB(0x7a8854)
            .setColor(Dyes.dyeLime)
            .setMeltingPoint(286)
            // C8H10
            .addMaterial(Materials.Carbon, 8)
            .addMaterial(Materials.Hydrogen, 10)
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadCobaltIINaphthenate() {
        return new MaterialBuilder().setName("Cobalt(II)Naphthenate")
            .setDefaultLocalName("Cobalt II Naphthenate")
            .setMetaItemSubID(761)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .setRGB(0x8f5f27)
            .setColor(Dyes.dyeBrown)
            .setMeltingPoint(413)
            // CoC22H14O4
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.Carbon, 22)
            .addMaterial(Materials.Hydrogen, 14)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadNaphthenicAcid() {
        return new MaterialBuilder().setName("NaphthenicAcid")
            .setDefaultLocalName("Naphthenic Acid")
            .setMetaItemSubID(760)
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
            .setMetaItemSubID(759)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setRGB(0xe58cef)
            .setColor(Dyes.dyePurple)
            .setMeltingPoint(441)
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadCobaltIIAcetate() {
        return new MaterialBuilder().setName("Cobalt(II)Acetate")
            .setDefaultLocalName("Cobalt II Acetate")
            .setMetaItemSubID(758)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setRGB(0xdba2e5)
            .setColor(Dyes.dyePurple)
            .setMeltingPoint(413)
            // C4H6CoO4
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
            .setMetaItemSubID(757)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setRGB(0xaa0000)
            .setColor(Dyes.dyeRed)
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
            .setMetaItemSubID(756)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setRGB(0xaa0000)
            .setColor(Dyes.dyeRed)
            .setMeltingPoint(373)
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.NitricAcid, 2)
            .constructMaterial();
    }

    private static Materials loadSodiumBorohydride() {
        return new MaterialBuilder().setName("SodiumBorohydride")
            .setDefaultLocalName("Sodium Borohydride")
            .setMetaItemSubID(755)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(673)
            // NaBH4
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Boron, 1)
            .addMaterial(Materials.Hydrogen, 4)
            .constructMaterial();
    }

    private static Materials loadRhodiumChloride() {
        return new MaterialBuilder().setName("RhodiumChloride")
            .setDefaultLocalName("Rhodium Chloride")
            .setChemicalFormula("RhCl₃")
            .setMetaItemSubID(754)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setRGB(0x800000)
            .setColor(Dyes.dyeRed)
            .setMeltingPoint(723)
            .constructMaterial();
    }

    private static Materials loadTriphenylphosphene() {
        return new MaterialBuilder().setName("Triphenylphosphene")
            .setDefaultLocalName("Triphenylphosphine")
            .setMetaItemSubID(753)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(353)
            // C18H15P
            .addMaterial(Materials.Carbon, 18)
            .addMaterial(Materials.Hydrogen, 15)
            .addMaterial(Materials.Phosphorus, 1)
            .constructMaterial();
    }

    private static Materials loadPhosphorusTrichloride() {
        return new MaterialBuilder().setName("PhosphorusTrichloride")
            .setDefaultLocalName("Phosphorus Trichloride")
            .setMetaItemSubID(752)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(179)
            // PCL3
            .addMaterial(Materials.Phosphorus, 1)
            .addMaterial(Materials.Chlorine, 3)
            .constructMaterial();
    }

    private static Materials loadSodiumHydride() {
        return new MaterialBuilder().setName("SodiumHydride")
            .setDefaultLocalName("Sodium Hydride")
            .setMetaItemSubID(751)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setRGB(0xc0c0c0)
            .setColor(Dyes.dyeLightGray)
            .setMeltingPoint(911)
            // NaH
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .constructMaterial();
    }

    private static Materials loadTrimethylBorate() {
        return new MaterialBuilder().setName("TrimethylBorate")
            .setDefaultLocalName("Trimethyl Borate")
            .setMetaItemSubID(750)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .addFluid()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(239)
            // C3H9BO3
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Hydrogen, 9)
            .addMaterial(Materials.Boron, 1)
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadSodiumMethoxide() {
        return new MaterialBuilder().setName("SodiumMethoxide")
            .setDefaultLocalName("Sodium Methoxide")
            .setMetaItemSubID(749)
            .setIconSet(TextureSet.SET_POWDER)
            .addDustItems()
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(400)
            // CH3NaO
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Hydrogen, 3)
            .addMaterial(Materials.Oxygen, 1)
            .addMaterial(Materials.Sodium, 1)
            .constructMaterial();
    }
}
