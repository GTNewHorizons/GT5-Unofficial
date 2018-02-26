package gtPlusPlus.api.interfaces;

import net.minecraft.block.Block;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;

public interface ITexturedBlock extends ITexturedTileEntity{

	ITexture[] getTexture(byte side);
	
	ITexture[] getTexture(Block block, byte side);

}
