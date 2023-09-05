package com.elisis.gtnhlanth.common.register;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;

import java.util.Arrays;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.util.Pair;

import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;

@SuppressWarnings({ "unchecked" })
public class WerkstoffMaterialPool implements Runnable {

    private static final int offsetID = 11_000;
    private static final int offsetID2 = 11_100;
    private static final int offsetID3 = 11_300;

    /*
     * public static final Werkstoff __ = new Werkstoff( new short[] {_, _, _}, "__", new Werkstoff.Stats(),
     * Werkstoff.Types.MIXTURE, new Werkstoff.GenerationFeatures().disable(), offsetID_, TextureSet.SET_DULL );
     */

    // Misc.
    public static final Werkstoff Hafnium = new Werkstoff(
            new short[] { 232, 224, 219 },
            "Hafnium",
            subscriptNumbers("Hf"),
            new Werkstoff.Stats(),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMetalItems().enforceUnification(), // Perhaps use
                                                                                                          // hafnia
                                                                                                          // liquid in
                                                                                                          // elemental
                                                                                                          // hafnium
                                                                                                          // synthesis
            offsetID,
            TextureSet.SET_DULL);

    public static final Werkstoff LowPurityHafnium = new Werkstoff(
            new short[] { 240, 223, 208 },
            "Low-Purity Hafnium",
            subscriptNumbers("??Hf??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(), // Perhaps use hafnia liquid in elemental hafnium
                                                                     // synthesis
            offsetID + 1,
            TextureSet.SET_DULL);

    public static final Werkstoff Hafnia = new Werkstoff(
            new short[] { 247, 223, 203 },
            "Hafnia",
            subscriptNumbers("HfO2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(), // Perhaps use hafnia liquid in elemental hafnium
                                                                     // synthesis
            offsetID + 2,
            TextureSet.SET_DULL);

    public static final Werkstoff HafniumTetrachloride = new Werkstoff(
            new short[] { 238, 247, 249 },
            "Hafnium Tetrachloride",
            subscriptNumbers("HfCl4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 3,
            TextureSet.SET_DULL);

    public static final Werkstoff HafniumTetrachlorideSolution = new Werkstoff(
            new short[] { 238, 247, 249 },
            "Hafnium Tetrachloride Solution",
            subscriptNumbers("HfCl4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 4,
            TextureSet.SET_FLUID);

    public static final Werkstoff HafniumIodide = new Werkstoff(
            new short[] { 216, 60, 1 },
            "Hafnium Iodide",
            subscriptNumbers("HfI4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 5,
            TextureSet.SET_DULL);

    public static final Werkstoff HafniumRunoff = new Werkstoff(
            new short[] { 74, 65, 42 }, // Literally the statistically ugliest colour
            "Hafnium Runoff",
            subscriptNumbers("??????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 6,
            TextureSet.SET_DULL);

    public static final Werkstoff Zirconium = new Werkstoff(
            new short[] { 225, 230, 225 },
            "Zirconium",
            subscriptNumbers("Zr"),
            new Werkstoff.Stats().setBlastFurnace(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMetalItems().enforceUnification(),
            offsetID + 7,
            TextureSet.SET_DULL);

    public static final Werkstoff Zirconia = new Werkstoff(
            new short[] { 177, 152, 101 },
            "Zirconia",
            subscriptNumbers("ZrO2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 8,
            TextureSet.SET_DULL);

    public static final Werkstoff ZirconiumTetrachloride = new Werkstoff(
            new short[] { 179, 164, 151 },
            "Zirconium Tetrachloride",
            subscriptNumbers("ZrCl4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 9,
            TextureSet.SET_DULL);

    public static final Werkstoff ZirconiumTetrachlorideSolution = new Werkstoff(
            new short[] { 179, 164, 151 },
            "Zirconium Tetrachloride Solution",
            subscriptNumbers("ZrCl4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(), // Blast Furnace needs liquid input because it
                                                                     // can't do 3 item inputs so have a shitty
            // material
            offsetID + 10,
            TextureSet.SET_FLUID);

    public static final Werkstoff HafniaZirconiaBlend = new Werkstoff(
            new short[] { 247, 223, 203 },
            "Hafnia-Zirconia Blend", // Maybe Hafnon??
            subscriptNumbers("??HfZr??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 11,
            TextureSet.SET_DULL);

    public static final Werkstoff Iodine = new Werkstoff(
            new short[] { 171, 40, 175 },
            "Iodine",
            subscriptNumbers("I"),
            new Werkstoff.Stats().setProtons(53).setMass(127).setSublimation(true).setBoilingPoint(484).setGas(true),
            Werkstoff.Types.ELEMENT,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addCells().enforceUnification(),
            offsetID + 12,
            TextureSet.SET_FLUID);

    // Lanthanide Line
    public static final Werkstoff MuddyRareEarthMonaziteSolution = new Werkstoff(
            new short[] { 111, 78, 55 },
            "Muddy Monazite Rare Earth Solution",
            subscriptNumbers("??LaNdZr??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 14,
            TextureSet.SET_FLUID);

    public static final Werkstoff DilutedRareEarthMonaziteMud = new Werkstoff(
            new short[] { 160, 120, 90 },
            "Diluted Monazite Rare Earth Mud",
            subscriptNumbers("??LaNdHf??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 15,
            TextureSet.SET_FLUID);

    public static final Werkstoff DilutedMonaziteSulfate = new Werkstoff(
            new short[] { 237, 201, 175 },
            "Diluted Monazite Sulfate",
            subscriptNumbers("??LaNd??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 16,
            TextureSet.SET_FLUID);

    public static final Werkstoff NitratedRareEarthMonaziteConcentrate = new Werkstoff(
            new short[] { 250, 223, 173 },
            "Nitrogenated Monazite Rare Earth Concentrate",
            subscriptNumbers("??LaNd??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 17,
            TextureSet.SET_FLUID);

    public static final Werkstoff NitricMonaziteLeachedConcentrate = new Werkstoff(
            new short[] { 244, 202, 22 },
            "Nitric Monazite Leached Concentrate",
            subscriptNumbers("??LaNd??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 18,
            TextureSet.SET_FLUID);

    public static final Werkstoff MonaziteSulfate = new Werkstoff(
            new short[] { 152, 118, 84 },
            "Monazite Sulfate",
            subscriptNumbers("??CeEu??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 19,
            TextureSet.SET_DULL);

    public static final Werkstoff AcidicMonazitePowder = new Werkstoff(
            new short[] { 50, 23, 77 },
            "Acidic Monazite Powder",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 20,
            TextureSet.SET_DULL);

    public static final Werkstoff MonaziteRareEarthFiltrate = new Werkstoff(
            new short[] { 72, 60, 50 },
            "Monazite Rare Earth Filtrate",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 21,
            TextureSet.SET_DULL);

    public static final Werkstoff NeutralizedMonaziteRareEarthFiltrate = new Werkstoff(
            new short[] { 50, 23, 77 },
            "Neutralized Monazite Rare Earth Filtrate",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 22,
            TextureSet.SET_DULL);

    public static final Werkstoff MonaziteRareEarthHydroxideConcentrate = new Werkstoff(
            new short[] { 193, 154, 107 },
            "Monazite Rare Earth Hydroxide Concentrate",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 23,
            TextureSet.SET_DULL);

    public static final Werkstoff DriedMonaziteRareEarthConcentrate = new Werkstoff(
            new short[] { 250, 214, 165 },
            "Dried Monazite Rare Earth Concentrate",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 24,
            TextureSet.SET_DULL);

    public static final Werkstoff CeriumDioxide = new Werkstoff(
            new short[] { 255, 255, 255 },
            "Cerium Dioxide",
            subscriptNumbers("CeO2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust().enforceUnification(),
            offsetID + 25,
            TextureSet.SET_DULL);

    public static final Werkstoff CeriumChloride = new Werkstoff(
            new short[] { 255, 255, 255 },
            "Cerium Chloride",
            subscriptNumbers("CeCl3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 26,
            TextureSet.SET_DULL);

    public static final Werkstoff CeriumOxalate = new Werkstoff(
            new short[] { 255, 255, 224 },
            "Cerium Oxalate",
            subscriptNumbers("Ce2(C2O4)3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 27,
            TextureSet.SET_DULL);

    public static final Werkstoff CeriumIIIOxide = new Werkstoff(
            new short[] { 255, 255, 102 },
            "Cerium (III) Oxide",
            subscriptNumbers("Ce2O3"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 28,
            TextureSet.SET_DULL,
            Arrays.asList(Materials.Cerium, Materials.Oxygen),
            new Pair<>(Materials.Cerium, 2),
            new Pair<>(Materials.Oxygen, 3));

    public static final Werkstoff CeriumRichMixture = new Werkstoff(
            new short[] { 244, 164, 96 },
            "Cerium-Rich Mixture",
            subscriptNumbers("??Ce??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 29,
            TextureSet.SET_DULL);

    public static final Werkstoff CooledMonaziteRareEarthConcentrate = new Werkstoff(
            new short[] { 250, 214, 165 },
            "Cooled Monazite Rare Earth Concentrate",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 30,
            TextureSet.SET_DULL);

    public static final Werkstoff MonaziteRarerEarthSediment = new Werkstoff(
            new short[] { 250, 214, 165 },
            "MonaziteRarer Earth Sediment",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 31,
            TextureSet.SET_DULL);

    public static final Werkstoff MonaziteHeterogenousHalogenicRareEarthMixture = new Werkstoff(
            new short[] { 250, 214, 165 },
            "Heterogenous Halogenic Monazite Rare Earth Mixture",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 32,
            TextureSet.SET_DULL);

    public static final Werkstoff SaturatedMonaziteRareEarthMixture = new Werkstoff(
            new short[] { 250, 214, 165 },
            "Saturated Monazite Rare Earth",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 33,
            TextureSet.SET_DULL);

    public static final Werkstoff SamaricResidue = new Werkstoff(
            new short[] { 248, 243, 231 },
            "Samaric Residue",
            subscriptNumbers("??SmGd??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 34,
            TextureSet.SET_DULL);

    public static final Werkstoff MonaziteResidue = new Werkstoff(
            new short[] { 64, 69, 62 },
            "Monazite Residue",
            subscriptNumbers("??ZrHfTh??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 35,
            TextureSet.SET_DULL);

    public static final Werkstoff AmmoniumNitrate = new Werkstoff(
            new short[] { 255, 255, 255 },
            "Ammonium Nitrate Solution",
            subscriptNumbers("NH4NO3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 36,
            TextureSet.SET_FLUID);

    public static final Werkstoff ThoriumPhosphateCake = new Werkstoff(
            new short[] { 188, 143, 143 },
            "Thorium-Phosphate Cake",
            subscriptNumbers("??ThP??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 37,
            TextureSet.SET_DULL);

    public static final Werkstoff ThoriumPhosphateConcentrate = new Werkstoff(
            new short[] { 217, 144, 88 },
            "Thorium-Phosphate Concentrate",
            subscriptNumbers("??ThP??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 38,
            TextureSet.SET_DULL);

    public static final Werkstoff UraniumFiltrate = new Werkstoff(
            new short[] { 190, 240, 94 },
            "UraniumFiltrate",
            subscriptNumbers("??U??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 39,
            TextureSet.SET_DULL);

    public static final Werkstoff NeutralizedUraniumFiltrate = new Werkstoff(
            new short[] { 217, 120, 88 },
            "Neutralized Uranium Filtrate",
            subscriptNumbers("??U??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 40,
            TextureSet.SET_DULL);

    public static final Werkstoff SeaweedAsh = new Werkstoff(
            new short[] { 70, 75, 71 },
            "Seaweed Ash",
            new Werkstoff.Stats(),
            Werkstoff.Types.BIOLOGICAL,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 41,
            TextureSet.SET_DULL);

    public static final Werkstoff SeaweedConcentrate = new Werkstoff(
            new short[] { 70, 100, 71 },
            "Seaweed Concentrate",
            subscriptNumbers("??I??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.BIOLOGICAL,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 42,
            TextureSet.SET_FLUID);

    public static final Werkstoff SeaweedByproducts = new Werkstoff(
            new short[] { 125, 50, 138 },
            "Seaweed Byproducts",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 45,
            TextureSet.SET_FLUID);

    public static final Werkstoff NitricLeachedMonaziteMixture = new Werkstoff(
            new short[] { 125, 50, 138 },
            "Nitric-Leached Monazite Mixture",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 46,
            TextureSet.SET_FLUID);

    public static final Werkstoff EuropiumOxide = new Werkstoff(
            new short[] { 255, 255, 255 },
            "Europium Oxide",
            subscriptNumbers("EuO"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 47,
            TextureSet.SET_DULL);

    public static final Werkstoff EuropiumSulfide = new Werkstoff(
            new short[] { 5, 0, 5 },
            "Europium Sulfide",
            subscriptNumbers("EuS"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 48,
            TextureSet.SET_DULL);

    public static final Werkstoff UnknownBlend = new Werkstoff(
            new short[] { 0, 0, 5 },
            "UnknownBlend",
            subscriptNumbers("?????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 49,
            TextureSet.SET_DULL);

    public static final Werkstoff EuropiumIIIOxide = new Werkstoff(
            new short[] { 255, 230, 255 },
            "Europium III Oxide",
            subscriptNumbers("Eu2O3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 50,
            TextureSet.SET_DULL);

    // TODO

    // BASTNASITE
    public static final Werkstoff MuddyRareEarthBastnasiteSolution = new Werkstoff(
            new short[] { 205, 133, 63 },
            "Muddy Bastnasite Rare Earth Solution",
            subscriptNumbers("??LaCeY??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID2,
            TextureSet.SET_FLUID);
    /*
     * public static final Werkstoff FluorosilicicAcid = new Werkstoff( new short[] {205, 133, 63},
     * "Hexafluorosilicic Acid", subscriptNumbers("H2SiF6"), new Werkstoff.Stats(), Werkstoff.Types.COMPOUND, new
     * Werkstoff.GenerationFeatures().disable().addCells(), offsetID2 + 1, TextureSet.SET_FLUID );
     */
    public static final Werkstoff SodiumFluorosilicate = new Werkstoff(
            new short[] { 205, 133, 63 },
            "Sodiumfluorosilicate",
            subscriptNumbers("Na2SiF6"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID2 + 2,
            TextureSet.SET_FLUID);

    public static final Werkstoff SteamCrackedBasnasiteSolution = new Werkstoff(
            new short[] { 205, 133, 63 },
            "Steam-Cracked Bastnasite Mud",
            subscriptNumbers("??LaCeY??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID2 + 3,
            TextureSet.SET_FLUID);

    public static final Werkstoff ConditionedBastnasiteMud = new Werkstoff(
            new short[] { 205, 133, 63 },
            "Conditioned Bastnasite Mud",
            subscriptNumbers("??LaCeY??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID2 + 4,
            TextureSet.SET_FLUID);

    public static final Werkstoff DiltedRareEarthBastnasiteMud = new Werkstoff(
            new short[] { 205, 133, 63 },
            "Diluted Bastnasite Mud",
            subscriptNumbers("??LaCeY??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID2 + 5,
            TextureSet.SET_FLUID);

    public static final Werkstoff FilteredBastnasiteMud = new Werkstoff(
            new short[] { 205, 133, 63 },
            "Filtered Bastnasite Mud",
            subscriptNumbers("??LaCeY??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID2 + 6,
            TextureSet.SET_FLUID);

    public static final Werkstoff BastnasiteRareEarthOxidePowder = new Werkstoff(
            new short[] { 205, 133, 63 },
            "Bastnasite Rare Earth Oxides",
            subscriptNumbers("??LaCeY??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 7,
            TextureSet.SET_DULL);

    public static final Werkstoff LeachedBastnasiteRareEarthOxides = new Werkstoff(
            new short[] { 205, 133, 63 },
            "Acid-Leached Bastnasite Rare Earth Oxides",
            subscriptNumbers("??LaCeY??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 8,
            TextureSet.SET_DULL);

    public static final Werkstoff Gangue = new Werkstoff(
            new short[] { 0, 0, 0 },
            "Gangue",
            subscriptNumbers("Useless..."),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 9,
            TextureSet.SET_DULL);
    // TODO: Deal with colouring
    public static final Werkstoff RoastedRareEarthOxides = new Werkstoff(
            new short[] { 160, 82, 45 },
            "Roasted Rare Earth Oxides",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 10,
            TextureSet.SET_DULL);

    public static final Werkstoff WetRareEarthOxides = new Werkstoff(
            new short[] { 160, 82, 49 },
            "Wet Rare Earth Oxides",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 11,
            TextureSet.SET_DULL);

    public static final Werkstoff CeriumOxidisedRareEarthOxides = new Werkstoff(
            new short[] { 160, 82, 49 },
            "Cerium-Oxidised Rare Earth Oxides",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 12,
            TextureSet.SET_DULL);

    public static final Werkstoff BastnasiteRarerEarthOxides = new Werkstoff(
            new short[] { 160, 82, 49 },
            "Bastnasite Rarer Earth Oxides",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 13,
            TextureSet.SET_DULL);

    public static final Werkstoff NitratedBastnasiteRarerEarthOxides = new Werkstoff(
            new short[] { 160, 90, 60 },
            "Nitrogenated Bastnasite Rarer Earth Oxides",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID2 + 14,
            TextureSet.SET_DULL);

    public static final Werkstoff SaturatedBastnasiteRarerEarthOxides = new Werkstoff(
            new short[] { 170, 90, 60 },
            "Bastnasite Rarer Earth Oxide Suspension",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID2 + 15,
            TextureSet.SET_DULL);

    public static final Werkstoff SamaricRareEarthConcentrate = new Werkstoff(
            new short[] { 170, 90, 60 },
            "Samaric Rare Earth Concentrate",
            subscriptNumbers("??SmHoTb??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 16,
            TextureSet.SET_DULL);

    public static final Werkstoff NeodymicRareEarthConcentrate = new Werkstoff(
            new short[] { 170, 90, 60 },
            "Neodymium Rare Earth Concentrate",
            subscriptNumbers("??LaNdPr??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 17,
            TextureSet.SET_DULL);

    public static final Werkstoff FluorinatedNeodymicRareEarths = new Werkstoff(
            new short[] { 170, 90, 60 },
            "Fluorinated Neodymium Rare Earths",
            subscriptNumbers("??LaNdPr??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 18,
            TextureSet.SET_DULL);

    public static final Werkstoff RareEarthTrifluorides = new Werkstoff(
            new short[] { 82, 112, 102 },
            "Rare Earth Trifluorides",
            subscriptNumbers("??LaNd??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 20,
            TextureSet.SET_DULL);

    public static final Werkstoff LanthaniumChloride = new Werkstoff(
            new short[] { 82, 112, 102 },
            "Lanthanium Chloride",
            subscriptNumbers("LaCl3"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 21,
            TextureSet.SET_DULL,
            Arrays.asList(Materials.Lanthanum, Materials.Chlorine),
            new Pair<>(Materials.Lanthanum, 1),
            new Pair<>(Materials.Chlorine, 3));

    public static final Werkstoff NeodymiumOxide = new Werkstoff(
            new short[] { 82, 112, 102 },
            "Neodymium Oxide",
            subscriptNumbers("Nd2O3"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 22,
            TextureSet.SET_DULL,
            Arrays.asList(Materials.Neodymium, Materials.Oxygen),
            new Pair<>(Materials.Neodymium, 2),
            new Pair<>(Materials.Oxygen, 3));

    public static final Werkstoff FluorinatedSamaricConcentrate = new Werkstoff(
            new short[] { 255, 182, 193 },
            "Fluorinated Samaric Concentrate",
            subscriptNumbers("??SmHo??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 23,
            TextureSet.SET_DULL);

    public static final Werkstoff CalciumFluoride = new Werkstoff(
            new short[] { 255, 250, 250 },
            "Calcium Fluoride",
            subscriptNumbers("CaF2"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addCells(),
            offsetID2 + 24,
            TextureSet.SET_DULL,
            Arrays.asList(Materials.Calcium, Materials.Fluorine),
            new Pair<>(Materials.Calcium, 1),
            new Pair<>(Materials.Fluorine, 2));

    public static final Werkstoff SamariumTerbiumMixture = new Werkstoff(
            new short[] { 223, 182, 193 },
            "Samarium-Terbium Mixture",
            subscriptNumbers("??SmTb??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 25,
            TextureSet.SET_DULL);

    public static final Werkstoff NitratedSamariumTerbiumMixture = new Werkstoff(
            new short[] { 223, 182, 193 },
            "Nitrogenated Samarium-Terbium Mixture",
            subscriptNumbers("??SmTb??NH4NO3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 26,
            TextureSet.SET_DULL);

    public static final Werkstoff TerbiumNitrate = new Werkstoff(
            new short[] { 167, 252, 0 },
            "Terbium Nitrate",
            subscriptNumbers("TbNO3"),
            new Werkstoff.Stats().setElektrolysis(true),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 27,
            TextureSet.SET_DULL,
            Arrays.asList(Materials.Terbium, Materials.Nitrogen, Materials.Oxygen),
            new Pair<>(Materials.Terbium, 1),
            new Pair<>(Materials.Nitrogen, 1),
            new Pair<>(Materials.Oxygen, 3));

    public static final Werkstoff SamariumOreConcentrate = new Werkstoff(
            new short[] { 255, 200, 230 },
            "Samarium Ore Concentrate",
            subscriptNumbers("??Sm??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 28,
            TextureSet.SET_DULL);

    public static final Werkstoff DephosphatedSamariumConcentrate = new Werkstoff(
            new short[] { 255, 170, 220 },
            "Dephosphated Samarium Concentrate",
            subscriptNumbers("??Sm??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID2 + 29,
            TextureSet.SET_DULL);

    // Weird/Exciting Chemicals
    public static final Werkstoff Tetrahydrofuran = new Werkstoff(
            new short[] { 222, 165, 164 },
            "Tetrahydrofuran",
            subscriptNumbers("(CH2)4O"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID3,
            TextureSet.SET_FLUID);

    // 1,4-Butanediol
    public static final Werkstoff Butanediol = new Werkstoff(
            new short[] { 185, 78, 72 },
            "1,4-Butanediol",
            subscriptNumbers("HO(CH2)4OH"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID3 + 1,
            TextureSet.SET_FLUID);

    // Acidicised 1,4-Butanediol
    public static final Werkstoff AcidicButanediol = new Werkstoff(
            new short[] { 255, 239, 213 },
            "Acidicised 1,4-Butanediol",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID3 + 2,
            TextureSet.SET_FLUID);

    // Tellurium-Molybdenum-Oxide Catalyst
    public static final Werkstoff MoTeOCatalyst = new Werkstoff(
            new short[] { 238, 131, 238 },
            "Tellurium-Molybdenum-Oxide Catalyst",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID3 + 3,
            TextureSet.SET_DULL);

    // Tellurium Oxide
    public static final Werkstoff TelluriumIVOxide = new Werkstoff(
            new short[] { 229, 199, 187 },
            "Tellurium (IV) Oxide",
            subscriptNumbers("TeO2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID3 + 4,
            TextureSet.SET_DULL);

    public static final Werkstoff MolybdenumIVOxide = new Werkstoff(
            new short[] { 52, 53, 57 },
            "Molybdenum (IV) Oxide",
            subscriptNumbers("MoO2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID3 + 5,
            TextureSet.SET_DULL);

    public static final Werkstoff Polytetrahydrofuran = new Werkstoff(
            new short[] { 192, 128, 129 },
            "Polytetrahydrofuran",
            subscriptNumbers("(C4H8O)OH2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addCells(),
            offsetID3 + 6,
            TextureSet.SET_DULL);

    public static final Werkstoff TungstophosphoricAcid = new Werkstoff(
            new short[] { 223, 255, 0 },
            "Tungstophosphoric Acid",
            subscriptNumbers("H3PW12O40"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID3 + 7,
            TextureSet.SET_FLUID);

    public static final Werkstoff TolueneDiisocyanate = new Werkstoff(
            new short[] { 255, 255, 102 },
            "Toluene Diisocyanate",
            subscriptNumbers("CH3C6H3(NCO)2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID3 + 8,
            TextureSet.SET_FLUID);

    public static final Werkstoff Dinitrotoluene = new Werkstoff(
            new short[] { 216, 191, 216 },
            "Dinitrotoluene",
            subscriptNumbers("C7H6N2O4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID3 + 9,
            TextureSet.SET_FLUID);

    public static final Werkstoff Diaminotoluene = new Werkstoff(
            new short[] { 227, 218, 201 },
            "Diaminotoluene",
            subscriptNumbers("C6H3(NH2)2CH3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID3 + 10,
            TextureSet.SET_FLUID);

    public static final Werkstoff TolueneTetramethylDiisocyanate = new Werkstoff(
            new short[] { 255, 255, 255 },
            "Toluene Tetramethyl Diisocyanate",
            subscriptNumbers("(CONH)2(C6H4)2CH2(C4O)"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID3 + 11,
            TextureSet.SET_FLUID);

    public static final Werkstoff PTMEGElastomer = new Werkstoff(
            new short[] { 248, 248, 255 },
            "PTMEG Elastomer",
            new Werkstoff.Stats().setMeltingPoint(600).setMeltingVoltage(64),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems(),
            offsetID3 + 12,
            TextureSet.SET_DULL);

    public static final Werkstoff DilutedAcetone = new Werkstoff(
            new short[] { 254, 254, 250 },
            "Diluted Acetone",
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID3 + 16,
            TextureSet.SET_FLUID);

    public static void runInit() {

        addSubTags();
    }

    private static void addSubTags() {

        // WerkstoffMaterialPool.PTMEGElastomer.add(SubTag.BOUNCY, SubTag.STRETCHY);

    }

    @Override
    public void run() {}
}
