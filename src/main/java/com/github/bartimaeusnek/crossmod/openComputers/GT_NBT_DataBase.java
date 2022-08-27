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

package com.github.bartimaeusnek.crossmod.openComputers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.nbt.NBTTagCompound;

public class GT_NBT_DataBase {

    private static final BiMap<NBTTagCompound, Long> tagIdBiMap = HashBiMap.create();
    private static final BiMap<GT_NBT_DataBase, Long> GTNBTBIMAP = HashBiMap.create();

    private static long maxID = Long.MIN_VALUE + 1;

    private final NBTTagCompound tagCompound;

    private final String mDataName;
    private final String mDataTitle;
    private long id;

    GT_NBT_DataBase(String mDataName, String mDataTitle, NBTTagCompound tagCompound) {
        this.mDataName = mDataName;
        this.mDataTitle = mDataTitle;
        this.tagCompound = tagCompound;
        this.id = GT_NBT_DataBase.maxID;
        GT_NBT_DataBase.tagIdBiMap.put(tagCompound, this.id);
        GT_NBT_DataBase.GTNBTBIMAP.put(this, this.id);
        ++GT_NBT_DataBase.maxID;
    }

    static GT_NBT_DataBase getGTTagFromId(Long id) {
        return GT_NBT_DataBase.GTNBTBIMAP.inverse().get(id);
    }

    static Long getIdFromGTTag(GT_NBT_DataBase tagCompound) {
        return GT_NBT_DataBase.GTNBTBIMAP.get(tagCompound);
    }

    static NBTTagCompound getTagFromId(Long id) {
        return GT_NBT_DataBase.tagIdBiMap.inverse().get(id);
    }

    static Long getIdFromTag(NBTTagCompound tagCompound) {
        return GT_NBT_DataBase.tagIdBiMap.get(tagCompound);
    }

    public NBTTagCompound getTagCompound() {
        return this.tagCompound;
    }

    public String getmDataName() {
        return this.mDataName;
    }

    static long getMaxID() {
        return GT_NBT_DataBase.maxID;
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

    private GT_NBT_DataBase(NBTTagCompound tagCompound, String mDataName, String mDataTitle, long id) {
        this.tagCompound = tagCompound;
        this.mDataName = mDataName;
        this.mDataTitle = mDataTitle;
        this.id = id;
    }

    public static GT_NBT_DataBase makeNewWithoutRegister(
            String mDataName, String mDataTitle, NBTTagCompound tagCompound) {
        return new GT_NBT_DataBase(tagCompound, mDataName, mDataTitle, Long.MIN_VALUE);
    }
}
