package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.CONTAINER_1by1_Turbine;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.GUI_1by1_Turbine;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.turbine.LargeTurbineTextureHandler;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GregtechMetaTileEntity_LargerTurbineBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_Hatch_Turbine extends GT_MetaTileEntity_Hatch {

	public boolean mHasController = false;
	public boolean mUsingAnimation = true;
	private String mControllerLocation;

	public GT_MetaTileEntity_Hatch_Turbine(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 16, "Turbine Rotor holder for XL Turbines");
	}

	public GT_MetaTileEntity_Hatch_Turbine(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 1, aDescription, aTextures);
	}

	public GT_MetaTileEntity_Hatch_Turbine(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 1, aDescription[0], aTextures);
	}

	@Override
	public ITexture[] getTexturesActive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, getFrontFacingTurbineTexture()};
	}

	@Override
	public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, getFrontFacingTurbineTexture()};
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public boolean isValidSlot(int aIndex) {
		return false;
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_Turbine(mName, mTier, StaticFields59.getDescriptionArray(this), mTextures);
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) return true;
		//aBaseMetaTileEntity.openGUI(aPlayer);
		PlayerUtils.messagePlayer(aPlayer, "[Turbine Assembly Data] Using Animations? "+usingAnimations());
		PlayerUtils.messagePlayer(aPlayer, "[Turbine Assembly Data] Has Controller? "+this.mHasController);
		if (mHasController) {
			PlayerUtils.messagePlayer(aPlayer, "[Turbine Assembly Data] Controller Location: "+BlockPos.generateBlockPos(mControllerLocation).getLocationString());
			PlayerUtils.messagePlayer(aPlayer, "[Turbine Assembly Data] Controller Active? "+this.isControllerActive());
		}
		PlayerUtils.messagePlayer(aPlayer, "[Turbine Assembly Data] Is Active? "+this.getBaseMetaTileEntity().isActive());
		return true;
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		switch (mTier) {
		default:
			return new CONTAINER_1by1_Turbine(aPlayerInventory, aBaseMetaTileEntity);
		}
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		switch (mTier) {
		default:
			return new GUI_1by1_Turbine(aPlayerInventory, aBaseMetaTileEntity, "Turbine Rotor Hatch");
		}
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setBoolean("mHasController", mHasController);
		aNBT.setBoolean("mUsingAnimation", mUsingAnimation);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		mHasController = aNBT.getBoolean("mHasController");
		mUsingAnimation = aNBT.getBoolean("mUsingAnimation");
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);		
		this.mUsingAnimation = Utils.invertBoolean(mUsingAnimation);
		if (this.mUsingAnimation) {
			PlayerUtils.messagePlayer(aPlayer, "Using Animated Turbine Texture.");
		}
		else {
			PlayerUtils.messagePlayer(aPlayer, "Using Static Turbine Texture.");			
		}	
		PlayerUtils.messagePlayer(aPlayer, "Has Controller: "+this.mHasController);
		if (mHasController) {
			PlayerUtils.messagePlayer(aPlayer, "Controller Location: "+this.mControllerLocation);
		}
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {		
		super.onPostTick(aBaseMetaTileEntity, aTick);
		if (this.mHasController) {		
			if (aTick % 20 == 0) {
				if (isControllerActive()) {
					this.getBaseMetaTileEntity().setActive(true);
				}
				else {
					this.getBaseMetaTileEntity().setActive(false);				
				}
			}	
		}
		else if (!this.mHasController && this.mControllerLocation != null) {
			//Weird Invalid State
			if (setController(BlockPos.generateBlockPos(mControllerLocation))) {
				//Valid
			}
		}
		else {
			//No Controller
		}
	}

	public boolean isControllerActive() {
		GregtechMetaTileEntity_LargerTurbineBase x = getController();
		if (x != null) {
			Logger.INFO("Checking Status of Controller.");
			return x.isMachineRunning();
		}
		Logger.INFO("Status of Controller failed, controller is null.");
		return false;
	}

	public GregtechMetaTileEntity_LargerTurbineBase getController() {
		if (this.mHasController && this.mControllerLocation != null && this.mControllerLocation.length() > 0) {
			BlockPos p = BlockPos.generateBlockPos(mControllerLocation);
			if (p != null) {
				//Logger.INFO(p.getLocationString());
				IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntity(p.xPos, p.yPos,
						p.zPos);
				if (tTileEntity != null && tTileEntity.getMetaTileEntity() instanceof GregtechMetaTileEntity_LargerTurbineBase) {
					return (GregtechMetaTileEntity_LargerTurbineBase) tTileEntity.getMetaTileEntity();
				}
				else {
					if (tTileEntity == null) {
						Logger.INFO("Controller MTE is null, somehow?");
					}
					else {
						Logger.INFO("Controller is a different MTE to expected");						
					}				
				}
			}
		}
		//Logger.INFO("Failed to Get Controller.");
		return null;
	}

	public boolean canSetNewController() {		
		if ((mControllerLocation != null && mControllerLocation.length() > 0) || this.mHasController) {
			return false;
		}		
		return true;
	}

	public boolean setController(BlockPos aPos) {	
		clearController();	
		if (canSetNewController()) {
			mControllerLocation = aPos.getUniqueIdentifier();
			mHasController = true;	
			Logger.INFO("Successfully injected controller into this Turbine Assembly Hatch.");
		}
		return mHasController;
	}

	public void clearController() {
		this.mControllerLocation = null;
		this.mHasController = false;
	}

	public boolean usingAnimations() {
		return mUsingAnimation;
	}

	private ITexture getFrontFacingTurbineTexture() {
		if (!mHasController) {						
			return this.getBaseMetaTileEntity().isActive() ? new GT_RenderedTexture(LargeTurbineTextureHandler.frontFaceHPActive_4) : new GT_RenderedTexture(LargeTurbineTextureHandler.frontFace_4 );
		}
		else {
			if (usingAnimations()) {
				if (isControllerActive()) {
					return getController().frontFaceActive;
				}
			}
			return getController().frontFace;
		}		
	}	

	@Override
	public long getMinimumStoredEU() {
		return 0;
	}

	@Override
	public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int aSide) {
		return new int[] {};
	}

	@Override
	public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
		return false;
	}

	@Override
	public boolean onWrenchRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY,
			float aZ) {
		// TODO Auto-generated method stub
		return super.onWrenchRightClick(aSide, aWrenchingSide, aPlayer, aX, aY, aZ);
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, byte aSide, float aX,
			float aY, float aZ) {		
		//Do Super
		boolean aSuper = super.onRightclick(aBaseMetaTileEntity, aPlayer, aSide, aX, aY, aZ);
		// Do Things
		if (this.getBaseMetaTileEntity().isServerSide()) {
			ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
			if (tCurrentItem != null) {
				if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSoftHammerList)) {					
					if (mControllerLocation != null && mControllerLocation.length() > 0) {
						if (setController(BlockPos.generateBlockPos(mControllerLocation))) {
							if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
								String tChat = "Trying to Reset linked Controller";
								IGregTechTileEntity g = this.getBaseMetaTileEntity();
								GT_Utility.sendChatToPlayer(aPlayer, tChat);
								GT_Utility.sendSoundToPlayers(g.getWorld(), GregTech_API.sSoundList.get(101), 1.0F, -1,
										g.getXCoord(), g.getYCoord(), g.getZCoord());
							}
						}
					}
				}
			}
		}
		return aSuper;
	}

	public void setActive(boolean b) {
		this.getBaseMetaTileEntity().setActive(b);
	}
	
	
	
	
	
}