package gtPlusPlus.xmod.gregtech.api.gui;

import java.lang.reflect.Field;
import java.util.List;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The Container I use for all my Basic Machines
 */
public class CONTAINER_MultiMachine extends GT_ContainerMetaTile_Machine {

	public String[] mTileDescription;
	private String[] oTileDescription;


	public CONTAINER_MultiMachine(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public CONTAINER_MultiMachine(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final boolean bindInventory) {
		super(aInventoryPlayer, aTileEntity, bindInventory);
	}


	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (this.mTileEntity.isClientSide() || this.mTileEntity.getMetaTileEntity() == null) {
			return;
		}

		try {
			this.mTileDescription = this.mTileEntity.getInfoData();

			Field bTimer = ReflectionUtils.getField(getClass(), "mTimer");
			Field bCrafters = ReflectionUtils.getField(getClass(), "crafters");
			int time = bTimer.getInt(this);
			List crafters = (List) bCrafters.get(this);
			AutoMap<ICrafting> aCrafting = new AutoMap<ICrafting>();
			if (crafters != null && !crafters.isEmpty()) {
				for (Object o : crafters) {
					aCrafting.put((ICrafting) o);
				}
			}

			for (final ICrafting var3 : aCrafting) {
				if (time % 500 == 10 || this.oTileDescription != this.mTileDescription) {
					var3.sendProgressBarUpdate((Container)this, 64, 0);
				}
			}

			this.oTileDescription = this.mTileDescription;
		}
		catch (Throwable t) {

		}
	}

}

/*@Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(mTileEntity, 1, 152, 5));
    }

    @Override
    public int getSlotCount() {
        return 1;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 0;
    }
}*/
