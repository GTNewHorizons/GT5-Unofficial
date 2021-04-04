package gregtech.api.interfaces.tileentity;

import gregtech.api.interfaces.ITexture;

public interface IPipeRenderedTileEntity extends ICoverable, ITexturedTileEntity {
    float getThickNess();

    byte getConnections();

    ITexture[] getTextureUncovered(byte aSide);

    default ITexture[] getTextureCovered(byte aSide) {
        return getTextureUncovered(aSide);
    }
}