package gregtech.common.tileentities.machines;

import gregtech.api.interfaces.ITexture;

@Deprecated
public class MTEHatchOutputBusME extends gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME {

    public MTEHatchOutputBusME(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEHatchOutputBusME(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }
}
