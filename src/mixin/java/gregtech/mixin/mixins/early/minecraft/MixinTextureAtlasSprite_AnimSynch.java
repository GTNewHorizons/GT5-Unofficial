package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.client.TextureAtlasSpriteSyncher;
import gregtech.mixin.interfaces.TextureAtlasSpriteExt;

@Mixin(TextureAtlasSprite.class)
public abstract class MixinTextureAtlasSprite_AnimSynch implements TextureAtlasSpriteExt {

    @Unique
    private boolean gt5u$isSynced = false;

    @Shadow
    protected int frameCounter;

    @Shadow
    public abstract int getFrameCount();

    @Override
    public void gt5u$setSynched(boolean sync) {
        this.gt5u$isSynced = sync;
    }

    @Inject(
        method = "updateAnimation",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/data/AnimationMetadataSection;getFrameTimeSingle(I)I",
            shift = Shift.BEFORE))
    public void syncFrameCounter(CallbackInfo ci) {
        if (gt5u$isSynced) {
            this.frameCounter = TextureAtlasSpriteSyncher.getFrameCounter() % this.getFrameCount();
        }
    }
}
