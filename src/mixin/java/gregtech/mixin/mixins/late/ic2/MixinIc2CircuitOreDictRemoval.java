package gregtech.mixin.mixins.late.ic2;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import ic2.core.IC2;

@Mixin(value = IC2.class, remap = false)
public class MixinIc2CircuitOreDictRemoval {

    @WrapWithCondition(
        method = "load(Lcpw/mods/fml/common/event/FMLPreInitializationEvent;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/oredict/OreDictionary;registerOre(Ljava/lang/String;Lnet/minecraft/item/ItemStack;)V"))
    private boolean gt5u$wrapRegisterOre(String name, ItemStack stack) {
        return !"circuitBasic".equals(name) && !"circuitAdvanced".equals(name);
    }
}
