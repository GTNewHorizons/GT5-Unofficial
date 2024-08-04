package gregtech.api.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import gregtech.api.multitileentity.interfaces.IMultiTileEntity;

public class GT_Util {

    // Last broken tile entity
    public static final ThreadLocal<TileEntity> LAST_BROKEN_TILEENTITY = new ThreadLocal<>();

    public static Tuple tuple(String key, Object value) {
        return new Tuple(key, value);
    }

    public static NBTTagCompound fuseNBT(NBTTagCompound nbt1, NBTTagCompound nbt2) {
        if (nbt1 == null) return nbt2 == null ? new NBTTagCompound() : (NBTTagCompound) nbt2.copy();
        final NBTTagCompound rNBT = (NBTTagCompound) nbt1.copy();
        if (nbt2 == null) return rNBT;
        for (Object tKey : nbt2.func_150296_c /* getKeySet */())
            if (!rNBT.hasKey(tKey.toString())) rNBT.setTag(tKey.toString(), nbt2.getTag(tKey.toString()));
        return rNBT;
    }

    /**
     * Construct a NBTTagCompound from a series of key, value pairs. Inspired from GT6.
     */
    public static NBTTagCompound makeNBT(Tuple... tags) {
        final NBTTagCompound nbt = new NBTTagCompound();
        for (Tuple t : tags) {
            if (t.getSecond() == null) continue;

            if (t.getSecond() instanceof Boolean) nbt.setBoolean(
                t.getFirst()
                    .toString(),
                (Boolean) t.getSecond());
            else if (t.getSecond() instanceof Byte) nbt.setByte(
                t.getFirst()
                    .toString(),
                (Byte) t.getSecond());
            else if (t.getSecond() instanceof Short) nbt.setShort(
                t.getFirst()
                    .toString(),
                (Short) t.getSecond());
            else if (t.getSecond() instanceof Integer) nbt.setInteger(
                t.getFirst()
                    .toString(),
                (Integer) t.getSecond());
            else if (t.getSecond() instanceof Long) nbt.setLong(
                t.getFirst()
                    .toString(),
                (Long) t.getSecond());
            else if (t.getSecond() instanceof Float) nbt.setFloat(
                t.getFirst()
                    .toString(),
                (Float) t.getSecond());
            else if (t.getSecond() instanceof Double) nbt.setDouble(
                t.getFirst()
                    .toString(),
                (Double) t.getSecond());
            else if (t.getSecond() instanceof String) nbt.setString(
                t.getFirst()
                    .toString(),
                (String) t.getSecond());
            else if (t.getSecond() instanceof NBTBase) nbt.setTag(
                t.getFirst()
                    .toString(),
                (NBTBase) t.getSecond());
            else nbt.setString(
                t.getFirst()
                    .toString(),
                t.getSecond()
                    .toString());
        }

        return nbt;
    }

    /**
     * Get a TileEntity
     */
    public static TileEntity getTileEntity(World world, int x, int y, int z, boolean aLoadUnloadedChunks) {
        if (aLoadUnloadedChunks || world.blockExists(x, y, z)) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof IMultiTileEntity && ((IMultiTileEntity) tileEntity).isDead()) return null;
            if (tileEntity != null) return tileEntity;
            tileEntity = LAST_BROKEN_TILEENTITY.get();
            if (tileEntity != null && tileEntity.xCoord == x && tileEntity.yCoord == y && tileEntity.zCoord == z)
                return tileEntity;
        }
        return null;
    }

    /** Sets the TileEntity at the passed position, with the option of turning adjacent TileEntity updates off. */
    public static TileEntity setTileEntity(World world, int x, int y, int z, TileEntity aTileEntity,
        boolean aCauseTileEntityUpdates) {
        if (aCauseTileEntityUpdates) world.setTileEntity(x, y, z, aTileEntity);
        else {
            final Chunk tChunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
            if (tChunk != null) {
                world.addTileEntity(aTileEntity);
                tChunk.func_150812_a /* setBlockTileEntityInChunk */(x & 15, y, z & 15, aTileEntity);
                tChunk.setChunkModified();
            }
        }
        return aTileEntity;
    }

    public static boolean setBlock(World world, int x, int y, int z, Block block, short aMeta, long aFlags,
        boolean aRemoveGrassBelow) {
        if (aRemoveGrassBelow) {
            final Block blockBelow = world.getBlock(x, y - 1, z);
            if (blockBelow == Blocks.grass || blockBelow == Blocks.mycelium)
                world.setBlock(x, y - 1, z, Blocks.dirt, 0, (byte) aFlags);
        }
        return world.setBlock(x, y, z, block, aMeta, (byte) aFlags);
    }

    public static TileEntity getTileEntity(World world, ChunkCoordinates coords, boolean loadUnloadedChunks) {
        return getTileEntity(world, coords.posX, coords.posY, coords.posZ, loadUnloadedChunks);
    }

    /** Marks a Chunk dirty so it is saved */
    public static boolean markChunkDirty(World world, int x, int z) {
        if (world == null || world.isRemote) return false;
        Chunk aChunk = world.getChunkFromBlockCoords(x, z);
        if (aChunk == null) {
            world.getBlockMetadata(x, 0, z);
            aChunk = world.getChunkFromBlockCoords(x, z);
            if (aChunk == null) {
                GT_Log.err.println(
                    "Some important Chunk does not exist for some reason at Coordinates X: " + x + " and Z: " + z);
                return false;
            }
        }
        aChunk.setChunkModified();
        return true;
    }

    /** Marks a Chunk dirty so it is saved */
    public static boolean markChunkDirty(Object maybeTile) {
        return maybeTile instanceof TileEntity tileEntity
            && markChunkDirty(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.zCoord);
    }

    public static int mixRGBInt(int aRGB1, int aRGB2) {
        return getRGBInt(
            new short[] { (short) ((getR(aRGB1) + getR(aRGB2)) >> 1), (short) ((getG(aRGB1) + getG(aRGB2)) >> 1),
                (short) ((getB(aRGB1) + getB(aRGB2)) >> 1) });
    }

    public static int getRGBInt(short[] aColors) {
        return aColors == null ? 16777215 : (aColors[0] << 16) | (aColors[1] << 8) | aColors[2];
    }

    public static int getRGBaInt(short[] aColors) {
        return aColors == null ? 16777215 : (aColors[0]) << 16 | (aColors[1] << 8) | aColors[2] | (aColors[3] << 24);
    }

    public static String toHexString(short[] aColors) {
        return aColors == null ? "FFFFFF" : Integer.toHexString((aColors[0] << 16) | (aColors[1] << 8) | aColors[2]);
    }

    public static int getRGBInt(short aR, short aG, short aB) {
        return (aR << 16) | (aG << 8) | aB;
    }

    public static int getRGBaInt(short aR, short aG, short aB, short aA) {
        return (aR << 16) | (aG << 8) | aB | (aA << 24);
    }

    public static short[] getRGBaArray(int aColors) {
        return new short[] { (short) ((aColors >>> 16) & 255), (short) ((aColors >>> 8) & 255), (short) (aColors & 255),
            (short) ((aColors >>> 24) & 255) };
    }

    public static short getR(int aColors) {
        return (short) ((aColors >>> 16) & 255);
    }

    public static short getG(int aColors) {
        return (short) ((aColors >>> 8) & 255);
    }

    public static short getB(int aColors) {
        return (short) (aColors & 255);
    }

    public static short getA(int aColors) {
        return (short) ((aColors >>> 24) & 255);
    }
}
