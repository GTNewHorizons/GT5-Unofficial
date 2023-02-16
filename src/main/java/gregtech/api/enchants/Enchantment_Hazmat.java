package gregtech.api.enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_LanguageManager;

public class Enchantment_Hazmat extends Enchantment {

    public static Enchantment_Hazmat INSTANCE;

    public Enchantment_Hazmat() {
        super(GT_Config.addIDConfig(ConfigCategories.IDs.enchantments, "Hazmat", 13), 0, EnumEnchantmentType.armor);
        GT_LanguageManager.addStringLocalization(getName(), "Hazmat");
        INSTANCE = this;
    }

    @Override
    public int getMinEnchantability(int aLevel) {
        return 50;
    }

    @Override
    public int getMaxEnchantability(int aLevel) {
        return 100;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApply(ItemStack aStack) {
        return aStack != null && (aStack.getItem() instanceof ItemArmor);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }

    @Override
    public String getName() {
        return "enchantment.protection.hazmat";
    }
}
