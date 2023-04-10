package com.github.technus.tectech.mechanics.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ChunkDataHandler {

    private final String BASE_TAG_NAME = "TecTechData";
    private final HashMap<Integer, HashMap<ChunkCoordIntPair, NBTChunk>> dimensionWiseChunkData = new HashMap<>();
    private final HashMap<String, HashMap<Integer, ChunkHashMap>> dimensionWiseMetaChunkData = new HashMap<>();
    private final HashMap<String, IChunkMetaDataHandler> metaDataHandlerHashMap = new HashMap<>();
    private final ArrayList<IChunkMetaDataHandler> pushSyncHandlers = new ArrayList<>();
    private final ArrayList<IChunkMetaDataHandler> pullSyncHandlers = new ArrayList<>();
    private final ArrayList<IChunkMetaDataHandler> serverHandlers = new ArrayList<>();
    private final ArrayList<IChunkMetaDataHandler> worldHandlers = new ArrayList<>();
    private final ArrayList<IChunkMetaDataHandler> playerHandlers = new ArrayList<>();
    private final ArrayList<IChunkMetaDataHandler> clientHandlers = new ArrayList<>();
    private final ArrayList<IChunkMetaDataHandler> renderHandlers = new ArrayList<>();

    private HashMap<ChunkCoordIntPair, NBTChunk> getOrCreateDimensionWiseChunkData(int dim) {
        return dimensionWiseChunkData.computeIfAbsent(dim, m -> {
            HashMap<ChunkCoordIntPair, NBTChunk> map = new HashMap<>();
            for (Map.Entry<String, IChunkMetaDataHandler> meta : metaDataHandlerHashMap.entrySet()) {
                dimensionWiseMetaChunkData.get(meta.getKey()).put(dim, new ChunkHashMap(meta.getValue(), map));
            }
            return map;
        });
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        int dim = event.world.provider.dimensionId;
        getOrCreateDimensionWiseChunkData(dim);
    }

    @SubscribeEvent
    public void handleChunkSaveEvent(ChunkDataEvent.Save event) {
        HashMap<ChunkCoordIntPair, NBTChunk> dimensionData = dimensionWiseChunkData
                .get(event.world.provider.dimensionId);
        NBTChunk chunkData = dimensionData != null ? dimensionData.get(event.getChunk().getChunkCoordIntPair()) : null;
        if (chunkData == null) {
            event.getData().removeTag(BASE_TAG_NAME);
        } else {
            chunkData.isLoaded = true;
            // make a copy of chunk data. this tag will be serialized on another thread. not making a copy might
            // cause the other thread to encounter ConcurrentModificationException.
            event.getData().setTag(BASE_TAG_NAME, chunkData.data.copy());
        }
    }

    @SubscribeEvent
    public void handleChunkLoadEvent(ChunkDataEvent.Load event) {
        NBTTagCompound loadedTag = event.getData().getCompoundTag(BASE_TAG_NAME);
        if (loadedTag.hasNoTags()) {
            return;
        }
        int dimId = event.world.provider.dimensionId;
        HashMap<ChunkCoordIntPair, NBTChunk> dimensionMemory = getOrCreateDimensionWiseChunkData(dimId);
        ChunkCoordIntPair chunkCoordIntPair = event.getChunk().getChunkCoordIntPair();
        Set<String> loadedKeys = loadedTag.func_150296_c();
        NBTChunk chunkMemory = dimensionMemory.get(chunkCoordIntPair);
        if (chunkMemory == null) {
            chunkMemory = new NBTChunk(loadedTag, true);
            dimensionMemory.put(chunkCoordIntPair, chunkMemory);
            for (String s : loadedKeys) {
                dimensionWiseMetaChunkData.get(s).get(dimId).putLoaded(chunkCoordIntPair, loadedTag.getCompoundTag(s));
            }
        } else if (!chunkMemory.isLoaded) {
            chunkMemory.isLoaded = true;

            Set<String> tagsDuplicated = new HashSet(loadedKeys);
            tagsDuplicated.retainAll(chunkMemory.data.func_150296_c());

            if (tagsDuplicated.isEmpty()) {
                for (String s : loadedKeys) {
                    NBTTagCompound tag = loadedTag.getCompoundTag(s);
                    chunkMemory.data.setTag(s, tag);
                    dimensionWiseMetaChunkData.get(s).get(dimId).putLoaded(chunkCoordIntPair, tag);
                }
            } else {
                for (String s : loadedKeys) {
                    NBTTagCompound memory = chunkMemory.data.getCompoundTag(s);
                    if (tagsDuplicated.contains(s)) {
                        metaDataHandlerHashMap.get(s).mergeData(memory, loadedTag.getCompoundTag(s));
                    } else {
                        chunkMemory.data.setTag(s, loadedTag.getCompoundTag(s));
                        dimensionWiseMetaChunkData.get(s).get(dimId).putLoaded(chunkCoordIntPair, memory);
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onLoadChunk(ChunkEvent.Load aEvent) {
        if (aEvent.world.isRemote && !Minecraft.getMinecraft().isSingleplayer()) { // we already have the data!
            pullSyncHandlers.forEach(chunkMetaDataHandler -> chunkMetaDataHandler.pullData(aEvent));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onUnloadChunk(ChunkEvent.Unload aEvent) {
        if (aEvent.world.isRemote && !Minecraft.getMinecraft().isSingleplayer()) { // we need all data if running local
                                                                                   // server!
            pullSyncHandlers.forEach(
                    chunkMetaDataHandler -> dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName())
                            .get(aEvent.world.provider.dimensionId).remove(aEvent.getChunk().getChunkCoordIntPair()));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTickEvent(TickEvent.ClientTickEvent aEvent) {
        clientHandlers.forEach(
                chunkMetaDataHandler -> chunkMetaDataHandler
                        .tickClient(dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName()), aEvent));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderTickEvent(TickEvent.RenderTickEvent aEvent) {
        renderHandlers.forEach(
                chunkMetaDataHandler -> chunkMetaDataHandler
                        .tickRender(dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName()), aEvent));
    }

    @SubscribeEvent
    public void onServerTickEvent(TickEvent.ServerTickEvent aEvent) {
        serverHandlers.forEach(
                chunkMetaDataHandler -> chunkMetaDataHandler
                        .tickServer(dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName()), aEvent));
    }

    // Ticks only on server side (but must be present for client server)
    @SubscribeEvent
    public void onWorldTickEvent(TickEvent.WorldTickEvent aEvent) {
        int dim = aEvent.world.provider.dimensionId;
        pushSyncHandlers.forEach(chunkMetaDataHandler -> {
            ChunkHashMap data = dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName()).get(dim);
            int cycle = chunkMetaDataHandler.pushPayloadSpreadPeriod();
            int epoch = (int) (aEvent.world.getTotalWorldTime() % cycle);
            ArrayList<ChunkCoordIntPair> work;
            if (epoch == 0) {
                int per = data.dirtyBoys.size() / cycle;
                int mod = data.dirtyBoys.size() % cycle;
                Iterator<ChunkCoordIntPair> iter = data.dirtyBoys.iterator();
                for (int periodWork = 0; periodWork < cycle; periodWork++) {
                    work = data.workLoad.get(periodWork);
                    for (int i = 0; i < per; i++) {
                        work.add(iter.next());
                    }
                    if (periodWork < mod) {
                        work.add(iter.next());
                    }
                }
                data.dirtyBoys.clear();
            }
            work = data.workLoad.get(epoch);
            chunkMetaDataHandler.pushPayload(aEvent.world, work);
            work.clear();
        });
        worldHandlers.forEach(
                chunkMetaDataHandler -> chunkMetaDataHandler
                        .tickWorld(dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName()), aEvent));
    }

    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent aEvent) {
        playerHandlers.forEach(
                chunkMetaDataHandler -> chunkMetaDataHandler
                        .tickPlayer(dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName()), aEvent));
    }

    public void clearData() {
        dimensionWiseChunkData.clear();
        dimensionWiseMetaChunkData.forEach((k, v) -> v.clear());
    }

    public IChunkMetaDataHandler getChunkMetaDataHandler(String s) {
        return metaDataHandlerHashMap.get(s);
    }

    public void registerChunkMetaDataHandler(IChunkMetaDataHandler handler) {
        metaDataHandlerHashMap.put(handler.getTagName(), handler);
        dimensionWiseMetaChunkData.put(handler.getTagName(), new HashMap<>());
        Class clazz = handler.getClass();
        try {
            if (clazz.getMethod("tickServer", HashMap.class, TickEvent.ServerTickEvent.class).getDeclaringClass()
                    != IChunkMetaDataHandler.class) {
                serverHandlers.add(handler);
            }
            if (clazz.getMethod("tickPlayer", HashMap.class, TickEvent.PlayerTickEvent.class).getDeclaringClass()
                    != IChunkMetaDataHandler.class) {
                playerHandlers.add(handler);
            }
            if (clazz.getMethod("pushData", World.class, ChunkCoordIntPair.class).getDeclaringClass()
                    != IChunkMetaDataHandler.class) {
                pushSyncHandlers.add(handler);
            }
            if (clazz.getMethod("tickWorld", HashMap.class, TickEvent.WorldTickEvent.class).getDeclaringClass()
                    != IChunkMetaDataHandler.class) {
                worldHandlers.add(handler);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot register common event handlers!", e);
        }
        if (FMLCommonHandler.instance().getSide().isClient()) {
            try {
                if (clazz.getMethod("pullData", ChunkEvent.Load.class).getDeclaringClass()
                        != IChunkMetaDataHandler.class) {
                    pullSyncHandlers.add(handler);
                }
                if (clazz.getMethod("tickClient", HashMap.class, TickEvent.ClientTickEvent.class).getDeclaringClass()
                        != IChunkMetaDataHandler.class) {
                    clientHandlers.add(handler);
                }
                if (clazz.getMethod("tickRender", HashMap.class, TickEvent.RenderTickEvent.class).getDeclaringClass()
                        != IChunkMetaDataHandler.class) {
                    renderHandlers.add(handler);
                }
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Cannot register client event handlers!", e);
            }
        }
    }

    public NBTTagCompound removeChunkData(IChunkMetaDataHandler handler, int world, ChunkCoordIntPair chunk) {
        return dimensionWiseMetaChunkData.get(handler.getTagName()).get(world).remove(chunk);
    }

    public NBTTagCompound getChunkData(IChunkMetaDataHandler handler, int world, ChunkCoordIntPair chunk) {
        return dimensionWiseMetaChunkData.get(handler.getTagName()).get(world).get(chunk);
    }

    public NBTTagCompound putChunkData(IChunkMetaDataHandler handler, int world, ChunkCoordIntPair chunk,
            NBTTagCompound data) {
        return dimensionWiseMetaChunkData.get(handler.getTagName()).get(world).put(chunk, data);
    }

    public NBTTagCompound createIfAbsentChunkData(IChunkMetaDataHandler handler, int world, ChunkCoordIntPair chunk) {
        return dimensionWiseMetaChunkData.get(handler.getTagName()).get(world)
                .computeIfAbsent(chunk, chunkCoordIntPair -> handler.createData());
    }

    public HashMap<Integer, ChunkHashMap> getChunkData(IChunkMetaDataHandler chunkMetaDataHandler) {
        return dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName());
    }

    public ChunkHashMap getChunkData(IChunkMetaDataHandler chunkMetaDataHandler, int world) {
        return dimensionWiseMetaChunkData.get(chunkMetaDataHandler.getTagName()).get(world);
    }

    public static final class ChunkHashMap implements Map<ChunkCoordIntPair, NBTTagCompound> {

        private final HashMap<ChunkCoordIntPair, NBTChunk> storage;
        private final HashMap<ChunkCoordIntPair, NBTTagCompound> storageMeta = new HashMap<>(1024);
        private final HashSet<ChunkCoordIntPair> dirtyBoys = new HashSet<>(1024);
        private final ArrayList<ArrayList<ChunkCoordIntPair>> workLoad = new ArrayList<>();
        private final String meta;

        private ChunkHashMap(IChunkMetaDataHandler meta, HashMap<ChunkCoordIntPair, NBTChunk> storage) {
            this.storage = storage;
            this.meta = meta.getTagName();
            for (int i = 0; i < meta.pushPayloadSpreadPeriod(); i++) {
                workLoad.add(new ArrayList<>(128));
            }
        }

        public void markForTransmissionToClient(ChunkCoordIntPair chunk) {
            dirtyBoys.add(chunk);
        }

        private void putLoaded(ChunkCoordIntPair key, NBTTagCompound value) {
            storageMeta.put(key, value);
        }

        @Override
        public NBTTagCompound remove(Object key) {
            NBTTagCompound compound = storageMeta.remove(key);
            if (compound != null) {
                NBTChunk chunk = storage.get(key);
                chunk.data.removeTag(meta);
                if (chunk.data.hasNoTags()) {
                    storage.remove(key);
                }
            }
            return compound;
        }

        @Override
        public NBTTagCompound put(ChunkCoordIntPair key, NBTTagCompound value) {
            if (value == null) {
                return remove(key);
            }
            NBTChunk chunk = storage.get(key);
            if (chunk == null) {
                NBTTagCompound base = new NBTTagCompound();
                base.setTag(meta, value);
                storage.put(key, new NBTChunk(base, false));
            } else {
                chunk.data.setTag(meta, value);
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
            storageMeta.entrySet().forEach(this::remove);
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

    private static final class NBTChunk {

        private final NBTTagCompound data;
        private boolean isLoaded;

        private NBTChunk(NBTTagCompound data, boolean isLoaded) {
            if (data == null) {
                data = new NBTTagCompound();
            }
            this.data = data;
            this.isLoaded = isLoaded;
        }
    }
}
