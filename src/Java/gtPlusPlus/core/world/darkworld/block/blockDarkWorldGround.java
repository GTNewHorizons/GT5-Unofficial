package gtPlusPlus.core.world.darkworld.block;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gtPlusPlus.core.creative.AddToCreativeTab;
import net.minecraft.block.BlockGrass;

public class blockDarkWorldGround extends BlockGrass {

	public blockDarkWorldGround() {
		this.setCreativeTab(AddToCreativeTab.tabBOP);
		this.setBlockName("blockDarkWorldGround");
		this.setHardness(1.0F);
		this.setBlockTextureName("minecraft" + ":" + "grass");
		LanguageRegistry.addName(this, "Unstable Earth");
	}

}
