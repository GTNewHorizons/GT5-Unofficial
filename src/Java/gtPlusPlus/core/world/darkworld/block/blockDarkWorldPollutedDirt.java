package gtPlusPlus.core.world.darkworld.block;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gtPlusPlus.core.creative.AddToCreativeTab;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;

public class blockDarkWorldPollutedDirt extends BlockDirt {

	public blockDarkWorldPollutedDirt() {
		this.setCreativeTab(AddToCreativeTab.tabBOP);
		this.setBlockName("blockDarkWorldGround2");
		this.setHardness(0.5F);
		this.setBlockTextureName("minecraft" + ":" + "dirt");
		LanguageRegistry.addName(this, "Polluted Soil");
	}

}
