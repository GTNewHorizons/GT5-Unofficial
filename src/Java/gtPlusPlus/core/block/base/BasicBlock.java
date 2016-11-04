package gtPlusPlus.core.block.base;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BasicBlock extends Block {

	public static enum BlockTypes {
		STANDARD("blockBlock", "pickaxe", Block.soundTypeStone), FRAME("blockFrameGt", "wrench", Block.soundTypeMetal);

		private String		TEXTURE_NAME;
		private String		HARVEST_TOOL;
		private SoundType	soundOfBlock;

		private BlockTypes(final String textureName, final String harvestTool, final SoundType blockSound) {
			this.TEXTURE_NAME = textureName;
			this.HARVEST_TOOL = harvestTool;
			this.soundOfBlock = blockSound;
		}

		public SoundType getBlockSoundType() {
			return this.soundOfBlock;
		}

		public String getHarvestTool() {
			return this.HARVEST_TOOL;
		}

		public String getTexture() {
			return this.TEXTURE_NAME;
		}

	}

	public BasicBlock(final String unlocalizedName, final Material material) {
		super(material);
		this.setBlockName(Utils.sanitizeString(unlocalizedName));
		this.setBlockTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabBlock);
		this.setHardness(2.0F);
		this.setResistance(6.0F);
		this.setLightLevel(0.0F);
		this.setHarvestLevel("pickaxe", 2);
		this.setStepSound(Block.soundTypeMetal);
	}

}
