package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.*;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.TierEU;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GTPPMTECable;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GTPPMTEFluidPipe;

public class GregtechConduits {

    public enum PipeStats {

        Staballoy(TextureSet.SET_ROUGH, 4, 68, 75, 66, 0, "Staballoy"),
        Void(TextureSet.SET_METALLIC, 3, 82, 17, 82, 0, "Void Metal"),
        Tantalloy60(TextureSet.SET_DULL, 3, 68, 75, 166, 0, "Tantalloy-60"),
        Tantalloy61(TextureSet.SET_DULL, 2, 122, 135, 196, 0, "Tantalloy-61"),
        Potin(TextureSet.SET_METALLIC, 2, 201, 151, 129, 0, "Potin"),
        Inconel792(TextureSet.SET_METALLIC, 2, 108, 240, 118, 0, "Inconel-792"),
        Inconel690(TextureSet.SET_DULL, 2, 118, 220, 138, 0, "Inconel-690"),
        MaragingSteel300(TextureSet.SET_METALLIC, 2, 150, 150, 150, 0, "Maraging Steel 300"),
        MaragingSteel350(TextureSet.SET_METALLIC, 2, 160, 160, 160, 0, "Maraging Steel 350"),
        HastelloyX(TextureSet.SET_SHINY, 2, 255, 193, 37, 0, "Hastelloy-X"),
        TriniumNaquadahCarbonite(TextureSet.SET_SHINY, 2, 255, 233, 0, 0, "Trinium Naquadah Carbonite"),

        ;

        public final short[] rgba;
        public final TextureSet iconSet;
        public final String defaultLocalName;
        public final byte toolQuality;

        PipeStats(TextureSet iconSet, int toolQuality, int r, int g, int b, int a, String localName) {
            this.toolQuality = (byte) toolQuality;
            this.iconSet = iconSet;
            this.defaultLocalName = localName;
            this.rgba = new short[] { (short) r, (short) g, (short) b, (short) a };
        }

        public Material getMaterial() {
            return Material.mMaterialCache.get(defaultLocalName.toLowerCase());
        }
    }

    // 30000-30999
    private static final int BaseWireID = 30600;
    private static final int BasePipeID = 30700;

    public static void run() {
        run1();
        run2();
    }

    private static void run1() {
        wireFactory("RedstoneAlloy", 32, BaseWireID + 45, 0, 2, 1, new short[] { 178, 34, 34, 0 });
        // need to go back id because fluid pipes already occupy
        makeCustomWires(MaterialsElements.STANDALONE.HYPOGEN, BaseWireID - 15, 0, 0, 8, GTValues.V[11], false, true);
    }

    private static void run2() {
        generateNonGTFluidPipes(PipeStats.Staballoy, BasePipeID, 12500, 7500);
        generateNonGTFluidPipes(PipeStats.Tantalloy60, BasePipeID + 5, 10000, 4250);
        generateNonGTFluidPipes(PipeStats.Tantalloy61, BasePipeID + 10, 12000, 5800);
        if (Thaumcraft.isModLoaded()) {
            generateNonGTFluidPipes(PipeStats.Void, BasePipeID + 15, 1600, 25000);
        }
        generateGTFluidPipes(Materials.Europium, BasePipeID + 20, 12000, 7500, true);
        generateNonGTFluidPipes(PipeStats.Potin, BasePipeID + 25, 500, 2000);
        generateNonGTFluidPipes(PipeStats.MaragingSteel300, BasePipeID + 30, 14000, 2500);
        generateNonGTFluidPipes(PipeStats.MaragingSteel350, BasePipeID + 35, 16000, 2500);
        generateNonGTFluidPipes(PipeStats.Inconel690, BasePipeID + 40, 15000, 4800);
        generateNonGTFluidPipes(PipeStats.Inconel792, BasePipeID + 45, 16000, 5500);
        generateNonGTFluidPipes(PipeStats.HastelloyX, BasePipeID + 50, 20000, 4200);
        generateNonGTFluidPipes(PipeStats.TriniumNaquadahCarbonite, 30500, 20, 250000);

        generateGTFluidPipes(Materials.Tungsten, BasePipeID + 55, 4320, 7200, true);
        if (EnderIO.isModLoaded()) {
            generateGTFluidPipes(Materials.DarkSteel, BasePipeID + 60, 2320, 2750, true);
        }
        generateGTFluidPipes(Materials.Clay, BasePipeID + 65, 100, 500, false);
        generateGTFluidPipes(Materials.Lead, BasePipeID + 70, 350, 1200, true);
    }

