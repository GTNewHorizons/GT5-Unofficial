package gregtech.common.items.armor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMechArmor extends ModelBiped {

    // fields
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

    public ModelMechArmor(float s) {
        super(s, 0, 64, 128);

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

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bipedHeadwear.showModel = false;
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
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
