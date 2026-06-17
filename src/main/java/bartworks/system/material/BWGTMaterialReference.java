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
package bartworks.system.material;

import bartworks.MainMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

public class BWGTMaterialReference {

    private static final Werkstoff.GenerationFeatures ADD_CASINGS_ONLY = new Werkstoff.GenerationFeatures().disable()
        .addPrefix(OrePrefixes.blockCasing)
        .addPrefix(OrePrefixes.blockCasingAdvanced);

    public static Werkstoff Silver = new Werkstoff(
        Materials.Silver,
        ADD_CASINGS_ONLY,
        Werkstoff.Types.ELEMENT,
        31_766 + 54);
    public static Werkstoff Naquadah = new Werkstoff(
        Materials.Naquadah,
        ADD_CASINGS_ONLY,
        Werkstoff.Types.ELEMENT,
        31_766 + 324);
    public static Werkstoff Wood = new Werkstoff(
        Materials.Wood,
        ADD_CASINGS_ONLY,
        Werkstoff.Types.BIOLOGICAL,
        31_766 + 809);
    public static Werkstoff Magnesia = new Werkstoff(
        Materials.Magnesia,
        new Werkstoff.GenerationFeatures().disable()
            .addMetalItems()
            .addMolten(),
        Werkstoff.Types.COMPOUND,
        31_766 + 471);

    public static void init() {
        MainMod.LOGGER.info("Load Elements from GT");
    }
}
