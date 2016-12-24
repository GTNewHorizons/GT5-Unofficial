package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_TieredTank
extends GT_MetaTileEntity_BasicTank {

	private NBTTagCompound mRecipeStuff;
	private String mFluidName;
	private int mFluidAmount;
	private FluidStack mInternalTank;

	public GT_MetaTileEntity_TieredTank(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 3, "Stores " + ((int) (Math.pow(2, aTier) * 32000)) + "L of fluid");
	}

	public GT_MetaTileEntity_TieredTank(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
	}

	@Override
	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		return new ITexture[0][0][0];
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		return aSide == 1 ? new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_POTIONBREWER_ACTIVE)} : new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_POTIONBREWER)};
	}

	
	private boolean setVars(){
		if (mRecipeStuff == null){
			mRecipeStuff = new NBTTagCompound();
		}
		Utils.LOG_INFO("setting Vars.");
		if (mFluidName.equals("") || !mFluidName.equals(null)){
			if (mFluid != null)	mFluidName = mFluid.getFluid().getName();
		}
		else{
			if (mFluid != null){
				mInternalTank = mFluid;
				if (!mFluidName.equalsIgnoreCase(mFluid.getFluid().getName())){
					mFluidName = mFluid.getFluid().getName();
				}
			}
			else {
				// Leave Values Blank.	
				return false;
			}
		}

		if (mFluidAmount <= 0){
			if (mFluid != null)	mFluidAmount = mFluid.amount;
		}
		else {
			if (mFluid != null){
				if (mFluidAmount != mFluid.amount){
					mFluidAmount = mFluid.amount;
				}
			}
			else {
				// Leave Values Blank.	
				return false;
			}
		}
		return true;
	}
	

	@Override
	public String[] getDescription() {
		//setVars();
		if ((mFluidName.equals("Empty")||mFluidName.equals("")) || mFluidAmount <= 0){
			return new String[] {mDescription, CORE.GT_Tooltip};
		}
		return new String[] {mDescription, "Stored Fluid: "+mFluidName, "Stored Amount: "+mFluidAmount+"l", CORE.GT_Tooltip};
	}


	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		setVars();
		mRecipeStuff.setString("mFluidName", mFluidName);		
		mRecipeStuff.setInteger("mFluidAmount", mFluidAmount);
		aNBT.setTag("GT.CraftingComponents", mRecipeStuff);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);  
		if (mRecipeStuff == null){
			mRecipeStuff = new NBTTagCompound();
		}
		else {
		mRecipeStuff = aNBT.getCompoundTag("GT.CraftingComponents");
		mFluidName = mRecipeStuff.getString("mFluidName");
		mFluidAmount = mRecipeStuff.getInteger("mFluidAmount");
		mFluid = FluidUtils.getFluidStack(mFluidName, mFluidAmount);
		}
		setItemNBT(aNBT);
	}

	@Override
	public void setItemNBT(NBTTagCompound aNBT) {
		super.setItemNBT(aNBT);
		if (mRecipeStuff == null){
			mRecipeStuff = new NBTTagCompound();
		}
		else {
		mRecipeStuff.setString("mFluidName", mFluidName);		
		mRecipeStuff.setInteger("mFluidAmount", mFluidAmount);
		}
		aNBT.setTag("GT.CraftingComponents", mRecipeStuff);
	}


	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()){
			//setVars();
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		setVars();
		return true;
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return true;
	}

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {
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
	public boolean displaysItemStack() {
		return true;
	}

	@Override
	public boolean displaysStackSize() {
		return false;
	}

	@Override
	public String[] getInfoData() {

		if (mFluid == null) {
			return new String[]{
					GT_Values.VOLTAGE_NAMES[mTier]+" Fluid Tank",
					"Stored Fluid:",
					"No Fluid",
					Integer.toString(0) + "L",
					Integer.toString(getCapacity()) + "L"};
		}
		return new String[]{
				GT_Values.VOLTAGE_NAMES[mTier]+" Fluid Tank",
				"Stored Fluid:",
				mFluid.getLocalizedName(),
				Integer.toString(mFluid.amount) + "L",
				Integer.toString(getCapacity()) + "L"};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_TieredTank(mName, mTier, mDescription, mTextures);
	}

	@Override
	public int getCapacity() {
		return (int) (Math.pow(2, mTier) * 32000);
	}

	@Override
	public int getTankPressure() {
		return 100;
	}

	@Override
	public void onMachineBlockUpdate() {
		this.getBaseMetaTileEntity().markDirty();
		super.onMachineBlockUpdate();
	}

	@Override
	public void onRemoval() {
		this.getBaseMetaTileEntity().markDirty();
		super.onRemoval();
	}

}