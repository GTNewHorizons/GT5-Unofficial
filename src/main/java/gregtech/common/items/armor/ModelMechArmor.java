package gregtech.common.items.armor;

import static java.awt.Color.HSBtoRGB;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class ModelMechArmor extends ModelBiped {

    ModelRenderer jettank2;
    ModelRenderer jetconnector2;
    ModelRenderer jetbooster2;
    ModelRenderer jettank1;
    ModelRenderer jetconnector1;
    ModelRenderer jetbooster1;
    ModelRenderer core1;
    ModelRenderer core2;
    ModelRenderer core3;
    ModelRenderer core4;

    private short[] color = new short[] { -1 };

    public ModelMechArmor(float s) {
        super(s, 0, 64, 128);
        setupJetpack();
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scale) {
        if (GL11.glGetInteger(GL11.GL_DEPTH_FUNC) == GL11.GL_EQUAL) {
            return;
        }

        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        if (color[0] == -1) {
            return;
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

        double ticks = (entity != null) ? (entity.ticksExisted + ageInTicks) : 0.0;

        // tests for infinity frame
        if (color[0] == 255 && color[1] == 255 && color[2] == 255) {
            float hue = (float) ((ticks % 150.0F) / 150.0F);

            int rgb = HSBtoRGB(hue, 1.0F, 1.0F);

            float r = ((rgb >> 16) & 0xFF) / 255.0F;
            float g = ((rgb >> 8) & 0xFF) / 255.0F;
            float b = (rgb & 0xFF) / 255.0F;

            GL11.glColor3f(r, g, b);
        } else {
            float pulse = 0.8F + 0.2F * (float) Math.sin(ticks / 30.0);

            GL11.glColor3f((color[0] / 255.0F) * pulse, (color[1] / 255.0F) * pulse, (color[2] / 255.0F) * pulse);
        }

        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.5F, 0.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public void setColor(short[] color) {
        this.color = color;
    }

    private void setupJetpack() {
        core1 = new ModelRenderer(this, 32, 0);
        core1.addBox(-2F, 3F, -3F, 4, 4, 1);
        core1.setRotationPoint(0F, 0F, 0F);
        core1.setTextureSize(64, 32);
        core1.mirror = true;
        setRotation(core1, 0F, 0F, 0F);
        core1.showModel = false;

        core2 = new ModelRenderer(this, 32, 5);
        core2.addBox(-2F, 3F, -3F, 4, 4, 1);
        core2.setRotationPoint(0F, 0F, 0F);
        core2.setTextureSize(64, 32);
        core2.mirror = true;
        setRotation(core2, 0F, 0F, 0F);
        core2.showModel = false;

        core3 = new ModelRenderer(this, 32, 10);
        core3.addBox(-2F, 3F, -3F, 4, 4, 1);
        core3.setRotationPoint(0F, 0F, 0F);
        core3.setTextureSize(64, 32);
        core3.mirror = true;
        setRotation(core3, 0F, 0F, 0F);
        core3.showModel = false;

        core4 = new ModelRenderer(this, 42, 0);
        core4.addBox(-2F, 3F, -3F, 4, 4, 1);
        core4.setRotationPoint(0F, 0F, 0F);
        core4.setTextureSize(64, 32);
        core4.mirror = true;
        setRotation(core4, 0F, 0F, 0F);
        core4.showModel = false;

        jettank2 = new ModelRenderer(this, 52, 0);
        jettank2.addBox(-4F, 2F, 2F, 3, 5, 3);
        jettank2.setRotationPoint(0F, 0F, 0F);
        jettank2.setTextureSize(64, 32);
        jettank2.mirror = true;
        setRotation(jettank2, 0F, 0F, 0F);

        jetconnector2 = new ModelRenderer(this, 60, 13);
        jetconnector2.addBox(-3F, 7F, 3F, 1, 1, 1);
        jetconnector2.setRotationPoint(0F, 0F, 0F);
        jetconnector2.setTextureSize(64, 32);
        jetconnector2.mirror = true;
        setRotation(jetconnector2, 0F, 0F, 0F);

        jetbooster2 = new ModelRenderer(this, 52, 8);
        jetbooster2.addBox(-4F, 8F, 2F, 3, 2, 3);
        jetbooster2.setRotationPoint(0F, 0F, 0F);
        jetbooster2.setTextureSize(64, 32);
        jetbooster2.mirror = true;
        setRotation(jetbooster2, 0F, 0F, 0F);

        jettank1 = new ModelRenderer(this, 52, 0);
        jettank1.addBox(1F, 2F, 2F, 3, 5, 3);
        jettank1.setRotationPoint(0F, 0F, 0F);
        jettank1.setTextureSize(64, 32);
        jettank1.mirror = true;
        setRotation(jettank1, 0F, 0F, 0F);

        jetconnector1 = new ModelRenderer(this, 60, 13);
        jetconnector1.addBox(2F, 7F, 3F, 1, 1, 1);
        jetconnector1.setRotationPoint(0F, 0F, 0F);
        jetconnector1.setTextureSize(64, 32);
        jetconnector1.mirror = true;
        setRotation(jetconnector1, 0F, 0F, 0F);

        jetbooster1 = new ModelRenderer(this, 52, 8);
        jetbooster1.addBox(1F, 8F, 2F, 3, 2, 3);
        jetbooster1.setRotationPoint(0F, 0F, 0F);
        jetbooster1.setTextureSize(64, 32);
        jetbooster1.mirror = true;
        setRotation(jetbooster1, 0F, 0F, 0F);

        bipedBody.addChild(jettank1);

        bipedBody.addChild(core1);
        bipedBody.addChild(core2);
        bipedBody.addChild(core3);
        bipedBody.addChild(core4);

        jettank1.addChild(jettank2);

        jettank1.addChild(jetbooster1);
        jettank1.addChild(jetbooster2);

        jettank1.addChild(jetconnector1);
        jettank1.addChild(jetconnector2);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}
