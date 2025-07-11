package gregtech.mixin.mixins.late.ic2;

import net.minecraft.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import ic2.core.item.ItemIC2;

@Mixin(value = ItemIC2.class, remap = false)
public class MixinIc2FissionFuelRemoval {

    @WrapWithCondition(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lcpw/mods/fml/common/registry/GameRegistry;registerItem(Lnet/minecraft/item/Item;Ljava/lang/String;)V"))
    public boolean gt5u$wrapRegister(Item item, String name) {
        return !name.equals("reactorMOXSimpledepleted") && !name.equals("reactorMOXDualdepleted")
            && !name.equals("reactorMOXQuaddepleted")
            && !name.equals("reactorUraniumSimpledepleted")
            && !name.equals("reactorUraniumDualdepleted")
            && !name.equals("reactorUraniumQuaddepleted")
            && !name.equals("reactorLithiumCell")
            && !name.equals("itemTritiumCell");
    }
}
