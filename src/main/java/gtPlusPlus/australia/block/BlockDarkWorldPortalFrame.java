package gtPlusPlus.australia.block;

import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;

public class BlockDarkWorldPortalFrame extends Block implements ITileTooltip{

	public BlockDarkWorldPortalFrame() {
		super(Material.iron);
		this.setCreativeTab(AddToCreativeTab.tabBlock);
		this.setBlockName("blockAustraliaPortalFrame");
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
