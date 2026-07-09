package gregtech.api.items.armor.renderer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class VoxelArmorRenderer extends ModelBiped {

    private static final Set<VoxelArmorRenderer> ALL_INSTANCES = Collections
        .newSetFromMap(new WeakHashMap<VoxelArmorRenderer, Boolean>());

    private final ResourceLocation compiledTexture;

    private final Map<String, Integer> displayLists = new HashMap<>();
    private BedrockArmorModel rawModel;
    private boolean isCompiled = false;

    private static final float PIXEL_SCALE = 0.0625F;
    private static final float RAD_TO_DEG = 180F / (float) Math.PI;

    public VoxelArmorRenderer(float scale, BedrockArmorModel compiledModel, ResourceLocation compiledTexture) {
        super(scale, 0, 64, 64);
        this.rawModel = compiledModel;
        this.compiledTexture = compiledTexture;

        ALL_INSTANCES.add(this);
    }

    private void drawFace(Tessellator tess, float[] uv, float[] uvSize, int texWidth, int texHeight, float trX,
        float trY, float trZ, // Top-Right
        float tlX, float tlY, float tlZ, // Top-Left
        float blX, float blY, float blZ, // Bottom-Left
        float brX, float brY, float brZ) { // Bottom-Right

        if (uv == null || uvSize == null || uv.length < 2 || uvSize.length < 2) return;

        float uMin = uv[0] / (float) texWidth;
        float vMin = uv[1] / (float) texHeight;
        float uMax = (uv[0] + uvSize[0]) / (float) texWidth;
        float vMax = (uv[1] + uvSize[1]) / (float) texHeight;

        tess.addVertexWithUV(trX, trY, trZ, uMax, vMin);
        tess.addVertexWithUV(tlX, tlY, tlZ, uMin, vMin);
        tess.addVertexWithUV(blX, blY, blZ, uMin, vMax);
        tess.addVertexWithUV(brX, brY, brZ, uMax, vMax);
    }

    private void compileModelToOpenGL() {
        int tw = this.rawModel.geometryList.get(0).description.texture_width;
        int th = this.rawModel.geometryList.get(0).description.texture_height;

        for (BedrockArmorModel.Bone bone : this.rawModel.geometryList.get(0).bones) {
            int listId = GL11.glGenLists(1);
            GL11.glNewList(listId, GL11.GL_COMPILE);

            Tessellator tess = Tessellator.instance;

            if (bone.cubes != null) {
                for (BedrockArmorModel.Cube cube : bone.cubes) {

                    float mcX = cube.origin[0] - bone.pivot[0];
                    float mcY = -(cube.origin[1] - bone.pivot[1]) - cube.size[1];
                    float mcZ = cube.origin[2] - bone.pivot[2];

                    float minX = (mcX - cube.inflate) * PIXEL_SCALE;
                    float maxX = (mcX + cube.size[0] + cube.inflate) * PIXEL_SCALE;
                    float minY = (mcY - cube.inflate) * PIXEL_SCALE;
                    float maxY = (mcY + cube.size[1] + cube.inflate) * PIXEL_SCALE;
                    float minZ = (mcZ - cube.inflate) * PIXEL_SCALE;
                    float maxZ = (mcZ + cube.size[2] + cube.inflate) * PIXEL_SCALE;

                    GL11.glPushMatrix();
                    applyCubeRotation(cube);

                    tess.startDrawingQuads();

                    // spotless:off
                    if (cube.uv != null) {

                        // UP (-Y)
                        tess.setNormal(0.0F, 1.0F, 0.0F);
                        if (cube.uv.up != null) {
                            drawFace(tess, cube.uv.up.uv, cube.uv.up.uv_size, tw, th,
                                maxX, minY, minZ, // TR
                                minX, minY, minZ, // TL
                                minX, minY, maxZ, // BL
                                maxX, minY, maxZ  // BR
                            );
                        }

                        // DOWN (+Y)
                        tess.setNormal(0.0F, -1.0F, 0.0F);
                        if (cube.uv.down != null) {
                            drawFace(tess, cube.uv.down.uv, cube.uv.down.uv_size, tw, th,
                                maxX, maxY, maxZ, // TR
                                minX, maxY, maxZ, // TL
                                minX, maxY, minZ, // BL
                                maxX, maxY, minZ  // BR
                            );
                        }

                        // NORTH (-Z)
                        tess.setNormal(0.0F, 0.0F, -1.0F);
                        if (cube.uv.north != null) {
                            drawFace(tess, cube.uv.north.uv, cube.uv.north.uv_size, tw, th,
                                maxX, minY, minZ, // TR
                                minX, minY, minZ, // TL
                                minX, maxY, minZ, // BL
                                maxX, maxY, minZ  // BR
                            );
                        }

                        // SOUTH (+Z)
                        tess.setNormal(0.0F, 0.0F, 1.0F);
                        if (cube.uv.south != null) {
                            drawFace(tess, cube.uv.south.uv, cube.uv.south.uv_size, tw, th,
                                minX, minY, maxZ, // TR
                                maxX, minY, maxZ, // TL
                                maxX, maxY, maxZ, // BL
                                minX, maxY, maxZ  // BR
                            );
                        }

                        // WEST (-X)
                        tess.setNormal(-1.0F, 0.0F, 0.0F);
                        if (cube.uv.west != null) {
                            drawFace(tess, cube.uv.west.uv, cube.uv.west.uv_size, tw, th,
                                minX, minY, minZ, // TR
                                minX, minY, maxZ, // TL
                                minX, maxY, maxZ, // BL
                                minX, maxY, minZ  // BR
                            );
                        }

                        // EAST (+X)
                        tess.setNormal(1.0F, 0.0F, 0.0F);
                        if (cube.uv.east != null) {
                            drawFace(tess, cube.uv.east.uv, cube.uv.east.uv_size, tw, th,
                                maxX, minY, maxZ, // TR
                                maxX, minY, minZ, // TL
                                maxX, maxY, minZ, // BL
                                maxX, maxY, maxZ  // BR
                            );
                        }
                    }
                    // spotless:on

                    tess.draw();
                    GL11.glPopMatrix();
                }
            }

            GL11.glEndList();
            this.displayLists.put(bone.name, listId);
        }

        this.rawModel = null;
        this.isCompiled = true;
    }

    private void applyCubeRotation(BedrockArmorModel.Cube cube) {
        if (cube.rotation != null && cube.pivot != null) {
            GL11.glTranslatef(cube.pivot[0] * PIXEL_SCALE, -cube.pivot[1] * PIXEL_SCALE, cube.pivot[2] * PIXEL_SCALE);

            if (cube.rotation[2] != 0.0F) GL11.glRotatef(cube.rotation[2], 0.0F, 0.0F, 1.0F);
            if (cube.rotation[1] != 0.0F) GL11.glRotatef(cube.rotation[1], 0.0F, 1.0F, 0.0F);
            if (cube.rotation[0] != 0.0F) GL11.glRotatef(cube.rotation[0], 1.0F, 0.0F, 0.0F);

            GL11.glTranslatef(-cube.pivot[0] * PIXEL_SCALE, cube.pivot[1] * PIXEL_SCALE, -cube.pivot[2] * PIXEL_SCALE);
        }
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        if (!this.isCompiled) {
            compileModelToOpenGL();
        }

        setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        if (this.compiledTexture != null) {
            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(this.compiledTexture);
        }

        if (this.bipedHead.showModel) renderBone("head", this.bipedHead);
        if (this.bipedBody.showModel) renderBone("body", this.bipedBody);

        if (this.bipedRightArm.showModel) renderBone("right_arm", this.bipedRightArm);
        if (this.bipedLeftArm.showModel) renderBone("left_arm", this.bipedLeftArm);

        if (this.bipedRightLeg.showModel) renderBone("right_leg", this.bipedRightLeg);
        if (this.bipedLeftLeg.showModel) renderBone("left_leg", this.bipedLeftLeg);
    }

    private void renderBone(String boneName, ModelRenderer vanillaPart) {
        Integer listId = this.displayLists.get(boneName);
        if (listId == null || vanillaPart.isHidden) return;

        GL11.glPushMatrix();

        GL11.glTranslatef(0.0F, vanillaPart.rotationPointY * 0.0625F, vanillaPart.rotationPointZ * 0.0625F);

        if (vanillaPart.rotateAngleZ != 0.0F) GL11.glRotatef(vanillaPart.rotateAngleZ * RAD_TO_DEG, 0.0F, 0.0F, 1.0F);
        if (vanillaPart.rotateAngleY != 0.0F) GL11.glRotatef(vanillaPart.rotateAngleY * RAD_TO_DEG, 0.0F, 1.0F, 0.0F);
        if (vanillaPart.rotateAngleX != 0.0F) GL11.glRotatef(vanillaPart.rotateAngleX * RAD_TO_DEG, 1.0F, 0.0F, 0.0F);

        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glCallList(listId);
        GL11.glDisable(GL11.GL_NORMALIZE);

        GL11.glPopMatrix();
    }

    public void deleteOpenGLData() {
        if (!this.isCompiled) return;

        for (Integer listId : this.displayLists.values()) {
            GL11.glDeleteLists(listId, 1);
        }
        this.displayLists.clear();
        this.isCompiled = false;
    }

    public static void invalidateAllRenderers() {
        for (VoxelArmorRenderer renderer : ALL_INSTANCES) {
            if (renderer != null) {
                renderer.deleteOpenGLData();
            }
        }
    }
}
