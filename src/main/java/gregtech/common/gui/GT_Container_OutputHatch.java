package gregtech.common.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_Container_BasicTank;
import gregtech.api.gui.GT_Slot_Output;
import gregtech.api.gui.GT_Slot_Render;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.nio.ByteBuffer;

public class GT_Container_OutputHatch extends GT_Container_BasicTank {

    private ByteBuffer buffer;
    private String fluidName = "";
    private byte mMode;

    public GT_Container_OutputHatch(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(mTileEntity, 0, 80, 17));
        addSlotToContainer(new GT_Slot_Output(mTileEntity, 1, 80, 53));
        addSlotToContainer(new GT_Slot_Render(mTileEntity, 2, 59, 42));
        addSlotToContainer(new GT_Slot_Render(mTileEntity, 3, 150, 42));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex == 3 && aMouseclick < 2) {
            GT_MetaTileEntity_Hatch_Output tHatch = (GT_MetaTileEntity_Hatch_Output) mTileEntity.getMetaTileEntity();
            FluidStack tReadyLockFluid = GT_Utility.getFluidForFilledItem(aPlayer.inventory.getItemStack(), true);
            byte tMode = tHatch.getMode();
            // If player click the locker slot with empty or the same fluid cell, clear the lock fluid
            if (tReadyLockFluid == null || (tMode >= 8 && tReadyLockFluid.getFluid().getName().equals(tHatch.getLockedFluidName()))) {
                tHatch.setLockedFluidName(null);
                GT_Utility.sendChatToPlayer(aPlayer, trans("300", "Fluid Lock Cleared."));
                tHatch.mMode = 0;
                fluidName = "";
            }
            else {
                tHatch.setLockedFluidName(tReadyLockFluid.getFluid().getName());
                GT_Utility.sendChatToPlayer(aPlayer, String.format(trans("151.4", "Sucessfully locked Fluid to %s"), tReadyLockFluid.getLocalizedName()));
                tHatch.mMode = 9;
                fluidName = tReadyLockFluid.getUnlocalizedName();
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public void addCraftingToCrafters(ICrafting clientHandle) {
        buffer.putInt(0, fluidName.length());
        buffer.put(Integer.BYTES, mMode);
        for (int i = 0; i < fluidName.length(); i++) {
            buffer.putChar(Integer.BYTES + Character.BYTES * i + 1, fluidName.charAt(i));
        }
        sendStateUpdate(clientHandle);
        super.addCraftingToCrafters(clientHandle);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (buffer == null) {
            buffer = ByteBuffer.allocate(256);
        }
        if(mTileEntity.isServerSide()) {
            GT_MetaTileEntity_Hatch_Output tile = (GT_MetaTileEntity_Hatch_Output) mTileEntity.getMetaTileEntity();
            if (tile == null) return;
            fluidName = tile.getLockedFluidName() == null ? "" : tile.getLockedFluidName();
            mMode = tile.getMode();
            buffer.putInt(0, fluidName.length());
            buffer.put(Integer.BYTES, mMode);
            for (int i = 0; i < fluidName.length(); i++) {
                buffer.putChar(Integer.BYTES + Character.BYTES * i + 1, fluidName.charAt(i));
            }
            for (Object clientHandle : this.crafters) {
                sendStateUpdate((ICrafting) clientHandle);
            }
        }
    }

    private void sendStateUpdate(ICrafting clientHandle) {
        final int bytes = Character.BYTES * fluidName.length() + Integer.BYTES + 1;
        for (int i = 0; i < bytes; i++) {
            clientHandle.sendProgressBarUpdate(this, i + 110, buffer.get(i));
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        super.updateProgressBar(index, value);
        index = index - 110;
        if(index >= 0 && index < buffer.capacity()) {
            buffer.put(index, (byte) value);
        }
    }

    @SideOnly(Side.CLIENT)
    public String getFluidName() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buffer.getInt(0); ++i) {
            sb.append(buffer.getChar(i * Character.BYTES + Integer.BYTES + 1));
        }
        byte mode = buffer.get(Integer.BYTES);
        FluidStack tFluid = FluidRegistry.getFluidStack(sb.toString(), 1);
        if (tFluid == null || mode < 8) return "Empty";
        return tFluid.getLocalizedName().replace("fluid.", "");
    }
}