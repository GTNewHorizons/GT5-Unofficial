package gregtech.common.gui.modularui2.sync;

import java.util.function.BooleanSupplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.value.sync.SyncHandler;

/**
 * Checks the supplied validator on every server tick of the GUI, and closes it if validator returns false.
 */
public class GuiValidator extends SyncHandler {

    private final EntityPlayer player;
    private final BooleanSupplier validator;

    public GuiValidator(EntityPlayer player, BooleanSupplier validator) {
        this.player = player;
        this.validator = validator;
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) {}

    @Override
    public void readOnServer(int id, PacketBuffer buf) {}

    @Override
    public void detectAndSendChanges(boolean init) {
        if (!validator.getAsBoolean()) {
            player.closeScreen();
        }
    }
}
