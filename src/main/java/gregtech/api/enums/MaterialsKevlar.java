package gregtech.api.enums;

import java.util.Arrays;

import gregtech.api.objects.MaterialStack;

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
        return new MaterialBuilder(796, TextureSet.SET_DULL, "4,4'-Diphenylmethane Diisocyanate")
            .setName("DiphenylmethaneDiisocyanate")
            .addDustItems()
            .setRGB(255, 230, 50)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(310)
            // C15H10N2O2
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 15),
                new MaterialStack(Materials.Hydrogen, 10),
                new MaterialStack(Materials.Nitrogen, 2),
                new MaterialStack(Materials.Oxygen, 2))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.TERRA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1)))
            .constructMaterial();
    }

    private static Materials loadDiaminodiphenylmethanMixture() {
        return new MaterialBuilder(795, TextureSet.SET_FLUID, "Diaminodiphenylmethane Mixture")
            .setName("DiaminodiphenylmethanMixture")
            .addCell()
            .addFluid()
            .setRGB(255, 243, 122)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(365)
            // C13H14N2
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 13),
                new MaterialStack(Materials.Hydrogen, 14),
                new MaterialStack(Materials.Nitrogen, 2))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1)))
            .constructMaterial();
    }

    private static Materials loadDiphenylmethaneDiisocyanateMixture() {
        return new MaterialBuilder(794, TextureSet.SET_FLUID, "Diphenylmethane Diisocyanate Mixture")
            .setName("DiphenylmethaneDiisocyanateMixture")
            .addCell()
            .addFluid()
            .setRGB(255, 230, 50)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(310)
            // C15H10N2O2
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 15),
                new MaterialStack(Materials.Hydrogen, 10),
                new MaterialStack(Materials.Nitrogen, 2),
                new MaterialStack(Materials.Oxygen, 2))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1)))
            .constructMaterial();
    }

    private static Materials loadButyraldehyde() {
        return new MaterialBuilder(793, TextureSet.SET_FLUID, "Butyraldehyde").setName("Butyraldehyde")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(176)
            // C4H8O
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 4),
                new MaterialStack(Materials.Hydrogen, 8),
                new MaterialStack(Materials.Oxygen, 1))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1)))
            .constructMaterial();
    }

    private static Materials loadIsobutyraldehyde() {
        return new MaterialBuilder(792, TextureSet.SET_FLUID, "Isobutyraldehyde").setName("Isobutyraldehyde")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(208)
            .setExtraData(1)
            // C4H8O
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 4),
                new MaterialStack(Materials.Hydrogen, 8),
                new MaterialStack(Materials.Oxygen, 1))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1)))
            .constructMaterial();
    }

    private static Materials loadNickelTetracarbonyl() {
        return new MaterialBuilder(791, TextureSet.SET_FLUID, "Nickel Tetracarbonyl").setName("NickelTetracarbonyl")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(256)
            // C4NiO4
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 4),
                new MaterialStack(Materials.Nickel, 1),
                new MaterialStack(Materials.Oxygen, 4))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1)))
            .constructMaterial();
    }

    private static Materials loadKevlarCatalyst() {
        return new MaterialBuilder(790, TextureSet.SET_DULL, "Polyurethane Catalyst A")
            .setName("PolyurethaneCatalystADust")
            .addDustItems()
            .setRGB(50, 50, 50)
            .setColor(Dyes.dyeBlack)
            .setMeltingPoint(300)
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1)))
            .constructMaterial();
    }

    private static Materials loadEthyleneOxide() {
        return new MaterialBuilder(789, TextureSet.SET_FLUID, "Ethylene Oxide").setName("EthyleneOxide")
            .addCell()
            .addGas()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(160)
            // C2H4O
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 2),
                new MaterialStack(Materials.Hydrogen, 4),
                new MaterialStack(Materials.Oxygen, 1))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1)))
            .constructMaterial();
    }

    private static Materials loadSiliconOil() {
        return new MaterialBuilder(788, TextureSet.SET_FLUID, "Silicon Oil").setName("SiliconOil")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(473)
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1)))
            .constructMaterial();
    }

    private static Materials loadEthyleneglycol() {
        return new MaterialBuilder(787, TextureSet.SET_FLUID, "Ethylene Glycol").setName("EthyleneGlycol")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(260)
            // C2H6O2
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 2),
                new MaterialStack(Materials.Hydrogen, 6),
                new MaterialStack(Materials.Oxygen, 2))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1)))
            .constructMaterial();
    }

    private static Materials loadAcetaldehyde() {
        return new MaterialBuilder(786, TextureSet.SET_FLUID, "Acetaldehyde").setName("Acetaldehyde")
            .addCell()
            .addGas()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(150)
            // C2H4O
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 2),
                new MaterialStack(Materials.Hydrogen, 4),
                new MaterialStack(Materials.Oxygen, 1))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1)))
            .constructMaterial();
    }

    private static Materials loadPentaerythritol() {
        return new MaterialBuilder(785, TextureSet.SET_DULL, "Pentaerythritol").setName("Pentaerythritol")
            .addDustItems()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(533)
            // C5H12O4
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 5),
                new MaterialStack(Materials.Hydrogen, 12),
                new MaterialStack(Materials.Oxygen, 4))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.TERRA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1)))
            .constructMaterial();
    }

    private static Materials loadPolyurethaneResin() {
        return new MaterialBuilder(784, TextureSet.SET_FLUID, "Polyurethane Resin").setName("PolyurethaneResin")
            .addCell()
            .addFluid()
            .setRGB(230, 230, 120)
            .setColor(Dyes.dyeYellow)
            .constructMaterial();
    }

    private static Materials loadNMethylIIPyrrolidone() {
        return new MaterialBuilder(783, TextureSet.SET_FLUID, "N-Methyl-2-pyrrolidone").setName("NMethylpyrolidone")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(249)
            // C5H9NO
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 5),
                new MaterialStack(Materials.Hydrogen, 9),
                new MaterialStack(Materials.Nitrogen, 1),
                new MaterialStack(Materials.Oxygen, 1))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.TERRA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1)))
            .constructMaterial();
    }

    private static Materials loadTerephthaloylChloride() {
        return new MaterialBuilder(782, TextureSet.SET_POWDER, "Terephthaloyl Chloride")
            .setName("TerephthaloylChloride")
            .addDustItems()
            .setRGB(0, 255, 12)
            .setColor(Dyes.dyeGreen)
            .setMeltingPoint(355)
            // C8H4Cl2O2
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 8),
                new MaterialStack(Materials.Hydrogen, 4),
                new MaterialStack(Materials.Chlorine, 2),
                new MaterialStack(Materials.Oxygen, 2))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.TERRA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1)))
            .constructMaterial();
    }

    private static Materials loadAcetylene() {
        // TODO: Add to JUPITER Athmosphere and Enceladus and to moon of Saturn
        return new MaterialBuilder(781, TextureSet.SET_FLUID, "Acetylene").setName("Acetylene")
            .addCell()
            .addGas()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(192)
            // C2H2
            .setMaterialList(new MaterialStack(Materials.Carbon, 2), new MaterialStack(Materials.Hydrogen, 2))
            .constructMaterial();
    }

    private static Materials loadIVNitroaniline() {
        return new MaterialBuilder(780, TextureSet.SET_FLUID, "4-Nitroaniline").setName("4Nitroaniline")
            .addCell()
            .addFluid()
            .setRGB(255, 135, 51)
            .setColor(Dyes.dyeOrange)
            .setMeltingPoint(420)
            // C6H6N2O2
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 6),
                new MaterialStack(Materials.Hydrogen, 6),
                new MaterialStack(Materials.Nitrogen, 2),
                new MaterialStack(Materials.Oxygen, 2))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.TERRA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1)))
            .constructMaterial();
    }

    private static Materials loadParaPhenylenediamine() {
        return new MaterialBuilder(779, TextureSet.SET_POWDER, "para-Phenylenediamine").setName("pPhenylenediamine")
            .addDustItems()
            .setRGB(251, 236, 93)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(293)
            // C6H6N2
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 6),
                new MaterialStack(Materials.Hydrogen, 8),
                new MaterialStack(Materials.Nitrogen, 2))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.TERRA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1)))
            .constructMaterial();
    }

    private static Materials loadMethylamine() {
        return new MaterialBuilder(778, TextureSet.SET_FLUID, "Methylamine").setName("Methylamine")
            .addCell()
            .addGas()
            .setRGB(65, 68, 105)
            .setColor(Dyes.dyeGray)
            .setMeltingPoint(180)
            .setExtraData(1)
            // CH5N
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 1),
                new MaterialStack(Materials.Hydrogen, 5),
                new MaterialStack(Materials.Nitrogen, 1))
            .constructMaterial();
    }

    private static Materials loadTrimethylamine() {
        return new MaterialBuilder(777, TextureSet.SET_FLUID, "Trimethylamine").setName("Trimethylamine")
            .addCell()
            .addGas()
            .setRGB(105, 68, 105)
            .setColor(Dyes.dyeGray)
            .setMeltingPoint(156)
            .setExtraData(1)
            // C3H9N
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 3),
                new MaterialStack(Materials.Hydrogen, 9),
                new MaterialStack(Materials.Nitrogen, 1))
            .constructMaterial();
    }

    private static Materials loadGammaButyrolactone() {
        return new MaterialBuilder(776, TextureSet.SET_FLUID, "gamma-Butyrolactone").setName("GammaButyrolactone")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 151)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(229)
            // C4H6O2
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 4),
                new MaterialStack(Materials.Hydrogen, 6),
                new MaterialStack(Materials.Oxygen, 2))
            .constructMaterial();
    }

    private static Materials loadCalciumCarbide() {
        return new MaterialBuilder(775, TextureSet.SET_DULL, "Calcium Carbide").setName("CacliumCarbide")
            .addDustItems()
            .setRGB(235, 235, 235)
            .setColor(Dyes.dyeGray)
            .setMeltingPoint(2430)
            // CaC2
            .setMaterialList(new MaterialStack(Materials.Calcium, 1), new MaterialStack(Materials.Carbon, 2))
            .constructMaterial();
    }

    private static Materials loadLiquidCrystalKevlar() {
        // [-CO-C6H4-CO-NH-C6H4-NH-]n
        return new MaterialBuilder(774, TextureSet.SET_FLUID, "Liquid Crystal Kevlar").setName("LiquidCrystalKevlar")
            .addCell()
            .addFluid()
            .setRGB(240, 240, 120)
            .setColor(Dyes.dyeYellow)
            .constructMaterial();
    }

    private static Materials loadIIButinIIVdiol() {
        return new MaterialBuilder(773, TextureSet.SET_POWDER, "2-Butin-1,4-diol").setName("2Butin14diol")
            .addDustItems()
            .setRGB(247, 247, 180)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(331)
            // C4H6O2
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 4),
                new MaterialStack(Materials.Hydrogen, 6),
                new MaterialStack(Materials.Oxygen, 2))
            .constructMaterial();
    }

    private static Materials loadNickelAluminide() {
        return new MaterialBuilder(772, TextureSet.SET_METALLIC, "Nickel Aluminide").setName("NickelAluminide")
            .addDustItems()
            .addMetalItems()
            .setRGB(230, 230, 230)
            .setColor(Dyes.dyeGray)
            .setMeltingPoint(1668)
            .setBlastFurnaceTemp(1668)
            .setBlastFurnaceRequired(true)
            // NiAl3
            .setMaterialList(new MaterialStack(Materials.Nickel, 1), new MaterialStack(Materials.Aluminium, 3))
            .constructMaterial()
            .disableAutoGeneratedBlastFurnaceRecipes();
    }

    private static Materials loadRaneyNickelActivated() {
        return new MaterialBuilder(771, TextureSet.SET_POWDER, "Raney Nickel").setName("RaneyNickelActivated")
            .addDustItems()
            .setRGB(230, 230, 230)
            .setColor(Dyes.dyeGray)
            .setMeltingPoint(1955)
            // NiAl
            .setMaterialList(new MaterialStack(Materials.Nickel, 1), new MaterialStack(Materials.Aluminium, 1))
            .constructMaterial();
    }

    private static Materials loadBismuthIIIOxide() {
        return new MaterialBuilder(769, TextureSet.SET_POWDER, "Bismuth Oxide").setName("BismuthIIIOxide")
            .addDustItems()
            .setRGB(50, 50, 50)
            .setColor(Dyes.dyeBlack)
            .setMeltingPoint(1090)
            // Bi2O3
            .setMaterialList(new MaterialStack(Materials.Bismuth, 2), new MaterialStack(Materials.Oxygen, 3))
            .constructMaterial();
    }

    private static Materials loadThionylChloride() {
        // SOCl2
        return new MaterialBuilder(768, TextureSet.SET_FLUID, "Thionyl Chloride").setName("ThionylChloride")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .constructMaterial();
    }

    private static Materials loadSulfurDichloride() {
        // SCl2
        return new MaterialBuilder(767, TextureSet.SET_FLUID, "Sulfur Dichloride").setName("SulfurDichloride")
            .addCell()
            .addFluid()
            .setRGB(200, 0, 0)
            .setColor(Dyes.dyeRed)
            .constructMaterial();
    }

    private static Materials loadDimethylTerephthalate() {
        return new MaterialBuilder(766, TextureSet.SET_FLUID, "Dimethyl Terephthalate").setName("DimethylTerephthalate")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(415)
            // C10H10O4
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 10),
                new MaterialStack(Materials.Hydrogen, 10),
                new MaterialStack(Materials.Oxygen, 4))
            .constructMaterial();
    }

    private static Materials loadKevlar() {
        return new MaterialBuilder(765, TextureSet.SET_DULL, "Kevlar").setName("Kevlar")
            .addDustItems()
            .addMetalItems()
            .addGearItems()
            .setRGB(240, 240, 120)
            .setColor(Dyes.dyeYellow)
            .constructMaterial();
    }

    private static Materials loadTerephthalicAcid() {
        return new MaterialBuilder(764, TextureSet.SET_FLUID, "Terephthalic Acid").setName("TerephthalicAcid")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(480)
            // C9H6O6
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 8L),
                new MaterialStack(Materials.Hydrogen, 6),
                new MaterialStack(Materials.Oxygen, 4))
            .constructMaterial();
    }

    private static Materials loadIIIDimethylbenzene() {
        return new MaterialBuilder(763, TextureSet.SET_FLUID, "1,3-Dimethylbenzene").addCell()
            .addFluid()
            .setRGB(112, 146, 74)
            .setColor(Dyes.dyeLime)
            .setMeltingPoint(225)
            // C8H10
            .setMaterialList(new MaterialStack(Materials.Carbon, 8), new MaterialStack(Materials.Hydrogen, 10))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadIVDimethylbenzene() {
        return new MaterialBuilder(762, TextureSet.SET_FLUID, "1,4-Dimethylbenzene").addCell()
            .addFluid()
            .setRGB(122, 136, 84)
            .setColor(Dyes.dyeLime)
            .setMeltingPoint(286)
            // C8H10
            .setMaterialList(new MaterialStack(Materials.Carbon, 8), new MaterialStack(Materials.Hydrogen, 10))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadCobaltIINaphthenate() {
        return new MaterialBuilder(761, TextureSet.SET_DULL, "Cobalt II Naphthenate").setName("Cobalt(II)Naphthenate")
            .addDustItems()
            .setRGB(143, 95, 39)
            .setColor(Dyes.dyeBrown)
            .setMeltingPoint(413)
            // CoC22H14O4
            .setMaterialList(
                new MaterialStack(Materials.Cobalt, 1),
                new MaterialStack(Materials.Carbon, 22),
                new MaterialStack(Materials.Hydrogen, 14),
                new MaterialStack(Materials.Oxygen, 4))
            .constructMaterial();
    }

    private static Materials loadNaphthenicAcid() {
        return new MaterialBuilder(760, TextureSet.SET_FLUID, "Naphthenic Acid").setName("NaphthenicAcid")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setFuel(MaterialBuilder.FuelType.SemiFluid, 80)
            .constructMaterial();
    }

    private static Materials loadCobaltIIHydroxide() {
        return new MaterialBuilder(759, TextureSet.SET_POWDER, "Cobalt II Hydroxide").setName("CobaltIIHydroxide")
            .addDustItems()
            .setRGB(229, 140, 239)
            .setColor(Dyes.dyePurple)
            .setMeltingPoint(441)
            // CoH2O2
            .setMaterialList(
                new MaterialStack(Materials.Cobalt, 1),
                new MaterialStack(Materials.Hydrogen, 2),
                new MaterialStack(Materials.Oxygen, 2))
            .constructMaterial();
    }

    private static Materials loadCobaltIIAcetate() {
        return new MaterialBuilder(758, TextureSet.SET_POWDER, "Cobalt II Acetate").setName("Cobalt(II)Acetate")
            .addDustItems()
            .setRGB(219, 162, 229)
            .setColor(Dyes.dyePurple)
            .setMeltingPoint(413)
            // C4H6CoO4
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 4L),
                new MaterialStack(Materials.Hydrogen, 6),
                new MaterialStack(Materials.Cobalt, 1),
                new MaterialStack(Materials.Oxygen, 4))
            .constructMaterial();
    }

    private static Materials loadCobaltIINitrate() {
        return new MaterialBuilder(757, TextureSet.SET_POWDER, "Cobalt II Nitrate").setName("Cobalt(II)Nitrate")
            .addDustItems()
            .setRGB(170, 0, 0)
            .setColor(Dyes.dyeRed)
            .setMeltingPoint(373)
            // Co(NO3)2
            .setMaterialList(
                new MaterialStack(Materials.Cobalt, 1),
                new MaterialStack(Materials.Nitrogen, 2),
                new MaterialStack(Materials.Oxygen, 6))
            .constructMaterial();
    }

    private static Materials loadOrganorhodiumCatalyst() {
        return new MaterialBuilder(756, TextureSet.SET_POWDER, "Organorhodium Catalyst")
            .setName("OrganorhodiumCatalyst")
            .addDustItems()
            .setRGB(170, 0, 0)
            .setColor(Dyes.dyeRed)
            .setMeltingPoint(373)
            // RhHCO(P(C6H5)3)3
            .setMaterialList(new MaterialStack(Materials.Cobalt, 1), new MaterialStack(Materials.NitricAcid, 2))
            .constructMaterial();
    }

    private static Materials loadSodiumBorohydride() {
        return new MaterialBuilder(755, TextureSet.SET_POWDER, "Sodium Borohydride").setName("SodiumBorohydride")
            .addDustItems()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(673)
            // NaBH4
            .setMaterialList(
                new MaterialStack(Materials.Sodium, 1),
                new MaterialStack(Materials.Boron, 1),
                new MaterialStack(Materials.Hydrogen, 4))
            .constructMaterial();
    }

    private static Materials loadRhodiumChloride() {
        // RHCL3
        return new MaterialBuilder(754, TextureSet.SET_POWDER, "Rhodium Chloride").setName("RhodiumChloride")
            .addDustItems()
            .setRGB(128, 0, 0)
            .setColor(Dyes.dyeRed)
            .setMeltingPoint(723)
            .constructMaterial();
    }

    private static Materials loadTriphenylphosphene() {
        return new MaterialBuilder(753, TextureSet.SET_POWDER, "Triphenylphosphine").setName("Triphenylphosphene")
            .addDustItems()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(353)
            // C18H15P
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 18L),
                new MaterialStack(Materials.Hydrogen, 15L),
                new MaterialStack(Materials.Phosphorus, 1L))
            .constructMaterial();
    }

    private static Materials loadPhosphorusTrichloride() {
        return new MaterialBuilder(752, TextureSet.SET_FLUID, "Phosphorus Trichloride").setName("PhosphorusTrichloride")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(179)
            // PCL3
            .setMaterialList(new MaterialStack(Materials.Phosphorus, 1L), new MaterialStack(Materials.Chlorine, 3L))
            .constructMaterial();
    }

    private static Materials loadSodiumHydride() {
        return new MaterialBuilder(751, TextureSet.SET_POWDER, "Sodium Hydride").setName("SodiumHydride")
            .addDustItems()
            .setRGB(192, 192, 192)
            .setColor(Dyes.dyeLightGray)
            .setMeltingPoint(911)
            // NaH
            .setMaterialList(new MaterialStack(Materials.Sodium, 1L), new MaterialStack(Materials.Hydrogen, 1L))
            .constructMaterial();
    }

    private static Materials loadTrimethylBorate() {
        return new MaterialBuilder(750, TextureSet.SET_FLUID, "Trimethyl Borate").setName("TrimethylBorate")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(239)
            // C3H9BO3
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 3L),
                new MaterialStack(Materials.Hydrogen, 9L),
                new MaterialStack(Materials.Boron, 1L),
                new MaterialStack(Materials.Oxygen, 3L))
            .constructMaterial();
    }

    private static Materials loadSodiumMethoxide() {
        return new MaterialBuilder(749, TextureSet.SET_POWDER, "Sodium Methoxide").setName("SodiumMethoxide")
            .addDustItems()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(400)
            // CH3NaO
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 1L),
                new MaterialStack(Materials.Hydrogen, 3L),
                new MaterialStack(Materials.Oxygen, 1L),
                new MaterialStack(Materials.Sodium, 1L))
            .constructMaterial();
    }
}
