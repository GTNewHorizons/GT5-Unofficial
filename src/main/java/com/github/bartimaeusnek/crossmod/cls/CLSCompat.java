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

package com.github.bartimaeusnek.crossmod.cls;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CLSCompat {
    private CLSCompat() {}

    private static final long MINIMAL_UPDATE_INTERVAL = 1000 / 30; // target 30 fps
    private static long lastUpdate = 0;
    private static Class alexiilMinecraftDisplayer;
    private static Class alexiilProgressDisplayer;
    private static Method displayProgress;
    private static Field isRegisteringBartWorks;

    static {
        try {
            alexiilMinecraftDisplayer = Class.forName("alexiil.mods.load.MinecraftDisplayer");
            alexiilProgressDisplayer = Class.forName("alexiil.mods.load.ProgressDisplayer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Optional.ofNullable(alexiilMinecraftDisplayer).ifPresent(e -> {
            try {
                isRegisteringBartWorks = e.getField("isRegisteringBartWorks");
            } catch (NoSuchFieldException ex) {
                ex.printStackTrace();
            }
        });

        Optional.ofNullable(alexiilProgressDisplayer).ifPresent(e -> {
            try {
                displayProgress = e.getMethod("displayProgress", String.class, float.class);
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static Integer[] initCls() {
        int sizeStep;
        int sizeStep2 = 1;

        try {
            isRegisteringBartWorks.set(null, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (Werkstoff.werkstoffHashSet.size() >= 100) sizeStep = Werkstoff.werkstoffHashSet.size() / 100 - 1;
        else sizeStep = sizeStep2 = Werkstoff.werkstoffHashSet.size();

        return new Integer[] {sizeStep, sizeStep2, sizeStep};
    }

    public static int invokeStepSize(Werkstoff werkstoff, Integer[] steps, int size) {
        --steps[0];

        long time = System.currentTimeMillis();
        if (time - lastUpdate >= MINIMAL_UPDATE_INTERVAL) {
            try {
                displayProgress.invoke(null, werkstoff.getDefaultName(), ((float) size) / 10000);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            lastUpdate = time;
        }

        if (steps[0] == 0 && Werkstoff.werkstoffHashSet.size() >= 100) steps[0] = steps[2];

        size += steps[1];
        return size;
    }

    public static void disableCls() {
        try {
            isRegisteringBartWorks.set(null, false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
