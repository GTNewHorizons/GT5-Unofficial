package com.github.technus.tectech.mechanics.chunkData;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.event.world.ChunkDataEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChunkDataHandler {
    private final String TEC_TAG="TecTechTag";
    private final HashMap<Integer,HashMap<ChunkCoordIntPair, NBTChunk>> dimensionWiseChunkData=new HashMap<>();
    private final HashMap<String, ChunkMetaDataHandler> metaDataHandlerHashMap =new HashMap<>();

    public void handleChunkSaveEvent(ChunkDataEvent.Save event) {
        HashMap<ChunkCoordIntPair, NBTChunk> dimensionData=dimensionWiseChunkData.get(event.world.provider.dimensionId);
        NBTChunk chunkData =dimensionData!=null?dimensionData.get(event.getChunk().getChunkCoordIntPair()):null;
        if(chunkData==null) {
            event.getData().removeTag(TEC_TAG);
        } else {
            event.getData().setTag(TEC_TAG,chunkData.getData());
        }
    }

    public void handleChunkLoadEvent(ChunkDataEvent.Load event) {
        HashMap<ChunkCoordIntPair, NBTChunk> dimensionData=
                dimensionWiseChunkData.computeIfAbsent(event.world.provider.dimensionId,k->new HashMap<>(1024));
        ChunkCoordIntPair chunkCoordIntPair=event.getChunk().getChunkCoordIntPair();
        NBTChunk chunkData =dimensionData.get(chunkCoordIntPair);
        if(chunkData==null) {
            dimensionData.put(chunkCoordIntPair,new NBTChunk(event.getData().getCompoundTag(TEC_TAG),true));
        }else if(!chunkData.isLoaded) {
            chunkData.isLoaded=true;
            Set<String> tags=new HashSet<>();
            tags.addAll(chunkData.getData().func_150296_c());
            tags.addAll(event.getData().func_150296_c());
            NBTTagCompound compound=new NBTTagCompound();
        }
    }

    public void onServerStarting() {
        dimensionWiseChunkData.clear();
    }

    public void registerChunkMetaDataHandler(ChunkMetaDataHandler handler){
        metaDataHandlerHashMap.put(handler.getTagName(),handler);
    }

    public static class NBTChunk {
        private final NBTTagCompound data;
        private boolean isLoaded;

        private NBTChunk(NBTTagCompound data, boolean isLoaded) {
            this.data = data;
            this.isLoaded = isLoaded;
        }

        public boolean isLoaded(){
            return isLoaded;
        }

        public NBTTagCompound getData(){
            return data;
        }
    }
}
