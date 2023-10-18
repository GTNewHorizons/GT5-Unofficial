package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaSpecialMultiCasings2 extends GregtechMetaCasingBlocksAbstract {

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i < 8; i++) {
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
            if (aMeta < 4) {
                aList.add("Provides quantum stability");
            }
            if (aMeta >= 4 && aMeta < 8) {
                aList.add("Provides quantum modulation");
            }
            super.addInformation(aStack, aPlayer, aList, aF3_H);
        }
    }

    public GregtechMetaSpecialMultiCasings2() {
        super(SpecialCasingItemBlock.class, "gtplusplus.blockspecialcasings.3", GT_Material_Casings.INSTANCE);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            // TAE.registerTextures(new GT_CopiedBlockTexture(this, 6, i));
            // Don't register these Textures, They already exist within vanilla GT. (May not exist in 5.08)
        }
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Resonance Chamber I");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Resonance Chamber II");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "Resonance Chamber III");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Resonance Chamber IV");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "Modulator I");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Modulator II");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Modulator III");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "Modulator IV");

        GregtechItemList.ResonanceChamber_I.set(new ItemStack(this, 1, 0));
        GregtechItemList.ResonanceChamber_II.set(new ItemStack(this, 1, 1));
        GregtechItemList.ResonanceChamber_III.set(new ItemStack(this, 1, 2));
        GregtechItemList.ResonanceChamber_IV.set(new ItemStack(this, 1, 3));
        GregtechItemList.Modulator_I.set(new ItemStack(this, 1, 4));
        GregtechItemList.Modulator_II.set(new ItemStack(this, 1, 5));
        GregtechItemList.Modulator_III.set(new ItemStack(this, 1, 6));
        GregtechItemList.Modulator_IV.set(new ItemStack(this, 1, 7));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
            final int ordinalSide) {
        return getStaticIcon((byte) ordinalSide, (byte) aWorld.getBlockMetadata(xCoord, yCoord, zCoord));
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int aMeta) {
        return getStaticIcon((byte) ordinalSide, (byte) aMeta);
    }

    public static IIcon getStaticIcon(final byte aSide, final byte aMeta) {
        return switch (aMeta) {
            case 0 -> TexturesGtBlock.Casing_Resonance_1.getIcon();
            case 1 -> TexturesGtBlock.Casing_Resonance_2.getIcon();
            case 2 -> TexturesGtBlock.Casing_Resonance_3.getIcon();
            case 3 -> TexturesGtBlock.Casing_Resonance_4.getIcon();
            case 4 -> TexturesGtBlock.Casing_Modulator_1.getIcon();
            case 5 -> TexturesGtBlock.Casing_Modulator_2.getIcon();
            case 6 -> TexturesGtBlock.Casing_Modulator_3.getIcon();
            case 7 -> TexturesGtBlock.Casing_Modulator_4.getIcon();
            default -> Textures.BlockIcons.RENDERING_ERROR.getIcon();
        };
    }
}
