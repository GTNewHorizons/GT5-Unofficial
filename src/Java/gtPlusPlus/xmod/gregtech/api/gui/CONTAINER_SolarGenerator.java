package gtPlusPlus.xmod.gregtech.api.gui;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GregtechMetaTileEntitySolarGenerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

public class CONTAINER_SolarGenerator extends GT_ContainerMetaTile_Machine {
	private final int	mSteamCapacity;
	public int			mWaterAmount		= 0;
	public int			mSteamAmount		= 0;
	public int			mProcessingEnergy	= 0;
	public int			mTemperature		= 2;

	public CONTAINER_SolarGenerator(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity,
			final int aSteamCapacity) {
		super(aInventoryPlayer, aTileEntity);
		this.mSteamCapacity = aSteamCapacity;
	}

	@Override
	public void addSlots(final InventoryPlayer aInventoryPlayer) {
		// addSlotToContainer(new Slot(this.mTileEntity, 2, 116, 62));
		// addSlotToContainer(new Slot(this.mTileEntity, 0, 44, 26));
		// addSlotToContainer(new Slot(this.mTileEntity, 1, 44, 62));
		// addSlotToContainer(new Slot(this.mTileEntity, 3, 116, 26));
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (this.mTileEntity.isClientSide() || this.mTileEntity.getMetaTileEntity() == null) {
			return;
		}
		this.mTemperature = ((GregtechMetaTileEntitySolarGenerator) this.mTileEntity.getMetaTileEntity()).mSolarCharge;
		this.mProcessingEnergy = ((GregtechMetaTileEntitySolarGenerator) this.mTileEntity
				.getMetaTileEntity()).mProcessingEnergy;

		this.mTemperature = Math.min(54, Math.max(0, this.mTemperature * 54
				/ (((GregtechMetaTileEntitySolarGenerator) this.mTileEntity.getMetaTileEntity()).maxProgresstime()
						- 10)));
		this.mEnergy = (int) Math.min(Integer.MAX_VALUE, this.mTileEntity.getStoredEU());
		this.mProcessingEnergy = Math.min(14,
				Math.max(this.mProcessingEnergy > 0 ? 1 : 0, this.mProcessingEnergy * 14 / 1000));

		final Iterator var2 = this.crafters.iterator();
		while (var2.hasNext()) {
			final ICrafting var1 = (ICrafting) var2.next();
			var1.sendProgressBarUpdate(this, 100, this.mTemperature);
			var1.sendProgressBarUpdate(this, 101, this.mProcessingEnergy);
			var1.sendProgressBarUpdate(this, 102, this.mSteamAmount);
			var1.sendProgressBarUpdate(this, 103, this.mWaterAmount);
		}
	}

	@Override
	public int getShiftClickSlotCount() {
		return 0;
	}

	@Override
	public int getSlotCount() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(final int par1, final int par2) {
		super.updateProgressBar(par1, par2);
		switch (par1) {
			case 100:
				this.mTemperature = par2;
				break;
			case 101:
				this.mProcessingEnergy = par2;
				break;
			case 102:
				this.mSteamAmount = par2;
				break;
			case 103:
				this.mWaterAmount = par2;
		}
	}
}
