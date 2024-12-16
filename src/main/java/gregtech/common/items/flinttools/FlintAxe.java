package gregtech.common.items.flinttools;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class FlintAxe extends ItemAxe {

    public FlintAxe() {
        super(FlintTools.FLINT_MATERIAL);
        this.setUnlocalizedName("flintAxe");
        this.setTextureName("gregtech:tools/flintAxe");
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
