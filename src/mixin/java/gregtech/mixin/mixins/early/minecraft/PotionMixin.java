package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.potion.Potion;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.mixin.interfaces.PotionExt;

@Mixin(Potion.class)
public class PotionMixin implements PotionExt {

    @Shadow
    @Final
    private boolean isBadEffect;

    @Override
    public boolean gt5u$isBadEffect() {
        return this.isBadEffect;
    }

}
