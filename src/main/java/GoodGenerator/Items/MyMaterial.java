package GoodGenerator.Items;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import gregtech.api.enums.TextureSet;
import com.github.bartimaeusnek.bartworks.util.Pair;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;
import static gregtech.api.enums.Materials.*;


public class MyMaterial implements Runnable {

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

    @Override
    public void run() { }
}
