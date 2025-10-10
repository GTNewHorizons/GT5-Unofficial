package gregtech.loaders.preload;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import java.util.stream.IntStream;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
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
            if (material.hasMetalItems() || material == Materials.Wood) {
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
                        .itemInputs(GTOreDictUnificator.get(OrePrefixes.stick, material, 4))
                        .circuit(4)
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
            .material(Materials.SuperconductorMVBase)
            .startId(2200)
            .lossWire(2)
            .amperage(1)
            .voltage(TierEU.MV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorHVBase)
            .startId(2220)
            .lossWire(8)
            .amperage(2)
            .voltage(TierEU.HV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorEVBase)
            .startId(2240)
            .lossWire(16)
            .amperage(3)
            .voltage(TierEU.EV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorIVBase)
            .startId(2260)
            .lossWire(64)
            .amperage(4)
            .voltage(TierEU.IV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorLuVBase)
            .startId(2280)
            .lossWire(256)
            .amperage(6)
            .voltage(TierEU.LuV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorZPMBase)
            .startId(2300)
            .lossWire(1024)
            .amperage(8)
            .voltage(TierEU.ZPM)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorUVBase)
            .startId(2500)
            .lossWire(4096)
            .amperage(12)
            .voltage(TierEU.UV)
            .disableCable()
            .build();
        WireCableBuilder.builder()
            .material(Materials.SuperconductorUHVBase)
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
            .material(Materials.SpaceTime)
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

        FluidPipeBuilder.builder()
            .material(Materials.Copper)
            .startId(5110)
            .baseCapacity(20)
            .heatCapacity(1000)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.Bronze)
            .startId(5120)
            .baseCapacity(120)
            .heatCapacity(2000)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.Steel)
            .startId(5130)
            .baseCapacity(240)
            .heatCapacity(2500)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.StainlessSteel)
            .startId(5140)
            .baseCapacity(360)
            .heatCapacity(3000)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.Titanium)
            .startId(5150)
            .baseCapacity(480)
            .heatCapacity(5000)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.TungstenSteel)
            .startId(5160)
            .multiFluidStartId(5270)
            .baseCapacity(600)
            .heatCapacity(7500)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.Polybenzimidazole)
            .displayName("PBI")
            .startId(5280)
            .multiFluidStartId(5290)
            .baseCapacity(600)
            .heatCapacity(1000)
            .build();

        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeSmall.get(Materials.ZPM),
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
            OrePrefixes.pipeMedium.get(Materials.ZPM),
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
            OrePrefixes.pipeLarge.get(Materials.ZPM),
            new MTEFluidPipe(
                5167,
                "GT_Pipe_HighPressure_Large",
                "Large High Pressure Fluid Pipe",
                0.75F,
                Materials.Redstone,
                9600,
                1500,
                true).getStackForm(1L));

        FluidPipeBuilder.builder()
            .material(Materials.Polyethylene)
            .displayName("Plastic")
            .startId(5170)
            .baseCapacity(360)
            .heatCapacity(350)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.NiobiumTitanium)
            .startId(5180)
            .baseCapacity(900)
            .heatCapacity(2900)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.Enderium)
            .startId(5190)
            .baseCapacity(1800)
            .heatCapacity(15000)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.Naquadah)
            .startId(5200)
            .baseCapacity(9000)
            .heatCapacity(19000)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.Neutronium)
            .startId(5210)
            .baseCapacity(16800)
            .heatCapacity(1_000_000)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.NetherStar)
            .startId(5220)
            .baseCapacity(19200)
            .heatCapacity(1_000_000)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.MysteriousCrystal)
            .startId(5230)
            .baseCapacity(24000)
            .heatCapacity(1_000_000)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.DraconiumAwakened)
            .startId(5240)
            .baseCapacity(45000)
            .heatCapacity(10_000_000)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.Infinity)
            .startId(5250)
            .baseCapacity(60000)
            .heatCapacity(10_000_000)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.WroughtIron)
            .startId(5260)
            .baseCapacity(180)
            .heatCapacity(2250)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.Polytetrafluoroethylene)
            .displayName("PTFE")
            .startId(5680)
            .baseCapacity(480)
            .heatCapacity(600)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.SpaceTime)
            .startId(5300)
            .baseCapacity(250000)
            .heatCapacity(Integer.MAX_VALUE)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.TranscendentMetal)
            .startId(5310)
            .baseCapacity(220000)
            .heatCapacity(Integer.MAX_VALUE)
            .build();
        FluidPipeBuilder.builder()
            .material(Materials.RadoxPolymer)
            .startId(5760)
            .baseCapacity(5000)
            .heatCapacity(1500)
            .build();
    }

    private static void registerItemPipes() {
        ItemPipeBuilder.builder()
            .material(Materials.Tin)
            .startId(5589)
            .invSlotsForHugePipe(2)
            .build();
        ItemPipeBuilder.builder()
            .material(Materials.Brass)
            .idList(5600, 5601, 5602, 5603, 5604, 5640, 5641, 5605, 5606, 5607)
            .invSlotsForHugePipe(4)
            .build();
        ItemPipeBuilder.builder()
            .material(Materials.Electrum)
            .idList(5610, 5611, 5612, 5613, 5614, 5642, 5643, 5615, 5616, 5617)
            .invSlotsForHugePipe(8)
            .build();
        ItemPipeBuilder.builder()
            .material(Materials.Platinum)
            .idList(5620, 5621, 5622, 5623, 5624, 5644, 5645, 5625, 5626, 5627)
            .invSlotsForHugePipe(16)
            .stackSizeMultiplier(4)
            .build();
        ItemPipeBuilder.builder()
            .material(Materials.Osmium)
            .idList(5630, 5631, 5632, 5633, 5634, 5646, 5647, 5635, 5636, 5637)
            .invSlotsForHugePipe(32)
            .stackSizeMultiplier(8)
            .build();
        ItemPipeBuilder.builder()
            .material(Materials.ElectrumFlux)
            .startId(5650)
            .invSlotsForHugePipe(64)
            .stackSizeMultiplier(16)
            .build();
        ItemPipeBuilder.builder()
            .material(Materials.BlackPlutonium)
            .startId(5660)
            .invSlotsForHugePipe(128)
            .stackSizeMultiplier(32)
            .build();
        ItemPipeBuilder.builder()
            .material(Materials.Bedrockium)
            .startId(5670)
            .invSlotsForHugePipe(256)
            .stackSizeMultiplier(64)
            .build();
        ItemPipeBuilder.builder()
            .material(Materials.PolyvinylChloride)
            .displayName("PVC")
            .startId(5690)
            .invSlotsForHugePipe(16)
            .disableTinyAndSmall()
            .build();
        ItemPipeBuilder.builder()
            .material(Materials.Nickel)
            .startId(5700)
            .invSlotsForHugePipe(4)
            .disableTinyAndSmall()
            .build();
        ItemPipeBuilder.builder()
            .material(Materials.Cobalt)
            .startId(5710)
            .invSlotsForHugePipe(8)
            .disableTinyAndSmall()
            .build();
        ItemPipeBuilder.builder()
            .material(Materials.Aluminium)
            .startId(5720)
            .invSlotsForHugePipe(8)
            .disableTinyAndSmall()
            .build();
        ItemPipeBuilder.builder()
            .material(Materials.Quantium)
            .startId(5730)
            .invSlotsForHugePipe(512)
            .stackSizeMultiplier(32)
            .build();
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

    private static class FluidPipeBuilder {

        private Materials material;
        private String displayName;
        private Integer startId;
        private Integer multiFluidStartId;
        private Integer baseCapacity;
        private Integer heatCapacity;

        private FluidPipeBuilder() {}

        private static FluidPipeBuilder builder() {
            return new FluidPipeBuilder();
        }

        /**
         * Sets material used for this fluid pipe.
         */
        private FluidPipeBuilder material(Materials material) {
            this.material = material;
            return this;
        }

        /**
         * Sets English display name. Material name by default.
         */
        private FluidPipeBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets MTE id to start. Takes up 5 for regular pipes, another 2 for multi fluid pipes.
         */
        private FluidPipeBuilder startId(int startId) {
            this.startId = startId;
            return this;
        }

        /**
         * Manually sets MTE id to start for multi fluid pipes. Mostly for legacy reason. Default to {@link #startId}
         * plus 5.
         */
        private FluidPipeBuilder multiFluidStartId(int multiFluidStartId) {
            this.multiFluidStartId = multiFluidStartId;
            return this;
        }

        /**
         * Sets capacity for regular size pipe. Automatically scales for other sizes.
         */
        private FluidPipeBuilder baseCapacity(int baseCapacity) {
            this.baseCapacity = baseCapacity;
            return this;
        }

        /**
         * Sets heat capacity.
         */
        private FluidPipeBuilder heatCapacity(int heatCapacity) {
            this.heatCapacity = heatCapacity;
            return this;
        }

        private void build() {
            if (material == null) throw new IllegalStateException("material must be set!");
            if (startId == null) throw new IllegalStateException("startId must be set!");
            if (baseCapacity == null) throw new IllegalStateException("baseCapacity must be set!");
            if (heatCapacity == null) throw new IllegalStateException("heatCapacity must be set!");
            if (displayName == null) {
                displayName = GTLanguageManager.i18nPlaceholder ? "%material" : material.mDefaultLocalName;
            }
            if (multiFluidStartId == null) {
                multiFluidStartId = startId + 5;
            }

            final String displayNameFluidPipe = displayName + " Fluid Pipe";
            final String internalNameFluidPipe = "GT_Pipe_" + material.mName;

            GTOreDictUnificator.registerOre(
                OrePrefixes.pipeTiny.get(material),
                new MTEFluidPipe(
                    startId,
                    internalNameFluidPipe + "_Tiny",
                    "Tiny " + displayNameFluidPipe,
                    0.25F,
                    material,
                    baseCapacity / 6,
                    heatCapacity,
                    true).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.pipeSmall.get(material),
                new MTEFluidPipe(
                    startId + 1,
                    internalNameFluidPipe + "_Small",
                    "Small " + displayNameFluidPipe,
                    0.375F,
                    material,
                    baseCapacity / 3,
                    heatCapacity,
                    true).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.pipeMedium.get(material),
                new MTEFluidPipe(
                    startId + 2,
                    internalNameFluidPipe,
                    displayNameFluidPipe,
                    0.5F,
                    material,
                    baseCapacity,
                    heatCapacity,
                    true).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.pipeLarge.get(material),
                new MTEFluidPipe(
                    startId + 3,
                    internalNameFluidPipe + "_Large",
                    "Large " + displayNameFluidPipe,
                    0.75F,
                    material,
                    baseCapacity * 2,
                    heatCapacity,
                    true).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.pipeHuge.get(material),
                new MTEFluidPipe(
                    startId + 4,
                    internalNameFluidPipe + "_Huge",
                    "Huge " + displayNameFluidPipe,
                    0.875F,
                    material,
                    baseCapacity * 4,
                    heatCapacity,
                    true).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.pipeQuadruple.get(material),
                new MTEFluidPipe(
                    multiFluidStartId,
                    internalNameFluidPipe + "_Quadruple",
                    "Quadruple " + displayNameFluidPipe,
                    1.0F,
                    material,
                    baseCapacity,
                    heatCapacity,
                    true,
                    4).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.pipeNonuple.get(material),
                new MTEFluidPipe(
                    multiFluidStartId + 1,
                    internalNameFluidPipe + "_Nonuple",
                    "Nonuple " + displayNameFluidPipe,
                    1.0F,
                    material,
                    baseCapacity / 3,
                    heatCapacity,
                    true,
                    9).getStackForm(1L));
        }
    }

    private static class ItemPipeBuilder {

        private Materials material;
        private String displayName;
        private Integer startId;
        private int[] idList;
        private Integer invSlotsForHugePipe;
        private int stackSizeMultiplier = 1;
        private boolean generateTinyAndSmall = true;

        private static ItemPipeBuilder builder() {
            return new ItemPipeBuilder();
        }

        /**
         * Sets material used for this item pipe.
         */
        private ItemPipeBuilder material(Materials material) {
            this.material = material;
            return this;
        }

        /**
         * Sets English display name. Material name by default.
         */
        private ItemPipeBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets MTE id to start. Takes up 3 for regular pipes, another 3 for restrictive pipes.
         */
        private ItemPipeBuilder startId(int startId) {
            this.startId = startId;
            return this;
        }

        /**
         * Sets sequence of MTE ids to use. Mostly for legacy reason. If your ids are sequential, use
         * {@link #startId(int)} instead.
         */
        private ItemPipeBuilder idList(int... idList) {
            this.idList = idList;
            return this;
        }

        /**
         * Sets how many item stacks huge pipe can hold. Automatically scales for other sizes.
         */
        private ItemPipeBuilder invSlotsForHugePipe(int invSlotsForHugePipe) {
            this.invSlotsForHugePipe = invSlotsForHugePipe;
            return this;
        }

        /**
         * Disables generating tiny and small pipes.
         */
        private ItemPipeBuilder disableTinyAndSmall() {
            this.generateTinyAndSmall = false;
            return this;
        }

        private ItemPipeBuilder stackSizeMultiplier(int mult) {
            this.stackSizeMultiplier = mult;
            return this;
        }

        private void build() {
            if (material == null) throw new IllegalStateException("material must be set!");
            if (startId == null && idList == null)
                throw new IllegalStateException("Either of startId or idList must be set!");
            if (startId != null && idList != null)
                throw new IllegalStateException("startId and idList cannot be set at the same time!");
            if (invSlotsForHugePipe == null) throw new IllegalStateException("invSlotsForHugePipe must be set!");
            if (displayName == null) {
                displayName = GTLanguageManager.i18nPlaceholder ? "%material" : material.mDefaultLocalName;
            }
            if (idList == null) {
                if (generateTinyAndSmall) {
                    idList = IntStream.range(startId, startId + 10)
                        .toArray();
                } else {
                    idList = IntStream.range(startId, startId + 6)
                        .toArray();
                }
            }

            final String internalNameItemPipe = "GT_Pipe_" + material.mName;
            final String internalNameRestrictivePipe = "GT_Pipe_Restrictive_" + material.mName;
            final String displayNameItemPipe = displayName + " Item Pipe";
            int idIndex = 0;

            if (generateTinyAndSmall) {
                GTOreDictUnificator.registerOre(
                    OrePrefixes.pipeTiny.get(material),
                    new MTEItemPipe(
                        idList[idIndex],
                        internalNameItemPipe + "_Tiny",
                        "Tiny " + displayNameItemPipe,
                        0.25F,
                        material,
                        Math.max(invSlotsForHugePipe / 16, 1),
                        524288 / invSlotsForHugePipe,
                        false,
                        Math.max(16 / invSlotsForHugePipe, 1) * 20,
                        stackSizeMultiplier).getStackForm(1L));
                idIndex++;
                GTOreDictUnificator.registerOre(
                    OrePrefixes.pipeSmall.get(material),
                    new MTEItemPipe(
                        idList[idIndex],
                        internalNameItemPipe + "_Small",
                        "Small " + displayNameItemPipe,
                        0.375F,
                        material,
                        Math.max(invSlotsForHugePipe / 8, 1),
                        262144 / invSlotsForHugePipe,
                        false,
                        Math.max(8 / invSlotsForHugePipe, 1) * 20,
                        stackSizeMultiplier).getStackForm(1L));
                idIndex++;
            }
            GTOreDictUnificator.registerOre(
                OrePrefixes.pipeMedium.get(material),
                new MTEItemPipe(
                    idList[idIndex],
                    internalNameItemPipe,
                    displayNameItemPipe,
                    0.50F,
                    material,
                    Math.max(invSlotsForHugePipe / 4, 1),
                    131072 / invSlotsForHugePipe,
                    false,
                    Math.max(4 / invSlotsForHugePipe, 1) * 20,
                    stackSizeMultiplier).getStackForm(1L));
            idIndex++;
            GTOreDictUnificator.registerOre(
                OrePrefixes.pipeLarge.get(material),
                new MTEItemPipe(
                    idList[idIndex],
                    internalNameItemPipe + "_Large",
                    "Large " + displayNameItemPipe,
                    0.75F,
                    material,
                    Math.max(invSlotsForHugePipe / 2, 1),
                    65536 / invSlotsForHugePipe,
                    false,
                    Math.max(2 / invSlotsForHugePipe, 1) * 20,
                    stackSizeMultiplier).getStackForm(1L));
            idIndex++;
            GTOreDictUnificator.registerOre(
                OrePrefixes.pipeHuge.get(material),
                new MTEItemPipe(
                    idList[idIndex],
                    internalNameItemPipe + "_Huge",
                    "Huge " + displayNameItemPipe,
                    1.00F,
                    material,
                    invSlotsForHugePipe,
                    32768 / invSlotsForHugePipe,
                    false,
                    20,
                    stackSizeMultiplier).getStackForm(1L));
            idIndex++;
            if (generateTinyAndSmall) {
                GTOreDictUnificator.registerOre(
                    OrePrefixes.pipeRestrictiveTiny.get(material),
                    new MTEItemPipe(
                        idList[idIndex],
                        internalNameRestrictivePipe + "_Tiny",
                        "Tiny Restrictive " + displayNameItemPipe,
                        0.25F,
                        material,
                        Math.max(invSlotsForHugePipe / 16, 1),
                        52428800 / invSlotsForHugePipe,
                        true,
                        Math.max(16 / invSlotsForHugePipe, 1) * 20,
                        stackSizeMultiplier).getStackForm(1L));
                idIndex++;
                GTOreDictUnificator.registerOre(
                    OrePrefixes.pipeRestrictiveSmall.get(material),
                    new MTEItemPipe(
                        idList[idIndex],
                        internalNameRestrictivePipe + "_Small",
                        "Small Restrictive " + displayNameItemPipe,
                        0.375F,
                        material,
                        Math.max(invSlotsForHugePipe / 8, 1),
                        26214400 / invSlotsForHugePipe,
                        true,
                        Math.max(8 / invSlotsForHugePipe, 1) * 20,
                        stackSizeMultiplier).getStackForm(1L));
                idIndex++;
            }
            GTOreDictUnificator.registerOre(
                OrePrefixes.pipeRestrictiveMedium.get(material),
                new MTEItemPipe(
                    idList[idIndex],
                    internalNameRestrictivePipe,
                    "Restrictive " + displayNameItemPipe,
                    0.50F,
                    material,
                    Math.max(invSlotsForHugePipe / 4, 1),
                    13107200 / invSlotsForHugePipe,
                    true,
                    Math.max(4 / invSlotsForHugePipe, 1) * 20,
                    stackSizeMultiplier).getStackForm(1L));
            idIndex++;
            GTOreDictUnificator.registerOre(
                OrePrefixes.pipeRestrictiveLarge.get(material),
                new MTEItemPipe(
                    idList[idIndex],
                    internalNameRestrictivePipe + "_Large",
                    "Large Restrictive " + displayNameItemPipe,
                    0.75F,
                    material,
                    Math.max(invSlotsForHugePipe / 2, 1),
                    6553600 / invSlotsForHugePipe,
                    true,
                    Math.max(2 / invSlotsForHugePipe, 1) * 20,
                    stackSizeMultiplier).getStackForm(1L));
            idIndex++;
            GTOreDictUnificator.registerOre(
                OrePrefixes.pipeRestrictiveHuge.get(material),
                new MTEItemPipe(
                    idList[idIndex],
                    internalNameRestrictivePipe + "_Huge",
                    "Huge Restrictive " + displayNameItemPipe,
                    0.875F,
                    material,
                    invSlotsForHugePipe,
                    3276800 / invSlotsForHugePipe,
                    true,
                    20,
                    stackSizeMultiplier).getStackForm(1L));
        }
    }
}
