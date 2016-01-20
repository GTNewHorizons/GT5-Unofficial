package miscutil.core.block;

import miscutil.core.creativetabs.TMCreativeTabs;
import miscutil.core.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BasicBlock extends Block {

    protected BasicBlock(String unlocalizedName, Material material) {
        super(material);
        this.setBlockName(unlocalizedName);
        this.setBlockTextureName(Strings.MODID + ":" + unlocalizedName);
        this.setCreativeTab(TMCreativeTabs.tabBlock);
        this.setHardness(2.0F);
        this.setResistance(6.0F);
        this.setLightLevel(0.0F);
        this.setHarvestLevel("pickaxe", 2);
        this.setStepSound(soundTypeMetal);
    }

}
