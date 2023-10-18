package gtPlusPlus.core.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.inventories.Inventory_VolumetricFlaskSetter;
import gtPlusPlus.core.slots.SlotNoInput;
import gtPlusPlus.core.slots.SlotVolumetricFlask;
import gtPlusPlus.core.tileentities.general.TileEntityVolumetricFlaskSetter;
import gtPlusPlus.core.util.Utils;

public class Container_VolumetricFlaskSetter extends Container {

    public TileEntityVolumetricFlaskSetter mTileEntity;
    public final Inventory_VolumetricFlaskSetter inventoryChest;

    public int mCustomValue;
    private int oCustomValue;
    private int mTimer;

    private final World worldObj;
    private final int posX;
    private final int posY;
    private final int posZ;

    public static final int SLOT_OUTPUT = 8;

    public static int StorageSlotNumber = 8; // Number of slots in storage area
    public static int InventorySlotNumber = 36; // Inventory Slots (Inventory
    // and Hotbar)
    public static int FullSlotNumber = InventorySlotNumber + StorageSlotNumber; // All
    // slots

    public Container_VolumetricFlaskSetter(final InventoryPlayer inventory, final TileEntityVolumetricFlaskSetter te) {
        this.mTileEntity = te;
        this.inventoryChest = te.getInventory();

        int var6;
        int var7;
        this.worldObj = te.getWorldObj();
        this.posX = te.xCoord;
        this.posY = te.yCoord;
        this.posZ = te.zCoord;
        // mCustomValue = te.getCustomValue();

        int o = 0;

        // Storage Side
        /*
         * for (var6 = 0; var6 < 3; var6++) { for (var7 = 0; var7 < 5; var7++) { this.addSlotToContainer(new
         * SlotIntegratedCircuit(o, this.inventoryChest, o, 44 + (var7 * 18), 15 + (var6 * 18))); o++; } }
         */

        int xStart = 26;
        int yStart = 12;

        try {
            // 0
            this.addSlotToContainer(new SlotVolumetricFlask(this.inventoryChest, o++, xStart, yStart));
            this.addSlotToContainer(new SlotVolumetricFlask(this.inventoryChest, o++, xStart += 18, yStart));
            this.addSlotToContainer(new SlotVolumetricFlask(this.inventoryChest, o++, xStart += 18, yStart));
            this.addSlotToContainer(new SlotVolumetricFlask(this.inventoryChest, o++, xStart += 18, yStart));
            this.addSlotToContainer(new SlotVolumetricFlask(this.inventoryChest, o++, xStart += 18, yStart));
            this.addSlotToContainer(new SlotVolumetricFlask(this.inventoryChest, o++, xStart += 18, yStart));
            this.addSlotToContainer(new SlotVolumetricFlask(this.inventoryChest, o++, xStart += 18, yStart));
            this.addSlotToContainer(new SlotVolumetricFlask(this.inventoryChest, o++, xStart, yStart + 18));

            // Add Output
            this.addSlotToContainer(new SlotNoInput(this.inventoryChest, o++, 8 + (8 * 18), 59));
            o++;

            // Player Inventory
            for (var6 = 0; var6 < 3; ++var6) {
                for (var7 = 0; var7 < 9; ++var7) {
                    this.addSlotToContainer(
                            new Slot(inventory, var7 + (var6 * 9) + 9, 8 + (var7 * 18), 84 + (var6 * 18)));
                }
            }
            // Player Hotbar
            for (var6 = 0; var6 < 9; ++var6) {
                this.addSlotToContainer(new Slot(inventory, var6, 8 + (var6 * 18), 142));
            }
            detectAndSendChanges();
        } catch (Throwable t) {}
    }

    @Override
    public boolean canInteractWith(final EntityPlayer par1EntityPlayer) {
        if (this.worldObj.getBlock(this.posX, this.posY, this.posZ) != ModBlocks.blockVolumetricFlaskSetter) {
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

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if ((Utils.isClient()) || (this.mTileEntity == null)) {
            return;
        }

        mCustomValue = mTileEntity.getCustomValue();
        mTimer++;

        for (Object crafter : this.crafters) {
            ICrafting var1 = (ICrafting) crafter;
            if (mTimer % 20 == 10 || oCustomValue != mCustomValue) {
                var1.sendProgressBarUpdate(this, 0, mCustomValue);
            }
        }

        oCustomValue = mCustomValue;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 0 -> mCustomValue = (short) par2;
        }
    }

    public void log(String aString) {
        Logger.INFO("[Flask-Container] " + aString);
    }
}
