package gtPlusPlus.australia.block;

import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.block.BlockGrass;

import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class BlockAustraliaSand extends BlockGrass implements ITileTooltip{

	public BlockAustraliaSand() {
		this.setCreativeTab(AddToCreativeTab.tabBlock);
		this.setBlockName("blockAustralianSand");
		this.setHardness(0.1F);
		this.setBlockTextureName("minecraft" + ":" + "grass");
		LanguageRegistry.addName(this, "Sandy Soil");
	}

	@Override
	public int getTooltipID() {
		return 2;
	}

}
