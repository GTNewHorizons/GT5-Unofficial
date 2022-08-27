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
package com.github.bartimaeusnek.bartworks.system.material;

import static com.github.bartimaeusnek.bartworks.system.material.Werkstoff.Types.*;

import com.github.bartimaeusnek.bartworks.MainMod;
import gregtech.api.enums.Materials;

public class BW_GT_MaterialReference {
    private static final Werkstoff.GenerationFeatures ADD_CASINGS_ONLY =
            new Werkstoff.GenerationFeatures().disable().addCasings();

    public static Werkstoff Aluminium = new Werkstoff(Materials.Aluminium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 19);
    public static Werkstoff Americium = new Werkstoff(Materials.Americium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 103);
    public static Werkstoff Antimony = new Werkstoff(Materials.Antimony, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 58);
    //    public static Werkstoff Argon = new Werkstoff(Materials.Argon, ADD_CASINGS_ONLY, ELEMENT, 31_766+24);
    public static Werkstoff Arsenic = new Werkstoff(Materials.Arsenic, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 39);
    public static Werkstoff Barium = new Werkstoff(Materials.Barium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 63);
    public static Werkstoff Beryllium = new Werkstoff(Materials.Beryllium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 8);
    public static Werkstoff Bismuth = new Werkstoff(Materials.Bismuth, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 90);
    public static Werkstoff Boron = new Werkstoff(Materials.Boron, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 9);
    public static Werkstoff Caesium = new Werkstoff(Materials.Caesium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 62);
    //    public static Werkstoff Calcium = new Werkstoff(Materials.Calcium, ADD_CASINGS_ONLY, ELEMENT, 31_766+26);
    public static Werkstoff Carbon = new Werkstoff(Materials.Carbon, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 10);
    public static Werkstoff Cadmium = new Werkstoff(Materials.Cadmium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 55);
    public static Werkstoff Cerium = new Werkstoff(Materials.Cerium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 65);
    //    public static Werkstoff Chlorine = new Werkstoff(Materials.Chlorine, ADD_CASINGS_ONLY, ELEMENT, 31_766+23);
    public static Werkstoff Chrome = new Werkstoff(Materials.Chrome, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 30);
    public static Werkstoff Cobalt = new Werkstoff(Materials.Cobalt, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 33);
    public static Werkstoff Copper = new Werkstoff(Materials.Copper, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 35);
    //    public static Werkstoff Deuterium = new Werkstoff(Materials.Deuterium, ADD_CASINGS_ONLY, ELEMENT,  31_766+2);
    public static Werkstoff Dysprosium = new Werkstoff(Materials.Dysprosium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 73);
    //    public static Werkstoff Empty = new Werkstoff(Materials.Empty, ADD_CASINGS_ONLY, ELEMENT,  31_766+0);
    public static Werkstoff Erbium = new Werkstoff(Materials.Erbium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 75);
    public static Werkstoff Europium = new Werkstoff(Materials.Europium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 70);
    //    public static Werkstoff Fluorine = new Werkstoff(Materials.Fluorine, ADD_CASINGS_ONLY, ELEMENT, 31_766+14);
    public static Werkstoff Gadolinium = new Werkstoff(Materials.Gadolinium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 71);
    public static Werkstoff Gallium = new Werkstoff(Materials.Gallium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 37);
    public static Werkstoff Gold = new Werkstoff(Materials.Gold, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 86);
    public static Werkstoff Holmium = new Werkstoff(Materials.Holmium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 74);
    //    public static Werkstoff Hydrogen = new Werkstoff(Materials.Hydrogen, ADD_CASINGS_ONLY, ELEMENT,  31_766+1);
    //    public static Werkstoff Helium = new Werkstoff(Materials.Helium, ADD_CASINGS_ONLY, ELEMENT,  31_766+4);
    //    public static Werkstoff Helium_3 = new Werkstoff(Materials.Helium_3, ADD_CASINGS_ONLY, ELEMENT,  31_766+5);
    public static Werkstoff Indium = new Werkstoff(Materials.Indium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 56);
    public static Werkstoff Iridium = new Werkstoff(Materials.Iridium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 84);
    public static Werkstoff Iron = new Werkstoff(Materials.Iron, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 32);
    public static Werkstoff Lanthanum = new Werkstoff(Materials.Lanthanum, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 64);
    public static Werkstoff Lead = new Werkstoff(Materials.Lead, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 89);
    public static Werkstoff Lithium = new Werkstoff(Materials.Lithium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 6);
    public static Werkstoff Lutetium = new Werkstoff(Materials.Lutetium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 78);
    public static Werkstoff Magnesium = new Werkstoff(Materials.Magnesium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 18);
    public static Werkstoff Manganese = new Werkstoff(Materials.Manganese, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 31);
    //    public static Werkstoff Mercury = new Werkstoff(Materials.Mercury, ADD_CASINGS_ONLY, ELEMENT, 31_766+87);
    public static Werkstoff Molybdenum = new Werkstoff(Materials.Molybdenum, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 48);
    public static Werkstoff Neodymium = new Werkstoff(Materials.Neodymium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 67);
    public static Werkstoff Neutronium = new Werkstoff(Materials.Neutronium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 129);
    public static Werkstoff Nickel = new Werkstoff(Materials.Nickel, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 34);
    public static Werkstoff Niobium = new Werkstoff(Materials.Niobium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 47);
    //    public static Werkstoff Nitrogen = new Werkstoff(Materials.Nitrogen, ADD_CASINGS_ONLY, ELEMENT, 31_766+12);
    public static Werkstoff Osmium = new Werkstoff(Materials.Osmium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 83);
    //    public static Werkstoff Oxygen = new Werkstoff(Materials.Oxygen, ADD_CASINGS_ONLY, ELEMENT, 31_766+13);
    public static Werkstoff Palladium = new Werkstoff(Materials.Palladium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 52);
    public static Werkstoff Phosphorus = new Werkstoff(Materials.Phosphorus, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 21);
    public static Werkstoff Platinum = new Werkstoff(Materials.Platinum, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 85);
    public static Werkstoff Plutonium = new Werkstoff(Materials.Plutonium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 100);
    public static Werkstoff Plutonium241 =
            new Werkstoff(Materials.Plutonium241, ADD_CASINGS_ONLY, ISOTOPE, 31_766 + 101);
    public static Werkstoff Potassium = new Werkstoff(Materials.Potassium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 25);
    public static Werkstoff Praseodymium =
            new Werkstoff(Materials.Praseodymium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 66);
    public static Werkstoff Promethium = new Werkstoff(Materials.Promethium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 68);
    //    public static Werkstoff Radon = new Werkstoff(Materials.Radon, ADD_CASINGS_ONLY, ELEMENT, 31_766+93);
    public static Werkstoff Rubidium = new Werkstoff(Materials.Rubidium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 43);
    public static Werkstoff Samarium = new Werkstoff(Materials.Samarium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 69);
    public static Werkstoff Scandium = new Werkstoff(Materials.Scandium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 27);
    public static Werkstoff Silicon = new Werkstoff(Materials.Silicon, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 20);
    public static Werkstoff Silver = new Werkstoff(Materials.Silver, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 54);
    public static Werkstoff Sodium = new Werkstoff(Materials.Sodium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 17);
    public static Werkstoff Strontium = new Werkstoff(Materials.Strontium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 44);
    public static Werkstoff Sulfur = new Werkstoff(Materials.Sulfur, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 22);
    public static Werkstoff Tantalum = new Werkstoff(Materials.Tantalum, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 80);
    //    public static Werkstoff Tellurium = new Werkstoff(Materials.Tellurium, ADD_CASINGS_ONLY, ELEMENT, 31_766+59);
    public static Werkstoff Terbium = new Werkstoff(Materials.Terbium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 72);
    public static Werkstoff Thorium = new Werkstoff(Materials.Thorium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 96);
    public static Werkstoff Thulium = new Werkstoff(Materials.Thulium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 76);
    public static Werkstoff Tin = new Werkstoff(Materials.Tin, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 57);
    public static Werkstoff Titanium = new Werkstoff(Materials.Titanium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 28);
    public static Werkstoff Tritanium = new Werkstoff(Materials.Tritanium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 329);
    public static Werkstoff Tritium = new Werkstoff(Materials.Tritium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 3);
    public static Werkstoff Tungsten = new Werkstoff(Materials.Tungsten, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 81);
    public static Werkstoff Uranium = new Werkstoff(Materials.Uranium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 98);
    public static Werkstoff Uranium235 = new Werkstoff(Materials.Uranium235, ADD_CASINGS_ONLY, ISOTOPE, 31_766 + 97);
    public static Werkstoff Vanadium = new Werkstoff(Materials.Vanadium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 29);
    public static Werkstoff Ytterbium = new Werkstoff(Materials.Ytterbium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 77);
    public static Werkstoff Yttrium = new Werkstoff(Materials.Yttrium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 45);
    public static Werkstoff Zinc = new Werkstoff(Materials.Zinc, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 36);
    public static Werkstoff Ardite = new Werkstoff(Materials.Ardite, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 382);
    public static Werkstoff Naquadah = new Werkstoff(Materials.Naquadah, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 324);
    public static Werkstoff NaquadahAlloy =
            new Werkstoff(Materials.NaquadahAlloy, ADD_CASINGS_ONLY, COMPOUND, 31_766 + 325);
    public static Werkstoff NaquadahEnriched =
            new Werkstoff(Materials.NaquadahEnriched, ADD_CASINGS_ONLY, ISOTOPE, 31_766 + 326);
    public static Werkstoff Naquadria = new Werkstoff(Materials.Naquadria, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 327);
    public static Werkstoff WroughtIron = new Werkstoff(Materials.WroughtIron, ADD_CASINGS_ONLY, ISOTOPE, 31_766 + 304);
    public static Werkstoff AnnealedCopper =
            new Werkstoff(Materials.AnnealedCopper, ADD_CASINGS_ONLY, ISOTOPE, 31_766 + 345);

