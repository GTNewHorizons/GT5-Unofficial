package com.github.technus.tectech.mechanics.anomaly;

import com.github.technus.tectech.mechanics.chunkData.ChunkMetaDataHandler;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;

import java.util.HashMap;

public class AnomalyHandler implements ChunkMetaDataHandler {

    @Override
    public String getTagName() {
        return "Anomaly";
    }

    @Override
    public void mergeData(NBTTagCompound target, NBTTagCompound loadedData) {
        target.setInteger("intensity",
                target.getInteger("intensity")+loadedData.getInteger("intensity"));
    }

    @Override
    public NBTTagCompound createData() {
        return new NBTTagCompound();
    }

    @Override
    public void TickData(HashMap<Integer, HashMap<ChunkCoordIntPair, NBTTagCompound>> data, TickEvent.ServerTickEvent event) {
        
    }
}
