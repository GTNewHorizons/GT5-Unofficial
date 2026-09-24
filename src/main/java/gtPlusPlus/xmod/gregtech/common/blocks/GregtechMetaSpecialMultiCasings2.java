package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.common.blocks.MaterialCasings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class GregtechMetaSpecialMultiCasings2 extends GregtechMetaCasingBlocksAbstract {

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < 8; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    public static class SpecialCasingItemBlock extends GregtechMetaCasingItems {

        public SpecialCasingItemBlock(Block par1) {
            super(par1);
        }

        @Override
        public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean aF3_H) {
            int aMeta = stack.getItemDamage();
            if (aMeta < 4) {
                tooltip.add(StatCollector.translateToLocal("GTPP.tooltip.meta_special.quantum_stability"));
            }
            if (aMeta >= 4 && aMeta < 8) {
                tooltip.add(StatCollector.translateToLocal("GTPP.tooltip.meta_special.quantum_modulation"));
            }
            super.addInformation(stack, player, tooltip, aF3_H);
        }
    }

    public GregtechMetaSpecialMultiCasings2() {
        super(SpecialCasingItemBlock.class, "gtplusplus.blockspecialcasings.3", MaterialCasings.INSTANCE);

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
        return getStaticIcon((byte) ordinalSide, aWorld.getBlockMetadata(xCoord, yCoord, zCoord));
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int aMeta) {
        return getStaticIcon((byte) ordinalSide, aMeta);
    }

    public static IIcon getStaticIcon(final byte aSide, final int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.Casing_Resonance_1.getIcon();
            case 1 -> Textures.BlockIcons.Casing_Resonance_2.getIcon();
            case 2 -> Textures.BlockIcons.Casing_Resonance_3.getIcon();
            case 3 -> Textures.BlockIcons.Casing_Resonance_4.getIcon();
            case 4 -> Textures.BlockIcons.Casing_Modulator_1.getIcon();
            case 5 -> Textures.BlockIcons.Casing_Modulator_2.getIcon();
            case 6 -> Textures.BlockIcons.Casing_Modulator_3.getIcon();
            case 7 -> Textures.BlockIcons.Casing_Modulator_4.getIcon();
            default -> Textures.GlobalIcons.RENDERING_ERROR.getIcon();
        };
    }
}