    private static void wireFactory(final String Material, final int Voltage, final int ID, final long insulatedLoss,
        final long uninsulatedLoss, final long Amps, final short[] rgb) {
        final Materials T = Materials.get(Material);
        int V = GTUtility.getTier(Voltage);
        if (V == -1) {
            Logger.ERROR("Failed to set voltage on " + Material + ". Invalid voltage of " + Voltage + "V set.");
            Logger.ERROR(Material + " has defaulted to 8v.");
            V = 0;
        }
        makeWires(T, ID, insulatedLoss, uninsulatedLoss, Amps, GTValues.V[V], true, false, rgb);
    }

    private static void makeWires(final Materials aMaterial, final int aStartID, final long aLossInsulated,
        final long aLoss, final long aAmperage, final long aVoltage, final boolean aInsulatable,
        final boolean aAutoInsulated, final short[] aRGB) {
        Logger.WARNING("Gregtech5u Content | Registered " + aMaterial.mName + " as a new material for Wire & Cable.");
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt01,
            aMaterial,
            new GTPPMTECable(
                aStartID + 0,
                "wire." + aMaterial.mName.toLowerCase() + ".01",
                "gt.oreprefix.1x_material_wire",
                0.125F,
                aMaterial,
                aLoss,
                1L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated,
                aRGB).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt02,
            aMaterial,
            new GTPPMTECable(
                aStartID + 1,
                "wire." + aMaterial.mName.toLowerCase() + ".02",
                "gt.oreprefix.2x_material_wire",
                0.25F,
                aMaterial,
                aLoss,
                2L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated,
                aRGB).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt04,
            aMaterial,
            new GTPPMTECable(
                aStartID + 2,
                "wire." + aMaterial.mName.toLowerCase() + ".04",
                "gt.oreprefix.4x_material_wire",
                0.375F,
                aMaterial,
                aLoss,
                4L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated,
                aRGB).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt08,
            aMaterial,
            new GTPPMTECable(
                aStartID + 3,
                "wire." + aMaterial.mName.toLowerCase() + ".08",
                "gt.oreprefix.8x_material_wire",
                0.50F,
                aMaterial,
                aLoss,
                8L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated,
                aRGB).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt12,
            aMaterial,
            new GTPPMTECable(
                aStartID + 4,
                "wire." + aMaterial.mName.toLowerCase() + ".12",
                "gt.oreprefix.12x_material_wire",
                0.625F,
                aMaterial,
                aLoss,
                12L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated,
                aRGB).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt16,
            aMaterial,
            new GTPPMTECable(
                aStartID + 5,
                "wire." + aMaterial.mName.toLowerCase() + ".16",
                "gt.oreprefix.16x_material_wire",
                0.75F,
                aMaterial,
                aLoss,
                16L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated,
                aRGB).getStackForm(1L));
        if (aInsulatable) {
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt01,
                aMaterial,
                new GTPPMTECable(
                    aStartID + 6,
                    "cable." + aMaterial.mName.toLowerCase() + ".01",
                    "gt.oreprefix.1x_material_cable",
                    0.25F,
                    aMaterial,
                    aLossInsulated,
                    1L * aAmperage,
                    aVoltage,
                    true,
                    false,
                    aRGB).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt02,
                aMaterial,
                new GTPPMTECable(
                    aStartID + 7,
                    "cable." + aMaterial.mName.toLowerCase() + ".02",
                    "gt.oreprefix.2x_material_cable",
                    0.375F,
                    aMaterial,
                    aLossInsulated,
                    2L * aAmperage,
                    aVoltage,
                    true,
                    false,
                    aRGB).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt04,
                aMaterial,
                new GTPPMTECable(
                    aStartID + 8,
                    "cable." + aMaterial.mName.toLowerCase() + ".04",
                    "gt.oreprefix.4x_material_cable",
                    0.5F,
                    aMaterial,
                    aLossInsulated,
                    4L * aAmperage,
                    aVoltage,
                    true,
                    false,
                    aRGB).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt08,
                aMaterial,
                new GTPPMTECable(
                    aStartID + 9,
                    "cable." + aMaterial.mName.toLowerCase() + ".08",
                    "gt.oreprefix.8x_material_cable",
                    0.625F,
                    aMaterial,
                    aLossInsulated,
                    8L * aAmperage,
                    aVoltage,
                    true,
                    false,
                    aRGB).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt12,
                aMaterial,
                new GTPPMTECable(
                    aStartID + 10,
                    "cable." + aMaterial.mName.toLowerCase() + ".12",
                    "gt.oreprefix.12x_material_cable",
                    0.75F,
                    aMaterial,
                    aLossInsulated,
                    12L * aAmperage,
                    aVoltage,
                    true,
                    false,
                    aRGB).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt16,
                aMaterial,
                new GTPPMTECable(
                    aStartID + 11,
                    "cable." + aMaterial.mName.toLowerCase() + ".16",
                    "gt.oreprefix.16x_material_cable",
                    0.875f,
                    aMaterial,
                    aLossInsulated,
                    16L * aAmperage,
                    aVoltage,
                    true,
                    false,
                    aRGB).getStackForm(1L));
        }
    }

    private static void customWireFactory(final Material Material, final int Voltage, final int ID,
        final long insulatedLoss, final long uninsulatedLoss, final long Amps) {
        int V = GTUtility.getTier(Voltage);
        if (V == -1) {
            Logger.ERROR("Failed to set voltage on " + Material + ". Invalid voltage of " + Voltage + "V set.");
            Logger.ERROR(Material + " has defaulted to 8v.");
            V = 0;
        }
        makeCustomWires(Material, ID, insulatedLoss, uninsulatedLoss, Amps, GTValues.V[V], true, false);
    }

    private static void makeCustomWires(final Material aMaterial, final int aStartID, final long aLossInsulated,
        final long aLoss, final long aAmperage, final long aVoltage, final boolean aInsulatable,
        final boolean aAutoInsulated) {
        Logger.WARNING(
            "Gregtech5u Content | Registered " + aMaterial.getDefaultLocalName()
                + " as a new material for Wire & Cable.");
        registerOre(
            OrePrefixes.wireGt01,
            aMaterial,
            new GTPPMTECable(
                aStartID + 0,
                "wire." + aMaterial.getDefaultLocalName()
                    .toLowerCase() + ".01",
                "gt.oreprefix.1x_material_wire",
                0.125F,
                aLoss,
                1L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated,
                aMaterial).getStackForm(1L));
        registerOre(
            OrePrefixes.wireGt02,
            aMaterial,
            new GTPPMTECable(
                aStartID + 1,
                "wire." + aMaterial.getDefaultLocalName()
                    .toLowerCase() + ".02",
                "gt.oreprefix.2x_material_wire",
                0.25F,
                aLoss,
                2L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated,
                aMaterial).getStackForm(1L));
        registerOre(
            OrePrefixes.wireGt04,
            aMaterial,
            new GTPPMTECable(
                aStartID + 2,
                "wire." + aMaterial.getDefaultLocalName()
                    .toLowerCase() + ".04",
                "gt.oreprefix.4x_material_wire",
                0.375F,
                aLoss,
                4L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated,
                aMaterial).getStackForm(1L));
        registerOre(
            OrePrefixes.wireGt08,
            aMaterial,
            new GTPPMTECable(
                aStartID + 3,
                "wire." + aMaterial.getDefaultLocalName()
                    .toLowerCase() + ".08",
                "gt.oreprefix.8x_material_wire",
                0.50F,
                aLoss,
                8L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated,
                aMaterial).getStackForm(1L));
        registerOre(
            OrePrefixes.wireGt12,
            aMaterial,
            new GTPPMTECable(
                aStartID + 4,
                "wire." + aMaterial.getDefaultLocalName()
                    .toLowerCase() + ".12",
                "gt.oreprefix.12x_material_wire",
                0.625F,
                aLoss,
                12L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated,
                aMaterial).getStackForm(1L));
        registerOre(
            OrePrefixes.wireGt16,
            aMaterial,
            new GTPPMTECable(
                aStartID + 5,
                "wire." + aMaterial.getDefaultLocalName()
                    .toLowerCase() + ".16",
                "gt.oreprefix.16x_material_wire",
                0.75F,
                aLoss,
                16L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated,
                aMaterial).getStackForm(1L));
        if (aInsulatable) {
            registerOre(
                OrePrefixes.cableGt01,
                aMaterial,
                new GTPPMTECable(
                    aStartID + 6,
                    "cable." + aMaterial.getDefaultLocalName()
                        .toLowerCase() + ".01",
                    "gt.oreprefix.1x_material_cable",
                    0.25F,
                    aLossInsulated,
                    1L * aAmperage,
                    aVoltage,
                    true,
                    false,
                    aMaterial).getStackForm(1L));
            registerOre(
                OrePrefixes.cableGt02,
                aMaterial,
                new GTPPMTECable(
                    aStartID + 7,
                    "cable." + aMaterial.getDefaultLocalName()
                        .toLowerCase() + ".02",
                    "gt.oreprefix.2x_material_cable",
                    0.375F,
                    aLossInsulated,
                    2L * aAmperage,
                    aVoltage,
                    true,
                    false,
                    aMaterial).getStackForm(1L));
            registerOre(
                OrePrefixes.cableGt04,
                aMaterial,
                new GTPPMTECable(
                    aStartID + 8,
                    "cable." + aMaterial.getDefaultLocalName()
                        .toLowerCase() + ".04",
                    "gt.oreprefix.4x_material_cable",
                    0.5F,
                    aLossInsulated,
                    4L * aAmperage,
                    aVoltage,
                    true,
                    false,
                    aMaterial).getStackForm(1L));
            registerOre(
                OrePrefixes.cableGt08,
                aMaterial,
                new GTPPMTECable(
                    aStartID + 9,
                    "cable." + aMaterial.getDefaultLocalName()
                        .toLowerCase() + ".08",
                    "gt.oreprefix.8x_material_cable",
                    0.625F,
                    aLossInsulated,
                    8L * aAmperage,
                    aVoltage,
                    true,
                    false,
                    aMaterial).getStackForm(1L));
            registerOre(
                OrePrefixes.cableGt12,
                aMaterial,
                new GTPPMTECable(
                    aStartID + 10,
                    "cable." + aMaterial.getDefaultLocalName()
                        .toLowerCase() + ".12",
                    "gt.oreprefix.12x_material_cable",
                    0.75F,
                    aLossInsulated,
                    12L * aAmperage,
                    aVoltage,
                    true,
                    false,
                    aMaterial).getStackForm(1L));
            registerOre(
                OrePrefixes.cableGt16,
                aMaterial,
                new GTPPMTECable(
                    aStartID + 11,
                    "cable." + aMaterial.getDefaultLocalName()
                        .toLowerCase() + ".16",
                    "gt.oreprefix.16x_material_cable",
                    0.875f,
                    aLossInsulated,
                    16L * aAmperage,
                    aVoltage,
                    true,
                    false,
                    aMaterial).getStackForm(1L));
        }
    }

    private static void generateGTFluidPipes(final Materials material, final int startID, final int transferRatePerSec,
        final int heatResistance, final boolean isGasProof) {
        final int transferRatePerTick = transferRatePerSec / 20;
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeTiny.get(material),
            new MTEFluidPipe(
                startID,
                "GT_Pipe_" + material.mDefaultLocalName + "_Tiny",
                "gt.oreprefix.tiny_material_fluid_pipe",
                0.25F,
                material,
                transferRatePerTick * 2,
                heatResistance,
                isGasProof).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeSmall.get(material),
            new MTEFluidPipe(
                startID + 1,
                "GT_Pipe_" + material.mDefaultLocalName + "_Small",
                "gt.oreprefix.small_material_fluid_pipe",
                0.375F,
                material,
                transferRatePerTick * 4,
                heatResistance,
                isGasProof).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(material),
            new MTEFluidPipe(
                startID + 2,
                "GT_Pipe_" + material.mDefaultLocalName,
                "gt.oreprefix.material_fluid_pipe",
                0.5F,
                material,
                transferRatePerTick * 12,
                heatResistance,
                isGasProof).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(material),
            new MTEFluidPipe(
                startID + 3,
                "GT_Pipe_" + material.mDefaultLocalName + "_Large",
                "gt.oreprefix.large_material_fluid_pipe",
                0.75F,
                material,
                transferRatePerTick * 24,
                heatResistance,
                isGasProof).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeHuge.get(material),
            new MTEFluidPipe(
                startID + 4,
                "GT_Pipe_" + material.mDefaultLocalName + "_Huge",
                "gt.oreprefix.huge_material_fluid_pipe",
                0.875F,
                material,
                transferRatePerTick * 48,
                heatResistance,
                isGasProof).getStackForm(1L));
    }

    private static void generateNonGTFluidPipes(final PipeStats pipeStats, final int startID,
        final int transferRatePerSec, final int heatResistance) {
        final int transferRatePerTick = transferRatePerSec / 20;
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeTiny.get(pipeStats),
            new GTPPMTEFluidPipe(
                startID,
                "GT_Pipe_" + pipeStats.defaultLocalName + "_Tiny",
                "gt.oreprefix.tiny_material_fluid_pipe",
                0.25F,
                pipeStats,
                transferRatePerTick * 2,
                heatResistance,
                true).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeSmall.get(pipeStats),
            new GTPPMTEFluidPipe(
                startID + 1,
                "GT_Pipe_" + pipeStats.defaultLocalName + "_Small",
                "gt.oreprefix.small_material_fluid_pipe",
                0.375F,
                pipeStats,
                transferRatePerTick * 4,
                heatResistance,
                true).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(pipeStats),
            new GTPPMTEFluidPipe(
                startID + 2,
                "GT_Pipe_" + pipeStats.defaultLocalName,
                "gt.oreprefix.material_fluid_pipe",
                0.5F,
                pipeStats,
                transferRatePerTick * 12,
                heatResistance,
                true).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(pipeStats),
            new GTPPMTEFluidPipe(
                startID + 3,
                "GT_Pipe_" + pipeStats.defaultLocalName + "_Large",
                "gt.oreprefix.large_material_fluid_pipe",
                0.75F,
                pipeStats,
                transferRatePerTick * 24,
                heatResistance,
                true).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeHuge.get(pipeStats),
            new GTPPMTEFluidPipe(
                startID + 4,
                "GT_Pipe_" + pipeStats.defaultLocalName + "_Huge",
                "gt.oreprefix.huge_material_fluid_pipe",
                0.875F,
                pipeStats,
                transferRatePerTick * 48,
                heatResistance,
                true).getStackForm(1L));
    }

    public static void generatePipeRecipes(final Material material) {
        // generatePipeRecipes multiplies the voltage multiplier by 8 because ??! reasons.
        generatePipeRecipes(
            material,
            material.getDefaultLocalName(),
            material.getMass(),
            material.vVoltageMultiplier / 8);
    }

    public static void generatePipeRecipes(final Material material, final String materialName, final long Mass,
        final long vMulti) {

        String output = materialName.substring(0, 1)
            .toUpperCase() + materialName.substring(1);
        output = StringUtils.sanitizeString(output);

        if (output.equals("VoidMetal")) {
            output = "Void";
        }

        Logger.INFO("Generating " + output + " pipes & respective recipes.");

        ItemStack pipeIngot = ItemUtils.getItemStackOfAmountFromOreDict("ingot" + output, 1);
        ItemStack pipePlate = ItemUtils.getItemStackOfAmountFromOreDict("plate" + output, 1);

        if (pipeIngot == null) {
            if (pipePlate != null) {
                pipeIngot = pipePlate;
            }
        }

        // Check all pipes are not null
        Logger.WARNING(
            "Generated pipeTiny from " + materialName
                + "? "
                + (ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Tiny" + output, 1) != null));
        Logger.WARNING(
            "Generated pipeSmall from " + materialName
                + "? "
                + (ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Small" + output, 1) != null));
        Logger.WARNING(
            "Generated pipeNormal from " + materialName
                + "? "
                + (ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Medium" + output, 1) != null));
        Logger.WARNING(
            "Generated pipeLarge from " + materialName
                + "? "
                + (ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Large" + output, 1) != null));
        Logger.WARNING(
            "Generated pipeHuge from " + materialName
                + "? "
                + (ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Huge" + output, 1) != null));

        int eut = (int) (8 * vMulti);

        if (material != null && material.vVoltageMultiplier <= TierEU.RECIPE_IV) {
            // Add the Four Shaped Recipes First
            GTModHandler.addCraftingRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Tiny" + output, 8),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "PPP", "h w", "PPP", 'P', pipePlate });

            GTModHandler.addCraftingRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Small" + output, 6),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "PwP", "P P", "PhP", 'P', pipePlate });

            GTModHandler.addCraftingRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Medium" + output, 2),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "PPP", "w h", "PPP", 'P', pipePlate });

            GTModHandler.addCraftingRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Large" + output, 1),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "PhP", "P P", "PwP", 'P', pipePlate });
        }

        if (pipeIngot != null) {
            // 1 Clay Plate = 1 Clay Dust = 2 Clay Ball
            int inputMultiplier = materialName.equals("Clay") ? 2 : 1;
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmount(1 * inputMultiplier, pipeIngot),
                    ItemList.Shape_Extruder_Pipe_Tiny.get(0))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("pipe" + "Tiny" + output, 2))
                .duration(5 * TICKS)
                .eut(eut)
                .addTo(extruderRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmount(1 * inputMultiplier, pipeIngot),
                    ItemList.Shape_Extruder_Pipe_Small.get(0))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Small" + output, 1))
                .duration(10 * TICKS)
                .eut(eut)
                .addTo(extruderRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmount(3 * inputMultiplier, pipeIngot),
                    ItemList.Shape_Extruder_Pipe_Medium.get(0))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Medium" + output, 1))
                .duration(20 * TICKS)
                .eut(eut)
                .addTo(extruderRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmount(6 * inputMultiplier, pipeIngot),
                    ItemList.Shape_Extruder_Pipe_Large.get(0))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Large" + output, 1))
                .duration(2 * SECONDS)
                .eut(eut)
                .addTo(extruderRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmount(12 * inputMultiplier, pipeIngot),
                    ItemList.Shape_Extruder_Pipe_Huge.get(0))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Huge" + output, 1))
                .duration(4 * SECONDS)
                .eut(eut)
                .addTo(extruderRecipes);

        }

        if ((eut < 512) && !output.equals("Void")) {
            ItemStack pipePlateDouble = ItemUtils.getItemStackOfAmountFromOreDict("plateDouble" + output, 1);
            if (pipePlateDouble != null) {
                GTModHandler.addCraftingRecipe(
                    ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Huge" + output, 1),
                    new Object[] { "DhD", "D D", "DwD", 'D', pipePlateDouble.copy() });
            } else {
                Logger.INFO(
                    "Failed to add a recipe for " + materialName + " Huge pipes. Double plates probably do not exist.");
            }
        }

        if (material != null && material.getFluid() != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Pipe_Tiny.get(0L))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("pipe" + "Tiny" + output, 1))
                .fluidInputs(material.getFluidStack(1 * HALF_INGOTS))
                .duration(1 * SECONDS)
                .eut(eut)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Pipe_Small.get(0L))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Small" + output, 1))
                .fluidInputs(material.getFluidStack(1 * INGOTS))
                .duration(2 * SECONDS)
                .eut(eut)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Pipe_Medium.get(0L))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Medium" + output, 1))
                .fluidInputs(material.getFluidStack(3 * INGOTS))
                .duration(4 * SECONDS)
                .eut(eut)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Pipe_Large.get(0L))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Large" + output, 1))
                .fluidInputs(material.getFluidStack(6 * INGOTS))
                .duration(8 * SECONDS)
                .eut(eut)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Pipe_Huge.get(0L))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Huge" + output, 1))
                .fluidInputs(material.getFluidStack(12 * INGOTS))
                .duration(16 * SECONDS)
                .eut(eut)
                .addTo(fluidSolidifierRecipes);
        }
    }

    public static boolean registerOre(OrePrefixes aPrefix, Material aMaterial, ItemStack aStack) {
        return registerOre(aPrefix.get(StringUtils.sanitizeString(aMaterial.getDefaultLocalName())), aStack);
    }

    public static boolean registerOre(Object aName, ItemStack aStack) {
        if ((aName == null) || (GTUtility.isStackInvalid(aStack))) return false;
        String tName = aName.toString();
        if (GTUtility.isStringInvalid(tName)) return false;
        ArrayList<ItemStack> tList = GTOreDictUnificator.getOres(tName);
        for (ItemStack itemStack : tList) if (GTUtility.areStacksEqual(itemStack, aStack, true)) return false;
        OreDictionary.registerOre(tName, GTUtility.copyAmount(1, aStack));
        return true;
    }

    public static boolean generateWireRecipes(Material aMaterial) {

        ItemStack aPlate = aMaterial.getPlate(1);
        ItemStack aIngot = aMaterial.getIngot(1);
        ItemStack aRod = aMaterial.getRod(1);
        ItemStack aWire01 = aMaterial.getWire01(1);
        ItemStack aWire02 = aMaterial.getWire02(1);
        ItemStack aWire04 = aMaterial.getWire04(1);
        ItemStack aWire08 = aMaterial.getWire08(1);
        ItemStack aWire12 = aMaterial.getWire12(1);
        ItemStack aWire16 = aMaterial.getWire16(1);
        ItemStack aCable01 = aMaterial.getCable01(1);
        ItemStack aCable02 = aMaterial.getCable02(1);
        ItemStack aCable04 = aMaterial.getCable04(1);
        ItemStack aCable08 = aMaterial.getCable08(1);
        ItemStack aCable12 = aMaterial.getCable12(1);
        ItemStack aCable16 = aMaterial.getCable16(1);
        ItemStack aFineWire = aMaterial.getFineWire(1);

        // Adds manual crafting recipe
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aPlate, aWire01 })
            && aMaterial.vVoltageMultiplier < 7680) {
            GTModHandler.addCraftingRecipe(aWire01, new Object[] { "Px ", "   ", "   ", 'P', aPlate });
        }

        // Wire mill
        if (ItemUtils
            .checkForInvalidItems(new ItemStack[] { aIngot, aWire01, aWire02, aWire04, aWire08, aWire12, aWire16 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getIngot(1))
                .circuit(1)
                .itemOutputs(aMaterial.getWire01(2))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getIngot(1))
                .circuit(2)
                .itemOutputs(aMaterial.getWire02(1))
                .duration(7 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getIngot(2))
                .circuit(4)
                .itemOutputs(aMaterial.getWire04(1))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getIngot(4))
                .circuit(8)
                .itemOutputs(aMaterial.getWire08(1))
                .duration(12 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getIngot(6))
                .circuit(12)
                .itemOutputs(aMaterial.getWire12(1))
                .duration(15 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getIngot(8))
                .circuit(16)
                .itemOutputs(aMaterial.getWire16(1))
                .duration(17 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);

        }

        if (ItemUtils
            .checkForInvalidItems(new ItemStack[] { aRod, aWire01, aWire02, aWire04, aWire08, aWire12, aWire16 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getRod(1))
                .circuit(1)
                .itemOutputs(aMaterial.getWire01(1))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getRod(2))
                .circuit(2)
                .itemOutputs(aMaterial.getWire02(1))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getRod(4))
                .circuit(4)
                .itemOutputs(aMaterial.getWire04(1))
                .duration(7 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getRod(8))
                .circuit(8)
                .itemOutputs(aMaterial.getWire08(1))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getRod(12))
                .circuit(12)
                .itemOutputs(aMaterial.getWire12(1))
                .duration(12 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getRod(16))
                .circuit(16)
                .itemOutputs(aMaterial.getWire16(1))
                .duration(15 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);

        }

        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aIngot, aFineWire })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getIngot(1))
                .circuit(3)
                .itemOutputs(aMaterial.getFineWire(8))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);

        }

        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aRod, aFineWire })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getRod(1))
                .circuit(3)
                .itemOutputs(aMaterial.getFineWire(4))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);

        }

        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aFineWire })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getWire01(1))
                .circuit(1)
                .itemOutputs(aMaterial.getFineWire(4))
                .duration(10 * SECONDS)
                .eut(8)
                .addTo(wiremillRecipes);

        }

        // Extruder
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aIngot, aWire01 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aIngot, ItemList.Shape_Extruder_Wire.get(0))
                .itemOutputs(aMaterial.getWire01(2))
                .duration(9 * SECONDS + 16 * TICKS)
                .eut(96)
                .addTo(extruderRecipes);
        }

        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aCable01, aWire01 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aCable01)
                .itemOutputs(aWire01)
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(unpackagerRecipes);
        }

        // Shapeless Down-Crafting
        // 2x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire02 })) {
            GTModHandler.addShapelessCraftingRecipe(aMaterial.getWire01(2), new ItemStack[] { aWire02 });
        }

        // 4x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire04 })) {
            GTModHandler.addShapelessCraftingRecipe(aMaterial.getWire01(4), new ItemStack[] { aWire04 });
        }

        // 8x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire08 })) {
            GTModHandler.addShapelessCraftingRecipe(aMaterial.getWire01(8), new ItemStack[] { aWire08 });
        }

        // 12x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire12 })) {
            GTModHandler.addShapelessCraftingRecipe(aMaterial.getWire01(12), new ItemStack[] { aWire12 });
        }

        // 16x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire16 })) {
            GTModHandler.addShapelessCraftingRecipe(aMaterial.getWire01(16), new ItemStack[] { aWire16 });
        }

        // 1x -> 2x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire02 })) {
            GTModHandler.addShapelessCraftingRecipe(aWire02, new ItemStack[] { aWire01, aWire01 });
        }

        // 2x -> 4x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire02, aWire04 })) {
            GTModHandler.addShapelessCraftingRecipe(aWire04, new ItemStack[] { aWire02, aWire02 });
        }

        // 4x -> 8x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire04, aWire08 })) {
            GTModHandler.addShapelessCraftingRecipe(aWire08, new ItemStack[] { aWire04, aWire04 });
        }

        // 8x -> 12x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire04, aWire08, aWire12 })) {
            GTModHandler.addShapelessCraftingRecipe(aWire12, new ItemStack[] { aWire04, aWire08 });
        }

        // 12x -> 16x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire04, aWire12, aWire16 })) {
            GTModHandler.addShapelessCraftingRecipe(aWire16, new ItemStack[] { aWire04, aWire12 });
        }

        // 8x -> 16x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire08, aWire16 })) {
            GTModHandler.addShapelessCraftingRecipe(aWire16, new ItemStack[] { aWire08, aWire08 });
        }

        // 1x -> 4x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire04 })) {
            GTModHandler.addShapelessCraftingRecipe(aWire04, new ItemStack[] { aWire01, aWire01, aWire01, aWire01 });
        }

        // 1x -> 8x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire08 })) {
            GTModHandler.addShapelessCraftingRecipe(
                aWire08,
                new ItemStack[] { aWire01, aWire01, aWire01, aWire01, aWire01, aWire01, aWire01, aWire01 });
        }

        // Wire to Cable
        // 1x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aCable01 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aWire01)
                .circuit(24)
                .itemOutputs(aCable01)
                .fluidInputs(Materials.Rubber.getMolten(1 * INGOTS))
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);

        }

        // 2x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire02, aCable02 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aWire02)
                .circuit(24)
                .itemOutputs(aCable02)
                .fluidInputs(Materials.Rubber.getMolten(1 * INGOTS))
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);

        }

        // 4x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire04, aCable04 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aWire04)
                .circuit(24)
                .itemOutputs(aCable04)
                .fluidInputs(Materials.Rubber.getMolten(2 * INGOTS))
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);

        }

        // 8x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire08, aCable08 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aWire08)
                .circuit(24)
                .itemOutputs(aCable08)
                .fluidInputs(Materials.Rubber.getMolten(3 * INGOTS))
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);

        }

        // 12x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire12, aCable12 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aWire12)
                .circuit(24)
                .itemOutputs(aCable12)
                .fluidInputs(Materials.Rubber.getMolten(4 * INGOTS))
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);

        }

        // 16x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire16, aCable16 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aWire16)
                .circuit(24)
                .itemOutputs(aCable16)
                .fluidInputs(Materials.Rubber.getMolten(5 * INGOTS))
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);

        }

        // Assemble small wires into bigger wires
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire02 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getWire01(2))
                .circuit(2)
                .itemOutputs(aWire02)
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getWire01(4))
                .circuit(4)
                .itemOutputs(aWire04)
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getWire01(8))
                .circuit(8)
                .itemOutputs(aWire08)
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getWire01(12))
                .circuit(12)
                .itemOutputs(aWire12)
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getWire01(16))
                .circuit(16)
                .itemOutputs(aWire16)
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);
        }

        return true;
    }
}
