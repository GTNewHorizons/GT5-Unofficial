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

package com.github.bartimaeusnek.bartworks.util;

import gregtech.api.enums.Element;
import gregtech.api.enums.OrePrefixes;
import net.minecraftforge.common.util.EnumHelper;

public class EnumUtils {
    private EnumUtils() {}

    public static OrePrefixes addNewOrePrefix(
            String enumName,
            String aRegularLocalName,
            String aLocalizedMaterialPre,
            String aLocalizedMaterialPost,
            boolean aIsUnificatable,
            boolean aIsMaterialBased,
            boolean aIsSelfReferencing,
            boolean aIsContainer,
            boolean aDontUnificateActively,
            boolean aIsUsedForBlocks,
            boolean aAllowNormalRecycling,
            boolean aGenerateDefaultItem,
            boolean aIsEnchantable,
            boolean aIsUsedForOreProcessing,
            int aMaterialGenerationBits,
            long aMaterialAmount,
            int aDefaultStackSize,
            int aTextureindex) {
        return EnumHelper.addEnum(
                OrePrefixes.class,
                enumName,
                new Class<?>[] {
                    String.class, String.class, String.class,
                    boolean.class, boolean.class, boolean.class,
                    boolean.class, boolean.class, boolean.class,
                    boolean.class, boolean.class, boolean.class,
                    boolean.class, int.class, long.class,
                    int.class, int.class
                },
                new Object[] {
                    aRegularLocalName, aLocalizedMaterialPre, aLocalizedMaterialPost,
                    aIsUnificatable, aIsMaterialBased, aIsSelfReferencing,
                    aIsContainer, aDontUnificateActively, aIsUsedForBlocks,
                    aAllowNormalRecycling, aGenerateDefaultItem, aIsEnchantable,
                    aIsUsedForOreProcessing, aMaterialGenerationBits, aMaterialAmount,
                    aDefaultStackSize, aTextureindex
                });
    }

    public static Element createNewElement(
            String enumName,
            long aProtons,
            long aNeutrons,
            long aAdditionalMass,
            long aHalfLifeSeconds,
            String aDecayTo,
            String aName,
            boolean aIsIsotope) {
        return EnumHelper.addEnum(
                Element.class,
                enumName,
                new Class[] {
                    long.class, long.class, long.class,
                    long.class, String.class, String.class,
                    boolean.class
                },
                new Object[] {
                    aProtons, aNeutrons, aAdditionalMass,
                    aHalfLifeSeconds, aDecayTo, aName,
                    aIsIsotope
                });
    }
}
