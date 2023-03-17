package gregtech.loaders.postload;

import static gregtech.api.enums.GT_Values.GT;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.common.blocks.GT_Item_Machines;

public class GT_MachineTooltipsLoader implements Runnable {

    @Override
    public void run() {
        if (!GT.isClientSide()) return;
        GT_Log.out.println("GT Mod: Register Block Machine's tooltips");
        for (int i = 0; i < 32768; i++) {
            ItemStack tStack = new ItemStack(GregTech_API.sBlockMachines, 1, i);
            if (tStack.getItem() != null && tStack.getItem() instanceof GT_Item_Machines) {
                ((GT_Item_Machines) tStack.getItem()).registerDescription(i);
            }
        }
        GT_LanguageManager.addStringLocalization("TileEntity_EUp_IN", "Voltage IN: ");
        GT_LanguageManager.addStringLocalization("TileEntity_EUp_OUT", "Voltage OUT: ");
        GT_LanguageManager.addStringLocalization("TileEntity_EUp_AMOUNT", "Amperage: ");
        GT_LanguageManager.addStringLocalization("TileEntity_EUp_STORE", "Capacity: ");
        GT_LanguageManager.addStringLocalization("TileEntity_TANK_INFO", "Contains Fluid: ");
        GT_LanguageManager.addStringLocalization("TileEntity_TANK_AMOUNT", "Fluid Amount: ");
        GT_LanguageManager.addStringLocalization("TileEntity_CHEST_INFO", "Contains Item: ");
        GT_LanguageManager.addStringLocalization("TileEntity_CHEST_AMOUNT", "Item Amount: ");
        GT_LanguageManager.addStringLocalization("GT_TileEntity_MUFFLER", "has Muffler Upgrade");
        GT_LanguageManager.addStringLocalization("GT_TileEntity_STEAMCONVERTER", "has Steam Upgrade");
        GT_LanguageManager.addStringLocalization("GT_TileEntity_STEAMTANKS", "Steam Tank Upgrades");
    }
}
