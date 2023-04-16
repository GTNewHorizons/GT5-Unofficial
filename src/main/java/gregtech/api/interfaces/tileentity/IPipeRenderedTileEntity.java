package gregtech.api.interfaces.tileentity;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;

public interface IPipeRenderedTileEntity extends ICoverable, ITexturedTileEntity {

    float getThickNess();

    byte getConnections();

    ITexture[] getTextureUncovered(ForgeDirection aSide);

    default ITexture[] getTextureCovered(ForgeDirection aSide) {
        return getTextureUncovered(aSide);
    }
}
