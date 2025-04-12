package tectech.thing.casing;

import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.BOLD;
import static net.minecraft.util.EnumChatFormatting.RED;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.common.blocks.ItemCasings;
import tectech.util.CommonValues;

public class ItemCasingsGodforge extends ItemCasings {

    public ItemCasingsGodforge(Block par1) {
        super(par1);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List tooltip, boolean aF3_H) {
        tooltip.add(CommonValues.GODFORGE_MARK);
        switch (aStack.getItemDamage()) {
            case 0:
                tooltip.add(StatCollector.translateToLocal("tt.godforge.casings.0.Tooltip.0"));
                tooltip.add(AQUA + "" + BOLD + StatCollector.translateToLocal("tt.godforge.casings.0.Tooltip.1"));
                break;
            case 1:
                tooltip.add(StatCollector.translateToLocal("tt.godforge.casings.1.Tooltip.0"));
                tooltip.add(AQUA + "" + BOLD + StatCollector.translateToLocal("tt.godforge.casings.1.Tooltip.1"));
                break;
            case 2:
                tooltip.add(StatCollector.translateToLocal("tt.godforge.casings.2.Tooltip.0"));
                tooltip.add(AQUA + "" + BOLD + StatCollector.translateToLocal("tt.godforge.casings.2.Tooltip.1"));
                break;
            case 3:
                tooltip.add(StatCollector.translateToLocal("tt.godforge.casings.3.Tooltip.0"));
                tooltip.add(AQUA + "" + BOLD + StatCollector.translateToLocal("tt.godforge.casings.3.Tooltip.1"));
                break;
            case 4:
                tooltip.add(StatCollector.translateToLocal("tt.godforge.casings.4.Tooltip.0"));
                tooltip.add(AQUA + "" + BOLD + StatCollector.translateToLocal("tt.godforge.casings.4.Tooltip.1"));
                break;
            case 5:
                tooltip.add(StatCollector.translateToLocal("tt.godforge.casings.5.Tooltip.0"));
                tooltip.add(AQUA + "" + BOLD + StatCollector.translateToLocal("tt.godforge.casings.5.Tooltip.1"));
                break;
            case 6:
                tooltip.add(StatCollector.translateToLocal("tt.godforge.casings.6.Tooltip.0"));
                tooltip.add(AQUA + "" + BOLD + StatCollector.translateToLocal("tt.godforge.casings.6.Tooltip.1"));
                break;
            case 7:
                tooltip.add(StatCollector.translateToLocal("tt.godforge.casings.7.Tooltip.0"));
                tooltip.add(AQUA + "" + BOLD + StatCollector.translateToLocal("tt.godforge.casings.7.Tooltip.1"));
                break;
            case 8:
                tooltip.add(StatCollector.translateToLocal("tt.godforge.casings.8.Tooltip.0"));
                tooltip.add(AQUA + "" + BOLD + StatCollector.translateToLocal("tt.godforge.casings.8.Tooltip.1"));
                tooltip.add(AQUA + "" + BOLD + StatCollector.translateToLocal("tt.godforge.casings.8.Tooltip.2"));
                break;
            default:
                tooltip.add(RED + "" + BOLD + StatCollector.translateToLocal("tt.godforge.casings.Error.Tooltip"));
        }
    }
}
