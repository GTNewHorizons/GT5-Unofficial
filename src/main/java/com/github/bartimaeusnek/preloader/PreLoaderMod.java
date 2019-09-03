/*
 * Copyright (c) 2019 bartimaeusnek
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

package com.github.bartimaeusnek.preloader;

import com.github.bartimaeusnek.bartworks.common.loaders.BeforeGTPreload;
import com.github.bartimaeusnek.crossmod.BartWorksCrossmod;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.GregTech_API;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = com.github.bartimaeusnek.preloader.PreLoaderMod.MOD_ID, name = com.github.bartimaeusnek.preloader.PreLoaderMod.NAME, version = com.github.bartimaeusnek.preloader.PreLoaderMod.VERSION,
        dependencies = "required-before:IC2; "
                + "required-before:gregtech; "
                + "required-before:bartworks;"
                + "before:GalacticraftMars; "
                + "before:GalacticraftCore; "
                + "before:miscutils;"
                + "before:dreamcraft;"
                + "before:EMT;"
)
public class PreLoaderMod {
    public static final String NAME = "BartWorks Preloader Mod";
    public static final String VERSION = "0.0.1";
    public static final String MOD_ID = "bartworkspreloader";
    public static final Logger LOGGER = LogManager.getLogger(BartWorksCrossmod.NAME);


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preinit) {
        GregTech_API.sBeforeGTPreload.add(new BeforeGTPreload());
    }
}
