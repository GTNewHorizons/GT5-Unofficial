package kubatech.mixin.mixins.minecraft;

import net.minecraft.entity.EntityLiving;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = EntityLiving.class)
public interface EntityLivingAccessor {

    @Invoker
    void callAddRandomArmor();

    @Invoker
    void callEnchantEquipment();
}
