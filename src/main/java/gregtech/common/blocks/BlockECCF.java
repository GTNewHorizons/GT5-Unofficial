package gregtech.common.blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

public class BlockECCF extends BlockCasingsAbstract {

    public BlockECCF() {
        super(ItemCasings.class, "gt.blockECCF", MaterialCasings.INSTANCE, 8);

        register(0, ItemList.ECCF_freezer_block_0, "ECCF Freezer Tier 1");
        register(1, ItemList.ECCF_freezer_block_1, "ECCF Freezer Tier 2");
        register(2, ItemList.ECCF_freezer_block_2, "ECCF Freezer Tier 3");
        register(3, ItemList.ECCF_freezer_block_3, "ECCF Freezer Tier 4");
        register(4, ItemList.ECCF_heater_block_0, "ECCF Heater Tier 1");
        register(5, ItemList.ECCF_heater_block_1, "ECCF Heater Tier 2");
        register(6, ItemList.ECCF_heater_block_2, "ECCF Heater Tier 3");
        register(7, ItemList.ECCF_heater_block_3, "ECCF Heater Tier 4");
        register(8, ItemList.ECCF_compressor_block_0, "ECCF Compressor Tier 1");
        register(9, ItemList.ECCF_compressor_block_1, "ECCF Compressor Tier 2");
        register(10, ItemList.ECCF_compressor_block_2, "ECCF Compressor Tier 3");
        register(11, ItemList.ECCF_compressor_block_3, "ECCF Compressor Tier 4");
        register(12, ItemList.ECCF_vacuum_block_0, "ECCF Vacuum Pump Tier 1");
        register(13, ItemList.ECCF_vacuum_block_1, "ECCF Vacuum Pump Tier 2");
        register(14, ItemList.ECCF_vacuum_block_2, "ECCF Vacuum Pump Tier 3");
        register(15, ItemList.ECCF_vacuum_block_3, "ECCF Vacuum Pump Tier 4");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.ECCF_FREEZER_BLOCK_0.getIcon();
            case 1 -> Textures.BlockIcons.ECCF_FREEZER_BLOCK_1.getIcon();
            case 2 -> Textures.BlockIcons.ECCF_FREEZER_BLOCK_2.getIcon();
            case 3 -> Textures.BlockIcons.ECCF_FREEZER_BLOCK_3.getIcon();
            case 4 -> Textures.BlockIcons.ECCF_HEATER_BLOCK_0.getIcon();
            case 5 -> Textures.BlockIcons.ECCF_HEATER_BLOCK_1.getIcon();
            case 6 -> Textures.BlockIcons.ECCF_HEATER_BLOCK_2.getIcon();
            case 7 -> Textures.BlockIcons.ECCF_HEATER_BLOCK_3.getIcon();
            case 8 -> Textures.BlockIcons.ECCF_COMPRESSOR_BLOCK_0.getIcon();
            case 9 -> Textures.BlockIcons.ECCF_COMPRESSOR_BLOCK_1.getIcon();
            case 10 -> Textures.BlockIcons.ECCF_COMPRESSOR_BLOCK_2.getIcon();
            case 11 -> Textures.BlockIcons.ECCF_COMPRESSOR_BLOCK_3.getIcon();
            case 12 -> Textures.BlockIcons.ECCF_VACUUM_BLOCK_0.getIcon();
            case 13 -> Textures.BlockIcons.ECCF_VACUUM_BLOCK_1.getIcon();
            case 14 -> Textures.BlockIcons.ECCF_VACUUM_BLOCK_2.getIcon();
            default -> Textures.BlockIcons.ECCF_VACUUM_BLOCK_3.getIcon();
        };
    }

    // 0-3 temperature decrease, >start
    // 4-7 temperature increase, <start
    // 8-11 pressure decrease, >start
    // 12-15 pressure increase, <start

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedTooltips) {
        super.addInformation(stack, player, tooltip, advancedTooltips);
        switch (stack.getItemDamage()) {
            case 0 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 50));
            case 1 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 35));
            case 2 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 15));
            case 3 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 5));
            case 4 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 50));
            case 5 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 35));
            case 6 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 15));
            case 7 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 5));
            case 8 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 60));
            case 9 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 35));
            case 10 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 20));
            case 11 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 5));
            case 12 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 60));
            case 13 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 35));
            case 14 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 20));
            case 15 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 5));
        }
    }
}
