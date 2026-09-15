package gregtech.api.render;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.gtnewhorizons.angelica.api.IBlockAccessExtended;

import gregtech.api.enums.Mods;
import gregtech.api.interfaces.ITexture;
import gregtech.mixin.interfaces.accessors.ChunkCacheAccessor;

/**
 * Allows you to render an overlay above supported blocks. This overlay is rendered above any original block texture the
 * overlayed block would have, be it textures or covers.
 *
 * All overlays must have an owner, and each owner can have at most one ITexture assigned to each render location.
 * Different overlays from different owners will be rendered in the zlevel specified, with biggest zlevel meaning
 * rendered on top. The exact render order remain unspecified if multiple overlay has the same zlevel.
 *
 * read methods are thread safe, but write methods are only supposed to be called on main thread only.
 *
 * Current supported blocks include all subclasses of {@link gregtech.common.blocks.BlockCasingsAbstract} and meta tile
 * entities that is rendered as a full block (e.g. very large pipes, frames or hatches) To add support to a new type of
 * block... * if it's a simple dumb block, i.e. render type of 0, just switch to
 * {@link gregtech.common.render.GTRendererCasing} * if it already has a ISBRH/TESR, you need to render the overlay of
 * each side after your blocks' main texture of each side rendered
 *
 * {@link OverlayTicket OverlayTickets} returned by set methods will strongly hold a reference to RenderOverlay
 * instances. Do not keep them around indefinitely or else there will be a memory leak
 *
 * This obviously doesn't work on server side...
 */
public class RenderOverlay {

    private static final LoadingCache<World, RenderOverlay> instances = CacheBuilder.newBuilder()
        .weakKeys()
        .build(new CacheLoader<>() {

            @Override
            public RenderOverlay load(World key) {
                return new RenderOverlay();
            }
        });
    private final Map<ChunkCoordinates, ITexture[]> overlays = new ConcurrentHashMap<>();
    private final Multimap<RenderLocation, OverlayTicket> ticketsByLocation = ArrayListMultimap.create();
    private final Multimap<ChunkCoordIntPair, OverlayTicket> byChunk = HashMultimap.create();

    public OverlayTicket set(int xOwner, int yOwner, int zOwner, int x, int y, int z, ForgeDirection dir,
        ITexture texture, int zlevel) {
        ChunkCoordinates loc = new ChunkCoordinates(x, y, z);
        RenderLocation renderLoc = new RenderLocation(x, y, z, dir);
        ITexture[] holder = overlays.computeIfAbsent(loc, xx -> new ITexture[6]);
        OverlayTicket ticket = new OverlayTicket(xOwner, yOwner, zOwner, x, y, z, dir, texture, zlevel);
        ticketsByLocation.put(renderLoc, ticket);
        if (holder[dir.ordinal()] == null) {
            holder[dir.ordinal()] = texture;
        } else {
            holder[dir.ordinal()] = getTextureArray(renderLoc);
        }
        byChunk.put(new ChunkCoordIntPair(xOwner >> 4, zOwner >> 4), ticket);
        return ticket;
    }

    private ITexture getTextureArray(RenderLocation renderLoc) {
        Collection<OverlayTicket> tickets = ticketsByLocation.get(renderLoc);
        if (tickets.isEmpty()) {
            return null;
        }
        // composing into a single object makes it easier to handle everywhere
        return TextureFactory.of(
            tickets.stream()
                .sorted()
                .map(t -> t.texture)
                .toArray(ITexture[]::new));
    }

    public ITexture[] get(int x, int y, int z) {
        return overlays.get(new ChunkCoordinates(x, y, z));
    }

    public static void reset() {
        instances.invalidateAll();
    }

    public static RenderOverlay getOrCreate(World world) {
        RenderOverlay instance = getRenderOverlay(world);
        if (instance == null) {
            instance = new RenderOverlay();
            instances.put(world, instance);
        }
        return instance;
    }

    private static RenderOverlay getRenderOverlay(World world) {
        try {
            return instances.get(world);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static ITexture[] get(IBlockAccess b, int x, int y, int z) {
        World world;
        if (b instanceof ChunkCacheAccessor cache) {
            world = cache.getWorld();
        } else if (b instanceof World w) {
            world = w;
        } else if (Mods.Angelica.isModLoaded() && b instanceof IBlockAccessExtended w) {
            world = w.getWorld();
        } else {
            return null;
        }
        RenderOverlay instance = getRenderOverlay(world);
        if (instance == null) {
            return null;
        }
        return instance.get(x, y, z);
    }

    public static void set(World world, int xOwner, int yOwner, int zOwner, int x, int y, int z, ForgeDirection dir,
        ITexture texture, int zlevel) {
        getOrCreate(world).set(xOwner, yOwner, zOwner, x, y, z, dir, texture, zlevel);
    }

    public static void onWorldUnload(World world) {
        if (!world.isRemote) {
            return;
        }
        instances.invalidate(world);
    }

    public static void onChunkUnload(World world, ChunkCoordIntPair chunk) {
        if (!world.isRemote) {
            return;
        }
        RenderOverlay inst = getRenderOverlay(world);
        if (inst == null) return;
        for (OverlayTicket loc : inst.byChunk.removeAll(chunk)) {
            loc.remove();
        }
        if (inst.byChunk.isEmpty()) {
            instances.invalidate(world);
        }
    }

    public final class OverlayTicket implements Comparable<OverlayTicket> {

        final int xOwner, yOwner, zOwner;
        final int x, y, z;
        final ForgeDirection dir;
        final ITexture texture;
        final int order;

        public OverlayTicket(int xOwner, int yOwner, int zOwner, int x, int y, int z, ForgeDirection dir,
            ITexture texture, int order) {
            this.xOwner = xOwner;
            this.yOwner = yOwner;
            this.zOwner = zOwner;
            this.x = x;
            this.y = y;
            this.z = z;
            this.dir = dir;
            this.texture = texture;
            this.order = order;
        }

        public void remove() {
            RenderLocation renderLoc = new RenderLocation(x, y, z, dir);
            if (!ticketsByLocation.remove(renderLoc, this)) return;
            ChunkCoordinates loc = new ChunkCoordinates(x, y, z);
            ITexture[] container = overlays.get(loc);
            if (container != null && container[dir.ordinal()] != null) {
                ITexture newArray = getTextureArray(renderLoc);
                container[dir.ordinal()] = newArray;
                if (newArray != null) return;
                for (ITexture elem : container) {
                    if (elem != null) {
                        return;
                    }
                }
                overlays.remove(loc);
                byChunk.remove(new ChunkCoordIntPair(xOwner >> 4, zOwner >> 4), loc);
            }
        }

        @Override
        public int compareTo(@NotNull RenderOverlay.OverlayTicket o) {
            return Integer.compare(this.order, o.order);
        }
    }

    private static final class RenderLocation {

        final int x, y, z;
        final ForgeDirection dir;

        public RenderLocation(int x, int y, int z, ForgeDirection dir) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.dir = dir;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof RenderLocation that)) return false;

            return x == that.x && y == that.y && z == that.z && dir == that.dir;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            result = 31 * result + dir.hashCode();
            return result;
        }
    }
}
