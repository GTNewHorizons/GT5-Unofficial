package tectech.thing.block;


import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import tectech.Reference;

import java.nio.FloatBuffer;

import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_0;
import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_1;
import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_2;

public class RenderForgeOfGods extends TileEntitySpecialRenderer {


    private static ShaderProgram starProgram;
    private static IModelCustom starModel;
    private static float modelNormalize = .0067f * 2;


    private static boolean initialized = false;
    private static int u_Color = -1, u_ModelMatrix = -1;
    private Matrix4fStack starModelMatrix = new Matrix4fStack(3);

    private static ShaderProgram beamProgram;
    private static int a_VertexID = -1;
    private static int u_BeamModelMatrix = -1;
    private static int u_CameraPosition = -1, u_SegmentArray = -1, u_SegmentQuads = -1;
    private static int u_BeamIntensity = -1, u_BeamColor = -1, u_BeamTime = -1;
    private static int beam_vboID = -1;
    private static int maxSegments = -1;
    private static final int beamSegmentQuads = 16;
    private static Matrix4fStack beamModelMatrix = new Matrix4fStack(2);

    private void init(){
        try{
            starProgram = new ShaderProgram(Reference.MODID,
                "shaders/star.vert.glsl",
                "shaders/star.frag.glsl");

            u_Color = starProgram.getUniformLocation("u_Color");
            u_ModelMatrix = starProgram.getUniformLocation("u_ModelMatrix");

        }catch(Exception e){
            System.out.println(e.getMessage());
            return;
        }

        starModel = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.MODID, "models/Star.obj"));

        try{
            beamProgram = new ShaderProgram(Reference.MODID,
                "shaders/gorgeBeam.vert.glsl",
                "shaders/gorgeBeam.frag.glsl");


            u_BeamModelMatrix = beamProgram.getUniformLocation("u_ModelMatrix");
            u_CameraPosition = beamProgram.getUniformLocation("u_CameraPosition");
            u_SegmentQuads = beamProgram.getUniformLocation("u_SegmentQuads");
            u_SegmentArray = beamProgram.getUniformLocation("u_SegmentArray");
            u_BeamColor = beamProgram.getUniformLocation("u_Color");
            u_BeamIntensity = beamProgram.getUniformLocation("u_Intensity");
            u_BeamTime = beamProgram.getUniformLocation("u_Time");



            a_VertexID = beamProgram.getAttribLocation("a_VertexID");
        }catch(Exception e){
            System.out.println(e.getMessage());
            return;
        }


        beamProgram.use();
        GL20.glUniform1f(u_SegmentQuads,(float)beamSegmentQuads);
        maxSegments = 10;//GL20.glGetActiveUniformSize(beamProgram.getProgram(), u_SegmentArray);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(maxSegments*beamSegmentQuads*6*3);


        for (int i = 0; i < maxSegments; i++) {
            for (int j = 0; j < beamSegmentQuads; j++) {
                for (int v = 0; v < 6; v++) {
                    int segID = i*beamSegmentQuads*6;
                    int quadID = j*6;
                    int vertID = segID+quadID+v;
                    buffer.put(vertID);
                    buffer.put(0);
                    buffer.put(0);
                }
            }
        }

        buffer.flip();
        beam_vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, beam_vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(a_VertexID, 1, GL11.GL_FLOAT, false, 3 * Float.BYTES, 0);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        ShaderProgram.clear();
        initialized = true;
    }

    public void RenderStarLayer(Vector4f color, ResourceLocation texture, float size, Vector3f rotationAxis, float degrees){
        starModelMatrix.pushMatrix();
        starModelMatrix.rotate((degrees/180f*((float) Math.PI)), rotationAxis.x,rotationAxis.y,rotationAxis.z);
        starModelMatrix.scale(size,size,size);

        this.bindTexture(texture);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        //Enable transparency if needed
        if (color.w < 1 && color.w > 0){
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false); // Disable depth writing for transparency
        }

        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        GL20.glUniformMatrix4(u_ModelMatrix,false, starModelMatrix.get(matrixBuffer));
        GL20.glUniform4f(u_Color,color.x,color.y,color.z,color.w);
        starModel.renderAll();
        GL11.glPopAttrib();
        starModelMatrix.popMatrix();
    }

    public void RenderEntireStar(TileEntityForgeOfGods tile, double x, double y, double z, float timer){
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glDisable(GL11.GL_LIGHTING);

        starProgram.use();

        float cx = (float)x+.5f;
        float cy = (float)y+.5f;
        float cz = (float)z+.5f;
        float size = modelNormalize;
        starModelMatrix.clear();
        starModelMatrix.translate(cx,cy,cz);

        size*=tile.getStarRadius();

        float r = tile.colorR, g = tile.colorG, b = tile.colorB;
        RenderStarLayer(new Vector4f(r,g,b,1f),STAR_LAYER_0, size,
            new Vector3f( 0F, 1F, 1).normalize(),130 + (timer / 64) % 360);
        RenderStarLayer(new Vector4f(r,g,b,0.4f),STAR_LAYER_1, size*1.02f,
            new Vector3f(1F, 1F, 0F).normalize(),-49 + (timer / 64) % 360);
        RenderStarLayer(new Vector4f(r,g,b,0.2f),STAR_LAYER_2, size*1.04f,
            new Vector3f(1F, 0F, 1F).normalize(),67 + (timer / 64) % 360);

        ShaderProgram.clear();
        GL11.glPopAttrib();
    }

    public void bufferSoftBeam(TileEntityForgeOfGods tile){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(maxSegments*3);

        float angle = tile.getStartAngle();
        float radius = tile.getStarRadius()*1.1f;
        float startx = -radius*((float) Math.cos(angle));
        float starty = radius*((float) Math.sin(angle));

        buffer.put(starty);
        buffer.put(startx);
        buffer.put(0);

        for (int i = tile.getRingCount()-1; i >=0; i--) {
            buffer.put(tile.getLenRadius(i));
            buffer.put(tile.getLensDistance(i));
            buffer.put(1f);
        }

        buffer.put(TileEntityForgeOfGods.backPlateRadius);
        buffer.put(TileEntityForgeOfGods.backPlateDistance);
        buffer.put(-.05f);

        buffer.rewind();
        GL20.glUniform3(u_SegmentArray, buffer);
        //return buffer;
    }


    public void bufferIntenseBeam(TileEntityForgeOfGods tile){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(maxSegments*3);
        float angle = tile.getStartAngle();
        float radius = tile.getStarRadius()*1.05f;
        float startx = -radius*((float) Math.cos(angle));
        float starty = radius*((float) Math.sin(angle));

        //first lens means the one closest to the star
        int firstLens = tile.getRingCount()-1;

        float nextx = tile.getLensDistance(firstLens);
        float nexty = tile.getLenRadius(firstLens)*.75f;

        float backx = Math.max(-radius,(nextx+radius)/2);
        float backy = TileEntityForgeOfGods.interpolate(startx,nextx,starty,nexty,backx);

        buffer.put(backy);
        buffer.put(backx);
        buffer.put(0);

        float transparency = .2f;
        for (int i = tile.getRingCount()-1; i >=0; i--) {
            buffer.put(tile.getLenRadius(i)/2);
            buffer.put(tile.getLensDistance(i));
            buffer.put(transparency);
            transparency+= .3f;
        }

        float currx = tile.getLensDistance(0);
        float curry = tile.getLenRadius(0)/2;
        float lastx = TileEntityForgeOfGods.backPlateDistance;
        float lasty = Math.min(tile.getLenRadius(firstLens),TileEntityForgeOfGods.backPlateRadius);


        float midx = lastx+8f;
        float midy = TileEntityForgeOfGods.interpolate(currx,lastx,curry,lasty,midx);

        buffer.put(midy);
        buffer.put(midx);
        buffer.put(transparency);

        buffer.put(lasty);
        buffer.put(lastx);
        buffer.put(0f);

        buffer.rewind();
        GL20.glUniform3(u_SegmentArray, buffer);
        //return buffer;
    }



    public void RenderBeamSegment(TileEntityForgeOfGods tile, double x, double y, double z, float timer){
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        //GL11.glDisable(GL11.GL_TEXTURE_2D);
        //GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        this.bindTexture(new ResourceLocation(Reference.MODID, "models/spaceLayer.png"));

        float cx = (float)x+.5f;
        float cy = (float)y+.5f;
        float cz = (float)z+.5f;
        beamModelMatrix.clear();
        beamModelMatrix.translate(cx,cy,cz);
        beamModelMatrix.rotate(tile.rotAngle, tile.rotx, tile.roty, tile.rotz);

        beamProgram.use();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false); // Disable depth writing for transparency

        bufferSoftBeam(tile);

        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        GL20.glUniformMatrix4(u_BeamModelMatrix,false, beamModelMatrix.get(matrixBuffer));

        beamModelMatrix.invert();

        Vector4f cameraPosition = new Vector4f(ActiveRenderInfo.objectX,ActiveRenderInfo.objectY,ActiveRenderInfo.objectZ,1);
        cameraPosition = beamModelMatrix.transform(cameraPosition);
        GL20.glUniform3f(u_CameraPosition,cameraPosition.x,cameraPosition.y,cameraPosition.z);
        GL20.glUniform3f(u_BeamColor,tile.colorR, tile.colorG, tile.colorB);
        GL20.glUniform1f(u_BeamIntensity,2);
        GL20.glUniform1f(u_BeamTime,timer);

        GL20.glEnableVertexAttribArray(a_VertexID);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, maxSegments*beamSegmentQuads*6);

        GL20.glUniform3f(u_BeamColor,1,1,1);
        GL20.glUniform1f(u_BeamIntensity,4);
        bufferIntenseBeam(tile);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, maxSegments*beamSegmentQuads*6);

        GL20.glDisableVertexAttribArray(a_VertexID);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

        GL11.glPopAttrib();
        ShaderProgram.clear();
    }




    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileEntityForgeOfGods forgeTile)) return;
        {

            if (!initialized){
                init();
                if(!initialized)
                    return;
            }

            float timer = forgeTile.getWorldObj().getWorldInfo().getWorldTotalTime()%(20*36000) + timeSinceLastTick;

            RenderEntireStar(forgeTile,x,y,z,timer);

            RenderBeamSegment(forgeTile,x,y,z,timer);

            /*
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);

            // Innermost layer should be opaque
            enableOpaqueColorInversion();
            renderStarLayer(0, STAR_LAYER_0, new Color(1.0f, 0.4f, 0.05f, 1.0f), 1.0f, 25);
            disableOpaqueColorInversion();

            enablePseudoTransparentColorInversion();
            renderStarLayer(1, STAR_LAYER_1, new Color(1.0f, 0.4f, 0.05f, 1.0f), 0.4f, 25);
            renderStarLayer(2, STAR_LAYER_2, new Color(1.0f, 0.4f, 0.05f, 1.0f), 0.2f, 25);

            GL11.glPopAttrib();
            GL11.glPopMatrix();

             */
        }
    }

    public static void enablePseudoTransparentColorInversion() {
        GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glLogicOp(GL11.GL_OR_INVERTED);
    }

    public static void enableOpaqueColorInversion() {
        GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glLogicOp(GL11.GL_COPY_INVERTED);
    }

    public static void disableOpaqueColorInversion() {
        GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
    }
}
