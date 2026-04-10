package gtPlusPlus.xmod.gregtech.common.blocks;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.common.blocks.MaterialCasings;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.storage.MTEPowerSubStation;

public class GregtechMetaCasingBlocks3 extends GregtechMetaCasingBlocksAbstract {

    public static class GregtechMetaCasingItemBlocks3 extends GregtechMetaCasingItems {

        public GregtechMetaCasingItemBlocks3(Block par1) {
            super(par1);
        }

        @Override
        public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
            int meta = aStack.getItemDamage();
            int tier = MTEPowerSubStation.getCellTier(field_150939_a, meta);
            if (tier > 0) {
                long capacity = MTEPowerSubStation.getCapacityFromCellTier(tier);
                aList.add(
                    StatCollector
                        .translateToLocalFormatted("gtpp.tooltip.meta_casing.energy_storage", formatNumber(capacity)));
            }
            super.addInformation(aStack, aPlayer, aList, aF3_H);
        }
    }

    public GregtechMetaCasingBlocks3() {
        super(GregtechMetaCasingItemBlocks3.class, "gtplusplus.blockcasings.3", MaterialCasings.INSTANCE);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            // Free up Redox casing in TAE
            if (i >= 4 && i <= 8) {
                continue;
            }
            TAE.registerTexture(2, i, TextureFactory.of(this, i));
        }

        GregtechItemList.Casing_FishPond.set(new ItemStack(this, 1, 0));
        GregtechItemList.Casing_Extruder.set(new ItemStack(this, 1, 1));
        GregtechItemList.Casing_Multi_Use.set(new ItemStack(this, 1, 2));
        GregtechItemList.Casing_BedrockMiner.set(new ItemStack(this, 1, 3));
        GregtechItemList.Casing_Vanadium_Redox_IV.set(new ItemStack(this, 1, 4));
        GregtechItemList.Casing_Vanadium_Redox_LuV.set(new ItemStack(this, 1, 5));
        GregtechItemList.Casing_Vanadium_Redox_ZPM.set(new ItemStack(this, 1, 6));
        GregtechItemList.Casing_Vanadium_Redox_UV.set(new ItemStack(this, 1, 7));
        GregtechItemList.Casing_Vanadium_Redox_MAX.set(new ItemStack(this, 1, 8));
        GregtechItemList.Casing_AmazonWarehouse.set(new ItemStack(this, 1, 9));
        GregtechItemList.Casing_AdvancedVacuum.set(new ItemStack(this, 1, 10));
        GregtechItemList.Casing_Adv_BlastFurnace.set(new ItemStack(this, 1, 11));
        GregtechItemList.Casing_Fusion_External.set(new ItemStack(this, 1, 12));
        GregtechItemList.Casing_Fusion_Internal.set(new ItemStack(this, 1, 13));
        GregtechItemList.Casing_Containment.set(new ItemStack(this, 1, 15));

        for (int i = 4; i < 9; i++) {
            GTStructureChannels.PSS_CELL.registerAsIndicator(new ItemStack(this, 1, i), i - 2);
        }
    }

    // exclude meta 14 to not create "Unnamed" casing
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 16; i++) {
            if (i == 14) continue;
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            return switch (aMeta) {
                case 0 ->
                    // Aquatic Casing
                    Textures.BlockIcons.AQUATIC_CASING.getIcon();
                case 1 ->
                    // Inconel Reinforced Casing
                    Textures.BlockIcons.INCONEL_REINFORCED_CASING.getIcon();
                case 2 ->
                    // Multi-Use Casing
                    Textures.BlockIcons.MULTI_USE_CASING.getIcon();
                case 3 ->
                    // Trinium Plated Mining Platform Casing
                    TexturesGtBlock.Casing_Trinium_Naquadah_Vent.getIcon();
                case 4 ->
                    // Vanadium Redox IV
                    TexturesGtBlock.Casing_Redox_2.getIcon();
                case 5 ->
                    // Vanadium Redox LuV
                    TexturesGtBlock.Casing_Redox_3.getIcon();
                case 6 ->
                    // Vanadium Redox ZPM
                    TexturesGtBlock.Casing_Redox_4.getIcon();
                case 7 ->
                    // Vanadium Redox UV
                    TexturesGtBlock.Casing_Redox_5.getIcon();
                case 8 ->
                    // Vanadium Redox MAX
                    TexturesGtBlock.Casing_Redox_6.getIcon();
                case 9 ->
                    // Amazon Warehouse Casing
                    TexturesGtBlock.TEXTURE_CASING_AMAZON.getIcon();
                case 10 ->
                    // Adv. Vac. Freezer
                    TexturesGtBlock.TEXTURE_CASING_ADVANCED_CRYOGENIC.getIcon();
                case 11 ->
                    // Adv. EBF
                    TexturesGtBlock.TEXTURE_CASING_ADVANCED_VOLCNUS.getIcon();
                case 12 -> TexturesGtBlock.TEXTURE_CASING_FUSION_COIL_II.getIcon();
                case 13 -> TexturesGtBlock.TEXTURE_CASING_FUSION_COIL_II_INNER.getIcon();
                case 14 -> TexturesGtBlock.TEXTURE_CASING_FUSION_CASING_ULTRA.getIcon();
                case 15 -> Textures.BlockIcons.CONTAINMENT_CASING.getIcon();
                default -> Textures.GlobalIcons.RENDERING_ERROR.getIcon();
            };
        }
        return Textures.GlobalIcons.RENDERING_ERROR.getIcon();
    }
}
