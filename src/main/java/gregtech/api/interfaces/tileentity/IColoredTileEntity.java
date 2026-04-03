package gregtech.api.interfaces.tileentity;

import gregtech.api.enums.Dyes;

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
        return Dyes.getOrDefault(getColorization(), Dyes.GUI_METAL)
            .toInt();
    }
}
