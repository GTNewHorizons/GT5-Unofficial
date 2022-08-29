package kubatech.loaders.item.items;

import java.util.List;
import kubatech.loaders.item.ItemProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class TeaIngredient extends ItemProxy {
    public TeaIngredient(String unlocalizedName) {
        super("teaingredient." + unlocalizedName, "teaingredient/" + unlocalizedName);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer entity, List<String> tooltipList, boolean showDebugInfo) {
        tooltipList.add(EnumChatFormatting.GRAY + "This is Tea ingredient");
    }
}
