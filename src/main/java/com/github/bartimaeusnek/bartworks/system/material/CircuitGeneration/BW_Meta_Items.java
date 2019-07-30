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

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictAdder;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.Pair;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.items.GT_MetaGenerated_Item;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;


public class BW_Meta_Items {

    public static BW_Meta_Items.BW_GT_MetaGenCircuits getNEWCIRCUITS() {
        return BW_Meta_Items.NEWCIRCUITS;
    }

    private static final BW_Meta_Items.BW_GT_MetaGenCircuits NEWCIRCUITS = new BW_Meta_Items.BW_GT_MetaGenCircuits();


    public void addNewCircuit(int aTier, int aID, String aName){

        String additionalOreDictData = "";
        String tooltip = "";
        String aOreDictPrefix = OrePrefixes.circuit.toString();
        switch (aTier){
            case   0:  additionalOreDictData = Materials.Primitive.toString();      tooltip = Materials.Primitive.getToolTip();         break;
            case   1:  additionalOreDictData = Materials.Basic.toString();          tooltip = Materials.Basic.getToolTip();             break;
            case   2:  additionalOreDictData = Materials.Good.toString();           tooltip = Materials.Good.getToolTip();              break;
            case   3:  additionalOreDictData = Materials.Advanced.toString();       tooltip = Materials.Advanced.getToolTip();          break;
            case   4:  additionalOreDictData = Materials.Data.toString();           tooltip = Materials.Data.getToolTip();              break;
            case   5:  additionalOreDictData = Materials.Elite.toString();          tooltip = Materials.Elite.getToolTip();             break;
            case   6:  additionalOreDictData = Materials.Master.toString();         tooltip = Materials.Master.getToolTip();            break;
            case   7:  additionalOreDictData = Materials.Ultimate.toString();       tooltip = Materials.Ultimate.getToolTip();          break;
            case   8:  additionalOreDictData = Materials.Superconductor.toString(); tooltip = Materials.Superconductor.getToolTip();    break;
            case   9:  additionalOreDictData = "Infinite";                          tooltip = "An Infinite Circuit";                    break;
            case  10:  additionalOreDictData = "Bio";                               tooltip = "A Bio Circuit";                          break;
        }

        ItemStack tStack = BW_Meta_Items.NEWCIRCUITS.addCircuit(aID,aName,tooltip,aTier);

        if (ConfigHandler.experimentalThreadedLoader)
            OreDictAdder.addToMap(new Pair<>((aOreDictPrefix + additionalOreDictData).replaceAll(" ",""), tStack));
        else
            GT_OreDictUnificator.registerOre((aOreDictPrefix + additionalOreDictData).replaceAll(" ",""), tStack);
    }

    public static class BW_GT_MetaGenCircuits extends BW_Meta_Items.BW_GT_MetaGen_Item_Hook{


        public BW_GT_MetaGenCircuits() {
            super("bwMetaGeneratedItem0", (short) 0, (short) 32766);
        }

        public final ItemStack addCircuit(int aID, String aEnglish, String aToolTip, int tier){
            CircuitImprintLoader.bwCircuitTagMap.put(new CircuitData(BW_Util.getMachineVoltageFromTier(tier-2),0,(byte)tier), CircuitImprintLoader.getTagFromStack(new ItemStack(BW_Meta_Items.NEWCIRCUITS,1,aID)));
            return this.addItem(aID, aEnglish, aToolTip,SubTag.NO_UNIFICATION);
        }

        public final void modifyIIconIndex(int aID, IIcon icon){
            this.mIconList[aID][0] = icon;
        }

        public final ItemStack getStack(int... meta_amount){
            ItemStack ret = new ItemStack(this);
            if (meta_amount.length <= 0 || meta_amount.length > 2)
                return ret;
            if (meta_amount.length == 1) {
                ret.setItemDamage(meta_amount[0]);
                return ret;
            }
            ret.setItemDamage(meta_amount[0]);
            ret.stackSize=meta_amount[1];
            return ret;
        }
    }

    private static class BW_GT_MetaGen_Item_Hook extends GT_MetaGenerated_Item{
        private BW_GT_MetaGen_Item_Hook(String aUnlocalized, short aOffset, short aItemAmount) {
            super(aUnlocalized, aOffset, aItemAmount);
        }
    }
}
