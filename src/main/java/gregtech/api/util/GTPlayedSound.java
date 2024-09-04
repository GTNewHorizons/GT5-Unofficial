package gregtech.api.util;

import net.minecraft.util.ResourceLocation;

public class GTPlayedSound {

    public final String mSoundName;
    public final int mX, mY, mZ;

    public GTPlayedSound(ResourceLocation aSoundResourceLocation, double aX, double aY, double aZ) {
        mSoundName = aSoundResourceLocation.toString();
        mX = (int) aX;
        mY = (int) aY;
        mZ = (int) aZ;
    }

    @Override
    public boolean equals(Object aObject) {
        if (aObject instanceof GTPlayedSound) {
            return ((GTPlayedSound) aObject).mX == mX && ((GTPlayedSound) aObject).mY == mY
                && ((GTPlayedSound) aObject).mZ == mZ
                && ((GTPlayedSound) aObject).mSoundName.equals(mSoundName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return mX + mY + mZ + mSoundName.hashCode();
    }
}
