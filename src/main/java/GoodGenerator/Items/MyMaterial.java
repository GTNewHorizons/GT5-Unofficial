package GoodGenerator.Items;

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
            subscriptNumbers("Th864Li4D2Hg"),
            new Werkstoff.Stats().setRadioactive(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            31005,
            TextureSet.SET_FLUID
    );

    public static final Werkstoff thoriumBasedLiquidFuelExcited = new Werkstoff(
            new short[]{0x50,0x32,0x66},
            "Thorium Based Liquid Fuel (Excited State)",
            subscriptNumbers("*(Th864Li4D2Hg)*"),
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

    @Override
    public void run() { }
}
