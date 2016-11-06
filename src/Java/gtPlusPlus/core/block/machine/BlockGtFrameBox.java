package gtPlusPlus.core.block.machine;

import gtPlusPlus.core.block.base.MetaBlock;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockGtFrameBox extends MetaBlock {

	private int[] colours;
	private int totalColours;
	
	public BlockGtFrameBox(
			String unlocalizedName, Material material,
			BlockTypes blockTypeENUM, boolean recolour, int... colour) {
		super(unlocalizedName, material, blockTypeENUM.getBlockSoundType());
        this.setBlockTextureName(CORE.MODID + ":" + "blockGtFrame");
        this.setHarvestLevel(blockTypeENUM.getHarvestTool(), 2);
        if (recolour && (colour != null && colour.length > 0)){
        	colours = colour;
        	totalColours = colours.length;
        }
	}

	@Override
	public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_,
			int p_149720_3_, int p_149720_4_) {
		for (int i : colours){
			
		}		
		return super.colorMultiplier(p_149720_1_, p_149720_2_, p_149720_3_, p_149720_4_);
	}

}
