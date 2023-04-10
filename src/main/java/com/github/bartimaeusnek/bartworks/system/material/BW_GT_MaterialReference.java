/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package com.github.bartimaeusnek.bartworks.system.material;

import static com.github.bartimaeusnek.bartworks.system.material.Werkstoff.Types.BIOLOGICAL;
import static com.github.bartimaeusnek.bartworks.system.material.Werkstoff.Types.COMPOUND;
import static com.github.bartimaeusnek.bartworks.system.material.Werkstoff.Types.ELEMENT;
import static com.github.bartimaeusnek.bartworks.system.material.Werkstoff.Types.ISOTOPE;
import static com.github.bartimaeusnek.bartworks.system.material.Werkstoff.Types.MIXTURE;

import com.github.bartimaeusnek.bartworks.MainMod;
import gregtech.api.enums.Materials;

public class BW_GT_MaterialReference {

    private static final Werkstoff.GenerationFeatures ADD_CASINGS_ONLY = new Werkstoff.GenerationFeatures().disable()
            .addCasings();

    public static Werkstoff Aluminium = new Werkstoff(Materials.Aluminium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 19);
    public static Werkstoff Americium = new Werkstoff(Materials.Americium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 103);
    public static Werkstoff Antimony = new Werkstoff(Materials.Antimony, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 58);
    public static Werkstoff Arsenic = new Werkstoff(Materials.Arsenic, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 39);
    public static Werkstoff Barium = new Werkstoff(Materials.Barium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 63);
    public static Werkstoff Beryllium = new Werkstoff(Materials.Beryllium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 8);
    public static Werkstoff Bismuth = new Werkstoff(Materials.Bismuth, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 90);
    public static Werkstoff Boron = new Werkstoff(Materials.Boron, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 9);
    public static Werkstoff Caesium = new Werkstoff(Materials.Caesium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 62);
    public static Werkstoff Carbon = new Werkstoff(Materials.Carbon, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 10);
    public static Werkstoff Cadmium = new Werkstoff(Materials.Cadmium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 55);
    public static Werkstoff Cerium = new Werkstoff(Materials.Cerium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 65);
    public static Werkstoff Chrome = new Werkstoff(Materials.Chrome, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 30);
    public static Werkstoff Cobalt = new Werkstoff(Materials.Cobalt, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 33);
    public static Werkstoff Copper = new Werkstoff(Materials.Copper, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 35);
    public static Werkstoff Dysprosium = new Werkstoff(Materials.Dysprosium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 73);
    public static Werkstoff Erbium = new Werkstoff(Materials.Erbium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 75);
    public static Werkstoff Europium = new Werkstoff(Materials.Europium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 70);
    public static Werkstoff Gadolinium = new Werkstoff(Materials.Gadolinium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 71);
    public static Werkstoff Gallium = new Werkstoff(Materials.Gallium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 37);
    public static Werkstoff Gold = new Werkstoff(Materials.Gold, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 86);
    public static Werkstoff Holmium = new Werkstoff(Materials.Holmium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 74);
    public static Werkstoff Indium = new Werkstoff(Materials.Indium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 56);
    public static Werkstoff Iridium = new Werkstoff(Materials.Iridium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 84);
    public static Werkstoff Iron = new Werkstoff(Materials.Iron, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 32);
    public static Werkstoff Lanthanum = new Werkstoff(Materials.Lanthanum, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 64);
    public static Werkstoff Lead = new Werkstoff(Materials.Lead, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 89);
    public static Werkstoff Lithium = new Werkstoff(Materials.Lithium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 6);
    public static Werkstoff Lutetium = new Werkstoff(Materials.Lutetium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 78);
    public static Werkstoff Magnesium = new Werkstoff(Materials.Magnesium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 18);
    public static Werkstoff Manganese = new Werkstoff(Materials.Manganese, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 31);
    public static Werkstoff Molybdenum = new Werkstoff(Materials.Molybdenum, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 48);
    public static Werkstoff Neodymium = new Werkstoff(Materials.Neodymium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 67);
    public static Werkstoff Neutronium = new Werkstoff(Materials.Neutronium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 129);
    public static Werkstoff Nickel = new Werkstoff(Materials.Nickel, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 34);
    public static Werkstoff Niobium = new Werkstoff(Materials.Niobium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 47);
    public static Werkstoff Osmium = new Werkstoff(Materials.Osmium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 83);
    public static Werkstoff Palladium = new Werkstoff(Materials.Palladium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 52);
    public static Werkstoff Phosphorus = new Werkstoff(Materials.Phosphorus, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 21);
    public static Werkstoff Platinum = new Werkstoff(Materials.Platinum, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 85);
    public static Werkstoff Plutonium = new Werkstoff(Materials.Plutonium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 100);
    public static Werkstoff Plutonium241 = new Werkstoff(
            Materials.Plutonium241,
            ADD_CASINGS_ONLY,
            ISOTOPE,
            31_766 + 101);
    public static Werkstoff Potassium = new Werkstoff(Materials.Potassium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 25);
    public static Werkstoff Praseodymium = new Werkstoff(
            Materials.Praseodymium,
            ADD_CASINGS_ONLY,
            ELEMENT,
            31_766 + 66);
    public static Werkstoff Promethium = new Werkstoff(Materials.Promethium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 68);
    public static Werkstoff Rubidium = new Werkstoff(Materials.Rubidium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 43);
    public static Werkstoff Samarium = new Werkstoff(Materials.Samarium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 69);
    public static Werkstoff Scandium = new Werkstoff(Materials.Scandium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 27);
    public static Werkstoff Silicon = new Werkstoff(Materials.Silicon, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 20);
    public static Werkstoff Silver = new Werkstoff(Materials.Silver, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 54);
    public static Werkstoff Sodium = new Werkstoff(Materials.Sodium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 17);
    public static Werkstoff Strontium = new Werkstoff(Materials.Strontium, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 44);
    public static Werkstoff Sulfur = new Werkstoff(Materials.Sulfur, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 22);
    public static Werkstoff Tantalum = new Werkstoff(Materials.Tantalum, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 80);
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
    public static Werkstoff NaquadahAlloy = new Werkstoff(
            Materials.NaquadahAlloy,
            ADD_CASINGS_ONLY,
            COMPOUND,
            31_766 + 325);
    public static Werkstoff NaquadahEnriched = new Werkstoff(
            Materials.NaquadahEnriched,
            ADD_CASINGS_ONLY,
            ISOTOPE,
            31_766 + 326);
    public static Werkstoff Naquadria = new Werkstoff(Materials.Naquadria, ADD_CASINGS_ONLY, ELEMENT, 31_766 + 327);
    public static Werkstoff WroughtIron = new Werkstoff(Materials.WroughtIron, ADD_CASINGS_ONLY, ISOTOPE, 31_766 + 304);
    public static Werkstoff AnnealedCopper = new Werkstoff(
            Materials.AnnealedCopper,
            ADD_CASINGS_ONLY,
            ISOTOPE,
            31_766 + 345);

