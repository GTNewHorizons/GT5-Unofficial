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

package com.github.bartimaeusnek.bartworks.util;


import com.github.bartimaeusnek.bartworks.MainMod;
import net.minecraft.item.EnumRarity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class BioCulture extends BioData {

    public static final ArrayList<BioCulture> BIO_CULTURE_ARRAY_LIST = new ArrayList<BioCulture>();
    public static final BioCulture NULLCULTURE = BioCulture.createAndRegisterBioCulture(Color.BLUE, "", BioPlasmid.NULLPLASMID,BioDNA.NULLDNA, false); //fallback NULL culture, also Blue =)

    Color color;
    BioPlasmid plasmid;
    BioDNA dDNA;
    boolean bBreedable;
    Fluid mFluid;

    protected BioCulture(Color color, String name, int ID, BioPlasmid plasmid, BioDNA dDNA, EnumRarity rarity, boolean bBreedable) {
        super(name, ID, rarity);
        this.color = color;
        this.plasmid = plasmid;
        this.dDNA = dDNA;
        this.bBreedable = bBreedable;
    }

    protected BioCulture(Color color, String name, int ID, BioPlasmid plasmid, BioDNA dDNA) {
        super(name, ID, dDNA.getRarity());
        this.color = color;
        this.plasmid = plasmid;
        this.dDNA = dDNA;
    }

    public static BioCulture createAndRegisterBioCulture(Color color, String name, BioPlasmid plasmid, BioDNA dna, EnumRarity rarity, boolean breedable) {
        BioCulture ret = new BioCulture(color, name, BIO_CULTURE_ARRAY_LIST.size(), plasmid, dna, rarity, breedable);
        BIO_CULTURE_ARRAY_LIST.add(ret);
        return ret;
    }

    public static BioCulture createAndRegisterBioCulture(Color color, String name, BioPlasmid plasmid, BioDNA dna, boolean breedable) {
        BioCulture ret = new BioCulture(color, name, BIO_CULTURE_ARRAY_LIST.size(), plasmid, dna, dna.getRarity(), breedable);
        BIO_CULTURE_ARRAY_LIST.add(ret);
        return ret;
    }

    public static NBTTagCompound getNBTTagFromCulture(BioCulture bioCulture) {
        if (bioCulture == null)
            return new NBTTagCompound();
        NBTTagCompound ret = new NBTTagCompound();
        ret.setString("Name", bioCulture.name);
        //ret.setInteger("ID", bioCulture.ID);
        ret.setIntArray("Color", new int[]{bioCulture.color.getRed(), bioCulture.color.getGreen(), bioCulture.color.getBlue()});
        ret.setTag("Plasmid", BioData.getNBTTagFromBioData(BioData.convertBioPlasmidToBioData(bioCulture.plasmid)));
        ret.setTag("DNA", BioData.getNBTTagFromBioData(BioData.convertBioDNAToBioData(bioCulture.dDNA)));
        ret.setBoolean("Breedable", bioCulture.bBreedable);
        ret.setByte("Rarety", BW_Util.getByteFromRarity(bioCulture.rarity));
        if (bioCulture.bBreedable)
            ret.setString("Fluid", bioCulture.getFluid().getName());
        return ret;
    }

    public static BioCulture getBioCultureFromNBTTag(NBTTagCompound tag) {
        if (tag == null || tag.getIntArray("Color").length == 0)
            return null;
        BioCulture ret = getBioCulture(tag.getString("Name"));

        if (ret == null)
                ret = createAndRegisterBioCulture(
                            new Color(tag.getIntArray("Color")[0], tag.getIntArray("Color")[1], tag.getIntArray("Color")[2]),
                            tag.getString("Name"),
                            BioPlasmid.convertDataToPlasmid(getBioDataFromNBTTag(tag.getCompoundTag("Plasmid"))),
                            BioDNA.convertDataToDNA(getBioDataFromNBTTag(tag.getCompoundTag("DNA"))),
                            BW_Util.getRarityFromByte(tag.getByte("Rarety")),
                            tag.getBoolean("Breedable")
                        );
        if (ret.bBreedable)
            ret.setFluid(FluidRegistry.getFluid(tag.getString("Fluid")));
        if (ret.getFluidNotSet()) //should never happen, but better safe than sorry
            ret.setbBreedable(false);
        return ret;
    }

    public static BioCulture getBioCulture(String Name) {
        for (BioCulture b : BIO_CULTURE_ARRAY_LIST)
            if (b.name.equals(Name))
                return b;
        return null;
    }

    public static BioCulture getBioCulture(BioDNA DNA) {
        for (BioCulture b : BIO_CULTURE_ARRAY_LIST)
            if (b.getdDNA().equals(DNA))
                return b;
        return null;
    }

    public Fluid getFluid() {
        if (this.mFluid == null)
            MainMod.LOGGER.error("Fluid has not been set yet! The issuring Culture is: " + this.name);
        return this.mFluid;
    }

    public boolean getFluidNotSet(){
        return this.mFluid == null && this.isBreedable();
    }

    public void setFluid(Fluid mFluid) {
        this.mFluid = mFluid;
    }

    public boolean isBreedable() {
        return this.bBreedable;
    }

    public void setbBreedable(boolean bBreedable) {
        this.bBreedable = bBreedable;
    }

    public int getColorRGB() {
        return BW_Util.getColorFromArray(new int[]{this.color.getRed(), this.color.getGreen(), this.color.getBlue()});
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

//    public static BioCulture createAndRegisterBioCulture(Color color, String name, long ID, BioPlasmid plasmid, BioDNA dDNA,EnumRarity rarity){
//        BioCulture ret =new BioCulture(color,name,ID,plasmid,dDNA,rarity);
//        BIO_CULTURE_ARRAY_LIST.add(ret);
//        return ret;
//    }

    public BioPlasmid getPlasmid() {
        return this.plasmid;
    }

    public void setPlasmid(BioPlasmid plasmid) {
        this.plasmid = plasmid;
    }

    public BioDNA getdDNA() {
        return this.dDNA;
    }

    public void setdDNA(BioDNA dDNA) {
        this.dDNA = dDNA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BioCulture culture = (BioCulture) o;
        return this.isBreedable() == culture.isBreedable() &&
                Objects.equals(this.getColor(), culture.getColor()) &&
                Objects.equals(this.getPlasmid(), culture.getPlasmid()) &&
                Objects.equals(this.getdDNA(), culture.getdDNA()) &&
                Objects.equals(this.mFluid, culture.mFluid);
    }

    @Override
    public int hashCode() {
        return MurmurHash3.murmurhash3_x86_32(ByteBuffer.allocate(17).putInt(MurmurHash3.murmurhash3_x86_32(this.getName(),0,this.getName().length(),31)).putInt(this.getColorRGB()).putInt(this.getPlasmid().ID).putInt(this.getdDNA().ID).put((byte) (isBreedable() ? 1 : 0)).array(),0,17,31);
    }
}
