package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.ToolMaterial.class)
public class ItemToolMaterialMixin {

    @Inject(method = "getEfficiencyOnProperMaterial", at = @At("HEAD"), cancellable = true)
    public void gt5u$getEfficiencyOnProperMaterial(CallbackInfoReturnable<Float> cir) {
        if ((Object) this == Item.ToolMaterial.WOOD) {
            cir.setReturnValue(4.0F);
            cir.cancel();
        }
    }
}
