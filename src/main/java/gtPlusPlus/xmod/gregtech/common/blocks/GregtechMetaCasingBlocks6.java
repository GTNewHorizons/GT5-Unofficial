package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.common.blocks.MaterialCasings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaCasingBlocks6 extends GregtechMetaCasingBlocksAbstract {

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i < 2; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }

    public static class GregtechMetaCasingItemBlocks3 extends GregtechMetaCasingItems {

        public GregtechMetaCasingItemBlocks3(Block par1) {
            super(par1);
        }

        @Override
        public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
            super.addInformation(aStack, aPlayer, aList, aF3_H);
        }
    }

    public GregtechMetaCasingBlocks6() {
        super(GregtechMetaCasingItemBlocks3.class, "gtplusplus.blockcasings.6", MaterialCasings.INSTANCE);
        TAE.registerTexture(3, 4, TextureFactory.of(this, 0));
        TAE.registerTexture(3, 5, TextureFactory.of(this, 1));
        GregtechItemList.Casing_Fusion_External2.set(new ItemStack(this, 1, 0));
        GregtechItemList.Casing_Fusion_Internal2.set(new ItemStack(this, 1, 1));
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            return switch (aMeta) {
                case 0 -> TexturesGtBlock.TEXTURE_CASING_FUSION_4.getIcon();
                case 1 -> TexturesGtBlock.TEXTURE_CASING_FUSION_COIL_4.getIcon();
                default -> Textures.GlobalIcons.RENDERING_ERROR.getIcon();
            };
        }
        return Textures.GlobalIcons.RENDERING_ERROR.getIcon();
    }
}
