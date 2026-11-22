package gregtech.nei.transfer;

import com.cleanroommc.modularui.screen.ModularContainer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;

/**
 * Keeps the client only container out of the GUI classes, which are loaded on the server too.
 */
@SideOnly(Side.CLIENT)
public final class RecipeTransferClient {

    private RecipeTransferClient() {}

    public static ModularContainer createContainer(MTEBasicMachine machine, RecipeTransferSyncHandler syncHandler) {
        return new RecipeTransferContainer(machine, syncHandler);
    }
}
