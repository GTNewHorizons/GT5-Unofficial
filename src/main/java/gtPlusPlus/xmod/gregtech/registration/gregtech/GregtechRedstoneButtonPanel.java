package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.RedstoneButtonPanel;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.MTERedstoneButtonPanel;

public class GregtechRedstoneButtonPanel {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Redstone Button Panel.");
        GregtechItemList.RedstoneButtonPanel.set(new MTERedstoneButtonPanel(RedstoneButtonPanel.ID).getStackForm(1L));

    }
}
