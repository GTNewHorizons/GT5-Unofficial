package gregtech.common.modularui2.sync;

import static gregtech.common.modularui2.factory.SelectItemGuiBuilder.DESELECTED;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.value.sync.ItemSlotSH;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.util.item.GhostCircuitItemStackHandler;
import gregtech.common.items.ItemIntegratedCircuit;

/**
 * Sync handler dedicated for {@link gregtech.common.modularui2.widget.GhostCircuitSlotWidget GhostCircuitSlotWidget}.
 */
public class GhostCircuitSyncHandler extends PhantomItemSlotSH {

    public static final int SYNC_CIRCUIT_CONFIG = 10;

    @SuppressWarnings("UnstableApiUsage")
    public GhostCircuitSyncHandler(ModularSlot slot) {
        super(slot);
    }

    @Override
    protected void phantomClick(MouseData mouseData, ItemStack cursorStack) {
        if (cursorStack != null && cursorStack.getItem() instanceof ItemIntegratedCircuit) {
            setCircuitConfig(cursorStack.getItemDamage());
        } else {
            if (mouseData.mouseButton == 0) {
                // increment on left-click
                setCircuitConfig(getNextCircuitConfig(1));
            } else if (mouseData.mouseButton == 1 && mouseData.shift) {
                // clear on shift-right-click
                setCircuitConfig(DESELECTED);
            } else if (mouseData.mouseButton == 1) {
                // decrement on right-click
                setCircuitConfig(getNextCircuitConfig(-1));
            }
        }
    }

    @Override
    protected void phantomScroll(MouseData mouseData) {
        setCircuitConfig(getNextCircuitConfig(mouseData.mouseButton));
    }

    private void setCircuitConfig(int config) {
        GhostCircuitItemStackHandler handler = getGhostCircuitHandler();
        if (handler.getCircuitConfig() != config) {
            handler.setCircuitConfig(config);
            syncToClient(ItemSlotSH.SYNC_ITEM, buf -> {
                buf.writeBoolean(false);// onlyAmountChanged
                NetworkUtils.writeItemStack(buf, handler.getStackInSlot(0));
                buf.writeBoolean(false);// init
                buf.writeBoolean(false);// force sync
            });
        }
    }

    @Override
    public void readOnServer(int id, PacketBuffer buf) throws IOException {
        if (id == SYNC_CIRCUIT_CONFIG) {
            setCircuitConfig(buf.readShort());
        } else {
            super.readOnServer(id, buf);
        }
    }

    private int getNextCircuitConfig(int delta) {
        GhostCircuitItemStackHandler handler = getGhostCircuitHandler();

        // If no circuit, skip 0 and return 24 if decrementing,
        // or, skip 0 and return 1 when incrementing
        if (!handler.hasCircuit()) {
            return delta == 1 ? 1 : ItemIntegratedCircuit.MAX_CIRCUIT_NUMBER;
            // If at max, loop around to no circuit
        } else if (handler.getCircuitConfig() + delta > ItemIntegratedCircuit.MAX_CIRCUIT_NUMBER) {
            return GhostCircuitItemStackHandler.NO_CONFIG;
            // If at 1, skip 0 and return to no circuit
        } else if (handler.getCircuitConfig() + delta < 1) {
            return GhostCircuitItemStackHandler.NO_CONFIG;
        }

        // Normal case: change by "delta" which is either 1 or -1
        return handler.getCircuitConfig() + delta;
    }

    public GhostCircuitItemStackHandler getGhostCircuitHandler() {
        IItemHandler handler = getSlot().getItemHandler();
        if (!(handler instanceof GhostCircuitItemStackHandler ghostHandler)) {
            throw new IllegalStateException(
                "GhostCircuitSyncHandler has IItemHandler that is not GhostCircuitItemStackHandler");
        }
        return ghostHandler;
    }
}
