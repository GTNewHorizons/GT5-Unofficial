package com.github.technus.tectech.chunkData;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.Objects;

public final class WorldData {
    private final World world;
    private final Chunk chunk;
    private final ChunkCoordIntPair coordIntPair;

    public WorldData(World world, Chunk data) {
        this.world = world;
        this.chunk = data;
        coordIntPair=data.getChunkCoordIntPair();
    }

    public WorldData(World world, ChunkCoordIntPair data) {
        this.world = world;
        this.coordIntPair = data;
        chunk= world.getChunkFromChunkCoords(data.chunkXPos,data.chunkZPos);
    }

    public World getWorld() {
        return world;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public ChunkCoordIntPair getCoordIntPair() {
        return coordIntPair;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldData worldData = (WorldData) o;
        return world.provider.dimensionId==worldData.world.provider.dimensionId &&
                coordIntPair.chunkXPos==worldData.coordIntPair.chunkXPos &&
                coordIntPair.chunkZPos==worldData.coordIntPair.chunkXPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(world.provider.dimensionId, coordIntPair);
    }
}
