package com.github.technus.tectech.mechanics.chunkData;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;

import java.util.HashMap;

public interface ChunkMetaDataHandler {
    String getTagName();
    void mergeData(NBTTagCompound target, NBTTagCompound loadedData);
    NBTTagCompound createData();
    void TickData(HashMap<Integer,HashMap<ChunkCoordIntPair, NBTTagCompound>> data, TickEvent.ServerTickEvent event);
}

