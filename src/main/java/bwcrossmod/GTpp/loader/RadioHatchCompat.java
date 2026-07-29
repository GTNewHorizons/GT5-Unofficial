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

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.util.log.DebugLog;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialUtils;

public class RadioHatchCompat {

    public static HashSet<String> TranslateSet = new HashSet<>();

    /// Ensures every radioactive material's `stick`/`stickLong` item is reachable under its legacy oredict
    /// name, for radio hatch machinery that still looks materials up that way. A material MaterialLib never
    /// generates a rod shape for is skipped -- unlike the retired gtPlusPlus `BaseItemRod`/`BaseItemRodLong`,
    /// nothing here can mint a rod item from scratch.
    public static void run() {
        DebugLog.log("Starting Generation of missing GT++ rods/longrods");

        for (Material mats : MaterialLibAPI.getMaterials()) {
            if (!Boolean.TRUE.equals(mats.getProperty(GTMaterialProperties.IS_RADIOACTIVE))) continue;

            String name = MaterialUtils.internalName(mats);

            if (OreDictionary.getOres("stick" + name)
                .isEmpty()) {
                ItemStack itemRod = MU.stack(OrePrefixes.stick, mats, 1);
                if (itemRod != null) {
                    OreDictionary.registerOre("stick" + name, itemRod);
                    DebugLog.log("Generate: stick" + name);
                }
            }

            if (OreDictionary.getOres("stickLong" + name)
                .isEmpty()) {
                ItemStack itemRodLong = MU.stack(OrePrefixes.stickLong, mats, 1);
                if (itemRodLong != null) {
                    OreDictionary.registerOre("stickLong" + name, itemRodLong);
                    DebugLog.log("Generate: stickLong" + name);
                }
            }
        }
    }

}
