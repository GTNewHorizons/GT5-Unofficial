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

package com.github.bartimaeusnek.bartworks.API;

import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import gregtech.api.enums.Materials;
import net.minecraftforge.fluids.FluidStack;

public final class AcidGenFuelAdder {

    public static boolean addLiquidFuel(Materials M, int burn) {
        return ((BWRecipes.BW_Recipe_Map_LiquidFuel) BWRecipes.instance.getMappingsFor((byte) 2)).addLiquidFuel(M, burn) != null;
    }

    public static boolean addLiquidFuel(FluidStack fluidStack, int burn) {
        return ((BWRecipes.BW_Recipe_Map_LiquidFuel) BWRecipes.instance.getMappingsFor((byte) 2)).addLiquidFuel(fluidStack, burn) != null;
    }

    public static boolean addMoltenFuel(Materials M, int burn) {
        return ((BWRecipes.BW_Recipe_Map_LiquidFuel) BWRecipes.instance.getMappingsFor((byte) 2)).addMoltenFuel(M, burn) != null;
    }
}
