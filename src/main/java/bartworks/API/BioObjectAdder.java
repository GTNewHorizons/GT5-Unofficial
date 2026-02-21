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

package bartworks.API;

import static gregtech.api.enums.Mods.Gendustry;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.awt.Color;

import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import bartworks.util.BioCulture;
import bartworks.util.BioDNA;
import bartworks.util.BioData;
import bartworks.util.BioPlasmid;
import gregtech.api.enums.FluidState;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.fluid.GTFluidFactory;
import gregtech.api.util.GTModHandler;

public final class BioObjectAdder {

    /**
     * @param color     the color of the Culture
     * @param name      the name of the Culture
     * @param plasmid   the cultures plasmid, get it from createAndRegisterBioPlasmid
     * @param dna       the cultures dna, get it from createAndRegisterBioDNA
     * @param breedable if the culture can be inserted into the BacterialVat
     * @param rarity    visual
     * @return
     */
    public static @Nullable BioCulture createAndRegisterBioCulture(Color color, String name, BioPlasmid plasmid,
        BioDNA dna, EnumRarity rarity, boolean breedable) {
        if (BioCulture.BIO_CULTURE_ARRAY_LIST.size() > 1)
            return BioCulture.createAndRegisterBioCulture(color, name, plasmid, dna, rarity, breedable);
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
    public static @Nullable BioCulture createAndRegisterBioCulture(Color color, String name, BioPlasmid plasmid,
        BioDNA dna, boolean breedable) {
        if (BioCulture.BIO_CULTURE_ARRAY_LIST.size() > 1)
            return BioCulture.createAndRegisterBioCulture(color, name, plasmid, dna, breedable);
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
    public static @Nullable BioData createAndRegisterBioData(String aName, EnumRarity rarity, int chance, int tier) {
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
    public static @Nullable BioDNA createAndRegisterBioDNA(String aName, EnumRarity rarity) {
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
    public static @Nullable BioPlasmid createAndRegisterBioPlasmid(String aName, EnumRarity rarity) {
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
    public static @Nullable BioDNA createAndRegisterBioDNA(String aName, EnumRarity rarity, int chance, int tier) {
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
    public static @Nullable BioPlasmid createAndRegisterBioPlasmid(String aName, EnumRarity rarity, int chance,
        int tier) {
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
     * If you get NPE's related to BioCultures (most likely because of Load Order or creating BioCultures after the
     * postinit Phase) execute this.
     */
    public static void regenerateBioFluids() {
        FluidStack dnaFluid = Gendustry.isModLoaded() ? GTModHandler.getLiquidDNA(100)
            : Materials.Biomass.getFluid(100L);
        for (BioCulture B : BioCulture.BIO_CULTURE_ARRAY_LIST) {
            if (B.getFluidNotSet()) {
                B.setFluid(
                    GTFluidFactory.builder(
                        B.getName()
                            .replace(" ", "")
                            .toLowerCase() + "fluid")
                        .withTextureName("molten.autogenerated")
                        .withColorRGBA(
                            new short[] { (short) B.getColor()
                                .getRed(),
                                (short) B.getColor()
                                    .getBlue(),
                                (short) B.getColor()
                                    .getGreen() })
                        .withStateAndTemperature(FluidState.LIQUID, 300)
                        .buildAndRegister()
                        .addLocalizedName()
                        .asFluid());

                GTValues.RA.stdBuilder()
                    .circuit(10)
                    .fluidInputs(new FluidStack(B.getFluid(), 1000))
                    .fluidOutputs(dnaFluid)
                    .duration(25 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(centrifugeRecipes);
            }
        }
    }
}
