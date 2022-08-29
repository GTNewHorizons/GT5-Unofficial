package gtPlusPlus.australia.block;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;
import net.minecraft.block.BlockSand;

public class BlockAustraliaSand extends BlockSand implements ITileTooltip {

    public BlockAustraliaSand() {
        this.setCreativeTab(AddToCreativeTab.tabBlock);
        this.setBlockName("blockAustralianSand");
        this.setHardness(0.1F);
        this.setBlockTextureName("minecraft" + ":" + "sand");
        LanguageRegistry.addName(this, "Sandy Earth");
    }

    @Override
    public int getTooltipID() {
        return 2;
    }
}
