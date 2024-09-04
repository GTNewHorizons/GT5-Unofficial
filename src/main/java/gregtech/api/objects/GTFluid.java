package gregtech.api.objects;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraftforge.fluids.Fluid;

import gregtech.api.GregTechAPI;
import gregtech.api.fluid.GTFluidFactory;

/**
 * @deprecated use {@link GTFluidFactory#builder}
 */
@Deprecated
public class GTFluid extends Fluid implements Runnable {

    public final String mTextureName;
    private final short[] mRGBa;

    public GTFluid(String aName, String aTextureName, short[] aRGBa) {
        super(aName);
        mRGBa = aRGBa;
        mTextureName = aTextureName;
        GregTechAPI.sGTBlockIconload.add(this);
    }

    @Override
    public int getColor() {
        return (Math.max(0, Math.min(255, mRGBa[0])) << 16) | (Math.max(0, Math.min(255, mRGBa[1])) << 8)
            | Math.max(0, Math.min(255, mRGBa[2]));
    }

    @Override
    public void run() {
        setIcons(GregTechAPI.sBlockIcons.registerIcon(GregTech.getResourcePath("fluids", "fluid." + mTextureName)));
    }
}
