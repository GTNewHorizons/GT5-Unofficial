package gregtech.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.List;

public class BlockCoilECCF extends BlockCasingsAbstract {

    public BlockCoilECCF() {
        super(ItemCasings.class, "gt.blockECCF", MaterialCasings.INSTANCE, 8);

        register(0, ItemList.ECCF_cooling_block_0, "ECCF Freezer Tier 1");
        register(1, ItemList.ECCF_cooling_block_1, "ECCF Freezer Tier 2");
        register(2, ItemList.ECCF_cooling_block_2, "ECCF Freezer Tier 3");
        register(3, ItemList.ECCF_cooling_block_3, "ECCF Freezer Tier 4");
        register(4, ItemList.ECCF_heating_block_0, "ECCF Heater Tier 1");
        register(5, ItemList.ECCF_heating_block_1, "ECCF Heater Tier 2");
        register(6, ItemList.ECCF_heating_block_2, "ECCF Heater Tier 3");
        register(7, ItemList.ECCF_heating_block_3, "ECCF Heater Tier 4");
        register(8, ItemList.ECCF_pressure_block_0, "ECCF Compressor Tier 1");
        register(9, ItemList.ECCF_pressure_block_1, "ECCF Compressor Tier 2");
        register(10, ItemList.ECCF_pressure_block_2, "ECCF Compressor Tier 3");
        register(11, ItemList.ECCF_pressure_block_3, "ECCF Compressor Tier 4");
        register(12, ItemList.ECCF_vacuum_block_0, "ECCF Vacuum Pump Tier 1");
        register(13, ItemList.ECCF_vacuum_block_1, "ECCF Vacuum Pump Tier 2");
        register(14, ItemList.ECCF_vacuum_block_2, "ECCF Vacuum Pump Tier 3");
        register(15, ItemList.ECCF_vacuum_block_3, "ECCF Vacuum Pump Tier 4");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.ECCF_COOLING_BLOCK_0.getIcon();
            case 1 -> Textures.BlockIcons.ECCF_COOLING_BLOCK_1.getIcon();
            case 2 -> Textures.BlockIcons.ECCF_COOLING_BLOCK_2.getIcon();
            case 3 -> Textures.BlockIcons.ECCF_COOLING_BLOCK_3.getIcon();
            case 4 -> Textures.BlockIcons.ECCF_HEATING_BLOCK_0.getIcon();
            case 5 -> Textures.BlockIcons.ECCF_HEATING_BLOCK_1.getIcon();
            case 6 -> Textures.BlockIcons.ECCF_HEATING_BLOCK_2.getIcon();
            case 7 -> Textures.BlockIcons.ECCF_HEATING_BLOCK_3.getIcon();
            case 8 -> Textures.BlockIcons.ECCF_PRESSURE_BLOCK_0.getIcon();
            case 9 -> Textures.BlockIcons.ECCF_PRESSURE_BLOCK_1.getIcon();
            case 10 -> Textures.BlockIcons.ECCF_PRESSURE_BLOCK_2.getIcon();
            case 11 -> Textures.BlockIcons.ECCF_PRESSURE_BLOCK_3.getIcon();
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
            case 0 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 200, 400));
            case 1 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 100, 200));
            case 2 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 30, 100));
            case 3 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 0, 30));
            case 4 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 300, 2300));
            case 5 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 2300, 5300));
            case 6 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 5300, 10000));
            case 7 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFTemp", 10000, 20000));
            case 8 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 100, 150));
            case 9 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 150, 350));
            case 10 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 350, 700));
            case 11 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 700, 1200));
            case 12 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 50, 100));
            case 13 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 30, 50));
            case 14 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 10, 30));
            case 15 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFPressure", 0, 10));
        }
    }
}
