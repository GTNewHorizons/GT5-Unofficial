package gtPlusPlus.everglades.block;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class BlockDarkWorldPortalFrame extends Block implements ITileTooltip {

    public BlockDarkWorldPortalFrame() {
        super(Material.iron);
        this.setCreativeTab(AddToCreativeTab.tabBOP);
        this.setBlockName("blockDarkWorldPortalFrame");
        this.setHardness(3.0F);
        this.setLightLevel(0.5F);
        this.setBlockTextureName(GTPlusPlus.ID + ":" + "metro/TEXTURE_TECH_PANEL_A");
    }

    @Override
    public int getTooltipID() {
        return 0;
    }
}
