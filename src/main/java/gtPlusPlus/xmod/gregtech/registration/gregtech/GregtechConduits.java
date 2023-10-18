package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.Thaumcraft;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GregtechMetaPipeEntityFluid;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GregtechMetaPipeEntity_Cable;

public class GregtechConduits {

    // 30000-30999

    private static int BaseWireID = 30600;
    private static int BasePipeID = 30700;
    private static int BasePipeHexadecupleID = 30100;

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Custom Cables/Wires/Pipes.");
        if (CORE.ConfigSwitches.enableCustom_Cables) {
            run1();
        }
        if (CORE.ConfigSwitches.enableCustom_Pipes) {
            run2();
            run3();
        }
    }

    private static void run3() {

        try {
            Class<GT_MetaPipeEntity_Fluid> aPipeEntity = GT_MetaPipeEntity_Fluid.class;
            Constructor<GT_MetaPipeEntity_Fluid> constructor = aPipeEntity.getConstructor(
                    int.class,
                    String.class,
                    String.class,
                    float.class,
                    Materials.class,
                    int.class,
                    int.class,
                    boolean.class,
                    int.class);
            if (constructor != null) {
                Logger.INFO("Generating Hexadecuple pipes.");
                generateFluidMultiPipes(
                        constructor,
                        Materials.Copper,
                        MaterialUtils.getMaterialName(Materials.Copper),
                        "Copper",
                        BasePipeHexadecupleID++,
                        60,
                        1000,
                        true);
                generateFluidMultiPipes(
                        constructor,
                        Materials.Bronze,
                        MaterialUtils.getMaterialName(Materials.Bronze),
                        "Bronze",
                        BasePipeHexadecupleID++,
                        120,
                        2000,
                        true);
                generateFluidMultiPipes(
                        constructor,
                        Materials.Steel,
                        MaterialUtils.getMaterialName(Materials.Steel),
                        "Steel",
                        BasePipeHexadecupleID++,
                        240,
                        2500,
                        true);
                generateFluidMultiPipes(
                        constructor,
                        Materials.StainlessSteel,
                        MaterialUtils.getMaterialName(Materials.StainlessSteel),
                        "Stainless Steel",
                        BasePipeHexadecupleID++,
                        360,
                        3000,
                        true);
                generateFluidMultiPipes(
                        constructor,
                        Materials.Titanium,
                        MaterialUtils.getMaterialName(Materials.Titanium),
                        "Titanium",
                        BasePipeHexadecupleID++,
                        480,
                        5000,
                        true);
                generateFluidMultiPipes(
                        constructor,
                        Materials.TungstenSteel,
                        MaterialUtils.getMaterialName(Materials.TungstenSteel),
                        "Tungsten Steel",
                        BasePipeHexadecupleID++,
                        600,
                        7500,
                        true);
                generateFluidMultiPipes(
                        constructor,
                        Materials.Plastic,
                        MaterialUtils.getMaterialName(Materials.Plastic),
                        "Plastic",
                        BasePipeHexadecupleID++,
                        360,
                        350,
                        true);

                Materials aPTFE = Materials.get("Polytetrafluoroethylene");
                if (aPTFE != null) {
                    generateFluidMultiPipes(
                            constructor,
                            aPTFE,
                            MaterialUtils.getMaterialName(aPTFE),
                            "PTFE",
                            BasePipeHexadecupleID++,
                            480,
                            600,
                            true);
                }
            } else {
                Logger.INFO("Failed during Hexadecuple pipe generation.");
            }

        } catch (NoSuchMethodException | SecurityException e) {
            Logger.INFO("Failed during Hexadecuple pipe generation. [Ecx]");
            e.printStackTrace();
        }

        // Generate Heat Pipes
        // GregtechItemList.HeatPipe_Tier_1.set(new GT_MetaPipeEntity_Heat(31021, "gtpp.pipe.heat.basic.01", "Lead Heat
        // Pipe (500C)", Materials.Lead, 500).getStackForm(1L));
        // GregtechItemList.HeatPipe_Tier_2.set(new GT_MetaPipeEntity_Heat(31022, "gtpp.pipe.heat.basic.02", "Iron Heat
        // Pipe (500C)", Materials.Iron, 500).getStackForm(1L));
        // GregtechItemList.HeatPipe_Tier_3.set(new GT_MetaPipeEntity_Heat(31023, "gtpp.pipe.heat.basic.03", "Silver
        // Heat Pipe (1500C)", Materials.Silver, 1500).getStackForm(1L));

    }

    private static void generateFluidMultiPipes(Constructor<GT_MetaPipeEntity_Fluid> aClazz, Materials aMaterial,
            String name, String displayName, int startID, int transferRatePerSec, int heatCapacity, boolean gasProof) {
        GT_MetaPipeEntity_Fluid aPipe;
        final int transferRatePerTick = transferRatePerSec / 20;
        try {
            aPipe = aClazz.newInstance(
                    startID,
                    "GT_Pipe_" + name + "_Hexadecuple",
                    "Hexadecuple " + displayName + " Fluid Pipe",
                    1.0F,
                    aMaterial,
                    transferRatePerTick,
                    heatCapacity,
                    gasProof,
                    16);
            if (aPipe == null) {
                Logger.INFO("Failed to Generate " + aMaterial + " Hexadecuple pipes.");
            } else {
                Logger.INFO("Generated " + aMaterial + " Hexadecuple pipes.");
                GT_OreDictUnificator.registerOre("pipeHexadecuple" + aMaterial, aPipe.getStackForm(1L));
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            Logger.INFO("Failed to Generate " + aMaterial + " Hexadecuple pipes. [Ecx]");
            e.printStackTrace();
        }
    }

    private static void run1() {

        wireFactory("RedstoneAlloy", 32, BaseWireID + 45, 0, 2, 1, new short[] { 178, 34, 34, 0 });

        // need to go back id because fluid pipes already occupy
        makeCustomWires(ELEMENT.STANDALONE.HYPOGEN, BaseWireID - 15, 0, 0, 8, GT_Values.V[11], false, true);

    }

    private static void run2() {
        generateNonGTFluidPipes(GT_Materials.Staballoy, ALLOY.STABALLOY, BasePipeID, 12500, 7500, true);
        generateNonGTFluidPipes(GT_Materials.Tantalloy60, ALLOY.TANTALLOY_60, BasePipeID + 5, 10000, 4250, true);
        generateNonGTFluidPipes(GT_Materials.Tantalloy61, ALLOY.TANTALLOY_61, BasePipeID + 10, 12000, 5800, true);
        if (Thaumcraft.isModLoaded()) {
            generateNonGTFluidPipes(GT_Materials.Void, null, BasePipeID + 15, 1600, 25000, true);
        }
        generateGTFluidPipes(Materials.Europium, BasePipeID + 20, 12000, 7500, true);
        generateNonGTFluidPipes(GT_Materials.Potin, ALLOY.POTIN, BasePipeID + 25, 500, 2000, true);
        generateNonGTFluidPipes(GT_Materials.MaragingSteel300, ALLOY.MARAGING300, BasePipeID + 30, 14000, 2500, true);
        generateNonGTFluidPipes(GT_Materials.MaragingSteel350, ALLOY.MARAGING350, BasePipeID + 35, 16000, 2500, true);
        generateNonGTFluidPipes(GT_Materials.Inconel690, ALLOY.INCONEL_690, BasePipeID + 40, 15000, 4800, true);
        generateNonGTFluidPipes(GT_Materials.Inconel792, ALLOY.INCONEL_792, BasePipeID + 45, 16000, 5500, true);
        generateNonGTFluidPipes(GT_Materials.HastelloyX, ALLOY.HASTELLOY_X, BasePipeID + 50, 20000, 4200, true);

        generateGTFluidPipes(Materials.Tungsten, BasePipeID + 55, 4320, 7200, true);
        if (EnderIO.isModLoaded()) {
            generateGTFluidPipes(Materials.DarkSteel, BasePipeID + 60, 2320, 2750, true);
        }
        generateGTFluidPipes(Materials.Clay, BasePipeID + 65, 100, 500, false);
        generateGTFluidPipes(Materials.Lead, BasePipeID + 70, 350, 1200, true);

        generateNonGTFluidPipes(
                GT_Materials.TriniumNaquadahCarbonite,
                ALLOY.TRINIUM_NAQUADAH_CARBON,
                30500,
                20,
                250000,
                true);
    }

    private static void wireFactory(final String Material, final int Voltage, final int ID, final long insulatedLoss,
            final long uninsulatedLoss, final long Amps, final short[] rgb) {
        final Materials T = Materials.get(Material);
        int V = GT_Utility.getTier(Voltage);
        if (V == -1) {
            Logger.ERROR("Failed to set voltage on " + Material + ". Invalid voltage of " + Voltage + "V set.");
            Logger.ERROR(Material + " has defaulted to 8v.");
            V = 0;
        }
        makeWires(T, ID, insulatedLoss, uninsulatedLoss, Amps, GT_Values.V[V], true, false, rgb);
    }

    private static void makeWires(final Materials aMaterial, final int aStartID, final long aLossInsulated,
            final long aLoss, final long aAmperage, final long aVoltage, final boolean aInsulatable,
            final boolean aAutoInsulated, final short[] aRGB) {
        Logger.WARNING("Gregtech5u Content | Registered " + aMaterial.mName + " as a new material for Wire & Cable.");
        GT_OreDictUnificator.registerOre(
                OrePrefixes.wireGt01,
                aMaterial,
                new GregtechMetaPipeEntity_Cable(
                        aStartID + 0,
                        "wire." + aMaterial.mName.toLowerCase() + ".01",
                        "1x " + aMaterial.mDefaultLocalName + " Wire",
                        0.125F,
                        aMaterial,
                        aLoss,
                        1L * aAmperage,
                        aVoltage,
                        false,
                        !aAutoInsulated,
                        aRGB).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
                OrePrefixes.wireGt02,
                aMaterial,
                new GregtechMetaPipeEntity_Cable(
                        aStartID + 1,
                        "wire." + aMaterial.mName.toLowerCase() + ".02",
                        "2x " + aMaterial.mDefaultLocalName + " Wire",
                        0.25F,
                        aMaterial,
                        aLoss,
                        2L * aAmperage,
                        aVoltage,
                        false,
                        !aAutoInsulated,
                        aRGB).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
                OrePrefixes.wireGt04,
                aMaterial,
                new GregtechMetaPipeEntity_Cable(
                        aStartID + 2,
                        "wire." + aMaterial.mName.toLowerCase() + ".04",
                        "4x " + aMaterial.mDefaultLocalName + " Wire",
                        0.375F,
                        aMaterial,
                        aLoss,
                        4L * aAmperage,
                        aVoltage,
                        false,
                        !aAutoInsulated,
                        aRGB).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
                OrePrefixes.wireGt08,
                aMaterial,
                new GregtechMetaPipeEntity_Cable(
                        aStartID + 3,
                        "wire." + aMaterial.mName.toLowerCase() + ".08",
                        "8x " + aMaterial.mDefaultLocalName + " Wire",
                        0.50F,
                        aMaterial,
                        aLoss,
                        8L * aAmperage,
                        aVoltage,
                        false,
                        !aAutoInsulated,
                        aRGB).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
                OrePrefixes.wireGt12,
                aMaterial,
                new GregtechMetaPipeEntity_Cable(
                        aStartID + 4,
                        "wire." + aMaterial.mName.toLowerCase() + ".12",
                        "12x " + aMaterial.mDefaultLocalName + " Wire",
                        0.625F,
                        aMaterial,
                        aLoss,
                        12L * aAmperage,
                        aVoltage,
                        false,
                        !aAutoInsulated,
                        aRGB).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
                OrePrefixes.wireGt16,
                aMaterial,
                new GregtechMetaPipeEntity_Cable(
                        aStartID + 5,
                        "wire." + aMaterial.mName.toLowerCase() + ".16",
                        "16x " + aMaterial.mDefaultLocalName + " Wire",
                        0.75F,
                        aMaterial,
                        aLoss,
                        16L * aAmperage,
                        aVoltage,
                        false,
                        !aAutoInsulated,
                        aRGB).getStackForm(1L));
        if (aInsulatable) {
            GT_OreDictUnificator.registerOre(
                    OrePrefixes.cableGt01,
                    aMaterial,
                    new GregtechMetaPipeEntity_Cable(
                            aStartID + 6,
                            "cable." + aMaterial.mName.toLowerCase() + ".01",
                            "1x " + aMaterial.mDefaultLocalName + " Cable",
                            0.25F,
                            aMaterial,
                            aLossInsulated,
                            1L * aAmperage,
                            aVoltage,
                            true,
                            false,
                            aRGB).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                    OrePrefixes.cableGt02,
                    aMaterial,
                    new GregtechMetaPipeEntity_Cable(
                            aStartID + 7,
                            "cable." + aMaterial.mName.toLowerCase() + ".02",
                            "2x " + aMaterial.mDefaultLocalName + " Cable",
                            0.375F,
                            aMaterial,
                            aLossInsulated,
                            2L * aAmperage,
                            aVoltage,
                            true,
                            false,
                            aRGB).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                    OrePrefixes.cableGt04,
                    aMaterial,
                    new GregtechMetaPipeEntity_Cable(
                            aStartID + 8,
                            "cable." + aMaterial.mName.toLowerCase() + ".04",
                            "4x " + aMaterial.mDefaultLocalName + " Cable",
                            0.5F,
                            aMaterial,
                            aLossInsulated,
                            4L * aAmperage,
                            aVoltage,
                            true,
                            false,
                            aRGB).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                    OrePrefixes.cableGt08,
                    aMaterial,
                    new GregtechMetaPipeEntity_Cable(
                            aStartID + 9,
                            "cable." + aMaterial.mName.toLowerCase() + ".08",
                            "8x " + aMaterial.mDefaultLocalName + " Cable",
                            0.625F,
                            aMaterial,
                            aLossInsulated,
                            8L * aAmperage,
                            aVoltage,
                            true,
                            false,
                            aRGB).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                    OrePrefixes.cableGt12,
                    aMaterial,
                    new GregtechMetaPipeEntity_Cable(
                            aStartID + 10,
                            "cable." + aMaterial.mName.toLowerCase() + ".12",
                            "12x " + aMaterial.mDefaultLocalName + " Cable",
                            0.75F,
                            aMaterial,
                            aLossInsulated,
                            12L * aAmperage,
                            aVoltage,
                            true,
                            false,
                            aRGB).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                    OrePrefixes.cableGt16,
                    aMaterial,
                    new GregtechMetaPipeEntity_Cable(
                            aStartID + 11,
                            "cable." + aMaterial.mName.toLowerCase() + ".16",
                            "16x " + aMaterial.mDefaultLocalName + " Cable",
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
        int V = GT_Utility.getTier(Voltage);
        if (V == -1) {
            Logger.ERROR("Failed to set voltage on " + Material + ". Invalid voltage of " + Voltage + "V set.");
            Logger.ERROR(Material + " has defaulted to 8v.");
            V = 0;
        }
        makeCustomWires(Material, ID, insulatedLoss, uninsulatedLoss, Amps, GT_Values.V[V], true, false);
    }

    private static void makeCustomWires(final Material aMaterial, final int aStartID, final long aLossInsulated,
            final long aLoss, final long aAmperage, final long aVoltage, final boolean aInsulatable,
            final boolean aAutoInsulated) {
        Logger.WARNING(
                "Gregtech5u Content | Registered " + aMaterial.getLocalizedName()
                        + " as a new material for Wire & Cable.");
        registerOre(
                OrePrefixes.wireGt01,
                aMaterial,
                new GregtechMetaPipeEntity_Cable(
                        aStartID + 0,
                        "wire." + aMaterial.getLocalizedName().toLowerCase() + ".01",
                        "1x " + aMaterial.getLocalizedName() + " Wire",
                        0.125F,
                        aLoss,
                        1L * aAmperage,
                        aVoltage,
                        false,
                        !aAutoInsulated,
                        aMaterial.getRGBA()).getStackForm(1L));
        registerOre(
                OrePrefixes.wireGt02,
                aMaterial,
                new GregtechMetaPipeEntity_Cable(
                        aStartID + 1,
                        "wire." + aMaterial.getLocalizedName().toLowerCase() + ".02",
                        "2x " + aMaterial.getLocalizedName() + " Wire",
                        0.25F,
                        aLoss,
                        2L * aAmperage,
                        aVoltage,
                        false,
                        !aAutoInsulated,
                        aMaterial.getRGBA()).getStackForm(1L));
        registerOre(
                OrePrefixes.wireGt04,
                aMaterial,
                new GregtechMetaPipeEntity_Cable(
                        aStartID + 2,
                        "wire." + aMaterial.getLocalizedName().toLowerCase() + ".04",
                        "4x " + aMaterial.getLocalizedName() + " Wire",
                        0.375F,
                        aLoss,
                        4L * aAmperage,
                        aVoltage,
                        false,
                        !aAutoInsulated,
                        aMaterial.getRGBA()).getStackForm(1L));
        registerOre(
                OrePrefixes.wireGt08,
                aMaterial,
                new GregtechMetaPipeEntity_Cable(
                        aStartID + 3,
                        "wire." + aMaterial.getLocalizedName().toLowerCase() + ".08",
                        "8x " + aMaterial.getLocalizedName() + " Wire",
                        0.50F,
                        aLoss,
                        8L * aAmperage,
                        aVoltage,
                        false,
                        !aAutoInsulated,
                        aMaterial.getRGBA()).getStackForm(1L));
        registerOre(
                OrePrefixes.wireGt12,
                aMaterial,
                new GregtechMetaPipeEntity_Cable(
                        aStartID + 4,
                        "wire." + aMaterial.getLocalizedName().toLowerCase() + ".12",
                        "12x " + aMaterial.getLocalizedName() + " Wire",
                        0.625F,
                        aLoss,
                        12L * aAmperage,
                        aVoltage,
                        false,
                        !aAutoInsulated,
                        aMaterial.getRGBA()).getStackForm(1L));
        registerOre(
                OrePrefixes.wireGt16,
                aMaterial,
                new GregtechMetaPipeEntity_Cable(
                        aStartID + 5,
                        "wire." + aMaterial.getLocalizedName().toLowerCase() + ".16",
                        "16x " + aMaterial.getLocalizedName() + " Wire",
                        0.75F,
                        aLoss,
                        16L * aAmperage,
                        aVoltage,
                        false,
                        !aAutoInsulated,
                        aMaterial.getRGBA()).getStackForm(1L));
        if (aInsulatable) {
            registerOre(
                    OrePrefixes.cableGt01,
                    aMaterial,
                    new GregtechMetaPipeEntity_Cable(
                            aStartID + 6,
                            "cable." + aMaterial.getLocalizedName().toLowerCase() + ".01",
                            "1x " + aMaterial.getLocalizedName() + " Cable",
                            0.25F,
                            aLossInsulated,
                            1L * aAmperage,
                            aVoltage,
                            true,
                            false,
                            aMaterial.getRGBA()).getStackForm(1L));
            registerOre(
                    OrePrefixes.cableGt02,
                    aMaterial,
                    new GregtechMetaPipeEntity_Cable(
                            aStartID + 7,
                            "cable." + aMaterial.getLocalizedName().toLowerCase() + ".02",
                            "2x " + aMaterial.getLocalizedName() + " Cable",
                            0.375F,
                            aLossInsulated,
                            2L * aAmperage,
                            aVoltage,
                            true,
                            false,
                            aMaterial.getRGBA()).getStackForm(1L));
            registerOre(
                    OrePrefixes.cableGt04,
                    aMaterial,
                    new GregtechMetaPipeEntity_Cable(
                            aStartID + 8,
                            "cable." + aMaterial.getLocalizedName().toLowerCase() + ".04",
                            "4x " + aMaterial.getLocalizedName() + " Cable",
                            0.5F,
                            aLossInsulated,
                            4L * aAmperage,
                            aVoltage,
                            true,
                            false,
                            aMaterial.getRGBA()).getStackForm(1L));
            registerOre(
                    OrePrefixes.cableGt08,
                    aMaterial,
                    new GregtechMetaPipeEntity_Cable(
                            aStartID + 9,
                            "cable." + aMaterial.getLocalizedName().toLowerCase() + ".08",
                            "8x " + aMaterial.getLocalizedName() + " Cable",
                            0.625F,
                            aLossInsulated,
                            8L * aAmperage,
                            aVoltage,
                            true,
                            false,
                            aMaterial.getRGBA()).getStackForm(1L));
            registerOre(
                    OrePrefixes.cableGt12,
                    aMaterial,
                    new GregtechMetaPipeEntity_Cable(
                            aStartID + 10,
                            "cable." + aMaterial.getLocalizedName().toLowerCase() + ".12",
                            "12x " + aMaterial.getLocalizedName() + " Cable",
                            0.75F,
                            aLossInsulated,
                            12L * aAmperage,
                            aVoltage,
                            true,
                            false,
                            aMaterial.getRGBA()).getStackForm(1L));
            registerOre(
                    OrePrefixes.cableGt16,
                    aMaterial,
                    new GregtechMetaPipeEntity_Cable(
                            aStartID + 11,
                            "cable." + aMaterial.getLocalizedName().toLowerCase() + ".16",
                            "16x " + aMaterial.getLocalizedName() + " Cable",
                            0.875f,
                            aLossInsulated,
                            16L * aAmperage,
                            aVoltage,
                            true,
                            false,
                            aMaterial.getRGBA()).getStackForm(1L));
        }
    }

    private static void superConductorFactory(final GT_Materials Material, final int Voltage, final int ID,
            final long insulatedLoss, final long uninsulatedLoss, final long Amps) {
        final GT_Materials T = Material;
        int V = 0;
        if (Voltage == 8) {
            V = 0;
        } else if (Voltage == 32) {
            V = 1;
        } else if (Voltage == 128) {
            V = 2;
        } else if (Voltage == 512) {
            V = 3;
        } else if (Voltage == 2048) {
            V = 4;
        } else if (Voltage == 8196) {
            V = 5;
        } else if (Voltage == 32768) {
            V = 6;
        } else if (Voltage == 131072) {
            V = 7;
        } else if (Voltage == 524288) {
            V = 8;
        } else if (Voltage == Integer.MAX_VALUE) {
            V = 9;
        } else {
            Logger.ERROR("Failed to set voltage on " + Material.name() + ". Invalid voltage of " + Voltage + "V set.");
            Logger.ERROR(Material.name() + " has defaulted to 8v.");
            V = 0;
        }
    }

    private static void generateGTFluidPipes(final Materials material, final int startID, final int transferRatePerSec,
            final int heatResistance, final boolean isGasProof) {
        final int transferRatePerTick = transferRatePerSec / 20;
        final long mass = material.getMass();
        final long voltage = material.mMeltingPoint >= 2800 ? 64 : 16;
        GT_OreDictUnificator.registerOre(
                OrePrefixes.pipeTiny.get(material),
                new GT_MetaPipeEntity_Fluid(
                        startID,
                        "GT_Pipe_" + material.mDefaultLocalName + "_Tiny",
                        "Tiny " + material.mDefaultLocalName + " Fluid Pipe",
                        0.25F,
                        material,
                        transferRatePerTick * 2,
                        heatResistance,
                        isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
                OrePrefixes.pipeSmall.get(material),
                new GT_MetaPipeEntity_Fluid(
                        startID + 1,
                        "GT_Pipe_" + material.mDefaultLocalName + "_Small",
                        "Small " + material.mDefaultLocalName + " Fluid Pipe",
                        0.375F,
                        material,
                        transferRatePerTick * 4,
                        heatResistance,
                        isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
                OrePrefixes.pipeMedium.get(material),
                new GT_MetaPipeEntity_Fluid(
                        startID + 2,
                        "GT_Pipe_" + material.mDefaultLocalName + "",
                        "" + material.mDefaultLocalName + " Fluid Pipe",
                        0.5F,
                        material,
                        transferRatePerTick * 12,
                        heatResistance,
                        isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
                OrePrefixes.pipeLarge.get(material),
                new GT_MetaPipeEntity_Fluid(
                        startID + 3,
                        "GT_Pipe_" + material.mDefaultLocalName + "_Large",
                        "Large " + material.mDefaultLocalName + " Fluid Pipe",
                        0.75F,
                        material,
                        transferRatePerTick * 24,
                        heatResistance,
                        isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
                OrePrefixes.pipeHuge.get(material),
                new GT_MetaPipeEntity_Fluid(
                        startID + 4,
                        "GT_Pipe_" + material.mDefaultLocalName + "_Huge",
                        "Huge " + material.mDefaultLocalName + " Fluid Pipe",
                        0.875F,
                        material,
                        transferRatePerTick * 48,
                        heatResistance,
                        isGasProof).getStackForm(1L));
        // generatePipeRecipes(material.mDefaultLocalName, mass, voltage);
    }

    private static void generateNonGTFluidPipes(final GT_Materials material, final Material myMaterial,
            final int startID, final int transferRatePerSec, final int heatResistance, final boolean isGasProof) {
        final int transferRatePerTick = transferRatePerSec / 20;
        long mass;
        if (myMaterial != null) {
            mass = myMaterial.getMass();
        } else {
            mass = ELEMENT.getInstance().IRON.getMass();
        }

        int tVoltageMultiplier = (material.mBlastFurnaceTemp >= 2800) ? 64 : 16;

        GT_OreDictUnificator.registerOre(
                OrePrefixes.pipeTiny.get(material),
                new GregtechMetaPipeEntityFluid(
                        startID,
                        "GT_Pipe_" + material.mDefaultLocalName + "_Tiny",
                        "Tiny " + material.mDefaultLocalName + " Fluid Pipe",
                        0.25F,
                        material,
                        transferRatePerTick * 2,
                        heatResistance,
                        isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
                OrePrefixes.pipeSmall.get(material),
                new GregtechMetaPipeEntityFluid(
                        startID + 1,
                        "GT_Pipe_" + material.mDefaultLocalName + "_Small",
                        "Small " + material.mDefaultLocalName + " Fluid Pipe",
                        0.375F,
                        material,
                        transferRatePerTick * 4,
                        heatResistance,
                        isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
                OrePrefixes.pipeMedium.get(material),
                new GregtechMetaPipeEntityFluid(
                        startID + 2,
                        "GT_Pipe_" + material.mDefaultLocalName + "",
                        "" + material.mDefaultLocalName + " Fluid Pipe",
                        0.5F,
                        material,
                        transferRatePerTick * 12,
                        heatResistance,
                        isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
                OrePrefixes.pipeLarge.get(material),
                new GregtechMetaPipeEntityFluid(
                        startID + 3,
                        "GT_Pipe_" + material.mDefaultLocalName + "_Large",
                        "Large " + material.mDefaultLocalName + " Fluid Pipe",
                        0.75F,
                        material,
                        transferRatePerTick * 24,
                        heatResistance,
                        isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
                OrePrefixes.pipeHuge.get(material),
                new GregtechMetaPipeEntityFluid(
                        startID + 4,
                        "GT_Pipe_" + material.mDefaultLocalName + "_Huge",
                        "Huge " + material.mDefaultLocalName + " Fluid Pipe",
                        0.875F,
                        material,
                        transferRatePerTick * 48,
                        heatResistance,
                        isGasProof).getStackForm(1L));
        // generatePipeRecipes(material.mDefaultLocalName, mass, tVoltageMultiplier);

    }

    public static void generatePipeRecipes(final String materialName, final long Mass, final long vMulti) {

        String output = materialName.substring(0, 1).toUpperCase() + materialName.substring(1);
        output = Utils.sanitizeString(output);

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
                        + ((ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Tiny" + output, 1) != null) ? true
                                : false));
        Logger.WARNING(
                "Generated pipeSmall from " + materialName
                        + "? "
                        + ((ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Small" + output, 1) != null) ? true
                                : false));
        Logger.WARNING(
                "Generated pipeNormal from " + materialName
                        + "? "
                        + ((ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Medium" + output, 1) != null) ? true
                                : false));
        Logger.WARNING(
                "Generated pipeLarge from " + materialName
                        + "? "
                        + ((ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Large" + output, 1) != null) ? true
                                : false));
        Logger.WARNING(
                "Generated pipeHuge from " + materialName
                        + "? "
                        + ((ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Huge" + output, 1) != null) ? true
                                : false));

        int eut = 120;
        eut = (int) (8 * vMulti);

        // Add the Three Shaped Recipes First
        RecipeUtils.addShapedRecipe(
                pipePlate,
                "craftingToolWrench",
                pipePlate,
                pipePlate,
                null,
                pipePlate,
                pipePlate,
                "craftingToolHardHammer",
                pipePlate,
                ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Small" + output, 6));

        RecipeUtils.addShapedRecipe(
                pipePlate,
                pipePlate,
                pipePlate,
                "craftingToolWrench",
                null,
                "craftingToolHardHammer",
                pipePlate,
                pipePlate,
                pipePlate,
                ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Medium" + output, 2));

        RecipeUtils.addShapedRecipe(
                pipePlate,
                "craftingToolHardHammer",
                pipePlate,
                pipePlate,
                null,
                pipePlate,
                pipePlate,
                "craftingToolWrench",
                pipePlate,
                ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Large" + output, 1));

        if (pipeIngot != null && ItemUtils.checkForInvalidItems(pipeIngot)) {
            // 1 Clay Plate = 1 Clay Dust = 2 Clay Ball
            int inputMultiplier = materialName.equals("Clay") ? 2 : 1;
            GT_Values.RA.addExtruderRecipe(
                    ItemUtils.getSimpleStack(pipeIngot, 1 * inputMultiplier),
                    ItemList.Shape_Extruder_Pipe_Tiny.get(0),
                    ItemUtils.getItemStackOfAmountFromOreDictNoBroken("pipe" + "Tiny" + output, 2),
                    5,
                    eut);

            GT_Values.RA.addExtruderRecipe(
                    ItemUtils.getSimpleStack(pipeIngot, 1 * inputMultiplier),
                    ItemList.Shape_Extruder_Pipe_Small.get(0),
                    ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Small" + output, 1),
                    10,
                    eut);

            GT_Values.RA.addExtruderRecipe(
                    ItemUtils.getSimpleStack(pipeIngot, 3 * inputMultiplier),
                    ItemList.Shape_Extruder_Pipe_Medium.get(0),
                    ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Medium" + output, 1),
                    1 * 20,
                    eut);

            GT_Values.RA.addExtruderRecipe(
                    ItemUtils.getSimpleStack(pipeIngot, 6 * inputMultiplier),
                    ItemList.Shape_Extruder_Pipe_Large.get(0),
                    ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Large" + output, 1),
                    2 * 20,
                    eut);

            GT_Values.RA.addExtruderRecipe(
                    ItemUtils.getSimpleStack(pipeIngot, 12 * inputMultiplier),
                    ItemList.Shape_Extruder_Pipe_Huge.get(0),
                    ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Huge" + output, 1),
                    4 * 20,
                    eut);
        }

        if ((eut < 512) && !output.equals("Void")) {
            try {
                final ItemStack pipePlateDouble = ItemUtils.getItemStackOfAmountFromOreDict("plateDouble" + output, 1)
                        .copy();
                if (pipePlateDouble != null) {
                    RecipeUtils.addShapedRecipe(
                            pipePlateDouble,
                            "craftingToolHardHammer",
                            pipePlateDouble,
                            pipePlateDouble,
                            null,
                            pipePlateDouble,
                            pipePlateDouble,
                            "craftingToolWrench",
                            pipePlateDouble,
                            ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Huge" + output, 1));
                } else {
                    Logger.INFO(
                            "Failed to add a recipe for " + materialName
                                    + " Huge pipes. Double plates probably do not exist.");
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static boolean registerOre(OrePrefixes aPrefix, Material aMaterial, ItemStack aStack) {
        return registerOre(aPrefix.get(Utils.sanitizeString(aMaterial.getLocalizedName())), aStack);
    }

    public static boolean registerOre(Object aName, ItemStack aStack) {
        if ((aName == null) || (GT_Utility.isStackInvalid(aStack))) return false;
        String tName = aName.toString();
        if (GT_Utility.isStringInvalid(tName)) return false;
        ArrayList<ItemStack> tList = GT_OreDictUnificator.getOres(tName);
        for (ItemStack itemStack : tList)
            if (GT_Utility.areStacksEqual((ItemStack) itemStack, aStack, true)) return false;
        OreDictionary.registerOre(tName, GT_Utility.copyAmount(1L, new Object[] { aStack }));
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
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aPlate, aWire01 })) {
            RecipeUtils.addShapedRecipe(
                    aPlate,
                    CI.craftingToolWireCutter,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    aWire01);
        }

        // Wire mill
        if (ItemUtils.checkForInvalidItems(
                new ItemStack[] { aIngot, aWire01, aWire02, aWire04, aWire08, aWire12, aWire16 })) {
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getIngot(1),
                    GT_Utility.getIntegratedCircuit(1),
                    aMaterial.getWire01(2),
                    100,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getIngot(1),
                    GT_Utility.getIntegratedCircuit(2),
                    aMaterial.getWire02(1),
                    150,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getIngot(2),
                    GT_Utility.getIntegratedCircuit(4),
                    aMaterial.getWire04(1),
                    200,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getIngot(4),
                    GT_Utility.getIntegratedCircuit(8),
                    aMaterial.getWire08(1),
                    250,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getIngot(6),
                    GT_Utility.getIntegratedCircuit(12),
                    aMaterial.getWire12(1),
                    300,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getIngot(8),
                    GT_Utility.getIntegratedCircuit(16),
                    aMaterial.getWire16(1),
                    350,
                    4);
        }

        if (ItemUtils
                .checkForInvalidItems(new ItemStack[] { aRod, aWire01, aWire02, aWire04, aWire08, aWire12, aWire16 })) {
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getRod(1),
                    GT_Utility.getIntegratedCircuit(1),
                    aMaterial.getWire01(1),
                    50,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getRod(2),
                    GT_Utility.getIntegratedCircuit(2),
                    aMaterial.getWire02(1),
                    100,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getRod(4),
                    GT_Utility.getIntegratedCircuit(4),
                    aMaterial.getWire04(1),
                    150,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getRod(8),
                    GT_Utility.getIntegratedCircuit(8),
                    aMaterial.getWire08(1),
                    200,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getRod(12),
                    GT_Utility.getIntegratedCircuit(12),
                    aMaterial.getWire12(1),
                    250,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getRod(16),
                    GT_Utility.getIntegratedCircuit(16),
                    aMaterial.getWire16(1),
                    300,
                    4);
        }

        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aIngot, aFineWire })) {
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getIngot(1),
                    GT_Utility.getIntegratedCircuit(3),
                    aMaterial.getFineWire(8),
                    100,
                    4);
        }

        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aRod, aFineWire })) {
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getRod(1),
                    GT_Utility.getIntegratedCircuit(3),
                    aMaterial.getFineWire(4),
                    50,
                    4);
        }

        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aFineWire })) {
            GT_Values.RA.addWiremillRecipe(
                    aMaterial.getWire01(1),
                    GT_Utility.getIntegratedCircuit(1),
                    aMaterial.getFineWire(4),
                    200,
                    8);
        }

        // Extruder
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aIngot, aWire01 })) {
            GT_Values.RA
                    .addExtruderRecipe(aIngot, ItemList.Shape_Extruder_Wire.get(0), aMaterial.getWire01(2), 196, 96);
        }

        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aCable01, aWire01 })) {
            GT_Values.RA.addUnboxingRecipe(aCable01, aWire01, null, 100, 8);
        }

        // Shapeless Down-Crafting
        // 2x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire02 })) {
            RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] { aWire02 }, aMaterial.getWire01(2));
        }

        // 4x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire04 })) {
            RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] { aWire04 }, aMaterial.getWire01(4));
        }

        // 8x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire08 })) {
            RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] { aWire08 }, aMaterial.getWire01(8));
        }

        // 12x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire12 })) {
            RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] { aWire12 }, aMaterial.getWire01(12));
        }

        // 16x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire16 })) {
            RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] { aWire16 }, aMaterial.getWire01(16));
        }

        // 1x -> 2x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire02 })) {
            RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] { aWire01, aWire01 }, aWire02);
        }

        // 2x -> 4x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire02, aWire04 })) {
            RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] { aWire02, aWire02 }, aWire04);
        }

        // 4x -> 8x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire04, aWire08 })) {
            RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] { aWire04, aWire04 }, aWire08);
        }

        // 8x -> 12x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire04, aWire08, aWire12 })) {
            RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] { aWire04, aWire08 }, aWire12);
        }

        // 12x -> 16x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire04, aWire12, aWire16 })) {
            RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] { aWire04, aWire12 }, aWire16);
        }

        // 8x -> 16x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire08, aWire16 })) {
            RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] { aWire08, aWire08 }, aWire16);
        }

        // 1x -> 4x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire04 })) {
            RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] { aWire01, aWire01, aWire01, aWire01 }, aWire04);
        }

        // 1x -> 8x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire08 })) {
            RecipeUtils.addShapelessGregtechRecipe(
                    new ItemStack[] { aWire01, aWire01, aWire01, aWire01, aWire01, aWire01, aWire01, aWire01 },
                    aWire08);
        }

        // Wire to Cable
        // 1x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aCable01 })) {
            GT_Values.RA.addAssemblerRecipe(
                    aWire01,
                    CI.getNumberedCircuit(24),
                    FluidUtils.getFluidStack("molten.rubber", 144),
                    aCable01,
                    100,
                    8);
        }

        // 2x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire02, aCable02 })) {
            GT_Values.RA.addAssemblerRecipe(
                    aWire02,
                    CI.getNumberedCircuit(24),
                    FluidUtils.getFluidStack("molten.rubber", 144),
                    aCable02,
                    100,
                    8);
        }

        // 4x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire04, aCable04 })) {
            GT_Values.RA.addAssemblerRecipe(
                    aWire04,
                    CI.getNumberedCircuit(24),
                    FluidUtils.getFluidStack("molten.rubber", 288),
                    aCable04,
                    100,
                    8);
        }

        // 8x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire08, aCable08 })) {
            GT_Values.RA.addAssemblerRecipe(
                    aWire08,
                    CI.getNumberedCircuit(24),
                    FluidUtils.getFluidStack("molten.rubber", 432),
                    aCable08,
                    100,
                    8);
        }

        // 12x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire12, aCable12 })) {
            GT_Values.RA.addAssemblerRecipe(
                    aWire12,
                    CI.getNumberedCircuit(24),
                    FluidUtils.getFluidStack("molten.rubber", 576),
                    aCable12,
                    100,
                    8);
        }

        // 16x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire16, aCable16 })) {
            GT_Values.RA.addAssemblerRecipe(
                    aWire16,
                    CI.getNumberedCircuit(24),
                    FluidUtils.getFluidStack("molten.rubber", 720),
                    aCable16,
                    100,
                    8);
        }

        // Assemble small wires into bigger wires

        // 2x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire02 })) {
            GT_Values.RA.addAssemblerRecipe(aMaterial.getWire01(2), CI.getNumberedCircuit(2), null, aWire02, 100, 8);
        }

        // 4x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire02 })) {
            GT_Values.RA.addAssemblerRecipe(aMaterial.getWire01(4), CI.getNumberedCircuit(4), null, aWire04, 100, 8);
        }

        // 8x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire02 })) {
            GT_Values.RA.addAssemblerRecipe(aMaterial.getWire01(8), CI.getNumberedCircuit(8), null, aWire08, 100, 8);
        }

        // 12x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire02 })) {
            GT_Values.RA.addAssemblerRecipe(aMaterial.getWire01(12), CI.getNumberedCircuit(12), null, aWire12, 100, 8);
        }

        // 16x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire02 })) {
            GT_Values.RA.addAssemblerRecipe(aMaterial.getWire01(16), CI.getNumberedCircuit(16), null, aWire16, 100, 8);
        }

        return true;
    }
}
