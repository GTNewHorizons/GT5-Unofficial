package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.common.blocks.MaterialCasings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTieredCasingBlocks1 extends GregtechMetaCasingBlocksAbstract {

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i < 10; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }

    public static class TieredCasingItemBlock extends GregtechMetaCasingItems {

        public TieredCasingItemBlock(Block par1) {
            super(par1);
        }

        @Override
        public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
            int aMeta = aStack.getItemDamage();
            if (aMeta < 10) {
                aList.add(StatCollector.translateToLocalFormatted("GT5U.tooltip.electric.tier.s", GTValues.VN[aMeta]));
            }
            super.addInformation(aStack, aPlayer, aList, aF3_H);
        }
    }

    public GregtechMetaTieredCasingBlocks1() {
        super(TieredCasingItemBlock.class, "gtplusplus.blocktieredcasings.1", MaterialCasings.INSTANCE);

        GregtechItemList.GTPP_Casing_ULV.set(new ItemStack(this, 1, 0));
        GregtechItemList.GTPP_Casing_LV.set(new ItemStack(this, 1, 1));
        GregtechItemList.GTPP_Casing_MV.set(new ItemStack(this, 1, 2));
        GregtechItemList.GTPP_Casing_HV.set(new ItemStack(this, 1, 3));
        GregtechItemList.GTPP_Casing_EV.set(new ItemStack(this, 1, 4));
        GregtechItemList.GTPP_Casing_IV.set(new ItemStack(this, 1, 5));
        GregtechItemList.GTPP_Casing_LuV.set(new ItemStack(this, 1, 6));
        GregtechItemList.GTPP_Casing_ZPM.set(new ItemStack(this, 1, 7));
        GregtechItemList.GTPP_Casing_UV.set(new ItemStack(this, 1, 8));
        GregtechItemList.GTPP_Casing_UHV.set(new ItemStack(this, 1, 9));
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if (aMeta < 10) {
            return TexturesGtBlock.TIERED_MACHINE_HULLS[aMeta].getIcon();
        }
        return Textures.BlockIcons.RENDERING_ERROR.getIcon();
    }
}
