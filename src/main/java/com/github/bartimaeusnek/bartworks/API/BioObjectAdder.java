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

import com.github.bartimaeusnek.bartworks.util.BioCulture;
import com.github.bartimaeusnek.bartworks.util.BioDNA;
import com.github.bartimaeusnek.bartworks.util.BioData;
import com.github.bartimaeusnek.bartworks.util.BioPlasmid;
import net.minecraft.item.EnumRarity;

import java.awt.*;

public final class BioObjectAdder {

//    @Deprecated
//    public static BioCulture createAndRegisterBioCulture(Color color, String name, long ID, BioPlasmid plasmid, BioDNA dna, EnumRarity rarity){
//        return BioCulture.createAndRegisterBioCulture(color,name,ID,plasmid,dna,rarity);
//    }

    /**
     * @param color     the color of the Culture
     * @param name      the name of the Culture
     * @param plasmid   the cultures plasmid, get it from createAndRegisterBioPlasmid
     * @param dna       the cultures dna, get it from createAndRegisterBioDNA
     * @param breedable if the culture can be inserted into the BacterialVat
     * @param rarity    visual
     * @return
     */
    public static BioCulture createAndRegisterBioCulture(Color color, String name, BioPlasmid plasmid, BioDNA dna, EnumRarity rarity, boolean breedable) {
        return BioCulture.createAndRegisterBioCulture(color, name, plasmid, dna, rarity, breedable);
    }

    /**
     * rarity inherits from dna
     *
     * @param color     the color of the Culture
     * @param name      the name of the Culture
     * @param plasmid   the cultures plasmid, get it from createAndRegisterBioPlasmid
     * @param dna       the cultures dna, get it from createAndRegisterBioDNA
     * @param breedable if the culture can be inserted into the BacterialVat
     * @return
     */
    public static BioCulture createAndRegisterBioCulture(Color color, String name, BioPlasmid plasmid, BioDNA dna, boolean breedable) {
        return BioCulture.createAndRegisterBioCulture(color, name, plasmid, dna, breedable);
    }

    /**
     * unspecific Biodata that can be converted into DNA and Plasmid with the propper methodes
     *
     * @param aName  the name of the Biodata
     * @param rarity visual only
     * @param chance the chanche to extract this BioData
     * @param tier   the tier of this BioData 0=HV, 1=EV etc.
     * @return
     */
    public static BioData createAndRegisterBioData(String aName, EnumRarity rarity, int chance, int tier) {
        return BioData.createAndRegisterBioData(aName, rarity, chance, tier);
    }

    /**
     * Default Constructor for HV Tier DNA with 75% extraction rate
     *
     * @param aName  Name of the DNA String
     * @param rarity visual
     * @return
     */
    public static BioDNA createAndRegisterBioDNA(String aName, EnumRarity rarity) {
        return BioDNA.createAndRegisterBioDNA(aName, rarity);
    }

    /**
     * Default Constructor for HV Tier Plasmid with 75% extraction rate
     *
     * @param aName  Name of the Plasmid
     * @param rarity visual
     * @return
     */
    public static BioPlasmid createAndRegisterBioPlasmid(String aName, EnumRarity rarity) {
        return BioPlasmid.createAndRegisterBioPlasmid(aName, rarity);
    }

    /**
     * @param aName  Name of the DNA String
     * @param rarity visual
     * @param chance chanche of extracting
     * @param tier   tier needed to extract 0=HV, 1=EV etc.
     * @return
     */
    public static BioDNA createAndRegisterBioDNA(String aName, EnumRarity rarity, int chance, int tier) {
        return BioDNA.createAndRegisterBioDNA(aName, rarity, chance, tier);
    }

    /**
     * @param aName  Name of the Plasmid
     * @param rarity visual
     * @param chance chanche of extracting
     * @param tier   tier needed to extract 0=HV, 1=EV etc.
     * @return
     */
    public static BioPlasmid createAndRegisterBioPlasmid(String aName, EnumRarity rarity, int chance, int tier) {
        return BioPlasmid.createAndRegisterBioPlasmid(aName, rarity, chance, tier);
    }


}
