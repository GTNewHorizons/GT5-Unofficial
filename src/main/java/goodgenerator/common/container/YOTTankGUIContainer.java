package goodgenerator.common.container;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_Container_MultiMachineEM;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.blocks.tileEntity.YottaFluidTank;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import java.nio.ByteBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.util.StatCollector;

public class YOTTankGUIContainer extends GT_Container_MultiMachineEM {

    private String currentStore = "";
    private String store = "";
    private String fluidName = "";

    private ByteBuffer buffer;

    public YOTTankGUIContainer(InventoryPlayer inventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(inventoryPlayer, aTileEntity, true, true, true);
    }

    @Override
    public void addCraftingToCrafters(ICrafting clientHandle) {
        buffer.putInt(0, currentStore.length());
        buffer.putInt(Integer.BYTES, store.length());
        buffer.putInt(Integer.BYTES * 2, fluidName.length());
        for (int i = 0; i < currentStore.length(); ++i) {
            buffer.putChar(Integer.BYTES * 3 + Character.BYTES * i, currentStore.charAt(i));
        }
        for (int i = 0; i < store.length(); ++i) {
            buffer.putChar(Integer.BYTES * 3 + Character.BYTES * (i + currentStore.length()), store.charAt(i));
        }
        for (int i = 0; i < fluidName.length(); ++i) {
            buffer.putChar(
                    Integer.BYTES * 3 + Character.BYTES * (i + currentStore.length() + store.length()),
                    fluidName.charAt(i));
        }
        sendStateUpdate(clientHandle);
        super.addCraftingToCrafters(clientHandle);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (buffer == null) {
            buffer = ByteBuffer.allocate(8192);
        }
        if (mTileEntity.isServerSide()) {
            YottaFluidTank tile = (YottaFluidTank) mTileEntity.getMetaTileEntity();
            if (tile == null) return;
            String newStored = tile.getStored();
            String newCap = tile.getCap();
            String newFluid = tile.getFluidName();
            boolean isUpdated = false;
            if (!newStored.equals(currentStore) || !newCap.equals(store) || !newFluid.equals(fluidName)) {
                currentStore = newStored;
                store = newCap;
                fluidName = newFluid;
                buffer.putInt(0, currentStore.length());
                buffer.putInt(Integer.BYTES, store.length());
                buffer.putInt(Integer.BYTES * 2, fluidName.length());
                for (int i = 0; i < currentStore.length(); ++i) {
                    buffer.putChar(Integer.BYTES * 3 + Character.BYTES * i, currentStore.charAt(i));
                }
                for (int i = 0; i < store.length(); ++i) {
                    buffer.putChar(Integer.BYTES * 3 + Character.BYTES * (i + currentStore.length()), store.charAt(i));
                }
                for (int i = 0; i < fluidName.length(); ++i) {
                    buffer.putChar(
                            Integer.BYTES * 3 + Character.BYTES * (i + currentStore.length() + store.length()),
                            fluidName.charAt(i));
                }
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
        final int bytes =
                Integer.BYTES * 3 + Character.BYTES * (currentStore.length() + store.length() + fluidName.length());
        for (int i = 0; i < bytes; i++) {
            clientHandle.sendProgressBarUpdate(this, i + 300, buffer.get(i));
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        super.updateProgressBar(index, value);
        index = index - 300;
        if (index >= 0 && index < buffer.capacity()) {
            buffer.put(index, (byte) value);
        }
    }

    @SideOnly(Side.CLIENT)
    public String getStorage() {
        StringBuilder sb = new StringBuilder();
        int startP = Integer.BYTES * 3;
        for (int i = 0; i < buffer.getInt(0); ++i) {
            sb.append(buffer.getChar(startP + Character.BYTES * i));
        }
        return sb.toString();
    }

    @SideOnly(Side.CLIENT)
    public String getCap() {
        StringBuilder sb = new StringBuilder();
        int startP = Integer.BYTES * 3 + Character.BYTES * buffer.getInt(0);
        for (int i = 0; i < buffer.getInt(Integer.BYTES); ++i) {
            sb.append(buffer.getChar(startP + Character.BYTES * i));
        }
        return sb.toString();
    }

    @SideOnly(Side.CLIENT)
    public String getFluidName() {
        StringBuilder sb = new StringBuilder();
        int startP = Integer.BYTES * 3 + Character.BYTES * (buffer.getInt(0) + buffer.getInt(Integer.BYTES));
        for (int i = 0; i < buffer.getInt(Integer.BYTES * 2); ++i) {
            sb.append(buffer.getChar(startP + Character.BYTES * i));
        }
        return StatCollector.translateToLocal(sb.toString());
    }
}
