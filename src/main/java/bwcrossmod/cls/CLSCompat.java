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

package bwcrossmod.cls;

import java.io.IOException;

import alexiil.mods.load.MinecraftDisplayer;
import alexiil.mods.load.ProgressDisplayer;
import bartworks.system.material.Werkstoff;

public class CLSCompat {

    private CLSCompat() {}

    private static final long MINIMAL_UPDATE_INTERVAL = 1000 / 30; // target 30 fps
    private static long lastUpdate = 0;

    public static int[] initCls() {
        int sizeStep;
        int sizeStep2 = 1;
        MinecraftDisplayer.isRegisteringBartWorks = true;
        if (Werkstoff.werkstoffHashSet.size() >= 100) sizeStep = Werkstoff.werkstoffHashSet.size() / 100 - 1;
        else sizeStep = sizeStep2 = Werkstoff.werkstoffHashSet.size();
        return new int[] { sizeStep, sizeStep2, sizeStep };
    }

    public static int invokeStepSize(Werkstoff werkstoff, int[] steps, int size) {
        --steps[0];
        long time = System.currentTimeMillis();
        if (time - lastUpdate >= MINIMAL_UPDATE_INTERVAL) {
            try {
                ProgressDisplayer.displayProgress(werkstoff.getDefaultName(), (float) size / 10000);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            lastUpdate = time;
        }
        if (steps[0] == 0 && Werkstoff.werkstoffHashSet.size() >= 100) steps[0] = steps[2];
        size += steps[1];
        return size;
    }

    public static void disableCls() {
        MinecraftDisplayer.isRegisteringBartWorks = false;
    }
}
