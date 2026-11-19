package gregtech.api.items.armor.renderer;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class VoxelArmorRenderer extends ModelBiped {

    private static final float PIXEL_SCALE = 0.0625F;
    private static final float RAD_TO_DEG = 180F / (float) Math.PI;

    private final BakedArmorModel bakedModel;
    private final ResourceLocation compiledTexture;
    private final Map<String, Integer> displayLists = new HashMap<>();
    private boolean isCompiled = false;

    public VoxelArmorRenderer(float scale, BakedArmorModel bakedModel, ResourceLocation compiledTexture) {
        super(scale, 0, 64, 64);
        this.bakedModel = bakedModel;
        this.compiledTexture = compiledTexture;
    }

    public ResourceLocation getCompiledTexture() {
        return compiledTexture;
    }

    private void compileModelToOpenGL() {
        for (BakedBone bone : bakedModel.bonesByName.values()) {
            displayLists.put(bone.name, compileBone(bone));
        }
        isCompiled = true;
    }

    private int compileBone(BakedBone bone) {
        float inverseTextureWidth = 1.0F / bakedModel.textureWidth;
        float inverseTextureHeight = 1.0F / bakedModel.textureHeight;

        int displayListId = GLAllocation.generateDisplayLists(1);
        GL11.glNewList(displayListId, GL11.GL_COMPILE);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        for (BakedPolygon polygon : bone.polygons) {
            tessellator.setNormal(polygon.normalX, polygon.normalY, polygon.normalZ);
            for (int quadCorner = 0; quadCorner < 4; quadCorner++) {
                int sourceVertex = Math.min(quadCorner, polygon.vertexCount - 1);
                tessellator.addVertexWithUV(
                    polygon.positions[sourceVertex * 3] - bone.pivotX,
                    polygon.positions[sourceVertex * 3 + 1] - bone.pivotY,
                    polygon.positions[sourceVertex * 3 + 2] - bone.pivotZ,
                    polygon.textureUVs[sourceVertex * 2] * inverseTextureWidth,
                    polygon.textureUVs[sourceVertex * 2 + 1] * inverseTextureHeight);
            }
        }
        tessellator.draw();

        GL11.glEndList();
        return displayListId;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                       float headPitch, float scale) {
        if (!isCompiled) compileModelToOpenGL();

        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

        if (compiledTexture != null) {
            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(compiledTexture);
        }

        if (bipedHead.showModel) renderBone("head", bipedHead);
        if (bipedBody.showModel) renderBone("body", bipedBody);
        if (bipedRightArm.showModel) renderBone("right_arm", bipedRightArm);
        if (bipedLeftArm.showModel) renderBone("left_arm", bipedLeftArm);
        if (bipedRightLeg.showModel) renderBone("right_leg", bipedRightLeg);
        if (bipedLeftLeg.showModel) renderBone("left_leg", bipedLeftLeg);
    }

    private void renderBone(String boneName, ModelRenderer vanillaPart) {
        Integer listId = displayLists.get(boneName);
        if (listId == null || vanillaPart.isHidden) return;

        GL11.glPushMatrix();
        GL11.glTranslatef(
            vanillaPart.rotationPointX * PIXEL_SCALE,
            vanillaPart.rotationPointY * PIXEL_SCALE,
            vanillaPart.rotationPointZ * PIXEL_SCALE);

        if (vanillaPart.rotateAngleZ != 0.0F) GL11.glRotatef(vanillaPart.rotateAngleZ * RAD_TO_DEG, 0.0F, 0.0F, 1.0F);
        if (vanillaPart.rotateAngleY != 0.0F) GL11.glRotatef(vanillaPart.rotateAngleY * RAD_TO_DEG, 0.0F, 1.0F, 0.0F);
        if (vanillaPart.rotateAngleX != 0.0F) GL11.glRotatef(vanillaPart.rotateAngleX * RAD_TO_DEG, 1.0F, 0.0F, 0.0F);

        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glCallList(listId);
        GL11.glDisable(GL11.GL_NORMALIZE);

        GL11.glPopMatrix();
    }

    public void deleteOpenGLData() {
        if (!isCompiled) return;

        for (Integer listId : displayLists.values()) {
            GLAllocation.deleteDisplayLists(listId);
        }
        displayLists.clear();
        isCompiled = false;
    }
}
