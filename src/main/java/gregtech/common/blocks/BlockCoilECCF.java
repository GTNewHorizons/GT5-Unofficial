package gregtech.common.blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

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
            case 0 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 200K to 400K"));
            case 1 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 100K to 200K"));
            case 2 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 30K to 100K"));
            case 3 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 0K to 30K"));
            case 4 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 300K to 2300K"));
            case 5 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 2300K to 5300K"));
            case 6 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 5300K to 10000K"));
            case 7 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 10000K to 20000K"));
            case 8 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Pressure range: 100kPa to 150kPa"));
            case 9 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Pressure range: 150kPa to 350kPa"));
            case 10 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Pressure range: 350kPa to 700kPa"));
            case 11 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Pressure range: 700kPa to 1200kPa"));
            case 12 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Pressure range: 50kPa to 100kPa"));
            case 13 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Pressure range: 30kPa to 50kPa"));
            case 14 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Pressure range: 10kPa to 30kPa"));
            case 15 -> tooltip
                .add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Pressure range: 0kPa to 10kPa"));
        }
    }
}
