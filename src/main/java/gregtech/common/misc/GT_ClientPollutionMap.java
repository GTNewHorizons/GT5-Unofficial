package gregtech.common.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class GT_ClientPollutionMap {
    private static final byte RADIUS = 15; //Area to keep stored.
    private static final byte DISTANCE_RELOAD_MAP = 2;
    private static final byte SIZE = RADIUS*2+1;

    private int x0, z0;
    private int dim;

    private boolean initialized = false;

    private static short[][] chunkMatrix; //short because reasons.


    public GT_ClientPollutionMap(){ }

    public void reset() {
        initialized = false;
        System.out.println("RESET MAP");
    }

    private void initialize(int playerChunkX, int playerChunkZ, int dimension) {
        initialized = true;
        chunkMatrix = new short[SIZE][SIZE];
        x0 = playerChunkX;
        z0 = playerChunkZ;
        dim = dimension;
        System.out.println("INIT MAP" + x0 + "/" + z0 + " d:" + dim);
    }

    public void addChunkPollution(int chunkX, int chunkZ, int pollution) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player == null || player.worldObj == null)
            return;

        int playerXChunk = MathHelper.floor_double(player.posX) >> 4;
        int playerZChunk = MathHelper.floor_double(player.posZ) >> 4; //posX/Z seems to be always loaded,

        if (!initialized) {
            initialize(playerXChunk, playerZChunk, player.dimension);
        }

        if (dim != player.dimension) {
            initialize(playerXChunk, playerZChunk, player.dimension);
        }

        if (Math.abs(x0 - playerXChunk) > DISTANCE_RELOAD_MAP || Math.abs(z0 - playerZChunk) > DISTANCE_RELOAD_MAP)
            shiftCenter(playerXChunk, playerZChunk);

        int relX = chunkX - x0 + RADIUS;
        if (relX >= SIZE || relX < 0) //out of bounds
            return;
        int relZ = chunkZ - z0 + RADIUS;
        if (relZ >= SIZE || relZ < 0) //out of bounds
            return;

        pollution = pollution/225;
        if (pollution > Short.MAX_VALUE) //Sanity
            chunkMatrix[relX][relZ] = Short.MAX_VALUE; //Max pollution = 7,3mill
        else if (pollution < 0)
            chunkMatrix[relX][relZ] = 0;
        else
            chunkMatrix[relX][relZ] = (short) (pollution);
    }

    //xy interpolation, between 4 chunks as corners, unknown treated as 0.
    public int getPollution(double fx, double fz) {
        if (!initialized)
            return 0;
        int x = MathHelper.floor_double(fx);
        int z = MathHelper.floor_double(fz);
        int xDiff = ((x-8) >> 4) - x0;
        int zDiff = ((z-8) >> 4) - z0;

        if (xDiff < -RADIUS || zDiff < -RADIUS || xDiff >= RADIUS || zDiff >= RADIUS )
            return 0;

        //coordinates in shifted chunk.
        x = (x-8) % 16;
        z = (z-8) % 16;
        if (x < 0)
            x = 16+x;
        if (z < 0)
            z = 16+z;

        int xi = 15 - x;
        int zi = 15 - z;

        //read pollution in 4 corner chunks
        int offsetX = RADIUS+xDiff;
        int offsetZ = RADIUS+zDiff;

        int c00 = chunkMatrix[offsetX][offsetZ];
        int c10 = chunkMatrix[offsetX+1][offsetZ];
        int c01 = chunkMatrix[offsetX][offsetZ+1];
        int c11 = chunkMatrix[offsetX+1][offsetZ+1];

        //Is divided by 15*15 but is handled when storing chunk data.
        return c00*xi*zi + c10*x*zi + c01*xi*z + c11*x*z;
    }

    //shift the matrix to fit new center
    private void shiftCenter(int chunkX, int chunkZ) {
        int xDiff = chunkX - x0;
        int zDiff = chunkZ - z0;
        boolean[] allEmpty = new boolean[SIZE]; //skip check z row if its empty.
        if (xDiff > 0)
            for (byte x = 0; x < SIZE; x++) {
                int xOff = x + xDiff;
                if (xOff < SIZE) {
                    chunkMatrix[x] = chunkMatrix[xOff].clone();
                } else {
                    chunkMatrix[x] = new short[SIZE];
                    allEmpty[x] = true;
                }
            }
        else if (xDiff < 0)
            for (byte x = SIZE-1; x >= 0; x--) {
                int xOff = x + xDiff;
                if (xOff > 0) {
                    chunkMatrix[x] = chunkMatrix[xOff].clone();
                } else {
                    chunkMatrix[x] = new short[SIZE];
                    allEmpty[x] = true;
                }
            }

        if (zDiff > 0)
            for (byte x = 0; x < SIZE; x++) {
                if (allEmpty[x])
                    continue;
                for (int z = 0; z < SIZE ; z++) {
                    int zOff = z + zDiff;
                    chunkMatrix[x][z] = (zOff < SIZE) ? chunkMatrix[x][zOff] : 0;
                }
            }
        else if (zDiff < 0)
            for (byte x = 0; x < SIZE; x++) {
                if (allEmpty[x])
                    continue;
                for (int z = SIZE-1; z >= 0 ; z--) {
                    int zOff = z+zDiff;
                    chunkMatrix[x][z] = (zOff > 0) ? chunkMatrix[x][zOff] : 0;
                }
            }

        x0 = chunkX;
        z0 = chunkZ;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("CENTER: " + x0 + "/" + z0));
    }

    public void draw3x3Chinks(double cX, double cZ) {
        if (!initialized) return;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);

        int px = (MathHelper.floor_double(cX)) >> 4;
        int pz = (MathHelper.floor_double(cZ)) >> 4;

        int mX = px - x0 + RADIUS;
        int mZ = pz - z0 + RADIUS;

        int LEN = 8;

        StringBuilder b = new StringBuilder();
        b.append(len(" ", LEN));
        for (int x = -2; x < 3; x++) {
            b.append(len("x:" + (mX - RADIUS + x0 + x), LEN));
        }
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(b.toString(), 200, 130 + -3 * 16, 0xFFFFFFFF);

        for (int z = 2; z > -3; z--) {
            b = new StringBuilder();
            b.append(len("z:" + (mZ - RADIUS + z0 +z), LEN));
            int zz = mZ + z;
            for (int x = -2; x < 3; x++) {
                int xx = mX + x;
                if (xx >= 0 && xx < SIZE && zz >= 0 && zz < SIZE) {
                    b.append(len(chunkMatrix[xx][zz]*225/1000, LEN));
                } else {
                    b.append(len(-1, LEN));
                }
            }
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(b.toString(), 200, 130 + -z * 16, 0xFFFFFFFF);
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private String len(String s, int lenghth) {
        StringBuilder sBuilder = new StringBuilder(lenghth);
        sBuilder.append(s);
        while (sBuilder.length() < lenghth) {
            sBuilder.append(" ");
        }
        return sBuilder.toString();
    }

    private String len(int s, int lenghth) {
        return len(Integer.toString(s), lenghth);
    }
}
