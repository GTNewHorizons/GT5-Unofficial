package gregtech.loaders.preload;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.api.metatileentity.implementations.MTEFrame;
import gregtech.api.metatileentity.implementations.MTEItemPipe;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockFrameBox;

public final class LoaderMetaPipeEntities implements Runnable {

    private static final String aTextWire1 = "wire.";
    private static final String aTextCable1 = "cable.";
    private static final String aTextWire2 = " Wire";
    private static final String aTextCable2 = " Cable";

    @Override
    public void run() {
        generateWiresAndPipes();
    }

    private static void generateWiresAndPipes() {
        for (int meta = 0; meta < GregTechAPI.sGeneratedMaterials.length; meta++) {
            Materials material = GregTechAPI.sGeneratedMaterials[meta];
            // This check is separated out because IntelliJ thinks Materials.Wood can be null.
            if (material == null) continue;
            if ((material.mTypes & 0x2) != 0 || material == Materials.Wood) {
                new MTEFrame(
                    4096 + meta,
                    "GT_Frame_" + material,
                    (GTLanguageManager.i18nPlaceholder ? "%material" : material.mDefaultLocalName)
                        + " Frame Box (TileEntity)",
                    material);

                // Generate recipes for frame box
                BlockFrameBox block = (BlockFrameBox) GregTechAPI.sBlockFrames;
                GTOreDictUnificator.registerOre(OrePrefixes.frameGt, material, block.getStackForm(1, meta));
                if (material.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        block.getStackForm(2, meta),
                        GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "SSS", "SwS", "SSS", 'S', OrePrefixes.stick.get(material) });
                }

                if (!material.contains(SubTag.NO_RECIPES)
                    && GTOreDictUnificator.get(OrePrefixes.stick, material, 1) != null) {
                    // Auto generate frame box recipe in an assembler.
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.stick, material, 4),
                            GTUtility.getIntegratedCircuit(4))
                        .itemOutputs(block.getStackForm(1, meta))
                        .duration(3 * SECONDS + 4 * TICKS)
                        .eut(calculateRecipeEU(material, 7))
                        .addTo(assemblerRecipes);
                }
            }
        }

        makeWires(Materials.RedAlloy, 2000, 0L, 1L, 1L, GTValues.V[0], true, false);

        makeWires(Materials.Cobalt, 1200, 1L, 2L, 2L, GTValues.V[1], true, false);
        makeWires(Materials.Lead, 1220, 2L, 4L, 2L, GTValues.V[1], true, false);
        makeWires(Materials.Tin, 1240, 1L, 2L, 1L, GTValues.V[1], true, false);
        makeWires(Materials.Zinc, 1260, 1L, 2L, 1L, GTValues.V[1], true, false);
        makeWires(Materials.SolderingAlloy, 1280, 1L, 2L, 1L, GTValues.V[1], true, false);

        makeWires(Materials.Iron, 1300, 3L, 6L, 2L, GTValues.V[2], true, false);
        makeWires(Materials.Nickel, 1320, 3L, 6L, 3L, GTValues.V[2], true, false);
        makeWires(Materials.Cupronickel, 1340, 3L, 6L, 4L, GTValues.V[2], true, false);
        makeWires(Materials.Copper, 1360, 2L, 4L, 1L, GTValues.V[2], true, false);
        makeWires(Materials.AnnealedCopper, 1380, 1L, 2L, 1L, GTValues.V[2], true, false);

        makeWires(Materials.Kanthal, 1400, 3L, 6L, 5L, GTValues.V[3], true, false);
        makeWires(Materials.Gold, 1420, 2L, 4L, 3L, GTValues.V[3], true, false);
        makeWires(Materials.Electrum, 1440, 1L, 2L, 2L, GTValues.V[3], true, false);
        makeWires(Materials.Silver, 1460, 1L, 2L, 1L, GTValues.V[3], true, false);
        makeWires(Materials.BlueAlloy, 1480, 1L, 2L, 2L, GTValues.V[3], true, false);

        makeWires(Materials.Nichrome, 1500, 4L, 8L, 6L, GTValues.V[4], true, false);
        makeWires(Materials.Steel, 1520, 3L, 6L, 2L, GTValues.V[4], true, false);
        makeWires(Materials.BlackSteel, 1540, 1L, 2L, 4L, GTValues.V[4], true, false);
        makeWires(Materials.Titanium, 1560, 2L, 4L, 4L, GTValues.V[4], true, false);
        makeWires(Materials.Aluminium, 1580, 1L, 2L, 1L, GTValues.V[4], true, false);
        makeWires(Materials.TPV, 1840, 1L, 2L, 6L, GTValues.V[4], true, false);

        makeWires(Materials.Graphene, 1600, 1L, 2L, 1L, GTValues.V[5], false, true);
        makeWires(Materials.Platinum, 1640, 1L, 2L, 2L, GTValues.V[5], true, false);
        makeWires(Materials.TungstenSteel, 1660, 4L, 8L, 4L, GTValues.V[5], true, false);
        makeWires(Materials.Tungsten, 1680, 2L, 4L, 6L, GTValues.V[5], true, false);

        makeWires(Materials.Osmium, 1620, 2L, 4L, 4L, GTValues.V[6], true, false);
        makeWires(Materials.HSSG, 1700, 2L, 4L, 4L, GTValues.V[6], true, false);
        makeWires(Materials.NiobiumTitanium, 1720, 2L, 4L, 4L, GTValues.V[6], true, false);
        makeWires(Materials.VanadiumGallium, 1740, 4L, 8L, 4L, GTValues.V[6], true, false);
        makeWires(Materials.YttriumBariumCuprate, 1760, 3L, 6L, 6L, GTValues.V[6], true, false);

        makeWires(Materials.Naquadah, 1780, 2L, 4L, 2L, GTValues.V[7], true, false);

        makeWires(Materials.NaquadahAlloy, 1800, 4L, 8L, 6L, GTValues.V[8], true, false);
        makeWires(Materials.Duranium, 1820, 2L, 4L, 4L, GTValues.V[8], true, false);

        // Superconductor base.
        makeWires(Materials.Pentacadmiummagnesiumhexaoxid, 2200, 1L, 2L, 1L, GTValues.V[2], false, false);
        makeWires(Materials.Titaniumonabariumdecacoppereikosaoxid, 2220, 1L, 8L, 2L, GTValues.V[3], false, false);
        makeWires(Materials.Uraniumtriplatinid, 2240, 1L, 16L, 3L, GTValues.V[4], false, false);
        makeWires(Materials.Vanadiumtriindinid, 2260, 1L, 64L, 4L, GTValues.V[5], false, false);
        makeWires(
            Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
            2280,
            2L,
            256L,
            6L,
            GTValues.V[6],
            false,
            false);
        makeWires(Materials.Tetranaquadahdiindiumhexaplatiumosminid, 2300, 2L, 1024L, 8L, GTValues.V[7], false, false);
        makeWires(Materials.Longasssuperconductornameforuvwire, 2500, 2L, 4096L, 12L, GTValues.V[8], false, false);
        makeWires(Materials.Longasssuperconductornameforuhvwire, 2520, 2L, 16384L, 16L, GTValues.V[9], false, false);
        makeWires(Materials.SuperconductorUEVBase, 2032, 2L, 65536L, 24L, GTValues.V[10], false, false);
        makeWires(Materials.SuperconductorUIVBase, 2052, 2L, 262144L, 32L, GTValues.V[11], false, false);
        makeWires(Materials.SuperconductorUMVBase, 2072, 2L, 1048576L, 32L, GTValues.V[12], false, false);

        // Actual superconductors.
        makeWires(Materials.SuperconductorMV, 2320, 0L, 0L, 4L, GTValues.V[2], false, true);
        makeWires(Materials.SuperconductorHV, 2340, 0L, 0L, 6L, GTValues.V[3], false, true);
        makeWires(Materials.SuperconductorEV, 2360, 0L, 0L, 8L, GTValues.V[4], false, true);
        makeWires(Materials.SuperconductorIV, 2380, 0L, 0L, 12L, GTValues.V[5], false, true);
        makeWires(Materials.SuperconductorLuV, 2400, 0L, 0L, 16L, GTValues.V[6], false, true);
        makeWires(Materials.SuperconductorZPM, 2420, 0L, 0L, 24L, GTValues.V[7], false, true);
        makeWires(Materials.SuperconductorUV, 2440, 0L, 0L, 32L, GTValues.V[8], false, true);
        makeWires(Materials.SuperconductorUHV, 2020, 0L, 0L, 48L, GTValues.V[9], false, true);
        makeWires(Materials.SuperconductorUEV, 2026, 0L, 0L, 64L, GTValues.V[10], false, true);
        makeWires(Materials.SuperconductorUIV, 2081, 0L, 0L, 64L, GTValues.V[11], false, true);
        makeWires(Materials.SuperconductorUMV, 2089, 0L, 0L, 64L, GTValues.V[12], false, true);

        makeWires(Materials.Ichorium, 2600, 4L, 8L, 12L, GTValues.V[9], false, true);
        makeWires(MaterialsUEVplus.SpaceTime, 2606, 0L, 0L, 1_000_000L, GTValues.V[14], false, true);

        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeSmall.get(Materials.Wood),
            new MTEFluidPipe(
                5101,
                "GT_Pipe_Wood_Small",
                "Small Wooden Fluid Pipe",
                0.375F,
                Materials.Wood,
                10,
                350,
                false).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(Materials.Wood),
            new MTEFluidPipe(5102, "GT_Pipe_Wood", "Wooden Fluid Pipe", 0.5F, Materials.Wood, 30, 350, false)
                .getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(Materials.Wood),
            new MTEFluidPipe(
                5103,
                "GT_Pipe_Wood_Large",
                "Large Wooden Fluid Pipe",
                0.75F,
                Materials.Wood,
                60,
                350,
                false).getStackForm(1L));

        generateFluidPipes(Materials.Copper, Materials.Copper.mName, 5110, 20, 1000, true);
        generateFluidMultiPipes(Materials.Copper, Materials.Copper.mName, 5115, 20, 1000, true);
        generateFluidPipes(Materials.Bronze, Materials.Bronze.mName, 5120, 120, 2000, true);
        generateFluidMultiPipes(Materials.Bronze, Materials.Bronze.mName, 5125, 120, 2000, true);
        generateFluidPipes(Materials.Steel, Materials.Steel.mName, 5130, 240, 2500, true);
        generateFluidMultiPipes(Materials.Steel, Materials.Steel.mName, 5135, 240, 2500, true);
        generateFluidPipes(Materials.StainlessSteel, Materials.StainlessSteel.mName, 5140, 360, 3000, true);
        generateFluidMultiPipes(Materials.StainlessSteel, Materials.StainlessSteel.mName, 5145, 360, 3000, true);
        generateFluidPipes(Materials.Titanium, Materials.Titanium.mName, 5150, 480, 5000, true);
        generateFluidMultiPipes(Materials.Titanium, Materials.Titanium.mName, 5155, 480, 5000, true);
        generateFluidPipes(Materials.TungstenSteel, Materials.TungstenSteel.mName, 5160, 600, 7500, true);
        generateFluidMultiPipes(Materials.TungstenSteel, Materials.TungstenSteel.mName, 5270, 600, 7500, true);
        generateFluidPipes(
            Materials.Polybenzimidazole,
            Materials.Polybenzimidazole.mName,
            "PBI",
            5280,
            600,
            1000,
            true);
        generateFluidMultiPipes(
            Materials.Polybenzimidazole,
            Materials.Polybenzimidazole.mName,
            "PBI",
            5290,
            600,
            1000,
            true);
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeSmall.get(Materials.Ultimate),
            new MTEFluidPipe(
                5165,
                "GT_Pipe_HighPressure_Small",
                "Small High Pressure Fluid Pipe",
                0.375F,
                Materials.Redstone,
                4800,
                1500,
                true).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(Materials.Ultimate),
            new MTEFluidPipe(
                5166,
                "GT_Pipe_HighPressure",
                "High Pressure Fluid Pipe",
                0.5F,
                Materials.Redstone,
                7200,
                1500,
                true).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(Materials.Ultimate),
            new MTEFluidPipe(
                5167,
                "GT_Pipe_HighPressure_Large",
                "Large High Pressure Fluid Pipe",
                0.75F,
                Materials.Redstone,
                9600,
                1500,
                true).getStackForm(1L));
        generateFluidPipes(Materials.Plastic, Materials.Plastic.mName, "Plastic", 5170, 360, 350, true);
        generateFluidMultiPipes(Materials.Plastic, Materials.Plastic.mName, "Plastic", 5175, 360, 350, true);
        generateFluidPipes(Materials.NiobiumTitanium, Materials.NiobiumTitanium.mName, 5180, 900, 2900, true);
        generateFluidMultiPipes(Materials.NiobiumTitanium, Materials.NiobiumTitanium.mName, 5185, 900, 2900, true);
        generateFluidPipes(Materials.Enderium, Materials.Enderium.mName, 5190, 1800, 15000, true);
        generateFluidMultiPipes(Materials.Enderium, Materials.Enderium.mName, 5195, 1800, 15000, true);
        generateFluidPipes(Materials.Naquadah, Materials.Naquadah.mName, 5200, 9000, 19000, true);
        generateFluidMultiPipes(Materials.Naquadah, Materials.Naquadah.mName, 5205, 9000, 19000, true);
        generateFluidPipes(Materials.Neutronium, Materials.Neutronium.mName, 5210, 16800, 1000000, true);
        generateFluidMultiPipes(Materials.Neutronium, Materials.Neutronium.mName, 5215, 16800, 1000000, true);
        generateFluidPipes(Materials.NetherStar, Materials.NetherStar.mName, 5220, 19200, 1000000, true);
        generateFluidMultiPipes(Materials.NetherStar, Materials.NetherStar.mName, 5225, 19200, 1000000, true);
        generateFluidPipes(Materials.MysteriousCrystal, Materials.MysteriousCrystal.mName, 5230, 24000, 1000000, true);
        generateFluidMultiPipes(
            Materials.MysteriousCrystal,
            Materials.MysteriousCrystal.mName,
            5235,
            24000,
            1000000,
            true);
        generateFluidPipes(Materials.DraconiumAwakened, Materials.DraconiumAwakened.mName, 5240, 45000, 10000000, true);
        generateFluidMultiPipes(
            Materials.DraconiumAwakened,
            Materials.DraconiumAwakened.mName,
            5245,
            45000,
            10000000,
            true);
        generateFluidPipes(Materials.Infinity, Materials.Infinity.mName, 5250, 60000, 10000000, true);
        generateFluidMultiPipes(Materials.Infinity, Materials.Infinity.mName, 5255, 60000, 10000000, true);
        generateFluidPipes(Materials.WroughtIron, Materials.WroughtIron.mName, 5260, 180, 2250, true);
        generateFluidMultiPipes(Materials.WroughtIron, Materials.WroughtIron.mName, 5265, 180, 2250, true);
        generateFluidPipes(
            Materials.Polytetrafluoroethylene,
            Materials.Polytetrafluoroethylene.mName,
            "PTFE",
            5680,
            480,
            600,
            true);
        generateFluidMultiPipes(
            Materials.Polytetrafluoroethylene,
            Materials.Polytetrafluoroethylene.mName,
            "PTFE",
            5685,
            480,
            600,
            true);
        generateFluidPipes(
            MaterialsUEVplus.SpaceTime,
            MaterialsUEVplus.SpaceTime.mName,
            5300,
            250000,
            2147483647,
            true);
        generateFluidMultiPipes(
            MaterialsUEVplus.SpaceTime,
            MaterialsUEVplus.SpaceTime.mName,
            5305,
            250000,
            2147483647,
            true);
        generateFluidPipes(
            MaterialsUEVplus.TranscendentMetal,
            MaterialsUEVplus.TranscendentMetal.mName,
            5310,
            220000,
            2147483647,
            true);
        generateFluidMultiPipes(
            MaterialsUEVplus.TranscendentMetal,
            MaterialsUEVplus.TranscendentMetal.mName,
            5315,
            220000,
            2147483647,
            true);

        generateItemPipes(Materials.Brass, Materials.Brass.mName, 5602, 1);
        generateItemPipes(Materials.Electrum, Materials.Electrum.mName, 5612, 2);
        generateItemPipes(Materials.Platinum, Materials.Platinum.mName, 5622, 4);
        generateItemPipes(Materials.Osmium, Materials.Osmium.mName, 5632, 8);
        generateItemPipes(Materials.PolyvinylChloride, Materials.PolyvinylChloride.mName, "PVC", 5690, 4);
        generateItemPipes(Materials.Nickel, Materials.Nickel.mName, 5700, 1);
        generateItemPipes(Materials.Cobalt, Materials.Cobalt.mName, 5710, 2);
        generateItemPipes(Materials.Aluminium, Materials.Aluminium.mName, 5720, 2);
        generateFluidPipes(Materials.RadoxPolymer, Materials.RadoxPolymer.mName, 5760, 5000, 1500, true);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    private static void makeWires(Materials aMaterial, int aStartID, long aLossInsulated, long aLoss, long aAmperage,
        long aVoltage, boolean aInsulatable, boolean aAutoInsulated) {
        String name = GTLanguageManager.i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName;
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt01,
            aMaterial,
            new MTECable(
                aStartID + 0,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".01",
                "1x " + name + aTextWire2,
                0.125F,
                aMaterial,
                aLoss,
                1L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt02,
            aMaterial,
            new MTECable(
                aStartID + 1,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".02",
                "2x " + name + aTextWire2,
                0.25F,
                aMaterial,
                aLoss,
                2L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt04,
            aMaterial,
            new MTECable(
                aStartID + 2,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".04",
                "4x " + name + aTextWire2,
                0.375F,
                aMaterial,
                aLoss,
                4L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt08,
            aMaterial,
            new MTECable(
                aStartID + 3,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".08",
                "8x " + name + aTextWire2,
                0.5F,
                aMaterial,
                aLoss,
                8L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt12,
            aMaterial,
            new MTECable(
                aStartID + 4,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".12",
                "12x " + name + aTextWire2,
                0.625F,
                aMaterial,
                aLoss,
                12L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt16,
            aMaterial,
            new MTECable(
                aStartID + 5,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".16",
                "16x " + name + aTextWire2,
                0.75F,
                aMaterial,
                aLoss,
                16L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        if (aInsulatable) {
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt01,
                aMaterial,
                new MTECable(
                    aStartID + 6,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".01",
                    "1x " + name + aTextCable2,
                    0.25F,
                    aMaterial,
                    aLossInsulated,
                    1L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt02,
                aMaterial,
                new MTECable(
                    aStartID + 7,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".02",
                    "2x " + name + aTextCable2,
                    0.375F,
                    aMaterial,
                    aLossInsulated,
                    2L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt04,
                aMaterial,
                new MTECable(
                    aStartID + 8,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".04",
                    "4x " + name + aTextCable2,
                    0.5F,
                    aMaterial,
                    aLossInsulated,
                    4L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt08,
                aMaterial,
                new MTECable(
                    aStartID + 9,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".08",
                    "8x " + name + aTextCable2,
                    0.625F,
                    aMaterial,
                    aLossInsulated,
                    8L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt12,
                aMaterial,
                new MTECable(
                    aStartID + 10,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".12",
                    "12x " + name + aTextCable2,
                    0.75F,
                    aMaterial,
                    aLossInsulated,
                    12L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt16,
                aMaterial,
                new MTECable(
                    aStartID + 11,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".16",
                    "16x " + name + aTextCable2,
                    0.875F,
                    aMaterial,
                    aLossInsulated,
                    16L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
        }
    }

    private static void generateItemPipes(Materials aMaterial, String name, int startID, int baseInvSlots) {
        generateItemPipes(
            aMaterial,
            name,
            GTLanguageManager.i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName,
            startID,
            baseInvSlots);
    }

    private static void generateItemPipes(Materials aMaterial, String name, String displayName, int startID,
        int baseInvSlots) {
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(aMaterial),
            new MTEItemPipe(
                startID,
                "GT_Pipe_" + name,
                displayName + " Item Pipe",
                0.50F,
                aMaterial,
                baseInvSlots,
                32768 / baseInvSlots,
                false).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(aMaterial),
            new MTEItemPipe(
                startID + 1,
                "GT_Pipe_" + name + "_Large",
                "Large " + displayName + " Item Pipe",
                0.75F,
                aMaterial,
                baseInvSlots * 2,
                16384 / baseInvSlots,
                false).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeHuge.get(aMaterial),
            new MTEItemPipe(
                startID + 2,
                "GT_Pipe_" + name + "_Huge",
                "Huge " + displayName + " Item Pipe",
                1.00F,
                aMaterial,
                baseInvSlots * 4,
                8192 / baseInvSlots,
                false).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeRestrictiveMedium.get(aMaterial),
            new MTEItemPipe(
                startID + 3,
                "GT_Pipe_Restrictive_" + name,
                "Restrictive " + displayName + " Item Pipe",
                0.50F,
                aMaterial,
                baseInvSlots,
                3276800 / baseInvSlots,
                true).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeRestrictiveLarge.get(aMaterial),
            new MTEItemPipe(
                startID + 4,
                "GT_Pipe_Restrictive_" + name + "_Large",
                "Large Restrictive " + displayName + " Item Pipe",
                0.75F,
                aMaterial,
                baseInvSlots * 2,
                1638400 / baseInvSlots,
                true).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeRestrictiveHuge.get(aMaterial),
            new MTEItemPipe(
                startID + 5,
                "GT_Pipe_Restrictive_" + name + "_Huge",
                "Huge Restrictive " + displayName + " Item Pipe",
                0.875F,
                aMaterial,
                baseInvSlots * 4,
                819200 / baseInvSlots,
                true).getStackForm(1L));
    }

    @SuppressWarnings("SameParameterValue")
    private static void generateFluidPipes(Materials aMaterial, String name, int startID, int baseCapacity,
        int heatCapacity, boolean gasProof) {
        generateFluidPipes(
            aMaterial,
            name,
            GTLanguageManager.i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName,
            startID,
            baseCapacity,
            heatCapacity,
            gasProof);
    }

    private static void generateFluidPipes(Materials aMaterial, String name, String displayName, int startID,
        int baseCapacity, int heatCapacity, boolean gasProof) {
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeTiny.get(aMaterial),
            new MTEFluidPipe(
                startID,
                "GT_Pipe_" + name + "_Tiny",
                "Tiny " + displayName + " Fluid Pipe",
                0.25F,
                aMaterial,
                baseCapacity / 6,
                heatCapacity,
                gasProof).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeSmall.get(aMaterial),
            new MTEFluidPipe(
                startID + 1,
                "GT_Pipe_" + name + "_Small",
                "Small " + displayName + " Fluid Pipe",
                0.375F,
                aMaterial,
                baseCapacity / 3,
                heatCapacity,
                gasProof).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(aMaterial),
            new MTEFluidPipe(
                startID + 2,
                "GT_Pipe_" + name,
                displayName + " Fluid Pipe",
                0.5F,
                aMaterial,
                baseCapacity,
                heatCapacity,
                gasProof).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(aMaterial),
            new MTEFluidPipe(
                startID + 3,
                "GT_Pipe_" + name + "_Large",
                "Large " + displayName + " Fluid Pipe",
                0.75F,
                aMaterial,
                baseCapacity * 2,
                heatCapacity,
                gasProof).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeHuge.get(aMaterial),
            new MTEFluidPipe(
                startID + 4,
                "GT_Pipe_" + name + "_Huge",
                "Huge " + displayName + " Fluid Pipe",
                0.875F,
                aMaterial,
                baseCapacity * 4,
                heatCapacity,
                gasProof).getStackForm(1L));
    }

    @SuppressWarnings("SameParameterValue")
    private static void generateFluidMultiPipes(Materials aMaterial, String name, int startID, int baseCapacity,
        int heatCapacity, boolean gasProof) {
        generateFluidMultiPipes(aMaterial, name, "%material", startID, baseCapacity, heatCapacity, gasProof);
    }

    private static void generateFluidMultiPipes(Materials aMaterial, String name, String displayName, int startID,
        int baseCapacity, int heatCapacity, boolean gasProof) {
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeQuadruple.get(aMaterial),
            new MTEFluidPipe(
                startID,
                "GT_Pipe_" + name + "_Quadruple",
                "Quadruple " + displayName + " Fluid Pipe",
                1.0F,
                aMaterial,
                baseCapacity,
                heatCapacity,
                gasProof,
                4).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeNonuple.get(aMaterial),
            new MTEFluidPipe(
                startID + 1,
                "GT_Pipe_" + name + "_Nonuple",
                "Nonuple " + displayName + " Fluid Pipe",
                1.0F,
                aMaterial,
                baseCapacity / 3,
                heatCapacity,
                gasProof,
                9).getStackForm(1L));
    }
}
