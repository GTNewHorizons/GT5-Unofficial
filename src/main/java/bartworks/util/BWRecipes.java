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

package bartworks.util;

import gregtech.api.enums.Materials;

public class BWRecipes {

    public static long calcDecayTicks(int x) {
        long ret;
        if (x == 43) ret = 5000;
        else if (x == 61) ret = 4500;
        else if (x <= 100) ret = MathUtils.ceilLong((8000D * Math.tanh(-x / 20D) + 8000D) * 1000D);
        else ret = MathUtils.ceilLong(8000D * Math.tanh(-x / 65D) + 8000D);
        return ret;
    }

    public static int computeSieverts(int givenSievert, int glassTier, boolean requiresExactSieverts, boolean cleanroom,
        boolean lowGravity) {
        byte specialValue = 0;
        if (cleanroom && lowGravity) {
            specialValue = 3;
        } else if (cleanroom) {
            specialValue = 2;
        } else if (lowGravity) {
            specialValue = 1;
        }
        int sievertValue = 0;
        if (givenSievert >= 83 || givenSievert == 61 || givenSievert == 43) sievertValue += givenSievert;
        sievertValue = sievertValue << 1;
        sievertValue = sievertValue | (requiresExactSieverts ? 1 : 0);
        sievertValue = sievertValue << 2;
        sievertValue = sievertValue | specialValue;
        sievertValue = sievertValue << 4;
        sievertValue = sievertValue | glassTier;
        return sievertValue;
    }

    public static int computeSieverts(Materials material, int glassTier, boolean requiresExactSieverts,
        boolean cleanroom, boolean lowGravity) {
        byte specialValue = 0;
        if (cleanroom && lowGravity) {
            specialValue = 3;
        } else if (cleanroom) {
            specialValue = 2;
        } else if (lowGravity) {
            specialValue = 1;
        }
        int aSievert = 0;
        if (material.getProtons() >= 83 || material.getProtons() == 61 || material.getProtons() == 43)
            aSievert += BWUtil.calculateSv(material);
        aSievert = aSievert << 1;
        aSievert = aSievert | (requiresExactSieverts ? 1 : 0);
        aSievert = aSievert << 2;
        aSievert = aSievert | specialValue;
        aSievert = aSievert << 4;
        aSievert = aSievert | glassTier;
        return aSievert;
    }
}
