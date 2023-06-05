package gregtech.api.enums;

import java.util.Arrays;

import gregtech.api.objects.MaterialStack;

public class MaterialsKevlar {

    public static Materials DiphenylmethaneDiisocyanate = new MaterialBuilder(
        796,
        TextureSet.SET_DULL,
        "4,4'-Diphenylmethane Diisocyanate").setName("DiphenylmethaneDiisocyanate")
            .addDustItems()
            .setRGB(255, 230, 50)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(310)
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 15),
                new MaterialStack(Materials.Hydrogen, 10),
                new MaterialStack(Materials.Nitrogen, 2),
                new MaterialStack(Materials.Oxygen, 2))
            .setAspects(
                Arrays.asList(
                    new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 1),
                    new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1)))
            .constructMaterial(); // C15H10N2O2
    public static Materials DiaminodiphenylmethanMixture = new MaterialBuilder(
        795,
        TextureSet.SET_FLUID,
        "Diaminodiphenylmethane Mixture").setName("DiaminodiphenylmethanMixture")
            .addCell()
            .addFluid()
            .setRGB(255, 243, 122)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(365)
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 13),
                new MaterialStack(Materials.Hydrogen, 14),
                new MaterialStack(Materials.Nitrogen, 2))
            .setAspects(
                Arrays.asList(
                    new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1),
                    new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1)))
            .constructMaterial(); // C13H14N2
    public static Materials DiphenylmethaneDiisocyanateMixture = new MaterialBuilder(
        794,
        TextureSet.SET_FLUID,
        "Diphenylmethane Diisocyanate Mixture").setName("DiphenylmethaneDiisocyanateMixture")
            .addCell()
            .addFluid()
            .setRGB(255, 230, 50)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(310)
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 15),
                new MaterialStack(Materials.Hydrogen, 10),
                new MaterialStack(Materials.Nitrogen, 2),
                new MaterialStack(Materials.Oxygen, 2))
            .setAspects(
                Arrays.asList(
                    new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1),
                    new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1)))
            .constructMaterial(); // C15H10N2O2
    public static Materials Butyraldehyde = new MaterialBuilder(793, TextureSet.SET_FLUID, "Butyraldehyde")
        .setName("Butyraldehyde")
        .addCell()
        .addFluid()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(176)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 4),
            new MaterialStack(Materials.Hydrogen, 8),
            new MaterialStack(Materials.Oxygen, 1))
        .setAspects(
            Arrays.asList(
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1)))
        .constructMaterial(); // C4H8O
    public static Materials Isobutyraldehyde = new MaterialBuilder(792, TextureSet.SET_FLUID, "Isobutyraldehyde")
        .setName("Isobutyraldehyde")
        .addCell()
        .addFluid()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(208)
        .setExtraData(1)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 4),
            new MaterialStack(Materials.Hydrogen, 8),
            new MaterialStack(Materials.Oxygen, 1))
        .setAspects(
            Arrays.asList(
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1)))
        .constructMaterial(); // C4H8O
    public static Materials NickelTetracarbonyl = new MaterialBuilder(791, TextureSet.SET_FLUID, "Nickel Tetracarbonyl")
        .setName("NickelTetracarbonyl")
        .addCell()
        .addFluid()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(256)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 4),
            new MaterialStack(Materials.Nickel, 1),
            new MaterialStack(Materials.Oxygen, 4))
        .setAspects(
            Arrays.asList(
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1)))
        .constructMaterial(); // C4NiO4
    public static Materials KevlarCatalyst = new MaterialBuilder(790, TextureSet.SET_DULL, "Polyurethane Catalyst A")
        .setName("PolyurethaneCatalystADust")
        .addDustItems()
        .setRGB(50, 50, 50)
        .setColor(Dyes.dyeBlack)
        .setMeltingPoint(300)
        .setAspects(
            Arrays.asList(
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 1)))
        .constructMaterial();
    public static Materials EthyleneOxide = new MaterialBuilder(789, TextureSet.SET_FLUID, "Ethylene Oxide")
        .setName("EthyleneOxide")
        .addCell()
        .addGas()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(160)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 2),
            new MaterialStack(Materials.Hydrogen, 4),
            new MaterialStack(Materials.Oxygen, 1))
        .setAspects(
            Arrays.asList(
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1)))
        .constructMaterial(); // C2H4O
    public static Materials SiliconOil = new MaterialBuilder(788, TextureSet.SET_FLUID, "Silicon Oil")
        .setName("SiliconOil")
        .addCell()
        .addFluid()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(473)
        .setAspects(
            Arrays.asList(
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1)))
        .constructMaterial();
    public static Materials Ethyleneglycol = new MaterialBuilder(787, TextureSet.SET_FLUID, "Ethylene Glycol")
        .setName("EthyleneGlycol")
        .addCell()
        .addFluid()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(260)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 2),
            new MaterialStack(Materials.Hydrogen, 6),
            new MaterialStack(Materials.Oxygen, 2))
        .setAspects(
            Arrays.asList(
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1)))
        .constructMaterial(); // C2H6O2
    public static Materials Acetaldehyde = new MaterialBuilder(786, TextureSet.SET_FLUID, "Acetaldehyde")
        .setName("Acetaldehyde")
        .addCell()
        .addGas()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(150)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 2),
            new MaterialStack(Materials.Hydrogen, 4),
            new MaterialStack(Materials.Oxygen, 1))
        .setAspects(
            Arrays.asList(
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1)))
        .constructMaterial(); // C2H4O
    public static Materials Pentaerythritol = new MaterialBuilder(785, TextureSet.SET_DULL, "Pentaerythritol")
        .setName("Pentaerythritol")
        .addDustItems()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(533)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 5),
            new MaterialStack(Materials.Hydrogen, 12),
            new MaterialStack(Materials.Oxygen, 4))
        .setAspects(
            Arrays.asList(
                new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 1),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 1)))
        .constructMaterial(); // C5H12O4
    public static Materials PolyurethaneResin = new MaterialBuilder(784, TextureSet.SET_FLUID, "Polyurethane Resin")
        .setName("PolyurethaneResin")
        .addCell()
        .addFluid()
        .setRGB(230, 230, 120)
        .setColor(Dyes.dyeYellow)
        .constructMaterial();
    public static Materials NMethylIIPyrrolidone = new MaterialBuilder(
        783,
        TextureSet.SET_FLUID,
        "N-Methyl-2-pyrrolidone").setName("NMethylpyrolidone")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(249)
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 5),
                new MaterialStack(Materials.Hydrogen, 9),
                new MaterialStack(Materials.Nitrogen, 1),
                new MaterialStack(Materials.Oxygen, 1))
            .setAspects(
                Arrays.asList(
                    new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 1),
                    new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1)))
            .constructMaterial(); // C5H9NO
    public static Materials TerephthaloylChloride = new MaterialBuilder(
        782,
        TextureSet.SET_POWDER,
        "Terephthaloyl Chloride").setName("TerephthaloylChloride")
            .addDustItems()
            .setRGB(0, 255, 12)
            .setColor(Dyes.dyeGreen)
            .setMeltingPoint(355)
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 8),
                new MaterialStack(Materials.Hydrogen, 4),
                new MaterialStack(Materials.Chlorine, 2),
                new MaterialStack(Materials.Oxygen, 2))
            .setAspects(
                Arrays.asList(
                    new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 1),
                    new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 1)))
            .constructMaterial(); // C8H4Cl2O2
    public static Materials Acetylene = new MaterialBuilder(781, TextureSet.SET_FLUID, "Acetylene").setName("Acetylene")
        .addCell()
        .addGas()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(192)
        .setMaterialList(new MaterialStack(Materials.Carbon, 2), new MaterialStack(Materials.Hydrogen, 2))
        .constructMaterial(); // C2H2
                              // TODO
                              // Add
                              // to
                              // JUPITER
                              // Athmosphere
                              // and
                              // Enceladus
                              // and
                              // to
                              // moon
                              // of
                              // Saturn
    public static Materials IVNitroaniline = new MaterialBuilder(780, TextureSet.SET_FLUID, "4-Nitroaniline")
        .setName("4Nitroaniline")
        .addCell()
        .addFluid()
        .setRGB(255, 135, 51)
        .setColor(Dyes.dyeOrange)
        .setMeltingPoint(420)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 6),
            new MaterialStack(Materials.Hydrogen, 6),
            new MaterialStack(Materials.Nitrogen, 2),
            new MaterialStack(Materials.Oxygen, 2))
        .setAspects(
            Arrays.asList(
                new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 1),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 1)))
        .constructMaterial(); // C6H6N2O2
    public static Materials ParaPhenylenediamine = new MaterialBuilder(
        779,
        TextureSet.SET_POWDER,
        "para-Phenylenediamine").setName("pPhenylenediamine")
            .addDustItems()
            .setRGB(251, 236, 93)
            .setColor(Dyes.dyeYellow)
            .setMeltingPoint(293)
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 6),
                new MaterialStack(Materials.Hydrogen, 8),
                new MaterialStack(Materials.Nitrogen, 2))
            .setAspects(
                Arrays.asList(
                    new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 1),
                    new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 1)))
            .constructMaterial(); // C6H6N2
    public static Materials Methylamine = new MaterialBuilder(778, TextureSet.SET_FLUID, "Methylamine")
        .setName("Methylamine")
        .addCell()
        .addGas()
        .setRGB(65, 68, 105)
        .setColor(Dyes.dyeGray)
        .setMeltingPoint(180)
        .setExtraData(1)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 1),
            new MaterialStack(Materials.Hydrogen, 5),
            new MaterialStack(Materials.Nitrogen, 1))
        .constructMaterial(); // CH5N
    public static Materials Trimethylamine = new MaterialBuilder(777, TextureSet.SET_FLUID, "Trimethylamine")
        .setName("Trimethylamine")
        .addCell()
        .addGas()
        .setRGB(105, 68, 105)
        .setColor(Dyes.dyeGray)
        .setMeltingPoint(156)
        .setExtraData(1)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 3),
            new MaterialStack(Materials.Hydrogen, 9),
            new MaterialStack(Materials.Nitrogen, 1))
        .constructMaterial(); // C3H9N
    public static Materials GammaButyrolactone = new MaterialBuilder(776, TextureSet.SET_FLUID, "gamma-Butyrolactone")
        .setName("GammaButyrolactone")
        .addCell()
        .addFluid()
        .setRGB(255, 255, 151)
        .setColor(Dyes.dyeYellow)
        .setMeltingPoint(229)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 4),
            new MaterialStack(Materials.Hydrogen, 6),
            new MaterialStack(Materials.Oxygen, 2))
        .constructMaterial(); // C4H6O2
    public static Materials CalciumCarbide = new MaterialBuilder(775, TextureSet.SET_DULL, "Calcium Carbide")
        .setName("CacliumCarbide")
        .addDustItems()
        .setRGB(235, 235, 235)
        .setColor(Dyes.dyeGray)
        .setMeltingPoint(2430)
        .setMaterialList(new MaterialStack(Materials.Calcium, 1), new MaterialStack(Materials.Carbon, 2))
        .constructMaterial(); // CaC2
    public static Materials LiquidCrystalKevlar = new MaterialBuilder(
        774,
        TextureSet.SET_FLUID,
        "Liquid Crystal Kevlar").setName("LiquidCrystalKevlar")
            .addCell()
            .addFluid()
            .setRGB(240, 240, 120)
            .setColor(Dyes.dyeYellow)
            .constructMaterial(); // [-CO-C6H4-CO-NH-C6H4-NH-]n
    public static Materials IIButinIIVdiol = new MaterialBuilder(773, TextureSet.SET_POWDER, "2-Butin-1,4-diol")
        .setName("2Butin14diol")
        .addDustItems()
        .setRGB(247, 247, 180)
        .setColor(Dyes.dyeYellow)
        .setMeltingPoint(331)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 4),
            new MaterialStack(Materials.Hydrogen, 6),
            new MaterialStack(Materials.Oxygen, 2))
        .constructMaterial(); // C4H6O2
    public static Materials NickelAluminide = new MaterialBuilder(772, TextureSet.SET_METALLIC, "Nickel Aluminide")
        .setName("NickelAluminide")
        .addDustItems()
        .addMetalItems()
        .setRGB(230, 230, 230)
        .setColor(Dyes.dyeGray)
        .setMeltingPoint(1668)
        .setBlastFurnaceTemp(1668)
        .setBlastFurnaceRequired(true)
        .setMaterialList(new MaterialStack(Materials.Nickel, 1), new MaterialStack(Materials.Aluminium, 3))
        .constructMaterial()
        .disableAutoGeneratedBlastFurnaceRecipes(); // NiAl3
    public static Materials RaneyNickelActivated = new MaterialBuilder(771, TextureSet.SET_POWDER, "Raney Nickel")
        .setName("RaneyNickelActivated")
        .addDustItems()
        .setRGB(230, 230, 230)
        .setColor(Dyes.dyeGray)
        .setMeltingPoint(1955)
        .setMaterialList(new MaterialStack(Materials.Nickel, 1), new MaterialStack(Materials.Aluminium, 1))
        .constructMaterial(); // NiAl
    public static Materials BismuthIIIOxide = new MaterialBuilder(769, TextureSet.SET_POWDER, "Bismuth Oxide")
        .setName("BismuthIIIOxide")
        .addDustItems()
        .setRGB(50, 50, 50)
        .setColor(Dyes.dyeBlack)
        .setMeltingPoint(1090)
        .setMaterialList(new MaterialStack(Materials.Bismuth, 2), new MaterialStack(Materials.Oxygen, 3))
        .constructMaterial(); // Bi2O3
    public static Materials ThionylChloride = new MaterialBuilder(768, TextureSet.SET_FLUID, "Thionyl Chloride")
        .setName("ThionylChloride")
        .addCell()
        .addFluid()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .constructMaterial(); // SOCl2
    public static Materials SulfurDichloride = new MaterialBuilder(767, TextureSet.SET_FLUID, "Sulfur Dichloride")
        .setName("SulfurDichloride")
        .addCell()
        .addFluid()
        .setRGB(200, 0, 0)
        .setColor(Dyes.dyeRed)
        .constructMaterial(); // SCl2
    public static Materials DimethylTerephthalate = new MaterialBuilder(
        766,
        TextureSet.SET_FLUID,
        "Dimethyl Terephthalate").setName("DimethylTerephthalate")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(415)
            .setMaterialList(
                new MaterialStack(Materials.Carbon, 10),
                new MaterialStack(Materials.Hydrogen, 10),
                new MaterialStack(Materials.Oxygen, 4))
            .constructMaterial(); // C10H10O4
    public static Materials Kevlar = new MaterialBuilder(765, TextureSet.SET_DULL, "Kevlar").setName("Kevlar")
        .addDustItems()
        .addMetalItems()
        .addGearItems()
        .setRGB(240, 240, 120)
        .setColor(Dyes.dyeYellow)
        .constructMaterial();
    public static Materials TerephthalicAcid = new MaterialBuilder(764, TextureSet.SET_FLUID, "Terephthalic Acid")
        .setName("TerephthalicAcid")
        .addCell()
        .addFluid()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(480)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 8L),
            new MaterialStack(Materials.Hydrogen, 6),
            new MaterialStack(Materials.Oxygen, 4))
        .constructMaterial(); // C9H6O6
    public static Materials IIIDimethylbenzene = new MaterialBuilder(763, TextureSet.SET_FLUID, "1,3-Dimethylbenzen")
        .addCell()
        .addFluid()
        .setRGB(112, 146, 74)
        .setColor(Dyes.dyeLime)
        .setMeltingPoint(225)
        .setMaterialList(new MaterialStack(Materials.Carbon, 8), new MaterialStack(Materials.Hydrogen, 10))
        .addElectrolyzerRecipe()
        .constructMaterial(); // C8H10
    public static Materials IVDimethylbenzene = new MaterialBuilder(762, TextureSet.SET_FLUID, "1,4-Dimethylbenzen")
        .addCell()
        .addFluid()
        .setRGB(122, 136, 84)
        .setColor(Dyes.dyeLime)
        .setMeltingPoint(286)
        .setMaterialList(new MaterialStack(Materials.Carbon, 8), new MaterialStack(Materials.Hydrogen, 10))
        .addElectrolyzerRecipe()
        .constructMaterial(); // C8H10
    public static Materials CobaltIINaphthenate = new MaterialBuilder(761, TextureSet.SET_DULL, "Cobalt II Naphthenate")
        .setName("Cobalt(II)Naphthenate")
        .addDustItems()
        .setRGB(143, 95, 39)
        .setColor(Dyes.dyeBrown)
        .setMeltingPoint(413)
        .setMaterialList(
            new MaterialStack(Materials.Cobalt, 1),
            new MaterialStack(Materials.Carbon, 22),
            new MaterialStack(Materials.Hydrogen, 14),
            new MaterialStack(Materials.Oxygen, 4))
        .constructMaterial(); // CoC22H14O4
    public static Materials NaphthenicAcid = new MaterialBuilder(760, TextureSet.SET_FLUID, "Naphthenic Acid")
        .setName("NaphthenicAcid")
        .addCell()
        .addFluid()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setFuelType(MaterialBuilder.SEMIFLUID)
        .setFuelPower(80)
        .constructMaterial();
    public static Materials CobaltIIHydroxide = new MaterialBuilder(759, TextureSet.SET_POWDER, "Cobalt II Hydroxide")
        .setName("CobaltIIHydroxide")
        .addDustItems()
        .setRGB(229, 140, 239)
        .setColor(Dyes.dyePurple)
        .setMeltingPoint(441)
        .setMaterialList(
            new MaterialStack(Materials.Cobalt, 1),
            new MaterialStack(Materials.Hydrogen, 2),
            new MaterialStack(Materials.Oxygen, 2))
        .constructMaterial(); // CoH2O2
    public static Materials CobaltIIAcetate = new MaterialBuilder(758, TextureSet.SET_POWDER, "Cobalt II Acetate")
        .setName("Cobalt(II)Acetate")
        .addDustItems()
        .setRGB(219, 162, 229)
        .setColor(Dyes.dyePurple)
        .setMeltingPoint(413)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 4L),
            new MaterialStack(Materials.Hydrogen, 6),
            new MaterialStack(Materials.Cobalt, 1),
            new MaterialStack(Materials.Oxygen, 4))
        .constructMaterial(); // C4H6CoO4
    public static Materials CobaltIINitrate = new MaterialBuilder(757, TextureSet.SET_POWDER, "Cobalt II Nitrate")
        .setName("Cobalt(II)Nitrate")
        .addDustItems()
        .setRGB(170, 0, 0)
        .setColor(Dyes.dyeRed)
        .setMeltingPoint(373)
        .setMaterialList(
            new MaterialStack(Materials.Cobalt, 1),
            new MaterialStack(Materials.Nitrogen, 2),
            new MaterialStack(Materials.Oxygen, 6))
        .constructMaterial(); // Co(NO3)2
    public static Materials OrganorhodiumCatalyst = new MaterialBuilder(
        756,
        TextureSet.SET_POWDER,
        "Organorhodium Catalyst").setName("OrganorhodiumCatalyst")
            .addDustItems()
            .setRGB(170, 0, 0)
            .setColor(Dyes.dyeRed)
            .setMeltingPoint(373)
            .setMaterialList(new MaterialStack(Materials.Cobalt, 1), new MaterialStack(Materials.NitricAcid, 2))
            .constructMaterial(); // RhHCO(P(C6H5)3)3
    public static Materials SodiumBorohydride = new MaterialBuilder(755, TextureSet.SET_POWDER, "Sodium Borohydride")
        .setName("SodiumBorohydride")
        .addDustItems()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(673)
        .setMaterialList(
            new MaterialStack(Materials.Sodium, 1),
            new MaterialStack(Materials.Boron, 1),
            new MaterialStack(Materials.Hydrogen, 4))
        .constructMaterial(); // NaBH4
    public static Materials RhodiumChloride = new MaterialBuilder(754, TextureSet.SET_POWDER, "Rhodium Chloride")
        .setName("RhodiumChloride")
        .addDustItems()
        .setRGB(128, 0, 0)
        .setColor(Dyes.dyeRed)
        .setMeltingPoint(723)
        .constructMaterial(); // RHCL3
    public static Materials Triphenylphosphene = new MaterialBuilder(753, TextureSet.SET_POWDER, "Triphenylphosphine")
        .setName("Triphenylphosphene")
        .addDustItems()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(353)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 18L),
            new MaterialStack(Materials.Hydrogen, 15L),
            new MaterialStack(Materials.Phosphorus, 1L))
        .constructMaterial(); // C18H15P
    public static Materials PhosphorusTrichloride = new MaterialBuilder(
        752,
        TextureSet.SET_FLUID,
        "Phosphorus Trichloride").setName("PhosphorusTrichloride")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(179)
            .setMaterialList(new MaterialStack(Materials.Phosphorus, 1L), new MaterialStack(Materials.Chlorine, 3L))
            .constructMaterial(); // PCL3
    public static Materials SodiumHydride = new MaterialBuilder(751, TextureSet.SET_POWDER, "Sodium Hydride")
        .setName("SodiumHydride")
        .addDustItems()
        .setRGB(192, 192, 192)
        .setColor(Dyes.dyeLightGray)
        .setMeltingPoint(911)
        .setMaterialList(new MaterialStack(Materials.Sodium, 1L), new MaterialStack(Materials.Hydrogen, 1L))
        .constructMaterial(); // NaH
    public static Materials TrimethylBorate = new MaterialBuilder(750, TextureSet.SET_FLUID, "Trimethyl Borate")
        .setName("TrimethylBorate")
        .addCell()
        .addFluid()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(239)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 3L),
            new MaterialStack(Materials.Hydrogen, 9L),
            new MaterialStack(Materials.Boron, 1L),
            new MaterialStack(Materials.Oxygen, 3L))
        .constructMaterial(); // C3H9BO3
    public static Materials SodiumMethoxide = new MaterialBuilder(749, TextureSet.SET_POWDER, "Sodium Methoxide")
        .setName("SodiumMethoxide")
        .addDustItems()
        .setRGB(255, 255, 255)
        .setColor(Dyes.dyeWhite)
        .setMeltingPoint(400)
        .setMaterialList(
            new MaterialStack(Materials.Carbon, 1L),
            new MaterialStack(Materials.Hydrogen, 3L),
            new MaterialStack(Materials.Oxygen, 1L),
            new MaterialStack(Materials.Sodium, 1L))
        .constructMaterial(); // CH3NaO

    // H3RhCl6

    /**
     * This method is called by Materials. It can be safely called multiple times.
     * It exists to allow Materials ensure this class is initialized.
     */
    public static void init() {
        // no-op. all work is done by <clinit>
    }
}
