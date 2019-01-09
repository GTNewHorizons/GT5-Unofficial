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

package com.github.bartimaeusnek.bartworks.API;

import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;
import com.github.bartimaeusnek.bartworks.util.BioCulture;
import com.github.bartimaeusnek.bartworks.util.BioDNA;
import com.github.bartimaeusnek.bartworks.util.BioData;
import com.github.bartimaeusnek.bartworks.util.BioPlasmid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collection;

public final class BioObjectGetter {

    public static BioCulture getBioCulture(String aName) {
        return BioCulture.getBioCulture(aName);
    }

    public static NBTTagCompound getNBTTagFromCulture(BioCulture bioCulture) {
        return BioCulture.getNBTTagFromCulture(bioCulture);
    }

    public static NBTTagCompound getNBTTagFromBioPlasmid(BioPlasmid bioPlasmid) {
        return getNBTTagFromBioData(convertBioPlasmidToData(bioPlasmid));
    }

    public static NBTTagCompound getNBTTagFromBioDNA(BioDNA bioDNA) {
        return getNBTTagFromBioData(convertBioDNAtoData(bioDNA));
    }

    public static NBTTagCompound getNBTTagFromBioData(BioData bioData) {
        return BioData.getNBTTagFromBioData(bioData);
    }

    public static BioDNA convertDataToDNA(BioData bioData) {
        return BioDNA.convertDataToDNA(bioData);
    }

    public static BioPlasmid convertDataToPlasmid(BioData bioData) {
        return BioPlasmid.convertDataToPlasmid(bioData);
    }

    public static BioData convertBioPlasmidToData(BioPlasmid bioPlasmid) {
        return BioData.convertBioPlasmidToBioData(bioPlasmid);
    }

    public static BioData convertDataToDNA(BioDNA bioData) {
        return BioData.convertBioDNAToBioData(bioData);
    }

    public static BioData convertBioDNAtoData(BioDNA bioDNA) {
        return BioData.convertBioDNAToBioData(bioDNA);
    }

    public static BioPlasmid convertBioDNAtoBioPlasmid(BioDNA bioDNA) {
        return convertDataToPlasmid(convertBioDNAtoData(bioDNA));
    }

    public static BioDNA convertBioPlasmidtoBioDNA(BioPlasmid bioPlasmid) {
        return convertDataToDNA(convertBioPlasmidToData(bioPlasmid));
    }

    public static Collection<ItemStack> getAllPetriDishes() {
        return BioItemList.getAllPetriDishes();
    }

    public static Collection<ItemStack> getAllDNASampleFlasks() {
        return BioItemList.getAllDNASampleFlasks();
    }

    public static Collection<ItemStack> getAllPlasmidCells() {
        return BioItemList.getAllPlasmidCells();
    }

    public static ItemStack getDNASampleFlask(BioDNA dna) {
        return BioItemList.getDNASampleFlask(dna);
    }

    public static ItemStack getPetriDish(BioCulture culture) {
        return BioItemList.getPetriDish(culture);
    }

    public static ItemStack getPlasmidCell(BioPlasmid plasmid) {
        return BioItemList.getPlasmidCell(plasmid);
    }

    /**
     * 1 - DetergentPowder
     * 2 - Agarose
     * 3 - IncubationModule
     * 4 - Plasma Membrane
     *
     * @param selection
     * @return the selected Item
     */
    public static ItemStack getOther(int selection) {
        return BioItemList.getOther(selection);
    }

}
