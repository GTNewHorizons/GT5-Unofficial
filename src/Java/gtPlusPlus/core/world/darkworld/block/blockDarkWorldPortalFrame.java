package gtPlusPlus.core.world.darkworld.block;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class blockDarkWorldPortalFrame extends Block implements ITileTooltip{

	public blockDarkWorldPortalFrame() {
		super(Material.iron);
		this.setCreativeTab(AddToCreativeTab.tabBOP);
		this.setBlockName("blockDarkWorldPortalFrame");
		this.setHardness(3.0F);
		this.setLightLevel(0.5F);
		this.setBlockTextureName(CORE.MODID + ":" + "metro/TEXTURE_TECH_PANEL_A");
		LanguageRegistry.addName(this, "Containment Frame");

	}

	@Override
	public int getTooltipID() {
		return 0;
	}

}
