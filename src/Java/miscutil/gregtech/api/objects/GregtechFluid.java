package miscutil.gregtech.api.objects;

import gregtech.api.GregTech_API;
import miscutil.core.lib.CORE;
import net.minecraftforge.fluids.Fluid;

public class GregtechFluid extends Fluid implements Runnable {
    public final String mTextureName;
    private final short[] mRGBa;

    public GregtechFluid(String aName, String aTextureName, short[] aRGBa) {
        super(aName);
        mRGBa = aRGBa;
        mTextureName = aTextureName;
        GregTech_API.sGTBlockIconload.add(this);
    }

    @Override
    public int getColor() {
        return (Math.max(0, Math.min(255, mRGBa[0])) << 16) | (Math.max(0, Math.min(255, mRGBa[1])) << 8) | Math.max(0, Math.min(255, mRGBa[2]));
    }

    @Override
    public void run() {
        setIcons(GregTech_API.sBlockIcons.registerIcon(CORE.MODID + "fluids/fluid." + mTextureName));
    }
}