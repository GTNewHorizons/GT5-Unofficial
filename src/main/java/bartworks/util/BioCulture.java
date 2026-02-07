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

package bartworks.util;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.Objects;

import bartworks.API.enums.BioCultureEnum;

import gregtech.api.enums.VoltageIndex;
import net.minecraft.item.EnumRarity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.util.GTLanguageManager;

public class BioCulture extends BioData implements IColorModulationContainer {

    public String getLocalisedName() {
        return GTLanguageManager.getTranslation(this.getName());
    }

    public void setLocalisedName(String localisedName) {
        GTLanguageManager.addStringLocalization(this.getName(), localisedName);
    }

    Color color;
    BioData plasmid;
    BioData dDNA;
    boolean bBreedable;
    Fluid mFluid;

    public BioCulture(BioCultureEnum culture){
        super(culture.name, culture.id, culture.rarity, 75_00, VoltageIndex.ULV);
        this.color = culture.color;
        this.plasmid = culture.plasmid.getBioData();
        this.dDNA = culture.dna.getBioData();
        this.bBreedable = culture.breedable;
    }

    public BioCulture(Color color, String name, int ID, BioData plasmid, BioData dDNA, EnumRarity rarity,
        boolean bBreedable) {
        super(name, ID, rarity, 75_00, VoltageIndex.ULV);
        this.color = color;
        this.plasmid = plasmid;
        this.dDNA = dDNA;
        this.bBreedable = bBreedable;
    }

    public static NBTTagCompound getNBTTagFromCulture(BioCulture bioCulture) {
        String name = bioCulture != null ? bioCulture.name : BioCultureEnum.NullBioCulture.name;
        NBTTagCompound ret = new NBTTagCompound();
        ret.setString("Name", name);
        return ret;
    }

    public static BioCulture getBioCultureFromNBTTag(NBTTagCompound tag) {
        if (tag == null || !tag.hasKey("Name")) return null;
        return BioCultureEnum.LOOKUPS_BY_NAME.getOrDefault(tag.getString("Name"), BioCultureEnum.NullBioCulture).bioCulture;
    }

    public static BioCulture getBioCulture(String Name) {
        if (Name == null || Name.isEmpty()) return null;
        for (BioCulture b : BioCultureEnum.BIO_CULTURES) if (b.name.equals(Name)) return b;
        return null;
    }

    public static BioCulture getBioCulture(BioData DNA) {
        for (BioCulture b : BioCultureEnum.BIO_CULTURES) if (b.getdDNA()
            .equals(DNA)) return b;
        return null;
    }

    public Fluid getFluid() {
        if (this.mFluid == null)
            throw new IllegalStateException("Fluid has not been set yet! The issuring Culture is: " + this.name);
        return this.mFluid;
    }

    public void setFluid(Fluid mFluid) {
        this.mFluid = mFluid;
    }

    public boolean getFluidNotSet() {
        return this.mFluid == null && this.isBreedable();
    }

    public boolean isBreedable() {
        return this.bBreedable;
    }

    public void setbBreedable(boolean bBreedable) {
        this.bBreedable = bBreedable;
    }

    public int getColorRGB() {
        return BWColorUtil
            .getColorFromRGBArray(new int[] { this.color.getRed(), this.color.getGreen(), this.color.getBlue() });
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public BioData getPlasmid() {
        return this.plasmid;
    }

    public BioCulture setPlasmid(BioData plasmid) {
        return this.checkForExisting(
            new BioCulture(this.color, this.name, this.ID, plasmid, this.dDNA, this.rarity, this.bBreedable));
    }

    private BioCulture checkForExisting(BioCulture culture) {
        if (culture == null) return null;
        for (BioCulture bc : BioCultureEnum.BIO_CULTURES) if (culture.getdDNA()
            .equals(bc.getdDNA())
            && culture.getPlasmid()
                .equals(bc.getPlasmid()))
            return bc;
        return culture;
    }

    public BioData getdDNA() {
        return this.dDNA;
    }

    public BioCulture setdDNA(BioData dDNA) {
        return this.checkForExisting(
            new BioCulture(this.color, this.name, this.ID, this.plasmid, dDNA, this.rarity, this.bBreedable));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass() || !super.equals(o)) return false;
        BioCulture culture = (BioCulture) o;
        return this.isBreedable() == culture.isBreedable() && Objects.equals(this.getColor(), culture.getColor())
            && Objects.equals(this.getPlasmid(), culture.getPlasmid())
            && Objects.equals(this.getdDNA(), culture.getdDNA())
            && Objects.equals(this.mFluid, culture.mFluid);
    }

    @Override
    public int hashCode() {
        return MurmurHash3.murmurhash3_x86_32(
            ByteBuffer.allocate(17)
                .putInt(
                    MurmurHash3.murmurhash3_x86_32(
                        this.getName(),
                        0,
                        this.getName()
                            .length(),
                        31))
                .putInt(this.getColorRGB())
                .putInt(this.getPlasmid().ID)
                .putInt(this.getdDNA().ID)
                .put((byte) (this.isBreedable() ? 1 : 0))
                .array(),
            0,
            17,
            31);
    }

    @Override
    public short[] getRGBA() {
        return new short[] { (short) this.getColor()
            .getRed(),
            (short) this.getColor()
                .getGreen(),
            (short) this.getColor()
                .getBlue(),
            (short) this.getColor()
                .getAlpha() };
    }

    @Override
    public String toString(){
        return String.format("BioCulture(color=%s, name=%s, id=%d, plasmid=%s, dDNA=%s, rarity=%s, bBreedable=%b)",
            this.color.toString(), this.name, this.ID, this.plasmid.toString(), this.dDNA.toString(),
            this.rarity.name(),  this.bBreedable);
    }
}