    public static Werkstoff Osmiridium = new Werkstoff(Materials.Osmiridium, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 317);
    public static Werkstoff SterlingSilver =
            new Werkstoff(Materials.SterlingSilver, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 350);
    public static Werkstoff RoseGold = new Werkstoff(Materials.RoseGold, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 351);
    public static Werkstoff BlackBronze = new Werkstoff(Materials.BlackBronze, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 352);
    public static Werkstoff BismuthBronze =
            new Werkstoff(Materials.BismuthBronze, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 353);
    public static Werkstoff BlackSteel = new Werkstoff(Materials.BlackSteel, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 334);
    public static Werkstoff RedSteel = new Werkstoff(Materials.RedSteel, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 348);
    public static Werkstoff BlueSteel = new Werkstoff(Materials.BlueSteel, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 349);
    public static Werkstoff DamascusSteel =
            new Werkstoff(Materials.DamascusSteel, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 335);
    public static Werkstoff TungstenSteel =
            new Werkstoff(Materials.TungstenSteel, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 316);
    public static Werkstoff Ultimet = new Werkstoff(Materials.Ultimet, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 344);
    public static Werkstoff TungstenCarbide =
            new Werkstoff(Materials.TungstenCarbide, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 370);
    public static Werkstoff VanadiumSteel =
            new Werkstoff(Materials.VanadiumSteel, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 371);
    public static Werkstoff HSSG = new Werkstoff(Materials.HSSG, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 372);
    public static Werkstoff HSSE = new Werkstoff(Materials.HSSE, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 373);
    public static Werkstoff HSSS = new Werkstoff(Materials.HSSS, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 374);
    public static Werkstoff StainlessSteel =
            new Werkstoff(Materials.StainlessSteel, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 306);
    public static Werkstoff Brass = new Werkstoff(Materials.Brass, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 301);
    public static Werkstoff Bronze = new Werkstoff(Materials.Bronze, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 300);

    public static Werkstoff Wood = new Werkstoff(Materials.Wood, ADD_CASINGS_ONLY, BIOLOGICAL, 31_766 + 809);
    // public static Werkstoff WoodSealed = new Werkstoff(Materials.WoodSealed, ADD_CASINGS_ONLY,
    // BIOLOGICAL,31_766+889);
    // public static Werkstoff Cheese = new Werkstoff(Materials.Cheese, new
    // Werkstoff.GenerationFeatures().addCasings().addMetalItems().addMultipleIngotMetalWorkingItems().enforceUnification(), BIOLOGICAL,31_766+894);

    public static Werkstoff Steel = new Werkstoff(Materials.Steel, ADD_CASINGS_ONLY, COMPOUND, 31_766 + 305);
    public static Werkstoff Polytetrafluoroethylene =
            new Werkstoff(Materials.Polytetrafluoroethylene, ADD_CASINGS_ONLY, COMPOUND, 31_766 + 473);
    public static Werkstoff Plastic = new Werkstoff(Materials.Plastic, ADD_CASINGS_ONLY, COMPOUND, 31_766 + 874);
    public static Werkstoff Epoxid = new Werkstoff(Materials.Epoxid, ADD_CASINGS_ONLY, COMPOUND, 31_766 + 470);
    public static Werkstoff Magnesia = new Werkstoff(
            Materials.Magnesia,
            new Werkstoff.GenerationFeatures().disable().addMetalItems().addMolten(),
            COMPOUND,
            31_766 + 471);

    public static void init() {
        MainMod.LOGGER.info("Load Elements from GT");
    }

    //    TODO: GT++ only?

    //     public static Werkstoff Flerovium = new Werkstoff(Materials.Flerovium, <GenerationFeatures>,
    // <TypeRef>,31_766+984);

    //    public static Werkstoff HeeEndium = new Werkstoff(Materials.HeeEndium, <GenerationFeatures>,
    // <TypeRef>,31_766+770);
    //    public static Werkstoff PlatinumGroupSludge = new Werkstoff(Materials.PlatinumGroupSludge,
    // <GenerationFeatures>, <TypeRef>,31_766+241);
    //    public static Werkstoff Draconium = new Werkstoff(Materials.Draconium, <GenerationFeatures>,
    // <TypeRef>,31_766+975);
    //    public static Werkstoff DraconiumAwakened = new Werkstoff(Materials.DraconiumAwakened, <GenerationFeatures>,
    // <TypeRef>,31_766+976);
    //    public static Werkstoff Adamantium = new Werkstoff(Materials.Adamantium, <GenerationFeatures>,
    // <TypeRef>,31_766+319);
    //    public static Werkstoff Alduorite = new Werkstoff(Materials.Alduorite, <GenerationFeatures>,
    // <TypeRef>,31_766+485);
    //    public static Werkstoff Amber = new Werkstoff(Materials.Amber, <GenerationFeatures>, <TypeRef>,31_766+514);
    //    public static Werkstoff Angmallen = new Werkstoff(Materials.Angmallen, <GenerationFeatures>,
    // <TypeRef>,31_766+958);

    //    public static Werkstoff Atlarus = new Werkstoff(Materials.Atlarus, <GenerationFeatures>,
    // <TypeRef>,31_766+965);
    //    public static Werkstoff Blizz = new Werkstoff(Materials.Blizz, <GenerationFeatures>, <TypeRef>,31_766+851);
    //    public static Werkstoff Blueschist = new Werkstoff(Materials.Blueschist, <GenerationFeatures>,
    // <TypeRef>,31_766+852);
    //    public static Werkstoff Bluestone = new Werkstoff(Materials.Bluestone, <GenerationFeatures>,
    // <TypeRef>,31_766+813);
    //    public static Werkstoff Carmot = new Werkstoff(Materials.Carmot, <GenerationFeatures>, <TypeRef>,31_766+962);
    //    public static Werkstoff Celenegil = new Werkstoff(Materials.Celenegil, <GenerationFeatures>,
    // <TypeRef>,31_766+964);
    //    public static Werkstoff CertusQuartz = new Werkstoff(Materials.CertusQuartz, <GenerationFeatures>,
    // <TypeRef>,31_766+516);
    //    public static Werkstoff Ceruclase = new Werkstoff(Materials.Ceruclase, <GenerationFeatures>,
    // <TypeRef>,31_766+952);
    //    public static Werkstoff CobaltHexahydrate = new Werkstoff(Materials.CobaltHexahydrate, <GenerationFeatures>,
    // <TypeRef>,31_766+853);
    //    public static Werkstoff ConstructionFoam = new Werkstoff(Materials.ConstructionFoam, <GenerationFeatures>,
    // <TypeRef>,31_766+854);
    //    public static Werkstoff Chert = new Werkstoff(Materials.Chert, <GenerationFeatures>, <TypeRef>,31_766+857);
    //    public static Werkstoff CrudeOil = new Werkstoff(Materials.CrudeOil, <GenerationFeatures>,
    // <TypeRef>,31_766+858);
    //    public static Werkstoff Dacite = new Werkstoff(Materials.Dacite, <GenerationFeatures>, <TypeRef>,31_766+859);
    //    public static Werkstoff DarkIron = new Werkstoff(Materials.DarkIron, <GenerationFeatures>,
    // <TypeRef>,31_766+342);
    //    public static Werkstoff Desh = new Werkstoff(Materials.Desh, <GenerationFeatures>, <TypeRef>,31_766+884);
    //    public static Werkstoff Dilithium = new Werkstoff(Materials.Dilithium, <GenerationFeatures>,
    // <TypeRef>,31_766+515);
    //    public static Werkstoff Duranium = new Werkstoff(Materials.Duranium, <GenerationFeatures>,
    // <TypeRef>,31_766+328);
    //    public static Werkstoff Eclogite = new Werkstoff(Materials.Eclogite, <GenerationFeatures>,
    // <TypeRef>,31_766+860);
    //    public static Werkstoff ElectrumFlux = new Werkstoff(Materials.ElectrumFlux, <GenerationFeatures>,
    // <TypeRef>,31_766+320);
    //    public static Werkstoff Emery = new Werkstoff(Materials.Emery, <GenerationFeatures>, <TypeRef>,31_766+861);
    //    public static Werkstoff EnderiumBase = new Werkstoff(Materials.EnderiumBase, <GenerationFeatures>,
    // <TypeRef>,31_766+380);
    //    public static Werkstoff Epidote = new Werkstoff(Materials.Epidote, <GenerationFeatures>,
    // <TypeRef>,31_766+862);
    //    public static Werkstoff Eximite = new Werkstoff(Materials.Eximite, <GenerationFeatures>,
    // <TypeRef>,31_766+959);
    //    public static Werkstoff FierySteel = new Werkstoff(Materials.FierySteel, <GenerationFeatures>,
    // <TypeRef>,31_766+346);
    //    public static Werkstoff Firestone = new Werkstoff(Materials.Firestone, <GenerationFeatures>,
    // <TypeRef>,31_766+347);
    //    public static Werkstoff FoolsRuby = new Werkstoff(Materials.FoolsRuby, <GenerationFeatures>,
    // <TypeRef>,31_766+512);
    //    public static Werkstoff Force = new Werkstoff(Materials.Force, <GenerationFeatures>, <TypeRef>,31_766+521);
    //    public static Werkstoff Forcicium = new Werkstoff(Materials.Forcicium, <GenerationFeatures>,
    // <TypeRef>,31_766+518);
    //    public static Werkstoff Forcillium = new Werkstoff(Materials.Forcillium, <GenerationFeatures>,
    // <TypeRef>,31_766+519);
    //    public static Werkstoff Gabbro = new Werkstoff(Materials.Gabbro, <GenerationFeatures>, <TypeRef>,31_766+863);
    //    public static Werkstoff Glowstone = new Werkstoff(Materials.Glowstone, <GenerationFeatures>,
    // <TypeRef>,31_766+811);
    //    public static Werkstoff Gneiss = new Werkstoff(Materials.Gneiss, <GenerationFeatures>, <TypeRef>,31_766+864);
    //    public static Werkstoff Graphite = new Werkstoff(Materials.Graphite, <GenerationFeatures>,
    // <TypeRef>,31_766+865);
    //    public static Werkstoff Graphene = new Werkstoff(Materials.Graphene, <GenerationFeatures>,
    // <TypeRef>,31_766+819);
    //    public static Werkstoff Greenschist = new Werkstoff(Materials.Greenschist, <GenerationFeatures>,
    // <TypeRef>,31_766+866);
    //    public static Werkstoff Greenstone = new Werkstoff(Materials.Greenstone, <GenerationFeatures>,
    // <TypeRef>,31_766+867);
    //    public static Werkstoff Greywacke = new Werkstoff(Materials.Greywacke, <GenerationFeatures>,
    // <TypeRef>,31_766+897);
    //    public static Werkstoff Haderoth = new Werkstoff(Materials.Haderoth, <GenerationFeatures>,
    // <TypeRef>,31_766+963);
    //    public static Werkstoff Hepatizon = new Werkstoff(Materials.Hepatizon, <GenerationFeatures>,
    // <TypeRef>,31_766+957);
    //    public static Werkstoff HSLA = new Werkstoff(Materials.HSLA, <GenerationFeatures>, <TypeRef>,31_766+322);
    //    public static Werkstoff Ignatius = new Werkstoff(Materials.Ignatius, <GenerationFeatures>,
    // <TypeRef>,31_766+950);
    //    public static Werkstoff Infuscolium = new Werkstoff(Materials.Infuscolium, <GenerationFeatures>,
    // <TypeRef>,31_766+490);
    //    public static Werkstoff InfusedGold = new Werkstoff(Materials.InfusedGold, <GenerationFeatures>,
    // <TypeRef>,31_766+323);
    //    public static Werkstoff InfusedAir = new Werkstoff(Materials.InfusedAir, <GenerationFeatures>,
    // <TypeRef>,31_766+540);
    //    public static Werkstoff InfusedFire = new Werkstoff(Materials.InfusedFire, <GenerationFeatures>,
    // <TypeRef>,31_766+541);
    //    public static Werkstoff InfusedEarth = new Werkstoff(Materials.InfusedEarth, <GenerationFeatures>,
    // <TypeRef>,31_766+542);
    //    public static Werkstoff InfusedWater = new Werkstoff(Materials.InfusedWater, <GenerationFeatures>,
    // <TypeRef>,31_766+543);
    //    public static Werkstoff InfusedEntropy = new Werkstoff(Materials.InfusedEntropy, <GenerationFeatures>,
    // <TypeRef>,31_766+544);
    //    public static Werkstoff InfusedOrder = new Werkstoff(Materials.InfusedOrder, <GenerationFeatures>,
    // <TypeRef>,31_766+545);
    //    public static Werkstoff Inolashite = new Werkstoff(Materials.Inolashite, <GenerationFeatures>,
    // <TypeRef>,31_766+954);
    //    public static Werkstoff Jade = new Werkstoff(Materials.Jade, <GenerationFeatures>, <TypeRef>,31_766+537);
    //    public static Werkstoff Jasper = new Werkstoff(Materials.Jasper, <GenerationFeatures>, <TypeRef>,31_766+511);
    //    public static Werkstoff Kalendrite = new Werkstoff(Materials.Kalendrite, <GenerationFeatures>,
    // <TypeRef>,31_766+953);
    //    public static Werkstoff Komatiite = new Werkstoff(Materials.Komatiite, <GenerationFeatures>,
    // <TypeRef>,31_766+869);
    //    public static Werkstoff Lava = new Werkstoff(Materials.Lava, <GenerationFeatures>, <TypeRef>,31_766+700);
    //    public static Werkstoff Lemurite = new Werkstoff(Materials.Lemurite, <GenerationFeatures>,
    // <TypeRef>,31_766+486);
    //    public static Werkstoff MeteoricIron = new Werkstoff(Materials.MeteoricIron, <GenerationFeatures>,
    // <TypeRef>,31_766+340);
    //    public static Werkstoff MeteoricSteel = new Werkstoff(Materials.MeteoricSteel, <GenerationFeatures>,
    // <TypeRef>,31_766+341);
    //    public static Werkstoff Meutoite = new Werkstoff(Materials.Meutoite, <GenerationFeatures>,
    // <TypeRef>,31_766+487);
    //    public static Werkstoff Migmatite = new Werkstoff(Materials.Migmatite, <GenerationFeatures>,
    // <TypeRef>,31_766+872);

