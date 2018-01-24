package gtPlusPlus.api.interfaces;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import net.minecraft.block.Block;

public interface ITexturedBlock extends ITexturedTileEntity{

	ITexture[] getTexture(byte side);
	
	ITexture[] getTexture(Block block, byte side);

}
