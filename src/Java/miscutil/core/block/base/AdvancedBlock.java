package miscutil.core.block.base;

import miscutil.core.lib.CORE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AdvancedBlock extends Block {

    protected AdvancedBlock(String unlocalizedName, Material material, CreativeTabs x, float blockHardness, float blockResistance, float blockLightLevel, 
    		String blockHarvestTool, int blockHarvestLevel, SoundType BlockSound) {
        super(material);
        this.setBlockName(unlocalizedName);
        this.setBlockTextureName(CORE.MODID + ":" + unlocalizedName);
        this.setCreativeTab(x);
        this.setHardness(blockHardness); //block Hardness
        this.setResistance(blockResistance);
        this.setLightLevel(blockLightLevel);
        this.setHarvestLevel(blockHarvestTool, blockHarvestLevel);
        this.setStepSound(BlockSound);
    }
    
    @Override
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        return false;
    }

}
