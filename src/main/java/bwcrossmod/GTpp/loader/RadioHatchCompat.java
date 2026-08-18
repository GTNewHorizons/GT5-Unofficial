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

package bwcrossmod.GTpp.loader;

import java.util.HashSet;

import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.MainMod;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialUtils;

public class RadioHatchCompat {

    public static HashSet<String> TranslateSet = new HashSet<>();

    /// Registers each radioactive material's `stick`/`stickLong` item under its `stick`/`stickLong` oredict
    /// name when no ore entry holds that name yet. A material without the rod shape is skipped.
    public static void run() {
        MainMod.BW_DEBUG_LOGGER.info("Starting Generation of missing GT++ rods/longrods");

        for (Material mats : MaterialLibAPI.getMaterials()) {
            if (!Boolean.TRUE.equals(mats.getProperty(GTMaterialProperties.IS_RADIOACTIVE))) continue;

            String name = MaterialUtils.internalName(mats);

            if (mats.hasShape(Shapes.stick) && OreDictionary.getOres("stick" + name)
                .isEmpty()) {
                OreDictionary.registerOre("stick" + name, MaterialLibAPI.getStack(mats, Shapes.stick, 1));
                MainMod.BW_DEBUG_LOGGER.info("Generate: stick{}", name);
            }

            if (mats.hasShape(Shapes.stickLong) && OreDictionary.getOres("stickLong" + name)
                .isEmpty()) {
                OreDictionary.registerOre("stickLong" + name, MaterialLibAPI.getStack(mats, Shapes.stickLong, 1));
                MainMod.BW_DEBUG_LOGGER.info("Generate: stickLong{}", name);
            }
        }
    }

}
