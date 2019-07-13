package com.github.technus.tectech.mechanics.chunkData;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

public interface ChunkMetaDataHandler {
    String getTagName();
    void mergeData(NBTTagCompound target, NBTTagCompound loadedData);
    NBTTagCompound createData();
    void TickData(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.ServerTickEvent event);
}