    //    public static Werkstoff NetherBrick = new Werkstoff(Materials.NetherBrick, <GenerationFeatures>,
    // <TypeRef>,31_766+814);
    //    public static Werkstoff NetherQuartz = new Werkstoff(Materials.NetherQuartz, <GenerationFeatures>,
    // <TypeRef>,31_766+522);
    //    public static Werkstoff NetherStar = new Werkstoff(Materials.NetherStar, <GenerationFeatures>,
    // <TypeRef>,31_766+506);
    //    public static Werkstoff Oilsands = new Werkstoff(Materials.Oilsands, <GenerationFeatures>,
    // <TypeRef>,31_766+878);
    //    public static Werkstoff Orichalcum = new Werkstoff(Materials.Orichalcum, <GenerationFeatures>,
    // <TypeRef>,31_766+966);
    //    public static Werkstoff Oureclase = new Werkstoff(Materials.Oureclase, <GenerationFeatures>,
    // <TypeRef>,31_766+961);
    //    public static Werkstoff Prometheum = new Werkstoff(Materials.Prometheum, <GenerationFeatures>,
    // <TypeRef>,31_766+960);
    //    public static Werkstoff Quartzite = new Werkstoff(Materials.Quartzite, <GenerationFeatures>,
    // <TypeRef>,31_766+523);
    //    public static Werkstoff Rhyolite = new Werkstoff(Materials.Rhyolite, <GenerationFeatures>,
    // <TypeRef>,31_766+875);
    //    public static Werkstoff Rubracium = new Werkstoff(Materials.Rubracium, <GenerationFeatures>,
    // <TypeRef>,31_766+488);
    //    public static Werkstoff Sanguinite = new Werkstoff(Materials.Sanguinite, <GenerationFeatures>,
    // <TypeRef>,31_766+955);
    //    public static Werkstoff Siltstone = new Werkstoff(Materials.Siltstone, <GenerationFeatures>,
    // <TypeRef>,31_766+876);
    //    public static Werkstoff Tartarite = new Werkstoff(Materials.Tartarite, <GenerationFeatures>,
    // <TypeRef>,31_766+956);
    //    public static Werkstoff UUAmplifier = new Werkstoff(Materials.UUAmplifier, <GenerationFeatures>,
    // <TypeRef>,31_766+721);
    //    public static Werkstoff UUMatter = new Werkstoff(Materials.UUMatter, <GenerationFeatures>,
    // <TypeRef>,31_766+703);
    //    public static Werkstoff Void = new Werkstoff(Materials.Void, <GenerationFeatures>, <TypeRef>,31_766+970);
    //    public static Werkstoff Vulcanite = new Werkstoff(Materials.Vulcanite, <GenerationFeatures>,
    // <TypeRef>,31_766+489);
    //    public static Werkstoff Vyroxeres = new Werkstoff(Materials.Vyroxeres, <GenerationFeatures>,
    // <TypeRef>,31_766+951);
    //    public static Werkstoff BioFuel = new Werkstoff(Materials.BioFuel, <GenerationFeatures>,
    // <TypeRef>,31_766+705);
    //    public static Werkstoff Biomass = new Werkstoff(Materials.Biomass, <GenerationFeatures>,
    // <TypeRef>,31_766+704);
    //    public static CharcoalByproducts      = new MaterialBuilder

