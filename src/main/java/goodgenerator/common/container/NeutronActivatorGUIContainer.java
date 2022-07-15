package goodgenerator.common.container;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_Container_MultiMachineEM;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.blocks.tileEntity.NeutronActivator;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import java.nio.ByteBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

public class NeutronActivatorGUIContainer extends GT_Container_MultiMachineEM {

    private int KineticE;

    private ByteBuffer buffer;

    public NeutronActivatorGUIContainer(InventoryPlayer inventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(inventoryPlayer, aTileEntity, true, true, true);
    }

    @Override
    public void addCraftingToCrafters(ICrafting clientHandle) {
        buffer.putInt(0, KineticE);
        sendStateUpdate(clientHandle);
        super.addCraftingToCrafters(clientHandle);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (buffer == null) {
            buffer = ByteBuffer.allocate(Integer.BYTES);
        }
        if (mTileEntity.isServerSide()) {
            NeutronActivator tile = (NeutronActivator) mTileEntity.getMetaTileEntity();
            if (tile == null) return;
            int currentKineticE = tile.getCurrentNeutronKineticEnergy();
            boolean isUpdated = false;
            if (currentKineticE != KineticE) {
                KineticE = currentKineticE;
                buffer.putInt(0, KineticE);
                isUpdated = true;
            }
            for (Object clientHandle : this.crafters) {
                if (isUpdated) {
                    sendStateUpdate((ICrafting) clientHandle);
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    private void sendStateUpdate(ICrafting clientHandle) {
        final int bytes = Integer.BYTES;
        for (int i = 0; i < bytes; i++) {
            clientHandle.sendProgressBarUpdate(this, i + 21, buffer.get(i));
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        super.updateProgressBar(index, value);
        index = index - 21;
        if (index >= 0 && index < buffer.capacity()) {
            buffer.put(index, (byte) value);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getKineticE() {
        return buffer.getInt(0);
    }
}
