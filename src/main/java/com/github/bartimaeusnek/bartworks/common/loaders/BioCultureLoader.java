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

package com.github.bartimaeusnek.bartworks.common.loaders;

import com.github.bartimaeusnek.bartworks.util.BioCulture;
import com.github.bartimaeusnek.bartworks.util.BioDNA;
import com.github.bartimaeusnek.bartworks.util.BioData;
import com.github.bartimaeusnek.bartworks.util.BioPlasmid;
import java.awt.*;
import net.minecraft.item.EnumRarity;

public class BioCultureLoader {

    public static final BioData BIO_DATA_BETA_LACMATASE =
            BioData.createAndRegisterBioData("beta-Lactamase", EnumRarity.uncommon, 10000, 0);
    private static final BioData BIO_DATA_YEAST =
            BioData.createAndRegisterBioData("Saccharomyces cerevisiae", EnumRarity.common);
    private static final BioDNA BIO_DNA_WHINE_YEAST =
            BioDNA.createAndRegisterBioDNA("Saccharomyces cerevisiae var bayanus", EnumRarity.uncommon);
    private static final BioDNA BIO_DNA_BEER_YEAST =
            BioDNA.createAndRegisterBioDNA("Saccharomyces cerevisiae var cerevisiae", EnumRarity.uncommon);
    private static final BioData eColiData =
            BioData.createAndRegisterBioData("Escherichia koli", EnumRarity.uncommon, 10000, 0);
    private static final BioDNA BIO_DNA_ANAEROBIC_OIL =
            BioDNA.createAndRegisterBioDNA("Pseudomonas Veronii", EnumRarity.uncommon);
    private static final BioData BIO_DATA_ANAEROBIC_OIL =
            BioData.createAndRegisterBioData("Pseudomonas Veronii", EnumRarity.uncommon, 5000, 1);
    public static BioCulture CommonYeast;
    public static BioCulture WhineYeast;
    public static BioCulture BeerYeast;
    public static BioCulture rottenFleshBacteria;
    public static BioCulture eColi;
    public static BioCulture anaerobicOil;
    public static BioCulture generalPurposeFermentingBacteria;

    public static void run() {
        BioCultureLoader.CommonYeast = BioCulture.createAndRegisterBioCulture(
                new Color(255, 248, 200),
                "Saccharomyces cerevisiae",
                BioPlasmid.convertDataToPlasmid(BioCultureLoader.BIO_DATA_YEAST),
                BioDNA.convertDataToDNA(BioCultureLoader.BIO_DATA_YEAST),
                true);
        BioCultureLoader.WhineYeast = BioCulture.createAndRegisterBioCulture(
                new Color(255, 248, 200),
                "Saccharomyces cerevisiae var bayanus",
                BioPlasmid.convertDataToPlasmid(BioCultureLoader.BIO_DNA_WHINE_YEAST),
                BioCultureLoader.BIO_DNA_WHINE_YEAST,
                EnumRarity.uncommon,
                true);
        BioCultureLoader.BeerYeast = BioCulture.createAndRegisterBioCulture(
                new Color(255, 248, 200),
                "Saccharomyces cerevisiae var cerevisiae",
                BioPlasmid.convertDataToPlasmid(BioCultureLoader.BIO_DNA_BEER_YEAST),
                BioCultureLoader.BIO_DNA_BEER_YEAST,
                EnumRarity.uncommon,
                true);
        BioCultureLoader.rottenFleshBacteria = BioCulture.createAndRegisterBioCulture(
                new Color(110, 40, 25),
                "Escherichia cadaver",
                BioPlasmid.convertDataToPlasmid(BioCultureLoader.BIO_DATA_BETA_LACMATASE),
                BioDNA.convertDataToDNA(BioCultureLoader.BIO_DATA_BETA_LACMATASE),
                false);
        BioCultureLoader.eColi = BioCulture.createAndRegisterBioCulture(
                new Color(149, 132, 75),
                "Escherichia koli",
                BioPlasmid.convertDataToPlasmid(BioCultureLoader.eColiData),
                BioDNA.convertDataToDNA(BioCultureLoader.eColiData),
                true);
        BioCultureLoader.anaerobicOil = BioCulture.createAndRegisterBioCulture(
                new Color(0, 0, 0),
                "Pseudomonas Veronii",
                BioPlasmid.convertDataToPlasmid(BioCultureLoader.BIO_DNA_ANAEROBIC_OIL),
                BioDNA.convertDataToDNA(BioCultureLoader.BIO_DATA_ANAEROBIC_OIL),
                true);
        BioCultureLoader.generalPurposeFermentingBacteria = BioCulture.createAndRegisterBioCulture(
                new Color(127, 69, 26),
                "Saccharomyces escherichia",
                BioCultureLoader.CommonYeast.getPlasmid(),
                BioCultureLoader.eColi.getdDNA(),
                EnumRarity.epic,
                true);

        BioCultureLoader.CommonYeast.setLocalisedName("Common Yeast");
        BioCultureLoader.WhineYeast.setLocalisedName("Whine Yeast");
        BioCultureLoader.BeerYeast.setLocalisedName("Beer Yeast");
        BioCultureLoader.rottenFleshBacteria.setLocalisedName("Rotten Flesh Bacteria");
        BioCultureLoader.eColi.setLocalisedName("eColi Bacteria");
        BioCultureLoader.anaerobicOil.setLocalisedName("Anaerobic Oil Bacteria");
        BioCultureLoader.generalPurposeFermentingBacteria.setLocalisedName("General Purpose Fermenting Bacteria");
    }
}
