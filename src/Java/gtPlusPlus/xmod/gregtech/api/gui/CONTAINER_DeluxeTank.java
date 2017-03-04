package gtPlusPlus.xmod.gregtech.api.gui;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.*;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_DeluxeTank;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The Container I use for all my Basic Tanks
 */
public class CONTAINER_DeluxeTank extends GT_Container_BasicTank {

	public CONTAINER_DeluxeTank(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	@Override
	public void addSlots(final InventoryPlayer aInventoryPlayer) {
		this.addSlotToContainer(new Slot(this.mTileEntity, 0, 80, 17));
		this.addSlotToContainer(new GT_Slot_Output(this.mTileEntity, 1, 80, 53));
		this.addSlotToContainer(new GT_Slot_Render(this.mTileEntity, 2, 41, 42));
		this.addSlotToContainer(new GT_Slot_Render(this.mTileEntity, 3, 59, 42));
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (this.mTileEntity.isClientSide() || (this.mTileEntity.getMetaTileEntity() == null)) {
			return;
		}
		if (((GT_MetaTileEntity_DeluxeTank) this.mTileEntity.getMetaTileEntity()).mFluid != null) {
			this.mContent = ((GT_MetaTileEntity_DeluxeTank) this.mTileEntity.getMetaTileEntity()).mFluid.amount;
		} else {
			this.mContent = 0;
		}
		final Iterator var2 = this.crafters.iterator();
		while (var2.hasNext()) {
			final ICrafting var1 = (ICrafting) var2.next();
			var1.sendProgressBarUpdate(this, 100, this.mContent & 65535);
			var1.sendProgressBarUpdate(this, 101, this.mContent >>> 16);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(final int par1, final int par2) {
		super.updateProgressBar(par1, par2);
		switch (par1) {
		case 100:
			this.mContent = (this.mContent & -65536) | par2;
			break;
		case 101:
			this.mContent = (this.mContent & 65535) | (par2 << 16);
			break;
		}
	}

	@Override
	public int getSlotCount() {
		return 2;
	}

	@Override
	public int getShiftClickSlotCount() {
		return 1;
	}
}
