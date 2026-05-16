package gregtech.loaders.postload;

import net.minecraft.item.ItemStack;

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
    }
}
