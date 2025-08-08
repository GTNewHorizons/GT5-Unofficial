package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import gregtech.api.util.GTMusicSystem;
import gregtech.client.ISeekingSound;
import gregtech.client.SeekingOggCodec;

@Mixin(SoundManager.class)
public class SoundManagerMixin {

    @WrapOperation(
        method = "playSound",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/audio/SoundPoolEntry;getSoundPoolEntryLocation()Lnet/minecraft/util/ResourceLocation;"))
    ResourceLocation gt5u$wrap(SoundPoolEntry instance, Operation<ResourceLocation> original,
        @Local(argsOnly = true) ISound sound) {
        ResourceLocation result = original.call(instance);
        if (sound instanceof ISeekingSound seekingSound) {
            result = SeekingOggCodec.seekResource(result, seekingSound.getSeekMillisecondOffset());
        }
        return result;
    }

    @Inject(method = "stopAllSounds", at = @At("HEAD"))
    void gt5u$notifyOfSoundStop(CallbackInfo ci) {
        GTMusicSystem.ClientSystem.onSoundBatchStop();
    }

    @Inject(method = "pauseAllSounds", at = @At("HEAD"))
    void gt5u$notifyOfSoundPause(CallbackInfo ci) {
        GTMusicSystem.ClientSystem.onSoundBatchPause();
    }

    @Inject(method = "resumeAllSounds", at = @At("HEAD"))
    void gt5u$notifyOfSoundResume(CallbackInfo ci) {
        GTMusicSystem.ClientSystem.onSoundBatchResume();
    }
}
