package com.github.technus.tectech.chunkData;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.ArrayList;
import java.util.HashMap;

public interface IChunkMetaDataHandler {
    String getTagName();
    void mergeData(NBTTagCompound target, NBTTagCompound loadedData);
    NBTTagCompound createData();
    @SideOnly(Side.CLIENT)
    default void requestData(ChunkEvent.Load aEvent){}
    @SideOnly(Side.SERVER)
    default void pushData(int world, ChunkCoordIntPair chunk){}
    @SideOnly(Side.SERVER)
    default void pushPayload(int world, ArrayList<ChunkCoordIntPair> chunk){
        chunk.forEach(chunkCoordIntPair -> pushData(world,chunkCoordIntPair));
    }
    default int pushPayloadSpreadPeriod(){
        return 20;
    }
    @SideOnly(Side.CLIENT)
    default void tickRender(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.RenderTickEvent aEvent){}
    @SideOnly(Side.CLIENT)
    default void tickClient(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.ClientTickEvent aEvent){}
    default void tickServer(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.ServerTickEvent event){}
    @SideOnly(Side.SERVER)
    default void tickWorld(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.WorldTickEvent aEvent){}
    default void tickPlayer(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.PlayerTickEvent aEvent){}
}

