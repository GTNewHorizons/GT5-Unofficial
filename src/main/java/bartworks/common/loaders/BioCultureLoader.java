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

package bartworks.common.loaders;

import java.awt.Color;

import bartworks.API.enums.BioCultureEnum;
import bartworks.API.enums.BioDataEnum;
import net.minecraft.item.EnumRarity;

import bartworks.util.BioCulture;
import bartworks.util.BioData;

public class BioCultureLoader {

    public static BioCulture CommonYeast;
    public static BioCulture WhineYeast;
    public static BioCulture BeerYeast;
    public static BioCulture rottenFleshBacteria;
    public static BioCulture eColi;
    public static BioCulture anaerobicOil;
    public static BioCulture generalPurposeFermentingBacteria;

    public static void run() {
        BioCultureLoader.CommonYeast = BioCultureEnum.SaccharomycesCerevisiae.bioCulture;
        BioCultureLoader.WhineYeast = BioCultureEnum.SaccharomycesCerevisiaeVarBayanus.bioCulture;
        BioCultureLoader.BeerYeast = BioCultureEnum.SaccharomycesCerevisiaeVarCerevisiae.bioCulture;
        BioCultureLoader.rottenFleshBacteria = BioCultureEnum.EscherichiaCadaver.bioCulture;
        BioCultureLoader.eColi = BioCultureEnum.EscherichiaKoli.bioCulture;
        BioCultureLoader.anaerobicOil = BioCultureEnum.PseudomonasVeronii.bioCulture;
        BioCultureLoader.generalPurposeFermentingBacteria = BioCultureEnum.SaccharomycesEscherichia.bioCulture;

        BioCultureLoader.CommonYeast.setLocalisedName("Common Yeast");
        BioCultureLoader.WhineYeast.setLocalisedName("Whine Yeast");
        BioCultureLoader.BeerYeast.setLocalisedName("Beer Yeast");
        BioCultureLoader.rottenFleshBacteria.setLocalisedName("Rotten Flesh Bacteria");
        BioCultureLoader.eColi.setLocalisedName("eColi Bacteria");
        BioCultureLoader.anaerobicOil.setLocalisedName("Anaerobic Oil Bacteria");
        BioCultureLoader.generalPurposeFermentingBacteria.setLocalisedName("General Purpose Fermenting Bacteria");
    }
}
