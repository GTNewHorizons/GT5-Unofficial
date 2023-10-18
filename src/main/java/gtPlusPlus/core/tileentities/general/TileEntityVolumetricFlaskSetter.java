package gtPlusPlus.core.tileentities.general;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.container.Container_VolumetricFlaskSetter;
import gtPlusPlus.core.inventories.Inventory_VolumetricFlaskSetter;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.helpers.VolumetricFlaskHelper;

public class TileEntityVolumetricFlaskSetter extends TileEntity implements ISidedInventory {

    private int tickCount = 0;
    private final Inventory_VolumetricFlaskSetter inventoryContents;
    private String customName;
    public int locationX;
    public int locationY;
    public int locationZ;
    private int aCurrentMode = 0;
    private int aCustomValue = 1000;

    public TileEntityVolumetricFlaskSetter() {
        this.inventoryContents = new Inventory_VolumetricFlaskSetter();
        this.setTileLocation();
    }

    public int getCustomValue() {
        // Logger.INFO("Value: "+this.aCustomValue);
        return this.aCustomValue;
    }

    public void setCustomValue(int aVal) {
        log("Old Value: " + this.aCustomValue);
        this.aCustomValue = (short) MathUtils.balance(aVal, 0, Short.MAX_VALUE);
        log("New Value: " + this.aCustomValue);
        markDirty();
    }

    public boolean setTileLocation() {
        if (this.hasWorldObj()) {
            if (!this.getWorldObj().isRemote) {
                this.locationX = this.xCoord;
                this.locationY = this.yCoord;
                this.locationZ = this.zCoord;
                return true;
            }
        }
        return false;
    }

    // Rename to hasCircuitToConfigure
    public final boolean hasFlask() {
        for (int i = 0; i < this.getInventory().getInventory().length - 1; i++) {
            if (i == Container_VolumetricFlaskSetter.SLOT_OUTPUT) {
                continue;
            }
            if (this.getInventory().getInventory()[i] != null) {
                return true;
            }
        }
        return false;
    }

    public Inventory_VolumetricFlaskSetter getInventory() {
        return this.inventoryContents;
    }

    private int getFlaskType(ItemStack aStack) {
        if (VolumetricFlaskHelper.isNormalVolumetricFlask(aStack)) {
            return 1;
        } else if (VolumetricFlaskHelper.isLargeVolumetricFlask(aStack)) {
            return 2;
        } else if (VolumetricFlaskHelper.isGiganticVolumetricFlask(aStack)) {
            return 3;
        }
        return 0;
    }

    private int getCapacityForSlot(int aSlot) {
        return switch (aSlot) {
            case 0 -> // 16
                16;
            case 1 -> // 36
                36;
            case 2 -> // 144
                144;
            case 3 -> // 432
                432;
            case 4 -> // 576
                576;
            case 5 -> // 720
                720;
            case 6 -> // 864
                864;
            case 7 -> // Custom
                getCustomValue();
            default -> 1000;
        };
    }

    public boolean addOutput() {

        // Don't do anything unless we have items
        if (!hasFlask()) {
            Logger.INFO("No Flasks.");
            return false;
        }

        ItemStack[] aInputs = this.getInventory().getInventory().clone();

        // Check if there is output in slot.
        Boolean hasOutput = false;
        if (aInputs[Container_VolumetricFlaskSetter.SLOT_OUTPUT] != null) {
            hasOutput = true;
            if (aInputs[Container_VolumetricFlaskSetter.SLOT_OUTPUT].stackSize >= 16) {
                return false;
            }
        }
        AutoMap<Integer> aValidSlots = new AutoMap<>();
        int aSlotCount = 0;
        for (ItemStack i : aInputs) {
            if (i != null) {
                aValidSlots.put(aSlotCount);
            }
            aSlotCount++;
        }
        for (int e : aValidSlots) {

            // Skip slot 7 (Custom) unless it has a value > 0
            if (e == 7 && getCustomValue() <= 0) {
                log("Skipping Custom slot as value <= 0");
                continue;
            }
            if (e == Container_VolumetricFlaskSetter.SLOT_OUTPUT) {
                continue;
            }

            boolean doAdd = false;
            ItemStack g = this.getStackInSlot(e);
            FluidStack aInputFluidStack = VolumetricFlaskHelper.getFlaskFluid(g);
            int aSize = 0;
            ItemStack aInputStack = null;
            int aTypeInSlot = getFlaskType(g);
            if (aTypeInSlot > 0 && g != null) {
                // No Existing Output
                if (!hasOutput) {
                    aSize = g.stackSize;
                    doAdd = true;
                }
                // Existing Output
                else {
                    ItemStack f = aInputs[Container_VolumetricFlaskSetter.SLOT_OUTPUT];
                    FluidStack aFluidInCheckedSlot = VolumetricFlaskHelper.getFlaskFluid(f);
                    int aTypeInCheckedSlot = getFlaskType(f);
                    // Check that the Circuit in the Output slot is not null and the same type as the circuit input.
                    if (aTypeInCheckedSlot > 0 && (aTypeInSlot == aTypeInCheckedSlot) && f != null) {
                        if (g.getItem() == f.getItem()
                                && VolumetricFlaskHelper.getFlaskCapacity(f) == getCapacityForSlot(e)
                                && ((aInputFluidStack == null && aFluidInCheckedSlot == null)
                                        || aInputFluidStack.isFluidEqual(aFluidInCheckedSlot))) {
                            log(
                                    "Input Slot Flask Contains: "
                                            + (aInputFluidStack != null ? aInputFluidStack.getLocalizedName()
                                                    : "Empty"));
                            log(
                                    "Output Slot Flask Contains: "
                                            + (aFluidInCheckedSlot != null ? aFluidInCheckedSlot.getLocalizedName()
                                                    : "Empty"));
                            aSize = f.stackSize + g.stackSize;
                            if (aSize > 16) {
                                aInputStack = g.copy();
                                aInputStack.stackSize = (aSize - 16);
                            }
                            doAdd = true;
                        }
                    }
                }
                if (doAdd) {
                    // Check Circuit Type
                    ItemStack aOutput;
                    FluidStack aOutputFluid = null;
                    if (!VolumetricFlaskHelper.isFlaskEmpty(g)) {
                        aOutputFluid = aInputFluidStack.copy();
                    }
                    if (aTypeInSlot == 1) {
                        aOutput = VolumetricFlaskHelper.getVolumetricFlask(1);
                    } else if (aTypeInSlot == 2) {
                        aOutput = VolumetricFlaskHelper.getLargeVolumetricFlask(1);
                    } else if (aTypeInSlot == 3) {
                        aOutput = VolumetricFlaskHelper.getGiganticVolumetricFlask(1);
                    } else {
                        aOutput = null;
                    }
                    if (aOutput != null) {
                        aOutput.stackSize = aSize;
                        int aCapacity = getCapacityForSlot(e);
                        VolumetricFlaskHelper.setNewFlaskCapacity(aOutput, aCapacity);
                        if (aOutputFluid != null) {
                            if (aOutputFluid.amount > aCapacity) {
                                aOutputFluid.amount = aCapacity;
                            }
                            VolumetricFlaskHelper.setFluid(aOutput, aOutputFluid);
                        }
                        this.setInventorySlotContents(e, aInputStack);
                        this.setInventorySlotContents(Container_VolumetricFlaskSetter.SLOT_OUTPUT, aOutput);
                        return true;
                    }
                }
            }
            continue;
        }
        return false;
    }

