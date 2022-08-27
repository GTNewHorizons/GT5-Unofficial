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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.item.EnumRarity;
import net.minecraft.nbt.NBTTagCompound;

public class BioData {
    public static final ArrayList<BioData> BIO_DATA_ARRAY_LIST = new ArrayList<>();

    protected String name;
    protected int ID;
    protected EnumRarity rarity;
    protected int chance;
    protected int tier;

    protected BioData(String name, int ID, EnumRarity rarity, int chance, int tier) {
        this.name = name;
        this.ID = ID;
        this.rarity = rarity;
        this.chance = chance;
        this.tier = tier;
    }

    protected BioData(String name, int ID, EnumRarity rarity) {
        this.name = name;
        this.ID = ID;
        this.rarity = rarity;
        this.chance = 7500;
        this.tier = 0;
    }

    protected BioData(BioData bioData) {
        this.rarity = bioData.rarity;
        this.name = bioData.name;
        this.ID = bioData.ID;
        this.chance = bioData.chance;
        this.tier = bioData.tier;
    }

    public static BioData convertBioPlasmidToBioData(BioPlasmid bioPlasmid) {
        return new BioData(bioPlasmid.name, bioPlasmid.ID, bioPlasmid.rarity, bioPlasmid.chance, bioPlasmid.tier);
    }

    public static BioData convertBioDNAToBioData(BioDNA bioDNA) {
        return new BioData(bioDNA.name, bioDNA.ID, bioDNA.rarity, bioDNA.chance, bioDNA.tier);
    }

    public static BioData createAndRegisterBioData(String aName, EnumRarity rarity, int chance, int tier) {
        BioData ret = new BioData(aName, BIO_DATA_ARRAY_LIST.size(), rarity, chance, tier);
        BIO_DATA_ARRAY_LIST.add(ret);
        return ret;
    }

    public static BioData createAndRegisterBioData(String aName, EnumRarity rarity) {
        BioData ret = new BioData(aName, BIO_DATA_ARRAY_LIST.size(), rarity);
        BIO_DATA_ARRAY_LIST.add(ret);
        return ret;
    }

    public static NBTTagCompound getNBTTagFromBioData(BioData bioData) {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setByte("Rarity", BW_Util.getByteFromRarity(bioData.rarity));
        ret.setString("Name", bioData.name);
        // ret.setInteger("ID", bioData.ID); buggy when load Order changes
        ret.setInteger("Chance", bioData.chance);
        ret.setInteger("Tier", bioData.tier);
        return ret;
    }

    public static BioData getBioDataFromNBTTag(NBTTagCompound tag) {
        if (tag == null) return null;
        return getBioDataFromName(tag.getString("Name"));
    }

    public static BioData getBioDataFromName(String Name) {
        for (BioData bd : BIO_DATA_ARRAY_LIST) if (bd.name.equals(Name)) return bd;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        BioData bioData = (BioData) o;
        return this.getID() == bioData.getID()
                || (this.getChance() == bioData.getChance()
                        && this.getTier() == bioData.getTier()
                        && Objects.equals(this.getName(), bioData.getName())
                        && this.getRarity() == bioData.getRarity());
    }

    @Override
    public int hashCode() {
        return MurmurHash3.murmurhash3_x86_32(
                ByteBuffer.allocate(13)
                        .putInt(MurmurHash3.murmurhash3_x86_32(
                                this.getName(), 0, this.getName().length(), 31))
                        .put(BW_Util.getByteFromRarity(this.getRarity()))
                        .putInt(this.getChance())
                        .putInt(this.getTier())
                        .array(),
                0,
                13,
                31);
    }

    public int getTier() {
        return this.tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    @Override
    public String toString() {
        return "BioData{" + "name='" + name + '\'' + ", ID=" + ID + '}';
    }

    public EnumRarity getRarity() {
        return this.rarity;
    }

    public void setRarity(EnumRarity rarity) {
        this.rarity = rarity;
    }

    public int getChance() {
        return this.chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * DO NOT USE GET ID TO GET THE OBJECT! THIS SHOULD ONLY BE USED FOR COMPARISON!
     *
     * @return the position in the loading list
     */
    public int getID() {
        return this.ID;
    }
}
