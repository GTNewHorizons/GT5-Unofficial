package gtPlusPlus.xmod.gregtech.api.gui;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Output;
import gregtech.api.gui.GT_Slot_Render;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gtPlusPlus.core.slots.SlotLockedInput;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_TieredChest;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving.GT4Entity_Shelf_Large;

public class CONTAINER_SuperChest extends GT_ContainerMetaTile_Machine {
	
	public int mContent = 0;
	private ItemStack mLockedSlotStack = null;

	public CONTAINER_SuperChest(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public void addSlots(InventoryPlayer aInventoryPlayer) {
		this.addSlotToContainer(new SlotLockedInput(this.mTileEntity, 0, 80, 17, mLockedSlotStack));
		this.addSlotToContainer(new GT_Slot_Output(this.mTileEntity, 1, 80, 53));
		this.addSlotToContainer(new GT_Slot_Render(this.mTileEntity, 2, 59, 42));
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!this.mTileEntity.isClientSide() && this.mTileEntity.getMetaTileEntity() != null) {			
			if (this.mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_TieredChest) {
				this.mContent = ((GT_MetaTileEntity_TieredChest) this.mTileEntity.getMetaTileEntity()).mItemCount;
				mLockedSlotStack =  ((GT_MetaTileEntity_TieredChest) this.mTileEntity.getMetaTileEntity()).mItemStack;
			}
			if (this.mTileEntity.getMetaTileEntity() instanceof GT4Entity_Shelf_Large) {
				this.mContent = ((GT4Entity_Shelf_Large) this.mTileEntity.getMetaTileEntity()).mItemCount;
				mLockedSlotStack =  ((GT4Entity_Shelf_Large) this.mTileEntity.getMetaTileEntity()).mItemStack;
			}
			else {
				this.mContent = 0;
			}

			Iterator var2 = this.crafters.iterator();

			while (var2.hasNext()) {
				ICrafting var1 = (ICrafting) var2.next();
				var1.sendProgressBarUpdate(this, 100, this.mContent & 65535);
				var1.sendProgressBarUpdate(this, 101, this.mContent >>> 16);
			}
		}		
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		super.updateProgressBar(par1, par2);
		switch (par1) {
			case 100 :
				this.mContent = this.mContent & -65536 | par2;
				break;
			case 101 :
				this.mContent = this.mContent & 65535 | par2 << 16;
		}

	}

	public int getSlotCount() {
		return 2;
	}

	public int getShiftClickSlotCount() {
		return 1;
	}
}