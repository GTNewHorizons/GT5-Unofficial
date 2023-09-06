package com.elisis.gtnhlanth.common.register;

import static com.github.bartimaeusnek.bartworks.system.material.Werkstoff.Types.*;
import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.TextureSet.*;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.util.Pair;

/*
 * Originally authored by botn365 under the MIT License. See BotdustriesLICENSE
 */
@SuppressWarnings("unchecked")
public class BotWerkstoffMaterialPool implements Runnable {

    public static final Werkstoff TungsticAcid = new Werkstoff(
            new short[] { 0xf5, 0xf1, 0x16 },
            "Tungstic Acid",
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().onlyDust().enforceUnification(),
            29900,
            SET_SHINY,
            new Pair<>(Hydrogen, 2),
            new Pair<>(Tungsten, 1),
            new Pair<>(Oxygen, 4));
    public static final Werkstoff TungstenTrioxide = new Werkstoff(
            new short[] { 0x0f, 0x5, 0x16 },
            "Tungsten Trioxide",
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().onlyDust().enforceUnification(),
            29901,
            SET_SHINY,
            new Pair<>(Tungsten, 1),
            new Pair<>(Oxygen, 3));
    public static final Werkstoff AmmoniumNitrate = new Werkstoff(
            new short[] { 0x81, 0xcc, 0x00 },
            "Ammonium Nitrate",
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().onlyDust(),
            29903,
            SET_FINE,
            new Pair<>(Nitrogen, 1),
            new Pair<>(Hydrogen, 4),
            new Pair<>(Nitrogen, 1),
            new Pair<>(Oxygen, 3));
    public static final Werkstoff SodiumTungstate = new Werkstoff(
            new short[] { 0xc, 0xed, 0xd7, 0 },
            "Sodium Tungstate",
            subscriptNumbers("Na2WO4"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29904,
            SET_FINE);
    public static final Werkstoff Phosgene = new Werkstoff(
            new short[] { 0x15, 0xa1, 0x1a },
            "Phosgene",
            subscriptNumbers("COCl2"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29905,
            SET_FINE);
    public static final Werkstoff OXylene = new Werkstoff(
            new short[] { 0x88, 0x94, 0xa8 },
            "O-Xylene",
            subscriptNumbers("C8H10"),
            new Werkstoff.Stats().setGas(true),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29915,
            SET_FINE);
    public static final Werkstoff Acetylhydrazine = new Werkstoff(
            new short[] { 0xd1, 0x5c, 0x5c },
            "Acetylhydrazine",
            subscriptNumbers("C2H6N2O"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29927,
            SET_METALLIC);
    public static final Werkstoff UnsymmetricalDimethylhydrazine = new Werkstoff(
            new short[] { 0x80, 0x06, 0x00 },
            "Unsymmetrical Dimethylhydrazine",
            subscriptNumbers("H2NN(CH3)2"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29928,
            SET_METALLIC);
    public static final Werkstoff Nitromethane = new Werkstoff(
            new short[] { 0x87, 0x7d, 0x60 },
            "Nitromethane",
            subscriptNumbers("CH3NO2"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29914,
            SET_METALLIC);

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }
}
