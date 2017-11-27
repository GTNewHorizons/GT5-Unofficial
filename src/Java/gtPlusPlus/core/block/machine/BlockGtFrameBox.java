package gtPlusPlus.core.block.machine;

import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.MetaBlock;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.IBlockAccess;

public class BlockGtFrameBox extends MetaBlock {

	private int[] colours;
	private int totalColours;

	public BlockGtFrameBox(
			final String unlocalizedName, final Material material,
			final BlockTypes blockTypeENUM, final boolean recolour, final int... colour) {
		super(unlocalizedName, material, blockTypeENUM.getBlockSoundType());
		this.setBlockTextureName(CORE.MODID + ":" + "blockGtFrame");
		this.setHarvestLevel(blockTypeENUM.getHarvestTool(), 2);
		if (recolour && ((colour != null) && (colour.length > 0))){
			this.colours = colour;
			this.totalColours = this.colours.length;
		}
	}

	@Override
	public int colorMultiplier(final IBlockAccess p_149720_1_, final int p_149720_2_,
			final int p_149720_3_, final int p_149720_4_) {
		for (final int i : this.colours){

		}
		return super.colorMultiplier(p_149720_1_, p_149720_2_, p_149720_3_, p_149720_4_);
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
		return false;
	}

}
