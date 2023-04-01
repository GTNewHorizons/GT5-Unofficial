package gregtech.api.interfaces.tileentity;

import gregtech.api.enums.Dyes;
import gregtech.api.util.GT_Util;

public interface IColoredTileEntity {

    /**
     * @return 0 - 15 are Colors, while -1 means uncolored
     */
    byte getColorization();

    /**
     * Sets the Color Modulation of the Block
     *
     * @param aColor the Color you want to set it to. -1 for reset.
     */
    byte setColorization(byte aColor);

    /**
     * @return Actual color shown on GUI
     */
    default int getGUIColorization() {
        return GT_Util.getRGBInt(
                (getColorization() != -1 ? Dyes.get(getColorization()) : Dyes.MACHINE_METAL).getRGBA());
    }
}
