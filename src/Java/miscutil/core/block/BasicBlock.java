package miscutil.core.block;

import miscutil.core.creativetabs.AddToCreativeTab;
import miscutil.core.lib.CORE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BasicBlock extends Block {

    protected BasicBlock(String unlocalizedName, Material material) {
        super(material);
        this.setBlockName(unlocalizedName);
        this.setBlockTextureName(CORE.MODID + ":" + unlocalizedName);
        this.setCreativeTab(AddToCreativeTab.tabBlock);
        this.setHardness(2.0F);
        this.setResistance(6.0F);
        this.setLightLevel(0.0F);
        this.setHarvestLevel("pickaxe", 2);
        this.setStepSound(soundTypeMetal);
    }

}
