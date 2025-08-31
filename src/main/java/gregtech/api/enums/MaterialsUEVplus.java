package gregtech.api.enums;

import java.util.Collections;

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
        return new Materials(
            748,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            10,
            20,
            20,
            1,
            "DimensionallyTranscendentCrudeCatalyst",
            "Dimensionally Transcendent Crude Catalyst",
            0,
            0,
            25_000_000,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes.dyeCyan).setHasCorrespondingFluid(true);
    }

    private static Materials loadDimensionallyTranscendentProsaicCatalyst() {
        return new Materials(
            747,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            10,
            20,
            20,
            1,
            "DimensionallyTranscendentProsaicCatalyst",
            "Dimensionally Transcendent Prosaic Catalyst",
            0,
            0,
            50_000_000,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes.dyeGreen).setHasCorrespondingFluid(true);
    }

    private static Materials loadDimensionallyTranscendentResplendentCatalyst() {
        return new Materials(
            746,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            10,
            20,
            20,
            1,
            "DimensionallyTranscendentResplendentCatalyst",
            "Dimensionally Transcendent Resplendent Catalyst",
            0,
            0,
            75_000_000,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes.dyeLime).setHasCorrespondingFluid(true);
    }

    private static Materials loadDimensionallyTranscendentExoticCatalyst() {
        return new Materials(
            745,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            10,
            20,
            20,
            1,
            "DimensionallyTranscendentExoticCatalyst",
            "Dimensionally Transcendent Exotic Catalyst",
            0,
            0,
            100_000_000,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes.dyeMagenta).setHasCorrespondingFluid(true);
    }

    private static Materials loadDimensionallyTranscendentStellarCatalyst() {
        return new Materials(
            130,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            10,
            20,
            20,
            1,
            "DimensionallyTranscendentStellarCatalyst",
            "Dimensionally Transcendent Stellar Catalyst",
            0,
            0,
            100_000_000,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes.dyeOrange).setHasCorrespondingFluid(true);
    }

    private static Materials loadExcitedDTCC() {
        return new Materials(
            109,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            10,
            20,
            20,
            1,
            "ExcitedDTCC",
            "Excited Dimensionally Transcendent Crude Catalyst",
            -1,
            -1,
            500000000,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes.dyeCyan);
    }

    private static Materials loadExcitedDTPC() {
        return new Materials(
            113,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            35,
            59,
            41,
            1,
            "ExcitedDTPC",
            "Excited Dimensionally Transcendent Prosaic Catalyst",
            -1,
            -1,
            500000000,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes.dyeGreen);
    }

    private static Materials loadExcitedDTRC() {
        return new Materials(
            121,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            38,
            20,
            56,
            1,
            "ExcitedDTRC",
            "Excited Dimensionally Transcendent Resplendent Catalyst",
            -1,
            -1,
            500000000,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes.dyeLime);
    }

    private static Materials loadExcitedDTEC() {
        return new Materials(
            126,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            240,
            240,
            41,
            1,
            "ExcitedDTEC",
            "Excited Dimensionally Transcendent Exotic Catalyst",
            -1,
            -1,
            500000000,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes.dyeMagenta);
    }

    private static Materials loadExcitedDTSC() {
        return new Materials(
            127,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            126,
            75,
            11,
            1,
            "ExcitedDTSC",
            "Excited Dimensionally Transcendent Stellar Catalyst",
            -1,
            -1,
            500000000,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes.dyeOrange);
    }

    private static Materials loadDimensionallyTranscendentResidue() {
        return new Materials(
            589,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            0,
            0,
            0,
            1,
            "DimensionallyTranscendentResidue",
            "Dimensionally Transcendent Residue",
            -1,
            -1,
            25,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes.dyeBlack);
    }

    private static Materials loadSpaceTime() {
        return new Materials(
            588,
            new TextureSet("spacetime", true),
            320.0F,
            4 * 2621440,
            25,
            1 | 2 | 64 | 128,
            255,
            255,
            255,
            0,
            "SpaceTime",
            "SpaceTime",
            -1,
            -1,
            0,
            0,
            false,
            true,
            2,
            1,
            1,
            Dyes._NULL,
            Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1)))
                .setProcessingMaterialTierEU(TierEU.RECIPE_UEV)
                .disableAutoGeneratedBlastFurnaceRecipes()
                .disableAutoGeneratedVacuumFreezerRecipe();
    }

    private static Materials loadTranscendentMetal() {
        return new Materials(
            581,
            TextureSet.SET_METALLIC,
            290.0F,
            3 * 2621440,
            22,
            1 | 2 | 64 | 128,
            50,
            50,
            50,
            0,
            "TranscendentMetal",
            "Transcendent Metal",
            -1,
            -1,
            0,
            3000,
            true,
            true,
            200,
            1000,
            1000,
            Dyes.dyeBlack,
            Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1)))
                .disableAutoGeneratedBlastFurnaceRecipes()
                .disableAutoGeneratedVacuumFreezerRecipe()
                .setProcessingMaterialTierEU(TierEU.RECIPE_UHV);
    }

    private static Materials loadMagnetohydrodynamicallyConstrainedStarMatter() {
        return new Materials(
            583,
            new TextureSet("MagnetohydrodynamicallyConstrainedStarMatter", true),
            320.0F,
            4 * 2621440,
            25,
            1 | 2 | 64 | 128,
            255,
            255,
            255,
            0,
            "MagnetohydrodynamicallyConstrainedStarMatter",
            "Magnetohydrodynamically Constrained Star Matter",
            -1,
            -1,
            0,
            0,
            false,
            true,
            2,
            1,
            1,
            Dyes._NULL,
            Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1)))
                .setProcessingMaterialTierEU(TierEU.RECIPE_UIV);
    }

    private static Materials loadRawStarMatter() {
        return new Materials(
            584,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16 | 32,
            100,
            1,
            255,
            255,
            "RawStarMatter",
            "Condensed Raw Stellar Plasma Mixture",
            -1,
            -1,
            0,
            0,
            false,
            false,
            200,
            1,
            1,
            Dyes.dyePurple);
    }

    private static Materials loadWhiteDwarfMatter() {
        return new Materials(
            585,
            new TextureSet("WhiteDwarfMatter", true),
            1.0F,
            0,
            2,
            1 | 2 | 64 | 128,
            255,
            255,
            255,
            0,
            "WhiteDwarfMatter",
            "White Dwarf Matter",
            -1,
            -1,
            0,
            0,
            false,
            false,
            200,
            1,
            1,
            Dyes.dyePurple).setHasCorrespondingFluid(true)
                .setProcessingMaterialTierEU(TierEU.RECIPE_UEV)
                .disableAutoGeneratedBlastFurnaceRecipes()
                .disableAutoGeneratedVacuumFreezerRecipe();
    }

    private static Materials loadBlackDwarfMatter() {
        return new Materials(
            586,
            TextureSet.SET_METALLIC,
            1.0F,
            0,
            2,
            1 | 2 | 64 | 128,
            0,
            0,
            0,
            255,
            "BlackDwarfMatter",
            "Black Dwarf Matter",
            -1,
            -1,
            0,
            0,
            false,
            false,
            200,
            1,
            1,
            Dyes.dyePurple).setHasCorrespondingFluid(true)
                .setProcessingMaterialTierEU(TierEU.RECIPE_UEV)
                .disableAutoGeneratedBlastFurnaceRecipes()
                .disableAutoGeneratedVacuumFreezerRecipe();
    }

    private static Materials loadTime() {
        return new Materials(
            587,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16 | 32,
            100,
            1,
            255,
            255,
            "temporalFluid",
            "Tachyon Rich Temporal Fluid",
            -1,
            -1,
            0,
            0,
            false,
            false,
            200,
            1,
            1,
            Dyes.dyePurple);
    }

    private static Materials loadSpace() {
        return new Materials(
            106,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16 | 32,
            100,
            1,
            255,
            255,
            "spatialFluid",
            "Spatially Enlarged Fluid",
            -1,
            -1,
            0,
            0,
            false,
            false,
            200,
            1,
            1,
            Dyes.dyePurple);
    }

    private static Materials loadUniversium() {
        return new Materials(
            139,
            new TextureSet("universium", true),
            1.0F,
            4 * 2621440,
            25,
            1 | 2 | 64 | 128,
            38,
            49,
            69,
            255,
            "Universium",
            "Universium",
            -1,
            -1,
            0,
            0,
            false,
            true,
            2,
            1,
            1,
            Dyes._NULL,
            Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1)))
                .setProcessingMaterialTierEU(TierEU.RECIPE_UMV);
    }

    private static Materials loadEternity() {
        return new Materials(
            141,
            new TextureSet("eternity", true),
            1.0F,
            8 * 2621440,
            26,
            1 | 2 | 64 | 128,
            255,
            255,
            255,
            0,
            "Eternity",
            "Eternity",
            -1,
            -1,
            0,
            14000,
            true,
            false,
            2,
            1,
            1,
            Dyes._NULL,
            Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1)))
                .disableAutoGeneratedBlastFurnaceRecipes()
                .disableAutoGeneratedVacuumFreezerRecipe()
                .setProcessingMaterialTierEU(TierEU.RECIPE_UMV);
    }

    private static Materials loadPrimordialMatter() {
        return new Materials(
            142,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            255,
            255,
            255,
            0,
            "PrimordialMatter",
            "Liquid Primordial Matter",
            -1,
            -1,
            2_000_000_000,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes.dyeBlue);
    }

    private static Materials loadMagMatter() {
        return new Materials(
            143,
            new TextureSet("magmatter", true),
            1.0F,
            64 * 2621440,
            26,
            1 | 2 | 64 | 128,
            255,
            255,
            255,
            0,
            "Magmatter",
            "Magmatter",
            -1,
            -1,
            0,
            25000,
            true,
            false,
            2,
            1,
            1,
            Dyes._NULL,
            Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1)))
                .setProcessingMaterialTierEU(TierEU.RECIPE_UMV);
    }

    private static Materials loadQuarkGluonPlasma() {
        return new Materials(
            144,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            255,
            255,
            255,
            0,
            "QuarkGluonPlasma",
            "Degenerate Quark Gluon Plasma",
            -1,
            -1,
            2_000_000_000,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes._NULL);
    }

    private static Materials loadPhononMedium() {
        return new Materials(
            145,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            255,
            255,
            255,
            0,
            "PhononMedium",
            "Lossless Phonon Transfer Medium",
            -1,
            -1,
            500,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes._NULL);
    }

    private static Materials loadPhononCrystalSolution() {
        return new Materials(
            146,
            TextureSet.SET_FLUID,
            1.0F,
            0,
            2,
            16,
            255,
            255,
            255,
            0,
            "PhononCrystalSolution",
            "Saturated Phononic Crystal Solution",
            -1,
            -1,
            500,
            1,
            false,
            true,
            1,
            1,
            1,
            Dyes._NULL);
    }

    private static Materials loadSixPhasedCopper() {
        return new Materials(
            147,
            TextureSet.SET_SHINY,
            1.0F,
            8 * 2621440,
            26,
            1 | 2 | 32 | 64 | 128,
            255,
            120,
            20,
            0,
            "SixPhasedCopper",
            "Six-Phased Copper",
            -1,
            -1,
            1000,
            14000,
            true,
            false,
            2,
            1,
            1,
            Dyes._NULL,
            Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ITER, 1)))
                .disableAutoGeneratedBlastFurnaceRecipes()
                .disableAutoGeneratedVacuumFreezerRecipe()
                .setProcessingMaterialTierEU(TierEU.RECIPE_UEV)
                .setHasCorrespondingPlasma(true);
    }

    private static Materials loadMellion() {
        return new Materials(
            148,
            TextureSet.SET_SHINY,
            1.0F,
            8 * 2621440,
            26,
            1 | 2 | 64 | 128,
            60,
            5,
            5,
            0,
            "Mellion",
            "Mellion",
            -1,
            -1,
            1000,
            14000,
            true,
            false,
            2,
            1,
            1,
            Dyes._NULL,
            Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.SENSUS, 1)))
                .disableAutoGeneratedBlastFurnaceRecipes()
                .disableAutoGeneratedVacuumFreezerRecipe()
                .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadCreon() {
        return new Materials(
            149,
            TextureSet.SET_SHINY,
            1.0F,
            8 * 2621440,
            26,
            1 | 2 | 32 | 64 | 128,
            70,
            0,
            70,
            0,
            "Creon",
            "Creon",
            -1,
            -1,
            1000,
            14000,
            true,
            false,
            2,
            1,
            1,
            Dyes._NULL,
            Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.SENSUS, 1)))
                .disableAutoGeneratedBlastFurnaceRecipes()
                .disableAutoGeneratedVacuumFreezerRecipe()
                .setProcessingMaterialTierEU(TierEU.RECIPE_UEV)
                .setHasCorrespondingPlasma(true);
    }

    private static Materials loadGravitonShard() {
        return new Materials(
            150,
            new TextureSet("GravitonShard", true),
            1.0F,
            8 * 2621440,
            26,
            256,
            255,
            255,
            255,
            0,
            "GravitonShard",
            "Graviton Shard",
            -1,
            -1,
            100000,
            100000,
            false,
            false,
            2,
            1,
            1,
            Dyes._NULL,
            Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.VACUOS, 150)))
                .disableAutoGeneratedBlastFurnaceRecipes()
                .disableAutoGeneratedVacuumFreezerRecipe();
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
