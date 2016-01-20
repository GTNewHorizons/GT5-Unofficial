package binnie.core.machines;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

class ModelBlock
  extends ModelBase
{
  private ModelRenderer Block;
  
  public ModelBlock()
  {
    this.textureWidth = 64;
    this.textureHeight = 32;
    
    this.Block = new ModelRenderer(this, 0, 0);
    this.Block.addBox(0.0F, 0.0F, 0.0F, 16, 16, 16);
    this.Block.setRotationPoint(-8.0F, 8.0F, -8.0F);
    this.Block.setTextureSize(64, 32);
    this.Block.mirror = true;
    setRotation(this.Block, 0.0F, 0.0F, 0.0F);
  }
  
  public void render(float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(null, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    this.Block.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
  }
}
