package com.github.technus.tectech.mechanics.chunkData;

import net.minecraft.nbt.NBTTagCompound;

public interface ChunkMetaDataHandler {
    String getTagName();
    void mergeData(NBTTagCompound inMemory,NBTTagCompound loaded,NBTTagCompound result);
    NBTTagCompound createData();
}

