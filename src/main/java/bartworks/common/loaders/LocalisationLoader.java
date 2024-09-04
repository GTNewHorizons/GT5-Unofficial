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

import gregtech.api.util.GTLanguageManager;

public class LocalisationLoader {

    private LocalisationLoader() {}

    private static void localiseTooltips() {
        GTLanguageManager
            .addStringLocalization("metaitem.01.tooltip.purify.2", "Throw into Cauldron to get clean crushed Ore");
        GTLanguageManager
            .addStringLocalization("metaitem.01.tooltip.nqgen", "Can be used as Enriched Naquadah Fuel Substitute");
    }

    private static void localiseAchivements() {
        GTLanguageManager
            .addStringLocalization("achievement.gt.blockmachines.electricimplosioncompressor", "Electric Implosions?");
        GTLanguageManager.addStringLocalization(
            "achievement.gt.blockmachines.electricimplosioncompressor.desc",
            "Basically a giant Hammer that presses Stuff - No more TNT!");
        GTLanguageManager.addStringLocalization("achievement.gt.blockmachines.dehp", "Heat from below!");
        GTLanguageManager
            .addStringLocalization("achievement.gt.blockmachines.dehp.desc", "Get ALL the thermal energy!");
        GTLanguageManager
            .addStringLocalization("achievement.gt.blockmachines.circuitassemblyline", "Cheaper Circuits?");
        GTLanguageManager.addStringLocalization(
            "achievement.gt.blockmachines.circuitassemblyline.desc",
            "Well, yes, but actually no...");
        GTLanguageManager.addStringLocalization("achievement.gt.blockmachines.voidminer1", "Ores from deep below!");
        GTLanguageManager
            .addStringLocalization("achievement.gt.blockmachines.voidminer1.desc", "Is this some kind of Ex-Nihilo?");
        GTLanguageManager.addStringLocalization(
            "achievement.gt.blockmachines.voidminer2",
            "Ores from deep below 2! Electric Boogaloo!");
        GTLanguageManager
            .addStringLocalization("achievement.gt.blockmachines.voidminer2.desc", "Ex-Nihilo, but faster!");
        GTLanguageManager.addStringLocalization(
            "achievement.gt.blockmachines.voidminer3",
            "Ores from deep below 3! Trinity Ex-Nihilo");
        GTLanguageManager
            .addStringLocalization("achievement.gt.blockmachines.voidminer3.desc", "3x the trouble, 3x the fun!");
    }

    public static void localiseAll() {
        localiseAchivements();
        localiseTooltips();
    }
}