    //    public static Werkstoff Chili = new Werkstoff(Materials.Chili, <GenerationFeatures>, <TypeRef>,31_766+895);
    //    public static Werkstoff Chocolate = new Werkstoff(Materials.Chocolate, <GenerationFeatures>,
    // <TypeRef>,31_766+886);
    //    public static Werkstoff CoalFuel = new Werkstoff(Materials.CoalFuel, <GenerationFeatures>,
    // <TypeRef>,31_766+710);
    //    public static Werkstoff Cocoa = new Werkstoff(Materials.Cocoa, <GenerationFeatures>, <TypeRef>,31_766+887);
    //    public static Werkstoff Coffee = new Werkstoff(Materials.Coffee, <GenerationFeatures>, <TypeRef>,31_766+888);
    //    public static Werkstoff Creosote = new Werkstoff(Materials.Creosote, <GenerationFeatures>,
    // <TypeRef>,31_766+712);
    //    public static Werkstoff Ethanol = new Werkstoff(Materials.Ethanol, <GenerationFeatures>,
    // <TypeRef>,31_766+706);
    //    public static FermentedBiomass        = new MaterialBuilder
    //    public static Werkstoff Fuel = new Werkstoff(Materials.Fuel, <GenerationFeatures>, <TypeRef>,31_766+708);
    //    public static Werkstoff Glue = new Werkstoff(Materials.Glue, <GenerationFeatures>, <TypeRef>,31_766+726);
    //    public static Werkstoff Gunpowder = new Werkstoff(Materials.Gunpowder, <GenerationFeatures>,
    // <TypeRef>,31_766+800);
    //    public static Werkstoff FryingOilHot = new Werkstoff(Materials.FryingOilHot, <GenerationFeatures>,
    // <TypeRef>,31_766+727);
    //    public static Werkstoff Honey = new Werkstoff(Materials.Honey, <GenerationFeatures>, <TypeRef>,31_766+725);
    //    public static Werkstoff Lubricant = new Werkstoff(Materials.Lubricant, <GenerationFeatures>,
    // <TypeRef>,31_766+724);
    //    public static Werkstoff McGuffium239 = new Werkstoff(Materials.McGuffium239, <GenerationFeatures>,
    // <TypeRef>,31_766+999);
    //    public static Werkstoff MeatRaw = new Werkstoff(Materials.MeatRaw, <GenerationFeatures>,
    // <TypeRef>,31_766+892);
    //    public static Werkstoff MeatCooked = new Werkstoff(Materials.MeatCooked, <GenerationFeatures>,
    // <TypeRef>,31_766+893);
    //    public static Werkstoff Milk = new Werkstoff(Materials.Milk, <GenerationFeatures>, <TypeRef>,31_766+885);
    //    public static Werkstoff Oil = new Werkstoff(Materials.Oil, <GenerationFeatures>, <TypeRef>,31_766+707);
    //    public static Werkstoff Paper = new Werkstoff(Materials.Paper, <GenerationFeatures>, <TypeRef>,31_766+879);
    //    public static Werkstoff RareEarth = new Werkstoff(Materials.RareEarth, <GenerationFeatures>,
    // <TypeRef>,31_766+891);
    //    public static Werkstoff Reinforced = new Werkstoff(Materials.Reinforced, <GenerationFeatures>,
    // <TypeRef>,31_766+383);
    //    public static Werkstoff SeedOil = new Werkstoff(Materials.SeedOil, <GenerationFeatures>,
    // <TypeRef>,31_766+713);
    //    public static Werkstoff SeedOilHemp = new Werkstoff(Materials.SeedOilHemp, <GenerationFeatures>,
    // <TypeRef>,31_766+722);
    //    public static Werkstoff SeedOilLin = new Werkstoff(Materials.SeedOilLin, <GenerationFeatures>,
    // <TypeRef>,31_766+723);
    //    public static Werkstoff Stone = new Werkstoff(Materials.Stone, <GenerationFeatures>, <TypeRef>,31_766+299);
    //    public static Werkstoff Unstable = new Werkstoff(Materials.Unstable, <GenerationFeatures>,
    // <TypeRef>,31_766+396);
    //    public static Vinegar                 = new MaterialBuilder
    //    public static Werkstoff Wheat = new Werkstoff(Materials.Wheat, <GenerationFeatures>, <TypeRef>,31_766+881);
    //    public static WoodGas                 = new MaterialBuilder
    //    public static WoodTar                 = new MaterialBuilder
    //    public static WoodVinegar             = new MaterialBuilder

    //    public static Werkstoff Sunnarium = new Werkstoff(Materials.Sunnarium, <GenerationFeatures>,
    // <TypeRef>,31_766+318);
    //    public static Werkstoff Endstone = new Werkstoff(Materials.Endstone, <GenerationFeatures>,
    // <TypeRef>,31_766+808);
    //    public static Werkstoff Netherrack = new Werkstoff(Materials.Netherrack, <GenerationFeatures>,
    // <TypeRef>,31_766+807);
    //    public static Werkstoff Methane = new Werkstoff(Materials.Methane, <GenerationFeatures>,
    // <TypeRef>,31_766+715);
    //    public static Werkstoff CarbonDioxide = new Werkstoff(Materials.CarbonDioxide, <GenerationFeatures>,
    // <TypeRef>,31_766+497);
    //    public static Werkstoff NobleGases = new Werkstoff(Materials.NobleGases, <GenerationFeatures>,
    // <TypeRef>,31_766+496);
    //    public static Werkstoff LiquidAir = new Werkstoff(Materials.LiquidAir, <GenerationFeatures>,
    // <TypeRef>,31_766+495);
    //    public static Werkstoff LiquidNitrogen = new Werkstoff(Materials.LiquidNitrogen, <GenerationFeatures>,
    // <TypeRef>,31_766+494);
    //    public static Werkstoff LiquidOxygen = new Werkstoff(Materials.LiquidOxygen, <GenerationFeatures>,
    // <TypeRef>,31_766+493);
    //    public static Werkstoff Almandine = new Werkstoff(Materials.Almandine, <GenerationFeatures>,
    // <TypeRef>,31_766+820);
    //    public static Werkstoff Andradite = new Werkstoff(Materials.Andradite, <GenerationFeatures>,
    // <TypeRef>,31_766+821);

    //    public static Werkstoff Asbestos = new Werkstoff(Materials.Asbestos, <GenerationFeatures>,
    // <TypeRef>,31_766+946);
    //    public static Werkstoff Ash = new Werkstoff(Materials.Ash, <GenerationFeatures>, <TypeRef>,31_766+815);
    //    public static Werkstoff BandedIron = new Werkstoff(Materials.BandedIron, <GenerationFeatures>,
    // <TypeRef>,31_766+917);
    //    public static Werkstoff BatteryAlloy = new Werkstoff(Materials.BatteryAlloy, <GenerationFeatures>,
    // <TypeRef>,31_766+315);
    //    public static Werkstoff BlueTopaz = new Werkstoff(Materials.BlueTopaz, <GenerationFeatures>,
    // <TypeRef>,31_766+513);
    //    public static Werkstoff Bone = new Werkstoff(Materials.Bone, <GenerationFeatures>, <TypeRef>,31_766+806);

    //    public static Werkstoff BrownLimonite = new Werkstoff(Materials.BrownLimonite, <GenerationFeatures>,
    // <TypeRef>,31_766+930);
    //    public static Werkstoff Calcite = new Werkstoff(Materials.Calcite, <GenerationFeatures>,
    // <TypeRef>,31_766+823);
    //    public static Werkstoff Cassiterite = new Werkstoff(Materials.Cassiterite, <GenerationFeatures>,
    // <TypeRef>,31_766+824);
    //    public static Werkstoff CassiteriteSand = new Werkstoff(Materials.CassiteriteSand, <GenerationFeatures>,
    // <TypeRef>,31_766+937);
    //    public static Werkstoff Chalcopyrite = new Werkstoff(Materials.Chalcopyrite, <GenerationFeatures>,
    // <TypeRef>,31_766+855);
    //    public static Werkstoff Charcoal = new Werkstoff(Materials.Charcoal, <GenerationFeatures>,
    // <TypeRef>,31_766+536);
    //    public static Werkstoff Chromite = new Werkstoff(Materials.Chromite, <GenerationFeatures>,
    // <TypeRef>,31_766+825);
    //    public static Werkstoff ChromiumDioxide = new Werkstoff(Materials.ChromiumDioxide, <GenerationFeatures>,
    // <TypeRef>,31_766+361);
    //    public static Werkstoff Cinnabar = new Werkstoff(Materials.Cinnabar, <GenerationFeatures>,
    // <TypeRef>,31_766+826);
    //    public static Werkstoff Water = new Werkstoff(Materials.Water, <GenerationFeatures>, <TypeRef>,31_766+701);
    //    public static Werkstoff Clay = new Werkstoff(Materials.Clay, <GenerationFeatures>, <TypeRef>,31_766+805);
    //    public static Werkstoff Coal = new Werkstoff(Materials.Coal, <GenerationFeatures>, <TypeRef>,31_766+535);
    //    public static Werkstoff Cobaltite = new Werkstoff(Materials.Cobaltite, <GenerationFeatures>,
    // <TypeRef>,31_766+827);
    //    public static Werkstoff Cooperite = new Werkstoff(Materials.Cooperite, <GenerationFeatures>,
    // <TypeRef>,31_766+828);
    //    public static Werkstoff Cupronickel = new Werkstoff(Materials.Cupronickel, <GenerationFeatures>,
    // <TypeRef>,31_766+310);
    //    public static Werkstoff DarkAsh = new Werkstoff(Materials.DarkAsh, <GenerationFeatures>,
    // <TypeRef>,31_766+816);
    //    public static Werkstoff DeepIron = new Werkstoff(Materials.DeepIron, <GenerationFeatures>,
    // <TypeRef>,31_766+829);
    //    public static Werkstoff Diamond = new Werkstoff(Materials.Diamond, <GenerationFeatures>,
    // <TypeRef>,31_766+500);
    //    public static Werkstoff Electrum = new Werkstoff(Materials.Electrum, <GenerationFeatures>,
    // <TypeRef>,31_766+303);
    //    public static Werkstoff Emerald = new Werkstoff(Materials.Emerald, <GenerationFeatures>,
    // <TypeRef>,31_766+501);
    //    public static Werkstoff Galena = new Werkstoff(Materials.Galena, <GenerationFeatures>, <TypeRef>,31_766+830);
    //    public static Werkstoff Garnierite = new Werkstoff(Materials.Garnierite, <GenerationFeatures>,
    // <TypeRef>,31_766+906);
    //    public static Werkstoff Glyceryl = new Werkstoff(Materials.Glyceryl, <GenerationFeatures>,
    // <TypeRef>,31_766+714);
    //    public static Werkstoff GreenSapphire = new Werkstoff(Materials.GreenSapphire, <GenerationFeatures>,
    // <TypeRef>,31_766+504);
    //    public static Werkstoff Grossular = new Werkstoff(Materials.Grossular, <GenerationFeatures>,
    // <TypeRef>,31_766+831);
    //    public static Werkstoff HolyWater = new Werkstoff(Materials.HolyWater, <GenerationFeatures>,
    // <TypeRef>,31_766+729);
    //    public static Werkstoff Ice = new Werkstoff(Materials.Ice, <GenerationFeatures>, <TypeRef>,31_766+702);
    //    public static Werkstoff Ilmenite = new Werkstoff(Materials.Ilmenite, <GenerationFeatures>,
    // <TypeRef>,31_766+918);
    //    public static Werkstoff Rutile = new Werkstoff(Materials.Rutile, <GenerationFeatures>, <TypeRef>,31_766+375);
    //    public static Werkstoff Bauxite = new Werkstoff(Materials.Bauxite, <GenerationFeatures>,
    // <TypeRef>,31_766+822);
    //    public static Werkstoff Titaniumtetrachloride = new Werkstoff(Materials.Titaniumtetrachloride,
    // <GenerationFeatures>, <TypeRef>,31_766+376);
    //    public static Werkstoff Magnesiumchloride = new Werkstoff(Materials.Magnesiumchloride, <GenerationFeatures>,
    // <TypeRef>,31_766+377);
    //    public static Werkstoff Invar = new Werkstoff(Materials.Invar, <GenerationFeatures>, <TypeRef>,31_766+302);
    //    public static Werkstoff Kanthal = new Werkstoff(Materials.Kanthal, <GenerationFeatures>,
    // <TypeRef>,31_766+312);
    //    public static Werkstoff Lazurite = new Werkstoff(Materials.Lazurite, <GenerationFeatures>,
    // <TypeRef>,31_766+524);
    //    public static Werkstoff Magnalium = new Werkstoff(Materials.Magnalium, <GenerationFeatures>,
    // <TypeRef>,31_766+313);
    //    public static Werkstoff Magnesite = new Werkstoff(Materials.Magnesite, <GenerationFeatures>,
    // <TypeRef>,31_766+908);
    //    public static Werkstoff Magnetite = new Werkstoff(Materials.Magnetite, <GenerationFeatures>,
    // <TypeRef>,31_766+870);
    //    public static Werkstoff Molybdenite = new Werkstoff(Materials.Molybdenite, <GenerationFeatures>,
    // <TypeRef>,31_766+942);
    //    public static Werkstoff Nichrome = new Werkstoff(Materials.Nichrome, <GenerationFeatures>,
    // <TypeRef>,31_766+311);
    //    public static Werkstoff NiobiumNitride = new Werkstoff(Materials.NiobiumNitride, <GenerationFeatures>,
    // <TypeRef>,31_766+359);
    //    public static Werkstoff NiobiumTitanium = new Werkstoff(Materials.NiobiumTitanium, <GenerationFeatures>,
    // <TypeRef>,31_766+360);
    //    public static Werkstoff NitroCarbon = new Werkstoff(Materials.NitroCarbon, <GenerationFeatures>,
    // <TypeRef>,31_766+716);
    //    public static Werkstoff NitrogenDioxide = new Werkstoff(Materials.NitrogenDioxide, <GenerationFeatures>,
    // <TypeRef>,31_766+717);
    //    public static Werkstoff Obsidian = new Werkstoff(Materials.Obsidian, <GenerationFeatures>,
    // <TypeRef>,31_766+804);
    //    public static Werkstoff Phosphate = new Werkstoff(Materials.Phosphate, <GenerationFeatures>,
    // <TypeRef>,31_766+833);
    //    public static Werkstoff PigIron = new Werkstoff(Materials.PigIron, <GenerationFeatures>,
    // <TypeRef>,31_766+307);

