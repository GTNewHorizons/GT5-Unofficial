package binnie.core.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;

public class ItemMetadataRenderer
  implements IItemRenderer
{
  public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type)
  {
    return (type == IItemRenderer.ItemRenderType.INVENTORY) || (type == IItemRenderer.ItemRenderType.ENTITY) || (type == IItemRenderer.ItemRenderType.EQUIPPED) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON);
  }
  
  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper)
  {
    if (type == IItemRenderer.ItemRenderType.INVENTORY) {
      return helper == IItemRenderer.ItemRendererHelper.INVENTORY_BLOCK;
    }
    if (type == IItemRenderer.ItemRenderType.ENTITY) {
      return (helper == IItemRenderer.ItemRendererHelper.ENTITY_BOBBING) || (helper == IItemRenderer.ItemRendererHelper.ENTITY_ROTATION);
    }
    if ((type == IItemRenderer.ItemRenderType.EQUIPPED) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)) {
      return helper == IItemRenderer.ItemRendererHelper.EQUIPPED_BLOCK;
    }
    return false;
  }
  
  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data)
  {
    GL11.glPushMatrix();
    









    Block block = Block.getBlockFromItem(item.getItem());
    if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
      GL11.glTranslated(0.5D, 0.5D, 0.5D);
    }
    if ((type == IItemRenderer.ItemRenderType.INVENTORY) && 
      (block.getRenderBlockPass() != 0))
    {
      GL11.glAlphaFunc(516, 0.1F);
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    }
    GL11.glPushMatrix();
    ((RenderBlocks)data[0]).renderBlockAsItem(block, TileEntityMetadata.getItemDamage(item), 1.0F);
    
    GL11.glPopMatrix();
    GL11.glPopMatrix();
  }
}
