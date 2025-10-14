package gregtech.api.enums;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.util.CustomGlyphs;

public class MaterialsUEVplus {

    public static Materials DimensionallyTranscendentCrudeCatalyst;
    public static Materials DimensionallyTranscendentProsaicCatalyst;
    public static Materials DimensionallyTranscendentResplendentCatalyst;
    public static Materials DimensionallyTranscendentExoticCatalyst;
    public static Materials DimensionallyTranscendentStellarCatalyst;
    public static Materials ExcitedDTCC;
    public static Materials ExcitedDTPC;
    public static Materials ExcitedDTRC;
    public static Materials ExcitedDTEC;
    public static Materials ExcitedDTSC;
    public static Materials DimensionallyTranscendentResidue;
    public static Materials SpaceTime;
    public static Materials TranscendentMetal;
    public static Materials MagnetohydrodynamicallyConstrainedStarMatter;
    public static Materials RawStarMatter;
    public static Materials WhiteDwarfMatter;
    public static Materials BlackDwarfMatter;
    public static Materials Time;
    public static Materials Space;
    public static Materials Universium;
    public static Materials Eternity;
    public static Materials PrimordialMatter;
    public static Materials MagMatter;
    public static Materials QuarkGluonPlasma;
    public static Materials PhononMedium;
    public static Materials PhononCrystalSolution;
    public static Materials SixPhasedCopper;
    public static Materials Mellion;
    public static Materials Creon;
    public static Materials GravitonShard;
    public static Materials DimensionallyShiftedSuperfluid;
    public static Materials MoltenProtoHalkoniteBase;
    public static Materials HotProtoHalkonite;
    public static Materials ProtoHalkonite;
    public static Materials MoltenExoHalkoniteBase;
    public static Materials HotExoHalkonite;
    public static Materials ExoHalkonite;
    public static Materials Antimatter;
    public static Materials Protomatter;
    public static Materials StargateCrystalSlurry;
    public static Materials LumipodExtract;
    public static Materials BiocatalyzedPropulsionFluid;

    public static void load() {
        MaterialsUEVplus.DimensionallyTranscendentCrudeCatalyst = loadDimensionallyTranscendentCrudeCatalyst();
        MaterialsUEVplus.DimensionallyTranscendentProsaicCatalyst = loadDimensionallyTranscendentProsaicCatalyst();
        MaterialsUEVplus.DimensionallyTranscendentResplendentCatalyst = loadDimensionallyTranscendentResplendentCatalyst();
        MaterialsUEVplus.DimensionallyTranscendentExoticCatalyst = loadDimensionallyTranscendentExoticCatalyst();
        MaterialsUEVplus.DimensionallyTranscendentStellarCatalyst = loadDimensionallyTranscendentStellarCatalyst();
        MaterialsUEVplus.ExcitedDTCC = loadExcitedDTCC();
        MaterialsUEVplus.ExcitedDTPC = loadExcitedDTPC();
        MaterialsUEVplus.ExcitedDTRC = loadExcitedDTRC();
        MaterialsUEVplus.ExcitedDTEC = loadExcitedDTEC();
        MaterialsUEVplus.ExcitedDTSC = loadExcitedDTSC();
        MaterialsUEVplus.DimensionallyTranscendentResidue = loadDimensionallyTranscendentResidue();
        MaterialsUEVplus.SpaceTime = loadSpaceTime();
        MaterialsUEVplus.TranscendentMetal = loadTranscendentMetal();
        MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter = loadMagnetohydrodynamicallyConstrainedStarMatter();
        MaterialsUEVplus.RawStarMatter = loadRawStarMatter();
        MaterialsUEVplus.WhiteDwarfMatter = loadWhiteDwarfMatter();
        MaterialsUEVplus.BlackDwarfMatter = loadBlackDwarfMatter();
        MaterialsUEVplus.Time = loadTime();
        MaterialsUEVplus.Space = loadSpace();
        MaterialsUEVplus.Universium = loadUniversium();
        MaterialsUEVplus.Eternity = loadEternity();
        MaterialsUEVplus.PrimordialMatter = loadPrimordialMatter();
        MaterialsUEVplus.MagMatter = loadMagMatter();
        MaterialsUEVplus.QuarkGluonPlasma = loadQuarkGluonPlasma();
        MaterialsUEVplus.PhononMedium = loadPhononMedium();
        MaterialsUEVplus.PhononCrystalSolution = loadPhononCrystalSolution();
        MaterialsUEVplus.SixPhasedCopper = loadSixPhasedCopper();
        MaterialsUEVplus.Mellion = loadMellion();
        MaterialsUEVplus.Creon = loadCreon();
        MaterialsUEVplus.GravitonShard = loadGravitonShard();
        MaterialsUEVplus.DimensionallyShiftedSuperfluid = loadDimensionallyShiftedSuperfluid();
        MaterialsUEVplus.MoltenProtoHalkoniteBase = loadMoltenProtoHalkoniteBase();
        MaterialsUEVplus.HotProtoHalkonite = loadHotProtoHalkonite();
        MaterialsUEVplus.ProtoHalkonite = loadProtoHalkonite();
        MaterialsUEVplus.MoltenExoHalkoniteBase = loadMoltenExoHalkoniteBase();
        MaterialsUEVplus.HotExoHalkonite = loadHotExoHalkonite();
        MaterialsUEVplus.ExoHalkonite = loadExoHalkonite();
        MaterialsUEVplus.Antimatter = loadAntimatter();
        MaterialsUEVplus.Protomatter = loadProtomatter();
        MaterialsUEVplus.StargateCrystalSlurry = loadStargateCrystalSlurry();
        MaterialsUEVplus.LumipodExtract = loadLumipodExtract();
        MaterialsUEVplus.BiocatalyzedPropulsionFluid = loadBiocatalyzedPropulsionFluid();
    }

