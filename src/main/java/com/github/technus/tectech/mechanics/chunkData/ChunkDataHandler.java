package com.github.technus.tectech.mechanics.chunkData;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChunkDataHandler {
    private final String TEC_TAG="TecTechTag";
    private final HashMap<Integer,HashMap<ChunkCoordIntPair, NBTChunk>> dimensionWiseChunkData=new HashMap<>();
    private final HashMap<String,HashMap<Integer,HashMap<ChunkCoordIntPair, NBTTagCompound>>> dimensionWiseMetaChunkData=new HashMap<>();
    private final HashMap<String,ChunkMetaDataHandler> metaDataHandlerHashMap =new HashMap<>();

    public void handleChunkSaveEvent(ChunkDataEvent.Save event) {
        HashMap<ChunkCoordIntPair, NBTChunk> dimensionData=dimensionWiseChunkData.get(event.world.provider.dimensionId);
        NBTChunk chunkData =dimensionData!=null?dimensionData.get(event.getChunk().getChunkCoordIntPair()):null;
        if(chunkData==null) {
            event.getData().removeTag(TEC_TAG);
        } else {
            chunkData.isLoaded=true;
            event.getData().setTag(TEC_TAG,chunkData.data);
        }
    }

    public void handleChunkLoadEvent(ChunkDataEvent.Load event) {
        NBTTagCompound loadedTag=event.getData().getCompoundTag(TEC_TAG);
        if(loadedTag.hasNoTags()){
            return;
        }

        int dimId=event.world.provider.dimensionId;
        HashMap<ChunkCoordIntPair, NBTChunk> dimensionMemory=
                dimensionWiseChunkData.computeIfAbsent(dimId,dim->{
                    for (String meta : metaDataHandlerHashMap.keySet()) {
                        dimensionWiseMetaChunkData.get(meta).put(dim, new HashMap<>(1024));
                    }
                    return new HashMap<>(1024);
                });

        ChunkCoordIntPair chunkCoordIntPair=event.getChunk().getChunkCoordIntPair();
        NBTChunk chunkMemory =dimensionMemory.get(chunkCoordIntPair);
        Set<String> loadedKeys=loadedTag.func_150296_c();

        if(chunkMemory==null) {
            chunkMemory=new NBTChunk(/*(NBTTagCompound)*/loadedTag/*.copy()*/,true);
            for (String s :loadedKeys) {
                if (metaDataHandlerHashMap.containsKey(s)) {
                    dimensionWiseMetaChunkData.get(s).get(dimId).put(chunkCoordIntPair,chunkMemory.data.getCompoundTag(s));
                } else {
                    throw new RuntimeException("Missing meta handler!" + s);
                }
            }
            dimensionMemory.put(chunkCoordIntPair,chunkMemory);
        }else if(!chunkMemory.isLoaded) {
            chunkMemory.isLoaded=true;

            Set<String> tagsDuplicated=new HashSet(loadedKeys);
            tagsDuplicated.retainAll(chunkMemory.data.func_150296_c());

            if (tagsDuplicated.isEmpty()) {
                for (String s:loadedKeys) {
                    if (metaDataHandlerHashMap.containsKey(s)) {
                        chunkMemory.data.setTag(s,loadedTag.getTag(s)/*.copy()*/);
                        dimensionWiseMetaChunkData.get(s).get(dimId).put(chunkCoordIntPair,chunkMemory.data.getCompoundTag(s));
                    } else {
                        throw new RuntimeException("Missing meta handler!" + s);
                    }
                }
            } else {
                for (String s : loadedKeys) {
                    if(tagsDuplicated.contains(s)){
                        ChunkMetaDataHandler metaDataHandler = metaDataHandlerHashMap.get(s);
                        if (metaDataHandler == null) {
                            throw new RuntimeException("Missing meta handler!" + s);
                        } else {
                            metaDataHandler.mergeData(
                                    chunkMemory.data.getCompoundTag(s),
                                    loadedTag.getCompoundTag(s));
                        }
                    }else {
                        if (metaDataHandlerHashMap.containsKey(s)) {
                            chunkMemory.data.setTag(s,loadedTag.getTag(s));
                            dimensionWiseMetaChunkData.get(s).get(dimId).put(chunkCoordIntPair,chunkMemory.data.getCompoundTag(s));
                        } else {
                            throw new RuntimeException("Missing meta handler!" + s);
                        }
                    }
                }
            }
        }
    }

    public void tick(TickEvent.ServerTickEvent event){
        dimensionWiseMetaChunkData.forEach((k,v)-> metaDataHandlerHashMap.get(k).TickData(v,event));
    }

    public void onServerStarting() {
        dimensionWiseChunkData.clear();
        dimensionWiseMetaChunkData.clear();
    }

    public void registerChunkMetaDataHandler(ChunkMetaDataHandler handler){
        metaDataHandlerHashMap.put(handler.getTagName(),handler);
        dimensionWiseMetaChunkData.put(handler.getTagName(),new HashMap<>());
    }

    public NBTTagCompound getChunkData(ChunkMetaDataHandler handler, World world, Chunk chunk){
        return getChunkData(handler,world.provider.dimensionId,chunk.getChunkCoordIntPair());
    }

    public NBTTagCompound getChunkData(ChunkMetaDataHandler handler, int world, ChunkCoordIntPair chunk){
        return dimensionWiseMetaChunkData.get(handler.getTagName()).get(world).get(chunk);
    }

    public NBTTagCompound computeIfAbsentChunkData(ChunkMetaDataHandler handler, World world, Chunk chunk){
        return computeIfAbsentChunkData(handler,world.provider.dimensionId,chunk.getChunkCoordIntPair());
    }

    public NBTTagCompound computeIfAbsentChunkData(ChunkMetaDataHandler handler, int world, ChunkCoordIntPair chunk){
        return dimensionWiseMetaChunkData.get(handler.getTagName()).get(world)
                .computeIfAbsent(chunk,chunkCoordIntPair -> handler.createData());
    }

    public HashMap<Integer,HashMap<ChunkCoordIntPair, NBTTagCompound>> getChunkData(ChunkMetaDataHandler chunkMetaDataHandler){
        return dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName());
    }

    public HashMap<ChunkCoordIntPair, NBTTagCompound> getChunkData(ChunkMetaDataHandler chunkMetaDataHandler,World world){
        return dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName()).get(world.provider.dimensionId);
    }

    public HashMap<ChunkCoordIntPair, NBTTagCompound> getChunkData(ChunkMetaDataHandler chunkMetaDataHandler,int world){
        return dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName()).get(world);
    }

    private static class NBTChunk {
        private final NBTTagCompound data;
        private boolean isLoaded;

        private NBTChunk(NBTTagCompound data, boolean isLoaded) {
            if(data==null){
                data=new NBTTagCompound();
            }
            this.data = data;
            this.isLoaded = isLoaded;
        }
    }
}
