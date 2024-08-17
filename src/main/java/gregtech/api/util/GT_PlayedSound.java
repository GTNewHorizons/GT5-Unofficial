package gregtech.api.util;

import net.minecraft.util.ResourceLocation;

public class GT_PlayedSound {

    public final String mSoundName;
    public final int mX, mY, mZ;

    public GT_PlayedSound(ResourceLocation aSoundResourceLocation, double aX, double aY, double aZ) {
        mSoundName = aSoundResourceLocation.toString();
        mX = (int) aX;
        mY = (int) aY;
        mZ = (int) aZ;
    }

    @Override
    public boolean equals(Object aObject) {
        if (aObject instanceof GT_PlayedSound) {
            return ((GT_PlayedSound) aObject).mX == mX && ((GT_PlayedSound) aObject).mY == mY
                && ((GT_PlayedSound) aObject).mZ == mZ
                && ((GT_PlayedSound) aObject).mSoundName.equals(mSoundName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return mX + mY + mZ + mSoundName.hashCode();
    }
}
