package gregtech.client;

import net.minecraft.client.audio.ISound;

/**
 * Metadata on a sound object used to seek it when starting playback.
 */
public interface ISeekingSound extends ISound {

    /**
     * @return The number of milliseconds to seek by.
     */
    long getSeekMillisecondOffset();
}
