package com.github.technus.tectech.mechanics.chunkData;

import net.minecraft.nbt.NBTTagCompound;

public interface ChunkMetaDataHandler {
    String getTagName();
    void mergeData(NBTTagCompound target, NBTTagCompound loadedData);
    NBTTagCompound createData();
}

