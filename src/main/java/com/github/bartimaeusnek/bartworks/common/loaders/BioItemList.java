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

package com.github.bartimaeusnek.bartworks.common.loaders;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_GlasBlocks;
import com.github.bartimaeusnek.bartworks.common.items.BW_ItemBlocks;
import com.github.bartimaeusnek.bartworks.common.items.LabModule;
import com.github.bartimaeusnek.bartworks.common.items.LabParts;
import com.github.bartimaeusnek.bartworks.util.BioCulture;
import com.github.bartimaeusnek.bartworks.util.BioDNA;
import com.github.bartimaeusnek.bartworks.util.BioData;
import com.github.bartimaeusnek.bartworks.util.BioPlasmid;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.HashSet;


public class BioItemList {


    public static final Block[] bw_glasses = {
            new BW_GlasBlocks("BW_GlasBlocks", new String[]{
                    MainMod.modID + ":BoronSilicateGlassBlock",
                    MainMod.modID + ":NickelReinforcedBoronSilicateGlassBlock",
                    MainMod.modID + ":TungstenReinforcedBoronSilicateGlassBlock",
                    MainMod.modID + ":ChromeReinforcedBoronSilicateGlassBlock",
                    MainMod.modID + ":IridiumReinforcedBoronSilicateGlassBlock",
                    MainMod.modID + ":OsmiumReinforcedBoronSilicateGlassBlock",
            }, MainMod.BioTab
            )
    };

    private static final Item mItemBioLabParts = new LabModule(new String[]{"DNAExtractionModule", "PCRThermoclyclingModule", "PlasmidSynthesisModule", "TransformationModule", "ClonalCellularSynthesisModule"});
    public static final ItemStack[] mBioLabParts = {new ItemStack(mItemBioLabParts), new ItemStack(mItemBioLabParts, 1, 1), new ItemStack(mItemBioLabParts, 1, 2), new ItemStack(mItemBioLabParts, 1, 3), new ItemStack(mItemBioLabParts, 1, 4)};
    private static final Item vanillaBioLabParts = new LabParts(new String[]{"petriDish", "DNASampleFlask", "PlasmidCell", "DetergentPowder", "Agarose", "IncubationModule", "PlasmaMembrane"});

    public BioItemList() {
        GameRegistry.registerItem(mItemBioLabParts, "BioLabModules");
        GameRegistry.registerItem(vanillaBioLabParts, "BioLabParts");
        GameRegistry.registerBlock(bw_glasses[0], BW_ItemBlocks.class, "BW_GlasBlocks");
        //fixing BorosilicateGlass... -_-'
        Materials.BorosilicateGlass.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_RECYCLING, SubTag.SMELTING_TO_FLUID);
        GT_OreDictUnificator.add(OrePrefixes.block, Materials.BorosilicateGlass, new ItemStack(bw_glasses[0], 1, 0));
    }

    public static Collection<ItemStack> getAllPetriDishes() {
        HashSet<ItemStack> ret = new HashSet<>();
        for (BioCulture Culture : BioCulture.BIO_CULTURE_ARRAY_LIST) {
            ret.add(getPetriDish(Culture));
        }
        return ret;
    }

    public static Collection<ItemStack> getAllDNASampleFlasks() {
        HashSet<ItemStack> ret = new HashSet<>();
        for (BioData dna : BioData.BIO_DATA_ARRAY_LIST) {
            ret.add(getDNASampleFlask(BioDNA.convertDataToDNA(dna)));
        }
        return ret;
    }

    public static Collection<ItemStack> getAllPlasmidCells() {
        HashSet<ItemStack> ret = new HashSet<>();
        for (BioData dna : BioData.BIO_DATA_ARRAY_LIST) {
            ret.add(getPlasmidCell(BioPlasmid.convertDataToPlasmid(dna)));
        }
        return ret;
    }

    public static ItemStack getPetriDish(BioCulture Culture) {
        if (Culture == null)
            return new ItemStack(vanillaBioLabParts);
        ItemStack ret = new ItemStack(vanillaBioLabParts);
        ret.setTagCompound(BioCulture.getNBTTagFromCulture(Culture));
        return ret;
    }

    public static ItemStack getDNASampleFlask(BioDNA dna) {
        if (dna == null)
            return new ItemStack(vanillaBioLabParts, 1, 1);

        ItemStack ret = new ItemStack(vanillaBioLabParts, 1, 1);
        ret.setTagCompound(BioData.getNBTTagFromBioData(dna));
        return ret;
    }

    public static ItemStack getPlasmidCell(BioPlasmid plasmid) {
        if (plasmid == null)
            return new ItemStack(vanillaBioLabParts, 1, 2);
        ItemStack ret = new ItemStack(vanillaBioLabParts, 1, 2);
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
        if (selection < 1 || selection > 4)
            return null;

        return new ItemStack(vanillaBioLabParts, 1, 2 + selection);
    }
}
