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

    @Override
    public void run() {
        registerFrames();
        registerWiresAndCables();
        registerFluidPipes();
        registerItemPipes();
    }

    private static void registerFrames() {
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
    }

    private static void registerWiresAndCables() {
        // ULV
        WireCableBuilder.builder()
            .material(Materials.RedAlloy)
            .startId(2000)
            .lossCable(0)
            .lossWire(1)
            .amperage(1)
            .voltage(TierEU.ULV)
            .build();

        // LV
        WireCableBuilder.builder()
            .material(Materials.Cobalt)
            .startId(1200)
            .lossCable(1)
            .lossWire(2)
            .amperage(2)
            .voltage(TierEU.LV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Lead)
            .startId(1220)
            .lossCable(2)
            .lossWire(4)
            .amperage(2)
            .voltage(TierEU.LV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Tin)
            .startId(1240)
            .lossCable(1)
            .lossWire(2)
            .amperage(1)
            .voltage(TierEU.LV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Zinc)
            .startId(1260)
            .lossCable(1)
            .lossWire(2)
            .amperage(1)
            .voltage(TierEU.LV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.SolderingAlloy)
            .startId(1280)
            .lossCable(1)
            .lossWire(2)
            .amperage(1)
            .voltage(TierEU.LV)
            .build();

        // MV
        WireCableBuilder.builder()
            .material(Materials.Iron)
            .startId(1300)
            .lossCable(3)
            .lossWire(6)
            .amperage(2)
            .voltage(TierEU.MV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Nickel)
            .startId(1320)
            .lossCable(3)
            .lossWire(6)
            .amperage(3)
            .voltage(TierEU.MV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Cupronickel)
            .startId(1340)
            .lossCable(3)
            .lossWire(6)
            .amperage(4)
            .voltage(TierEU.MV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Copper)
            .startId(1360)
            .lossCable(2)
            .lossWire(4)
            .amperage(1)
            .voltage(TierEU.MV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.AnnealedCopper)
            .startId(1380)
            .lossCable(1)
            .lossWire(2)
            .amperage(1)
            .voltage(TierEU.MV)
            .build();

        // HV
        WireCableBuilder.builder()
            .material(Materials.Kanthal)
            .startId(1400)
            .lossCable(3)
            .lossWire(6)
            .amperage(5)
            .voltage(TierEU.HV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Gold)
            .startId(1420)
            .lossCable(2)
            .lossWire(4)
            .amperage(3)
            .voltage(TierEU.HV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Electrum)
            .startId(1440)
            .lossCable(1)
            .lossWire(2)
            .amperage(2)
            .voltage(TierEU.HV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Silver)
            .startId(1460)
            .lossCable(1)
            .lossWire(2)
            .amperage(1)
            .voltage(TierEU.HV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.BlueAlloy)
            .startId(1480)
            .lossCable(1)
            .lossWire(2)
            .amperage(2)
            .voltage(TierEU.HV)
            .build();

        // EV
        WireCableBuilder.builder()
            .material(Materials.Nichrome)
            .startId(1500)
            .lossCable(4)
            .lossWire(8)
            .amperage(6)
            .voltage(TierEU.EV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Steel)
            .startId(1520)
            .lossCable(3)
            .lossWire(6)
            .amperage(2)
            .voltage(TierEU.EV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.BlackSteel)
            .startId(1540)
            .lossCable(1)
            .lossWire(2)
            .amperage(4)
            .voltage(TierEU.EV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Titanium)
            .startId(1560)
            .lossCable(2)
            .lossWire(4)
            .amperage(4)
            .voltage(TierEU.EV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Aluminium)
            .startId(1580)
            .lossCable(1)
            .lossWire(2)
            .amperage(1)
            .voltage(TierEU.EV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.TPV)
            .startId(1840)
            .lossCable(1)
            .lossWire(2)
            .amperage(6)
            .voltage(TierEU.EV)
            .build();

        // IV
        WireCableBuilder.builder()
            .material(Materials.Graphene)
            .startId(1600)
            .lossWire(2)
            .amperage(1)
            .voltage(TierEU.IV)
            .disableCable()
            .disableElectricDamage()
            .build();
        WireCableBuilder.builder()
            .material(Materials.Platinum)
            .startId(1640)
            .lossCable(1)
            .lossWire(2)
            .amperage(2)
            .voltage(TierEU.IV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.TungstenSteel)
            .startId(1660)
            .lossCable(4)
            .lossWire(8)
            .amperage(4)
            .voltage(TierEU.IV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Tungsten)
            .startId(1680)
            .lossCable(2)
            .lossWire(4)
            .amperage(6)
            .voltage(TierEU.IV)
            .build();

        // LuV
        WireCableBuilder.builder()
            .material(Materials.Osmium)
            .startId(1620)
            .lossCable(2)
            .lossWire(4)
            .amperage(4)
            .voltage(TierEU.LuV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.HSSG)
            .startId(1700)
            .lossCable(2)
            .lossWire(4)
            .amperage(4)
            .voltage(TierEU.LuV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.NiobiumTitanium)
            .startId(1720)
            .lossCable(2)
            .lossWire(4)
            .amperage(4)
            .voltage(TierEU.LuV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.VanadiumGallium)
            .startId(1740)
            .lossCable(4)
            .lossWire(8)
            .amperage(4)
            .voltage(TierEU.LuV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.YttriumBariumCuprate)
            .startId(1760)
            .lossCable(3)
            .lossWire(6)
            .amperage(6)
            .voltage(TierEU.LuV)
            .build();

        // ZPM
        WireCableBuilder.builder()
            .material(Materials.Naquadah)
            .startId(1780)
            .lossCable(2)
            .lossWire(4)
            .amperage(2)
            .voltage(TierEU.ZPM)
            .build();

        // UV
        WireCableBuilder.builder()
            .material(Materials.NaquadahAlloy)
            .startId(1800)
            .lossCable(4)
            .lossWire(8)
            .amperage(6)
            .voltage(TierEU.UV)
            .build();
        WireCableBuilder.builder()
            .material(Materials.Duranium)
            .startId(1820)
            .lossCable(2)
            .lossWire(4)
            .amperage(4)
            .voltage(TierEU.UV)
            .build();

        // Superconductor base
        WireCableBuilder.builder()
            .material(Materials.Pentacadmiummagnesiumhexaoxid)
            .startId(2200)
            .lossWire(2)
            .amperage(1)
            .voltage(TierEU.MV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.Titaniumonabariumdecacoppereikosaoxid)
            .startId(2220)
            .lossWire(8)
            .amperage(2)
            .voltage(TierEU.HV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.Uraniumtriplatinid)
            .startId(2240)
            .lossWire(16)
            .amperage(3)
            .voltage(TierEU.EV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.Vanadiumtriindinid)
            .startId(2260)
            .lossWire(64)
            .amperage(4)
            .voltage(TierEU.IV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid)
            .startId(2280)
            .lossWire(256)
            .amperage(6)
            .voltage(TierEU.LuV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.Tetranaquadahdiindiumhexaplatiumosminid)
            .startId(2300)
            .lossWire(1024)
            .amperage(8)
            .voltage(TierEU.ZPM)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.Longasssuperconductornameforuvwire)
            .startId(2500)
            .lossWire(4096)
            .amperage(12)
            .voltage(TierEU.UV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.Longasssuperconductornameforuhvwire)
            .startId(2520)
            .lossWire(16384)
            .amperage(16)
            .voltage(TierEU.UHV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorUEVBase)
            .startId(2032)
            .lossWire(65536)
            .amperage(24)
            .voltage(TierEU.UEV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorUIVBase)
            .startId(2052)
            .lossWire(262144)
            .amperage(32)
            .voltage(TierEU.UIV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorUMVBase)
            .startId(2072)
            .lossWire(1048576)
            .amperage(32)
            .voltage(TierEU.UMV)
            .disableCable()
            .build();

        // Superconductor
        WireCableBuilder.builder()
            .material(Materials.SuperconductorMV)
            .startId(2320)
            .lossWire(0)
            .amperage(4)
            .voltage(TierEU.MV)
            .disableCable()
            .disableElectricDamage()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorHV)
            .startId(2340)
            .lossWire(0)
            .amperage(6)
            .voltage(TierEU.HV)
            .disableCable()
            .disableElectricDamage()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorEV)
            .startId(2360)
            .lossWire(0)
            .amperage(8)
            .voltage(TierEU.EV)
            .disableCable()
            .disableElectricDamage()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorIV)
            .startId(2380)
            .lossWire(0)
            .amperage(12)
            .voltage(TierEU.IV)
            .disableCable()
            .disableElectricDamage()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorLuV)
            .startId(2400)
            .lossWire(0)
            .amperage(16)
            .voltage(TierEU.LuV)
            .disableCable()
            .disableElectricDamage()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorZPM)
            .startId(2420)
            .lossWire(0)
            .amperage(24)
            .voltage(TierEU.ZPM)
            .disableCable()
            .disableElectricDamage()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorUV)
            .startId(2440)
            .lossWire(0)
            .amperage(32)
            .voltage(TierEU.UV)
            .disableCable()
            .disableElectricDamage()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorUHV)
            .startId(2020)
            .lossWire(0)
            .amperage(48)
            .voltage(TierEU.UHV)
            .disableCable()
            .disableElectricDamage()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorUEV)
            .startId(2026)
            .lossWire(0)
            .amperage(64)
            .voltage(TierEU.UEV)
            .disableCable()
            .disableElectricDamage()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorUIV)
            .startId(2081)
            .lossWire(0)
            .amperage(64)
            .voltage(TierEU.UIV)
            .disableCable()
            .disableElectricDamage()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorUMV)
            .startId(2089)
            .lossWire(0)
            .amperage(64)
            .voltage(TierEU.UMV)
            .disableCable()
            .disableElectricDamage()
            .build();

        // Others
        WireCableBuilder.builder()
            .material(Materials.Ichorium)
            .startId(2600)
            .lossWire(8)
            .amperage(12)
            .voltage(TierEU.UHV)
            .disableCable()
            .disableElectricDamage()
            .build();
        WireCableBuilder.builder()
            .material(MaterialsUEVplus.SpaceTime)
            .startId(2606)
            .lossWire(0)
            .amperage(1_000_000)
            .voltage(TierEU.MAX)
            .disableCable()
            .disableElectricDamage()
            .build();
    }

    private static void registerFluidPipes() {
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
        generateFluidPipes(Materials.RadoxPolymer, Materials.RadoxPolymer.mName, 5760, 5000, 1500, true);
    }

    private static void registerItemPipes() {
        generateItemPipes(Materials.Brass, Materials.Brass.mName, 5602, 1);
        generateItemPipes(Materials.Electrum, Materials.Electrum.mName, 5612, 2);
        generateItemPipes(Materials.Platinum, Materials.Platinum.mName, 5622, 4);
        generateItemPipes(Materials.Osmium, Materials.Osmium.mName, 5632, 8);
        generateItemPipes(Materials.PolyvinylChloride, Materials.PolyvinylChloride.mName, "PVC", 5690, 4);
        generateItemPipes(Materials.Nickel, Materials.Nickel.mName, 5700, 1);
        generateItemPipes(Materials.Cobalt, Materials.Cobalt.mName, 5710, 2);
        generateItemPipes(Materials.Aluminium, Materials.Aluminium.mName, 5720, 2);
    }

    private static class WireCableBuilder {

        private Materials material;
        private Integer startId;
        private Integer lossCable;
        private Integer lossWire;
        private Integer amperage;
        private Integer voltage;
        private boolean generateCable = true;
        private boolean canShock = true;

        private WireCableBuilder() {}

        private static WireCableBuilder builder() {
            return new WireCableBuilder();
        }

        /**
         * Sets material used for this wire/cable.
         */
        private WireCableBuilder material(Materials material) {
            this.material = material;
            return this;
        }

        /**
         * Sets MTE id to start. Takes up 6 for wires, another 6 for cables.
         */
        private WireCableBuilder startId(int startId) {
            this.startId = startId;
            return this;
        }

        /**
         * Sets voltage loss for cables. Can be skipped if {@link #disableCable} is set to false.
         */
        private WireCableBuilder lossCable(int lossCable) {
            this.lossCable = lossCable;
            return this;
        }

        /**
         * Sets voltage loss for wires.
         */
        private WireCableBuilder lossWire(int lossWire) {
            this.lossWire = lossWire;
            return this;
        }

        /**
         * Sets amperage for 1x wire/cable. Automatically gets multiplied for thicker ones.
         */
        private WireCableBuilder amperage(int amperage) {
            this.amperage = amperage;
            return this;
        }

        /**
         * Sets voltage this wire/cable can handle.
         */
        private WireCableBuilder voltage(long voltage) {
            this.voltage = (int) voltage;
            return this;
        }

        /**
         * Disables cable generation.
         */
        private WireCableBuilder disableCable() {
            this.generateCable = false;
            return this;
        }

        /**
         * Disables bare wire to deal electric damage to player on contact.
         */
        private WireCableBuilder disableElectricDamage() {
            this.canShock = false;
            return this;
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        private void build() {
            if (material == null) throw new IllegalStateException("material must be set!");
            if (startId == null) throw new IllegalStateException("startId must be set!");
            if (lossWire == null) throw new IllegalStateException("lossWire must be set!");
            if (amperage == null) throw new IllegalStateException("amperage must be set!");
            if (voltage == null) throw new IllegalStateException("voltage must be set!");

            final String displayNameMaterial = GTLanguageManager.i18nPlaceholder ? "%material"
                : material.mDefaultLocalName;
            final String displayNameWire = displayNameMaterial + " Wire";
            final String displayNameCable = displayNameMaterial + " Cable";
            final String internalNameWire = "wire." + material.mName.toLowerCase();
            final String internalNameCable = "cable." + material.mName.toLowerCase();

            GTOreDictUnificator.registerOre(
                OrePrefixes.wireGt01,
                material,
                new MTECable(
                    startId + 0,
                    internalNameWire + ".01",
                    "1x " + displayNameWire,
                    0.125F,
                    material,
                    lossWire,
                    1L * amperage,
                    voltage,
                    false,
                    canShock).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.wireGt02,
                material,
                new MTECable(
                    startId + 1,
                    internalNameWire + ".02",
                    "2x " + displayNameWire,
                    0.25F,
                    material,
                    lossWire,
                    2L * amperage,
                    voltage,
                    false,
                    canShock).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.wireGt04,
                material,
                new MTECable(
                    startId + 2,
                    internalNameWire + ".04",
                    "4x " + displayNameWire,
                    0.375F,
                    material,
                    lossWire,
                    4L * amperage,
                    voltage,
                    false,
                    canShock).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.wireGt08,
                material,
                new MTECable(
                    startId + 3,
                    internalNameWire + ".08",
                    "8x " + displayNameWire,
                    0.5F,
                    material,
                    lossWire,
                    8L * amperage,
                    voltage,
                    false,
                    canShock).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.wireGt12,
                material,
                new MTECable(
                    startId + 4,
                    internalNameWire + ".12",
                    "12x " + displayNameWire,
                    0.625F,
                    material,
                    lossWire,
                    12L * amperage,
                    voltage,
                    false,
                    canShock).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.wireGt16,
                material,
                new MTECable(
                    startId + 5,
                    internalNameWire + ".16",
                    "16x " + displayNameWire,
                    0.75F,
                    material,
                    lossWire,
                    16L * amperage,
                    voltage,
                    false,
                    canShock).getStackForm(1L));

            if (!generateCable) return;

            if (lossCable == null) throw new IllegalStateException("lossCable must be set to generate cable!");

            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt01,
                material,
                new MTECable(
                    startId + 6,
                    internalNameCable + ".01",
                    "1x " + displayNameCable,
                    0.25F,
                    material,
                    lossCable,
                    1L * amperage,
                    voltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt02,
                material,
                new MTECable(
                    startId + 7,
                    internalNameCable + ".02",
                    "2x " + displayNameCable,
                    0.375F,
                    material,
                    lossCable,
                    2L * amperage,
                    voltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt04,
                material,
                new MTECable(
                    startId + 8,
                    internalNameCable + ".04",
                    "4x " + displayNameCable,
                    0.5F,
                    material,
                    lossCable,
                    4L * amperage,
                    voltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt08,
                material,
                new MTECable(
                    startId + 9,
                    internalNameCable + ".08",
                    "8x " + displayNameCable,
                    0.625F,
                    material,
                    lossCable,
                    8L * amperage,
                    voltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt12,
                material,
                new MTECable(
                    startId + 10,
                    internalNameCable + ".12",
                    "12x " + displayNameCable,
                    0.75F,
                    material,
                    lossCable,
                    12L * amperage,
                    voltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt16,
                material,
                new MTECable(
                    startId + 11,
                    internalNameCable + ".16",
                    "16x " + displayNameCable,
                    0.875F,
                    material,
                    lossCable,
                    16L * amperage,
                    voltage,
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
