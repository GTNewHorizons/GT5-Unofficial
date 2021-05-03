package GoodGenerator.Items;

import GoodGenerator.util.CharExchanger;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.TextureSet;
import com.github.bartimaeusnek.bartworks.util.Pair;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;
import static gregtech.api.enums.Materials.*;


public class MyMaterial implements Runnable {

    //Uranium Based Fuel Line
    public static final Werkstoff graphiteUraniumMixture = new Werkstoff(
            new short[]{0x3a,0x77,0x3d},
            "Graphite-Uranium Mixture",
            subscriptNumbers("C3U"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addMixerRecipes().onlyDust(),
            31000,
            TextureSet.SET_DULL,
            new Pair<> (Graphite,3),
            new Pair<> (Uranium,1)
    );

    public static final Werkstoff uraniumBasedLiquidFuel = new Werkstoff(
            new short[]{0x00,0xff,0x00},
            "Uranium Based Liquid Fuel",
            subscriptNumbers("U36Rb8Qt4Rn"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31001,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff uraniumBasedLiquidFuelExcited = new Werkstoff(
            new short[]{0x00,0xff,0x00},
            "Uranium Based Liquid Fuel (Excited State)",
            subscriptNumbers("*(U36Rb8Qt4Rn)*"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31002,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff uraniumBasedLiquidFuelDepleted = new Werkstoff(
            new short[]{0x6e,0x8b,0x3d},
            "Uranium Based Liquid Fuel (Depleted)",
            subscriptNumbers("Pb?Bi?Ba?Xe?"),
            new Werkstoff.Stats().setToxic(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31003,
            TextureSet.SET_FLUID
    );

    //Thorium Based Fuel
    public static final Werkstoff uraniumCarbideThoriumMixture = new Werkstoff(
            new short[]{0x16,0x32,0x07},
            "Uranium Carbide-Thorium Mixture",
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addMixerRecipes().onlyDust(),
            31004,
            TextureSet.SET_DULL,
            new Pair<> (Thorium,8),
            new Pair<> (WerkstoffLoader.Thorium232,4),
            new Pair<> (Uranium235,1),
            new Pair<> (Carbon,3)
    );

    public static final Werkstoff thoriumBasedLiquidFuel = new Werkstoff(
            new short[]{0x50,0x32,0x66},
            "Thorium Based Liquid Fuel",
            subscriptNumbers("Th432Li4D2Hg"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31005,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff thoriumBasedLiquidFuelExcited = new Werkstoff(
            new short[]{0x50,0x32,0x66},
            "Thorium Based Liquid Fuel (Excited State)",
            subscriptNumbers("*(Th432Li4D2Hg)*"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31006,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff thoriumBasedLiquidFuelDepleted = new Werkstoff(
            new short[]{0x7d,0x6c,0x8a},
            "Thorium Based Liquid Fuel (Depleted)",
            subscriptNumbers("Lu?Pr?B?In?"),
            new Werkstoff.Stats().setToxic(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31007,
            TextureSet.SET_FLUID
    );

    //Plutonium Based Fuel
    public static final Werkstoff plutoniumOxideUraniumMixture = new Werkstoff(
            new short[]{0xd1,0x1f,0x4a},
            "Plutonium Oxide-Uranium Mixture",
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addMixerRecipes().onlyDust(),
            31008,
            TextureSet.SET_SHINY,
            new Pair<> (Plutonium,10),
            new Pair<> (Oxygen,12),
            new Pair<> (Uranium,2),
            new Pair<> (Carbon,8)
    );

    public static final Werkstoff plutoniumBasedLiquidFuel = new Werkstoff(
            new short[]{0xef,0x15,0x15},
            "Plutonium Based Liquid Fuel",
            subscriptNumbers("Pu45Nt8Cs16Am2"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31009,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff plutoniumBasedLiquidFuelExcited = new Werkstoff(
            new short[]{0xef,0x15,0x15},
            "Plutonium Based Liquid Fuel (Excited State)",
            subscriptNumbers("*(Pu45Nt8Cs16Am2)*"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31010,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff plutoniumBasedLiquidFuelDepleted = new Werkstoff(
            new short[]{0x67,0x19,0x19},
            "Plutonium Based Liquid Fuel (Depleted)",
            subscriptNumbers("Tn?Ce?Au?Kr?"),
            new Werkstoff.Stats().setToxic(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31011,
            TextureSet.SET_FLUID
    );

    //Thorium-233
    public static final Werkstoff oxalate = new Werkstoff(
            new short[]{0x79,0xd8,0x55},
            "Oxalate",
            Werkstoff.Types.BIOLOGICAL,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31012,
            TextureSet.SET_FLUID,
            new Pair<> (Hydrogen,2),
            new Pair<> (Carbon,2),
            new Pair<> (Oxygen,4)
    );

    public static final Werkstoff vanadiumPentoxide = new Werkstoff(
            new short[]{0xde,0x8d,0x12},
            "Vanadium Pentoxide",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            31013,
            TextureSet.SET_SHINY,
            new Pair<> (Vanadium,2),
            new Pair<> (Oxygen,5)
    );

    public static final Werkstoff thoriumNitrate = new Werkstoff(
            new short[]{0xba,0xe8,0x26},
            "Thorium Nitrate",
            subscriptNumbers("Th(NO3)4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31014,
            TextureSet.SET_DULL
    );

    public static final Werkstoff thoriumOxalate = new Werkstoff(
            new short[]{0x50,0x63,0x13},
            "Thorium Oxalate",
            subscriptNumbers("Th(C2O4)2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            31015,
            TextureSet.SET_DULL
    );

    public static final Werkstoff thoriumHydroxide = new Werkstoff(
            new short[]{0x92,0xae,0x89},
            "Thorium Hydroxide",
            subscriptNumbers("Th(OH)4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            31016,
            TextureSet.SET_SHINY
    );

    public static final Werkstoff sodiumOxalate = new Werkstoff(
            new short[]{0xe4,0xf8,0x9b},
            "Sodium Oxalate",
            subscriptNumbers("Na2C2O4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            31017,
            TextureSet.SET_DULL
    );

    public static final Werkstoff thoriumTetrachloride = new Werkstoff(
            new short[]{0x13,0x7c,0x16},
            "thorium Tetrachloride",
            subscriptNumbers("ThCl4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31018,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff thoriumTetrafluoride = new Werkstoff(
            new short[]{0x15,0x6a,0x6a},
            "Thorium Tetrafluoride",
            subscriptNumbers("ThF4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31019,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff thorium232Tetrafluoride = new Werkstoff(
            new short[]{0x15,0x6a,0x6a},
            "Thorium-232 Tetrafluoride",
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31020,
            TextureSet.SET_FLUID,
            new Pair<> (WerkstoffLoader.Thorium232,1),
            new Pair<> (Fluorine,4)
    );

    //Atomic Separation Catalyst
    public static final Werkstoff atomicSeparationCatalyst = new Werkstoff(
            new short[]{0xe8,0x5e,0x0c},
            "Atomic Separation Catalyst",
            "the melting core...",
            new Werkstoff.Stats().setMeltingPoint(5000).setBlastFurnace(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems().addSimpleMetalWorkingItems().addCraftingMetalWorkingItems().addMultipleIngotMetalWorkingItems(),
            31021,
            TextureSet.SET_SHINY
    );

    public static final Werkstoff orundum = new Werkstoff(
            new short[]{0xcd,0x26,0x26},
            "Orundum",
            "Or",
            new Werkstoff.Stats(),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().addGems(),
            31022,
            TextureSet.SET_DIAMOND
    );

    //Naquadah Fuel Rework
    public static final Werkstoff extremelyUnstableNaquadah = new Werkstoff(
            new short[]{0x06,0x26,0x05},
            "Extremely Unstable Naquadah",
            "Nq"+ CharExchanger.shifter(9734),
            new Werkstoff.Stats().setMeltingPoint(7000).setBlastFurnace(true),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems().addSimpleMetalWorkingItems().addCraftingMetalWorkingItems().addMultipleIngotMetalWorkingItems(),
            31023,
            TextureSet.SET_SHINY
    );

    public static final Werkstoff lightNaquadahFuel = new Werkstoff(
            new short[]{92,203,92},
            "Light Naquadah Fuel",
            "far from enough",
            new Werkstoff.Stats().setToxic(true).setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31024,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff heavyNaquadahFuel = new Werkstoff(
            new short[]{54,255,54},
            "Heavy Naquadah Fuel",
            "still need processing",
            new Werkstoff.Stats().setToxic(true).setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31025,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff naquadahGas = new Werkstoff(
            new short[]{93,219,0},
            "Naquadah Gas",
            "Who need it?",
            new Werkstoff.Stats().setToxic(true).setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31026,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff naquadahAsphalt = new Werkstoff(
            new short[]{5,37,5},
            "Naquadah Asphalt",
            "It will damage the reactor.",
            new Werkstoff.Stats().setToxic(true).setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31027,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff ether = new Werkstoff(
            new short[]{0xeb,0xbc,0x2f},
            "ether",
            subscriptNumbers("CH3CH2OCH2CH3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31028,
            TextureSet.SET_FLUID,
            new Pair<> (Carbon,4),
            new Pair<> (Hydrogen, 10),
            new Pair<> (Oxygen, 1)
    );

    public static final Werkstoff antimonyTrichloride = new Werkstoff(
            new short[]{0x0f,0xdc,0x34},
            "Antimony Trichloride",
            subscriptNumbers("SbCl3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31029,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff antimonyPentachloride = new Werkstoff(
            new short[]{0x15,0x93,0x2c},
            "Antimony Pentachloride",
            subscriptNumbers("SbCl5"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31030,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff antimonyPentafluoride = new Werkstoff(
            new short[]{0x16,0xd5,0xe2},
            "Antimony Pentafluoride",
            subscriptNumbers("SbF5"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31031,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff fluoroantimonicAcid = new Werkstoff(
            new short[]{0x16,0xd5,0xe2},
            "Fluoroantimonic Acid",
            subscriptNumbers("HSbF6"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31032,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff radioactiveSludge = new Werkstoff(
            new short[]{0xb3,0x49,0x1e},
            "Radioactive Sludge",
            ">>> DANGER <<<",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            31033,
            TextureSet.SET_DULL
    );

    public static final Werkstoff acidNaquadahEmulsion = new Werkstoff(
            new short[]{0x25,0x22,0x22},
            "Acid Naquadah Emulsion",
            "??Nq??H"+CharExchanger.shifter(8314),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31034,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff NaquadahEmulsion = new Werkstoff(
            new short[]{0x4a,0x46,0x45},
            "Naquadah Emulsion",
            "??Nq??",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31035,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff NaquadahSolution = new Werkstoff(
            new short[]{0x84,0x81,0x80},
            "Naquadah Solution",
            "~Nq~",
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31036,
            TextureSet.SET_FLUID
    );

    @Override
    public void run() { }
}
