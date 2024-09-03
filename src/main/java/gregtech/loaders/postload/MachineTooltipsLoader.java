package gregtech.loaders.postload;

import static gregtech.api.enums.GTValues.GT;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTLog;
import gregtech.common.blocks.ItemMachines;

public class MachineTooltipsLoader implements Runnable {

    @Override
    public void run() {
        if (!GT.isClientSide()) return;
        GTLog.out.println("GT Mod: Register Block Machine's tooltips");
        for (int i = 0; i < 32768; i++) {
            ItemStack tStack = new ItemStack(GregTechAPI.sBlockMachines, 1, i);
            if (tStack.getItem() != null && tStack.getItem() instanceof ItemMachines) {
                ((ItemMachines) tStack.getItem()).registerDescription(i);
            }
        }
        GTLanguageManager.addStringLocalization("TileEntity_EUp_IN", "Voltage IN: ");
        GTLanguageManager.addStringLocalization("TileEntity_EUp_OUT", "Voltage OUT: ");
        GTLanguageManager.addStringLocalization("TileEntity_EUp_AMOUNT", "Amperage: ");
        GTLanguageManager.addStringLocalization("TileEntity_EUp_STORE", "Capacity: ");
        GTLanguageManager.addStringLocalization("TileEntity_TANK_INFO", "Contains Fluid: ");
        GTLanguageManager.addStringLocalization("TileEntity_TANK_AMOUNT", "Fluid Amount: ");
        GTLanguageManager.addStringLocalization("TileEntity_CHEST_INFO", "Contains Item: ");
        GTLanguageManager.addStringLocalization("TileEntity_CHEST_AMOUNT", "Item Amount: ");
        GTLanguageManager.addStringLocalization("GT_TileEntity_MUFFLER", "has Muffler Upgrade");
        GTLanguageManager.addStringLocalization("GT_TileEntity_STEAMCONVERTER", "has Steam Upgrade");
        GTLanguageManager.addStringLocalization("GT_TileEntity_STEAMTANKS", "Steam Tank Upgrades");
    }
}
