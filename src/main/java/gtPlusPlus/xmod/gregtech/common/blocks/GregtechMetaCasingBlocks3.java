package gtPlusPlus.xmod.gregtech.common.blocks;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
        public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean aF3_H) {
            int meta = stack.getItemDamage();
            int tier = MTEPowerSubStation.getCellTier(field_150939_a, meta);
            if (tier > 0) {
                long capacity = MTEPowerSubStation.getCapacityFromCellTier(tier);
                tooltip.add(
                    StatCollector
                        .translateToLocalFormatted("gtpp.tooltip.meta_casing.energy_storage", formatNumber(capacity)));
            }
            super.addInformation(stack, player, tooltip, aF3_H);
        }
    }

    public GregtechMetaCasingBlocks3() {
        super(GregtechMetaCasingItemBlocks3.class, "gtplusplus.blockcasings.3", MaterialCasings.INSTANCE);
        for (byte i = 0; i < 15; i = (byte) (i + 1)) {
            // Free up Redox casing in TAE
            if (i >= 3 && i <= 7) {
                continue;
            }
            TAE.registerTexture(2, i, TextureFactory.of(this, i));
        }

        GregtechItemList.Casing_FishPond.set(new ItemStack(this, 1, 0));
        GregtechItemList.Casing_Extruder.set(new ItemStack(this, 1, 1));
        GregtechItemList.Casing_Multi_Use.set(new ItemStack(this, 1, 2));
        GregtechItemList.Casing_Vanadium_Redox_IV.set(new ItemStack(this, 1, 3));
        GregtechItemList.Casing_Vanadium_Redox_LuV.set(new ItemStack(this, 1, 4));
        GregtechItemList.Casing_Vanadium_Redox_ZPM.set(new ItemStack(this, 1, 5));
        GregtechItemList.Casing_Vanadium_Redox_UV.set(new ItemStack(this, 1, 6));
        GregtechItemList.Casing_Vanadium_Redox_MAX.set(new ItemStack(this, 1, 7));
        GregtechItemList.Casing_AmazonWarehouse.set(new ItemStack(this, 1, 8));
        GregtechItemList.Casing_AdvancedVacuum.set(new ItemStack(this, 1, 9));
        GregtechItemList.Casing_Adv_BlastFurnace.set(new ItemStack(this, 1, 10));
        GregtechItemList.Casing_Fusion_External.set(new ItemStack(this, 1, 11));
        GregtechItemList.Casing_Fusion_Internal.set(new ItemStack(this, 1, 12));
        GregtechItemList.Casing_Containment.set(new ItemStack(this, 1, 14));

        for (int i = 3; i < 8; i++) {
            GTStructureChannels.PSS_CELL.registerAsIndicator(new ItemStack(this, 1, i), i - 1);
        }
    }

    // exclude meta 13 to not create "Unnamed" casing
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < 15; i++) {
            if (i == 13) continue;
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int aMeta) {
        if ((aMeta >= 0) && (aMeta < 15)) {
            return switch (aMeta) {
                case 0 ->
                    // Aquatic Casing
                    ordinalSide < 2 ? Textures.BlockIcons.AQUATIC_CASING_TOP.getIcon()
                        : Textures.BlockIcons.AQUATIC_CASING.getIcon();
                case 1 ->
                    // Inconel Reinforced Casing
                    Textures.BlockIcons.INCONEL_REINFORCED_CASING.getIcon();
                case 2 ->
                    // Multi-Use Casing
                    Textures.BlockIcons.MULTI_USE_CASING.getIcon();
                case 3 ->
                    // Vanadium Redox IV
                    Textures.BlockIcons.CASING_REDOX_IV.getIcon();
                case 4 ->
                    // Vanadium Redox LuV
                    Textures.BlockIcons.CASING_REDOX_LUV.getIcon();
                case 5 ->
                    // Vanadium Redox ZPM
                    Textures.BlockIcons.CASING_REDOX_ZPM.getIcon();
                case 6 ->
                    // Vanadium Redox UV
                    Textures.BlockIcons.CASING_REDOX_UV.getIcon();
                case 7 ->
                    // Vanadium Redox MAX
                    Textures.BlockIcons.CASING_REDOX_UHV.getIcon();
                case 8 ->
                    // Amazon Warehouse Casing
                    TexturesGtBlock.TEXTURE_CASING_AMAZON.getIcon();
                case 9 ->
                    // Adv. Vac. Freezer
                    TexturesGtBlock.TEXTURE_CASING_ADVANCED_CRYOGENIC.getIcon();
                case 10 ->
                    // Adv. EBF
                    TexturesGtBlock.TEXTURE_CASING_ADVANCED_VOLCNUS.getIcon();
                case 11 -> TexturesGtBlock.TEXTURE_CASING_FUSION_COIL_II.getIcon();
                case 12 -> TexturesGtBlock.TEXTURE_CASING_FUSION_COIL_II_INNER.getIcon();
                case 13 -> TexturesGtBlock.TEXTURE_CASING_FUSION_CASING_ULTRA.getIcon();
                case 14 -> Textures.BlockIcons.CONTAINMENT_CASING.getIcon();
                default -> Textures.GlobalIcons.RENDERING_ERROR.getIcon();
            };
        }
        return Textures.GlobalIcons.RENDERING_ERROR.getIcon();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side) {
        Block block = worldIn.getBlock(x, y, z);

        if (worldIn.getBlockMetadata(x, y, z) != worldIn.getBlockMetadata(
            x - Facing.offsetsXForSide[side],
            y - Facing.offsetsYForSide[side],
            z - Facing.offsetsZForSide[side])) {
            return true;
        }

        if (block == this) {
            return false;
        }

        return super.shouldSideBeRendered(worldIn, x, y, z, side);
    }
}
