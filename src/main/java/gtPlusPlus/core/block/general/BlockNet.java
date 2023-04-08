package gtPlusPlus.core.block.general;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.Random;

import net.minecraft.block.BlockWeb;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.ModItems;

public class BlockNet extends BlockWeb {

    public BlockNet() {
        this.setCreativeTab(AddToCreativeTab.tabBlock);
        this.setLightOpacity(1);
        this.setHardness(4.0F);
        this.setBlockName("blockNet");
        GameRegistry.registerBlock(this, "blockNet");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iIcon) {
        this.blockIcon = iIcon.registerIcon(GTPlusPlus.ID + ":" + "net");
    }

    @Override
    public Item getItemDropped(final int p_149650_1_, final Random p_149650_2_, final int p_149650_3_) {
        return ModItems.itemRope;
    }
}