    //    public static Polydimethylsiloxane    = new MaterialBuilder
    //    public static Werkstoff Silicone = new Werkstoff(Materials.Silicone, <GenerationFeatures>,
    // <TypeRef>,31_766+471);
    //    public static Werkstoff Polycaprolactam = new Werkstoff(Materials.Polycaprolactam, <GenerationFeatures>,
    // <TypeRef>,31_766+472);

    //    public static Werkstoff Powellite = new Werkstoff(Materials.Powellite, <GenerationFeatures>,
    // <TypeRef>,31_766+883);
    //    public static Werkstoff Pumice = new Werkstoff(Materials.Pumice, <GenerationFeatures>, <TypeRef>,31_766+926);
    //    public static Werkstoff Pyrite = new Werkstoff(Materials.Pyrite, <GenerationFeatures>, <TypeRef>,31_766+834);
    //    public static Werkstoff Pyrolusite = new Werkstoff(Materials.Pyrolusite, <GenerationFeatures>,
    // <TypeRef>,31_766+943);
    //    public static Werkstoff Pyrope = new Werkstoff(Materials.Pyrope, <GenerationFeatures>, <TypeRef>,31_766+835);
    //    public static Werkstoff RockSalt = new Werkstoff(Materials.RockSalt, <GenerationFeatures>,
    // <TypeRef>,31_766+944);
    //    public static Werkstoff Rubber = new Werkstoff(Materials.Rubber, <GenerationFeatures>, <TypeRef>,31_766+880);
    //    public static Werkstoff RawRubber = new Werkstoff(Materials.RawRubber, <GenerationFeatures>,
    // <TypeRef>,31_766+896);
    //    public static Werkstoff Ruby = new Werkstoff(Materials.Ruby, <GenerationFeatures>, <TypeRef>,31_766+502);
    //    public static Werkstoff Salt = new Werkstoff(Materials.Salt, <GenerationFeatures>, <TypeRef>,31_766+817);
    //    public static Werkstoff Saltpeter = new Werkstoff(Materials.Saltpeter, <GenerationFeatures>,
    // <TypeRef>,31_766+836);
    //    public static Werkstoff Sapphire = new Werkstoff(Materials.Sapphire, <GenerationFeatures>,
    // <TypeRef>,31_766+503);
    //    public static Werkstoff Scheelite = new Werkstoff(Materials.Scheelite, <GenerationFeatures>,
    // <TypeRef>,31_766+910);
    //    public static Werkstoff SiliconDioxide = new Werkstoff(Materials.SiliconDioxide, <GenerationFeatures>,
    // <TypeRef>,31_766+837);
    //    public static Werkstoff Snow = new Werkstoff(Materials.Snow, <GenerationFeatures>, <TypeRef>,31_766+728);
    //    public static Werkstoff Sodalite = new Werkstoff(Materials.Sodalite, <GenerationFeatures>,
    // <TypeRef>,31_766+525);
    //    public static Werkstoff SodiumPersulfate = new Werkstoff(Materials.SodiumPersulfate, <GenerationFeatures>,
    // <TypeRef>,31_766+718);
    //    public static Werkstoff SodiumSulfide = new Werkstoff(Materials.SodiumSulfide, <GenerationFeatures>,
    // <TypeRef>,31_766+719);
    //    public static Werkstoff HydricSulfide = new Werkstoff(Materials.HydricSulfide, <GenerationFeatures>,
    // <TypeRef>,31_766+460);
    //    public static Werkstoff OilHeavy = new Werkstoff(Materials.OilHeavy, <GenerationFeatures>,
    // <TypeRef>,31_766+730);
    //    public static Werkstoff OilMedium = new Werkstoff(Materials.OilMedium, <GenerationFeatures>,
    // <TypeRef>,31_766+731);
    //    public static Werkstoff OilLight = new Werkstoff(Materials.OilLight, <GenerationFeatures>,
    // <TypeRef>,31_766+732);
    //    public static Werkstoff NatruralGas = new Werkstoff(Materials.NatruralGas, <GenerationFeatures>,
    // <TypeRef>,31_766+733);
    //    public static Werkstoff SulfuricGas = new Werkstoff(Materials.SulfuricGas, <GenerationFeatures>,
    // <TypeRef>,31_766+734);
    //    public static Werkstoff Gas = new Werkstoff(Materials.Gas, <GenerationFeatures>, <TypeRef>,31_766+735);
    //    public static Werkstoff SulfuricNaphtha = new Werkstoff(Materials.SulfuricNaphtha, <GenerationFeatures>,
    // <TypeRef>,31_766+736);
    //    public static Werkstoff SulfuricLightFuel = new Werkstoff(Materials.SulfuricLightFuel, <GenerationFeatures>,
    // <TypeRef>,31_766+737);
    //    public static Werkstoff SulfuricHeavyFuel = new Werkstoff(Materials.SulfuricHeavyFuel, <GenerationFeatures>,
    // <TypeRef>,31_766+738);
    //    public static Werkstoff Naphtha = new Werkstoff(Materials.Naphtha, <GenerationFeatures>,
    // <TypeRef>,31_766+739);
    //    public static Werkstoff LightFuel = new Werkstoff(Materials.LightFuel, <GenerationFeatures>,
    // <TypeRef>,31_766+740);
    //    public static Werkstoff HeavyFuel = new Werkstoff(Materials.HeavyFuel, <GenerationFeatures>,
    // <TypeRef>,31_766+741);
    //    public static Werkstoff LPG = new Werkstoff(Materials.LPG, <GenerationFeatures>, <TypeRef>,31_766+742);
    //    public static FluidNaquadahFuel       = new MaterialBuilder
    //    public static EnrichedNaquadria       = new MaterialBuilder
    //    public static ReinforceGlass          = new MaterialBuilder
    //    public static BioMediumRaw            = new MaterialBuilder
    //    public static BioMediumSterilized     = new MaterialBuilder
    //    public static Chlorobenzene             = new MaterialBuild
    //    public static DilutedHydrochloricAcid   = new MaterialBuild
    //    public static Pyrochlore                = new MaterialBuild
    //    public static GrowthMediumRaw           = new MaterialBuild
    //    public static GrowthMediumSterilized    = new MaterialBuild
    //    public static FerriteMixture            = new MaterialBuild
    //    public static NickelZincFerrite         = new MaterialBuild
    //    public static Massicot                  = new MaterialBuild
    //    public static ArsenicTrioxide           = new MaterialBuild
    //    public static CobaltOxide               = new MaterialBuild
    //    public static Zincite                   = new MaterialBuild
    //    public static AntimonyTrioxide          = new MaterialBuild
    //    public static CupricOxide               = new MaterialBuild
    //    public static Ferrosilite               = new MaterialBuild
    //    public static Magnesia                  = new MaterialBuild
    //    public static Quicklime                 = new MaterialBuild
    //    public static Potash                    = new MaterialBuild
    //    public static SodaAsh                   = new MaterialBuild
    //    public static Brick                     = new MaterialBuild
    //    public static Fireclay                  = new MaterialBuild
    //    public static BioDiesel                 = new MaterialBuild
    //    public static NitrationMixture          = new MaterialBuild
    //    public static Glycerol                  = new MaterialBuild
    //    public static SodiumBisulfate           = new MaterialBuild
    //    public static PolyphenyleneSulfide      = new MaterialBuild
    //    public static Dichlorobenzene           = new MaterialBuild
    //    public static Polystyrene               = new MaterialBuild
    //    public static Styrene                   = new MaterialBuild
    //    public static Isoprene                  = new MaterialBuild
    //    public static Tetranitromethane         = new MaterialBuild
    //    public static Ethenone                  = new MaterialBuild
    //    public static Ethane                    = new MaterialBuild
    //    public static Propane                   = new MaterialBuild
    //    public static Butane                    = new MaterialBuild
    //    public static Butene                    = new MaterialBuild
    //    public static Butadiene                 = new MaterialBuild
    //    public static RawStyreneButadieneRubber = new MaterialBuild
    //    public static StyreneButadieneRubber    = new MaterialBuild
    //    public static Toluene                   = new MaterialBuild
    //    public static Epichlorohydrin           = new MaterialBuild
    //    public static PolyvinylChloride         = new MaterialBuild
    //    public static VinylChloride             = new MaterialBuild
    //    public static SulfurDioxide             = new MaterialBuild
    //    public static SulfurTrioxide            = new MaterialBuild
    //    public static NitricAcid                = new MaterialBuild
    //    public static Dimethylhydrazine         = new MaterialBuild
    //    public static Chloramine                = new MaterialBuild
    //    public static Dimethylamine             = new MaterialBuild
    //    public static DinitrogenTetroxide       = new MaterialBuild
    //    public static NitricOxide               = new MaterialBuild
    //    public static Ammonia                   = new MaterialBuild
    //    public static Dimethyldichlorosilane    = new MaterialBuild
    //    public static Chloromethane             = new MaterialBuild
    //    public static PhosphorousPentoxide      = new MaterialBuild
    //    public static Tetrafluoroethylene       = new MaterialBuild
    //    public static HydrofluoricAcid          = new MaterialBuild
    //    public static Chloroform                = new MaterialBuild
    //    public static BisphenolA                = new MaterialBuild
    //    public static AceticAcid                = new MaterialBuild
    //    public static CalciumAcetateSolution    = new MaterialBuild
    //    public static Acetone                   = new MaterialBuild
    //    public static Methanol                  = new MaterialBuild
    //    public static CarbonMonoxide            = new MaterialBuild
    //    public static MetalMixture              = new MaterialBuild
    //    public static Ethylene                  = new MaterialBuild
    //    public static Propene                   = new MaterialBuild
    //    public static VinylAcetate              = new MaterialBuild
    //    public static PolyvinylAcetate          = new MaterialBuild
    //    public static MethylAcetate             = new MaterialBuild
    //    public static AllylChloride             = new MaterialBuild
    //    public static HydrochloricAcid          = new MaterialBuild
    //    public static HypochlorousAcid          = new MaterialBuild
    //    public static SodiumHydroxide           = new MaterialBuild
    //    public static Benzene                   = new MaterialBuild
    //    public static Phenol                    = new MaterialBuild
    //    public static Cumene                    = new MaterialBuild
    //    public static PhosphoricAcid            = new MaterialBuild
    //    public static SaltWater                 = new MaterialBuild
    //    public static IronIIIChloride           = new MaterialBuild
    //    public static LifeEssence               = new MaterialBuild
    //    public static Werkstoff SolderingAlloy = new Werkstoff(Materials.SolderingAlloy, <GenerationFeatures>,
    // <TypeRef>,31_766+314);
    //    public static Werkstoff GalliumArsenide = new Werkstoff(Materials.GalliumArsenide, <GenerationFeatures>,
    // <TypeRef>,31_766+980);
    //    public static Werkstoff IndiumGalliumPhosphide = new Werkstoff(Materials.IndiumGalliumPhosphide,
    // <GenerationFeatures>, <TypeRef>,31_766+981);
    //    public static Werkstoff Spessartine = new Werkstoff(Materials.Spessartine, <GenerationFeatures>,
    // <TypeRef>,31_766+838);
    //    public static Werkstoff Sphalerite = new Werkstoff(Materials.Sphalerite, <GenerationFeatures>,
    // <TypeRef>,31_766+839);

