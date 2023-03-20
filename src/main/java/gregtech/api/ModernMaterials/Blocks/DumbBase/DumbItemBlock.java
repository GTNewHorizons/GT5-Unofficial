package gregtech.api.ModernMaterials.Blocks.DumbBase;

import gregtech.api.GregTech_API;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class DumbItemBlock extends ItemBlock {

    public DumbItemBlock(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTech_API.TAB_GREGTECH); // todo add new gt frame tab.
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }
}
