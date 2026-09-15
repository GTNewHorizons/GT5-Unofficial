package gregtech.api.interfaces.metatileentity;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IMetaTileEntityPipe extends IMetaTileEntity {

    /**
     * Icon of the Texture. If this returns null then it falls back to getTextureIndex.
     *
     * @param side          is the Side of the Block
     * @param facingBitMask is the Bitmask of all Connections
     * @param colorIndex    The Minecraft Color the Block is having
     * @param active        if the Machine is currently active (use this instead of calling
     *                      mBaseMetaTileEntity.mActive!!!). Note: In case of Pipes this means if this Side is connected
     *                      to something or not.
     * @param redstoneLevel if the Machine is currently outputting a RedstoneSignal (use this instead of calling
     *                      mBaseMetaTileEntity.mRedstone!!!)
     */
    ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, int facingBitMask,
        int colorIndex, boolean active, boolean redstoneLevel);
}
