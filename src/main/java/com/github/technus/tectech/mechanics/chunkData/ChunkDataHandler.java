package com.github.technus.tectech.mechanics.chunkData;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;

import java.util.*;

public class ChunkDataHandler {
    private final String BASE_TAG_NAME ="TecTechData";
    private final                HashMap<Integer,HashMap<ChunkCoordIntPair, NBTChunk>> dimensionWiseChunkData=new HashMap<>();
    private final HashMap<String,HashMap<Integer,ChunkHashMap                       >> dimensionWiseMetaChunkData=new HashMap<>();
    private final HashMap<String,ChunkMetaDataHandler> metaDataHandlerHashMap =new HashMap<>();

    public void handleChunkSaveEvent(ChunkDataEvent.Save event) {
        HashMap<ChunkCoordIntPair, NBTChunk> dimensionData=dimensionWiseChunkData.get(event.world.provider.dimensionId);
        NBTChunk chunkData =dimensionData!=null?dimensionData.get(event.getChunk().getChunkCoordIntPair()):null;
        if(chunkData==null) {
            event.getData().removeTag(BASE_TAG_NAME);
        } else {
            chunkData.isLoaded=true;
            event.getData().setTag(BASE_TAG_NAME,chunkData.data);
        }
    }

    public void handleChunkLoadEvent(ChunkDataEvent.Load event) {
        NBTTagCompound loadedTag=event.getData().getCompoundTag(BASE_TAG_NAME);
        if(loadedTag.hasNoTags()){
            return;
        }

        int dimId=event.world.provider.dimensionId;
        HashMap<ChunkCoordIntPair, NBTChunk> dimensionMemory=
                dimensionWiseChunkData.computeIfAbsent(dimId, this::createDimensionData);

        ChunkCoordIntPair chunkCoordIntPair=event.getChunk().getChunkCoordIntPair();
        NBTChunk chunkMemory =dimensionMemory.get(chunkCoordIntPair);
        Set<String> loadedKeys=loadedTag.func_150296_c();

        if(chunkMemory==null) {
            chunkMemory=new NBTChunk(loadedTag,true);
            dimensionMemory.put(chunkCoordIntPair,chunkMemory);
            for (String s :loadedKeys) {
                dimensionWiseMetaChunkData.get(s).get(dimId).putLoaded(chunkCoordIntPair, loadedTag.getCompoundTag(s));
            }
        }else if(!chunkMemory.isLoaded) {
            chunkMemory.isLoaded=true;

            Set<String> tagsDuplicated=new HashSet(loadedKeys);
            tagsDuplicated.retainAll(chunkMemory.data.func_150296_c());

            if (tagsDuplicated.isEmpty()) {
                for (String s:loadedKeys) {
                    NBTTagCompound tag=loadedTag.getCompoundTag(s);
                    chunkMemory.data.setTag(s,tag);
                    dimensionWiseMetaChunkData.get(s).get(dimId).putLoaded(chunkCoordIntPair,tag);
                }
            } else {
                for (String s : loadedKeys) {
                    NBTTagCompound memory=chunkMemory.data.getCompoundTag(s);
                    if(tagsDuplicated.contains(s)){
                        metaDataHandlerHashMap.get(s).mergeData(memory,loadedTag.getCompoundTag(s));
                    }else {
                        chunkMemory.data.setTag(s,loadedTag.getCompoundTag(s));
                        dimensionWiseMetaChunkData.get(s).get(dimId).putLoaded(chunkCoordIntPair,memory);
                    }
                }
            }
        }
    }

    public void tickData(TickEvent.ServerTickEvent event){
        dimensionWiseMetaChunkData.forEach((k, v) -> metaDataHandlerHashMap.get(k).TickData(v, event));
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

    public HashMap<Integer,ChunkHashMap> getChunkData(ChunkMetaDataHandler chunkMetaDataHandler){
        return dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName());
    }

    public ChunkHashMap getChunkData(ChunkMetaDataHandler chunkMetaDataHandler,World world){
        return dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName()).get(world.provider.dimensionId);
    }

    public ChunkHashMap getChunkData(ChunkMetaDataHandler chunkMetaDataHandler,int world){
        return dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName()).get(world);
    }

    private HashMap<ChunkCoordIntPair, NBTChunk> createDimensionData(Integer dim) {
        HashMap<ChunkCoordIntPair, NBTChunk> map = new HashMap<>();
        for (String meta : metaDataHandlerHashMap.keySet()) {
            dimensionWiseMetaChunkData.get(meta).put(dim, new ChunkHashMap(meta, map));
        }
        return map;
    }

    public static class ChunkHashMap implements Map<ChunkCoordIntPair,NBTTagCompound>{
        private final HashMap<ChunkCoordIntPair,NBTChunk> storage;
        private final HashMap<ChunkCoordIntPair,NBTTagCompound> storageMeta=new HashMap<>(1024);
        private final String meta;

        private ChunkHashMap(String meta, HashMap<ChunkCoordIntPair, NBTChunk> storage) {
            this.storage =storage;
            this.meta=meta;
        }

        private void putLoaded(ChunkCoordIntPair key, NBTTagCompound value) {
            storageMeta.put(key, value);
        }

        @Override
        public NBTTagCompound remove(Object key) {
            NBTTagCompound compound=storageMeta.remove(key);
            if(compound!=null) {
                NBTChunk chunk = storage.get(key);
                chunk.data.removeTag(meta);
                if(chunk.data.hasNoTags()){
                    storage.remove(key);
                }
            }
            return compound;
        }

        @Override
        public NBTTagCompound put(ChunkCoordIntPair key, NBTTagCompound value) {
            NBTChunk chunk = storage.get(key);
            if(chunk==null){
                NBTTagCompound base=new NBTTagCompound();
                base.setTag(meta,value);
                storage.put(key,new NBTChunk(base,false));
            }else {
                chunk.data.setTag(meta,value);
            }
            return storageMeta.put(key, value);
        }

        @Override
        public int size() {
            return storageMeta.size();
        }

        @Override
        public boolean isEmpty() {
            return storageMeta.isEmpty();
        }

        @Override
        public NBTTagCompound get(Object key) {
            return storageMeta.get(key);
        }

        @Override
        public void clear() {
            entrySet().forEach(this::remove);
        }

        @Override
        public void putAll(Map<? extends ChunkCoordIntPair, ? extends NBTTagCompound> m) {
            m.forEach(this::put);
        }

        @Override
        public boolean containsKey(Object key) {
            return storageMeta.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return storageMeta.containsValue(value);
        }

        @Override
        public Set<ChunkCoordIntPair> keySet() {
            return storageMeta.keySet();
        }

        @Override
        public Collection<NBTTagCompound> values() {
            return storageMeta.values();
        }

        @Override
        public Set<Entry<ChunkCoordIntPair, NBTTagCompound>> entrySet() {
            return storageMeta.entrySet();
        }
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
