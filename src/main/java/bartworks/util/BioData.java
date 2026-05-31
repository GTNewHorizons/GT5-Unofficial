/*
 * Copyright (c) 2018-2019 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
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

import java.nio.ByteBuffer;
import java.util.Objects;

import bartworks.API.enums.BioDataEnum;
import gregtech.api.util.GTLanguageManager;
import net.minecraft.item.EnumRarity;
import net.minecraft.nbt.NBTTagCompound;

public class BioData {

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

    public BioData(BioDataEnum data) {
        this.rarity = data.rarity;
        this.name = data.name;
        this.ID = data.id;
        this.chance = data.chance;
        this.tier = data.tier;
    }

    public String getLocalisedName() {
        return GTLanguageManager.getTranslation(this.getName());
    }

    public static NBTTagCompound getNBTTagFromBioData(BioData bioData) {
        String name = bioData != null ? bioData.name : BioDataEnum.NullBioData.name;
        NBTTagCompound ret = new NBTTagCompound();
        ret.setString("Name", name);
        return ret;
    }

    public static BioData getBioDataFromNBTTag(NBTTagCompound tag) {
        if (tag == null) return null;
        if (!tag.hasKey("Name")) return null;
        return BioDataEnum.LOOKUPS_BY_NAME.get(tag.getString("Name")).getBioData();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        BioData bioData = (BioData) o;
        return this.getID() == bioData.getID()
            || this.getChance() == bioData.getChance() && this.getTier() == bioData.getTier()
                && Objects.equals(this.getName(), bioData.getName())
                && this.getRarity() == bioData.getRarity();
    }

    @Override
    public int hashCode() {
        return MurmurHash3.murmurhash3_x86_32(
            ByteBuffer.allocate(13)
                .putInt(
                    MurmurHash3.murmurhash3_x86_32(
                        this.getName(),
                        0,
                        this.getName()
                            .length(),
                        31))
                .put(BWUtil.getByteFromRarity(this.getRarity()))
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
        return String.format("BioData(name=%s, id=%d, rarity=%s, chance=%d, tier=%d)", this.name, this.ID, this.rarity.name(), this.chance, this.tier);
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
