package gregtech.api.enchants;

import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTConfig;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;

public class EnchantmentRadioactivity extends EnchantmentDamage {

    public static EnchantmentRadioactivity INSTANCE;

    public EnchantmentRadioactivity() {
        super(GTConfig.addIDConfig(ConfigCategories.IDs.enchantments, "Radioactivity", 14), 0, -1);
        GTLanguageManager.addStringLocalization(getName(), "Radioactivity");
        Materials.Plutonium.setEnchantmentForTools(this, 1)
            .setEnchantmentForArmors(this, 1);
        Materials.Uranium235.setEnchantmentForTools(this, 2)
            .setEnchantmentForArmors(this, 2);
        Materials.Plutonium241.setEnchantmentForTools(this, 3)
            .setEnchantmentForArmors(this, 3);
        Materials.NaquadahEnriched.setEnchantmentForTools(this, 4)
            .setEnchantmentForArmors(this, 4);
        Materials.Naquadria.setEnchantmentForTools(this, 5)
            .setEnchantmentForArmors(this, 5);
        INSTANCE = this;
    }

    @Override
    public int getMinEnchantability(int aLevel) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxEnchantability(int aLevel) {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean canApply(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }

    @Override
    public void func_151367_b(EntityLivingBase aHurtEntity, Entity aDamagingEntity, int aLevel) {
        GTUtility.applyRadioactivity(aHurtEntity, aLevel, 1);
    }

    @Override
    public String getName() {
        return "enchantment.damage.radioactivity";
    }
}
