package toxiceverglades.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import gregtech.api.enums.Textures;
import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class BlockDarkWorldPortalFrame extends Block implements ITileTooltip {

    public BlockDarkWorldPortalFrame() {
        super(Material.iron);
        this.setCreativeTab(AddToCreativeTab.tabBOP);
        this.setBlockName("blockDarkWorldPortalFrame");
        this.setHardness(3.0F);
        this.setLightLevel(0.5F);
        this.setBlockTextureName(
            Textures.BlockIcons.CONTAINMENT_FRAME.getTextureFile()
                .toString());
    }

    @Override
    public int getTooltipID() {
        return 0;
    }
}
