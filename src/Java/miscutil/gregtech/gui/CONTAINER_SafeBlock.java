package miscutil.gregtech.gui;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GT_Utility;
import miscutil.core.util.Utils;
import miscutil.gregtech.metatileentity.implementations.GregtechMetaSafeBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CONTAINER_SafeBlock
        extends GT_ContainerMetaTile_Machine {
    public CONTAINER_SafeBlock(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }
    
    public String UUID = ((BaseMetaTileEntity)mTileEntity).getMetaTileEntity().getBaseMetaTileEntity().getOwnerName();
    public String ownerUUID = ((GregtechMetaSafeBlock)this.mTileEntity.getMetaTileEntity()).ownerUUID;
    public boolean blockStatus = ((GregtechMetaSafeBlock)this.mTileEntity.getMetaTileEntity()).bUnbreakable;

    @Override
	public void addSlots(InventoryPlayer aInventoryPlayer) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(this.mTileEntity, x + y * 9, 8 + x * 18, 5 + y * 18));
            }
        }
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 27, 8, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 27, 26, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 27, 44, 63, false, true, 1));
    }

    @Override
	public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex < 27) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        Slot tSlot = (Slot) this.inventorySlots.get(aSlotIndex);
        if (tSlot != null) {
            if (this.mTileEntity.getMetaTileEntity() == null) {
                return null;
            }
           /* if (aSlotIndex == 27) {
                ((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity()).bOutput = (!((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity()).bOutput);
                if (((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity()).bOutput) {
                    GT_Utility.sendChatToPlayer(aPlayer, "Emit Energy to Outputside");
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, "Don't emit Energy");
                }
                return null;
            }
            if (aSlotIndex == 28) {
                ((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull = (!((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull);
                if (((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull) {
                    GT_Utility.sendChatToPlayer(aPlayer, "Emit Redstone if no Slot is free");
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, "Don't emit Redstone");
                }
                return null;
            }*/
            if (aSlotIndex == 29) {
                ((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity()).bUnbreakable = (!((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity()).bUnbreakable);
                if (((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity()).bUnbreakable) {
                    GT_Utility.sendChatToPlayer(aPlayer, "Block is now unbreakable.");
                   // World world;
                    //world.markBlockForUpdate(x, y, z);
                    //this.updateProgressBar(par1, par2);
                    //PlayerCache.appendParamChanges(aPlayer.getDisplayName(), aPlayer.getUniqueID().toString());
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, "Block is now breakable.");
                    Utils.LOG_WARNING("Owner Name - From GT Values: "+UUID);
                }
                return null;
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
	public int getSlotCount() {
        return 27;
    }

    @Override
	public int getShiftClickSlotCount() {
        return 27;
    }
}
