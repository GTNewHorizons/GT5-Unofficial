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

package bartworks.API;

import net.minecraft.item.ItemStack;

public interface IRadMaterial {

    /**
     * @return the amount of Radiation (0-150, commonly, higher values are possible but not recommended!)
     */
    int getRadiationLevel(ItemStack aStack);

    /**
     * @return 1 for stick, 2 for long rods, 3 for fuel rods
     */
    byte getAmountOfMaterial(ItemStack aStack);

    /**
     * @return the color of the material for display purposes
     */
    short[] getColorForGUI(ItemStack aStack);

    /**
     * @return the name of the material for display purposes
     */
    String getNameForGUI(ItemStack aStack);
}
