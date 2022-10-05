package goodgenerator.items;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;
import static gregtech.api.enums.Materials.*;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.Pair;
import goodgenerator.util.CharExchanger;
import gregtech.api.enums.TextureSet;

@SuppressWarnings({"unchecked"})
public class MyMaterial implements Runnable {

    protected static final int OffsetID = 10001;

    // Uranium Based Fuel Line
    public static final Werkstoff graphiteUraniumMixture = new Werkstoff(
            new short[] {0x3a, 0x77, 0x3d},
            "Graphite-Uranium Mixture",
            subscriptNumbers("C3U"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addMixerRecipes().onlyDust(),
            OffsetID,
            TextureSet.SET_DULL,
            new Pair<>(Graphite, 3),
            new Pair<>(Uranium, 1));

    public static final Werkstoff uraniumBasedLiquidFuel = new Werkstoff(
            new short[] {0x00, 0xff, 0x00},
            "Uranium Based Liquid Fuel",
            subscriptNumbers("U36K8Qt4Rn"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 1,
            TextureSet.SET_FLUID);

    public static final Werkstoff uraniumBasedLiquidFuelExcited = new Werkstoff(
            new short[] {0x00, 0xff, 0x00},
            "Uranium Based Liquid Fuel (Excited State)",
            subscriptNumbers("*(U36K8Qt4Rn)*"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 2,
            TextureSet.SET_FLUID);

    public static final Werkstoff uraniumBasedLiquidFuelDepleted = new Werkstoff(
            new short[] {0x6e, 0x8b, 0x3d},
            "Uranium Based Liquid Fuel (Depleted)",
            subscriptNumbers("Pb?Bi?Ba?Xe?"),
            new Werkstoff.Stats().setToxic(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 3,
            TextureSet.SET_FLUID);

    // Thorium Based Fuel
    public static final Werkstoff uraniumCarbideThoriumMixture = new Werkstoff(
            new short[] {0x16, 0x32, 0x07},
            "Uranium Carbide-Thorium Mixture",
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addMixerRecipes().onlyDust(),
            OffsetID + 4,
            TextureSet.SET_DULL,
            new Pair<>(Thorium, 8),
            new Pair<>(WerkstoffLoader.Thorium232, 4),
            new Pair<>(Uranium235, 1),
            new Pair<>(Carbon, 3));

    public static final Werkstoff thoriumBasedLiquidFuel = new Werkstoff(
            new short[] {0x50, 0x32, 0x66},
            "Thorium Based Liquid Fuel",
            subscriptNumbers("Th432Li4D2Hg"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 5,
            TextureSet.SET_FLUID);

    public static final Werkstoff thoriumBasedLiquidFuelExcited = new Werkstoff(
            new short[] {0x50, 0x32, 0x66},
            "Thorium Based Liquid Fuel (Excited State)",
            subscriptNumbers("*(Th432Li4D2Hg)*"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 6,
            TextureSet.SET_FLUID);

    public static final Werkstoff thoriumBasedLiquidFuelDepleted = new Werkstoff(
            new short[] {0x7d, 0x6c, 0x8a},
            "Thorium Based Liquid Fuel (Depleted)",
            subscriptNumbers("Lu?Pr?B?In?"),
            new Werkstoff.Stats().setToxic(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 7,
            TextureSet.SET_FLUID);

    // Plutonium Based Fuel
    public static final Werkstoff plutoniumOxideUraniumMixture = new Werkstoff(
            new short[] {0xd1, 0x1f, 0x4a},
            "Plutonium Oxide-Uranium Mixture",
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addMixerRecipes().onlyDust(),
            OffsetID + 8,
            TextureSet.SET_SHINY,
            new Pair<>(Plutonium, 10),
            new Pair<>(Oxygen, 12),
            new Pair<>(Uranium, 2),
            new Pair<>(Carbon, 8));

    public static final Werkstoff plutoniumBasedLiquidFuel = new Werkstoff(
            new short[] {0xef, 0x15, 0x15},
            "Plutonium Based Liquid Fuel",
            subscriptNumbers("Pu45Nt8Cs16Nq2"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 9,
            TextureSet.SET_FLUID);

    public static final Werkstoff plutoniumBasedLiquidFuelExcited = new Werkstoff(
            new short[] {0xef, 0x15, 0x15},
            "Plutonium Based Liquid Fuel (Excited State)",
            subscriptNumbers("*(Pu45Nt8Cs16Nq2)*"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 10,
            TextureSet.SET_FLUID);

    public static final Werkstoff plutoniumBasedLiquidFuelDepleted = new Werkstoff(
            new short[] {0x67, 0x19, 0x19},
            "Plutonium Based Liquid Fuel (Depleted)",
            subscriptNumbers("Tn?Ce?Au?Kr?"),
            new Werkstoff.Stats().setToxic(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 11,
            TextureSet.SET_FLUID);

    // Thorium-233
    public static final Werkstoff oxalate = new Werkstoff(
            new short[] {0x79, 0xd8, 0x55},
            "Oxalate",
            Werkstoff.Types.BIOLOGICAL,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 12,
            TextureSet.SET_FLUID,
            new Pair<>(Hydrogen, 2),
            new Pair<>(Carbon, 2),
            new Pair<>(Oxygen, 4));

    public static final Werkstoff vanadiumPentoxide = new Werkstoff(
            new short[] {0xde, 0x8d, 0x12},
            "Vanadium Pentoxide",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 13,
            TextureSet.SET_SHINY,
            new Pair<>(Vanadium, 2),
            new Pair<>(Oxygen, 5));

    public static final Werkstoff thoriumNitrate = new Werkstoff(
            new short[] {0xba, 0xe8, 0x26},
            "Thorium Nitrate",
            subscriptNumbers("Th(NO3)4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 14,
            TextureSet.SET_DULL);

    public static final Werkstoff thoriumOxalate = new Werkstoff(
            new short[] {0x50, 0x63, 0x13},
            "Thorium Oxalate",
            subscriptNumbers("Th(C2O4)2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 15,
            TextureSet.SET_DULL);

    public static final Werkstoff thoriumHydroxide = new Werkstoff(
            new short[] {0x92, 0xae, 0x89},
            "Thorium Hydroxide",
            subscriptNumbers("Th(OH)4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 16,
            TextureSet.SET_SHINY);

    public static final Werkstoff sodiumOxalate = new Werkstoff(
            new short[] {0xe4, 0xf8, 0x9b},
            "Sodium Oxalate",
            subscriptNumbers("Na2C2O4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 17,
            TextureSet.SET_DULL);

    public static final Werkstoff thoriumTetrachloride = new Werkstoff(
            new short[] {0x13, 0x7c, 0x16},
            "Thorium Tetrachloride",
            subscriptNumbers("ThCl4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 18,
            TextureSet.SET_FLUID);

    public static final Werkstoff thoriumTetrafluoride = new Werkstoff(
            new short[] {0x15, 0x6a, 0x6a},
            "Thorium Tetrafluoride",
            subscriptNumbers("ThF4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 19,
            TextureSet.SET_FLUID);

    public static final Werkstoff thorium232Tetrafluoride = new Werkstoff(
            new short[] {0x15, 0x6a, 0x6a},
            "Thorium-232 Tetrafluoride",
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 20,
            TextureSet.SET_FLUID,
            new Pair<>(WerkstoffLoader.Thorium232, 1),
            new Pair<>(Fluorine, 4));

    // Atomic Separation Catalyst
    public static final Werkstoff orundum = new Werkstoff(
            new short[] {0xcd, 0x26, 0x26},
            "Orundum",
            "Or",
            new Werkstoff.Stats().setProtons(120).setMass(300),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().addGems().addMolten(),
            OffsetID + 22,
            TextureSet.SET_DIAMOND);

    public static final Werkstoff atomicSeparationCatalyst = new Werkstoff(
            new short[] {0xe8, 0x5e, 0x0c},
            "Atomic Separation Catalyst",
            "the melting core...",
            new Werkstoff.Stats().setMeltingPoint(5000).setBlastFurnace(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures()
                    .disable()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addSimpleMetalWorkingItems()
                    .addCraftingMetalWorkingItems()
                    .addMultipleIngotMetalWorkingItems(),
            OffsetID + 21,
            TextureSet.SET_SHINY,
            new Pair<>(MyMaterial.orundum, 2),
            new Pair<>(Plutonium, 1),
            new Pair<>(Naquadah, 2));

    // Naquadah Fuel Rework
    public static final Werkstoff extremelyUnstableNaquadah = new Werkstoff(
            new short[] {0x06, 0x26, 0x05},
            "Extremely Unstable Naquadah",
            "Nq" + CharExchanger.shifter(9734),
            new Werkstoff.Stats()
                    .setMeltingPoint(7000)
                    .setBlastFurnace(true)
                    .setProtons(200)
                    .setMass(450)
                    .setRadioactive(true),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures()
                    .disable()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addSimpleMetalWorkingItems()
                    .addCraftingMetalWorkingItems()
                    .addMultipleIngotMetalWorkingItems(),
            OffsetID + 23,
            TextureSet.SET_SHINY);

    public static final Werkstoff lightNaquadahFuel = new Werkstoff(
            new short[] {92, 203, 92},
            "Light Naquadah Fuel",
            "far from enough",
            new Werkstoff.Stats().setToxic(true).setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 24,
            TextureSet.SET_FLUID);

    public static final Werkstoff heavyNaquadahFuel = new Werkstoff(
            new short[] {54, 255, 54},
            "Heavy Naquadah Fuel",
            "still need processing",
            new Werkstoff.Stats().setToxic(true).setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 25,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahGas = new Werkstoff(
            new short[] {93, 219, 0},
            "Naquadah Gas",
            "Who need it?",
            new Werkstoff.Stats().setToxic(true).setRadioactive(true).setGas(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 26,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahAsphalt = new Werkstoff(
            new short[] {5, 37, 5},
            "Naquadah Asphalt",
            "It will damage the reactor.",
            new Werkstoff.Stats().setToxic(true).setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 27,
            TextureSet.SET_FLUID);

    public static final Werkstoff ether = new Werkstoff(
            new short[] {0xeb, 0xbc, 0x2f},
            "Ether",
            subscriptNumbers("CH3CH2OCH2CH3"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 28,
            TextureSet.SET_FLUID,
            new Pair<>(Carbon, 4),
            new Pair<>(Hydrogen, 10),
            new Pair<>(Oxygen, 1));

    public static final Werkstoff antimonyTrichloride = new Werkstoff(
            new short[] {0x0f, 0xdc, 0x34},
            "Antimony Trichloride Solution",
            subscriptNumbers("SbCl3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 29,
            TextureSet.SET_FLUID);

    public static final Werkstoff antimonyPentachlorideSolution = new Werkstoff(
            new short[] {0x15, 0x93, 0x2c},
            "Antimony Pentachloride Solution",
            subscriptNumbers("SbCl5"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 30,
            TextureSet.SET_FLUID);

    public static final Werkstoff antimonyPentachloride = new Werkstoff(
            new short[] {0x15, 0x93, 0x2c},
            "Antimony Pentachloride",
            subscriptNumbers("SbCl5"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 31,
            TextureSet.SET_FLUID);

    public static final Werkstoff antimonyPentafluoride = new Werkstoff(
            new short[] {0x16, 0xd5, 0xe2},
            "Antimony Pentafluoride",
            subscriptNumbers("SbF5"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 32,
            TextureSet.SET_FLUID);

    public static final Werkstoff fluoroantimonicAcid = new Werkstoff(
            new short[] {0x16, 0xd5, 0xe2},
            "Fluoroantimonic Acid",
            subscriptNumbers("HSbF6"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 33,
            TextureSet.SET_FLUID);

    public static final Werkstoff radioactiveSludge = new Werkstoff(
            new short[] {0xb3, 0x49, 0x1e},
            "Radioactive Sludge",
            ">>> DANGER <<<",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 34,
            TextureSet.SET_DULL);

    public static final Werkstoff acidNaquadahEmulsion = new Werkstoff(
            new short[] {0x25, 0x22, 0x22},
            "Acid Naquadah Emulsion",
            "??Nq??H" + CharExchanger.shifter(8314),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 35,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahEmulsion = new Werkstoff(
            new short[] {0x4a, 0x46, 0x45},
            "Naquadah Emulsion",
            "??Nq??",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 36,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahSolution = new Werkstoff(
            new short[] {0x84, 0x81, 0x80},
            "Naquadah Solution",
            "~Nq~",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 37,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkI = new Werkstoff(
            new short[] {0x62, 0x5c, 0x5b},
            "Naquadah Based Liquid Fuel MkI",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 38,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkIDepleted = new Werkstoff(
            new short[] {0xcb, 0xc3, 0xc1},
            "Naquadah Based Liquid Fuel MkI (Depleted)",
            new Werkstoff.Stats().setToxic(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 39,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkII = new Werkstoff(
            new short[] {0x52, 0x4e, 0x4d},
            "Naquadah Based Liquid Fuel MkII",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 40,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkIIDepleted = new Werkstoff(
            new short[] {0xb5, 0xb0, 0xae},
            "Naquadah Based Liquid Fuel MkII (Depleted)",
            new Werkstoff.Stats().setToxic(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 41,
            TextureSet.SET_FLUID);
    /*These materials will be enable when they are removed in GregTech*/
    /*
    public static final Werkstoff praseodymium = new Werkstoff(
            new short[]{0xff,0xff,0xff},
            "praseodymium",
            "Pr",
            new Werkstoff.Stats(),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems(),
            OffsetID + 42,
            TextureSet.SET_METALLIC
    );

    public static final Werkstoff rubidium = new Werkstoff(
            new short[]{0xff,0x2a,0x00},
            "rubidium",
            "Rb",
            new Werkstoff.Stats(),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems(),
            OffsetID + 43,
            TextureSet.SET_SHINY
    );

    public static final Werkstoff thulium = new Werkstoff(
            new short[]{0xff,0xff,0xff},
            "Thulium",
            "Tm",
            new Werkstoff.Stats(),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems(),
            OffsetID + 44,
            TextureSet.SET_METALLIC
    );
    */
    public static final Werkstoff naquadahBasedFuelMkIII = new Werkstoff(
            new short[] {0x29, 0x22, 0x21},
            "Naquadah Based Liquid Fuel MkIII",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 45,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkIIIDepleted = new Werkstoff(
            new short[] {0x66, 0x40, 0x38},
            "Naquadah Based Liquid Fuel MkIII (Depleted)",
            new Werkstoff.Stats().setToxic(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 46,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkIV = new Werkstoff(
            new short[] {0x0e, 0x0c, 0x0c},
            "Naquadah Based Liquid Fuel MkIV",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 47,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkIVDepleted = new Werkstoff(
            new short[] {0x8e, 0x34, 0x22},
            "Naquadah Based Liquid Fuel MkIV (Depleted)",
            new Werkstoff.Stats().setToxic(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 48,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkV = new Werkstoff(
            new short[] {0x00, 0x00, 0x00},
            "Naquadah Based Liquid Fuel MkV",
            "THE END",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 49,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahBasedFuelMkVDepleted = new Werkstoff(
            new short[] {0xff, 0xff, 0xff},
            "Naquadah Based Liquid Fuel MkV (Depleted)",
            "THE END (literally)",
            new Werkstoff.Stats().setToxic(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 50,
            TextureSet.SET_FLUID);

    public static final Werkstoff zincChloride = new Werkstoff(
            new short[] {0x73, 0xa5, 0xfc},
            "Zinc Chloride",
            subscriptNumbers("ZnCl2"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 51,
            TextureSet.SET_SHINY,
            new Pair<>(Zinc, 1),
            new Pair<>(Chlorine, 2));

    public static final Werkstoff zincThoriumAlloy = new Werkstoff(
            new short[] {0x12, 0x34, 0x56},
            "Zn-Th Alloy",
            subscriptNumbers("ZnTh"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems(),
            OffsetID + 52,
            TextureSet.SET_SHINY,
            new Pair<>(Zinc, 1),
            new Pair<>(Thorium, 1));

    // Naquadah Rework Materials
    public static final Werkstoff naquadahEarth = new Werkstoff(
            new short[] {0x4c, 0x4c, 0x4c},
            "Naquadah Oxide Mixture",
            subscriptNumbers("??NqTiGaAd??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures(),
            OffsetID + 53,
            TextureSet.SET_METALLIC);

    public static final Werkstoff titaniumTrifluoride = new Werkstoff(
            new short[] {0xc0, 0x92, 0xa8},
            "Titanium Trifluoride",
            subscriptNumbers("TiF3"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 54,
            TextureSet.SET_METALLIC,
            new Pair<>(Titanium, 1),
            new Pair<>(Fluorine, 3));

    public static final Werkstoff lowQualityNaquadahEmulsion = new Werkstoff(
            new short[] {0x4c, 0x4c, 0x4c},
            "Low Quality Naquadah Emulsion",
            subscriptNumbers("??NqGaAd??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 55,
            TextureSet.SET_FLUID);

    public static final Werkstoff galliumHydroxide = new Werkstoff(
            new short[] {0xa6, 0xa6, 0xa6},
            "Gallium Hydroxide",
            subscriptNumbers("Ga(OH)3"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 56,
            TextureSet.SET_DULL,
            new Pair<>(Gallium, 1),
            new Pair<>(Oxygen, 3),
            new Pair<>(Hydrogen, 3));

    public static final Werkstoff lowQualityNaquadahSolution = new Werkstoff(
            new short[] {0x71, 0x62, 0x62},
            "Low Quality Naquadah Solution",
            subscriptNumbers("~??NqAd??~"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 57,
            TextureSet.SET_FLUID);

    public static final Werkstoff towEthyl1Hexanol = new Werkstoff(
            new short[] {0x80, 0xb5, 0x57},
            "2-Ethyl-1-Hexanol",
            subscriptNumbers("C8H18O"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 58,
            TextureSet.SET_FLUID,
            new Pair<>(Carbon, 8),
            new Pair<>(Oxygen, 1),
            new Pair<>(Hydrogen, 18));

    public static final Werkstoff P507 = new Werkstoff(
            new short[] {0x29, 0xc2, 0x2a},
            "P-507",
            subscriptNumbers("(C8H17)2PO3H"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 59,
            TextureSet.SET_FLUID,
            new Pair<>(Carbon, 16),
            new Pair<>(Phosphorus, 1),
            new Pair<>(Oxygen, 3),
            new Pair<>(Hydrogen, 35));

    public static final Werkstoff naquadahAdamantiumSolution = new Werkstoff(
            new short[] {0x3d, 0x38, 0x38},
            "Naquadah-Adamantium Solution",
            subscriptNumbers("~NqAd~"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 60,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahRichSolution = new Werkstoff(
            new short[] {0x33, 0x33, 0x33},
            "Naquadah-Rich Solution",
            subscriptNumbers("~?Nq?~"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 61,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadahine = new Werkstoff(
            new short[] {0x33, 0x33, 0x33},
            "Naquadahine",
            subscriptNumbers("NqO2"),
            new Werkstoff.Stats().setElektrolysis(false),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 62,
            TextureSet.SET_METALLIC,
            new Pair<>(Naquadah, 1),
            new Pair<>(Oxygen, 2));

    public static final Werkstoff fluorineRichWasteLiquid = new Werkstoff(
            new short[] {0x13, 0x68, 0x62},
            "Fluorine-Rich Waste Liquid",
            new Werkstoff.Stats().setToxic(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 63,
            TextureSet.SET_FLUID);

    public static final Werkstoff wasteLiquid = new Werkstoff(
            new short[] {0x14, 0x1c, 0x68},
            "Waste Liquid",
            new Werkstoff.Stats().setToxic(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 64,
            TextureSet.SET_FLUID);

    public static final Werkstoff adamantine = new Werkstoff(
            new short[] {0xb7, 0xb7, 0xb7},
            "Adamantine",
            subscriptNumbers("Ad2O3"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 65,
            TextureSet.SET_DULL,
            new Pair<>(Adamantium, 2),
            new Pair<>(Oxygen, 3));

    public static final Werkstoff enrichedNaquadahEarth = new Werkstoff(
            new short[] {0x82, 0x68, 0x68},
            "Enriched-Naquadah Oxide Mixture",
            subscriptNumbers("??KeNq") + CharExchanger.shifter(8314) + "??",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures(),
            OffsetID + 66,
            TextureSet.SET_METALLIC);

    public static final Werkstoff triniumSulphate = new Werkstoff(
            new short[] {0xda, 0xda, 0xda},
            "Trinium Sulphate",
            subscriptNumbers("KeSO4"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 67,
            TextureSet.SET_METALLIC,
            new Pair<>(Trinium, 1),
            new Pair<>(Sulfur, 1),
            new Pair<>(Oxygen, 4));

    public static final Werkstoff enrichedNaquadahRichSolution = new Werkstoff(
            new short[] {0x52, 0x39, 0x39},
            "Enriched-Naquadah-Rich Solution",
            subscriptNumbers("~?Nq") + CharExchanger.shifter(8314) + "?~",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 68,
            TextureSet.SET_FLUID);

    public static final Werkstoff concentratedEnrichedNaquadahSludge = new Werkstoff(
            new short[] {0x52, 0x39, 0x39},
            "Concentrated Enriched-Naquadah Sludge",
            subscriptNumbers("?Nq") + CharExchanger.shifter(8314) + "?",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 69,
            TextureSet.SET_METALLIC);

    public static final Werkstoff enrichedNaquadahSulphate = new Werkstoff(
            new short[] {0x52, 0x39, 0x39},
            "Enriched-Naquadah Sulphate",
            "Nq" + CharExchanger.shifter(8314) + subscriptNumbers("(SO4)2"),
            new Werkstoff.Stats().setRadioactive(true).setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 70,
            TextureSet.SET_DULL,
            new Pair<>(NaquadahEnriched, 1),
            new Pair<>(Sulfur, 2),
            new Pair<>(Oxygen, 8));

    public static final Werkstoff naquadriaEarth = new Werkstoff(
            new short[] {0x4d, 0x4d, 0x55},
            "Naquadria Oxide Mixture",
            subscriptNumbers("??Nq*BaIn??"),
            new Werkstoff.Stats().setRadioactive(true).setToxic(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures(),
            OffsetID + 71,
            TextureSet.SET_METALLIC);

    public static final Werkstoff indiumPhosphate = new Werkstoff(
            new short[] {0x2b, 0x2e, 0x70},
            "Indium Phosphate",
            subscriptNumbers("InPO4"),
            new Werkstoff.Stats().setToxic(true).setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 72,
            TextureSet.SET_DULL,
            new Pair<>(Indium, 1),
            new Pair<>(Phosphorus, 1),
            new Pair<>(Oxygen, 4));

    public static final Werkstoff lowQualityNaquadriaPhosphate = new Werkstoff(
            new short[] {0x4d, 0x4d, 0x55},
            "Low Quality Naquadria Phosphate",
            subscriptNumbers("??Nq*3(PO4)4??"),
            new Werkstoff.Stats().setRadioactive(true).setToxic(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 73,
            TextureSet.SET_DULL);

    public static final Werkstoff naquadriaRichSolution = new Werkstoff(
            new short[] {0x1f, 0x1e, 0x33},
            "Naquadria-Rich Solution",
            subscriptNumbers("~?Nq*?~"),
            new Werkstoff.Stats().setRadioactive(true).setToxic(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 74,
            TextureSet.SET_FLUID);

    public static final Werkstoff lowQualityNaquadriaSulphate = new Werkstoff(
            new short[] {0x73, 0x72, 0x84},
            "Low Quality Naquadria Sulphate",
            subscriptNumbers("??Nq*(SO4)2??"),
            new Werkstoff.Stats().setRadioactive(true).setToxic(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 75,
            TextureSet.SET_METALLIC);

    public static final Werkstoff lowQualityNaquadriaSolution = new Werkstoff(
            new short[] {0x73, 0x72, 0x84},
            "Low Quality Naquadria Sulphate",
            subscriptNumbers("~??Nq*??~"),
            new Werkstoff.Stats().setRadioactive(true).setToxic(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 76,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadriaSulphate = new Werkstoff(
            new short[] {0x1f, 0x1e, 0x33},
            "Naquadria Sulphate",
            subscriptNumbers("Nq*(SO4)2"),
            new Werkstoff.Stats().setRadioactive(true).setToxic(true).setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 77,
            TextureSet.SET_METALLIC,
            new Pair<>(Naquadria, 1),
            new Pair<>(Sulfur, 2),
            new Pair<>(Oxygen, 8));

    public static final Werkstoff naquadahGoo = new Werkstoff(
            new short[] {0x4c, 0x4c, 0x4c},
            "Naquadah Goo",
            subscriptNumbers("??NqTiGaAd??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 78,
            TextureSet.SET_FLUID);

    public static final Werkstoff enrichedNaquadahGoo = new Werkstoff(
            new short[] {0x82, 0x68, 0x68},
            "Enriched Naquadah Goo",
            subscriptNumbers("??KeNq") + CharExchanger.shifter(8314) + "??",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 79,
            TextureSet.SET_FLUID);

    public static final Werkstoff naquadriaGoo = new Werkstoff(
            new short[] {0x4d, 0x4d, 0x55},
            "Naquadria Goo",
            subscriptNumbers("??Nq*BaIn??"),
            new Werkstoff.Stats().setRadioactive(true).setToxic(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 80,
            TextureSet.SET_FLUID);

    // material for reactor stuff
    public static final Werkstoff zircaloy4 = new Werkstoff(
            new short[] {0x8a, 0x6e, 0x68},
            "Zircaloy-4",
            subscriptNumbers("Zr34Sn5Fe2Cr"),
            new Werkstoff.Stats().setCentrifuge(true).setBlastFurnace(true).setMeltingPoint(2800),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addCraftingMetalWorkingItems()
                    .addMixerRecipes((short) 4),
            OffsetID + 81,
            TextureSet.SET_METALLIC,
            new Pair<>(WerkstoffLoader.Zirconium, 34),
            new Pair<>(Tin, 5),
            new Pair<>(Iron, 2),
            new Pair<>(Chrome, 1));

    public static final Werkstoff zircaloy2 = new Werkstoff(
            new short[] {0xa4, 0x8f, 0x8b},
            "Zircaloy-2",
            subscriptNumbers("Zr34Sn4FeCrNi"),
            new Werkstoff.Stats().setCentrifuge(true).setBlastFurnace(true).setMeltingPoint(2800),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addCraftingMetalWorkingItems()
                    .addMixerRecipes((short) 2),
            OffsetID + 82,
            TextureSet.SET_METALLIC,
            new Pair<>(WerkstoffLoader.Zirconium, 34),
            new Pair<>(Tin, 4),
            new Pair<>(Iron, 1),
            new Pair<>(Chrome, 1),
            new Pair<>(Nickel, 1));

    public static final Werkstoff incoloy903 = new Werkstoff(
            new short[] {0xa4, 0x8f, 0x8b},
            "Incoloy-903",
            subscriptNumbers("Fe12Ni10Co8Ti4Mo2Al"),
            new Werkstoff.Stats().setCentrifuge(true).setBlastFurnace(true).setMeltingPoint(3700),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addCraftingMetalWorkingItems()
                    .addSimpleMetalWorkingItems()
                    .addMixerRecipes((short) 6),
            OffsetID + 83,
            TextureSet.SET_METALLIC,
            new Pair<>(Iron, 12),
            new Pair<>(Nickel, 10),
            new Pair<>(Cobalt, 8),
            new Pair<>(Titanium, 4),
            new Pair<>(Molybdenum, 2),
            new Pair<>(Aluminium, 1));

    public static final Werkstoff adamantiumAlloy = new Werkstoff(
            new short[] {0xa0, 0xa0, 0xa0},
            "Adamantium Alloy",
            subscriptNumbers("Ad5Nq2La3"),
            new Werkstoff.Stats()
                    .setCentrifuge(true)
                    .setBlastFurnace(true)
                    .setMeltingPoint(5500)
                    .setSpeedOverride(191.2F)
                    .setDurOverride(102400),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addCraftingMetalWorkingItems()
                    .addSimpleMetalWorkingItems()
                    .addMultipleIngotMetalWorkingItems()
                    .addMixerRecipes((short) 3),
            OffsetID + 84,
            TextureSet.SET_SHINY,
            new Pair<>(Adamantium, 5),
            new Pair<>(Naquadah, 2),
            new Pair<>(Lanthanum, 3));

    public static final Werkstoff ethanolGasoline = new Werkstoff(
            new short[] {0xe4, 0xc6, 0x61},
            "Ethanol Gasoline",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 85,
            TextureSet.SET_FLUID);

    public static final Werkstoff cyclopentadiene = new Werkstoff(
            new short[] {0xff, 0xf6, 0xbd},
            "Cyclopentadiene",
            subscriptNumbers("C5H6"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 86,
            TextureSet.SET_FLUID);

    public static final Werkstoff ferrousChloride = new Werkstoff(
            new short[] {0x5b, 0x5b, 0x5b},
            "Iron II Chloride",
            subscriptNumbers("FeCl2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 87,
            TextureSet.SET_FLUID);

    public static final Werkstoff diethylamine = new Werkstoff(
            new short[] {0x69, 0x77, 0xca},
            "Diethylamine",
            subscriptNumbers("C4H11N"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 88,
            TextureSet.SET_FLUID);

    public static final Werkstoff impureFerroceneMixture = new Werkstoff(
            new short[] {0x79, 0x55, 0x08},
            "Impure Ferrocene Mixture",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 89,
            TextureSet.SET_FLUID);

    public static final Werkstoff ferroceneSolution = new Werkstoff(
            new short[] {0xde, 0x7e, 0x1c},
            "Ferrocene Solution",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 90,
            TextureSet.SET_FLUID);

    public static final Werkstoff ferroceneWaste = new Werkstoff(
            new short[] {0x35, 0x1d, 0x03},
            "Ferrocene Waste",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 91,
            TextureSet.SET_FLUID);

    public static final Werkstoff ferrocene = new Werkstoff(
            new short[] {0xf1, 0x8f, 0x2b},
            "Ferrocene",
            subscriptNumbers("Fe(C5H5)2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 92,
            TextureSet.SET_SHINY);

    public static final Werkstoff ironedKerosene = new Werkstoff(
            new short[] {0x97, 0x00, 0x61},
            "Jet Fuel No.3",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 93,
            TextureSet.SET_FLUID);

    public static final Werkstoff ironedFuel = new Werkstoff(
            new short[] {0xff, 0x98, 0x00},
            "Jet Fuel A",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 94,
            TextureSet.SET_FLUID);

    public static final Werkstoff marM200 = new Werkstoff(
            new short[] {0x51, 0x51, 0x51},
            "MAR-M200 Steel",
            new Werkstoff.Stats().setCentrifuge(true).setBlastFurnace(true).setMeltingPoint(5000),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addCraftingMetalWorkingItems()
                    .addSimpleMetalWorkingItems()
                    .addMultipleIngotMetalWorkingItems()
                    .addMixerRecipes((short) 7),
            OffsetID + 95,
            TextureSet.SET_SHINY,
            new Pair<>(Niobium, 2),
            new Pair<>(Chrome, 9),
            new Pair<>(Aluminium, 5),
            new Pair<>(Titanium, 2),
            new Pair<>(Cobalt, 10),
            new Pair<>(Tungsten, 13),
            new Pair<>(Nickel, 18));

    public static final Werkstoff marCeM200 = new Werkstoff(
            new short[] {0x38, 0x30, 0x30},
            "MAR-Ce-M200 Steel",
            new Werkstoff.Stats()
                    .setCentrifuge(true)
                    .setBlastFurnace(true)
                    .setMeltingPoint(5000)
                    .setMass(1200)
                    .setProtons(1000)
                    .setSpeedOverride(150F)
                    .setDurOverride(204800),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addCraftingMetalWorkingItems()
                    .addSimpleMetalWorkingItems()
                    .addMultipleIngotMetalWorkingItems(),
            OffsetID + 96,
            TextureSet.SET_METALLIC,
            new Pair<>(marM200, 18),
            new Pair<>(Cerium, 1));

    public static final Werkstoff lithiumChloride = new Werkstoff(
            new short[] {0xb7, 0xe2, 0xce},
            "Lithium Chloride",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .enforceUnification(),
            OffsetID + 97,
            TextureSet.SET_DULL,
            new Pair<>(Lithium, 1),
            new Pair<>(Chlorine, 1));

    public static final Werkstoff signalium = new Werkstoff(
            new short[] {0xd4, 0x40, 0x00},
            "Signalium",
            new Werkstoff.Stats().setBlastFurnace(true).setMeltingPoint(4000),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addCraftingMetalWorkingItems(),
            OffsetID + 98,
            TextureSet.SET_SHINY,
            new Pair<>(AnnealedCopper, 4),
            new Pair<>(Ardite, 2),
            new Pair<>(RedAlloy, 2));

    public static final Werkstoff lumiinessence = new Werkstoff(
            new short[] {0xe8, 0xf2, 0x24},
            "Lumiinessence",
            subscriptNumbers("(Al??)2(PO4)4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().onlyDust(),
            OffsetID + 99,
            TextureSet.SET_DULL);

    public static final Werkstoff lumiium = new Werkstoff(
            new short[] {0xe8, 0xf2, 0x24},
            "Lumiium",
            new Werkstoff.Stats().setBlastFurnace(true).setMeltingPoint(4000),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addCraftingMetalWorkingItems(),
            OffsetID + 100,
            TextureSet.SET_SHINY,
            new Pair<>(TinAlloy, 4),
            new Pair<>(SterlingSilver, 2),
            new Pair<>(lumiinessence, 2));

    public static final Werkstoff artheriumSn = new Werkstoff(
            new short[] {0x60, 0x36, 0xf7},
            "Artherium-Sn",
            new Werkstoff.Stats().setBlastFurnace(true).setMeltingPoint(6500).setCentrifuge(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addCraftingMetalWorkingItems()
                    .addMixerRecipes((short) 6),
            OffsetID + 101,
            TextureSet.SET_SHINY,
            new Pair<>(adamantiumAlloy, 12),
            new Pair<>(orundum, 9),
            new Pair<>(Tin, 8),
            new Pair<>(Arsenic, 7),
            new Pair<>(Caesium, 4),
            new Pair<>(Osmiridium, 3));

    public static final Werkstoff titaniumBetaC = new Werkstoff(
            new short[] {0xc7, 0x2f, 0xcc},
            "Tanmolyium Beta-C",
            new Werkstoff.Stats().setBlastFurnace(true).setMeltingPoint(5300).setCentrifuge(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addCraftingMetalWorkingItems()
                    .addMixerRecipes((short) 5),
            OffsetID + 102,
            TextureSet.SET_METALLIC,
            new Pair<>(Titanium, 5),
            new Pair<>(Molybdenum, 5),
            new Pair<>(Vanadium, 2),
            new Pair<>(Chrome, 3),
            new Pair<>(Aluminium, 1));

    public static final Werkstoff dalisenite = new Werkstoff(
            new short[] {0xb0, 0xb8, 0x12},
            "Dalisenite",
            new Werkstoff.Stats().setMeltingPoint(8700).setCentrifuge(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addCraftingMetalWorkingItems()
                    .addMixerRecipes((short) 6),
            OffsetID + 103,
            TextureSet.SET_SHINY,
            new Pair<>(titaniumBetaC, 14),
            new Pair<>(Tungsten, 10),
            new Pair<>(NiobiumTitanium, 9),
            new Pair<>(WerkstoffLoader.LuVTierMaterial, 8),
            new Pair<>(Quantium, 7),
            new Pair<>(Erbium, 3));

    public static final Werkstoff hikarium = new Werkstoff(
            new short[] {0xff, 0xd6, 0xfb},
            "Hikarium",
            new Werkstoff.Stats().setBlastFurnace(true).setMeltingPoint(5400).setCentrifuge(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addCraftingMetalWorkingItems()
                    .addMixerRecipes((short) 3),
            OffsetID + 104,
            TextureSet.SET_SHINY,
            new Pair<>(lumiium, 18),
            new Pair<>(Silver, 8),
            new Pair<>(Sunnarium, 4));

    public static final Werkstoff tairitsu = new Werkstoff(
            new short[] {0x36, 0x36, 0x36},
            "Tairitsu",
            new Werkstoff.Stats().setBlastFurnace(true).setMeltingPoint(7400).setCentrifuge(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures()
                    .onlyDust()
                    .addMolten()
                    .addMetalItems()
                    .addCraftingMetalWorkingItems()
                    .addMixerRecipes((short) 6),
            OffsetID + 105,
            TextureSet.SET_SHINY,
            new Pair<>(Tungsten, 8),
            new Pair<>(Naquadria, 7),
            new Pair<>(Bedrockium, 4),
            new Pair<>(Carbon, 4),
            new Pair<>(Vanadium, 3),
            new Pair<>(BlackPlutonium, 1));

    public static final Werkstoff antimonyPentafluorideSolution = new Werkstoff(
            new short[] {0x16, 0xd5, 0xe2},
            "Antimony Pentafluoride Solution",
            subscriptNumbers("SbF5"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            OffsetID + 106,
            TextureSet.SET_FLUID);

    public static final Werkstoff magnesiumSulphate = new Werkstoff(
            new short[] {0x87, 0x74, 0x91},
            "Magnesium Sulphate",
            subscriptNumbers("MgSO4"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            OffsetID + 107,
            TextureSet.SET_DULL,
            new Pair<>(Magnesium, 1),
            new Pair<>(Sulfur, 1),
            new Pair<>(Oxygen, 4));

    @Override
    public void run() {}
}
