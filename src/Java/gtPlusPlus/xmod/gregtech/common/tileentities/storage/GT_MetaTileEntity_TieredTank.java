package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_RenderedTexture;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.preloader.asm.AsmConfig;

public class GT_MetaTileEntity_TieredTank extends GT_MetaTileEntity_BasicTank {

	public GT_MetaTileEntity_TieredTank(final int aID, final String aName, final String aNameRegional, final int aTier) {
		super(aID, aName, aNameRegional, aTier, 3, "Stores " + ((int) (Math.pow(2, aTier) * 32000)) + "L of fluid");
	}

	public GT_MetaTileEntity_TieredTank(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, 3, "Stores " + ((int) (Math.pow(2, aTier) * 32000)) + "L of fluid", aTextures);
	}

	@Override
	public String[] getDescription() {
		String[] aTip;

		String aTankPortableness = CORE.GTNH ? "non-portable" : "portable";

		if (this.mFluid == null) {
			aTip = new String[] {this.mDescription, "A "+aTankPortableness+" tank.", CORE.GT_Tooltip};
		}
		else {
			aTip = new String[] {this.mDescription, "A "+aTankPortableness+" tank.", "Fluid: "+mFluid.getLocalizedName()+" "+mFluid.amount+"L", CORE.GT_Tooltip};
		}		
		return aTip;
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		return new ITexture[0][0][0];
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return aSide == 1 ? new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_POTIONBREWER_ACTIVE)} : new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_POTIONBREWER)};
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return true;
	}

	@Override
	public boolean isAccessAllowed(final EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public final byte getUpdateData() {
		return 0x00;
	}

	@Override
	public boolean doesFillContainers() {
		return true;
	}

	@Override
	public boolean doesEmptyContainers() {
		return true;
	}

	@Override
	public boolean canTankBeFilled() {
		return true;
	}

	@Override
	public boolean canTankBeEmptied() {
		return true;
	}

	@Override
	public String[] getInfoData() {

		if (this.mFluid == null) {
			return new String[]{
					GT_Values.VOLTAGE_NAMES[this.mTier]+" Fluid Tank",
					"Stored Fluid:",
					"No Fluid",
					Integer.toString(0) + "L",
					Integer.toString(this.getCapacity()) + "L"};
		}
		return new String[]{
				GT_Values.VOLTAGE_NAMES[this.mTier]+" Fluid Tank",
				"Stored Fluid:",
				this.mFluid.getLocalizedName(),
				Integer.toString(this.mFluid.amount) + "L",
				Integer.toString(this.getCapacity()) + "L"};
	}

	@Override
	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_TieredTank(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	@Override
	public int getCapacity() {
		return (int) (Math.pow(2, this.mTier) * 32000);
	}

	@Override
	public int getTankPressure() {
		return 100;
	}

	@Override 	
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) { 	
		if (aBaseMetaTileEntity.isClientSide()){ 	
			//setVars(); 	
			return true; 	
		} 	
		aBaseMetaTileEntity.openGUI(aPlayer); 	
		return true; 	
	}

	@Override
	public boolean displaysItemStack() { 	
		return true; 	
	} 	

	@Override 	
	public boolean displaysStackSize() { 	
		return false; 	
	}

	@Override
	public void setItemNBT(NBTTagCompound aNBT) {
		if (CORE.NBT_PERSISTENCY_PATCH_APPLIED) {		
			if (mFluid != null){
				Logger.INFO("Setting item fluid nbt");
				aNBT.setTag("mFluid", mFluid.writeToNBT(new NBTTagCompound()));
				if (aNBT.hasKey("mFluid")) {
					Logger.INFO("Set mFluid to NBT.");        		
				}
			}     
		}
	}

}