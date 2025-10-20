package gregtech.loaders.postload;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.util.GTLog;
import gregtech.common.blocks.ItemMachines;

public class MachineTooltipsLoader implements Runnable {

    @Override
    public void run() {
        if (!GTMod.GT.isClientSide()) return;
        GTLog.out.println("GT Mod: Register Block Machine's tooltips");
        for (int i = 0; i < 32768; i++) {
            ItemStack tStack = new ItemStack(GregTechAPI.sBlockMachines, 1, i);
            if (tStack.getItem() != null && tStack.getItem() instanceof ItemMachines) {
                ((ItemMachines) tStack.getItem()).registerDescription(i);
            }
        }
        StatCollector.translateToLocal("gt.tooltip.volt_in");
        StatCollector.translateToLocal("gt.tooltip.volt_out");
        StatCollector.translateToLocal("gt.tooltip.amp");
        StatCollector.translateToLocal("gt.tooltip.capacity");
        StatCollector.translateToLocal("gt.tooltip.contains_fluid");
        StatCollector.translateToLocal("gt.tooltip.fluid_amount");
        StatCollector.translateToLocal("gt.tooltip.contains_item");
        StatCollector.translateToLocal("gt.tooltip.item_amount");
        StatCollector.translateToLocal("gt.tooltip.has_muffler");
        StatCollector.translateToLocal("gt.tooltip.steam_tank_upgrades");
    }
}