    //    public static Werkstoff Stibnite = new Werkstoff(Materials.Stibnite, <GenerationFeatures>,
    // <TypeRef>,31_766+945);
    //    public static Werkstoff SulfuricAcid = new Werkstoff(Materials.SulfuricAcid, <GenerationFeatures>,
    // <TypeRef>,31_766+720);
    //    public static Werkstoff Tanzanite = new Werkstoff(Materials.Tanzanite, <GenerationFeatures>,
    // <TypeRef>,31_766+508);
    //    public static Werkstoff Tetrahedrite = new Werkstoff(Materials.Tetrahedrite, <GenerationFeatures>,
    // <TypeRef>,31_766+840);
    //    public static Werkstoff TinAlloy = new Werkstoff(Materials.TinAlloy, <GenerationFeatures>,
    // <TypeRef>,31_766+363);
    //    public static Werkstoff Topaz = new Werkstoff(Materials.Topaz, <GenerationFeatures>, <TypeRef>,31_766+507);
    //    public static Werkstoff Tungstate = new Werkstoff(Materials.Tungstate, <GenerationFeatures>,
    // <TypeRef>,31_766+841);

    //    public static Werkstoff Uraninite = new Werkstoff(Materials.Uraninite, <GenerationFeatures>,
    // <TypeRef>,31_766+922);
    //    public static Werkstoff Uvarovite = new Werkstoff(Materials.Uvarovite, <GenerationFeatures>,
    // <TypeRef>,31_766+842);
    //    public static Werkstoff VanadiumGallium = new Werkstoff(Materials.VanadiumGallium, <GenerationFeatures>,
    // <TypeRef>,31_766+357);

    //    public static Werkstoff Wulfenite = new Werkstoff(Materials.Wulfenite, <GenerationFeatures>,
    // <TypeRef>,31_766+882);
    //    public static Werkstoff YellowLimonite = new Werkstoff(Materials.YellowLimonite, <GenerationFeatures>,
    // <TypeRef>,31_766+931);
    //    public static Werkstoff YttriumBariumCuprate = new Werkstoff(Materials.YttriumBariumCuprate,
    // <GenerationFeatures>, <TypeRef>,31_766+358);

    //    public static Werkstoff LiveRoot = new Werkstoff(Materials.LiveRoot, <GenerationFeatures>,
    // <TypeRef>,31_766+832);
    //    public static Werkstoff IronWood = new Werkstoff(Materials.IronWood, <GenerationFeatures>,
    // <TypeRef>,31_766+338);
    //    public static Werkstoff Glass = new Werkstoff(Materials.Glass, <GenerationFeatures>, <TypeRef>,31_766+890);
    //    public static Werkstoff BorosilicateGlass = new Werkstoff(Materials.BorosilicateGlass,
    // Werkstoff.GenerationFeatures.DISABLED, Werkstoff.Types.COMPOUND, 31_766+611);
    //    public static Werkstoff Perlite = new Werkstoff(Materials.Perlite, <GenerationFeatures>,
    // <TypeRef>,31_766+925);
    //    public static Werkstoff Borax = new Werkstoff(Materials.Borax, <GenerationFeatures>, <TypeRef>,31_766+941);
    //    public static Werkstoff Lignite = new Werkstoff(Materials.Lignite, <GenerationFeatures>,
    // <TypeRef>,31_766+538);
    //    public static Werkstoff Olivine = new Werkstoff(Materials.Olivine, <GenerationFeatures>,
    // <TypeRef>,31_766+505);
    //    public static Werkstoff Opal = new Werkstoff(Materials.Opal, <GenerationFeatures>, <TypeRef>,31_766+510);
    //    public static Werkstoff Amethyst = new Werkstoff(Materials.Amethyst, <GenerationFeatures>,
    // <TypeRef>,31_766+509);
    //    public static Werkstoff Redstone = new Werkstoff(Materials.Redstone, <GenerationFeatures>,
    // <TypeRef>,31_766+810);
    //    public static Werkstoff Lapis = new Werkstoff(Materials.Lapis, <GenerationFeatures>, <TypeRef>,31_766+526);
    //    public static Werkstoff Blaze = new Werkstoff(Materials.Blaze, <GenerationFeatures>, <TypeRef>,31_766+801);
    //    public static Werkstoff EnderPearl = new Werkstoff(Materials.EnderPearl, <GenerationFeatures>,
    // <TypeRef>,31_766+532);
    //    public static Werkstoff EnderEye = new Werkstoff(Materials.EnderEye, <GenerationFeatures>,
    // <TypeRef>,31_766+533);
    //    public static Werkstoff Flint = new Werkstoff(Materials.Flint, <GenerationFeatures>, <TypeRef>,31_766+802);
    //    public static Werkstoff Diatomite = new Werkstoff(Materials.Diatomite, <GenerationFeatures>,
    // <TypeRef>,31_766+948);
    //    public static Werkstoff VolcanicAsh = new Werkstoff(Materials.VolcanicAsh, <GenerationFeatures>,
    // <TypeRef>,31_766+940);
    //    public static Werkstoff Niter = new Werkstoff(Materials.Niter, <GenerationFeatures>, <TypeRef>,31_766+531);
    //    public static Werkstoff Pyrotheum = new Werkstoff(Materials.Pyrotheum, <GenerationFeatures>,
    // <TypeRef>,31_766+843);
    //    public static Werkstoff Cryotheum = new Werkstoff(Materials.Cryotheum, <GenerationFeatures>,
    // <TypeRef>,31_766+898);
    //    public static Werkstoff HydratedCoal = new Werkstoff(Materials.HydratedCoal, <GenerationFeatures>,
    // <TypeRef>,31_766+818);
    //    public static Werkstoff Apatite = new Werkstoff(Materials.Apatite, <GenerationFeatures>,
    // <TypeRef>,31_766+530);
    //    public static Werkstoff Alumite = new Werkstoff(Materials.Alumite, <GenerationFeatures>,
    // <TypeRef>,31_766+400);
    //    public static Werkstoff Manyullyn = new Werkstoff(Materials.Manyullyn, <GenerationFeatures>,
    // <TypeRef>,31_766+386);
    //    public static Werkstoff Steeleaf = new Werkstoff(Materials.Steeleaf, <GenerationFeatures>,
    // <TypeRef>,31_766+339);
    //    public static Werkstoff Knightmetal = new Werkstoff(Materials.Knightmetal, <GenerationFeatures>,
    // <TypeRef>,31_766+362);

