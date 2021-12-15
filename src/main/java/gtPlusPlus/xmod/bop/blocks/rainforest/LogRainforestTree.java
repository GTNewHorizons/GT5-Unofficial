package gtPlusPlus.xmod.bop.blocks.rainforest;

import gtPlusPlus.xmod.bop.blocks.base.LogBase;

public class LogRainforestTree extends LogBase {

	public LogRainforestTree(){
	super("Rainforest Oak", "rainforestoak", new String[]{"rainforest"});
	this.treeType = new String[] {"rainforest"};
	}

	/*@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getSideIcon(int metaID){
		return this.textureSide[metaID % this.textureSide.length];
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getTopIcon(int metaID){
		return this.textureTop[metaID % this.textureTop.length];
	}*/
	
}