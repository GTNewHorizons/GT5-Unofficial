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

package com.github.bartimaeusnek.bartworks.API;

import com.github.bartimaeusnek.bartworks.util.BioCulture;
import com.github.bartimaeusnek.bartworks.util.BioDNA;
import com.github.bartimaeusnek.bartworks.util.BioData;
import com.github.bartimaeusnek.bartworks.util.BioPlasmid;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.objects.GT_Fluid;
import gregtech.api.util.GT_Utility;
import java.awt.*;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("ALL")
public final class BioObjectAdder {

    //    @Deprecated
    //    public static BioCulture createAndRegisterBioCulture(Color color, String name, long ID, BioPlasmid plasmid,
    // BioDNA dna, EnumRarity rarity){
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
    public static BioCulture createAndRegisterBioCulture(
            Color color, String name, BioPlasmid plasmid, BioDNA dna, EnumRarity rarity, boolean breedable) {
        if (BioCulture.BIO_CULTURE_ARRAY_LIST.size() > 1)
            return BioCulture.createAndRegisterBioCulture(color, name, plasmid, dna, rarity, breedable);
        else
            new Exception(
                            "Too Early to register a BioCulture! You MUST run this either after:bartworks OR in the init Phase!")
                    .printStackTrace();
        return null;
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
    public static BioCulture createAndRegisterBioCulture(
            Color color, String name, BioPlasmid plasmid, BioDNA dna, boolean breedable) {
        if (BioCulture.BIO_CULTURE_ARRAY_LIST.size() > 1)
            return BioCulture.createAndRegisterBioCulture(color, name, plasmid, dna, breedable);
        else
            new Exception(
                            "Too Early to register a BioCulture! You MUST run this either after:bartworks OR in the init Phase!")
                    .printStackTrace();
        return null;
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
        if (BioData.BIO_DATA_ARRAY_LIST.size() > 1)
            return BioData.createAndRegisterBioData(aName, rarity, chance, tier);
        new Exception("Too Early to register a BioData! You MUST run this either after:bartworks OR in the init Phase!")
                .printStackTrace();
        return null;
    }

    /**
     * Default Constructor for HV Tier DNA with 75% extraction rate
     *
     * @param aName  Name of the DNA String
     * @param rarity visual
     * @return
     */
    public static BioDNA createAndRegisterBioDNA(String aName, EnumRarity rarity) {
        if (BioData.BIO_DATA_ARRAY_LIST.size() > 1) return BioDNA.createAndRegisterBioDNA(aName, rarity);
        new Exception("Too Early to register a BioData! You MUST run this either after:bartworks OR in the init Phase!")
                .printStackTrace();
        return null;
    }

    /**
     * Default Constructor for HV Tier Plasmid with 75% extraction rate
     *
     * @param aName  Name of the Plasmid
     * @param rarity visual
     * @return
     */
    public static BioPlasmid createAndRegisterBioPlasmid(String aName, EnumRarity rarity) {
        if (BioData.BIO_DATA_ARRAY_LIST.size() > 1) return BioPlasmid.createAndRegisterBioPlasmid(aName, rarity);
        new Exception("Too Early to register a BioData! You MUST run this either after:bartworks OR in the init Phase!")
                .printStackTrace();
        return null;
    }

    /**
     * @param aName  Name of the DNA String
     * @param rarity visual
     * @param chance chanche of extracting
     * @param tier   tier needed to extract 0=HV, 1=EV etc.
     * @return
     */
    public static BioDNA createAndRegisterBioDNA(String aName, EnumRarity rarity, int chance, int tier) {
        if (BioData.BIO_DATA_ARRAY_LIST.size() > 1) return BioDNA.createAndRegisterBioDNA(aName, rarity, chance, tier);
        new Exception("Too Early to register a BioData! You MUST run this either after:bartworks OR in the init Phase!")
                .printStackTrace();
        return null;
    }

    /**
     * @param aName  Name of the Plasmid
     * @param rarity visual
     * @param chance chanche of extracting
     * @param tier   tier needed to extract 0=HV, 1=EV etc.
     * @return
     */
    public static BioPlasmid createAndRegisterBioPlasmid(String aName, EnumRarity rarity, int chance, int tier) {
        if (BioData.BIO_DATA_ARRAY_LIST.size() > 1)
            return BioPlasmid.createAndRegisterBioPlasmid(aName, rarity, chance, tier);
        new Exception("Too Early to register a BioData! You MUST run this either after:bartworks OR in the init Phase!")
                .printStackTrace();
        return null;
    }

    /**
     * @param voltageTier (i.e. 6 for LuV, 7 for ZPM, only intresting for LuV+)
     * @return the propper Bacteria Tier (at least 0)
     */
    public static int getBacteriaTierFromVoltageTier(int voltageTier) {
        return Math.max(voltageTier - 6, 0);
    }

    /**
     * If you get NPE's related to BioCultures (most likely because of Load Order or creating BioCultures after the postinit Phase) execute this.
     */
    public static void regenerateBioFluids() {
        FluidStack dnaFluid = LoaderReference.gendustry
                ? FluidRegistry.getFluidStack("liquiddna", 100)
                : Materials.Biomass.getFluid(100L);
        for (BioCulture B : BioCulture.BIO_CULTURE_ARRAY_LIST) {
            if (B.getFluidNotSet()) {
                B.setFluid(new GT_Fluid(
                        B.getName().replaceAll(" ", "").toLowerCase() + "fluid", "molten.autogenerated", new short[] {
                            (short) B.getColor().getRed(),
                            (short) B.getColor().getBlue(),
                            (short) B.getColor().getGreen()
                        }));
                if (!FluidRegistry.registerFluid(B.getFluid()))
                    new Exception("FAILED TO REGISTER FLUID FOR: " + B.getName()).printStackTrace();
                GT_Values.RA.addCentrifugeRecipe(
                        GT_Utility.getIntegratedCircuit(10),
                        GT_Values.NI,
                        new FluidStack(B.getFluid(), 1000),
                        dnaFluid,
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Values.NI,
                        null,
                        500,
                        120);
            }
        }
    }
}
