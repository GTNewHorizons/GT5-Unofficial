package gregtech.api.items.armor.renderer;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class VoxelArmorRenderer extends ModelBiped {

    private final ResourceLocation compiledTexture;

    private final Map<String, Integer> displayLists = new HashMap<>();
    private final BedrockArmorModel rawModel;
    private boolean isCompiled = false;

    private static final float PIXEL_SCALE = 0.0625F;
    private static final float RAD_TO_DEG = 180F / (float) Math.PI;

    public VoxelArmorRenderer(float scale, BedrockArmorModel compiledModel, ResourceLocation compiledTexture) {
        super(scale, 0, 64, 64);
        this.rawModel = compiledModel;
        this.compiledTexture = compiledTexture;
    }

    private void drawFace(Tessellator tess, float[] uv, float[] uvSize, int texWidth, int texHeight, float x1, float y1,
        float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {

        if (uv == null || uvSize == null || uv.length < 2 || uvSize.length < 2) return;

        float uMin = uv[0] / (float) texWidth;
        float vMin = uv[1] / (float) texHeight;
        float uMax = (uv[0] + uvSize[0]) / (float) texWidth;
        float vMax = (uv[1] + uvSize[1]) / (float) texHeight;

        tess.addVertexWithUV(x1, y1, z1, uMax, vMax);
        tess.addVertexWithUV(x2, y2, z2, uMin, vMax);
        tess.addVertexWithUV(x3, y3, z3, uMin, vMin);
        tess.addVertexWithUV(x4, y4, z4, uMax, vMin);
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
                        // 1. TOP (up)
                        if (cube.uv.up != null) {
                            drawFace(tess, cube.uv.up.uv, cube.uv.up.uv_size, tw, th,
                                minX, minY, maxZ, maxX, minY, maxZ, maxX, minY, minZ, minX, minY, minZ);
                        }

                        // 2. BOTTOM (down)
                        if (cube.uv.down != null) {
                            drawFace(tess, cube.uv.down.uv, cube.uv.down.uv_size, tw, th,
                                minX, maxY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, minX, maxY, minZ);
                        }

                        // 3. NORTH
                        if (cube.uv.north != null) {
                            drawFace(tess, cube.uv.north.uv, cube.uv.north.uv_size, tw, th,
                                maxX, maxY, minZ, minX, maxY, minZ, minX, minY, minZ, maxX, minY, minZ);
                        }

                        // 4. SOUTH
                        if (cube.uv.south != null) {
                            drawFace(tess, cube.uv.south.uv, cube.uv.south.uv_size, tw, th,
                                minX, maxY, maxZ, maxX, maxY, maxZ, maxX, minY, maxZ, minX, minY, maxZ);
                        }

                        // 5. WEST
                        if (cube.uv.west != null) {
                            drawFace(tess, cube.uv.west.uv, cube.uv.west.uv_size, tw, th,
                                minX, maxY, minZ, minX, maxY, maxZ, minX, minY, maxZ, minX, minY, minZ);
                        }

                        // 6. EAST
                        if (cube.uv.east != null) {
                            drawFace(tess, cube.uv.east.uv, cube.uv.east.uv_size, tw, th,
                                maxX, maxY, maxZ, maxX, maxY, minZ, maxX, minY, minZ, maxX, minY, maxZ);
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

        GL11.glTranslatef(
            vanillaPart.rotationPointX * 0.0625F,
            vanillaPart.rotationPointY * 0.0625F,
            vanillaPart.rotationPointZ * 0.0625F);

        if (vanillaPart.rotateAngleZ != 0.0F) GL11.glRotatef(vanillaPart.rotateAngleZ * RAD_TO_DEG, 0.0F, 0.0F, 1.0F);
        if (vanillaPart.rotateAngleY != 0.0F) GL11.glRotatef(vanillaPart.rotateAngleY * RAD_TO_DEG, 0.0F, 1.0F, 0.0F);
        if (vanillaPart.rotateAngleX != 0.0F) GL11.glRotatef(vanillaPart.rotateAngleX * RAD_TO_DEG, 1.0F, 0.0F, 0.0F);

        GL11.glCallList(listId);

        GL11.glPopMatrix();
    }
}
