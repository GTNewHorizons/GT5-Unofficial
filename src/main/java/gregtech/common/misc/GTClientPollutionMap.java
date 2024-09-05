package gregtech.common.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.util.MathHelper;

public class GTClientPollutionMap {

    private static final byte RADIUS = 24;
    private static final byte DISTANCE_RELOAD_MAP = 5; // When player moved x chunks, shift the map to new center.
    private static final byte SIZE = RADIUS * 2 + 1; // Area to keep stored.

    private int x0, z0;
    private int dim;

    private boolean initialized = false;

    private static short[][] chunkMatrix; // short because reasons.

    public GTClientPollutionMap() {}

    public void reset() {
        initialized = false;
    }

    private void initialize(int playerChunkX, int playerChunkZ, int dimension) {
        initialized = true;
        chunkMatrix = new short[SIZE][SIZE];
        x0 = playerChunkX;
        z0 = playerChunkZ;
        dim = dimension;
    }

    public void addChunkPollution(int chunkX, int chunkZ, int pollution) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player == null || player.worldObj == null) return;

        int playerXChunk = MathHelper.floor_double(player.posX) >> 4;
        int playerZChunk = MathHelper.floor_double(player.posZ) >> 4; // posX/Z seems to be always loaded,

        if (!initialized) {
            initialize(playerXChunk, playerZChunk, player.dimension);
        }

        if (dim != player.dimension) {
            initialize(playerXChunk, playerZChunk, player.dimension);
        }

        if (Math.abs(x0 - playerXChunk) > DISTANCE_RELOAD_MAP || Math.abs(z0 - playerZChunk) > DISTANCE_RELOAD_MAP)
            shiftCenter(playerXChunk, playerZChunk);

        int relX = chunkX - x0 + RADIUS;
        if (relX >= SIZE || relX < 0) // out of bounds
            return;
        int relZ = chunkZ - z0 + RADIUS;
        if (relZ >= SIZE || relZ < 0) // out of bounds
            return;

        pollution = pollution / 225;
        if (pollution > Short.MAX_VALUE) // Sanity
            chunkMatrix[relX][relZ] = Short.MAX_VALUE; // Max pollution = 7,3mill
        else if (pollution < 0) chunkMatrix[relX][relZ] = 0;
        else chunkMatrix[relX][relZ] = (short) (pollution);
    }

    // xy interpolation, between 4 chunks as corners, unknown treated as 0.
    public int getPollution(double fx, double fz) {
        if (!initialized) return 0;
        int x = MathHelper.floor_double(fx);
        int z = MathHelper.floor_double(fz);
        int xDiff = ((x - 8) >> 4) - x0;
        int zDiff = ((z - 8) >> 4) - z0;

        if (xDiff < -RADIUS || zDiff < -RADIUS || xDiff >= RADIUS || zDiff >= RADIUS) return 0;

        // coordinates in shifted chunk.
        x = (x - 8) % 16;
        z = (z - 8) % 16;
        if (x < 0) x = 16 + x;
        if (z < 0) z = 16 + z;

        int xi = 15 - x;
        int zi = 15 - z;

        // read pollution in 4 corner chunks
        int offsetX = RADIUS + xDiff;
        int offsetZ = RADIUS + zDiff;

        int c00 = chunkMatrix[offsetX][offsetZ];
        int c10 = chunkMatrix[offsetX + 1][offsetZ];
        int c01 = chunkMatrix[offsetX][offsetZ + 1];
        int c11 = chunkMatrix[offsetX + 1][offsetZ + 1];

        // Is divided by 15*15 but is handled when storing chunk data.
        return c00 * xi * zi + c10 * x * zi + c01 * xi * z + c11 * x * z;
    }

    // shift the matrix to fit new center
    private void shiftCenter(int chunkX, int chunkZ) {
        int xDiff = chunkX - x0;
        int zDiff = chunkZ - z0;
        boolean[] allEmpty = new boolean[SIZE]; // skip check z row if its empty.
        if (xDiff > 0) for (byte x = 0; x < SIZE; x++) {
            int xOff = x + xDiff;
            if (xOff < SIZE) {
                chunkMatrix[x] = chunkMatrix[xOff].clone();
            } else {
                chunkMatrix[x] = new short[SIZE];
                allEmpty[x] = true;
            }
        }
        else if (xDiff < 0) for (byte x = SIZE - 1; x >= 0; x--) {
            int xOff = x + xDiff;
            if (xOff > 0) {
                chunkMatrix[x] = chunkMatrix[xOff].clone();
            } else {
                chunkMatrix[x] = new short[SIZE];
                allEmpty[x] = true;
            }
        }

        if (zDiff > 0) for (byte x = 0; x < SIZE; x++) {
            if (allEmpty[x]) continue;
            for (int z = 0; z < SIZE; z++) {
                int zOff = z + zDiff;
                chunkMatrix[x][z] = (zOff < SIZE) ? chunkMatrix[x][zOff] : 0;
            }
        }
        else if (zDiff < 0) for (byte x = 0; x < SIZE; x++) {
            if (allEmpty[x]) continue;
            for (int z = SIZE - 1; z >= 0; z--) {
                int zOff = z + zDiff;
                chunkMatrix[x][z] = (zOff > 0) ? chunkMatrix[x][zOff] : 0;
            }
        }

        x0 = chunkX;
        z0 = chunkZ;
    }
}
