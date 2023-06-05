package com.elisis.gtnhlanth.common.register;

import static com.github.bartimaeusnek.bartworks.system.material.Werkstoff.Types.*;
import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.TextureSet.*;

import net.minecraft.util.EnumChatFormatting;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.util.Pair;

/*
 * Originally authored by botn365 under the MIT License. See BotdustriesLICENSE
 */
@SuppressWarnings("unchecked")
public class BotWerkstoffMaterialPool implements Runnable {

    private static final String DEPRECATED = EnumChatFormatting.DARK_RED
            + "Deprecated; Will be removed in the next update";

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
    public static final Werkstoff Ethylchloroformate = new Werkstoff(
            new short[] { 0x0a, 0xc2, 0xcc },
            "Ethyl Chloroformate",
            subscriptNumbers("C3H5ClO2"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29906,
            SET_FINE);
    public static final Werkstoff Ethylcarbamate = new Werkstoff(
            new short[] { 0x0d, 0xa9, 0xb8 },
            "Ethyl Carbamate",
            subscriptNumbers("CH3CH2O2CNH2"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29907,
            SET_FINE);
    public static final Werkstoff EthylNnitrocarbamate = new Werkstoff(
            new short[] { 0x0d, 0x85, 0xb8 },
            "Ethyl N-nitrocarbamate",
            subscriptNumbers("C3H6N2O4"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29908,
            SET_FINE);
    public static final Werkstoff AmmoniumNnitrourethane = new Werkstoff(
            new short[] { 0x0d, 0x54, 0xb8 },
            "Ammonium N-nitrourethane",
            subscriptNumbers("C3H9N3O4"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29909,
            SET_FINE);
    public static final Werkstoff EthylDinitrocarbamate = new Werkstoff(
            new short[] { 0x39, 0x08, 0xc2 },
            "Ethyl Dinitrocarbamate",
            subscriptNumbers("C3H5N3O6"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29910,
            SET_FINE);
    public static final Werkstoff DinitrogenPentoxide = new Werkstoff(
            new short[] { 0xcf, 0xeb, 0x34 },
            "Dinitrogen Pentoxide",
            subscriptNumbers("N2O5"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().onlyDust(),
            29911,
            SET_FINE);
    public static final Werkstoff AmmoniumDinitramide = new Werkstoff(
            new short[] { 0x8a, 0x0f, 0xd1 },
            "Ammonium Dinitramide",
            subscriptNumbers("H4N4O4"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29912,
            SET_FINE);
    public static final Werkstoff LMP103S = new Werkstoff(
            new short[] { 0xbf, 0x2f, 0xc2 },
            "LMP-103S",
            DEPRECATED,
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29913,
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
    public static final Werkstoff PhthalicAnhydride = new Werkstoff(
            new short[] { 0x7c, 0x99, 0x42 },
            "Phthalic Anhydride",
            subscriptNumbers("C8H4O3"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().onlyDust(),
            29916,
            SET_METALLIC);
    public static final Werkstoff VanadiumPentoxide = new Werkstoff(
            new short[] { 0x69, 0x69, 0x69 },
            "Vanadium Pentoxide",
            subscriptNumbers("V2O5"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().onlyDust(),
            29917,
            SET_METALLIC);
    public static final Werkstoff TertButylbenzene = new Werkstoff(
            new short[] { 0, 0, 0 },
            "Tert-Butylbenzene",
            subscriptNumbers("C10H14"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29918,
            SET_METALLIC);
    public static final Werkstoff TwoTertButylAnthraquinone = new Werkstoff(
            new short[] { 0xcc, 0x86, 0x5a },
            "2-tert-butyl-anthraquinone",
            subscriptNumbers("C18H16O2"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29919,
            SET_METALLIC);
    public static final Werkstoff TwoTertButylAnthrahydroquinone = new Werkstoff(
            new short[] { 0xad, 0x53, 0x1a },
            "2-tert-butyl-anthrahydroquinone",
            subscriptNumbers("C18H17O2"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29920,
            SET_METALLIC);
    public static final Werkstoff DimethylSulfate = new Werkstoff(
            new short[] { 0xff, 0xfb, 0x00 },
            "Dimethyl Sulfate",
            subscriptNumbers("(CH3O)2SO2"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29923,
            SET_METALLIC);
    public static final Werkstoff Monomethylhydrazine = new Werkstoff(
            new short[] { 0xff, 0x61, 0x00 },
            "Monomethylhydrazine",
            subscriptNumbers("CH6N2"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29924,
            SET_METALLIC);
    public static final Werkstoff EthylAcetate = new Werkstoff(
            new short[] { 0x0c, 0xfb, 0x32b },
            "Ethyl Acetate",
            subscriptNumbers("C4H8O2"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29926,
            SET_METALLIC);
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
    public static final Werkstoff MonomethylhydrazineFuelMix = new Werkstoff(
            new short[] { 0x78, 0xe3, 0xa7 },
            "Monomethylhydrazine Fuel Mix",
            DEPRECATED,
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29929,
            SET_METALLIC);
    public static final Werkstoff UnsymmetricalDimethylhydrazineFuelMix = new Werkstoff(
            new short[] { 0xc8, 0xff, 0x00 },
            "Unsymmetrical Dimethylhydrazine Fuel Mix",
            DEPRECATED,
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29930,
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
    public static final Werkstoff BoronTrioxide = new Werkstoff(
            new short[] { 0xe3, 0xa6, 0xd3 },
            "Boron Trioxide",
            subscriptNumbers("B2O3"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().onlyDust(),
            29931,
            SET_METALLIC);
    public static final Werkstoff BoronTrifluoride = new Werkstoff(
            new short[] { 0xd0, 0xe0, 0x3f },
            "Boron Trifluoride",
            subscriptNumbers("BF3"),
            new Werkstoff.Stats().setGas(true),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29932,
            SET_METALLIC);
    public static final Werkstoff NitroniumTetrafluoroborate = new Werkstoff(
            new short[] { 0x57, 0x69, 0x2d },
            "Nitronium Tetrafluoroborate",
            subscriptNumbers("NO2BF4"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().onlyDust(),
            29933,
            SET_METALLIC);
    public static final Werkstoff Trinitramid = new Werkstoff(
            new short[] { 0x28, 0x2b, 0x70 },
            "Trinitramid",
            subscriptNumbers("N4O6"),
            new Werkstoff.Stats().setGas(true),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29934,
            SET_METALLIC);
    public static final Werkstoff AmmoniaBoronfluorideSolution = new Werkstoff(
            new short[] { 0x28, 0x2b, 0x70 },
            "Ammonia Boronfluoride Solution",
            subscriptNumbers("NH4BF4"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29935,
            SET_METALLIC);
    public static final Werkstoff SodiumTetrafluoroborate = new Werkstoff(
            new short[] { 0xbe, 0x6e, 0xe0 },
            "Sodium Tetrafluoroborate",
            subscriptNumbers("NaBF4"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29936,
            SET_METALLIC);
    public static final Werkstoff SodiumFluoride = new Werkstoff(
            new short[] { 0x9f, 0x70, 0xe6 },
            "Sodium Fluoride",
            subscriptNumbers("NaF"),
            new Werkstoff.Stats(),
            COMPOUND,
            new Werkstoff.GenerationFeatures().onlyDust(),
            29937,
            SET_METALLIC);
    public static final Werkstoff Tetrafluoroborate = new Werkstoff(
            new short[] { 0x6a, 0x53, 0x8c },
            "Tetrafluoroboric Acid",
            subscriptNumbers("HBF4"),
            new Werkstoff.Stats().setGas(true),
            COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            29938,
            SET_METALLIC);

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }
}
