/*
 * Copyright (c) 2018-2019 bartimaeusnek
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

package com.github.bartimaeusnek.bartworks.util;

import net.minecraft.item.EnumRarity;

public class BioPlasmid extends BioData {

    public static final BioPlasmid NULLPLASMID = convertDataToPlasmid(BioData.convertBioDNAToBioData(BioDNA.NULLDNA));

    private BioPlasmid(String name, int ID, EnumRarity rarity) {
        super(name, ID, rarity);
    }

    protected BioPlasmid(BioData bioData) {
        super(bioData);
        this.name = bioData.name;
        this.ID = bioData.ID;
        this.rarity = bioData.rarity;
    }

    public static BioPlasmid convertDataToPlasmid(BioData bioData) {
        return new BioPlasmid(bioData);
    }

    public static BioPlasmid createAndRegisterBioPlasmid(String aName, EnumRarity rarity) {
        BioData ret = BioData.createAndRegisterBioData(aName, rarity);
        return new BioPlasmid(ret);
    }

    public static BioPlasmid createAndRegisterBioPlasmid(String aName, EnumRarity rarity, int chance, int tier) {
        BioData ret = BioData.createAndRegisterBioData(aName, rarity, chance, tier);
        return new BioPlasmid(ret);
    }

    @Override
    public String toString() {
        return "BioPlasmid{" + "name='" + name + '\'' + ", ID=" + ID + '}';
    }
}
