package gtPlusPlus.xmod.gregtech.api.gui.fluidreactor;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_Container_BasicMachine;
import gregtech.api.gui.GT_Slot_Render;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.slots.SlotChemicalPlantInput;
import gtPlusPlus.core.slots.SlotNoInput;
import gtPlusPlus.core.slots.SlotNoInputLogging;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntity_ChemicalReactor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;

public class Container_FluidReactor extends GT_Container_BasicMachine {

	public boolean mFluidTransfer_1 = false;
	public boolean mFluidTransfer_2 = false;
	public boolean oFluidTransfer_1 = false;
	public boolean oFluidTransfer_2 = false;

	public Container_FluidReactor(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	@Override
	public void addSlots(InventoryPlayer aInventoryPlayer) {		

		// Gui Buttons
		this.addSlotToContainer(new SlotNoInput(this.mTileEntity, 0, 8, 63)); // Fluid 1
		this.addSlotToContainer(new SlotNoInput(this.mTileEntity, 1, 44, 63));	
		this.addSlotToContainer(new SlotNoInput(this.mTileEntity, 2, 26, 63));	// Fluid 2

		int tStartIndex = 3;
		// Input Slots
		this.addSlotToContainer(new SlotChemicalPlantInput(this.mTileEntity, tStartIndex++, 8, 7));
		this.addSlotToContainer(new SlotChemicalPlantInput(this.mTileEntity, tStartIndex++, 26, 7));
		this.addSlotToContainer(new SlotChemicalPlantInput(this.mTileEntity, tStartIndex++, 44, 7));
		this.addSlotToContainer(new SlotChemicalPlantInput(this.mTileEntity, tStartIndex++, 62, 7));		

		// Output Slots
		this.addSlotToContainer(new SlotNoInput(this.mTileEntity, tStartIndex++, 107, 16));
		this.addSlotToContainer(new SlotNoInput(this.mTileEntity, tStartIndex++, 125, 16));
		this.addSlotToContainer(new SlotNoInput(this.mTileEntity, tStartIndex++, 107, 34));
		this.addSlotToContainer(new SlotNoInput(this.mTileEntity, tStartIndex++, 125, 34));	

		// Cell Collector Slot
		this.addSlotToContainer(new SlotNoInput(this.mTileEntity, tStartIndex++, 116, 63));	


		// Inputs Fluids
		this.addSlotToContainer(new GT_Slot_Render(this.mTileEntity, tStartIndex++, 8, 42));
		this.addSlotToContainer(new GT_Slot_Render(this.mTileEntity, tStartIndex++, 26, 42));
		this.addSlotToContainer(new GT_Slot_Render(this.mTileEntity, tStartIndex++, 44, 42));
		this.addSlotToContainer(new GT_Slot_Render(this.mTileEntity, tStartIndex++, 62, 42));

		// Output Fluids
		this.addSlotToContainer(new GT_Slot_Render(this.mTileEntity, tStartIndex++, 143, 16));
		this.addSlotToContainer(new GT_Slot_Render(this.mTileEntity, tStartIndex++, 143, 34));




	}

	public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
		if (aSlotIndex == 0 || aSlotIndex == 2) {
			if (this.mTileEntity != null && this.mTileEntity.isServerSide()) {
				try {
					final IMetaTileEntity aMetaTileEntity = this.mTileEntity.getMetaTileEntity();
					if (aMetaTileEntity == null) {
						return null;
					}
					if (aMetaTileEntity instanceof GregtechMetaTileEntity_ChemicalReactor) {
						//Set Tile
						if (aSlotIndex == 0) {
							((GregtechMetaTileEntity_ChemicalReactor)aMetaTileEntity).mFluidTransfer_1 = !((GregtechMetaTileEntity_ChemicalReactor)aMetaTileEntity).mFluidTransfer_1;
						}
						else if (aSlotIndex == 2) {
							((GregtechMetaTileEntity_ChemicalReactor)aMetaTileEntity).mFluidTransfer_2 = !((GregtechMetaTileEntity_ChemicalReactor)aMetaTileEntity).mFluidTransfer_2;
						}
						return null;						
					}
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}	
		}
		//Logger.INFO("Clicked slot "+aSlotIndex);
		return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);			
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (this.mTileEntity != null && this.mTileEntity.isServerSide()) {
			try {
				Iterator var2 = this.crafters.iterator();
				final IMetaTileEntity aMetaTileEntity = this.mTileEntity.getMetaTileEntity();
				if (aMetaTileEntity == null) {
					Logger.INFO("bad");
					return;
				}
				if (aMetaTileEntity instanceof GregtechMetaTileEntity_ChemicalReactor) {
					//Read from Tile
					this.mFluidTransfer_1 = ((GregtechMetaTileEntity_ChemicalReactor)aMetaTileEntity).mFluidTransfer_1;
					this.mFluidTransfer_2 = ((GregtechMetaTileEntity_ChemicalReactor)aMetaTileEntity).mFluidTransfer_2;	
					int mTimer;
					mTimer = (int) ReflectionUtils.getField(this.getClass(), "mTimer").get(this);
					while (true) {
						ICrafting var1;
						do {
							if (!var2.hasNext()) {
								this.oFluidTransfer_1 = this.mFluidTransfer_1;
								this.oFluidTransfer_2 = this.mFluidTransfer_2;
								return;
							}
							var1 = (ICrafting) var2.next();
							if (mTimer % 500 == 10 || this.oFluidTransfer_1 != this.mFluidTransfer_1) {
								var1.sendProgressBarUpdate(this, -50, this.mFluidTransfer_1 ? 1 : 0);
							}
							if (mTimer % 500 == 10 || this.oFluidTransfer_2 != this.mFluidTransfer_2) {
								var1.sendProgressBarUpdate(this, -51, this.mFluidTransfer_2 ? 1 : 0);
							}
						} while (mTimer % 500 != 10);
					}
				}
				else {
					Logger.INFO("bad cast");					
				}
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}	

	}

	public void addCraftingToCrafters(ICrafting par1ICrafting) {
		super.addCraftingToCrafters(par1ICrafting);
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 > 0) {
			super.updateProgressBar(par1, par2);			
		}
		else {
			switch (par1) {
				case -50 :
					this.mFluidTransfer_1 = par2 != 0;
					break;
				case -51 :
					this.mFluidTransfer_2 = par2 != 0;
					break;
				default :
					break;
			}
		}		
	}

	public int getSlotStartIndex() {
		return 3;
	}

	public int getShiftClickStartIndex() {
		return 3;
	}

	public int getSlotCount() {
		return this.getShiftClickSlotCount() + 5 + 2;
	}

	public int getShiftClickSlotCount() {
		return 4;
	}
}