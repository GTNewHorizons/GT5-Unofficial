package gtPlusPlus.core.block.base;

import gtPlusPlus.core.lib.CORE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AdvancedBlock extends Block {

	protected AdvancedBlock(final String unlocalizedName, final Material material, final CreativeTabs x,
			final float blockHardness, final float blockResistance, final float blockLightLevel,
			final String blockHarvestTool, final int blockHarvestLevel, final SoundType BlockSound) {
		super(material);
		this.setBlockName(unlocalizedName);
		this.setBlockTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setCreativeTab(x);
		this.setHardness(blockHardness); // block Hardness
		this.setResistance(blockResistance);
		this.setLightLevel(blockLightLevel);
		this.setHarvestLevel(blockHarvestTool, blockHarvestLevel);
		this.setStepSound(BlockSound);
	}

	@Override
	public boolean onBlockActivated(final World p_149727_1_, final int p_149727_2_, final int p_149727_3_,
			final int p_149727_4_, final EntityPlayer p_149727_5_, final int p_149727_6_, final float p_149727_7_,
			final float p_149727_8_, final float p_149727_9_) {
		return false;
	}

}
