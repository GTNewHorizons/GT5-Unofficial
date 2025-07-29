package gregtech.mixin.mixins.late.forestry;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import forestry.apiculture.genetics.alleles.AlleleEffectThrottled;
import gregtech.mixin.interfaces.accessors.AlleleEffectThrottledAccessor;

@Mixin(AlleleEffectThrottled.class)
public class AlleleEffectThrottledMixin implements AlleleEffectThrottledAccessor {

    @Final
    @Shadow(remap = false)
    private int throttle;

    @Override
    public int gt5u$getThrottle() {
        return throttle;
    }
}
