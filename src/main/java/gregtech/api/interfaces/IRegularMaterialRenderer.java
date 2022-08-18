package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public interface IRegularMaterialRenderer {

    void renderRegularItem(IItemRenderer.ItemRenderType type, ItemStack aStack, IIcon icon, IIcon overlay);
}
