package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.CONTAINER_1by1_Turbine;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.GUI_1by1_Turbine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class GT_MetaTileEntity_Hatch_TurbineProvider extends GT_MetaTileEntity_Hatch_InputBus {

	public GT_MetaTileEntity_Hatch_TurbineProvider(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier);
	}
	
	public GT_MetaTileEntity_Hatch_TurbineProvider(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}
	
	public GT_MetaTileEntity_Hatch_TurbineProvider(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription[0], aTextures);
	}

	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_TurbineProvider(this.mName, this.mTier, this.mDescription, this.mTextures);
	}
	
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_1by1_Turbine(aPlayerInventory, aBaseMetaTileEntity);
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_1by1_Turbine(aPlayerInventory, aBaseMetaTileEntity, "Turbine Housing");		
	}
	
	@Override
	public String[] getDescription() {					
		return new String[]{				
				"An automation port for Large Turbines", "Will attempt once per 1200 ticks to fill the turbine slot of it's parent turbine", "You may adjust this with a screwdriver", "Hold shift to adjust in finer amounts", "Hold control to adjust direction", "Left Click with Screwdriver to reset", "This module assumes the entire turbine is in the same Chunk"};
	}
	
	
	private GT_MetaTileEntity_LargeTurbine mParent = null;

	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
		super.onPostTick(aBaseMetaTileEntity, aTimer);
		if (aTimer % mRefreshTime == 0 && this.getBaseMetaTileEntity().isServerSide()) {
			tryRefillTurbine();
		}
	}

	private final void tryFindParentTurbine() {
		Logger.INFO("This turbine housing has no parent, searching world.");
		IGregTechTileEntity T = this.getBaseMetaTileEntity();
		World W = T.getWorld();
		Chunk C = W.getChunkFromBlockCoords(T.getXCoord(), T.getZCoord());
		for (Object o : C.chunkTileEntityMap.values()) {			
			if (o instanceof IGregTechTileEntity) {
				IGregTechTileEntity G = (IGregTechTileEntity) o;
				final IMetaTileEntity aMetaTileEntity = G.getMetaTileEntity();
				if (aMetaTileEntity == null) {
					continue;
				}		
				if (aMetaTileEntity instanceof GT_MetaTileEntity_LargeTurbine) {
					GT_MetaTileEntity_LargeTurbine aTurb = (GT_MetaTileEntity_LargeTurbine) aMetaTileEntity;
					for (GT_MetaTileEntity_Hatch_InputBus ee : aTurb.mInputBusses) {
						if (ee.equals(this)) {
							mParent = aTurb;
							Logger.INFO("Found a Parent to attach to this housing.");
							return;
						}
					}
				}
			}			
		}		
	}
	
	private final void tryRefillTurbine() {
		if (mParent == null) {
			tryFindParentTurbine();			
		}
		if (mParent != null && mParent.mInventory[1] == null) {
			for (ItemStack aStack : this.mInventory) {					
				if (isItemStackTurbine(aStack)) {
					setGUIItemStack(aStack);	
				}
			}	
		}
	}
	
	protected boolean setGUIItemStack(ItemStack aNewGuiSlotContents) {
		boolean result = false;
		if (mParent.mInventory[1] == null) {
			mParent.mInventory[1] = aNewGuiSlotContents != null ? aNewGuiSlotContents.copy() : null;
			mParent.depleteInput(aNewGuiSlotContents);
			mParent.updateSlots();
			this.updateSlots();
			result = true;
		}	
		return result;
	}

	public boolean isItemStackTurbine(ItemStack aStack) {
		if (aStack.getItem() instanceof GT_MetaGenerated_Tool) {
			if (aStack.getItemDamage() >= 170 && aStack.getItemDamage() <= 176) {
				return true;						
			}    			
		}
		return false;
	}

	public boolean isItemStackScrewdriver(ItemStack aStack) {
		if (aStack.getItem() instanceof GT_MetaGenerated_Tool) {
			if (aStack.getItemDamage() == 22 || aStack.getItemDamage() == 150) {
				return true;						
			}    			
		}
		return false;
	}
	
	
	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return isItemStackTurbine(aStack);
	}
	
	private int mRefreshTime = 1200;
	private boolean mDescending = true;

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setInteger("mRefreshTime", mRefreshTime);
		aNBT.setBoolean("mDescending", mDescending);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		mRefreshTime = aNBT.getInteger("mRefreshTime");
		mDescending = aNBT.getBoolean("mDescending");
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (aPlayer != null) {
			if (KeyboardUtils.isCtrlKeyDown()) {
				mDescending = Utils.invertBoolean(mDescending);
				PlayerUtils.messagePlayer(aPlayer, "Direction: "+(mDescending ? "DOWN" : "UP"));
			}
			else {
				int aAmount = 0;
				if (KeyboardUtils.isShiftKeyDown()) {
					aAmount = 10;
				}
				else {
					aAmount = 100;
				}
				if (mDescending) {
					mRefreshTime -= aAmount;
					if (mRefreshTime < 0) {
						mRefreshTime = 1200;
					}
				}
				else {
					mRefreshTime += aAmount;
					if (mRefreshTime > 1200) {
						mRefreshTime = 0;
					}					
				}
				PlayerUtils.messagePlayer(aPlayer, "Set check time to be every "+mRefreshTime+" ticks.");
			}			
		}		
	}

	@Override
	public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		boolean aDidScrewdriver = false;
		if (aPlayer != null) {
			if (aPlayer.getHeldItem() != null) {
				if (isItemStackScrewdriver(aPlayer.getHeldItem())) {
					aDidScrewdriver = true;
					mRefreshTime = 1200;
					PlayerUtils.messagePlayer(aPlayer, "Reset check time to "+mRefreshTime+" ticks.");
				}
			}
		}
		if (!aDidScrewdriver) {
			super.onLeftclick(aBaseMetaTileEntity, aPlayer);
		}
	}
	

}
