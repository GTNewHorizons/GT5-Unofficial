package gtnhlanth.common.register;

import static bartworks.system.material.Werkstoff.Types.*;
import static bartworks.util.BWUtil.subscriptNumbers;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.TextureSet.*;

import org.apache.commons.lang3.tuple.Pair;

import bartworks.system.material.Werkstoff;

/*
 * Originally authored by botn365 under the MIT License. See BotdustriesLICENSE
 */
public class BotWerkstoffMaterialPool implements Runnable {

    public static final Werkstoff TungsticAcid = new Werkstoff(
        new short[] { 0xf5, 0xf1, 0x16 },
        "Tungstic Acid",
        new Werkstoff.Stats(),
        COMPOUND,
        new Werkstoff.GenerationFeatures().onlyDust()
            .enforceUnification(),
        29900,
        SET_SHINY,
        Pair.of(Hydrogen, 2),
        Pair.of(Tungsten, 1),
        Pair.of(Oxygen, 4));
    public static final Werkstoff TungstenTrioxide = new Werkstoff(
        new short[] { 0x0f, 0x5, 0x16 },
        "Tungsten Trioxide",
        new Werkstoff.Stats(),
        COMPOUND,
        new Werkstoff.GenerationFeatures().onlyDust()
            .enforceUnification(),
        29901,
        SET_SHINY,
        Pair.of(Tungsten, 1),
        Pair.of(Oxygen, 3));
    public static final Werkstoff AmmoniumNitrate = new Werkstoff(
        new short[] { 0x81, 0xcc, 0x00 },
        "Ammonium Nitrate",
        new Werkstoff.Stats(),
        COMPOUND,
        new Werkstoff.GenerationFeatures().onlyDust(),
        29903,
        SET_FINE,
        Pair.of(Nitrogen, 1),
        Pair.of(Hydrogen, 4),
        Pair.of(Nitrogen, 1),
        Pair.of(Oxygen, 3));
    public static final Werkstoff SodiumTungstate = new Werkstoff(
        new short[] { 0xc, 0xed, 0xd7, 0 },
        "Sodium Tungstate",
        subscriptNumbers("Na2WO4"),
        new Werkstoff.Stats(),
        COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        29904,
        SET_FINE);
    public static final Werkstoff Phosgene = new Werkstoff(
        new short[] { 0x15, 0xa1, 0x1a },
        "Phosgene",
        subscriptNumbers("COCl2"),
        new Werkstoff.Stats(),
        COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        29905,
        SET_FINE);
    public static final Werkstoff Nitromethane = new Werkstoff(
        new short[] { 0x87, 0x7d, 0x60 },
        "Nitromethane",
        subscriptNumbers("CH3NO2"),
        new Werkstoff.Stats(),
        COMPOUND,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        29914,
        SET_METALLIC);

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }
}
