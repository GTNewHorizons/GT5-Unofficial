package kubatech.mixin.mixins.minecraft;

import net.minecraft.entity.EntityLivingBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = EntityLivingBase.class)
public interface EntityLivingBaseAccessor {

    @Invoker
    void callDropFewItems(boolean recentlyHit, int lootingLevel);

    @Invoker
    void callDropRareDrop(int lootingLevel);
}
