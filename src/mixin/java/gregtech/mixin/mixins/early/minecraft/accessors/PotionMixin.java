package gregtech.mixin.mixins.early.minecraft.accessors;

import net.minecraft.potion.Potion;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.mixin.interfaces.accessors.PotionAccessor;

@Mixin(Potion.class)
public class PotionMixin implements PotionAccessor {

    @Shadow
    @Final
    private boolean isBadEffect;

    @Override
    public boolean gt5u$isBadEffect() {
        return this.isBadEffect;
    }

}
