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

    public UUID mInventoryID;
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    public static final int BOTH = 2;
    private String mInventoryName = "inventory";
    private int mInventorySize;
    private int mType = BOTH;

    public String getInventoryName() {
        return mInventoryName;
    }

    public void setInventoryName(String aInventoryName) {
        mInventoryName = aInventoryName;
    }

    @Override
    protected void customWork(IMultiBlockController aTarget) {
        int tInvSize = mInventorySize;
        if (mType == BOTH) {
            tInvSize /= 2;
        }
        aTarget.registerInventory(mInventoryName, mInventoryID.toString(), tInvSize, mType);
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.inventory";
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        super.readMultiTileNBT(aNBT);
        if (aNBT.hasKey(NBT.UPGRADE_INVENTORY_UUID)) {
            mInventoryID = UUID.fromString(aNBT.getString(NBT.UPGRADE_INVENTORY_UUID));
        } else {
            mInventoryID = UUID.randomUUID();
        }
        if (aNBT.hasKey(NBT.UPGRADE_INVENTORY_NAME)) {
            mInventoryName = aNBT.getString(NBT.UPGRADE_INVENTORY_NAME);
        } else {
            mInventoryName = "inventory";
        }
        mInventorySize = aNBT.getInteger(NBT.UPGRADE_INVENTORY_SIZE);
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound aNBT) {
        super.writeMultiTileNBT(aNBT);
        aNBT.setString(NBT.UPGRADE_INVENTORY_UUID, mInventoryID.toString());
        aNBT.setString(NBT.UPGRADE_INVENTORY_NAME, mInventoryName);
    }

    @Override
    protected void onBaseTEDestroyed() {
        super.onBaseTEDestroyed();
        unregisterInventories();
    }

    private void unregisterInventories() {
        final IMultiBlockController controller = getTarget(false);
        if (controller != null) {
            controller.unregisterInventory(mInventoryName, mInventoryID.toString(), mType);
        }
    }

    @Override
    public boolean hasGui(ForgeDirection side) {
        return true;
    }

    @Override
    public void addUIWidgets(Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new TextFieldWidget().setGetter(() -> mInventoryName)
                .setSetter((val) -> {
                    mInventoryName = val;
                    final IMultiBlockController controller = getTarget(false);
                    if (controller != null) {
                        controller.changeInventoryName(mInventoryName, mInventoryID.toString(), mType);
                    }
                })
                .setSize(100, 25)
                .setPos(50, 30));
    }

    @Override
    public GT_Packet_MultiTileEntity getClientDataPacket() {
        final GT_Packet_MultiTileEntity packet = super.getClientDataPacket();
        String name = getInventoryName();
        packet.setInventoryName(name);
        return packet;
    }

    @Override
    public void addToolTips(List<String> list, ItemStack stack, boolean f3_h) {
        super.addToolTips(list, stack, f3_h);
        list.add("Adds another item inventory");
        list.add("Inventory size: " + mInventorySize);
    }
}
