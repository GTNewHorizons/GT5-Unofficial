package gtPlusPlus.xmod.gregtech.common.helpers;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;

public class GT_MethodHelper {

    public static ITexture[] getTexture(TileEntity tTileEntity, Block aBlock, byte aSide) {
        if (tTileEntity instanceof ITexturedTileEntity) {
            return ((ITexturedTileEntity) tTileEntity).getTexture(aBlock, aSide);
        }
        return BlockIcons.ERROR_RENDERING;
    }
}
