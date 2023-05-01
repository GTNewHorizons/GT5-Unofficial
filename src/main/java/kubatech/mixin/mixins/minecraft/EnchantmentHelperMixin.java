package kubatech.mixin.mixins.minecraft;

import static kubatech.loaders.MobRecipeLoader.randomEnchantmentDetectedString;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import kubatech.api.utils.FastRandom;
import kubatech.loaders.MobRecipeLoader;

@SuppressWarnings("unused")
@Mixin(value = EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    private static final Random rnd = new FastRandom();

    @Inject(method = "addRandomEnchantment", at = @At("HEAD"), require = 1)
    private static void addRandomEnchantmentDetector(Random random, ItemStack itemStack, int enchantabilityLevel,
        CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
        if (MobRecipeLoader.isInGenerationProcess && random instanceof MobRecipeLoader.fakeRand) {
            itemStack.setTagInfo(randomEnchantmentDetectedString, new NBTTagInt(enchantabilityLevel));
        }
    }

    @ModifyVariable(method = "addRandomEnchantment", at = @At("HEAD"), ordinal = 0, argsOnly = true, require = 1)
    private static Random addRandomEnchantmentModifier(Random random) {
        if (!MobRecipeLoader.isInGenerationProcess) return random;
        if (random instanceof MobRecipeLoader.fakeRand) return rnd;
        return random;
    }
}
