package gregtech.api.enums;

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
    }

    private static Materials loadDimensionallyTranscendentCrudeCatalyst() {
        return new MaterialBuilder().setName("DimensionallyTranscendentCrudeCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Crude Catalyst")
            .setMetaItemSubID(748)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .setARGB(0x010a1414)
            .addCell()
            .setMeltingPoint(25_000_000)
            .setBlastFurnaceTemp(1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadDimensionallyTranscendentProsaicCatalyst() {
        return new MaterialBuilder().setName("DimensionallyTranscendentProsaicCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Prosaic Catalyst")
            .setMetaItemSubID(747)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x010a1414)
            .addCell()
            .setMeltingPoint(50_000_000)
            .setBlastFurnaceTemp(1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadDimensionallyTranscendentResplendentCatalyst() {
        return new MaterialBuilder().setName("DimensionallyTranscendentResplendentCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Resplendent Catalyst")
            .setMetaItemSubID(746)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setARGB(0x010a1414)
            .addCell()
            .setMeltingPoint(75_000_000)
            .setBlastFurnaceTemp(1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadDimensionallyTranscendentExoticCatalyst() {
        return new MaterialBuilder().setName("DimensionallyTranscendentExoticCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Exotic Catalyst")
            .setMetaItemSubID(745)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeMagenta)
            .setARGB(0x010a1414)
            .addCell()
            .setMeltingPoint(100_000_000)
            .setBlastFurnaceTemp(1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadDimensionallyTranscendentStellarCatalyst() {
        return new MaterialBuilder().setName("DimensionallyTranscendentStellarCatalyst")
            .setDefaultLocalName("Dimensionally Transcendent Stellar Catalyst")
            .setMetaItemSubID(130)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x010a1414)
            .addCell()
            .setMeltingPoint(100_000_000)
            .setBlastFurnaceTemp(1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
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
            .constructMaterial();
    }

    private static Materials loadExcitedDTSC() {
        return new MaterialBuilder().setName("ExcitedDTSC")
            .setDefaultLocalName("Excited Dimensionally Transcendent Stellar Catalyst")
            .setMetaItemSubID(127)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x017e4b0b)
            .addCell()
            .setMeltingPoint(500_000_000)
            .setBlastFurnaceTemp(1)
            .constructMaterial();
    }

    private static Materials loadDimensionallyTranscendentResidue() {
        return new MaterialBuilder().setName("DimensionallyTranscendentResidue")
            .setDefaultLocalName("Dimensionally Transcendent Residue")
            .setMetaItemSubID(589)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x01000000)
            .addCell()
            .setMeltingPoint(25)
            .setBlastFurnaceTemp(1)
            .constructMaterial();
    }

    private static Materials loadSpaceTime() {
        return new MaterialBuilder().setName("SpaceTime")
            .setDefaultLocalName("SpaceTime")
            .setMetaItemSubID(588)
            .setIconSet(new TextureSet("spacetime", true))
            .setTool(10_485_760, 25, 320.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadTranscendentMetal() {
        return new MaterialBuilder().setName("TranscendentMetal")
            .setDefaultLocalName("Transcendent Metal")
            .setMetaItemSubID(581)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x323232)
            .setTool(7864320, 22, 290.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(3_000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .setDensityMultiplier(1_000)
            .setDensityDivider(1_000)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UHV);
    }

    private static Materials loadMagnetohydrodynamicallyConstrainedStarMatter() {
        return new MaterialBuilder().setName("MagnetohydrodynamicallyConstrainedStarMatter")
            .setDefaultLocalName("Magnetohydrodynamically Constrained Star Matter")
            .setMetaItemSubID(583)
            .setIconSet(new TextureSet("MagnetohydrodynamicallyConstrainedStarMatter", true))
            .setTool(10485760, 25, 320.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UIV);
    }

    private static Materials loadRawStarMatter() {
        return new MaterialBuilder().setName("RawStarMatter")
            .setDefaultLocalName("Condensed Raw Stellar Plasma Mixture")
            .setMetaItemSubID(584)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePurple)
            .setARGB(0xff6401ff)
            .addCell()
            .addPlasma()
            .constructMaterial();
    }

    private static Materials loadWhiteDwarfMatter() {
        return new MaterialBuilder().setName("WhiteDwarfMatter")
            .setDefaultLocalName("White Dwarf Matter")
            .setMetaItemSubID(585)
            .setIconSet(new TextureSet("WhiteDwarfMatter", true))
            .setColor(Dyes.dyePurple)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .constructMaterial()
            .setHasCorrespondingFluid(true)
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadBlackDwarfMatter() {
        return new MaterialBuilder().setName("BlackDwarfMatter")
            .setDefaultLocalName("Black Dwarf Matter")
            .setMetaItemSubID(586)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePurple)
            .setARGB(0xff000000)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .constructMaterial()
            .setHasCorrespondingFluid(true)
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
            .addPlasma()
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
            .addPlasma()
            .constructMaterial();
    }

    private static Materials loadUniversium() {
        return new MaterialBuilder().setName("Universium")
            .setDefaultLocalName("Universium")
            .setMetaItemSubID(139)
            .setIconSet(new TextureSet("universium", true))
            .setARGB(0xff263145)
            .setTool(10_485_760, 25, 1.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UMV);
    }

    private static Materials loadEternity() {
        return new MaterialBuilder().setName("Eternity")
            .setDefaultLocalName("Eternity")
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
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UMV);
    }

    private static Materials loadPrimordialMatter() {
        return new MaterialBuilder().setName("PrimordialMatter")
            .setDefaultLocalName("Liquid Primordial Matter")
            .setMetaItemSubID(142)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .addCell()
            .setMeltingPoint(2_000_000_000)
            .setBlastFurnaceTemp(1)
            .constructMaterial();
    }

    private static Materials loadMagMatter() {
        return new MaterialBuilder().setName("Magmatter")
            .setDefaultLocalName("Magmatter")
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
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UMV);
    }

    private static Materials loadQuarkGluonPlasma() {
        return new MaterialBuilder().setName("QuarkGluonPlasma")
            .setDefaultLocalName("Degenerate Quark Gluon Plasma")
            .setMetaItemSubID(144)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .setMeltingPoint(2_000_000_000)
            .setBlastFurnaceTemp(1)
            .constructMaterial();
    }

    private static Materials loadPhononMedium() {
        return new MaterialBuilder().setName("PhononMedium")
            .setDefaultLocalName("Lossless Phonon Transfer Medium")
            .setMetaItemSubID(145)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .setMeltingPoint(500)
            .setBlastFurnaceTemp(1)
            .constructMaterial();
    }

    private static Materials loadPhononCrystalSolution() {
        return new MaterialBuilder().setName("PhononCrystalSolution")
            .setDefaultLocalName("Saturated Phononic Crystal Solution")
            .setMetaItemSubID(146)
            .setIconSet(TextureSet.SET_FLUID)
            .addCell()
            .setMeltingPoint(500)
            .setBlastFurnaceTemp(1)
            .constructMaterial();
    }

    private static Materials loadSixPhasedCopper() {
        return new MaterialBuilder().setName("SixPhasedCopper")
            .setDefaultLocalName("Six-Phased Copper")
            .setMetaItemSubID(147)
            .setIconSet(TextureSet.SET_SHINY)
            .setRGB(0xff7814)
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
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV)
            .setHasCorrespondingPlasma(true);
    }

    private static Materials loadMellion() {
        return new MaterialBuilder().setName("Mellion")
            .setDefaultLocalName("Mellion")
            .setMetaItemSubID(148)
            .setIconSet(TextureSet.SET_SHINY)
            .setRGB(0x3c0505)
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
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadCreon() {
        return new MaterialBuilder().setName("Creon")
            .setDefaultLocalName("Creon")
            .setMetaItemSubID(149)
            .setIconSet(TextureSet.SET_SHINY)
            .setRGB(0x460046)
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
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV)
            .setHasCorrespondingPlasma(true);
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
        return new MaterialBuilder(
            151,
            new TextureSet("dimensionallyshiftedsuperfluid", true),
            "Dimensionally Shifted Superfluid").addCell()
                .addFluid()
                .setRGBA(255, 255, 255, 0)
                .setTransparent(true)
                .setName("dimensionallyshiftedsuperfluid")
                .setColor(Dyes._NULL)
                .constructMaterial()
                .setHasCorrespondingFluid(true);
    }

    private static Materials loadMoltenProtoHalkoniteBase() {
        return new MaterialBuilder(152, new TextureSet("protohalkonitebase", true), "Molten Proto-Halkonite Steel Base")
            .setName("protohalkonitebase")
            .addFluid()
            .addCell()
            .setRGBA(255, 255, 255, 0)
            .setTransparent(true)
            .setColor(Dyes._NULL)
            .constructMaterial()
            .disableAutoGeneratedVacuumFreezerRecipe()
            .disableAutoGeneratedBlastFurnaceRecipes()
            .disableAutoGeneratedRecycleRecipes();
    }

    private static Materials loadHotProtoHalkonite() {
        return new MaterialBuilder(153, new TextureSet("hotprotohalkonite", true), "Hot Proto-Halkonite Steel")
            .setName("hotprotohalkonite")
            .setTypes(1 | 2 | 64 | 128)
            .setRGBA(255, 255, 255, 0)
            .setTransparent(false)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV)
            .disableAutoGeneratedVacuumFreezerRecipe()
            .disableAutoGeneratedBlastFurnaceRecipes()
            .disableAutoGeneratedRecycleRecipes();
    }

    private static Materials loadProtoHalkonite() {
        return new MaterialBuilder(154, new TextureSet("protohalkonite", true), "Proto-Halkonite Steel")
            .setName("protohalkonite")
            .setTypes(1 | 2 | 64 | 128)
            .setRGBA(255, 255, 255, 0)
            .setTransparent(false)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV)
            .disableAutoGeneratedVacuumFreezerRecipe()
            .disableAutoGeneratedBlastFurnaceRecipes();
    }

    private static Materials loadMoltenExoHalkoniteBase() {
        return new MaterialBuilder(155, TextureSet.SET_FLUID, "Molten Exo-Halkonite Steel Preparation Base")
            .setName("moltenexohalkonitebase")
            .addFluid()
            .addCell()
            .setRGBA(30, 30, 30, 0)
            .setTransparent(false)
            .constructMaterial()
            .disableAutoGeneratedVacuumFreezerRecipe()
            .disableAutoGeneratedBlastFurnaceRecipes()
            .disableAutoGeneratedRecycleRecipes();
    }

    private static Materials loadHotExoHalkonite() {
        return new MaterialBuilder(156, new TextureSet("hotexohalkonite", true), "Hot Exo-Halkonite Steel")
            .setName("hotexohalkonite")
            .setTypes(1 | 2 | 64 | 128)
            .setRGBA(255, 255, 255, 0)
            .setTransparent(false)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV)
            .disableAutoGeneratedVacuumFreezerRecipe()
            .disableAutoGeneratedBlastFurnaceRecipes()
            .disableAutoGeneratedRecycleRecipes();
    }

    private static Materials loadExoHalkonite() {
        return new MaterialBuilder(157, new TextureSet("exohalkonite", true), "Exo-Halkonite Steel")
            .setName("exohalkonite")
            .setTypes(1 | 2 | 64 | 128)
            .setRGBA(255, 255, 255, 0)
            .setTransparent(false)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV)
            .disableAutoGeneratedVacuumFreezerRecipe()
            .disableAutoGeneratedBlastFurnaceRecipes();
    }

    private static Materials loadAntimatter() {
        return new Materials(
            158,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            255,
            255,
            255,
            0,
            "Antimatter",
            "Semi-Stable Antimatter",
            -1,
            -1,
            0,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes._NULL);
    }

    private static Materials loadProtomatter() {
        return new Materials(
            159,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            255,
            255,
            255,
            0,
            "Protomatter",
            "Protomatter",
            -1,
            -1,
            0,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes._NULL);
    }

    private static Materials loadStargateCrystalSlurry() {
        return new MaterialBuilder(160, new TextureSet("sgcrystalfluid", true), "Stargate Crystal Slurry")
            .setName("sgcrystalslurry")
            .addFluid()
            .addCell()
            .setRGBA(255, 255, 255, 0)
            .setTransparent(false)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_MAX);
    }
}
