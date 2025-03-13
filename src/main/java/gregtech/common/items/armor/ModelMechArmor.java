package gregtech.common.items.armor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMechArmor extends ModelBiped
{
  //fields
    ModelRenderer jettank2;
    ModelRenderer jetconnector2;
    ModelRenderer jetbooster2;
    ModelRenderer jettank1;
    ModelRenderer jetconnector1;
    ModelRenderer jetbooster1;

  public ModelMechArmor()
  {
    textureWidth = 64;
    textureHeight = 32;
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

      this.bipedBody.addChild(jettank1);

      this.jettank1.addChild(jettank2);

      this.jettank1.addChild(jetbooster1);
      this.jettank1.addChild(jetbooster2);

      this.jettank1.addChild(jetconnector1);
      this.jettank1.addChild(jetconnector2);
  }

  public void changeAugmentRender(int augment, boolean show) {
      switch (augment) {
          case 0 -> jettank1.showModel = show;
      }
  }

  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }

  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

}
