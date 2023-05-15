package gregtech.common.tileentities.casings.upgrade;

import java.util.List;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;
import gregtech.api.net.GT_Packet_MultiTileEntity;

public class Inventory extends UpgradeCasing {

    public UUID inventoryID;
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    public static final int BOTH = 2;
    private String inventoryName = "inventory";
    private int inventorySize;
    private int type = BOTH;
    private boolean wasBroken;

    public String getCustomInventoryName() {
        return inventoryName;
    }

    public String getInventoryID() {
        return inventoryID.toString();
    }

    public void setInventoryName(String aInventoryName) {
        inventoryName = aInventoryName;
    }

    public int getType() {
        return type;
    }

    @Override
    protected void customWork(IMultiBlockController aTarget) {
        int tInvSize = inventorySize;
        if (type == BOTH) {
            tInvSize /= 2;
        }
        aTarget.registerInventory(inventoryName, inventoryID.toString(), tInvSize, type);
        if (isServerSide()) {
            issueClientUpdate();
        }
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.inventory";
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        super.readMultiTileNBT(aNBT);
        if (aNBT.hasKey(NBT.UPGRADE_INVENTORY_UUID)) {
            inventoryID = UUID.fromString(aNBT.getString(NBT.UPGRADE_INVENTORY_UUID));
        } else {
            inventoryID = UUID.randomUUID();
        }
        if (aNBT.hasKey(NBT.UPGRADE_INVENTORY_NAME)) {
            inventoryName = aNBT.getString(NBT.UPGRADE_INVENTORY_NAME);
        } else {
            inventoryName = "inventory";
        }
        inventorySize = aNBT.getInteger(NBT.UPGRADE_INVENTORY_SIZE);
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound aNBT) {
        super.writeMultiTileNBT(aNBT);
        aNBT.setString(NBT.UPGRADE_INVENTORY_UUID, inventoryID.toString());
        aNBT.setString(NBT.UPGRADE_INVENTORY_NAME, inventoryName);
    }

    @Override
    public boolean breakBlock() {
        final IMultiBlockController controller = getTarget(false);
        if (controller != null) {
            controller.unregisterInventory(inventoryName, inventoryID.toString(), type);
        }
        return super.breakBlock();
    }

    @Override
    public boolean hasGui(ForgeDirection side) {
        return true;
    }

    @Override
    public void addUIWidgets(Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new TextFieldWidget().setGetter(() -> inventoryName)
                .setSetter((val) -> {
                    inventoryName = val;
                    final IMultiBlockController controller = getTarget(false);
                    if (controller != null) {
                        controller.changeInventoryName(inventoryName, inventoryID.toString(), type);
                    }
                })
                .setSize(100, 25)
                .setPos(50, 30));
    }

    @Override
    protected boolean canOpenControllerGui() {
        return false;
    }

    @Override
    public GT_Packet_MultiTileEntity getClientDataPacket() {
        final GT_Packet_MultiTileEntity packet = super.getClientDataPacket();
        String name = getCustomInventoryName();
        packet.setInventoryName(name);
        return packet;
    }

    @Override
    public void addToolTips(List<String> list, ItemStack stack, boolean f3_h) {
        super.addToolTips(list, stack, f3_h);
        list.add("Adds another item inventory");
        list.add("Inventory size: " + inventorySize);
    }
}
