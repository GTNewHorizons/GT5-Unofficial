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

import bartworks.API.enums.BioCultureEnum;
import bartworks.API.enums.BioDataEnum;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import gregtech.api.enums.ItemList;
import net.minecraft.item.ItemStack;

public class BioLabLoader {

    public static void run(FMLInitializationEvent event) {
        FluidLoader.run(event);
        BioItemList.registerBioItems();
        BioCultureEnum.registerAllCultures();
        BioDataEnum.registerAllDNAItemStacks();
        BioDataEnum.registerAllPlasmidItemStacks();


        ItemList.EmptyPetriDish.set(new ItemStack(BioItemList.vanillaBioLabParts, 1, 0));
        ItemList.EmptyDNAFlask.set(new ItemStack(BioItemList.vanillaBioLabParts, 1, 1));
        ItemList.EmptyPlasmid.set(new ItemStack(BioItemList.vanillaBioLabParts, 1, 2));
        ItemList.DetergentPowder.set(new ItemStack(BioItemList.vanillaBioLabParts, 1, 3));
        ItemList.Agarose.set(new ItemStack(BioItemList.vanillaBioLabParts, 1, 4));
        ItemList.IncubationModule.set(new ItemStack(BioItemList.vanillaBioLabParts, 1, 5));
        ItemList.PlasmaMembrane.set(new ItemStack(BioItemList.vanillaBioLabParts, 1, 6));
        BioRecipeLoader.run();
    }
}
