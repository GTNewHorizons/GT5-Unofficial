package miscutil.core.block.base;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MetaBlock extends MultiTextureBlock { 

    protected MetaBlock(String unlocalizedName, Material material, SoundType soundType) {
        super(unlocalizedName, material, soundType);
    }
    
    @Override
    public int damageDropped(int meta) {
        return meta;
    }
    
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 6; i ++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
    
}   