package gregtech.mixin.mixins.early.minecraft;

import net.minecraftforge.fluids.Fluid;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.mixin.ObjectWithId;

@Mixin(Fluid.class)
public class FluidMixin_Ids implements ObjectWithId {

    @Unique
    public int gt5u$fluidId;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    public void init(CallbackInfo ci) {
        gt5u$fluidId = NEXT_ID.getAndAdd(1);
    }

    @Override
    public int getId() {
        return gt5u$fluidId;
    }
}
