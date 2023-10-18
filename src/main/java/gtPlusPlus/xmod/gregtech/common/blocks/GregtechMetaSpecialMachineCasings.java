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
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.objects.GTPP_CopiedBlockTexture;
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

        @Override
        public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
            int aMeta = aStack.getItemDamage();
            if (aMeta < 10) {
                // aList.add("Tier: "+GT_Values.VN[aMeta]);
            }
            super.addInformation(aStack, aPlayer, aList, aF3_H);
        }
    }

    public GregtechMetaSpecialMachineCasings() {
        super(SpecialCasingItemBlock.class, "gtplusplus.blockspecialcasings.2", GT_Material_Casings.INSTANCE);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            // TAE.registerTextures(new GT_CopiedBlockTexture(this, 6, i));
            // Don't register these Textures, They already exist within vanilla GT. (May not exist in 5.08)
        }
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Strong Bronze Machine Casing");
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".1.name", "Sturdy Aluminium Machine Casing");
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".2.name", "Vigorous Laurenium Machine Casing");
        TAE.registerTexture(84, new GTPP_CopiedBlockTexture(this, 6, 2));
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".3.name", "Rugged Botmium Machine Casing");

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
            case 3 -> Textures.BlockIcons.MACHINE_HEATPROOFCASING.getIcon();
            default -> Textures.BlockIcons.RENDERING_ERROR.getIcon();
        };
    }
}
