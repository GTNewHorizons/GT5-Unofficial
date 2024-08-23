package gtPlusPlus.core.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.inventories.InventoryPestKiller;
import gtPlusPlus.core.slots.SlotGeneric;
import gtPlusPlus.core.slots.SlotNoInput;
import gtPlusPlus.core.tileentities.machines.TileEntityPestKiller;

public class Container_PestKiller extends Container {

    public TileEntityPestKiller tile_entity;
    public final InventoryPestKiller inventoryChest;

    private final World worldObj;
    private final int posX;
    private final int posY;
    private final int posZ;

    public static int StorageSlotNumber = 3; // Number of slots in storage area
    public static int InventorySlotNumber = 36; // Inventory Slots (Inventory
    // and Hotbar)
    public static int FullSlotNumber = InventorySlotNumber + StorageSlotNumber; // All
    // slots

    public Container_PestKiller(final InventoryPlayer inventory, final TileEntityPestKiller te) {
        this.tile_entity = te;
        this.inventoryChest = te.getInventory();
        te.openInventory();
        int var6;
        int var7;
        this.worldObj = te.getWorldObj();
        this.posX = te.xCoord;
        this.posY = te.yCoord;
        this.posZ = te.zCoord;
        int o = 0;

        int aSlotX = 134;

        this.addSlotToContainer(new SlotGeneric(this.inventoryChest, o++, aSlotX, 10));
        this.addSlotToContainer(new SlotNoInput(this.inventoryChest, o++, aSlotX, 60));
        addSlotToContainer(new GT_Slot_Render(tile_entity, o++, aSlotX, 35));

        // Player Inventory
        for (var6 = 0; var6 < 3; ++var6) {
            for (var7 = 0; var7 < 9; ++var7) {
                this.addSlotToContainer(new Slot(inventory, var7 + (var6 * 9) + 9, 8 + (var7 * 18), 84 + (var6 * 18)));
            }
        }

        // Player Hotbar
        for (var6 = 0; var6 < 9; ++var6) {
            this.addSlotToContainer(new Slot(inventory, var6, 8 + (var6 * 18), 142));
        }
    }

    public FluidStack getFluidOfStoredTank() {
        if (tile_entity != null) {
            if (tile_entity.getTank() != null) {
                return tile_entity.getTank()
                    .getFluid();
            }
        }
        return null;
    }

    public int getFluidStoredAmount() {
        FluidStack f = getFluidOfStoredTank();
        return f == null ? 0 : f.amount;
    }

    @Override
    public ItemStack slotClick(final int aSlotIndex, final int aMouseclick, final int aShifthold,
        final EntityPlayer aPlayer) {
        boolean fluid = false;
        if (aSlotIndex == 2) {
            fluid = true;
        }
        if (!fluid) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        } else {
            return null;
        }
    }

    @Override
    public void onContainerClosed(final EntityPlayer par1EntityPlayer) {
        super.onContainerClosed(par1EntityPlayer);
        tile_entity.closeInventory();
    }

    @Override
    public boolean canInteractWith(final EntityPlayer par1EntityPlayer) {
        if (this.worldObj.getBlock(this.posX, this.posY, this.posZ) != ModBlocks.blockPestKiller) {
            return false;
        }
        return par1EntityPlayer.getDistanceSq(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) <= 64D;
    }

    @Override
    public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2) {
        ItemStack var3 = null;
        final Slot var4 = (Slot) this.inventorySlots.get(par2);

        if ((var4 != null) && var4.getHasStack()) {
            final ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            /*
             * if (par2 == 0) { if (!this.mergeItemStack(var5, InOutputSlotNumber, FullSlotNumber, true)) { return null;
             * } var4.onSlotChange(var5, var3); } else if (par2 >= InOutputSlotNumber && par2 < InventoryOutSlotNumber)
             * { if (!this.mergeItemStack(var5, InventoryOutSlotNumber, FullSlotNumber, false)) { return null; } } else
             * if (par2 >= InventoryOutSlotNumber && par2 < FullSlotNumber) { if (!this.mergeItemStack(var5,
             * InOutputSlotNumber, InventoryOutSlotNumber, false)) { return null; } } else if
             * (!this.mergeItemStack(var5, InOutputSlotNumber, FullSlotNumber, false)) { return null; }
             */

            if (var5.stackSize == 0) {
                var4.putStack((ItemStack) null);
            } else {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize) {
                return null;
            }

            var4.onPickupFromSlot(par1EntityPlayer, var5);
        }

        return var3;
    }

    // Can merge Slot
    @Override
    public boolean func_94530_a(final ItemStack p_94530_1_, final Slot p_94530_2_) {
        return super.func_94530_a(p_94530_1_, p_94530_2_);
    }

    private class GT_Slot_Render extends Slot {

        public final int mSlotIndex;
        public boolean mEnabled = true;
        public boolean mCanInsertItem, mCanStackItem;
        public int mMaxStacksize = 127;

        public GT_Slot_Render(IInventory inventory, int slotIndex, int xPos, int yPos) {
            this(inventory, slotIndex, xPos, yPos, false, false, 0);
        }

        public GT_Slot_Render(IInventory inventory, int slotIndex, int xPos, int yPos, boolean aCanInsertItem,
            boolean aCanStackItem, int aMaxStacksize) {
            super(inventory, slotIndex, xPos, yPos);
            mCanInsertItem = aCanInsertItem;
            mCanStackItem = aCanStackItem;
            mMaxStacksize = aMaxStacksize;
            mSlotIndex = slotIndex;
        }

        @Override
        public boolean isItemValid(ItemStack itemStack) {
            return mCanInsertItem;
        }

        @Override
        public int getSlotStackLimit() {
            return mMaxStacksize;
        }

        @Override
        public boolean getHasStack() {
            return false;
        }

        @Override
        public ItemStack decrStackSize(int amount) {
            if (!mCanStackItem) return null;
            return super.decrStackSize(amount);
        }

        @Override
        public boolean canTakeStack(EntityPlayer player) {
            return false;
        }

        /**
         * Sets if this slot should be ignored in event-processing. For example, highlight the slot on mouseOver.
         *
         * @param enabled if the slot should be enabled
         */
        public void setEnabled(boolean enabled) {
            mEnabled = enabled;
        }

        /**
         * Use this value to determine whether to ignore this slot in event processing
         */
        public boolean isEnabled() {
            return mEnabled;
        }

        /**
         * This function controls whether to highlight the slot on mouseOver.
         */
        @Override
        @SideOnly(Side.CLIENT)
        public boolean func_111238_b() {
            return isEnabled();
        }

        /**
         * NEI has a nice and "useful" Delete-All Function, which would delete the Content of this Slot. This is here to
         * prevent that.
         */
        @Override
        public void putStack(ItemStack aStack) {
            if (inventory instanceof TileEntity && ((TileEntity) inventory).getWorldObj().isRemote) {
                inventory.setInventorySlotContents(getSlotIndex(), aStack);
            }
            onSlotChanged();
        }
    }
}
