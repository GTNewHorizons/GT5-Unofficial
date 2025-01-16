package gregtech.mixin.mixins.early.minecraft;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.village.MerchantRecipeList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntityVillager.class)
public class VanillaTradingMixin {

    @Inject(method = "func_146089_b", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void gt5u$removeEyeOfEnder(MerchantRecipeList p_146089_0_, Item p_146089_1_, Random p_146089_2_,
        float p_146089_3_, CallbackInfo ci) {
        if (p_146089_1_.equals(Items.ender_eye)) {
            ci.cancel();
        }
    }
}
