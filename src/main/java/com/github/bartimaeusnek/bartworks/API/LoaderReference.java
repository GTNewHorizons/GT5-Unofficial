/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.API;

import cpw.mods.fml.common.Loader;

/**
 * DO NOT CALL THIS IN THE CORE MOD! IT WILL __NOT__ WORK AND FUCK EVERYTHING UP!
 */
public class LoaderReference {
    private LoaderReference() {}

    public static boolean Natura;
    public static boolean RandomThings;
    public static boolean TConstruct;
    public static boolean chisel;
    public static boolean Railcraft;
    public static boolean Ztones;
    public static boolean witchery;
    public static boolean GalaxySpace;
    public static boolean GalacticraftCore;
    public static boolean GalacticraftMars;
    public static boolean Thaumcraft;
    public static boolean miscutils;
    public static boolean tectech;
    public static boolean ExtraUtilities;
    public static boolean RWG;
    public static boolean galacticgreg;
    public static boolean gendustry;
    public static boolean croploadcore;
    public static boolean Forestry;
    public static boolean berriespp;
    public static boolean dreamcraft;
    public static boolean BloodArsenal;
    public static boolean Botany;
    public static boolean EnderIO;
    public static boolean HardcoreEnderExpension;
    public static boolean betterloadingscreen;
    public static boolean TGregworks;
    public static boolean ProjRedIllumination;
    public static boolean Botania;

    public static void init() {
        Natura = Loader.isModLoaded("Natura");
        RandomThings = Loader.isModLoaded("RandomThings");
        TConstruct = Loader.isModLoaded("TConstruct");
        chisel = Loader.isModLoaded("chisel");
        Railcraft = Loader.isModLoaded("Railcraft");
        Ztones = Loader.isModLoaded("Ztones");
        witchery = Loader.isModLoaded("witchery");
        GalaxySpace = Loader.isModLoaded("GalaxySpace");
        GalacticraftCore = Loader.isModLoaded("GalacticraftCore");
        GalacticraftMars = Loader.isModLoaded("GalacticraftMars");
        Thaumcraft = Loader.isModLoaded("Thaumcraft");
        miscutils = Loader.isModLoaded("miscutils");
        tectech = Loader.isModLoaded("tectech");
        ExtraUtilities = Loader.isModLoaded("ExtraUtilities");
        RWG = Loader.isModLoaded("RWG");
        galacticgreg = Loader.isModLoaded("galacticgreg");
        gendustry = Loader.isModLoaded("gendustry");
        croploadcore = Loader.isModLoaded("croploadcore");
        Forestry = Loader.isModLoaded("Forestry");
        berriespp = Loader.isModLoaded("berriespp");
        dreamcraft = Loader.isModLoaded("dreamcraft");
        BloodArsenal = Loader.isModLoaded("BloodArsenal");
        Botany = Loader.isModLoaded("Botany");
        EnderIO = Loader.isModLoaded("EnderIO");
        HardcoreEnderExpension = Loader.isModLoaded("HardcoreEnderExpension");
        betterloadingscreen = Loader.isModLoaded("betterloadingscreen");
        TGregworks = Loader.isModLoaded("TGregworks");
        ProjRedIllumination = Loader.isModLoaded("ProjRed|Illumination");
        Botania = Loader.isModLoaded("Botania");
    }
}
