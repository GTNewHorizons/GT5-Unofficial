/*
 * Copyright (c) 2018-2019 bartimaeusnek
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

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import java.util.Arrays;

@SuppressWarnings("unused")
public class BW_ColorUtil {
    private BW_ColorUtil() {}

    public static byte getDarknessFromColor(short[] rgba, int index) {
        int g = rgba[index];
        if (g >= 0 && g < 64) return 0;
        else if (g >= 64 && g < 160) return 1;
        else if (g >= 160 && g < 223) return 2;
        else if (g >= 233 && g <= 255) return 3;
        return 4;
    }

    public static Dyes getDyeFromColor(short[] rgba) {
        rgba = correctCorlorArray(rgba);
        if (isGrayScale(rgba, 2)) {
            switch (getDarknessFromColor(rgba, 0)) {
                case 0:
                    return Dyes.dyeBlack;
                case 1:
                    return Dyes.dyeGray;
                case 2:
                    return Dyes.dyeLightGray;
                case 3:
                    return Dyes.dyeWhite;
            }
        } else {
            short[] tmp = roundColor(rgba, 2);
            if (isRedScale(tmp)) {
                if (isPurpleScale(tmp)) {
                    switch (getDarknessFromColor(rgba, 0)) {
                        case 0:
                        case 1:
                            if (rgba[3] - 50 > rgba[0]) return Dyes.dyePurple;
                            else return Dyes.dyeRed;
                        case 2:
                        case 3:
                            if (rgba[3] - 50 > rgba[0]) return Dyes.dyeMagenta;
                            else if (rgba[0] > 200 && rgba[2] > 140) return Dyes.dyePink;
                            else if (rgba[0] > rgba[1] + rgba[1] / 10
                                    && rgba[0] > rgba[2] + rgba[2] / 10
                                    && rgba[1] >> 4 == rgba[2] >> 4
                                    && rgba[1] + 50 > rgba[0]) {
                                return Dyes.dyeBrown;
                            } else return Dyes.dyeRed;
                        case 4:
                            return Dyes._NULL;
                    }
                }
                if (isYellowScale(tmp))
                    switch (getDarknessFromColor(rgba, 0)) {
                        case 0:
                        case 1:
                            return Dyes.dyeBrown;
                        case 2:
                        case 3: {
                            if (rgba[0] >> 5 > rgba[1] >> 5) return Dyes.dyeOrange;
                            else return Dyes.dyeYellow;
                        }
                        case 4:
                            return Dyes._NULL;
                    }
                return Dyes.dyePink;
            } else if (isGrenScale(tmp)) {
                if (isCyanScale(tmp)) {
                    if (rgba[2] + 40 < rgba[1])
                        switch (getDarknessFromColor(rgba, 0)) {
                            case 0:
                            case 1:
                                return Dyes.dyeGreen;
                            case 2:
                            case 3:
                                return Dyes.dyeLime;
                        }
                    return Dyes.dyeCyan;
                }
                if (isYellowScale(tmp))
                    switch (getDarknessFromColor(rgba, 0)) {
                        case 0:
                        case 1:
                            return Dyes.dyeBrown;
                        case 2:
                        case 3: {
                            if (rgba[0] >> 5 > rgba[1] >> 5) return Dyes.dyeOrange;
                            else return Dyes.dyeYellow;
                        }
                    }
                switch (getDarknessFromColor(rgba, 0)) {
                    case 0:
                    case 1:
                        return Dyes.dyeGreen;
                    case 2:
                    case 3:
                        return Dyes.dyeLime;
                }
            } else if (isBlueScale(tmp)) {
                if (isPurpleScale(tmp)) {
                    switch (getDarknessFromColor(rgba, 0)) {
                        case 0:
                        case 1:
                            return Dyes.dyePurple;
                        case 2:
                        case 3:
                            return Dyes.dyeMagenta;
                    }
                } else if (isCyanScale(tmp)) {
                    return Dyes.dyeCyan;
                }
                switch (getDarknessFromColor(rgba, 0)) {
                    case 0:
                    case 1:
                        return Dyes.dyeBlue;
                    case 2:
                    case 3:
                        return Dyes.dyeLightBlue;
                }
            }
        }
        return Dyes._NULL;
    }

    public static boolean isCyanScale(short[] rgba) {
        return !isRedScale(rgba);
    }

    public static boolean isPurpleScale(short[] rgba) {
        return !isGrenScale(rgba);
    }

    public static boolean isYellowScale(short[] rgba) {
        return !isBlueScale(rgba);
    }

    public static boolean isBlueScale(short[] rgba) {
        rgba = correctCorlorArray(rgba);
        return (rgba[2] * 2) >= (rgba[1] + rgba[0]);
    }

    public static boolean isGrenScale(short[] rgba) {
        rgba = correctCorlorArray(rgba);
        return (rgba[1] * 2) >= (rgba[0] + rgba[2]);
    }

    public static boolean isRedScale(short[] rgba) {
        rgba = correctCorlorArray(rgba);
        return (rgba[0] * 2) >= (rgba[1] + rgba[2]);
    }

    public static boolean isGrayScale(short[] rgba, int magin) {
        rgba = correctCorlorArray(rgba);
        return rgba[0] >> magin == rgba[1] >> magin && rgba[1] >> magin == rgba[2] >> magin;
    }

    public static short[] roundColor(short[] rgba, int magin) {
        short[] tmp = Arrays.copyOf(rgba, 4);
        tmp[0] = (short) (rgba[0] >> magin);
        tmp[1] = (short) (rgba[1] >> magin);
        tmp[2] = (short) (rgba[2] >> magin);
        return tmp;
    }

    public static boolean isGrayScale(short[] rgba) {
        rgba = correctCorlorArray(rgba);
        return rgba[0] == rgba[1] && rgba[1] == rgba[2];
    }

    public static short[] correctCorlorArray(short[] rgba) {
        if (rgba.length > 4) {
            rgba = Arrays.copyOfRange(rgba, 0, 4);
        }
        if (rgba.length < 4) {
            short[] tmp = Arrays.copyOf(rgba, 4);
            Arrays.fill(tmp, rgba.length, 4, (short) 0);
            rgba = tmp;
        }
        if (rgba[0] > 255) rgba[0] = 255;
        if (rgba[1] > 255) rgba[1] = 255;
        if (rgba[2] > 255) rgba[2] = 255;
        if (rgba[3] > 255) rgba[3] = 255;
        if (rgba[0] < 0) rgba[0] = 0;
        if (rgba[1] < 0) rgba[1] = 0;
        if (rgba[2] < 0) rgba[2] = 0;
        if (rgba[3] < 0) rgba[3] = 0;
        return rgba;
    }

    public static short[] splitColorToRBGArray(int rgb) {
        return new short[] {(short) ((rgb >> 16) & 0xFF), (short) ((rgb >> 8) & 0xFF), (short) (rgb & 0xFF)};
    }

    public static int getColorFromRGBArray(short[] color) {
        return ((color[0] & 0x0ff) << 16) | ((color[1] & 0x0ff) << 8) | (color[2] & 0x0ff);
    }

    public static int getColorFromRGBArray(int[] color) {
        return ((color[0] & 0x0ff) << 16) | ((color[1] & 0x0ff) << 8) | (color[2] & 0x0ff);
    }

    public static String getColorForTier(int tier) {
        return GT_Values.TIER_COLORS[tier];
    }
}
