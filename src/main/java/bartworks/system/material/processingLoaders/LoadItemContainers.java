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

package bartworks.system.material.processingLoaders;

import bartworks.system.material.BWNonMetaMaterialItems;
import gregtech.api.items.ItemRadioactiveCellIC;
import gregtech.common.items.ItemDepletedCell;

public class LoadItemContainers {

    public static void run() {
        BWNonMetaMaterialItems.Depleted_Tiberium_1
            .set(new ItemDepletedCell("TiberiumcellDep", "Fuel Rod (Depleted Tiberium)", 1));
        BWNonMetaMaterialItems.Depleted_Tiberium_2
            .set(new ItemDepletedCell("Double_TiberiumcellDep", "Dual Fuel Rod (Depleted Tiberium)", 1));
        BWNonMetaMaterialItems.Depleted_Tiberium_4
            .set(new ItemDepletedCell("Quad_TiberiumcellDep", "Quad Fuel Rod (Depleted Tiberium)", 1));
        BWNonMetaMaterialItems.TiberiumCell_1.set(
            new ItemRadioactiveCellIC(
                "Tiberiumcell",
                "Fuel Rod (Tiberium)",
                1,
                50000,
                2F,
                1,
                0.5F,
                BWNonMetaMaterialItems.Depleted_Tiberium_1.get(1),
                false));
        BWNonMetaMaterialItems.TiberiumCell_2.set(
            new ItemRadioactiveCellIC(
                "Double_Tiberiumcell",
                "Dual Fuel Rod (Tiberium)",
                2,
                50000,
                2F,
                1,
                0.5F,
                BWNonMetaMaterialItems.Depleted_Tiberium_2.get(1),
                false));
        BWNonMetaMaterialItems.TiberiumCell_4.set(
            new ItemRadioactiveCellIC(
                "Quad_Tiberiumcell",
                "Quad Fuel Rod (Tiberium)",
                4,
                50000,
                2F,
                1,
                0.5F,
                BWNonMetaMaterialItems.Depleted_Tiberium_4.get(1),
                false));
        BWNonMetaMaterialItems.Depleted_TheCoreCell
            .set(new ItemDepletedCell("Core_Reactor_CellDep", "Depleted \"The Core\" Cell", 32));
        BWNonMetaMaterialItems.TheCoreCell.set(
            new ItemRadioactiveCellIC(
                "Core_Reactor_Cell",
                "\"The Core\" Cell",
                32,
                100000,
                8F,
                32,
                1F,
                BWNonMetaMaterialItems.Depleted_TheCoreCell.get(1),
                false));
    }
}
