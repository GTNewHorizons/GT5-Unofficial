package gtPlusPlus.core.world.darkworld.block;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class blockDarkWorldPortalFrame extends Block{

	public blockDarkWorldPortalFrame() {
		super(Material.iron);
		this.setCreativeTab(AddToCreativeTab.tabBOP);
		this.setBlockName("blockDarkWorldPortalFrame");
		this.setHardness(3.0F);
		this.setLightLevel(0.5F);
		this.setBlockTextureName(CORE.MODID + ":" + "SwirlRed");
		LanguageRegistry.addName(this, "Mystical Frame");

	}

}
