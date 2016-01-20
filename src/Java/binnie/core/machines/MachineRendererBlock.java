package binnie.core.machines;

import binnie.core.BinnieCore;
import binnie.core.proxy.BinnieProxy;
import binnie.core.resource.BinnieResource;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MachineRendererBlock
{
  public static MachineRendererBlock instance = new MachineRendererBlock();
  private BinnieResource texture;
  private ModelBlock model;
  
  public MachineRendererBlock()
  {
    this.model = new ModelBlock();
  }
  
  public void renderMachine(BinnieResource texture, double x, double y, double z, float var8)
  {
    this.texture = texture;
    
    GL11.glPushMatrix();
    
    GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
    
    BinnieCore.proxy.bindTexture(texture);
    
    GL11.glPushMatrix();
    
    this.model.render((float)x, (float)y, (float)z, 0.0625F, 0.0625F, 0.0625F);
    
    GL11.glPopMatrix();
    
    GL11.glPopMatrix();
  }
}
