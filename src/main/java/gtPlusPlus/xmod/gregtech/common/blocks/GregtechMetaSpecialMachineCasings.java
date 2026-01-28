package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.common.blocks.MaterialCasings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaSpecialMachineCasings extends GregtechMetaCasingBlocksAbstract {

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i < 4; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }

    public static class SpecialCasingItemBlock extends GregtechMetaCasingItems {

        public SpecialCasingItemBlock(Block par1) {
            super(par1);
        }
    }

    public GregtechMetaSpecialMachineCasings() {
        super(SpecialCasingItemBlock.class, "gtplusplus.blockspecialcasings.2", MaterialCasings.INSTANCE);
        TAE.registerTexture(84, TextureFactory.of(this, 2));
        TAE.registerTexture(100, TextureFactory.of(this, 3));

        GregtechItemList.Casing_Machine_Custom_1.set(new ItemStack(this, 1, 0));
        GregtechItemList.Casing_Machine_Custom_2.set(new ItemStack(this, 1, 1));
        GregtechItemList.Casing_Machine_Custom_3.set(new ItemStack(this, 1, 2));
        GregtechItemList.Casing_Machine_Custom_4.set(new ItemStack(this, 1, 3));
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_FROST_PROOF.getIcon();
            case 2 -> TexturesGtBlock.Casing_Material_Laurenium.getIcon();
            case 3 -> TexturesGtBlock.Casing_Material_Botmium.getIcon();
            default -> Textures.BlockIcons.RENDERING_ERROR.getIcon();
        };
    }
}