    public static Werkstoff Osmiridium = new Werkstoff(Materials.Osmiridium, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 317);
    public static Werkstoff SterlingSilver = new Werkstoff(
            Materials.SterlingSilver,
            ADD_CASINGS_ONLY,
            MIXTURE,
            31_766 + 350);
    public static Werkstoff RoseGold = new Werkstoff(Materials.RoseGold, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 351);
    public static Werkstoff BlackBronze = new Werkstoff(Materials.BlackBronze, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 352);
    public static Werkstoff BismuthBronze = new Werkstoff(
            Materials.BismuthBronze,
            ADD_CASINGS_ONLY,
            MIXTURE,
            31_766 + 353);
    public static Werkstoff BlackSteel = new Werkstoff(Materials.BlackSteel, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 334);
    public static Werkstoff RedSteel = new Werkstoff(Materials.RedSteel, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 348);
    public static Werkstoff BlueSteel = new Werkstoff(Materials.BlueSteel, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 349);
    public static Werkstoff DamascusSteel = new Werkstoff(
            Materials.DamascusSteel,
            ADD_CASINGS_ONLY,
            MIXTURE,
            31_766 + 335);
    public static Werkstoff TungstenSteel = new Werkstoff(
            Materials.TungstenSteel,
            ADD_CASINGS_ONLY,
            MIXTURE,
            31_766 + 316);
    public static Werkstoff Ultimet = new Werkstoff(Materials.Ultimet, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 344);
    public static Werkstoff TungstenCarbide = new Werkstoff(
            Materials.TungstenCarbide,
            ADD_CASINGS_ONLY,
            MIXTURE,
            31_766 + 370);
    public static Werkstoff VanadiumSteel = new Werkstoff(
            Materials.VanadiumSteel,
            ADD_CASINGS_ONLY,
            MIXTURE,
            31_766 + 371);
    public static Werkstoff HSSG = new Werkstoff(Materials.HSSG, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 372);
    public static Werkstoff HSSE = new Werkstoff(Materials.HSSE, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 373);
    public static Werkstoff HSSS = new Werkstoff(Materials.HSSS, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 374);
    public static Werkstoff StainlessSteel = new Werkstoff(
            Materials.StainlessSteel,
            ADD_CASINGS_ONLY,
            MIXTURE,
            31_766 + 306);
    public static Werkstoff Brass = new Werkstoff(Materials.Brass, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 301);
    public static Werkstoff Bronze = new Werkstoff(Materials.Bronze, ADD_CASINGS_ONLY, MIXTURE, 31_766 + 300);

    public static Werkstoff Wood = new Werkstoff(Materials.Wood, ADD_CASINGS_ONLY, BIOLOGICAL, 31_766 + 809);

    public static Werkstoff Steel = new Werkstoff(Materials.Steel, ADD_CASINGS_ONLY, COMPOUND, 31_766 + 305);
    public static Werkstoff Polytetrafluoroethylene = new Werkstoff(
            Materials.Polytetrafluoroethylene,
            ADD_CASINGS_ONLY,
            COMPOUND,
            31_766 + 473);
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
}
