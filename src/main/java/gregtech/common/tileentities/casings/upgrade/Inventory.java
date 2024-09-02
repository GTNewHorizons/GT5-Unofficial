package gregtech.common.tileentities.casings.upgrade;

import java.util.List;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.enums.GTValues.NBT;
import gregtech.api.enums.InventoryType;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;

public class Inventory extends UpgradeCasing {

    public UUID inventoryID;

    private String inventoryName = "inventory";
    private int inventorySize;
    private InventoryType type = InventoryType.Both;

    public String getCustomInventoryName() {
        return inventoryName;
    }

    public String getInventoryID() {
        return inventoryID != null ? inventoryID.toString() : "";
    }

    public void setInventoryName(String aInventoryName) {
        inventoryName = aInventoryName;
    }

    public InventoryType getType() {
        return type;
    }

    @Override
    protected void customWork(IMultiBlockController target) {
        int invSize = inventorySize;
        if (type == InventoryType.Both) {
            invSize /= 2;
        }
        target.registerItemInventory(invSize, tier, type, true);
        if (isServerSide()) {
            issueClientUpdate();
        }
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.inventory";
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound nbt) {
        super.readMultiTileNBT(nbt);
        if (nbt.hasKey(NBT.UPGRADE_INVENTORY_NAME)) {
            inventoryName = nbt.getString(NBT.UPGRADE_INVENTORY_NAME);
        } else {
            inventoryName = "inventory";
        }
        inventorySize = nbt.getInteger(NBT.UPGRADE_INVENTORY_SIZE);
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound nbt) {
        super.writeMultiTileNBT(nbt);
        if (inventoryID != null) nbt.setString(NBT.UPGRADE_INVENTORY_UUID, inventoryID.toString());
        if (inventoryName != null) nbt.setString(NBT.UPGRADE_INVENTORY_NAME, inventoryName);
    }

    @Override
    public boolean onBlockBroken() {
        final IMultiBlockController controller = getTarget(false);
        if (controller != null && inventoryID != null) {
            controller.unregisterItemInventory(inventoryID, type);
        }
        return super.onBlockBroken();
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
                    if (controller != null && inventoryID != null) {
                        controller.changeItemInventoryDisplayName(inventoryID, inventoryName, type);
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
    public void addToolTips(List<String> list, ItemStack stack, boolean f3_h) {
        super.addToolTips(list, stack, f3_h);
        list.add("Adds another item inventory");
        list.add("Inventory size: " + inventorySize);
        list.add("Inventory Type: " + type);
    }

    public void setInventoryId(String inventoryID) {
        this.inventoryID = UUID.fromString(inventoryID);
    }
}
