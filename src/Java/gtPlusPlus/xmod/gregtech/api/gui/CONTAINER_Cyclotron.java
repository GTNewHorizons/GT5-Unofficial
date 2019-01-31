package gtPlusPlus.xmod.gregtech.api.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_LanguageManager;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

public class CONTAINER_Cyclotron extends GT_ContainerMetaTile_Machine {
	
	public int mActive = 0;
	public int mMaxProgressTime = 0;
	public int mProgressTime = 0;
	public long mEnergy = 0;
	public int mSteam = 0;
	public int mSteamStorage = 0;
	public int mStorage = 0;
	public int mOutput = 0;
	public int mInput = 0;
	public int mID = 0;
	public int mDisplayErrorCode = 0;
	private int oActive = 0;
	private int oMaxProgressTime = 0;
	private int oProgressTime = 0;
	private long oEnergy = 0;
	private int oSteam = 0;
	private int oSteamStorage = 0;
	private int oStorage = 0;
	private int oOutput = 0;
	private int oInput = 0;
	private int oID = 0;
	private int oDisplayErrorCode = 0;
	private int mTimer = 0;

	public CONTAINER_Cyclotron(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
		this.mTileEntity = aTileEntity;
		if (this.mTileEntity != null && this.mTileEntity.getMetaTileEntity() != null) {
			this.addSlots(aInventoryPlayer);
			if (this.doesBindPlayerInventory()) {
				this.bindPlayerInventory(aInventoryPlayer);
			}

			this.detectAndSendChanges();
		} else {
			aInventoryPlayer.player.openContainer = aInventoryPlayer.player.inventoryContainer;
		}

	}

