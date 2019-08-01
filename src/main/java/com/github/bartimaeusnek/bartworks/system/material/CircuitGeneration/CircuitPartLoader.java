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

package com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration;

import com.github.bartimaeusnek.bartworks.client.renderer.BW_GT_ItemRenderer;
import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.ItemList.Circuit_Parts_Transistor;

public class CircuitPartLoader implements Runnable {
    @Override
    public void run() {
        CircuitPartLoader.makeCircuitParts();
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            new BW_GT_ItemRenderer();
    }

    public static void makeCircuitParts() {
        ItemList[] itemLists = values();
        for (ItemList single : itemLists) {
            if (!single.hasBeenSet())
                continue;
            if (
                    single.toString().contains("Wafer") ||
                            single.toString().contains("Circuit_Silicon_Ingot") ||
                            single.toString().contains("Raw") ||
                            single.toString().contains("raw") ||
                            single.toString().contains("Glass_Tube") ||
                            single == Circuit_Parts_GlassFiber ||
                            single == Circuit_Parts_Advanced ||
                            single == Circuit_Parts_Wiring_Advanced ||
                            single == Circuit_Parts_Wiring_Elite ||
                            single == Circuit_Parts_Wiring_Basic ||
                            single == Circuit_Integrated ||
                            single == Circuit_Parts_PetriDish ||
                            single == Circuit_Parts_Vacuum_Tube ||
                            single == Circuit_Integrated_Good ||
                            single == Circuit_Parts_Capacitor ||
                            single == Circuit_Parts_Diode ||
                            single == Circuit_Parts_Resistor ||
                            single == Circuit_Parts_Transistor

            ){
                CircuitImprintLoader.blacklistSet.add(single.get(1));
                continue;
            }
            ItemStack itemStack = single.get(1);
            if (!GT_Utility.isStackValid(itemStack))
                continue;
            int[] oreIDS = OreDictionary.getOreIDs(itemStack);
            if (oreIDS.length > 1)
                continue;
            String name = null;
            if (oreIDS.length == 1)
                name = OreDictionary.getOreName(oreIDS[0]);
            if ((name == null || name.isEmpty()) && (single.toString().contains("Circuit") || single.toString().contains("circuit") || single.toString().contains("board")) && single.getBlock() == Blocks.air) {
                ArrayList<String> toolTip = new ArrayList<>();
                single.getItem().addInformation(single.get(1).copy(), null, toolTip, true);
                String tt = (toolTip.size() > 0 ? toolTip.get(0) : "");
                //  tt += "Internal Name = "+single;
                String localised = GT_LanguageManager.getTranslation(GT_LanguageManager.getTranslateableItemStackName(itemStack));
                BW_Meta_Items.getNEWCIRCUITS().addItem(CircuitImprintLoader.reverseIDs, "Wrap of " + localised+"s", tt);
                GT_Values.RA.addAssemblerRecipe(new ItemStack[]{single.get(16).copy()}, Materials.Plastic.getMolten(576),BW_Meta_Items.getNEWCIRCUITS().getStack(CircuitImprintLoader.reverseIDs),600,30);
                CircuitImprintLoader.circuitIIconRefs.put(CircuitImprintLoader.reverseIDs,single);
                CircuitImprintLoader.reverseIDs--;
            }
        }
    }
}
