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

package com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe;

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.github.bartimaeusnek.bartworks.util.log.DebugLog;
import com.github.bartimaeusnek.crossmod.thaumcraft.util.ThaumcraftHandler;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TC_Aspects;
import java.util.Objects;

public class AspectLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        if (!LoaderReference.Thaumcraft) return;
        for (OrePrefixes enabledOrePrefixes : WerkstoffLoader.ENABLED_ORE_PREFIXES) {
            if (werkstoff.hasItemType(enabledOrePrefixes)) {
                if (enabledOrePrefixes.mMaterialAmount >= 3628800L || enabledOrePrefixes == OrePrefixes.ore) {
                    DebugLog.log("OrePrefix: " + enabledOrePrefixes.name() + " mMaterialAmount: "
                            + enabledOrePrefixes.mMaterialAmount / 3628800L);
                    if (Objects.nonNull(WerkstoffLoader.items.get(enabledOrePrefixes)))
                        ThaumcraftHandler.AspectAdder.addAspectViaBW(
                                werkstoff.get(enabledOrePrefixes),
                                werkstoff.getTCAspects(
                                        enabledOrePrefixes == OrePrefixes.ore
                                                ? 1
                                                : (int) (enabledOrePrefixes.mMaterialAmount / 3628800L)));
                } else if (enabledOrePrefixes.mMaterialAmount >= 0L) {
                    if (Objects.nonNull(WerkstoffLoader.items.get(enabledOrePrefixes)))
                        //noinspection unchecked
                        ThaumcraftHandler.AspectAdder.addAspectViaBW(
                                werkstoff.get(enabledOrePrefixes), new Pair<>(TC_Aspects.PERDITIO.mAspect, 1));
                }
            }
        }
    }
}