    //    public static Werkstoff NitroFuel = new Werkstoff(Materials.NitroFuel, <GenerationFeatures>,
    // <TypeRef>,31_766+709);
    //    public static Werkstoff RedAlloy = new Werkstoff(Materials.RedAlloy, <GenerationFeatures>,
    // <TypeRef>,31_766+308);
    //    public static Werkstoff CobaltBrass = new Werkstoff(Materials.CobaltBrass, <GenerationFeatures>,
    // <TypeRef>,31_766+343);
    //    public static Werkstoff TricalciumPhosphate = new Werkstoff(Materials.TricalciumPhosphate,
    // <GenerationFeatures>, <TypeRef>,31_766+534);
    //    public static Werkstoff Basalt = new Werkstoff(Materials.Basalt, <GenerationFeatures>, <TypeRef>,31_766+844);
    //    public static Werkstoff GarnetRed = new Werkstoff(Materials.GarnetRed, <GenerationFeatures>,
    // <TypeRef>,31_766+527);
    //    public static Werkstoff GarnetYellow = new Werkstoff(Materials.GarnetYellow, <GenerationFeatures>,
    // <TypeRef>,31_766+528);
    //    public static Werkstoff Marble = new Werkstoff(Materials.Marble, <GenerationFeatures>, <TypeRef>,31_766+845);
    //    public static Werkstoff Sugar = new Werkstoff(Materials.Sugar, <GenerationFeatures>, <TypeRef>,31_766+803);
    //    public static Werkstoff Thaumium = new Werkstoff(Materials.Thaumium, <GenerationFeatures>,
    // <TypeRef>,31_766+330);
    //    public static Werkstoff Vinteum = new Werkstoff(Materials.Vinteum, <GenerationFeatures>,
    // <TypeRef>,31_766+529);
    //    public static Werkstoff Redrock = new Werkstoff(Materials.Redrock, <GenerationFeatures>,
    // <TypeRef>,31_766+846);
    //    public static Werkstoff PotassiumFeldspar = new Werkstoff(Materials.PotassiumFeldspar, <GenerationFeatures>,
    // <TypeRef>,31_766+847);
    //    public static Werkstoff Biotite = new Werkstoff(Materials.Biotite, <GenerationFeatures>,
    // <TypeRef>,31_766+848);
    //    public static Werkstoff GraniteBlack = new Werkstoff(Materials.GraniteBlack, <GenerationFeatures>,
    // <TypeRef>,31_766+849);
    //    public static Werkstoff GraniteRed = new Werkstoff(Materials.GraniteRed, <GenerationFeatures>,
    // <TypeRef>,31_766+850);
    //    public static Werkstoff Chrysotile = new Werkstoff(Materials.Chrysotile, <GenerationFeatures>,
    // <TypeRef>,31_766+912);
    //    public static Werkstoff Realgar = new Werkstoff(Materials.Realgar, <GenerationFeatures>,
    // <TypeRef>,31_766+913);
    //    public static Werkstoff VanadiumMagnetite = new Werkstoff(Materials.VanadiumMagnetite, <GenerationFeatures>,
    // <TypeRef>,31_766+923);
    //    public static Werkstoff BasalticMineralSand = new Werkstoff(Materials.BasalticMineralSand,
    // <GenerationFeatures>, <TypeRef>,31_766+935);
    //    public static Werkstoff GraniticMineralSand = new Werkstoff(Materials.GraniticMineralSand,
    // <GenerationFeatures>, <TypeRef>,31_766+936);
    //    public static Werkstoff GarnetSand = new Werkstoff(Materials.GarnetSand, <GenerationFeatures>,
    // <TypeRef>,31_766+938);
    //    public static Werkstoff QuartzSand = new Werkstoff(Materials.QuartzSand, <GenerationFeatures>,
    // <TypeRef>,31_766+939);
    //    public static Werkstoff Bastnasite = new Werkstoff(Materials.Bastnasite, <GenerationFeatures>,
    // <TypeRef>,31_766+905);
    //    public static Werkstoff Pentlandite = new Werkstoff(Materials.Pentlandite, <GenerationFeatures>,
    // <TypeRef>,31_766+909);
    //    public static Werkstoff Spodumene = new Werkstoff(Materials.Spodumene, <GenerationFeatures>,
    // <TypeRef>,31_766+920);
    //    public static Werkstoff Pollucite = new Werkstoff(Materials.Pollucite, <GenerationFeatures>,
    // <TypeRef>,31_766+919);
    //    public static Werkstoff Tantalite = new Werkstoff(Materials.Tantalite, <GenerationFeatures>,
    // <TypeRef>,31_766+921);
    //    public static Werkstoff Lepidolite = new Werkstoff(Materials.Lepidolite, <GenerationFeatures>,
    // <TypeRef>,31_766+907);
    //    public static Werkstoff Glauconite = new Werkstoff(Materials.Glauconite, <GenerationFeatures>,
    // <TypeRef>,31_766+933);
    //    public static Werkstoff GlauconiteSand = new Werkstoff(Materials.GlauconiteSand, <GenerationFeatures>,
    // <TypeRef>,31_766+949);
    //    public static Werkstoff Vermiculite = new Werkstoff(Materials.Vermiculite, <GenerationFeatures>,
    // <TypeRef>,31_766+932);
    //    public static Werkstoff Bentonite = new Werkstoff(Materials.Bentonite, <GenerationFeatures>,
    // <TypeRef>,31_766+927);
    //    public static Werkstoff FullersEarth = new Werkstoff(Materials.FullersEarth, <GenerationFeatures>,
    // <TypeRef>,31_766+928);
    //    public static Werkstoff Pitchblende = new Werkstoff(Materials.Pitchblende, <GenerationFeatures>,
    // <TypeRef>,31_766+873);
    //    public static Werkstoff Monazite = new Werkstoff(Materials.Monazite, <GenerationFeatures>,
    // <TypeRef>,31_766+520);
    //    public static Werkstoff Malachite = new Werkstoff(Materials.Malachite, <GenerationFeatures>,
    // <TypeRef>,31_766+871);
    //    public static Werkstoff Mirabilite = new Werkstoff(Materials.Mirabilite, <GenerationFeatures>,
    // <TypeRef>,31_766+900);
    //    public static Werkstoff Mica = new Werkstoff(Materials.Mica, <GenerationFeatures>, <TypeRef>,31_766+901);
    //    public static Werkstoff Trona = new Werkstoff(Materials.Trona, <GenerationFeatures>, <TypeRef>,31_766+903);
    //    public static Werkstoff Barite = new Werkstoff(Materials.Barite, <GenerationFeatures>, <TypeRef>,31_766+904);
    //    public static Werkstoff Gypsum = new Werkstoff(Materials.Gypsum, <GenerationFeatures>, <TypeRef>,31_766+934);
    //    public static Werkstoff Alunite = new Werkstoff(Materials.Alunite, <GenerationFeatures>,
    // <TypeRef>,31_766+911);
    //    public static Werkstoff Dolomite = new Werkstoff(Materials.Dolomite, <GenerationFeatures>,
    // <TypeRef>,31_766+914);
    //    public static Werkstoff Wollastonite = new Werkstoff(Materials.Wollastonite, <GenerationFeatures>,
    // <TypeRef>,31_766+915);
    //    public static Werkstoff Zeolite = new Werkstoff(Materials.Zeolite, <GenerationFeatures>,
    // <TypeRef>,31_766+916);
    //    public static Werkstoff Kyanite = new Werkstoff(Materials.Kyanite, <GenerationFeatures>,
    // <TypeRef>,31_766+924);
    //    public static Werkstoff Kaolinite = new Werkstoff(Materials.Kaolinite, <GenerationFeatures>,
    // <TypeRef>,31_766+929);
    //    public static Werkstoff Talc = new Werkstoff(Materials.Talc, <GenerationFeatures>, <TypeRef>,31_766+902);
    //    public static Werkstoff Soapstone = new Werkstoff(Materials.Soapstone, <GenerationFeatures>,
    // <TypeRef>,31_766+877);
    //    public static Werkstoff Concrete = new Werkstoff(Materials.Concrete, <GenerationFeatures>,
    // <TypeRef>,31_766+947);
    //    public static Werkstoff IronMagnetic = new Werkstoff(Materials.IronMagnetic, <GenerationFeatures>,
    // <TypeRef>,31_766+354);
    //    public static Werkstoff SteelMagnetic = new Werkstoff(Materials.SteelMagnetic, <GenerationFeatures>,
    // <TypeRef>,31_766+355);
    //    public static Werkstoff NeodymiumMagnetic = new Werkstoff(Materials.NeodymiumMagnetic, <GenerationFeatures>,
    // <TypeRef>,31_766+356);
    //    public static Werkstoff SamariumMagnetic = new Werkstoff(Materials.SamariumMagnetic, <GenerationFeatures>,
    // <TypeRef>,31_766+399);

