package gregtech.common.items.flinttools;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class FlintSword extends ItemSword {

    public FlintSword() {
        super(FlintTools.FLINT_MATERIAL);
        this.setUnlocalizedName("flintSword");
        this.setTextureName("gregtech:tools/flintSword");
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
        // Create the default sword item
        ItemStack flintTool = new ItemStack(this);

        // Enchant the sword with Fire Aspect I
        flintTool.addEnchantment(Enchantment.fireAspect, 1);

        // Add the enchanted sword to the creative tab and NEI
        subItems.add(flintTool);
    }
}
