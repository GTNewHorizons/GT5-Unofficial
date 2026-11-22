package gregtech.nei.transfer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChatComponentTranslation;

import com.cleanroommc.modularui.value.sync.SyncHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;

/**
 * Carries NEI recipe transfer requests from the client to the machine and carries them out on the server.
 * <p>
 * It only exists while the player has the machine GUI open, and it only moves items between that player's inventory
 * and that machine, so a client cannot do anything it could not do by hand.
 */
public class RecipeTransferSyncHandler extends SyncHandler<RecipeTransferSyncHandler> {

    public static final String KEY = "neiRecipeTransfer";
    private static final int TRANSFER = 0;

    private final MTEBasicMachine machine;

    public RecipeTransferSyncHandler(MTEBasicMachine machine) {
        this.machine = machine;
        allowC2S();
    }

    @SideOnly(Side.CLIENT)
    void requestTransfer(RecipeTransferRequest request) {
        syncToServer(TRANSFER, request::write);
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) {}

    @Override
    public void readOnServer(int id, PacketBuffer buf) {
        // Only machines that process recipes; others share this GUI but may have slot rules of their own
        if (id != TRANSFER || machine.getRecipeMap() == null) return;
        RecipeTransferRequest request = RecipeTransferRequest.read(buf);
        if (request == null) return;

        EntityPlayer player = getSyncManager().getPlayer();
        MachineRecipeTransfer transfer = new MachineRecipeTransfer(machine, player, request);
        switch (transfer.result()
            .outcome()) {
            case TRANSFERRED -> transfer.apply();
            case NO_ROOM_FOR_RETURNED_ITEMS -> player
                .addChatMessage(new ChatComponentTranslation("GT5U.machines.nei_transfer.no_room"));
            case OTHER_FLUID_IN_TANK -> player
                .addChatMessage(new ChatComponentTranslation("GT5U.machines.nei_transfer.other_fluid"));
        }
    }
}
