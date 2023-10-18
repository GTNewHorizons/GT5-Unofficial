package gtPlusPlus.core.tileentities.machines;

import java.util.List;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import gregtech.api.enums.ItemList;
import gregtech.common.items.GT_MetaGenerated_Item_01;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.container.Container_ProjectTable;
import gtPlusPlus.core.inventories.projecttable.InventoryProjectMain;
import gtPlusPlus.core.inventories.projecttable.InventoryProjectOutput;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechItems;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;

public class TileEntityProjectTable extends TileEntity
        implements INetworkDataProvider, INetworkUpdateListener, IWrenchable {

    public InventoryProjectMain inventoryGrid;
    public InventoryProjectOutput inventoryOutputs;

    private Container_ProjectTable container;

    public TileEntityProjectTable() {
        this.inventoryGrid = new InventoryProjectMain(); // number of slots - without product slot
        this.inventoryOutputs = new InventoryProjectOutput(); // number of slots - without product slot
        this.canUpdate();
    }

    public void setContainer(Container_ProjectTable container) {
        this.container = container;
    }

    public NBTTagCompound getTag(final NBTTagCompound nbt, final String tag) {
        if (!nbt.hasKey(tag)) {
            nbt.setTag(tag, new NBTTagCompound());
        }
        return nbt.getCompoundTag(tag);
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("facing", this.facing);
        this.inventoryGrid.writeToNBT(this.getTag(nbt, "ContentsGrid"));
        this.inventoryOutputs.writeToNBT(this.getTag(nbt, "ContentsOutput"));
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.prevFacing = (this.facing = nbt.getShort("facing"));
        this.inventoryGrid.readFromNBT(nbt.getCompoundTag("ContentsGrid"));
        this.inventoryOutputs.readFromNBT(nbt.getCompoundTag("ContentsOutput"));
    }

    @Override
    public List<String> getNetworkedFields() {
        final List<String> ret = new Vector(2);
        ret.add("facing");
        return ret;
    }

    @Override
    public boolean wrenchCanSetFacing(final EntityPlayer entityPlayer, final int side) {
        return false;
    }

    private short facing = 0;
    public short prevFacing = 0;

    @Override
    public void setFacing(final short facing1) {
        this.facing = facing1;
        if (this.prevFacing != facing1) {
            IC2.network.get().updateTileEntityField(this, "facing");
        }
        this.prevFacing = facing1;
    }

    @Override
    public short getFacing() {
        return this.facing;
    }

    @Override
    public boolean wrenchCanRemove(final EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public float getWrenchDropRate() {
        return 1.0F;
    }

    @Override
    public ItemStack getWrenchDrop(final EntityPlayer entityPlayer) {
        return new ItemStack(
                this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord),
                1,
                this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
    }

    @Override
    public void onNetworkUpdate(final String field) {
        this.prevFacing = this.facing;
    }

    @Override
    public void updateEntity() {
        if (!this.worldObj.isRemote) {
            // Data stick
            ItemStack dataStick = this.inventoryOutputs.getStackInSlot(0);
            if (dataStick != null && this.container != null && container.getOutputContent() != null) {
                if ((dataStick.getItem() instanceof GT_MetaGenerated_Item_01 && dataStick.getItemDamage() == 32708)
                        || (dataStick == ItemList.Tool_DataStick.get(1))
                        || (dataStick == GregtechItemList.Old_Tool_DataStick.get(1))
                        || (dataStick.getItem() instanceof MetaGeneratedGregtechItems
                                && dataStick.getItemDamage() == 32208)) {

                    Logger.INFO("Found Data Stick and valid container.");

                    ItemStack outputComponent = container.getOutputContent();
                    ItemStack[] craftInputComponent = container.getInputComponents();

                    ItemStack newStick = NBTUtils
                            .writeItemsToNBT(dataStick, new ItemStack[] { outputComponent }, "Output");
                    newStick = NBTUtils.writeItemsToNBT(newStick, craftInputComponent);
                    NBTUtils.setBookTitle(newStick, "Encrypted Project Data");
                    NBTUtils.setBoolean(newStick, "mEncrypted", true);
                    int slotm = 0;
                    Logger.WARNING("Uploading to Data Stick.");
                    for (ItemStack is : NBTUtils.readItemsFromNBT(newStick)) {
                        if (is != null) {
                            Logger.WARNING("Uploaded " + is.getDisplayName() + " into memory slot " + slotm + ".");
                        } else {
                            Logger.WARNING("Left memory slot " + slotm + " blank.");
                        }
                        slotm++;
                    }
                    Logger.WARNING("Encrypting Data Stick.");
                    this.inventoryOutputs.setInventorySlotContents(1, newStick);
                    this.inventoryOutputs.setInventorySlotContents(0, null);
                }
            }

        }
        super.updateEntity();
    }

    @Override
    public boolean canUpdate() {
        return true;
    }
}
