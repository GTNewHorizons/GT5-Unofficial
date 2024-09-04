package gregtech.api.threads;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import gregtech.api.util.GTPlayedSound;
import gregtech.api.util.GTUtility;

public class RunnableSound implements Runnable {

    private final double mX, mY, mZ;
    private final int mTimeUntilNextSound;
    private final World mWorld;
    private final ResourceLocation mSoundResourceLocation;
    private final float mSoundStrength, mSoundModulation;

    public RunnableSound(World aWorld, double aX, double aY, double aZ, int aTimeUntilNextSound,
        ResourceLocation aSoundResourceLocation, float aSoundStrength, float aSoundModulation) {
        mWorld = aWorld;
        mX = aX;
        mY = aY;
        mZ = aZ;
        mTimeUntilNextSound = aTimeUntilNextSound;
        mSoundResourceLocation = aSoundResourceLocation;
        mSoundStrength = aSoundStrength;
        mSoundModulation = aSoundModulation;
    }

    @Override
    public void run() {
        try {
            GTPlayedSound tSound;
            if (GTUtility.sPlayedSoundMap.containsKey(tSound = new GTPlayedSound(mSoundResourceLocation, mX, mY, mZ)))
                return;
            mWorld.playSound(mX, mY, mZ, mSoundResourceLocation.toString(), mSoundStrength, mSoundModulation, false);
            GTUtility.sPlayedSoundMap.put(tSound, mTimeUntilNextSound);
        } catch (Throwable e) {
            /**/
        }
    }
}
