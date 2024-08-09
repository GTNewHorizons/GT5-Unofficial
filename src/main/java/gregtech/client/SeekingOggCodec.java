package gregtech.client;

import java.net.URL;

import javax.sound.sampled.AudioFormat;

import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.StringUtils;

import paulscode.sound.SoundBuffer;
import paulscode.sound.codecs.CodecJOrbis;

/**
 * A somewhat hacky codec that allows starting music playback from the middle of a Ogg Vorbis file.
 * Registers for URLs of the form: {@literal jar:blah/blah.jar!blah/music.ogg?seek_ms=5000&ext=.gt5oggseek}
 */
public class SeekingOggCodec extends CodecJOrbis {

    public static final String EXTENSION = "gt5oggseek";

    private volatile boolean fullyInitialized = false;
    private volatile SoundBuffer nextBuffer = null;

    /**
     * Encodes the given millisecond seek amount into a URL/resource name suffix that can be appended to the sound path
     * to start playing from that point onwards.
     */
    public static String getEncodedSeekSuffxix(long milliseconds) {
        return String.format("?seek_ms=%d&ext=." + EXTENSION, milliseconds);
    }

    /**
     * @return The given path with the seeking metadata stripped from the URL
     */
    public static String stripSeekMetadata(String path) {
        while (path.endsWith("." + EXTENSION)) {
            int qMark = path.lastIndexOf('?');
            if (qMark == -1) {
                break;
            }
            path = path.substring(0, qMark);
        }
        return path;
    }

    /**
     * Turns the input sound ResourceLocation into one that is seeked forward the given number of milliseconds
     */
    public static ResourceLocation seekResource(ResourceLocation loc, long milliseconds) {
        String original = loc.getResourcePath();
        original = stripSeekMetadata(original);
        return new ResourceLocation(loc.getResourceDomain(), original + getEncodedSeekSuffxix(milliseconds));
    }

    @Override
    public boolean initialize(URL url) {
        final String textUrl = url.toString();
        final String[] queryParts = url.getQuery()
            .split("&");
        long seekMs = 0;
        for (String part : queryParts) {
            if (!part.startsWith("seek_ms=")) {
                continue;
            }
            part = StringUtils.removeStart(part, "seek_ms=");
            seekMs = Long.parseLong(part);
        }

        if (!super.initialize(url)) {
            return false;
        }

        final AudioFormat format = this.getAudioFormat();
        final long samplesPerS = (long) format.getSampleRate();
        final int bytesPerSample = (format.getChannels() * format.getSampleSizeInBits() / 8);

        long remainingBytes = seekMs * samplesPerS * bytesPerSample / 1000L;

        while (remainingBytes > 0) {
            final SoundBuffer buf = read();
            if (buf == null || buf.audioData == null) {
                return false;
            }
            remainingBytes -= buf.audioData.length;
        }

        synchronized (this) {
            fullyInitialized = true;
        }
        return true;
    }

    @Override
    public synchronized boolean initialized() {
        return fullyInitialized;
    }
}
