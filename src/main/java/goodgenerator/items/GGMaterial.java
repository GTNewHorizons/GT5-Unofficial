package goodgenerator.items;

import static bartworks.util.BWUtil.subscriptNumbers;
import static gregtech.api.enums.Materials.*;

import org.apache.commons.lang3.tuple.Pair;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import goodgenerator.util.CharExchanger;
import gregtech.api.enums.TextureSet;

public class GGMaterial implements Runnable {

    protected static final int OffsetID = 10001;

    // Uranium Based Fuel Line
    public static final Werkstoff graphiteUraniumMixture = new Werkstoff(
        new short[] { 0x3a, 0x77, 0x3d },
        "Graphite-Uranium Mixture",
        subscriptNumbers("C3U"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addMixerRecipes()
            .onlyDust(),
        OffsetID,
        TextureSet.SET_DULL,
        Pair.of(Graphite, 3),
        Pair.of(Uranium, 1));

    public static final Werkstoff uraniumBasedLiquidFuel = new Werkstoff(
        new short[] { 0x00, 0xff, 0x00 },
        "Uranium Based Liquid Fuel",
        subscriptNumbers("U36K8Qt4Rn"),
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 1,
        TextureSet.SET_FLUID);

    public static final Werkstoff uraniumBasedLiquidFuelExcited = new Werkstoff(
        new short[] { 0x00, 0xff, 0x00 },
        "Uranium Based Liquid Fuel (Excited State)",
        subscriptNumbers("*(U36K8Qt4Rn)*"),
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 2,
        TextureSet.SET_FLUID);

    public static final Werkstoff uraniumBasedLiquidFuelDepleted = new Werkstoff(
        new short[] { 0x6e, 0x8b, 0x3d },
        "Uranium Based Liquid Fuel (Depleted)",
        subscriptNumbers("Pb?Bi?Ba?Xe?"),
        new Werkstoff.Stats().setToxic(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 3,
        TextureSet.SET_FLUID);

    // Thorium Based Fuel
    public static final Werkstoff uraniumCarbideThoriumMixture = new Werkstoff(
        new short[] { 0x16, 0x32, 0x07 },
        "Uranium Carbide-Thorium Mixture",
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addMixerRecipes()
            .onlyDust(),
        OffsetID + 4,
        TextureSet.SET_DULL,
        Pair.of(Thorium, 11),
        Pair.of(WerkstoffLoader.Thorium232, 1),
        Pair.of(Uranium235, 1),
        Pair.of(Carbon, 3));

    public static final Werkstoff thoriumBasedLiquidFuel = new Werkstoff(
        new short[] { 0x50, 0x32, 0x66 },
        "Thorium Based Liquid Fuel",
        subscriptNumbers("Th432Li4D2Hg"),
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 5,
        TextureSet.SET_FLUID);

    public static final Werkstoff thoriumBasedLiquidFuelExcited = new Werkstoff(
        new short[] { 0x50, 0x32, 0x66 },
        "Thorium Based Liquid Fuel (Excited State)",
        subscriptNumbers("*(Th432Li4D2Hg)*"),
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 6,
        TextureSet.SET_FLUID);

    public static final Werkstoff thoriumBasedLiquidFuelDepleted = new Werkstoff(
        new short[] { 0x7d, 0x6c, 0x8a },
        "Thorium Based Liquid Fuel (Depleted)",
        subscriptNumbers("Th?Pr?B?In?"),
        new Werkstoff.Stats().setToxic(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 7,
        TextureSet.SET_FLUID);

    // Plutonium Based Fuel
    public static final Werkstoff plutoniumOxideUraniumMixture = new Werkstoff(
        new short[] { 0xd1, 0x1f, 0x4a },
        "Plutonium Oxide-Uranium Mixture",
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addMixerRecipes()
            .onlyDust(),
        OffsetID + 8,
        TextureSet.SET_SHINY,
        Pair.of(Plutonium, 10),
        Pair.of(Oxygen, 12),
        Pair.of(Uranium, 2),
        Pair.of(Carbon, 8));

    public static final Werkstoff plutoniumBasedLiquidFuel = new Werkstoff(
        new short[] { 0xef, 0x15, 0x15 },
        "Plutonium Based Liquid Fuel",
        subscriptNumbers("Pu45Nt8Cs16Nq2"),
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 9,
        TextureSet.SET_FLUID);

    public static final Werkstoff plutoniumBasedLiquidFuelExcited = new Werkstoff(
        new short[] { 0xef, 0x15, 0x15 },
        "Plutonium Based Liquid Fuel (Excited State)",
        subscriptNumbers("*(Pu45Nt8Cs16Nq2)*"),
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 10,
        TextureSet.SET_FLUID);

    public static final Werkstoff plutoniumBasedLiquidFuelDepleted = new Werkstoff(
        new short[] { 0x67, 0x19, 0x19 },
        "Plutonium Based Liquid Fuel (Depleted)",
        subscriptNumbers("Tn?Ce?Au?Kr?"),
        new Werkstoff.Stats().setToxic(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 11,
        TextureSet.SET_FLUID);

    // Thorium-233
    public static final Werkstoff oxalate = new Werkstoff(
        new short[] { 0x79, 0xd8, 0x55 },
        "Oxalate",
        Werkstoff.Types.BIOLOGICAL,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 12,
        TextureSet.SET_FLUID,
        Pair.of(Hydrogen, 2),
        Pair.of(Carbon, 2),
        Pair.of(Oxygen, 4));

    public static final Werkstoff vanadiumPentoxide = new Werkstoff(
        new short[] { 0xde, 0x8d, 0x12 },
        "Vanadium Pentoxide",
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 13,
        TextureSet.SET_SHINY,
        Pair.of(Vanadium, 2),
        Pair.of(Oxygen, 5));

    public static final Werkstoff thoriumNitrate = new Werkstoff(
        new short[] { 0xba, 0xe8, 0x26 },
        "Thorium Nitrate",
        subscriptNumbers("Th(NO3)4"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 14,
        TextureSet.SET_DULL);

    public static final Werkstoff thoriumOxalate = new Werkstoff(
        new short[] { 0x50, 0x63, 0x13 },
        "Thorium Oxalate",
        subscriptNumbers("Th(C2O4)2"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 15,
        TextureSet.SET_DULL);

    public static final Werkstoff thoriumHydroxide = new Werkstoff(
        new short[] { 0x92, 0xae, 0x89 },
        "Thorium Hydroxide",
        subscriptNumbers("Th(OH)4"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 16,
        TextureSet.SET_SHINY);

    public static final Werkstoff sodiumOxalate = new Werkstoff(
        new short[] { 0xe4, 0xf8, 0x9b },
        "Sodium Oxalate",
        subscriptNumbers("Na2C2O4"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 17,
        TextureSet.SET_DULL);

    public static final Werkstoff thoriumTetrachloride = new Werkstoff(
        new short[] { 0x13, 0x7c, 0x16 },
        "Thorium Tetrachloride",
        subscriptNumbers("ThCl4"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 18,
        TextureSet.SET_FLUID);

    @Deprecated // use GT++ ThoriumTetraFluoride
    public static final Werkstoff thoriumTetrafluoride = new Werkstoff(
        new short[] { 0x15, 0x6a, 0x6a },
        "Thorium Tetrafluoride",
        subscriptNumbers("ThF4"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 19,
        TextureSet.SET_FLUID);

    public static final Werkstoff thorium232Tetrafluoride = new Werkstoff(
        new short[] { 0x15, 0x6a, 0x6a },
        "Thorium-232 Tetrafluoride",
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 20,
        TextureSet.SET_FLUID,
        Pair.of(WerkstoffLoader.Thorium232, 1),
        Pair.of(Fluorine, 4));

    // Orundum
    public static final Werkstoff orundum = new Werkstoff(
        new short[] { 0xcd, 0x26, 0x26 },
        "Orundum",
        "Or",
        new Werkstoff.Stats().setProtons(120)
            .setMass(300),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().addGems()
            .addMolten(),
        OffsetID + 22,
        TextureSet.SET_DIAMOND);

    // Atomic Separation Catalyst
    public static final Werkstoff atomicSeparationCatalyst = new Werkstoff(
        new short[] { 0xe8, 0x5e, 0x0c },
        "Atomic Separation Catalyst",
        "The melting core...",
        new Werkstoff.Stats().setMeltingPoint(5000)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addSimpleMetalWorkingItems()
            .addDoubleAndDensePlates()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes(),
        OffsetID + 21,
        TextureSet.SET_SHINY,
        Pair.of(GGMaterial.orundum, 2),
        Pair.of(Plutonium, 1),
        Pair.of(Naquadah, 2));

    // Naquadah Fuel Rework
    public static final Werkstoff extremelyUnstableNaquadah = new Werkstoff(
        new short[] { 0x06, 0x26, 0x05 },
        "Extremely Unstable Naquadah",
        "Nq" + CharExchanger.shifter(9734),
        new Werkstoff.Stats().setMeltingPoint(7000)
            .setBlastFurnace(true)
            .setProtons(200)
            .setMass(450)
            .setRadioactive(true)
            .setDurOverride(180224)
            .setSpeedOverride(100f)
            .setQualityOverride((byte) 11)
            .disableAutoGeneratedBlastFurnaceRecipes()
            .disableAutoGeneratedVacuumFreezerRecipes(),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addSimpleMetalWorkingItems()
            .addDoubleAndDensePlates()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes(),
        OffsetID + 23,
        TextureSet.SET_SHINY);

    public static final Werkstoff lightNaquadahFuel = new Werkstoff(
        new short[] { 92, 203, 92 },
        "Light Naquadah Fuel",
        "Far from enough",
        new Werkstoff.Stats().setToxic(true)
            .setRadioactive(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 24,
        TextureSet.SET_FLUID);

    public static final Werkstoff heavyNaquadahFuel = new Werkstoff(
        new short[] { 54, 255, 54 },
        "Heavy Naquadah Fuel",
        "Still needs processing",
        new Werkstoff.Stats().setToxic(true)
            .setRadioactive(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 25,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahGas = new Werkstoff(
        new short[] { 93, 219, 0 },
        "Naquadah Gas",
        "Who needs it?",
        new Werkstoff.Stats().setToxic(true)
            .setRadioactive(true)
            .setGas(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 26,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahAsphalt = new Werkstoff(
        new short[] { 5, 37, 5 },
        "Naquadah Asphalt",
        "Atomic runoff",
        new Werkstoff.Stats().setToxic(true)
            .setRadioactive(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 27,
        TextureSet.SET_FLUID);

    public static final Werkstoff ether = new Werkstoff(
        new short[] { 0xeb, 0xbc, 0x2f },
        "Ether",
        subscriptNumbers("CH3CH2OCH2CH3"),
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 28,
        TextureSet.SET_FLUID,
        Pair.of(Carbon, 4),
        Pair.of(Hydrogen, 10),
        Pair.of(Oxygen, 1));

    public static final Werkstoff antimonyTrichloride = new Werkstoff(
        new short[] { 0x0f, 0xdc, 0x34 },
        "Antimony Trichloride Solution",
        subscriptNumbers("SbCl3"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 29,
        TextureSet.SET_FLUID);

    public static final Werkstoff antimonyPentachlorideSolution = new Werkstoff(
        new short[] { 0x15, 0x93, 0x2c },
        "Antimony Pentachloride Solution",
        subscriptNumbers("SbCl5"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 30,
        TextureSet.SET_FLUID);

    public static final Werkstoff antimonyPentachloride = new Werkstoff(
        new short[] { 0x15, 0x93, 0x2c },
        "Antimony Pentachloride",
        subscriptNumbers("SbCl5"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 31,
        TextureSet.SET_FLUID);

    public static final Werkstoff antimonyPentafluoride = new Werkstoff(
        new short[] { 0x16, 0xd5, 0xe2 },
        "Antimony Pentafluoride",
        subscriptNumbers("SbF5"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 32,
        TextureSet.SET_FLUID);

    public static final Werkstoff fluoroantimonicAcid = new Werkstoff(
        new short[] { 0x16, 0xd5, 0xe2 },
        "Fluoroantimonic Acid",
        subscriptNumbers("HSbF6"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 33,
        TextureSet.SET_FLUID);

    public static final Werkstoff radioactiveSludge = new Werkstoff(
        new short[] { 0xb3, 0x49, 0x1e },
        "Radioactive Sludge",
        ">>> DANGER <<<",
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 34,
        TextureSet.SET_DULL);

    public static final Werkstoff acidNaquadahEmulsion = new Werkstoff(
        new short[] { 0x25, 0x22, 0x22 },
        "Acid Naquadah Emulsion",
        "??Nq??H" + CharExchanger.shifter(8314),
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 35,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahEmulsion = new Werkstoff(
        new short[] { 0x4a, 0x46, 0x45 },
        "Naquadah Emulsion",
        "??Nq??",
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 36,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahSolution = new Werkstoff(
        new short[] { 0x84, 0x81, 0x80 },
        "Naquadah Solution",
        "~Nq~",
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 37,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkI = new Werkstoff(
        new short[] { 0x62, 0x5c, 0x5b },
        "Naquadah Based Liquid Fuel MkI",
        "THE FIRST STEP",
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 38,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkIDepleted = new Werkstoff(
        new short[] { 0xcb, 0xc3, 0xc1 },
        "Naquadah Based Liquid Fuel MkI (Depleted)",
        "THE FIRST STEP (taken)",
        new Werkstoff.Stats().setToxic(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 39,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkII = new Werkstoff(
        new short[] { 0x52, 0x4e, 0x4d },
        "Naquadah Based Liquid Fuel MkII",
        "SIXTY BILLION KILOWATT-HOURS",
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 40,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkIIDepleted = new Werkstoff(
        new short[] { 0xb5, 0xb0, 0xae },
        "Naquadah Based Liquid Fuel MkII (Depleted)",
        "SIXTY BILLION KILOWATT-HOURS (spent)",
        new Werkstoff.Stats().setToxic(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 41,
        TextureSet.SET_FLUID);
    /* These materials will be enable when they are removed in GregTech */
    /*
     * public static final Werkstoff praseodymium = new Werkstoff( new short[]{0xff,0xff,0xff}, "praseodymium", "Pr",
     * new Werkstoff.Stats(), Werkstoff.Types.ELEMENT, new
     * Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems(), OffsetID + 42,
     * TextureSet.SET_METALLIC ); public static final Werkstoff rubidium = new Werkstoff( new short[]{0xff,0x2a,0x00},
     * "rubidium", "Rb", new Werkstoff.Stats(), Werkstoff.Types.ELEMENT, new
     * Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems(), OffsetID + 43,
     * TextureSet.SET_SHINY ); public static final Werkstoff thulium = new Werkstoff( new short[]{0xff,0xff,0xff},
     * "Thulium", "Tm", new Werkstoff.Stats(), Werkstoff.Types.ELEMENT, new
     * Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems(), OffsetID + 44,
     * TextureSet.SET_METALLIC );
     */
    public static final Werkstoff naquadahBasedFuelMkIII = new Werkstoff(
        new short[] { 0x29, 0x22, 0x21 },
        "Naquadah Based Liquid Fuel MkIII",
        "POWER OVERWHELMING",
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 45,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkIIIDepleted = new Werkstoff(
        new short[] { 0x66, 0x40, 0x38 },
        "Naquadah Based Liquid Fuel MkIII (Depleted)",
        "POWER OVERWHELMING (no longer)",
        new Werkstoff.Stats().setToxic(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 46,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkIV = new Werkstoff(
        new short[] { 0x0e, 0x0c, 0x0c },
        "Naquadah Based Liquid Fuel MkIV",
        "STRIKE DOWN THE SUN",
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 47,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkIVDepleted = new Werkstoff(
        new short[] { 0x8e, 0x34, 0x22 },
        "Naquadah Based Liquid Fuel MkIV (Depleted)",
        "STRIKE DOWN THE SUN (done)",
        new Werkstoff.Stats().setToxic(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 48,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkV = new Werkstoff(
        new short[] { 0x00, 0x00, 0x00 },
        "Naquadah Based Liquid Fuel MkV",
        "THE END",
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 49,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkVDepleted = new Werkstoff(
        new short[] { 0xff, 0xff, 0xff },
        "Naquadah Based Liquid Fuel MkV (Depleted)",
        "THE END (literally)",
        new Werkstoff.Stats().setToxic(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 50,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkVI = new Werkstoff(
        new short[] { 0x30, 0x00, 0x00 },
        "Naquadah Based Liquid Fuel MkVI",
        "THE FUTURE",
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 115,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkVIDepleted = new Werkstoff(
        new short[] { 0x99, 0x33, 0x33 },
        "Naquadah Based Liquid Fuel MkVI (Depleted)",
        "THE FUTURE (achieved)",
        new Werkstoff.Stats().setToxic(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 116,
        TextureSet.SET_FLUID);

    public static final Werkstoff zincChloride = new Werkstoff(
        new short[] { 0x73, 0xa5, 0xfc },
        "Zinc Chloride",
        subscriptNumbers("ZnCl2"),
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 51,
        TextureSet.SET_SHINY,
        Pair.of(Zinc, 1),
        Pair.of(Chlorine, 2));

    public static final Werkstoff zincThoriumAlloy = new Werkstoff(
        new short[] { 0x12, 0x34, 0x56 },
        "Zn-Th Alloy",
        subscriptNumbers("ZnTh"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten()
            .addMetalItems()
            .addMetalCraftingSolidifierRecipes(),
        OffsetID + 52,
        TextureSet.SET_SHINY,
        Pair.of(Zinc, 1),
        Pair.of(Thorium, 1));

    // Naquadah Rework Materials
    public static final Werkstoff naquadahEarth = new Werkstoff(
        new short[] { 0x4c, 0x4c, 0x4c },
        "Naquadah Oxide Mixture",
        subscriptNumbers("Nq?Ti?Ga?Ad?"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        OffsetID + 53,
        TextureSet.SET_METALLIC);

    public static final Werkstoff titaniumTrifluoride = new Werkstoff(
        new short[] { 0xc0, 0x92, 0xa8 },
        "Titanium Trifluoride",
        subscriptNumbers("TiF3"),
        new Werkstoff.Stats().setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 54,
        TextureSet.SET_METALLIC,
        Pair.of(Titanium, 1),
        Pair.of(Fluorine, 3));

    public static final Werkstoff lowQualityNaquadahEmulsion = new Werkstoff(
        new short[] { 0x4c, 0x4c, 0x4c },
        "Low Quality Naquadah Emulsion",
        subscriptNumbers("Nq?Ga?Ad?"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 55,
        TextureSet.SET_FLUID);

    public static final Werkstoff galliumHydroxide = new Werkstoff(
        new short[] { 0xa6, 0xa6, 0xa6 },
        "Gallium Hydroxide",
        subscriptNumbers("Ga(OH)3"),
        new Werkstoff.Stats().setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 56,
        TextureSet.SET_DULL,
        Pair.of(Gallium, 1),
        Pair.of(Oxygen, 3),
        Pair.of(Hydrogen, 3));

    public static final Werkstoff lowQualityNaquadahSolution = new Werkstoff(
        new short[] { 0x71, 0x62, 0x62 },
        "Low Quality Naquadah Solution",
        subscriptNumbers("~Nq?Ad?~"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 57,
        TextureSet.SET_FLUID);

    public static final Werkstoff towEthyl1Hexanol = new Werkstoff(
        new short[] { 0x80, 0xb5, 0x57 },
        "2-Ethyl-1-Hexanol",
        subscriptNumbers("C8H18O"),
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 58,
        TextureSet.SET_FLUID,
        Pair.of(Carbon, 8),
        Pair.of(Oxygen, 1),
        Pair.of(Hydrogen, 18));

    public static final Werkstoff P507 = new Werkstoff(
        new short[] { 0x29, 0xc2, 0x2a },
        "P-507",
        subscriptNumbers("(C8H17)2PO3H"),
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 59,
        TextureSet.SET_FLUID,
        Pair.of(Carbon, 16),
        Pair.of(Phosphorus, 1),
        Pair.of(Oxygen, 3),
        Pair.of(Hydrogen, 35));

    public static final Werkstoff naquadahAdamantiumSolution = new Werkstoff(
        new short[] { 0x3d, 0x38, 0x38 },
        "Naquadah-Adamantium Solution",
        subscriptNumbers("~NqAd~"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 60,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahRichSolution = new Werkstoff(
        new short[] { 0x33, 0x33, 0x33 },
        "Naquadah-Rich Solution",
        subscriptNumbers("~Nq?~"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 61,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadahine = new Werkstoff(
        new short[] { 0x33, 0x33, 0x33 },
        "Naquadahine",
        subscriptNumbers("NqO2"),
        new Werkstoff.Stats().setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 62,
        TextureSet.SET_METALLIC,
        Pair.of(Naquadah, 1),
        Pair.of(Oxygen, 2));

    public static final Werkstoff fluorineRichWasteLiquid = new Werkstoff(
        new short[] { 0x13, 0x68, 0x62 },
        "Fluorine-Rich Waste Liquid",
        "??F??",
        new Werkstoff.Stats().setToxic(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 63,
        TextureSet.SET_FLUID);

    public static final Werkstoff wasteLiquid = new Werkstoff(
        new short[] { 0x14, 0x1c, 0x68 },
        "Waste Liquid",
        "????",
        new Werkstoff.Stats().setToxic(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 64,
        TextureSet.SET_FLUID);

    public static final Werkstoff adamantine = new Werkstoff(
        new short[] { 0xb7, 0xb7, 0xb7 },
        "Adamantine",
        subscriptNumbers("Ad2O3"),
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 65,
        TextureSet.SET_DULL,
        Pair.of(Adamantium, 2),
        Pair.of(Oxygen, 3));

    public static final Werkstoff enrichedNaquadahEarth = new Werkstoff(
        new short[] { 0x82, 0x68, 0x68 },
        "Enriched-Naquadah Oxide Mixture",
        subscriptNumbers("Ke?Nq") + CharExchanger.shifter(8314) + subscriptNumbers("?"),
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        OffsetID + 66,
        TextureSet.SET_METALLIC);

    public static final Werkstoff triniumSulphate = new Werkstoff(
        new short[] { 0xda, 0xda, 0xda },
        "Trinium Sulphate",
        subscriptNumbers("KeSO4"),
        new Werkstoff.Stats().setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 67,
        TextureSet.SET_METALLIC,
        Pair.of(Trinium, 1),
        Pair.of(Sulfur, 1),
        Pair.of(Oxygen, 4));

    public static final Werkstoff enrichedNaquadahRichSolution = new Werkstoff(
        new short[] { 0x52, 0x39, 0x39 },
        "Enriched-Naquadah-Rich Solution",
        subscriptNumbers("~Nq") + CharExchanger.shifter(8314) + subscriptNumbers("?~"),
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 68,
        TextureSet.SET_FLUID);

    public static final Werkstoff concentratedEnrichedNaquadahSludge = new Werkstoff(
        new short[] { 0x52, 0x39, 0x39 },
        "Concentrated Enriched-Naquadah Sludge",
        subscriptNumbers("Nq") + CharExchanger.shifter(8314) + subscriptNumbers("?"),
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 69,
        TextureSet.SET_METALLIC);

    public static final Werkstoff enrichedNaquadahSulphate = new Werkstoff(
        new short[] { 0x52, 0x39, 0x39 },
        "Enriched-Naquadah Sulphate",
        "Nq" + CharExchanger.shifter(8314) + subscriptNumbers("(SO4)2"),
        new Werkstoff.Stats().setRadioactive(true)
            .setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 70,
        TextureSet.SET_DULL,
        Pair.of(NaquadahEnriched, 1),
        Pair.of(Sulfur, 2),
        Pair.of(Oxygen, 8));

    public static final Werkstoff naquadriaEarth = new Werkstoff(
        new short[] { 0x4d, 0x4d, 0x55 },
        "Naquadria Oxide Mixture",
        subscriptNumbers("Nq*?Ba?In?"),
        new Werkstoff.Stats().setRadioactive(true)
            .setToxic(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures(),
        OffsetID + 71,
        TextureSet.SET_METALLIC);

    public static final Werkstoff indiumPhosphate = new Werkstoff(
        new short[] { 0x2b, 0x2e, 0x70 },
        "Indium Phosphate",
        subscriptNumbers("InPO4"),
        new Werkstoff.Stats().setToxic(true)
            .setElektrolysis(false),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 72,
        TextureSet.SET_DULL,
        Pair.of(Indium, 1),
        Pair.of(Phosphorus, 1),
        Pair.of(Oxygen, 4));

    public static final Werkstoff lowQualityNaquadriaPhosphate = new Werkstoff(
        new short[] { 0x4d, 0x4d, 0x55 },
        "Low Quality Naquadria Phosphate",
        "??" + subscriptNumbers("Nq*3(PO4)4") + "??",
        new Werkstoff.Stats().setRadioactive(true)
            .setToxic(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 73,
        TextureSet.SET_DULL);

    public static final Werkstoff naquadriaRichSolution = new Werkstoff(
        new short[] { 0x1f, 0x1e, 0x33 },
        "Naquadria-Rich Solution",
        subscriptNumbers("~Nq*?~"),
        new Werkstoff.Stats().setRadioactive(true)
            .setToxic(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 74,
        TextureSet.SET_FLUID);

    public static final Werkstoff lowQualityNaquadriaSulphate = new Werkstoff(
        new short[] { 0x73, 0x72, 0x84 },
        "Low Quality Naquadria Sulphate",
        "??" + subscriptNumbers("Nq*(SO4)2") + "??",
        new Werkstoff.Stats().setRadioactive(true)
            .setToxic(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 75,
        TextureSet.SET_METALLIC);

    public static final Werkstoff lowQualityNaquadriaSolution = new Werkstoff(
        new short[] { 0x73, 0x72, 0x84 },
        "Low Quality Naquadria Sulphate",
        subscriptNumbers("~Nq*?~"),
        new Werkstoff.Stats().setRadioactive(true)
            .setToxic(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 76,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadriaSulphate = new Werkstoff(
        new short[] { 0x1f, 0x1e, 0x33 },
        "Naquadria Sulphate",
        subscriptNumbers("Nq*(SO4)2"),
        new Werkstoff.Stats().setRadioactive(true)
            .setToxic(true)
            .setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 77,
        TextureSet.SET_METALLIC,
        Pair.of(Naquadria, 1),
        Pair.of(Sulfur, 2),
        Pair.of(Oxygen, 8));

    public static final Werkstoff naquadahGoo = new Werkstoff(
        new short[] { 0x4c, 0x4c, 0x4c },
        "Naquadah Goo",
        subscriptNumbers("Nq?Ti?Ga?Ad?"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 78,
        TextureSet.SET_FLUID);

    public static final Werkstoff enrichedNaquadahGoo = new Werkstoff(
        new short[] { 0x82, 0x68, 0x68 },
        "Enriched Naquadah Goo",
        subscriptNumbers("Ke?Nq") + CharExchanger.shifter(8314) + subscriptNumbers("?"),
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 79,
        TextureSet.SET_FLUID);

    public static final Werkstoff naquadriaGoo = new Werkstoff(
        new short[] { 0x4d, 0x4d, 0x55 },
        "Naquadria Goo",
        subscriptNumbers("Nq*?Ba?In?"),
        new Werkstoff.Stats().setRadioactive(true)
            .setToxic(true),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 80,
        TextureSet.SET_FLUID);

    // material for reactor stuff
    public static final Werkstoff zircaloy4 = new Werkstoff(
        new short[] { 0x8a, 0x6e, 0x68 },
        "Zircaloy-4",
        subscriptNumbers("Zr34Sn5Fe2Cr"),
        new Werkstoff.Stats().setCentrifuge(true)
            .setBlastFurnace(true)
            .setMeltingPoint(2800)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .addMixerRecipes((short) 4),
        OffsetID + 81,
        TextureSet.SET_METALLIC,
        Pair.of(WerkstoffLoader.Zirconium, 34),
        Pair.of(Tin, 5),
        Pair.of(Iron, 2),
        Pair.of(Chrome, 1));

    public static final Werkstoff zircaloy2 = new Werkstoff(
        new short[] { 0xa4, 0x8f, 0x8b },
        "Zircaloy-2",
        subscriptNumbers("Zr34Sn4FeCrNi"),
        new Werkstoff.Stats().setCentrifuge(true)
            .setBlastFurnace(true)
            .setMeltingPoint(2800)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .addMixerRecipes((short) 2),
        OffsetID + 82,
        TextureSet.SET_METALLIC,
        Pair.of(WerkstoffLoader.Zirconium, 34),
        Pair.of(Tin, 4),
        Pair.of(Iron, 1),
        Pair.of(Chrome, 1),
        Pair.of(Nickel, 1));

    public static final Werkstoff incoloy903 = new Werkstoff(
        new short[] { 0xa4, 0x8f, 0x8b },
        "Incoloy-903",
        subscriptNumbers("Fe12Ni10Co8Ti4Mo2Al"),
        new Werkstoff.Stats().setCentrifuge(true)
            .setBlastFurnace(true)
            .setMeltingPoint(3700)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addSimpleMetalWorkingItems()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .addMixerRecipes((short) 6),
        OffsetID + 83,
        TextureSet.SET_METALLIC,
        Pair.of(Iron, 12),
        Pair.of(Nickel, 10),
        Pair.of(Cobalt, 8),
        Pair.of(Titanium, 4),
        Pair.of(Molybdenum, 2),
        Pair.of(Aluminium, 1));

    public static final Werkstoff adamantiumAlloy = new Werkstoff(
        new short[] { 0xa0, 0xa0, 0xa0 },
        "Adamantium Alloy",
        subscriptNumbers("Ad5Nq2La3"),
        new Werkstoff.Stats().setCentrifuge(true)
            .setBlastFurnace(true)
            .setMeltingPoint(5500)
            .setSpeedOverride(191.2F)
            .setDurOverride(102400)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addSimpleMetalWorkingItems()
            .addDoubleAndDensePlates()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .addMixerRecipes((short) 3),
        OffsetID + 84,
        TextureSet.SET_SHINY,
        Pair.of(Adamantium, 5),
        Pair.of(Naquadah, 2),
        Pair.of(Lanthanum, 3));

    public static final Werkstoff ethanolGasoline = new Werkstoff(
        new short[] { 0xe4, 0xc6, 0x61 },
        "Ethanol Gasoline",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 85,
        TextureSet.SET_FLUID);

    public static final Werkstoff cyclopentadiene = new Werkstoff(
        new short[] { 0xff, 0xf6, 0xbd },
        "Cyclopentadiene",
        subscriptNumbers("C5H6"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 86,
        TextureSet.SET_FLUID);

    public static final Werkstoff ferrousChloride = new Werkstoff(
        new short[] { 0x5b, 0x5b, 0x5b },
        "Iron II Chloride",
        subscriptNumbers("FeCl2"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 87,
        TextureSet.SET_FLUID);

    public static final Werkstoff diethylamine = new Werkstoff(
        new short[] { 0x69, 0x77, 0xca },
        "Diethylamine",
        subscriptNumbers("C4H11N"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 88,
        TextureSet.SET_FLUID);

    public static final Werkstoff impureFerroceneMixture = new Werkstoff(
        new short[] { 0x79, 0x55, 0x08 },
        "Impure Ferrocene Mixture",
        "??Fe??C??H??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 89,
        TextureSet.SET_FLUID);

    public static final Werkstoff ferroceneSolution = new Werkstoff(
        new short[] { 0xde, 0x7e, 0x1c },
        "Ferrocene Solution",
        subscriptNumbers("Fe(C5H5)2??"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 90,
        TextureSet.SET_FLUID);

    public static final Werkstoff ferroceneWaste = new Werkstoff(
        new short[] { 0x35, 0x1d, 0x03 },
        "Ferrocene Waste",
        "??C??H??",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 91,
        TextureSet.SET_FLUID);

    public static final Werkstoff ferrocene = new Werkstoff(
        new short[] { 0xf1, 0x8f, 0x2b },
        "Ferrocene",
        subscriptNumbers("Fe(C5H5)2"),
        new Werkstoff.Stats(),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 92,
        TextureSet.SET_SHINY);

    public static final Werkstoff ironedKerosene = new Werkstoff(
        new short[] { 0x97, 0x00, 0x61 },
        "Jet Fuel No.3",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 93,
        TextureSet.SET_FLUID);

    public static final Werkstoff ironedFuel = new Werkstoff(
        new short[] { 0xff, 0x98, 0x00 },
        "Jet Fuel A",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 94,
        TextureSet.SET_FLUID);

    public static final Werkstoff marM200 = new Werkstoff(
        new short[] { 0x51, 0x51, 0x51 },
        "MAR-M200 Steel",
        new Werkstoff.Stats().setCentrifuge(true)
            .setBlastFurnace(true)
            .setMeltingPoint(5000)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addSimpleMetalWorkingItems()
            .addDoubleAndDensePlates()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .addMixerRecipes((short) 7),
        OffsetID + 95,
        TextureSet.SET_SHINY,
        Pair.of(Niobium, 2),
        Pair.of(Chrome, 9),
        Pair.of(Aluminium, 5),
        Pair.of(Titanium, 2),
        Pair.of(Cobalt, 10),
        Pair.of(Tungsten, 13),
        Pair.of(Nickel, 18));

    public static final Werkstoff marCeM200 = new Werkstoff(
        new short[] { 0x38, 0x30, 0x30 },
        "MAR-Ce-M200 Steel",
        new Werkstoff.Stats().setCentrifuge(true)
            .setBlastFurnace(true)
            .setMeltingPoint(5000)
            .setMass(1200)
            .setProtons(1000)
            .setSpeedOverride(150F)
            .setDurOverride(204800),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addSimpleMetalWorkingItems()
            .addDoubleAndDensePlates()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes(),
        OffsetID + 96,
        TextureSet.SET_METALLIC,
        Pair.of(marM200, 18),
        Pair.of(Cerium, 1));

    public static final Werkstoff lithiumChloride = new Werkstoff(
        new short[] { 0xb7, 0xe2, 0xce },
        "Lithium Chloride",
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .enforceUnification(),
        OffsetID + 97,
        TextureSet.SET_DULL,
        Pair.of(Lithium, 1),
        Pair.of(Chlorine, 1));

    public static final Werkstoff signalium = new Werkstoff(
        new short[] { 0xd4, 0x40, 0x00 },
        "Signalium",
        new Werkstoff.Stats().setBlastFurnace(true)
            .setMeltingPoint(4000)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes(),
        OffsetID + 98,
        TextureSet.SET_SHINY,
        Pair.of(AnnealedCopper, 4),
        Pair.of(Ardite, 2),
        Pair.of(RedAlloy, 2));

    public static final Werkstoff lumiinessence = new Werkstoff(
        new short[] { 0xe8, 0xf2, 0x24 },
        "Lumiinessence",
        "(Al??)" + subscriptNumbers("2(PO4)4"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust(),
        OffsetID + 99,
        TextureSet.SET_DULL);

    public static final Werkstoff lumiium = new Werkstoff(
        new short[] { 0xe8, 0xf2, 0x24 },
        "Lumiium",
        new Werkstoff.Stats().setBlastFurnace(true)
            .setMeltingPoint(4000)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes(),
        OffsetID + 100,
        TextureSet.SET_SHINY,
        Pair.of(TinAlloy, 4),
        Pair.of(SterlingSilver, 2),
        Pair.of(lumiinessence, 2));

    public static final Werkstoff artheriumSn = new Werkstoff(
        new short[] { 0x60, 0x36, 0xf7 },
        "Artherium-Sn",
        new Werkstoff.Stats().setBlastFurnace(true)
            .setMeltingPoint(6500)
            .setCentrifuge(true)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .addMixerRecipes((short) 6),
        OffsetID + 101,
        TextureSet.SET_SHINY,
        Pair.of(adamantiumAlloy, 12),
        Pair.of(orundum, 9),
        Pair.of(Tin, 8),
        Pair.of(Arsenic, 7),
        Pair.of(Caesium, 4),
        Pair.of(Osmiridium, 3));

    public static final Werkstoff titaniumBetaC = new Werkstoff(
        new short[] { 0xc7, 0x2f, 0xcc },
        "Tanmolyium Beta-C",
        new Werkstoff.Stats().setBlastFurnace(true)
            .setMeltingPoint(5300)
            .setCentrifuge(true)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .addMixerRecipes((short) 5),
        OffsetID + 102,
        TextureSet.SET_METALLIC,
        Pair.of(Titanium, 5),
        Pair.of(Molybdenum, 5),
        Pair.of(Vanadium, 2),
        Pair.of(Chrome, 3),
        Pair.of(Aluminium, 1));

    public static final Werkstoff dalisenite = new Werkstoff(
        new short[] { 0xb0, 0xb8, 0x12 },
        "Dalisenite",
        new Werkstoff.Stats().setMeltingPoint(8700)
            .setCentrifuge(true)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .addMixerRecipes((short) 6),
        OffsetID + 103,
        TextureSet.SET_SHINY,
        Pair.of(titaniumBetaC, 14),
        Pair.of(Tungsten, 10),
        Pair.of(NiobiumTitanium, 9),
        Pair.of(WerkstoffLoader.LuVTierMaterial, 8),
        Pair.of(Quantium, 7),
        Pair.of(Erbium, 3));

    public static final Werkstoff hikarium = new Werkstoff(
        new short[] { 0xff, 0xd6, 0xfb },
        "Hikarium",
        new Werkstoff.Stats().setBlastFurnace(true)
            .setMeltingPoint(5400)
            .setCentrifuge(true)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .addMixerRecipes((short) 3),
        OffsetID + 104,
        TextureSet.SET_SHINY,
        Pair.of(lumiium, 18),
        Pair.of(Silver, 8),
        Pair.of(Sunnarium, 4));

    public static final Werkstoff tairitsu = new Werkstoff(
        new short[] { 0x36, 0x36, 0x36 },
        "Tairitsu",
        new Werkstoff.Stats().setBlastFurnace(true)
            .setMeltingPoint(7400)
            .setCentrifuge(true)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .addMixerRecipes((short) 6),
        OffsetID + 105,
        TextureSet.SET_SHINY,
        Pair.of(Tungsten, 8),
        Pair.of(Naquadria, 7),
        Pair.of(Bedrockium, 4),
        Pair.of(Carbon, 4),
        Pair.of(Vanadium, 3),
        Pair.of(BlackPlutonium, 1));

    public static final Werkstoff antimonyPentafluorideSolution = new Werkstoff(
        new short[] { 0x16, 0xd5, 0xe2 },
        "Antimony Pentafluoride Solution",
        subscriptNumbers("SbF5"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 106,
        TextureSet.SET_FLUID);

    public static final Werkstoff magnesiumSulphate = new Werkstoff(
        new short[] { 0x87, 0x74, 0x91 },
        "Magnesium Sulphate",
        subscriptNumbers("MgSO4"),
        new Werkstoff.Stats().setElektrolysis(true),
        Werkstoff.Types.COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 107,
        TextureSet.SET_DULL,
        Pair.of(Magnesium, 1),
        Pair.of(Sulfur, 1),
        Pair.of(Oxygen, 4));

    public static final Werkstoff preciousMetalAlloy = new Werkstoff(
        new short[] { 0x9d, 0x90, 0xc6 },
        "Precious Metals Alloy",
        new Werkstoff.Stats().setBlastFurnace(true)
            .setMeltingPoint(10000)
            .setCentrifuge(true)
            .setSpeedOverride(100F)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addDoubleAndDensePlates()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .addMixerRecipes((short) 6),
        OffsetID + 108,
        TextureSet.SET_SHINY,
        Pair.of(WerkstoffLoader.Ruthenium, 1),
        Pair.of(WerkstoffLoader.Rhodium, 1),
        Pair.of(Palladium, 1),
        Pair.of(Platinum, 1),
        Pair.of(Osmium, 1),
        Pair.of(Iridium, 1));

    public static final Werkstoff enrichedNaquadahAlloy = new Werkstoff(
        new short[] { 0x16, 0x07, 0x40 },
        "Enriched Naquadah Alloy",
        new Werkstoff.Stats().setBlastFurnace(true)
            .setMeltingPoint(11000)
            .setCentrifuge(true)
            .setSpeedOverride(180F)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addSimpleMetalWorkingItems()
            .addDoubleAndDensePlates()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes()
            .addMixerRecipes((short) 4),
        OffsetID + 109,
        TextureSet.SET_METALLIC,
        Pair.of(NaquadahEnriched, 8),
        Pair.of(Tritanium, 5),
        Pair.of(WerkstoffLoader.Californium, 3),
        Pair.of(BlackPlutonium, 2));

    public static final Werkstoff metastableOganesson = new Werkstoff(
        new short[] { 0x14, 0x39, 0x7f },
        "Metastable Oganesson",
        "Og*",
        new Werkstoff.Stats().setBlastFurnace(true)
            .setProtons(118)
            .setMass(294)
            .setMeltingPoint(11000)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addSimpleMetalWorkingItems()
            .addDoubleAndDensePlates()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes(),
        OffsetID + 110,
        TextureSet.SET_SHINY);

    public static final Werkstoff shirabon = new Werkstoff(
        new short[] { 0xe0, 0x15, 0x6d },
        "Shirabon",
        "Sh" + CharExchanger.shifter(9191),
        new Werkstoff.Stats().setProtons(500)
            .setMass(750)
            .setMeltingPoint(13000)
            .setSpeedOverride(640.0F)
            .setDurOverride(15728640)
            .setQualityOverride((byte) 26)
            .disableAutoGeneratedBlastFurnaceRecipes(),
        Werkstoff.Types.ELEMENT,
        new Werkstoff.GenerationFeatures().onlyDust()
            .addMolten()
            .addMetalItems()
            .addCraftingMetalWorkingItems()
            .addSimpleMetalWorkingItems()
            .addDoubleAndDensePlates()
            .addMultiPlates()
            .addMetaSolidifierRecipes()
            .addMetalCraftingSolidifierRecipes(),
        OffsetID + 111,
        TextureSet.SET_SHINY);

    public static final Werkstoff inertNaquadah = new Werkstoff(
        new short[] { 0x3b, 0x3b, 0x3b },
        "Inert Naquadah",
        new Werkstoff.Stats(),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 112,
        TextureSet.SET_METALLIC,
        Pair.of(Naquadah, 1));

    public static final Werkstoff inertEnrichedNaquadah = new Werkstoff(
        new short[] { 0x61, 0x44, 0x44 },
        "Inert Enriched Naquadah",
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 113,
        TextureSet.SET_METALLIC,
        Pair.of(NaquadahEnriched, 1));

    public static final Werkstoff inertNaquadria = new Werkstoff(
        new short[] { 0x00, 0x00, 0x00 },
        "Inert Naquadria",
        new Werkstoff.Stats().setRadioactive(true),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 114,
        TextureSet.SET_METALLIC,
        Pair.of(Naquadria, 1));

    @Override
    public void run() {}
}
