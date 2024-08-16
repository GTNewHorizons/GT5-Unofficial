package gregtech.client;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.util.ResourceLocation;

public class ElectricJukeboxSound implements ISound, ISeekingSound, ITickableSound {

    public final ResourceLocation soundResource;
    public float volume = 1.0F;
    public float pitch = 1.0F;
    public float xPosition;
    public float yPosition;
    public float zPosition;
    public boolean repeating = false;
    public int repeatDelay = 0;
    public ISound.AttenuationType attenuationType = AttenuationType.LINEAR;
    public boolean donePlaying = false;

    public final long seekMs;

    public ElectricJukeboxSound(ResourceLocation resource, long seekMs) {
        this.soundResource = resource;
        this.seekMs = seekMs;
    }

    public ElectricJukeboxSound(ResourceLocation soundResource, float volume, long seekMs, float xPosition,
        float yPosition, float zPosition) {
        this(soundResource, seekMs);
        this.volume = volume;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.zPosition = zPosition;
    }

    @Override
    public long getSeekMillisecondOffset() {
        return seekMs;
    }

    @Override
    public ResourceLocation getPositionedSoundLocation() {
        return soundResource;
    }

    @Override
    public boolean canRepeat() {
        return repeating;
    }

    @Override
    public int getRepeatDelay() {
        return repeatDelay;
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public float getXPosF() {
        return xPosition;
    }

    @Override
    public float getYPosF() {
        return yPosition;
    }

    @Override
    public float getZPosF() {
        return zPosition;
    }

    @Override
    public AttenuationType getAttenuationType() {
        return attenuationType;
    }

    @Override
    public boolean isDonePlaying() {
        return donePlaying;
    }

    @Override
    public void update() {
        // no-op
    }
}
