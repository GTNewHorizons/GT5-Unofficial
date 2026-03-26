package gtPlusPlus.api.interfaces;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;

public interface ITexturedBlock extends ITexturedTileEntity {

    @Override
    ITexture[] getTexture(ForgeDirection side);

}
