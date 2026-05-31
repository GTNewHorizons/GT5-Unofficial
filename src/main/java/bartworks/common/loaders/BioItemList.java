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

package bartworks.common.loaders;

import java.util.Collection;
import java.util.HashSet;

import bartworks.API.enums.BioCultureEnum;
import bartworks.API.enums.BioDataEnum;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import bartworks.common.items.ItemLabModule;
import bartworks.common.items.ItemLabParts;
import bartworks.util.BioCulture;
import bartworks.util.BioData;
import cpw.mods.fml.common.registry.GameRegistry;

public class BioItemList {

    private BioItemList() {}

    private static final Item mItemBioLabParts = new ItemLabModule(
        new String[] { "DNAExtractionModule", "PCRThermoclyclingModule", "PlasmidSynthesisModule",
            "TransformationModule", "ClonalCellularSynthesisModule" });
    public static final ItemStack[] mBioLabParts = { new ItemStack(BioItemList.mItemBioLabParts),
        new ItemStack(BioItemList.mItemBioLabParts, 1, 1), new ItemStack(BioItemList.mItemBioLabParts, 1, 2),
        new ItemStack(BioItemList.mItemBioLabParts, 1, 3), new ItemStack(BioItemList.mItemBioLabParts, 1, 4) };
    public static final Item vanillaBioLabParts = new ItemLabParts(
        new String[] { "petriDish", "DNASampleFlask", "PlasmidCell", "DetergentPowder", "Agarose", "IncubationModule",
            "PlasmaMembrane" });

    public static void registerBioItems() {
        GameRegistry.registerItem(BioItemList.mItemBioLabParts, "BioLabModules");
        GameRegistry.registerItem(BioItemList.vanillaBioLabParts, "BioLabParts");
    }
}
