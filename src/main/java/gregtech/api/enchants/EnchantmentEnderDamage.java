package gregtech.api.enchants;

import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.util.GTConfig;
import gregtech.api.util.GTLanguageManager;

public class EnchantmentEnderDamage extends EnchantmentDamage {

    public static EnchantmentEnderDamage INSTANCE;

    public EnchantmentEnderDamage() {
        super(GTConfig.addIDConfig(ConfigCategories.IDs.enchantments, "Disjunction", 15), 2, -1);
        GTLanguageManager.addStringLocalization(getName(), "Disjunction");
        INSTANCE = this;
    }

    @Override
    public int getMinEnchantability(int aLevel) {
        return 5 + (aLevel - 1) * 8;
    }

    @Override
    public int getMaxEnchantability(int aLevel) {
        return this.getMinEnchantability(aLevel) + 20;
    }

    @Override
    public void func_151367_b(EntityLivingBase aHurtEntity, Entity aDamagingEntity, int aLevel) {
        if ((aHurtEntity instanceof EntityEnderman || aHurtEntity instanceof EntityDragon
            || (aHurtEntity.getClass()
                .getName()
                .contains(".")
                && aHurtEntity.getClass()
                    .getName()
                    .substring(
                        aHurtEntity.getClass()
                            .getName()
                            .lastIndexOf("."))
                    .contains("Ender")))) {
            // Weakness causes Endermen to not be able to teleport with GT being installed.
            aHurtEntity
                .addPotionEffect(new PotionEffect(Potion.weakness.id, aLevel * 200, Math.max(1, (5 * aLevel) / 7)));
            // They also get Poisoned. If you have this Enchant on an Arrow, you can kill the Ender Dragon easier.
            aHurtEntity
                .addPotionEffect(new PotionEffect(Potion.poison.id, aLevel * 200, Math.max(1, (5 * aLevel) / 7)));
        }
    }

    @Override
    public String getName() {
        return "enchantment.damage.endermen";
    }
}
