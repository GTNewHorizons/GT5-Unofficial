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

import net.minecraft.item.ItemStack;

import bartworks.API.enums.BioCultureEnum;
import bartworks.API.enums.BioDataEnum;
import bartworks.common.items.ItemLabParts;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import gregtech.api.enums.ItemList;

public class BioLabLoader {

    public static void run(FMLInitializationEvent event) {
        FluidLoader.run(event);
        BioItemList.registerBioItems();
        BioCultureEnum.registerAllCultures();
        BioDataEnum.registerAllDNAItemStacks();
        BioDataEnum.registerAllPlasmidItemStacks();

        ItemList.EmptyPetriDish.set(new ItemStack(BioItemList.vanillaBioLabParts, 1, ItemLabParts.PETRI_DISH));
        ItemList.EmptyDNAFlask.set(new ItemStack(BioItemList.vanillaBioLabParts, 1, ItemLabParts.DNA_FLASK));
        ItemList.EmptyPlasmid.set(new ItemStack(BioItemList.vanillaBioLabParts, 1, ItemLabParts.PLASMID_CELL));
        ItemList.DetergentPowder.set(new ItemStack(BioItemList.vanillaBioLabParts, 1, ItemLabParts.DETERGENT));
        ItemList.Agarose.set(new ItemStack(BioItemList.vanillaBioLabParts, 1, ItemLabParts.AGAROSE));
        ItemList.PlasmaMembrane.set(new ItemStack(BioItemList.vanillaBioLabParts, 1, ItemLabParts.PLASMA_MEMBRANE));
        BioRecipeLoader.run();
    }
}
