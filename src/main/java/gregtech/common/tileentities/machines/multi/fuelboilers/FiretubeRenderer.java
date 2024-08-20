package gregtech.common.tileentities.machines.multi.fuelboilers;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.*;
import org.joml.Matrix4fStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class FiretubeRenderer extends TileEntitySpecialRenderer {
    private static ShaderProgram steamProgram;
    private static boolean isShaderInit;
    private static final WavefrontObject steamBoxObj = new WavefrontObject(new ResourceLocation("gregtech", "models/firetube.obj"));
    private static final int BYTES_P_FLOAT = 4;
    private static final int POS_W = 3; // three floats for a XYZ
    private static final int UV_W = 2; // two floats for a UV
    private static final int STRIDE = POS_W + UV_W;
    // 6 faces, two tris per, 3 vertices per, five floats per
    private static final float[] steamBoxTris = new float[6 * 2 * 3 * STRIDE];
    private static final int VERTEX_COUNT = steamBoxTris.length / STRIDE;

    private static int aPos;
    private static int aUV;

    private static int uBlockAtlas;
    private static int uModelProjection;
    private static int uTime;
    private static int uHeight;
    private static int uminUV;
    private static int udUV;
    private static int vertBuf;

    // Since TESRs are singlethreaded, we can use just the one
    private static final Matrix4fStack modelProjectionMatrix = new Matrix4fStack(2);
    private static final FloatBuffer projMatBuf = BufferUtils.createFloatBuffer(16);

    public FiretubeRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(Tile.class, this);
    }

    private static int addTri(Vertex[] vs, TextureCoordinate[] tcs, int offset, int i) {
        for (int ii = 0; ii < 3; ++ii) {
            final Vertex v = vs[(ii + offset) % 4];
            steamBoxTris[i++] = v.x;
            steamBoxTris[i++] = v.y;
            steamBoxTris[i++] = v.z;

            final TextureCoordinate tc = tcs[(ii + offset) % 4];
            steamBoxTris[i++] = tc.u;
            steamBoxTris[i++] = tc.v;
        }
        return i;
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float timeSinceLastTick) {
        if (te instanceof Tile tile) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            this.bindTexture(TextureMap.locationBlocksTexture);

            if (!isShaderInit) {

                // The water texture is 32x1024 - write the first 16x16 slice as the base UVs, but upload the max size
                // to the shader too. Then, the shader can animate by switching between which slice is being shown
                final float minU = Blocks.flowing_water.getBlockTextureFromSide(0).getMinU();
                final float maxU = Blocks.flowing_water.getBlockTextureFromSide(0).getMaxU();
                final float minV = Blocks.flowing_water.getBlockTextureFromSide(0).getMinV();

                // Stop at 61/64 of the way to the bottom, since we display a 3-long slice
                final float maxV = Blocks.flowing_water.getBlockTextureFromSide(0).getMaxV(); // * 61 / 64;

                final float dU = maxU - minU;
                final float dV = maxV - minV;

                // Read the .obj to a float array
                int i = 0;
                for (final GroupObject go : steamBoxObj.groupObjects) {
                    for (final Face f : go.faces) {
                        i = addTri(f.vertices, f.textureCoordinates, 0, i);
                        i = addTri(f.vertices, f.textureCoordinates, 2, i);
                    }
                }

                steamProgram = new ShaderProgram(
                    "gregtech",
                    "shaders/firetube.vert.glsl",
                    "shaders/firetube.frag.glsl");
                steamProgram.use();

                // Register attributes
                aPos = steamProgram.getAttribLocation("pos");
                aUV = steamProgram.getAttribLocation("uvIn");

                // Register uniforms
                uBlockAtlas = steamProgram.getUniformLocation("u_BlockAtlas");
                uModelProjection = steamProgram.getUniformLocation("u_ModelProjection");
                uTime = steamProgram.getUniformLocation("u_Time");
                uHeight = steamProgram.getUniformLocation("u_Height");
                uminUV = steamProgram.getUniformLocation("u_minUV");
                udUV = steamProgram.getUniformLocation("u_dUV");

                // Create + bind vertex buffer
                vertBuf = GL15.glGenBuffers();
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertBuf);
                // Make a 3x-sized buffer to act as a vec3 position input to the shader.
                final ByteBuffer vertexIDData = BufferUtils.createByteBuffer(VERTEX_COUNT * BYTES_P_FLOAT * STRIDE);
                for (i = 0; i < VERTEX_COUNT * STRIDE; i++) {
                    vertexIDData.putFloat(i * BYTES_P_FLOAT, steamBoxTris[i]);
                }
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexIDData, GL15.GL_STATIC_DRAW);
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

                // Load constant uniforms
                GL20.glUniform1i(uBlockAtlas, OpenGlHelper.defaultTexUnit - GL13.GL_TEXTURE0);
                GL20.glUniform2f(uminUV, minU, minV);
                GL20.glUniform2f(udUV, dU, dV);

                ShaderProgram.clear();

                isShaderInit = true;
            }

            steamProgram.use();

            // Load uniforms
            GL20.glUniform1f(
                uTime,
                ((tile.getWorldObj().getWorldInfo().getWorldTotalTime() % 60) + timeSinceLastTick) / 60f);
            GL20.glUniform1f(uHeight, 1.5f);

            modelProjectionMatrix.identity();
            modelProjectionMatrix.translate((float) x, (float) y, (float) z);
            modelProjectionMatrix.get(0, projMatBuf);
            GL20.glUniformMatrix4(uModelProjection, false, projMatBuf);

            // Draw a bunch of vertices, under the effect of the shaders
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertBuf);
            GL20.glEnableVertexAttribArray(aPos);
            GL20.glEnableVertexAttribArray(aUV);
            GL20.glVertexAttribPointer(aPos, POS_W, GL_FLOAT, false, STRIDE * BYTES_P_FLOAT, 0);
            GL20.glVertexAttribPointer(aUV, UV_W, GL_FLOAT, false, STRIDE * BYTES_P_FLOAT, POS_W * BYTES_P_FLOAT);
            GL11.glEnableClientState(GL_VERTEX_ARRAY);

            GL11.glDisableClientState(GL_CULL_FACE);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, VERTEX_COUNT);
            GL11.glEnableClientState(GL_CULL_FACE);

            GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
            GL20.glDisableVertexAttribArray(0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            ShaderProgram.clear();
        }
    }

    public static class Block extends net.minecraft.block.Block {
        public Block() {
            super(Material.iron);
            this.setResistance(20f);
            this.setHardness(-1.0f);
            this.setBlockName("firetube_render");
            this.setLightLevel(100.0f);
            GameRegistry.registerBlock(this, getUnlocalizedName());
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void registerBlockIcons(IIconRegister iconRegister) {
            blockIcon = iconRegister.registerIcon("gregtech:iconsets/TRANSPARENT");
        }

        @Override
        public String getUnlocalizedName() {
            return "gt.firetube.renderblock";
        }

        @Override
        public boolean isOpaqueCube() {
            return false;
        }

        @Override
        public int getRenderBlockPass() {
            return 1;
        }

        @Override
        public boolean renderAsNormalBlock() {
            return false;
        }

        @Override
        public boolean hasTileEntity(int metadata) {
            return true;
        }

        @Override
        public TileEntity createTileEntity(World world, int metadata) {
            return new Tile();
        }

        @Override
        public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
            return new ArrayList<>();
        }
    }

    public static class Tile extends TileEntity {

    }
}