    private static Materials loadDimensionallyTranscendentCrudeCatalyst() {
        return new MaterialBuilder().setName("DimensionallyTranscendentCrudeCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Crude Catalyst")
            .setMetaItemSubID(748)
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

    private static Materials loadDimensionallyTranscendentProsaicCatalyst() {
        return new MaterialBuilder().setName("DimensionallyTranscendentProsaicCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Prosaic Catalyst")
            .setMetaItemSubID(747)
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

    private static Materials loadDimensionallyTranscendentResplendentCatalyst() {
        return new MaterialBuilder().setName("DimensionallyTranscendentResplendentCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Resplendent Catalyst")
            .setMetaItemSubID(746)
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

    private static Materials loadDimensionallyTranscendentExoticCatalyst() {
        return new MaterialBuilder().setName("DimensionallyTranscendentExoticCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Exotic Catalyst")
            .setMetaItemSubID(745)
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

    private static Materials loadDimensionallyTranscendentStellarCatalyst() {
        return new MaterialBuilder().setName("DimensionallyTranscendentStellarCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Stellar Catalyst")
            .setChemicalFormula("Stellar")
            .setMetaItemSubID(130)
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
            .setMetaItemSubID(109)
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
            .setMetaItemSubID(113)
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
            .setMetaItemSubID(121)
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
            .setMetaItemSubID(126)
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
            .setMetaItemSubID(127)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x017e4b0b)
            .addCell()
            .setMeltingPoint(500_000_000)
            .setBlastFurnaceTemp(1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadDimensionallyTranscendentResidue() {
        return new MaterialBuilder().setName("DimensionallyTranscendentResidue")
            .setDefaultLocalName("Dimensionally Transcendent Residue")
            .setChemicalFormula(CustomGlyphs.SPARKLES + "-" + CustomGlyphs.EMPTY_SET)
            .setMetaItemSubID(589)
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
            .setChemicalFormula("Reality itself distilled into physical form")
            .setMetaItemSubID(588)
            .setIconSet(new TextureSet("spacetime", true))
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
            .setMetaItemSubID(581)
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
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UHV);
    }

    private static Materials loadMagnetohydrodynamicallyConstrainedStarMatter() {
        return new MaterialBuilder().setName("MagnetohydrodynamicallyConstrainedStarMatter")
            .setDefaultLocalName("Magnetohydrodynamically Constrained Star Matter")
            .setChemicalFormula(
                "⇲" + CustomGlyphs.ARROW_CORNER_SOUTH_EAST
                    + CustomGlyphs.GALAXY
                    + CustomGlyphs.ARROW_CORNER_NORTH_WEST
                    + "⇱")
            .setMetaItemSubID(583)
            .setIconSet(new TextureSet("MagnetohydrodynamicallyConstrainedStarMatter", true))
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
            .removeOrePrefix(OrePrefixes.cell)
            .removeOrePrefix(OrePrefixes.turbineBlade)
            .removeOrePrefix(OrePrefixes.dust)
            .removeOrePrefix(OrePrefixes.dustSmall)
            .removeOrePrefix(OrePrefixes.dustTiny)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UIV);
    }

    private static Materials loadRawStarMatter() {
        return new MaterialBuilder().setName("RawStarMatter")
            .setDefaultLocalName("Condensed Raw Stellar Plasma Mixture")
            .setChemicalFormula(CustomGlyphs.GALAXY)
            .setMetaItemSubID(584)
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
            .setMetaItemSubID(585)
            .setIconSet(new TextureSet("WhiteDwarfMatter", true))
            .setColor(Dyes.dyePurple)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addFluid()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadBlackDwarfMatter() {
        return new MaterialBuilder().setName("BlackDwarfMatter")
            .setDefaultLocalName("Black Dwarf Matter")
            .setChemicalFormula(">>∅<<")
            .setMetaItemSubID(586)
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
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadTime() {
        return new MaterialBuilder().setName("temporalFluid")
            .setDefaultLocalName("Tachyon Rich Temporal Fluid")
            .setMetaItemSubID(587)
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
            .setMetaItemSubID(106)
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
            .setChemicalFormula("A tear into the space beyond space")
            .setMetaItemSubID(139)
            .setIconSet(new TextureSet("universium", true))
            .setARGB(0xff263145)
            .setTool(10_485_760, 30, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.TRANSPARENT)
            .removeOrePrefix(OrePrefixes.frame)
            .removeOrePrefix(OrePrefixes.frameGt) // disabled but shows up and is used in game
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UMV);
    }

    private static Materials loadEternity() {
        return new MaterialBuilder().setName("Eternity")
            .setDefaultLocalName("Eternity")
            .setChemicalFormula("En⦼")
            .setMetaItemSubID(141)
            .setIconSet(new TextureSet("eternity", true))
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
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UMV);
    }

    private static Materials loadPrimordialMatter() {
        return new MaterialBuilder().setName("PrimordialMatter")
            .setDefaultLocalName("Liquid Primordial Matter")
            .setMetaItemSubID(142)
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
            .setMetaItemSubID(143)
            .setIconSet(new TextureSet("magmatter", true))
            .setTool(167_772_160, 26, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(25_000)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.AQUA, 1)
            .addSubTag(SubTag.METAL)
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
            .setMetaItemSubID(144)
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
            .setMetaItemSubID(145)
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
            .setMetaItemSubID(146)
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
            .setMetaItemSubID(147)
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
            .setMetaItemSubID(148)
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
            .setMetaItemSubID(149)
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
            .setMetaItemSubID(150)
            .setIconSet(new TextureSet("GravitonShard", true))
            .setTool(20_971_520, 26, 1.0f)
            .setMeltingPoint(100_000)
            .setBlastFurnaceTemp(100_000)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.VACUOS, 150)
            .constructMaterial();
    }

    private static Materials loadDimensionallyShiftedSuperfluid() {
        return new MaterialBuilder().setName("dimensionallyshiftedsuperfluid")
            .setDefaultLocalName("Dimensionally Shifted Superfluid")
            .setMetaItemSubID(151)
            .setIconSet(new TextureSet("dimensionallyshiftedsuperfluid", true))
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
                "(" + MaterialsUEVplus.TranscendentMetal.mChemicalFormula
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
            .setMetaItemSubID(152)
            .setIconSet(new TextureSet("protohalkonitebase", true))
            .setARGB(0x00ffffff)
            .addFluid()
            .addCell()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .setAutoGeneratedRecycleRecipes(false)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadHotProtoHalkonite() {
        return new MaterialBuilder().setName("hotprotohalkonite")
            .setDefaultLocalName("Hot Proto-Halkonite Steel")
            .setChemicalFormula(MaterialsUEVplus.MoltenProtoHalkoniteBase.mChemicalFormula)
            .setMetaItemSubID(153)
            .setIconSet(new TextureSet("hotprotohalkonite", true))
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
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadProtoHalkonite() {
        return new MaterialBuilder().setName("protohalkonite")
            .setDefaultLocalName("Proto-Halkonite Steel")
            .setChemicalFormula(MaterialsUEVplus.MoltenProtoHalkoniteBase.mChemicalFormula)
            .setMetaItemSubID(154)
            .setIconSet(new TextureSet("protohalkonite", true))
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.NO_RECIPES)
            .addSubTag(SubTag.NO_RECYCLING_RECIPES)
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
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadMoltenExoHalkoniteBase() {
        return new MaterialBuilder().setName("moltenexohalkonitebase")
            .setDefaultLocalName("Molten Exo-Halkonite Steel Preparation Base")
            .setMetaItemSubID(155)
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
            .setMetaItemSubID(156)
            .setIconSet(new TextureSet("hotexohalkonite", true))
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
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadExoHalkonite() {
        return new MaterialBuilder().setName("exohalkonite")
            .setDefaultLocalName("Exo-Halkonite Steel")
            .setMetaItemSubID(157)
            .setIconSet(new TextureSet("exohalkonite", true))
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addSubTag(SubTag.METAL)
            .addSubTag(SubTag.NO_RECIPES)
            .addSubTag(SubTag.NO_RECYCLING_RECIPES)
            .addSubTag(SubTag.SMELTING_TO_FLUID)
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
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadAntimatter() {
        return new MaterialBuilder().setName("Antimatter")
            .setDefaultLocalName("Semi-Stable Antimatter")
            .setMetaItemSubID(158)
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
            .setMetaItemSubID(159)
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
            .setMetaItemSubID(160)
            .setIconSet(new TextureSet("sgcrystalfluid", true))
            .addFluid()
            .addCell()
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_MAX);
    }

    private static Materials loadLumipodExtract() {
        return new MaterialBuilder().setName("BrightLumipodExtract")
            .setDefaultLocalName("Bright Lumipod Extract")
            .setMetaItemSubID(165)
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
            .setMetaItemSubID(166)
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
}
