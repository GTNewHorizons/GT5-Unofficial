package gregtech.api.interfaces.tileentity;

import gregtech.api.interfaces.ITexture;
import net.minecraft.block.Block;

public interface IPipeRenderedTileEntity extends ICoverable, ITexturedTileEntity {
    float getThickNess();

    byte getConnections();

    ITexture[] getTextureUncovered(byte aSide);

    default ITexture[] getTextureCovered(byte aSide) {
        return getTextureUncovered(aSide);
    };
}