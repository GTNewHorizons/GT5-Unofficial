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

package bwcrossmod.openComputers;

import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class GTNBTDataBase {

    private static final BiMap<NBTTagCompound, Long> tagIdBiMap = HashBiMap.create();
    private static final BiMap<GTNBTDataBase, Long> GTNBTBIMAP = HashBiMap.create();

    private static long maxID = Long.MIN_VALUE + 1;

    private final NBTTagCompound tagCompound;

    private final String mDataName;
    private final String mDataTitle;
    private long id;

    GTNBTDataBase(String mDataName, String mDataTitle, NBTTagCompound tagCompound) {
        this.mDataName = mDataName;
        this.mDataTitle = mDataTitle;
        this.tagCompound = tagCompound;
        this.id = GTNBTDataBase.maxID;
        GTNBTDataBase.tagIdBiMap.put(tagCompound, this.id);
        GTNBTDataBase.GTNBTBIMAP.put(this, this.id);
        ++GTNBTDataBase.maxID;
    }

    static GTNBTDataBase getGTTagFromId(Long id) {
        return GTNBTDataBase.GTNBTBIMAP.inverse()
            .get(id);
    }

    static Long getIdFromGTTag(GTNBTDataBase tagCompound) {
        return GTNBTDataBase.GTNBTBIMAP.get(tagCompound);
    }

    static NBTTagCompound getTagFromId(Long id) {
        return GTNBTDataBase.tagIdBiMap.inverse()
            .get(id);
    }

    static Long getIdFromTag(NBTTagCompound tagCompound) {
        return GTNBTDataBase.tagIdBiMap.get(tagCompound);
    }

    public NBTTagCompound getTagCompound() {
        return this.tagCompound;
    }

    public String getmDataName() {
        return this.mDataName;
    }

    static long getMaxID() {
        return GTNBTDataBase.maxID;
    }

    public String getmDataTitle() {
        return this.mDataTitle;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private GTNBTDataBase(NBTTagCompound tagCompound, String mDataName, String mDataTitle, long id) {
        this.tagCompound = tagCompound;
        this.mDataName = mDataName;
        this.mDataTitle = mDataTitle;
        this.id = id;
    }

    public static GTNBTDataBase makeNewWithoutRegister(String mDataName, String mDataTitle,
        NBTTagCompound tagCompound) {
        return new GTNBTDataBase(tagCompound, mDataName, mDataTitle, Long.MIN_VALUE);
    }
}