	public CONTAINER_Cyclotron(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity,
			boolean doesBindInventory) {
		super(aInventoryPlayer, aTileEntity);
		this.mTileEntity = aTileEntity;
		if (this.mTileEntity != null && this.mTileEntity.getMetaTileEntity() != null) {
			this.addSlots(aInventoryPlayer);
			if (this.doesBindPlayerInventory() && doesBindInventory) {
				this.bindPlayerInventory(aInventoryPlayer);
			}

			this.detectAndSendChanges();
		} else {
			aInventoryPlayer.player.openContainer = aInventoryPlayer.player.inventoryContainer;
		}

	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!this.mTileEntity.isClientSide() && this.mTileEntity.getMetaTileEntity() != null) {
			this.mStorage = (int) Math.min(2147483647L, this.mTileEntity.getEUCapacity());
			this.mEnergy = this.mTileEntity.getStoredEU();
			this.mSteamStorage = (int) Math.min(2147483647L, this.mTileEntity.getSteamCapacity());
			this.mSteam = (int) Math.min(2147483647L, this.mTileEntity.getStoredSteam());
			this.mOutput = (int) Math.min(2147483647L, this.mTileEntity.getOutputVoltage());
			this.mInput = (int) Math.min(2147483647L, this.mTileEntity.getInputVoltage());
			this.mDisplayErrorCode = this.mTileEntity.getErrorDisplayID();
			this.mProgressTime = this.mTileEntity.getProgress();
			this.mMaxProgressTime = this.mTileEntity.getMaxProgress();
			this.mActive = this.mTileEntity.isActive() ? 1 : 0;
			++this.mTimer;
			Iterator var2 = this.crafters.iterator();

			while (true) {
				ICrafting var1;
				do {
					if (!var2.hasNext()) {
						this.oID = this.mID;
						this.oSteam = this.mSteam;
						this.oInput = this.mInput;
						this.oActive = this.mActive;
						this.oOutput = this.mOutput;
						this.oEnergy = this.mEnergy;
						this.oStorage = this.mStorage;
						this.oSteamStorage = this.mSteamStorage;
						this.oProgressTime = this.mProgressTime;
						this.oMaxProgressTime = this.mMaxProgressTime;
						this.oDisplayErrorCode = this.mDisplayErrorCode;
						return;
					}

					var1 = (ICrafting) var2.next();
					if (this.mTimer % 500 == 10 || this.oEnergy != this.mEnergy) {
						
						int uEnergy = (int) Math.min(2147483647L, this.mTileEntity.getStoredEU());
						
						var1.sendProgressBarUpdate(this, 0, uEnergy & '￿');
						var1.sendProgressBarUpdate(this, 1, uEnergy >>> 16);
					}

					if (this.mTimer % 500 == 10 || this.oStorage != this.mStorage) {
						var1.sendProgressBarUpdate(this, 2, this.mStorage & '￿');
						var1.sendProgressBarUpdate(this, 3, this.mStorage >>> 16);
					}

					if (this.mTimer % 500 == 10 || this.oOutput != this.mOutput) {
						var1.sendProgressBarUpdate(this, 4, this.mOutput);
					}

					if (this.mTimer % 500 == 10 || this.oInput != this.mInput) {
						var1.sendProgressBarUpdate(this, 5, this.mInput);
					}

					if (this.mTimer % 500 == 10 || this.oDisplayErrorCode != this.mDisplayErrorCode) {
						var1.sendProgressBarUpdate(this, 6, this.mDisplayErrorCode);
					}

					if (this.mTimer % 500 == 10 || this.oProgressTime != this.mProgressTime) {
						var1.sendProgressBarUpdate(this, 11, this.mProgressTime & '￿');
						var1.sendProgressBarUpdate(this, 12, this.mProgressTime >>> 16);
					}

					if (this.mTimer % 500 == 10 || this.oMaxProgressTime != this.mMaxProgressTime) {
						var1.sendProgressBarUpdate(this, 13, this.mMaxProgressTime & '￿');
						var1.sendProgressBarUpdate(this, 14, this.mMaxProgressTime >>> 16);
					}

					if (this.mTimer % 500 == 10 || this.oID != this.mID) {
						var1.sendProgressBarUpdate(this, 15, this.mID);
					}

					if (this.mTimer % 500 == 10 || this.oActive != this.mActive) {
						var1.sendProgressBarUpdate(this, 16, this.mActive);
					}

					if (this.mTimer % 500 == 10 || this.oSteam != this.mSteam) {
						var1.sendProgressBarUpdate(this, 17, this.mSteam & '￿');
						var1.sendProgressBarUpdate(this, 18, this.mSteam >>> 16);
					}
				} while (this.mTimer % 500 != 10 && this.oSteamStorage == this.mSteamStorage);

				var1.sendProgressBarUpdate(this, 19, this.mSteamStorage & '￿');
				var1.sendProgressBarUpdate(this, 20, this.mSteamStorage >>> 16);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		super.updateProgressBar(par1, par2);
		switch (par1) {
			case 0 :
				this.mEnergy = this.mEnergy & -65536 | par2;
				break;
			case 1 :
				this.mEnergy = this.mEnergy & '￿' | par2 << 16;
				break;
			case 2 :
				this.mStorage = this.mStorage & -65536 | par2;
				break;
			case 3 :
				this.mStorage = this.mStorage & '￿' | par2 << 16;
				break;
			case 4 :
				this.mOutput = par2;
				break;
			case 5 :
				this.mInput = par2;
				break;
			case 6 :
				this.mDisplayErrorCode = par2;
			case 7 :
			case 8 :
			case 9 :
			case 10 :
			default :
				break;
			case 11 :
				this.mProgressTime = this.mProgressTime & -65536 | par2;
				break;
			case 12 :
				this.mProgressTime = this.mProgressTime & '￿' | par2 << 16;
				break;
			case 13 :
				this.mMaxProgressTime = this.mMaxProgressTime & -65536 | par2;
				break;
			case 14 :
				this.mMaxProgressTime = this.mMaxProgressTime & '￿' | par2 << 16;
				break;
			case 15 :
				this.mID = par2;
				break;
			case 16 :
				this.mActive = par2;
				break;
			case 17 :
				this.mSteam = this.mSteam & -65536 | par2;
				break;
			case 18 :
				this.mSteam = this.mSteam & '￿' | par2 << 16;
				break;
			case 19 :
				this.mSteamStorage = this.mSteamStorage & -65536 | par2;
				break;
			case 20 :
				this.mSteamStorage = this.mSteamStorage & '￿' | par2 << 16;
		}

	}

	public boolean canInteractWith(EntityPlayer player) {
		return this.mTileEntity.isUseableByPlayer(player);
	}

	public String trans(String aKey, String aEnglish) {
		return GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_" + aKey, aEnglish, false);
	}
}        