    @Override
    public void updateEntity() {
        try {
            if (!this.worldObj.isRemote) {
                if (tickCount % 10 == 0) {
                    if (hasFlask()) {
                        this.addOutput();
                        this.markDirty();
                    }
                }
                this.tickCount++;
            }
        } catch (final Throwable t) {}
    }

    public boolean anyPlayerInRange() {
        return this.worldObj.getClosestPlayer(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, 32) != null;
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
        // Utils.LOG_WARNING("Trying to write NBT data to TE.");
        final NBTTagCompound chestData = new NBTTagCompound();
        this.inventoryContents.writeToNBT(chestData);
        nbt.setTag("ContentsChest", chestData);
        nbt.setInteger("aCustomValue", aCustomValue);
        if (this.hasCustomInventoryName()) {
            nbt.setString("CustomName", this.getCustomName());
        }
        nbt.setInteger("aCurrentMode", aCurrentMode);
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        // Utils.LOG_WARNING("Trying to read NBT data from TE.");
        this.inventoryContents.readFromNBT(nbt.getCompoundTag("ContentsChest"));
        this.aCustomValue = nbt.getInteger("aCustomValue");
        if (nbt.hasKey("CustomName", 8)) {
            this.setCustomName(nbt.getString("CustomName"));
        }
        aCurrentMode = nbt.getInteger("aCurrentMode");
    }

    @Override
    public int getSizeInventory() {
        return this.getInventory().getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(final int slot) {
        return this.getInventory().getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(final int slot, final int count) {
        return this.getInventory().decrStackSize(slot, count);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int slot) {
        return this.getInventory().getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(final int slot, final ItemStack stack) {
        this.getInventory().setInventorySlotContents(slot, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return this.getInventory().getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(final EntityPlayer entityplayer) {
        return this.getInventory().isUseableByPlayer(entityplayer);
    }

    @Override
    public void openInventory() {
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 1);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
        this.getInventory().openInventory();
    }

    @Override
    public void closeInventory() {
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 1);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
        this.getInventory().closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(final int slot, final ItemStack itemstack) {
        return this.getInventory().isItemValidForSlot(slot, itemstack);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
        final int[] accessibleSides = new int[this.getSizeInventory()];
        for (int r = 0; r < this.getInventory().getSizeInventory(); r++) {
            accessibleSides[r] = r;
        }
        return accessibleSides;
    }

    @Override
    public boolean canInsertItem(final int aSlot, final ItemStack p_102007_2_, final int p_102007_3_) {
        return aSlot == aCurrentMode;
    }

    @Override
    public boolean canExtractItem(final int aSlot, final ItemStack p_102008_2_, final int p_102008_3_) {
        return aSlot == Container_VolumetricFlaskSetter.SLOT_OUTPUT;
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(final String customName) {
        this.customName = customName;
    }

    @Override
    public String getInventoryName() {
        return this.hasCustomInventoryName() ? this.customName : "container.VolumetricFlaskSetter";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return (this.customName != null) && !this.customName.equals("");
    }

    @Override
    public Packet getDescriptionPacket() {
        final NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
    }

    @Override
    public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
        final NBTTagCompound tag = pkt.func_148857_g();
        this.readFromNBT(tag);
    }

    public boolean onScrewdriverRightClick(byte side, EntityPlayer player, int x, int y, int z) {

        if (player.isSneaking()) {
            PlayerUtils.messagePlayer(player, "Value: " + this.getCustomValue());
        }

        try {
            if (aCurrentMode == 7) {
                aCurrentMode = 0;
            } else {
                aCurrentMode++;
            }
            PlayerUtils.messagePlayer(player, "Slot " + aCurrentMode + " is now default.");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    public void log(String aString) {
        Logger.INFO("[Flask-Tile] " + aString);
    }
}
