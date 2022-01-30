package gtPlusPlus.xmod.gregtech.api.gui;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_MassFabricator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The Container I use for all my Basic Machines
 */
public class CONTAINER_MatterFab extends GT_ContainerMetaTile_Machine {
	
	public int mMatterProduced = 0;
	public int mScrapProduced = 0;
	public int mAmplifierProduced = 0;
	public int mScrapUsed = 0;
	public int mAmplifierUsed = 0;

	public CONTAINER_MatterFab(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public CONTAINER_MatterFab(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final boolean bindInventory) {
		super(aInventoryPlayer, aTileEntity, bindInventory);
	}

	@Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        //addSlotToContainer(new SlotNoInput(mTileEntity, 1, 152, 5));
    }

    @Override
    public int getSlotCount() {
        return 0;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 0;
    }
    
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) {
			return;
		}
		GregtechMetaTileEntity_MassFabricator aTile = ((GregtechMetaTileEntity_MassFabricator)this.mTileEntity.getMetaTileEntity());

		mAmplifierProduced = aTile.mAmplifierProduced;
		mAmplifierUsed = aTile.mAmplifierUsed;
		mMatterProduced = aTile.mMatterProduced;
		mScrapProduced = aTile.mScrapProduced;
		mScrapUsed = aTile.mScrapUsed;
		
		Iterator var2 = this.crafters.iterator();
		while (var2.hasNext()) {
			ICrafting var1 = (ICrafting) var2.next();
			var1.sendProgressBarUpdate(this, 201, mAmplifierProduced);
			var1.sendProgressBarUpdate(this, 202, mAmplifierUsed);
			var1.sendProgressBarUpdate(this, 203, mMatterProduced);
			var1.sendProgressBarUpdate(this, 204, mScrapProduced);
			var1.sendProgressBarUpdate(this, 205, mScrapUsed);
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting) {
		super.addCraftingToCrafters(par1ICrafting);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		super.updateProgressBar(par1, par2);
		switch (par1) {			
			case 201:
				mAmplifierProduced = (par2);
				break;		
			case 202:
				mAmplifierUsed = (par2);
				break;		
			case 203:
				mMatterProduced = (par2);
				break;		
			case 204:
				mScrapProduced = (par2);
				break;		
			case 205:
				mScrapUsed = (par2);
				break;
		}
	}
	
}
