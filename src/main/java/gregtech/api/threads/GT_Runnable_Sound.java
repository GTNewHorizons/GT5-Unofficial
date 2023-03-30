package gregtech.api.threads;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import gregtech.api.util.GT_PlayedSound;
import gregtech.api.util.GT_Utility;

public class GT_Runnable_Sound implements Runnable {

    private final double mX, mY, mZ;
    private final int mTimeUntilNextSound;
    private final World mWorld;
    private final ResourceLocation mSoundResourceLocation;
    private final float mSoundStrength, mSoundModulation;

    public GT_Runnable_Sound(World aWorld, double aX, double aY, double aZ, int aTimeUntilNextSound,
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

    /**
     * @deprecated Use {@link #GT_Runnable_Sound(World, double, double, double, int, ResourceLocation, float, float)}
     */
    public GT_Runnable_Sound(World aWorld, int aX, int aY, int aZ, int aTimeUntilNextSound,
            ResourceLocation aSoundResourceLocation, float aSoundStrength, float aSoundModulation) {
        this(
                aWorld,
                (double) aX + 0.5D,
                (double) aY + 0.5D,
                (double) aZ + 0.5D,
                aTimeUntilNextSound,
                aSoundResourceLocation,
                aSoundStrength,
                aSoundModulation);
    }

    /**
     * @deprecated Use {@link #GT_Runnable_Sound(World, double, double, double, int, ResourceLocation, float, float)}
     */
    @Deprecated
    public GT_Runnable_Sound(World aWorld, int aX, int aY, int aZ, int aTimeUntilNextSound, String aSoundName,
            float aSoundStrength, float aSoundModulation) {
        this(
                aWorld,
                (double) aX + 0.5D,
                (double) aY + 0.5D,
                (double) aZ + 0.5D,
                aTimeUntilNextSound,
                new ResourceLocation(aSoundName),
                aSoundStrength,
                aSoundModulation);
    }

    @Override
    public void run() {
        try {
            GT_PlayedSound tSound;
            if (GT_Utility.sPlayedSoundMap.containsKey(tSound = new GT_PlayedSound(mSoundResourceLocation, mX, mY, mZ)))
                return;
            mWorld.playSound(mX, mY, mZ, mSoundResourceLocation.toString(), mSoundStrength, mSoundModulation, false);
            GT_Utility.sPlayedSoundMap.put(tSound, mTimeUntilNextSound);
        } catch (Throwable e) {
            /**/
        }
    }
}