    //    public static DilutedSulfuricAcid     = new MaterialBuilder(31_766+640)
    //    public static Werkstoff EpoxidFiberReinforced = new Werkstoff(Materials.EpoxidFiberReinforced,
    // <GenerationFeatures>, <TypeRef>,31_766+610);
    //    public static PotassiumNitrade     = new MaterialBuilder(59
    //    public static ChromiumTrioxide = new MaterialBuilder(31_766+591)
    //    public static Nitrochlorobenzene = new MaterialBuilder(31_766+592)
    //    public static Dimethylbenzene = new MaterialBuilder(31_766+593)
    //    public static Potassiumdichromate = new MaterialBuilder(594
    //    public static PhthalicAcid = new MaterialBuilder(31_766+595)
    //    public static Dichlorobenzidine = new MaterialBuilder(31_766+596)
    //    public static Diaminobenzidin = new MaterialBuilder(31_766+597)
    //    public static Diphenylisophthalate = new MaterialBuilder(59
    //    public static Werkstoff Polybenzimidazole = new Werkstoff(Materials.Polybenzimidazole, <GenerationFeatures>,
    // <TypeRef>,  31_766+599);
    //    public static NitrousOxide       = new MaterialBuilder(31_766+993)
    //    public static AntiKnock          = new MaterialBuilder(31_766+994)
    //    public static Octane             = new MaterialBuilder(31_766+995)
    //    public static GasolineRaw        = new MaterialBuilder(31_766+996)
    //    public static GasolineRegular    = new MaterialBuilder(31_766+997)
    //    public static GasolinePremium    = new MaterialBuilder(31_766+998)
    //    public static Werkstoff Electrotine = new Werkstoff(Materials.Electrotine, <GenerationFeatures>,
    // <TypeRef>,31_766+812);
    //    public static  Galgadorian = new Werkstoff(Materials.Galgadorian, <GenerationFeatures>, <TypeRef>,31_766+384);
    //    public static Werkstoff EnhancedGalgadorian = new Werkstoff(Materials.EnhancedGalgadorian,
    // <GenerationFeatures>, <TypeRef>,31_766+385);
    //    public static Werkstoff BloodInfusedIron = new Werkstoff(Materials.BloodInfusedIron, <GenerationFeatures>,
    // <TypeRef>,31_766+977);
    //    public static Werkstoff Shadow = new Werkstoff(Materials.Shadow, <GenerationFeatures>, <TypeRef>,31_766+368);
    //    public static Werkstoff Ledox = new Werkstoff(Materials.Ledox, <GenerationFeatures>, <TypeRef>,31_766+390);
    //    public static Werkstoff Quantium = new Werkstoff(Materials.Quantium, <GenerationFeatures>,
    // <TypeRef>,31_766+391);
    //    public static Werkstoff Mytryl = new Werkstoff(Materials.Mytryl, <GenerationFeatures>, <TypeRef>,31_766+387);
    //    public static Werkstoff BlackPlutonium = new Werkstoff(Materials.BlackPlutonium, <GenerationFeatures>,
    // <TypeRef>,31_766+388);
    //    public static Werkstoff CallistoIce = new Werkstoff(Materials.CallistoIce, <GenerationFeatures>,
    // <TypeRef>,31_766+389);
    //    public static Werkstoff Duralumin = new Werkstoff(Materials.Duralumin, <GenerationFeatures>,
    // <TypeRef>,31_766+392);
    //    public static Werkstoff Oriharukon = new Werkstoff(Materials.Oriharukon, <GenerationFeatures>,
    // <TypeRef>,31_766+393);
    //    public static Werkstoff MysteriousCrystal = new Werkstoff(Materials.MysteriousCrystal, <GenerationFeatures>,
    // <TypeRef>,31_766+398);
    //    public static Werkstoff RedstoneAlloy = new Werkstoff(Materials.RedstoneAlloy, <GenerationFeatures>,
    // <TypeRef>,31_766+381);
    //    public static Werkstoff Soularium = new Werkstoff(Materials.Soularium, <GenerationFeatures>,
    // <TypeRef>,31_766+379);
    //    public static Werkstoff ConductiveIron = new Werkstoff(Materials.ConductiveIron, <GenerationFeatures>,
    // <TypeRef>,31_766+369);
    //    public static Werkstoff ElectricalSteel = new Werkstoff(Materials.ElectricalSteel, <GenerationFeatures>,
    // <TypeRef>,31_766+365);
    //    public static Werkstoff EnergeticAlloy = new Werkstoff(Materials.EnergeticAlloy, <GenerationFeatures>,
    // <TypeRef>,31_766+366);
    //    public static Werkstoff VibrantAlloy = new Werkstoff(Materials.VibrantAlloy, <GenerationFeatures>,
    // <TypeRef>,31_766+367);
    //    public static Werkstoff PulsatingIron = new Werkstoff(Materials.PulsatingIron, <GenerationFeatures>,
    // <TypeRef>,31_766+378);
    //    public static Werkstoff DarkSteel = new Werkstoff(Materials.DarkSteel, <GenerationFeatures>,
    // <TypeRef>,31_766+364);
    //    public static Werkstoff EndSteel = new Werkstoff(Materials.EndSteel, <GenerationFeatures>,
    // <TypeRef>,31_766+401);
    //    public static Werkstoff CrudeSteel = new Werkstoff(Materials.CrudeSteel, <GenerationFeatures>,
    // <TypeRef>,31_766+402);
    //    public static Werkstoff CrystallineAlloy = new Werkstoff(Materials.CrystallineAlloy, <GenerationFeatures>,
    // <TypeRef>,31_766+403);
    //    public static Werkstoff MelodicAlloy = new Werkstoff(Materials.MelodicAlloy, <GenerationFeatures>,
    // <TypeRef>,31_766+404);
    //    public static Werkstoff StellarAlloy = new Werkstoff(Materials.StellarAlloy, <GenerationFeatures>,
    // <TypeRef>,31_766+405);
    //    public static Werkstoff CrystallinePinkSlime = new Werkstoff(Materials.CrystallinePinkSlime,
    // <GenerationFeatures>, <TypeRef>,31_766+406);
    //    public static Werkstoff EnergeticSilver = new Werkstoff(Materials.EnergeticSilver, <GenerationFeatures>,
    // <TypeRef>,31_766+407);
    //    public static Werkstoff VividAlloy = new Werkstoff(Materials.VividAlloy, <GenerationFeatures>,
    // <TypeRef>,31_766+408);
    //    public static Werkstoff Enderium = new Werkstoff(Materials.Enderium, <GenerationFeatures>,
    // <TypeRef>,31_766+321);
    //    public static Werkstoff Mithril = new Werkstoff(Materials.Mithril, <GenerationFeatures>,
    // <TypeRef>,31_766+331);
    //    public static Werkstoff BlueAlloy = new Werkstoff(Materials.BlueAlloy, <GenerationFeatures>,
    // <TypeRef>,31_766+309);
    //    public static Werkstoff ShadowIron = new Werkstoff(Materials.ShadowIron, <GenerationFeatures>,
    // <TypeRef>,31_766+336);
    //    public static Werkstoff ShadowSteel = new Werkstoff(Materials.ShadowSteel, <GenerationFeatures>,
    // <TypeRef>,31_766+337);
    //    public static Werkstoff AstralSilver = new Werkstoff(Materials.AstralSilver, <GenerationFeatures>,
    // <TypeRef>,31_766+333);
    //    public static Werkstoff InfinityCatalyst = new Werkstoff(Materials.InfinityCatalyst, <GenerationFeatures>,
    // <TypeRef>, 31_766 + 394);
    //    public static Werkstoff Infinity = new Werkstoff(Materials.Infinity, <GenerationFeatures>, <TypeRef>, 31_766 +
    // 397);
    //    public static Werkstoff Bedrockium = new Werkstoff(Materials.Bedrockium, <GenerationFeatures>, <TypeRef>,
    // 31_766 + 395);
    //    public static Werkstoff Trinium = new Werkstoff(Materials.Trinium, <GenerationFeatures>, <TypeRef>, 31_766 +
    // 868);
    //    public static Werkstoff Ichorium = new Werkstoff(Materials.Ichorium, <GenerationFeatures>, <TypeRef>, 31_766 +
    // 978);
    //    public static Werkstoff CosmicNeutronium = new Werkstoff(Materials.CosmicNeutronium, <GenerationFeatures>,
    // <TypeRef>, 31_766 + 982);
    //    public static Werkstoff Pentacadmiummagnesiumhexaoxid = new Werkstoff(Materials.Pentacadmiummagnesiumhexaoxid,
    // <GenerationFeatures>, <TypeRef>, 31_766 + 987);
    //    public static Werkstoff Titaniumonabariumdecacoppereikosaoxid = new
    // Werkstoff(Materials.Titaniumonabariumdecacoppereikosaoxid, <GenerationFeatures>, <TypeRef>, 31_766 + 988);
    //    public static Werkstoff Uraniumtriplatinid = new Werkstoff(Materials.Uraniumtriplatinid, <GenerationFeatures>,
    // <TypeRef>, 31_766 + 989);
    //    public static Werkstoff Vanadiumtriindinid = new Werkstoff(Materials.Vanadiumtriindinid, <GenerationFeatures>,
    // <TypeRef>, 31_766 + 990);
    //    public static Werkstoff Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid = new
    // Werkstoff(Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid, <GenerationFeatures>, <TypeRef>,
    // 31_766 + 991);
    //    public static Werkstoff Tetranaquadahdiindiumhexaplatiumosminid = new
    // Werkstoff(Materials.Tetranaquadahdiindiumhexaplatiumosminid, <GenerationFeatures>, <TypeRef>, 31_766 + 992);
    //    public static Werkstoff Longasssuperconductornameforuvwire = new
    // Werkstoff(Materials.Longasssuperconductornameforuvwire, <GenerationFeatures>, <TypeRef>, 31_766 + 986);
    //    public static Werkstoff Longasssuperconductornameforuhvwire = new
    // Werkstoff(Materials.Longasssuperconductornameforuhvwire, <GenerationFeatures>, <TypeRef>, 31_766 + 985);
}
