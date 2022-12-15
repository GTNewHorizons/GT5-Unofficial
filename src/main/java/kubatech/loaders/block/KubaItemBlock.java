package kubatech.loaders.block;

import java.util.List;
import kubatech.kubatech;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class KubaItemBlock extends ItemBlock {
    public KubaItemBlock(Block p_i45328_1_) {
        super(p_i45328_1_);
        setHasSubtypes(true);
    }

    @Override
    public void registerIcons(IIconRegister p_94581_1_) {
        super.registerIcons(p_94581_1_);
    }

    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_) {
        return KubaBlock.blocks.get(p_77667_1_.getItemDamage()).getUnlocalizedName();
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return KubaBlock.blocks.get(p_77653_1_.getItemDamage()).getDisplayName(p_77653_1_);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        KubaBlock.blocks.get(p_77624_1_.getItemDamage()).addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
    }
}
