package miscutil.core.block.base;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class MultiTextureBlock extends Block { 

	public IIcon[] icons = new IIcon[6];
	
    protected MultiTextureBlock(String unlocalizedName, Material material, SoundType blockSound) 
    { 
        super(material);
        this.setBlockName(unlocalizedName);
        this.setBlockTextureName(CORE.MODID + ":" + unlocalizedName);
        this.setCreativeTab(AddToCreativeTab.tabBlock);
        this.setHardness(2.0F);
        this.setResistance(6.0F);
        this.setStepSound(blockSound);
    }
    
    @Override
    public void registerBlockIcons(IIconRegister reg) {
        for (int i = 0; i < 6; i ++) {
            this.icons[i] = reg.registerIcon(this.textureName + "_" + i);
        }
    }
    
    @Override
    public IIcon getIcon(int side, int meta) {
        return this.icons[side];
    }
}