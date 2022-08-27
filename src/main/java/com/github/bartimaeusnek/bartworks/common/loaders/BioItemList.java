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

package com.github.bartimaeusnek.bartworks.common.loaders;

import com.github.bartimaeusnek.bartworks.common.items.LabModule;
import com.github.bartimaeusnek.bartworks.common.items.LabParts;
import com.github.bartimaeusnek.bartworks.util.BioCulture;
import com.github.bartimaeusnek.bartworks.util.BioDNA;
import com.github.bartimaeusnek.bartworks.util.BioData;
import com.github.bartimaeusnek.bartworks.util.BioPlasmid;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BioItemList {

    private BioItemList() {}

    private static final Item mItemBioLabParts = new LabModule(new String[] {
        "DNAExtractionModule",
        "PCRThermoclyclingModule",
        "PlasmidSynthesisModule",
        "TransformationModule",
        "ClonalCellularSynthesisModule"
    });
    public static final ItemStack[] mBioLabParts = {
        new ItemStack(BioItemList.mItemBioLabParts),
        new ItemStack(BioItemList.mItemBioLabParts, 1, 1),
        new ItemStack(BioItemList.mItemBioLabParts, 1, 2),
        new ItemStack(BioItemList.mItemBioLabParts, 1, 3),
        new ItemStack(BioItemList.mItemBioLabParts, 1, 4)
    };
    private static final Item vanillaBioLabParts = new LabParts(new String[] {
        "petriDish", "DNASampleFlask", "PlasmidCell", "DetergentPowder", "Agarose", "IncubationModule", "PlasmaMembrane"
    });

    public static void registerBioItems() {
        GameRegistry.registerItem(BioItemList.mItemBioLabParts, "BioLabModules");
        GameRegistry.registerItem(BioItemList.vanillaBioLabParts, "BioLabParts");
    }

    public static Collection<ItemStack> getAllPetriDishes() {
        HashSet<ItemStack> ret = new HashSet<>();
        for (BioCulture Culture : BioCulture.BIO_CULTURE_ARRAY_LIST) {
            ret.add(BioItemList.getPetriDish(Culture));
        }
        return ret;
    }

    public static Collection<ItemStack> getAllDNASampleFlasks() {
        HashSet<ItemStack> ret = new HashSet<>();
        for (BioData dna : BioData.BIO_DATA_ARRAY_LIST) {
            ret.add(BioItemList.getDNASampleFlask(BioDNA.convertDataToDNA(dna)));
        }
        return ret;
    }

    public static Collection<ItemStack> getAllPlasmidCells() {
        HashSet<ItemStack> ret = new HashSet<>();
        for (BioData dna : BioData.BIO_DATA_ARRAY_LIST) {
            ret.add(BioItemList.getPlasmidCell(BioPlasmid.convertDataToPlasmid(dna)));
        }
        return ret;
    }

    public static ItemStack getPetriDish(BioCulture Culture) {
        if (Culture == null) return new ItemStack(BioItemList.vanillaBioLabParts);
        ItemStack ret = new ItemStack(BioItemList.vanillaBioLabParts);
        ret.setTagCompound(BioCulture.getNBTTagFromCulture(Culture));
        return ret;
    }

    public static ItemStack getDNASampleFlask(BioDNA dna) {
        if (dna == null) return new ItemStack(BioItemList.vanillaBioLabParts, 1, 1);

        ItemStack ret = new ItemStack(BioItemList.vanillaBioLabParts, 1, 1);
        ret.setTagCompound(BioData.getNBTTagFromBioData(dna));
        return ret;
    }

    public static ItemStack getPlasmidCell(BioPlasmid plasmid) {
        if (plasmid == null) return new ItemStack(BioItemList.vanillaBioLabParts, 1, 2);
        ItemStack ret = new ItemStack(BioItemList.vanillaBioLabParts, 1, 2);
        ret.setTagCompound(BioData.getNBTTagFromBioData(plasmid));
        return ret;
    }

    /**
     * 1 - Detergent Powder
     * 2 - Agarose
     * 3 - Incubation Module
     * 4 - Plasma Membrane
     * others are null
     *
     * @param selection see above
     * @return the selected Item
     */
    public static ItemStack getOther(int selection) {
        if (selection < 1 || selection > 4) return null;

        return new ItemStack(BioItemList.vanillaBioLabParts, 1, 2 + selection);
    }
}
