package gregtech.mixin.mixins.early.minecraft.pollution;

import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.common.pollution.Pollution;
import gregtech.common.pollution.PollutionConfig;

// Merged from ModMixins under the MIT License Copyright bartimaeusnek & GTNewHorizons
@Mixin(Explosion.class)
public class MixinExplosionPollution {

    @Shadow
    public float explosionSize;

    @Shadow
    private World worldObj;

    @Shadow
    public double explosionX;

    @Shadow
    public double explosionZ;

    @Inject(method = "doExplosionA", at = @At(value = "TAIL"))
    public void gt5u$addExplosionPollution(CallbackInfo ci) {
        if (!this.worldObj.isRemote) {
            Pollution.addPollution(
                this.worldObj.getChunkFromBlockCoords((int) this.explosionX, (int) this.explosionZ),
                (int) Math.ceil(explosionSize * PollutionConfig.explosionPollutionAmount));
        }
    }
}
