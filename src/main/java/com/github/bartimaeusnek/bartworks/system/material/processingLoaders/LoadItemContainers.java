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

package com.github.bartimaeusnek.bartworks.system.material.processingLoaders;

import com.github.bartimaeusnek.bartworks.system.material.BW_NonMeta_MaterialItems;
import gregtech.api.items.GT_RadioactiveCellIC_Item;
import gregtech.common.items.GT_DepletetCell_Item;

public class LoadItemContainers {

    public static void run() {
        BW_NonMeta_MaterialItems.Depleted_Tiberium_1.set(
                new GT_DepletetCell_Item("TiberiumcellDep", "Fuel Rod (Depleted Tiberium)", 1));
        BW_NonMeta_MaterialItems.Depleted_Tiberium_2.set(
                new GT_DepletetCell_Item("Double_TiberiumcellDep", "Dual Fuel Rod (Depleted Tiberium)", 1));
        BW_NonMeta_MaterialItems.Depleted_Tiberium_4.set(
                new GT_DepletetCell_Item("Quad_TiberiumcellDep", "Quad Fuel Rod (Depleted Tiberium)", 1));
        BW_NonMeta_MaterialItems.TiberiumCell_1.set(new GT_RadioactiveCellIC_Item(
                "Tiberiumcell",
                "Fuel Rod (Tiberium)",
                1,
                50000,
                2F,
                1,
                0.5F,
                BW_NonMeta_MaterialItems.Depleted_Tiberium_1.get(1),
                false));
        BW_NonMeta_MaterialItems.TiberiumCell_2.set(new GT_RadioactiveCellIC_Item(
                "Double_Tiberiumcell",
                "Dual Fuel Rod (Tiberium)",
                2,
                50000,
                2F,
                1,
                0.5F,
                BW_NonMeta_MaterialItems.Depleted_Tiberium_2.get(1),
                false));
        BW_NonMeta_MaterialItems.TiberiumCell_4.set(new GT_RadioactiveCellIC_Item(
                "Quad_Tiberiumcell",
                "Quad Fuel Rod (Tiberium)",
                4,
                50000,
                2F,
                1,
                0.5F,
                BW_NonMeta_MaterialItems.Depleted_Tiberium_4.get(1),
                false));
        BW_NonMeta_MaterialItems.Depleted_TheCoreCell.set(
                new GT_DepletetCell_Item("Core_Reactor_CellDep", "Depleted \"The Core\" Cell", 32));
        BW_NonMeta_MaterialItems.TheCoreCell.set(new GT_RadioactiveCellIC_Item(
                "Core_Reactor_Cell",
                "\"The Core\" Cell",
                32,
                100000,
                8F,
                32,
                1F,
                BW_NonMeta_MaterialItems.Depleted_TheCoreCell.get(1),
                false));
    }
}
